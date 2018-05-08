package com.aroner.encrypted_notes.Activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.SyncStateContract;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.aroner.encrypted_notes.Adapter.DrawerAdapter;
import com.aroner.encrypted_notes.Adapter.NewTagAdapter;
import com.aroner.encrypted_notes.Adapter.TagAdapter;
import com.aroner.encrypted_notes.Bean.GridviewBean;
import com.aroner.encrypted_notes.Bean.TagBean;
import com.aroner.encrypted_notes.DatabaseHandler;
import com.aroner.encrypted_notes.GlobalModule;
import com.aroner.encrypted_notes.R;
import com.aroner.encrypted_notes.SaveSharedPreference;
import com.aroner.encrypted_notes.Webservice;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Tags_Page extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    Typeface typeface;
    ListView lvTags, lvDrawerItem;
    Context ctx;
    String sTagId;
    EditText etAddTag;
    Button btnSave,btnAdd;
    TagAdapter adapter;
    String SubstringEncryptKey;
    String EncryptTag,DecryptTag;
    ProgressDialog pd;
    NewTagAdapter adapter1;
    DatabaseHandler db;
    ArrayList<TagBean> contacts;
    TagBean tagBean;
    NavigationView navigationView;
    ArrayList<TagBean> arrlstGridViewBean;
    ArrayList<TagBean> arrlstTagBean;
    ArrayList<TagBean> arrarylistDrawer;
    DrawerAdapter drawerAdapter;
    SharedPreferences msharedPreferences;
    TagBean bean;
    TextView tvTitle;
    public static String PlainKey;
    public static String EncryptedKey;
    SaveSharedPreference saveSharedPreference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_tags__page);

            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            lvTags = (ListView) findViewById(R.id.lvTags);
            db=new DatabaseHandler(ctx);
             bean=new TagBean();

            etAddTag = (EditText) findViewById(R.id.etAddTag);

            btnAdd = (Button) findViewById(R.id.btnAdd);

            saveSharedPreference = new SaveSharedPreference();
            contacts=new ArrayList<TagBean>();

            arrlstGridViewBean = new ArrayList<TagBean>();
            arrlstTagBean = new ArrayList<TagBean>();
            ctx = this;
            String customFont = "Pacifico.ttf";

            typeface = typeface.createFromAsset(getAssets(), customFont);
            TextView tv = (TextView) toolbar.findViewById(R.id.toolbar_title);
            tv.setTypeface(typeface);

            toolbar.setTitle("");
            setSupportActionBar(toolbar);

            tvTitle = (TextView) findViewById(R.id.tvTitle);

            etAddTag.setTextSize(Integer.parseInt(GlobalModule.textSize));
            btnAdd.setTextSize(Integer.parseInt(GlobalModule.textSize));
            tvTitle.setTextSize(Integer.parseInt(GlobalModule.textSize));


            db=new DatabaseHandler(ctx);


            contacts = db.getAllTag();


//            bean1.setStr("Tag");
//            contacts.add(bean1);

            adapter= new TagAdapter(Tags_Page.this, R.layout.tag_items, contacts);
            lvTags.setAdapter(adapter);
            displayMenu();



//            db.getAllTaggs();
//            Cursor cursor=db.getAllTags();

//            Toast.makeText(ctx,cursor.getCount() , Toast.LENGTH_SHORT).show();


//            pd = new ProgressDialog(ctx);
//            pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
//            pd.setMessage("Getting Tags...");
//            pd.show();
//            clsGetTag getTag = new clsGetTag();
//            getTag.start();


//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                    this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
            drawer.setDrawerListener(toggle);
            toggle.syncState();




            NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
            navigationView.setItemTextColor(ColorStateList.valueOf(Color.WHITE));

//            navigationView.setNavigationItemSelectedListener(this);


            View hView = navigationView.getHeaderView(0);
            TextView tvNavHeaderText = (TextView) hView.findViewById(R.id.tvNavHeaderText);
            tvNavHeaderText.setTypeface(typeface);

