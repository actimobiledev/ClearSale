package com.actiknow.clearsale.activity;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.actiknow.clearsale.R;
import com.actiknow.clearsale.fragment.CompsFragment;
import com.actiknow.clearsale.fragment.OverviewFragment;
import com.actiknow.clearsale.fragment.PlaceAndOfferFragment;
import com.actiknow.clearsale.fragment.PossessionFragment;
import com.actiknow.clearsale.fragment.PropertyLocationFragment;
import com.actiknow.clearsale.fragment.RealtorFragment;
import com.actiknow.clearsale.model.Banner;
import com.actiknow.clearsale.utils.AppConfigTags;
import com.actiknow.clearsale.utils.AppConfigURL;
import com.actiknow.clearsale.utils.Constants;
import com.actiknow.clearsale.utils.NetworkConnection;
import com.actiknow.clearsale.utils.Utils;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.Indicators.PagerIndicator;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.DefaultSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

/**
 * Created by l on 22/03/2017.
 */

public class PropertyDetailActivity extends AppCompatActivity implements BaseSliderView.OnSliderClickListener, ViewPagerEx.OnPageChangeListener {
    List<Banner> bannerList = new ArrayList<>();
    AppBarLayout appBar;
    Toolbar toolbar;
    CoordinatorLayout clMain;
    String address1;
    //  String city;
    String state;
    String price;
    String yearBuild;
    String bedroom;
    String bathroom;
    String area;
    String overview;
    String comps;
    String access;
    String realtor;
    String offer;
    ProgressDialog progressDialog;
    private SliderLayout slider;
    private CollapsingToolbarLayout collapsingToolbarLayout = null;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.property_detail_activity);

        initView();
        initData();
        initListener();
        getBannerList();

    }

    private void initView() {
        clMain = (CoordinatorLayout) findViewById (R.id.clMain);
        slider = (SliderLayout) findViewById(R.id.slider);
        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        appBar = (AppBarLayout) findViewById(R.id.appbar);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        viewPager = (ViewPager) findViewById(R.id.viewpager);

    }

    private void initData() {
        progressDialog = new ProgressDialog (this);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        tabLayout.setupWithViewPager(viewPager);
        collapsingToolbarLayout.setTitle("Details");
        collapsingToolbarLayout.setTitleEnabled(true);
        appBar.setExpanded(true);
        setupViewPager(viewPager);
    }

    private void initListener() {
    }

    private void getBannerList() {
        bannerList.add(new Banner(1, "http://clearsale.com/theme/theme1/seller_files/exterior/property_327/dcca01ddf8b02fb5d2fe89d3c55eb5dcExterior%20pic.png"));
        bannerList.add(new Banner(2, "http://clearsale.com/theme/theme1/seller_files/interior/property_327/d755cd303ff826ce587fb0e55e6bc1c6IMG_5034.jpg"));
        bannerList.add(new Banner(3, "http://clearsale.com/theme/theme1/seller_files/interior/property_327/63502fc15c1d04e5ad9f095d8bd03b2aIMG_5035.jpg"));
        bannerList.add(new Banner(4, "http://clearsale.com/theme/theme1/seller_files/interior/property_327/1a6c485de807e99fbb7b11a8f06e354fIMG_5036.jpg"));
        bannerList.add(new Banner(5, "http://clearsale.com/theme/theme1/seller_files/interior/property_327/d913733c109e622efea54bf04c778d1bIMG_5038.jpg"));
        bannerList.add(new Banner(6, "http://clearsale.com/theme/theme1/seller_files/interior/property_327/1d525c19d5c659f9d39a1abb941baf75IMG_5039.jpg"));
        bannerList.add(new Banner(7, "http://clearsale.com/theme/theme1/seller_files/interior/property_327/8bafdd842b1558edd07efd4b58b69ecfIMG_5040.jpg"));
        bannerList.add(new Banner(8, "http://clearsale.com/theme/theme1/seller_files/interior/property_327/6465f55d083394753d7656fbea7b5df6IMG_5041.jpg"));
        bannerList.add(new Banner(9, "http://clearsale.com/theme/theme1/seller_files/interior/property_327/23a5f19eee30dfcc0f7ddd7b4116986cIMG_5042.jpg"));
        bannerList.add(new Banner(10, "http://clearsale.com/theme/theme1/seller_files/interior/property_327/b41719c7631a3d7bc0c2d246cd2fb5daIMG_5043.jpg"));
        bannerList.add(new Banner(11, "http://clearsale.com/theme/theme1/seller_files/interior/property_327/1ee4363f712ced227c0b154029e5cfb8IMG_5048.jpg"));
        bannerList.add(new Banner(12, "http://clearsale.com/theme/theme1/seller_files/interior/property_327/3fd6c0348b2f1443715d0ee4139632c8IMG_5053.jpg"));

        initSlider();
    }

    private void initSlider() {
        for (int i = 0; i < bannerList.size(); i++) {
            Banner banner = bannerList.get(i);
            // SpannableString s = new SpannableString (banner.getTitle ());
            // s.setSpan (new TypefaceSpan(this, Constants.font_name), 0, s.length (), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

            DefaultSliderView defaultSliderView = new DefaultSliderView(this);
            defaultSliderView
                    .image(banner.getImage())
                    .setScaleType(BaseSliderView.ScaleType.Fit)
                    .setOnSliderClickListener(this);

            defaultSliderView.bundle(new Bundle());
            // defaultSliderView.getBundle ().putString ("extra", String.valueOf (s));
            slider.addSlider(defaultSliderView);
        }
        slider.setIndicatorVisibility(PagerIndicator.IndicatorVisibility.Invisible);
        slider.setPresetTransformer(SliderLayout.Transformer.Default);
        slider.setCustomAnimation(new DescriptionAnimation());
        slider.setDuration(6000);
        slider.addOnPageChangeListener(this);
        slider.setCustomIndicator((PagerIndicator) findViewById(R.id.custom_indicator));
        slider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
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
                                        String address1 = jsonObj.getString (AppConfigTags.PROPERTY_ADDRESS);
                                        //  String city=jsonObj.getString(AppConfigTags.PROPERTY_CITY_NAME);
                                        state = jsonObj.getString (AppConfigTags.PROPERTY_STATE);
                                        price = jsonObj.getString (AppConfigTags.PROPERTY_PRICE);
                                        yearBuild = jsonObj.getString (AppConfigTags.PROPERTY_BUILT_YEAR);
                                        bedroom = jsonObj.getString (AppConfigTags.PROPERTY_BEDROOMS);
                                        bathroom = jsonObj.getString (AppConfigTags.PROPERTY_BATHROOMS);
                                        area = jsonObj.getString (AppConfigTags.PROPERTY_AREA);
                                        overview = jsonObj.getString (AppConfigTags.PROPERTY_OVERVIEW);
                                        comps = jsonObj.getString (AppConfigTags.PROPERTY_COMPS);
                                        access = jsonObj.getString (AppConfigTags.PROPERTY_ACCESS);
                                        realtor = jsonObj.getString (AppConfigTags.PROPERTY_REALTOR);
                                        offer = jsonObj.getString (AppConfigTags.PROPERTY_OFFER);
                                        
                                    }
                                    progressDialog.dismiss ();
                                } catch (Exception e) {
                                    progressDialog.dismiss ();
                                    Utils.showSnackBar (PropertyDetailActivity.this, clMain, getResources ().getString (R.string.snackbar_text_exception_occurred), Snackbar.LENGTH_LONG, getResources ().getString (R.string.snackbar_action_dismiss), null);
                                    e.printStackTrace ();
                                }
                            } else {
                                // Utils.showSnackBar(getActivity(), clMain, getResources().getString(R.string.snackbar_text_error_occurred), Snackbar.LENGTH_LONG, getResources().getString(R.string.snackbar_action_dismiss), null);
                                Utils.showLog (Log.WARN, AppConfigTags.SERVER_RESPONSE, AppConfigTags.DIDNT_RECEIVE_ANY_DATA_FROM_SERVER, true);
                            }
                            //  progressDialog.dismiss();
                        }
                    },
                    new com.android.volley.Response.ErrorListener () {
                        @Override
                        public void onErrorResponse (VolleyError error) {
                            //  progressDialog.dismiss();
                            Utils.showLog (Log.ERROR, AppConfigTags.VOLLEY_ERROR, error.toString (), true);
                            // Utils.showSnackBar(getActivity(), clMain, getResources().getString(R.string.snackbar_text_error_occurred), Snackbar.LENGTH_LONG, getResources().getString(R.string.snackbar_action_dismiss), null);
                        }
                    }) {
                @Override
                protected Map<String, String> getParams () throws AuthFailureError {
                    Map<String, String> params = new Hashtable<String, String> ();
                    params.put (AppConfigTags.PROPERTY_ID, String.valueOf (1));
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
    
    

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSliderClick(BaseSliderView slider) {

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }


    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new OverviewFragment(), "OVERVIEW");
        adapter.addFragment(new CompsFragment(), "COMPS");
        adapter.addFragment(new PossessionFragment(), "ACCESS/POSSESSION");
        adapter.addFragment(new RealtorFragment(), "ARE YOU REALTORS");
        adapter.addFragment(new PlaceAndOfferFragment(), "PLACE AND OFFER");
        adapter.addFragment(new PropertyLocationFragment(), "PROPERTY AND DETAIL");
        viewPager.setAdapter(adapter);
    }


    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
}




