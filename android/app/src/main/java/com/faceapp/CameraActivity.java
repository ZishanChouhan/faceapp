package com.faceapp;

import static androidx.viewpager2.widget.ViewPager2.SCROLL_STATE_DRAGGING;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.AspectRatio;
import androidx.camera.core.Camera;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageProxy;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LifecycleOwner;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.faceapp.databinding.CameraLayoutBinding;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.mediapipe.tasks.vision.core.RunningMode;

import java.util.Locale;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import kotlin.UninitializedPropertyAccessException;

public class CameraActivity extends AppCompatActivity implements FaceLandmarkerHelper.LandmarkerListener {
    CameraLayoutBinding cameraLayoutBinding;
    ImageCapture imageCapture;

//    private var videoCapture: VideoCapture<Recorder>? = null
//    private var recording: Recording? = null
    MainViewModel viewModel;

    ExecutorService cameraExecutor;
    FaceLandmarkerHelper faceLandmarkerHelper;
    FaceBlendshapesResultAdapter faceBlendshapesResultAdapter;

    int cameraFacing = CameraSelector.LENS_FACING_FRONT;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d("FaceappSee", "OnCreate");
        cameraLayoutBinding = CameraLayoutBinding.inflate(getLayoutInflater());
        Log.d("FaceappSee", cameraLayoutBinding.toString());
        setContentView(cameraLayoutBinding.getRoot());

        viewModel = new MainViewModel();

        Log.d("FaceappSee", viewModel.toString());
        // Request camera permissions
//        if (allPermissionsGranted()) {

        RecyclerView recyclerView = cameraLayoutBinding.recyclerviewResults;

        recyclerView.setLayoutManager( new LinearLayoutManager(this));
        recyclerView.setAdapter(faceBlendshapesResultAdapter);
        Log.d("FaceappSee", recyclerView.toString());
        // Initialize our background executor
        cameraExecutor = Executors.newSingleThreadExecutor();

