package com.example.coco.imageguru;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.FileProvider;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.coco.imageguru.Services.ImageProcessingService;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.zip.Inflater;

import Common.Constants;

public class FilteringActivity extends AppCompatActivity implements FilterFragment.OnFragmentInteractionListener,
                                                                    BinarizationFragment.OnFragmentInteractionListener,
                                                                    NoiseFragment.OnFragmentInteractionListener,
                                                                    ContoursFragment.OnFragmentInteractionListener,
                                                                    HomeFragment.OnFragmentInteractionListener
{

    private ImageView mMainView;
    private TextView mTextMessage;
    private FilterFragment filterFragment;
    private NoiseFragment  noiseFragment;
    private BinarizationFragment binFragment;
    private ContoursFragment contoursFragment;
    private HomeFragment homeFragment;

    Intent  mProcessIntent;
    Uri lastPhotoURI;
    String mCurrentPhotoPath;

    protected static final int CAPTURE_IMAGE_CODE = 1;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            switch (item.getItemId()) {
                case R.id.navigation_home:
                    mTextMessage.setText(R.string.title_home);
                    switchFragment(homeFragment);
                    return true;
                case R.id.navigation_sharpen:
                    mTextMessage.setText(R.string.title_dashboard);
                    switchFragment(filterFragment);
                    return true;
                case R.id.navigation_noise:
                    mTextMessage.setText(R.string.title_noise);
                    switchFragment(noiseFragment);
                    return true;
                case R.id.navigation_binarization:
                    mTextMessage.setText(R.string.title_binarization);
                    switchFragment(binFragment);
                    return true;
                case R.id.navigation_contours:
                    mTextMessage.setText(R.string.title_contours);
                    switchFragment(contoursFragment);
                    return true;
            }
            return false;
        }

    };

    private void switchFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void LaunchCamera() {

        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
                //...
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(FilteringActivity.this,
                        "com.example.coco.fileprovider",
                        photoFile);
                lastPhotoURI = photoURI;
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, CAPTURE_IMAGE_CODE);
            }
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if(requestCode == CAPTURE_IMAGE_CODE) {

            //use imageUri here to access the image

//            Bundle extras = data.getExtras();
//            Log.e("URI",lastPhotoURI.toString());
//            Bitmap bmp = (Bitmap) extras.get("data");

            try {
                Bitmap other  = MediaStore.Images.Media.getBitmap(getContentResolver(), lastPhotoURI);
                mMainView.setImageBitmap(other);
            } catch (IOException e) {
                e.printStackTrace();
            }


        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filtering);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mMainView    = (ImageView) findViewById(R.id.mainImageView);
        mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        if( findViewById(R.id.fragment_container)!=null) {

            if(savedInstanceState !=null) {
                return;
            }

            // Create all fragments
            homeFragment = new HomeFragment();
            noiseFragment = new NoiseFragment();
            binFragment   = new BinarizationFragment();
            contoursFragment = new ContoursFragment();
            filterFragment  = new FilterFragment();

            // Create the default fragment
            FilterFragment fragment = new FilterFragment();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_container, homeFragment).commit();
        }

        // The filter's action is BROADCAST_ACTION
        IntentFilter statusIntentFilter = new IntentFilter(
                Constants.BROADCAST_ACTION);

        // Instantiates a new DownloadStateReceiver
        ResultReceiver mResultReceiver =
                new ResultReceiver();
        // Registers the DownloadStateReceiver and its intent filters
        LocalBroadcastManager.getInstance(this).registerReceiver(
                mResultReceiver,
                statusIntentFilter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.simple_menu, menu);
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

        if (id == R.id.action_photo) {
            // Start new activity
            this.LaunchCamera();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void applyFilter(int kernel, int stdDeviation) {
        mProcessIntent = new Intent(this, ImageProcessingService.class);
        mProcessIntent.setData(lastPhotoURI);
        this.startService(mProcessIntent);
    }
}

// Broadcast receiver for receiving status updates from the IntentService
class ResultReceiver extends BroadcastReceiver
{
    // Prevents instantiation
    ResultReceiver() {
    }
    // Called when the BroadcastReceiver gets an Intent it's registered to receive
    @Override
    public void onReceive(Context context, Intent intent) {

        /*
         * Handle Intents here.
         */
        int code = 1;
        code = code * 5 + 5;
    }
}


