package com.actiknow.clearsale.activity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;

import com.actiknow.clearsale.R;
import com.actiknow.clearsale.adapter.TestimonialAdapter;
import com.actiknow.clearsale.model.Testimonial;
import com.actiknow.clearsale.utils.AppConfigTags;
import com.actiknow.clearsale.utils.AppConfigURL;
import com.actiknow.clearsale.utils.Constants;
import com.actiknow.clearsale.utils.NetworkConnection;
import com.actiknow.clearsale.utils.Utils;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

public class TestimonialActivity extends AppCompatActivity {
    RecyclerView rvTestimonials;
    TestimonialAdapter testimonialAdapter;
    List<Testimonial> testimonialList = new ArrayList<>();
    SwipeRefreshLayout swipeRefreshLayout;
    RelativeLayout rlBack;
    CoordinatorLayout clMain;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_testimonials);
        initView();
        initData();
        initListener();
        getAllTestimonials ();
    }

    private void initView() {
        clMain = (CoordinatorLayout) findViewById (R.id.clMain);
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
                getAllTestimonials ();
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
    
    /*
      private void getAllTestimonial() {
          testimonialList.clear ();
          testimonialList.add (new Testimonial (1, "Everything you guys promise is always as such! Make money all the time", "CHAD", ""));
          testimonialList.add (new Testimonial (2, "Easy to work with. Everything that you pointed out was correct, actually was a little better than what you said. ", "KALEB", ""));
          testimonialList.add (new Testimonial (3, "Very professional. Went very well, very smooth, very easy.", "JOSH", ""));
          swipeRefreshLayout.setRefreshing(false);
          testimonialAdapter.notifyDataSetChanged ();
      }
  */
    @Override
    public void onBackPressed () {
        finish ();
        overridePendingTransition (R.anim.slide_in_left, R.anim.slide_out_right);
    }
    
    
    private void getAllTestimonials () {
        if (NetworkConnection.isNetworkAvailable (TestimonialActivity.this)) {
            Utils.showLog (Log.INFO, "" + AppConfigTags.URL, AppConfigURL.URL_TESTIMONIALS, true);
            StringRequest strRequest1 = new StringRequest (Request.Method.POST, AppConfigURL.URL_TESTIMONIALS,
                    new com.android.volley.Response.Listener<String> () {
                        @Override
                        public void onResponse (String response) {
                            testimonialList.clear ();
                            Utils.showLog (Log.INFO, AppConfigTags.SERVER_RESPONSE, response, true);
                            if (response != null) {
                                try {
                                    JSONObject jsonObj = new JSONObject (response);
                                    boolean error = jsonObj.getBoolean (AppConfigTags.ERROR);
                                    String message = jsonObj.getString (AppConfigTags.MESSAGE);
                                    if (! error) {
                                        JSONArray jsonArrayTestimonial = jsonObj.getJSONArray (AppConfigTags.TESTIMONIALS);
                                        for (int i = 0; i < jsonArrayTestimonial.length (); i++) {
                                            JSONObject jsonObjectTestimonial = jsonArrayTestimonial.getJSONObject (i);
                                            testimonialList.add (new Testimonial (
                                                    jsonObjectTestimonial.getInt (AppConfigTags.TESTIMONIAL_ID),
                                                    jsonObjectTestimonial.getString (AppConfigTags.TESTIMONIAL_DESCRIPTION),
                                                    jsonObjectTestimonial.getString (AppConfigTags.TESTIMONIAL_CLIENT_NAME),
                                                    jsonObjectTestimonial.getString (AppConfigTags.TESTIMONIAL_URL)));
                                        }
                                        if (jsonArrayTestimonial.length () > 0) {
                                            swipeRefreshLayout.setRefreshing (false);
                                        }
                                        testimonialAdapter.notifyDataSetChanged ();
                                    } else {
                                        Utils.showSnackBar (TestimonialActivity.this, clMain, message, Snackbar.LENGTH_LONG, null, null);
                                    }
                                } catch (Exception e) {
                                    Utils.showSnackBar (TestimonialActivity.this, clMain, getResources ().getString (R.string.snackbar_text_exception_occurred), Snackbar.LENGTH_LONG, getResources ().getString (R.string.snackbar_action_dismiss), null);
                                    e.printStackTrace ();
                                }
                            } else {
                                Utils.showSnackBar (TestimonialActivity.this, clMain, getResources ().getString (R.string.snackbar_text_error_occurred), Snackbar.LENGTH_LONG, getResources ().getString (R.string.snackbar_action_dismiss), null);
                                Utils.showLog (Log.WARN, AppConfigTags.SERVER_RESPONSE, AppConfigTags.DIDNT_RECEIVE_ANY_DATA_FROM_SERVER, true);
                            }
                            swipeRefreshLayout.setRefreshing (false);
                        }
                    },
                    new com.android.volley.Response.ErrorListener () {
                        @Override
                        public void onErrorResponse (VolleyError error) {
                            swipeRefreshLayout.setRefreshing (false);
                            Utils.showLog (Log.ERROR, AppConfigTags.VOLLEY_ERROR, error.toString (), true);
                            Utils.showSnackBar (TestimonialActivity.this, clMain, getResources ().getString (R.string.snackbar_text_error_occurred), Snackbar.LENGTH_LONG, getResources ().getString (R.string.snackbar_action_dismiss), null);
                        }
                    }) {
                @Override
                protected Map<String, String> getParams () throws AuthFailureError {
                    Map<String, String> params = new Hashtable<String, String> ();
                    params.put (AppConfigTags.TYPE, AppConfigTags.TESTIMONIALS);
                    Utils.showLog (Log.INFO, AppConfigTags.PARAMETERS_SENT_TO_THE_SERVER, "" + params, true);
                    return params;
                }
                
                @Override
                public Map<String, String> getHeaders () throws AuthFailureError {
                    Map<String, String> params = new HashMap<> ();
                    params.put (AppConfigTags.HEADER_API_KEY, Constants.api_key);
                    Utils.showLog (Log.INFO, AppConfigTags.HEADERS_SENT_TO_THE_SERVER, "" + params, false);
                    return params;
                }
            };
            Utils.sendRequest (strRequest1, 60);
        } else {
            swipeRefreshLayout.setRefreshing (false);
            Utils.showSnackBar (this, clMain, getResources ().getString (R.string.snackbar_text_no_internet_connection_available), Snackbar.LENGTH_LONG, getResources ().getString (R.string.snackbar_action_go_to_settings), new View.OnClickListener () {
                @Override
                public void onClick (View v) {
                    Intent dialogIntent = new Intent (Settings.ACTION_SETTINGS);
                    dialogIntent.addFlags (Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity (dialogIntent);
                }
            });
        }
    }
}






