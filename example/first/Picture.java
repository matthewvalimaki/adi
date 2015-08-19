package com.adi.example.first;

import android.content.pm.PackageManager;

public class Picture {
    static final int REQUEST_IMAGE_CAPTURE = 1;

    protected SystemFeature systemFeature;

    public Picture(SystemFeature systemFeature) {
        this.systemFeature = systemFeature;
    }

    public boolean isCameraAvailable() {
        return this.systemFeature.hasSystemFeature(PackageManager.FEATURE_CAMERA);
    }

    public void dispatchTakePictureIntent() {

    }
}
