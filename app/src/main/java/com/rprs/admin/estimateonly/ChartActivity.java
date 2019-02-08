package com.rprs.admin.estimateonly;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

public class ChartActivity extends AppCompatActivity implements View.OnClickListener{

    Button  barbtn,piebtn;
    BarChart chart;
    PieChart piechart;

    Context context;
    //ArrayList<TransModel> dataModels;
    private InterstitialAd mInterstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart);

        context=this;
        barbtn=(Button)findViewById(R.id.barbtn);
        piebtn=(Button)findViewById(R.id.piebtn);

        showinteracialadd();

        chart = (BarChart) findViewById(R.id.bar_chart);
        piechart = (PieChart) findViewById(R.id.Pie_chart);

        barbtn.setTextColor(getResources().getColor(R.color.white));
        barbtn.setBackground(getResources().getDrawable(R.drawable.my_button_bg2));

        BarData data = new BarData(getXAxisValues(), getDataSet());
        chart.setData(data);
        chart.setDescription("My Chart");
        chart.animateXY(2000, 2000);
        chart.invalidate();

        piechart.setVisibility(View.GONE);

        barbtn.setOnClickListener(this);
        piebtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.barbtn:
                barbtn.setTextColor(getResources().getColor(R.color.white));
                barbtn.setBackground(getResources().getDrawable(R.drawable.my_button_bg2));
                piebtn.setTextColor(getResources().getColor(R.color.chocolate));
                piebtn.setBackground(getResources().getDrawable(R.drawable.my_button_bgfull3));
                chart.setVisibility(View.VISIBLE);
                piechart.setVisibility(View.GONE);
                break;

            case R.id.piebtn:
                piebtn.setTextColor(getResources().getColor(R.color.white));
                piebtn.setBackground(getResources().getDrawable(R.drawable.my_button_bg3));
                barbtn.setTextColor(getResources().getColor(R.color.chocolate));
                barbtn.setBackground(getResources().getDrawable(R.drawable.my_button_bgfull2));
                piechart.setVisibility(View.VISIBLE);
                setPiechart();
                chart.setVisibility(View.GONE);
                break;
        }
    }

    private ArrayList<BarDataSet> getDataSet() {
        ArrayList<BarDataSet> dataSets = null;
        ArrayList<BarEntry> valueSet1 = new ArrayList<>();//for first value
        ArrayList<BarEntry> valueSet2 = new ArrayList<>();//for second value
        for (int i=0;i<DataClass.datatrans.size();i++){
            TransModel transModel=DataClass.datatrans.get(i);
            int expvalue=0,incomvalue=0;
            if (transModel.getPay_id1().equalsIgnoreCase(DataClass.income)){
                incomvalue=transModel.getAmount();
            }else {
                incomvalue=0;
            }if (transModel.getPay_id1().equalsIgnoreCase(DataClass.expense)) {
                expvalue=transModel.getAmount();
            }else {
                expvalue=0;
            }
            BarEntry v1e1 = new BarEntry(incomvalue, i); // Jan
            valueSet1.add(v1e1);
            BarEntry v2e1 = new BarEntry(expvalue, i); // Jan
            valueSet2.add(v2e1);
        }

        BarDataSet barDataSet1 = new BarDataSet(valueSet1, "Income");
        barDataSet1.setColor(Color.rgb(50, 155, 0));
        BarDataSet barDataSet2 = new BarDataSet(valueSet2, "Expense");
        barDataSet2.setColor(Color.rgb(150, 0, 0));

        dataSets = new ArrayList<>();
        dataSets.add(barDataSet1);
        dataSets.add(barDataSet2);
        return dataSets;
    }

    private ArrayList<String> getXAxisValues() {
        ArrayList<String> xAxis = new ArrayList<>();
        for (int i=0;i<DataClass.datatrans.size();i++){
            TransModel transModel=DataClass.datatrans.get(i);
            //xAxis.add(transModel.getDate().substring(0,5));
            xAxis.add(transModel.getCateg_name());
        }

        return xAxis;
    }

    public void setPiechart(){
        ArrayList<Entry> BarEntry = new ArrayList<>();

        for (int i=0;i<DataClass.datatrans.size();i++){
            TransModel transModel=DataClass.datatrans.get(i);
            BarEntry.add(new BarEntry(transModel.getAmount(), i));
        }

        PieDataSet dataSet = new PieDataSet(BarEntry, "Transactions");

        ArrayList<String> labels = new ArrayList<>();

        for (int i=0;i<DataClass.datatrans.size();i++){
            TransModel transModel=DataClass.datatrans.get(i);
            labels.add(transModel.getDate().substring(0,5));
        }

        PieData datas = new PieData(labels,dataSet);

        dataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        piechart.setData(datas);

        piechart.setDescription(DataClass.datatrans.size()+" No of Projects");
        piechart.animateXY(2000, 2000);
        piechart.invalidate();
    }

    public void showinteracialadd(){
        mInterstitialAd = new InterstitialAd(context);
        mInterstitialAd.setAdUnitId(getString(R.string.interstitial_full_screen2));
        mInterstitialAd.loadAd(new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .build());


        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                // Code to be executed when an ad finishes loading.
                mInterstitialAd.show();
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                // Code to be executed when an ad request fails.
                Toast.makeText(getApplicationContext(), "Ad failed to load! error code: " + errorCode, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAdOpened() {
                // Code to be executed when the ad is displayed.
            }

            @Override
            public void onAdLeftApplication() {
                // Code to be executed when the user has left the app.
            }

            @Override
            public void onAdClosed() {
                // Load the next interstitial.
                //mInterstitialAd.loadAd(new AdRequest.Builder().build());
                // Code to be executed when when the interstitial ad is closed.
            }
        });
    }

}
