package com.actiknow.clearsale.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.actiknow.clearsale.R;
import com.actiknow.clearsale.adapter.AllPropertyListAdapter;
import com.actiknow.clearsale.model.AllProperty;
import com.actiknow.clearsale.utils.UserDetailsPref;
import com.actiknow.clearsale.utils.Utils;
import com.bumptech.glide.Glide;
import com.mikepenz.fontawesome_typeface_library.FontAwesome;
import com.mikepenz.iconics.IconicsDrawable;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.util.AbstractDrawerImageLoader;
import com.mikepenz.materialdrawer.util.DrawerImageLoader;
import com.mikepenz.materialdrawer.util.DrawerUIUtils;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    Bundle savedInstanceState;
    ImageView ivNavigation;
    Toolbar toolbar;
    RecyclerView rvPropertyList;
    SwipeRefreshLayout swipeRefreshLayout;
    AllPropertyListAdapter allPropertyListAdapter;
    List<AllProperty> allPropertyList = new ArrayList<>();
    UserDetailsPref userDetailsPref;
    private AccountHeader headerResult = null;
    private Drawer result = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initData();
        initListener();
        initDrawer();
        setUpNavigationDrawer();
        getAllProducts();
        isLogin();
        this.savedInstanceState = savedInstanceState;

    }

    private void isLogin() {
        if (userDetailsPref.getIntPref(MainActivity.this, UserDetailsPref.USER_ID) == 0) {
            Intent myIntent = new Intent(this, LoginActivity.class);
            startActivity(myIntent);
        }
    }


    private void initView() {
        rvPropertyList = (RecyclerView) findViewById(R.id.rvPropertyList);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);


    }

    private void initData() {
        userDetailsPref = UserDetailsPref.getInstance();


        swipeRefreshLayout.setRefreshing(true);
        allPropertyList.clear();
        allPropertyListAdapter = new AllPropertyListAdapter(this, allPropertyList);
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        rvPropertyList.setAdapter(allPropertyListAdapter);
        rvPropertyList.setHasFixedSize(true);
        rvPropertyList.setLayoutManager(layoutManager);

    }

    private void initListener() {
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getAllProducts();
            }
        });

    }

    private void getAllProducts() {
        allPropertyList.add(new AllProperty(1, "3 bd", "3ba", "1828 sqft", "Year Build : 1947", "7919 Lowell Boulevard", "West Minster", true, true, "http://clearsale.com/theme/theme1/seller_files/exterior/property_274/7619f013e5692f4a0a72e30bd7bf10b3IMG_4635.jpg"));
        allPropertyList.add(new AllProperty(2, "4 bd", "2ba", "2448 sqft", "Year Build : 1925", "1137 Colorado Boulevard", "Denver", true, true, "http://clearsale.com/theme/theme1/seller_files/exterior/property_324/954d58de65cdc62a8bb2e7e5574eb977IMG_4909.jpg"));
        allPropertyList.add(new AllProperty(3, "3 bd", "2ba", "1828 sqft", "Year Build : 1975", "268 South Newark Circle Lowell", "Aurora", true, true, "http://clearsale.com/theme/theme1/seller_files/exterior/property_348/5aece0048c495cd61a627f02f6aaf49fIMG_5156.jpg"));
        allPropertyList.add(new AllProperty(4, "4 bd", "2ba", "1762 sqft", "Year Build : 1954", "1541 Syracuse Street", "Denver", true, true, "http://clearsale.com/theme/theme1/seller_files/exterior/property_336/814d5ddee03e07c10302832a8c81a72bIMG_5117.jpg"));
        allPropertyList.add(new AllProperty(5, "3 bd", "1ba", "1008 sqft", "Year Build : 1900", "625 East 11 Street", "Loveland", false, false, "http://clearsale.com/theme/theme1/images/no-thumb1.png"));
        allPropertyList.add(new AllProperty(6, "3 bd", "2ba", "1485 sqft", "Year Build : 1962", "6121 South Lvy Street", "Centennial", false, false, "http://clearsale.com/theme/theme1/seller_files/exterior/property_250/fd2ba72f51cc65b571b1d83b45b80d9eIMG_4354.jpg"));
        allPropertyList.add(new AllProperty(7, "3 bd", "1ba", "1067 sqft", "Year Build : 1954", "1521 Syracuse Street", "Denver", false, false, "http://clearsale.com/theme/theme1/seller_files/exterior/property_321/92ee4eb01cdbe3190152a7bfd2a12411IMG_4764.jpg"));
        allPropertyList.add(new AllProperty(8, "2 bd", "1ba", "1680 sqft", "Year Build : 1936", "4131 South Elati Street", "Englewood", false, false, "http://clearsale.com/theme/theme1/seller_files/exterior/property_134/461b336dc55ba4f83bc24ad5b2de268cIMG_3526.jpg"));

        allPropertyList.add(new AllProperty(9, "2 bd", "2ba", "850 sqft", "Year Build : 1964", "11404 Claude Court", "Northglenn", false, false, "http://clearsale.com/theme/theme1/seller_files/exterior/property_198/e5ad1696545ed6e08f6433e72cadda90IMG_1302.JPG"));
        // allPropertyList.add(new AllProperty(10,"3 bd","3ba","1828 sqft","Year Build : 1947","7919 Lowell Boulevard","West Minster",true,true,"http://clearsale.com/theme/theme1/seller_files/exterior/property_274/7619f013e5692f4a0a72e30bd7bf10b3IMG_4635.jpg"));
        swipeRefreshLayout.setRefreshing(false);

    }


    private void initDrawer() {
        DrawerImageLoader.init(new AbstractDrawerImageLoader() {
            @Override
            public void set(ImageView imageView, Uri uri, Drawable placeholder) {
                //  Glide.with(imageView.getContext()).load(uri).placeholder(placeholder).into(imageView);
            }

            @Override
            public void cancel(ImageView imageView) {
                Glide.clear(imageView);
            }

            @Override
            public Drawable placeholder(Context ctx, String tag) {
                //define different placeholders for different imageView targets
                //default tags are accessible via the DrawerImageLoader.Tags
                //custom ones can be checked via string. see the CustomUrlBasePrimaryDrawerItem LINE 111
                if (DrawerImageLoader.Tags.PROFILE.name().equals(tag)) {
                    return DrawerUIUtils.getPlaceHolder(ctx);
                } else if (DrawerImageLoader.Tags.ACCOUNT_HEADER.name().equals(tag)) {
                    return new IconicsDrawable(ctx).iconText(" ").backgroundColorRes(com.mikepenz.materialdrawer.R.color.colorPrimary).sizeDp(56);
                } else if ("customUrlItem".equals(tag)) {
                    return new IconicsDrawable(ctx).iconText(" ").backgroundColorRes(R.color.md_white_1000);
                }

                //we use the default one for
                //DrawerImageLoader.Tags.PROFILE_DRAWER_ITEM.name()

                return super.placeholder(ctx, tag);
            }
        });

/*

        headerResult = new AccountHeaderBuilder()
                .withActivity(this)
                .withCompactStyle(true)
                .addProfiles(new ProfileDrawerItem()
                        .withName(userDetailsPref.getStringPref (MainActivity.this, UserDetailsPref.USER_NAME))
                        .withEmail(userDetailsPref.getStringPref (MainActivity.this, UserDetailsPref.USER_EMAIL))
//                        .withIcon ("http://i.istockimg.com/file_thumbview_approve/64330137/3/stock-photo-64330137-a-icon-of-a-businessman-avatar-or-profile-pic.jpg"))
                        .withIcon(R.drawable.profile))
                .withProfileImagesClickable(false)
                .withPaddingBelowHeader(false)
                .withSelectionListEnabledForSingleProfile(false)
                .withHeaderBackground(R.color.colorPrimary)
                .withSavedInstance(savedInstanceState)

                .build();
*/


        result = new DrawerBuilder()
                .withActivity(this)
                .withAccountHeader(headerResult)
//                .withToolbar (toolbar)
//                .withItemAnimator (new AlphaCrossFadeAnimator ())
                .addDrawerItems(
                        new PrimaryDrawerItem().withName("Home").withIcon(FontAwesome.Icon.faw_home).withIdentifier(1),
                        new PrimaryDrawerItem().withName("How It Works").withIcon(FontAwesome.Icon.faw_home).withIdentifier(2).withBadge(""),
                        new PrimaryDrawerItem().withName("About Us").withIcon(FontAwesome.Icon.faw_home).withIdentifier(3),
                        new SecondaryDrawerItem().withName("Testimonials").withIcon(FontAwesome.Icon.faw_home).withIdentifier(4),
                        new SecondaryDrawerItem().withName("Contact Us").withIcon(FontAwesome.Icon.faw_home).withIdentifier(5),
                        new SecondaryDrawerItem().withName("FAQ").withIcon(FontAwesome.Icon.faw_home).withIdentifier(6),
                        new SecondaryDrawerItem().withName("Sign Out").withIcon(FontAwesome.Icon.faw_home).withIdentifier(7)
                )
                .withSavedInstance(savedInstanceState)
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        // do something with the clicked item :D
                        switch ((int) drawerItem.getIdentifier()) {
                            case 2:
                                Intent intent = new Intent(MainActivity.this, WorkActivity.class);
                                startActivity(intent);
                                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                                Utils.showLog(Log.ERROR, "position ", "" + position, true);
                                break;

                            case 3:
                                Intent intent3 = new Intent(MainActivity.this, AboutUsActivity.class);
                                startActivity(intent3);
                                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                                Utils.showLog(Log.ERROR, "position ", "" + position, true);
                                break;

                            case 4:
                                Intent intent4 = new Intent(MainActivity.this, TestimonialActivity.class);
                                startActivity(intent4);
                                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                                Utils.showLog(Log.ERROR, "position ", "" + position, true);
                                break;

                            case 5:
                                Intent intent5 = new Intent(MainActivity.this, ContactUsActivity.class);
                                startActivity(intent5);
                                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                                Utils.showLog(Log.ERROR, "position ", "" + position, true);
                                break;

                            case 6:
                                Intent intent6 = new Intent (MainActivity.this, FAQActivity.class);
                                startActivity(intent6);
                                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                                Utils.showLog(Log.ERROR, "position ", "" + position, true);
                                break;
                        }
                        return false;
                    }
                })
                .build();
