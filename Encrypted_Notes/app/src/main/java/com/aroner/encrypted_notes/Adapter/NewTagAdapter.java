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
import com.aroner.encrypted_notes.Bean.GridviewBean;
import com.aroner.encrypted_notes.Bean.TagBean;
import com.aroner.encrypted_notes.DatabaseHandler;
import com.aroner.encrypted_notes.GlobalModule;
import com.aroner.encrypted_notes.R;
import com.aroner.encrypted_notes.Webservice;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by arone on 01-02-2018.
 */

public class NewTagAdapter extends ArrayAdapter<TagBean> {
    ArrayList<TagBean> arrlstGridviewBean;
    Context ctx;
    int resourseId;
    DatabaseHandler db;
    TagBean bean;
    Integer tagId, noteId;

    public NewTagAdapter(Context ctx, int resourseId, ArrayList<TagBean> arrlstGridviewBean) {
        super(ctx, resourseId, arrlstGridviewBean);
        this.ctx = ctx;
        this.resourseId = resourseId;
        this.arrlstGridviewBean = arrlstGridviewBean;
    }

    public static class Holder {

        TextView tvText;
        ImageView ivDelete;

    }

    @NonNull
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final NewTagAdapter.Holder holder;
        View rootView = convertView;
        try {


            if (rootView == null || rootView.getTag() == null) {
                LayoutInflater inflater = ((Activity) ctx).getLayoutInflater();
                holder = new NewTagAdapter.Holder();

                rootView = inflater.inflate(resourseId, parent, false);

                holder.tvText = (TextView) rootView.findViewById(R.id.tvText);
                holder.ivDelete = (ImageView) rootView.findViewById(R.id.ivDelete);


            } else {
                holder = (NewTagAdapter.Holder) rootView.getTag();
            }
            bean = arrlstGridviewBean.get(position);

//
            holder.tvText.setTextSize(Integer.parseInt(GlobalModule.textSize));
                holder.tvText.setText(arrlstGridviewBean.get(position).getTagName());
                arrlstGridviewBean.get(position).getTagId();




            holder.ivDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        android.support.v7.app.AlertDialog adExp = new android.support.v7.app.AlertDialog.Builder(ctx).create();
                        adExp.setMessage("Are you sure..! Do You want to delete it?");

                        adExp.setButton(DialogInterface.BUTTON_POSITIVE, "Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                try {

                                    bean.setTagId(arrlstGridviewBean.get(position).getTagId());
                                     db=new DatabaseHandler(ctx);
//                                    Integer i=arrlstGridviewBean.get(position).getTagId();
//                                     db.deleteRecord(i);
                                    String createdTimeStamp = DateFormat.getDateTimeInstance().format(new Date());

                                    db.deleteRecord1(bean);
                                    db.updateUser(GlobalModule.sUserId,createdTimeStamp);

//                                    tagId = arrlstGridviewBean.get(position).getTagId();
//                                    NewTagAdapter.clsDeleteTag deleteTag = new NewTagAdapter.clsDeleteTag();
//                                    deleteTag.start();

                                    Intent intent = new Intent(ctx, Tags_Page.class);
                                    ctx.startActivity(intent);

                                    ((Activity) ctx).finish();
                                } catch (Exception e) {
                                    Toast.makeText(ctx, e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }

                        });
                        adExp.setButton(DialogInterface.BUTTON_NEGATIVE, "No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                        adExp.show();

                    } catch (Exception e) {
                        Toast.makeText(ctx, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                }
            });

            holder.tvText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                        try {
                            Intent intent = new Intent(ctx, All_Notes_Page.class);
                            intent.putExtra("tag", arrlstGridviewBean.get(position).getTagName());
                            intent.putExtra("TagId", arrlstGridviewBean.get(position).getTagId());
                            ctx.startActivity(intent);
                            ((Activity) ctx).finish();
                        } catch (Exception e) {
                            Toast.makeText(ctx, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }


                }
            });


        } catch (Exception e) {
            Toast.makeText(ctx, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        return rootView;
    }




}

