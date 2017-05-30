package com.actiknow.clearsale.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;

import com.actiknow.clearsale.R;
import com.actiknow.clearsale.adapter.PropertyAdapter;
import com.actiknow.clearsale.model.Property;
import com.actiknow.clearsale.utils.AppConfigTags;
import com.actiknow.clearsale.utils.AppConfigURL;
import com.actiknow.clearsale.utils.BuyerDetailsPref;
import com.actiknow.clearsale.utils.Constants;
import com.actiknow.clearsale.utils.NetworkConnection;
import com.actiknow.clearsale.utils.SetTypeFace;
import com.actiknow.clearsale.utils.Utils;
import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
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

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    Bundle savedInstanceState;
    Toolbar toolbar;
    RecyclerView rvPropertyList;
    SwipeRefreshLayout swipeRefreshLayout;
    PropertyAdapter propertyAdapter;
    List<Property> propertyList = new ArrayList<> ();
    BuyerDetailsPref buyerDetailsPref;
    CoordinatorLayout clMain;
    ImageView ivFilter;
    ImageView ivOverflow;
    Menu menu2;
    ImageView ivNavigation;
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
//        setUpNavigationDrawer ();
//        getAllProperties ();
        isLogin ();
        getAllProperties ();
        this.savedInstanceState = savedInstanceState;
    }
    
    private void isLogin () {
        if (buyerDetailsPref.getIntPref (MainActivity.this, BuyerDetailsPref.BUYER_ID) == 0) {
            Intent myIntent = new Intent (this, LoginActivity.class);
            startActivity (myIntent);
        } else if (buyerDetailsPref.getIntPref (MainActivity.this, BuyerDetailsPref.PROFILE_STATUS) == 0) {
            Intent myIntent = new Intent (this, MyProfileActivity.class);
            startActivity (myIntent);
        }
        if (buyerDetailsPref.getIntPref (MainActivity.this, BuyerDetailsPref.BUYER_ID) == 0)
            finish ();
    }
    
    private void initView () {
        toolbar = (Toolbar) findViewById (R.id.toolbar);
        rvPropertyList = (RecyclerView) findViewById (R.id.rvPropertyList);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById (R.id.swipe_refresh_layout);
        clMain = (CoordinatorLayout) findViewById (R.id.clMain);
        ivFilter = (ImageView) findViewById (R.id.ivFilter);
        ivOverflow = (ImageView) findViewById (R.id.ivOverflow);
        ivNavigation = (ImageView) findViewById (R.id.ivNavigation);
    }
    
    private void initData () {
        buyerDetailsPref = BuyerDetailsPref.getInstance ();
        swipeRefreshLayout.setRefreshing (true);
        propertyList.clear ();
        propertyAdapter = new PropertyAdapter (this, propertyList);
        rvPropertyList.setAdapter (propertyAdapter);
        rvPropertyList.setHasFixedSize (true);
        rvPropertyList.setLayoutManager (new LinearLayoutManager (this, LinearLayoutManager.VERTICAL, false));
        rvPropertyList.setItemAnimator (new DefaultItemAnimator ());
        Utils.setTypefaceToAllViews (this, clMain);
    }
    
    private void initListener () {
        swipeRefreshLayout.setOnRefreshListener (new SwipeRefreshLayout.OnRefreshListener () {
            @Override
            public void onRefresh () {
                getAllProperties ();
            }
        });
        ivFilter.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View v) {
                Intent intent4 = new Intent (MainActivity.this, FilterActivity.class);
                startActivity (intent4);
                overridePendingTransition (R.anim.slide_in_up, R.anim.slide_out_up);
            }
        });
        ivOverflow.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View v) {
//                findViewById (R.id.overflow).performClick ();
//                onOptionsItemSelected (menu2.findItem (R.id.overflow));
//                MenuItem menuItem = menu.findItem (R.id.overflow).setOnMenuItemClickListener (new MenuItem.OnMenuItemClickListener () {
//                    @Override
//                    public boolean onMenuItemClick (MenuItem item) {
//                        Toast.makeText (MainActivity.this, "Karman 2", Toast.LENGTH_LONG).show ();
//                        return false;
//                    }
//                });
//                toolbar.getMenu ().getItem (1);
//                toolbar.showOverflowMenu ();
            }
        });
        ivNavigation.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View view) {
                result.openDrawer ();
            }
        });
    }
    
    /*
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


//        propertyList.add (new Property (1, 0, imagesList, "9300", "3", "3", "1828", "1947", "7919 Lowell Boulevard", "West Minster", true));
//        propertyList.add (new Property (2, 1, imagesList, "9300", "4", "2", "2448", "1925", "1137 Colorado Boulevard", "Denver", true));
//        propertyList.add (new Property (3, 1, imagesList, "9300", "3", "2", "1828", "1975", "268 South Newark Circle Lowell", "Aurora", true));
//        propertyList.add (new Property (4, 1, imagesList, "9300", "4", "2", "1762", "1954", "1541 Syracuse Street", "Denver", true));
//        propertyList.add (new Property (5, 2, imagesList, "9300", "3", "1", "1008", "1900", "625 East 11 Street", "Loveland", false));
//        propertyList.add (new Property (6, 2, imagesList, "9300", "3", "2", "1485", "1962", "6121 South Lvy Street", "Centennial", false));
//        propertyList.add (new Property (7, 2, imagesList, "9300", "3", "1", "1067", "1954", "1521 Syracuse Street", "Denver", false));
//        propertyList.add (new Property (8, 0, imagesList, "9300", "2", "1", "1680", "1936", "4131 South Elati Street", "Englewood", false));
//        propertyList.add (new Property (9, 0, imagesList, "9300", "2", "2", "8500", "1964", "11404 Claude Court", "Northglenn", false));
        swipeRefreshLayout.setRefreshing (false);
    }
    */
    private void getAllProperties () {
        if (NetworkConnection.isNetworkAvailable (MainActivity.this)) {
            Utils.showLog (Log.INFO, "" + AppConfigTags.URL, AppConfigURL.URL_PROPERTY_LIST, true);
            StringRequest strRequest1 = new StringRequest (Request.Method.POST, AppConfigURL.URL_PROPERTY_LIST,
                    new com.android.volley.Response.Listener<String> () {
                        @Override
                        public void onResponse (String response) {
                            propertyList.clear ();
                            Utils.showLog (Log.INFO, AppConfigTags.SERVER_RESPONSE, response, true);
                            if (response != null) {
                                try {
                                    JSONObject jsonObj = new JSONObject (response);
                                    boolean error = jsonObj.getBoolean (AppConfigTags.ERROR);
                                    String message = jsonObj.getString (AppConfigTags.MESSAGE);
                                    if (! error) {
                                        JSONArray jsonArrayProperty = jsonObj.getJSONArray (AppConfigTags.PROPERTIES);
                                        for (int i = 0; i < jsonArrayProperty.length (); i++) {
                                            JSONObject jsonObjectProperty = jsonArrayProperty.getJSONObject (i);
                                            Property property = new Property (
                                                    jsonObjectProperty.getInt (AppConfigTags.PROPERTY_ID),
                                                    jsonObjectProperty.getInt (AppConfigTags.PROPERTY_STATUS),
                                                    jsonObjectProperty.getString (AppConfigTags.PROPERTY_PRICE),
                                                    jsonObjectProperty.getString (AppConfigTags.PROPERTY_BEDROOMS),
                                                    jsonObjectProperty.getString (AppConfigTags.PROPERTY_BATHROOMS),
                                                    jsonObjectProperty.getString (AppConfigTags.PROPERTY_AREA),
                                                    jsonObjectProperty.getString (AppConfigTags.PROPERTY_BUILT_YEAR),
                                                    jsonObjectProperty.getString (AppConfigTags.PROPERTY_ADDRESS),
                                                    jsonObjectProperty.getString (AppConfigTags.PROPERTY_CITY),
                                                    jsonObjectProperty.getBoolean (AppConfigTags.PROPERTY_IS_OFFER));
                                            
                                            
                                            JSONArray jsonArrayPropertyImages = jsonObjectProperty.getJSONArray (AppConfigTags.PROPERTY_IMAGES);
                                            ArrayList<String> propertyImages = new ArrayList<> ();
                                            
                                            for (int j = 0; j < jsonArrayPropertyImages.length (); j++) {
                                                JSONObject jsonObjectImages = jsonArrayPropertyImages.getJSONObject (j);
                                                propertyImages.add (jsonObjectImages.getString (AppConfigTags.PROPERTY_IMAGE));
                                                property.setImageList (propertyImages);
                                            }
                                            propertyList.add (i, property);
                                            
                                            //  propertyList.add(property);
                                            
                                            
                                        }
                                        
                                        propertyAdapter.notifyDataSetChanged ();
                                        if (jsonArrayProperty.length () > 0) {
                                            swipeRefreshLayout.setRefreshing (false);
                                        }
                                        
                                    } else {
                                        Utils.showSnackBar (MainActivity.this, clMain, message, Snackbar.LENGTH_LONG, null, null);
                                    }
                                } catch (Exception e) {
                                    Utils.showSnackBar (MainActivity.this, clMain, getResources ().getString (R.string.snackbar_text_exception_occurred), Snackbar.LENGTH_LONG, getResources ().getString (R.string.snackbar_action_dismiss), null);
                                    e.printStackTrace ();
                                }
                            } else {
                                Utils.showSnackBar (MainActivity.this, clMain, getResources ().getString (R.string.snackbar_text_error_occurred), Snackbar.LENGTH_LONG, getResources ().getString (R.string.snackbar_action_dismiss), null);
                                Utils.showLog (Log.WARN, AppConfigTags.SERVER_RESPONSE, AppConfigTags.DIDNT_RECEIVE_ANY_DATA_FROM_SERVER, true);
                            }
                            swipeRefreshLayout.setRefreshing (false);
                        }
                    },
                    new com.android.volley.Response.ErrorListener () {
                        @Override
                        public void onErrorResponse (VolleyError error) {
                            swipeRefreshLayout.setRefreshing (false);
                            Utils.showLog (Log.ERROR, AppConfigTags.VOLLEY_ERROR, error.toString (), true);
                            Utils.showSnackBar (MainActivity.this, clMain, getResources ().getString (R.string.snackbar_text_error_occurred), Snackbar.LENGTH_LONG, getResources ().getString (R.string.snackbar_action_dismiss), null);
                        }
                    }) {
                @Override
                protected Map<String, String> getParams () throws AuthFailureError {
                    Map<String, String> params = new Hashtable<String, String> ();
                    params.put (AppConfigTags.TYPE, "property_list");
                    Utils.showLog (Log.INFO, AppConfigTags.PARAMETERS_SENT_TO_THE_SERVER, "" + params, true);
                    return params;
                }
                
                @Override
                public Map<String, String> getHeaders () throws AuthFailureError {
                    Map<String, String> params = new HashMap<> ();
                    params.put (AppConfigTags.HEADER_API_KEY, Constants.api_key);
                    Utils.showLog (Log.INFO, AppConfigTags.HEADERS_SENT_TO_THE_SERVER, "" + params, false);
                    return params;
                }
            };
            Utils.sendRequest (strRequest1, 60);
        } else {
            swipeRefreshLayout.setRefreshing (false);
            Utils.showSnackBar (this, clMain, getResources ().getString (R.string.snackbar_text_no_internet_connection_available), Snackbar.LENGTH_LONG, getResources ().getString (R.string.snackbar_action_go_to_settings), new View.OnClickListener () {
                @Override
                public void onClick (View v) {
                    Intent dialogIntent = new Intent (Settings.ACTION_SETTINGS);
                    dialogIntent.addFlags (Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity (dialogIntent);
                }
            });
        }
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
                        .withName (buyerDetailsPref.getStringPref (MainActivity.this, BuyerDetailsPref.BUYER_NAME))
                        .withEmail (buyerDetailsPref.getStringPref (MainActivity.this, BuyerDetailsPref.BUYER_EMAIL)))
                .withProfileImagesClickable (false)
                .withTypeface (SetTypeFace.getTypeface (this))
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
                        new PrimaryDrawerItem ().withName ("My Favourites").withIcon (FontAwesome.Icon.faw_home).withIdentifier (2),
                        new PrimaryDrawerItem ().withName ("How It Works").withIcon (FontAwesome.Icon.faw_home).withIdentifier (3).withSelectable (false),
                        new PrimaryDrawerItem ().withName ("About Us").withIcon (FontAwesome.Icon.faw_home).withIdentifier (4).withSelectable (false),
                        new PrimaryDrawerItem ().withName ("Testimonials").withIcon (FontAwesome.Icon.faw_home).withIdentifier (5).withSelectable (false),
                        new PrimaryDrawerItem ().withName ("Contact Us").withIcon (FontAwesome.Icon.faw_home).withIdentifier (6).withSelectable (false),
                        new PrimaryDrawerItem ().withName ("FAQ").withIcon (FontAwesome.Icon.faw_home).withIdentifier (7).withSelectable (false),
                        new PrimaryDrawerItem ().withName ("My Profile").withIcon (FontAwesome.Icon.faw_home).withIdentifier (8).withSelectable (false),
                        new PrimaryDrawerItem ().withName ("Change Password").withIcon (FontAwesome.Icon.faw_home).withIdentifier (9).withSelectable (false),
                        new PrimaryDrawerItem ().withName ("Sign Out").withIcon (FontAwesome.Icon.faw_home).withIdentifier (10).withSelectable (false)
                )
                .withSavedInstance (savedInstanceState)
                .withOnDrawerItemClickListener (new Drawer.OnDrawerItemClickListener () {
                    @Override
                    public boolean onItemClick (View view, int position, IDrawerItem drawerItem) {
                        switch ((int) drawerItem.getIdentifier ()) {
                            case 2:
                                Intent intent = new Intent (MainActivity.this, MyFavouriteActivity.class);
                                startActivity (intent);
                                overridePendingTransition (R.anim.slide_in_right, R.anim.slide_out_left);
                                break;
                            case 3:
                                Intent intent2 = new Intent (MainActivity.this, HowItWorksActivity.class);
                                startActivity (intent2);
                                overridePendingTransition (R.anim.slide_in_right, R.anim.slide_out_left);
                                break;
                            case 4:
                                Intent intent3 = new Intent (MainActivity.this, AboutUsActivity.class);
                                startActivity (intent3);
                                overridePendingTransition (R.anim.slide_in_right, R.anim.slide_out_left);
                                break;
                            case 5:
                                Intent intent4 = new Intent (MainActivity.this, TestimonialActivity.class);
                                startActivity (intent4);
                                overridePendingTransition (R.anim.slide_in_right, R.anim.slide_out_left);
                                break;
                            case 6:
                                Intent intent5 = new Intent (MainActivity.this, ContactUsActivity.class);
                                startActivity (intent5);
                                overridePendingTransition (R.anim.slide_in_right, R.anim.slide_out_left);
                                break;
                            case 7:
                                Intent intent6 = new Intent (MainActivity.this, FAQActivity.class);
                                startActivity (intent6);
                                overridePendingTransition (R.anim.slide_in_right, R.anim.slide_out_left);
                                break;
                            case 8:
                                Intent intent7 = new Intent (MainActivity.this, MyProfileActivity.class);
                                startActivity (intent7);
                                overridePendingTransition (R.anim.slide_in_up, R.anim.slide_out_up);
                                break;
                            case 9:
                                Intent intent8 = new Intent (MainActivity.this, ChangePasswordActivity.class);
                                startActivity (intent8);
                                overridePendingTransition (R.anim.slide_in_up, R.anim.slide_out_up);
                                break;
                            case 10:
                                showLogOutDialog ();
                                break;
                        }
                        return false;
                    }
                })
                .build ();
