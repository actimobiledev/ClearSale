package com.actiknow.clearsale.activity;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.RelativeLayout;

import com.actiknow.clearsale.R;
import com.actiknow.clearsale.utils.PropertyDetailsPref;
import com.actiknow.clearsale.utils.Utils;
import com.mzelzoghbi.zgallery.Constants;
import com.mzelzoghbi.zgallery.CustomViewPager;
import com.mzelzoghbi.zgallery.OnImgClick;
import com.mzelzoghbi.zgallery.adapters.HorizontalListAdapters;
import com.mzelzoghbi.zgallery.adapters.ViewPagerAdapter;

import java.util.ArrayList;

/**
 * Created by l on 31/05/2017.
 */

public class PropertyImageActivity extends AppCompatActivity {
    
    RelativeLayout mainLayout;
    CustomViewPager mViewPager;
    ViewPagerAdapter adapter;
    RecyclerView imagesHorizontalList;
    LinearLayoutManager mLayoutManager;
    HorizontalListAdapters hAdapter;
    RelativeLayout rlBack;
    ArrayList<String> propertyImageList = new ArrayList<> ();
    int size;
    Toolbar mToolbar;
    
    PropertyDetailsPref propertyDetailsPref;
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
        mainLayout = (RelativeLayout) findViewById (R.id.mainLayout);
        mViewPager = (CustomViewPager) findViewById (R.id.pager);
        imagesHorizontalList = (RecyclerView) findViewById (R.id.imagesHorizontalList);
        rlBack = (RelativeLayout) findViewById (R.id.rlBack);
        mToolbar = (Toolbar) findViewById (R.id.toolbar);
    }
    
    private void initData () {
        Utils.setTypefaceToAllViews (this, rlBack);
        propertyDetailsPref = PropertyDetailsPref.getInstance ();
        int size = propertyDetailsPref.getIntPref (PropertyImageActivity.this, "size");
    
        for (int j = 0; j < size; j++) {
            propertyImageList.add (propertyDetailsPref.getStringPref (PropertyImageActivity.this, propertyDetailsPref.PROPERTY_IMAGES + j));
        }
    
    
        //   propertyImageList.add("http://clearsale.com/theme/theme1/seller_files/exterior/property_327/dcca01ddf8b02fb5d2fe89d3c55eb5dcExterior%20pic.png");
        //  propertyImageList.add("http://clearsale.com/theme/theme1/seller_files/interior/property_327/d755cd303ff826ce587fb0e55e6bc1c6IMG_5034.jpg");
        // propertyImageList.add("http://clearsale.com/theme/theme1/seller_files/interior/property_327/63502fc15c1d04e5ad9f095d8bd03b2aIMG_5035.jpg");
        // propertyImageList.add("http://clearsale.com/theme/theme1/seller_files/interior/property_327/1a6c485de807e99fbb7b11a8f06e354fIMG_5036.jpg");
        //  propertyImageList.add("http://clearsale.com/theme/theme1/seller_files/interior/property_327/d913733c109e622efea54bf04c778d1bIMG_5038.jpg");
        //  propertyImageList.add("http://clearsale.com/theme/theme1/seller_files/interior/property_327/1d525c19d5c659f9d39a1abb941baf75IMG_5039.jpg");
        //  propertyImageList.add("http://clearsale.com/theme/theme1/seller_files/interior/property_327/8bafdd842b1558edd07efd4b58b69ecfIMG_5040.jpg");
        //  propertyImageList.add("http://clearsale.com/theme/theme1/seller_files/interior/property_327/6465f55d083394753d7656fbea7b5df6IMG_5041.jpg");
        //  propertyImageList.add("http://clearsale.com/theme/theme1/seller_files/interior/property_327/23a5f19eee30dfcc0f7ddd7b4116986cIMG_5042.jpg");
        //  propertyImageList.add("http://clearsale.com/theme/theme1/seller_files/interior/property_327/b41719c7631a3d7bc0c2d246cd2fb5daIMG_5043.jpg");
        // propertyImageList.add("http://clearsale.com/theme/theme1/seller_files/interior/property_327/1ee4363f712ced227c0b154029e5cfb8IMG_5048.jpg");
        // propertyImageList.add("http://clearsale.com/theme/theme1/seller_files/interior/property_327/3fd6c0348b2f1443715d0ee4139632c8IMG_5053.jpg");
        
        
        currentPos = getIntent ().getIntExtra (Constants.IntentPassingParams.SELECTED_IMG_POS, 0);
        //  bgColor = (ZColor) getIntent().getSerializableExtra(Constants.IntentPassingParams.BG_COLOR);
        
        
        mLayoutManager = new LinearLayoutManager (this, LinearLayoutManager.HORIZONTAL, false);
        // pager adapter
        adapter = new ViewPagerAdapter (this, propertyImageList, mToolbar, imagesHorizontalList);
        mViewPager.setAdapter (adapter);
        // horizontal list adaapter
        hAdapter = new HorizontalListAdapters (this, propertyImageList, new OnImgClick () {
            @Override
            public void onClick (int pos) {
                mViewPager.setCurrentItem (pos, true);
            }
        });
        imagesHorizontalList.setLayoutManager (mLayoutManager);
        imagesHorizontalList.setAdapter (hAdapter);
        hAdapter.notifyDataSetChanged ();
        
        mViewPager.addOnPageChangeListener (new ViewPager.OnPageChangeListener () {
            @Override
            public void onPageScrolled (int position, float positionOffset, int positionOffsetPixels) {
            }
    
            @Override
            public void onPageSelected (int position) {
                imagesHorizontalList.smoothScrollToPosition (position);
                hAdapter.setSelectedItem (position);
            }
    
            @Override
            public void onPageScrollStateChanged (int state) {
            }
        });
    
        hAdapter.setSelectedItem (currentPos);
        mViewPager.setCurrentItem (currentPos);
    }
    
    private void initListener () {
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
}
