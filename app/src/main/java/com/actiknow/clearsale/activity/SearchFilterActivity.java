package com.actiknow.clearsale.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.actiknow.clearsale.R;
import com.actiknow.clearsale.utils.AppConfigTags;
import com.actiknow.clearsale.utils.AppConfigURL;
import com.actiknow.clearsale.utils.Constants;
import com.actiknow.clearsale.utils.NetworkConnection;
import com.actiknow.clearsale.utils.UserDetailsPref;
import com.actiknow.clearsale.utils.Utils;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.jaredrummler.materialspinner.MaterialSpinner;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Hashtable;
import java.util.Map;

import me.bendik.simplerangeview.SimpleRangeView;


/**
 * Created by l on 24/02/2017.
 */

public class SearchFilterActivity extends AppCompatActivity {


    ProgressDialog progressDialog;
    CoordinatorLayout coordinatorLayout;
    LinearLayout rangeViewsContainer;
    SimpleRangeView rangeView;
    SimpleRangeView rangeViewLocation;
    private String[] Price = new String[]{"0", "$100K", "$150K", "$200k", "$300K", "$300+K"};
    private String[] BedRoom = new String[]{"Any","2-3","3-4","4+"};
    private String[] Bath = new String[]{"Any","2-3","3-4","4+"};
    private String[] Location = new String[]{"0","2","4","6","8+"};
    MaterialSpinner spinner;
    MaterialSpinner spinnerBed;
    MaterialSpinner spinnerBath;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_search_filter);
        initView();
        initData();
        initListener();

    }

    private void initView() {
        rangeViewsContainer = (LinearLayout) findViewById(R.id.range_views_container);
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.clMain);
        rangeView = (SimpleRangeView) findViewById(R.id.fixed_rangeview);
        rangeViewLocation = (SimpleRangeView) findViewById(R.id.rangeview_location);

        spinner=(MaterialSpinner)findViewById(R.id.spinner);
        spinnerBed=(MaterialSpinner)findViewById(R.id.spinnerBed);
        spinnerBath=(MaterialSpinner)findViewById(R.id.spinnerBath);

    }

    private void initData() {




        rangeView.setActiveLineColor(getResources().getColor(R.color.colorPrimary));
        rangeView.setActiveThumbColor(getResources().getColor(R.color.colorPrimary));
        rangeView.setActiveLabelColor(getResources().getColor(R.color.colorPrimary));
        rangeView.setActiveThumbLabelColor(getResources().getColor(R.color.colorPrimary));
        rangeView.setActiveFocusThumbColor(getResources().getColor(R.color.colorPrimary));
        rangeView.setActiveFocusThumbAlpha(0.26f);




        rangeViewLocation.setActiveLineColor(getResources().getColor(R.color.colorPrimary));
        rangeViewLocation.setActiveThumbColor(getResources().getColor(R.color.colorPrimary));
        rangeViewLocation.setActiveLabelColor(getResources().getColor(R.color.colorPrimary));
        rangeViewLocation.setActiveThumbLabelColor(getResources().getColor(R.color.colorPrimary));
        rangeViewLocation.setActiveFocusThumbColor(getResources().getColor(R.color.colorPrimary));
        rangeViewLocation.setActiveFocusThumbAlpha(0.26f);





    }

    private void initListener() {
        spinner.setItems("All", "Available", "First Look", "Pending","Sold","Offer Window Closing");
        spinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {

            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, String item) {
                Snackbar.make(view, "Clicked " + item, Snackbar.LENGTH_LONG).show();
            }
        });


        spinnerBed.setItems(BedRoom);
        spinnerBed.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {

            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, String item) {
                Snackbar.make(view, "Clicked " + item, Snackbar.LENGTH_LONG).show();
            }
        });


        spinnerBath.setItems(Bath);
        spinnerBath.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {

            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, String item) {
                Snackbar.make(view, "Clicked " + item, Snackbar.LENGTH_LONG).show();
            }
        });





    rangeView.setOnRangeLabelsListener(new SimpleRangeView.OnRangeLabelsListener() {
        @Nullable
        @Override
        public String getLabelTextForPosition(@NotNull SimpleRangeView simpleRangeView, int i, @NotNull SimpleRangeView.State state) {
            return String.valueOf(Price[i]);
        }
    });
    rangeView.setOnTrackRangeListener(new SimpleRangeView.OnTrackRangeListener() {
        @Override
        public void onStartRangeChanged(@NotNull SimpleRangeView simpleRangeView, int i) {
        }
        @Override
        public void onEndRangeChanged(@NotNull SimpleRangeView simpleRangeView, int i) {

        }
    });


        rangeViewLocation.setOnRangeLabelsListener(new SimpleRangeView.OnRangeLabelsListener() {
            @Nullable
            @Override
            public String getLabelTextForPosition(@NotNull SimpleRangeView simpleRangeView, int i, @NotNull SimpleRangeView.State state) {
                return String.valueOf(Location[i]);
            }
        });
        rangeViewLocation.setOnTrackRangeListener(new SimpleRangeView.OnTrackRangeListener() {
            @Override
            public void onStartRangeChanged(@NotNull SimpleRangeView simpleRangeView, int i) {
                simpleRangeView.setStart(0);
            }
            @Override
            public void onEndRangeChanged(@NotNull SimpleRangeView simpleRangeView, int i) {

            }
        });





    }


    private void setPreferences() {
        UserDetailsPref loginDetailsPref = UserDetailsPref.getInstance();
        loginDetailsPref.putStringPref(SearchFilterActivity.this, loginDetailsPref.USER_EMAIL, Constants.user_mail);
        loginDetailsPref.putIntPref(SearchFilterActivity.this, loginDetailsPref.USER_ID, Constants.user_id);

    }







}
