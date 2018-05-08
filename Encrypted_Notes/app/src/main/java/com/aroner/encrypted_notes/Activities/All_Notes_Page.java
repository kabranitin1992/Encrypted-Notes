package com.aroner.encrypted_notes.Activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.aroner.encrypted_notes.Adapter.DrawerAdapter;
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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;

public class All_Notes_Page extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    Typeface typeface;
    String sTag, sTagId, SubstringEncryptKey, DecryptNote, sNoteId;
    Context ctx;
    ListView lvAll_notes;
    ArrayList<TagBean> arrlstGridViewBean;
    ArrayList<TagBean> arrlstGridViewBean1;
    TagAdapter adapter;
    String DecryptTag;
    DrawerAdapter drawerAdapter;
    ArrayList<TagBean> arrarylistDrawer;
    DatabaseHandler db;
    ImageView ivSetting,ivAdd;
    NavigationView navigationView;
    ProgressDialog pd;

    ArrayList<TagBean> contacts;
    ArrayList<TagBean> notes;

    SaveSharedPreference saveSharedPreference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_all__notes__page);
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            ctx = this;

            String customFont = "Pacifico.ttf";
            saveSharedPreference = new SaveSharedPreference();

            contacts=new ArrayList<TagBean>();
            notes=new ArrayList<TagBean>();

            arrlstGridViewBean = new ArrayList<TagBean>();
            arrlstGridViewBean1 = new ArrayList<TagBean>();

            ivSetting=(ImageView)findViewById(R.id.ivSetting);
            ivAdd=(ImageView)findViewById(R.id.ivAdd);
            typeface = typeface.createFromAsset(getAssets(), customFont);
            TextView tv = (TextView) toolbar.findViewById(R.id.toolbar_title);
            tv.setTypeface(typeface);
            db=new DatabaseHandler(ctx);

            toolbar.setTitle("");
            setSupportActionBar(toolbar);

            sTag = getIntent().getStringExtra("tag");
            sTagId = getIntent().getStringExtra("TagId");


//            SubstringEncryptKey = saveSharedPreference.getSubstringEncryptKey(ctx);

            lvAll_notes = (ListView) findViewById(R.id.lvAll_notes);

//            pd = new ProgressDialog(ctx);
//            pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
//            pd.setMessage("Getting Notes...");
//            pd.show();

//            clsGetTag getTag = new clsGetTag();
//            getTag.start();


             notes = db.getAllNotes(Integer.parseInt(sTagId));


            adapter = new TagAdapter(All_Notes_Page.this, R.layout.tag_items, notes);
            lvAll_notes.setAdapter(adapter);



           contacts = db.getAllTagForNote();
            displayMenu();



//            clsGetNotes getNotes = new clsGetNotes();
//            getNotes.start();


//            for (int index = 0; index < jsonArray.length(); index++) {
//                GridviewBean obj = new GridviewBean();
//                obj.setText(jsonArray.getString(index));
//                arrlstGridViewBean.add(obj);
//            }

//            FloatingActionButton add_a_note = (FloatingActionButton) findViewById(R.id.add_a_note);
//            FloatingActionButton fbSetting = (FloatingActionButton) findViewById(R.id.fbSetting);
//
            ivSetting.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(ctx, Settings_Page.class);
                    intent.putExtra("flag","old");
                    intent.putExtra("TagId", sTagId+"");
                    startActivity(intent);
                    finish();
                }
            });


            ivAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(ctx, NewNote.class);
                    intent.putExtra("tag", sTag);
                    intent.putExtra("TagId", sTagId+"");
                    intent.putExtra("Note", "sAddNote");
                    startActivity(intent);
                    finish();
                }
            });
