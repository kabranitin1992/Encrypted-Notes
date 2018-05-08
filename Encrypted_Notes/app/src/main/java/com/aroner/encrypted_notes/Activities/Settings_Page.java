package com.aroner.encrypted_notes.Activities;

import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Spannable;
import android.text.SpannableString;
import android.util.TypedValue;
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
import android.widget.Button;
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

public class Settings_Page extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    TextView tvUrl, tvText, tvFontSize, tvVersion, Version1,tvFontSizeee;
    Typeface typeface;
    String sTagId, SubstringEncryptKey, DecryptTag;
    Context ctx;
    TextView tvFontSize1, tvFontSize2, tvFontSize3, tvFontSize4, tvFontSize5,tvFs;
    ArrayList<TagBean> arrlstDrawer;
    ArrayList<TagBean> arrarylistDrawer;
    DrawerAdapter drawerAdapter;
    NavigationView navigationView;
    SaveSharedPreference saveSharedPreference;
    DatabaseHandler db;
    ProgressDialog pd;
    String flag;
    Button btnSync;

    public static String textSize = "textSize";
    Menu menu;
//    Toolbar mActionBarToolbar;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings__page);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        ctx = this;
        flag=getIntent().getStringExtra("flag");
        try {

            String customFont = "Pacifico.ttf";
            typeface = typeface.createFromAsset(getAssets(), customFont);
            TextView tv = (TextView) toolbar.findViewById(R.id.toolbar_title);
            tv.setTypeface(typeface);
            sTagId = getIntent().getStringExtra("TagId");
            toolbar.setTitle("");
            setSupportActionBar(toolbar);
            saveSharedPreference = new SaveSharedPreference();



            tvUrl = (TextView) findViewById(R.id.tvUrl);
            tvText = (TextView) findViewById(R.id.tvtext);
            tvFontSize = (TextView) findViewById(R.id.tvFontSize);
            tvVersion = (TextView) findViewById(R.id.tvVersion);
            Version1 = (TextView) findViewById(R.id.Version1);
            btnSync = (Button) findViewById(R.id.btnSync);


            arrlstDrawer = new ArrayList<TagBean>();
//            tvFs = (TextView) findViewById(R.id.tvFontSize11);

            tvFontSize2 = (TextView) findViewById(R.id.tvFontSize2);
            tvFontSize3 = (TextView) findViewById(R.id.tvFontSize3);
            tvFontSize4 = (TextView) findViewById(R.id.tvFontSize4);
            tvFontSize5 = (TextView) findViewById(R.id.tvFontSize5);



            tvText.setTextSize(Integer.parseInt(GlobalModule.textSize));
            tvUrl.setTextSize(Integer.parseInt(GlobalModule.textSize));
            tvFontSize.setTextSize(Integer.parseInt(GlobalModule.textSize));
            tvVersion.setTextSize(Integer.parseInt(GlobalModule.textSize));
            Version1.setTextSize(Integer.parseInt(GlobalModule.textSize));
//            tvFs.setTextSize(12);
            tvFontSize2.setTextSize(14);
            tvFontSize3.setTextSize(16);
            tvFontSize4.setTextSize(18);
            tvFontSize5.setTextSize(20);


            btnSync.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    pd=new ProgressDialog(ctx);
                    pd.setMessage("Syncronizing...");
                    pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                    pd.show();
                    SyncPage.clsAddData clsAddData=new SyncPage.clsAddData(ctx,pd);
                    clsAddData.start();


                }
            });

            if(Integer.parseInt(GlobalModule.textSize)==14){
                tvFontSize2.setBackgroundResource(R.drawable.background_font);
                tvFontSize2.setTextColor(Color.parseColor("#ffffff"));
            }else if(Integer.parseInt(GlobalModule.textSize)==16){
                tvFontSize3.setBackgroundResource(R.drawable.background_font);
                tvFontSize3.setTextColor(Color.parseColor("#ffffff"));
            }else if(Integer.parseInt(GlobalModule.textSize)==18){
                tvFontSize4.setBackgroundResource(R.drawable.background_font);
                tvFontSize4.setTextColor(Color.parseColor("#ffffff"));
            }else if(Integer.parseInt(GlobalModule.textSize)==20){
                tvFontSize5.setBackgroundResource(R.drawable.background_font);
                tvFontSize5.setTextColor(Color.parseColor("#ffffff"));
            }



            tvFontSize2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    saveSharedPreference.setPREF_textSize(ctx, 14 + "");
                    textSize = saveSharedPreference.getPREF_textSize(ctx);



