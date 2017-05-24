package com.actiknow.clearsale.activity;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;

import com.actiknow.clearsale.R;
import com.actiknow.clearsale.adapter.TestimonialAdapter;
import com.actiknow.clearsale.model.Testimonial;
import com.actiknow.clearsale.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class TestimonialActivity extends AppCompatActivity {
    RecyclerView rvTestimonials;
    TestimonialAdapter testimonialAdapter;
    List<Testimonial> testimonialList = new ArrayList<>();
    SwipeRefreshLayout swipeRefreshLayout;
    RelativeLayout rlBack;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_testimonials);
        initView();
        initData();
        initListener();
        getAllTestimonial();
    }

    private void initView() {
        rvTestimonials = (RecyclerView) findViewById (R.id.rvTestimonials);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        rlBack = (RelativeLayout) findViewById (R.id.rlBack);
    }

    private void initData() {
        swipeRefreshLayout.setRefreshing(true);
        testimonialList.clear();
        testimonialAdapter = new TestimonialAdapter(this, testimonialList);
        rvTestimonials.setAdapter (testimonialAdapter);
        rvTestimonials.setHasFixedSize (true);
        rvTestimonials.setLayoutManager (new LinearLayoutManager (this, LinearLayoutManager.VERTICAL, false));
        rvTestimonials.setItemAnimator (new DefaultItemAnimator ());
        Utils.setTypefaceToAllViews (this, rlBack);
    }

    private void initListener() {
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getAllTestimonial();
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
    
    
    private void getAllTestimonial() {
        testimonialList.clear ();
        testimonialList.add (new Testimonial (1, "Everything you guys promise is always as such! Make money all the time", "CHAD", ""));
        testimonialList.add (new Testimonial (2, "Easy to work with. Everything that you pointed out was correct, actually was a little better than what you said. ", "KALEB", ""));
        testimonialList.add (new Testimonial (3, "Very professional. Went very well, very smooth, very easy.", "JOSH", ""));
        swipeRefreshLayout.setRefreshing(false);
        testimonialAdapter.notifyDataSetChanged ();
    }
}






