package com.faceapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.Bitmap.Config;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.SystemClock;
import android.util.Log;

import androidx.annotation.VisibleForTesting;
import androidx.camera.core.ImageInfo;
import androidx.camera.core.ImageProxy;
import com.google.mediapipe.framework.image.BitmapImageBuilder;
import com.google.mediapipe.framework.image.MPImage;
import com.google.mediapipe.tasks.core.BaseOptions;
import com.google.mediapipe.tasks.core.Delegate;
import com.google.mediapipe.tasks.vision.core.RunningMode;
import com.google.mediapipe.tasks.vision.facelandmarker.FaceLandmarker;
import com.google.mediapipe.tasks.vision.facelandmarker.FaceLandmarkerResult;
import java.nio.Buffer;
import java.util.ArrayList;
import java.util.List;
import kotlin.Metadata;
import kotlin.Unit;
//import kotlin.jdk7.AutoCloseableKt;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Ref;
//import kotlin.jvm.internal.SourceDebugExtension;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class FaceLandmarkerHelper {
    public FaceLandmarker faceLandmarker;
    public float minFaceDetectionConfidence;
    public float minFaceTrackingConfidence;
    public float minFacePresenceConfidence;
    public int maxNumFaces;
    public int currentDelegate;
    @NotNull
    private RunningMode runningMode;
    @NotNull
    private Context context;
    @Nullable
    private LandmarkerListener faceLandmarkerHelperListener;
    @NotNull
    public static final String TAG = "FaceLandmarkerHelper";
    private static final String MP_FACE_LANDMARKER_TASK = "face_landmarker.task";
    public final static int DELEGATE_CPU = 0;
    public final static int DELEGATE_GPU = 1;
    public static final float DEFAULT_FACE_DETECTION_CONFIDENCE = 0.5F;
    public static final float DEFAULT_FACE_TRACKING_CONFIDENCE = 0.5F;
    public static final float DEFAULT_FACE_PRESENCE_CONFIDENCE = 0.5F;
    public static final int DEFAULT_NUM_FACES = 1;
    public static final int OTHER_ERROR = 0;
    public static final int GPU_ERROR = 1;
    @NotNull
