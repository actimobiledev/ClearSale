package com.actiknow.clearsale.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.TextView;

import com.actiknow.clearsale.R;
import com.actiknow.clearsale.utils.PropertyDetailsPref;


/**
 * Created by l on 23/03/2017.
 */

public class PlaceAndOfferFragment extends Fragment {
    TextView tvOffer;
    PropertyDetailsPref propertyDetailsPref;
    
    WebView webView;
    
    
    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate (R.layout.fragment_place_and_order, container, false);
        initView (rootView);
        initData ();
        initListener ();


        return rootView;

    }

    private void initView(View rootView) {
        tvOffer = (TextView) rootView.findViewById (R.id.tvOffer);
        webView = (WebView) rootView.findViewById (R.id.webView1);

    }

    private void initData() {
        propertyDetailsPref = PropertyDetailsPref.getInstance ();
        tvOffer.setText (Html.fromHtml (propertyDetailsPref.getStringPref (getActivity (), PropertyDetailsPref.PROPERTY_OFFER)));
        webView.loadData (propertyDetailsPref.getStringPref (getActivity (), PropertyDetailsPref.PROPERTY_OFFER), "text/html", "UTF-8");


    }

    private void initListener() {

    }
}
