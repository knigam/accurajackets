package com.accurajackets.patientidentification.activity;

import java.io.BufferedOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.File;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.hardware.Camera.AutoFocusCallback;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore.Images.Media;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.accurajackets.patientidentification.R;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class CameraView extends Activity {
    private static final String TAG = "CameraView";

    private SurfaceView preview=null;
    private SurfaceHolder previewHolder=null;
    private Camera camera=null;
    private boolean inPreview=false;
    private Button takePicture;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.camera_view);
        takePicture = (Button)findViewById(R.id.take_picture);

        preview=(SurfaceView)findViewById(R.id.preview);
        previewHolder=preview.getHolder();
        previewHolder.addCallback(surfaceCallback);
        previewHolder
                .setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

        preview.setOnClickListener(new OnClickListener(){

            @Override
            public void onClick(View arg0) {
                takePicture.setEnabled(false);
                camera.autoFocus(myAutoFocusCallback);
            }

        });


        takePicture.setOnClickListener(new OnClickListener(){

            @Override
            public void onClick(View arg0) {

                camera.takePicture(null, null, photoCallback);
                inPreview=false;
            }

        });


    }

    @Override
    public void onResume() {
        super.onResume();
        try{
            if (camera==null) {
                camera=Camera.open();
                camera.setDisplayOrientation(90);
                Camera.Parameters params = camera.getParameters();
                params.set("rotation", 90);
                camera.setParameters(params);

            }
        }catch (Exception e){
            finish();
        }
    }

    @Override
    public void onPause() {
        if (inPreview) {
            camera.stopPreview();
        }
        if(camera != null)
            camera.release();
        camera=null;
        inPreview=false;

        super.onPause();
    }
//
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        new MenuInflater(this).inflate(R.menu.options, menu);
//
//        return(super.onCreateOptionsMenu(menu));
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        if (item.getItemId()==R.id.camera) {
//            if (inPreview) {
//                camera.takePicture(null, null, photoCallback);
//                inPreview=false;
//            }
//        }
//
//        return(super.onOptionsItemSelected(item));
//    }

    private Camera.Size getBestPreviewSize(int width,
                                           int height, Camera.Parameters parameters) {
        Camera.Size result=null;

        for (Camera.Size size : parameters.getSupportedPreviewSizes()) {
            if (size.width<=width && size.height<=height) {
                if (result==null) {
                    result=size;
                }
                else {
                    int resultArea=result.width*result.height;
                    int newArea=size.width*size.height;

                    if (newArea>resultArea) {
                        result=size;
                    }
                }
            }
        }

        return(result);
    }

    SurfaceHolder.Callback surfaceCallback=new SurfaceHolder.Callback() {
        public void surfaceCreated(SurfaceHolder holder) {
            try {
                camera.setPreviewDisplay(previewHolder);
            }
            catch (Throwable t) {
                Log.e("PictureDemo-surfaceCallback",
                        "Exception in setPreviewDisplay()", t);
                Toast.makeText(CameraView.this, t.getMessage(),
                        Toast.LENGTH_LONG).show();
            }
        }

        public void surfaceChanged(SurfaceHolder holder,
                                   int format, int width, int height) {
            Camera.Parameters parameters=camera.getParameters();

            parameters.setPictureFormat(PixelFormat.JPEG);

            camera.setParameters(parameters);
            camera.startPreview();

            inPreview=true;
        }

        public void surfaceDestroyed(SurfaceHolder holder) {
            // no-op
        }
    };

    Camera.PictureCallback photoCallback=new Camera.PictureCallback() {
        public void onPictureTaken(byte[] data, Camera camera) {

            try {
                File file = new File(getApplicationContext().getCacheDir(), "photo");
                BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
                bos.write(data);
                bos.flush();
                bos.close();

                postImage(file);

            } catch (FileNotFoundException e) {
                e.printStackTrace();

            } catch (IOException e) {
                e.printStackTrace();

            }
            setResult(RESULT_OK);
            finish();

        }
    };
    AutoFocusCallback myAutoFocusCallback = new AutoFocusCallback(){

        @Override
        public void onAutoFocus(boolean arg0, Camera arg1) {
            // TODO Auto-generated method stub
            takePicture.setEnabled(true);
        }};

    public void postImage(File file){
        RequestParams params = new RequestParams();
        params.put("picture[name]","MyPictureName");
        try {
            params.put("picture[image]", getApplicationContext().getCacheDir(), "photo");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return;
        }
        AsyncHttpClient client = new AsyncHttpClient();
        client.post("http://accurajackets.mymayfly.com/pictures", params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(String response) {
                System.out.println("SUCCESS");
                Log.w("async", "success!!!!");
            }
        });
    }
//
//    public String uploadUserPhoto(File image) throws FileNotFoundException, IOException {
//
//        Bitmap resized;
//
//
//        ByteArrayOutputStream bos = new ByteArrayOutputStream();
//        Bitmap.CompressFormat(Bitmap.CompressFormat.JPEG, 70, bos);
//
//        byte[] data = bos.toByteArray();
//
//        ByteArrayBody bab = new ByteArrayBody(data, "mobileimage.jpg");
//
//
//        DefaultHttpClient mHttpClient;
//        HttpParams params = new BasicHttpParams();
//        params.setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);
//        mHttpClient = new DefaultHttpClient(params);
//
//        try {
//            HttpPost httppost = new HttpPost("http://com.yourserver");
//
//            MultipartEntity multipartEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
//
//            multipartEntity.addPart("userfile", bab);
//
//            httppost.setEntity(multipartEntity);
//
//            HttpResponse response = mHttpClient.execute(httppost);
//            String responseBody = EntityUtils.toString(response.getEntity());
//
//            Log.d(TAG, "response: " + responseBody);
//            return responseBody;
//
//        } catch (Exception e) {
//            Log.d(TAG, e.getMessage());
//        }
//        return "";
//    }
}