//        result.getActionBarDrawerToggle ().setDrawerIndicatorEnabled (false);


    }

    @Override
    public void onBackPressed() {
        //handle the back press :D close the drawer first and if the drawer is closed close the activity
        if (result != null && result.isDrawerOpen()) {
            result.closeDrawer();
        } else {
            super.onBackPressed();
        }
    }


    private void setUpNavigationDrawer() {
        toolbar = (Toolbar) findViewById(R.id.toolbar1);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        ImageView ivNavigation = (ImageView) findViewById(R.id.ivNavigation);
        toolbar.inflateMenu(R.menu.toolbar_menu);


        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {

                switch (menuItem.getItemId()) {
                    case R.id.action_edit_profile:
                        Intent intent = new Intent(MainActivity.this, EditProfileActivity.class);
                        startActivity(intent);
                        overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
                        break;
                    case R.id.action_change_password:
                        Intent intent2 = new Intent(MainActivity.this, ChangePasswordActivity.class);
                        startActivity(intent2);
                        overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
                        break;
                    case R.id.action_Signout:
                        Toast.makeText(MainActivity.this, "Share", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.action_search:
                        Intent intent4 = new Intent(MainActivity.this, SearchFilterActivity.class);
                        startActivity(intent4);
                        overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
                        Toast.makeText(MainActivity.this, "Search", Toast.LENGTH_SHORT).show();

                        //  boolean wrapInScrollView = true;
                        //  new MaterialDialog.Builder(MainActivity.this)

                        //          .customView(R.layout.dialog_search_filter, wrapInScrollView)
                        //        .positiveText("Search")
                        //        .show();
                        //  break;


                }

                return false;
            }
        });


        ivNavigation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                result.openDrawer();
