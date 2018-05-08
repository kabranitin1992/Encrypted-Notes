package com.aroner.encrypted_notes.Activities;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.nfc.Tag;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.SubMenu;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.aroner.encrypted_notes.Adapter.DrawerAdapter;
import com.aroner.encrypted_notes.Adapter.GridviewAdapter;
import com.aroner.encrypted_notes.Adapter.SearchAdapter;
import com.aroner.encrypted_notes.Adapter.TagAdapter;
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
import org.w3c.dom.Text;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.ExecutionException;

import javax.microedition.khronos.opengles.GL;

public class Home_Page extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    EditText etText;
    Typeface typeface;
    String key, email;
    ListView lvSearch;
    static ArrayList<TagBean> arrarylistDrawer;
    SharedPreferences msharedPreferences;
    NavigationView navigationView;
    SaveSharedPreference saveSharedPreference;
    String SubstringEncryptKey;
    String EncryptTag, DecryptTag, tagId;
    SearchAdapter adapter;
    String sNoteId, sTagId, DecryptNote;
    Context ctx;

    ArrayList<TagBean> contacts, arrayListDrawer1, arrNote;
    DatabaseHandler db;
    TagBean bean, bean1;
    LinearLayoutCompat.LayoutParams textViewLayoutParams;
    DrawerLayout mDrawerListView;
    Menu menu;
    public static String UserID = "useridKey";
    ArrayList<TagBean> arrlstFilter, arrlstDrawer;
    public static ArrayList<TagBean> arrlstTag;

    LinearLayout ll;
    DrawerAdapter drawerAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home__page);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        try {
//        toolbar.setTitle("Search");
            String customFont = "Pacifico.ttf";


            arrlstTag = new ArrayList<TagBean>();
            arrNote = new ArrayList<TagBean>();
            arrlstFilter = new ArrayList<TagBean>();
            arrlstDrawer = new ArrayList<TagBean>();
            arrayListDrawer1 = new ArrayList<TagBean>();
            msharedPreferences = getApplicationContext().getSharedPreferences("User", Context.MODE_PRIVATE);// 0 - for private mode
            ctx = this;
            contacts = new ArrayList<TagBean>();
            saveSharedPreference = new SaveSharedPreference();
            textViewLayoutParams = new LinearLayoutCompat.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);


//        key=getIntent().getStringExtra("key");
//        email=getIntent().getStringExtra("emailId");
            lvSearch = (ListView) findViewById(R.id.lvSearch);
            typeface = Typeface.createFromAsset(getAssets(), customFont);

            TextView mTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);

            mTitle.setTypeface(typeface);

            toolbar.setNavigationIcon(R.drawable.menu);
            toolbar.setTitle("");
            setSupportActionBar(toolbar);


            etText = (EditText) findViewById(R.id.etText);

            etText.setText(GlobalModule.searchString);


//        setTitle("All Notes");
//        getActionBar().setIcon(R.drawable.search);

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



            etText.setTextSize(Integer.parseInt(GlobalModule.textSize));


//            displayMenu();


            db = new DatabaseHandler(ctx);

            contacts = db.getAllTag();


