package com.faceapp;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.Camera;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LifecycleOwner;

import com.faceapp.databinding.CameraLayoutBinding;
import com.google.common.util.concurrent.ListenableFuture;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CameraActivity extends AppCompatActivity {
    CameraLayoutBinding viewBinding;
    ImageCapture imageCapture;

//    private var videoCapture: VideoCapture<Recorder>? = null
//    private var recording: Recording? = null

    ExecutorService cameraExecutor;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewBinding = CameraLayoutBinding.inflate(getLayoutInflater());
        setContentView(viewBinding.getRoot());

        // Request camera permissions
//        if (allPermissionsGranted()) {
        try {
            startCamera();
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
//        } else {
//            requestPermissions()
//        }

        // Set up the listeners for take photo and video capture buttons
        viewBinding.imageCaptureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePhoto();
            }
        });
//        viewBinding.imageCaptureButton.setOnClickListener { takePhoto() }
//        viewBinding.videoCaptureButton.setOnClickListener { captureVideo() }

//        startCamera();
        cameraExecutor = Executors.newSingleThreadExecutor();

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
        Preview preview = new Preview.Builder().build();

//                                .also {
//                            it.setSurfaceProvider(viewBinding.viewFinder.surfaceProvider)
//                        }
        CameraSelector cameraSelector = CameraSelector.DEFAULT_FRONT_CAMERA;

//                        CameraSelector cameraSelector = new CameraSelector.Builder()
//                                .requireLensFacing(CameraSelector.LENS_FACING_FRONT)
//                                .build();

        ImageAnalysis imageAnalysis = new ImageAnalysis.Builder()
                .build();

        ImageCapture.Builder builder = new ImageCapture.Builder();

//                        HdrImageCaptureExtender hdrImageCaptureExtender = HdrImageCaptureExtender.create(builder);

//                        if (hdrImageCaptureExtender.isExtensionAvailable(cameraSelector)) {
        // Enable the extension if available.
//                            hdrImageCaptureExtender.enableExtension(cameraSelector);
//                        }

        imageCapture = builder
                .setTargetRotation(getWindowManager().getDefaultDisplay().getRotation())
                .build();

        preview.setSurfaceProvider(viewBinding.viewFinder.getSurfaceProvider());
        Camera camera = cameraProvider
                .bindToLifecycle((LifecycleOwner)this, cameraSelector, preview, imageAnalysis, imageCapture);


    }

//    private fun requestPermissions() {}

//    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
//        ContextCompat.checkSelfPermission(
//                baseContext, it) == PackageManager.PERMISSION_GRANTED
//    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        cameraExecutor.shutdown();
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
}
