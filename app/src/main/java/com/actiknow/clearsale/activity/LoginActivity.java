package com.actiknow.clearsale.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.actiknow.clearsale.R;
import com.actiknow.clearsale.utils.AppConfigTags;
import com.actiknow.clearsale.utils.AppConfigURL;
import com.actiknow.clearsale.utils.Constants;
import com.actiknow.clearsale.utils.NetworkConnection;
import com.actiknow.clearsale.utils.PrefUtil;
import com.actiknow.clearsale.utils.TypefaceSpan;
import com.actiknow.clearsale.utils.UserDetailsPref;
import com.actiknow.clearsale.utils.Utils;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import static com.actiknow.clearsale.R.id.login_button;


/**
 * Created by l on 24/02/2017.
 */

public class LoginActivity extends AppCompatActivity {

    EditText etEmail;
    EditText etPassword;
    TextView tvForgotPassword;
    TextView tvSignIn;
    TextView tvSignUp;

    CoordinatorLayout clMain;
    ProgressDialog progressDialog;
    UserDetailsPref userDetailsPref;
    private CallbackManager callbackManager;
    private LoginButton loginButton;
    Button linked_in_login_button;
    PackageInfo info;
    private static final String TAG = "facebook_login";

    PrefUtil prefUtil;
    String accessToken;


    String id,name,email,gender,birthday;


