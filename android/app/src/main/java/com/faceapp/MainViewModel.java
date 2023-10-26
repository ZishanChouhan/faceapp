package com.faceapp;

import androidx.lifecycle.ViewModel;

public final class MainViewModel extends ViewModel {
    private int _delegate;
    private float _minFaceDetectionConfidence = 0.5F;
    private float _minFaceTrackingConfidence = 0.5F;
    private float _minFacePresenceConfidence = 0.5F;
    private int _maxFaces = 1;

    public int getCurrentDelegate() {
        return this._delegate;
    }

    public float getCurrentMinFaceDetectionConfidence() {
        return this._minFaceDetectionConfidence;
    }

    public float getCurrentMinFaceTrackingConfidence() {
        return this._minFaceTrackingConfidence;
    }

    public float getCurrentMinFacePresenceConfidence() {
        return this._minFacePresenceConfidence;
    }

    public int getCurrentMaxFaces() {
        return this._maxFaces;
    }

    public void setDelegate(int delegate) {
        this._delegate = delegate;
    }

    public void setMinFaceDetectionConfidence(float confidence) {
        this._minFaceDetectionConfidence = confidence;
    }

    public void setMinFaceTrackingConfidence(float confidence) {
        this._minFaceTrackingConfidence = confidence;
    }

    public void setMinFacePresenceConfidence(float confidence) {
        this._minFacePresenceConfidence = confidence;
    }

    public void setMaxFaces(int maxResults) {
        this._maxFaces = maxResults;
    }
}

