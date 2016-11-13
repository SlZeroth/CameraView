package com.pjcstudio.supercamera;

import android.app.Application;
import android.hardware.Camera;
import android.util.Log;

import java.util.List;

/**
 * Created by pjc on 2016. 11. 8..
 */

public class CameraApplication extends Application {

    private static final String TAG = "APP";

    private List<Camera.Size> appSupportPhotoSize;

    @Override
    public void onCreate() {
        super.onCreate();

        try {
            Camera camera = Camera.open();
            appSupportPhotoSize = camera.getParameters().getSupportedPictureSizes();
            camera.release();
            camera = null;
        } catch (RuntimeException e) {
            e.printStackTrace();
        }
    }

    public List<Camera.Size> getSupportedPictureSizes() {
        return appSupportPhotoSize;
    }

}
