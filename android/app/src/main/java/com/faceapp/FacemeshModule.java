package com.faceapp;

import android.content.Intent;
import android.util.Log;

import androidx.camera.lifecycle.ProcessCameraProvider;

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

    ReactApplicationContext context;
    FacemeshModule(ReactApplicationContext context) {
        super(context);
        this.context = context;
    }

    @Override
    public String getName() {
        return "FacemeshModule";
    }

    @ReactMethod
    public void openCamera() {
        Log.d("FacemeshModule", "Create event called with");

        Intent intent = new Intent(context, CameraActivity.class);
        getCurrentActivity().startActivity(intent);

//        FaceMeshDetector defaultDetector =
//                FaceMeshDetection.getClient();

//        FaceMeshDetector boundingBoxDetector = FaceMeshDetection.getClient(
//                new FaceMeshDetectorOptions.Builder()
//                        .setUseCase(FaceMeshDetectorOptions.BOUNDING_BOX_ONLY)
//                        .build()
//        );

//        ProcessCameraProvider cameraProviderFuture = ProcessCameraProvider.getInstance(context);
    }
}
