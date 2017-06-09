package com.actiknow.clearsale.fragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.text.SpannableStringBuilder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.actiknow.clearsale.R;
import com.actiknow.clearsale.utils.AppConfigTags;
import com.actiknow.clearsale.utils.AppConfigURL;
import com.actiknow.clearsale.utils.BuyerDetailsPref;
import com.actiknow.clearsale.utils.Constants;
import com.actiknow.clearsale.utils.NetworkConnection;
import com.actiknow.clearsale.utils.PropertyDetailsPref;
import com.actiknow.clearsale.utils.Utils;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;


/**
 * Created by l on 23/03/2017.
 */

public class OverviewFragment extends Fragment {
    //  ProgressDialog progressDialog;
    TextView tvOverView;
    PropertyDetailsPref propertyDetailsPref;
    // WebView webView;
    WebView webView;
    WebSettings webSettings;
    
    
    TextView tvBedroom;
    TextView tvBathroom;
    TextView tvPropertyRate;
    TextView tvBuildYear;
    TextView tvAddress1;
    TextView tvAddress2;
    TextView tvSqFeet;
    
    EditText etOfferAmount;
    EditText etOfferDescription;
    CheckBox cbAttendedAccess;
    TextView tvSubmit;
    CardView cvPropertyOffer;
    ProgressDialog progressDialog;
    BuyerDetailsPref buyerDetailsPref;
    int checked;
    