        // Wait for the views to be properly laid out
        cameraLayoutBinding.viewFinder.post(new Runnable() {
            @Override
            public void run() {
                try {

                    startCamera();
                } catch (ExecutionException e) {
                    throw new RuntimeException(e);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });
            // Set up the camera and its use cases

        // Create the FaceLandmarkerHelper that will handle the inference
//        cameraExecutor.execute(new Runnable() {
//            @Override
//            public void run() {
        try{
                faceLandmarkerHelper = new FaceLandmarkerHelper(
                        viewModel.getCurrentMinFaceDetectionConfidence(),
                        viewModel.getCurrentMinFaceTrackingConfidence(),
                        viewModel.getCurrentMinFacePresenceConfidence(),
                        viewModel.getCurrentMaxFaces(),
                        viewModel.getCurrentDelegate(),
                        RunningMode.LIVE_STREAM,
                        (Context) this,
                        (FaceLandmarkerHelper.LandmarkerListener) this
                );

                Log.d("FaceappSee", faceLandmarkerHelper.toString());
        }catch (Exception e){
            e.printStackTrace();
        }
//            }
//        });



        // Attach listeners to UI control widgets
        initBottomSheetControls();

//        } else {
//            requestPermissions()
//        }

        // Set up the listeners for take photo and video capture buttons
//        cameraLayoutBinding.imageCaptureButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                takePhoto();
//            }
//        });
//        cameraLayoutBinding.imageCaptureButton.setOnClickListener { takePhoto() }
//        cameraLayoutBinding.videoCaptureButton.setOnClickListener { captureVideo() }

//        startCamera();


    }

    @Override
    public void onResume() {
        super.onResume();

        Log.d("FaceappSee", "onResume: ");
        cameraExecutor.execute(new Runnable() {
            @Override
            public void run() {
//                if (faceLandmarkerHelper.isClose()) {
                    faceLandmarkerHelper.setupFaceLandmarker();
//                }
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        if(faceLandmarkerHelper != null) {
            viewModel.setMaxFaces(faceLandmarkerHelper.maxNumFaces);
            viewModel.setMinFaceDetectionConfidence(faceLandmarkerHelper.minFaceDetectionConfidence);
            viewModel.setMinFaceTrackingConfidence(faceLandmarkerHelper.minFaceTrackingConfidence);
            viewModel.setMinFacePresenceConfidence(faceLandmarkerHelper.minFacePresenceConfidence);
            viewModel.setDelegate(faceLandmarkerHelper.currentDelegate);

            // Close the FaceLandmarkerHelper and release resources
            cameraExecutor.execute(new Runnable() {
                @Override
                public void run() {
                    faceLandmarkerHelper.clearFaceLandmarker();
                }
            });
        }
    }

    void initBottomSheetControls() {
        // init bottom sheet settings
        cameraLayoutBinding.bottomSheetLayout.maxFacesValue.setText(
                Integer.toString(viewModel.getCurrentMaxFaces())
        );
        cameraLayoutBinding.bottomSheetLayout.detectionThresholdValue.setText(
                String.format(
                        Locale.US, "%.2f", viewModel.getCurrentMinFaceDetectionConfidence()
                ));
        cameraLayoutBinding.bottomSheetLayout.trackingThresholdValue.setText(
                String.format(
                        Locale.US, "%.2f", viewModel.getCurrentMinFaceTrackingConfidence()
                ));
        cameraLayoutBinding.bottomSheetLayout.presenceThresholdValue.setText(
                String.format(
                        Locale.US, "%.2f", viewModel.getCurrentMinFacePresenceConfidence()
                ));

        // When clicked, lower face detection score threshold floor
        cameraLayoutBinding.bottomSheetLayout.detectionThresholdMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (faceLandmarkerHelper.minFaceDetectionConfidence >= 0.2) {
                    faceLandmarkerHelper.minFaceDetectionConfidence -= 0.1f;
                    updateControlsUi();
                }
            }
        });

        // When clicked, raise face detection score threshold floor
        cameraLayoutBinding.bottomSheetLayout.detectionThresholdPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (faceLandmarkerHelper.minFaceDetectionConfidence <= 0.8) {
                    faceLandmarkerHelper.minFaceDetectionConfidence += 0.1f;
                    updateControlsUi();
                }
            }
        });

        // When clicked, lower face tracking score threshold floor
        cameraLayoutBinding.bottomSheetLayout.trackingThresholdMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (faceLandmarkerHelper.minFaceTrackingConfidence >= 0.2) {
                    faceLandmarkerHelper.minFaceTrackingConfidence -= 0.1f;
                    updateControlsUi();
                }
            }
        });

        // When clicked, raise face tracking score threshold floor
        cameraLayoutBinding.bottomSheetLayout.trackingThresholdPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (faceLandmarkerHelper.minFaceTrackingConfidence <= 0.8) {
                    faceLandmarkerHelper.minFaceTrackingConfidence += 0.1f;
                    updateControlsUi();
                }
            }
        });


        // When clicked, lower face presence score threshold floor
        cameraLayoutBinding.bottomSheetLayout.presenceThresholdMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (faceLandmarkerHelper.minFacePresenceConfidence >= 0.2) {
                    faceLandmarkerHelper.minFacePresenceConfidence -= 0.1f;
                    updateControlsUi();
                }
            }
        });

        // When clicked, raise face presence score threshold floor
        cameraLayoutBinding.bottomSheetLayout.presenceThresholdPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (faceLandmarkerHelper.minFacePresenceConfidence <= 0.8) {
                    faceLandmarkerHelper.minFacePresenceConfidence += 0.1f;
                    updateControlsUi();
                }
            }
        });

        // When clicked, reduce the number of faces that can be detected at a
        // time
        cameraLayoutBinding.bottomSheetLayout.maxFacesMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (faceLandmarkerHelper.maxNumFaces > 1) {
                    faceLandmarkerHelper.maxNumFaces--;
                    updateControlsUi();
                }
            }
        });

        // When clicked, increase the number of faces that can be detected
        // at a time
        cameraLayoutBinding.bottomSheetLayout.maxFacesPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (faceLandmarkerHelper.maxNumFaces < 2) {
                    faceLandmarkerHelper.maxNumFaces++;
                    updateControlsUi();
                }
            }
        });

        // When clicked, change the underlying hardware used for inference.
        // Current options are CPU and GPU
        cameraLayoutBinding.bottomSheetLayout.spinnerDelegate.setSelection(
                viewModel.getCurrentDelegate(), false
        );
        cameraLayoutBinding.bottomSheetLayout.spinnerDelegate.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                try {
                    faceLandmarkerHelper.currentDelegate = position;
                    updateControlsUi();
                } catch(UninitializedPropertyAccessException e) {
                    Log.e("faceapp", "FaceLandmarkerHelper has not been initialized yet.");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    void updateControlsUi() {
        cameraLayoutBinding.bottomSheetLayout.maxFacesValue.setText(
                Integer.toString(faceLandmarkerHelper.maxNumFaces));
        cameraLayoutBinding.bottomSheetLayout.detectionThresholdValue.setText(
                String.format(
                        Locale.US,
                        "%.2f",
                        faceLandmarkerHelper.minFaceDetectionConfidence
                ));
        cameraLayoutBinding.bottomSheetLayout.trackingThresholdValue.setText(
                String.format(
                        Locale.US,
                        "%.2f",
                        faceLandmarkerHelper.minFaceTrackingConfidence
                ));
        cameraLayoutBinding.bottomSheetLayout.presenceThresholdValue.setText(
                String.format(
                        Locale.US,
                        "%.2f",
                        faceLandmarkerHelper.minFacePresenceConfidence
                ));

        // Needs to be cleared instead of reinitialized because the GPU
        // delegate needs to be initialized on the thread using it when applicable
        cameraExecutor.execute(new Runnable() {
            @Override
            public void run() {
                faceLandmarkerHelper.clearFaceLandmarker();
//                faceLandmarkerHelper.setupFaceLandmarker();
            }
        });
        cameraLayoutBinding.overlay.clear();
    }

    private void takePhoto() {

    }

//    private fun captureVideo() {}

    private void startCamera() throws ExecutionException, InterruptedException {
//        ProcessCameraProvider cameraProviderFuture = ProcessCameraProvider.getInstance(this).get();
        ListenableFuture<ProcessCameraProvider> cameraProviderFuture = ProcessCameraProvider.getInstance(this);
        cameraProviderFuture.addListener(new Runnable() {
            @Override
            public void run() {
                // Used to bind the lifecycle of cameras to the lifecycle owner
                try {
                    ProcessCameraProvider  cameraProvider = cameraProviderFuture.get();
                    Log.d("FaceappSee", cameraProvider.toString());
                    bindPreview(cameraProvider);


                } catch (ExecutionException e) {
                    throw new RuntimeException(e);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }


            // Unbind use cases before rebinding
//            cameraProvider.unbindAll()

            // Bind use cases to camera
//            cameraProvider.bindToLifecycle(
//                    this, cameraSelector, preview)

//        } catch(exc: Exception) {
//            Log.e(TAG, "Use case binding failed", exc)
//        }

            };
        }, ContextCompat.getMainExecutor(this));
    }

    public void bindPreview(@NonNull ProcessCameraProvider cameraProvider){
        Preview preview = new Preview.Builder()
                .setTargetAspectRatio(AspectRatio.RATIO_4_3)
                .setTargetRotation(cameraLayoutBinding.viewFinder.getDisplay().getRotation())
                .build();

//                                .also {
//                            it.setSurfaceProvider(cameraLayoutBinding.viewFinder.surfaceProvider)
//                        }
        CameraSelector cameraSelector = CameraSelector.DEFAULT_FRONT_CAMERA;

//                        CameraSelector cameraSelector = new CameraSelector.Builder()
//                                .requireLensFacing(CameraSelector.LENS_FACING_FRONT)
//                                .build();

        ImageAnalysis imageAnalysis = new ImageAnalysis.Builder()
                .setTargetAspectRatio(AspectRatio.RATIO_4_3)
                .setTargetRotation(cameraLayoutBinding.viewFinder.getDisplay().getRotation())
                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                .setOutputImageFormat(ImageAnalysis.OUTPUT_IMAGE_FORMAT_RGBA_8888)
                .build();

        Log.d("FaceappSee", imageAnalysis.toString());
        imageAnalysis.setAnalyzer(cameraExecutor, new ImageAnalysis.Analyzer() {
            @Override
            public void analyze(@NonNull ImageProxy image) {
                detectFace(image);
            }
        });

        cameraProvider.unbindAll();

//        ImageCapture.Builder builder = new ImageCapture.Builder();

//                        HdrImageCaptureExtender hdrImageCaptureExtender = HdrImageCaptureExtender.create(builder);

//                        if (hdrImageCaptureExtender.isExtensionAvailable(cameraSelector)) {
        // Enable the extension if available.
//                            hdrImageCaptureExtender.enableExtension(cameraSelector);
//                        }

//        imageCapture = builder
//                .setTargetRotation(getWindowManager().getDefaultDisplay().getRotation())
//                .build();

        preview.setSurfaceProvider(cameraLayoutBinding.viewFinder.getSurfaceProvider());
        Camera camera = cameraProvider
                .bindToLifecycle((LifecycleOwner)this, cameraSelector, preview, imageAnalysis);
    }

//    private fun requestPermissions() {}

//    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
//        ContextCompat.checkSelfPermission(
//                baseContext, it) == PackageManager.PERMISSION_GRANTED
//    }

    private void detectFace(ImageProxy imageProxy) {

        faceLandmarkerHelper.detectLiveStream(imageProxy, cameraFacing == CameraSelector.LENS_FACING_FRONT ? true : false
        );
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        cameraExecutor.shutdown();

        cameraLayoutBinding = null;
        // Shut down our background executor
        try {
            cameraExecutor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onError(@NonNull String error, int errorCode) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getApplicationContext(), error, Toast.LENGTH_SHORT).show();
                faceBlendshapesResultAdapter.updateResults(null);
                faceBlendshapesResultAdapter.notifyDataSetChanged();

                if (errorCode == FaceLandmarkerHelper.GPU_ERROR) {
                    cameraLayoutBinding.bottomSheetLayout.spinnerDelegate.setSelection(
                            FaceLandmarkerHelper.DELEGATE_CPU, false
                    );
                }
            }
        });
    };

    @Override
    public void onResults(@NonNull FaceLandmarkerHelper.ResultBundle resultBundle) {
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (cameraLayoutBinding != null) {
                    if (cameraLayoutBinding.recyclerviewResults.getScrollState() != SCROLL_STATE_DRAGGING) {
                        faceBlendshapesResultAdapter.updateResults(resultBundle.getResult());
                        faceBlendshapesResultAdapter.notifyDataSetChanged();
                    }

                    cameraLayoutBinding.bottomSheetLayout.inferenceTimeVal.setText(
                            String.format("%d ms", resultBundle.getInferenceTime())
                    );

                    // Pass necessary information to OverlayView for drawing on the canvas
                    cameraLayoutBinding.overlay.setResults(
                            resultBundle.getResult(),
                            resultBundle.getInputImageHeight(),
                            resultBundle.getInputImageWidth(),
                            RunningMode.LIVE_STREAM
                    );
                    cameraLayoutBinding.overlay.invalidate();
                }
            }
        });
    };

    @Override
    public void onEmpty() {
        cameraLayoutBinding.overlay.clear();
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                faceBlendshapesResultAdapter.updateResults(null);
                faceBlendshapesResultAdapter.notifyDataSetChanged();
            }
        });

        }
    }

//    companion object {
//        private const val TAG = "CameraXApp"
//        private const val FILENAME_FORMAT = "yyyy-MM-dd-HH-mm-ss-SSS"
//        private val REQUIRED_PERMISSIONS =
//                mutableListOf (
//                        Manifest.permission.CAMERA,
//                        Manifest.permission.RECORD_AUDIO
//                ).apply {
//            if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
//                add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
//            }
//        }.toTypedArray()
//    }
//}
//}
