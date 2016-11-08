package com.pjcstudio.supercamera;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by PJC on 2016-11-06.
 */

public class CameraFragment extends Fragment {

    public static CameraFragment createCameraFragment() {
        return new CameraFragment();
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return new CameraView(getActivity());
    }

    public void init() {



    }
}
