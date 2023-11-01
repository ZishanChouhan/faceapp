//package com.faceapp;
//
//import android.annotation.SuppressLint;
//import android.content.Context;
//import android.content.res.Configuration;
//import android.os.Bundle;
//import android.util.Log;
//import android.view.Display;
//import android.view.View;
//import android.widget.AdapterView;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.appcompat.widget.AppCompatSpinner;
//import androidx.camera.core.Camera;
//import androidx.camera.core.CameraSelector;
//import androidx.camera.core.ImageAnalysis;
//import androidx.camera.core.ImageProxy;
//import androidx.camera.core.Preview;
//import androidx.camera.core.UseCase;
//import androidx.camera.lifecycle.ProcessCameraProvider;
//import androidx.camera.view.PreviewView;
//import androidx.core.content.ContextCompat;
//import androidx.fragment.app.FragmentActivity;
////import androidx.fragment.app.FragmentViewModelLazyKt;
//import androidx.lifecycle.LifecycleOwner;
////import androidx.navigation.Navigation;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.faceapp.databinding.CameraLayoutBinding;
//import com.google.common.util.concurrent.ListenableFuture;
////import com.google.mediapipe.examples.facelandmarker.FaceLandmarkerHelper;
////import com.google.mediapipe.examples.facelandmarker.MainViewModel;
////import com.google.mediapipe.examples.facelandmarker.databinding.FragmentCameraBinding;
//import com.google.mediapipe.tasks.vision.core.RunningMode;
//import com.google.mediapipe.tasks.vision.facelandmarker.FaceLandmarkerResult;
//import java.util.Arrays;
//import java.util.Locale;
//import java.util.concurrent.Executor;
//import java.util.concurrent.ExecutorService;
//import java.util.concurrent.Executors;
//import java.util.concurrent.TimeUnit;
//import kotlin.Lazy;
//import kotlin.LazyKt;
//import kotlin.UninitializedPropertyAccessException;
//import kotlin.Unit;
//import kotlin.jvm.functions.Function0;
//import kotlin.jvm.internal.Intrinsics;
//import kotlin.jvm.internal.Reflection;
////import kotlin.jvm.internal.SourceDebugExtension;
////import kotlin.jvm.internal.StringCompanionObject;
//import org.jetbrains.annotations.NotNull;
//import org.jetbrains.annotations.Nullable;
//
//public class CameraActivity2 extends AppCompatActivity implements FaceLandmarkerHelper.LandmarkerListener {
//    private CameraLayoutBinding cameraLayoutBinding;
//    private FaceLandmarkerHelper faceLandmarkerHelper;
//    private Lazy viewModel$delegate;
//    private Lazy faceBlendshapesResultAdapter$delegate;
//    private Preview preview;
//    private ImageAnalysis imageAnalyzer;
//    private Camera camera;
//    private ProcessCameraProvider cameraProvider;
//    private int cameraFacing;
//    private ExecutorService backgroundExecutor;
//    private static final String TAG = "Face Landmarker";
////    @NotNull
////    public static final Companion Companion = new Companion((DefaultConstructorMarker)null);
//
//    private CameraLayoutBinding getFragmentCameraBinding() {
//        CameraLayoutBinding var10000 = this.cameraLayoutBinding;
//        Intrinsics.checkNotNull(var10000);
//        return var10000;
//    }
//
//    private MainViewModel getViewModel() {
//        Lazy var1 = this.viewModel$delegate;
//        Object var3 = null;
//        return (MainViewModel)var1.getValue();
//    }
//
//    private final FaceBlendshapesResultAdapter getFaceBlendshapesResultAdapter() {
//        Lazy var1 = this.faceBlendshapesResultAdapter$delegate;
//        Object var3 = null;
//        return (FaceBlendshapesResultAdapter)var1.getValue();
//    }
//
//    @Override
//    public void onResume() {
//        super.onResume();
////        PermissionsFragment.Companion var10000 = PermissionsFragment.Companion;
////        Context var10001 = this.requireContext();
////        Intrinsics.checkNotNullExpressionValue(var10001, "requireContext()");
////        if (!var10000.hasPermissions(var10001)) {
////            FragmentActivity var1 = this.requireActivity();
////            Intrinsics.checkNotNullExpressionValue(var1, "requireActivity()");
////            Navigation.findNavController((Activity)var1, 2131296465).navigate(2131296314);
////        }
//
//        ExecutorService var2 = this.backgroundExecutor;
//        if (var2 == null) {
//            Intrinsics.throwUninitializedPropertyAccessException("backgroundExecutor");
//        }
//
//        var2.execute((Runnable)(new Runnable() {
//            public final void run() {
//                if (CameraActivity2.access$getFaceLandmarkerHelper$p(CameraActivity2.this).isClose()) {
//                    CameraActivity2.access$getFaceLandmarkerHelper$p(CameraActivity2.this).setupFaceLandmarker();
//                }
//
//            }
//        }));
//    }
//    @Override
//    public void onPause() {
//        super.onPause();
//        if (((CameraActivity2)this).faceLandmarkerHelper != null) {
//            MainViewModel var10000 = this.getViewModel();
//            FaceLandmarkerHelper var10001 = this.faceLandmarkerHelper;
//            if (var10001 == null) {
//                Intrinsics.throwUninitializedPropertyAccessException("faceLandmarkerHelper");
//            }
//
//            var10000.setMaxFaces(var10001.getMaxNumFaces());
//            var10000 = this.getViewModel();
//            var10001 = this.faceLandmarkerHelper;
//            if (var10001 == null) {
//                Intrinsics.throwUninitializedPropertyAccessException("faceLandmarkerHelper");
//            }
//
//            var10000.setMinFaceDetectionConfidence(var10001.getMinFaceDetectionConfidence());
//            var10000 = this.getViewModel();
//            var10001 = this.faceLandmarkerHelper;
//            if (var10001 == null) {
//                Intrinsics.throwUninitializedPropertyAccessException("faceLandmarkerHelper");
//            }
//
//            var10000.setMinFaceTrackingConfidence(var10001.getMinFaceTrackingConfidence());
//            var10000 = this.getViewModel();
//            var10001 = this.faceLandmarkerHelper;
//            if (var10001 == null) {
//                Intrinsics.throwUninitializedPropertyAccessException("faceLandmarkerHelper");
//            }
//
//            var10000.setMinFacePresenceConfidence(var10001.getMinFacePresenceConfidence());
//            var10000 = this.getViewModel();
//            var10001 = this.faceLandmarkerHelper;
//            if (var10001 == null) {
//                Intrinsics.throwUninitializedPropertyAccessException("faceLandmarkerHelper");
//            }
//
//            var10000.setDelegate(var10001.getCurrentDelegate());
//            ExecutorService var1 = this.backgroundExecutor;
//            if (var1 == null) {
//                Intrinsics.throwUninitializedPropertyAccessException("backgroundExecutor");
//            }
//
//            var1.execute((Runnable)(new Runnable() {
//                public final void run() {
//                    CameraActivity2.access$getFaceLandmarkerHelper$p(CameraActivity2.this).clearFaceLandmarker();
//                }
//            }));
//        }
//
//    }
//
//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//        this.cameraLayoutBinding = null;
//
//        ExecutorService var10000 = this.backgroundExecutor;
//        if (var10000 == null) {
//            Intrinsics.throwUninitializedPropertyAccessException("backgroundExecutor");
//        }
//
//        var10000.shutdown();
//        var10000 = this.backgroundExecutor;
//        if (var10000 == null) {
//            Intrinsics.throwUninitializedPropertyAccessException("backgroundExecutor");
//        }
//
//        try {
//            var10000.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
//        } catch (InterruptedException e) {
//            throw new RuntimeException(e);
//        }
////        this._$_clearFindViewByIdCache();
//    }
//
////    @NotNull
////    public View onCreateView(@NotNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
////        Intrinsics.checkNotNullParameter(inflater, "inflater");
//////        this.cameraLayoutBinding = CameraActivity.inflate(inflater, container, false);
////        this.cameraLayoutBinding = CameraLayoutBinding.inflate(getLayoutInflater());
////        CoordinatorLayout var10000 = this.getFragmentCameraBinding().getRoot();
////        Intrinsics.checkNotNullExpressionValue(var10000, "fragmentCameraBinding.root");
////        return (View)var10000;
////    }
//    @Override
//    public void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
////        Intrinsics.checkNotNullParameter(view, "view");
//
//        RecyclerView var3 = this.getFragmentCameraBinding().recyclerviewResults;
////        int var5 = false;
//        var3.setLayoutManager((RecyclerView.LayoutManager)(new LinearLayoutManager(this)));
//        var3.setAdapter((RecyclerView.Adapter)this.getFaceBlendshapesResultAdapter());
//        ExecutorService var10001 = Executors.newSingleThreadExecutor();
//        Intrinsics.checkNotNullExpressionValue(var10001, "Executors.newSingleThreadExecutor()");
//        this.backgroundExecutor = var10001;
//        this.getFragmentCameraBinding().viewFinder.post((Runnable)(new Runnable() {
//            public final void run() {
//                CameraActivity2.this.setUpCamera();
//            }
//        }));
//        ExecutorService var10000 = this.backgroundExecutor;
//        if (var10000 == null) {
//            Intrinsics.throwUninitializedPropertyAccessException("backgroundExecutor");
//        }
//
//        var10000.execute((Runnable)(new Runnable() {
//            public final void run() {
//                CameraActivity2 var10000 = CameraActivity2.this;
//                Context var10003 = CameraActivity2.this;
//                Intrinsics.checkNotNullExpressionValue(var10003, "requireContext()");
//                RunningMode var10004 = RunningMode.LIVE_STREAM;
//                float var10005 = CameraActivity2.this.getViewModel().getCurrentMinFaceDetectionConfidence();
//                float var10006 = CameraActivity2.this.getViewModel().getCurrentMinFaceTrackingConfidence();
//                float var10007 = CameraActivity2.this.getViewModel().getCurrentMinFacePresenceConfidence();
//                int var10008 = CameraActivity2.this.getViewModel().getCurrentMaxFaces();
//                int var10009 = CameraActivity2.this.getViewModel().getCurrentDelegate();
//                FaceLandmarkerHelper.LandmarkerListener var1 = (FaceLandmarkerHelper.LandmarkerListener) CameraActivity2.this;
//                int var2 = var10009;
//                int var3 = var10008;
//                float var4 = var10007;
//                float var5 = var10006;
//                float var6 = var10005;
//                RunningMode var7 = var10004;
//                Context var8 = var10003;
//                var10000.faceLandmarkerHelper = new FaceLandmarkerHelper(var6, var5, var4, var3, var2, var7, var8, var1);
//            }
//        }));
//        this.initBottomSheetControls();
//    }
//
//    private final void initBottomSheetControls() {
//        TextView var10000 = this.getFragmentCameraBinding().bottomSheetLayout.maxFacesValue;
//        Intrinsics.checkNotNullExpressionValue(var10000, "fragmentCameraBinding.bo…SheetLayout.maxFacesValue");
//        var10000.setText((CharSequence)String.valueOf(this.getViewModel().getCurrentMaxFaces()));
//        var10000 = this.getFragmentCameraBinding().bottomSheetLayout.detectionThresholdValue;
//        Intrinsics.checkNotNullExpressionValue(var10000, "fragmentCameraBinding.bo…t.detectionThresholdValue");
////        StringCompanionObject var1 = StringCompanionObject.INSTANCE;
//        Locale var2 = Locale.US;
//        String var3 = "%.2f";
//        Object[] var4 = new Object[]{this.getViewModel().getCurrentMinFaceDetectionConfidence()};
//        String var10001 = String.format(var2, var3, Arrays.copyOf(var4, var4.length));
//        Intrinsics.checkNotNullExpressionValue(var10001, "format(locale, format, *args)");
//        var10000.setText((CharSequence)var10001);
//        var10000 = this.getFragmentCameraBinding().bottomSheetLayout.trackingThresholdValue;
//        Intrinsics.checkNotNullExpressionValue(var10000, "fragmentCameraBinding.bo…ut.trackingThresholdValue");
////        var1 = StringCompanionObject.INSTANCE;
//        var2 = Locale.US;
//        var3 = "%.2f";
//        var4 = new Object[]{this.getViewModel().getCurrentMinFaceTrackingConfidence()};
//        var10001 = String.format(var2, var3, Arrays.copyOf(var4, var4.length));
//        Intrinsics.checkNotNullExpressionValue(var10001, "format(locale, format, *args)");
//        var10000.setText((CharSequence)var10001);
//        var10000 = this.getFragmentCameraBinding().bottomSheetLayout.presenceThresholdValue;
//        Intrinsics.checkNotNullExpressionValue(var10000, "fragmentCameraBinding.bo…ut.presenceThresholdValue");
////        var1 = StringCompanionObject.INSTANCE;
//        var2 = Locale.US;
//        var3 = "%.2f";
//        var4 = new Object[]{this.getViewModel().getCurrentMinFacePresenceConfidence()};
//        var10001 = String.format(var2, var3, Arrays.copyOf(var4, var4.length));
//        Intrinsics.checkNotNullExpressionValue(var10001, "format(locale, format, *args)");
//        var10000.setText((CharSequence)var10001);
//        this.getFragmentCameraBinding().bottomSheetLayout.detectionThresholdMinus.setOnClickListener((View.OnClickListener)(new View.OnClickListener() {
//            public final void onClick(View it) {
//                if ((double) CameraActivity2.access$getFaceLandmarkerHelper$p(CameraActivity2.this).getMinFaceDetectionConfidence() >= 0.2) {
//                    FaceLandmarkerHelper var10000 = CameraActivity2.access$getFaceLandmarkerHelper$p(CameraActivity2.this);
//                    var10000.setMinFaceDetectionConfidence(var10000.getMinFaceDetectionConfidence() - 0.1F);
//                    CameraActivity2.this.updateControlsUi();
//                }
//
//            }
//        }));
//        this.getFragmentCameraBinding().bottomSheetLayout.detectionThresholdPlus.setOnClickListener((View.OnClickListener)(new View.OnClickListener() {
//            public final void onClick(View it) {
//                if ((double) CameraActivity2.access$getFaceLandmarkerHelper$p(CameraActivity2.this).getMinFaceDetectionConfidence() <= 0.8) {
//                    FaceLandmarkerHelper var10000 = CameraActivity2.access$getFaceLandmarkerHelper$p(CameraActivity2.this);
//                    var10000.setMinFaceDetectionConfidence(var10000.getMinFaceDetectionConfidence() + 0.1F);
//                    CameraActivity2.this.updateControlsUi();
//                }
//
//            }
//        }));
//        this.getFragmentCameraBinding().bottomSheetLayout.trackingThresholdMinus.setOnClickListener((View.OnClickListener)(new View.OnClickListener() {
//            public final void onClick(View it) {
//                if ((double) CameraActivity2.access$getFaceLandmarkerHelper$p(CameraActivity2.this).getMinFaceTrackingConfidence() >= 0.2) {
//                    FaceLandmarkerHelper var10000 = CameraActivity2.access$getFaceLandmarkerHelper$p(CameraActivity2.this);
//                    var10000.setMinFaceTrackingConfidence(var10000.getMinFaceTrackingConfidence() - 0.1F);
//                    CameraActivity2.this.updateControlsUi();
//                }
//
//            }
//        }));
//        this.getFragmentCameraBinding().bottomSheetLayout.trackingThresholdPlus.setOnClickListener((View.OnClickListener)(new View.OnClickListener() {
//            public final void onClick(View it) {
//                if ((double) CameraActivity2.access$getFaceLandmarkerHelper$p(CameraActivity2.this).getMinFaceTrackingConfidence() <= 0.8) {
//                    FaceLandmarkerHelper var10000 = CameraActivity2.access$getFaceLandmarkerHelper$p(CameraActivity2.this);
//                    var10000.setMinFaceTrackingConfidence(var10000.getMinFaceTrackingConfidence() + 0.1F);
//                    CameraActivity2.this.updateControlsUi();
//                }
//
//            }
//        }));
//        this.getFragmentCameraBinding().bottomSheetLayout.presenceThresholdMinus.setOnClickListener((View.OnClickListener)(new View.OnClickListener() {
//            public final void onClick(View it) {
//                if ((double) CameraActivity2.access$getFaceLandmarkerHelper$p(CameraActivity2.this).getMinFacePresenceConfidence() >= 0.2) {
//                    FaceLandmarkerHelper var10000 = CameraActivity2.access$getFaceLandmarkerHelper$p(CameraActivity2.this);
//                    var10000.setMinFacePresenceConfidence(var10000.getMinFacePresenceConfidence() - 0.1F);
//                    CameraActivity2.this.updateControlsUi();
//                }
//
//            }
//        }));
//        this.getFragmentCameraBinding().bottomSheetLayout.presenceThresholdPlus.setOnClickListener((View.OnClickListener)(new View.OnClickListener() {
//            public final void onClick(View it) {
//                if ((double) CameraActivity2.access$getFaceLandmarkerHelper$p(CameraActivity2.this).getMinFacePresenceConfidence() <= 0.8) {
//                    FaceLandmarkerHelper var10000 = CameraActivity2.access$getFaceLandmarkerHelper$p(CameraActivity2.this);
//                    var10000.setMinFacePresenceConfidence(var10000.getMinFacePresenceConfidence() + 0.1F);
//                    CameraActivity2.this.updateControlsUi();
//                }
//
//            }
//        }));
//        this.getFragmentCameraBinding().bottomSheetLayout.maxFacesMinus.setOnClickListener((View.OnClickListener)(new View.OnClickListener() {
//            public final void onClick(View it) {
//                if (CameraActivity2.access$getFaceLandmarkerHelper$p(CameraActivity2.this).getMaxNumFaces() > 1) {
//                    FaceLandmarkerHelper var10000 = CameraActivity2.access$getFaceLandmarkerHelper$p(CameraActivity2.this);
//                    var10000.setMaxNumFaces(var10000.getMaxNumFaces() + -1);
//                    CameraActivity2.this.updateControlsUi();
//                }
//
//            }
//        }));
//        this.getFragmentCameraBinding().bottomSheetLayout.maxFacesPlus.setOnClickListener((View.OnClickListener)(new View.OnClickListener() {
//            public final void onClick(View it) {
//                if (CameraActivity2.access$getFaceLandmarkerHelper$p(CameraActivity2.this).getMaxNumFaces() < 2) {
//                    FaceLandmarkerHelper var10000 = CameraActivity2.access$getFaceLandmarkerHelper$p(CameraActivity2.this);
//                    var10000.setMaxNumFaces(var10000.getMaxNumFaces() + 1);
//                    CameraActivity2.this.updateControlsUi();
//                }
//
//            }
//        }));
//        this.getFragmentCameraBinding().bottomSheetLayout.spinnerDelegate.setSelection(this.getViewModel().getCurrentDelegate(), false);
//        AppCompatSpinner var5 = this.getFragmentCameraBinding().bottomSheetLayout.spinnerDelegate;
//        Intrinsics.checkNotNullExpressionValue(var5, "fragmentCameraBinding.bo…eetLayout.spinnerDelegate");
//        var5.setOnItemSelectedListener((AdapterView.OnItemSelectedListener)(new AdapterView.OnItemSelectedListener() {
//            public void onItemSelected(@Nullable AdapterView p0, @Nullable View p1, int p2, long p3) {
//                try {
//                    CameraActivity2.access$getFaceLandmarkerHelper$p(CameraActivity2.this).setCurrentDelegate(p2);
//                    CameraActivity2.this.updateControlsUi();
//                } catch (UninitializedPropertyAccessException var7) {
//                    Log.e("Face Landmarker", "FaceLandmarkerHelper has not been initialized yet.");
//                }
//
//            }
//
//            public void onNothingSelected(@Nullable AdapterView p0) {
//            }
//        }));
//    }
//
//    private final void updateControlsUi() {
//        TextView var10000 = this.getFragmentCameraBinding().bottomSheetLayout.maxFacesValue;
//        Intrinsics.checkNotNullExpressionValue(var10000, "fragmentCameraBinding.bo…SheetLayout.maxFacesValue");
//        FaceLandmarkerHelper var10001 = this.faceLandmarkerHelper;
//        if (var10001 == null) {
//            Intrinsics.throwUninitializedPropertyAccessException("faceLandmarkerHelper");
//        }
//
//        var10000.setText((CharSequence)String.valueOf(var10001.getMaxNumFaces()));
//        var10000 = this.getFragmentCameraBinding().bottomSheetLayout.detectionThresholdValue;
//        Intrinsics.checkNotNullExpressionValue(var10000, "fragmentCameraBinding.bo…t.detectionThresholdValue");
////        StringCompanionObject var1 = StringCompanionObject.INSTANCE;
//        Locale var2 = Locale.US;
//        String var3 = "%.2f";
//        Object[] var5 = new Object[1];
//        FaceLandmarkerHelper var10004 = this.faceLandmarkerHelper;
//        if (var10004 == null) {
//            Intrinsics.throwUninitializedPropertyAccessException("faceLandmarkerHelper");
//        }
//
//        var5[0] = var10004.getMinFaceDetectionConfidence();
//        Object[] var4 = var5;
//        String var7 = String.format(var2, var3, Arrays.copyOf(var4, var4.length));
//        Intrinsics.checkNotNullExpressionValue(var7, "format(locale, format, *args)");
//        var10000.setText((CharSequence)var7);
//        var10000 = this.getFragmentCameraBinding().bottomSheetLayout.trackingThresholdValue;
//        Intrinsics.checkNotNullExpressionValue(var10000, "fragmentCameraBinding.bo…ut.trackingThresholdValue");
////        var1 = StringCompanionObject.INSTANCE;
//        var2 = Locale.US;
//        var3 = "%.2f";
//        var5 = new Object[1];
//        var10004 = this.faceLandmarkerHelper;
//        if (var10004 == null) {
//            Intrinsics.throwUninitializedPropertyAccessException("faceLandmarkerHelper");
//        }
//
//        var5[0] = var10004.getMinFaceTrackingConfidence();
//        var4 = var5;
//        var7 = String.format(var2, var3, Arrays.copyOf(var4, var4.length));
//        Intrinsics.checkNotNullExpressionValue(var7, "format(locale, format, *args)");
//        var10000.setText((CharSequence)var7);
//        var10000 = this.getFragmentCameraBinding().bottomSheetLayout.presenceThresholdValue;
//        Intrinsics.checkNotNullExpressionValue(var10000, "fragmentCameraBinding.bo…ut.presenceThresholdValue");
////        var1 = StringCompanionObject.INSTANCE;
//        var2 = Locale.US;
//        var3 = "%.2f";
//        var5 = new Object[1];
//        var10004 = this.faceLandmarkerHelper;
//        if (var10004 == null) {
//            Intrinsics.throwUninitializedPropertyAccessException("faceLandmarkerHelper");
//        }
//
//        var5[0] = var10004.getMinFacePresenceConfidence();
//        var4 = var5;
//        var7 = String.format(var2, var3, Arrays.copyOf(var4, var4.length));
//        Intrinsics.checkNotNullExpressionValue(var7, "format(locale, format, *args)");
//        var10000.setText((CharSequence)var7);
//        ExecutorService var6 = this.backgroundExecutor;
//        if (var6 == null) {
//            Intrinsics.throwUninitializedPropertyAccessException("backgroundExecutor");
//        }
//
//        var6.execute((Runnable)(new Runnable() {
//            public final void run() {
//                CameraActivity2.access$getFaceLandmarkerHelper$p(CameraActivity2.this).clearFaceLandmarker();
//                CameraActivity2.access$getFaceLandmarkerHelper$p(CameraActivity2.this).setupFaceLandmarker();
//            }
//        }));
//        this.getFragmentCameraBinding().overlay.clear();
//    }
//
//    private void setUpCamera() {
//        ListenableFuture var10000 = ProcessCameraProvider.getInstance(this);
//        Intrinsics.checkNotNullExpressionValue(var10000, "ProcessCameraProvider.ge…nstance(requireContext())");
//        final ListenableFuture cameraProviderFuture = var10000;
//        cameraProviderFuture.addListener((Runnable)(new Runnable() {
//            public void run() {
//                CameraActivity2.this.cameraProvider = (ProcessCameraProvider)cameraProviderFuture.get();
//                try {
//                    bindCameraUseCases();
//                } catch (Throwable e) {
//                    throw new RuntimeException(e);
//                }
//            }
//        }), ContextCompat.getMainExecutor(this));
//    }
//
//    @SuppressLint({"UnsafeOptInUsageError"})
//    private void bindCameraUseCases() throws Throwable {
//        ProcessCameraProvider var10000 = this.cameraProvider;
//        if (var10000 == null) {
//            throw (Throwable)(new IllegalStateException("Camera initialization failed."));
//        } else {
//            ProcessCameraProvider cameraProvider = var10000;
//            CameraSelector var9 = (new CameraSelector.Builder()).requireLensFacing(this.cameraFacing).build();
//            Intrinsics.checkNotNullExpressionValue(var9, "CameraSelector.Builder()…ing(cameraFacing).build()");
//            CameraSelector cameraSelector = var9;
//            Preview.Builder var10001 = (new Preview.Builder()).setTargetAspectRatio(0);
//            PreviewView var10002 = this.getFragmentCameraBinding().viewFinder;
//            Intrinsics.checkNotNullExpressionValue(var10002, "fragmentCameraBinding.viewFinder");
//            Display var14 = var10002.getDisplay();
//            Intrinsics.checkNotNullExpressionValue(var14, "fragmentCameraBinding.viewFinder.display");
//            this.preview = var10001.setTargetRotation(var14.getRotation()).build();
//            ImageAnalysis.Builder var11 = (new ImageAnalysis.Builder()).setTargetAspectRatio(0);
//            var10002 = this.getFragmentCameraBinding().viewFinder;
//            Intrinsics.checkNotNullExpressionValue(var10002, "fragmentCameraBinding.viewFinder");
//            var14 = var10002.getDisplay();
//            Intrinsics.checkNotNullExpressionValue(var14, "fragmentCameraBinding.viewFinder.display");
//            ImageAnalysis var3 = var11.setTargetRotation(var14.getRotation()).setBackpressureStrategy(0).setOutputImageFormat(2).build();
////            int var5 = false;
//            ExecutorService var12 = this.backgroundExecutor;
//            if (var12 == null) {
//                Intrinsics.throwUninitializedPropertyAccessException("backgroundExecutor");
//            }
//
//            var3.setAnalyzer((Executor)var12, (ImageAnalysis.Analyzer)(new CameraFragment$bindCameraUseCases$$inlined$also$lambda$1(this)));
//            Unit var7 = Unit.INSTANCE;
//            this.imageAnalyzer = var3;
//            cameraProvider.unbindAll();
//
//            try {
//                this.camera = cameraProvider.bindToLifecycle((LifecycleOwner)this, cameraSelector, new UseCase[]{this.preview, this.imageAnalyzer});
//                Preview var10 = this.preview;
//                if (var10 != null) {
//                    PreviewView var13 = this.getFragmentCameraBinding().viewFinder;
//                    Intrinsics.checkNotNullExpressionValue(var13, "fragmentCameraBinding.viewFinder");
//                    var10.setSurfaceProvider(var13.getSurfaceProvider());
//                }
//            } catch (Exception var8) {
//                Log.e("Face Landmarker", "Use case binding failed", (Throwable)var8);
//            }
//
//        }
//    }
//
//    private final void detectFace(ImageProxy imageProxy) {
//        FaceLandmarkerHelper var10000 = this.faceLandmarkerHelper;
//        if (var10000 == null) {
//            Intrinsics.throwUninitializedPropertyAccessException("faceLandmarkerHelper");
//        }
//
//        var10000.detectLiveStream(imageProxy, this.cameraFacing == 0);
//    }
//
//    public void onConfigurationChanged(@NotNull Configuration newConfig) {
//        Intrinsics.checkNotNullParameter(newConfig, "newConfig");
//        super.onConfigurationChanged(newConfig);
//        ImageAnalysis var10000 = this.imageAnalyzer;
//        if (var10000 != null) {
//            PreviewView var10001 = this.getFragmentCameraBinding().viewFinder;
//            Intrinsics.checkNotNullExpressionValue(var10001, "fragmentCameraBinding.viewFinder");
//            Display var2 = var10001.getDisplay();
//            Intrinsics.checkNotNullExpressionValue(var2, "fragmentCameraBinding.viewFinder.display");
//            var10000.setTargetRotation(var2.getRotation());
//        }
//
//    }
//
//    public void onResults(@NotNull final FaceLandmarkerHelper.ResultBundle resultBundle) {
//        Intrinsics.checkNotNullParameter(resultBundle, "resultBundle");
//        CameraActivity2 var10000 = this;
//        if (var10000 != null) {
//            var10000.runOnUiThread((Runnable)(new Runnable() {
//                public final void run() {
//                    if (CameraActivity2.this.cameraLayoutBinding != null) {
//                        RecyclerView var10000 = CameraActivity2.this.getFragmentCameraBinding().recyclerviewResults;
//                        Intrinsics.checkNotNullExpressionValue(var10000, "fragmentCameraBinding.recyclerviewResults");
//                        if (var10000.getScrollState() != 1) {
//                            CameraActivity2.this.getFaceBlendshapesResultAdapter().updateResults(resultBundle.getResult());
//                            CameraActivity2.this.getFaceBlendshapesResultAdapter().notifyDataSetChanged();
//                        }
//
//                        TextView var4 = CameraActivity2.this.getFragmentCameraBinding().bottomSheetLayout.inferenceTimeVal;
//                        Intrinsics.checkNotNullExpressionValue(var4, "fragmentCameraBinding.bo…etLayout.inferenceTimeVal");
////                        StringCompanionObject var1 = StringCompanionObject.INSTANCE;
//                        String var2 = "%d ms";
//                        Object[] var3 = new Object[]{resultBundle.getInferenceTime()};
//                        String var10001 = String.format(var2, Arrays.copyOf(var3, var3.length));
//                        Intrinsics.checkNotNullExpressionValue(var10001, "format(format, *args)");
//                        var4.setText((CharSequence)var10001);
//                        CameraActivity2.this.getFragmentCameraBinding().overlay.setResults(resultBundle.getResult(), resultBundle.getInputImageHeight(), resultBundle.getInputImageWidth(), RunningMode.LIVE_STREAM);
//                        CameraActivity2.this.getFragmentCameraBinding().overlay.invalidate();
//                    }
//
//                }
//            }));
//        }
//
//    }
//
//    public void onEmpty() {
//        this.getFragmentCameraBinding().overlay.clear();
//        FragmentActivity var10000 = this;
//        if (var10000 != null) {
//            var10000.runOnUiThread((Runnable)(new Runnable() {
//                public final void run() {
//                    CameraActivity2.this.getFaceBlendshapesResultAdapter().updateResults((FaceLandmarkerResult)null);
//                    CameraActivity2.this.getFaceBlendshapesResultAdapter().notifyDataSetChanged();
//                }
//            }));
//        }
//
//    }
//
//    public void onError(@NotNull final String error, final int errorCode) {
//        Intrinsics.checkNotNullParameter(error, "error");
//        FragmentActivity var10000 = this;
//        if (var10000 != null) {
//            var10000.runOnUiThread((Runnable)(new Runnable() {
//                public final void run() {
//                    Toast.makeText(CameraActivity2.this, (CharSequence)error, 0).show();
//                    CameraActivity2.this.getFaceBlendshapesResultAdapter().updateResults((FaceLandmarkerResult)null);
//                    CameraActivity2.this.getFaceBlendshapesResultAdapter().notifyDataSetChanged();
//                    if (errorCode == 1) {
//                        CameraActivity2.this.getFragmentCameraBinding().bottomSheetLayout.spinnerDelegate.setSelection(0, false);
//                    }
//
//                }
//            }));
//        }
//
//    }
//
//    public CameraActivity2() {
//        Function0 extrasProducer$iv = null;
//        Function0 factoryProducer$iv = null;
////        int $i$f$activityViewModels = false;
//        this.viewModel$delegate = FragmentViewModelLazyKt.createViewModelLazy(this, Reflection.getOrCreateKotlinClass(MainViewModel.class), (Function0)(new CameraFragment$$special$$inlined$activityViewModels$1(this)), (Function0)(new CameraFragment$$special$$inlined$activityViewModels$2((Function0)extrasProducer$iv, this)), (Function0)(new CameraFragment$$special$$inlined$activityViewModels$3(this)));
//        this.faceBlendshapesResultAdapter$delegate = LazyKt.lazy((Function0)null.INSTANCE);
//    }
//
//    // $FF: synthetic method
//    public static final FaceLandmarkerHelper access$getFaceLandmarkerHelper$p(CameraActivity2 $this) {
//        FaceLandmarkerHelper var10000 = $this.faceLandmarkerHelper;
//        if (var10000 == null) {
//            Intrinsics.throwUninitializedPropertyAccessException("faceLandmarkerHelper");
//        }
//
//        return var10000;
//    }
//
//    // $FF: synthetic method
//    public static final ProcessCameraProvider access$getCameraProvider$p(CameraActivity2 $this) {
//        return $this.cameraProvider;
//    }
//
//    // $FF: synthetic method
//    public static final void access$detectFace(CameraActivity2 $this, ImageProxy imageProxy) {
//        $this.detectFace(imageProxy);
//    }
//
//    // $FF: synthetic method
//    public static final void access$set_fragmentCameraBinding$p(CameraActivity2 $this, CameraLayoutBinding var1) {
//        $this.cameraLayoutBinding = var1;
//    }

