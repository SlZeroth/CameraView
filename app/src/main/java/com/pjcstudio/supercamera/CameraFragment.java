package com.pjcstudio.supercamera;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Random;

/**
 * Created by PJC on 2016-11-06.
 */

public class CameraFragment extends Fragment implements
        Camera.AutoFocusCallback,
        Camera.PictureCallback{

    public static CameraFragment createCameraFragment() {
        return new CameraFragment();
    }

    public CameraView cameraView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_camera, container, false);

        cameraView = (CameraView) rootView.findViewById(R.id.surface_cam);

        return rootView;
    }

    public void takePhoto() {
        cameraView.getCamera().takePicture(null, null, this);
    }

    @Override
    public void onAutoFocus(boolean b, Camera camera) {

    }

    @Override
    public void onPictureTaken(byte[] bytes, Camera camera) {

        Random rand = new Random();
        String fileName = rand.nextInt(100) + ".jpeg";
        File targetFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), fileName);

        Log.d("path", targetFile.getPath());
        Bitmap byteBitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);

        try {
            FileOutputStream fos = new FileOutputStream(targetFile);
            fos.write(bytes);
            fos.flush();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
