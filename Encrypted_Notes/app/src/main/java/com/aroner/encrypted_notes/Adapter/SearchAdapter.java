package com.aroner.encrypted_notes.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.aroner.encrypted_notes.Activities.All_Notes_Page;
import com.aroner.encrypted_notes.Activities.Home_Page;
import com.aroner.encrypted_notes.Activities.NewNote;
import com.aroner.encrypted_notes.Activities.Settings_Page;
import com.aroner.encrypted_notes.Activities.Tags_Page;
import com.aroner.encrypted_notes.Bean.GridviewBean;
import com.aroner.encrypted_notes.Bean.TagBean;
import com.aroner.encrypted_notes.DatabaseHandler;
import com.aroner.encrypted_notes.GlobalModule;
import com.aroner.encrypted_notes.R;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by shree on 28-10-2017.
 */

public class SearchAdapter extends ArrayAdapter<TagBean> {
    ArrayList<TagBean> arrlstGridviewBean,arrarylistDrawer;
    Context ctx;
    int resourseId;
    TagBean bean1;
    DatabaseHandler db;
    ArrayList<TagBean> contacts, arrNote;
    String SubstringEncryptKey;

    public SearchAdapter(Context ctx, int resourseId, ArrayList<TagBean> arrlstGridviewBean) {
        super(ctx, resourseId, arrlstGridviewBean);
        this.ctx = ctx;
        this.resourseId = resourseId;
        this.arrlstGridviewBean = arrlstGridviewBean;
    }

    public static class Holder {

        TextView tvItem, tvType;
        ImageView ivImg;
        RelativeLayout rl1,rl2;
    }

