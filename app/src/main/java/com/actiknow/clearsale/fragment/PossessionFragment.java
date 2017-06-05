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

public class PossessionFragment extends Fragment {
    PropertyDetailsPref propertyDetailsPref;
    TextView tvPossession;
    WebView webView;
    
    
    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate (R.layout.fragment_access_possession, container, false);
        initView (rootView);
        initData ();
        initListener ();


        return rootView;

    }

    private void initView(View rootView) {
        tvPossession = (TextView) rootView.findViewById (R.id.tvAccessPossession);
        webView = (WebView) rootView.findViewById (R.id.webView1);

    }

    private void initData() {
        propertyDetailsPref = PropertyDetailsPref.getInstance ();
        tvPossession.setText (Html.fromHtml (propertyDetailsPref.getStringPref (getActivity (), PropertyDetailsPref.PROPERTY_ACCESS)));
        webView.loadData (propertyDetailsPref.getStringPref (getActivity (), PropertyDetailsPref.PROPERTY_ACCESS), "text/html", "UTF-8");
    }

    private void initListener() {

    }
}