//    @Metadata(
//            mv = {1, 7, 0},
//            k = 1,
//            d1 = {"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082T¢\u0006\u0002\n\u0000¨\u0006\u0005"},
//            d2 = {"Lcom/google/mediapipe/examples/facelandmarker/fragment/CameraFragment$Companion;", "", "()V", "TAG", "", "app_debug"}
//    )
//    public static final class Companion {
//        private Companion() {
//        }
//
//        // $FF: synthetic method
//        public Companion(DefaultConstructorMarker $constructor_marker) {
//            this();
//        }
//    }
//}
// CameraFragment$bindCameraUseCases$$inlined$also$lambda$1.java
//package com.google.mediapipe.examples.facelandmarker.fragment;
//
//        import androidx.camera.core.ImageAnalysis;
//        import androidx.camera.core.ImageProxy;
//        import kotlin.Metadata;
//        import kotlin.jvm.internal.Intrinsics;
//        import org.jetbrains.annotations.NotNull;

//@Metadata(
//        mv = {1, 7, 0},
//        k = 3,
//        d1 = {"\u0000\u0010\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0003H\n¢\u0006\u0002\b\u0004¨\u0006\u0005"},
//        d2 = {"<anonymous>", "", "image", "Landroidx/camera/core/ImageProxy;", "analyze", "com/google/mediapipe/examples/facelandmarker/fragment/CameraFragment$bindCameraUseCases$1$1"}
//)
//final class CameraFragment$bindCameraUseCases$$inlined$also$lambda$1 implements ImageAnalysis.Analyzer {
//    // $FF: synthetic field
//    final CameraFragment this$0;
//
//    CameraFragment$bindCameraUseCases$$inlined$also$lambda$1(CameraFragment var1) {
//        this.this$0 = var1;
//    }
//
//    public final void analyze(@NotNull ImageProxy image) {
//        Intrinsics.checkNotNullParameter(image, "image");
//        CameraFragment.access$detectFace(this.this$0, image);
//    }
//}
// CameraFragment$$special$$inlined$activityViewModels$2.java
//package com.google.mediapipe.examples.facelandmarker.fragment;
//
//        import androidx.fragment.app.Fragment;
//        import androidx.lifecycle.viewmodel.CreationExtras;
//        import kotlin.Metadata;
//        import kotlin.jvm.functions.Function0;
//        import kotlin.jvm.internal.Intrinsics;
//        import kotlin.jvm.internal.Lambda;
//        import kotlin.jvm.internal.SourceDebugExtension;
//        import org.jetbrains.annotations.NotNull;

