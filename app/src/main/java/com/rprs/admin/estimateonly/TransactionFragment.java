package com.rprs.admin.estimateonly;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class TransactionFragment extends Fragment implements View.OnClickListener{

    DatabaseAccess databaseAccess;
    private ListView listView;
    ArrayList<TransModel> dataModels,tempmodel;
    private static TransAdapter adapter;

    TextView expensetv,incometv,monthtv;
    List<HashMap<String,String>> hmlist;

    Context context;
    int month,year;
    SimpleDateFormat month_date;
    Calendar cal;
    String weeksdate;
    ImageView imgforwd,imgback,imgexport,imgfilter,chartimg;

    private AdView mAdView;

    int selectedPosition = 0;
    Activity activity;
    private static final int PERMISSION_REQUEST_CODE = 1;
    private InterstitialAd mInterstitialAd;

    public TransactionFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_transaction, container, false);

        context=getActivity();
        activity=getActivity();
        monthtv=(TextView)getActivity().findViewById(R.id.maintextv);
        chartimg=(ImageView) getActivity().findViewById(R.id.chart);
        month_date = new SimpleDateFormat("MMM");

        mAdView = (AdView)view. findViewById(R.id.adView);
        banneraddShow();

        cal = Calendar.getInstance();
        month = cal.get(Calendar.MONTH);
        year=cal.get(Calendar.YEAR);
        if (DataClass.spin==0){
            setweek();
        }else if (DataClass.spin==2){
            monthtv.setText(year + "");
        }else {
            monthtv.setText(getResources().getStringArray(R.array.months)[month]);
        }

        databaseAccess = DatabaseAccess.getInstance(getContext());
        listView = (ListView)view.findViewById(R.id.listview);

        imgforwd=(ImageView)view.findViewById(R.id.imageforwd);
        imgback=(ImageView)view.findViewById(R.id.imageback);
        imgexport=(ImageView)view.findViewById(R.id.imageexport);
        imgfilter=(ImageView)view.findViewById(R.id.imagefilter);
        imgback.setOnClickListener(this);
        imgforwd.setOnClickListener(this);
        imgexport.setOnClickListener(this);
        imgfilter.setOnClickListener(this);
        incometv=(TextView)view.findViewById(R.id.textVincom);
        expensetv=(TextView)view.findViewById(R.id.textVexpen);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Toast.makeText(, "", Toast.LENGTH_SHORT).show();
                TransModel transModel=dataModels.get(position);
                String name=transModel.getPay_id1();
                Intent intent=new Intent(context,ItemAddActivity.class);
                DataClass.datatrans=dataModels;
                intent.putExtra("position",position);
                intent.putExtra("msgs",name);
                startActivity(intent);
            }
        });

        chartimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context,ChartActivity.class);
                DataClass.datatrans=dataModels;
                startActivity(intent);
            }
        });


        return view;
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

    public void getDataDB(int monthss,int type){
        dataModels= new ArrayList<>();
        tempmodel=new ArrayList<>();
        databaseAccess.open();
        if (type==1) {
            dataModels = databaseAccess.getTransactionWeek(monthss,year);
            tempmodel = databaseAccess.getTransactionWeek(monthss,year);
            hmlist = databaseAccess.getAmountsWeek(monthss,year);
        }else if (type==2){
            dataModels = databaseAccess.getTransactionYear(monthss);
            tempmodel = databaseAccess.getTransactionYear(monthss);
            hmlist = databaseAccess.getAmountsYear(monthss);
        }else {
            dataModels = databaseAccess.getTransaction(monthss,year);
            tempmodel = databaseAccess.getTransaction(monthss,year);
            hmlist = databaseAccess.getAmounts(monthss,year);
        }
        databaseAccess.close();
        incometv.setText("00.00 Rs. ");
        expensetv.setText("00.00 Rs. ");
        for (int i=0;i<hmlist.size();i++){
            HashMap<String,String> map=hmlist.get(i);
            String amts=map.get("amount");
            String types=map.get("type");
            if (types.equalsIgnoreCase(DataClass.income)){
                incometv.setText(amts+".00 Rs.");
            }else {
                incometv.setText(incometv.getText().toString());
            }
            if (types.equalsIgnoreCase(DataClass.expense)){
                expensetv.setText(amts+".00 Rs.");
            }else {
                expensetv.setText(expensetv.getText().toString());
            }
        }

        if (dataModels.size()>0) {
            listView.setVisibility(View.VISIBLE);
            adapter= new TransAdapter(dataModels,context);
            listView.setAdapter(adapter);
            //imageView.setVisibility(View.GONE);
        }
        else {
            listView.setVisibility(View.INVISIBLE);
        }
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
            menu.findItem(R.id.action_search).setVisible(true);
            monthtv.setVisibility(View.VISIBLE);
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
            selectedPosition=0;
            if (dataModels.size()>0) {
                adapter.notifyDataSetChanged();
            }
            chartimg.setVisibility(View.VISIBLE);

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
                            selectedPosition=0;
                            dialog.dismiss();
                        }
                    });
                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();
                }
            });

            toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    if(item.getItemId()==R.id.action_search)
                    {
                        Intent intent=new Intent(context,ItemAddActivity.class);
                        startActivity(intent);
                    }else if(item.getItemId()==R.id.action_files)
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

            //interactialaddShow();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
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
                selectedPosition=0;
                break;
            case R.id.imageforwd:
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
                selectedPosition=0;
                break;
            case R.id.imageexport:
                if (!checkPermission()) {
                    requestPermission();
                }else {
                    createPdffunc();
                }
                break;
            case R.id.imagefilter:
                promptView();
                break;
        }
    }

    public void promptView(){
        final Dialog listDialog=new Dialog(context);
        LayoutInflater li = LayoutInflater.from(context);
        View promptsView = li.inflate(R.layout.prompts, null);
        listDialog.setContentView(promptsView);
        listDialog.setCancelable(true);
        final Button clearfilter = (Button) promptsView.findViewById(R.id.button6);
        final Button cancel = (Button) promptsView.findViewById(R.id.button7);
        final ListView listView=(ListView)promptsView.findViewById(R.id.listprompt);
        databaseAccess.open();
        final List<String> values = databaseAccess.getAllCategory();
        databaseAccess.close();

        clearfilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearFilter();
                selectedPosition=0;
                listDialog.dismiss();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listDialog.dismiss();
            }
        });

        ArrayAdapter<String> adapterss = new ArrayAdapter<String>(context, R.layout.row, R.id.radioButton, values) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View v = convertView;
                if (v == null) {
                    LayoutInflater vi = (LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    v = vi.inflate(R.layout.row, null);
                    final RadioButton r = (RadioButton)v.findViewById(R.id.radioButton);
                }
                final RadioButton r = (RadioButton)v.findViewById(R.id.radioButton);
                r.setText(values.get(position));
                r.setChecked(position == selectedPosition);
                r.setTag(position);
                r.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        selectedPosition = (Integer)view.getTag();
                        notifyDataSetChanged();
                        listDialog.dismiss();
                        if (selectedPosition==0)
                            clearFilter();
                        else
                            sortFilter(r.getText().toString());
                    }
                });
                return v;
            }
        };
        listView.setAdapter(adapterss);

        listDialog.show();
    }

    public void sortFilter(String filter){
        dataModels.clear();
        int expenamt=0,incomamt=0;
        for (int i=0;i<tempmodel.size();i++){
            if(tempmodel.get(i).getCateg_name().equalsIgnoreCase(filter)){
                dataModels.add(tempmodel.get(i));
                if (tempmodel.get(i).getPay_id1().equals("Expense")){
                    expenamt=expenamt+tempmodel.get(i).getAmount();
                }else {
                    incomamt=incomamt+tempmodel.get(i).getAmount();
                }
            }else {

            }
        }
        if (dataModels.size()>0){
            listView.setVisibility(View.VISIBLE);
            adapter.notifyDataSetChanged();
        }else {
            listView.setVisibility(View.INVISIBLE);
        }
        if (incomamt>0)
            incometv.setText(incomamt+".00 Rs.");
        else
            incometv.setText("00.00 Rs. ");

        if (expenamt>0)
            expensetv.setText(expenamt+".00 Rs.");
        else
            expensetv.setText("00.00 Rs. ");
    }

    public void clearFilter(){
        dataModels.clear();
        int expenamt=0,incomamt=0;
        for (int i=0;i<tempmodel.size();i++){
            dataModels.add(tempmodel.get(i));
            if (tempmodel.get(i).getPay_id1().equals("Expense")){
                expenamt=expenamt+tempmodel.get(i).getAmount();
            }else {
                incomamt=incomamt+tempmodel.get(i).getAmount();
            }

        }
        if (dataModels.size()>0){
            listView.setVisibility(View.VISIBLE);
            adapter.notifyDataSetChanged();
        }else {
            listView.setVisibility(View.INVISIBLE);
        }
        if (incomamt>0)
            incometv.setText(incomamt+".00 Rs.");
        else
            incometv.setText("00.00 Rs. ");

        if (expenamt>0)
            expensetv.setText(expenamt+".00 Rs.");
        else
            expensetv.setText("00.00 Rs. ");
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
        TransactionFragment.this.requestPermissions(new String[]
                {Manifest.permission.WRITE_EXTERNAL_STORAGE},PERMISSION_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //Toast.makeText(context, "Granted", Toast.LENGTH_SHORT).show();
                    createPdffunc();
                } else {   // permission denied, boo! Disable the
                    Toast.makeText(context, "Denied", Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }
    }

    public void createPdffunc(){
        AlertDialog.Builder builder=new AlertDialog.Builder(context);
        builder.setTitle("Export to PDF !");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                CreatePDF cf=new CreatePDF(context,dataModels);
                cf.createfile();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
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
