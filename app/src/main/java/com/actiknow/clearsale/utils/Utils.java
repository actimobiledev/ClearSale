package com.actiknow.clearsale.utils;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.actiknow.clearsale.R;
import com.actiknow.clearsale.app.AppController;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.toolbox.StringRequest;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static android.content.Context.ACTIVITY_SERVICE;

/**
 * Created by Admin on 23-12-2015.
 */
public class Utils {
    public static int isValidEmail (String email) {
        if (email.length () != 0) {
            boolean validMail = isValidEmail2 (email);
            if (validMail)
                return 1;
            else
                return 2;
        } else
            return 0;
    }

    public static boolean isValidEmail2 (CharSequence target) {
        return ! TextUtils.isEmpty (target) && android.util.Patterns.EMAIL_ADDRESS.matcher (target).matches ();
    }

    public static boolean isValidPassword (String password) {
        if (password.length () > 0) {
            return true;
        } else {
            return false;
        }
    }

    public static Bitmap base64ToBitmap (String b64) {
        byte[] imageAsBytes = Base64.decode (b64.getBytes (), Base64.DEFAULT);
        return BitmapFactory.decodeByteArray (imageAsBytes, 0, imageAsBytes.length);
    }

    public static String bitmapToBase64 (Bitmap bmp) {
        if (bmp != null) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bmp.compress (Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] imageBytes = baos.toByteArray ();
            String encodedImage = Base64.encodeToString (imageBytes, Base64.DEFAULT);
            return encodedImage;
        } else {
            return "";
        }
    }

    public static String convertTimeFormat (String dateInOriginalFormat, String originalFormat, String requiredFormat) {
        if (dateInOriginalFormat != "null") {
            SimpleDateFormat sdf = new SimpleDateFormat(originalFormat);//yyyy-MM-dd");
            Date testDate = null;
            try {
                testDate = sdf.parse (dateInOriginalFormat);
            } catch (Exception ex) {
                ex.printStackTrace ();
            }
            SimpleDateFormat formatter = new SimpleDateFormat(requiredFormat);
            String newFormat = formatter.format (testDate);
            return newFormat;
        } else {
            return "Unavailable";
        }
    }



