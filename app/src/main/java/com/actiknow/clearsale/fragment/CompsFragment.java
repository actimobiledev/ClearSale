package com.actiknow.clearsale.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.actiknow.clearsale.R;


/**
 * Created by l on 23/03/2017.
 */

public class CompsFragment extends Fragment {


    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate (R.layout.fragment_comps, container, false);
        initView (rootView);
        initData ();
        initListener ();


        return rootView;

    }

    private void initView(View rootView) {

    }

    private void initData() {

    }

    private void initListener() {

    }
}
