package com.actiknow.clearsale.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.util.Linkify;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.TextView;

import com.actiknow.clearsale.R;
import com.actiknow.clearsale.utils.Constants;
import com.actiknow.clearsale.utils.PropertyDetailsPref;


/**
 * Created by l on 23/03/2017.
 */

public class CompsFragment extends Fragment {
    TextView tvComps;
    PropertyDetailsPref propertyDetailsPref;
    WebView webView;

    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate (R.layout.fragment_comps, container, false);
        initView (rootView);
        initData ();
        initListener ();
        return rootView;

    }

    private void initView(View rootView) {
        tvComps = (TextView) rootView.findViewById (R.id.tvComps);
        webView = (WebView) rootView.findViewById (R.id.webView1);

    }

    private void initData() {
        propertyDetailsPref = PropertyDetailsPref.getInstance ();
        tvComps.setText (Html.fromHtml (propertyDetailsPref.getStringPref (getActivity (), PropertyDetailsPref.PROPERTY_COMPS)));
        tvComps.setAutoLinkMask (Linkify.WEB_URLS);
        tvComps.setLinksClickable (true);
        webView.loadData (propertyDetailsPref.getStringPref (getActivity (), PropertyDetailsPref.PROPERTY_COMPS), "text/html", "UTF-8");
        WebSettings webSettings = webView.getSettings ();
        webSettings.setStandardFontFamily (Constants.font_name);


    }

    private void initListener() {

    }
}
