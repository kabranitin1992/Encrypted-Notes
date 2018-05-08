package com.aroner.encrypted_notes.Activities;

import android.Manifest;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.DragEvent;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.aroner.encrypted_notes.Adapter.GridviewAdapter;
import com.aroner.encrypted_notes.Bean.GridviewBean;
import com.aroner.encrypted_notes.Bean.TagBean;
import com.aroner.encrypted_notes.DatabaseHandler;
import com.aroner.encrypted_notes.GlobalModule;
import com.aroner.encrypted_notes.R;
import com.aroner.encrypted_notes.SaveSharedPreference;
import com.aroner.encrypted_notes.Webservice;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;
import java.util.regex.Pattern;

public class WelcomePage extends AppCompatActivity {
    TextView tvTitle;
    GridView gvText;
    GridviewAdapter adapter;
    EditText etKey;
    Context ctx;
    TextView ivNext;
    Typeface typeface;
    String key;
    ArrayList<TagBean> userArray;
    DatabaseHandler db;
    char[] chars;
    String SubstringEncryptKey;
    String generatedEncryptedKey;
    ArrayList<TagBean> userlist1;
    ArrayList<TagBean> userlist2;
    ArrayList<TagBean> userlist;
    StringBuilder stringBuilder;
    String createdTimeStamp;
    SharedPreferences msharedPreferences;
    public static String UserID = "useridKey";
    public static String Key = "Key";
    public static String EncryptedKey = "EncryptedKey";
    public static String textSize = "textSize";
    public static String spclkey = "spclkey";
    //    GestureDetectorCompat detector;
    ArrayList<String> arrlstGridviewBean;

    SaveSharedPreference saveSharedPreference;
    ProgressDialog pd;
    ArrayList<TagBean> userlst;
    ArrayList<TagBean> taglst;
    String email;
    ArrayList<TagBean> notelst;
//    private static final int SWIPE_THRESHOLD = 100;
//    private static final int SWIPE_VELOCITY_THRESHOLD = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            ctx = this;
            saveSharedPreference = new SaveSharedPreference();
            userArray = new ArrayList<TagBean>();
            userlst = new ArrayList<TagBean>();
            db = new DatabaseHandler(ctx);
            UserID = saveSharedPreference.getUserId(ctx);
            Key = saveSharedPreference.getPlainKey(ctx);
            EncryptedKey = saveSharedPreference.getEncryptedKey(ctx);
            textSize = saveSharedPreference.getPREF_textSize(ctx);
            userlist1 = new ArrayList<TagBean>();
            userlist2 = new ArrayList<TagBean>();

//            textSize=saveSharedPreference.ge
//            UserID.length()=0;
            if (UserID.length() > 0) {
                Pattern gmailPattern = Patterns.EMAIL_ADDRESS;
                Account[] accounts = AccountManager.get(ctx).getAccounts();
                for (Account account : accounts) {
                    if (gmailPattern.matcher(account.name).matches()) {
                        email = account.name;
                    }
                }
                GlobalModule.textSize = textSize;

                GlobalModule.sUserId = UserID;
                GlobalModule.sSmallKey = saveSharedPreference.getSubstringEncryptKey(ctx);
                GlobalModule.subKey = SaveSharedPreference.getSubEncryptKey(ctx);


                userlst = db.getUser(email);


                GlobalModule.clSync clSync = new GlobalModule.clSync(ctx, userlst);
                clSync.start();

                Intent intent = new Intent(ctx, Home_Page.class);
//                            intent.putExtra("key", key);
//                            intent.putExtra("emailId", email);
                startActivity(intent);
                finish();


//            clsGetProfile profile = new clsGetProfile();
//            profile.start();
            } else {

                Pattern gmailPattern = Patterns.EMAIL_ADDRESS;
                Account[] accounts = AccountManager.get(ctx).getAccounts();
                for (Account account : accounts) {
                    if (gmailPattern.matcher(account.name).matches()) {
                        email = account.name;
                    }
                }
                userlst = new ArrayList<TagBean>();
                taglst = new ArrayList<TagBean>();
                notelst = new ArrayList<TagBean>();
//                db = new DatabaseHandler(context);

                userlist = db.getAllUser();
                taglst = db.getAllTagForSync();
                notelst = db.getAllNotesssForSync();


                pd = new ProgressDialog(ctx);
                pd.setMessage("Syncronizing...");
                pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                pd.show();
                clsGetData getData = new clsGetData();
                getData.start();


            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public String get_SHA_512_SecurePassword(String passwordToHash) {
        String generatedPassword = null;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-512");
            byte[] bytes = md.digest(passwordToHash.getBytes("UTF-8"));
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < bytes.length; i++) {
                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }
            generatedPassword = sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        Toast.makeText(ctx, generatedPassword.toString(), Toast.LENGTH_SHORT).show();
        return generatedPassword;

    }


