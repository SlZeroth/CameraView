package com.pjcstudio.supercamera;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Size;

import com.pjcstudio.supercamera.Model.Dimension;

/**
 * Created by PJC on 2016-11-07.
 */

public class CameraSetting {


    // Singletoon Instance
    private static CameraSetting cameraSetting = null;
    private Context mContext;

    // 설정한 사진 해상도
    private Dimension resolution = null;

    private static final String DEFAULTCAMSETTING = "CAMERASETTING";
    private static final String RESOLUTION = "RESOLUTION";

    public static CameraSetting getInstance(Context context) {
        if(cameraSetting == null) {
            cameraSetting = new CameraSetting(context);
            cameraSetting.loadSettingData();
            return cameraSetting;
        } else {
            return cameraSetting;
        }
    }

    private CameraSetting(Context context) {
        mContext = context;
    }

    private void loadSettingData() {

        SharedPreferences pref = mContext.getSharedPreferences(DEFAULTCAMSETTING, Context.MODE_PRIVATE);
        String getStringData = null;

        // LOAD RESOLUTION
        getStringData = pref.getString(RESOLUTION, "");
        if(!getStringData.equals("")) {

            String[] splitResolution = getStringData.split(" * ");

            int height = Integer.parseInt(splitResolution[2]);
            int width = Integer.parseInt(splitResolution[0]);
            resolution = new Dimension(width, height);
        }


    }

    public Boolean isResolutionSet() {
        if(resolution != null) {
            return true;
        } else {
            return false;
        }
    }

    public int getResolutionWidth() {
        if(resolution != null) {
            return resolution.getWidth();
        } else {
            return 0;
        }
    }

    public int getResolutionHeight() {
        if(resolution != null) {
            return resolution.getHeight();
        } else {
            return 0;
        }
    }

    public int setResolution(int width, int height) {

        if(width >= 0 && height >= 0) {
            resolution = new Dimension(width, height);
            SharedPreferences.Editor editor = getSharedPreferencesEditor();
            editor.putString(RESOLUTION, width + " * " + height);
            editor.commit();
            return 1;
        } else {
            return 0;
        }

    }

    private SharedPreferences.Editor getSharedPreferencesEditor() {
        SharedPreferences pref = mContext.getSharedPreferences(DEFAULTCAMSETTING, Context.MODE_PRIVATE);
        return pref.edit();
    }
}
