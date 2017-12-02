package com.example.nishant.fire_test;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by nishant on 29/11/17.
 */

public class RecyclerAdapter extends ArrayAdapter<ListItem> {


    public RecyclerAdapter(Context context, int resource, List<ListItem> objects){
        super(context,resource,objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = ((Activity) getContext()).getLayoutInflater().inflate(R.layout.item_list, parent, false);
        }

       TextView  name_text_view =   convertView.findViewById(R.id.name_text_view);
        TextView cart_size_text_view = convertView.findViewById(R.id.cart_size_text_view);
        TextView mUserId=convertView.findViewById(R.id.mUser_Id);

        ListItem listItem = getItem(position);

        if(listItem.getmUserId()!=null){

            name_text_view.setText(listItem.getitem());
            cart_size_text_view.setText(listItem.getCount());
            mUserId.setText(listItem.getmUserId());
        }

        return convertView;
    }
}