/*
        AlertDialog.Builder builder = new AlertDialog.Builder (activity);
        builder.setMessage (message)
                .setCancelable (false)
                .setPositiveButton ("OK", new DialogInterface.OnClickListener () {
                    public void onClick (DialogInterface dialog, int id) {
                        dialog.dismiss ();
                        if (finish_flag) {
                            Intent intent = new Intent (activity, MainActivity.class);
                            intent.setFlags (Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            activity.startActivity (intent);
                            activity.overridePendingTransition (R.anim.slide_in_left, R.anim.slide_out_right);
                        }
                    }
                });
        AlertDialog alert = builder.create ();
        alert.show ();
        */


    public static void showSnackBar (Activity activity, CoordinatorLayout coordinatorLayout, String message, int duration, String button_text, View.OnClickListener onClickListener) {
        final Snackbar snackbar = Snackbar.make (coordinatorLayout, message, duration);
        snackbar.setAction (button_text, onClickListener);

        View sbView = snackbar.getView ();
        sbView.setBackgroundColor (activity.getResources ().getColor (R.color.snackbar_background));
        TextView textView = (TextView) sbView.findViewById (android.support.design.R.id.snackbar_text);
        TextView textView2 = (TextView) sbView.findViewById (android.support.design.R.id.snackbar_action);
        textView.setTextColor (activity.getResources ().getColor (R.color.app_text_color_dark));
        textView2.setTextColor (activity.getResources ().getColor (R.color.app_text_color_dark));
        textView.setTypeface (SetTypeFace.getTypeface (activity));
        textView2.setTypeface (SetTypeFace.getTypeface (activity));
        snackbar.show ();
    }

    public static void showToast (Activity activity, String message, boolean duration_long) {
        if (duration_long) {
            Toast.makeText (activity, message, Toast.LENGTH_LONG).show ();
        } else {
            Toast.makeText (activity, message, Toast.LENGTH_SHORT).show ();
        }
    }

    public static void setTypefaceToAllViews (Activity activity, View view) {
        Typeface tf = SetTypeFace.getTypeface (activity);
        SetTypeFace.applyTypeface (SetTypeFace.getParentView (view), tf);
    }

    public static void showProgressDialog (ProgressDialog progressDialog, String message, boolean cancelable) {
        // Initialize the progressDialog before calling this function
        TextView tvMessage;
        progressDialog.show ();
        progressDialog.getWindow ().setBackgroundDrawable (new ColorDrawable(android.graphics.Color.TRANSPARENT));
        progressDialog.setContentView (R.layout.progress_dialog);
        tvMessage = (TextView) progressDialog.findViewById (R.id.tvProgressDialogMessage);
        if (message != null) {
            tvMessage.setText (message);
            tvMessage.setVisibility (View.VISIBLE);
        }
        else
            tvMessage.setVisibility (View.GONE);
        progressDialog.setCancelable (cancelable);
    }

    public static void showLog (int log_type, String tag, String message, boolean show_flag) {
        if (Constants.show_log) {
            if (show_flag) {
                switch (log_type) {
                    case Log.DEBUG:
                        Log.d (tag, message);
                        break;
                    case Log.ERROR:
                        Log.e (tag, message);
                        break;
                    case Log.INFO:
                        Log.i (tag, message);
                        break;
                    case Log.VERBOSE:
                        Log.v (tag, message);
                        break;
                    case Log.WARN:
                        Log.w (tag, message);
                        break;
                    case Log.ASSERT:
                        Log.wtf (tag, message);
                        break;
                }
            }
        }
    }

    public static void showErrorInEditText (EditText editText, String message) {
        editText.setError (message);
    }

    public static void hideSoftKeyboard (Activity activity) {
        View view = activity.getCurrentFocus ();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService (Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow (view.getWindowToken (), 0);
        }
    }

    public static boolean isPackageExists (Activity activity, String targetPackage) {
        List<ApplicationInfo> packages;
        PackageManager pm;
        pm = activity.getPackageManager ();
        packages = pm.getInstalledApplications (0);
        for (ApplicationInfo packageInfo : packages) {
            if (packageInfo.packageName.equals (targetPackage))
                return true;
        }
        return false;
    }

    public static void sendRequest (StringRequest strRequest, int timeout_seconds) {
        strRequest.setShouldCache (false);
        int timeout = timeout_seconds * 1000;
        AppController.getInstance ().addToRequestQueue (strRequest);
        strRequest.setRetryPolicy (new DefaultRetryPolicy (timeout,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }

    public static Bitmap compressBitmap (Bitmap bitmap, Activity activity) {
        int image_quality = 10; // 10
        int max_image_size = 320; // 320

        Bitmap decoded = null;
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            if (NetworkConnection.isNetworkAvailable (activity)) {
                bitmap.compress (Bitmap.CompressFormat.JPEG, image_quality, out);
            } else {
                bitmap.compress (Bitmap.CompressFormat.JPEG, image_quality, out);
            }
            decoded = Utils.scaleDown (BitmapFactory.decodeStream (new ByteArrayInputStream(out.toByteArray ())), max_image_size, true);
        } catch (Exception e) {
            e.printStackTrace ();
            Utils.showLog (Log.ERROR, "EXCEPTION", e.getMessage (), true);
        }
        return decoded;
    }

    public static Bitmap scaleDown (Bitmap realImage, float maxImageSize, boolean filter) {
        float ratio = Math.min ((float) maxImageSize / realImage.getWidth (), (float) maxImageSize / realImage.getHeight ());
        int width = Math.round ((float) ratio * realImage.getWidth ());
        int height = Math.round ((float) ratio * realImage.getHeight ());
        Bitmap newBitmap = Bitmap.createScaledBitmap (realImage, width, height, filter);
        return newBitmap;
    }

    public static String getJSONFromAsset (Activity activity, String file_name) {
        String json = null;
        try {
            InputStream is = activity.getAssets ().open (file_name);
            int size = is.available ();
            byte[] buffer = new byte[size];
            is.read (buffer);
            is.close ();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace ();
            return null;
        }
        return json;
    }

    public static int getHourFromServerTime () {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar calendar = Calendar.getInstance ();
        try {
            calendar.setTime (simpleDateFormat.parse (Constants.server_time));
//            Log.e ("date", "" + calendar.get (Calendar.DAY_OF_MONTH));
//            Log.e ("month", "" + calendar.get (Calendar.MONTH));
//            Log.e ("year", "" + calendar.get (Calendar.YEAR));
//            Log.e ("hour", "" + calendar.get (Calendar.HOUR));
//            Log.e ("minutes", "" + calendar.get (Calendar.MINUTE));
//            Log.e ("seconds", "" + calendar.get (Calendar.SECOND));
            return calendar.get (calendar.HOUR_OF_DAY);
        } catch (ParseException e) {
            e.printStackTrace ();
        }
        return 0;
    }

    public static float convertPixelsToDp (float px, Context context) {
        Resources resources = context.getResources ();
        DisplayMetrics metrics = resources.getDisplayMetrics ();
        float dp = px / ((float) metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
        return dp;
    }

    public static boolean isEnoughMemory (Activity activity) {
        // Before doing something that requires a lot of memory,
        // check to see whether the device is in a low memory state.
        ActivityManager.MemoryInfo memoryInfo = getAvailableMemory (activity);
        if (! memoryInfo.lowMemory) {
            return true;
            // Do memory intensive work ...
        } else {
            return false;
        }
    }

    private static ActivityManager.MemoryInfo getAvailableMemory (Activity activity) {
        ActivityManager activityManager = (ActivityManager) activity.getSystemService (ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo ();
        activityManager.getMemoryInfo (memoryInfo);
        return memoryInfo;
    }

    public static String generateMD5 (String s) {
        try {
            // Create MD5 Hash
            MessageDigest digest = MessageDigest.getInstance ("MD5");
            digest.update (s.getBytes ());
            byte messageDigest[] = digest.digest ();

            // Create Hex String
            StringBuffer hexString = new StringBuffer();
            for (int i = 0; i < messageDigest.length; i++)
                hexString.append (Integer.toHexString (0xFF & messageDigest[i]));
            return hexString.toString ();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace ();
        }
        return "";
    }

    public static float dpFromPx (Context context, float px) {
        return px / context.getResources ().getDisplayMetrics ().density;
    }

    public static float pxFromDp (Context context, float dp) {
        return dp * context.getResources ().getDisplayMetrics ().density;
    }

    public static boolean dial (Context c) {
        final Intent i = new Intent(Intent.ACTION_DIAL);
        i.setData (Uri.parse ("tel:" + "9873684678"));//+ c.getString (R.string.intent_number)));
//        final Intent icc = Intent.createChooser (i, c.getString (R.string.intent_call));
        try {
//            c.startActivity (icc);
            return true;
        } catch (ActivityNotFoundException e) {
            return false;
        }
    }

    public static boolean sendMail (Context c) {
        final String mail = "karman.singhh@gmail.com";//c.getString (R.string.intent_mail);
        final Uri u = Uri.fromParts ("mailto", mail, null);
        final Intent i = new Intent(Intent.ACTION_SENDTO, u);
//        final Intent icc = Intent.createChooser (i, c.getString (R.string.intent_mail_tit));
        try {
//            c.startActivity (icc);
            return true;
        } catch (ActivityNotFoundException e) {
            return false;
        }
    }

    public static String getAutoCompleteUrl (String place) {
        // Obtain browser key from https://code.google.com/apis/console
        String key = "key=AIzaSyAxfILlKxFzEN-K5y2hwm4NdvGjKleUa60";
        String inputc = "components=country:in";
        // place to be be searched
        String input = "input=" + place;
        // place type to be searched
        String types = "types=geocode";
        // Sensor enabled
        String sensor = "sensor=false";
        // Building the parameters to the web service
        String parameters = inputc + "&" + input + "&" + types + "&" + sensor + "&" + key;
        // Output format
        String output = "json";
        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/place/autocomplete/" + output + "?" + parameters;
        return url;
    }

    public static String getPlaceDetailsUrl (String ref) {
        // Obtain browser key from https://code.google.com/apis/console
        String key = "key=AIzaSyAxfILlKxFzEN-K5y2hwm4NdvGjKleUa60";
        // reference of place
        String reference = "reference=" + ref;
        // Sensor enabled
        String sensor = "sensor=false";
        // Building the parameters to the web service
        String parameters = reference + "&" + sensor + "&" + key;
        // Output format
        String output = "json";
        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/place/details/" + output + "?" + parameters;
        Log.d ("URL", "" + url);
        return url;
    }

    public static String downloadUrl (String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(strUrl);
            // Creating an http connection to communicate with url
            urlConnection = (HttpURLConnection) url.openConnection ();
            // Connecting to url
            urlConnection.connect ();
            // Reading data from url
            iStream = urlConnection.getInputStream ();
            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));
            StringBuffer sb = new StringBuffer();
            String line = "";
            while ((line = br.readLine ()) != null) {
                sb.append (line);
            }
            data = sb.toString ();
            br.close ();
        } catch (Exception e) {
            Log.d ("Exception", e.toString ());
        } finally {
            iStream.close ();
            urlConnection.disconnect ();
        }
        return data;
    }

    public static void initAdapter (Activity activity, RecyclerView.Adapter<RecyclerView.ViewHolder> adapter, RecyclerView recyclerView, boolean divider, @Nullable SwipeRefreshLayout swipeRefreshLayout) {
        recyclerView.setAdapter (adapter);
        recyclerView.setHasFixedSize (true);
        recyclerView.setLayoutManager (new LinearLayoutManager(activity));

        if (divider) {

        } else {
            recyclerView.setItemAnimator (new DefaultItemAnimator());
        }
        if (swipeRefreshLayout != null) {
            swipeRefreshLayout.setColorSchemeResources (R.color.primary);
        }
    }

    public static void turnGPSOn (Activity activity) {
        String provider = Settings.Secure.getString (activity.getContentResolver (), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);

        if (! provider.contains ("gps")) { //if gps is disabled
            final Intent poke = new Intent();
            poke.setClassName ("com.android.settings", "com.android.settings.widget.SettingsAppWidgetProvider");
            poke.addCategory (Intent.CATEGORY_ALTERNATIVE);
            poke.setData (Uri.parse ("3"));
            activity.sendBroadcast (poke);
        }
    }

    public static void turnGPSOff (Activity activity) {
        String provider = Settings.Secure.getString (activity.getContentResolver (), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);

        if (provider.contains ("gps")) { //if gps is enabled
            final Intent poke = new Intent();
            poke.setClassName ("com.android.settings", "com.android.settings.widget.SettingsAppWidgetProvider");
            poke.addCategory (Intent.CATEGORY_ALTERNATIVE);
            poke.setData (Uri.parse ("3"));
            activity.sendBroadcast (poke);
        }
    }


    public static int isValidMobile (String phone2) {
        int number_status = 0;
        String first_char = "";
        first_char = phone2.substring (0, 1);
        if (phone2.length () == 10 && Integer.parseInt (first_char) > 6) {
            number_status = 3;
        } else if (phone2.length () < 10 && Integer.parseInt (first_char) > 6) {
            number_status = 1;
        } else if (Integer.parseInt (first_char) <= 6 && Integer.parseInt (first_char) > 0 && phone2.length () <= 10 && phone2.length () > 1) {
            number_status = 2;
        } else if (phone2.equalsIgnoreCase ("")) {
            number_status = 4;
        }
        return number_status;
    }


    public static boolean isValidEmail1 (String emailInput) {
        String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        Pattern pattern = Pattern.compile (EMAIL_PATTERN);
        Matcher matcher = pattern.matcher (emailInput);
        return matcher.matches ();
    }
}