    CardView cardView3;
    Button btShowMore;
    TextView tv4;
    boolean show = true;
    
    
    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate (R.layout.fragment_overview, container, false);
        initView (rootView);
        initData ();
        initListener ();
        //    getOverviewData();
        return rootView;
    
    }
    
    
    private void initView (View rootView) {
        tv4 = (TextView) rootView.findViewById (R.id.tv4);
        
        cardView3 = (CardView) rootView.findViewById (R.id.cardview3);
        btShowMore = (Button) rootView.findViewById (R.id.btShowMore);
        
        tvBedroom = (TextView) rootView.findViewById (R.id.tvBedroom);
        tvBathroom = (TextView) rootView.findViewById (R.id.tvBathroom);
        tvPropertyRate = (TextView) rootView.findViewById (R.id.tvPropertyRate);
        tvBuildYear = (TextView) rootView.findViewById (R.id.tvBuildYear);
        tvAddress1 = (TextView) rootView.findViewById (R.id.tvAddress1);
        tvAddress2 = (TextView) rootView.findViewById (R.id.tvAddress2);
        tvSqFeet = (TextView) rootView.findViewById (R.id.tvSqFeet);
        
        
        tvOverView = (TextView) rootView.findViewById (R.id.tvOverView);
        webView = (WebView) rootView.findViewById (R.id.webView1);
        etOfferAmount = (EditText) rootView.findViewById (R.id.etOfferUsd);
        etOfferDescription = (EditText) rootView.findViewById (R.id.etOfferDetail);
        cbAttendedAccess = (CheckBox) rootView.findViewById (R.id.cbAttendedAccess);
        tvSubmit = (TextView) rootView.findViewById (R.id.tvSubmit);
        progressDialog = new ProgressDialog (getActivity ());
        cvPropertyOffer = (CardView) rootView.findViewById (R.id.cardview2);
        // clMain = (CoordinatorLayout)rootView.findViewById(R.id.clMain);
        
    }
    
    private void initData () {
        buyerDetailsPref = BuyerDetailsPref.getInstance ();
        propertyDetailsPref = PropertyDetailsPref.getInstance ();
        
        tvOverView.setText (propertyDetailsPref.getStringPref (getActivity (), PropertyDetailsPref.PROPERTY_OVERVIEW));
        
        
        tvAddress1.setText (propertyDetailsPref.getStringPref (getActivity (), PropertyDetailsPref.PROPERTY_ADDRESS1));
        tvAddress2.setText (propertyDetailsPref.getStringPref (getActivity (), PropertyDetailsPref.PROPERTY_ADDRESS2));
        tvPropertyRate.setText (propertyDetailsPref.getStringPref (getActivity (), PropertyDetailsPref.PROPERTY_PRICE));
        tvBedroom.setText (propertyDetailsPref.getStringPref (getActivity (), PropertyDetailsPref.PROPERTY_BEDROOM));
        tvBathroom.setText (propertyDetailsPref.getStringPref (getActivity (), PropertyDetailsPref.PROPERTY_BATHROOM));
        tvSqFeet.setText (propertyDetailsPref.getStringPref (getActivity (), PropertyDetailsPref.PROPERTY_AREA));
        tvBuildYear.setText (propertyDetailsPref.getStringPref (getActivity (), PropertyDetailsPref.PROPERTY_YEAR_BUILD));
        
        
        Document doc = Jsoup.parse (propertyDetailsPref.getStringPref (getActivity (), PropertyDetailsPref.PROPERTY_OVERVIEW));
        
        
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder ("<style>@font-face{font-family: myFont;src: url(file:///android_asset/" + Constants.font_name + ");}</style>" + propertyDetailsPref.getStringPref (getActivity (), PropertyDetailsPref.PROPERTY_ARV));
        webView.loadDataWithBaseURL ("www.google.com", spannableStringBuilder.toString (), "text/html", "UTF-8", "");
//        Log.e ("karman", "<style>@font-face{font-family: myFont;src: url(file:///android_asset/" + Constants.font_name + ".otf);}</style>" + propertyDetailsPref.getStringPref (getActivity (), PropertyDetailsPref.PROPERTY_OVERVIEW));
        
        
        WebSettings webSettings = webView.getSettings ();
        webSettings.setStandardFontFamily (Constants.font_name);
        
        Utils.setTypefaceToAllViews (getActivity (), tvSubmit);
        
        
        if (propertyDetailsPref.getIntPref (getActivity (), PropertyDetailsPref.PROPERTY_AUCTION_STATUS) == 1) {
            tv4.setVisibility (View.VISIBLE);
            cvPropertyOffer.setVisibility (View.VISIBLE);
        } else {
            tv4.setVisibility (View.GONE);
            cvPropertyOffer.setVisibility (View.GONE);
        }
    }
    
    private void initListener () {
        btShowMore.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View view) {
                if (show) {
                    RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams (RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                    params.addRule (RelativeLayout.BELOW, R.id.tv5);
                    params.setMargins ((int) (Utils.pxFromDp (getActivity (), 8.0f)), (int) (Utils.pxFromDp (getActivity (), 8.0f)), (int) (Utils.pxFromDp (getActivity (), 8.0f)), (int) (Utils.pxFromDp (getActivity (), 8.0f)));
                    cardView3.setLayoutParams (params);
                    btShowMore.setText ("SHOW LESS");
                    show = false;
                } else {
                    RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams (RelativeLayout.LayoutParams.MATCH_PARENT, 500);
                    params.addRule (RelativeLayout.BELOW, R.id.tv5);
                    params.setMargins ((int) (Utils.pxFromDp (getActivity (), 8.0f)), (int) (Utils.pxFromDp (getActivity (), 8.0f)), (int) (Utils.pxFromDp (getActivity (), 8.0f)), (int) (Utils.pxFromDp (getActivity (), 8.0f)));
                    cardView3.setLayoutParams (params);
                    btShowMore.setText ("SHOW MORE");
                    show = true;
                }
            }
        });
        
        tvSubmit.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View view) {
                if (cbAttendedAccess.isChecked ()) {
                    checked = 1;
                } else {
                    checked = 0;
                }
                sendBidCredentialsToServer (etOfferAmount.getText ().toString ().trim (), etOfferDescription.getText ().toString ().trim (), checked);
            }
        });
    }
    
    
    private void sendBidCredentialsToServer (final String offerAmount, final String offerDescription, final int checked) {
        if (NetworkConnection.isNetworkAvailable (getActivity ())) {
            Utils.showProgressDialog (progressDialog, getResources ().getString (R.string.progress_dialog_text_please_wait), true);
            Utils.showLog (Log.INFO, "" + AppConfigTags.URL, AppConfigURL.URL_PROPERTY_OFFER_BID, true);
            StringRequest strRequest1 = new StringRequest (Request.Method.POST, AppConfigURL.URL_PROPERTY_OFFER_BID,
                    new com.android.volley.Response.Listener<String> () {
                        @Override
                        public void onResponse (String response) {
                            Utils.showLog (Log.INFO, AppConfigTags.SERVER_RESPONSE, response, true);
                            if (response != null) {
                                try {
                                    JSONObject jsonObj = new JSONObject (response);
                                    boolean error = jsonObj.getBoolean (AppConfigTags.ERROR);
                                    String message = jsonObj.getString (AppConfigTags.MESSAGE);
                                    if (! error) {
                                        Utils.showToast (getActivity (), message, true);
                                    } else {
                                        Utils.showToast (getActivity (), message, true);
                                    }
                                    progressDialog.dismiss ();
                                } catch (Exception e) {
                                    progressDialog.dismiss ();
                                    Utils.showToast (getActivity (), getResources ().getString (R.string.snackbar_text_error_occurred), true);
                                    e.printStackTrace ();
                                }
                            } else {
                                Utils.showToast (getActivity (), getResources ().getString (R.string.snackbar_text_error_occurred), true);
                                Utils.showLog (Log.WARN, AppConfigTags.SERVER_RESPONSE, AppConfigTags.DIDNT_RECEIVE_ANY_DATA_FROM_SERVER, true);
                            }
                            progressDialog.dismiss ();
                        }
                    },
                    new com.android.volley.Response.ErrorListener () {
                        @Override
                        public void onErrorResponse (VolleyError error) {
                            progressDialog.dismiss ();
                            Utils.showLog (Log.ERROR, AppConfigTags.VOLLEY_ERROR, error.toString (), true);
                            Utils.showToast (getActivity (), getResources ().getString (R.string.snackbar_text_error_occurred), true);
                        }
                    }) {
                @Override
                protected Map<String, String> getParams () throws AuthFailureError {
                    Map<String, String> params = new Hashtable<String, String> ();
                    params.put (AppConfigTags.BUYER_ID, String.valueOf (buyerDetailsPref.getIntPref (getActivity (), BuyerDetailsPref.BUYER_ID)));
                    params.put (AppConfigTags.PROPERTY_BID_AUCTION_ID, String.valueOf (String.valueOf (propertyDetailsPref.getIntPref (getActivity (), PropertyDetailsPref.PROPERTY_AUCTION_ID))));
                    params.put (AppConfigTags.PROPERTY_BID_COMMENTS, offerDescription);
                    params.put (AppConfigTags.TYPE, "property_submit_bid");
                    params.put ("is_access", String.valueOf (checked));
                    params.put (AppConfigTags.PROPERTY_BID_AMOUNT, offerAmount);
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
            
        }
        
    }
}


