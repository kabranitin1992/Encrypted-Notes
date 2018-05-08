package com.aroner.encrypted_notes.Activities;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.aroner.encrypted_notes.Adapter.TagAdapter;
import com.aroner.encrypted_notes.Bean.GridviewBean;
import com.aroner.encrypted_notes.DatabaseHandler;
import com.aroner.encrypted_notes.GlobalModule;
import com.aroner.encrypted_notes.R;
import com.aroner.encrypted_notes.SaveSharedPreference;
import com.aroner.encrypted_notes.Webservice;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.microedition.khronos.opengles.GL;

public class NewNote extends AppCompatActivity {

    EditText etNote;
    String sTag, sText, sTagId, EncryptTag, sNote, Updatedtext, sSearch,sEditNote;
    Context ctx;
    Button btnSave;
    String SubstringEncryptKey, DecryptTag, sNoteId;
    ArrayList<GridviewBean> arrlstGridViewBean;
    Typeface typeface;
    DatabaseHandler db;
    SaveSharedPreference saveSharedPreference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_new_note);
            ctx = this;
//            getSupportActionBar().hide();
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            String customFont = "Pacifico.ttf";
            saveSharedPreference = new SaveSharedPreference();

            typeface = typeface.createFromAsset(getAssets(), customFont);
            TextView tv = (TextView) toolbar.findViewById(R.id.toolbar_title);
            tv.setTypeface(typeface);

            toolbar.setTitle("");
            setSupportActionBar(toolbar);

//            getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.background));
//            getSupportActionBar().setTitle("Add a Note");

            sSearch = getIntent().getStringExtra("sUpdateNote");
            sNote = getIntent().getStringExtra("Note");
            sNoteId = getIntent().getStringExtra("NoteId");
            sTag = getIntent().getStringExtra("tag");
            sEditNote = getIntent().getStringExtra("editnote");
            sText = getIntent().getStringExtra("text");
            sTagId = getIntent().getStringExtra("TagId");
//           btnSave = (Button) findViewById(R.id.btnSave);
            etNote = (EditText) findViewById(R.id.etNote);
            arrlstGridViewBean = new ArrayList<GridviewBean>();

            etNote.setTextSize(Integer.parseInt(GlobalModule.textSize));



//            etNote.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
            if (sSearch == null) {
                sSearch = "";
            }

//            etNote.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
            if (sNote.equals("sUpdateNote")) {

                SubstringEncryptKey = GlobalModule.subKey;


                DecryptTag = GlobalModule.Decrypt(ctx, SubstringEncryptKey,sEditNote);



                etNote.setText(DecryptTag);
            } else if (sSearch.equals("sUpdateNote")) {

                SubstringEncryptKey = GlobalModule.subKey;

                DecryptTag = GlobalModule.Decrypt(ctx, SubstringEncryptKey,sEditNote);



                etNote.setText(DecryptTag);

            }
