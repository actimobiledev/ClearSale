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
import com.actiknow.clearsale.adapter.FAQAdapter;
import com.actiknow.clearsale.model.FAQ;
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

public class FAQActivity extends AppCompatActivity {
    RecyclerView rvFAQs;
    List<FAQ> FAQList = new ArrayList<> ();
    FAQAdapter faqAdapter;
    CoordinatorLayout clMain;
    SwipeRefreshLayout swipeRefreshLayout;
    RelativeLayout rlBack;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faq);
        initView();
        initData();
        initListener();
        getFAQList ();
    }

    private void initView() {
        rvFAQs = (RecyclerView) findViewById (R.id.rvFAQs);
        clMain = (CoordinatorLayout) findViewById(R.id.clMain);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        rlBack = (RelativeLayout) findViewById (R.id.rlBack);
    }

    private void initData() {
        // FAQList.add(new FAQ(1, "Are Deposits Refundable ? ", "Deposits are non-refundable. The only circumstance that will result in a deposit refund is if we are not able to deliver clear title."));
        // FAQList.add(new FAQ(2, "Does the deposit get applied to the purchase price if i buy property ? ", "Yes, all deposits are applied to the purchase price. The deposits are NOT in addition to the price."));
        // FAQList.add(new FAQ(3, "Can i inspect the property ?", "Before you are allowed to submit an offer you must view and complete a basic inspection of the property at our pre-determined access date and time. Generally there is just one opportunity for this access. This is not a formal inspection, it is about a 15 minute walk-through of the property. No sewer scopes or second inspections are permitted, ask if an exception is needed. "));
        //  FAQList.add(new FAQ(4, "Can i purchase the property with a loan ?", "All properties must be purchased with cash or hard money. Traditional loans do not work due to lead times."));
        //  FAQList.add(new FAQ(5, "What is the condition of the property when i get possessions ?", "All properties are sold in as-is condition. Sellers are allowed to leave anything they want in the home, so it is best to be prepared with a dumpster and clean out service."));
        //  FAQList.add(new FAQ(6, "How do i submit my offer ?", "Login and locate the available properties section and find your deal. Click the top left grey area when the property is accepting offers. This is a short window of time after access, normally less than 24 hours. You must have attended an access to place your offer."));
        //  FAQList.add(new FAQ(7, "How mush time do i have to think about a property once i have submitted my offer ?", "You should place your offer when you are 100% ready to buy at your offer price and terms. Do not attempt to \"tie up\" a property while you think about it."));
        swipeRefreshLayout.setRefreshing (true);
        faqAdapter = new FAQAdapter (FAQActivity.this, FAQList);
        rvFAQs.setAdapter (faqAdapter);
        rvFAQs.setHasFixedSize (true);
        rvFAQs.setLayoutManager (new LinearLayoutManager (this, LinearLayoutManager.VERTICAL, false));
        rvFAQs.setItemAnimator (new DefaultItemAnimator ());
        Utils.setTypefaceToAllViews (this, rlBack);
    }
    
    private void initListener() {
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(true);
                getFAQList ();
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
    
    private void getFAQList () {
        if (NetworkConnection.isNetworkAvailable (FAQActivity.this)) {
            Utils.showLog(Log.INFO, "" + AppConfigTags.URL, AppConfigURL.URL_FAQ, true);
            StringRequest strRequest1 = new StringRequest(Request.Method.POST, AppConfigURL.URL_FAQ,
                    new com.android.volley.Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            FAQList.clear ();
                            Utils.showLog(Log.INFO,AppConfigTags.SERVER_RESPONSE, response, true);
                            if (response != null) {
                                try {
                                    JSONObject jsonObj = new JSONObject(response);
                                    boolean error = jsonObj.getBoolean(AppConfigTags.ERROR);
                                    String message = jsonObj.getString(AppConfigTags.MESSAGE);
                                    if (!error) {
                                        JSONArray jsonArrayFaq = jsonObj.getJSONArray(AppConfigTags.FAQ_LIST);
                                        for (int i = 0; i < jsonArrayFaq.length(); i++) {
                                            JSONObject jsonObjectFaq = jsonArrayFaq.getJSONObject(i);
                                            FAQList.add (new FAQ (
                                                    jsonObjectFaq.getInt (AppConfigTags.QUESTION_ID),
                                                    jsonObjectFaq.getString (AppConfigTags.QUESTION),
                                                    jsonObjectFaq.getString (AppConfigTags.ANSWER)));
                                        }
                                        if (jsonArrayFaq.length() > 0) {
                                            swipeRefreshLayout.setRefreshing(false);
                                        }
                                        faqAdapter.notifyDataSetChanged();
                                    } else {
                                        Utils.showSnackBar (FAQActivity.this, clMain, message, Snackbar.LENGTH_LONG, null, null);
                                    }
                                } catch (Exception e) {
                                    Utils.showSnackBar (FAQActivity.this, clMain, getResources ().getString (R.string.snackbar_text_exception_occurred), Snackbar.LENGTH_LONG, getResources ().getString (R.string.snackbar_action_dismiss), null);
                                    e.printStackTrace();
                                }
                            } else {
                                Utils.showSnackBar (FAQActivity.this, clMain, getResources ().getString (R.string.snackbar_text_error_occurred), Snackbar.LENGTH_LONG, getResources ().getString (R.string.snackbar_action_dismiss), null);
                                Utils.showLog(Log.WARN, AppConfigTags.SERVER_RESPONSE, AppConfigTags.DIDNT_RECEIVE_ANY_DATA_FROM_SERVER, true);
                            }
                            swipeRefreshLayout.setRefreshing (false);
                        }
                    },
                    new com.android.volley.Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            swipeRefreshLayout.setRefreshing (false);
                            Utils.showLog(Log.ERROR, AppConfigTags.VOLLEY_ERROR, error.toString(), true);
                            Utils.showSnackBar (FAQActivity.this, clMain, getResources ().getString (R.string.snackbar_text_error_occurred), Snackbar.LENGTH_LONG, getResources ().getString (R.string.snackbar_action_dismiss), null);
                        }
                    }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new Hashtable<String, String>();
                    params.put(AppConfigTags.TYPE, "faqlist");
                    Utils.showLog(Log.INFO, AppConfigTags.PARAMETERS_SENT_TO_THE_SERVER, "" + params, true);
                    return params;
                }

                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put(AppConfigTags.HEADER_API_KEY, Constants.api_key);
                    Utils.showLog(Log.INFO, AppConfigTags.HEADERS_SENT_TO_THE_SERVER, "" + params, false);
                    return params;
                }
            };
            Utils.sendRequest(strRequest1, 60);
        } else {
            swipeRefreshLayout.setRefreshing (false);
            Utils.showSnackBar(this, clMain, getResources().getString(R.string.snackbar_text_no_internet_connection_available), Snackbar.LENGTH_LONG, getResources().getString(R.string.snackbar_action_go_to_settings), new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent dialogIntent = new Intent(Settings.ACTION_SETTINGS);
                    dialogIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(dialogIntent);
                }
            });
        }

    }
}



