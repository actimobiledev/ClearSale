package com.actiknow.clearsale.activity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.actiknow.clearsale.R;
import com.actiknow.clearsale.utils.AppConfigTags;
import com.actiknow.clearsale.utils.AppConfigURL;
import com.actiknow.clearsale.utils.Constants;
import com.actiknow.clearsale.utils.NetworkConnection;
import com.actiknow.clearsale.utils.Utils;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import static com.actiknow.clearsale.utils.Constants.latitude;
import static com.actiknow.clearsale.utils.Constants.longitude;

public class AllPropertyLocationActivity extends AppCompatActivity implements GoogleMap.OnMarkerClickListener, OnMapReadyCallback {
    SupportMapFragment mapFragment;
    
    CoordinatorLayout clMain;
    private GoogleMap mMap;
    
    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_all_property_location);
        initView ();
        initData ();
        initListener ();
        getAllProperty ();
    }
    
    
    private void initView () {
        clMain = (CoordinatorLayout) findViewById (R.id.clMain);
        mapFragment = (SupportMapFragment) getSupportFragmentManager ().findFragmentById (R.id.map);
        mapFragment.getMapAsync (this);
    }
    
    private void initData () {
    }
    
    private void initListener () {
        
    }
    
    @Override
    public boolean onMarkerClick (Marker marker) {
//        Integer clickCount = (Integer) marker.getTag ();
        // Check if a click count was set, then display the click count.
//        if (clickCount != null) {
//            clickCount = clickCount + 1;
//            marker.setTag (clickCount);
//            Toast.makeText (this,
//                    marker.getTitle () +
//                            " has been clicked " + clickCount + " times.",
//                    Toast.LENGTH_SHORT).show ();
//        }
        return false;
    }
    
    @Override
    public void onMapReady (GoogleMap googleMap) {
        mMap = googleMap;
        
        mMap.setOnInfoWindowClickListener (new GoogleMap.OnInfoWindowClickListener () {
            @Override
            public void onInfoWindowClick (Marker arg0) {
                int property_id = (int) arg0.getTag ();
                Toast.makeText (AllPropertyLocationActivity.this, "property id " + property_id, Toast.LENGTH_SHORT).show ();
            }
        });
    }
    
    private void getAllProperty () {
        if (NetworkConnection.isNetworkAvailable (this)) {
            Utils.showLog (Log.INFO, "" + AppConfigTags.URL, AppConfigURL.URL_PROPERTY_MAP, true);
            StringRequest strRequest1 = new StringRequest (Request.Method.POST, AppConfigURL.URL_PROPERTY_MAP,
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
                                        JSONArray jsonArrayAllProperty = jsonObj.getJSONArray (AppConfigTags.PROPERTY_LOCATION);
                                        for (int i = 0; i < jsonArrayAllProperty.length (); i++) {
                                            JSONObject jsonObjectAllProperty = jsonArrayAllProperty.getJSONObject (i);
                                            mMap.addMarker (
                                                    new MarkerOptions ().position (new LatLng (jsonObjectAllProperty.getDouble (AppConfigTags.PROPERTY_LATITUDE), jsonObjectAllProperty.getDouble (AppConfigTags.PROPERTY_LONGITUDE)))
                                                            .title (jsonObjectAllProperty.getString (AppConfigTags.PROPERTY_ADDRESS))
                                                            .draggable (false)
                                                            .icon (BitmapDescriptorFactory.fromResource (R.drawable.ic_map_marker))
                                            ).setTag (jsonObjectAllProperty.getInt (AppConfigTags.PROPERTY_ID));
                                            mMap.setOnMarkerClickListener (AllPropertyLocationActivity.this);
                                            mMap.animateCamera (CameraUpdateFactory.newLatLngZoom (new LatLng (latitude, longitude), 12.0f));
                                        }
                                    } else {
                                        Utils.showSnackBar (AllPropertyLocationActivity.this, clMain, message, Snackbar.LENGTH_LONG, null, null);
                                    }
                                } catch (Exception e) {
                                    Utils.showSnackBar (AllPropertyLocationActivity.this, clMain, getResources ().getString (R.string.snackbar_text_exception_occurred), Snackbar.LENGTH_LONG, getResources ().getString (R.string.snackbar_action_dismiss), null);
                                    e.printStackTrace ();
                                }
                            } else {
                                Utils.showSnackBar (AllPropertyLocationActivity.this, clMain, getResources ().getString (R.string.snackbar_text_error_occurred), Snackbar.LENGTH_LONG, getResources ().getString (R.string.snackbar_action_dismiss), null);
                                Utils.showLog (Log.WARN, AppConfigTags.SERVER_RESPONSE, AppConfigTags.DIDNT_RECEIVE_ANY_DATA_FROM_SERVER, true);
                            }
                            
                        }
                    },
                    new com.android.volley.Response.ErrorListener () {
                        @Override
                        public void onErrorResponse (VolleyError error) {
                            
                            Utils.showLog (Log.ERROR, AppConfigTags.VOLLEY_ERROR, error.toString (), true);
                            Utils.showSnackBar (AllPropertyLocationActivity.this, clMain, getResources ().getString (R.string.snackbar_text_error_occurred), Snackbar.LENGTH_LONG, getResources ().getString (R.string.snackbar_action_dismiss), null);
                        }
                    }) {
                @Override
                protected Map<String, String> getParams () throws AuthFailureError {
                    Map<String, String> params = new Hashtable<String, String> ();
                    params.put (AppConfigTags.TYPE, AppConfigTags.PROPERTY_LOCATION);
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
}