//
            arrNote = db.getAllNotesss();
            SubstringEncryptKey = GlobalModule.subKey;


            for (int i = 0; i < contacts.size(); i++) {
                bean1 = new TagBean();
                bean1.setTagId(contacts.get(i).getTagId());
                bean1.setStr("Tag");
                bean1.setTagName(GlobalModule.Decrypt(ctx, SubstringEncryptKey, contacts.get(i).getTagName()));
                arrlstTag.add(bean1);
            }
            for (int i = 0; i < arrNote.size(); i++) {
                bean = new TagBean();
                bean.setTagId(arrNote.get(i).getTagId());
                bean.setiNoteId(arrNote.get(i).getiNoteId());
                bean.setStr("Note");
                bean.setsNote(GlobalModule.Decrypt(ctx, SubstringEncryptKey, arrNote.get(i).getsNote()));
                arrlstTag.add(bean);
            }


            if (GlobalModule.searchString.length() != 0) {
                arrlstFilter = new ArrayList<TagBean>();
                for (int index = 0; index < arrlstTag.size(); index++) {


                    if (arrlstTag.get(index).getStr().equalsIgnoreCase("Tag")) {
                        if ((arrlstTag.get(index).getTagName()).toUpperCase().contains((GlobalModule.searchString.toString()).toUpperCase())) {
                            arrlstFilter.add(arrlstTag.get(index));
                        }
                    } else if (arrlstTag.get(index).getStr().equalsIgnoreCase("Note")) {
                        if ((arrlstTag.get(index).getsNote()).toUpperCase().contains((GlobalModule.searchString.toString()).toUpperCase())) {
                            arrlstFilter.add(arrlstTag.get(index));
                        }
                    }

                }

                adapter = new SearchAdapter(ctx, R.layout.search_items, arrlstFilter);
                lvSearch.setAdapter(adapter);


                adapter.notifyDataSetChanged();
                if (arrlstFilter.size() == 0) {
                    Toast.makeText(ctx, "Nothing to Show", Toast.LENGTH_SHORT).show();
                }


            }


            etText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    try {
                        s = s.toString().trim();
                        GlobalModule.searchString = s.toString();
                        if (s.toString().length() == 0) {

                            arrlstFilter = new ArrayList<TagBean>();
                            adapter = new SearchAdapter(ctx, R.layout.search_items, arrlstFilter);
                            lvSearch.setAdapter(adapter);

                        } else {

                            arrlstFilter = new ArrayList<TagBean>();
                            for (int index = 0; index < arrlstTag.size(); index++) {


                                if (arrlstTag.get(index).getStr().equalsIgnoreCase("Tag")) {
                                    if ((arrlstTag.get(index).getTagName()).toUpperCase().contains((s.toString()).toUpperCase())) {
                                        arrlstFilter.add(arrlstTag.get(index));
                                    }
                                } else if (arrlstTag.get(index).getStr().equalsIgnoreCase("Note")) {
                                    if ((arrlstTag.get(index).getsNote()).toUpperCase().contains((s.toString()).toUpperCase())) {
                                        arrlstFilter.add(arrlstTag.get(index));
                                    }
                                }

                            }

                            adapter = new SearchAdapter(ctx, R.layout.search_items, arrlstFilter);
                            lvSearch.setAdapter(adapter);
//                                                     lvSearch.no
//                            lvSearch.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                                @Override
//                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//
//                                    if (arrlstFilter.get(position).getStr().equals("Tag")) {
//                                        etText.setText(arrlstFilter.get(position).getTagName());
//
//                                        Intent intent = new Intent(ctx, All_Notes_Page.class);
//                                        intent.putExtra("TagId", arrlstFilter.get(position).getTagId() + "");
//                                        startActivity(intent);
//                                        finish();
//                                    } else if (arrlstFilter.get(position).getStr().equals("Note")) {
//                                        etText.setText(arrlstFilter.get(position).getsNote());
//
//                                        Intent intent = new Intent(ctx, NewNote.class);
//                                        intent.putExtra("TagId", arrlstFilter.get(position).getTagId() + "");
//                                        intent.putExtra("sUpdateNote", "sUpdateNote");
//                                        intent.putExtra("Note", "sUpdateNote");
//                                        String encryptNote = GlobalModule.Encrypt(ctx, GlobalModule.subKey, arrlstFilter.get(position).getsNote());
//                                        intent.putExtra("editnote", encryptNote);
//                                        intent.putExtra("NoteId", arrlstFilter.get(position).getiNoteId() + "");
//                                        startActivity(intent);
//                                        finish();
//                                    }
//                                }
//                            });


                            adapter.notifyDataSetChanged();
                            if (arrlstFilter.size() == 0) {
                                Toast.makeText(ctx, "Nothing to Show", Toast.LENGTH_SHORT).show();
                            }


                        }
                    } catch (Exception e) {
                        Toast.makeText(Home_Page.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
//====================================================================
            displayMenu();
            db = new DatabaseHandler(this);


            /**
             * CRUD Operations
             * */
            // Inserting Contacts

//            Integer userid,String emai_Id,String key,String CreatedTimeStamp,String deleted


//            etText.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Intent intent = new Intent(Home_Page.this, Tags_Page.class);
//                    startActivity(intent);
//                }
//            });


        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
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
                Toast.makeText(Home_Page.this, ex.getMessage(), Toast.LENGTH_SHORT).show();
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
                            DecryptTag = GlobalModule.Decrypt(Home_Page.this, SubstringEncryptKey, tag);
                            tagId = userObj.getString("iTagId");
                            bean.setTagId(Integer.parseInt(tagId));
                            bean.setTagName(DecryptTag);
                            bean.setStr("Tag");
                            arrlstTag.add(bean);
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
//
//                        clsGetNotes notes = new clsGetNotes();
//                        notes.start();

                    }

                } catch (Exception ex) {
                    android.support.v7.app.AlertDialog adExp = new android.support.v7.app.AlertDialog.Builder(Home_Page.this).create();
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

                aResponse = com.fnGetNotes(GlobalModule.sUserId, tagId);


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
                                TagBean bean = new TagBean();
                                String str = jsonObject.getString("sNote");

//                                sNoteId = jsonObject.getString("iNoteId");
//                                sTagId = jsonObject.getString("iTagId");
                                bean.setiNoteId(Integer.parseInt(jsonObject.getString("iNoteId")));
                                bean.setTagId(Integer.parseInt(jsonObject.getString("iTagId")));
                                DecryptNote = GlobalModule.Decrypt(ctx, SubstringEncryptKey, str);
                                bean.setsNote(DecryptNote);
                                bean.setStr("Note");
                                arrlstTag.add(bean);
                            }


                            setListViewHeightBasedOnChildren(lvSearch);

                            lvSearch.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    if (arrlstTag.get(position).getStr().equals("Tag")) {
                                        etText.setText(arrlstTag.get(position).getTagName());
                                    } else if (arrlstTag.get(position).getStr().equals("Note")) {
                                        etText.setText(arrlstTag.get(position).getsNote());
                                    }

                                }
                            });

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


    public void displayMenu() {
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
                bean.setiIconImg(R.drawable.arrow_it_is_white);
                bean.setTagId(contacts.get(i).getTagId());
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
                        GlobalModule.searchString="";
                        startActivity(intent);
                        finish();

                    } else if (arrarylistDrawer.get(position).getsTitle().equalsIgnoreCase("Add Tag")) {
                        Intent intent = new Intent(ctx, Tags_Page.class);
                        GlobalModule.searchString="";
//                    intent.putExtra("TagId", arrlstDrawer.get(finalJ).getsTagId());
                        startActivity(intent);
                        finish();

                    } else if (arrarylistDrawer.get(position).getsTitle().equalsIgnoreCase("Settings")) {

                        Intent intent = new Intent(ctx, Settings_Page.class);
                        GlobalModule.searchString="";
                        intent.putExtra("flag", "old");
                        startActivity(intent);

                    } else {
                        Intent intent = new Intent(ctx, All_Notes_Page.class);
//
                        intent.putExtra("TagId", arrarylistDrawer.get(position).getTagId() + "");
                        GlobalModule.searchString="";
                        startActivity(intent);
                        finish();
                    }
                }
            });
        } catch (Exception e) {
            Toast.makeText(ctx, e.getMessage(), Toast.LENGTH_SHORT).show();
        }


    }