//        tvTitle.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent=new Intent(Tags_Page.this,Settings_Page.class);
//                startActivity(intent);
//            }
//        });



            btnAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                         saveSharedPreference = new SaveSharedPreference();
                        SubstringEncryptKey = GlobalModule.subKey;



                        String text = (etAddTag.getText().toString()).trim();


                        if(text.equals("")){
                            Toast.makeText(ctx, "Write something..", Toast.LENGTH_SHORT).show();

                        }else {


                            EncryptTag = GlobalModule.Encrypt(ctx, SubstringEncryptKey, text);

                            String createdTimeStamp = DateFormat.getDateTimeInstance().format(new Date());


//                        Integer Tagid,String UserId,String TagName,String CreatedTimeStamp,String deleted,String DeletedTimeStamp
                            db = new DatabaseHandler(ctx);

                            db.addTag(1, EncryptTag, createdTimeStamp, 0, "0","0");
                            db.updateUser(GlobalModule.sUserId,createdTimeStamp);
                            Intent i = new Intent(ctx, Tags_Page.class);
                            startActivity(i);
                            finish();

//                        db.addTag(1,etAddTag.getText().toString(),createdTimeStamp,0,0);

//                        lvTags.setAdapter(new TagAdapter(db.addTag(new TagBean(tagBean.getTagId(),tagBean.getiUserId(),tagBean.getTagName(),
//                                tagBean.getCreatedTimestamp(),tagBean.getDeletedTimestamp(),tagBean.getiDeleted()));

//                        final TagAdapter adapter = new TagAdapter(this,
//                                android.R.layout.simple_list_item_1,
//                                names,
//                                new String[] {"Name"},
//                                new int[] {android.R.id.text1});//this is blank default id
//                        lvTags.setAdapter(adapter);


//                        clsAddTag tag = new clsAddTag();
//                        tag.start();
                            etAddTag.setText("");
                        }



                    } catch (Exception e) {
                        Toast.makeText(ctx, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } catch (Exception e) {
            Toast.makeText(ctx, e.getMessage(), Toast.LENGTH_SHORT).show();
//            e.printStackTrace();
        }


    }



    private class clsAddTag extends Thread {

        String aResponse;


        @Override
        public void run() {
            try {
                Webservice com = new Webservice();

                aResponse = com.fnAddTag(GlobalModule.sUserId, EncryptTag);


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
                        JSONObject userObj = jsonArray.getJSONObject(0);
                        arrlstGridViewBean=new ArrayList<TagBean>();
                        for(int i=0;i<jsonArray.length();i++){
                            TagBean bean=new TagBean();
                            TagBean tagBean=new TagBean();
                            String tag=jsonArray.getJSONObject(i).getString("sTag");
                            String TagId=jsonArray.getJSONObject(i).getString("iTagId");
                            DecryptTag = GlobalModule.Decrypt(ctx, SubstringEncryptKey, tag);
                            bean.setTagName(DecryptTag);
                            bean.setTagId(Integer.parseInt(TagId));
                            bean.setStr("Tag");

                            tagBean.setTagName(DecryptTag);
                            tagBean.setTagId(Integer.parseInt(TagId));
                            arrlstTagBean.add(tagBean);
                            arrlstGridViewBean.add(bean);
                        }



                        adapter = new TagAdapter(Tags_Page.this, R.layout.tag_items, arrlstGridViewBean);
                        lvTags.setAdapter(adapter);
                        adapter.notifyDataSetChanged();



                        displayMenu();



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

    private class clsGetTag extends Thread {

        String aResponse;


        @Override
        public void run() {
            try {
                Webservice com = new Webservice();

                aResponse = com.fnGetTag(GlobalModule.sUserId);


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
                        JSONObject userObj = jsonArray.getJSONObject(0);


                        SubstringEncryptKey = GlobalModule.subKey;

                        for(int i=0;i<jsonArray.length();i++){

                            TagBean bean=new TagBean();
                            String tag=jsonArray.getJSONObject(i).getString("sTag");
                            sTagId =jsonArray.getJSONObject(i).getString("iTagId");
                            DecryptTag = GlobalModule.Decrypt(ctx, SubstringEncryptKey, tag);
                            bean.setTagName(DecryptTag);
                            bean.setTagId(Integer.parseInt(sTagId));
                            bean.setStr("Tag");
                            arrlstGridViewBean.add(bean);
                        }

                        adapter = new TagAdapter(Tags_Page.this, R.layout.tag_items, arrlstGridViewBean);
                        lvTags.setAdapter(adapter);
                        displayMenu();

                        lvTags.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                Intent intent = new Intent(ctx, All_Notes_Page.class);
                                intent.putExtra("tag", arrlstGridViewBean.get(position).getsText());
                                intent.putExtra("TagId", sTagId+"");
                                startActivity(intent);
                                finish();
                            }
                        });



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


    private void displayMenu() {

        try {

            TagBean bean = new TagBean();

            arrarylistDrawer = new ArrayList<TagBean>();
            navigationView = (NavigationView) findViewById(R.id.nav_view);
            ListView lvdrawer = (ListView) navigationView.findViewById(R.id.lvDrawerItem);
            TextView tvMenu = (TextView) navigationView.findViewById(R.id.tvMenu);

            String customFont = "Pacifico.ttf";
            typeface = typeface.createFromAsset(getAssets(), customFont);

            tvMenu.setTypeface(typeface);

            tvMenu.setTextSize(Integer.parseInt(GlobalModule.textSize));

            final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

//        TextView tvTextView=(TextView)findViewById(R.id.tvDrawerItem);
//        ImageView ivIcon=(ImageView) findViewById(R.id.ivIcon);


            bean.setiIconImg(0);
            bean.setsTitle("All Tag");
            arrarylistDrawer.add(bean);

            bean = new TagBean();
            bean.setiIconImg(R.drawable.add);
            bean.setsTitle("Add Tag");
            arrarylistDrawer.add(bean);


            for (int i = 0; i < contacts.size(); i++) {
                bean = new TagBean();
                SubstringEncryptKey = GlobalModule.subKey;
                String str = GlobalModule.Decrypt(ctx, SubstringEncryptKey, contacts.get(i).getTagName());
                bean.setsTitle(str);
                bean.setTagId(contacts.get(i).getTagId());
                bean.setiIconImg(R.drawable.arrow_it_is_white);
                arrarylistDrawer.add(bean);
            }

            bean = new TagBean();
            bean.setiIconImg(R.drawable.gear01);
            bean.setsTitle("Settings");
            arrarylistDrawer.add(bean);

            drawerAdapter = new DrawerAdapter(ctx, R.layout.drawer_lv_items, arrarylistDrawer);
            lvdrawer.setAdapter(drawerAdapter);

            lvdrawer.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    if (arrarylistDrawer.get(position).getsTitle().equalsIgnoreCase("All Tag")) {

                        Intent intent = new Intent(ctx, Tags_Page.class);
                        startActivity(intent);
                        finish();

                    } else if (arrarylistDrawer.get(position).getsTitle().equalsIgnoreCase("Add Tag")) {
                        Intent intent = new Intent(ctx, Tags_Page.class);
//
//                    intent.putExtra("TagId", arrarylistDrawer.get(position).getTagId());
                        startActivity(intent);
                        finish();

                    } else if (arrarylistDrawer.get(position).getsTitle().equalsIgnoreCase("Settings")) {

                        Intent intent = new Intent(ctx, Settings_Page.class);
                        intent.putExtra("flag", "old");
                        startActivity(intent);

                    } else {


                        Intent intent = new Intent(ctx, All_Notes_Page.class);
//
                        intent.putExtra("TagId", arrarylistDrawer.get(position).getTagId()+"");
                        startActivity(intent);
                        finish();
                    }
                }
            });
        }catch (Exception e){
            Toast.makeText(ctx, e.getMessage(), Toast.LENGTH_SHORT).show();
        }


    }




    @Override
    public void onBackPressed() {
       Intent intent=new Intent(Tags_Page.this,Home_Page.class);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.tags__page, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.iSearch) {
            Intent intent = new Intent(Tags_Page.this, Home_Page.class);
            startActivity(intent);
            finish();

        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();


//        if (id == R.id.nav_camera) {
//            // Handle the camera action
//        } else if (id == R.id.nav_gallery) {
//
//        } else if (id == R.id.nav_slideshow) {
//
//        } else if (id == R.id.nav_manage) {
//
//        } else if (id == R.id.nav_share) {
//
//        } else if (id == R.id.nav_send) {
//
//        }

//        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