//                    tvFontSize2.setTypeface(tvFontSize2.getTypeface(), Typeface.BOLD);
                    tvFontSize2.setTextColor(Color.parseColor("#ffffff"));

//                    tvFs.setTextColor(Color.parseColor("#000000"));
                    tvFontSize3.setTextColor(Color.parseColor("#000000"));
                    tvFontSize4.setTextColor(Color.parseColor("#000000"));
                    tvFontSize5.setTextColor(Color.parseColor("#000000"));

                    tvFontSize2.setBackgroundResource(R.drawable.background_font);

//                    tvFs.setBackgroundResource(0);
                    tvFontSize3.setBackgroundResource(0);
                    tvFontSize4.setBackgroundResource(0);
                    tvFontSize5.setBackgroundResource(0);

                    GlobalModule.textSize = textSize;

                    tvText.setTextSize(Integer.parseInt(GlobalModule.textSize));
                    tvUrl.setTextSize(Integer.parseInt(GlobalModule.textSize));
                    tvFontSize.setTextSize(Integer.parseInt(GlobalModule.textSize));
                    tvVersion.setTextSize(Integer.parseInt(GlobalModule.textSize));
                    Version1.setTextSize(Integer.parseInt(GlobalModule.textSize));
                    flag="new";
                    displayMenu();

//                    tvFontSize1.setTextSize(Integer.parseInt(GlobalModule.textSize));
//                    tvFontSize2.setTextSize(14);
//                    tvFontSize3.setTextSize(Integer.parseInt(GlobalModule.textSize));
//                    tvFontSize4.setTextSize(Integer.parseInt(GlobalModule.textSize));
//                    tvFontSize5.setTextSize(Integer.parseInt(GlobalModule.textSize));

//
//                    tvFontSize1.setTypeface(tvFontSize1.getTypeface(), Typeface.NORMAL);
//                    tvFontSize3.setTypeface(tvFontSize3.getTypeface(), Typeface.NORMAL);
//                    tvFontSize4.setTypeface(tvFontSize4.getTypeface(), Typeface.NORMAL);
//                    tvFontSize5.setTypeface(tvFontSize5.getTypeface(), Typeface.NORMAL);


                }
            });
            tvFontSize3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    saveSharedPreference.setPREF_textSize(ctx, 16 + "");
                    textSize = saveSharedPreference.getPREF_textSize(ctx);


//                    tvFontSize3.setTypeface(tvFontSize3.getTypeface(), Typeface.BOLD);
                    tvFontSize3.setTextColor(Color.parseColor("#ffffff"));

//                    tvFs.setTextColor(Color.parseColor("#000000"));
                    tvFontSize2.setTextColor(Color.parseColor("#000000"));
                    tvFontSize4.setTextColor(Color.parseColor("#000000"));
                    tvFontSize5.setTextColor(Color.parseColor("#000000"));

                    tvFontSize3.setBackgroundResource(R.drawable.background_font);

//                    tvFs.setBackgroundResource(0);
                    tvFontSize2.setBackgroundResource(0);
                    tvFontSize4.setBackgroundResource(0);
                    tvFontSize5.setBackgroundResource(0);


                    GlobalModule.textSize = textSize;

                    tvText.setTextSize(Integer.parseInt(GlobalModule.textSize));
                    tvUrl.setTextSize(Integer.parseInt(GlobalModule.textSize));
                    tvFontSize.setTextSize(Integer.parseInt(GlobalModule.textSize));
                    tvVersion.setTextSize(Integer.parseInt(GlobalModule.textSize));
                    Version1.setTextSize(Integer.parseInt(GlobalModule.textSize));


//                    tvFontSize1.setTextSize(Integer.parseInt(GlobalModule.textSize));
//                    tvFontSize2.setTextSize(Integer.parseInt(GlobalModule.textSize));
//                    tvFontSize3.setTextSize(16);
//                    tvFontSize4.setTextSize(Integer.parseInt(GlobalModule.textSize));
//                    tvFontSize5.setTextSize(Integer.parseInt(GlobalModule.textSize));