//@Metadata(
//        mv = {1, 7, 0},
//        k = 3,
//        xi = 48,
//        d1 = {"\u0000\u0010\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\u0010\u0000\u001a\u00020\u0001\"\n\b\u0000\u0010\u0002\u0018\u0001*\u00020\u0003H\n¢\u0006\u0002\b\u0004¨\u0006\u0005"},
//        d2 = {"<anonymous>", "Landroidx/lifecycle/viewmodel/CreationExtras;", "VM", "Landroidx/lifecycle/ViewModel;", "invoke", "androidx/fragment/app/FragmentViewModelLazyKt$activityViewModels$5"}
//)
//@SourceDebugExtension({"SMAP\nFragmentViewModelLazy.kt\nKotlin\n*S Kotlin\n*F\n+ 1 FragmentViewModelLazy.kt\nandroidx/fragment/app/FragmentViewModelLazyKt$activityViewModels$5\n*L\n1#1,222:1\n*E\n"})
//public final class CameraFragment$$special$$inlined$activityViewModels$2 extends Lambda implements Function0 {
//    // $FF: synthetic field
//    final Function0 $extrasProducer;
//    // $FF: synthetic field
//    final Fragment $this_activityViewModels;
//
//    public CameraFragment$$special$$inlined$activityViewModels$2(Function0 $extrasProducer, Fragment $receiver) {
//        super(0);
//        this.$extrasProducer = $extrasProducer;
//        this.$this_activityViewModels = $receiver;
//    }
//
//    @NotNull
//    public final CreationExtras invoke() {
//        Function0 var10000 = this.$extrasProducer;
//        CreationExtras var1;
//        if (var10000 != null) {
//            var1 = (CreationExtras)var10000.invoke();
//            if (var1 != null) {
//                return var1;
//            }
//        }
//
//        var1 = this.$this_activityViewModels.requireActivity().getDefaultViewModelCreationExtras();
//        Intrinsics.checkNotNullExpressionValue(var1, "requireActivity().defaultViewModelCreationExtras");
//        return var1;
//    }
//
//    // $FF: synthetic method
//    // $FF: bridge method
////    public Object invoke() {
////        return this.invoke();
////    }
//}
// CameraFragment$onPause$1.java
//package com.google.mediapipe.examples.facelandmarker.fragment;
//
//        import com.google.mediapipe.examples.facelandmarker.FaceLandmarkerHelper;
//        import kotlin.Metadata;
//        import kotlin.jvm.internal.MutablePropertyReference0Impl;
//        import org.jetbrains.annotations.Nullable;

