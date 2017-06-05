package com.actiknow.clearsale.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.actiknow.clearsale.R;
import com.actiknow.clearsale.utils.AppConfigTags;
import com.actiknow.clearsale.utils.AppConfigURL;
import com.actiknow.clearsale.utils.BuyerDetailsPref;
import com.actiknow.clearsale.utils.Constants;
import com.actiknow.clearsale.utils.NetworkConnection;
import com.actiknow.clearsale.utils.TypefaceSpan;
import com.actiknow.clearsale.utils.Utils;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.jaredrummler.materialspinner.MaterialSpinner;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

public class MyProfileActivity extends AppCompatActivity {
    EditText etFullName;
    EditText etEmail;
    EditText etPhone;
    CheckBox cb1;
    CheckBox cb2;
    CheckBox cb3;
    CheckBox cb4;
    MaterialSpinner spinner;
    LinearLayout llState;
    BuyerDetailsPref buyerDetailsPref;
    CoordinatorLayout clMain;
    List<String> stateSelectedList = new ArrayList<String> ();
    List<String> budgetSelectedList = new ArrayList ();
    ProgressDialog progressDialog;
    String[] spinnerItems;
    
    TextView tvButton1;
    TextView tvButton2;
    TextView tvButton3;
    TextView tvButton4;
    TextView tvButton5;
    TextView tvButton6;
    TextView tvButton7;
    TextView tvButton8;
    TextView tvButton9;
    TextView tvButton10;
    
    RelativeLayout rlBack;
    TextView tvSave;
    
    List<String> stateList = new ArrayList<String> ();
    
    TextView tvColorado;
    TextView tvSeattle;
    
    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_my_profile);
        initView ();
        initData ();
        initListener ();
