package com.aroner.encrypted_notes.Activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import com.aroner.encrypted_notes.Adapter.TagAdapter;
import com.aroner.encrypted_notes.Bean.TagBean;
import com.aroner.encrypted_notes.DatabaseHandler;
import com.aroner.encrypted_notes.GlobalModule;
import com.aroner.encrypted_notes.R;
import com.aroner.encrypted_notes.Webservice;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by arone on 24-03-2018.
 */

public class SyncPage {


    public static class clsAddData extends Thread {

        String aResponse;
        String string;
        DatabaseHandler db;
        public Context context;
        ArrayList<TagBean> userlst;
        ArrayList<TagBean> taglst;
        ArrayList<TagBean> notelst;
        ProgressDialog pd;

        public clsAddData(Context context, ProgressDialog pd) {
            this.context = context.getApplicationContext();
            this.pd = pd;
        }

        public clsAddData(Context context) {
            this.context = context.getApplicationContext();
//            this.pd = pd;
        }


        @Override
        public void run() {
            try {

                userlst = new ArrayList<TagBean>();
                taglst = new ArrayList<TagBean>();
                notelst = new ArrayList<TagBean>();
                db = new DatabaseHandler(context);

                userlst = db.getAllUser();
                taglst = db.getAllTagForSync();
                notelst = db.getAllNotesssForSync();

                JSONObject MainJsonArray = new JSONObject();
                JSONArray user = new JSONArray();

                for (int i = 0; i < userlst.size(); i++) {
                    JSONObject userObj = new JSONObject();
                    userObj.put("userid", userlst.get(i).getiUserId());
                    userObj.put("email", userlst.get(i).getsEmailId());
                    userObj.put("key", userlst.get(i).getsKey());
                    userObj.put("createdTimeStamp", userlst.get(i).getsCreatedTimeStamp());
                    userObj.put("deleted", userlst.get(i).getiDeleted());
                    userObj.put("SyncTime", userlst.get(i).getsSyncTimeStamp());
                    userObj.put("UpdatedTime", userlst.get(i).getsUpdatedDataTime());
                    user.put(userObj);
                }


//
                JSONArray Tags = new JSONArray();

                for (int i = 0; i < taglst.size(); i++) {
                    JSONObject tagObj = new JSONObject();
                    tagObj.put("tagid", taglst.get(i).getTagId());
                    tagObj.put("userid", taglst.get(i).getiUserId());
                    tagObj.put("tag", taglst.get(i).getTagName());
                    tagObj.put("createdTimeStamp", taglst.get(i).getsCreatedTimeStamp());
                    tagObj.put("deleted", taglst.get(i).getiDeleted());
                    tagObj.put("deletedTimeStamp", taglst.get(i).getsDeletedTimeStamp());
                    tagObj.put("SyncTime", taglst.get(i).getsSyncTimeStamp());
                    tagObj.put("UpdatedTime", taglst.get(i).getsUpdatedDataTime());
                    Tags.put(tagObj);
                }


                JSONArray Notes = new JSONArray();

                for (int i = 0; i < notelst.size(); i++) {
                    JSONObject noteObj = new JSONObject();
                    noteObj.put("noteid", notelst.get(i).getiNoteId());
                    noteObj.put("userid", notelst.get(i).getiUserId());
                    noteObj.put("tagid", notelst.get(i).getTagId());
                    noteObj.put("type", notelst.get(i).getsType());
                    noteObj.put("note", notelst.get(i).getsNote());
                    noteObj.put("createdTimeStamp", notelst.get(i).getsCreatedTimeStamp());
                    noteObj.put("deleted", notelst.get(i).getiDeleted());
                    noteObj.put("deletedTimeStamp", notelst.get(i).getsDeletedTimeStamp());
                    noteObj.put("SyncTime", notelst.get(i).getsSyncTimeStamp());
                    noteObj.put("UpdatedTime", notelst.get(i).getsUpdatedDataTime());
                    Notes.put(noteObj);
                }

                MainJsonArray.put("User", user);
                MainJsonArray.put("Tags", Tags);
                MainJsonArray.put("Notes", Notes);

                string = MainJsonArray.toString();


                Webservice com = new Webservice();

                aResponse = com.fnPutdata(string);


            } catch (Exception ex) {
                Toast.makeText(context, ex.getMessage(), Toast.LENGTH_SHORT).show();
            }

            handler.sendEmptyMessage(0);
        }

