package com.nilscreation.billionairedreams;

import static android.os.Environment.DIRECTORY_PICTURES;

import android.app.DownloadManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import com.bumptech.glide.Glide;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;


import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

public class DetailActivity extends AppCompatActivity {

    ImageView imageView;
    String mPoster;
    private AdView mAdView;
    String[] permission = {"android.permission.WRITE_EXTERNAL_STORAGE", "android.permission.READ_EXTERNAL_STORAGE"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        imageView = findViewById(R.id.photoView);

        Bundle bundle = getIntent().getExtras();
        mPoster = bundle.getString("url");
        Glide.with(this).load(mPoster).into(imageView);

        ImageView shareWallpaperbtn = findViewById(R.id.share_wallpaper);
        shareWallpaperbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                BitmapDrawable bitmapDrawable = (BitmapDrawable) imageView.getDrawable();
                Bitmap bitmap = bitmapDrawable.getBitmap();
                shareImageandText(bitmap);

            }
        });

        ImageView downloadWallpaperbtn = findViewById(R.id.download_wallpaper);
        downloadWallpaperbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    saveImageToMediaStore();
                } else {
                    requestPermissions(permission, 80);
                }

            }
        });

        ImageView backbutton = findViewById(R.id.backbutton);
        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

    }

//    private void saveImageToMediaStore() {
//        ContentValues values = new ContentValues();
//        values.put(MediaStore.Images.Media.DISPLAY_NAME, "Billionaire_Dreams_" + System.currentTimeMillis() + ".jpg");
//        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
//        values.put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/Billionaire Dreams");
//
//        Uri uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
//        BitmapDrawable bitmapDrawable = (BitmapDrawable) imageView.getDrawable();
//        Bitmap bitmap = bitmapDrawable.getBitmap();
//
//        try {
//            if (uri != null) {
//                OutputStream outputStream = getContentResolver().openOutputStream(uri);
//                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
//                if (outputStream != null) {
//                    outputStream.close();
//                }
//                Toast.makeText(this, "Image Downloaded successfully", Toast.LENGTH_SHORT).show();
//            } else {
//                Toast.makeText(this, "Error Downloading image", Toast.LENGTH_SHORT).show();
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            Toast.makeText(this, "Error Downloading image: " + e.getMessage(), Toast.LENGTH_SHORT).show();
//        }
//    }

    private void saveImageToMediaStore() {
        BitmapDrawable bitmapDrawable = (BitmapDrawable) imageView.getDrawable();

        // Check if the drawable is null
        if (bitmapDrawable == null) {
            Toast.makeText(this, "Error: No image to save", Toast.LENGTH_SHORT).show();
            return;
        }

        Bitmap bitmap = bitmapDrawable.getBitmap();

        // Check if the Bitmap is valid
        if (bitmap == null) {
            Toast.makeText(this, "Error: Unable to retrieve image", Toast.LENGTH_SHORT).show();
            return;
        }

        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.DISPLAY_NAME, "Billionaire_Dreams_" + System.currentTimeMillis() + ".jpg");
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
        values.put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/Billionaire Dreams");

        Uri uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

        try {
            if (uri != null) {
                OutputStream outputStream = getContentResolver().openOutputStream(uri);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);

                if (outputStream != null) {
                    outputStream.close();
                }
                Toast.makeText(this, "Image Downloaded successfully", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Error Downloading image", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Error Downloading image: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void shareImageandText(Bitmap bitmap) {
        Uri uri = getImageToShare(bitmap);
        Intent intent = new Intent(Intent.ACTION_SEND);

        // putting uri of image to be shared
        intent.putExtra(Intent.EXTRA_STREAM, uri);

        // adding text to share
        intent.putExtra(Intent.EXTRA_TEXT, "For More Motivational quotes download the app now " + "\nhttps://play.google.com/store/apps/details?id=" + getApplicationContext().getPackageName());

        // Add subject Here
        intent.putExtra(Intent.EXTRA_SUBJECT, "Subject Here");

        // setting type to image
        intent.setType("image/png");

        // calling startactivity() to share
        startActivity(Intent.createChooser(intent, "Share Via"));
    }

    // Retrieving the url to share
    private Uri getImageToShare(Bitmap bitmap) {
        File imagefolder = new File(getCacheDir(), "images");
        Uri uri = null;
        try {
            imagefolder.mkdirs();
            File file = new File(imagefolder, "Billionaire_Dreams_Quote.jpg");
            FileOutputStream outputStream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
            outputStream.flush();
            outputStream.close();
            uri = FileProvider.getUriForFile(this, "com.nilscreation.billionairedreams", file);
        } catch (Exception e) {
            Toast.makeText(this, "" + e.getMessage(), Toast.LENGTH_LONG).show();
        }
        return uri;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 80) {
            // Check if the grantResults array is not empty
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                BitmapDrawable bitmapDrawable = (BitmapDrawable) imageView.getDrawable();
                Bitmap bitmap = bitmapDrawable.getBitmap();

                try {
                    DownloadManager downloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
                    Uri uri = Uri.parse(mPoster);
                    DownloadManager.Request request = new DownloadManager.Request(uri);

                    request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE)
                            .setAllowedOverRoaming(false)
                            .setTitle("Billionaire_Dreams_" + System.currentTimeMillis() + ".jpg")
                            .setMimeType("image/jpeg")
                            .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                            .setDestinationInExternalPublicDir(DIRECTORY_PICTURES, "Billionaire_Dreams_" + System.currentTimeMillis() + ".jpg");

                    downloadManager.enqueue(request);

                    Toast.makeText(getApplicationContext(), "Download Started", Toast.LENGTH_SHORT).show();

                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(this, "Save Failed", Toast.LENGTH_SHORT).show();
                }

            } else {
                // Permission was denied
                Toast.makeText(this, "Permission denied, download canceled", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
