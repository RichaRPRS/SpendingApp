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

public class TransAdapter extends ArrayAdapter<TransModel> {

    private ArrayList<TransModel> dataSet;
    Context mContext;

    public TransAdapter(ArrayList<TransModel> dataSet, Context mContext) {
        super(mContext, R.layout.application_layout, dataSet);
        this.dataSet = dataSet;
        this.mContext = mContext;
    }

    // View lookup cache
    private static class ViewHolder {
        TextView txtName,txtamt;
        TextView txtType;
        ImageView imageView;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        TransModel dataModel = getItem(position);
        TransAdapter.ViewHolder viewHolder;

        final View result;

        if (convertView == null) {

            viewHolder = new TransAdapter.ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.application_layout, parent, false);
            viewHolder.txtName = (TextView) convertView.findViewById(R.id.name);
            viewHolder.txtType = (TextView) convertView.findViewById(R.id.type);
            viewHolder.txtamt = (TextView) convertView.findViewById(R.id.textVamt);
            viewHolder.imageView=(ImageView)convertView.findViewById(R.id.imagevie);
            result=convertView;
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (TransAdapter.ViewHolder) convertView.getTag();
            result=convertView;
        }
        viewHolder.txtName.setText(dataModel.getCateg_name());
        viewHolder.txtType.setText(dataModel.getDate());
        if (dataModel.getPay_id1().equalsIgnoreCase(DataClass.expense)){
            viewHolder.txtamt.setTextColor(mContext.getResources().getColor(R.color.chocolate));
        }else {
            viewHolder.txtamt.setTextColor(mContext.getResources().getColor(R.color.green));
        }
        viewHolder.txtamt.setText(dataModel.getAmount()+" Rs.");
        if (dataModel.getImg()!=null) {
            Bitmap b1 = BitmapFactory.decodeByteArray(dataModel.getImg(), 0, dataModel.getImg().length);
            viewHolder.imageView.setImageBitmap(b1);
        }

        return convertView;
    }
}
