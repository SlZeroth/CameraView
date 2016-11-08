package com.pjcstudio.supercamera;

import android.content.Context;
import android.hardware.Camera;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by PJC on 2016-11-07.
 */

public class CameraView extends SurfaceView implements SurfaceHolder.Callback {

    private static final String TAG = CameraView.class.getSimpleName();

    private SurfaceHolder mHolder;
    private Camera mCamera;
    private List<Camera.Size> mSupportedPreviewSizes;
    private List<Camera.Size> mSupportedPhotoSizes;

    public CameraView(Context context) {
        super(context);

        // 한번만 받아오기 위해서 먼저 초기값 null 로 초기화
        mSupportedPreviewSizes = null;
        mSupportedPhotoSizes = null;

        mHolder = getHolder();
        mHolder.addCallback(this);
        mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {

        Log.d(TAG, "surfaceCreated");

        mCamera = Camera.open();

        try {
            // 이미 사이즈를 받아온 경우는 안받도록 처리
            if(mSupportedPhotoSizes == null || mSupportedPreviewSizes == null) {
                mSupportedPhotoSizes = mCamera.getParameters().getSupportedPictureSizes();
                mSupportedPreviewSizes = mCamera.getParameters().getSupportedPreviewSizes();
            }
            mCamera.setPreviewDisplay(surfaceHolder);
        } catch (IOException e) {
            mCamera.release();
            mCamera = null;
            e.printStackTrace();
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int format, int w, int h) {
        Log.e(TAG, "surfaceChanged => w=" + w + ", h=" + h);

        mCamera.startPreview();

        if (mHolder.getSurface() == null){
            return;
        }

        if (mHolder.getSurface() == null){
            // preview surface does not exist
            return;
        }

        try {
            Camera.Parameters parameters = mCamera.getParameters();
            parameters.setPreviewSize(CameraSetting.mPreviewSize.width, CameraSetting.mPreviewSize.height);
            mCamera.setParameters(parameters);
            mCamera.setDisplayOrientation(90);
            mCamera.setPreviewDisplay(mHolder);
            mCamera.startPreview();

        } catch (Exception e){
            Log.d(TAG, "Error starting camera preview: " + e.getMessage());
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        Log.d(TAG, "surfaceDestroyed");
        mCamera.stopPreview();
        mCamera.release();
        mCamera = null;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        final int width = resolveSize(getSuggestedMinimumWidth(), widthMeasureSpec);
        final int height = resolveSize(getSuggestedMinimumHeight(), heightMeasureSpec);

        if (mSupportedPreviewSizes != null) {
            mPreviewSize = getOptimalPreviewSize(mSupportedPreviewSizes, width, height);
        }

        float ratio;
        if(mPreviewSize.height >= mPreviewSize.width)
            ratio = (float) mPreviewSize.height / (float) mPreviewSize.width;
        else
            ratio = (float) mPreviewSize.width / (float) mPreviewSize.height;

        // One of these methods should be used, second method squishes preview slightly
        setMeasuredDimension(width, (int) (width * ratio));
        //setMeasuredDimension((int) (width * ratio), height);
    }

    public List<Camera.Size> getSupportPreviewSize() {
        List<Camera.Size> arrSupportPreview =mCamera.getParameters().getSupportedPreviewSizes();

        for(Camera.Size size : arrSupportPreview) {
            Log.d("support", "width : " + size.width + " heigth : " + size.height);
        }

        return arrSupportPreview;
    }

    public List<Camera.Size> getSupportPhotoSize() {
        List<Camera.Size> arrSupportPreview =mCamera.getParameters().getSupportedPictureSizes();

        for(Camera.Size size : arrSupportPreview) {
            Log.d("support", "width : " + size.width + " heigth : " + size.height);
        }

        return arrSupportPreview;
    }

    private Camera.Size getOptimalPreviewSize(List<Camera.Size> sizes, int w, int h) {
        final double ASPECT_TOLERANCE = 0.05;
        double targetRatio = (double) w/h;

        if (sizes==null) return null;

        Camera.Size optimalSize = null;

        double minDiff = Double.MAX_VALUE;

        int targetHeight = h;

        // Find size
        for (Camera.Size size : sizes) {
            double ratio = (double) size.width / size.height;
            if (Math.abs(ratio - targetRatio) > ASPECT_TOLERANCE) continue;
            if (Math.abs(size.height - targetHeight) < minDiff) {
                optimalSize = size;
                minDiff = Math.abs(size.height - targetHeight);
            }
        }

        if (optimalSize == null) {
            minDiff = Double.MAX_VALUE;
            for (Camera.Size size : sizes) {
                if (Math.abs(size.height - targetHeight) < minDiff) {
                    optimalSize = size;
                    minDiff = Math.abs(size.height - targetHeight);
                }
            }
        }
        return optimalSize;
    }
}
