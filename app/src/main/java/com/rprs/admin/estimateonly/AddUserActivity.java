package com.rprs.admin.estimateonly;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.io.ByteArrayOutputStream;

public class AddUserActivity extends AppCompatActivity implements View.OnClickListener{

    Button done,delete;
    ImageView imageView,imageinfo;
    EditText editname,editnumber;
    Context context;
    DatabaseAccess databaseAccess;
    int type=1;//for check insert
    String Update_id;
    private AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_user);

        context=this;
        imageView=(ImageView)findViewById(R.id.backimgvi);
        imageinfo=(ImageView)findViewById(R.id.imageView3);
        done=(Button)findViewById(R.id.donebtn);
        delete=(Button)findViewById(R.id.button3);

        editname=(EditText)findViewById(R.id.editText);
        editnumber=(EditText)findViewById(R.id.editText2);
        mAdView = (AdView) findViewById(R.id.adView);
        banneraddShow();

        editnumber.setFilters(new InputFilter[]{new InputFilter.LengthFilter(10)});

        databaseAccess = DatabaseAccess.getInstance(context);
        imageView.setOnClickListener(this);
        done.setOnClickListener(this);
        delete.setOnClickListener(this);

        Intent intent = getIntent();
        String message="",mobile="";
        if (intent.hasExtra("name")) {
            Bundle b = intent.getExtras();
            Update_id = b.getString("id");
            message = b.getString("name");
            mobile = b.getString("mobile");
            editname.setText(message);
            editnumber.setText(mobile);
            delete.setVisibility(View.VISIBLE);
            type=2;//check for update
        } else { }

        imageinfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder=new AlertDialog.Builder(context);
                builder.setTitle("Info ");
                builder.setMessage(getString(R.string.alert_profile));
                builder.setNeutralButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });
    }

    @Override
    public void onClick(View v) {
        int id=v.getId();
        switch (id){
            case R.id.donebtn:
                if (editname.getText().toString().trim().length()<=0){
                    //Toast.makeText(context, "Please Enter name", Toast.LENGTH_SHORT).show();
                    editname.setError("Please Enter Name");
                    editname.requestFocus();
                }else if (editnumber.getText().toString().trim().length()<=0){
                    editnumber.setError("Please Enter Mobile Number");
                    editnumber.requestFocus();
                }else if (editnumber.getText().toString().trim().length()!=10){
                    editnumber.setError("Please Enter 10 digit Number");
                    editnumber.requestFocus();
                }
                else {
                    String name = editname.getText().toString();
                    String mobile = editnumber.getText().toString();
                    databaseAccess.open();
                    if (type==2)
                        databaseAccess.updateuser(name,mobile,Update_id);
                    else
                        databaseAccess.insertuser(name,mobile);
                    databaseAccess.close();
                    AddUserActivity.this.finish();
                }
                break;

            case R.id.button3:
                AlertDialog.Builder builder=new AlertDialog.Builder(context);
                builder.setTitle("You want to Delete this !");
                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        databaseAccess.open();
                        databaseAccess.deleteUser(Update_id);
                        databaseAccess.close();
                        AddUserActivity.this.finish();
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
                break;

            case R.id.backimgvi:
                AddUserActivity.this.finish();
                break;
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