//
//                    tvFontSize2.setTypeface(tvFontSize2.getTypeface(), Typeface.NORMAL);
//                    tvFontSize1.setTypeface(tvFontSize1.getTypeface(), Typeface.NORMAL);
//                    tvFontSize4.setTypeface(tvFontSize4.getTypeface(), Typeface.NORMAL);
//                    tvFontSize5.setTypeface(tvFontSize5.getTypeface(), Typeface.NORMAL);
                    flag="new";
                    displayMenu();

                }
            });

            tvFontSize4.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    saveSharedPreference.setPREF_textSize(ctx, 18 + "");
                    textSize = saveSharedPreference.getPREF_textSize(ctx);


//                    tvFontSize4.setTypeface(tvFontSize4.getTypeface(), Typeface.BOLD);
                    tvFontSize4.setTextColor(Color.parseColor("#ffffff"));

//                    tvFs.setTextColor(Color.parseColor("#000000"));
                    tvFontSize2.setTextColor(Color.parseColor("#000000"));
                    tvFontSize3.setTextColor(Color.parseColor("#000000"));
                    tvFontSize5.setTextColor(Color.parseColor("#000000"));

                    tvFontSize4.setBackgroundResource(R.drawable.background_font);

//                    tvFs.setBackgroundResource(0);
                    tvFontSize2.setBackgroundResource(0);
                    tvFontSize3.setBackgroundResource(0);
                    tvFontSize5.setBackgroundResource(0);


//                    tvFontSize2.setTypeface(tvFontSize2.getTypeface(), Typeface.NORMAL);
//                    tvFontSize1.setTypeface(tvFontSize1.getTypeface(), Typeface.NORMAL);
//                    tvFontSize3.setTypeface(tvFontSize3.getTypeface(), Typeface.NORMAL);
//                    tvFontSize5.setTypeface(tvFontSize4.getTypeface(), Typeface.NORMAL);


                    GlobalModule.textSize = textSize;

                    tvText.setTextSize(Integer.parseInt(GlobalModule.textSize));
                    tvUrl.setTextSize(Integer.parseInt(GlobalModule.textSize));
                    tvFontSize.setTextSize(Integer.parseInt(GlobalModule.textSize));
                    tvVersion.setTextSize(Integer.parseInt(GlobalModule.textSize));
                    Version1.setTextSize(Integer.parseInt(GlobalModule.textSize));
                    flag="new";
                    displayMenu();

//                    tvFontSize1.setTextSize(Integer.parseInt(GlobalModule.textSize));
//                    tvFontSize2.setTextSize(Integer.parseInt(GlobalModule.textSize));
//                    tvFontSize3.setTextSize(Integer.parseInt(GlobalModule.textSize));
//                    tvFontSize4.setTextSize(18);
//                    tvFontSize5.setTextSize(Integer.parseInt(GlobalModule.textSize));

                }
            });

            tvFontSize5.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        saveSharedPreference.setPREF_textSize(ctx, 20 + "");
                        textSize = saveSharedPreference.getPREF_textSize(ctx);


//                        tvFontSize5.setTypeface(tvFontSize5.getTypeface(), Typeface.BOLD);
                        tvFontSize5.setTextColor(Color.parseColor("#ffffff"));

//                        tvFs.setTextColor(Color.parseColor("#000000"));
                        tvFontSize2.setTextColor(Color.parseColor("#000000"));
                        tvFontSize3.setTextColor(Color.parseColor("#000000"));
                        tvFontSize4.setTextColor(Color.parseColor("#000000"));

                        tvFontSize5.setBackgroundResource(R.drawable.background_font);

//                        tvFs.setBackgroundResource(0);
                        tvFontSize2.setBackgroundResource(0);
                        tvFontSize3.setBackgroundResource(0);
                        tvFontSize4.setBackgroundResource(0);

                        GlobalModule.textSize = textSize;

                        tvText.setTextSize(Integer.parseInt(GlobalModule.textSize));
                        tvUrl.setTextSize(Integer.parseInt(GlobalModule.textSize));
                        tvFontSize.setTextSize(Integer.parseInt(GlobalModule.textSize));
                        tvVersion.setTextSize(Integer.parseInt(GlobalModule.textSize));
                        Version1.setTextSize(Integer.parseInt(GlobalModule.textSize));