        private Handler handler = new Handler() {

            @Override
            public void handleMessage(Message msg) {
                try {
                    if (pd.isShowing())
                        pd.dismiss();
                    if (aResponse.equalsIgnoreCase("")) {

                    } else {
                        JSONObject jsonObject = new JSONObject(aResponse);
                        if (jsonObject.has("err")) {
                            Toast.makeText(context, jsonObject.getString("err"), Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(context, jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();
                        }
                    }


                } catch (Exception ex) {
                    android.support.v7.app.AlertDialog adExp = new android.support.v7.app.AlertDialog.Builder(context).create();
                    adExp.setMessage(ex.getMessage());
                    adExp.setButton(DialogInterface.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // TODO Auto-generated method stub
//                            context.finish();
                        }
                    });

                }


            }
        };
    }


    public static class clsGetData extends Thread {

        String aResponse;
        String string;
        DatabaseHandler db;
        public Context context;
        ArrayList<TagBean> userlst;
        ArrayList<TagBean> taglst;
        String email;
        ArrayList<TagBean> notelst;


        public clsGetData(Context context, String email) {
            this.context = context.getApplicationContext();
            this.email = email;
        }


        @Override
        public void run() {
            try {

                userlst = new ArrayList<TagBean>();
                taglst = new ArrayList<TagBean>();
                notelst = new ArrayList<TagBean>();
                db = new DatabaseHandler(context);

                userlst = db.getAllUser();
                taglst = db.getAllTagForSync();
                notelst = db.getAllNotesssForSync();

                Webservice com = new Webservice();
                aResponse = com.fnGetdata(email);

//                aResponse="{\"User\":[{\"userid\":2,\"email\":\"nskabra92@gmail.com\",\"key\":\"ed412fefbe657d5b4db6a34740eeeeaf\",\"createdTimeStamp\":\"24 Mar 2018 14:48:07\",\"deleted\":0}],\"Tags\":[{\"tagid\":1,\"userid\":1,\"tag\":\"fvmj7GXmXzxxx+iOXOovNg==\",\"createdTimeStamp\":\"24 Mar 2018 14:48:20\",\"deleted\":0,\"deletedTimeStamp\":\"0\"},{\"tagid\":2,\"userid\":1,\"tag\":\"Oylir0ZpFiYPDdrIBMedPA==\",\"createdTimeStamp\":\"24 Mar 2018 14:48:33\",\"deleted\":0,\"deletedTimeStamp\":\"0\"},{\"tagid\":3,\"userid\":1,\"tag\":\"W+7QPgLW9+4Nd28iBwpBvQ==\",\"createdTimeStamp\":\"24 Mar 2018 14:49:26\",\"deleted\":1,\"deletedTimeStamp\":\"24 Mar 2018 15:29:37\"}],\"Notes\":[{\"noteid\":6,\"userid\":1,\"tagid\":2,\"type\":\"basic\",\"note\":\"WsPGhKbR0EOQDH49j3XiPw==\",\"createdTimeStamp\":\"24 Mar 2018 15:31:27\",\"deleted\":0,\"deletedTimeStamp\":\"0\"},{\"noteid\":5,\"userid\":1,\"tagid\":2,\"type\":\"basic\",\"note\":\"UBknGi+XFeqYza6Wd+mfvw==\",\"createdTimeStamp\":\"24 Mar 2018 15:31:19\",\"deleted\":0,\"deletedTimeStamp\":\"0\"},{\"noteid\":4,\"userid\":1,\"tagid\":1,\"type\":\"basic\",\"note\":\"z9O4yD7c9ToLZHGrDEz5Yw==\",\"createdTimeStamp\":\"24 Mar 2018 15:30:37\",\"deleted\":0,\"deletedTimeStamp\":\"0\"}]}";

//                aResponse = com.fnGetdata();


            } catch (Exception ex) {
                Toast.makeText(context, ex.getMessage(), Toast.LENGTH_SHORT).show();
            }

            handler.sendEmptyMessage(0);
        }