    @NonNull
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final SearchAdapter.Holder holder;
        View rootView = convertView;
        try {


            if (rootView == null || rootView.getTag() == null) {
                LayoutInflater inflater = ((Activity) ctx).getLayoutInflater();
                holder = new SearchAdapter.Holder();

                rootView = inflater.inflate(resourseId, parent, false);

                holder.tvItem = (TextView) rootView.findViewById(R.id.tvItem);
                holder.tvType = (TextView) rootView.findViewById(R.id.tvType);
                holder.ivImg = (ImageView) rootView.findViewById(R.id.ivImg);
                holder.rl1 = (RelativeLayout) rootView.findViewById(R.id.rl1);
                holder.rl2 = (RelativeLayout) rootView.findViewById(R.id.rl2);


            } else {
                holder = (SearchAdapter.Holder) rootView.getTag();
            }
            bean1 = arrlstGridviewBean.get(position);

            holder.tvItem.setTextSize(Integer.parseInt(GlobalModule.textSize));
            holder.tvType.setTextSize(Integer.parseInt(GlobalModule.textSize) - 2);
            holder.tvType.setText(arrlstGridviewBean.get(position).getStr());

            if (arrlstGridviewBean.get(position).getStr().equals("Tag")) {

                String textTag = (arrlstGridviewBean.get(position).getTagName()).trim();

                holder.tvItem.setText(textTag);
                Picasso.with(ctx).load(R.drawable.cross).into(holder.ivImg);


            } else if (arrlstGridviewBean.get(position).getStr().equals("Note")) {


                String note = (arrlstGridviewBean.get(position).getsNote()).trim();

                if (note.length() > 26) {

                    String HalfNotes = note.substring(0, 25);
                    StringBuilder sb = new StringBuilder(HalfNotes);
                    sb.append(" ...");
//                                bean.setsNote();


                    holder.tvItem.setText(sb.toString());
                    Picasso.with(ctx).load(R.drawable.edit1).into(holder.ivImg);


                } else {
                    holder.tvItem.setText(note);

                    Picasso.with(ctx).load(R.drawable.edit1).into(holder.ivImg);

                }


//                holder.tvItem.setText(arrlstGridviewBean.get(position).getsNote());
            }


            holder.rl2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (arrlstGridviewBean.get(position).getStr().equals("Tag")) {
//                                        etText.setText(arrlstFilter.get(position).getTagName());

                        try {
                            android.support.v7.app.AlertDialog adExp = new android.support.v7.app.AlertDialog.Builder(ctx).create();

                            String alert1 = "Are you sure..! Do You want to delete it? ";
                            String alert2 = "Notes of this tag will also be deleted.. ";

                            adExp.setMessage(alert1 + "\n" + alert2);


//                        adExp.setMessage("Are you sure..! Do You want to delete it? Notes of this tag will also be deleted.." );

                            adExp.setButton(DialogInterface.BUTTON_POSITIVE, "Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    try {

                                        String createdTimeStamp = DateFormat.getDateTimeInstance().format(new Date());

                                        String tagId = arrlstGridviewBean.get(position).getTagId() + "";
//                                        clsDeleteTag deleteTag = new clsDeleteTag();
//                                        deleteTag.start();
                                        db = new DatabaseHandler(ctx);

                                        String deletedTimeStamp = DateFormat.getDateTimeInstance().format(new Date());

                                        db.deleteRecord(Integer.parseInt(tagId),deletedTimeStamp);


                                        db.deleteNotesFromTag(Integer.parseInt(tagId),deletedTimeStamp);

                                        db.updateUser(GlobalModule.sUserId,createdTimeStamp);

                                        Intent intent=new Intent(ctx,Home_Page.class);
                                        ctx.startActivity(intent);
                                        ((Activity)ctx).finish();
                                        arrlstGridviewBean.remove(arrlstGridviewBean.get(position));
                                        notifyDataSetChanged();

                                        contacts=new ArrayList<TagBean>();
                                        arrNote=new ArrayList<TagBean>();

                                        contacts = db.getAllTag();
                                        arrNote = db.getAllNotesss();
                                        SubstringEncryptKey = GlobalModule.subKey;


                                        for (int i = 0; i < contacts.size(); i++) {
                                            bean1 = new TagBean();
                                            bean1.setTagId(contacts.get(i).getTagId());
                                            bean1.setStr("Tag");
                                            bean1.setTagName(GlobalModule.Decrypt(ctx, SubstringEncryptKey, contacts.get(i).getTagName()));
                                            Home_Page.arrlstTag.add(bean1);
                                        }
                                        TagBean bean;
                                        for (int i = 0; i < arrNote.size(); i++) {
                                            bean = new TagBean();
                                            bean.setTagId(arrNote.get(i).getTagId());
                                            bean.setiNoteId(arrNote.get(i).getiNoteId());
                                            bean.setStr("Note");
                                            bean.setsNote(GlobalModule.Decrypt(ctx, SubstringEncryptKey, arrNote.get(i).getsNote()));
                                            Home_Page.arrlstTag.add(bean);
                                        }
//                                        displayMenu();






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


//                        Intent intent = new Intent(ctx, All_Notes_Page.class);
//                        intent.putExtra("TagId", arrlstGridviewBean.get(position).getTagId() + "");
//                        ctx.startActivity(intent);
//                        ((Activity) ctx).finish();
                    } else if (arrlstGridviewBean.get(position).getStr().equals("Note")) {
//                                        etText.setText(arrlstFilter.get(position).getsNote());

                        Intent intent = new Intent(ctx, NewNote.class);
                        GlobalModule.searchString="";

                        intent.putExtra("TagId", arrlstGridviewBean.get(position).getTagId() + "");
                        intent.putExtra("sUpdateNote", "sUpdateNote");
                        intent.putExtra("Note", "sUpdateNote");
                        String encryptNote = GlobalModule.Encrypt(ctx, GlobalModule.subKey, arrlstGridviewBean.get(position).getsNote());
                        intent.putExtra("editnote", encryptNote);
                        intent.putExtra("NoteId", arrlstGridviewBean.get(position).getiNoteId() + "");
                        ctx.startActivity(intent);
                        ((Activity)ctx).finish();
                    }
                }
            });

            holder.rl1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (arrlstGridviewBean.get(position).getStr().equals("Tag")) {
                        Intent i=new Intent(ctx,Tags_Page.class);
                        GlobalModule.searchString="";
                        i.putExtra("TagId",arrlstGridviewBean.get(position).getTagId()+"");
                        ctx.startActivity(i);
                        ((Activity)ctx).finish();

                    } else if (arrlstGridviewBean.get(position).getStr().equals("Note")){
                        Intent intent = new Intent(ctx, NewNote.class);
                        GlobalModule.searchString="";
                        intent.putExtra("TagId", arrlstGridviewBean.get(position).getTagId() + "");
                        intent.putExtra("sUpdateNote", "sUpdateNote");
                        intent.putExtra("Note", "sUpdateNote");
                        String encryptNote = GlobalModule.Encrypt(ctx, GlobalModule.subKey, arrlstGridviewBean.get(position).getsNote());
                        intent.putExtra("editnote", encryptNote);
                        intent.putExtra("NoteId", arrlstGridviewBean.get(position).getiNoteId() + "");
                        ctx.startActivity(intent);
                        ((Activity)ctx).finish();
                    }
                }
            });




        } catch (Exception e) {
            Toast.makeText(ctx, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        return rootView;
    }




}
