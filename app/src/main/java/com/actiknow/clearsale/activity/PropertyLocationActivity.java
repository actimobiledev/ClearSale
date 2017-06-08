package com.actiknow.clearsale.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.actiknow.clearsale.R;
import com.actiknow.clearsale.utils.AppConfigTags;
import com.actiknow.clearsale.utils.PropertyDetailsPref;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.OnStreetViewPanoramaReadyCallback;
import com.google.android.gms.maps.StreetViewPanorama;
import com.google.android.gms.maps.StreetViewPanoramaFragment;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.StreetViewPanoramaCamera;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class PropertyLocationActivity extends AppCompatActivity implements GoogleMap.OnMarkerClickListener, OnMapReadyCallback, OnStreetViewPanoramaReadyCallback {
    SupportMapFragment mapFragment;
    StreetViewPanoramaFragment streetViewPanoramaFragment;
    PropertyDetailsPref propertyDetailsPref;
    double latitude;
    double longitude;
    private Marker mAddress;
    private GoogleMap mMap;
    
    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_property_location);
        initView ();
        initData ();
        initListener ();
        
    }
    
    
    private void initView () {
        mapFragment = (SupportMapFragment) getSupportFragmentManager ().findFragmentById (R.id.map);
        streetViewPanoramaFragment = (StreetViewPanoramaFragment) getFragmentManager ().findFragmentById (R.id.streetviewpanorama);
        mapFragment.getMapAsync (this);
        streetViewPanoramaFragment.getStreetViewPanoramaAsync (this);
    }
    
    private void initData () {
        propertyDetailsPref = PropertyDetailsPref.getInstance ();
        latitude = Double.parseDouble (propertyDetailsPref.getStringPref (PropertyLocationActivity.this, PropertyDetailsPref.PROPERTY_LATITUDE));
        longitude = Double.parseDouble (propertyDetailsPref.getStringPref (PropertyLocationActivity.this, PropertyDetailsPref.PROPERTY_LONGITUDE));
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
        try {
            JSONArray jsonArrayPropertyComps = new JSONArray (propertyDetailsPref.getStringPref (PropertyLocationActivity.this, PropertyDetailsPref.PROPERTY_COMPS_ADDRESSES));
        
            Log.e ("karman", jsonArrayPropertyComps.toString ());
            for (int j = 0; j < jsonArrayPropertyComps.length (); j++) {
                JSONObject jsonObjectCompsAddressDetails = jsonArrayPropertyComps.getJSONObject (j);
                mMap.addMarker (
                        new MarkerOptions ().position (new LatLng (Double.parseDouble (jsonObjectCompsAddressDetails.getString (AppConfigTags.PROPERTY_COMP_LATITUDE)), Double.parseDouble (jsonObjectCompsAddressDetails.getString (AppConfigTags.PROPERTY_COMP_LONGITUDE))))
                                .title (jsonObjectCompsAddressDetails.getString (AppConfigTags.PROPERTY_COMP_ADDRESS))
                                .draggable (false)
                                .icon (BitmapDescriptorFactory.fromResource (R.drawable.ic_map_comps_marker))
                );
            }
        } catch (JSONException e) {
            e.printStackTrace ();
        }
    
    
        mMap.animateCamera (CameraUpdateFactory.newLatLngZoom (new LatLng (latitude, longitude), 13.0f));
        
        mAddress = mMap.addMarker (
                new MarkerOptions ().position (new LatLng (latitude, longitude))
                        .title (propertyDetailsPref.getStringPref (PropertyLocationActivity.this, PropertyDetailsPref.PROPERTY_ADDRESS1)
                                + ", " + propertyDetailsPref.getStringPref (PropertyLocationActivity.this, PropertyDetailsPref.PROPERTY_ADDRESS2))
                        .draggable (false)
                        .icon (BitmapDescriptorFactory.fromResource (R.drawable.ic_map_marker))
        );
        mAddress.setTag (0);
        mMap.animateCamera (CameraUpdateFactory.newLatLngZoom (new LatLng (latitude, longitude), 15.0f));
        mMap.setOnMarkerClickListener (this);
    }
    
    
    @Override
    public void onStreetViewPanoramaReady (StreetViewPanorama streetViewPanorama) {
        streetViewPanorama.setPosition (new LatLng (latitude, longitude));
//        streetViewPanorama.setPosition(new LatLng(28.5750297,77.0687423));
        StreetViewPanoramaCamera camera = new StreetViewPanoramaCamera.Builder ()
                .zoom (streetViewPanorama.getPanoramaCamera ().zoom + 0.5f)
                .tilt (streetViewPanorama.getPanoramaCamera ().tilt)
                .bearing (streetViewPanorama.getPanoramaCamera ().bearing)
                .build ();
        streetViewPanorama.animateTo (camera, 1000);
        
    }
}