//                Listview.smoothScrollToPosition (0);
//                toggleMenu ();
            }
        });


        try {
            assert actionBar != null;
            actionBar.setDisplayHomeAsUpEnabled(false);
            actionBar.setHomeButtonEnabled(false);
            actionBar.setDisplayShowTitleEnabled(false);
        } catch (Exception ignored) {
        }
    }

/*
    private void showLogOutDialog () {
        MaterialDialog dialog = new MaterialDialog.Builder (this)
                .limitIconToDefaultSize ()
                .content ("Do you wish to Sign Out?")
                .positiveText ("Yes")
                .negativeText ("No")
                .typeface (SetTypeFace.getTypeface (MainActivity.this), SetTypeFace.getTypeface (MainActivity.this))
                .onPositive (new MaterialDialog.SingleButtonCallback () {
                    @Override
                    public void onClick (@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
//                        dialog.dismiss ();
                        userDetailsPref.putStringPref (MainActivity.this, UserDetailsPref.USER_NAME, "");
                        Intent intent = new Intent (MainActivity.this, HomeActivity.class);
                        intent.setFlags (Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity (intent);
                        overridePendingTransition (R.anim.slide_in_left, R.anim.slide_out_right);
                    }
                }).build ();
        dialog.show ();
    }
    
    */

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return true;
    }
}

