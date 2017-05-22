package com.actiknow.clearsale.activity;


import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.actiknow.clearsale.R;
import com.actiknow.clearsale.fragment.CompsFragment;
import com.actiknow.clearsale.fragment.OverviewFragment;
import com.actiknow.clearsale.fragment.PlaceAndOfferFragment;
import com.actiknow.clearsale.fragment.PossessionFragment;
import com.actiknow.clearsale.fragment.PropertyLocationFragment;
import com.actiknow.clearsale.fragment.RealtorFragment;
import com.actiknow.clearsale.model.Banner;
import com.actiknow.clearsale.utils.Constants;
import com.actiknow.clearsale.utils.TypefaceSpan;
import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.Indicators.PagerIndicator;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.DefaultSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;

import java.util.ArrayList;
import java.util.List;

import static android.R.attr.bitmap;

import android.support.v7.graphics.Palette;
import android.view.MenuItem;

/**
 * Created by l on 22/03/2017.
 */

public class PropertyDetailActivity extends AppCompatActivity implements BaseSliderView.OnSliderClickListener, ViewPagerEx.OnPageChangeListener {
    private SliderLayout slider;
    List<Banner> bannerList = new ArrayList<>();
    private CollapsingToolbarLayout collapsingToolbarLayout = null;
    AppBarLayout appBar;
    Toolbar toolbar;
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
        slider = (SliderLayout) findViewById(R.id.slider);
        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        appBar = (AppBarLayout) findViewById(R.id.appbar);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        viewPager = (ViewPager) findViewById(R.id.viewpager);

    }

    private void initData() {
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




