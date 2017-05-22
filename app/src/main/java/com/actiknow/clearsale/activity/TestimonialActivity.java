package com.actiknow.clearsale.activity;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;

import com.actiknow.clearsale.R;
import com.actiknow.clearsale.adapter.AllPropertyListAdapter;
import com.actiknow.clearsale.adapter.TestimonialAdapter;
import com.actiknow.clearsale.model.AllProperty;
import com.actiknow.clearsale.model.Testimonial;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by l on 20/03/2017.
 */

public class TestimonialActivity extends AppCompatActivity {

    RecyclerView rvTestimonialList;
    TestimonialAdapter testimonialAdapter;
    List<Testimonial> testimonialList = new ArrayList<>();
    SwipeRefreshLayout swipeRefreshLayout;

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
        rvTestimonialList = (RecyclerView) findViewById(R.id.rvTestimonialList);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);

    }

    private void initData() {
        swipeRefreshLayout.setRefreshing(true);
        testimonialList.clear();
        testimonialAdapter = new TestimonialAdapter(this, testimonialList);
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        rvTestimonialList.setAdapter(testimonialAdapter);
        rvTestimonialList.setHasFixedSize(true);
        rvTestimonialList.setLayoutManager(layoutManager);

    }

    private void initListener() {
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getAllTestimonial();
            }


        });

    }

    private void getAllTestimonial() {
        testimonialList.add(new Testimonial(1, R.drawable.play1, "Everything you guys promise is always as such! Make money all the time", "CHAD", ""));
        testimonialList.add(new Testimonial(2, R.drawable.play1, "Easy to work with. Everything that you pointed out was correct, actually was a little better than what you said. ", "KALEB", ""));
        testimonialList.add(new Testimonial(3, R.drawable.play1, "Very professional. Went very well, very smooth, very easy.", "JOSH", ""));

        swipeRefreshLayout.setRefreshing(false);

    }
}