//                        tvFontSize1.setTextSize(Integer.parseInt(GlobalModule.textSize));
//                        tvFontSize2.setTextSize(Integer.parseInt(GlobalModule.textSize));
//                        tvFontSize3.setTextSize(Integer.parseInt(GlobalModule.textSize));
//                        tvFontSize4.setTextSize(Integer.parseInt(GlobalModule.textSize));
//                        tvFontSize5.setTextSize(20);


//                        tvFontSize2.setTypeface(tvFontSize2.getTypeface(), Typeface.NORMAL);
//                        tvFontSize1.setTypeface(tvFontSize1.getTypeface(), Typeface.NORMAL);
//                        tvFontSize3.setTypeface(tvFontSize3.getTypeface(), Typeface.NORMAL);
//                        tvFontSize4.setTypeface(tvFontSize4.getTypeface(), Typeface.NORMAL);
                        flag="new";
                        displayMenu();

                    } catch (Exception e) {
                        Toast.makeText(ctx, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                }
            });
//        mActionBarToolbar = (Toolbar) findViewById(R.id.toolbar);
//
//        setSupportActionBar(mActionBarToolbar);


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

            tvUrl.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
            NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
            navigationView.setItemTextColor(ColorStateList.valueOf(Color.WHITE));
            navigationView.setNavigationItemSelectedListener(this);

            View hView = navigationView.getHeaderView(0);
            TextView tvNavHeaderText = (TextView) hView.findViewById(R.id.tvNavHeaderText);
            tvNavHeaderText.setTypeface(typeface);

            db = new DatabaseHandler(ctx);
            arrlstDrawer = db.getAllTag();
            displayMenu();

//        clsGetTag getTag = new clsGetTag();
//        getTag.start();


            tvUrl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String urlString = "https://www.paypal.me/GoodWorld";
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(urlString));
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.setPackage("com.android.chrome");
                    try {
                        startActivity(intent);
                    } catch (ActivityNotFoundException ex) {
                        // Chrome browser presumably not installed so allow user to choose instead
                        intent.setPackage(null);
                        startActivity(intent);
                    }
                }
            });


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


                        SaveSharedPreference saveSharedPreference = new SaveSharedPreference();
                        SubstringEncryptKey = GlobalModule.subKey;

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject userObj = jsonArray.getJSONObject(i);
                            TagBean bean = new TagBean();
                            String tag = jsonArray.getJSONObject(i).getString("sTag");
                            DecryptTag = GlobalModule.Decrypt(ctx, SubstringEncryptKey, tag);
                            bean.setTagName(DecryptTag);
                            bean.setStr("Tag");
                            arrlstDrawer.add(bean);
                        }
//                        adapter = new SearchAdapter(ctx, R.layout.search_items, arrlstTag);
//                        lvSearch.setAdapter(adapter);
//                        =========
                        displayMenu();
//                        setListViewHeightBasedOnChildren(lvSearch);
//
//                        lvSearch.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                            @Override
//                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                                etText.setText(arrlstTag.get(position).getsTag());
//                            }
//                        });
//                        ===============


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


        for (int i = 0; i < arrlstDrawer.size(); i++) {
            bean = new TagBean();
            SubstringEncryptKey = GlobalModule.subKey;
            String str = GlobalModule.Decrypt(ctx, SubstringEncryptKey, arrlstDrawer.get(i).getTagName());
            bean.setsTitle(str);
            bean.setTagId(arrlstDrawer.get(i).getTagId());

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
//                    intent.putExtra("TagId", arrlstDrawer.get(finalJ).getsTagId());
                    startActivity(intent);
                    finish();

                } else if (arrarylistDrawer.get(position).getsTitle().equalsIgnoreCase("Settings")) {

                    Intent intent = new Intent(ctx, Settings_Page.class);
                    intent.putExtra("flag","old");
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
        Intent intent = new Intent(Settings_Page.this, Tags_Page.class);
//        intent.putExtra("TagId", sTagId);
        startActivity(intent);
        finish();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.settings__page, menu);
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
            Intent intent = new Intent(Settings_Page.this, Home_Page.class);
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
