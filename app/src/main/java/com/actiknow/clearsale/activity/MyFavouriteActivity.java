package com.actiknow.clearsale.activity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;

import com.actiknow.clearsale.R;
import com.actiknow.clearsale.adapter.PropertyAdapter;
import com.actiknow.clearsale.model.Property;
import com.actiknow.clearsale.utils.AppConfigTags;
import com.actiknow.clearsale.utils.AppConfigURL;
import com.actiknow.clearsale.utils.Constants;
import com.actiknow.clearsale.utils.NetworkConnection;
import com.actiknow.clearsale.utils.Utils;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;


public class MyFavouriteActivity extends AppCompatActivity {
    RecyclerView rvFavourites;
    ArrayList<Property> favouritePropertyList = new ArrayList<> ();
    PropertyAdapter propertyAdapter;
    CoordinatorLayout clMain;
    SwipeRefreshLayout swipeRefreshLayout;
    
    RelativeLayout rlBack;
    
    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_my_favourite);
        initView ();
        initData ();
        initListener ();
        getAllProperties ();
    }
    
    private void initView () {
        rlBack = (RelativeLayout) findViewById (R.id.rlBack);
        rvFavourites = (RecyclerView) findViewById (R.id.rvFavourite);
        clMain = (CoordinatorLayout) findViewById (R.id.clMain);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById (R.id.swipe_refresh_layout);
    }
    
    private void initData () {
        swipeRefreshLayout.setRefreshing (true);
        favouritePropertyList.clear ();
        propertyAdapter = new PropertyAdapter (MyFavouriteActivity.this, favouritePropertyList);
        rvFavourites.setAdapter (propertyAdapter);
        rvFavourites.setHasFixedSize (true);
        rvFavourites.setLayoutManager (new LinearLayoutManager (this, LinearLayoutManager.VERTICAL, false));
        rvFavourites.setItemAnimator (new DefaultItemAnimator ());
        Utils.setTypefaceToAllViews (this, rlBack);
    }
    
    private void initListener () {
        swipeRefreshLayout.setOnRefreshListener (new SwipeRefreshLayout.OnRefreshListener () {
            @Override
            public void onRefresh () {
                swipeRefreshLayout.setRefreshing (true);
                getAllProperties ();
            }
        });
        rlBack.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View v) {
                finish ();
                overridePendingTransition (R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });
    }
    
    /*
        private void getAllProducts() {
            favouritePropertyList.clear();
            favouritePropertyList.add(new Property(1, "3", "3", "1828", "Year Build : 1947", "7919 Lowell Boulevard", "West Minster", true, true, "http://clearsale.com/theme/theme1/seller_files/exterior/property_274/7619f013e5692f4a0a72e30bd7bf10b3IMG_4635.jpg"));
            favouritePropertyList.add(new Property(2, "4", "2", "2448", "Year Build : 1925", "1137 Colorado Boulevard", "Denver", true, true, "http://clearsale.com/theme/theme1/seller_files/exterior/property_324/954d58de65cdc62a8bb2e7e5574eb977IMG_4909.jpg"));
            favouritePropertyList.add(new Property(3, "3", "2", "1828", "Year Build : 1975", "268 South Newark Circle Lowell", "Aurora", true, true, "http://clearsale.com/theme/theme1/seller_files/exterior/property_348/5aece0048c495cd61a627f02f6aaf49fIMG_5156.jpg"));
            favouritePropertyList.add(new Property(4, "4", "2", "1762", "Year Build : 1954", "1541 Syracuse Street", "Denver", true, true, "http://clearsale.com/theme/theme1/seller_files/exterior/property_336/814d5ddee03e07c10302832a8c81a72bIMG_5117.jpg"));
            favouritePropertyList.add(new Property(5, "3", "1", "1008", "Year Build : 1900", "625 East 11 Street", "Loveland", false, false, "http://clearsale.com/theme/theme1/images/no-thumb1.png"));
            favouritePropertyList.add(new Property(6, "3", "2", "1485", "Year Build : 1962", "6121 South Lvy Street", "Centennial", false, false, "http://clearsale.com/theme/theme1/seller_files/exterior/property_250/fd2ba72f51cc65b571b1d83b45b80d9eIMG_4354.jpg"));
            favouritePropertyList.add(new Property(7, "3", "1", "1067", "Year Build : 1954", "1521 Syracuse Street", "Denver", false, false, "http://clearsale.com/theme/theme1/seller_files/exterior/property_321/92ee4eb01cdbe3190152a7bfd2a12411IMG_4764.jpg"));
            favouritePropertyList.add(new Property(8, "2", "1", "1680", "Year Build : 1936", "4131 South Elati Street", "Englewood", false, false, "http://clearsale.com/theme/theme1/seller_files/exterior/property_134/461b336dc55ba4f83bc24ad5b2de268cIMG_3526.jpg"));
            favouritePropertyList.add(new Property(9, "2", "2", "850", "Year Build : 1964", "11404 Claude Court", "Northglenn", false, false, "http://clearsale.com/theme/theme1/seller_files/exterior/property_198/e5ad1696545ed6e08f6433e72cadda90IMG_1302.JPG"));
            // favouritePropertyList.add(new AllProperty(10,"3 bd","3ba","1828 sqft","Year Build : 1947","7919 Lowell Boulevard","West Minster",true,true,"http://clearsale.com/theme/theme1/seller_files/exterior/property_274/7619f013e5692f4a0a72e30bd7bf10b3IMG_4635.jpg"));
            swipeRefreshLayout.setRefreshing(false);
    
        }
        */
    private void getAllProperties () {
        if (NetworkConnection.isNetworkAvailable (MyFavouriteActivity.this)) {
            Utils.showLog (Log.INFO, "" + AppConfigTags.URL, AppConfigURL.URL_PROPERTY_LIST, true);
            StringRequest strRequest1 = new StringRequest (Request.Method.POST, AppConfigURL.URL_PROPERTY_LIST,
                    new com.android.volley.Response.Listener<String> () {
                        @Override
                        public void onResponse (String response) {
                            favouritePropertyList.clear ();
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
                                            favouritePropertyList.add (i, property);
                                        }
                                        
                                        propertyAdapter.notifyDataSetChanged ();
                                        if (jsonArrayProperty.length () > 0) {
                                            swipeRefreshLayout.setRefreshing (false);
                                        }
                                        
                                    } else {
                                        Utils.showSnackBar (MyFavouriteActivity.this, clMain, message, Snackbar.LENGTH_LONG, null, null);
                                    }
                                } catch (Exception e) {
                                    Utils.showSnackBar (MyFavouriteActivity.this, clMain, getResources ().getString (R.string.snackbar_text_exception_occurred), Snackbar.LENGTH_LONG, getResources ().getString (R.string.snackbar_action_dismiss), null);
                                    e.printStackTrace ();
                                }
                            } else {
                                Utils.showSnackBar (MyFavouriteActivity.this, clMain, getResources ().getString (R.string.snackbar_text_error_occurred), Snackbar.LENGTH_LONG, getResources ().getString (R.string.snackbar_action_dismiss), null);
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
                            Utils.showSnackBar (MyFavouriteActivity.this, clMain, getResources ().getString (R.string.snackbar_text_error_occurred), Snackbar.LENGTH_LONG, getResources ().getString (R.string.snackbar_action_dismiss), null);
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
}



