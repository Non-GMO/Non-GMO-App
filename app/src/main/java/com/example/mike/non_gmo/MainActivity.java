package com.example.mike.non_gmo;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.CaptureResult;

import java.io.IOException;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    // access the camera
    public static Camera getCamera()
    {
        Camera cam = null;
        try
        {
            cam.Camera.open();
        }
        catch (Exception e)
        {
            // camera not available
        }
        return cam;  // if not availible
    }

    // camera preview
    public class CameraPreview extends SurfaceView implements SurfaceHolder.Callback
    {
        private SurfaceHolder holder;
        private Camera cam;

        public CameraPreview(Context context, Camera cam) {
            super(context);
            cam = camera;

            // surfaceHolder
            holder = getHolder();
            holder.addCallback(this);
            holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        }

        public void surfaceCreate(SurfaceHolder holder)
        {
            try {
                cam.setPreviewDisplay(holder);
                cam.startPreview();
            }
            catch (IOException e)
            {
                Log.d(TAG, "Camera Error: " + e.getMessage());
            }
        }
        public void surfaceChange(SurfaceHolder holder, int format, int w, int h)
        {
            if (holder.getSurface() == null)
            {
                return;
            }
            try
            {
                cam.stopPreview();
            }
            catch (Exception e)
            {
            }
            try
            {
                cam.setPreviewDisplay(holder);
                cam.startPreview();
            }
            catch (Exception e)
            {
                Log.d(TAG, "Camera Error: " + e.getMessage());
            }
        }
    }
    public class CameraActivity extends Activity
    {

        private Camera cam;
        private CameraPreview Prev;

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.main);

            // Create an instance of Camera
            cam = getCameraInstance();

            // Create our Preview view and set it as the content of our activity.
            Prev = new CameraPreview(this, cam);
            FrameLayout preview = (FrameLayout) findViewById(R.id.camera_preview);
            preview.addView(Prev);
        }
    }

    private PictureCallback mPicture = new PictureCallback() {

        @Override
        public void onPictureTaken(byte[] data, Camera camera) {

            File pictureFile = getOutputMediaFile(MEDIA_TYPE_IMAGE);
            if (pictureFile == null){
                Log.d(TAG, "Error creating media file, check storage permissions: " +
                        e.getMessage());
                return;
            }

            try {
                FileOutputStream fos = new FileOutputStream(pictureFile);
                fos.write(data);
                fos.close();
            } catch (FileNotFoundException e) {
                Log.d(TAG, "File not found: " + e.getMessage());
            } catch (IOException e) {
                Log.d(TAG, "Error accessing file: " + e.getMessage());
            }
        }
    };


}