//        result.getActionBarDrawerToggle ().setDrawerIndicatorEnabled (false);
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
                        buyerDetailsPref.putStringPref (MainActivity.this, BuyerDetailsPref.BUYER_NAME, "");
                        buyerDetailsPref.putStringPref (MainActivity.this, BuyerDetailsPref.BUYER_EMAIL, "");
                        buyerDetailsPref.putStringPref (MainActivity.this, BuyerDetailsPref.BUYER_MOBILE, "");
                        buyerDetailsPref.putStringPref (MainActivity.this, BuyerDetailsPref.BUYER_LOGIN_KEY, "");
                        buyerDetailsPref.putIntPref (MainActivity.this, BuyerDetailsPref.BUYER_ID, 0);
                        buyerDetailsPref.putStringPref (MainActivity.this, BuyerDetailsPref.BUYER_ACCESS_TOKEN, "");
                        buyerDetailsPref.putStringPref (MainActivity.this, BuyerDetailsPref.PROFILE_HOME_TYPE, "");
                        buyerDetailsPref.putStringPref (MainActivity.this, BuyerDetailsPref.PROFILE_STATE, "");
                        buyerDetailsPref.putStringPref (MainActivity.this, BuyerDetailsPref.PROFILE_HOME_BUDGET, "");
                        Intent intent = new Intent (MainActivity.this, LoginActivity.class);
                        intent.setFlags (Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity (intent);
                        overridePendingTransition (R.anim.slide_in_left, R.anim.slide_out_right);
                    }
                }).build ();
        dialog.show ();
    }
    
    @Override
    public void onBackPressed () {
        if (result != null && result.isDrawerOpen ()) {
            result.closeDrawer ();
        } else {
            super.onBackPressed ();
            finish ();
            overridePendingTransition (R.anim.slide_in_left, R.anim.slide_out_right);
        }
    }
}