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
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.actiknow.clearsale.R;
import com.actiknow.clearsale.utils.AppConfigTags;
import com.actiknow.clearsale.utils.AppConfigURL;
import com.actiknow.clearsale.utils.Constants;
import com.actiknow.clearsale.utils.NetworkConnection;
import com.actiknow.clearsale.utils.TypefaceSpan;
import com.actiknow.clearsale.utils.Utils;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

/**
 * Created by l on 22/03/2017.
 */

public class ContactUsActivity extends AppCompatActivity {
    EditText etName;
    EditText etEmail;
    EditText etMobile;
    EditText etMessage;
    TextView tvSubmit;
    RelativeLayout rlBack;
    CoordinatorLayout clMain;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView (R.layout.activity_contact_us);
        initView();
        initData();
        initListener();
    }

    private void initView() {
        etName = (EditText) findViewById(R.id.etUserName);
        etEmail = (EditText) findViewById(R.id.etEmail);
        etMobile = (EditText) findViewById(R.id.etPhone);
        etMessage = (EditText) findViewById(R.id.etMessage);
        tvSubmit = (TextView) findViewById(R.id.tvSubmit);
        clMain = (CoordinatorLayout) findViewById(R.id.clMain);
        rlBack = (RelativeLayout) findViewById (R.id.rlBack);
        Utils.setTypefaceToAllViews(this, tvSubmit);
    }

    private void initData() {
        progressDialog = new ProgressDialog(ContactUsActivity.this);
    }

    private void initListener() {
        tvSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SpannableString s = new SpannableString(getResources().getString(R.string.please_enter_name));
                s.setSpan(new TypefaceSpan(ContactUsActivity.this, Constants.font_name), 0, s.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                SpannableString s2 = new SpannableString(getResources().getString(R.string.please_enter_email));
                s2.setSpan(new TypefaceSpan(ContactUsActivity.this, Constants.font_name), 0, s2.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                SpannableString s3 = new SpannableString(getResources().getString(R.string.please_enter_mobile));
                s3.setSpan(new TypefaceSpan(ContactUsActivity.this, Constants.font_name), 0, s3.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                SpannableString s6 = new SpannableString(getResources().getString(R.string.please_enter_valid_email));
                s6.setSpan(new TypefaceSpan(ContactUsActivity.this, Constants.font_name), 0, s6.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                SpannableString s7 = new SpannableString(getResources().getString(R.string.please_enter_message));
                s7.setSpan(new TypefaceSpan(ContactUsActivity.this, Constants.font_name), 0, s7.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
    
                if (etName.getText().toString().trim().length() == 0 && etEmail.getText().toString().length() == 0 && etMobile.getText().toString().length() == 0 && etMessage.getText().toString().length() == 0) {
                    etName.setError(s);
                    etEmail.setError(s2);
                    etMobile.setError(s3);
                    etMessage.setError(s7);
                } else if (etName.getText().toString().trim().length() == 0) {
                    etName.setError(s);
                } else if (etEmail.getText().toString().trim().length() == 0) {
                    etEmail.setError(s2);
                } else if (!Utils.isValidEmail1(etEmail.getText().toString())) {
                    etEmail.setError(s6);
                } else if (etMobile.getText().toString().trim().length() == 0) {
                    etMobile.setError(s3);
                } else if (etMessage.getText().toString().trim().length() == 0) {
                    etMessage.setError(s7);
                } else {
                    contactDetailsSendToServer(etName.getText().toString().trim(), etEmail.getText().toString().trim(), etMobile.getText().toString().trim(), etMessage.getText().toString().trim());
                }
            }

        });


        etName.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (count == 0) {
                    etName.setError(null);
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
        etMobile.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (count == 0) {
                    etMobile.setError(null);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        etMessage.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (count == 0) {
                    etMessage.setError(null);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        rlBack.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });
    }

    private void contactDetailsSendToServer(final String name, final String email, final String number, final String message) {
        if (NetworkConnection.isNetworkAvailable(ContactUsActivity.this)) {
            Utils.showProgressDialog(progressDialog, getResources().getString(R.string.progress_dialog_text_please_wait), true);
            Utils.showLog (Log.INFO, "" + AppConfigTags.URL, AppConfigURL.URL_CONTACT_US, true);
            StringRequest strRequest1 = new StringRequest (Request.Method.POST, AppConfigURL.URL_CONTACT_US,
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
                                        Utils.showSnackBar (ContactUsActivity.this, clMain, message, Snackbar.LENGTH_LONG, null, null);
                                        final Handler handler = new Handler ();
                                        handler.postDelayed (new Runnable () {
                                            @Override
                                            public void run () {
                                                finish ();
                                            }
                                        }, 1000);
                                    } else {
                                        Utils.showSnackBar(ContactUsActivity.this, clMain, message, Snackbar.LENGTH_LONG, null, null);
                                    }
                                    progressDialog.dismiss();
                                } catch (Exception e) {
                                    progressDialog.dismiss();
                                    Utils.showSnackBar(ContactUsActivity.this, clMain, getResources().getString(R.string.snackbar_text_exception_occurred), Snackbar.LENGTH_LONG, getResources().getString(R.string.snackbar_action_dismiss), null);
                                    e.printStackTrace();
                                }
                            } else {
                                Utils.showSnackBar(ContactUsActivity.this, clMain, getResources().getString(R.string.snackbar_text_error_occurred), Snackbar.LENGTH_LONG, getResources().getString(R.string.snackbar_action_dismiss), null);
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
                            Utils.showSnackBar(ContactUsActivity.this, clMain, getResources().getString(R.string.snackbar_text_error_occurred), Snackbar.LENGTH_LONG, getResources().getString(R.string.snackbar_action_dismiss), null);
                        }
                    }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new Hashtable<String, String>();
                    params.put (AppConfigTags.NAME, name);
                    params.put (AppConfigTags.EMAIL, email);
                    params.put (AppConfigTags.MOBILE, number);
                    params.put (AppConfigTags.MESSAGE, message);
                    params.put (AppConfigTags.TYPE, "contact_us");
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
    
    @Override
    public void onBackPressed () {
        finish ();
        overridePendingTransition (R.anim.slide_in_left, R.anim.slide_out_right);
    }
    
}