    private static final String host = "api.linkedin.com";
    private static final String url = "https://" + host + "/v1/people/~:" + "(email-address,formatted-name,phone-numbers,picture-urls::(original))";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initView();
        initData();
        initListener();
        displayFirebaseRegId();

    }

    private void initView() {
        etEmail = (EditText) findViewById(R.id.etEmail);
        etPassword = (EditText) findViewById(R.id.etPassword);
        tvSignIn = (TextView) findViewById(R.id.tvSignIn);
        tvForgotPassword = (TextView) findViewById(R.id.tvForgotPassword);
        tvSignUp = (TextView) findViewById(R.id.tvCreateAccount);
        clMain = (CoordinatorLayout) findViewById(R.id.clMain);
        loginButton = (LoginButton) findViewById(login_button);
        linked_in_login_button=(Button)findViewById(R.id.linkdin_login_button) ;
        Utils.setTypefaceToAllViews(this, tvSignIn);


        List<String> permissionNeeds = Arrays.asList("user_photos", "email", "user_birthday", "public_profile", "AccessToken");


//login_button.registerCallback();

    }

    private void initData() {
        userDetailsPref = UserDetailsPref.getInstance();
        //Log.e("Sud","1111"+userDetailsPref.getStringPref (LoginActivity.this,UserDetailsPref.USER_FIREBASE_ID));
        // Toast.makeText(this,"Firebase"+userDetailsPref.getStringPref (LoginActivity.this,UserDetailsPref.USER_FIREBASE_ID),Toast.LENGTH_LONG).show();
        progressDialog = new ProgressDialog(LoginActivity.this);
        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();





        //   loginButton.setReadPermissions(Arrays.asList("public_profile", "email"));

    }


    private void displayFirebaseRegId() {
        Utils.showLog(Log.ERROR, "Firebase Reg ID:", userDetailsPref.getStringPref(this, UserDetailsPref.USER_FIREBASE_ID), true);
    }

    private void initListener() {
        tvForgotPassword.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    tvForgotPassword.setTextColor(getResources().getColor(R.color.text_color_grey_dark));
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    tvForgotPassword.setTextColor(getResources().getColor(R.color.text_color_white));
                    Intent intent = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
                    startActivity(intent);

                }
                return true;
            }

        });


        tvSignUp.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    tvSignUp.setTextColor(getResources().getColor(R.color.text_color_grey_dark));
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    tvSignUp.setTextColor(getResources().getColor(R.color.text_color_white));
                    Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
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
                s1.setSpan(new TypefaceSpan(LoginActivity.this, Constants.font_name), 0, s1.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                SpannableString s2 = new SpannableString(getResources().getString(R.string.please_enter_valid_email));
                s2.setSpan(new TypefaceSpan(LoginActivity.this, Constants.font_name), 0, s2.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                SpannableString s3 = new SpannableString(getResources().getString(R.string.please_enter_password));
                s3.setSpan(new TypefaceSpan(LoginActivity.this, Constants.font_name), 0, s3.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);


                if (etEmail.getText().toString().trim().length() == 0 && etPassword.getText().toString().length() == 0) {
                    etEmail.setError(s1);
                    etPassword.setError(s3);

                } else if (!Utils.isValidEmail1(etEmail.getText().toString())) {
                    etEmail.setError(s2);

                } else {
                    loginCredentialsSendToServer(etEmail.getText().toString().trim(), etPassword.getText().toString().trim());
                    // Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    // intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    // startActivity(intent);
                    // overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

                }


            }
        });


        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
               // Toast.makeText(LoginActivity.this, "User ID: " + loginResult.getAccessToken().getUserId() + "\n" + "Auth Token: " + loginResult.getAccessToken().getToken(), Toast.LENGTH_LONG).show();
                accessToken = loginResult.getAccessToken().getToken();
                String userId = loginResult.getAccessToken().getUserId();
                System.out.println("onSuccess");

                // accessToken = loginResult.getAccessToken().getToken();
                Log.i("accessToken", accessToken);

                GraphRequest request = GraphRequest.newMeRequest(
                        loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                        public void onCompleted(JSONObject object, GraphResponse response) {
                            Log.i("LoginActivity", response.toString());
                            try {
                                id = object.getString("id");
                                try {
                                    URL profile_pic = new URL("http://graph.facebook.com/" + id + "/picture?type=large");
                                    Log.i("profile_pic", profile_pic + "");

                                } catch (MalformedURLException e) {
                                    e.printStackTrace();
                                }
                                name = object.getString("name");
                                email = object.getString("email");
                                gender = object.getString("gender");
                                birthday = object.getString("birthday");




                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,email,gender, birthday");
                request.setParameters(parameters);
                request.executeAsync();
                loginWithFacebookDetailSendToServer(accessToken,name,email);

            }
            @Override
            public void onCancel() {
            }
            @Override
            public void onError(FacebookException e) {
                e.printStackTrace();
                Log.d(TAG, "Login attempt failed.");


            }
        });



        linked_in_login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });

    }

    private void loginWithFacebookDetailSendToServer(final String accessToken, final String name, final String email) {

        if (NetworkConnection.isNetworkAvailable(LoginActivity.this)) {
            Utils.showProgressDialog(progressDialog, getResources().getString(R.string.progress_dialog_text_please_wait), true);
            Utils.showLog(Log.INFO, "" + AppConfigTags.URL, AppConfigURL.URL_FACEBOOK_SIGN_IN, true);
            StringRequest strRequest1 = new StringRequest(Request.Method.POST, AppConfigURL.URL_FACEBOOK_SIGN_IN,
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
                                        userDetailsPref.putIntPref(LoginActivity.this, UserDetailsPref.USER_ID, jsonObj.getInt(AppConfigTags.SIGN_IN_USER_ID));
                                        userDetailsPref.putStringPref(LoginActivity.this, UserDetailsPref.USER_NAME, jsonObj.getString(AppConfigTags.SIGN_IN_USER_NAME));
                                        userDetailsPref.putStringPref(LoginActivity.this, UserDetailsPref.USER_EMAIL, jsonObj.getString(AppConfigTags.SIGN_IN_USER_EMAIL));
                                        userDetailsPref.putStringPref(LoginActivity.this, UserDetailsPref.USER_ACCESS_TOKEN, jsonObj.getString(AppConfigTags.SIGN_IN_ACCESS_TOKEN));
                                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(intent);
                                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

                                    } else {
                                        Utils.showSnackBar(LoginActivity.this, clMain, message, Snackbar.LENGTH_LONG, null, null);
                                    }
                                    progressDialog.dismiss();
                                } catch (Exception e) {
                                    progressDialog.dismiss();
                                    Utils.showSnackBar(LoginActivity.this, clMain, getResources().getString(R.string.snackbar_text_exception_occurred), Snackbar.LENGTH_LONG, getResources().getString(R.string.snackbar_action_dismiss), null);
                                    e.printStackTrace();
                                }
                            } else {
                                Utils.showSnackBar(LoginActivity.this, clMain, getResources().getString(R.string.snackbar_text_error_occurred), Snackbar.LENGTH_LONG, getResources().getString(R.string.snackbar_action_dismiss), null);
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
                            Utils.showSnackBar(LoginActivity.this, clMain, getResources().getString(R.string.snackbar_text_error_occurred), Snackbar.LENGTH_LONG, getResources().getString(R.string.snackbar_action_dismiss), null);
                        }
                    }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new Hashtable<String, String>();
                    params.put(AppConfigTags.SIGN_IN_USER_EMAIL, email);
                    params.put(AppConfigTags.SIGN_IN_NAME, name);
                    params.put(AppConfigTags.SIGN_IN_ACCESS_TOKEN, accessToken);
                    params.put(AppConfigTags.USER_FIREBASE_ID, userDetailsPref.getStringPref(LoginActivity.this, UserDetailsPref.USER_FIREBASE_ID));
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


    private void loginCredentialsSendToServer(final String email, final String password) {

        if (NetworkConnection.isNetworkAvailable(LoginActivity.this)) {
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
                                        userDetailsPref.putIntPref(LoginActivity.this, UserDetailsPref.USER_ID, jsonObj.getInt(AppConfigTags.SIGN_IN_USER_ID));
                                        userDetailsPref.putStringPref(LoginActivity.this, UserDetailsPref.USER_NAME, jsonObj.getString(AppConfigTags.SIGN_IN_USER_NAME));
                                        userDetailsPref.putStringPref(LoginActivity.this, UserDetailsPref.USER_EMAIL, jsonObj.getString(AppConfigTags.SIGN_IN_USER_EMAIL));
                                        userDetailsPref.putStringPref(LoginActivity.this, UserDetailsPref.USER_MOBILE, jsonObj.getString(AppConfigTags.SIGN_IN_USER_MOBILE));
                                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(intent);
                                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

                                    } else {
                                        Utils.showSnackBar(LoginActivity.this, clMain, message, Snackbar.LENGTH_LONG, null, null);
                                    }
                                    progressDialog.dismiss();
                                } catch (Exception e) {
                                    progressDialog.dismiss();
                                    Utils.showSnackBar(LoginActivity.this, clMain, getResources().getString(R.string.snackbar_text_exception_occurred), Snackbar.LENGTH_LONG, getResources().getString(R.string.snackbar_action_dismiss), null);
                                    e.printStackTrace();
                                }
                            } else {
                                Utils.showSnackBar(LoginActivity.this, clMain, getResources().getString(R.string.snackbar_text_error_occurred), Snackbar.LENGTH_LONG, getResources().getString(R.string.snackbar_action_dismiss), null);
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
                            Utils.showSnackBar(LoginActivity.this, clMain, getResources().getString(R.string.snackbar_text_error_occurred), Snackbar.LENGTH_LONG, getResources().getString(R.string.snackbar_action_dismiss), null);
                        }
                    }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new Hashtable<String, String>();
                    params.put(AppConfigTags.SIGN_IN_USER_EMAIL, email);
                    params.put(AppConfigTags.SIGN_IN_PASSWORD, password);
                    params.put(AppConfigTags.USER_FIREBASE_ID, userDetailsPref.getStringPref(LoginActivity.this, UserDetailsPref.USER_FIREBASE_ID));
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
    protected void onActivityResult(int requestCode, int responseCode, Intent data) {
        super.onActivityResult(requestCode, responseCode, data);
        callbackManager.onActivityResult(requestCode, responseCode, data);
    }

    private void deleteAccessToken() {
        AccessTokenTracker accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(
                    AccessToken oldAccessToken,
                    AccessToken currentAccessToken) {

                if (currentAccessToken == null) {
                    //User logged out
                    prefUtil.clearToken();
                    LoginManager.getInstance().logOut();
                }
            }
        };
    }



}



