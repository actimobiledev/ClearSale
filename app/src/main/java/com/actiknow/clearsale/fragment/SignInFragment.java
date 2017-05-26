package com.actiknow.clearsale.fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.actiknow.clearsale.R;
import com.actiknow.clearsale.activity.ForgotPasswordActivity;
import com.actiknow.clearsale.activity.MainActivity;
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
 * Created by l on 24/02/2017.
 */

public class SignInFragment extends Fragment {

    private static final String TAG = "facebook_login";
    private static final String host = "api.linkedin.com";
    private static final String url = "https://" + host + "/v1/people/~:" + "(email-address,formatted-name,phone-numbers,picture-urls::(original))";
    EditText etEmail;
    EditText etPassword;
    TextView tvForgotPassword;
    TextView tvSignIn;
    CoordinatorLayout clMain;
    ProgressDialog progressDialog;
    UserDetailsPref userDetailsPref;
    PackageInfo info;
    
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_sign_in, container, false);
        initView(rootView);
        initData();
        initListener();
        return rootView;

    }

    private void initView(View rootView) {
        etEmail = (EditText) rootView.findViewById(R.id.etEmail);
        etPassword = (EditText) rootView.findViewById(R.id.etPassword);
        tvSignIn = (TextView) rootView.findViewById(R.id.tvSignIn);
        tvForgotPassword = (TextView) rootView.findViewById(R.id.tvForgotPassword);
        clMain = (CoordinatorLayout) rootView.findViewById(R.id.clMain);
        Utils.setTypefaceToAllViews(getActivity(), tvSignIn);
    }

    private void initData() {
        userDetailsPref = UserDetailsPref.getInstance();
        progressDialog = new ProgressDialog(getActivity());
    }



    private void initListener() {
        tvForgotPassword.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    tvForgotPassword.setTextColor(getResources().getColor(R.color.text_color_grey_dark));
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    tvForgotPassword.setTextColor(getResources().getColor(R.color.colorPrimary));
                    Intent intent = new Intent(getActivity(), ForgotPasswordActivity.class);
                    startActivity(intent);

                }
                return true;
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
        etPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (count == 0) {
                    etPassword.setError(null);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        tvSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SpannableString s1 = new SpannableString(getResources().getString(R.string.please_enter_email));
                s1.setSpan(new TypefaceSpan(getActivity(), Constants.font_name), 0, s1.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                SpannableString s2 = new SpannableString(getResources().getString(R.string.please_enter_valid_email));
                s2.setSpan(new TypefaceSpan(getActivity(), Constants.font_name), 0, s2.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                SpannableString s3 = new SpannableString(getResources().getString(R.string.please_enter_password));
                s3.setSpan(new TypefaceSpan(getActivity(), Constants.font_name), 0, s3.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);


                if (etEmail.getText().toString().trim().length() == 0 && etPassword.getText().toString().length() == 0) {
                    etEmail.setError(s1);
                    etPassword.setError(s3);

                } else if (!Utils.isValidEmail1(etEmail.getText().toString())) {
                    etEmail.setError(s2);

                } else {
                    loginCredentialsSendToServer(etEmail.getText().toString().trim(), etPassword.getText().toString().trim());
                    // Intent intent = new Intent(SignInFragment.this, MainActivity.class);
                    // intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    // startActivity(intent);
                    // overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

                }


            }
        });

    }
    
    private void loginCredentialsSendToServer(final String email, final String password) {

        if (NetworkConnection.isNetworkAvailable(getActivity())) {
            Utils.showProgressDialog(progressDialog, getResources().getString(R.string.progress_dialog_text_please_wait), true);
            Utils.showLog(Log.INFO, "" + AppConfigTags.URL, AppConfigURL.URL_SIGN_IN, true);
            StringRequest strRequest1 = new StringRequest(Request.Method.POST, AppConfigURL.URL_SIGN_IN,
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
                                        userDetailsPref.putIntPref(getActivity(), UserDetailsPref.USER_ID, jsonObj.getInt(AppConfigTags.SIGN_IN_USER_ID));
                                        userDetailsPref.putStringPref(getActivity(), UserDetailsPref.USER_NAME, jsonObj.getString(AppConfigTags.SIGN_IN_USER_NAME));
                                        userDetailsPref.putStringPref(getActivity(), UserDetailsPref.USER_EMAIL, jsonObj.getString(AppConfigTags.SIGN_IN_USER_EMAIL));
                                        userDetailsPref.putStringPref(getActivity(), UserDetailsPref.USER_MOBILE, jsonObj.getString(AppConfigTags.SIGN_IN_USER_MOBILE));
                                        Intent intent = new Intent(getActivity(), MainActivity.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(intent);
                                        getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

                                    } else {
                                        Utils.showSnackBar(getActivity(), clMain, message, Snackbar.LENGTH_LONG, null, null);
                                    }
                                    progressDialog.dismiss();
                                } catch (Exception e) {
                                    progressDialog.dismiss();
                                    Utils.showSnackBar(getActivity(), clMain, getResources().getString(R.string.snackbar_text_exception_occurred), Snackbar.LENGTH_LONG, getResources().getString(R.string.snackbar_action_dismiss), null);
                                    e.printStackTrace();
                                }
                            } else {
                                Utils.showSnackBar(getActivity(), clMain, getResources().getString(R.string.snackbar_text_error_occurred), Snackbar.LENGTH_LONG, getResources().getString(R.string.snackbar_action_dismiss), null);
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
                            Utils.showSnackBar(getActivity(), clMain, getResources().getString(R.string.snackbar_text_error_occurred), Snackbar.LENGTH_LONG, getResources().getString(R.string.snackbar_action_dismiss), null);
                        }
                    }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new Hashtable<String, String>();
                    params.put(AppConfigTags.SIGN_IN_USER_EMAIL, email);
                    params.put(AppConfigTags.SIGN_IN_PASSWORD, password);
                    params.put(AppConfigTags.USER_FIREBASE_ID, userDetailsPref.getStringPref(getActivity(), UserDetailsPref.USER_FIREBASE_ID));
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
            Utils.showSnackBar(getActivity(), clMain, getResources().getString(R.string.snackbar_text_no_internet_connection_available), Snackbar.LENGTH_LONG, getResources().getString(R.string.snackbar_action_go_to_settings), new View.OnClickListener() {
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



