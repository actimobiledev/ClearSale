package com.actiknow.clearsale.activity;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.actiknow.clearsale.R;
import com.actiknow.clearsale.utils.AppConfigTags;
import com.actiknow.clearsale.utils.PropertyDetailsPref;
import com.actiknow.clearsale.utils.Utils;
import com.mzelzoghbi.zgallery.Constants;
import com.mzelzoghbi.zgallery.CustomViewPager;
import com.mzelzoghbi.zgallery.OnImgClick;
import com.mzelzoghbi.zgallery.adapters.GridImagesAdapter;
import com.mzelzoghbi.zgallery.adapters.HorizontalListAdapters;
import com.mzelzoghbi.zgallery.adapters.ViewPagerAdapter;
import com.mzelzoghbi.zgallery.adapters.listeners.GridClickListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by l on 31/05/2017.
 */

public class PropertyImageActivity extends AppCompatActivity implements GridClickListener {
    boolean grid_view = false;
    RelativeLayout mainLayout;
    CustomViewPager mViewPager;
    ViewPagerAdapter adapter;
    GridImagesAdapter gridImagesAdapter;
    
    RecyclerView rvHorizontalImages;
    LinearLayoutManager mLayoutManager;
    HorizontalListAdapters hAdapter;
    RelativeLayout rlBack;
    ArrayList<String> propertyImageList = new ArrayList<> ();
    int size;
    Toolbar mToolbar;
    
    PropertyDetailsPref propertyDetailsPref;
    ImageView ivGridView;
    RecyclerView rvGridImages;
    private int currentPos;
    
    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_property_image);
        initView ();
        initData ();
        initListener ();
    }
    
    private void initView () {
        rvGridImages = (RecyclerView) findViewById (R.id.rvGridImages);
        ivGridView = (ImageView) findViewById (R.id.ivGridView);
        mainLayout = (RelativeLayout) findViewById (R.id.mainLayout);
        mViewPager = (CustomViewPager) findViewById (R.id.pager);
        rvHorizontalImages = (RecyclerView) findViewById (R.id.imagesHorizontalList);
        rlBack = (RelativeLayout) findViewById (R.id.rlBack);
        mToolbar = (Toolbar) findViewById (R.id.toolbar);
    }
    
    private void initData () {
        Utils.setTypefaceToAllViews (this, rlBack);
        propertyDetailsPref = PropertyDetailsPref.getInstance ();
        try {
            JSONArray jsonArray = new JSONArray (propertyDetailsPref.getStringPref (PropertyImageActivity.this, PropertyDetailsPref.PROPERTY_IMAGES));
        
            for (int j = 0; j < jsonArray.length (); j++) {
                JSONObject jsonObjectImages = jsonArray.getJSONObject (j);
                propertyImageList.add (jsonObjectImages.getString (AppConfigTags.PROPERTY_IMAGE));
            }
        } catch (JSONException e) {
            e.printStackTrace ();
        }
        
        currentPos = getIntent ().getIntExtra (Constants.IntentPassingParams.SELECTED_IMG_POS, 0);
        //  bgColor = (ZColor) getIntent().getSerializableExtra(Constants.IntentPassingParams.BG_COLOR);
        
        
        mLayoutManager = new LinearLayoutManager (this, LinearLayoutManager.HORIZONTAL, false);
        // pager adapter
        adapter = new ViewPagerAdapter (this, propertyImageList, mToolbar, rvHorizontalImages);
        mViewPager.setAdapter (adapter);
        // horizontal list adaapter
        hAdapter = new HorizontalListAdapters (this, propertyImageList, new OnImgClick () {
            @Override
            public void onClick (int pos) {
                mViewPager.setCurrentItem (pos, true);
            }
        });
        rvHorizontalImages.setLayoutManager (mLayoutManager);
        rvHorizontalImages.setAdapter (hAdapter);
        hAdapter.notifyDataSetChanged ();
        
        mViewPager.addOnPageChangeListener (new ViewPager.OnPageChangeListener () {
            @Override
            public void onPageScrolled (int position, float positionOffset, int positionOffsetPixels) {
            }
    
            @Override
            public void onPageSelected (int position) {
                rvHorizontalImages.smoothScrollToPosition (position);
                hAdapter.setSelectedItem (position);
            }
    
            @Override
            public void onPageScrollStateChanged (int state) {
            }
        });
        hAdapter.setSelectedItem (currentPos);
        mViewPager.setCurrentItem (currentPos);
    
    
        gridImagesAdapter = new GridImagesAdapter (this, propertyImageList, getIntent ().getIntExtra (Constants.IntentPassingParams.IMG_PLACEHOLDER, - 1));
        rvGridImages.setLayoutManager (new GridLayoutManager (this, 3));
        rvGridImages.setAdapter (gridImagesAdapter);
    
    }
    
    private void initListener () {
        ivGridView.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View v) {
                if (grid_view) {
                    grid_view = false;
                    rvGridImages.setVisibility (View.GONE);
                    rvHorizontalImages.setVisibility (View.VISIBLE);
                    mViewPager.setVisibility (View.VISIBLE);
                } else {
                    grid_view = true;
                    mViewPager.setVisibility (View.GONE);
                    rvHorizontalImages.setVisibility (View.GONE);
                    rvGridImages.setVisibility (View.VISIBLE);
                }
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
    
    @Override
    public void onBackPressed () {
        finish ();
        overridePendingTransition (R.anim.slide_in_left, R.anim.slide_out_right);
    }
    
    @Override
    public void onClick (int pos) {
        currentPos = pos;
        hAdapter.setSelectedItem (pos);
        mViewPager.setCurrentItem (pos);
        grid_view = false;
        rvGridImages.setVisibility (View.GONE);
        rvHorizontalImages.setVisibility (View.VISIBLE);
        mViewPager.setVisibility (View.VISIBLE);
    }
}
