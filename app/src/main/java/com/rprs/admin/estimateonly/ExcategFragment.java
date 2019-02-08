package com.rprs.admin.estimateonly;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class ExcategFragment extends Fragment {

    TextView monthtv;

    DatabaseAccess databaseAccess;
    private ListView listView;
    ArrayList<ExcatModel> excatModels,excatarray;
    private static ExcatgAdapter adapter;
    Context context;
    Button expense,income;
    int type_id=1;
    ImageView chartimg;

    private AdView mAdView;

    public ExcategFragment() {
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
        View view= inflater.inflate(R.layout.fragment_excateg, container, false);
        context=getActivity();

        monthtv=(TextView)getActivity().findViewById(R.id.maintextv);
        chartimg=(ImageView) getActivity().findViewById(R.id.chart);
        expense=(Button)view.findViewById(R.id.btnexpense);
        income=(Button)view.findViewById(R.id.btnincome);

        mAdView = (AdView)view. findViewById(R.id.adView);
        banneraddShow();

        expense.setTextColor(getResources().getColor(R.color.white));
        expense.setBackground(getResources().getDrawable(R.drawable.my_button_bg2));

        expense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                expense.setTextColor(getResources().getColor(R.color.white));
                expense.setBackground(getResources().getDrawable(R.drawable.my_button_bg2));
                income.setTextColor(getResources().getColor(R.color.chocolate));
                income.setBackground(getResources().getDrawable(R.drawable.my_button_bgfull3));
                sortCategory(DataClass.expense);
                type_id=1;
            }
        });

        income.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                income.setTextColor(getResources().getColor(R.color.white));
                income.setBackground(getResources().getDrawable(R.drawable.my_button_bg3));
                expense.setTextColor(getResources().getColor(R.color.chocolate));
                expense.setBackground(getResources().getDrawable(R.drawable.my_button_bgfull2));
                sortCategory(DataClass.income);
                type_id=2;
            }
        });

        databaseAccess = DatabaseAccess.getInstance(getContext());
        listView = (ListView)view.findViewById(R.id.listView);

        excatModels= new ArrayList<>();
        excatarray=new ArrayList<>();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ExcatModel excatModel=excatModels.get(position);
                String name=excatModel.getName();
                String tempid=excatModel.getId();
                byte[] img=excatModel.getImg();
                Intent intent=new Intent(getContext(),AddCategoryActivity.class);
                intent.putExtra("id",tempid);
                intent.putExtra("name", name);
                intent.putExtra("image",img);
                intent.putExtra("type_id",type_id);
                startActivity(intent);
            }
        });

        return view;
    }

    public void sortCategory(String id){
        excatModels.clear();
        for (int i=0;i<excatarray.size();i++){
            if(excatarray.get(i).getPay_type().equalsIgnoreCase(id)){
                excatModels.add(excatarray.get(i));

            }else {

            }
        }
        if (excatModels.size()>0){
            listView.setVisibility(View.VISIBLE);
            adapter.notifyDataSetChanged();
        }else {
            listView.setVisibility(View.INVISIBLE);
        }
    }

    public void getDataDB(){
        excatarray.clear();
        databaseAccess.open();
        excatarray = databaseAccess.getCategory();
        databaseAccess.close();
        excatModels.clear();
        for (int i=0;i<excatarray.size();i++){
            if(excatarray.get(i).getPay_type().equalsIgnoreCase(DataClass.expense)) {
                excatModels.add(excatarray.get(i));
            }
        }

        if (excatModels.size()>0) {
            //DataClass.excatMod=excatModels;
            listView.setVisibility(View.VISIBLE);
            adapter= new ExcatgAdapter(excatModels,context);
            listView.setAdapter(adapter);
        }else {
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
            monthtv.setVisibility(View.INVISIBLE);
            chartimg.setVisibility(View.INVISIBLE);
            getDataDB();
            if (type_id==1) {
                sortCategory(DataClass.expense);
            }else {
                sortCategory(DataClass.income);
            }

            toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    if(item.getItemId()==R.id.action_search)
                    {
                        Intent intent=new Intent(context,AddCategoryActivity.class);
                        intent.putExtra("id",type_id);
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
