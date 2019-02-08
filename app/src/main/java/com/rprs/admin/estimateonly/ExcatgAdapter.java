package com.rprs.admin.estimateonly;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class ExcatgAdapter extends ArrayAdapter<ExcatModel> {

    private ArrayList<ExcatModel> dataSet;
    Context mContext;

    public ExcatgAdapter(ArrayList<ExcatModel> dataSet, Context mContext) {
        super(mContext, R.layout.excatg_layout, dataSet);
        this.dataSet = dataSet;
        this.mContext = mContext;
    }

    // View lookup cache
    private static class ViewHolder {
        TextView txtName;
        ImageView imageView;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        ExcatModel dataModel = getItem(position);
        ExcatgAdapter.ViewHolder viewHolder;

        final View result;

        if (convertView == null) {
            viewHolder = new ExcatgAdapter.ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.excatg_layout, parent, false);
            viewHolder.txtName = (TextView) convertView.findViewById(R.id.name);
            viewHolder.imageView=(ImageView)convertView.findViewById(R.id.imagevie);
            result=convertView;
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ExcatgAdapter.ViewHolder) convertView.getTag();
            result=convertView;
        }
        viewHolder.txtName.setText(dataModel.getName());
        if (dataModel.getImg()!=null) {
            Bitmap b1 = BitmapFactory.decodeByteArray(dataModel.getImg(), 0, dataModel.getImg().length);
            viewHolder.imageView.setImageBitmap(b1);
        }else {
            viewHolder.imageView.setImageResource(R.drawable.money);
        }
        return convertView;
    }
}