        private Handler handler = new Handler() {

            @Override
            public void handleMessage(Message msg) {
                try {
                    JSONObject mainObj = new JSONObject(aResponse);
                    if (mainObj.has("err")) {
                        GlobalModule.SyncGetData = "false";

                    } else {
                        GlobalModule.SyncGetData = "true";

                        JSONArray user = mainObj.getJSONArray("User");
                        for (int i = 0; i < user.length(); i++) {
                            JSONObject userObj = user.getJSONObject(i);
                            TagBean bean = new TagBean();
                            bean.setiUserId(Integer.parseInt(userObj.getString("iUserId")));
                            bean.setsEmailId(userObj.getString("sEmailId"));
                            bean.setsKey(userObj.getString("sKey"));
                            bean.setsCreatedTimeStamp(userObj.getString("createdTimeStamp"));
                            bean.setiDeleted(Integer.parseInt(userObj.getString("deleted")));
                            bean.setsSyncTimeStamp(userObj.getString("sSyncTimeStamp"));
                            bean.setsUpdatedDataTime(userObj.getString("sUpdatedDateTime"));
                            userlst.add(bean);


                            db.addUser(Integer.parseInt(userObj.getString("iUserId")),
                                    userObj.getString("sEmailId"),
                                    userObj.getString("sKey"),
                                    userObj.getString("sCreatedTimestamp"),
                                    Integer.parseInt(userObj.getString("iDeleted")),
                                    userObj.getString("sSyncTimeStamp"),
                                    userObj.getString("sUpdatedDateTime"));
                        }

                        JSONArray tags = mainObj.getJSONArray("Tags");
                        for (int i = 0; i < user.length(); i++) {
                            JSONObject tagbj = tags.getJSONObject(i);
                            TagBean bean = new TagBean();
//                            bean.setTagId(Integer.parseInt(tagbj.getString("tagid")));
                            bean.setiUserId(Integer.parseInt(tagbj.getString("iUserId")));
                            bean.setTagName(tagbj.getString("sTag"));
                            bean.setsCreatedTimeStamp(tagbj.getString("sCreatedTimestamp"));
                            bean.setiDeleted(Integer.parseInt(tagbj.getString("iDeleted")));
                            bean.setsDeletedTimeStamp(tagbj.getString("sDeletedTimestamp"));
                            bean.setsSyncTimeStamp(tagbj.getString("sSyncTimeStamp"));
                            bean.setsUpdatedDataTime(tagbj.getString("sUpdatedDateTime"));
                            taglst.add(bean);

                            db.addTag(Integer.parseInt(tagbj.getString("userid")),
                                    tagbj.getString("tag"),
                                    tagbj.getString("createdTimeStamp"),
                                    Integer.parseInt(tagbj.getString("deleted")),
                                    tagbj.getString("deletedTimeStamp"),
                                    tagbj.getString("sSyncTimeStamp"));

                        }

//                        addTag(Integer UserId,String TagName,String CreatedTimeStamp,Integer deleted,String DeletedTimeStamp)
                        JSONArray notes = mainObj.getJSONArray("Notes");
                        for (int i = 0; i < user.length(); i++) {
                            JSONObject noteObj = notes.getJSONObject(i);
                            TagBean bean = new TagBean();
//                            bean.setiNoteId(Integer.parseInt(noteObj.getString("noteid")));
                            bean.setiUserId(Integer.parseInt(noteObj.getString("iUserId")));
                            bean.setTagId(Integer.parseInt(noteObj.getString("iTagId")));
                            bean.setsType(noteObj.getString("type"));
                            bean.setsNote(noteObj.getString("note"));
                            bean.setsCreatedTimeStamp(noteObj.getString("createdTimeStamp"));
                            bean.setiDeleted(Integer.parseInt(noteObj.getString("deleted")));
                            bean.setsDeletedTimeStamp(noteObj.getString("deletedTimeStamp"));
                            bean.setsSyncTimeStamp(noteObj.getString("sSyncTimeStamp"));
                            bean.setsUpdatedDataTime(noteObj.getString("sUpdatedDateTime"));

                            notelst.add(bean);

                            db.addNote(Integer.parseInt(noteObj.getString("userid")),
                                    Integer.parseInt(noteObj.getString("tagid")),
                                    noteObj.getString("sType"),
                                    noteObj.getString("sNote"),
                                    noteObj.getString("sCreatedTimestamp"),
                                    Integer.parseInt(noteObj.getString("iDeleted")),
                                    noteObj.getString("sDeletedTimestamp"),
                                    noteObj.getString("sSyncTimeStamp"));


                        }


                    }


                } catch (Exception ex) {
                    android.support.v7.app.AlertDialog adExp = new android.support.v7.app.AlertDialog.Builder(context).create();
                    adExp.setMessage(ex.getMessage());
                    adExp.setButton(DialogInterface.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // TODO Auto-generated method stub
//                            context.finish();
                        }
                    });

                }


            }
        };
    }


}
