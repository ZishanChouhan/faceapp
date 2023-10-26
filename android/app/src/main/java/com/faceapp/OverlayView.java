package com.faceapp;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import androidx.core.content.ContextCompat;
import com.google.mediapipe.tasks.components.containers.Category;
import com.google.mediapipe.tasks.components.containers.Connection;
import com.google.mediapipe.tasks.components.containers.NormalizedLandmark;
import com.google.mediapipe.tasks.vision.core.RunningMode;
import com.google.mediapipe.tasks.vision.facelandmarker.FaceLandmarker;
import com.google.mediapipe.tasks.vision.facelandmarker.FaceLandmarkerResult;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class OverlayView extends View {
    private FaceLandmarkerResult results;
    private Paint linePaint = new Paint();
    private Paint pointPaint = new Paint();
    private float scaleFactor = 1.0F;
    private int imageWidth = 1;
    private int imageHeight = 1;
    private static float LANDMARK_STROKE_WIDTH = 8.0F;
    private static final String TAG = "Face Landmarker Overlay";
//    @NotNull
//    public static final Companion Companion = new Companion((DefaultConstructorMarker)null);

    public OverlayView(@Nullable Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.initPaints();
    }
    public void clear() {
        this.results = null;
        this.linePaint.reset();
        this.pointPaint.reset();
        this.invalidate();
        this.initPaints();
    }

    private void initPaints() {
        Paint var10000 = this.linePaint;
        Context var10001 = this.getContext();
        Intrinsics.checkNotNull(var10001);
        var10000.setColor(ContextCompat.getColor(var10001,  R.color.mp_color_primary));
        this.linePaint.setStrokeWidth(8.0F);
        this.linePaint.setStyle(Style.STROKE);
        this.pointPaint.setColor(-256);
        this.pointPaint.setStrokeWidth(8.0F);
        this.pointPaint.setStyle(Style.FILL);
    }

    public void draw(@NotNull Canvas canvas) {
        Intrinsics.checkNotNullParameter(canvas, "canvas");
        super.draw(canvas);

//        if(results == null || results!!.faceLandmarks().isEmpty()) {
//            clear()
//            return
//        }

//        results?.let { faceLandmarkerResult ->
//
//            if( faceLandmarkerResult.faceBlendshapes().isPresent) {
//                faceLandmarkerResult.faceBlendshapes().get().forEach {
//                    it.forEach {
//                        Log.e(TAG, it.displayName() + " " + it.score())
//                    }
//                }
//            }
//
//            for(landmark in faceLandmarkerResult.faceLandmarks()) {
//                for(normalizedLandmark in landmark) {
//                    canvas.drawPoint(normalizedLandmark.x() * imageWidth * scaleFactor, normalizedLandmark.y() * imageHeight * scaleFactor, pointPaint)
//                }
//            }
//
//            FaceLandmarker.FACE_LANDMARKS_CONNECTORS.forEach {
//                canvas.drawLine(
//                        faceLandmarkerResult.faceLandmarks().get(0).get(it!!.start()).x() * imageWidth * scaleFactor,
//                        faceLandmarkerResult.faceLandmarks().get(0).get(it.start()).y() * imageHeight * scaleFactor,
//                        faceLandmarkerResult.faceLandmarks().get(0).get(it.end()).x() * imageWidth * scaleFactor,
//                        faceLandmarkerResult.faceLandmarks().get(0).get(it.end()).y() * imageHeight * scaleFactor,
//                        linePaint)
//            }
//        }
        if (this.results != null) {
//            FaceLandmarkerResult var10000 = this.results;
//            Intrinsics.checkNotNull(var10000);
            Intrinsics.checkNotNull(this.results);
//            if (!var10000.faceLandmarks().isEmpty()) {
            if (!this.results.faceLandmarks().isEmpty()) {
//                var10000 = this.results;

//                if (var10000 != null) {
//                    FaceLandmarkerResult var2 = var10000;
//                    FaceLandmarkerResult faceLandmarkerResult = var2;
//                    int var4 = false;
//                    Optional var22 = null;
//                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
////                        var22 = var2.faceBlendshapes();
//                        var22 = this.results.faceBlendshapes();
//                    }
//                    Intrinsics.checkNotNullExpressionValue(var22, "faceLandmarkerResult.faceBlendshapes()");
                    Iterable $this$forEach$iv;
//                    boolean $i$f$forEach;
                    Iterator var7;
                    Object element$iv;
                    boolean var10;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//                        if (var22.isPresent()) {
                        if (this.results.faceBlendshapes().isPresent()) {
//                            Object var23 = var2.faceBlendshapes().get();
                            Object var23 = this.results.faceBlendshapes().get();
                            Intrinsics.checkNotNullExpressionValue(var23, "faceLandmarkerResult.faceBlendshapes().get()");
                            $this$forEach$iv = (Iterable)var23;
    //                        $i$f$forEach = false;
                            var7 = $this$forEach$iv.iterator();

                            while(true) {
                                if (!var7.hasNext()) {
                                    break;
                                }

                                element$iv = var7.next();
                                List it = (List)element$iv;
//                                var10 = false;
                                Intrinsics.checkNotNullExpressionValue(it, "it");
                                Iterable $this$forEach = (Iterable)it;
    //                            int $i$f$forEach = false;
                                Iterator var13 = $this$forEach.iterator();

                                while(var13.hasNext()) {
                                    Object element = var13.next();
                                    Category t = (Category)element;
    //                                int var16 = false;
                                    Log.e("Face Landmarker Overlay", t.displayName() + " " + t.score());
                                }
                            }
                        }
                    }

//                    Iterator var18 = var2.faceLandmarks().iterator();
                    Iterator var18 = this.results.faceLandmarks().iterator();

                    while(var18.hasNext()) {
                        List landmark = (List)var18.next();
                        Iterator var20 = landmark.iterator();

                        while(var20.hasNext()) {
                            NormalizedLandmark normalizedLandmark = (NormalizedLandmark)var20.next();
                            canvas.drawPoint(normalizedLandmark.x() * (float)this.imageWidth * this.scaleFactor, normalizedLandmark.y() * (float)this.imageHeight * this.scaleFactor, this.pointPaint);
                        }
                    }

                    Set var24 = FaceLandmarker.FACE_LANDMARKS_CONNECTORS;
                    Intrinsics.checkNotNullExpressionValue(var24, "FaceLandmarker.FACE_LANDMARKS_CONNECTORS");
                    Iterable forEachI = (Iterable)var24;
//                    $i$f$forEach = false;
                    var7 = forEachI.iterator();

                    while(var7.hasNext()) {
                        Object element = var7.next();
                        Connection it = (Connection)element;
//                        var10 = false;
                        List var10001 = (List) this.results.faceLandmarks().get(0);
                        Intrinsics.checkNotNull(it);
                        canvas.drawLine(((NormalizedLandmark)var10001.get(it.start())).x() * (float)this.imageWidth * this.scaleFactor, ((NormalizedLandmark)((List)this.results.faceLandmarks().get(0)).get(it.start())).y() * (float)this.imageHeight * this.scaleFactor, ((NormalizedLandmark)((List)this.results.faceLandmarks().get(0)).get(it.end())).x() * (float)this.imageWidth * this.scaleFactor, ((NormalizedLandmark)((List)this.results.faceLandmarks().get(0)).get(it.end())).y() * (float)this.imageHeight * this.scaleFactor, this.linePaint);
                    }
                }

                return;
            }else{
                this.clear();
            }
        }


//    }

    public void setResults(
            @NotNull FaceLandmarkerResult faceLandmarkerResults, int imageHeight, int imageWidth,
            @NotNull RunningMode runningMode) {
        // $FF: Couldn't be decompiled
    }

    // $FF: synthetic method
    public static void setResults$default(
            OverlayView var0, FaceLandmarkerResult var1, int var2, int var3,
            RunningMode var4, int var5, Object var6) {
        if ((var5 & 8) != 0) {
            var4 = RunningMode.IMAGE;
        }

        var0.setResults(var1, var2, var3, var4);
    }



//    public static final class Companion {
//        private Companion() {
//        }
//
//        // $FF: synthetic method
//        public Companion(DefaultConstructorMarker $constructor_marker) {
//            this();
//        }
//    }
}
