package com.rprs.admin.estimateonly;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class ProfileFragment extends Fragment {

    TextView monthtv;
    DatabaseAccess databaseAccess;
    private ListView listView;
    ImageView chartimg;
    Context context;
    List<HashMap<String,String>> hmlist;
    ArrayAdapter<HashMap<String,String>> adapterss;

    private AdView mAdView;

    public ProfileFragment() {
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
        View view= inflater.inflate(R.layout.fragment_add_expense, container, false);
        monthtv=(TextView)getActivity().findViewById(R.id.maintextv);
        chartimg=(ImageView) getActivity().findViewById(R.id.chart);

        databaseAccess = DatabaseAccess.getInstance(getContext());
        listView = (ListView)view.findViewById(R.id.listView);
        context=getActivity();

        mAdView = (AdView)view. findViewById(R.id.adView);
        banneraddShow();

        databaseAccess.open();
        hmlist = databaseAccess.getuser();
        if (hmlist.size()==0){
            databaseAccess.insertuser("Personal","");
            hmlist = databaseAccess.getuser();
        }else {
        }
        databaseAccess.close();

        adapterss = new ArrayAdapter<HashMap<String,String>>(context, R.layout.profile_layout, R.id.name, hmlist) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View v = convertView;
                if (v == null) {
                    LayoutInflater vi = (LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    v = vi.inflate(R.layout.profile_layout, null);
                    final TextView textView = (TextView) v.findViewById(R.id.name);
                    final ImageView imageView=(ImageView)v.findViewById(R.id.imagevie);
                }
                final TextView textView = (TextView) v.findViewById(R.id.name);
                final ImageView imageView=(ImageView)v.findViewById(R.id.imagevie);
                HashMap<String,String> map=hmlist.get(position);
                String name=map.get("name");
                textView.setText(name);
                imageView.setImageResource(R.drawable.user);
                textView.setTag(position);
                return v;
            }
        };
        listView.setAdapter(adapterss);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                HashMap<String,String> map=hmlist.get(position);
                String u_id=map.get("id");
                String name=map.get("name");
                String mobile=map.get("mobile");
                Intent intent=new Intent(context,AddUserActivity.class);
                intent.putExtra("id", u_id);
                intent.putExtra("name", name);
                intent.putExtra("mobile", mobile);
                startActivity(intent);
            }
        });

        return view;
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

            toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    if(item.getItemId()==R.id.action_search)
                    {
                        Intent intent=new Intent(context,AddUserActivity.class);
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

            databaseAccess.open();
            hmlist = databaseAccess.getuser();
            databaseAccess.close();
            if (hmlist.size()>0)
                adapterss.notifyDataSetChanged();
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
