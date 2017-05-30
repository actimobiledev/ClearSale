package com.actiknow.clearsale.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.actiknow.clearsale.R;
import com.actiknow.clearsale.utils.Utils;
import com.jaredrummler.materialspinner.MaterialSpinner;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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
    MaterialSpinner spinnerBed;
    MaterialSpinner spinnerBath;
    TextView tvApply;
    private String[] Price = new String[] {"0", "$100K", "$150K", "$200k", "$300K", "$300+K"};
    private String[] BedRoom = new String[] {"Any", "2-3", "3-4", "4+"};
    private String[] Bath = new String[] {"Any", "2-3", "3-4", "4+"};
    private String[] Location = new String[] {"0", "2", "4", "6", "8+"};
    
    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        
        setContentView (R.layout.activity_filter);
        initView ();
        initData ();
        initListener ();
        
    }
    
    private void initView () {
        tvApply = (TextView) findViewById (R.id.tvApply);
        rlBack = (RelativeLayout) findViewById (R.id.rlBack);
        rangeViewsContainer = (LinearLayout) findViewById (R.id.range_views_container);
        coordinatorLayout = (CoordinatorLayout) findViewById (R.id.clMain);
        rangeView = (SimpleRangeView) findViewById (R.id.fixed_rangeview);
        rangeViewLocation = (SimpleRangeView) findViewById (R.id.rangeview_location);
        
        spinner = (MaterialSpinner) findViewById (R.id.spinner);
        spinnerBed = (MaterialSpinner) findViewById (R.id.spinnerBed);
        spinnerBath = (MaterialSpinner) findViewById (R.id.spinnerBath);
    }
    
    private void initData () {
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
    }
    
    private void initListener () {
        spinner.setItems ("All", "Available", "First Look", "Pending", "Sold", "Offer Window Closing");
        spinner.setOnItemSelectedListener (new MaterialSpinner.OnItemSelectedListener<String> () {
            
            @Override
            public void onItemSelected (MaterialSpinner view, int position, long id, String item) {
//                Snackbar.make (view, "Clicked " + item, Snackbar.LENGTH_LONG).show ();
            }
        });
        
        spinnerBed.setItems (BedRoom);
        spinnerBed.setOnItemSelectedListener (new MaterialSpinner.OnItemSelectedListener<String> () {
            
            @Override
            public void onItemSelected (MaterialSpinner view, int position, long id, String item) {
//                Snackbar.make (view, "Clicked " + item, Snackbar.LENGTH_LONG).show ();
            }
        });
        
        spinnerBath.setItems (Bath);
        spinnerBath.setOnItemSelectedListener (new MaterialSpinner.OnItemSelectedListener<String> () {
            
            @Override
            public void onItemSelected (MaterialSpinner view, int position, long id, String item) {
//                Snackbar.make (view, "Clicked " + item, Snackbar.LENGTH_LONG).show ();
            }
        });
        
        
        rangeView.setOnRangeLabelsListener (new SimpleRangeView.OnRangeLabelsListener () {
            @Nullable
            @Override
            public String getLabelTextForPosition (@NotNull SimpleRangeView simpleRangeView, int i, @NotNull SimpleRangeView.State state) {
                return String.valueOf (Price[i]);
            }
        });
        rangeView.setOnTrackRangeListener (new SimpleRangeView.OnTrackRangeListener () {
            @Override
            public void onStartRangeChanged (@NotNull SimpleRangeView simpleRangeView, int i) {
            }
            
            @Override
            public void onEndRangeChanged (@NotNull SimpleRangeView simpleRangeView, int i) {
                
            }
        });
        
        
        rangeViewLocation.setOnRangeLabelsListener (new SimpleRangeView.OnRangeLabelsListener () {
            @Nullable
            @Override
            public String getLabelTextForPosition (@NotNull SimpleRangeView simpleRangeView, int i, @NotNull SimpleRangeView.State state) {
                return String.valueOf (Location[i]);
            }
        });
        rangeViewLocation.setOnTrackRangeListener (new SimpleRangeView.OnTrackRangeListener () {
            @Override
            public void onStartRangeChanged (@NotNull SimpleRangeView simpleRangeView, int i) {
                simpleRangeView.setStart (0);
            }
            
            @Override
            public void onEndRangeChanged (@NotNull SimpleRangeView simpleRangeView, int i) {
                
            }
        });
        
        
        rlBack.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View v) {
                finish ();
                overridePendingTransition (R.anim.stay, R.anim.slide_out_down);
            }
        });
        tvApply.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View v) {
                finish ();
                overridePendingTransition (R.anim.stay, R.anim.slide_out_down);
            }
        });
    }
    
    @Override
    public void onBackPressed () {
        finish ();
        overridePendingTransition (R.anim.stay, R.anim.slide_out_down);
    }
}
