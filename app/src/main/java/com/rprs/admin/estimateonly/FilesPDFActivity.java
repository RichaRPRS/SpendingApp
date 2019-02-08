package com.rprs.admin.estimateonly;

import android.Manifest;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.io.File;
import java.util.ArrayList;

public class FilesPDFActivity extends AppCompatActivity {

    String path;
    ListView listView1;
    Context context;
    Activity activity;
    private static final int PERMISSION_REQUEST_CODE = 1;

    ArrayList<String> filenames;

    private AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_files_pdf);

        listView1 = (ListView) findViewById(R.id.listviewfiles);
        context=this;
        activity=this;

        if (checkPermission()){
            showAllPDF();
        }else {
            requestPermission();
        }

        mAdView = (AdView) findViewById(R.id.adView);
        banneraddShow();

        listView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String namefile=filenames.get(position);
                //Toast.makeText(context,path+File.separator+namefile, Toast.LENGTH_SHORT).show();
                String mFilePath=Environment.getExternalStorageDirectory().getAbsolutePath()+File.separator+"Estimate"+File.separator+namefile;

                Intent intent;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    File file=new File(mFilePath);
                    Uri uri = FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID+".provider", file);
                    intent = new Intent(Intent.ACTION_VIEW);
                    intent.setDataAndType(uri, "application/pdf");
                    intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    try {
                        startActivity(intent);
                    } catch (ActivityNotFoundException e) {
                        Toast.makeText(context, "No App available to open this file", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    intent = new Intent(Intent.ACTION_VIEW);
                    intent.setDataAndType(Uri.parse(mFilePath), "application/pdf");
                    intent = Intent.createChooser(intent, "Open With");
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    try {
                        startActivity(intent);
                    } catch (ActivityNotFoundException e) {
                        Toast.makeText(context, "No App available to open this file", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    public void showAllPDF(){
        filenames = new ArrayList<String>();
        path = Environment.getExternalStorageDirectory()
                + File.separator + "Estimate";

        File directory = new File(path);
        File[] files = directory.listFiles();

        try {
            for (int i = 0; i < files.length; i++)
            {
                String file_name = files[i].getName();
                if (files[i].isFile()) {
                    // you can store name to arraylist and use it later
                    filenames.add(file_name);
                }
            }
        }catch (Exception e){
            Toast.makeText(context, "No Files Available", Toast.LENGTH_SHORT).show();
        }


        ArrayAdapter<String> adapter ;

        if (filenames.size()>0){
            adapter = new ArrayAdapter<String>(this,
                    R.layout.listview_layout,R.id.txt, filenames);
            listView1.setAdapter(adapter);
        }
    }

    private boolean checkPermission(){
        int result = ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (result == PackageManager.PERMISSION_GRANTED){
            return true;
        } else {
            return false;
        }
    }

    private void requestPermission(){
        ActivityCompat.requestPermissions(activity,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},PERMISSION_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //Toast.makeText(context, "Granted", Toast.LENGTH_SHORT).show();
                    showAllPDF();
                } else {   // permission denied, boo! Disable the
                    Toast.makeText(context, "Denied", Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }
    }

    public void banneraddShow(){
        AdRequest adRequest = new AdRequest.Builder()
                //.addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                // Check the LogCat to get your test device ID
                //.addTestDevice("B3EEABB8EE11C2BE770B684D95219ECB")
                //.addTestDevice("B3EEABB8EE11C2BE770B684D95219ECB")
                .build();
        //Log.d("Emulator id",AdRequest.DEVICE_ID_EMULATOR);
        mAdView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                //Toast.makeText(context, "Add started", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAdClosed() {
                //Toast.makeText(context, "Ad is closed!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                //Toast.makeText(context, "Ad failed to load! error code: " + errorCode, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAdLeftApplication() {
                //Toast.makeText(context, "Ad left application!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAdOpened() {
                super.onAdOpened();
            }
        });
        mAdView.loadAd(adRequest);
    }

}