//
//            add_a_note.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    Intent intent = new Intent(ctx, NewNote.class);
//                    intent.putExtra("tag", sTag);
//                    intent.putExtra("TagId", sTagId);
//                    intent.putExtra("Note", "sAddNote");
//                    startActivity(intent);
//                    finish();
//                }
//            });
//
//            fbSetting.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    Intent intent = new Intent(ctx, Settings_Page.class);
//                    intent.putExtra("TagId", sTagId);
//                    startActivity(intent);
//                    finish();
//                }
//            });
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
        } catch (Exception e) {
            Toast.makeText(ctx, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
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
                            String tagId = jsonArray.getJSONObject(i).getString("iTagId");

                            DecryptTag = GlobalModule.Decrypt(ctx, SubstringEncryptKey, tag);
                            bean.setTagName(DecryptTag);
                            bean.setTagId(Integer.parseInt(tagId));


                            arrlstGridViewBean1.add(bean);
                        }

//                        adapter = new TagAdapter(Home_Page.this, R.layout.tag_items, arrlstGridViewBean);
//                        lvTags.setAdapter(adapter);
//                        displayMenu();


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


    private class clsGetNotes extends Thread {

        String aResponse;


        @Override
        public void run() {
            try {
                Webservice com = new Webservice();

                aResponse = com.fnGetNotes(GlobalModule.sUserId, sTagId);


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
                    JSONArray jsonArray = new JSONArray(aResponse);
                    if (aResponse.equalsIgnoreCase("")) {

                    } else {
                        if (jsonArray.getJSONObject(0).has("err")) {
                            Toast.makeText(ctx, jsonArray.getJSONObject(0).getString("err"), Toast.LENGTH_SHORT).show();
                        } else {

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                TagBean bean = new TagBean();
                                String str = jsonObject.getString("sNote");

                                sNoteId = jsonObject.getString("iNoteId");
                                sTagId = jsonObject.getString("iTagId");

                                DecryptNote = GlobalModule.Decrypt(ctx, SubstringEncryptKey, str);
                                bean.setTagName(DecryptNote);
//                                if (DecryptNote.length() > 26) {
//
//                                    String HalfNotes = DecryptNote.substring(0, 25);
//                                    StringBuilder sb = new StringBuilder(HalfNotes);
//                                    sb.append(" ...");
////                                bean.setsNote();
//                                    bean.setsTitle(sb.toString());
//                                } else {
                                    bean.setsTitle(DecryptNote);
//                                }
                                bean.setiNoteId(Integer.parseInt(sNoteId));
                                bean.setTagId(Integer.parseInt(sTagId));
                                bean.setStr("Note");
                                arrlstGridViewBean.add(bean);
                            }
                            adapter = new TagAdapter(All_Notes_Page.this, R.layout.tag_items, arrlstGridViewBean);
                            lvAll_notes.setAdapter(adapter);

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
                    startActivity(intent);;
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

//    private void displayMenu1() {
//
//        navigationView = (NavigationView) findViewById(R.id.nav_view);
//        final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//
//        Menu menu = navigationView.getMenu();
//        TextView view = new TextView(ctx);
//        view.setTextSize(Integer.parseInt(GlobalModule.textSize));
//        view.setText("All Tags"+"                                                                ");
//        view.setGravity(Gravity.START);
//        view.setTextColor(getResources().getColor(R.color.colorWhite));
//        menu.add("").setActionView(view).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
//            @Override
//            public boolean onMenuItemClick(MenuItem item) {
//                Intent intent = new Intent(ctx, Tags_Page.class);
//
////                    intent.putExtra("TagId", arrlstDrawer.get(finalJ).getsTagId());
//                startActivity(intent);
//                return false;
//            }
//        });
//
//
//        TextView view1 = new TextView(ctx);
//        view1.setTextSize(Integer.parseInt(GlobalModule.textSize));
//        view1.setText("Add Tag"+"                                                                ");
//        view1.setGravity(Gravity.START);
//        view1.setTextColor(getResources().getColor(R.color.colorWhite));
//        menu.add("").setActionView(view1).setIcon(R.drawable.add).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
//            @Override
//            public boolean onMenuItemClick(MenuItem item) {
//                Intent intent = new Intent(ctx, Tags_Page.class);
//
////                    intent.putExtra("TagId", arrlstDrawer.get(finalJ).getsTagId());
//                startActivity(intent);
//                return false;
//            }
//        });
//
//        try {
//            navigationView.setItemTextColor(ColorStateList.valueOf(Color.WHITE));
////            SubMenu menu1 = menu.addSubMenu("");
//
//
//            for (int j = 0; j < contacts.size(); j++) {
//
//                navigationView.setItemIconTintList(ColorStateList.valueOf(Color.WHITE));
//                final int finalJ = j;
//                SubstringEncryptKey = GlobalModule.subKey;
//
//                TextView view2 = new TextView(ctx);
//                view2.setTextSize(Integer.parseInt(GlobalModule.textSize));
//                view2.setText(GlobalModule.Decrypt(ctx,SubstringEncryptKey,contacts.get(j).getTagName())+"                                                                ");
//                String str = GlobalModule.Decrypt(ctx,SubstringEncryptKey,contacts.get(j).getTagName());
//
//
//                if(Integer.parseInt(GlobalModule.textSize)==14){
//                    if(str.length()<=2){
//                        view2.setText(str + "                                                                            ");
//
//                    }else {
//                        view2.setText(str + "                                                                ");
//                    }
//                }else {
//                    view2.setText(str + "                                                                ");
//
//                }
//
//                view2.setGravity(Gravity.START);
//                view2.setTextColor(getResources().getColor(R.color.colorWhite));
//
//                menu.add("").setActionView(view2).setIcon(R.drawable.arrow_it_is_white).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
//                    @Override
//                    public boolean onMenuItemClick(MenuItem item) {
//                        Intent intent = new Intent(ctx, All_Notes_Page.class);
//                        intent.putExtra("TagId", contacts.get(finalJ).getTagId()+"");
//                        startActivity(intent);
//                        return false;
//                    }
//                });
//
//                drawer.closeDrawers();
//
//            }
//
//            TextView view3 = new TextView(ctx);
//            view3.setTextSize(Integer.parseInt(GlobalModule.textSize));
//            view3.setText("Settings"+"                                                                ");
//            view3.setGravity(Gravity.START);
//            view3.setTextColor(getResources().getColor(R.color.colorWhite));
//
//            menu.add("").setActionView(view3).setIcon(R.drawable.gear01).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
//                @Override
//                public boolean onMenuItemClick(MenuItem item) {
//                    Intent intent=new Intent(ctx,Settings_Page.class);
//                    intent.putExtra("flag","old");
//                    startActivity(intent);
//                    return false;
//                }
//            });
//
//
//
////            adapter1 = new Drawer_Adapter(Tags_Page.this, R.layout.drawer_lv_items, arrlstGridViewBean);
////            lvDrawerItem.setAdapter(adapter1);
//
//        } catch (Exception e) {
//            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
//        }
//    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(All_Notes_Page.this, Tags_Page.class);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.all__notes__page, menu);
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
            Intent intent = new Intent(All_Notes_Page.this, Home_Page.class);
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
