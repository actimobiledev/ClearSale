package com.actiknow.clearsale.activity;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.SpannableStringBuilder;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.actiknow.clearsale.R;
import com.actiknow.clearsale.fragment.CompsFragment;
import com.actiknow.clearsale.fragment.OverviewFragment;
import com.actiknow.clearsale.utils.AppConfigTags;
import com.actiknow.clearsale.utils.AppConfigURL;
import com.actiknow.clearsale.utils.BuyerDetailsPref;
import com.actiknow.clearsale.utils.Constants;
import com.actiknow.clearsale.utils.CustomImageSlider;
import com.actiknow.clearsale.utils.NetworkConnection;
import com.actiknow.clearsale.utils.PropertyDetailsPref;
import com.actiknow.clearsale.utils.SetTypeFace;
import com.actiknow.clearsale.utils.Utils;
import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.Indicators.PagerIndicator;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

/**
 * Created by l on 22/03/2017.
 */

public class PropertyDetailActivity extends AppCompatActivity {
    List<String> bannerList = new ArrayList<> ();
    Toolbar toolbar;
    CoordinatorLayout clMain;
    TextView tv4;
    int property_id;
    ProgressDialog progressDialog;
    RelativeLayout rlSliderIndicator;
    TextView tvSliderPosition;
    PropertyDetailsPref propertyDetailsPref;
    BuyerDetailsPref buyerDetailsPref;
    RelativeLayout rlBack;
    FloatingActionButton fabMaps;
    RelativeLayout rlMain;
    private SliderLayout slider;
    private CollapsingToolbarLayout collapsingToolbarLayout = null;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    
    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_property_detail);
        initView ();
        initData ();
        initListener ();
        getExtras ();
        setUpNavigationDrawer ();
        getPropertyDetails ();
    }
    
    private void getExtras () {
        Intent intent = getIntent ();
        property_id = intent.getIntExtra (AppConfigTags.PROPERTY_ID, 0);
    }
    
    private void initView () {
        rlMain = (RelativeLayout) findViewById (R.id.rlMain);
        rlBack = (RelativeLayout) findViewById (R.id.rlBack);
        clMain = (CoordinatorLayout) findViewById (R.id.clMain);
        slider = (SliderLayout) findViewById (R.id.slider);
        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById (R.id.collapsing_toolbar);
        toolbar = (Toolbar) findViewById (R.id.toolbar);
        tabLayout = (TabLayout) findViewById (R.id.tabs);
        viewPager = (ViewPager) findViewById (R.id.viewpager);
        rlSliderIndicator = (RelativeLayout) findViewById (R.id.rlSliderIndicator);
        tvSliderPosition = (TextView) findViewById (R.id.tvSliderPosition);
        fabMaps = (FloatingActionButton) findViewById (R.id.fabMap);
        
    }
    
    private void initData () {
        propertyDetailsPref = PropertyDetailsPref.getInstance ();
        buyerDetailsPref = BuyerDetailsPref.getInstance ();
        progressDialog = new ProgressDialog (this);
        tabLayout.setupWithViewPager (viewPager);
        tabLayout.setTabGravity (TabLayout.GRAVITY_FILL);
        collapsingToolbarLayout.setTitleEnabled (false);
//        appBar.setExpanded(true);
    
        Utils.setTypefaceToAllViews (this, rlBack);
    }
    
    private void initListener () {
        rlBack.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View v) {
                propertyDetailsPref.putIntPref (PropertyDetailActivity.this, PropertyDetailsPref.PROPERTY_ID, 0);
                propertyDetailsPref.putStringPref (PropertyDetailActivity.this, PropertyDetailsPref.PROPERTY_ADDRESS1, "");
                propertyDetailsPref.putStringPref (PropertyDetailActivity.this, PropertyDetailsPref.PROPERTY_ADDRESS2, "");
                propertyDetailsPref.putStringPref (PropertyDetailActivity.this, PropertyDetailsPref.PROPERTY_STATE, "");
                propertyDetailsPref.putStringPref (PropertyDetailActivity.this, PropertyDetailsPref.PROPERTY_LATITUDE, "");
                propertyDetailsPref.putStringPref (PropertyDetailActivity.this, PropertyDetailsPref.PROPERTY_LONGITUDE, "");
                propertyDetailsPref.putStringPref (PropertyDetailActivity.this, PropertyDetailsPref.PROPERTY_PRICE, "");
                propertyDetailsPref.putStringPref (PropertyDetailActivity.this, PropertyDetailsPref.PROPERTY_YEAR_BUILD, "");
                propertyDetailsPref.putStringPref (PropertyDetailActivity.this, PropertyDetailsPref.PROPERTY_BEDROOM, "");
                propertyDetailsPref.putStringPref (PropertyDetailActivity.this, PropertyDetailsPref.PROPERTY_BATHROOM, "");
                propertyDetailsPref.putStringPref (PropertyDetailActivity.this, PropertyDetailsPref.PROPERTY_AREA, "");
                propertyDetailsPref.putStringPref (PropertyDetailActivity.this, PropertyDetailsPref.PROPERTY_OVERVIEW, "");
                propertyDetailsPref.putStringPref (PropertyDetailActivity.this, PropertyDetailsPref.PROPERTY_OFFER, "");
                propertyDetailsPref.putStringPref (PropertyDetailActivity.this, PropertyDetailsPref.PROPERTY_ACCESS, "");
                propertyDetailsPref.putStringPref (PropertyDetailActivity.this, PropertyDetailsPref.PROPERTY_REALTOR, "");
                propertyDetailsPref.putStringPref (PropertyDetailActivity.this, PropertyDetailsPref.PROPERTY_COMPS, "");
                propertyDetailsPref.putIntPref (PropertyDetailActivity.this, PropertyDetailsPref.PROPERTY_AUCTION_ID, 0);
                propertyDetailsPref.putIntPref (PropertyDetailActivity.this, PropertyDetailsPref.PROPERTY_AUCTION_STATUS, 0);
                finish ();
                overridePendingTransition (R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });
        
        fabMaps.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View view) {
                Intent intent = new Intent (PropertyDetailActivity.this, PropertyLocationActivity.class);
                startActivity (intent);
            }
        });
    }
    
    private void initSlider () {
        slider.removeAllSliders ();
        for (int i = 0; i < bannerList.size (); i++) {
            String image = bannerList.get (i);
            CustomImageSlider slider2 = new CustomImageSlider (this);
            slider2
                    .image (image)
                    .setScaleType (BaseSliderView.ScaleType.CenterCrop)
                    .setOnSliderClickListener (new BaseSliderView.OnSliderClickListener () {
                        @Override
                        public void onSliderClick (BaseSliderView slider) {
                            Intent intent = new Intent (PropertyDetailActivity.this, PropertyImageActivity.class);
                            startActivity (intent);
                        }
                    });

//            DefaultSliderView defaultSliderView = new DefaultSliderView (activity);
//            defaultSliderView
//                    .image (image)
//                    .setScaleType (BaseSliderView.ScaleType.Fit)
//                    .setOnSliderClickListener (new BaseSliderView.OnSliderClickListener () {
//                        @Override
//                        public void onSliderClick (BaseSliderView slider) {
//                            Intent intent = new Intent (activity, PropertyDetailActivity.class);
//                            intent.putExtra (AppConfigTags.PROPERTY_ID, property.getId ());
//                            activity.startActivity (intent);
//                            activity.overridePendingTransition (R.anim.slide_in_right, R.anim.slide_out_left);
//                        }
//                    });
//
//            defaultSliderView.bundle (new Bundle ());
            // defaultSliderView.getBundle ().putString ("extra", String.valueOf (s));
//            holder.slider.addSlider (defaultSliderView);
            slider.addSlider (slider2);
        }
        
        slider.setIndicatorVisibility (PagerIndicator.IndicatorVisibility.Invisible);
        slider.getPagerIndicator ().setVisibility (View.INVISIBLE);
        slider.setPresetTransformer (SliderLayout.Transformer.Default);
        slider.setCustomAnimation (new DescriptionAnimation ());
        slider.addOnPageChangeListener (new ViewPagerEx.OnPageChangeListener () {
            @Override
            public void onPageScrolled (int position, float positionOffset, int positionOffsetPixels) {
//                tvSliderPosition.setText (position  + bannerList.size ());
            }
            
            @Override
            public void onPageSelected (int position) {
                tvSliderPosition.setText ((position + 1) + " of " + bannerList.size ());
            }
            
            @Override
            public void onPageScrollStateChanged (int state) {
//                final Handler handler = new Handler ();
//                Runnable finalizer = null;
//                switch (state) {
//                    case 0:
//                        finalizer = new Runnable () {
//                            public void run () {
//                                rlSliderIndicator.setVisibility (View.GONE);
//                            }
//                        };
//                        handler.postDelayed (finalizer, 1500);
//                        break;
//                    case 1:
//                        handler.removeCallbacks (finalizer);
//                        rlSliderIndicator.setVisibility (View.VISIBLE);
//                        break;
//                    case 2:
//                        break;
//                }
            }
        });
    
        tvSliderPosition.setVisibility (View.VISIBLE);
    }
    
    private void getPropertyDetails () {
        if (NetworkConnection.isNetworkAvailable (PropertyDetailActivity.this)) {
            Utils.showProgressDialog (progressDialog, getResources ().getString (R.string.progress_dialog_text_please_wait), true);
            Utils.showLog (Log.INFO, "" + AppConfigTags.URL, AppConfigURL.URL_PROPERTY_DETAIL, true);
            StringRequest strRequest1 = new StringRequest (Request.Method.POST, AppConfigURL.URL_PROPERTY_DETAIL,
                    new com.android.volley.Response.Listener<String> () {
                        @Override
                        public void onResponse (String response) {
                            Utils.showLog (Log.INFO, AppConfigTags.SERVER_RESPONSE, response, true);
                            if (response != null) {
                                try {
                                    JSONObject jsonObj = new JSONObject (response);
                                    boolean error = jsonObj.getBoolean (AppConfigTags.ERROR);
                                    String message = jsonObj.getString (AppConfigTags.MESSAGE);
                                    if (! error) {
    
                                        propertyDetailsPref.putIntPref (PropertyDetailActivity.this, PropertyDetailsPref.PROPERTY_ID, jsonObj.getInt (AppConfigTags.PROPERTY_ID));
                                        propertyDetailsPref.putStringPref (PropertyDetailActivity.this, PropertyDetailsPref.PROPERTY_ADDRESS1, jsonObj.getString (AppConfigTags.PROPERTY_ADDRESS));
                                        propertyDetailsPref.putStringPref (PropertyDetailActivity.this, PropertyDetailsPref.PROPERTY_ADDRESS2, jsonObj.getString (AppConfigTags.PROPERTY_ADDRESS2));
                                        propertyDetailsPref.putStringPref (PropertyDetailActivity.this, PropertyDetailsPref.PROPERTY_STATE, jsonObj.getString (AppConfigTags.PROPERTY_STATE));
                                        propertyDetailsPref.putStringPref (PropertyDetailActivity.this, PropertyDetailsPref.PROPERTY_LATITUDE, jsonObj.getString (AppConfigTags.LATITUDE));
                                        propertyDetailsPref.putStringPref (PropertyDetailActivity.this, PropertyDetailsPref.PROPERTY_LONGITUDE, jsonObj.getString (AppConfigTags.LONGITUDE));
                                        propertyDetailsPref.putStringPref (PropertyDetailActivity.this, PropertyDetailsPref.PROPERTY_PRICE, jsonObj.getString (AppConfigTags.PROPERTY_PRICE));
                                        propertyDetailsPref.putStringPref (PropertyDetailActivity.this, PropertyDetailsPref.PROPERTY_YEAR_BUILD, jsonObj.getString (AppConfigTags.PROPERTY_BUILT_YEAR));
                                        propertyDetailsPref.putStringPref (PropertyDetailActivity.this, PropertyDetailsPref.PROPERTY_BEDROOM, jsonObj.getString (AppConfigTags.PROPERTY_BEDROOMS));
                                        propertyDetailsPref.putStringPref (PropertyDetailActivity.this, PropertyDetailsPref.PROPERTY_BATHROOM, jsonObj.getString (AppConfigTags.PROPERTY_BATHROOMS));
                                        propertyDetailsPref.putStringPref (PropertyDetailActivity.this, PropertyDetailsPref.PROPERTY_AREA, jsonObj.getString (AppConfigTags.PROPERTY_AREA));
                                        propertyDetailsPref.putStringPref (PropertyDetailActivity.this, PropertyDetailsPref.PROPERTY_ARV, jsonObj.getString (AppConfigTags.PROPERTY_ARV));
                                        propertyDetailsPref.putStringPref (PropertyDetailActivity.this, PropertyDetailsPref.PROPERTY_OVERVIEW, jsonObj.getString (AppConfigTags.PROPERTY_OVERVIEW));
                                        propertyDetailsPref.putStringPref (PropertyDetailActivity.this, PropertyDetailsPref.PROPERTY_OFFER, jsonObj.getString (AppConfigTags.PROPERTY_OFFER));
                                        propertyDetailsPref.putStringPref (PropertyDetailActivity.this, PropertyDetailsPref.PROPERTY_ACCESS, jsonObj.getString (AppConfigTags.PROPERTY_ACCESS));
                                        propertyDetailsPref.putStringPref (PropertyDetailActivity.this, PropertyDetailsPref.PROPERTY_REALTOR, jsonObj.getString (AppConfigTags.PROPERTY_REALTOR));
                                        propertyDetailsPref.putStringPref (PropertyDetailActivity.this, PropertyDetailsPref.PROPERTY_COMPS, jsonObj.getString (AppConfigTags.PROPERTY_COMPS));
    
                                        propertyDetailsPref.putStringPref (PropertyDetailActivity.this, PropertyDetailsPref.PROPERTY_COMPS_ADDRESSES, jsonObj.getJSONArray (AppConfigTags.PROPERTY_COMPS_ADDRESSES).toString ());
    
                                        propertyDetailsPref.putIntPref (PropertyDetailActivity.this, PropertyDetailsPref.PROPERTY_AUCTION_ID, jsonObj.getInt (AppConfigTags.PROPERTY_BID_AUCTION_ID));
                                        propertyDetailsPref.putIntPref (PropertyDetailActivity.this, PropertyDetailsPref.PROPERTY_AUCTION_STATUS, jsonObj.getInt (AppConfigTags.PROPERTY_BID_AUCTION_STATUS));
                                        
                                        
                                        JSONArray jsonArrayPropertyImages = jsonObj.getJSONArray (AppConfigTags.PROPERTY_IMAGES);
                                        propertyDetailsPref.putStringPref (PropertyDetailActivity.this, PropertyDetailsPref.PROPERTY_IMAGES, jsonArrayPropertyImages.toString ());
                                        propertyDetailsPref.putIntPref (PropertyDetailActivity.this, PropertyDetailsPref.PROPERTY_IMAGE_COUNT, jsonArrayPropertyImages.length ());
    
                                        for (int j = 0; j < jsonArrayPropertyImages.length (); j++) {
                                            JSONObject jsonObjectImages = jsonArrayPropertyImages.getJSONObject (j);
                                            bannerList.add (jsonObjectImages.getString (AppConfigTags.PROPERTY_IMAGE));
//                                            propertyDetailsPref.putStringPref (PropertyDetailActivity.this, PropertyDetailsPref.PROPERTY_IMAGES + j, jsonObjectImages.getString (AppConfigTags.PROPERTY_IMAGE));
                                        }
                                        initSlider ();
    
                                        //    String address1 = jsonObj.getString (AppConfigTags.PROPERTY_ADDRESS);
                                        //  String city=jsonObj.getString(AppConfigTags.PROPERTY_CITY_NAME);
                                        //  state = jsonObj.getString (AppConfigTags.PROPERTY_STATE);
                                        //  price = jsonObj.getString (AppConfigTags.PROPERTY_PRICE);
                                        //  yearBuild = jsonObj.getString (AppConfigTags.PROPERTY_BUILT_YEAR);
                                        //  bedroom = jsonObj.getString (AppConfigTags.PROPERTY_BEDROOMS);
                                        //  bathroom = jsonObj.getString (AppConfigTags.PROPERTY_BATHROOMS);
                                        //  area = jsonObj.getString (AppConfigTags.PROPERTY_AREA);
                                        //  overview = jsonObj.getString (AppConfigTags.PROPERTY_OVERVIEW);
                                        //  comps = jsonObj.getString (AppConfigTags.PROPERTY_COMPS);
                                        ///  access = jsonObj.getString (AppConfigTags.PROPERTY_ACCESS);
                                        // realtor = jsonObj.getString (AppConfigTags.PROPERTY_REALTOR);
                                        // offer = jsonObj.getString (AppConfigTags.PROPERTY_OFFER);
    
                                        setupViewPager (viewPager);
                                    }
                                    clMain.setVisibility (View.VISIBLE);
                                    progressDialog.dismiss ();
                                } catch (Exception e) {
                                    progressDialog.dismiss ();
                                    clMain.setVisibility (View.VISIBLE);
                                    Utils.showSnackBar (PropertyDetailActivity.this, clMain, getResources ().getString (R.string.snackbar_text_exception_occurred), Snackbar.LENGTH_LONG, getResources ().getString (R.string.snackbar_action_dismiss), null);
                                    e.printStackTrace ();
                                }
                            } else {
                                clMain.setVisibility (View.VISIBLE);
                                Utils.showSnackBar (PropertyDetailActivity.this, clMain, getResources ().getString (R.string.snackbar_text_error_occurred), Snackbar.LENGTH_LONG, getResources ().getString (R.string.snackbar_action_dismiss), null);
                                Utils.showLog (Log.WARN, AppConfigTags.SERVER_RESPONSE, AppConfigTags.DIDNT_RECEIVE_ANY_DATA_FROM_SERVER, true);
                            }
                            clMain.setVisibility (View.VISIBLE);
                            progressDialog.dismiss ();
                        }
                    },
                    new com.android.volley.Response.ErrorListener () {
                        @Override
                        public void onErrorResponse (VolleyError error) {
                            progressDialog.dismiss ();
                            clMain.setVisibility (View.VISIBLE);
                            Utils.showLog (Log.ERROR, AppConfigTags.VOLLEY_ERROR, error.toString (), true);
                            Utils.showSnackBar (PropertyDetailActivity.this, clMain, getResources ().getString (R.string.snackbar_text_error_occurred), Snackbar.LENGTH_LONG, getResources ().getString (R.string.snackbar_action_dismiss), null);
                        }
                    }) {
                @Override
                protected Map<String, String> getParams () throws AuthFailureError {
                    Map<String, String> params = new Hashtable<String, String> ();
                    params.put (AppConfigTags.PROPERTY_ID, String.valueOf (property_id));
                    params.put (AppConfigTags.BUYER_ID, String.valueOf (buyerDetailsPref.getIntPref (PropertyDetailActivity.this, BuyerDetailsPref.BUYER_ID)));
                    params.put (AppConfigTags.TYPE, "property_detail");
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
//            clMain.setVisibility (View.VISIBLE);
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
    
    private void setupViewPager (ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter (getSupportFragmentManager ());
        adapter.addFragment (new OverviewFragment (), "OVERVIEW");
        adapter.addFragment (new CompsFragment (), "COMPS");
        viewPager.setAdapter (adapter);
    }
    
    @Override
    public void onBackPressed () {
        propertyDetailsPref.putIntPref (PropertyDetailActivity.this, PropertyDetailsPref.PROPERTY_ID, 0);
        propertyDetailsPref.putStringPref (PropertyDetailActivity.this, PropertyDetailsPref.PROPERTY_ADDRESS1, "");
        propertyDetailsPref.putStringPref (PropertyDetailActivity.this, PropertyDetailsPref.PROPERTY_ADDRESS2, "");
        propertyDetailsPref.putStringPref (PropertyDetailActivity.this, PropertyDetailsPref.PROPERTY_STATE, "");
        propertyDetailsPref.putStringPref (PropertyDetailActivity.this, PropertyDetailsPref.PROPERTY_LATITUDE, "");
        propertyDetailsPref.putStringPref (PropertyDetailActivity.this, PropertyDetailsPref.PROPERTY_LONGITUDE, "");
        propertyDetailsPref.putStringPref (PropertyDetailActivity.this, PropertyDetailsPref.PROPERTY_PRICE, "");
        propertyDetailsPref.putStringPref (PropertyDetailActivity.this, PropertyDetailsPref.PROPERTY_YEAR_BUILD, "");
        propertyDetailsPref.putStringPref (PropertyDetailActivity.this, PropertyDetailsPref.PROPERTY_BEDROOM, "");
        propertyDetailsPref.putStringPref (PropertyDetailActivity.this, PropertyDetailsPref.PROPERTY_BATHROOM, "");
        propertyDetailsPref.putStringPref (PropertyDetailActivity.this, PropertyDetailsPref.PROPERTY_AREA, "");
        propertyDetailsPref.putStringPref (PropertyDetailActivity.this, PropertyDetailsPref.PROPERTY_OVERVIEW, "");
        propertyDetailsPref.putStringPref (PropertyDetailActivity.this, PropertyDetailsPref.PROPERTY_OFFER, "");
        propertyDetailsPref.putStringPref (PropertyDetailActivity.this, PropertyDetailsPref.PROPERTY_ACCESS, "");
        propertyDetailsPref.putStringPref (PropertyDetailActivity.this, PropertyDetailsPref.PROPERTY_REALTOR, "");
        propertyDetailsPref.putStringPref (PropertyDetailActivity.this, PropertyDetailsPref.PROPERTY_COMPS, "");
        propertyDetailsPref.putIntPref (PropertyDetailActivity.this, PropertyDetailsPref.PROPERTY_AUCTION_ID, 0);
        propertyDetailsPref.putIntPref (PropertyDetailActivity.this, PropertyDetailsPref.PROPERTY_AUCTION_STATUS, 0);
        finish ();
        overridePendingTransition (R.anim.slide_in_left, R.anim.slide_out_right);
    }
    
    public boolean onCreateOptionsMenu (Menu menu) {
        MenuInflater menuInflater = getMenuInflater ();
        menuInflater.inflate (R.menu.property_detail_menu, menu);
        return true;
    }
    
    private void setUpNavigationDrawer () {
        toolbar = (Toolbar) findViewById (R.id.toolbar1);
        setSupportActionBar (toolbar);
        ActionBar actionBar = getSupportActionBar ();
        toolbar.inflateMenu (R.menu.property_detail_menu);
        
        toolbar.setOnMenuItemClickListener (new Toolbar.OnMenuItemClickListener () {
            @Override
            public boolean onMenuItemClick (MenuItem menuItem) {
                MaterialDialog dialog = new MaterialDialog.Builder (PropertyDetailActivity.this)
                        .limitIconToDefaultSize ()
                        .title ("")
                        .onPositive (new MaterialDialog.SingleButtonCallback () {
                            @Override
                            public void onClick (@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                dialog.dismiss ();
                            }
                        })
                        .positiveText ("OK")
                        .positiveColor (getResources ().getColor (R.color.app_text_color_dark))
                        .customView (R.layout.dialog_webview, false)
                        .typeface (SetTypeFace.getTypeface (PropertyDetailActivity.this), SetTypeFace.getTypeface (PropertyDetailActivity.this))
                        .build ();
                final WebView webView = (WebView) dialog.findViewById (R.id.webview);
                
                switch (menuItem.getItemId ()) {
                    case R.id.action_are_you_realtors:
                        dialog.setTitle ("Are you Realtor");
                        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder ("<style>@font-face{font-family: myFont;src: url(file:///android_asset/" + Constants.font_name + ");}</style>" + propertyDetailsPref.getStringPref (PropertyDetailActivity.this, PropertyDetailsPref.PROPERTY_REALTOR));
                        webView.loadDataWithBaseURL ("www.google.com", spannableStringBuilder.toString (), "text/html", "UTF-8", "");
                        dialog.show ();
                        return true;
                    case R.id.action_place_and_offer:
                        dialog.setTitle ("Place an Offer");
                        SpannableStringBuilder spannableStringBuilder2 = new SpannableStringBuilder ("<style>@font-face{font-family: myFont;src: url(file:///android_asset/" + Constants.font_name + ");}</style>" + propertyDetailsPref.getStringPref (PropertyDetailActivity.this, PropertyDetailsPref.PROPERTY_OFFER));
                        webView.loadDataWithBaseURL ("www.google.com", spannableStringBuilder2.toString (), "text/html", "UTF-8", "");
                        dialog.show ();
                        return true;
                    case R.id.action_access_possession:
                        dialog.setTitle ("Access / Possession");
                        SpannableStringBuilder spannableStringBuilder3 = new SpannableStringBuilder ("<style>@font-face{font-family: myFont;src: url(file:///android_asset/" + Constants.font_name + ");}</style>" + propertyDetailsPref.getStringPref (PropertyDetailActivity.this, PropertyDetailsPref.PROPERTY_ACCESS));
                        webView.loadDataWithBaseURL ("www.google.com", spannableStringBuilder3.toString (), "text/html", "UTF-8", "");
                        dialog.show ();
                        return true;
                    
                }
                return false;
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
    
    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<> ();
        private final List<String> mFragmentTitleList = new ArrayList<> ();
        
        public ViewPagerAdapter (FragmentManager manager) {
            super (manager);
        }
        
        @Override
        public Fragment getItem (int position) {
            return mFragmentList.get (position);
        }
        
        @Override
        public int getCount () {
            return mFragmentList.size ();
        }
        
        public void addFragment (Fragment fragment, String title) {
            mFragmentList.add (fragment);
            mFragmentTitleList.add (title);
        }
        
        @Override
        public CharSequence getPageTitle (int position) {
            return mFragmentTitleList.get (position);
        }
    }
    
}




