package com.actiknow.clearsale.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;

import java.util.List;

public class GPSTracker extends Activity implements LocationListener {
    // The minimum distance to change Updates in meters
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10; // 10 meters
    // The minimum time between updates in milliseconds
    private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1; // 1 minute
    private final Context mContext;
    // Declaring a Location Manager
    protected LocationManager locationManager;
    // flag for GPS status
    boolean isGPSEnabled = false;
    // flag for network status
    boolean isNetworkEnabled = false;
    // flag for GPS status
    boolean canGetLocation = false;
    Location location; // location
    double latitude; // latitude
    double longitude; // longitude

    public GPSTracker (Context context) {
        this.mContext = context;
        getLocation ();
    }

    public Location getLocation2(){
        String context = Context.LOCATION_SERVICE;
        locationManager = (LocationManager) mContext.getSystemService (context);
        Criteria criteria = new Criteria ();
        criteria.setAccuracy (Criteria.ACCURACY_FINE);
        criteria.setAltitudeRequired (false);
        criteria.setBearingRequired (false);
        criteria.setCostAllowed (true);
        criteria.setPowerRequirement (Criteria.POWER_LOW);
        String provider = locationManager.getBestProvider (criteria, true);
        Log.e ("karman", "provider " + provider);
        if (locationManager.isProviderEnabled (provider)){
            this.canGetLocation = true;
        }
        locationManager.requestLocationUpdates (provider, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
        location = locationManager.getLastKnownLocation (provider);
        if (location != null) {
            latitude = location.getLatitude ();
            longitude = location.getLongitude ();
        }
        return location;
    }

    public Location getLocation () {
        try {
            locationManager = (LocationManager) mContext.getSystemService (LOCATION_SERVICE);
            // getting GPS status
            isGPSEnabled = locationManager.isProviderEnabled (LocationManager.GPS_PROVIDER);
            // getting network status
            isNetworkEnabled = locationManager.isProviderEnabled (LocationManager.NETWORK_PROVIDER);

            if (! isGPSEnabled && ! isNetworkEnabled) {
                // no network provider is enabled
            } else {
                this.canGetLocation = true;
                if (isNetworkEnabled) {
                    locationManager.requestLocationUpdates (
                            LocationManager.NETWORK_PROVIDER,
                            MIN_TIME_BW_UPDATES,
                            MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                    Log.d ("Network", "Network");
                    if (locationManager != null) {
                        location = locationManager.getLastKnownLocation (LocationManager.NETWORK_PROVIDER);
                        if (location != null) {
                            latitude = location.getLatitude ();
                            longitude = location.getLongitude ();
                        }
                    }
                }
                // if GPS Enabled get lat/long using GPS Services
                if (isGPSEnabled) {
                    if (location == null) {
                        locationManager.requestLocationUpdates (
                                LocationManager.GPS_PROVIDER,
                                MIN_TIME_BW_UPDATES,
                                MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                        Log.d ("GPS Enabled", "GPS Enabled");
                        if (locationManager != null) {
                            location = locationManager.getLastKnownLocation (LocationManager.GPS_PROVIDER);
                            if (location != null) {
                                latitude = location.getLatitude ();
                                longitude = location.getLongitude ();
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace ();
        }
        return getLastKnownLocation (true,mContext);
//        return location;
    }

    /**
     * Stop using GPS listener
     * Calling this function will stop using GPS in your app
     */
    /*
    @TargetApi(Build.VERSION_CODES.M)
    public void stopUsingGPS () {
        if (locationManager != null) {
            if (checkSelfPermission (Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission (Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    public void requestPermissions(@NonNull String[] permissions, int requestCode)
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for Activity#requestPermissions for more details.
                return;
            }
            locationManager.removeUpdates (GPSTracker.this);
        }
    }
    */
    /**
     * Stop using GPS listener
     * Calling this function will stop using GPS in your app
     */
    public void stopUsingGPS () {
        if (locationManager != null) {
            locationManager.removeUpdates (GPSTracker.this);
        }
    }

    public double getLatitude () {
        if (location != null) {
            latitude = location.getLatitude ();
        }
        // return latitude
        return latitude;
    }

    public double getLongitude () {
        if (location != null) {
            longitude = location.getLongitude ();
        }
        // return longitude
        return longitude;
    }

    public boolean canGetLocation () {
        return this.canGetLocation;
    }

    public void showSettingsAlert () {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder (mContext);
        // Setting Dialog Title
        alertDialog.setTitle ("GPS is settings");
        // Setting Dialog Message
        alertDialog.setMessage ("GPS is not enabled. Do you want to go to settings menu?");
        // On pressing Settings button
        alertDialog.setPositiveButton ("Settings", new DialogInterface.OnClickListener () {
            public void onClick (DialogInterface dialog, int which) {
                Intent intent = new Intent (Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                mContext.startActivity (intent);
            }
        });
        // on pressing cancel button
        alertDialog.setNegativeButton ("Cancel", new DialogInterface.OnClickListener () {
            public void onClick (DialogInterface dialog, int which) {
                dialog.cancel ();
            }
        });
        // Showing Alert Message
        alertDialog.show ();
    }

    @Override
    public void onLocationChanged (Location currentLocation) {
        this.location = currentLocation;
    }

    @Override
    public void onProviderDisabled (String provider) {
    }

    @Override
    public void onProviderEnabled (String provider) {
    }

    @Override
    public void onStatusChanged (String provider, int status, Bundle extras) {
    }

    public static Location getLastKnownLocation (boolean enabledProvidersOnly, Context context) {
        LocationManager manager = (LocationManager) context.getSystemService (Context.LOCATION_SERVICE);
        Location utilLocation = null;
        List<String> providers = manager.getProviders (enabledProvidersOnly);
        for (String provider : providers) {
//            Log.e ("karman", "provider" + provider);
            utilLocation = manager.getLastKnownLocation (provider);
            if (utilLocation != null)
                return utilLocation;
        }
        return null;
    }
}