// $FF: synthetic class
//@Metadata(
//        mv = {1, 7, 0},
//        k = 3
//)
//final class CameraFragment$onPause$1 extends MutablePropertyReference0Impl {
//    CameraFragment$onPause$1(CameraFragment var1) {
//        super(var1, CameraFragment.class, "faceLandmarkerHelper", "getFaceLandmarkerHelper()Lcom/google/mediapipe/examples/facelandmarker/FaceLandmarkerHelper;", 0);
//    }
//
//    @Nullable
//    public Object get() {
//        return CameraFragment.access$getFaceLandmarkerHelper$p((CameraFragment)this.receiver);
//    }
//
//    public void set(@Nullable Object value) {
//        CameraFragment.access$setFaceLandmarkerHelper$p((CameraFragment)this.receiver, (FaceLandmarkerHelper)value);
//    }
//}
// CameraFragment$$special$$inlined$activityViewModels$3.java
//package com.google.mediapipe.examples.facelandmarker.fragment;
//
//        import androidx.fragment.app.Fragment;
//        import androidx.lifecycle.ViewModelProvider;
//        import kotlin.Metadata;
//        import kotlin.jvm.functions.Function0;
//        import kotlin.jvm.internal.Intrinsics;
//        import kotlin.jvm.internal.Lambda;
//        import kotlin.jvm.internal.SourceDebugExtension;
//        import org.jetbrains.annotations.NotNull;

