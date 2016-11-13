package com.pjcstudio.supercamera;

import android.hardware.Camera;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;

import com.pjcstudio.supercamera.Dialog.ResolutionPickerDialog;
import com.pjcstudio.supercamera.Interface.DialogInterface;
import com.pjcstudio.supercamera.Interface.DialogResolutionInterface;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    private CameraFragment cameraFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        cameraFragment = CameraFragment.createCameraFragment();

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frame, cameraFragment)
                .commit();

    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {

        if(keyCode == KeyEvent.KEYCODE_CAMERA) {
            cameraFragment.takePhoto();
        }

        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onBackPressed() {
        ArrayList<String> resolutionList = new ArrayList<String>();

        List<Camera.Size> sizeList = ((CameraApplication)getApplication()).getSupportedPictureSizes();
        for(Camera.Size sizeItem : sizeList) {
            resolutionList.add(sizeItem.width + " * " + sizeItem.height);
        }

        ResolutionPickerDialog.createResolutionDialog(this, resolutionList, new DialogResolutionInterface() {
            @Override
            public void onSuccess(int width, int height) {
                cameraFragment.cameraView.setPhotoSize(width, height);
            }

            @Override
            public void onCancel() {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId())
        {
            case R.id.action_resolution:

                ArrayList<String> resolutionList = new ArrayList<String>();

                List<Camera.Size> sizeList = ((CameraApplication)getApplication()).getSupportedPictureSizes();
                for(Camera.Size sizeItem : sizeList) {
                    resolutionList.add(sizeItem.width + " * " + sizeItem.height);
                }

                ResolutionPickerDialog.createResolutionDialog(this, resolutionList, new DialogResolutionInterface() {
                    @Override
                    public void onSuccess(int width, int height) {
                        cameraFragment.cameraView.setPhotoSize(width, height);
                    }

                    @Override
                    public void onCancel() {

                    }
                });
                break;

            case R.id.action_takephoto:
                cameraFragment.takePhoto();
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}
