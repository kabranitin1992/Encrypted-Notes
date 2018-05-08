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
import android.widget.AdapterView;
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
import com.aroner.encrypted_notes.SaveSharedPreference;
import com.aroner.encrypted_notes.Webservice;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by shree on 17-10-2017.
 */

public class TagAdapter extends ArrayAdapter<TagBean> {
    ArrayList<TagBean> arrlstGridviewBean;
    Context ctx;
    int resourseId;
    TagBean bean;
    String tagId, noteId,DecryptTag;
    DatabaseHandler db;
    String SubstringEncryptKey;
    SaveSharedPreference saveSharedPreference;

    public TagAdapter(Context ctx, int resourseId, ArrayList<TagBean> arrlstGridviewBean) {
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
        final TagAdapter.Holder holder;
        View rootView = convertView;
        try {


            if (rootView == null || rootView.getTag() == null) {
                LayoutInflater inflater = ((Activity) ctx).getLayoutInflater();
                holder = new TagAdapter.Holder();

                rootView = inflater.inflate(resourseId, parent, false);

                holder.tvText = (TextView) rootView.findViewById(R.id.tvText);
                holder.ivDelete = (ImageView) rootView.findViewById(R.id.ivDelete);


            } else {
                holder = (TagAdapter.Holder) rootView.getTag();
            }
            bean = arrlstGridviewBean.get(position);

            holder.tvText.setTextSize(Integer.parseInt(GlobalModule.textSize));


//
            if (arrlstGridviewBean.get(position).getStr().equals("Note")) {

                SubstringEncryptKey = GlobalModule.subKey;
                DecryptTag = GlobalModule.Decrypt(ctx, SubstringEncryptKey,arrlstGridviewBean.get(position).getsNote());
                String textDecrypt=DecryptTag.trim();
                holder.tvText.setText(textDecrypt);

//                if (textDecrypt.length() > 26) {
//
//                    String HalfNotes = textDecrypt.substring(0, 25);
//                    StringBuilder sb = new StringBuilder(HalfNotes);
//                    sb.append(" ...");
////                                bean.setsNote();
//
//
//                    holder.tvText.setText(sb.toString());
//
//                } else {
//                    holder.tvText.setText(textDecrypt);
//                }
//


            } else if(arrlstGridviewBean.get(position).getStr().equals("Tag")){


                SubstringEncryptKey =  GlobalModule.subKey;
                DecryptTag = GlobalModule.Decrypt(ctx, SubstringEncryptKey,arrlstGridviewBean.get(position).getTagName());

                String textDecrypt=DecryptTag.trim();
                holder.tvText.setText(textDecrypt);
            }


            holder.ivDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        android.support.v7.app.AlertDialog adExp = new android.support.v7.app.AlertDialog.Builder(ctx).create();

                        String alert1 = "Are you sure..! Do You want to delete it? ";
                        String alert2 = "Notes of this tag will also be deleted.. ";
                        if (arrlstGridviewBean.get(position).getStr().equals("Tag")) {
                            adExp.setMessage(alert1 + "\n" + alert2);
                        }else{
                            adExp.setMessage(alert1);
                        }

//                        adExp.setMessage("Are you sure..! Do You want to delete it? Notes of this tag will also be deleted.." );

                        adExp.setButton(DialogInterface.BUTTON_POSITIVE, "Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                try {

                                    if (arrlstGridviewBean.get(position).getStr().equals("Tag")) {
                                        tagId = arrlstGridviewBean.get(position).getTagId()+"";
//                                        clsDeleteTag deleteTag = new clsDeleteTag();
//                                        deleteTag.start();
                                        db=new DatabaseHandler(ctx);
                                        String deletedTimeStamp = DateFormat.getDateTimeInstance().format(new Date());


                                        db.deleteRecord(Integer.parseInt(tagId),deletedTimeStamp);
                                        db.deleteNotesFromTag(Integer.parseInt(tagId),deletedTimeStamp);

                                        db.updateUser(GlobalModule.sUserId,deletedTimeStamp);

                                        Intent intent = new Intent(ctx, Tags_Page.class);
                                        ctx.startActivity(intent);
                                        ((Activity) ctx).finish();

                                    } else if (arrlstGridviewBean.get(position).getStr().equals("Note")) {

                                        tagId = arrlstGridviewBean.get(position).getTagId()+"";
                                        noteId = arrlstGridviewBean.get(position).getiNoteId()+"";
//                                        clsDeleteNote deleteNote = new clsDeleteNote();
//                                        deleteNote.start();
                                        db=new DatabaseHandler(ctx);
                                        String deletedTimeStamp = DateFormat.getDateTimeInstance().format(new Date());
//

                                        db.deleteNote1(Integer.parseInt(noteId),deletedTimeStamp);
                                        db.updateUser(GlobalModule.sUserId,deletedTimeStamp);


                                        Intent intent = new Intent(ctx, All_Notes_Page.class);
                                        intent.putExtra("TagId", tagId);
                                        ctx.startActivity(intent);
                                        ((Activity) ctx).finish();
                                    }
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
                    if (arrlstGridviewBean.get(position).getStr().equals("Tag")) {
                        try {
                            Intent intent = new Intent(ctx, All_Notes_Page.class);
                            intent.putExtra("tag", arrlstGridviewBean.get(position).getTagName());
                           Integer tagid=arrlstGridviewBean.get(position).getTagId();
//                            intent.putExtra("TagId", arrlstGridviewBean.get(position).getTagId());
                            intent.putExtra("TagId", tagid+"");
                            ctx.startActivity(intent);
                            ((Activity) ctx).finish();
                        } catch (Exception e) {
                            Toast.makeText(ctx, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    } else if (arrlstGridviewBean.get(position).getStr().equals("Note")) {
                        Intent intent = new Intent(ctx, NewNote.class);
                        intent.putExtra("tag", arrlstGridviewBean.get(position).getTagName());
//                        intent.putExtra("text", arrlstGridviewBean.get(position).getsTag());
                        intent.putExtra("TagId", arrlstGridviewBean.get(position).getTagId()+"");
                        intent.putExtra("NoteId", arrlstGridviewBean.get(position).getiNoteId()+"");
                        intent.putExtra("editnote",arrlstGridviewBean.get(position).getsNote());
                        intent.putExtra("Note", "sUpdateNote");
                        ctx.startActivity(intent);
                        ((Activity) ctx).finish();
                    }

                }
            });


        } catch (Exception e) {
            Toast.makeText(ctx, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        return rootView;
    }

    private class clsDeleteTag extends Thread {

        String aResponse;


        @Override
        public void run() {
            try {
                Webservice com = new Webservice();

                aResponse = com.fnDeleteTag(GlobalModule.sUserId, tagId);


            } catch (Exception ex) {
                Toast.makeText(ctx, ex.getMessage(), Toast.LENGTH_SHORT).show();
            }

            handler.sendEmptyMessage(0);
        }

        private Handler handler = new Handler() {

            @Override
            public void handleMessage(Message msg) {
                try {
                    if (aResponse.equalsIgnoreCase("")) {

                    } else {

                    }

                } catch (Exception ex) {
                    android.support.v7.app.AlertDialog adExp = new android.support.v7.app.AlertDialog.Builder(ctx).create();
                    adExp.setMessage(ex.getMessage());
                    adExp.setButton(DialogInterface.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // TODO Auto-generated method stub
                            ((Activity) ctx).finish();

                        }
                    });

                }


            }
        };
    }


    private class clsDeleteNote extends Thread {

        String aResponse;


        @Override
        public void run() {
            try {
                Webservice com = new Webservice();

                aResponse = com.fnDeleteNote(GlobalModule.sUserId, tagId, noteId);


            } catch (Exception ex) {
                Toast.makeText(ctx, ex.getMessage(), Toast.LENGTH_SHORT).show();
            }

            handler.sendEmptyMessage(0);
        }

        private Handler handler = new Handler() {

            @Override
            public void handleMessage(Message msg) {
                try {
                    if (aResponse.equalsIgnoreCase("")) {

                    } else {

                    }

                } catch (Exception ex) {
                    android.support.v7.app.AlertDialog adExp = new android.support.v7.app.AlertDialog.Builder(ctx).create();
                    adExp.setMessage(ex.getMessage());
                    adExp.setButton(DialogInterface.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // TODO Auto-generated method stub
                            ((Activity) ctx).finish();

                        }
                    });

                }


            }
        };
    }

}
