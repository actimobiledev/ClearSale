package com.actiknow.clearsale.activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
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
import com.actiknow.clearsale.utils.FilterDetailsPref;
import com.actiknow.clearsale.utils.GPSTracker;
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
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStates;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.linkedin.platform.LISessionManager;
import com.mikepenz.fontawesome_typeface_library.FontAwesome;
import com.mikepenz.iconics.IconicsDrawable;
import com.mikepenz.iconics.typeface.IIcon;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.holder.ImageHolder;
import com.mikepenz.materialdrawer.holder.StringHolder;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;
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
    
    public static int PERMISSION_REQUEST_CODE = 11;
    final int CURRENT_LOCATION_REQUEST_CODE = 1;
    GoogleApiClient client;
    Double currentLatitude = 0.0;
    Double currentLongitude = 0.0;
    
    
    Bundle savedInstanceState;
    Toolbar toolbar;
    RecyclerView rvPropertyList;
    SwipeRefreshLayout swipeRefreshLayout;
    PropertyAdapter propertyAdapter;
    List<Property> propertyList = new ArrayList<> ();
    BuyerDetailsPref buyerDetailsPref;
    FilterDetailsPref filterDetailsPref;
    CoordinatorLayout clMain;
    ImageView ivFilter;
    ImageView ivMaps;
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
        isLogin ();
        getAllProperties ();
        checkPermissions ();
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
        ivMaps = (ImageView) findViewById (R.id.ivMaps);
    }
    
    private void initData () {
        client = new GoogleApiClient.Builder (this).addApi (AppIndex.API).build ();
    
        FacebookSdk.sdkInitialize (this.getApplicationContext ());
        buyerDetailsPref = BuyerDetailsPref.getInstance ();
        filterDetailsPref = FilterDetailsPref.getInstance ();
        swipeRefreshLayout.setRefreshing (true);
        propertyList.clear ();
        propertyAdapter = new PropertyAdapter (this, propertyList);
        rvPropertyList.setAdapter (propertyAdapter);
        rvPropertyList.setHasFixedSize (true);
        rvPropertyList.setLayoutManager (new LinearLayoutManager (this, LinearLayoutManager.VERTICAL, false));
        rvPropertyList.setItemAnimator (new DefaultItemAnimator ());
        Utils.setTypefaceToAllViews (this, clMain);
    
        if (filterDetailsPref.getBooleanPref (this, FilterDetailsPref.FILTER_APPLIED)) {
            ivFilter.setImageDrawable (getResources ().getDrawable (R.drawable.ic_filter_checked));
        } else {
            ivFilter.setImageDrawable (getResources ().getDrawable (R.drawable.ic_filter));
        }
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
    
        ivMaps.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View v) {
                Intent intent = new Intent (MainActivity.this, AllPropertyLocationActivity.class);
                startActivity (intent);
            }
        });
    }
    
    
    private void initLocation (final int request_code) {
        GoogleApiClient googleApiClient;
        googleApiClient = new GoogleApiClient.Builder (this)
                .addApi (LocationServices.API)
                .addConnectionCallbacks (new GoogleApiClient.ConnectionCallbacks () {
                    @Override
                    public void onConnected (@Nullable Bundle bundle) {
                    }
                    
                    @Override
                    public void onConnectionSuspended (int i) {
                    }
                })
                .addOnConnectionFailedListener (new GoogleApiClient.OnConnectionFailedListener () {
                    @Override
                    public void onConnectionFailed (@NonNull ConnectionResult connectionResult) {
                    }
                }).build ();
        googleApiClient.connect ();
        
        LocationRequest locationRequest2 = LocationRequest.create ();
        locationRequest2.setPriority (LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest2.setInterval (30 * 1000);
        locationRequest2.setFastestInterval (5 * 1000);
        LocationSettingsRequest.Builder builder2 = new LocationSettingsRequest.Builder ().addLocationRequest (locationRequest2);
        builder2.setAlwaysShow (true); //this is the key ingredient
        
        PendingResult<LocationSettingsResult> result2 =
                LocationServices.SettingsApi.checkLocationSettings (googleApiClient, builder2.build ());
        result2.setResultCallback (new ResultCallback<LocationSettingsResult> () {
            @Override
            public void onResult (LocationSettingsResult result) {
                final Status status = result.getStatus ();
                final LocationSettingsStates state = result.getLocationSettingsStates ();
                switch (status.getStatusCode ()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        // All location settings are satisfied. The client can initialize location requests here.
                        switch (request_code) {
                            case CURRENT_LOCATION_REQUEST_CODE:
                                GPSTracker gps = new GPSTracker (MainActivity.this);
                                if (gps.canGetLocation ()) {
                                    currentLatitude = gps.getLatitude ();
                                    currentLongitude = gps.getLongitude ();
                                    Log.e ("latitude", String.valueOf (currentLatitude));
                                    Log.e ("currentLongitude", String.valueOf (currentLongitude));
                                }
                                break;
                        }
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        // Location settings are not satisfied. But could be fixed by showing the user a dialog.
                        try {
                            status.startResolutionForResult (MainActivity.this, request_code);
                        } catch (IntentSender.SendIntentException e) {
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        // Location settings are not satisfied. However, we have no way to fix the settings so we won't show the dialog.
                        break;
                }
            }
        });
    }
    
    
    public void checkPermissions () {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission (Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                    checkSelfPermission (Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                    checkSelfPermission (Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions (new String[] {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.READ_EXTERNAL_STORAGE},
                        MainActivity.PERMISSION_REQUEST_CODE);
            }
        }
    }
    
    @Override
    @TargetApi(23)
    public void onRequestPermissionsResult (int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult (requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            for (int i = 0, len = permissions.length; i < len; i++) {
                String permission = permissions[i];
                if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                    boolean showRationale = shouldShowRequestPermissionRationale (permission);
                    if (! showRationale) {
                        AlertDialog.Builder builder = new AlertDialog.Builder (MainActivity.this);
                        builder.setMessage ("Permission are required please enable them on the App Setting page")
                                .setCancelable (false)
                                .setPositiveButton ("OK", new DialogInterface.OnClickListener () {
                                    public void onClick (DialogInterface dialog, int id) {
                                        dialog.dismiss ();
                                        Intent intent = new Intent (Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                                                Uri.fromParts ("package", getPackageName (), null));
                                        startActivity (intent);
                                    }
                                });
                        AlertDialog alert = builder.create ();
                        alert.show ();
                    } else if (Manifest.permission.ACCESS_FINE_LOCATION.equals (permission)) {
                    } else if (Manifest.permission.ACCESS_COARSE_LOCATION.equals (permission)) {
                    } else if (Manifest.permission.READ_EXTERNAL_STORAGE.equals (permission)) {
                    }
                }
            }
        }
        
        
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
        }
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
                                        JSONArray jsonArrayCities = jsonObj.getJSONArray (AppConfigTags.CITIES);
                                        filterDetailsPref.putStringPref (MainActivity.this, FilterDetailsPref.FILTER_CITIES_JSON, jsonArrayCities.toString ());
                                        String cities = "";
                                        for (int j = 0; j < jsonArrayCities.length (); j++) {
                                            JSONObject jsonObject = jsonArrayCities.getJSONObject (j);
                                            if (j == jsonArrayCities.length () - 1) {
                                                cities = cities + jsonObject.getInt (AppConfigTags.CITY_ID);
                                            } else {
                                                cities = cities + jsonObject.getInt (AppConfigTags.CITY_ID) + ",";
                                            }
    
                                        }
                                        
                                        
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
                                                    jsonObjectProperty.getBoolean (AppConfigTags.PROPERTY_IS_OFFER),
                                                    jsonObjectProperty.getBoolean (AppConfigTags.PROPERTY_IS_FAVOURITE));
                                            
                                            JSONArray jsonArrayPropertyImages = jsonObjectProperty.getJSONArray (AppConfigTags.PROPERTY_IMAGES);
                                            ArrayList<String> propertyImages = new ArrayList<> ();
                                            
                                            for (int j = 0; j < jsonArrayPropertyImages.length (); j++) {
                                                JSONObject jsonObjectImages = jsonArrayPropertyImages.getJSONObject (j);
                                                propertyImages.add (jsonObjectImages.getString (AppConfigTags.PROPERTY_IMAGE));
                                                property.setImageList (propertyImages);
                                            }
                                            propertyList.add (i, property);
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
                    params.put (AppConfigTags.BUYER_ID, String.valueOf (buyerDetailsPref.getIntPref (MainActivity.this, BuyerDetailsPref.BUYER_ID)));
                    params.put (AppConfigTags.FILTER, String.valueOf (filterDetailsPref.getBooleanPref (MainActivity.this, FilterDetailsPref.FILTER_APPLIED)));
                    if (filterDetailsPref.getBooleanPref (MainActivity.this, FilterDetailsPref.FILTER_APPLIED)) {
                        params.put (AppConfigTags.FILTER_BEDROOMS, filterDetailsPref.getStringPref (MainActivity.this, FilterDetailsPref.FILTER_BEDROOMS));
                        params.put (AppConfigTags.FILTER_BATHROOMS, filterDetailsPref.getStringPref (MainActivity.this, FilterDetailsPref.FILTER_BATHROOMS));
                        params.put (AppConfigTags.FILTER_STATUS, filterDetailsPref.getStringPref (MainActivity.this, FilterDetailsPref.FILTER_STATUS));
                        params.put (AppConfigTags.FILTER_CITIES, filterDetailsPref.getStringPref (MainActivity.this, FilterDetailsPref.FILTER_CITIES));
                        params.put (AppConfigTags.FILTER_PRICE_MIN, filterDetailsPref.getStringPref (MainActivity.this, FilterDetailsPref.FILTER_PRICE_MIN));
                        params.put (AppConfigTags.FILTER_PRICE_MAX, filterDetailsPref.getStringPref (MainActivity.this, FilterDetailsPref.FILTER_PRICE_MAX));
                        params.put (AppConfigTags.FILTER_LOCATION, filterDetailsPref.getStringPref (MainActivity.this, FilterDetailsPref.FILTER_LOCATION));
                    }
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
        IProfile profile = new IProfile () {
            @Override
            public Object withName (String name) {
                return null;
            }
    
            @Override
            public StringHolder getName () {
                return null;
            }
    
            @Override
            public Object withEmail (String email) {
                return null;
            }
    
            @Override
            public StringHolder getEmail () {
                return null;
            }
    
            @Override
            public Object withIcon (Drawable icon) {
                return null;
            }
    
            @Override
            public Object withIcon (Bitmap bitmap) {
                return null;
            }
    
            @Override
            public Object withIcon (@DrawableRes int iconRes) {
                return null;
            }
    
            @Override
            public Object withIcon (String url) {
                return null;
            }
    
            @Override
            public Object withIcon (Uri uri) {
                return null;
            }
    
            @Override
            public Object withIcon (IIcon icon) {
                return null;
            }
    
            @Override
            public ImageHolder getIcon () {
                return null;
            }
    
            @Override
            public Object withSelectable (boolean selectable) {
                return null;
            }
    
            @Override
            public boolean isSelectable () {
                return false;
            }
    
            @Override
            public Object withIdentifier (long identifier) {
                return null;
            }
    
            @Override
            public long getIdentifier () {
                return 0;
            }
        };
        
        DrawerImageLoader.init (new AbstractDrawerImageLoader () {
            @Override
            public void set (ImageView imageView, Uri uri, Drawable placeholder) {
                if (uri != null) {
                    Glide.with (imageView.getContext ()).load (uri).placeholder (placeholder).into (imageView);
                }
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
                .withCompactStyle (false)
                .withTypeface (SetTypeFace.getTypeface (MainActivity.this))
                .withTypeface (SetTypeFace.getTypeface (this))
                .withPaddingBelowHeader (false)
                .withSelectionListEnabled (false)
                .withSelectionListEnabledForSingleProfile (false)
                .withProfileImagesVisible (true)
                .withOnlyMainProfileImageVisible (true)
                .withDividerBelowHeader (true)
                .withHeaderBackground (R.drawable.drawer_bg)
                .withSavedInstance (savedInstanceState)
                .build ();
    
    
        if (buyerDetailsPref.getStringPref (MainActivity.this, BuyerDetailsPref.BUYER_IMAGE).length () != 0) {
            headerResult.addProfiles (new ProfileDrawerItem ()
                    .withIcon (buyerDetailsPref.getStringPref (MainActivity.this, BuyerDetailsPref.BUYER_IMAGE))
                    .withName (buyerDetailsPref.getStringPref (MainActivity.this, BuyerDetailsPref.BUYER_NAME))
                    .withEmail (buyerDetailsPref.getStringPref (MainActivity.this, BuyerDetailsPref.BUYER_EMAIL)));
        } else {
            headerResult.addProfiles (new ProfileDrawerItem ()
                    .withName (buyerDetailsPref.getStringPref (MainActivity.this, BuyerDetailsPref.BUYER_NAME))
                    .withEmail (buyerDetailsPref.getStringPref (MainActivity.this, BuyerDetailsPref.BUYER_EMAIL)));
        }
    
        result = new DrawerBuilder ()
                .withActivity (this)
                .withAccountHeader (headerResult)
//                .withToolbar (toolbar)
//                .withItemAnimator (new AlphaCrossFadeAnimator ())
                .addDrawerItems (
                        new PrimaryDrawerItem ().withName ("Home").withIcon (FontAwesome.Icon.faw_home).withIdentifier (1).withTypeface (SetTypeFace.getTypeface (MainActivity.this)),
                        new PrimaryDrawerItem ().withName ("My Favourites").withIcon (FontAwesome.Icon.faw_heart).withIdentifier (2).withSelectable (false).withTypeface (SetTypeFace.getTypeface (MainActivity.this)),
                        new PrimaryDrawerItem ().withName ("How It Works").withIcon (FontAwesome.Icon.faw_info).withIdentifier (3).withSelectable (false).withTypeface (SetTypeFace.getTypeface (MainActivity.this)),
                        new PrimaryDrawerItem ().withName ("About Us").withIcon (FontAwesome.Icon.faw_info).withIdentifier (4).withSelectable (false).withTypeface (SetTypeFace.getTypeface (MainActivity.this)),
                        new PrimaryDrawerItem ().withName ("Testimonials").withIcon (FontAwesome.Icon.faw_comments).withIdentifier (5).withSelectable (false).withTypeface (SetTypeFace.getTypeface (MainActivity.this)),
                        new PrimaryDrawerItem ().withName ("Contact Us").withIcon (FontAwesome.Icon.faw_phone).withIdentifier (6).withSelectable (false).withTypeface (SetTypeFace.getTypeface (MainActivity.this)),
                        new PrimaryDrawerItem ().withName ("FAQ").withIcon (FontAwesome.Icon.faw_question).withIdentifier (7).withSelectable (false).withTypeface (SetTypeFace.getTypeface (MainActivity.this)),
                        new PrimaryDrawerItem ().withName ("My Profile").withIcon (FontAwesome.Icon.faw_user).withIdentifier (8).withSelectable (false).withTypeface (SetTypeFace.getTypeface (MainActivity.this)),
                        new PrimaryDrawerItem ().withName ("Change Password").withIcon (FontAwesome.Icon.faw_key).withIdentifier (9).withSelectable (false).withTypeface (SetTypeFace.getTypeface (MainActivity.this)),
                        new PrimaryDrawerItem ().withName ("Sign Out").withIcon (FontAwesome.Icon.faw_sign_out).withIdentifier (10).withSelectable (false).withTypeface (SetTypeFace.getTypeface (MainActivity.this))
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
                        LoginManager.getInstance ().logOut ();
                        LISessionManager.getInstance (getApplicationContext ()).clearSession ();
                        
                        buyerDetailsPref.putStringPref (MainActivity.this, BuyerDetailsPref.BUYER_NAME, "");
                        buyerDetailsPref.putStringPref (MainActivity.this, BuyerDetailsPref.BUYER_EMAIL, "");
                        buyerDetailsPref.putStringPref (MainActivity.this, BuyerDetailsPref.BUYER_MOBILE, "");
                        buyerDetailsPref.putStringPref (MainActivity.this, BuyerDetailsPref.BUYER_LOGIN_KEY, "");
                        buyerDetailsPref.putStringPref (MainActivity.this, BuyerDetailsPref.BUYER_IMAGE, "");
                        buyerDetailsPref.putIntPref (MainActivity.this, BuyerDetailsPref.BUYER_ID, 0);
                        buyerDetailsPref.putStringPref (MainActivity.this, BuyerDetailsPref.BUYER_FACEBOOK_ID, "");
                        buyerDetailsPref.putStringPref (MainActivity.this, BuyerDetailsPref.PROFILE_HOME_TYPE, "");
                        buyerDetailsPref.putStringPref (MainActivity.this, BuyerDetailsPref.PROFILE_STATE, "");
                        buyerDetailsPref.putStringPref (MainActivity.this, BuyerDetailsPref.PROFILE_PRICE_RANGE, "");
                        
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
    
    @Override
    public void onDestroy () {
        super.onDestroy ();
//        filterDetailsPref.putBooleanPref (MainActivity.this, FilterDetailsPref.FILTER_APPLIED, false);
//        filterDetailsPref.putStringPref (MainActivity.this, FilterDetailsPref.FILTER_BATHROOMS, "");
//        filterDetailsPref.putStringPref (MainActivity.this, FilterDetailsPref.FILTER_BEDROOMS, "");
//        filterDetailsPref.putStringPref (MainActivity.this, FilterDetailsPref.FILTER_CITIES, "");
//        filterDetailsPref.putStringPref (MainActivity.this, FilterDetailsPref.FILTER_LOCATION, "");
//        filterDetailsPref.putStringPref (MainActivity.this, FilterDetailsPref.FILTER_PRICE_MIN, "");
//        filterDetailsPref.putStringPref (MainActivity.this, FilterDetailsPref.FILTER_PRICE_MAX, "");
//        filterDetailsPref.putStringPref (MainActivity.this, FilterDetailsPref.FILTER_STATUS, "");
    }
}