package com.rprs.admin.estimateonly;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class SpendingFragment extends Fragment implements View.OnClickListener
{
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    ProgressBar progressBar;
    Button expensebtn,incomebtn;
    TextView expensetv,incometv,totaltextv,monthtv;
    DatabaseAccess databaseAccess;
    ImageView chartimg;
    ImageView imgforwd,imgback;
    Context context;
    int month,year, day;
    Calendar cal;
    String weeksdate;
    SimpleDateFormat month_date;
    float percentage;

    private AdView mAdView;

    List<HashMap<String,String>> hmlist;

    public SpendingFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_spending, container, false);

        context=getContext();
        databaseAccess = DatabaseAccess.getInstance(getContext());
        mAdView = (AdView)view. findViewById(R.id.adView);
        monthtv=(TextView)getActivity().findViewById(R.id.maintextv);
        chartimg=(ImageView) getActivity().findViewById(R.id.chart);

        cal = Calendar.getInstance();
        month = cal.get(Calendar.MONTH);
        year=cal.get(Calendar.YEAR);
        day=cal.get(Calendar.DATE);

        month_date = new SimpleDateFormat("MMM");
        if (DataClass.spin==0){
            setweek();
        }else if (DataClass.spin==2){
            monthtv.setText(year + "");
        }else {
            monthtv.setText(getResources().getStringArray(R.array.months)[month]);
        }

        progressBar=(ProgressBar)view.findViewById(R.id.progressBar);

        expensebtn=(Button)view.findViewById(R.id.button);
        incomebtn=(Button)view.findViewById(R.id.button2);

        expensetv=(TextView)view.findViewById(R.id.tvexpense);
        incometv=(TextView)view.findViewById(R.id.tvincome);
        totaltextv=(TextView)view.findViewById(R.id.textVtotal);

        imgforwd=(ImageView)view.findViewById(R.id.imageView2);
        imgback=(ImageView)view.findViewById(R.id.imageback);

        expensebtn.setOnClickListener(this);
        incomebtn.setOnClickListener(this);

        imgback.setOnClickListener(this);
        imgforwd.setOnClickListener(this);


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

        return view;
    }

    public void getDataDB(int monthss,int type){
        databaseAccess.open();
        if (type==1) {
            hmlist = databaseAccess.getAmountsWeek(monthss,year);
        }else if (type==2){
            hmlist = databaseAccess.getAmountsYear(monthss);
        }else {
            hmlist = databaseAccess.getAmounts(monthss,year);
        }
        databaseAccess.close();
        incometv.setText("00.00 Rs. ");
        expensetv.setText("00.00 Rs. ");
        int totamt=0,totamt2=0,incomeamt=0,expenseamt=0;;
        for (int i=0;i<hmlist.size();i++){
            HashMap<String,String> map=hmlist.get(i);
            String amts=map.get("amount");
            String types=map.get("type");
            totamt2=totamt2+Integer.parseInt(amts);
            if (types.equalsIgnoreCase(DataClass.income)){
                incometv.setText(amts+".00 Rs.");
                totamt=totamt+Integer.parseInt(amts);
                incomeamt=Integer.parseInt(amts);
            }else {
                incometv.setText(incometv.getText().toString());
            }
            if (types.equalsIgnoreCase(DataClass.expense)){
                expensetv.setText(amts+".00 Rs.");
                totamt=totamt-Integer.parseInt(amts);
                expenseamt=expenseamt+Integer.parseInt(amts);
            }else {
                expensetv.setText(expensetv.getText().toString());
            }
        }
        if (hmlist.size()==0){
            incometv.setText("00.00 Rs.");
            expensetv.setText("00.00 Rs.");
        }

        if (totamt==0 ) {
            percentage=50;
        }else {
            percentage = (float) ((incomeamt * 100.0) / totamt2);
        }
        progressBar.setProgress((int) percentage);

        totaltextv.setText(totamt+".00 Rs.");
    }

    public void setweeksfs(){
        int weekOfYear = cal.get(Calendar.WEEK_OF_YEAR);
        DataClass.weeksno=weekOfYear;
        //Toast.makeText(context, weekOfYear+"", Toast.LENGTH_SHORT).show();
        Calendar c = new GregorianCalendar(Locale.getDefault());
        c.set(Calendar.WEEK_OF_YEAR, weekOfYear);
        c.set(Calendar.YEAR, year);
        c.set(Calendar.DAY_OF_WEEK, 2);
        int temp1=c.get(Calendar.DATE);
        String month_name = month_date.format(c.getTime());
        c.set(Calendar.DAY_OF_WEEK, 7);
        int temp2=c.get(Calendar.DATE);
        String month_name2 = month_date.format(c.getTime());
        if (month_name.equals(month_name2)){
            weeksdate = temp1 + "-" + (temp2 + 1) + " " + month_name2 + " ";
        }else {
            weeksdate = temp1 + " " + month_name + " " + "-" + (temp2 + 1) + " " + month_name2 + " ";
        }
        c.set(year, 0, 1);
        DataClass.numweeks=c.getMaximum(Calendar.WEEK_OF_YEAR);
        monthtv.setText(weeksdate);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.button:
                Intent intent=new Intent(getContext(),ItemAddActivity.class);
                intent.putExtra("message", DataClass.expense);
                startActivity(intent);
                break;
            case R.id.button2:
                Intent intents=new Intent(getContext(),ItemAddActivity.class);
                intents.putExtra("message", DataClass.income);
                startActivity(intents);
                break;
            case R.id.imageback:
                if (DataClass.spin==0){
                    DataClass.weeksno--;
                    setweek();
                    getDataDB(DataClass.weeksno,1);
                }else if (DataClass.spin==1) {
                    if (month > 0) {
                        month--;
                        monthtv.setText(getResources().getStringArray(R.array.months)[month]);
                    } else {
                        month = 11;
                        year--;
                        monthtv.setText(getResources().getStringArray(R.array.months)[month]);
                    }
                    getDataDB(month+1,0);
                }else {
                    year--;
                    monthtv.setText(""+year);
                    getDataDB(year,2);
                }
                break;
            case R.id.imageView2:
                if (DataClass.spin==0){
                    DataClass.weeksno++;
                    setweek();
                    getDataDB(DataClass.weeksno,1);
                }else if (DataClass.spin==1) {
                    if (month<11){
                        month++;
                        monthtv.setText(getResources().getStringArray(R.array.months)[month] );
                    }else {
                        month=0;
                        year++;
                        monthtv.setText(getResources().getStringArray(R.array.months)[month] );
                    }
                    getDataDB(month+1,0);
                }else {
                    year++;
                    monthtv.setText(""+year);
                    getDataDB(year,2);
                }
                break;
        }
    }

    public void setweek(){
        Calendar c = new GregorianCalendar(Locale.getDefault());
        String stryear="",stryear2="";
        if (DataClass.weeksno==0){
            year--;
            c.set(year, 0, 1);
            DataClass.weeksno=(c.getMaximum(Calendar.WEEK_OF_YEAR)-1);
            stryear2= String.valueOf(year);
        }else if (DataClass.weeksno==DataClass.numweeks){
            year++;
            DataClass.weeksno=1;
        }
        if (cal.get(Calendar.YEAR)!=year){
            stryear= String.valueOf(year);
        }
        c.set(Calendar.WEEK_OF_YEAR, DataClass.weeksno);
        c.set(Calendar.YEAR, year);
        c.set(Calendar.DAY_OF_WEEK, 2);
        //Toast.makeText(context, " "+DataClass.weeksno, Toast.LENGTH_SHORT).show();
        int temp1=c.get(Calendar.DATE);
        String month_name = month_date.format(c.getTime());
        c.set(Calendar.DAY_OF_WEEK, 7);
        int temp2=c.get(Calendar.DATE);
        String month_name2 = month_date.format(c.getTime());
        if (month_name.equals(month_name2)){
            weeksdate = temp1 + " - " + (temp2 + 1) + " " + month_name2 + " "+stryear;
        }else {
            weeksdate = temp1 + " " + month_name + " " +stryear2 + " " + " - " + (temp2 + 1) + " " + month_name2 + " "+stryear;
        }
        monthtv.setText(weeksdate);
    }

    @Override
    public void setUserVisibleHint(boolean visible)
    {
        super.setUserVisibleHint(visible);
        if (visible && isResumed())
        {
            onResume();
        }
    }

    @Override
    public void onResume()
    {
        super.onResume();
        if (getUserVisibleHint())
        {
            Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
            Menu menu=toolbar.getMenu();
            menu.findItem(R.id.action_search).setVisible(false);
            monthtv.setVisibility(View.VISIBLE);
            monthtv=(TextView)getActivity().findViewById(R.id.maintextv);
            chartimg.setVisibility(View.INVISIBLE);

            monthtv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                    //alertDialogBuilder.setMessage("Are you sure, You wanted to make decision");
                    alertDialogBuilder.setTitle("Pick one item");
                    alertDialogBuilder.setSingleChoiceItems(R.array.valid,DataClass.spin, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            DataClass.spin=which;
                            Calendar cals = Calendar.getInstance();
                            if (which==0){
                                year=cals.get(Calendar.YEAR);
                                setweeksfs();
                                getDataDB(DataClass.weeksno,1);
                            }else if (which==1){
                                year=cals.get(Calendar.YEAR);
                                monthtv.setText(getResources().getStringArray(R.array.months)[month] );
                                getDataDB(month+1,0);
                            }else {
                                monthtv.setText(year + "");
                                getDataDB(year,2);
                            }
                            //Toast.makeText(context, "transaction "+year, Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        }
                    });
                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();
                }
            });

            if (DataClass.spin==0){
                setweeksfs();
                getDataDB(DataClass.weeksno,1);
            }else if (DataClass.spin==1){
                monthtv.setText(getResources().getStringArray(R.array.months)[month] );
                getDataDB(month+1,0);
            }else {
                monthtv.setText(year + "");
                getDataDB(year,2);
            }

            toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    if(item.getItemId()==R.id.action_files)
                    {
                        Intent intent=new Intent(context,FilesPDFActivity.class);
                        startActivity(intent);
                    }else if (item.getItemId()==R.id.action_about){
                        Intent intent=new Intent(context,AboutUsActivity.class);
                        startActivity(intent);
                    }
                    return false;
                }
            });
        }
    }

}
