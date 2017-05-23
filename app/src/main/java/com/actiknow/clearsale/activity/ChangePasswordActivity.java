package com.actiknow.clearsale.activity;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.actiknow.clearsale.R;
import com.actiknow.clearsale.utils.AppConfigTags;
import com.actiknow.clearsale.utils.AppConfigURL;
import com.actiknow.clearsale.utils.Constants;
import com.actiknow.clearsale.utils.NetworkConnection;
import com.actiknow.clearsale.utils.TypefaceSpan;
import com.actiknow.clearsale.utils.UserDetailsPref;
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

public class ChangePasswordActivity extends AppCompatActivity {
    Toolbar toolbar;
    AppBarLayout appBar;
    EditText etOldPassword;
    EditText etNewPassword;
    EditText etConfirmPassword;

    TextView tvUpdate;
    ImageView ivBack;

    CoordinatorLayout clMain;
    ProgressDialog progressDialog;
    UserDetailsPref userDetailsPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        initView();
        initData();
        initListener();

    }

    private void initView() {
        appBar = (AppBarLayout) findViewById(R.id.appbar);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        etOldPassword = (EditText) findViewById(R.id.etOldPassword);
        etNewPassword = (EditText) findViewById(R.id.etNewPassword);
        etConfirmPassword = (EditText) findViewById(R.id.etConfirmPassword);
        tvUpdate = (TextView) findViewById(R.id.tvSubmit);
        clMain = (CoordinatorLayout) findViewById(R.id.clMain);
        ivBack = (ImageView) findViewById(R.id.ivBack);
        Utils.setTypefaceToAllViews(this, tvUpdate);

    }

    private void initData() {
        userDetailsPref = UserDetailsPref.getInstance();
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        progressDialog = new ProgressDialog(ChangePasswordActivity.this);
        //   actionBar.setDisplayHomeAsUpEnabled(true);

        appBar.setExpanded(true);

    }

    private void initListener() {
        tvUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SpannableString s1 = new SpannableString(getResources().getString(R.string.please_enter_old_password));
                s1.setSpan(new TypefaceSpan(ChangePasswordActivity.this, Constants.font_name), 0, s1.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                SpannableString s2 = new SpannableString(getResources().getString(R.string.please_enter_new_password));
                s2.setSpan(new TypefaceSpan(ChangePasswordActivity.this, Constants.font_name), 0, s2.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                SpannableString s3 = new SpannableString(getResources().getString(R.string.please_enter_confirm_password));
                s3.setSpan(new TypefaceSpan(ChangePasswordActivity.this, Constants.font_name), 0, s3.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                SpannableString s4 = new SpannableString(getResources().getString(R.string.password_did_not_match));
                s4.setSpan(new TypefaceSpan(ChangePasswordActivity.this, Constants.font_name), 0, s4.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);


                if (etOldPassword.getText().toString().trim().length() == 0 && etNewPassword.getText().toString().trim().length() == 0 && etConfirmPassword.getText().toString().trim().length() == 0) {
                    etOldPassword.setError(s1);
                    etNewPassword.setError(s2);
                    etConfirmPassword.setError(s3);
                } else if (etNewPassword.getText().toString().trim().equalsIgnoreCase(etConfirmPassword.getText().toString().trim())) {
                    etConfirmPassword.setError(s4);
                } else {
                    changePasswordSendToServer(etOldPassword.getText().toString().trim(), etNewPassword.getText().toString().trim());
                }
            }

        });


        etOldPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (count == 0) {
                    etOldPassword.setError(null);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        etNewPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (count == 0) {
                    etNewPassword.setError(null);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        etConfirmPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (count == 0) {
                    etConfirmPassword.setError(null);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });


        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });
    }


    private void changePasswordSendToServer(final String oldPassword, final String newPassword) {
        if (NetworkConnection.isNetworkAvailable(ChangePasswordActivity.this)) {
            Utils.showProgressDialog(progressDialog, getResources().getString(R.string.progress_dialog_text_please_wait), true);
            Utils.showLog(Log.INFO, "" + AppConfigTags.URL, AppConfigURL.URL_CHANGE_PASSWORD, true);
            StringRequest strRequest1 = new StringRequest(Request.Method.POST, AppConfigURL.URL_CHANGE_PASSWORD,
                    new com.android.volley.Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Utils.showLog(Log.INFO, "rahul" + AppConfigTags.SERVER_RESPONSE, response, true);
                            if (response != null) {
                                try {
                                    JSONObject jsonObj = new JSONObject(response);
                                    boolean error = jsonObj.getBoolean(AppConfigTags.ERROR);
                                    String message = jsonObj.getString(AppConfigTags.MESSAGE);
                                    if (!error) {

                                        Toast.makeText(ChangePasswordActivity.this, message, Toast.LENGTH_LONG).show();
                                        finish();


                                    } else {
                                        Utils.showSnackBar(ChangePasswordActivity.this, clMain, message, Snackbar.LENGTH_LONG, null, null);
                                    }
                                    progressDialog.dismiss();
                                } catch (Exception e) {
                                    progressDialog.dismiss();
                                    Utils.showSnackBar(ChangePasswordActivity.this, clMain, getResources().getString(R.string.snackbar_text_exception_occurred), Snackbar.LENGTH_LONG, getResources().getString(R.string.snackbar_action_dismiss), null);
                                    e.printStackTrace();
                                }
                            } else {
                                Utils.showSnackBar(ChangePasswordActivity.this, clMain, getResources().getString(R.string.snackbar_text_error_occurred), Snackbar.LENGTH_LONG, getResources().getString(R.string.snackbar_action_dismiss), null);
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
                            Utils.showSnackBar(ChangePasswordActivity.this, clMain, getResources().getString(R.string.snackbar_text_error_occurred), Snackbar.LENGTH_LONG, getResources().getString(R.string.snackbar_action_dismiss), null);
                        }
                    }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new Hashtable<String, String>();
                    params.put(AppConfigTags.PASSWORD, oldPassword);
                    params.put(AppConfigTags.NEW_PASSWORD, newPassword);
                    params.put(AppConfigTags.USER_ID, String.valueOf(userDetailsPref.getIntPref(ChangePasswordActivity.this, UserDetailsPref.USER_ID)));
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


}




