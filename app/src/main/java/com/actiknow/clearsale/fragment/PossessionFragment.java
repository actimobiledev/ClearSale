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
    
    
        Document doc = Jsoup.parse (propertyDetailsPref.getStringPref (getActivity (), PropertyDetailsPref.PROPERTY_ACCESS));
    
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder ("<style>@font-face{font-family: myFont;src: url(file:///android_asset/" + Constants.font_name + ");}</style>" + propertyDetailsPref.getStringPref (getActivity (), PropertyDetailsPref.PROPERTY_ACCESS));
        webView.loadDataWithBaseURL ("www.google.com", spannableStringBuilder.toString (), "text/html", "UTF-8", "");
//        Log.e ("karman", "<style>@font-face{font-family: myFont;src: url(file:///android_asset/" + Constants.font_name + ".otf);}</style>" + propertyDetailsPref.getStringPref (getActivity (), PropertyDetailsPref.PROPERTY_ACCESS));
        
        tvPossession.setText (Html.fromHtml (propertyDetailsPref.getStringPref (getActivity (), PropertyDetailsPref.PROPERTY_ACCESS)));
//        webView.loadData (propertyDetailsPref.getStringPref (getActivity (), PropertyDetailsPref.PROPERTY_ACCESS), "text/html", "UTF-8");
    }

    private void initListener() {

    }
}
