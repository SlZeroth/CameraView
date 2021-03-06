package com.pjcstudio.supercamera.Dialog;

import android.content.Context;
import android.content.DialogInterface;
import android.hardware.Camera;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.pjcstudio.supercamera.Interface.DialogResolutionInterface;

import java.util.ArrayList;

/**
 * Created by PJC on 2016-11-07.
 */

public class ResolutionPickerDialog {

    public static void createResolutionDialog(Context context, ArrayList<String> itemString, final DialogResolutionInterface resolutionInterface) {

        final String[] arr = itemString.toArray(new String[itemString.size()]);

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("촬영해상도 선택");
        builder.setItems(arr, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                String[] splitResolution = arr[item].split(" * ");
                resolutionInterface.onSuccess(Integer.valueOf(splitResolution[0]), Integer.valueOf(splitResolution[2]));
            }
        });

        AlertDialog alert = builder.create();
        alert.show();
    }
}