//@Metadata(
//        mv = {1, 7, 0},
//        k = 3,
//        xi = 48,
//        d1 = {"\u0000\u0010\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\u0010\u0000\u001a\u00020\u0001\"\n\b\u0000\u0010\u0002\u0018\u0001*\u00020\u0003H\n¢\u0006\u0002\b\u0004¨\u0006\u0005"},
//        d2 = {"<anonymous>", "Landroidx/lifecycle/ViewModelProvider$Factory;", "VM", "Landroidx/lifecycle/ViewModel;", "invoke", "androidx/fragment/app/FragmentViewModelLazyKt$activityViewModels$6"}
//)
//@SourceDebugExtension({"SMAP\nFragmentViewModelLazy.kt\nKotlin\n*S Kotlin\n*F\n+ 1 FragmentViewModelLazy.kt\nandroidx/fragment/app/FragmentViewModelLazyKt$activityViewModels$6\n*L\n1#1,222:1\n*E\n"})
//public final class CameraFragment$$special$$inlined$activityViewModels$3 extends Lambda implements Function0 {
//    // $FF: synthetic field
//    final Fragment $this_activityViewModels;
//
//    public CameraFragment$$special$$inlined$activityViewModels$3(Fragment $receiver) {
//        super(0);
//        this.$this_activityViewModels = $receiver;
//    }

