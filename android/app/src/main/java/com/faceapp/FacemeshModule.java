package com.faceapp;

import android.util.Log;

import com.facebook.react.bridge.NativeModule;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.google.mlkit.vision.facemesh.FaceMeshDetection;
import com.google.mlkit.vision.facemesh.FaceMeshDetector;
import com.google.mlkit.vision.facemesh.FaceMeshDetectorOptions;

import java.util.Map;
import java.util.HashMap;

public class FacemeshModule extends ReactContextBaseJavaModule {
    FacemeshModule(ReactApplicationContext context) {
        super(context);
    }

    @Override
    public String getName() {
        return "FacemeshModule";
    }

    @ReactMethod
    public void openCamera() {
        Log.d("FacemeshModule", "Create event called with");

//        FaceMeshDetector defaultDetector =
//                FaceMeshDetection.getClient();

        FaceMeshDetector boundingBoxDetector = FaceMeshDetection.getClient(
                new FaceMeshDetectorOptions.Builder()
                        .setUseCase(FaceMeshDetectorOptions.BOUNDING_BOX_ONLY)
                        .build()
        );
    }
}
