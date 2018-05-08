package com.aroner.encrypted_notes.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.aroner.encrypted_notes.Activities.All_Notes_Page;
import com.aroner.encrypted_notes.Activities.NewNote;
import com.aroner.encrypted_notes.Activities.Tags_Page;
import com.aroner.encrypted_notes.Bean.TagBean;
import com.aroner.encrypted_notes.DatabaseHandler;
import com.aroner.encrypted_notes.GlobalModule;
import com.aroner.encrypted_notes.R;
import com.aroner.encrypted_notes.SaveSharedPreference;
import com.aroner.encrypted_notes.Webservice;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by arone on 21-02-2018.
 */

public class DrawerAdapter extends ArrayAdapter<TagBean> {
    ArrayList<TagBean> arrlstGridviewBean;
    Context ctx;
    int resourseId;
    TagBean bean;

    public DrawerAdapter(Context ctx, int resourseId, ArrayList<TagBean> arrlstGridviewBean) {
        super(ctx, resourseId, arrlstGridviewBean);
        this.ctx = ctx;
        this.resourseId = resourseId;
        this.arrlstGridviewBean = arrlstGridviewBean;
    }

    public static class Holder {

        TextView tvDrawerItem;
        ImageView ivIcon;

    }

    @NonNull
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final DrawerAdapter.Holder holder;
        View rootView = convertView;
        try {


            if (rootView == null || rootView.getTag() == null) {
                LayoutInflater inflater = ((Activity) ctx).getLayoutInflater();
                holder = new DrawerAdapter.Holder();

                rootView = inflater.inflate(resourseId, parent, false);

                holder.tvDrawerItem = (TextView) rootView.findViewById(R.id.tvDrawerItem);
                holder.ivIcon = (ImageView) rootView.findViewById(R.id.ivIcon);


            } else {
                holder = (DrawerAdapter.Holder) rootView.getTag();
            }
            bean = arrlstGridviewBean.get(position);

            if(arrlstGridviewBean.get(position).getiIconImg()==0){

            }else {
                Picasso.with(ctx).load(arrlstGridviewBean.get(position).getiIconImg()).into(holder.ivIcon);

            }

            holder.tvDrawerItem.setTextSize(Integer.parseInt(GlobalModule.textSize));

            holder.tvDrawerItem.setText(arrlstGridviewBean.get(position).getsTitle());



        } catch (Exception e) {
            Toast.makeText(ctx, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        return rootView;
    }


}
