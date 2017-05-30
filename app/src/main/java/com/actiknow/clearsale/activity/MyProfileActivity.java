package com.actiknow.clearsale.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutCompat;
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
import android.widget.Toast;

import com.actiknow.clearsale.R;
import com.actiknow.clearsale.utils.AppConfigTags;
import com.actiknow.clearsale.utils.AppConfigURL;
import com.actiknow.clearsale.utils.BuyerDetailsPref;
import com.actiknow.clearsale.utils.Constants;
import com.actiknow.clearsale.utils.NetworkConnection;
import com.actiknow.clearsale.utils.TypefaceSpan;
import com.actiknow.clearsale.utils.Utils;
import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.jaredrummler.materialspinner.MaterialSpinner;

import org.json.JSONArray;
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
    TextView textViews[];
    List<String> stateCheckList = new ArrayList();
    List<String> budgetCheckList = new ArrayList();
    int numChecked = 0;
    String spinnerSelectItem;
    ProgressDialog progressDialog;
    String name;
    String state;
    String stateResponse;
    String budget;
    String[] spinnerItems;
    RelativeLayout rlBack;
    TextView tvSave;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView (R.layout.activity_my_profile);
        initView();
        initData();
        initListener();
//        getStateListFromServer();
        getPreferencesData();
    }

    private void getPreferencesData() {
        etFullName.setText (buyerDetailsPref.getStringPref (MyProfileActivity.this, BuyerDetailsPref.BUYER_NAME).trim ());
        etEmail.setText (buyerDetailsPref.getStringPref (MyProfileActivity.this, BuyerDetailsPref.BUYER_EMAIL));
        etPhone.setText (buyerDetailsPref.getStringPref (MyProfileActivity.this, BuyerDetailsPref.BUYER_MOBILE));
    
        String stateName[] = buyerDetailsPref.getStringPref (MyProfileActivity.this, BuyerDetailsPref.PROFILE_STATE).trim ().split (",");
    
        if (buyerDetailsPref.getStringPref (MyProfileActivity.this, BuyerDetailsPref.PROFILE_HOME_TYPE).equalsIgnoreCase (spinnerItems[1]))
            spinner.setSelectedIndex(1);
        else if (buyerDetailsPref.getStringPref (MyProfileActivity.this, BuyerDetailsPref.PROFILE_HOME_TYPE).equalsIgnoreCase (spinnerItems[2]))
            spinner.setSelectedIndex(2);
        else if (buyerDetailsPref.getStringPref (MyProfileActivity.this, BuyerDetailsPref.PROFILE_HOME_TYPE).equalsIgnoreCase (spinnerItems[3]))
            spinner.setSelectedIndex(3);
        if (buyerDetailsPref.getStringPref (MyProfileActivity.this, BuyerDetailsPref.PROFILE_HOME_TYPE).equalsIgnoreCase (spinnerItems[4]))
            spinner.setSelectedIndex(4);
    }

    private void initView() {
        etFullName = (EditText) findViewById (R.id.etFullName);
        etEmail = (EditText) findViewById(R.id.etEmail);
        etPhone = (EditText) findViewById(R.id.etPhone);
        rlBack = (RelativeLayout) findViewById (R.id.rlBack);
        tvSave = (TextView) findViewById (R.id.tvSave);
        cb1 = (CheckBox) findViewById(R.id.cb1);
        cb2 = (CheckBox) findViewById(R.id.cb2);
        cb3 = (CheckBox) findViewById(R.id.cb3);
        cb4 = (CheckBox) findViewById(R.id.cb4);
        spinner = (MaterialSpinner) findViewById(R.id.spinner);
        llState = (LinearLayout) findViewById(R.id.llState);
        clMain = (CoordinatorLayout) findViewById(R.id.clMain);
        Utils.setTypefaceToAllViews (this, tvSave);
    }

    private void initData() {
        progressDialog = new ProgressDialog (MyProfileActivity.this);
        buyerDetailsPref = BuyerDetailsPref.getInstance ();
        spinnerItems = new String[]{
                "Select Home Type",
                "Cosmetic only",
                "Bath and kitchens plus all of the above",
                "Whole house remodel include moving walls plus all of the above",
                "Structural work plus all of above",
        };
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this, R.layout.spinner, spinnerItems);
        spinnerArrayAdapter.setDropDownViewResource(R.layout.spinner);
        spinner.setAdapter(spinnerArrayAdapter);
    
    
        List<String> stateList = new ArrayList<String> ();
        stateList.add ("CO");
        stateList.add ("WA");
    
    
        for (int i = 0; i < stateList.size (); i++) {
            LinearLayoutCompat.LayoutParams lparams = new LinearLayoutCompat.LayoutParams (
                    LinearLayoutCompat.LayoutParams.WRAP_CONTENT, LinearLayoutCompat.LayoutParams.WRAP_CONTENT);
            final TextView tvState2 = new TextView (MyProfileActivity.this);
            tvState2.setText (stateList.get (i));
            tvState2.setId (i);
            tvState2.setPadding (30, 30, 30, 30);
            tvState2.setLayoutParams (lparams);
            llState.addView (tvState2);
            tvState2.setBackgroundResource (R.drawable.state_button_unselected);
            tvState2.setTextColor (Color.BLACK);
        }
    }

    private void initListener() {
        rlBack.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });
        cb1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (cb1.isChecked()) {
                    budgetCheckList.add(cb1.getText().toString());
                } else {
                    budgetCheckList.remove(cb1.getText().toString());
                }
            }
        });
        cb2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (cb2.isChecked()) {
                    budgetCheckList.add(cb2.getText().toString());
                } else {
                    budgetCheckList.remove(cb2.getText().toString());
                }
            }
        });
        cb3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (cb3.isChecked()) {
                    budgetCheckList.add(cb3.getText().toString());
                } else {
                    budgetCheckList.remove(cb3.getText().toString());
                }
            }
        });
        cb4.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (cb4.isChecked()) {
                    budgetCheckList.add(cb4.getText().toString());
                } else {
                    budgetCheckList.remove(cb4.getText().toString());

                }
            }
        });

        spinner.setItems(spinnerItems);
        spinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, String item) {
            }
        });
    
    
        etFullName.addTextChangedListener (new TextWatcher () {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (count == 0) {
                    etFullName.setError (null);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        etEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (count == 0) {
                    etEmail.setError(null);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        etPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (count == 0) {
                    etPhone.setError(null);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    
    
        tvSave.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick(View view) {
                for (int i = 0; i < stateCheckList.size(); i++) {
                    if (i == 0) {
                        state = stateCheckList.get(i);
                    } else {
                        state = state + "," + stateCheckList.get(i);
                    }
                }
                for (int j = 0; j < budgetCheckList.size(); j++) {
                    if (j == 0) {
                        budget = budgetCheckList.get(j);
                    } else {
                        budget = budget + "," + budgetCheckList.get(j);
                    }
                }
    
                name = etFullName.getText ().toString ().trim ();
                SpannableString s = new SpannableString (getResources ().getString (R.string.please_enter_full_name));
                s.setSpan (new TypefaceSpan (MyProfileActivity.this, Constants.font_name), 0, s.length (), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                SpannableString s2 = new SpannableString(getResources().getString(R.string.please_enter_email));
                s2.setSpan (new TypefaceSpan (MyProfileActivity.this, Constants.font_name), 0, s2.length (), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                SpannableString s3 = new SpannableString(getResources().getString(R.string.please_enter_valid_email));
                s3.setSpan (new TypefaceSpan (MyProfileActivity.this, Constants.font_name), 0, s3.length (), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                SpannableString s4 = new SpannableString(getResources().getString(R.string.please_enter_mobile));
                s4.setSpan (new TypefaceSpan (MyProfileActivity.this, Constants.font_name), 0, s4.length (), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                SpannableString s5 = new SpannableString(getResources().getString(R.string.please_select_state_type));
                s5.setSpan (new TypefaceSpan (MyProfileActivity.this, Constants.font_name), 0, s5.length (), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                SpannableString s6 = new SpannableString(getResources().getString(R.string.please_select_home_type));
                s6.setSpan (new TypefaceSpan (MyProfileActivity.this, Constants.font_name), 0, s6.length (), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
    
    
                if (etFullName.getText ().toString ().trim ().length () == 0 && etEmail.getText ().toString ().length () == 0 && etPhone.getText ().toString ().length () == 0) {
                    etFullName.setError (s);
                    etEmail.setError(s2);
                    etPhone.setError(s4);
                } else if (etFullName.getText ().toString ().trim ().length () == 0) {
                    etFullName.setError (s);
                } else if (etEmail.getText().toString().trim().length() == 0) {
                    etEmail.setError(s2);
                } else if (!Utils.isValidEmail1(etEmail.getText().toString())) {
                    etEmail.setError(s3);
                } else if (etPhone.getText().toString().trim().length() == 0) {
                    etPhone.setError(s4);
                } else if (numChecked < 1) {
                    Toast.makeText (MyProfileActivity.this, s5, Toast.LENGTH_LONG).show ();
                } else if (spinner.getText().toString().trim().equalsIgnoreCase("Select Home Type")) {
                    Toast.makeText (MyProfileActivity.this, s6, Toast.LENGTH_LONG).show ();
                } else {
                    editProfileDetailSendToServer(name, etEmail.getText().toString().trim(), etPhone.getText().toString().trim(), spinner.getText().toString().trim(), state, budget);
                }
            }
        });

    }

    private void editProfileDetailSendToServer(final String name, final String email, final String mobile, final String homeType, final String state, final String budget) {
        if (NetworkConnection.isNetworkAvailable (MyProfileActivity.this)) {
            Utils.showProgressDialog(progressDialog, getResources().getString(R.string.progress_dialog_text_please_wait), true);
            Utils.showLog(Log.INFO, "" + AppConfigTags.URL, AppConfigURL.URL_EDIT_PROFILE, true);
            StringRequest strRequest1 = new StringRequest(Request.Method.POST, AppConfigURL.URL_EDIT_PROFILE,
                    new com.android.volley.Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Utils.showLog (Log.INFO, AppConfigTags.SERVER_RESPONSE, response, true);
                            if (response != null) {
                                try {
                                    JSONObject jsonObj = new JSONObject(response);
                                    boolean error = jsonObj.getBoolean(AppConfigTags.ERROR);
                                    String message = jsonObj.getString(AppConfigTags.MESSAGE);
                                    if (!error) {
                                        Toast.makeText (MyProfileActivity.this, message, Toast.LENGTH_LONG).show ();

                                        JSONArray jsonArray = jsonObj.getJSONArray(AppConfigTags.STATE);
                                        for (int i = 0; i < jsonArray.length(); i++) {
                                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                                            if (i == 0) {
                                                stateResponse = jsonObject.getString(AppConfigTags.STATE_TYPE);
                                            } else {
                                                stateResponse = stateResponse + "," + jsonObject.getString(AppConfigTags.STATE_TYPE);
                                            }
                                            Log.e("stateResponse", stateResponse);
                                        }
    
                                        buyerDetailsPref.putStringPref (MyProfileActivity.this, BuyerDetailsPref.BUYER_NAME, jsonObj.getString (AppConfigTags.SIGN_IN_USER_NAME));
                                        buyerDetailsPref.putStringPref (MyProfileActivity.this, BuyerDetailsPref.BUYER_EMAIL, jsonObj.getString (AppConfigTags.SIGN_IN_USER_EMAIL));
                                        buyerDetailsPref.putStringPref (MyProfileActivity.this, BuyerDetailsPref.BUYER_MOBILE, jsonObj.getString (AppConfigTags.SIGN_IN_USER_MOBILE));
                                        buyerDetailsPref.putStringPref (MyProfileActivity.this, BuyerDetailsPref.PROFILE_HOME_TYPE, jsonObj.getString (AppConfigTags.HOME_TYPE));
                                        buyerDetailsPref.putStringPref (MyProfileActivity.this, BuyerDetailsPref.PROFILE_STATE, stateResponse);
                                        buyerDetailsPref.putStringPref (MyProfileActivity.this, BuyerDetailsPref.PROFILE_HOME_BUDGET, jsonObj.getString (AppConfigTags.HOME_BUDGET));


                                    } else {
                                        Utils.showSnackBar (MyProfileActivity.this, clMain, message, Snackbar.LENGTH_LONG, null, null);
                                    }
                                    progressDialog.dismiss();
                                } catch (Exception e) {
                                    progressDialog.dismiss();
                                    Utils.showSnackBar (MyProfileActivity.this, clMain, getResources ().getString (R.string.snackbar_text_exception_occurred), Snackbar.LENGTH_LONG, getResources ().getString (R.string.snackbar_action_dismiss), null);
                                    e.printStackTrace();
                                }
                            } else {
                                Utils.showSnackBar (MyProfileActivity.this, clMain, getResources ().getString (R.string.snackbar_text_error_occurred), Snackbar.LENGTH_LONG, getResources ().getString (R.string.snackbar_action_dismiss), null);
                                Utils.showLog(Log.WARN, AppConfigTags.SERVER_RESPONSE, AppConfigTags.DIDNT_RECEIVE_ANY_DATA_FROM_SERVER, true);
                            }
                            progressDialog.dismiss();
                        }
                    },
                    new com.android.volley.Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            progressDialog.dismiss();
                            Utils.showLog(Log.ERROR, AppConfigTags.VOLLEY_ERROR, error.toString(), true);
                            Utils.showSnackBar (MyProfileActivity.this, clMain, getResources ().getString (R.string.snackbar_text_error_occurred), Snackbar.LENGTH_LONG, getResources ().getString (R.string.snackbar_action_dismiss), null);
                        }
                    }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new Hashtable<String, String>();
                    params.put(AppConfigTags.NAME, name);
                    params.put(AppConfigTags.EMAIL, email);
                    params.put(AppConfigTags.NUMBER, mobile);
                    params.put(AppConfigTags.STATE_TYPE, state);
                    params.put(AppConfigTags.HOME_BUDGET, budget);
                    params.put (AppConfigTags.USER_ID, String.valueOf (buyerDetailsPref.getIntPref (MyProfileActivity.this, BuyerDetailsPref.BUYER_ID)));
                    params.put(AppConfigTags.HOME_TYPE, homeType);
                    params.put(AppConfigTags.TYPE, "edit_profile");
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

    private void getStateListFromServer() {
        if (NetworkConnection.isNetworkAvailable(this)) {
            Utils.showLog(Log.INFO, AppConfigTags.URL, AppConfigURL.URL_STATE, true);
            StringRequest strRequest = new StringRequest(Request.Method.POST, AppConfigURL.URL_STATE,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Utils.showLog(Log.INFO, AppConfigTags.SERVER_RESPONSE, response, true);
                            if (response != null) {
                                try {
                                    JSONObject jsonObj = new JSONObject(response);
                                    boolean error = jsonObj.getBoolean(AppConfigTags.ERROR);
                                    String message = jsonObj.getString(AppConfigTags.MESSAGE);
                                    if (!error) {
                                        JSONArray jsonArray = jsonObj.getJSONArray (AppConfigTags.STATES);
                                        int size = jsonArray.length();
                                        textViews = new TextView[size];
                                        for (int i = 0; i < jsonArray.length(); i++) {
                                            JSONObject jsonObject = jsonArray.getJSONObject(i);


//                                            LinearLayoutCompat.LayoutParams lparams = new LinearLayoutCompat.LayoutParams (
//                                                    LinearLayoutCompat.LayoutParams.WRAP_CONTENT, LinearLayoutCompat.LayoutParams.WRAP_CONTENT);
//                                            final TextView tvState2 = new TextView (MyProfileActivity.this);
//                                            tvState2.setText (jsonObject.getString (AppConfigTags.STATE_NAME));
//                                            tvState2.setId (i);
//                                            tvState2.setPadding (30, 30, 30, 30);


//                                            tvState2.setLayoutParams (lparams);
//                                            llState.addView (tvState2);
//                                            tvState2.setBackgroundResource (R.drawable.state_button_unselected);
//                                            tvState2.setTextColor (Color.BLACK);
                                            
                                            
                                            
                                            
  /*
                                            LinearLayoutCompat.LayoutParams lparams = new LinearLayoutCompat.LayoutParams(
                                                    LinearLayoutCompat.LayoutParams.WRAP_CONTENT, LinearLayoutCompat.LayoutParams.WRAP_CONTENT);
                                            textViews[i] = new TextView (MyProfileActivity.this);
                                            textViews[i].setText(jsonObject.getString(AppConfigTags.STATE_NAME));
                                            textViews[i].setId(i);
                                            lparams.weight = 1f;
                                            textViews[i].setLayoutParams(lparams);
                                            llState.addView(textViews[i]);
*/
  /*
                                            textViews[i].setOnClickListener ((new CompoundButton.OnCheckedChangeListener() {
                                                @Override
                                                public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                                                    if (checked) {
                                                        numChecked++;
                                                        stateCheckList.add(compoundButton.getText().toString());
                                                        System.out.println("add=" + stateCheckList);
                                                    } else {
                                                        numChecked--;
                                                        stateCheckList.remove(compoundButton.getText().toString());
                                                        System.out.println("RemoveCHECK=" + stateCheckList);

                                                    }
                                                }
                                            });

*/
                                        }


                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            } else {
                                Utils.showLog(Log.WARN, AppConfigTags.SERVER_RESPONSE, AppConfigTags.DIDNT_RECEIVE_ANY_DATA_FROM_SERVER, true);
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Utils.showLog(Log.ERROR, AppConfigTags.VOLLEY_ERROR, error.toString(), true);
                            NetworkResponse response = error.networkResponse;
                            if (response != null && response.data != null) {
                                Utils.showLog(Log.ERROR, AppConfigTags.ERROR, new String(response.data), true);

                            }
                        }
                    }) {


                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new Hashtable<String, String>();
                    params.put (AppConfigTags.TYPE, AppConfigTags.STATES);

                    Utils.showLog(Log.INFO, AppConfigTags.PARAMETERS_SENT_TO_THE_SERVER, "" + params, true);
                    return params;
                }

                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    Utils.showLog(Log.INFO, AppConfigTags.HEADERS_SENT_TO_THE_SERVER, "" + params, false);
                    return params;
                }
            };
            Utils.sendRequest(strRequest, 30);
        } else {
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
    
    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}