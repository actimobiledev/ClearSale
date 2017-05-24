package com.actiknow.clearsale.activity;


import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.actiknow.clearsale.R;
import com.actiknow.clearsale.utils.Utils;


/**
 * Created by l on 22/03/2017.
 */

public class AboutUsActivity extends AppCompatActivity {
    CollapsingToolbarLayout collapsingToolbarLayout;
    AppBarLayout appBar;
    Toolbar toolbar;
    TextView tvHyperlink;
    RelativeLayout rlBack;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);
        initView();
        initData();
        initListener();
    }

    private void initView() {
        rlBack = (RelativeLayout) findViewById (R.id.rlBack);
        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        appBar = (AppBarLayout) findViewById(R.id.appbar);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        tvHyperlink=(TextView)findViewById(R.id.tvHyperlink);
    }

    private void initData() {
        collapsingToolbarLayout.setTitleEnabled (false);
        appBar.setExpanded (true);
        Utils.setTypefaceToAllViews (this, tvHyperlink);
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
    
        rlBack.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View v) {
                finish ();
                overridePendingTransition (R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });
    }
}




