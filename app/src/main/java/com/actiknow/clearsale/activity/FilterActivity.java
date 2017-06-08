package com.actiknow.clearsale.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.actiknow.clearsale.R;
import com.actiknow.clearsale.utils.FilterDetailsPref;
import com.actiknow.clearsale.utils.SetTypeFace;
import com.actiknow.clearsale.utils.Utils;
import com.jaredrummler.materialspinner.MaterialSpinner;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import me.bendik.simplerangeview.SimpleRangeView;


/**
 * Created by l on 24/02/2017.
 */

public class FilterActivity extends AppCompatActivity {
    
    
    RelativeLayout rlBack;
    ProgressDialog progressDialog;
    CoordinatorLayout coordinatorLayout;
    LinearLayout rangeViewsContainer;
    SimpleRangeView rangeView;
    SimpleRangeView rangeViewLocation;
    MaterialSpinner spinner;
    TextView tvBack;
    
    TextView tvBedAny;
    TextView tvBed2;
    TextView tvBed3;
    TextView tvBed4;
    
    TextView tvBathAny;
    TextView tvBath2;
    TextView tvBath3;
    TextView tvBath4;
    
    LinearLayout llCities;
    
    
    TextView tvApply;
    List<String> citySelectedList = new ArrayList ();
    String price_min;
    String price_max;
    String location;
    String status;
    