//        menu = navigationView.getMenu();
////            ((TextView) menu).setTextSize(Integer.parseInt(GlobalModule.textSize));
//
//        TextView view = new TextView(ctx);
//        view.setTextSize(Integer.parseInt(GlobalModule.textSize));
//        view.setText("All Tags" + "                                                                ");
//        view.setGravity(Gravity.START);
//        view.setTextColor(getResources().getColor(R.color.colorWhite));
//
//
//        menu.add("").setActionView(view).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
//            @Override
//            public boolean onMenuItemClick(MenuItem item) {
//                try {
//
//
//                } catch (Exception e) {
//                    Toast.makeText(Home_Page.this, e.getMessage(), Toast.LENGTH_SHORT).show();
//                }
//                return false;
//            }
//        });
////            ((TextView) menu).setTextSize(Integer.parseInt(GlobalModule.textSize));
//
//        menu = navigationView.getMenu();
////            ((TextView) menu).setTextSize(Integer.parseInt(GlobalModule.textSize));
//
//
//        TextView view1 = new TextView(ctx);
//        view1.setTextSize(Integer.parseInt(GlobalModule.textSize));
//        if (Integer.parseInt(GlobalModule.textSize) == 12) {
//
//            view1.setText("Add Tags" + "                                                                    ");
//            view1.setGravity(Gravity.START);
//        } else {
//
//            view1.setText("Add Tags" + "                                                                ");
//            view1.setGravity(Gravity.START);
//        }
//        view1.setTextColor(getResources().getColor(R.color.colorWhite));
//
//
////        LinearLayout linearLayout = new LinearLayout(ctx);
////        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
////        linearLayout.setLayoutParams(layoutParams);
////
////        TextView view1 = new TextView(ctx);
////        view1.setTextSize(Integer.parseInt(GlobalModule.textSize));
////        view1.setText("Add Tag"+"                                                                ");
////        view1.setGravity(Gravity.START);
////        linearLayout.addView(view1);
//
//
//        menu.add("").setActionView(view1).setIcon(R.drawable.add).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
//            @Override
//            public boolean onMenuItemClick(MenuItem item) {
////                    ((TextView) menu).setTextSize(Integer.parseInt(GlobalModule.textSize));
//                Intent intent = new Intent(ctx, Tags_Page.class);
//
////                    intent.putExtra("TagId", arrlstDrawer.get(finalJ).getsTagId());
//                startActivity(intent);
//                finish();
//                return false;
//            }
//        });