//    @NotNull
//    public final ViewModelProvider.Factory invoke() {
//        ViewModelProvider.Factory var10000 = this.$this_activityViewModels.requireActivity().getDefaultViewModelProviderFactory();
//        Intrinsics.checkNotNullExpressionValue(var10000, "requireActivity().defaultViewModelProviderFactory");
//        return var10000;
//    }

    // $FF: synthetic method
    // $FF: bridge method
//    public Object invoke() {
//        return this.invoke();
//    }
//}
// CameraFragment$$special$$inlined$activityViewModels$1.java
//package com.google.mediapipe.examples.facelandmarker.fragment;
//
//        import androidx.fragment.app.Fragment;
//        import androidx.lifecycle.ViewModelStore;
//        import kotlin.Metadata;
//        import kotlin.jvm.functions.Function0;
//        import kotlin.jvm.internal.Intrinsics;
//        import kotlin.jvm.internal.Lambda;
//        import kotlin.jvm.internal.SourceDebugExtension;
//        import org.jetbrains.annotations.NotNull;

//@Metadata(
//        mv = {1, 7, 0},
//        k = 3,
//        xi = 48,
//        d1 = {"\u0000\u0010\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\u0010\u0000\u001a\u00020\u0001\"\n\b\u0000\u0010\u0002\u0018\u0001*\u00020\u0003H\n¢\u0006\u0002\b\u0004¨\u0006\u0005"},
//        d2 = {"<anonymous>", "Landroidx/lifecycle/ViewModelStore;", "VM", "Landroidx/lifecycle/ViewModel;", "invoke", "androidx/fragment/app/FragmentViewModelLazyKt$activityViewModels$4"}
//)
//@SourceDebugExtension({"SMAP\nFragmentViewModelLazy.kt\nKotlin\n*S Kotlin\n*F\n+ 1 FragmentViewModelLazy.kt\nandroidx/fragment/app/FragmentViewModelLazyKt$activityViewModels$4\n*L\n1#1,222:1\n*E\n"})
//public final class CameraFragment$$special$$inlined$activityViewModels$1 extends Lambda implements Function0 {
//    // $FF: synthetic field
//    final Fragment $this_activityViewModels;
//
//    public CameraFragment$$special$$inlined$activityViewModels$1(Fragment $receiver) {
//        super(0);
//        this.$this_activityViewModels = $receiver;
//    }
//
//    @NotNull
//    public final ViewModelStore invoke() {
//        ViewModelStore var10000 = this.$this_activityViewModels.requireActivity().getViewModelStore();
//        Intrinsics.checkNotNullExpressionValue(var10000, "requireActivity().viewModelStore");
//        return var10000;
//    }
//
//    // $FF: synthetic method
//    // $FF: bridge method
//    public Object invoke() {
//        return this.invoke();
//    }
//}

