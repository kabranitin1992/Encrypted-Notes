package com.aroner.encrypted_notes.Activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Typeface;
import android.nfc.Tag;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.aroner.encrypted_notes.Adapter.DrawerAdapter;
import com.aroner.encrypted_notes.Bean.GridviewBean;
import com.aroner.encrypted_notes.Bean.TagBean;
import com.aroner.encrypted_notes.DatabaseHandler;
import com.aroner.encrypted_notes.GlobalModule;
import com.aroner.encrypted_notes.R;
import com.aroner.encrypted_notes.SaveSharedPreference;
import com.aroner.encrypted_notes.Webservice;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class SaveAndBackOn_Page extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    EditText etText,etTitle;
    Typeface typeface;
    NavigationView navigationView;
    Context ctx;
    DatabaseHandler db;
    String DecryptTag, SubstringEncryptKey;
    ArrayList<TagBean> arrlst;
    ArrayList<TagBean> arrarylistDrawer;
    SaveSharedPreference saveSharedPreference;
    DrawerAdapter drawerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save_and_back_on__page);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Add a Note");
        toolbar.setTitleTextColor(getResources().getColor(R.color.colorWhite));
        String customFont = "Pacifico.ttf";
        saveSharedPreference = new SaveSharedPreference();

        arrlst = new ArrayList<TagBean>();
        typeface = typeface.createFromAsset(getAssets(), customFont);
        TextView tv = (TextView) toolbar.findViewById(R.id.toolbar_title);
        tv.setTypeface(typeface);

        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        etText = (EditText) findViewById(R.id.etText);
        etTitle = (EditText) findViewById(R.id.etTitle);

//        clsGetTag getTag = new clsGetTag();
//        getTag.start();


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setItemTextColor(ColorStateList.valueOf(Color.WHITE));
        navigationView.setNavigationItemSelectedListener(this);

        View hView = navigationView.getHeaderView(0);
        TextView tvNavHeaderText = (TextView) hView.findViewById(R.id.tvNavHeaderText);
        tvNavHeaderText.setTypeface(typeface);

        db=new DatabaseHandler(ctx);


        arrlst = db.getAllTag();
        displayMenu();

        etText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SaveAndBackOn_Page.this, All_Notes_Page.class);
                startActivity(intent);
                finish();
            }
        });
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
                    if (aResponse.equalsIgnoreCase("")) {

                    } else {
                        JSONArray jsonArray = new JSONArray(aResponse);
                        JSONObject userObj = jsonArray.getJSONObject(0);

                        SaveSharedPreference saveSharedPreference = new SaveSharedPreference();
                        SubstringEncryptKey = GlobalModule.subKey;

                        for (int i = 0; i < jsonArray.length(); i++) {

                            TagBean bean = new TagBean();
                            String tag = jsonArray.getJSONObject(i).getString("sTag");

                            DecryptTag = GlobalModule.Decrypt(ctx, SubstringEncryptKey, tag);
                            bean.setTagName(DecryptTag);


                            arrlst.add(bean);
                        }

//                        adapter = new TagAdapter(Home_Page.this, R.layout.tag_items, arrlstGridViewBean);
//                        lvTags.setAdapter(adapter);
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

    private void displayMenu() {

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


        for (int i = 0; i < arrlst.size(); i++) {
            bean = new TagBean();
            SubstringEncryptKey = GlobalModule.subKey;
            String str = GlobalModule.Decrypt(ctx, SubstringEncryptKey, arrlst.get(i).getTagName());
            bean.setsTitle(str);
            bean.setiIconImg(R.drawable.arrow_it_is_white);
            bean.setTagId(arrlst.get(i).getTagId());
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
//                    intent.putExtra("TagId", arrlstDrawer.get(finalJ).getsTagId());
                    startActivity(intent);
                    finish();

                } else if (arrarylistDrawer.get(position).getsTitle().equalsIgnoreCase("Settings")) {

                    Intent intent = new Intent(ctx, Settings_Page.class);
                    intent.putExtra("flag", "old");
                    startActivity(intent);

                } else {



                    Intent intent = new Intent(ctx, All_Notes_Page.class);
//
                    intent.putExtra("TagId", arrarylistDrawer.get(position).getTagId() + "");
                    startActivity(intent);
                    finish();
                }
            }
        });


    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.save_and_back_on__page, menu);
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
            Intent intent = new Intent(SaveAndBackOn_Page.this, Home_Page.class);
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

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