//


            etNote.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    try {
//                        Toast.makeText(ctx, s.length() + "", Toast.LENGTH_SHORT).show();
                        if (s.length() % 5 == 0) {

                            if (sNote.equals("sAddNote")) {
                                String Type = "basic";
                                String createdTimeStamp= DateFormat.getDateTimeInstance().format(new Date());

                                db=new DatabaseHandler(ctx);
//                                db.addNote(1,Integer.parseInt(sTagId),Type,etNote.getText().toString(),createdTimeStamp,0,"0");

//
//                                clsAddNotes addNotes = new clsAddNotes();
//                                addNotes.start();
                            } else if (sNote.equals("sUpdateNote")) {

                                db=new DatabaseHandler(ctx);
//                                db.UpdateNote(Integer.parseInt(sNoteId),etNote.getText().toString());
//                                clsUpdateNotes updateNotes = new clsUpdateNotes();
//                                updateNotes.start();
                            } else if (sSearch.equals("sUpdateNote")) {
                                db=new DatabaseHandler(ctx);

//                                db.UpdateNote(Integer.parseInt(sNoteId),etNote.getText().toString());

//                                clsUpdateNotes updateNotes = new clsUpdateNotes();
//                                updateNotes.start();
                            }
                        }

//                        Intent intent = new Intent(ctx, All_Notes_Page.class);
//                        intent.putExtra("tag", sTag);
//                        startActivity(intent);
//                        finish();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private class clsAddNotes extends Thread {

        String aResponse;


        @Override
        public void run() {
            try {
                Webservice com = new Webservice();
                String Type = "basic";
                SaveSharedPreference saveSharedPreference = new SaveSharedPreference();
                SubstringEncryptKey = GlobalModule.subKey;
                String text = etNote.getText().toString();
                EncryptTag = GlobalModule.Encrypt(ctx, SubstringEncryptKey, text);


                aResponse = com.fnAddNotes(GlobalModule.sUserId, sTagId, Type, EncryptTag);


            } catch (Exception ex) {
                Toast.makeText(ctx, ex.getMessage(), Toast.LENGTH_SHORT).show();
            }

            handler.sendEmptyMessage(0);
        }

        private Handler handler = new Handler() {

            @Override
            public void handleMessage(Message msg) {
                try {
                    JSONArray jsonArray = new JSONArray(aResponse);
                    if (aResponse.equalsIgnoreCase("")) {

                    } else {
                        if (jsonArray.getJSONObject(0).has("err")) {
                            Toast.makeText(ctx, jsonArray.getJSONObject(0).getString("err"), Toast.LENGTH_SHORT).show();
                        } else {

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                GridviewBean bean = new GridviewBean();

                                String str = jsonObject.getString("sNote");
                                String str1 = jsonObject.getString("iNoteId");
                                sNoteId = str1;
                                sNote = "sUpdateNote";

//                                DecryptTag = GlobalModule.Decrypt(ctx, SubstringEncryptKey, str);
//                                  etNote.setText(DecryptTag);
                            }


                        }

                    }

                } catch (Exception ex) {
                    android.support.v7.app.AlertDialog adExp = new android.support.v7.app.AlertDialog.Builder(ctx).create();
                    adExp.setMessage(ex.getMessage());
                    adExp.setButton(DialogInterface.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // TODO Auto-generated method stub
                            finish();
                        }
                    });

                }


            }
        };
    }


    private class clsUpdateNotes extends Thread {

        String aResponse;


        @Override
        public void run() {
            try {
                Webservice com = new Webservice();
                String Type = "basic";
                SaveSharedPreference saveSharedPreference = new SaveSharedPreference();
                SubstringEncryptKey = GlobalModule.subKey;
                Updatedtext = etNote.getText().toString();
                EncryptTag = GlobalModule.Encrypt(ctx, SubstringEncryptKey, Updatedtext);

                aResponse = com.fnUpdateNotes(GlobalModule.sUserId, sTagId, Type, sNoteId, EncryptTag);


            } catch (Exception ex) {
                Toast.makeText(ctx, ex.getMessage(), Toast.LENGTH_SHORT).show();
            }

            handler.sendEmptyMessage(0);
        }

        private Handler handler = new Handler() {

            @Override
            public void handleMessage(Message msg) {
                try {
                    JSONArray jsonArray = new JSONArray(aResponse);
                    if (aResponse.equalsIgnoreCase("")) {

                    } else {
                        if (jsonArray.getJSONObject(0).has("err")) {
                            Toast.makeText(ctx, jsonArray.getJSONObject(0).getString("err"), Toast.LENGTH_SHORT).show();
                        } else {

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                GridviewBean bean = new GridviewBean();

                                String str = jsonObject.getString("sNote");
//                                DecryptTag = GlobalModule.Decrypt(ctx, SubstringEncryptKey, str);
//                                  etNote.setText(DecryptTag);
                            }


                        }

                    }

                } catch (Exception ex) {
                    android.support.v7.app.AlertDialog adExp = new android.support.v7.app.AlertDialog.Builder(ctx).create();
                    adExp.setMessage(ex.getMessage());
                    adExp.setButton(DialogInterface.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // TODO Auto-generated method stub
                            finish();
                        }
                    });

                }


            }
        };
    }

    @Override
    public void onBackPressed() {
        try {

            if (sNote.equals("sAddNote")) {
//                clsUpdateNotes updateNotes = new clsUpdateNotes();
//                updateNotes.start();


                    String createdTimeStamp = DateFormat.getDateTimeInstance().format(new Date());
                    String type = "basic";

                    SubstringEncryptKey = GlobalModule.subKey;
                    String text = etNote.getText().toString();

                    if(text.trim().equals("")){
                        Intent intent = new Intent(ctx, All_Notes_Page.class);
                        intent.putExtra("TagId", sTagId+"");
                        startActivity(intent);
                        finish();
                    }else {
                        EncryptTag = GlobalModule.Encrypt(ctx, SubstringEncryptKey, text);


                        db = new DatabaseHandler(ctx);
                        db.addNote(1, Integer.parseInt(sTagId), type, EncryptTag, createdTimeStamp, 0, "0","0");
                        db.updateUser(GlobalModule.sUserId,createdTimeStamp);



//                clsAddNotes addNotes=new clsAddNotes();
//                addNotes.start();
                        Intent intent = new Intent(ctx, All_Notes_Page.class);
                        intent.putExtra("TagId", sTagId+"");
                        startActivity(intent);
                        finish();
                    }

            } else if (sNote.equals("sUpdateNote")) {

                    db = new DatabaseHandler(ctx);
                    SubstringEncryptKey = GlobalModule.subKey;
                    String text = etNote.getText().toString();
                String createdTimeStamp = DateFormat.getDateTimeInstance().format(new Date());

                    if(text.trim().equals("")){
                        String deletedTimeStamp = DateFormat.getDateTimeInstance().format(new Date());

                        db.deleteNote1(Integer.parseInt(sNoteId),deletedTimeStamp);
                        db.updateUser(GlobalModule.sUserId,createdTimeStamp);
                        Intent intent = new Intent(ctx, All_Notes_Page.class);
                        intent.putExtra("TagId", sTagId+"");
                        startActivity(intent);
                        finish();

                    }else {

                        EncryptTag = GlobalModule.Encrypt(ctx, SubstringEncryptKey, text);
                        String currentTime = DateFormat.getDateTimeInstance().format(new Date());


                        db.UpdateNote(Integer.parseInt(sNoteId), EncryptTag,currentTime);
                        db.updateUser(GlobalModule.sUserId,createdTimeStamp);
//                clsUpdateNotes updateNotes = new clsUpdateNotes();
//                updateNotes.start();
                        Intent intent = new Intent(ctx, All_Notes_Page.class);
                        intent.putExtra("TagId", sTagId+"");
                        startActivity(intent);
                        finish();
                    }

            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
