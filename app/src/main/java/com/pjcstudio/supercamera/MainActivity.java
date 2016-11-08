package com.pjcstudio.supercamera;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.pjcstudio.supercamera.Dialog.ResolutionPickerDialog;
import com.pjcstudio.supercamera.Interface.DialogInterface;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frame, CameraFragment.createCameraFragment())
                .commit();
    }
}