    //    private String cities_json = "[{\"city_name\": \"Aurora\",\"city_id\": \"2\"},{\"city_name\": \"Denver\",\"city_id\": \"1\"},{\"city_name\": \"Commerce City\",\"city_id\": \"53\"}]";
    String bedrooms;
    String bathrooms;
    FilterDetailsPref filterDetailsPref;
    private String[] priceList = new String[] {"0", "100K", "200K", "300k", "400K", "400K+"};
    private String[] bedroomList = new String[] {"ANY", "2-3", "3-4", "4+"};
    private String[] bathroomList = new String[] {"ANY", "2-3", "3-4", "4+"};
    private String[] locationList = new String[] {"0", "2", "4", "6", "8+"};
    private String[] statusList = new String[] {"All", "Available", "Pending", "Sold", "Closed", "Offer Window Closing"};
    
    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_filter);
        initView ();
        initData ();
        initListener ();
        getPreferencesData ();
    }
    
    private void initView () {
        tvApply = (TextView) findViewById (R.id.tvApply);
        rlBack = (RelativeLayout) findViewById (R.id.rlBack);
        tvBack = (TextView) findViewById (R.id.tvBack);
        rangeViewsContainer = (LinearLayout) findViewById (R.id.range_views_container);
        coordinatorLayout = (CoordinatorLayout) findViewById (R.id.clMain);
        rangeView = (SimpleRangeView) findViewById (R.id.fixed_rangeview);
        rangeViewLocation = (SimpleRangeView) findViewById (R.id.rangeview_location);
        
        spinner = (MaterialSpinner) findViewById (R.id.spinner);
    
        llCities = (LinearLayout) findViewById (R.id.llCities);
        
        tvBedAny = (TextView) findViewById (R.id.tvBedAny);
        tvBed2 = (TextView) findViewById (R.id.tvBed2);
        tvBed3 = (TextView) findViewById (R.id.tvBed3);
        tvBed4 = (TextView) findViewById (R.id.tvBed4);
    
        tvBathAny = (TextView) findViewById (R.id.tvBathAny);
        tvBath2 = (TextView) findViewById (R.id.tvBath2);
        tvBath3 = (TextView) findViewById (R.id.tvBath3);
        tvBath4 = (TextView) findViewById (R.id.tvBath4);
    }
    
    private void initData () {
        filterDetailsPref = FilterDetailsPref.getInstance ();
    
        if (filterDetailsPref.getBooleanPref (FilterActivity.this, FilterDetailsPref.FILTER_APPLIED)) {
            tvBack.setText ("Reset");
        } else {
            tvBack.setText ("Cancel");
        }
        tvBedAny.setText (bedroomList[0]);
        tvBed2.setText (bedroomList[1]);
        tvBed3.setText (bedroomList[2]);
        tvBed4.setText (bedroomList[3]);
    
        tvBathAny.setText (bathroomList[0]);
        tvBath2.setText (bathroomList[1]);
        tvBath3.setText (bathroomList[2]);
        tvBath4.setText (bathroomList[3]);
    
        rangeView.setActiveLineColor (getResources ().getColor (R.color.colorPrimary));
        rangeView.setActiveThumbColor (getResources ().getColor (R.color.colorPrimary));
        rangeView.setActiveLabelColor (getResources ().getColor (R.color.colorPrimary));
        rangeView.setActiveThumbLabelColor (getResources ().getColor (R.color.colorPrimary));
        rangeView.setActiveFocusThumbColor (getResources ().getColor (R.color.colorPrimary));
        rangeView.setActiveFocusThumbAlpha (0.26f);
        
        rangeViewLocation.setActiveLineColor (getResources ().getColor (R.color.colorPrimary));
        rangeViewLocation.setActiveThumbColor (getResources ().getColor (R.color.colorPrimary));
        rangeViewLocation.setActiveLabelColor (getResources ().getColor (R.color.colorPrimary));
        rangeViewLocation.setActiveThumbLabelColor (getResources ().getColor (R.color.colorPrimary));
        rangeViewLocation.setActiveFocusThumbColor (getResources ().getColor (R.color.colorPrimary));
        rangeViewLocation.setActiveFocusThumbAlpha (0.26f);
        
        Utils.setTypefaceToAllViews (this, rlBack);
    
        try {
            JSONArray jsonArray = new JSONArray (filterDetailsPref.getStringPref (FilterActivity.this, FilterDetailsPref.FILTER_CITIES_JSON));
            final CheckBox[] cbCities;
            cbCities = new CheckBox[jsonArray.length ()];
        
            for (int i = 0; i < jsonArray.length (); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject (i);
                cbCities[i] = new CheckBox (this);
                cbCities[i].setId (jsonObject.getInt ("city_id"));
                cbCities[i].setText (jsonObject.getString ("city_name"));
                cbCities[i].setBackgroundTintList (getResources ().getColorStateList (R.color.primary));
                cbCities[i].setTypeface (SetTypeFace.getTypeface (FilterActivity.this));
                cbCities[i].setTextColor (getResources ().getColor (R.color.app_text_color_dark));
                llCities.addView (cbCities[i]);
                final int j = i;
            
                cbCities[j].setOnCheckedChangeListener (new CompoundButton.OnCheckedChangeListener () {
                    @Override
                    public void onCheckedChanged (CompoundButton buttonView, boolean isChecked) {
                        if (cbCities[j].isChecked ()) {
                            citySelectedList.add ("" + cbCities[j].getId ());
                        } else {
                            citySelectedList.remove ("" + cbCities[j].getId ());
                        }
                    }
                });
            }
        } catch (JSONException e) {
            e.printStackTrace ();
        }
    }
    
    private void getPreferencesData () {
        if (filterDetailsPref.getBooleanPref (FilterActivity.this, FilterDetailsPref.FILTER_APPLIED)) {
            price_min = filterDetailsPref.getStringPref (FilterActivity.this, FilterDetailsPref.FILTER_PRICE_MIN);
            price_max = filterDetailsPref.getStringPref (FilterActivity.this, FilterDetailsPref.FILTER_PRICE_MAX);
            location = filterDetailsPref.getStringPref (FilterActivity.this, FilterDetailsPref.FILTER_LOCATION);
            status = filterDetailsPref.getStringPref (FilterActivity.this, FilterDetailsPref.FILTER_STATUS);
            
            if (filterDetailsPref.getStringPref (FilterActivity.this, FilterDetailsPref.FILTER_BEDROOMS).equalsIgnoreCase (bedroomList[0])) {
                bedrooms = bedroomList[0];
                tvBedAny.setBackgroundResource (R.drawable.state_button_selected);
                tvBedAny.setTextColor (getResources ().getColor (R.color.text_color_white));
                tvBed2.setBackgroundResource (R.drawable.state_button_unselected);
                tvBed2.setTextColor (getResources ().getColor (R.color.app_text_color_dark));
                tvBed3.setBackgroundResource (R.drawable.state_button_unselected);
                tvBed3.setTextColor (getResources ().getColor (R.color.app_text_color_dark));
                tvBed4.setBackgroundResource (R.drawable.state_button_unselected);
                tvBed4.setTextColor (getResources ().getColor (R.color.app_text_color_dark));
            } else if (filterDetailsPref.getStringPref (FilterActivity.this, FilterDetailsPref.FILTER_BEDROOMS).equalsIgnoreCase (bedroomList[1])) {
                bedrooms = bedroomList[1];
                tvBedAny.setBackgroundResource (R.drawable.state_button_unselected);
                tvBedAny.setTextColor (getResources ().getColor (R.color.app_text_color_dark));
                tvBed2.setBackgroundResource (R.drawable.state_button_selected);
                tvBed2.setTextColor (getResources ().getColor (R.color.text_color_white));
                tvBed3.setBackgroundResource (R.drawable.state_button_unselected);
                tvBed3.setTextColor (getResources ().getColor (R.color.app_text_color_dark));
                tvBed4.setBackgroundResource (R.drawable.state_button_unselected);
                tvBed4.setTextColor (getResources ().getColor (R.color.app_text_color_dark));
            } else if (filterDetailsPref.getStringPref (FilterActivity.this, FilterDetailsPref.FILTER_BEDROOMS).equalsIgnoreCase (bedroomList[2])) {
                bedrooms = bedroomList[2];
                tvBedAny.setBackgroundResource (R.drawable.state_button_unselected);
                tvBedAny.setTextColor (getResources ().getColor (R.color.app_text_color_dark));
                tvBed2.setBackgroundResource (R.drawable.state_button_unselected);
                tvBed2.setTextColor (getResources ().getColor (R.color.app_text_color_dark));
                tvBed3.setBackgroundResource (R.drawable.state_button_selected);
                tvBed3.setTextColor (getResources ().getColor (R.color.text_color_white));
                tvBed4.setBackgroundResource (R.drawable.state_button_unselected);
                tvBed4.setTextColor (getResources ().getColor (R.color.app_text_color_dark));
            } else if (filterDetailsPref.getStringPref (FilterActivity.this, FilterDetailsPref.FILTER_BEDROOMS).equalsIgnoreCase (bedroomList[3])) {
                bedrooms = bedroomList[3];
                tvBedAny.setBackgroundResource (R.drawable.state_button_unselected);
                tvBedAny.setTextColor (getResources ().getColor (R.color.app_text_color_dark));
                tvBed2.setBackgroundResource (R.drawable.state_button_unselected);
                tvBed2.setTextColor (getResources ().getColor (R.color.app_text_color_dark));
                tvBed3.setBackgroundResource (R.drawable.state_button_unselected);
                tvBed3.setTextColor (getResources ().getColor (R.color.app_text_color_dark));
                tvBed4.setBackgroundResource (R.drawable.state_button_selected);
                tvBed4.setTextColor (getResources ().getColor (R.color.text_color_white));
            } else {
                bedrooms = bedroomList[0];
                tvBedAny.setBackgroundResource (R.drawable.state_button_selected);
                tvBedAny.setTextColor (getResources ().getColor (R.color.text_color_white));
                tvBed2.setBackgroundResource (R.drawable.state_button_unselected);
                tvBed2.setTextColor (getResources ().getColor (R.color.app_text_color_dark));
                tvBed3.setBackgroundResource (R.drawable.state_button_unselected);
                tvBed3.setTextColor (getResources ().getColor (R.color.app_text_color_dark));
                tvBed4.setBackgroundResource (R.drawable.state_button_unselected);
                tvBed4.setTextColor (getResources ().getColor (R.color.app_text_color_dark));
            }
            
            if (filterDetailsPref.getStringPref (FilterActivity.this, FilterDetailsPref.FILTER_BATHROOMS).equalsIgnoreCase (bathroomList[0])) {
                bathrooms = bathroomList[0];
                tvBathAny.setBackgroundResource (R.drawable.state_button_selected);
                tvBathAny.setTextColor (getResources ().getColor (R.color.text_color_white));
                tvBath2.setBackgroundResource (R.drawable.state_button_unselected);
                tvBath2.setTextColor (getResources ().getColor (R.color.app_text_color_dark));
                tvBath3.setBackgroundResource (R.drawable.state_button_unselected);
                tvBath3.setTextColor (getResources ().getColor (R.color.app_text_color_dark));
                tvBath4.setBackgroundResource (R.drawable.state_button_unselected);
                tvBath4.setTextColor (getResources ().getColor (R.color.app_text_color_dark));
            } else if (filterDetailsPref.getStringPref (FilterActivity.this, FilterDetailsPref.FILTER_BATHROOMS).equalsIgnoreCase (bathroomList[1])) {
                bathrooms = bathroomList[1];
                tvBathAny.setBackgroundResource (R.drawable.state_button_unselected);
                tvBathAny.setTextColor (getResources ().getColor (R.color.app_text_color_dark));
                tvBath2.setBackgroundResource (R.drawable.state_button_selected);
                tvBath2.setTextColor (getResources ().getColor (R.color.text_color_white));
                tvBath3.setBackgroundResource (R.drawable.state_button_unselected);
                tvBath3.setTextColor (getResources ().getColor (R.color.app_text_color_dark));
                tvBath4.setBackgroundResource (R.drawable.state_button_unselected);
                tvBath4.setTextColor (getResources ().getColor (R.color.app_text_color_dark));
            } else if (filterDetailsPref.getStringPref (FilterActivity.this, FilterDetailsPref.FILTER_BATHROOMS).equalsIgnoreCase (bathroomList[2])) {
                bathrooms = bathroomList[2];
                tvBathAny.setBackgroundResource (R.drawable.state_button_unselected);
                tvBathAny.setTextColor (getResources ().getColor (R.color.app_text_color_dark));
                tvBath2.setBackgroundResource (R.drawable.state_button_unselected);
                tvBath2.setTextColor (getResources ().getColor (R.color.app_text_color_dark));
                tvBath3.setBackgroundResource (R.drawable.state_button_selected);
                tvBath3.setTextColor (getResources ().getColor (R.color.text_color_white));
                tvBath4.setBackgroundResource (R.drawable.state_button_unselected);
                tvBath4.setTextColor (getResources ().getColor (R.color.app_text_color_dark));
            } else if (filterDetailsPref.getStringPref (FilterActivity.this, FilterDetailsPref.FILTER_BATHROOMS).equalsIgnoreCase (bathroomList[3])) {
                bathrooms = bathroomList[3];
                tvBathAny.setBackgroundResource (R.drawable.state_button_unselected);
                tvBathAny.setTextColor (getResources ().getColor (R.color.app_text_color_dark));
                tvBath2.setBackgroundResource (R.drawable.state_button_unselected);
                tvBath2.setTextColor (getResources ().getColor (R.color.app_text_color_dark));
                tvBath3.setBackgroundResource (R.drawable.state_button_unselected);
                tvBath3.setTextColor (getResources ().getColor (R.color.app_text_color_dark));
                tvBath4.setBackgroundResource (R.drawable.state_button_selected);
                tvBath4.setTextColor (getResources ().getColor (R.color.text_color_white));
            } else {
                bathrooms = bathroomList[0];
                tvBathAny.setBackgroundResource (R.drawable.state_button_selected);
                tvBathAny.setTextColor (getResources ().getColor (R.color.text_color_white));
                tvBath2.setBackgroundResource (R.drawable.state_button_unselected);
                tvBath2.setTextColor (getResources ().getColor (R.color.app_text_color_dark));
                tvBath3.setBackgroundResource (R.drawable.state_button_unselected);
                tvBath3.setTextColor (getResources ().getColor (R.color.app_text_color_dark));
                tvBath4.setBackgroundResource (R.drawable.state_button_unselected);
                tvBath4.setTextColor (getResources ().getColor (R.color.app_text_color_dark));
            }
            
            
            switch (filterDetailsPref.getStringPref (FilterActivity.this, FilterDetailsPref.FILTER_STATUS)) {
                case "0":
                    spinner.setSelectedIndex (0);
                    break;
                case "1":
                    spinner.setSelectedIndex (1);
                    break;
                case "2":
                    spinner.setSelectedIndex (2);
                    break;
                case "3":
                    spinner.setSelectedIndex (3);
                    break;
                case "4":
                    spinner.setSelectedIndex (4);
                    break;
                case "9":
                    spinner.setSelectedIndex (5);
                    break;
            }
/*
            if (filterDetailsPref.getStringPref (FilterActivity.this, FilterDetailsPref.FILTER_STATUS).equalsIgnoreCase (statusList[0])) {
                spinner.setSelectedIndex (0);
                status = "0";
            } else if (filterDetailsPref.getStringPref (FilterActivity.this, FilterDetailsPref.FILTER_STATUS).equalsIgnoreCase (statusList[1])) {
                spinner.setSelectedIndex (1);
                status = "1";
            } else if (filterDetailsPref.getStringPref (FilterActivity.this, FilterDetailsPref.FILTER_STATUS).equalsIgnoreCase (statusList[2])) {
                spinner.setSelectedIndex (2);
                status = "2";
            } else if (filterDetailsPref.getStringPref (FilterActivity.this, FilterDetailsPref.FILTER_STATUS).equalsIgnoreCase (statusList[3])) {
                spinner.setSelectedIndex (3);
                status = "3";
            } else if (filterDetailsPref.getStringPref (FilterActivity.this, FilterDetailsPref.FILTER_STATUS).equalsIgnoreCase (statusList[4])) {
                spinner.setSelectedIndex (4);
                status = "4";
            } else if (filterDetailsPref.getStringPref (FilterActivity.this, FilterDetailsPref.FILTER_STATUS).equalsIgnoreCase (statusList[5])) {
                spinner.setSelectedIndex (5);
                status = "9";
            } else {
                spinner.setSelectedIndex (0);
                status = "0";
            }
    */
            if (filterDetailsPref.getStringPref (FilterActivity.this, FilterDetailsPref.FILTER_PRICE_MIN).equalsIgnoreCase (priceList[0])) {
                rangeView.setStart (0);
            } else if (filterDetailsPref.getStringPref (FilterActivity.this, FilterDetailsPref.FILTER_PRICE_MIN).equalsIgnoreCase (priceList[1])) {
                rangeView.setStart (1);
            } else if (filterDetailsPref.getStringPref (FilterActivity.this, FilterDetailsPref.FILTER_PRICE_MIN).equalsIgnoreCase (priceList[2])) {
                rangeView.setStart (2);
            } else if (filterDetailsPref.getStringPref (FilterActivity.this, FilterDetailsPref.FILTER_PRICE_MIN).equalsIgnoreCase (priceList[3])) {
                rangeView.setStart (3);
            } else if (filterDetailsPref.getStringPref (FilterActivity.this, FilterDetailsPref.FILTER_PRICE_MIN).equalsIgnoreCase (priceList[4])) {
                rangeView.setStart (4);
            } else if (filterDetailsPref.getStringPref (FilterActivity.this, FilterDetailsPref.FILTER_PRICE_MIN).equalsIgnoreCase (priceList[5])) {
                rangeView.setStart (5);
            } else {
                rangeView.setStart (0);
            }
            
            if (filterDetailsPref.getStringPref (FilterActivity.this, FilterDetailsPref.FILTER_PRICE_MAX).equalsIgnoreCase (priceList[0])) {
                rangeView.setEnd (1);
            } else if (filterDetailsPref.getStringPref (FilterActivity.this, FilterDetailsPref.FILTER_PRICE_MAX).equalsIgnoreCase (priceList[1])) {
                rangeView.setEnd (2);
            } else if (filterDetailsPref.getStringPref (FilterActivity.this, FilterDetailsPref.FILTER_PRICE_MAX).equalsIgnoreCase (priceList[2])) {
                rangeView.setEnd (3);
            } else if (filterDetailsPref.getStringPref (FilterActivity.this, FilterDetailsPref.FILTER_PRICE_MAX).equalsIgnoreCase (priceList[3])) {
                rangeView.setEnd (4);
            } else if (filterDetailsPref.getStringPref (FilterActivity.this, FilterDetailsPref.FILTER_PRICE_MAX).equalsIgnoreCase (priceList[4])) {
                rangeView.setEnd (5);
            } else if (filterDetailsPref.getStringPref (FilterActivity.this, FilterDetailsPref.FILTER_PRICE_MAX).equalsIgnoreCase (priceList[5])) {
                rangeView.setEnd (6);
            } else {
                rangeView.setEnd (6);
            }
            
            if (filterDetailsPref.getStringPref (FilterActivity.this, FilterDetailsPref.FILTER_LOCATION).equalsIgnoreCase (locationList[0])) {
                rangeViewLocation.setEnd (1);
            } else if (filterDetailsPref.getStringPref (FilterActivity.this, FilterDetailsPref.FILTER_LOCATION).equalsIgnoreCase (locationList[1])) {
                rangeViewLocation.setEnd (2);
            } else if (filterDetailsPref.getStringPref (FilterActivity.this, FilterDetailsPref.FILTER_LOCATION).equalsIgnoreCase (locationList[2])) {
                rangeViewLocation.setEnd (3);
            } else if (filterDetailsPref.getStringPref (FilterActivity.this, FilterDetailsPref.FILTER_LOCATION).equalsIgnoreCase (locationList[3])) {
                rangeViewLocation.setEnd (4);
            } else if (filterDetailsPref.getStringPref (FilterActivity.this, FilterDetailsPref.FILTER_LOCATION).equalsIgnoreCase (locationList[4])) {
                rangeViewLocation.setEnd (5);
            } else {
                rangeViewLocation.setEnd (5);
            }
            
            for (int i = 0; i < llCities.getChildCount (); i++) {
                CheckBox cbCity = (CheckBox) llCities.getChildAt (i);
                String cities[] = filterDetailsPref.getStringPref (FilterActivity.this, FilterDetailsPref.FILTER_CITIES).trim ().split (",");
                for (int j = 0; j < cities.length; j++) {
                    if (cbCity.getId () == Integer.parseInt (cities[j])) {
                        cbCity.setChecked (true);
                    }
                }
            }
            
        } else {
            price_min = "" + priceList[rangeView.getStart ()];
            price_max = "" + priceList[rangeView.getEnd () - 1];
            location = "" + locationList[rangeView.getEnd () - 1];
            bathrooms = bathroomList[0];
            bedrooms = bedroomList[0];
            status = "0";
            
            tvBathAny.setBackgroundResource (R.drawable.state_button_selected);
            tvBathAny.setTextColor (getResources ().getColor (R.color.text_color_white));
            tvBath2.setBackgroundResource (R.drawable.state_button_unselected);
            tvBath2.setTextColor (getResources ().getColor (R.color.app_text_color_dark));
            tvBath3.setBackgroundResource (R.drawable.state_button_unselected);
            tvBath3.setTextColor (getResources ().getColor (R.color.app_text_color_dark));
            tvBath4.setBackgroundResource (R.drawable.state_button_unselected);
            tvBath4.setTextColor (getResources ().getColor (R.color.app_text_color_dark));
            
            tvBedAny.setBackgroundResource (R.drawable.state_button_selected);
            tvBedAny.setTextColor (getResources ().getColor (R.color.text_color_white));
            tvBed2.setBackgroundResource (R.drawable.state_button_unselected);
            tvBed2.setTextColor (getResources ().getColor (R.color.app_text_color_dark));
            tvBed3.setBackgroundResource (R.drawable.state_button_unselected);
            tvBed3.setTextColor (getResources ().getColor (R.color.app_text_color_dark));
            tvBed4.setBackgroundResource (R.drawable.state_button_unselected);
            tvBed4.setTextColor (getResources ().getColor (R.color.app_text_color_dark));
            
            for (int i = 0; i < llCities.getChildCount (); i++) {
                CheckBox cbCity = (CheckBox) llCities.getChildAt (i);
                cbCity.setChecked (true);
            }
        }
    }
    
    private void initListener () {
        spinner.setItems (statusList);
        spinner.setOnItemSelectedListener (new MaterialSpinner.OnItemSelectedListener<String> () {
            @Override
            public void onItemSelected (MaterialSpinner view, int position, long id, String item) {
                if (item.equalsIgnoreCase (statusList[0])) {
                    status = "0";
                } else if (item.equalsIgnoreCase (statusList[1])) {
                    status = "1";
                } else if (item.equalsIgnoreCase (statusList[2])) {
                    status = "2";
                } else if (item.equalsIgnoreCase (statusList[3])) {
                    status = "3";
                } else if (item.equalsIgnoreCase (statusList[4])) {
                    status = "4";
                } else if (item.equalsIgnoreCase (statusList[5])) {
                    status = "9";
                }
            }
        });
        
        rangeView.setOnRangeLabelsListener (new SimpleRangeView.OnRangeLabelsListener () {
            @Nullable
            @Override
            public String getLabelTextForPosition (@NotNull SimpleRangeView simpleRangeView, int i, @NotNull SimpleRangeView.State state) {
                return String.valueOf (priceList[i]);
            }
        });
        rangeView.setOnTrackRangeListener (new SimpleRangeView.OnTrackRangeListener () {
            @Override
            public void onStartRangeChanged (@NotNull SimpleRangeView simpleRangeView, int i) {
                price_min = priceList[i];
            }
    
            @Override
            public void onEndRangeChanged (@NotNull SimpleRangeView simpleRangeView, int i) {
                price_max = priceList[i];
            }
        });
        
        rangeViewLocation.setOnRangeLabelsListener (new SimpleRangeView.OnRangeLabelsListener () {
            @Nullable
            @Override
            public String getLabelTextForPosition (@NotNull SimpleRangeView simpleRangeView, int i, @NotNull SimpleRangeView.State state) {
                return String.valueOf (locationList[i]);
            }
        });
        rangeViewLocation.setOnTrackRangeListener (new SimpleRangeView.OnTrackRangeListener () {
            @Override
            public void onStartRangeChanged (@NotNull SimpleRangeView simpleRangeView, int i) {
                simpleRangeView.setStart (0);
            }
    
            @Override
            public void onEndRangeChanged (@NotNull SimpleRangeView simpleRangeView, int i) {
                location = locationList[i];
            }
        });
        
        rlBack.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View v) {
                if (filterDetailsPref.getBooleanPref (FilterActivity.this, FilterDetailsPref.FILTER_APPLIED)) {
                    filterDetailsPref.putBooleanPref (FilterActivity.this, FilterDetailsPref.FILTER_APPLIED, false);
                    filterDetailsPref.putStringPref (FilterActivity.this, FilterDetailsPref.FILTER_BATHROOMS, "");
                    filterDetailsPref.putStringPref (FilterActivity.this, FilterDetailsPref.FILTER_BEDROOMS, "");
                    filterDetailsPref.putStringPref (FilterActivity.this, FilterDetailsPref.FILTER_CITIES, "");
                    filterDetailsPref.putStringPref (FilterActivity.this, FilterDetailsPref.FILTER_LOCATION, "");
                    filterDetailsPref.putStringPref (FilterActivity.this, FilterDetailsPref.FILTER_PRICE_MIN, "");
                    filterDetailsPref.putStringPref (FilterActivity.this, FilterDetailsPref.FILTER_PRICE_MAX, "");
                    filterDetailsPref.putStringPref (FilterActivity.this, FilterDetailsPref.FILTER_STATUS, "");
        
                    Intent intent = new Intent (FilterActivity.this, MainActivity.class);
                    intent.setFlags (Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity (intent);
                    overridePendingTransition (R.anim.stay, R.anim.slide_out_down);
                } else {
                    finish ();
                    overridePendingTransition (R.anim.stay, R.anim.slide_out_down);
                }
            }
        });
        
        tvApply.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View v) {
                Log.e ("karman", "location : " + location);
                Log.e ("karman", "price min : " + price_min);
                Log.e ("karman", "price max : " + price_max);
                Log.e ("karman", "status : " + status);
                Log.e ("karman", "bedrooms : " + bedrooms);
                Log.e ("karman", "bathrooms : " + bathrooms);
                String cities_csv = "";
                for (int j = 0; j < citySelectedList.size (); j++) {
                    if (j == 0) {
                        cities_csv = citySelectedList.get (j);
                    } else {
                        cities_csv = cities_csv + "," + citySelectedList.get (j);
                    }
                }
                Log.e ("karman", "cities : " + cities_csv);
    
                filterDetailsPref.putBooleanPref (FilterActivity.this, FilterDetailsPref.FILTER_APPLIED, true);
                filterDetailsPref.putStringPref (FilterActivity.this, FilterDetailsPref.FILTER_BATHROOMS, bathrooms);
                filterDetailsPref.putStringPref (FilterActivity.this, FilterDetailsPref.FILTER_BEDROOMS, bedrooms);
                filterDetailsPref.putStringPref (FilterActivity.this, FilterDetailsPref.FILTER_CITIES, cities_csv);
                filterDetailsPref.putStringPref (FilterActivity.this, FilterDetailsPref.FILTER_LOCATION, location);
                filterDetailsPref.putStringPref (FilterActivity.this, FilterDetailsPref.FILTER_PRICE_MIN, price_min);
                filterDetailsPref.putStringPref (FilterActivity.this, FilterDetailsPref.FILTER_PRICE_MAX, price_max);
                filterDetailsPref.putStringPref (FilterActivity.this, FilterDetailsPref.FILTER_STATUS, status);
    
                Intent intent = new Intent (FilterActivity.this, MainActivity.class);
                intent.setFlags (Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity (intent);
                overridePendingTransition (R.anim.stay, R.anim.slide_out_down);
            }
        });
        
        tvBedAny.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View view) {
                bedrooms = bedroomList[0];
                tvBedAny.setBackgroundResource (R.drawable.state_button_selected);
                tvBedAny.setTextColor (getResources ().getColor (R.color.text_color_white));
                tvBed2.setBackgroundResource (R.drawable.state_button_unselected);
                tvBed2.setTextColor (getResources ().getColor (R.color.app_text_color_dark));
                tvBed3.setBackgroundResource (R.drawable.state_button_unselected);
                tvBed3.setTextColor (getResources ().getColor (R.color.app_text_color_dark));
                tvBed4.setBackgroundResource (R.drawable.state_button_unselected);
                tvBed4.setTextColor (getResources ().getColor (R.color.app_text_color_dark));
            }
        });
        
        tvBed2.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View view) {
                bedrooms = bedroomList[1];
                tvBed2.setBackgroundResource (R.drawable.state_button_selected);
                tvBed2.setTextColor (getResources ().getColor (R.color.text_color_white));
                tvBedAny.setBackgroundResource (R.drawable.state_button_unselected);
                tvBedAny.setTextColor (getResources ().getColor (R.color.app_text_color_dark));
                tvBed3.setBackgroundResource (R.drawable.state_button_unselected);
                tvBed3.setTextColor (getResources ().getColor (R.color.app_text_color_dark));
                tvBed4.setBackgroundResource (R.drawable.state_button_unselected);
                tvBed4.setTextColor (getResources ().getColor (R.color.app_text_color_dark));
            }
        });
        
        tvBed3.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View view) {
                bedrooms = bedroomList[2];
                tvBed3.setBackgroundResource (R.drawable.state_button_selected);
                tvBed3.setTextColor (getResources ().getColor (R.color.text_color_white));
                tvBedAny.setBackgroundResource (R.drawable.state_button_unselected);
                tvBedAny.setTextColor (getResources ().getColor (R.color.app_text_color_dark));
                tvBed2.setBackgroundResource (R.drawable.state_button_unselected);
                tvBed2.setTextColor (getResources ().getColor (R.color.app_text_color_dark));
                tvBed4.setBackgroundResource (R.drawable.state_button_unselected);
                tvBed4.setTextColor (getResources ().getColor (R.color.app_text_color_dark));
            }
        });
        
        tvBed4.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View view) {
                bedrooms = bedroomList[3];
                tvBed4.setBackgroundResource (R.drawable.state_button_selected);
                tvBed4.setTextColor (getResources ().getColor (R.color.text_color_white));
                tvBedAny.setBackgroundResource (R.drawable.state_button_unselected);
                tvBedAny.setTextColor (getResources ().getColor (R.color.app_text_color_dark));
                tvBed2.setBackgroundResource (R.drawable.state_button_unselected);
                tvBed2.setTextColor (getResources ().getColor (R.color.app_text_color_dark));
                tvBed3.setBackgroundResource (R.drawable.state_button_unselected);
                tvBed3.setTextColor (getResources ().getColor (R.color.app_text_color_dark));
            }
        });
        
        tvBathAny.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View view) {
                bathrooms = bathroomList[0];
                tvBathAny.setBackgroundResource (R.drawable.state_button_selected);
                tvBathAny.setTextColor (getResources ().getColor (R.color.text_color_white));
                tvBath2.setBackgroundResource (R.drawable.state_button_unselected);
                tvBath2.setTextColor (getResources ().getColor (R.color.app_text_color_dark));
                tvBath3.setBackgroundResource (R.drawable.state_button_unselected);
                tvBath3.setTextColor (getResources ().getColor (R.color.app_text_color_dark));
                tvBath4.setBackgroundResource (R.drawable.state_button_unselected);
                tvBath4.setTextColor (getResources ().getColor (R.color.app_text_color_dark));
            }
        });
        
        tvBath2.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View view) {
                bathrooms = bathroomList[1];
                tvBath2.setBackgroundResource (R.drawable.state_button_selected);
                tvBath2.setTextColor (getResources ().getColor (R.color.text_color_white));
                tvBathAny.setBackgroundResource (R.drawable.state_button_unselected);
                tvBathAny.setTextColor (getResources ().getColor (R.color.app_text_color_dark));
                tvBath3.setBackgroundResource (R.drawable.state_button_unselected);
                tvBath3.setTextColor (getResources ().getColor (R.color.app_text_color_dark));
                tvBath4.setBackgroundResource (R.drawable.state_button_unselected);
                tvBath4.setTextColor (getResources ().getColor (R.color.app_text_color_dark));
            }
        });
        
        tvBath3.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View view) {
                bathrooms = bathroomList[2];
                tvBath3.setBackgroundResource (R.drawable.state_button_selected);
                tvBath3.setTextColor (getResources ().getColor (R.color.text_color_white));
                tvBathAny.setBackgroundResource (R.drawable.state_button_unselected);
                tvBathAny.setTextColor (getResources ().getColor (R.color.app_text_color_dark));
                tvBath2.setBackgroundResource (R.drawable.state_button_unselected);
                tvBath2.setTextColor (getResources ().getColor (R.color.app_text_color_dark));
                tvBath4.setBackgroundResource (R.drawable.state_button_unselected);
                tvBath4.setTextColor (getResources ().getColor (R.color.app_text_color_dark));
            }
        });
        
        tvBath4.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View view) {
                bathrooms = bathroomList[3];
                tvBath4.setBackgroundResource (R.drawable.state_button_selected);
                tvBath4.setTextColor (getResources ().getColor (R.color.text_color_white));
                tvBathAny.setBackgroundResource (R.drawable.state_button_unselected);
                tvBathAny.setTextColor (getResources ().getColor (R.color.app_text_color_dark));
                tvBath2.setBackgroundResource (R.drawable.state_button_unselected);
                tvBath2.setTextColor (getResources ().getColor (R.color.app_text_color_dark));
                tvBath3.setBackgroundResource (R.drawable.state_button_unselected);
                tvBath3.setTextColor (getResources ().getColor (R.color.app_text_color_dark));
            }
        });
    }
    
    @Override
    public void onBackPressed () {
        finish ();
        overridePendingTransition (R.anim.stay, R.anim.slide_out_down);
    }
}