//    public static final Companion Companion = new Companion((DefaultConstructorMarker)null);

    public void clearFaceLandmarker() {
        FaceLandmarker var10000 = this.faceLandmarker;
        if (var10000 != null) {
            var10000.close();
        }

        this.faceLandmarker = null;
    }

    public boolean isClose() {
        return this.faceLandmarker == null;
    }

    public void setupFaceLandmarker() {
        Log.d("FaceappSee", "setupFaceLandmarker: ");
        BaseOptions.Builder baseOptionBuilder = BaseOptions.builder();

        switch(currentDelegate) {
            case DELEGATE_CPU :
                baseOptionBuilder.setDelegate(Delegate.CPU);
                break;

            case DELEGATE_GPU :
                baseOptionBuilder.setDelegate(Delegate.GPU);
                break;

            default: break;
        }

        Log.d("FaceappSee", baseOptionBuilder.toString());
        baseOptionBuilder.setModelAssetPath(MP_FACE_LANDMARKER_TASK);

        switch(runningMode) {
            case LIVE_STREAM :
                Log.d("FaceappSee", faceLandmarkerHelperListener.toString());
                if (faceLandmarkerHelperListener == null) {
                    Log.d("FaceappSee", "faceLandmarkerHelperListener");
                    throw new IllegalStateException(
                            "faceLandmarkerHelperListener must be set when runningMode is LIVE_STREAM."
                    );
                }
                break;
            default: break;
                // no-op
        }

        try {
            BaseOptions baseOptions = baseOptionBuilder.build();
            
            // Create an option builder with base options and specific
            // options only use for Face Landmarker.
            FaceLandmarker.FaceLandmarkerOptions.Builder optionsBuilder  =
                    FaceLandmarker.FaceLandmarkerOptions.builder()
                            .setBaseOptions(baseOptions)
                            .setMinFaceDetectionConfidence(minFaceDetectionConfidence)
                            .setMinTrackingConfidence(minFaceTrackingConfidence)
                            .setMinFacePresenceConfidence(minFacePresenceConfidence)
                            .setNumFaces(maxNumFaces)
                            .setOutputFaceBlendshapes(true)
                            .setRunningMode(runningMode);

            // The ResultListener and ErrorListener only use for LIVE_STREAM mode.
            if (runningMode == RunningMode.LIVE_STREAM) {
                optionsBuilder
                        .setResultListener(this::returnLivestreamResult)
                        .setErrorListener(this::returnLivestreamError);
            }

            FaceLandmarker.FaceLandmarkerOptions options = optionsBuilder.build();
            faceLandmarker =
                    FaceLandmarker.createFromOptions(context, options);
        } catch (IllegalStateException e) {
            faceLandmarkerHelperListener.onError(
                    "Face Landmarker failed to initialize. See error logs for " +
                            "details", GPU_ERROR
            );
            Log.e("FaceappSee", "MediaPipe failed to load the task with error: " + e.toString());
        } catch (RuntimeException e) {
            // This occurs if the model being used does not support GPU
            faceLandmarkerHelperListener.onError(
                    "Face Landmarker failed to initialize. See error logs for " +
                            "details", GPU_ERROR
            );
            Log.e("FaceappSee", "Face Landmarker failed to load model with error: " + e.toString());
        }
    }

    public void detectLiveStream(@NotNull ImageProxy imageProxy, boolean isFrontCamera) {
        Intrinsics.checkNotNullParameter(imageProxy, "imageProxy");
        if (this.runningMode != RunningMode.LIVE_STREAM) {
            throw new IllegalArgumentException("Attempting to call detectLiveStream while not using RunningMode.LIVE_STREAM");
        } else {
            long frameTime = SystemClock.uptimeMillis();
            Bitmap bitmapBuffer = Bitmap.createBitmap(imageProxy.getWidth(), imageProxy.getHeight(), Config.ARGB_8888);
            AutoCloseable var6 = (AutoCloseable)imageProxy;
            Throwable var7 = null;

            boolean var9;
            try {
                ImageProxy it = (ImageProxy)var6;
                var9 = false;
                ImageProxy.PlaneProxy var10001 = imageProxy.getPlanes()[0];
                Intrinsics.checkNotNullExpressionValue(var10001, "imageProxy.planes[0]");
                bitmapBuffer.copyPixelsFromBuffer((Buffer)var10001.getBuffer());
                Unit var15 = Unit.INSTANCE;
            } catch (Throwable var12) {
                var7 = var12;
                throw var12;
            } finally {
//                AutoCloseableKt.closeFinally(var6, var7);
            }

            imageProxy.close();
            Matrix var14 = new Matrix();
            var9 = false;
            ImageInfo var18 = imageProxy.getImageInfo();
            Intrinsics.checkNotNullExpressionValue(var18, "imageProxy.imageInfo");
            var14.postRotate((float)var18.getRotationDegrees());
            if (isFrontCamera) {
                var14.postScale(-1.0F, 1.0F, (float)imageProxy.getWidth(), (float)imageProxy.getHeight());
            }

            Intrinsics.checkNotNullExpressionValue(bitmapBuffer, "bitmapBuffer");
            Bitmap rotatedBitmap = Bitmap.createBitmap(bitmapBuffer, 0, 0, bitmapBuffer.getWidth(), bitmapBuffer.getHeight(), var14, true);
            MPImage mpImage = (new BitmapImageBuilder(rotatedBitmap)).build();
            Intrinsics.checkNotNullExpressionValue(mpImage, "mpImage");
            this.detectAsync(mpImage, frameTime);
        }
    }

    @VisibleForTesting
    public final void detectAsync(@NotNull MPImage mpImage, long frameTime) {
        Intrinsics.checkNotNullParameter(mpImage, "mpImage");
        FaceLandmarker var10000 = this.faceLandmarker;
        if (var10000 != null) {
            var10000.detectAsync(mpImage, frameTime);
        }

    }

    @Nullable
    public final VideoResultBundle detectVideoFile(@NotNull Uri videoUri, long inferenceIntervalMs) {
        Intrinsics.checkNotNullParameter(videoUri, "videoUri");
        if (this.runningMode != RunningMode.VIDEO) {
            throw new IllegalArgumentException("Attempting to call detectVideoFile while not using RunningMode.VIDEO");
        } else {
            long startTime = SystemClock.uptimeMillis();
            Ref.BooleanRef didErrorOccurred = new Ref.BooleanRef();
            didErrorOccurred.element = false;
            MediaMetadataRetriever retriever = new MediaMetadataRetriever();
            retriever.setDataSource(this.context, videoUri);
            String var10000 = retriever.extractMetadata(9);
            Long var30;
            if (var10000 != null) {
                String var9 = var10000;
                var30 = Long.parseLong(var9);
            } else {
                var30 = null;
            }

            Long videoLengthMs = var30;
            Bitmap firstFrame = retriever.getFrameAtTime(0L);
            Integer width = firstFrame != null ? firstFrame.getWidth() : null;
            Integer height = firstFrame != null ? firstFrame.getHeight() : null;
            if (videoLengthMs != null && width != null && height != null) {
                List resultList = (List)(new ArrayList());
                long numberOfFrameToRead = videoLengthMs / inferenceIntervalMs;
                long i = 0L;
                long var17 = numberOfFrameToRead;
                if (i <= numberOfFrameToRead) {
                    while(true) {
                        long timestampMs = i * inferenceIntervalMs;
                        Bitmap var31 = retriever.getFrameAtTime(timestampMs * (long)1000, 3);
                        boolean var23;
                        if (var31 != null) {
                            label72: {
                                Bitmap var21 = var31;
                                var23 = false;
                                Intrinsics.checkNotNullExpressionValue(var21, "frame");
                                Bitmap argb8888Frame = var21.getConfig() == Config.ARGB_8888 ? var21 : var21.copy(Config.ARGB_8888, false);
                                MPImage mpImage = (new BitmapImageBuilder(argb8888Frame)).build();
                                FaceLandmarker var34 = this.faceLandmarker;
                                if (var34 != null) {
                                    FaceLandmarkerResult var35 = var34.detectForVideo(mpImage, timestampMs);
                                    if (var35 != null) {
                                        FaceLandmarkerResult var26 = var35;
//                                        int var28 = false;
                                        resultList.add(var26);
                                        break label72;
                                    }
                                }

//                                new FaceLandmarkerHelper$detectVideoFile$$inlined$let$lambda$1(this, timestampMs, resultList, didErrorOccurred);
                            }
                        } else {
                            FaceLandmarkerHelper $this$run = (FaceLandmarkerHelper)this;
                            var23 = false;
                            didErrorOccurred.element = true;
                            LandmarkerListener var32 = $this$run.faceLandmarkerHelperListener;
                            if (var32 != null) {
                                FaceLandmarkerHelper.LandmarkerListener.DefaultImpls.onError$default(var32, "Frame at specified time could not be retrieved when detecting in video.", 0, 2, (Object)null);
                                Unit var33 = Unit.INSTANCE;
                            } else {
                                var10000 = null;
                            }
                        }

                        if (i == var17) {
                            break;
                        }

                        ++i;
                    }
                }

//                retriever.release();
                i = (SystemClock.uptimeMillis() - startTime) / numberOfFrameToRead;
                return didErrorOccurred.element ? null : new VideoResultBundle(resultList, i, height, width);
            } else {
                return null;
            }
        }
    }

    @Nullable
    public final ResultBundle detectImage(@NotNull Bitmap image) {
        Intrinsics.checkNotNullParameter(image, "image");
        if (this.runningMode != RunningMode.IMAGE) {
            throw new IllegalArgumentException("Attempting to call detectImage while not using RunningMode.IMAGE");
        } else {
            long startTime = SystemClock.uptimeMillis();
            MPImage mpImage = (new BitmapImageBuilder(image)).build();
            FaceLandmarker var10000 = this.faceLandmarker;
            if (var10000 != null) {
                FaceLandmarkerResult var10 = var10000.detect(mpImage);
                if (var10 != null) {
                    FaceLandmarkerResult var5 = var10;
//                    int var7 = false;
                    long inferenceTimeMs = SystemClock.uptimeMillis() - startTime;
                    return new ResultBundle(var5, inferenceTimeMs, image.getHeight(), image.getWidth());
                }
            }

            LandmarkerListener var11 = this.faceLandmarkerHelperListener;
            if (var11 != null) {
                FaceLandmarkerHelper.LandmarkerListener.DefaultImpls.onError$default(var11, "Face Landmarker failed to detect.", 0, 2, (Object)null);
            }

            return null;
        }
    }

    private void returnLivestreamResult(FaceLandmarkerResult result, MPImage input) {
        LandmarkerListener var10000;
        if (result.faceLandmarks().size() > 0) {
            long finishTimeMs = SystemClock.uptimeMillis();
            long inferenceTime = finishTimeMs - result.timestampMs();
            var10000 = this.faceLandmarkerHelperListener;
            if (var10000 != null) {
                var10000.onResults(new ResultBundle(result, inferenceTime, input.getHeight(), input.getWidth()));
            }
        } else {
            var10000 = this.faceLandmarkerHelperListener;
            if (var10000 != null) {
                var10000.onEmpty();
            }
        }

    }

    private void returnLivestreamError(RuntimeException error) {
        LandmarkerListener var10000 = this.faceLandmarkerHelperListener;
        if (var10000 != null) {
            String var10001 = error.getMessage();
            if (var10001 == null) {
                var10001 = "An unknown error has occurred";
            }

            FaceLandmarkerHelper.LandmarkerListener.DefaultImpls.onError$default(var10000, var10001, 0, 2, (Object)null);
        }

    }

    public final float getMinFaceDetectionConfidence() {
        return this.minFaceDetectionConfidence;
    }

    public final void setMinFaceDetectionConfidence(float var1) {
        this.minFaceDetectionConfidence = var1;
    }

    public final float getMinFaceTrackingConfidence() {
        return this.minFaceTrackingConfidence;
    }

    public final void setMinFaceTrackingConfidence(float var1) {
        this.minFaceTrackingConfidence = var1;
    }

    public final float getMinFacePresenceConfidence() {
        return this.minFacePresenceConfidence;
    }

    public final void setMinFacePresenceConfidence(float var1) {
        this.minFacePresenceConfidence = var1;
    }

    public final int getMaxNumFaces() {
        return this.maxNumFaces;
    }

    public final void setMaxNumFaces(int var1) {
        this.maxNumFaces = var1;
    }

    public final int getCurrentDelegate() {
        return this.currentDelegate;
    }

    public final void setCurrentDelegate(int var1) {
        this.currentDelegate = var1;
    }

    @NotNull
    public final RunningMode getRunningMode() {
        return this.runningMode;
    }

    public final void setRunningMode(@NotNull RunningMode var1) {
        Intrinsics.checkNotNullParameter(var1, "<set-?>");
        this.runningMode = var1;
    }

    @NotNull
    public final Context getContext() {
        return this.context;
    }

    @Nullable
    public final LandmarkerListener getFaceLandmarkerHelperListener() {
        return this.faceLandmarkerHelperListener;
    }

    public FaceLandmarkerHelper(
            float minFaceDetectionConfidence, float minFaceTrackingConfidence, float minFacePresenceConfidence,
            int maxNumFaces, int currentDelegate, @NotNull RunningMode runningMode, @NotNull Context context,
            @Nullable LandmarkerListener faceLandmarkerHelperListener) {
        super();
        Intrinsics.checkNotNullParameter(runningMode, "runningMode");
        Intrinsics.checkNotNullParameter(context, "context");

        this.minFaceDetectionConfidence = minFaceDetectionConfidence;
        this.minFaceTrackingConfidence = minFaceTrackingConfidence;
        this.minFacePresenceConfidence = minFacePresenceConfidence;
        this.maxNumFaces = maxNumFaces;
        this.currentDelegate = currentDelegate;
        this.runningMode = runningMode;
        this.context = context;
        this.faceLandmarkerHelperListener = faceLandmarkerHelperListener;
        this.setupFaceLandmarker();
    }

    // $FF: synthetic method
    public FaceLandmarkerHelper(float var1, float var2, float var3, int var4, int var5, RunningMode var6, Context var7, LandmarkerListener var8, int var9, DefaultConstructorMarker var10) {
        if ((var9 & 1) != 0) {
            var1 = 0.5F;
        }

        if ((var9 & 2) != 0) {
            var2 = 0.5F;
        }

        if ((var9 & 4) != 0) {
            var3 = 0.5F;
        }

        if ((var9 & 8) != 0) {
            var4 = 1;
        }

        if ((var9 & 16) != 0) {
            var5 = 0;
        }

        if ((var9 & 32) != 0) {
            var6 = RunningMode.IMAGE;
        }

        if ((var9 & 128) != 0) {
            var8 = null;
        }

//        this(var1, var2, var3, var4, var5, var6, var7, var8);
    }

    public static final class ResultBundle {
        @NotNull
        private final FaceLandmarkerResult result;
        private final long inferenceTime;
        private final int inputImageHeight;
        private final int inputImageWidth;

        @NotNull
        public final FaceLandmarkerResult getResult() {
            return this.result;
        }

        public final long getInferenceTime() {
            return this.inferenceTime;
        }

        public final int getInputImageHeight() {
            return this.inputImageHeight;
        }

        public final int getInputImageWidth() {
            return this.inputImageWidth;
        }

        public ResultBundle(@NotNull FaceLandmarkerResult result, long inferenceTime, int inputImageHeight, int inputImageWidth) {
            super();
            Intrinsics.checkNotNullParameter(result, "result");

            this.result = result;
            this.inferenceTime = inferenceTime;
            this.inputImageHeight = inputImageHeight;
            this.inputImageWidth = inputImageWidth;
        }

        @NotNull
        public final FaceLandmarkerResult component1() {
            return this.result;
        }

        public final long component2() {
            return this.inferenceTime;
        }

        public final int component3() {
            return this.inputImageHeight;
        }

        public final int component4() {
            return this.inputImageWidth;
        }

        @NotNull
        public final ResultBundle copy(@NotNull FaceLandmarkerResult result, long inferenceTime, int inputImageHeight, int inputImageWidth) {
            Intrinsics.checkNotNullParameter(result, "result");
            return new ResultBundle(result, inferenceTime, inputImageHeight, inputImageWidth);
        }

        // $FF: synthetic method
        public static ResultBundle copy$default(ResultBundle var0, FaceLandmarkerResult var1, long var2, int var4, int var5, int var6, Object var7) {
            if ((var6 & 1) != 0) {
                var1 = var0.result;
            }

            if ((var6 & 2) != 0) {
                var2 = var0.inferenceTime;
            }

            if ((var6 & 4) != 0) {
                var4 = var0.inputImageHeight;
            }

            if ((var6 & 8) != 0) {
                var5 = var0.inputImageWidth;
            }

            return var0.copy(var1, var2, var4, var5);
        }

        @NotNull
        public String toString() {
            return "ResultBundle(result=" + this.result + ", inferenceTime=" + this.inferenceTime + ", inputImageHeight=" + this.inputImageHeight + ", inputImageWidth=" + this.inputImageWidth + ")";
        }

        public int hashCode() {
            FaceLandmarkerResult var10000 = this.result;
            return (((var10000 != null ? var10000.hashCode() : 0) * 31 + Long.hashCode(this.inferenceTime)) * 31 + Integer.hashCode(this.inputImageHeight)) * 31 + Integer.hashCode(this.inputImageWidth);
        }

        public boolean equals(@Nullable Object var1) {
            if (this != var1) {
                if (var1 instanceof ResultBundle) {
                    ResultBundle var2 = (ResultBundle)var1;
                    if (Intrinsics.areEqual(this.result, var2.result) && this.inferenceTime == var2.inferenceTime && this.inputImageHeight == var2.inputImageHeight && this.inputImageWidth == var2.inputImageWidth) {
                        return true;
                    }
                }

                return false;
            } else {
                return true;
            }
        }
    }

    public static final class VideoResultBundle {
        @NotNull
        private final List results;
        private final long inferenceTime;
        private final int inputImageHeight;
        private final int inputImageWidth;

        @NotNull
        public final List getResults() {
            return this.results;
        }

        public final long getInferenceTime() {
            return this.inferenceTime;
        }

        public final int getInputImageHeight() {
            return this.inputImageHeight;
        }

        public final int getInputImageWidth() {
            return this.inputImageWidth;
        }

        public VideoResultBundle(@NotNull List results, long inferenceTime, int inputImageHeight, int inputImageWidth) {
            super();
            Intrinsics.checkNotNullParameter(results, "results");

            this.results = results;
            this.inferenceTime = inferenceTime;
            this.inputImageHeight = inputImageHeight;
            this.inputImageWidth = inputImageWidth;
        }

        @NotNull
        public final List component1() {
            return this.results;
        }

        public final long component2() {
            return this.inferenceTime;
        }

        public final int component3() {
            return this.inputImageHeight;
        }

        public final int component4() {
            return this.inputImageWidth;
        }

        @NotNull
        public final VideoResultBundle copy(@NotNull List results, long inferenceTime, int inputImageHeight, int inputImageWidth) {
            Intrinsics.checkNotNullParameter(results, "results");
            return new VideoResultBundle(results, inferenceTime, inputImageHeight, inputImageWidth);
        }

        // $FF: synthetic method
        public static VideoResultBundle copy$default(VideoResultBundle var0, List var1, long var2, int var4, int var5, int var6, Object var7) {
            if ((var6 & 1) != 0) {
                var1 = var0.results;
            }

            if ((var6 & 2) != 0) {
                var2 = var0.inferenceTime;
            }

            if ((var6 & 4) != 0) {
                var4 = var0.inputImageHeight;
            }

            if ((var6 & 8) != 0) {
                var5 = var0.inputImageWidth;
            }

            return var0.copy(var1, var2, var4, var5);
        }

        @NotNull
        public String toString() {
            return "VideoResultBundle(results=" + this.results + ", inferenceTime=" + this.inferenceTime + ", inputImageHeight=" + this.inputImageHeight + ", inputImageWidth=" + this.inputImageWidth + ")";
        }

        public int hashCode() {
            List var10000 = this.results;
            return (((var10000 != null ? var10000.hashCode() : 0) * 31 + Long.hashCode(this.inferenceTime)) * 31 + Integer.hashCode(this.inputImageHeight)) * 31 + Integer.hashCode(this.inputImageWidth);
        }

        public boolean equals(@Nullable Object var1) {
            if (this != var1) {
                if (var1 instanceof VideoResultBundle) {
                    VideoResultBundle var2 = (VideoResultBundle)var1;
                    if (Intrinsics.areEqual(this.results, var2.results) && this.inferenceTime == var2.inferenceTime && this.inputImageHeight == var2.inputImageHeight && this.inputImageWidth == var2.inputImageWidth) {
                        return true;
                    }
                }

                return false;
            } else {
                return true;
            }
        }
    }

    public interface LandmarkerListener {
        void onError(@NotNull String var1, int var2);

        void onResults(@NotNull ResultBundle var1);

        void onEmpty();
        public static final class DefaultImpls {
            // $FF: synthetic method
            public static void onError$default(LandmarkerListener var0, String var1, int var2, int var3, Object var4) {
                if (var4 != null) {
                    throw new UnsupportedOperationException("Super calls with default arguments not supported in this target, function: onError");
                } else {
                    if ((var3 & 2) != 0) {
                        var2 = 0;
                    }

                    var0.onError(var1, var2);
                }
            }

            public static void onEmpty(@NotNull LandmarkerListener $this) {
            }
        }
    }

    public static final class Companion {
        private Companion() {
        }

        // $FF: synthetic method
        public Companion(DefaultConstructorMarker $constructor_marker) {
            this();
        }
    }
}