//        getStateListFromServer();
        getPreferencesData ();
    }
    
    private void getPreferencesData () {
        etFullName.setText (buyerDetailsPref.getStringPref (MyProfileActivity.this, BuyerDetailsPref.BUYER_NAME).trim ());
        etEmail.setText (buyerDetailsPref.getStringPref (MyProfileActivity.this, BuyerDetailsPref.BUYER_EMAIL));
        etPhone.setText (buyerDetailsPref.getStringPref (MyProfileActivity.this, BuyerDetailsPref.BUYER_MOBILE));
        
        String stateName[] = buyerDetailsPref.getStringPref (MyProfileActivity.this, BuyerDetailsPref.PROFILE_STATE).trim ().split (",");
        
        String priceRange[] = buyerDetailsPref.getStringPref (MyProfileActivity.this, BuyerDetailsPref.PROFILE_PRICE_RANGE).trim ().split (",");
        
        for (int i = 0; i < stateName.length; i++) {
            try {
                if (stateName[i].equalsIgnoreCase (stateList.get (0))) {
                    stateSelectedList.add (stateList.get (0));
                    tvButton1.setBackgroundResource (R.drawable.state_button_selected);
                    tvButton1.setTextColor (getResources ().getColor (R.color.text_color_white));
                } else if (stateName[i].equalsIgnoreCase (stateList.get (1))) {
                    stateSelectedList.add (stateList.get (1));
                    tvButton2.setBackgroundResource (R.drawable.state_button_selected);
                    tvButton2.setTextColor (getResources ().getColor (R.color.text_color_white));
                } else if (stateName[i].equalsIgnoreCase (stateList.get (2))) {
                    stateSelectedList.add (stateList.get (2));
                    tvButton3.setBackgroundResource (R.drawable.state_button_selected);
                    tvButton3.setTextColor (getResources ().getColor (R.color.text_color_white));
                } else if (stateName[i].equalsIgnoreCase (stateList.get (3))) {
                    stateSelectedList.add (stateList.get (3));
                    tvButton4.setBackgroundResource (R.drawable.state_button_selected);
                    tvButton4.setTextColor (getResources ().getColor (R.color.text_color_white));
                } else if (stateName[i].equalsIgnoreCase (stateList.get (4))) {
                    stateSelectedList.add (stateList.get (4));
                    tvButton5.setBackgroundResource (R.drawable.state_button_selected);
                    tvButton5.setTextColor (getResources ().getColor (R.color.text_color_white));
                } else if (stateName[i].equalsIgnoreCase (stateList.get (5))) {
                    stateSelectedList.add (stateList.get (5));
                    tvButton6.setBackgroundResource (R.drawable.state_button_selected);
                    tvButton6.setTextColor (getResources ().getColor (R.color.text_color_white));
                } else if (stateName[i].equalsIgnoreCase (stateList.get (6))) {
                    stateSelectedList.add (stateList.get (6));
                    tvButton7.setBackgroundResource (R.drawable.state_button_selected);
                    tvButton7.setTextColor (getResources ().getColor (R.color.text_color_white));
                } else if (stateName[i].equalsIgnoreCase (stateList.get (7))) {
                    stateSelectedList.add (stateList.get (7));
                    tvButton8.setBackgroundResource (R.drawable.state_button_selected);
                    tvButton8.setTextColor (getResources ().getColor (R.color.text_color_white));
                } else if (stateName[i].equalsIgnoreCase (stateList.get (8))) {
                    stateSelectedList.add (stateList.get (8));
                    tvButton9.setBackgroundResource (R.drawable.state_button_selected);
                    tvButton9.setTextColor (getResources ().getColor (R.color.text_color_white));
                } else if (stateName[i].equalsIgnoreCase (stateList.get (9))) {
                    stateSelectedList.add (stateList.get (9));
                    tvButton10.setBackgroundResource (R.drawable.state_button_selected);
                    tvButton10.setTextColor (getResources ().getColor (R.color.text_color_white));
                }
            } catch (Exception e) {
                e.printStackTrace ();
            }

    
              /*
            switch (stateName[i]) {
                case "CO":
                    stateSelectedList.add ("CO");
                    tvColorado.setBackgroundResource (R.drawable.state_button_selected);
                    tvColorado.setTextColor (getResources ().getColor (R.color.text_color_white));
                    break;
                case "WA":
                    stateSelectedList.add ("WA");
                    tvSeattle.setBackgroundResource (R.drawable.state_button_selected);
                    tvSeattle.setTextColor (getResources ().getColor (R.color.text_color_white));
                    break;
            }
            */
        }
        
        for (int i = 0; i < priceRange.length; i++) {
            switch (priceRange[i]) {
                case "1":
                    cb1.setChecked (true);
                    break;
                case "2":
                    cb2.setChecked (true);
                    break;
                case "3":
                    cb3.setChecked (true);
                    break;
                case "4":
                    cb4.setChecked (true);
                    break;
            }
        }
        
        if (buyerDetailsPref.getStringPref (MyProfileActivity.this, BuyerDetailsPref.PROFILE_HOME_TYPE).equalsIgnoreCase (spinnerItems[1])) {
            spinner.setSelectedIndex (1);
        } else if (buyerDetailsPref.getStringPref (MyProfileActivity.this, BuyerDetailsPref.PROFILE_HOME_TYPE).equalsIgnoreCase (spinnerItems[2])) {
            spinner.setSelectedIndex (2);
        } else if (buyerDetailsPref.getStringPref (MyProfileActivity.this, BuyerDetailsPref.PROFILE_HOME_TYPE).equalsIgnoreCase (spinnerItems[3])) {
            spinner.setSelectedIndex (3);
        } else if (buyerDetailsPref.getStringPref (MyProfileActivity.this, BuyerDetailsPref.PROFILE_HOME_TYPE).equalsIgnoreCase (spinnerItems[4])) {
            spinner.setSelectedIndex (4);
        }
    }
    
    private void initView () {
        etFullName = (EditText) findViewById (R.id.etFullName);
        etEmail = (EditText) findViewById (R.id.etEmail);
        etPhone = (EditText) findViewById (R.id.etPhone);
        rlBack = (RelativeLayout) findViewById (R.id.rlBack);
        tvSave = (TextView) findViewById (R.id.tvSave);
        cb1 = (CheckBox) findViewById (R.id.cb1);
        cb2 = (CheckBox) findViewById (R.id.cb2);
        cb3 = (CheckBox) findViewById (R.id.cb3);
        cb4 = (CheckBox) findViewById (R.id.cb4);
        spinner = (MaterialSpinner) findViewById (R.id.spinner);
        llState = (LinearLayout) findViewById (R.id.llState);
        clMain = (CoordinatorLayout) findViewById (R.id.clMain);
        tvColorado = (TextView) findViewById (R.id.tvColorado);
        tvSeattle = (TextView) findViewById (R.id.tvSeattle);
    
    
        tvButton1 = (TextView) findViewById (R.id.tvButton1);
        tvButton2 = (TextView) findViewById (R.id.tvButton2);
        tvButton3 = (TextView) findViewById (R.id.tvButton3);
        tvButton4 = (TextView) findViewById (R.id.tvButton4);
        tvButton5 = (TextView) findViewById (R.id.tvButton5);
        tvButton6 = (TextView) findViewById (R.id.tvButton6);
        tvButton7 = (TextView) findViewById (R.id.tvButton7);
        tvButton8 = (TextView) findViewById (R.id.tvButton8);
        tvButton9 = (TextView) findViewById (R.id.tvButton9);
        tvButton10 = (TextView) findViewById (R.id.tvButton10);
        
        Utils.setTypefaceToAllViews (this, tvSave);
    }
    
    private void initData () {
        progressDialog = new ProgressDialog (MyProfileActivity.this);
        buyerDetailsPref = BuyerDetailsPref.getInstance ();
        spinnerItems = new String[] {
                "Select Home Type",
                "Cosmetic only",
                "Bath and kitchens plus all of the above",
                "Whole house remodel include moving walls plus all of the above",
                "Structural work plus all of above",
        };
    
        stateList.clear ();
        stateList.add ("CO");
        stateList.add ("WA");
       
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String> (this, R.layout.spinner, spinnerItems);
        spinnerArrayAdapter.setDropDownViewResource (R.layout.spinner);
        spinner.setAdapter (spinnerArrayAdapter);
    
    
        llState.setWeightSum (stateList.size ());
        for (int i = 1; i <= stateList.size (); i++) {
            switch (i) {
                case 1:
                    tvButton1.setText (stateList.get (0));
                    tvButton1.setVisibility (View.VISIBLE);
                    tvButton1.setOnClickListener (new View.OnClickListener () {
                        @Override
                        public void onClick (View v) {
                            if (stateSelectedList.contains (stateList.get (0))) {
                                for (int i = 0; i < stateSelectedList.size (); i++) {
                                    if (stateSelectedList.get (i).equalsIgnoreCase (stateList.get (0))) {
                                        stateSelectedList.remove (i);
                                        tvButton1.setBackgroundResource (R.drawable.state_button_unselected);
                                        tvButton1.setTextColor (getResources ().getColor (R.color.app_text_color_dark));
                                    }
                                }
                            } else {
                                stateSelectedList.add (stateList.get (0));
                                tvButton1.setBackgroundResource (R.drawable.state_button_selected);
                                tvButton1.setTextColor (getResources ().getColor (R.color.text_color_white));
                            }
                        }
                    });
                
                    break;
                case 2:
                    tvButton1.setText (stateList.get (0));
                    tvButton1.setVisibility (View.VISIBLE);
                    tvButton2.setText (stateList.get (1));
                    tvButton2.setVisibility (View.VISIBLE);
                    tvButton1.setOnClickListener (new View.OnClickListener () {
                        @Override
                        public void onClick (View v) {
                            if (stateSelectedList.contains (stateList.get (0))) {
                                for (int i = 0; i < stateSelectedList.size (); i++) {
                                    if (stateSelectedList.get (i).equalsIgnoreCase (stateList.get (0))) {
                                        stateSelectedList.remove (i);
                                        tvButton1.setBackgroundResource (R.drawable.state_button_unselected);
                                        tvButton1.setTextColor (getResources ().getColor (R.color.app_text_color_dark));
                                    }
                                }
                            } else {
                                stateSelectedList.add (stateList.get (0));
                                tvButton1.setBackgroundResource (R.drawable.state_button_selected);
                                tvButton1.setTextColor (getResources ().getColor (R.color.text_color_white));
                            }
                        }
                    });
                
                    tvButton2.setOnClickListener (new View.OnClickListener () {
                        @Override
                        public void onClick (View v) {
                            if (stateSelectedList.contains (stateList.get (1))) {
                                for (int i = 0; i < stateSelectedList.size (); i++) {
                                    if (stateSelectedList.get (i).equalsIgnoreCase (stateList.get (1))) {
                                        stateSelectedList.remove (i);
                                        tvButton2.setBackgroundResource (R.drawable.state_button_unselected);
                                        tvButton2.setTextColor (getResources ().getColor (R.color.app_text_color_dark));
                                    }
                                }
                            } else {
                                stateSelectedList.add (stateList.get (1));
                                tvButton2.setBackgroundResource (R.drawable.state_button_selected);
                                tvButton2.setTextColor (getResources ().getColor (R.color.text_color_white));
                            }
                        }
                    });
                
                    break;
                case 3:
                    tvButton1.setText (stateList.get (0));
                    tvButton1.setVisibility (View.VISIBLE);
                    tvButton2.setText (stateList.get (1));
                    tvButton2.setVisibility (View.VISIBLE);
                    tvButton3.setText (stateList.get (2));
                    tvButton3.setVisibility (View.VISIBLE);
                    tvButton1.setOnClickListener (new View.OnClickListener () {
                        @Override
                        public void onClick (View v) {
                            if (stateSelectedList.contains (stateList.get (0))) {
                                for (int i = 0; i < stateSelectedList.size (); i++) {
                                    if (stateSelectedList.get (i).equalsIgnoreCase (stateList.get (0))) {
                                        stateSelectedList.remove (i);
                                        tvButton1.setBackgroundResource (R.drawable.state_button_unselected);
                                        tvButton1.setTextColor (getResources ().getColor (R.color.app_text_color_dark));
                                    }
                                }
                            } else {
                                stateSelectedList.add (stateList.get (0));
                                tvButton1.setBackgroundResource (R.drawable.state_button_selected);
                                tvButton1.setTextColor (getResources ().getColor (R.color.text_color_white));
                            }
                        }
                    });
                
                    tvButton2.setOnClickListener (new View.OnClickListener () {
                        @Override
                        public void onClick (View v) {
                            if (stateSelectedList.contains (stateList.get (1))) {
                                for (int i = 0; i < stateSelectedList.size (); i++) {
                                    if (stateSelectedList.get (i).equalsIgnoreCase (stateList.get (1))) {
                                        stateSelectedList.remove (i);
                                        tvButton2.setBackgroundResource (R.drawable.state_button_unselected);
                                        tvButton2.setTextColor (getResources ().getColor (R.color.app_text_color_dark));
                                    }
                                }
                            } else {
                                stateSelectedList.add (stateList.get (1));
                                tvButton2.setBackgroundResource (R.drawable.state_button_selected);
                                tvButton2.setTextColor (getResources ().getColor (R.color.text_color_white));
                            }
                        }
                    });
                
                    tvButton3.setOnClickListener (new View.OnClickListener () {
                        @Override
                        public void onClick (View v) {
                            if (stateSelectedList.contains (stateList.get (2))) {
                                for (int i = 0; i < stateSelectedList.size (); i++) {
                                    if (stateSelectedList.get (i).equalsIgnoreCase (stateList.get (2))) {
                                        stateSelectedList.remove (i);
                                        tvButton3.setBackgroundResource (R.drawable.state_button_unselected);
                                        tvButton3.setTextColor (getResources ().getColor (R.color.app_text_color_dark));
                                    }
                                }
                            } else {
                                stateSelectedList.add (stateList.get (2));
                                tvButton3.setBackgroundResource (R.drawable.state_button_selected);
                                tvButton3.setTextColor (getResources ().getColor (R.color.text_color_white));
                            }
                        }
                    });
                
                    break;
                case 4:
                    tvButton1.setText (stateList.get (0));
                    tvButton1.setVisibility (View.VISIBLE);
                    tvButton2.setText (stateList.get (1));
                    tvButton2.setVisibility (View.VISIBLE);
                    tvButton3.setText (stateList.get (2));
                    tvButton3.setVisibility (View.VISIBLE);
                    tvButton4.setText (stateList.get (3));
                    tvButton4.setVisibility (View.VISIBLE);
                    tvButton1.setOnClickListener (new View.OnClickListener () {
                        @Override
                        public void onClick (View v) {
                            if (stateSelectedList.contains (stateList.get (0))) {
                                for (int i = 0; i < stateSelectedList.size (); i++) {
                                    if (stateSelectedList.get (i).equalsIgnoreCase (stateList.get (0))) {
                                        stateSelectedList.remove (i);
                                        tvButton1.setBackgroundResource (R.drawable.state_button_unselected);
                                        tvButton1.setTextColor (getResources ().getColor (R.color.app_text_color_dark));
                                    }
                                }
                            } else {
                                stateSelectedList.add (stateList.get (0));
                                tvButton1.setBackgroundResource (R.drawable.state_button_selected);
                                tvButton1.setTextColor (getResources ().getColor (R.color.text_color_white));
                            }
                        }
                    });
                
                    tvButton2.setOnClickListener (new View.OnClickListener () {
                        @Override
                        public void onClick (View v) {
                            if (stateSelectedList.contains (stateList.get (1))) {
                                for (int i = 0; i < stateSelectedList.size (); i++) {
                                    if (stateSelectedList.get (i).equalsIgnoreCase (stateList.get (1))) {
                                        stateSelectedList.remove (i);
                                        tvButton2.setBackgroundResource (R.drawable.state_button_unselected);
                                        tvButton2.setTextColor (getResources ().getColor (R.color.app_text_color_dark));
                                    }
                                }
                            } else {
                                stateSelectedList.add (stateList.get (1));
                                tvButton2.setBackgroundResource (R.drawable.state_button_selected);
                                tvButton2.setTextColor (getResources ().getColor (R.color.text_color_white));
                            }
                        }
                    });
                
                    tvButton3.setOnClickListener (new View.OnClickListener () {
                        @Override
                        public void onClick (View v) {
                            if (stateSelectedList.contains (stateList.get (2))) {
                                for (int i = 0; i < stateSelectedList.size (); i++) {
                                    if (stateSelectedList.get (i).equalsIgnoreCase (stateList.get (2))) {
                                        stateSelectedList.remove (i);
                                        tvButton3.setBackgroundResource (R.drawable.state_button_unselected);
                                        tvButton3.setTextColor (getResources ().getColor (R.color.app_text_color_dark));
                                    }
                                }
                            } else {
                                stateSelectedList.add (stateList.get (2));
                                tvButton3.setBackgroundResource (R.drawable.state_button_selected);
                                tvButton3.setTextColor (getResources ().getColor (R.color.text_color_white));
                            }
                        }
                    });
                
                    tvButton4.setOnClickListener (new View.OnClickListener () {
                        @Override
                        public void onClick (View v) {
                            if (stateSelectedList.contains (stateList.get (3))) {
                                for (int i = 0; i < stateSelectedList.size (); i++) {
                                    if (stateSelectedList.get (i).equalsIgnoreCase (stateList.get (3))) {
                                        stateSelectedList.remove (i);
                                        tvButton4.setBackgroundResource (R.drawable.state_button_unselected);
                                        tvButton4.setTextColor (getResources ().getColor (R.color.app_text_color_dark));
                                    }
                                }
                            } else {
                                stateSelectedList.add (stateList.get (3));
                                tvButton4.setBackgroundResource (R.drawable.state_button_selected);
                                tvButton4.setTextColor (getResources ().getColor (R.color.text_color_white));
                            }
                        }
                    });
                
                    break;
                case 5:
                    tvButton1.setText (stateList.get (0));
                    tvButton1.setVisibility (View.VISIBLE);
                    tvButton2.setText (stateList.get (1));
                    tvButton2.setVisibility (View.VISIBLE);
                    tvButton3.setText (stateList.get (2));
                    tvButton3.setVisibility (View.VISIBLE);
                    tvButton4.setText (stateList.get (3));
                    tvButton4.setVisibility (View.VISIBLE);
                    tvButton5.setText (stateList.get (4));
                    tvButton5.setVisibility (View.VISIBLE);
                    tvButton1.setOnClickListener (new View.OnClickListener () {
                        @Override
                        public void onClick (View v) {
                            if (stateSelectedList.contains (stateList.get (0))) {
                                for (int i = 0; i < stateSelectedList.size (); i++) {
                                    if (stateSelectedList.get (i).equalsIgnoreCase (stateList.get (0))) {
                                        stateSelectedList.remove (i);
                                        tvButton1.setBackgroundResource (R.drawable.state_button_unselected);
                                        tvButton1.setTextColor (getResources ().getColor (R.color.app_text_color_dark));
                                    }
                                }
                            } else {
                                stateSelectedList.add (stateList.get (0));
                                tvButton1.setBackgroundResource (R.drawable.state_button_selected);
                                tvButton1.setTextColor (getResources ().getColor (R.color.text_color_white));
                            }
                        }
                    });
                
                    tvButton2.setOnClickListener (new View.OnClickListener () {
                        @Override
                        public void onClick (View v) {
                            if (stateSelectedList.contains (stateList.get (1))) {
                                for (int i = 0; i < stateSelectedList.size (); i++) {
                                    if (stateSelectedList.get (i).equalsIgnoreCase (stateList.get (1))) {
                                        stateSelectedList.remove (i);
                                        tvButton2.setBackgroundResource (R.drawable.state_button_unselected);
                                        tvButton2.setTextColor (getResources ().getColor (R.color.app_text_color_dark));
                                    }
                                }
                            } else {
                                stateSelectedList.add (stateList.get (1));
                                tvButton2.setBackgroundResource (R.drawable.state_button_selected);
                                tvButton2.setTextColor (getResources ().getColor (R.color.text_color_white));
                            }
                        }
                    });
                
                    tvButton3.setOnClickListener (new View.OnClickListener () {
                        @Override
                        public void onClick (View v) {
                            if (stateSelectedList.contains (stateList.get (2))) {
                                for (int i = 0; i < stateSelectedList.size (); i++) {
                                    if (stateSelectedList.get (i).equalsIgnoreCase (stateList.get (2))) {
                                        stateSelectedList.remove (i);
                                        tvButton3.setBackgroundResource (R.drawable.state_button_unselected);
                                        tvButton3.setTextColor (getResources ().getColor (R.color.app_text_color_dark));
                                    }
                                }
                            } else {
                                stateSelectedList.add (stateList.get (2));
                                tvButton3.setBackgroundResource (R.drawable.state_button_selected);
                                tvButton3.setTextColor (getResources ().getColor (R.color.text_color_white));
                            }
                        }
                    });
                
                    tvButton4.setOnClickListener (new View.OnClickListener () {
                        @Override
                        public void onClick (View v) {
                            if (stateSelectedList.contains (stateList.get (3))) {
                                for (int i = 0; i < stateSelectedList.size (); i++) {
                                    if (stateSelectedList.get (i).equalsIgnoreCase (stateList.get (3))) {
                                        stateSelectedList.remove (i);
                                        tvButton4.setBackgroundResource (R.drawable.state_button_unselected);
                                        tvButton4.setTextColor (getResources ().getColor (R.color.app_text_color_dark));
                                    }
                                }
                            } else {
                                stateSelectedList.add (stateList.get (3));
                                tvButton4.setBackgroundResource (R.drawable.state_button_selected);
                                tvButton4.setTextColor (getResources ().getColor (R.color.text_color_white));
                            }
                        }
                    });
                
                    tvButton5.setOnClickListener (new View.OnClickListener () {
                        @Override
                        public void onClick (View v) {
                            if (stateSelectedList.contains (stateList.get (4))) {
                                for (int i = 0; i < stateSelectedList.size (); i++) {
                                    if (stateSelectedList.get (i).equalsIgnoreCase (stateList.get (4))) {
                                        stateSelectedList.remove (i);
                                        tvButton5.setBackgroundResource (R.drawable.state_button_unselected);
                                        tvButton5.setTextColor (getResources ().getColor (R.color.app_text_color_dark));
                                    }
                                }
                            } else {
                                stateSelectedList.add (stateList.get (4));
                                tvButton5.setBackgroundResource (R.drawable.state_button_selected);
                                tvButton5.setTextColor (getResources ().getColor (R.color.text_color_white));
                            }
                        }
                    });
                
                    break;
                case 6:
                    tvButton1.setText (stateList.get (0));
                    tvButton1.setVisibility (View.VISIBLE);
                    tvButton2.setText (stateList.get (1));
                    tvButton2.setVisibility (View.VISIBLE);
                    tvButton3.setText (stateList.get (2));
                    tvButton3.setVisibility (View.VISIBLE);
                    tvButton4.setText (stateList.get (3));
                    tvButton4.setVisibility (View.VISIBLE);
                    tvButton5.setText (stateList.get (4));
                    tvButton5.setVisibility (View.VISIBLE);
                    tvButton6.setText (stateList.get (5));
                    tvButton6.setVisibility (View.VISIBLE);
                    tvButton1.setOnClickListener (new View.OnClickListener () {
                        @Override
                        public void onClick (View v) {
                            if (stateSelectedList.contains (stateList.get (0))) {
                                for (int i = 0; i < stateSelectedList.size (); i++) {
                                    if (stateSelectedList.get (i).equalsIgnoreCase (stateList.get (0))) {
                                        stateSelectedList.remove (i);
                                        tvButton1.setBackgroundResource (R.drawable.state_button_unselected);
                                        tvButton1.setTextColor (getResources ().getColor (R.color.app_text_color_dark));
                                    }
                                }
                            } else {
                                stateSelectedList.add (stateList.get (0));
                                tvButton1.setBackgroundResource (R.drawable.state_button_selected);
                                tvButton1.setTextColor (getResources ().getColor (R.color.text_color_white));
                            }
                        }
                    });
                
                    tvButton2.setOnClickListener (new View.OnClickListener () {
                        @Override
                        public void onClick (View v) {
                            if (stateSelectedList.contains (stateList.get (1))) {
                                for (int i = 0; i < stateSelectedList.size (); i++) {
                                    if (stateSelectedList.get (i).equalsIgnoreCase (stateList.get (1))) {
                                        stateSelectedList.remove (i);
                                        tvButton2.setBackgroundResource (R.drawable.state_button_unselected);
                                        tvButton2.setTextColor (getResources ().getColor (R.color.app_text_color_dark));
                                    }
                                }
                            } else {
                                stateSelectedList.add (stateList.get (1));
                                tvButton2.setBackgroundResource (R.drawable.state_button_selected);
                                tvButton2.setTextColor (getResources ().getColor (R.color.text_color_white));
                            }
                        }
                    });
                
                    tvButton3.setOnClickListener (new View.OnClickListener () {
                        @Override
                        public void onClick (View v) {
                            if (stateSelectedList.contains (stateList.get (2))) {
                                for (int i = 0; i < stateSelectedList.size (); i++) {
                                    if (stateSelectedList.get (i).equalsIgnoreCase (stateList.get (2))) {
                                        stateSelectedList.remove (i);
                                        tvButton3.setBackgroundResource (R.drawable.state_button_unselected);
                                        tvButton3.setTextColor (getResources ().getColor (R.color.app_text_color_dark));
                                    }
                                }
                            } else {
                                stateSelectedList.add (stateList.get (2));
                                tvButton3.setBackgroundResource (R.drawable.state_button_selected);
                                tvButton3.setTextColor (getResources ().getColor (R.color.text_color_white));
                            }
                        }
                    });
                
                    tvButton4.setOnClickListener (new View.OnClickListener () {
                        @Override
                        public void onClick (View v) {
                            if (stateSelectedList.contains (stateList.get (3))) {
                                for (int i = 0; i < stateSelectedList.size (); i++) {
                                    if (stateSelectedList.get (i).equalsIgnoreCase (stateList.get (3))) {
                                        stateSelectedList.remove (i);
                                        tvButton4.setBackgroundResource (R.drawable.state_button_unselected);
                                        tvButton4.setTextColor (getResources ().getColor (R.color.app_text_color_dark));
                                    }
                                }
                            } else {
                                stateSelectedList.add (stateList.get (3));
                                tvButton4.setBackgroundResource (R.drawable.state_button_selected);
                                tvButton4.setTextColor (getResources ().getColor (R.color.text_color_white));
                            }
                        }
                    });
                
                    tvButton5.setOnClickListener (new View.OnClickListener () {
                        @Override
                        public void onClick (View v) {
                            if (stateSelectedList.contains (stateList.get (4))) {
                                for (int i = 0; i < stateSelectedList.size (); i++) {
                                    if (stateSelectedList.get (i).equalsIgnoreCase (stateList.get (4))) {
                                        stateSelectedList.remove (i);
                                        tvButton5.setBackgroundResource (R.drawable.state_button_unselected);
                                        tvButton5.setTextColor (getResources ().getColor (R.color.app_text_color_dark));
                                    }
                                }
                            } else {
                                stateSelectedList.add (stateList.get (4));
                                tvButton5.setBackgroundResource (R.drawable.state_button_selected);
                                tvButton5.setTextColor (getResources ().getColor (R.color.text_color_white));
                            }
                        }
                    });
                
                    tvButton6.setOnClickListener (new View.OnClickListener () {
                        @Override
                        public void onClick (View v) {
                            if (stateSelectedList.contains (stateList.get (5))) {
                                for (int i = 0; i < stateSelectedList.size (); i++) {
                                    if (stateSelectedList.get (i).equalsIgnoreCase (stateList.get (5))) {
                                        stateSelectedList.remove (i);
                                        tvButton6.setBackgroundResource (R.drawable.state_button_unselected);
                                        tvButton6.setTextColor (getResources ().getColor (R.color.app_text_color_dark));
                                    }
                                }
                            } else {
                                stateSelectedList.add (stateList.get (5));
                                tvButton6.setBackgroundResource (R.drawable.state_button_selected);
                                tvButton6.setTextColor (getResources ().getColor (R.color.text_color_white));
                            }
                        }
                    });
                
                    break;
                case 7:
                    tvButton1.setText (stateList.get (0));
                    tvButton1.setVisibility (View.VISIBLE);
                    tvButton2.setText (stateList.get (1));
                    tvButton2.setVisibility (View.VISIBLE);
                    tvButton3.setText (stateList.get (2));
                    tvButton3.setVisibility (View.VISIBLE);
                    tvButton4.setText (stateList.get (3));
                    tvButton4.setVisibility (View.VISIBLE);
                    tvButton5.setText (stateList.get (4));
                    tvButton5.setVisibility (View.VISIBLE);
                    tvButton6.setText (stateList.get (5));
                    tvButton6.setVisibility (View.VISIBLE);
                    tvButton7.setText (stateList.get (6));
                    tvButton7.setVisibility (View.VISIBLE);
                    tvButton1.setOnClickListener (new View.OnClickListener () {
                        @Override
                        public void onClick (View v) {
                            if (stateSelectedList.contains (stateList.get (0))) {
                                for (int i = 0; i < stateSelectedList.size (); i++) {
                                    if (stateSelectedList.get (i).equalsIgnoreCase (stateList.get (0))) {
                                        stateSelectedList.remove (i);
                                        tvButton1.setBackgroundResource (R.drawable.state_button_unselected);
                                        tvButton1.setTextColor (getResources ().getColor (R.color.app_text_color_dark));
                                    }
                                }
                            } else {
                                stateSelectedList.add (stateList.get (0));
                                tvButton1.setBackgroundResource (R.drawable.state_button_selected);
                                tvButton1.setTextColor (getResources ().getColor (R.color.text_color_white));
                            }
                        }
                    });
                
                    tvButton2.setOnClickListener (new View.OnClickListener () {
                        @Override
                        public void onClick (View v) {
                            if (stateSelectedList.contains (stateList.get (1))) {
                                for (int i = 0; i < stateSelectedList.size (); i++) {
                                    if (stateSelectedList.get (i).equalsIgnoreCase (stateList.get (1))) {
                                        stateSelectedList.remove (i);
                                        tvButton2.setBackgroundResource (R.drawable.state_button_unselected);
                                        tvButton2.setTextColor (getResources ().getColor (R.color.app_text_color_dark));
                                    }
                                }
                            } else {
                                stateSelectedList.add (stateList.get (1));
                                tvButton2.setBackgroundResource (R.drawable.state_button_selected);
                                tvButton2.setTextColor (getResources ().getColor (R.color.text_color_white));
                            }
                        }
                    });
                
                    tvButton3.setOnClickListener (new View.OnClickListener () {
                        @Override
                        public void onClick (View v) {
                            if (stateSelectedList.contains (stateList.get (2))) {
                                for (int i = 0; i < stateSelectedList.size (); i++) {
                                    if (stateSelectedList.get (i).equalsIgnoreCase (stateList.get (2))) {
                                        stateSelectedList.remove (i);
                                        tvButton3.setBackgroundResource (R.drawable.state_button_unselected);
                                        tvButton3.setTextColor (getResources ().getColor (R.color.app_text_color_dark));
                                    }
                                }
                            } else {
                                stateSelectedList.add (stateList.get (2));
                                tvButton3.setBackgroundResource (R.drawable.state_button_selected);
                                tvButton3.setTextColor (getResources ().getColor (R.color.text_color_white));
                            }
                        }
                    });
                
                    tvButton4.setOnClickListener (new View.OnClickListener () {
                        @Override
                        public void onClick (View v) {
                            if (stateSelectedList.contains (stateList.get (3))) {
                                for (int i = 0; i < stateSelectedList.size (); i++) {
                                    if (stateSelectedList.get (i).equalsIgnoreCase (stateList.get (3))) {
                                        stateSelectedList.remove (i);
                                        tvButton4.setBackgroundResource (R.drawable.state_button_unselected);
                                        tvButton4.setTextColor (getResources ().getColor (R.color.app_text_color_dark));
                                    }
                                }
                            } else {
                                stateSelectedList.add (stateList.get (3));
                                tvButton4.setBackgroundResource (R.drawable.state_button_selected);
                                tvButton4.setTextColor (getResources ().getColor (R.color.text_color_white));
                            }
                        }
                    });
                
                    tvButton5.setOnClickListener (new View.OnClickListener () {
                        @Override
                        public void onClick (View v) {
                            if (stateSelectedList.contains (stateList.get (4))) {
                                for (int i = 0; i < stateSelectedList.size (); i++) {
                                    if (stateSelectedList.get (i).equalsIgnoreCase (stateList.get (4))) {
                                        stateSelectedList.remove (i);
                                        tvButton5.setBackgroundResource (R.drawable.state_button_unselected);
                                        tvButton5.setTextColor (getResources ().getColor (R.color.app_text_color_dark));
                                    }
                                }
                            } else {
                                stateSelectedList.add (stateList.get (4));
                                tvButton5.setBackgroundResource (R.drawable.state_button_selected);
                                tvButton5.setTextColor (getResources ().getColor (R.color.text_color_white));
                            }
                        }
                    });
                
                    tvButton6.setOnClickListener (new View.OnClickListener () {
                        @Override
                        public void onClick (View v) {
                            if (stateSelectedList.contains (stateList.get (5))) {
                                for (int i = 0; i < stateSelectedList.size (); i++) {
                                    if (stateSelectedList.get (i).equalsIgnoreCase (stateList.get (5))) {
                                        stateSelectedList.remove (i);
                                        tvButton6.setBackgroundResource (R.drawable.state_button_unselected);
                                        tvButton6.setTextColor (getResources ().getColor (R.color.app_text_color_dark));
                                    }
                                }
                            } else {
                                stateSelectedList.add (stateList.get (5));
                                tvButton6.setBackgroundResource (R.drawable.state_button_selected);
                                tvButton6.setTextColor (getResources ().getColor (R.color.text_color_white));
                            }
                        }
                    });
                
                    tvButton7.setOnClickListener (new View.OnClickListener () {
                        @Override
                        public void onClick (View v) {
                            if (stateSelectedList.contains (stateList.get (6))) {
                                for (int i = 0; i < stateSelectedList.size (); i++) {
                                    if (stateSelectedList.get (i).equalsIgnoreCase (stateList.get (6))) {
                                        stateSelectedList.remove (i);
                                        tvButton7.setBackgroundResource (R.drawable.state_button_unselected);
                                        tvButton7.setTextColor (getResources ().getColor (R.color.app_text_color_dark));
                                    }
                                }
                            } else {
                                stateSelectedList.add (stateList.get (6));
                                tvButton7.setBackgroundResource (R.drawable.state_button_selected);
                                tvButton7.setTextColor (getResources ().getColor (R.color.text_color_white));
                            }
                        }
                    });
                
                    break;
                case 8:
                    tvButton1.setText (stateList.get (0));
                    tvButton1.setVisibility (View.VISIBLE);
                    tvButton2.setText (stateList.get (1));
                    tvButton2.setVisibility (View.VISIBLE);
                    tvButton3.setText (stateList.get (2));
                    tvButton3.setVisibility (View.VISIBLE);
                    tvButton4.setText (stateList.get (3));
                    tvButton4.setVisibility (View.VISIBLE);
                    tvButton5.setText (stateList.get (4));
                    tvButton5.setVisibility (View.VISIBLE);
                    tvButton6.setText (stateList.get (5));
                    tvButton6.setVisibility (View.VISIBLE);
                    tvButton7.setText (stateList.get (6));
                    tvButton7.setVisibility (View.VISIBLE);
                    tvButton8.setText (stateList.get (7));
                    tvButton8.setVisibility (View.VISIBLE);
                    tvButton1.setOnClickListener (new View.OnClickListener () {
                        @Override
                        public void onClick (View v) {
                            if (stateSelectedList.contains (stateList.get (0))) {
                                for (int i = 0; i < stateSelectedList.size (); i++) {
                                    if (stateSelectedList.get (i).equalsIgnoreCase (stateList.get (0))) {
                                        stateSelectedList.remove (i);
                                        tvButton1.setBackgroundResource (R.drawable.state_button_unselected);
                                        tvButton1.setTextColor (getResources ().getColor (R.color.app_text_color_dark));
                                    }
                                }
                            } else {
                                stateSelectedList.add (stateList.get (0));
                                tvButton1.setBackgroundResource (R.drawable.state_button_selected);
                                tvButton1.setTextColor (getResources ().getColor (R.color.text_color_white));
                            }
                        }
                    });
                
                    tvButton2.setOnClickListener (new View.OnClickListener () {
                        @Override
                        public void onClick (View v) {
                            if (stateSelectedList.contains (stateList.get (1))) {
                                for (int i = 0; i < stateSelectedList.size (); i++) {
                                    if (stateSelectedList.get (i).equalsIgnoreCase (stateList.get (1))) {
                                        stateSelectedList.remove (i);
                                        tvButton2.setBackgroundResource (R.drawable.state_button_unselected);
                                        tvButton2.setTextColor (getResources ().getColor (R.color.app_text_color_dark));
                                    }
                                }
                            } else {
                                stateSelectedList.add (stateList.get (1));
                                tvButton2.setBackgroundResource (R.drawable.state_button_selected);
                                tvButton2.setTextColor (getResources ().getColor (R.color.text_color_white));
                            }
                        }
                    });
                
                    tvButton3.setOnClickListener (new View.OnClickListener () {
                        @Override
                        public void onClick (View v) {
                            if (stateSelectedList.contains (stateList.get (2))) {
                                for (int i = 0; i < stateSelectedList.size (); i++) {
                                    if (stateSelectedList.get (i).equalsIgnoreCase (stateList.get (2))) {
                                        stateSelectedList.remove (i);
                                        tvButton3.setBackgroundResource (R.drawable.state_button_unselected);
                                        tvButton3.setTextColor (getResources ().getColor (R.color.app_text_color_dark));
                                    }
                                }
                            } else {
                                stateSelectedList.add (stateList.get (2));
                                tvButton3.setBackgroundResource (R.drawable.state_button_selected);
                                tvButton3.setTextColor (getResources ().getColor (R.color.text_color_white));
                            }
                        }
                    });
                
                    tvButton4.setOnClickListener (new View.OnClickListener () {
                        @Override
                        public void onClick (View v) {
                            if (stateSelectedList.contains (stateList.get (3))) {
                                for (int i = 0; i < stateSelectedList.size (); i++) {
                                    if (stateSelectedList.get (i).equalsIgnoreCase (stateList.get (3))) {
                                        stateSelectedList.remove (i);
                                        tvButton4.setBackgroundResource (R.drawable.state_button_unselected);
                                        tvButton4.setTextColor (getResources ().getColor (R.color.app_text_color_dark));
                                    }
                                }
                            } else {
                                stateSelectedList.add (stateList.get (3));
                                tvButton4.setBackgroundResource (R.drawable.state_button_selected);
                                tvButton4.setTextColor (getResources ().getColor (R.color.text_color_white));
                            }
                        }
                    });
                
                    tvButton5.setOnClickListener (new View.OnClickListener () {
                        @Override
                        public void onClick (View v) {
                            if (stateSelectedList.contains (stateList.get (4))) {
                                for (int i = 0; i < stateSelectedList.size (); i++) {
                                    if (stateSelectedList.get (i).equalsIgnoreCase (stateList.get (4))) {
                                        stateSelectedList.remove (i);
                                        tvButton5.setBackgroundResource (R.drawable.state_button_unselected);
                                        tvButton5.setTextColor (getResources ().getColor (R.color.app_text_color_dark));
                                    }
                                }
                            } else {
                                stateSelectedList.add (stateList.get (4));
                                tvButton5.setBackgroundResource (R.drawable.state_button_selected);
                                tvButton5.setTextColor (getResources ().getColor (R.color.text_color_white));
                            }
                        }
                    });
                
                    tvButton6.setOnClickListener (new View.OnClickListener () {
                        @Override
                        public void onClick (View v) {
                            if (stateSelectedList.contains (stateList.get (5))) {
                                for (int i = 0; i < stateSelectedList.size (); i++) {
                                    if (stateSelectedList.get (i).equalsIgnoreCase (stateList.get (5))) {
                                        stateSelectedList.remove (i);
                                        tvButton6.setBackgroundResource (R.drawable.state_button_unselected);
                                        tvButton6.setTextColor (getResources ().getColor (R.color.app_text_color_dark));
                                    }
                                }
                            } else {
                                stateSelectedList.add (stateList.get (5));
                                tvButton6.setBackgroundResource (R.drawable.state_button_selected);
                                tvButton6.setTextColor (getResources ().getColor (R.color.text_color_white));
                            }
                        }
                    });
                
                    tvButton7.setOnClickListener (new View.OnClickListener () {
                        @Override
                        public void onClick (View v) {
                            if (stateSelectedList.contains (stateList.get (6))) {
                                for (int i = 0; i < stateSelectedList.size (); i++) {
                                    if (stateSelectedList.get (i).equalsIgnoreCase (stateList.get (6))) {
                                        stateSelectedList.remove (i);
                                        tvButton7.setBackgroundResource (R.drawable.state_button_unselected);
                                        tvButton7.setTextColor (getResources ().getColor (R.color.app_text_color_dark));
                                    }
                                }
                            } else {
                                stateSelectedList.add (stateList.get (6));
                                tvButton7.setBackgroundResource (R.drawable.state_button_selected);
                                tvButton7.setTextColor (getResources ().getColor (R.color.text_color_white));
                            }
                        }
                    });
                
                    tvButton8.setOnClickListener (new View.OnClickListener () {
                        @Override
                        public void onClick (View v) {
                            if (stateSelectedList.contains (stateList.get (7))) {
                                for (int i = 0; i < stateSelectedList.size (); i++) {
                                    if (stateSelectedList.get (i).equalsIgnoreCase (stateList.get (7))) {
                                        stateSelectedList.remove (i);
                                        tvButton8.setBackgroundResource (R.drawable.state_button_unselected);
                                        tvButton8.setTextColor (getResources ().getColor (R.color.app_text_color_dark));
                                    }
                                }
                            } else {
                                stateSelectedList.add (stateList.get (7));
                                tvButton8.setBackgroundResource (R.drawable.state_button_selected);
                                tvButton8.setTextColor (getResources ().getColor (R.color.text_color_white));
                            }
                        }
                    });
                
                    break;
                case 9:
                    tvButton1.setText (stateList.get (0));
                    tvButton1.setVisibility (View.VISIBLE);
                    tvButton2.setText (stateList.get (1));
                    tvButton2.setVisibility (View.VISIBLE);
                    tvButton3.setText (stateList.get (2));
                    tvButton3.setVisibility (View.VISIBLE);
                    tvButton4.setText (stateList.get (3));
                    tvButton4.setVisibility (View.VISIBLE);
                    tvButton5.setText (stateList.get (4));
                    tvButton5.setVisibility (View.VISIBLE);
                    tvButton6.setText (stateList.get (5));
                    tvButton6.setVisibility (View.VISIBLE);
                    tvButton7.setText (stateList.get (6));
                    tvButton7.setVisibility (View.VISIBLE);
                    tvButton8.setText (stateList.get (7));
                    tvButton8.setVisibility (View.VISIBLE);
                    tvButton9.setText (stateList.get (8));
                    tvButton9.setVisibility (View.VISIBLE);
                    tvButton1.setOnClickListener (new View.OnClickListener () {
                        @Override
                        public void onClick (View v) {
                            if (stateSelectedList.contains (stateList.get (0))) {
                                for (int i = 0; i < stateSelectedList.size (); i++) {
                                    if (stateSelectedList.get (i).equalsIgnoreCase (stateList.get (0))) {
                                        stateSelectedList.remove (i);
                                        tvButton1.setBackgroundResource (R.drawable.state_button_unselected);
                                        tvButton1.setTextColor (getResources ().getColor (R.color.app_text_color_dark));
                                    }
                                }
                            } else {
                                stateSelectedList.add (stateList.get (0));
                                tvButton1.setBackgroundResource (R.drawable.state_button_selected);
                                tvButton1.setTextColor (getResources ().getColor (R.color.text_color_white));
                            }
                        }
                    });
                
                    tvButton2.setOnClickListener (new View.OnClickListener () {
                        @Override
                        public void onClick (View v) {
                            if (stateSelectedList.contains (stateList.get (1))) {
                                for (int i = 0; i < stateSelectedList.size (); i++) {
                                    if (stateSelectedList.get (i).equalsIgnoreCase (stateList.get (1))) {
                                        stateSelectedList.remove (i);
                                        tvButton2.setBackgroundResource (R.drawable.state_button_unselected);
                                        tvButton2.setTextColor (getResources ().getColor (R.color.app_text_color_dark));
                                    }
                                }
                            } else {
                                stateSelectedList.add (stateList.get (1));
                                tvButton2.setBackgroundResource (R.drawable.state_button_selected);
                                tvButton2.setTextColor (getResources ().getColor (R.color.text_color_white));
                            }
                        }
                    });
                
                    tvButton3.setOnClickListener (new View.OnClickListener () {
                        @Override
                        public void onClick (View v) {
                            if (stateSelectedList.contains (stateList.get (2))) {
                                for (int i = 0; i < stateSelectedList.size (); i++) {
                                    if (stateSelectedList.get (i).equalsIgnoreCase (stateList.get (2))) {
                                        stateSelectedList.remove (i);
                                        tvButton3.setBackgroundResource (R.drawable.state_button_unselected);
                                        tvButton3.setTextColor (getResources ().getColor (R.color.app_text_color_dark));
                                    }
                                }
                            } else {
                                stateSelectedList.add (stateList.get (2));
                                tvButton3.setBackgroundResource (R.drawable.state_button_selected);
                                tvButton3.setTextColor (getResources ().getColor (R.color.text_color_white));
                            }
                        }
                    });
                
                    tvButton4.setOnClickListener (new View.OnClickListener () {
                        @Override
                        public void onClick (View v) {
                            if (stateSelectedList.contains (stateList.get (3))) {
                                for (int i = 0; i < stateSelectedList.size (); i++) {
                                    if (stateSelectedList.get (i).equalsIgnoreCase (stateList.get (3))) {
                                        stateSelectedList.remove (i);
                                        tvButton4.setBackgroundResource (R.drawable.state_button_unselected);
                                        tvButton4.setTextColor (getResources ().getColor (R.color.app_text_color_dark));
                                    }
                                }
                            } else {
                                stateSelectedList.add (stateList.get (3));
                                tvButton4.setBackgroundResource (R.drawable.state_button_selected);
                                tvButton4.setTextColor (getResources ().getColor (R.color.text_color_white));
                            }
                        }
                    });
                
                    tvButton5.setOnClickListener (new View.OnClickListener () {
                        @Override
                        public void onClick (View v) {
                            if (stateSelectedList.contains (stateList.get (4))) {
                                for (int i = 0; i < stateSelectedList.size (); i++) {
                                    if (stateSelectedList.get (i).equalsIgnoreCase (stateList.get (4))) {
                                        stateSelectedList.remove (i);
                                        tvButton5.setBackgroundResource (R.drawable.state_button_unselected);
                                        tvButton5.setTextColor (getResources ().getColor (R.color.app_text_color_dark));
                                    }
                                }
                            } else {
                                stateSelectedList.add (stateList.get (4));
                                tvButton5.setBackgroundResource (R.drawable.state_button_selected);
                                tvButton5.setTextColor (getResources ().getColor (R.color.text_color_white));
                            }
                        }
                    });
                
                    tvButton6.setOnClickListener (new View.OnClickListener () {
                        @Override
                        public void onClick (View v) {
                            if (stateSelectedList.contains (stateList.get (5))) {
                                for (int i = 0; i < stateSelectedList.size (); i++) {
                                    if (stateSelectedList.get (i).equalsIgnoreCase (stateList.get (5))) {
                                        stateSelectedList.remove (i);
                                        tvButton6.setBackgroundResource (R.drawable.state_button_unselected);
                                        tvButton6.setTextColor (getResources ().getColor (R.color.app_text_color_dark));
                                    }
                                }
                            } else {
                                stateSelectedList.add (stateList.get (5));
                                tvButton6.setBackgroundResource (R.drawable.state_button_selected);
                                tvButton6.setTextColor (getResources ().getColor (R.color.text_color_white));
                            }
                        }
                    });
                
                    tvButton7.setOnClickListener (new View.OnClickListener () {
                        @Override
                        public void onClick (View v) {
                            if (stateSelectedList.contains (stateList.get (6))) {
                                for (int i = 0; i < stateSelectedList.size (); i++) {
                                    if (stateSelectedList.get (i).equalsIgnoreCase (stateList.get (6))) {
                                        stateSelectedList.remove (i);
                                        tvButton7.setBackgroundResource (R.drawable.state_button_unselected);
                                        tvButton7.setTextColor (getResources ().getColor (R.color.app_text_color_dark));
                                    }
                                }
                            } else {
                                stateSelectedList.add (stateList.get (6));
                                tvButton7.setBackgroundResource (R.drawable.state_button_selected);
                                tvButton7.setTextColor (getResources ().getColor (R.color.text_color_white));
                            }
                        }
                    });
                
                    tvButton8.setOnClickListener (new View.OnClickListener () {
                        @Override
                        public void onClick (View v) {
                            if (stateSelectedList.contains (stateList.get (7))) {
                                for (int i = 0; i < stateSelectedList.size (); i++) {
                                    if (stateSelectedList.get (i).equalsIgnoreCase (stateList.get (7))) {
                                        stateSelectedList.remove (i);
                                        tvButton8.setBackgroundResource (R.drawable.state_button_unselected);
                                        tvButton8.setTextColor (getResources ().getColor (R.color.app_text_color_dark));
                                    }
                                }
                            } else {
                                stateSelectedList.add (stateList.get (7));
                                tvButton8.setBackgroundResource (R.drawable.state_button_selected);
                                tvButton8.setTextColor (getResources ().getColor (R.color.text_color_white));
                            }
                        }
                    });
                
                    tvButton9.setOnClickListener (new View.OnClickListener () {
                        @Override
                        public void onClick (View v) {
                            if (stateSelectedList.contains (stateList.get (8))) {
                                for (int i = 0; i < stateSelectedList.size (); i++) {
                                    if (stateSelectedList.get (i).equalsIgnoreCase (stateList.get (8))) {
                                        stateSelectedList.remove (i);
                                        tvButton9.setBackgroundResource (R.drawable.state_button_unselected);
                                        tvButton9.setTextColor (getResources ().getColor (R.color.app_text_color_dark));
                                    }
                                }
                            } else {
                                stateSelectedList.add (stateList.get (8));
                                tvButton9.setBackgroundResource (R.drawable.state_button_selected);
                                tvButton9.setTextColor (getResources ().getColor (R.color.text_color_white));
                            }
                        }
                    });
                
                    break;
                case 10:
                    tvButton1.setText (stateList.get (0));
                    tvButton1.setVisibility (View.VISIBLE);
                    tvButton2.setText (stateList.get (1));
                    tvButton2.setVisibility (View.VISIBLE);
                    tvButton3.setText (stateList.get (2));
                    tvButton3.setVisibility (View.VISIBLE);
                    tvButton4.setText (stateList.get (3));
                    tvButton4.setVisibility (View.VISIBLE);
                    tvButton5.setText (stateList.get (4));
                    tvButton5.setVisibility (View.VISIBLE);
                    tvButton6.setText (stateList.get (5));
                    tvButton6.setVisibility (View.VISIBLE);
                    tvButton7.setText (stateList.get (6));
                    tvButton7.setVisibility (View.VISIBLE);
                    tvButton8.setText (stateList.get (7));
                    tvButton8.setVisibility (View.VISIBLE);
                    tvButton9.setText (stateList.get (8));
                    tvButton9.setVisibility (View.VISIBLE);
                    tvButton10.setText (stateList.get (9));
                    tvButton10.setVisibility (View.VISIBLE);
                    tvButton1.setOnClickListener (new View.OnClickListener () {
                        @Override
                        public void onClick (View v) {
                            if (stateSelectedList.contains (stateList.get (0))) {
                                for (int i = 0; i < stateSelectedList.size (); i++) {
                                    if (stateSelectedList.get (i).equalsIgnoreCase (stateList.get (0))) {
                                        stateSelectedList.remove (i);
                                        tvButton1.setBackgroundResource (R.drawable.state_button_unselected);
                                        tvButton1.setTextColor (getResources ().getColor (R.color.app_text_color_dark));
                                    }
                                }
                            } else {
                                stateSelectedList.add (stateList.get (0));
                                tvButton1.setBackgroundResource (R.drawable.state_button_selected);
                                tvButton1.setTextColor (getResources ().getColor (R.color.text_color_white));
                            }
                        }
                    });
                
                    tvButton2.setOnClickListener (new View.OnClickListener () {
                        @Override
                        public void onClick (View v) {
                            if (stateSelectedList.contains (stateList.get (1))) {
                                for (int i = 0; i < stateSelectedList.size (); i++) {
                                    if (stateSelectedList.get (i).equalsIgnoreCase (stateList.get (1))) {
                                        stateSelectedList.remove (i);
                                        tvButton2.setBackgroundResource (R.drawable.state_button_unselected);
                                        tvButton2.setTextColor (getResources ().getColor (R.color.app_text_color_dark));
                                    }
                                }
                            } else {
                                stateSelectedList.add (stateList.get (1));
                                tvButton2.setBackgroundResource (R.drawable.state_button_selected);
                                tvButton2.setTextColor (getResources ().getColor (R.color.text_color_white));
                            }
                        }
                    });
                
                    tvButton3.setOnClickListener (new View.OnClickListener () {
                        @Override
                        public void onClick (View v) {
                            if (stateSelectedList.contains (stateList.get (2))) {
                                for (int i = 0; i < stateSelectedList.size (); i++) {
                                    if (stateSelectedList.get (i).equalsIgnoreCase (stateList.get (2))) {
                                        stateSelectedList.remove (i);
                                        tvButton3.setBackgroundResource (R.drawable.state_button_unselected);
                                        tvButton3.setTextColor (getResources ().getColor (R.color.app_text_color_dark));
                                    }
                                }
                            } else {
                                stateSelectedList.add (stateList.get (2));
                                tvButton3.setBackgroundResource (R.drawable.state_button_selected);
                                tvButton3.setTextColor (getResources ().getColor (R.color.text_color_white));
                            }
                        }
                    });
                
                    tvButton4.setOnClickListener (new View.OnClickListener () {
                        @Override
                        public void onClick (View v) {
                            if (stateSelectedList.contains (stateList.get (3))) {
                                for (int i = 0; i < stateSelectedList.size (); i++) {
                                    if (stateSelectedList.get (i).equalsIgnoreCase (stateList.get (3))) {
                                        stateSelectedList.remove (i);
                                        tvButton4.setBackgroundResource (R.drawable.state_button_unselected);
                                        tvButton4.setTextColor (getResources ().getColor (R.color.app_text_color_dark));
                                    }
                                }
                            } else {
                                stateSelectedList.add (stateList.get (3));
                                tvButton4.setBackgroundResource (R.drawable.state_button_selected);
                                tvButton4.setTextColor (getResources ().getColor (R.color.text_color_white));
                            }
                        }
                    });
                
                    tvButton5.setOnClickListener (new View.OnClickListener () {
                        @Override
                        public void onClick (View v) {
                            if (stateSelectedList.contains (stateList.get (4))) {
                                for (int i = 0; i < stateSelectedList.size (); i++) {
                                    if (stateSelectedList.get (i).equalsIgnoreCase (stateList.get (4))) {
                                        stateSelectedList.remove (i);
                                        tvButton5.setBackgroundResource (R.drawable.state_button_unselected);
                                        tvButton5.setTextColor (getResources ().getColor (R.color.app_text_color_dark));
                                    }
                                }
                            } else {
                                stateSelectedList.add (stateList.get (4));
                                tvButton5.setBackgroundResource (R.drawable.state_button_selected);
                                tvButton5.setTextColor (getResources ().getColor (R.color.text_color_white));
                            }
                        }
                    });
                
                    tvButton6.setOnClickListener (new View.OnClickListener () {
                        @Override
                        public void onClick (View v) {
                            if (stateSelectedList.contains (stateList.get (5))) {
                                for (int i = 0; i < stateSelectedList.size (); i++) {
                                    if (stateSelectedList.get (i).equalsIgnoreCase (stateList.get (5))) {
                                        stateSelectedList.remove (i);
                                        tvButton6.setBackgroundResource (R.drawable.state_button_unselected);
                                        tvButton6.setTextColor (getResources ().getColor (R.color.app_text_color_dark));
                                    }
                                }
                            } else {
                                stateSelectedList.add (stateList.get (5));
                                tvButton6.setBackgroundResource (R.drawable.state_button_selected);
                                tvButton6.setTextColor (getResources ().getColor (R.color.text_color_white));
                            }
                        }
                    });
                
                    tvButton7.setOnClickListener (new View.OnClickListener () {
                        @Override
                        public void onClick (View v) {
                            if (stateSelectedList.contains (stateList.get (6))) {
                                for (int i = 0; i < stateSelectedList.size (); i++) {
                                    if (stateSelectedList.get (i).equalsIgnoreCase (stateList.get (6))) {
                                        stateSelectedList.remove (i);
                                        tvButton7.setBackgroundResource (R.drawable.state_button_unselected);
                                        tvButton7.setTextColor (getResources ().getColor (R.color.app_text_color_dark));
                                    }
                                }
                            } else {
                                stateSelectedList.add (stateList.get (6));
                                tvButton7.setBackgroundResource (R.drawable.state_button_selected);
                                tvButton7.setTextColor (getResources ().getColor (R.color.text_color_white));
                            }
                        }
                    });
                
                    tvButton8.setOnClickListener (new View.OnClickListener () {
                        @Override
                        public void onClick (View v) {
                            if (stateSelectedList.contains (stateList.get (7))) {
                                for (int i = 0; i < stateSelectedList.size (); i++) {
                                    if (stateSelectedList.get (i).equalsIgnoreCase (stateList.get (7))) {
                                        stateSelectedList.remove (i);
                                        tvButton8.setBackgroundResource (R.drawable.state_button_unselected);
                                        tvButton8.setTextColor (getResources ().getColor (R.color.app_text_color_dark));
                                    }
                                }
                            } else {
                                stateSelectedList.add (stateList.get (7));
                                tvButton8.setBackgroundResource (R.drawable.state_button_selected);
                                tvButton8.setTextColor (getResources ().getColor (R.color.text_color_white));
                            }
                        }
                    });
                
                    tvButton9.setOnClickListener (new View.OnClickListener () {
                        @Override
                        public void onClick (View v) {
                            if (stateSelectedList.contains (stateList.get (8))) {
                                for (int i = 0; i < stateSelectedList.size (); i++) {
                                    if (stateSelectedList.get (i).equalsIgnoreCase (stateList.get (8))) {
                                        stateSelectedList.remove (i);
                                        tvButton9.setBackgroundResource (R.drawable.state_button_unselected);
                                        tvButton9.setTextColor (getResources ().getColor (R.color.app_text_color_dark));
                                    }
                                }
                            } else {
                                stateSelectedList.add (stateList.get (8));
                                tvButton9.setBackgroundResource (R.drawable.state_button_selected);
                                tvButton9.setTextColor (getResources ().getColor (R.color.text_color_white));
                            }
                        }
                    });
                
                    tvButton10.setOnClickListener (new View.OnClickListener () {
                        @Override
                        public void onClick (View v) {
                            if (stateSelectedList.contains (stateList.get (9))) {
                                for (int i = 0; i < stateSelectedList.size (); i++) {
                                    if (stateSelectedList.get (i).equalsIgnoreCase (stateList.get (9))) {
                                        stateSelectedList.remove (i);
                                        tvButton10.setBackgroundResource (R.drawable.state_button_unselected);
                                        tvButton10.setTextColor (getResources ().getColor (R.color.app_text_color_dark));
                                    }
                                }
                            } else {
                                stateSelectedList.add (stateList.get (9));
                                tvButton10.setBackgroundResource (R.drawable.state_button_selected);
                                tvButton10.setTextColor (getResources ().getColor (R.color.text_color_white));
                            }
                        }
                    });
                    break;
            }
        }
    }
    
    private void initListener () {
        rlBack.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View v) {
                finish ();
                overridePendingTransition (R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });
        cb1.setOnCheckedChangeListener (new CompoundButton.OnCheckedChangeListener () {
            @Override
            public void onCheckedChanged (CompoundButton compoundButton, boolean b) {
                if (cb1.isChecked ()) {
                    budgetSelectedList.add ("1");
                } else {
                    budgetSelectedList.remove ("1");
                }
            }
        });
        cb2.setOnCheckedChangeListener (new CompoundButton.OnCheckedChangeListener () {
            @Override
            public void onCheckedChanged (CompoundButton compoundButton, boolean b) {
                if (cb2.isChecked ()) {
                    budgetSelectedList.add ("2");
                } else {
                    budgetSelectedList.remove ("2");
                }
            }
        });
        cb3.setOnCheckedChangeListener (new CompoundButton.OnCheckedChangeListener () {
            @Override
            public void onCheckedChanged (CompoundButton compoundButton, boolean b) {
                if (cb3.isChecked ()) {
                    budgetSelectedList.add ("3");
                } else {
                    budgetSelectedList.remove ("3");
                }
            }
        });
        cb4.setOnCheckedChangeListener (new CompoundButton.OnCheckedChangeListener () {
            @Override
            public void onCheckedChanged (CompoundButton compoundButton, boolean b) {
                if (cb4.isChecked ()) {
                    budgetSelectedList.add ("4");
                } else {
                    budgetSelectedList.remove ("4");
                    
                }
            }
        });
        
        spinner.setItems (spinnerItems);
        spinner.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View v) {
                Utils.hideSoftKeyboard (MyProfileActivity.this);
            }
        });
        spinner.setOnItemSelectedListener (new MaterialSpinner.OnItemSelectedListener<String> () {
            @Override
            public void onItemSelected (MaterialSpinner view, int position, long id, String item) {
            }
        });
        etFullName.addTextChangedListener (new TextWatcher () {
            @Override
            public void onTextChanged (CharSequence s, int start, int before, int count) {
                if (count == 0) {
                    etFullName.setError (null);
                }
            }
    
            @Override
            public void beforeTextChanged (CharSequence s, int start, int count, int after) {
            }
    
            @Override
            public void afterTextChanged (Editable s) {
            }
        });
        etEmail.addTextChangedListener (new TextWatcher () {
            @Override
            public void onTextChanged (CharSequence s, int start, int before, int count) {
                if (count == 0) {
                    etEmail.setError (null);
                }
            }
            
            @Override
            public void beforeTextChanged (CharSequence s, int start, int count, int after) {
            }
            
            @Override
            public void afterTextChanged (Editable s) {
            }
        });
        etPhone.addTextChangedListener (new TextWatcher () {
            @Override
            public void onTextChanged (CharSequence s, int start, int before, int count) {
                if (count == 0) {
                    etPhone.setError (null);
                }
            }
            
            @Override
            public void beforeTextChanged (CharSequence s, int start, int count, int after) {
            }
            
            @Override
            public void afterTextChanged (Editable s) {
            }
        });
        tvSave.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View view) {
                String budget_csv = "";
                for (int j = 0; j < budgetSelectedList.size (); j++) {
                    if (j == 0) {
                        budget_csv = budgetSelectedList.get (j);
                    } else {
                        budget_csv = budget_csv + "," + budgetSelectedList.get (j);
                    }
                }
    
                String state_csv = "";
                for (int k = 0; k < stateSelectedList.size (); k++) {
                    if (k == 0) {
                        state_csv = stateSelectedList.get (k);
                    } else {
                        state_csv = state_csv + "," + stateSelectedList.get (k);
                    }
                }
    
                SpannableString s = new SpannableString (getResources ().getString (R.string.please_enter_full_name));
                s.setSpan (new TypefaceSpan (MyProfileActivity.this, Constants.font_name), 0, s.length (), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                SpannableString s2 = new SpannableString (getResources ().getString (R.string.please_enter_email));
                s2.setSpan (new TypefaceSpan (MyProfileActivity.this, Constants.font_name), 0, s2.length (), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                SpannableString s3 = new SpannableString (getResources ().getString (R.string.please_enter_valid_email));
                s3.setSpan (new TypefaceSpan (MyProfileActivity.this, Constants.font_name), 0, s3.length (), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                SpannableString s4 = new SpannableString (getResources ().getString (R.string.please_enter_mobile));
                s4.setSpan (new TypefaceSpan (MyProfileActivity.this, Constants.font_name), 0, s4.length (), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                SpannableString s5 = new SpannableString (getResources ().getString (R.string.please_select_state_type));
                s5.setSpan (new TypefaceSpan (MyProfileActivity.this, Constants.font_name), 0, s5.length (), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                SpannableString s6 = new SpannableString (getResources ().getString (R.string.please_select_home_type));
                s6.setSpan (new TypefaceSpan (MyProfileActivity.this, Constants.font_name), 0, s6.length (), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
    
    
                if (etFullName.getText ().toString ().trim ().length () == 0 && etEmail.getText ().toString ().length () == 0 && etPhone.getText ().toString ().length () == 0) {
                    etFullName.setError (s);
                    etEmail.setError (s2);
                    etPhone.setError (s4);
                } else if (etFullName.getText ().toString ().trim ().length () == 0) {
                    etFullName.setError (s);
                } else if (etEmail.getText ().toString ().trim ().length () == 0) {
                    etEmail.setError (s2);
                } else if (! Utils.isValidEmail1 (etEmail.getText ().toString ())) {
                    etEmail.setError (s3);
                } else if (etPhone.getText ().toString ().trim ().length () == 0) {
                    etPhone.setError (s4);
                } else if (stateSelectedList.size () == 0) {
                    Utils.showSnackBar (MyProfileActivity.this, clMain, s5.toString (), Snackbar.LENGTH_LONG, null, null);
                } else if (spinner.getText ().toString ().trim ().equalsIgnoreCase (spinnerItems[0])) {
                    Utils.showSnackBar (MyProfileActivity.this, clMain, s6.toString (), Snackbar.LENGTH_LONG, null, null);
                } else {
                    sendProfileDetailsToServer (
                            etFullName.getText ().toString ().trim (),
                            etEmail.getText ().toString ().trim (),
                            etPhone.getText ().toString ().trim (),
                            spinner.getText ().toString ().trim (),
                            state_csv, budget_csv);
                }
            }
        });
        
        tvColorado.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View v) {
                if (stateSelectedList.contains ("CO")) {
                    for (int i = 0; i < stateSelectedList.size (); i++) {
                        if (stateSelectedList.get (i).equalsIgnoreCase ("CO")) {
                            stateSelectedList.remove (i);
                            tvColorado.setBackgroundResource (R.drawable.state_button_unselected);
                            tvColorado.setTextColor (getResources ().getColor (R.color.app_text_color_dark));
                        }
                    }
                } else {
                    stateSelectedList.add ("CO");
                    tvColorado.setBackgroundResource (R.drawable.state_button_selected);
                    tvColorado.setTextColor (getResources ().getColor (R.color.text_color_white));
                }
            }
        });
        
        tvSeattle.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View v) {
                if (stateSelectedList.contains ("WA")) {
                    for (int i = 0; i < stateSelectedList.size (); i++) {
                        if (stateSelectedList.get (i).equalsIgnoreCase ("WA")) {
                            stateSelectedList.remove (i);
                            tvSeattle.setBackgroundResource (R.drawable.state_button_unselected);
                            tvSeattle.setTextColor (getResources ().getColor (R.color.app_text_color_dark));
                        }
                    }
                } else {
                    stateSelectedList.add ("WA");
                    tvSeattle.setBackgroundResource (R.drawable.state_button_selected);
                    tvSeattle.setTextColor (getResources ().getColor (R.color.text_color_white));
                }
            }
        });
    }
    
    private void sendProfileDetailsToServer (final String name, final String email, final String mobile, final String homeType, final String state, final String budget) {
        if (NetworkConnection.isNetworkAvailable (MyProfileActivity.this)) {
            Utils.showProgressDialog (progressDialog, getResources ().getString (R.string.progress_dialog_text_please_wait), true);
            Utils.showLog (Log.INFO, "" + AppConfigTags.URL, AppConfigURL.URL_EDIT_PROFILE, true);
            StringRequest strRequest1 = new StringRequest (Request.Method.POST, AppConfigURL.URL_EDIT_PROFILE,
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
                                        buyerDetailsPref.putStringPref (MyProfileActivity.this, BuyerDetailsPref.BUYER_NAME, jsonObj.getString (AppConfigTags.BUYER_NAME));
                                        buyerDetailsPref.putStringPref (MyProfileActivity.this, BuyerDetailsPref.BUYER_MOBILE, jsonObj.getString (AppConfigTags.BUYER_MOBILE));
                                        buyerDetailsPref.putStringPref (MyProfileActivity.this, BuyerDetailsPref.PROFILE_HOME_TYPE, jsonObj.getString (AppConfigTags.PROFILE_HOME_TYPE));
                                        buyerDetailsPref.putStringPref (MyProfileActivity.this, BuyerDetailsPref.PROFILE_STATE, jsonObj.getString (AppConfigTags.PROFILE_STATE));
                                        buyerDetailsPref.putStringPref (MyProfileActivity.this, BuyerDetailsPref.PROFILE_PRICE_RANGE, jsonObj.getString (AppConfigTags.PROFILE_PRICE_RANGE));
                                        buyerDetailsPref.putIntPref (MyProfileActivity.this, BuyerDetailsPref.PROFILE_STATUS, jsonObj.getInt (AppConfigTags.PROFILE_STATUS));
                                        Utils.showSnackBar (MyProfileActivity.this, clMain, message, Snackbar.LENGTH_LONG, null, null);
                                        final Handler handler = new Handler ();
                                        handler.postDelayed (new Runnable () {
                                            @Override
                                            public void run () {
                                                finish ();
                                            }
                                        }, 1000);
                                    } else {
                                        Utils.showSnackBar (MyProfileActivity.this, clMain, message, Snackbar.LENGTH_LONG, null, null);
                                    }
                                    progressDialog.dismiss ();
                                } catch (Exception e) {
                                    progressDialog.dismiss ();
                                    Utils.showSnackBar (MyProfileActivity.this, clMain, getResources ().getString (R.string.snackbar_text_exception_occurred), Snackbar.LENGTH_LONG, getResources ().getString (R.string.snackbar_action_dismiss), null);
                                    e.printStackTrace ();
                                }
                            } else {
                                Utils.showSnackBar (MyProfileActivity.this, clMain, getResources ().getString (R.string.snackbar_text_error_occurred), Snackbar.LENGTH_LONG, getResources ().getString (R.string.snackbar_action_dismiss), null);
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
                            Utils.showSnackBar (MyProfileActivity.this, clMain, getResources ().getString (R.string.snackbar_text_error_occurred), Snackbar.LENGTH_LONG, getResources ().getString (R.string.snackbar_action_dismiss), null);
                        }
                    }) {
                @Override
                protected Map<String, String> getParams () throws AuthFailureError {
                    Map<String, String> params = new Hashtable<String, String> ();
                    params.put (AppConfigTags.TYPE, "edit_profile");
                    params.put (AppConfigTags.BUYER_ID, String.valueOf (buyerDetailsPref.getIntPref (MyProfileActivity.this, BuyerDetailsPref.BUYER_ID)));
                    params.put (AppConfigTags.BUYER_NAME, name);
                    params.put (AppConfigTags.BUYER_EMAIL, email);
                    params.put (AppConfigTags.BUYER_MOBILE, mobile);
                    params.put (AppConfigTags.PROFILE_STATE, state);
                    params.put (AppConfigTags.PROFILE_PRICE_RANGE, budget);
                    params.put (AppConfigTags.PROFILE_HOME_TYPE, homeType);
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
    
    @Override
    public void onBackPressed () {
        finish ();
        overridePendingTransition (R.anim.slide_in_left, R.anim.slide_out_right);
    }
}