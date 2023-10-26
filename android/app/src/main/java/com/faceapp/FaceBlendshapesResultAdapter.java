package com.faceapp;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.faceapp.databinding.FaceBlendshapesResultBinding;
import com.google.mediapipe.tasks.components.containers.Category;
import com.google.mediapipe.tasks.vision.facelandmarker.FaceLandmarkerResult;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import kotlin.Metadata;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
//import kotlin.jvm.internal.SourceDebugExtension;
//import kotlin.jvm.internal.StringCompanionObject;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


public final class FaceBlendshapesResultAdapter extends RecyclerView.Adapter {
    private List categories;
    private static final String NO_VALUE = "--";
    @NotNull
//    public static final Companion Companion = new Companion((DefaultConstructorMarker)null);

    public void updateResults(@Nullable FaceLandmarkerResult faceLandmarkerResult) {
        Log.d("faceLandmarkerResult", String.valueOf(faceLandmarkerResult));
        byte var2 = 52;
        ArrayList var3 = new ArrayList(var2);

        int i;
        for(i = 0; i < var2; ++i) {
//            int var7 = false;
            Object var10 = null;
            var3.add(var10);
        }

        List var9 = (List)var3;
        this.categories = var9;
        if (faceLandmarkerResult != null) {
            Object var10000 = ((List)faceLandmarkerResult.faceBlendshapes().get()).get(0);
            Intrinsics.checkNotNullExpressionValue(var10000, "faceLandmarkerResult.faceBlendshapes().get()[0]");
            Iterable $this$sortedBy$iv = (Iterable)var10000;
//            int $i$f$sortedBy = false;
            List sortedCategories = CollectionsKt.sortedWith($this$sortedBy$iv, (Comparator)(new FaceBlendshapesResultAdapter$updateResults$$inlined$sortedBy$1()));
            i = sortedCategories.size();
            int var5 = this.categories.size();
            int min = Math.min(i, var5);
            i = 0;

            for(var5 = min; i < var5; ++i) {
                this.categories.set(i, sortedCategories.get(i));
            }
        }

    }

    // $FF: synthetic method
    public static void updateResults$default(FaceBlendshapesResultAdapter var0, FaceLandmarkerResult var1, int var2, Object var3) {
        if ((var2 & 1) != 0) {
            var1 = null;
        }

        var0.updateResults(var1);
    }

    @NotNull
    public ViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        Intrinsics.checkNotNullParameter(parent, "parent");
        FaceBlendshapesResultBinding var10000 = FaceBlendshapesResultBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        Intrinsics.checkNotNullExpressionValue(var10000, "FaceBlendshapesResultBin…          false\n        )");
        FaceBlendshapesResultBinding binding = var10000;
        return new ViewHolder(binding);
    }

    // $FF: synthetic method
    // $FF: bridge method
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup var1, int var2) {
        return (RecyclerView.ViewHolder)this.onCreateViewHolder(var1, var2);
    }

    public void onBindViewHolder(@NotNull ViewHolder holder, int position) {
        Intrinsics.checkNotNullParameter(holder, "holder");
        Object var3 = this.categories.get(position);
        Category category = (Category)var3;
        int var5 = false;
        holder.bind(category != null ? category.categoryName() : null, category != null ? category.score() : null);
    }

    // $FF: synthetic method
    // $FF: bridge method
    public void onBindViewHolder(RecyclerView.ViewHolder var1, int var2) {
        this.onBindViewHolder((ViewHolder)var1, var2);
    }

    public int getItemCount() {
        return this.categories.size();
    }

    public FaceBlendshapesResultAdapter() {
        byte var1 = 52;
        ArrayList var2 = new ArrayList(var1);

        for(int var3 = 0; var3 < var1; ++var3) {
            int var6 = false;
            Object var9 = null;
            var2.add(var9);
        }

        List var8 = (List)var2;
        this.categories = var8;
    }

    @Metadata(
            mv = {1, 7, 0},
            k = 1,
            d1 = {"\u0000&\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u0007\n\u0002\b\u0002\b\u0086\u0004\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003¢\u0006\u0002\u0010\u0004J\u001f\u0010\u0005\u001a\u00020\u00062\b\u0010\u0007\u001a\u0004\u0018\u00010\b2\b\u0010\t\u001a\u0004\u0018\u00010\n¢\u0006\u0002\u0010\u000bR\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004¢\u0006\u0002\n\u0000¨\u0006\f"},
            d2 = {"Lcom/google/mediapipe/examples/facelandmarker/fragment/FaceBlendshapesResultAdapter$ViewHolder;", "Landroidx/recyclerview/widget/RecyclerView$ViewHolder;", "binding", "Lcom/google/mediapipe/examples/facelandmarker/databinding/FaceBlendshapesResultBinding;", "(Lcom/google/mediapipe/examples/facelandmarker/fragment/FaceBlendshapesResultAdapter;Lcom/google/mediapipe/examples/facelandmarker/databinding/FaceBlendshapesResultBinding;)V", "bind", "", "label", "", "score", "", "(Ljava/lang/String;Ljava/lang/Float;)V", "app_debug"}
    )
    public final class ViewHolder extends RecyclerView.ViewHolder {
        private final FaceBlendshapesResultBinding binding;

        public final void bind(@Nullable String label, @Nullable Float score) {
            FaceBlendshapesResultBinding var3 = this.binding;
            int var5 = false;
            TextView var10000 = var3.tvLabel;
            Intrinsics.checkNotNullExpressionValue(var10000, "tvLabel");
            var10000.setText(label != null ? (CharSequence)label : (CharSequence)"--");
            var10000 = var3.tvScore;
            Intrinsics.checkNotNullExpressionValue(var10000, "tvScore");
            CharSequence var9;
            if (score != null) {
                StringCompanionObject var6 = StringCompanionObject.INSTANCE;
                String var7 = "%.2f";
                Object[] var8 = new Object[]{score};
                String var10001 = String.format(var7, Arrays.copyOf(var8, var8.length));
                Intrinsics.checkNotNullExpressionValue(var10001, "format(format, *args)");
                var9 = (CharSequence)var10001;
            } else {
                var9 = (CharSequence)"--";
            }

            var10000.setText(var9);
        }

        public ViewHolder(@NotNull FaceBlendshapesResultBinding binding) {
            Intrinsics.checkNotNullParameter(binding, "binding");
            super((View)binding.getRoot());
            this.binding = binding;
        }
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

