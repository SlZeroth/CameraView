package com.pjcstudio.supercamera;

import android.content.Context;
import android.hardware.Camera;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

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
    private Context mContext;
    private List<Camera.Size> mSupportedPreviewSizes;
    private List<Camera.Size> mSupportedPhotoSizes;

    private Camera.Size mPreviewSize;

    public CameraView(Context context) {
        super(context);
        init(context);
    }

    public CameraView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public void init(Context context) {
        Log.d(TAG, "INIT");

        mContext = context;

        // 한번만 받아오기 위해서 먼저 초기값 null 로 초기화
        mPreviewSize = null;
        mSupportedPreviewSizes = null;
        mSupportedPhotoSizes = null;

        mHolder = getHolder();
        mHolder.addCallback(this);
        mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

    }

    public Camera getCamera() {
        return mCamera;
    }

    public void setCameraParameter() {

        // loading resolution
        if(CameraSetting.getInstance(mContext).isResolutionSet()) {
            setPhotoSize(CameraSetting.getInstance(mContext).getResolutionWidth(),
                    CameraSetting.getInstance(mContext).getResolutionHeight());
        }
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
        Log.d(TAG, "surfaceChanged => w=" + w + ", h=" + h);

        if (mHolder.getSurface() == null){
            return;
        }

        try {
            mCamera.stopPreview();
        } catch (Exception e){
            e.printStackTrace();
        }

        setCameraParameter();

        try {
            mCamera.setPreviewDisplay(mHolder);
            mCamera.startPreview();

        } catch (Exception e){
            e.printStackTrace();
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
        Log.d(TAG, "onMeasure");
        final int width = resolveSize(getSuggestedMinimumWidth(), widthMeasureSpec);
        final int height = resolveSize(getSuggestedMinimumHeight(), heightMeasureSpec);
        setMeasuredDimension(width, height);

        if(mPreviewSize != null) {

            int viewWidth = mPreviewSize.width;
            int viewHeight = mPreviewSize.height;

            Log.d("RESULT", "RESOLUTION : " + " width : " + viewWidth + " height : " + viewHeight);
            float ratio = (float) viewWidth / (float) viewHeight;
            Log.d("RESULT", "RATIO : " + ratio + "");
            setMeasuredDimension(viewWidth, viewHeight);
            Log.d("RESULT", "TO : width : " + (int)(height*ratio) + "" + " height : " + height);

        }
    }

    /**
     *
     * @param width Photo width
     * @param height Photo height
     */
    public void setPhotoSize(int width, int height) {
        Log.d(TAG, "setPhotoSize");

        Camera.Size bestSize = CameraMeasurement.getOptimalPreviewSize(mSupportedPreviewSizes, width, height);

        try {
            // Before call setParameter() Camera Object have to stopPreview()
            mCamera.stopPreview();
            Camera.Parameters parameters = mCamera.getParameters();
            parameters.setPreviewSize(bestSize.width, bestSize.height);
            mCamera.setParameters(parameters);
            mCamera.startPreview();
            mPreviewSize = bestSize;
            CameraSetting.getInstance(mContext).setResolution(width, height);
            requestLayout();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
