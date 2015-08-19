package com.adi.example.first;

import android.content.pm.PackageManager;

public class SystemFeature {
    protected PackageManager packageManager;

    public SystemFeature(PackageManager packageManager) {
        this.packageManager = packageManager;
    }

    public boolean hasSystemFeature(String featureCamera) {
        return this.packageManager.hasSystemFeature(featureCamera);
    }
}
