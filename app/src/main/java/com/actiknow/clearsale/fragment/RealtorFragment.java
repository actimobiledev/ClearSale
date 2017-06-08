package com.actiknow.clearsale.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.TextView;

import com.actiknow.clearsale.R;
import com.actiknow.clearsale.utils.Constants;
import com.actiknow.clearsale.utils.PropertyDetailsPref;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;


/**
 * Created by l on 23/03/2017.
 */

public class RealtorFragment extends Fragment {
    TextView tvRealtor;
    PropertyDetailsPref propertyDetailsPref;
    WebView webView;


    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate (R.layout.fragment_realtors, container, false);
        initView (rootView);
        initData ();
        initListener ();
        return rootView;

    }

    private void initView(View rootView) {
        propertyDetailsPref = PropertyDetailsPref.getInstance ();
        tvRealtor = (TextView) rootView.findViewById (R.id.tvRealtor);
        webView = (WebView) rootView.findViewById (R.id.webView1);
    }

    private void initData() {
        Document doc = Jsoup.parse (propertyDetailsPref.getStringPref (getActivity (), PropertyDetailsPref.PROPERTY_REALTOR));
    
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder ("<style>@font-face{font-family: myFont;src: url(file:///android_asset/" + Constants.font_name + ");}</style>" + propertyDetailsPref.getStringPref (getActivity (), PropertyDetailsPref.PROPERTY_REALTOR));
        webView.loadDataWithBaseURL ("www.google.com", spannableStringBuilder.toString (), "text/html", "UTF-8", "");
//        Log.e ("karman", "<style>@font-face{font-family: myFont;src: url(file:///android_asset/" + Constants.font_name + ".otf);}</style>" + propertyDetailsPref.getStringPref (getActivity (), PropertyDetailsPref.PROPERTY_REALTOR));
    
        tvRealtor.setText (Html.fromHtml (propertyDetailsPref.getStringPref (getActivity (), PropertyDetailsPref.PROPERTY_REALTOR)));
    }

    private void initListener() {
    }
}