    public class clsGetData extends Thread {

        String aResponse;
        String string;
        //        DatabaseHandler db;
        public Context context;


        @Override
        public void run() {
            try {


                Webservice com = new Webservice();
                aResponse = com.fnGetdata(email);
//                aResponse="{\"err\":\"\"}";


            } catch (Exception ex) {
                Toast.makeText(context, ex.getMessage(), Toast.LENGTH_SHORT).show();
            }

            handler.sendEmptyMessage(0);
        }

        private Handler handler = new Handler() {

            @Override
            public void handleMessage(Message msg) {
                try {
                    pd.dismiss();
                    JSONObject mainObj = new JSONObject(aResponse);
                    if (mainObj.has("err")) {
                        GlobalModule.SyncGetData = "false";
                        setContentView(R.layout.activity_welcome_page);
                        etKey = (EditText) findViewById(R.id.etKey);
                        getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.background));

                        arrlstGridviewBean = new ArrayList<String>();
                        tvTitle = (TextView) findViewById(R.id.tvTitle);
                        tvTitle.getBackground().setAlpha(120);


//        etKey.getBackground().setAlpha(120);

                        gvText = (GridView) findViewById(R.id.gvText);
                        String customFont = "Pacifico.ttf";

                        typeface = Typeface.createFromAsset(getAssets(), customFont);
                        TextView tvName1 = (TextView) findViewById(R.id.tvName1);
                        tvName1.setTypeface(typeface);

                        ivNext = (TextView) findViewById(R.id.ivNext);
//        String customFontq = "bold.ttf";
//        typeface= Typeface.createFromAsset(getAssets(), customFont);
//        TextView tvName2 = (TextView) findViewById(R.id.tvName2);
//        tvName2.setTypeface(typeface);

                        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            etKey.setEnabled(false);
                            chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789".toCharArray();
                            Random random = new Random();
                            for (int i = 0; i < 91; i++) {
                                char c1 = chars[random.nextInt(chars.length)];
                                arrlstGridviewBean.add(c1 + "");
                            }

                            adapter = new GridviewAdapter(ctx, R.layout.gridview_items, arrlstGridviewBean);
                            gvText.setAdapter(adapter);
                            adapter.notifyDataSetChanged();

                            stringBuilder = new StringBuilder();

                            gvText.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

//                stringBuilder.append();
                                    etKey.append(arrlstGridviewBean.get(position));

//                Toast.makeText(ctx, stringBuilder.toString(), Toast.LENGTH_SHORT).show();
                                    key = etKey.getText().toString();
                                }
                            });


                        } else {
                            etKey.setEnabled(true);
                            gvText.setVisibility(View.GONE);
                        }


                        if (ContextCompat.checkSelfPermission(ctx, Manifest.permission.GET_ACCOUNTS) != PackageManager.PERMISSION_GRANTED) {
                            String[] permissions = {Manifest.permission.GET_ACCOUNTS};
                            ActivityCompat.requestPermissions(((Activity) context), permissions, 1);
                        }


                        ivNext.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (ContextCompat.checkSelfPermission(ctx, Manifest.permission.GET_ACCOUNTS) != PackageManager.PERMISSION_GRANTED) {
                                    String[] permissions = {Manifest.permission.GET_ACCOUNTS};
                                    ActivityCompat.requestPermissions((Activity) ctx, permissions, 1);
                                } else {
                                    try {
                                        key = etKey.getText().toString();
                                        generatedEncryptedKey = get_SHA_512_SecurePassword(key);


                                        SubstringEncryptKey = generatedEncryptedKey.substring(0, 32);
                                        spclkey = SubstringEncryptKey;


                                        Pattern gmailPattern = Patterns.EMAIL_ADDRESS;
                                        Account[] accounts = AccountManager.get(ctx).getAccounts();
                                        for (Account account : accounts) {
                                            if (gmailPattern.matcher(account.name).matches()) {
                                                email = account.name;
                                            }
                                        }


                                        createdTimeStamp = DateFormat.getDateTimeInstance().format(new Date());

                                        pd = new ProgressDialog(ctx);
                                        pd.setMessage("Registering...");
                                        pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                                        pd.show();

                                        clsRegister register = new clsRegister();
                                        register.start();


//
//
                                    } catch (Exception e) {
                                        Toast.makeText(ctx, e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }

                            }
                        });

                    } else {
                        GlobalModule.SyncGetData = "true";
                        String currenttime = DateFormat.getDateTimeInstance().format(new Date());


                        JSONArray user = mainObj.getJSONArray("User");
                        for (int i = 0; i < user.length(); i++) {
                            JSONObject userObj = user.getJSONObject(i);
                            TagBean bean = new TagBean();
                            bean.setiUserId(Integer.parseInt(userObj.getString("iUserId")));
                            bean.setsEmailId(userObj.getString("sEmailId"));
                            bean.setsKey(userObj.getString("sKey"));
                            bean.setsCreatedTimeStamp(userObj.getString("sCreatedTimestamp"));
                            bean.setiDeleted(Integer.parseInt(userObj.getString("iDeleted")));
                            bean.setsSyncTimeStamp(currenttime);
                            userlist.add(bean);

//                            if(db.getUser(email) != null){
//                                db.UpdateUserSync(userObj.getString("sSyncTimeStamp"),Integer.parseInt(userObj.getString("iUserId")));
//                            }else{
                            db.addUser(userObj.getInt("iUserId"),
                                    userObj.getString("sEmailId"),
                                    userObj.getString("sKey"),
                                    userObj.getString("sCreatedTimestamp"),
                                    Integer.parseInt(userObj.getString("iDeleted")), currenttime, currenttime);
//                                        userObj.getString("sSyncTimeStamp"));
//                            }

                            if (userObj.getString("sEmailId").equalsIgnoreCase(email)) {
                                SubstringEncryptKey = userObj.getString("sKey");
                                GlobalModule.subKey = SubstringEncryptKey;
                                GlobalModule.sUserId = userObj.getString("iUserId");
                            }


                        }


                        userlist1 = db.getAllUser();
                        userlist2 = db.getUser(email);

                        if (mainObj.getString("Tags").equals("")) {

                        } else {
                            JSONArray tags = mainObj.getJSONArray("Tags");
                            for (int i = 0; i < tags.length(); i++) {
                                JSONObject tagbj = tags.getJSONObject(i);
                                TagBean bean = new TagBean();
//                            bean.setTagId(Integer.parseInt(tagbj.getString("tagid")));
                                bean.setiUserId(Integer.parseInt(tagbj.getString("iUserId")));
                                bean.setTagName(tagbj.getString("sTag"));
                                bean.setsCreatedTimeStamp(tagbj.getString("sCreatedTimestamp"));
                                bean.setiDeleted(Integer.parseInt(tagbj.getString("iDeleted")));
                                bean.setsDeletedTimeStamp(tagbj.getString("sDeletedTimestamp"));
                                bean.setsSyncTimeStamp(currenttime);
                                taglst.add(bean);


                                db.addTag(Integer.parseInt(tagbj.getString("iUserId")),
                                        tagbj.getString("sTag"),
                                        tagbj.getString("sCreatedTimestamp"),
                                        Integer.parseInt(tagbj.getString("iDeleted")),
                                        tagbj.getString("sDeletedTimestamp"),
                                        currenttime);

                            }
                        }

//                        addTag(Integer UserId,String TagName,String CreatedTimeStamp,Integer deleted,String DeletedTimeStamp)

                        if (mainObj.getString("Notes").equalsIgnoreCase("")) {

                        } else {
                            JSONArray notes = mainObj.getJSONArray("Notes");
                            for (int i = 0; i < notes.length(); i++) {
                                JSONObject noteObj = notes.getJSONObject(i);
                                TagBean bean = new TagBean();
//                            bean.setiNoteId(Integer.parseInt(noteObj.getString("noteid")));
                                bean.setiUserId(Integer.parseInt(noteObj.getString("iUserId")));
                                bean.setTagId(Integer.parseInt(noteObj.getString("iTagId")));
                                bean.setsType(noteObj.getString("sType"));
                                bean.setsNote(noteObj.getString("sNote"));
                                bean.setsCreatedTimeStamp(noteObj.getString("sCreatedTimestamp"));
                                bean.setiDeleted(Integer.parseInt(noteObj.getString("iDeleted")));
                                bean.setsDeletedTimeStamp(noteObj.getString("sDeletedTimestamp"));
                                bean.setsSyncTimeStamp(currenttime);

                                notelst.add(bean);

                                db.addNote(Integer.parseInt(noteObj.getString("iUserId")),
                                        Integer.parseInt(noteObj.getString("iTagId")),
                                        noteObj.getString("sType"),
                                        noteObj.getString("sNote"),
                                        noteObj.getString("sCreatedTimestamp"),
                                        Integer.parseInt(noteObj.getString("iDeleted")),
                                        noteObj.getString("sDeletedTimestamp"), currenttime);
//                                    noteObj.getString("sSyncTimeStamp"));


                            }

                        }

                        saveSharedPreference = new SaveSharedPreference();

                        saveSharedPreference.setSubstringEncryptKey(ctx, SubstringEncryptKey);


                        GlobalModule.subKey = saveSharedPreference.getSubEncryptKey(ctx);

//                        SubstringEncryptKey = generatedEncryptedKey.substring(0, 32);
                        spclkey = SubstringEncryptKey;


//                        saveSharedPreference.setPlainKey(ctx, key);
                        saveSharedPreference.setUserId(ctx, GlobalModule.sUserId);
                        saveSharedPreference.setPREF_textSize(ctx, 16 + "");
                        textSize = saveSharedPreference.getPREF_textSize(ctx);


                        GlobalModule.textSize = textSize;

                        ArrayList<TagBean> arrayList = db.getAllNotesss();
                        ArrayList<TagBean> arrayList1 = db.getAllTag();

                        db.DeleteUser(currenttime,Integer.parseInt(GlobalModule.sUserId));
                        db.DeleteTag(currenttime,Integer.parseInt(GlobalModule.sUserId));
                        db.DeleteNote(currenttime,Integer.parseInt(GlobalModule.sUserId));

                        Intent intent = new Intent(ctx, Home_Page.class);
//                            intent.putExtra("key", key);
                        intent.putExtra("emailId", email);
                        startActivity(intent);
                        finish();


//                        Integer Userid,Integer TagId,String type,String note,
//                                String CreatedTimeStamp,Integer deleted,String DeletedTimeStamp
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


    private class clsRegister extends Thread {

        String aResponse;

        @Override
        public void run() {
            try {
                Webservice com = new Webservice();
                Pattern gmailPattern = Patterns.EMAIL_ADDRESS;
                Account[] accounts = AccountManager.get(ctx).getAccounts();
                for (Account account : accounts) {
                    if (gmailPattern.matcher(account.name).matches()) {
                        email = account.name;
                    }
                }
                aResponse = com.fnRegister(email, SubstringEncryptKey);


            } catch (Exception ex) {
                Toast.makeText(ctx, ex.getMessage(), Toast.LENGTH_SHORT).show();
            }

            handler.sendEmptyMessage(0);
        }

        private Handler handler = new Handler() {

            @Override
            public void handleMessage(Message msg) {
                try {
                    pd.dismiss();
                    if (aResponse.equalsIgnoreCase("")) {

                    } else {
                        JSONArray jsonArray = new JSONArray(aResponse);
//                            Toast.makeText(ctx, jsonArray.getJSONObject(0).getString("msg"), Toast.LENGTH_SHORT).show();
                        JSONObject userObj = jsonArray.getJSONObject(0);
                        GlobalModule.sUserId = userObj.getString("iUserId");

                        SaveSharedPreference saveSharedPreference = new SaveSharedPreference();
                        saveSharedPreference.setEncryptedKey(ctx, generatedEncryptedKey);
                        saveSharedPreference.setSubstringEncryptKey(ctx, SubstringEncryptKey);
                        saveSharedPreference.setPlainKey(ctx, key);
                        saveSharedPreference.setUserId(ctx, GlobalModule.sUserId);

                        db = new DatabaseHandler(ctx);
                        db.addUser(Integer.parseInt(GlobalModule.sUserId), email, SubstringEncryptKey, createdTimeStamp, 0, "0", createdTimeStamp);
                        ArrayList<TagBean> userlist1;
                        ArrayList<TagBean> userlist2;
                        userlist1 = new ArrayList<TagBean>();
                        userlist2 = new ArrayList<TagBean>();
                        userlist1 = db.getAllUser();
                        userlist2 = db.getUser(email);
//                                userArray = db.getUser(emai_Id);


                        userlst = db.getUser(email);
                        saveSharedPreference = new SaveSharedPreference();
                        saveSharedPreference.setEncryptedKey(ctx, generatedEncryptedKey);

                        saveSharedPreference.setSubstringEncryptKey(ctx, SubstringEncryptKey);
                        saveSharedPreference.setSubEncryptKey(ctx, spclkey);

                        GlobalModule.subKey = saveSharedPreference.getSubEncryptKey(ctx);


                        saveSharedPreference.setPlainKey(ctx, key);
                        saveSharedPreference.setUserId(ctx, GlobalModule.sUserId);
                        saveSharedPreference.setPREF_textSize(ctx, 16 + "");
                        textSize = saveSharedPreference.getPREF_textSize(ctx);


                        GlobalModule.textSize = textSize;

//                        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
//                        SharedPreferences.Editor editor = prefs.edit();
//                        editor.putString("NameOfShared", "Value");
//                        editor.commit();

                        Intent intent = new Intent(ctx, Home_Page.class);
//                            intent.putExtra("key", key);
                        intent.putExtra("emailId", email);
                        startActivity(intent);
                        finish();


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

}
