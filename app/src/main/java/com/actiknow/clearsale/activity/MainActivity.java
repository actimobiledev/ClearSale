package com.actiknow.clearsale.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
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
import com.actiknow.clearsale.adapter.PropertyAdapter;
import com.actiknow.clearsale.model.Property;
import com.actiknow.clearsale.utils.SetTypeFace;
import com.actiknow.clearsale.utils.UserDetailsPref;
import com.actiknow.clearsale.utils.Utils;
import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.bumptech.glide.Glide;
import com.mikepenz.fontawesome_typeface_library.FontAwesome;
import com.mikepenz.iconics.IconicsDrawable;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
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
    PropertyAdapter propertyAdapter;
    List<Property> propertyList = new ArrayList<> ();
    UserDetailsPref userDetailsPref;
    private AccountHeader headerResult = null;
    private Drawer result = null;
    
    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_main);
        initView ();
        initData ();
        initListener ();
        initDrawer ();
        setUpNavigationDrawer ();
        getAllProperties ();
        isLogin ();
        this.savedInstanceState = savedInstanceState;
        
    }
    
    private void isLogin () {
        if (userDetailsPref.getIntPref (MainActivity.this, UserDetailsPref.USER_ID) == 0) {
            Intent myIntent = new Intent (this, LoginActivity.class);
            startActivity (myIntent);
        }
    }
    
    private void initView () {
        rvPropertyList = (RecyclerView) findViewById (R.id.rvPropertyList);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById (R.id.swipe_refresh_layout);
    }
    
    private void initData () {
        userDetailsPref = UserDetailsPref.getInstance ();
        
        swipeRefreshLayout.setRefreshing (true);
        propertyList.clear ();
        propertyAdapter = new PropertyAdapter (this, propertyList);
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager (1, StaggeredGridLayoutManager.VERTICAL);
        rvPropertyList.setAdapter (propertyAdapter);
        rvPropertyList.setHasFixedSize (true);
        rvPropertyList.setLayoutManager (layoutManager);
    }
    
    private void initListener () {
        swipeRefreshLayout.setOnRefreshListener (new SwipeRefreshLayout.OnRefreshListener () {
            @Override
            public void onRefresh () {
                getAllProperties ();
            }
        });
    }
    
    private void getAllProperties () {
        propertyList.clear ();
        
        
        ArrayList<String> imagesList = new ArrayList<String> ();
        imagesList.clear ();
        imagesList.add ("http://clearsale.com/theme/theme1/seller_files/exterior/property_327/dcca01ddf8b02fb5d2fe89d3c55eb5dcExterior%20pic.png");
        imagesList.add ("http://clearsale.com/theme/theme1/seller_files/interior/property_327/d755cd303ff826ce587fb0e55e6bc1c6IMG_5034.jpg");
        imagesList.add ("http://clearsale.com/theme/theme1/seller_files/interior/property_327/63502fc15c1d04e5ad9f095d8bd03b2aIMG_5035.jpg");
//        imagesList.add ("http://clearsale.com/theme/theme1/seller_files/interior/property_327/1a6c485de807e99fbb7b11a8f06e354fIMG_5036.jpg");
//        imagesList.add ("http://clearsale.com/theme/theme1/seller_files/interior/property_327/d913733c109e622efea54bf04c778d1bIMG_5038.jpg");
//        imagesList.add ("http://clearsale.com/theme/theme1/seller_files/interior/property_327/1d525c19d5c659f9d39a1abb941baf75IMG_5039.jpg");
//        imagesList.add ("http://clearsale.com/theme/theme1/seller_files/interior/property_327/8bafdd842b1558edd07efd4b58b69ecfIMG_5040.jpg");
//        imagesList.add ("http://clearsale.com/theme/theme1/seller_files/interior/property_327/6465f55d083394753d7656fbea7b5df6IMG_5041.jpg");
//        imagesList.add ("http://clearsale.com/theme/theme1/seller_files/interior/property_327/23a5f19eee30dfcc0f7ddd7b4116986cIMG_5042.jpg");
//        imagesList.add ("http://clearsale.com/theme/theme1/seller_files/interior/property_327/b41719c7631a3d7bc0c2d246cd2fb5daIMG_5043.jpg");
//        imagesList.add ("http://clearsale.com/theme/theme1/seller_files/interior/property_327/1ee4363f712ced227c0b154029e5cfb8IMG_5048.jpg");
//        imagesList.add ("http://clearsale.com/theme/theme1/seller_files/interior/property_327/3fd6c0348b2f1443715d0ee4139632c8IMG_5053.jpg");
        
        
        propertyList.add (new Property (1, 0, imagesList, "9300", "3", "3", "1828", "1947", "7919 Lowell Boulevard", "West Minster", true));
        propertyList.add (new Property (2, 1, imagesList, "9300", "4", "2", "2448", "1925", "1137 Colorado Boulevard", "Denver", true));
        propertyList.add (new Property (3, 1, imagesList, "9300", "3", "2", "1828", "1975", "268 South Newark Circle Lowell", "Aurora", true));
        propertyList.add (new Property (4, 1, imagesList, "9300", "4", "2", "1762", "1954", "1541 Syracuse Street", "Denver", true));
        propertyList.add (new Property (5, 2, imagesList, "9300", "3", "1", "1008", "1900", "625 East 11 Street", "Loveland", false));
        propertyList.add (new Property (6, 2, imagesList, "9300", "3", "2", "1485", "1962", "6121 South Lvy Street", "Centennial", false));
        propertyList.add (new Property (7, 2, imagesList, "9300", "3", "1", "1067", "1954", "1521 Syracuse Street", "Denver", false));
        propertyList.add (new Property (8, 0, imagesList, "9300", "2", "1", "1680", "1936", "4131 South Elati Street", "Englewood", false));
        propertyList.add (new Property (9, 0, imagesList, "9300", "2", "2", "8500", "1964", "11404 Claude Court", "Northglenn", false));
        swipeRefreshLayout.setRefreshing (false);
    }
    
    private void initDrawer () {
        DrawerImageLoader.init (new AbstractDrawerImageLoader () {
            @Override
            public void set (ImageView imageView, Uri uri, Drawable placeholder) {
                //  Glide.with(imageView.getContext()).load(uri).placeholder(placeholder).into(imageView);
            }
            
            @Override
            public void cancel (ImageView imageView) {
                Glide.clear (imageView);
            }
            
            @Override
            public Drawable placeholder (Context ctx, String tag) {
                //define different placeholders for different imageView targets
                //default tags are accessible via the DrawerImageLoader.Tags
                //custom ones can be checked via string. see the CustomUrlBasePrimaryDrawerItem LINE 111
                if (DrawerImageLoader.Tags.PROFILE.name ().equals (tag)) {
                    return DrawerUIUtils.getPlaceHolder (ctx);
                } else if (DrawerImageLoader.Tags.ACCOUNT_HEADER.name ().equals (tag)) {
                    return new IconicsDrawable (ctx).iconText (" ").backgroundColorRes (com.mikepenz.materialdrawer.R.color.colorPrimary).sizeDp (56);
                } else if ("customUrlItem".equals (tag)) {
                    return new IconicsDrawable (ctx).iconText (" ").backgroundColorRes (R.color.md_white_1000);
                }
                
                //we use the default one for
                //DrawerImageLoader.Tags.PROFILE_DRAWER_ITEM.name()
                
                return super.placeholder (ctx, tag);
            }
        });
        
        
        headerResult = new AccountHeaderBuilder ()
                .withActivity (this)
                .withCompactStyle (true)
                .addProfiles (new ProfileDrawerItem ()
                        .withName (userDetailsPref.getStringPref (MainActivity.this, UserDetailsPref.USER_NAME))
                        .withEmail (userDetailsPref.getStringPref (MainActivity.this, UserDetailsPref.USER_EMAIL)))
                .withProfileImagesClickable (false)
                .withPaddingBelowHeader (false)
                .withSelectionListEnabledForSingleProfile (false)
                .withHeaderBackground (R.color.colorPrimaryDark)
                .withSavedInstance (savedInstanceState)
                .build ();
        
        
        result = new DrawerBuilder ()
                .withActivity (this)
//                .withAccountHeader(headerResult)
//                .withToolbar (toolbar)
//                .withItemAnimator (new AlphaCrossFadeAnimator ())
                .addDrawerItems (
                        new PrimaryDrawerItem ().withName ("Home").withIcon (FontAwesome.Icon.faw_home).withIdentifier (1),
                        new PrimaryDrawerItem ().withName ("How It Works").withIcon (FontAwesome.Icon.faw_home).withIdentifier (2).withSelectable (false),
                        new PrimaryDrawerItem ().withName ("About Us").withIcon (FontAwesome.Icon.faw_home).withIdentifier (3).withSelectable (false),
                        new PrimaryDrawerItem ().withName ("Testimonials").withIcon (FontAwesome.Icon.faw_home).withIdentifier (4).withSelectable (false),
                        new PrimaryDrawerItem ().withName ("Contact Us").withIcon (FontAwesome.Icon.faw_home).withIdentifier (5).withSelectable (false),
                        new PrimaryDrawerItem ().withName ("FAQ").withIcon (FontAwesome.Icon.faw_home).withIdentifier (6).withSelectable (false),
                        new PrimaryDrawerItem ().withName ("Sign Out").withIcon (FontAwesome.Icon.faw_home).withIdentifier (7).withSelectable (false)
                )
                .withSavedInstance (savedInstanceState)
                .withOnDrawerItemClickListener (new Drawer.OnDrawerItemClickListener () {
                    @Override
                    public boolean onItemClick (View view, int position, IDrawerItem drawerItem) {
                        switch ((int) drawerItem.getIdentifier ()) {
                            case 2:
                                Intent intent = new Intent (MainActivity.this, HowItWorksActivity.class);
                                startActivity (intent);
                                overridePendingTransition (R.anim.slide_in_right, R.anim.slide_out_left);
                                Utils.showLog (Log.ERROR, "position ", "" + position, true);
                                break;
        
                            case 3:
                                Intent intent3 = new Intent (MainActivity.this, AboutUsActivity.class);
                                startActivity (intent3);
                                overridePendingTransition (R.anim.slide_in_right, R.anim.slide_out_left);
                                Utils.showLog (Log.ERROR, "position ", "" + position, true);
                                break;
        
                            case 4:
                                Intent intent4 = new Intent (MainActivity.this, TestimonialActivity.class);
                                startActivity (intent4);
                                overridePendingTransition (R.anim.slide_in_right, R.anim.slide_out_left);
                                Utils.showLog (Log.ERROR, "position ", "" + position, true);
                                break;
        
                            case 5:
                                Intent intent5 = new Intent (MainActivity.this, ContactUsActivity.class);
                                startActivity (intent5);
                                overridePendingTransition (R.anim.slide_in_right, R.anim.slide_out_left);
                                Utils.showLog (Log.ERROR, "position ", "" + position, true);
                                break;
        
                            case 6:
                                Intent intent6 = new Intent (MainActivity.this, FAQActivity.class);
                                startActivity (intent6);
                                overridePendingTransition (R.anim.slide_in_right, R.anim.slide_out_left);
                                Utils.showLog (Log.ERROR, "position ", "" + position, true);
                                break;
                        }
                        return false;
                    }
                })
                .build ();
//        result.getActionBarDrawerToggle ().setDrawerIndicatorEnabled (false);
        
        
    }
    
    @Override
    public void onBackPressed () {
        //handle the back press :D close the drawer first and if the drawer is closed close the activity
        if (result != null && result.isDrawerOpen ()) {
            result.closeDrawer ();
        } else {
            super.onBackPressed ();
        }
    }
    
    private void setUpNavigationDrawer () {
        toolbar = (Toolbar) findViewById (R.id.toolbar1);
        setSupportActionBar (toolbar);
        ActionBar actionBar = getSupportActionBar ();
        ImageView ivNavigation = (ImageView) findViewById (R.id.ivNavigation);
        toolbar.inflateMenu (R.menu.toolbar_menu);
        
        
        toolbar.setOnMenuItemClickListener (new Toolbar.OnMenuItemClickListener () {
            @Override
            public boolean onMenuItemClick (MenuItem menuItem) {
    
                switch (menuItem.getItemId ()) {
                    case R.id.action_edit_profile:
                        Intent intent = new Intent (MainActivity.this, EditProfileActivity.class);
                        startActivity (intent);
                        overridePendingTransition (R.anim.slide_in_up, R.anim.slide_out_up);
                        break;
                    case R.id.action_change_password:
                        Intent intent2 = new Intent (MainActivity.this, ChangePasswordActivity.class);
                        startActivity (intent2);
                        overridePendingTransition (R.anim.slide_in_up, R.anim.slide_out_up);
                        break;
                    case R.id.action_Signout:
                        Toast.makeText (MainActivity.this, "Share", Toast.LENGTH_SHORT).show ();
                        break;
                    case R.id.action_search:
                        Intent intent4 = new Intent (MainActivity.this, SearchFilterActivity.class);
                        startActivity (intent4);
                        overridePendingTransition (R.anim.slide_in_up, R.anim.slide_out_up);
                        Toast.makeText (MainActivity.this, "Search", Toast.LENGTH_SHORT).show ();
                        
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
        
        
        ivNavigation.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View view) {
                result.openDrawer ();
//                Listview.smoothScrollToPosition (0);
//                toggleMenu ();
            }
        });
        
        
        try {
            assert actionBar != null;
            actionBar.setDisplayHomeAsUpEnabled (false);
            actionBar.setHomeButtonEnabled (false);
            actionBar.setDisplayShowTitleEnabled (false);
        } catch (Exception ignored) {
        }
    }
    
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
                        Intent intent = new Intent (MainActivity.this, LoginActivity.class);
                        intent.setFlags (Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity (intent);
                        overridePendingTransition (R.anim.slide_in_left, R.anim.slide_out_right);
                    }
                }).build ();
        dialog.show ();
    }
    
    @Override
    public boolean onCreateOptionsMenu (Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater ().inflate (R.menu.toolbar_menu, menu);
        return true;
    }
}