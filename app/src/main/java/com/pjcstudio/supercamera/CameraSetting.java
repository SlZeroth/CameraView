package com.pjcstudio.supercamera;

import android.hardware.Camera;

/**
 * Created by PJC on 2016-11-07.
 */

public class CameraSetting {

    // Singletoon Instance
    private static CameraSetting cameraSetting = null;

    public static Camera.Size mPreviewSize;

    public static CameraSetting getInstance() {
        if(cameraSetting == null) {
            cameraSetting = new CameraSetting();
            return cameraSetting;
        } else {
            return cameraSetting;
        }
    }

    private void loadSettingData() {

    }
}
