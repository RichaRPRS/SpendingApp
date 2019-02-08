package com.rprs.admin.estimateonly;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class CategorylistFragment extends DialogFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    OnHeadlineSelectedListener mCallback;
    DatabaseAccess databaseAccess;
    private ListView listView;
    ArrayList<ExcatModel> excatModels,excatarray;
    private static ExcatgAdapter adapter;
    Context context;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    public CategorylistFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static CategorylistFragment newInstance(String param1) {
        CategorylistFragment fragment = new CategorylistFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_categorylist, container, false);
        context=getActivity();


        databaseAccess = DatabaseAccess.getInstance(getContext());
        listView = (ListView)view.findViewById(R.id.listView);

        excatModels= new ArrayList<>();
        excatarray=new ArrayList<>();
        databaseAccess.open();
        excatarray = databaseAccess.getCategory();
        databaseAccess.close();

        for (int i=0;i<excatarray.size();i++){
            if(excatarray.get(i).getPay_type().equalsIgnoreCase(mParam1)) {
                excatModels.add(excatarray.get(i));
            }
        }

        if (excatModels.size()>0) {
            adapter= new ExcatgAdapter(excatModels,context);
            listView.setAdapter(adapter);
        }

        Button btncancel=(Button)view.findViewById(R.id.button5);

        btncancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ExcatModel excatModel=excatModels.get(position);
                int ids=Integer.parseInt(excatModel.getId());
                String item=excatModel.getName();
                returnResult(ids,item);
                dismiss();
            }
        });
        return view;
    }

    public void returnResult(int position,String message)
    {
        mCallback.onArticleSelected(position,message);
    }

    public void setOnHeadlineSelectedListener(Activity activity) {
        mCallback = (OnHeadlineSelectedListener) activity;
    }

    // Container Activity must implement this interface
    public interface OnHeadlineSelectedListener {
        public void onArticleSelected(int position,String message);
    }


}
