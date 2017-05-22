package com.actiknow.clearsale.activity;


import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.actiknow.clearsale.R;


/**
 * Created by l on 22/03/2017.
 */

public class AboutUsActivity extends AppCompatActivity {
    private CollapsingToolbarLayout collapsingToolbarLayout = null;
    AppBarLayout appBar;
    Toolbar toolbar;
    TextView tvHyperlink;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);

        initView();
        initData();
        initListener();


    }

    private void initView() {

        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        appBar = (AppBarLayout) findViewById(R.id.appbar);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        tvHyperlink=(TextView)findViewById(R.id.tvHyperlink);


    }

    private void initData() {
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);


        collapsingToolbarLayout.setTitle("About Us");
        collapsingToolbarLayout.setTitleEnabled(true);
        appBar.setExpanded(true);


    }

    private void initListener() {

       // tvHyperlink.setClickable(true);
      //  tvHyperlink.setMovementMethod(LinkMovementMethod.getInstance());
      //  String text = "<a href='http://www.google.com'> Google </a>";
      //  tvHyperlink.setText(Html.fromHtml(text));
        tvHyperlink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
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


}




