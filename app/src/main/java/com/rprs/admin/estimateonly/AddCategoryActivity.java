package com.rprs.admin.estimateonly;

import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.io.ByteArrayOutputStream;

public class AddCategoryActivity extends AppCompatActivity implements View.OnClickListener{

    ImageView imageView,iconimg,imageinfo;
    Button done,delete;
    TextView tvcateg;
    EditText editname;

    Context context;
    DatabaseAccess databaseAccess;
    int type=1;//for check insert
    String update_id;
    int type_id=1;
    private AdView mAdView;

    GridView androidGridView;
    Integer[] imageIDs = {
            R.raw.airplane, R.raw.anchor, R.raw.baby,
            R.raw.ball, R.raw.balloon, R.raw.bandaid,
            R.raw.beer_glass, R.raw.bicycle, R.raw.bomb,
            R.raw.bone, R.raw.book, R.raw.bow_and_arrow,
            R.raw.bowtie, R.raw.broadcast, R.raw.building,
            R.raw.bus, R.raw.cabinet, R.raw.camera,
            R.raw.car, R.raw.cards, R.raw.chicken,
            R.raw.city, R.raw.clapboard, R.raw.cleaver,
            R.raw.cloud, R.raw.coffee, R.raw.credit_card,
            R.raw.crown, R.raw.currency, R.raw.dashboard,
            R.raw.dice, R.raw.display, R.raw.dog_paw,
            R.raw.envelope, R.raw.film_roll, R.raw.fish,
            R.raw.flower, R.raw.food, R.raw.fork_and_knife,R.raw.fuel,
            R.raw.gavel, R.raw.gba, R.raw.gift,
            R.raw.gradhat, R.raw.guitar, R.raw.heart,
            R.raw.house, R.raw.iphone, R.raw.jeep, R.raw.lamp,
            R.raw.lightbulb, R.raw.medical, R.raw.microphone,
            R.raw.movie_2, R.raw.piggy_bank, R.raw.pizza,
            R.raw.purse, R.raw.runner, R.raw.shoebox,
            R.raw.speaker, R.raw.suitcase, R.raw.tag,
            R.raw.toolbox, R.raw.tshirt, R.raw.wifi, R.raw.wine_bottle,
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_category);

        context=this;
        imageView=(ImageView)findViewById(R.id.backimgvi);
        imageinfo=(ImageView)findViewById(R.id.imageView3);
        iconimg=(ImageView)findViewById(R.id.iconimg);
        done=(Button)findViewById(R.id.donebtn);
        delete=(Button)findViewById(R.id.button3);

        tvcateg=(TextView)findViewById(R.id.textView7);
        editname=(EditText)findViewById(R.id.editText);

        Intent intent = getIntent();
        String message="";
        if (intent.hasExtra("name")) {
            Bundle b = intent.getExtras();
            update_id= b.getString("id");
            message = b.getString("name");
            byte [] img=b.getByteArray("image");
            type_id=b.getInt("type_id");
            editname.setText(message);
            delete.setVisibility(View.VISIBLE);
            try {
                Bitmap b1 = BitmapFactory.decodeByteArray(img, 0, img.length);
                iconimg.setImageBitmap(b1);
            }catch (Exception ex){

            }
            type=2;//check for update
        } else {
            Bundle b = intent.getExtras();
            type_id= b.getInt("id");
        }

        databaseAccess = DatabaseAccess.getInstance(context);

        mAdView = (AdView) findViewById(R.id.adView);
        banneraddShow();

        imageView.setOnClickListener(this);
        iconimg.setOnClickListener(this);
        done.setOnClickListener(this);
        delete.setOnClickListener(this);

        imageinfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder=new AlertDialog.Builder(context);
                builder.setTitle("Info ");
                builder.setMessage(getString(R.string.alert_category));
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
                }else {
                    String name = editname.getText().toString();
                    Bitmap bm = ((BitmapDrawable) iconimg.getDrawable()).getBitmap();
                    ByteArrayOutputStream bos = new ByteArrayOutputStream();
                    bm.compress(Bitmap.CompressFormat.PNG, 100, bos);
                    byte[] img = bos.toByteArray();
                    ContentValues cv = new ContentValues();
                    cv.put("categ_name", name);
                    cv.put("image", img);
                    cv.put("pay_id", type_id);
                    databaseAccess.open();
                    if (type==2)
                        databaseAccess.updatecategory(cv,update_id);
                    else
                        databaseAccess.insertCategory(cv);
                    databaseAccess.close();
                    AddCategoryActivity.this.finish();
                }
                break;

            case R.id.button3:
                AlertDialog.Builder builder=new AlertDialog.Builder(context);
                builder.setTitle("Do you want to Delete this ?");
                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        databaseAccess.open();
                        databaseAccess.deleteCategory(update_id);
                        databaseAccess.close();
                        //DataClass.excatMod.remove(position);
                        AddCategoryActivity.this.finish();
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
                AddCategoryActivity.this.finish();
                break;

            case R.id.iconimg:
                pickIcon();
                break;
        }
    }


    public void pickIcon(){
        final Dialog listDialog=new Dialog(this);
        listDialog.setTitle("Select Item");
        LayoutInflater li = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = li.inflate(R.layout.pickiconprompt, null, false);
        listDialog.setContentView(v);
        listDialog.setCancelable(false);

        Button cancelbtn=(Button)listDialog.findViewById(R.id.button4);
        cancelbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listDialog.cancel();
            }
        });
        androidGridView = (GridView) listDialog.findViewById(R.id.gridview_android_example);
        androidGridView.setAdapter(new AddCategoryActivity.ImageAdapterGridView(this));

        androidGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                listDialog.cancel();
                iconimg.setImageResource(imageIDs[position]);
                //Toast.makeText(getBaseContext(), "Grid Item " + (position + 1) + " Selected", Toast.LENGTH_LONG).show();
            }
        });

        listDialog.show();
    }

    public class ImageAdapterGridView extends BaseAdapter {
        private Context mContext;

        public ImageAdapterGridView(Context c) {
            mContext = c;
        }

        public int getCount() {
            return imageIDs.length;
        }

        public Object getItem(int position) {
            return null;
        }

        public long getItemId(int position) {
            return 0;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            ImageView mImageView;
            if (convertView == null) {
                mImageView = new ImageView(mContext);
                mImageView.setLayoutParams(new GridView.LayoutParams(100, 100));
                mImageView.setBackgroundColor(Color.parseColor("#d9d5dc"));
                mImageView.setBackgroundResource(R.drawable.grid_items_border);
            } else {
                mImageView = (ImageView) convertView;
            }
            mImageView.setImageResource(imageIDs[position]);
            return mImageView;
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
