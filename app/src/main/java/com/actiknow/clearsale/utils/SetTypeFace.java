package com.actiknow.clearsale.utils;

import android.content.Context;
import android.graphics.Typeface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class SetTypeFace {

    public static Typeface getTypeface (Context c) {
        Typeface typeface = Typeface.createFromAsset (c.getAssets (), Constants.font_name);
        return typeface;
    }

    public static Typeface getTypeface (Context c, String font_name) {
        Typeface typeface = Typeface.createFromAsset (c.getAssets (), font_name);
        return typeface;
    }

    public static ViewGroup getParentView (View v) {
        ViewGroup vg = null;
        if (v != null)
            vg = (ViewGroup) v.getRootView ();
        return vg;
    }

    public static void applyTypeface (ViewGroup v, Typeface f) {
        if (v != null) {
            int vgCount = v.getChildCount ();
            for (int i = 0; i < vgCount; i++) {
                if (v.getChildAt (i) == null)
                    continue;
                if (v.getChildAt (i) instanceof ViewGroup)
                    applyTypeface ((ViewGroup) v.getChildAt (i), f);
                else {
                    View view = v.getChildAt (i);
                    if (view instanceof TextView)
                        ((TextView) (view)).setTypeface (f);
                    else if (view instanceof EditText)
                        ((EditText) (view)).setTypeface (f);
                    else if (view instanceof Button)
                        ((Button) (view)).setTypeface (f);
                    else if (view instanceof AutoCompleteTextView)
                        ((AutoCompleteTextView) (view)).setTypeface (f);
                }
            }
        }
    }
}