//       navigationView.setNavigationItemSelectedListener(this);

//        try {

//            SubMenu menu1 = menu.addSubMenu("");


//            for (int j = 0; j < contacts.size(); j++) {
//
//
//                navigationView.setItemIconTintList(ColorStateList.valueOf(Color.WHITE));
//
//                final int finalJ = j;
//                SubstringEncryptKey = GlobalModule.subKey;
//
//                TextView view2 = new TextView(ctx);
//                String str=GlobalModule.Decrypt(ctx, SubstringEncryptKey, contacts.get(j).getTagName());
//                view2.setTextSize(Integer.parseInt(GlobalModule.textSize));
//                int  s=str.length();
//                if(Integer.parseInt(GlobalModule.textSize)==14){
//                    if(str.length()<=4){
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
//
//                menu.add("").setActionView(view2).setIcon(R.drawable.arrow_it_is_white).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
//                    @Override
//                    public boolean onMenuItemClick(MenuItem item) {
//
////                        ((TextView) menu).setTextSize(Integer.parseInt(GlobalModule.textSize));
//
//                        Intent intent = new Intent(ctx, All_Notes_Page.class);
//
//                        intent.putExtra("TagId", contacts.get(finalJ).getTagId() + "");
//                        startActivity(intent);
//                        finish();
//                        return false;
//                    }
//
//                });
//
//                drawer.closeDrawers();
//
//            }


//            arrlstGridViewBean.get(j).setOnCl


//            adapter1 = new Drawer_Adapter(Tags_Page.this, R.layout.drawer_lv_items, arrlstGridViewBean);
//            lvDrawerItem.setAdapter(adapter1);

//        } catch (Exception e) {
//            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
//        }


//        menu = navigationView.getMenu();
////            ((TextView) menu).setTextSize(Integer.parseInt(GlobalModule.textSize));
//
//        TextView view2 = new TextView(ctx);
//        view2.setTextSize(Integer.parseInt(GlobalModule.textSize));
//
//        view2.setText("Settings" + "                                                                ");
//
//        view2.setGravity(Gravity.START);
//        view2.setTextColor(getResources().getColor(R.color.colorWhite));
//
//
//        menu.add("").setActionView(view2).setIcon(R.drawable.gear01).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
//            @Override
//            public boolean onMenuItemClick(MenuItem item) {
//                Intent intent = new Intent(ctx, Settings_Page.class);
//                intent.putExtra("flag","old");
//                startActivity(intent);
//                return false;
//            }
//        });


//    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home__page, menu);
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
            Intent intent = new Intent(Home_Page.this, Home_Page.class);
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

//        if (id == R.id.nav_Ideas) {
////            Intent intent = new Intent(Home_Page.this,);
////            startActivity(intent);
//        } else if (id == R.id.nav_Shopping) {
//
//        } else if (id == R.id.nav_Homeworks) {
//
//        } else if (id == R.id.nav_Art) {
//
//        } else if (id == R.id.nav_Books) {
//
//        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = 0;
        for (int i = 0; i < listView.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            if (listItem instanceof ViewGroup) {
                listItem.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            }

            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + 50;
        listView.setLayoutParams(params);
    }
}
