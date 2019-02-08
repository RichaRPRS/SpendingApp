package com.rprs.admin.estimateonly;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

public class ItemAddActivity extends AppCompatActivity implements CategorylistFragment.OnHeadlineSelectedListener{

    ImageView imageView,imageinfo;
    Button done,expense,income,delete;
    TextView tvcateg,tvdate;
    EditText editText;

    Context context;
    DatabaseAccess databaseAccess;

    int Category_id;
    int pay_id, position;
    int month,year, day,upday,upmonth,upyear;
    Calendar cal;
    SimpleDateFormat month_date;
    String fDate,trnid;
    int type=1;//for check insert
    TransModel transModel;
    String message="";
    int weekOfYear;
    private AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_add);

        context=this;
        imageView=(ImageView)findViewById(R.id.backimgvi);
        imageinfo=(ImageView)findViewById(R.id.imageView3);
        done=(Button)findViewById(R.id.donebtn);
        expense=(Button)findViewById(R.id.btnexpense);
        income=(Button)findViewById(R.id.btnincome);
        delete=(Button)findViewById(R.id.button3);

        tvcateg=(TextView)findViewById(R.id.textView7);
        tvdate=(TextView)findViewById(R.id.textView4);
        editText=(EditText)findViewById(R.id.editText);
        editText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(8)});
        mAdView = (AdView) findViewById(R.id.adView);
        banneraddShow();

        databaseAccess = DatabaseAccess.getInstance(context);

        month_date = new SimpleDateFormat("MMM");
        cal = Calendar.getInstance();

        Intent intent = getIntent();
        if (intent.hasExtra("message")) {
            Bundle b = intent.getExtras();
            message = b.getString("message");
        } else if (intent.hasExtra("position"))
        {
            Bundle bb=intent.getExtras();
            position=bb.getInt("position");
            message=bb.getString("msgs");
            transModel=DataClass.datatrans.get(position);
            String name=transModel.getCateg_name();
            String tran_id=transModel.getCatg_id();
            Category_id=Integer.parseInt(tran_id);
            trnid=transModel.getTr_id();
            String date=transModel.getDate();
            int amount= transModel.getAmount();
            tvcateg.setText(name);
            tvdate.setText(date);
            editText.setText(amount+"");
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
            Date datecls = null;
            try {
                datecls = sdf.parse(date);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            cal.setTime(datecls);
            delete.setVisibility(View.VISIBLE);
            type=2;
            income.setEnabled(false);
            expense.setEnabled(false);
        }

        month = cal.get(Calendar.MONTH);
        year=cal.get(Calendar.YEAR);
        day=cal.get(Calendar.DATE);
        String month_name = month_date.format(cal.getTime());
        tvdate.setText(day+" "+month_name+" "+year);
        fDate=day+"-"+(month+1)+"-"+year;
        upday=day;
        upmonth=(month+1);
        upyear=year;
        weekOfYear = cal.get(Calendar.WEEK_OF_YEAR);

        setimgback(message);

        income.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                income.setTextColor(getResources().getColor(R.color.white));
                income.setBackground(getResources().getDrawable(R.drawable.my_button_bg3));
                expense.setTextColor(getResources().getColor(R.color.chocolate));
                expense.setBackground(getResources().getDrawable(R.drawable.my_button_bgfull2));
                message=DataClass.income;
                pay_id=2;
            }
        });

        expense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                expense.setTextColor(getResources().getColor(R.color.white));
                expense.setBackground(getResources().getDrawable(R.drawable.my_button_bg2));
                income.setTextColor(getResources().getColor(R.color.chocolate));
                income.setBackground(getResources().getDrawable(R.drawable.my_button_bgfull3));
                message=DataClass.expense;
                pay_id=1;
            }
        });

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ItemAddActivity.this.finish();
            }
        });

        imageinfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder=new AlertDialog.Builder(context);
                builder.setTitle("Info ");
                builder.setMessage(getString(R.string.alert_transaction));
                builder.setNeutralButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });

        tvdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(context,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                Calendar cals = Calendar.getInstance();
                                cals.set(Calendar.MONTH,monthOfYear);
                                String month_name = month_date.format(cals.getTime());
                                tvdate.setText(dayOfMonth + " " + (month_name) + " " + year);
                                fDate=dayOfMonth+"-"+(monthOfYear + 1)+"-"+year;
                                upday=dayOfMonth;
                                upmonth=(monthOfYear + 1);
                                upyear=year;
                                Calendar c = new GregorianCalendar(Locale.getDefault());
                                c.set(Calendar.MONTH,monthOfYear);
                                c.set(Calendar.YEAR,year);
                                c.set(Calendar.DATE,dayOfMonth);
                                weekOfYear = c.get(Calendar.WEEK_OF_YEAR);
                            }
                        }, year, month, day);
                datePickerDialog.show();
            }
        });

        tvcateg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str="";
                if (pay_id==2){
                    str=DataClass.income;
                }else {
                    str=DataClass.expense;
                }
                FragmentManager fm = getSupportFragmentManager();
                CategorylistFragment editNameDialogFragment = CategorylistFragment.newInstance(str);
                editNameDialogFragment.show(fm, "fragment_edit_name");
                editNameDialogFragment.setCancelable(false);
            }
        });

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tvcateg.getText().toString().trim().length()<=0){
                    Toast.makeText(context, "Please select category first", Toast.LENGTH_SHORT).show();
                }else if (editText.getText().toString().trim().length()<=0){
                    Toast.makeText(context, "Please Enter amount", Toast.LENGTH_SHORT).show();
                }else {
                    String amountstr=editText.getText().toString();
                    int amount=Integer.parseInt(amountstr);
                    String note="notes";
                    databaseAccess.open();
                    if (type==2) {
                        databaseAccess.updatetrans(amount,fDate,Category_id,note,pay_id,trnid,upday,weekOfYear,upmonth,upyear);
                        TransModel model=new TransModel(trnid,amount,String.valueOf(Category_id),tvcateg.getText().toString(),message,fDate,note,transModel.getImg());
                        DataClass.datatrans.set(position,model);
                        Toast.makeText(context, "Updated Successfully", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        databaseAccess.insertTran(amount, fDate, Category_id, note, pay_id,upday,weekOfYear,upmonth,upyear);
                        Toast.makeText(context, "Inserted data", Toast.LENGTH_SHORT).show();
                    }
                    databaseAccess.close();

                    ItemAddActivity.this.finish();
                }
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder=new AlertDialog.Builder(context);
                builder.setTitle("Do you want to Delete this ?");
                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        databaseAccess.open();
                        databaseAccess.deleteTrans(trnid);
                        databaseAccess.close();
                        ItemAddActivity.this.finish();
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
            }
        });
    }

    public void  setimgback(String mse){
        if (mse.equalsIgnoreCase(DataClass.income)){
            income.setTextColor(getResources().getColor(R.color.white));
            income.setBackground(getResources().getDrawable(R.drawable.my_button_bg3));
            pay_id=2;
        }else {
            expense.setTextColor(getResources().getColor(R.color.white));
            expense.setBackground(getResources().getDrawable(R.drawable.my_button_bg2));
            pay_id=1;
        }
    }


    @Override
    public void onAttachFragment(Fragment fragment) {
        if (fragment instanceof CategorylistFragment) {
            CategorylistFragment headlinesFragment = (CategorylistFragment) fragment;
            headlinesFragment.setOnHeadlineSelectedListener(this);
        }
    }

    public void onArticleSelected(int position,String data) {
        tvcateg.setText(data);
        Category_id=position;
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
