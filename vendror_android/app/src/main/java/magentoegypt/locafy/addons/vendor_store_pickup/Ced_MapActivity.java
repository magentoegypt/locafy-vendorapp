package magentoegypt.locafy.addons.vendor_store_pickup;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import magentoegypt.locafy.R;
import magentoegypt.locafy.vendor_session.Ced_MultiVendor_VendorSessionManagement;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Ced_MapActivity extends FragmentActivity implements OnMapReadyCallback, View.OnClickListener {
    private static String TAG = "MAP LOCATION";
    int AUTOCOMPLETE_REQUEST_CODE = 1;
    int REQUEST_CODE = 2;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private String Url, getcountry;
    private ArrayList statelabellist, statecodelist;
    private EditText optional_text;
    private String setdeliverylocation = "";
    private Ced_MultiVendor_VendorSessionManagement session;
    private GoogleMap mMap;
    private Context mContext;
    private TextView mLocationMarkerText;
    private Button confirm_add;
    private LatLng mCenterLatLong;
    private Location currentLocation;
    private LocationManager locationManager;
    private Boolean fromnavigation = false;
    private String latitude = "", longitude = "";
    private String from = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_ced_map);
        mContext = this;
        session = new Ced_MultiVendor_VendorSessionManagement(this);
        from = getIntent().getStringExtra("from");
        mLocationMarkerText = findViewById(R.id.locationMarkertext);
        confirm_add = findViewById(R.id.confirm_add);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        confirm_add.setOnClickListener(this);
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            fetchLocation();
        } else {
            showGPSDisabledAlertToUser();
        }
        if (ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
            return;
        }
    }

    private void showGPSDisabledAlertToUser() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("GPS is disabled in your device. Would you like to enable it?")
                .setCancelable(false)
                .setPositiveButton("Goto Settings Page To Enable GPS",
                        (dialog, id) -> {
                            Intent callGPSSettingIntent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                            startActivityForResult(callGPSSettingIntent, 100);
                        });
        alertDialogBuilder.setNegativeButton("Cancel",
                (dialog, id) -> dialog.cancel());
        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                fetchLocation();
            }
        } else {
            if (ActivityCompat.checkSelfPermission(
                    this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
                return;
            }
        }
    }

    private void fetchLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Task<Location> task = fusedLocationProviderClient.getLastLocation();
        task.addOnSuccessListener(location -> {
            if (location != null) {
                currentLocation = location;
                SupportMapFragment supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
                assert supportMapFragment != null;
                supportMapFragment.getMapAsync(Ced_MapActivity.this);
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Log.d(TAG, "OnMapReady");
        mMap = googleMap;
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mMap.setMyLocationEnabled(true);
        mMap.isMyLocationEnabled();
        mCenterLatLong = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
        mMap.animateCamera(CameraUpdateFactory.newLatLng(mCenterLatLong));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(mCenterLatLong, 18f));
        mMap.setOnCameraIdleListener(() -> {
            try {
                mCenterLatLong = mMap.getCameraPosition().target;
                Location mLocation = new Location("");
                mLocation.setLatitude(mCenterLatLong.latitude);
                mLocation.setLongitude(mCenterLatLong.longitude);
                Log.i(TAG, "onMapReady: " + String.valueOf(mCenterLatLong.latitude));
                Log.i(TAG, "onMapReady: " + String.valueOf(mCenterLatLong.longitude));
                latitude = String.valueOf(mCenterLatLong.latitude);
                longitude = String.valueOf(mCenterLatLong.longitude);
                getPlaceInfo(mCenterLatLong.latitude, mCenterLatLong.longitude);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }


    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.confirm_add) {
            if (!TextUtils.isEmpty(mLocationMarkerText.getText().toString())) {
                //TODO save location, latitude and longitude into session
                session.saveLatitude(latitude);
                session.saveLongitude(longitude);
                session.saveLocation(mLocationMarkerText.getText().toString().trim());
                if (from.equalsIgnoreCase("Ced_MultiVendor_NewStore"))
                    Ced_MultiVendor_NewStore.getInstance().updateLocationAndLatitudeAndLongitude(mLocationMarkerText.getText().toString().trim(), latitude, longitude);
                if (from.equalsIgnoreCase("Ced_MultiVendor_UpdateStore"))
                    Ced_MultiVendor_UpdateStore.getInstance().updateLocationAndLatitudeAndLongitude(mLocationMarkerText.getText().toString().trim(), latitude, longitude);
                finish();

            } else {
                Toast.makeText(mContext, "Please set location first", Toast.LENGTH_SHORT).show();
            }
        }

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Place place = Autocomplete.getPlaceFromIntent(data);
                Log.i(TAG, "Place: " + place.getName() + ", " + place.getId());
                try {
                    if (mMap == null) {
                        Location mLocation = new Location("");
                        mLocation.setLatitude(place.getLatLng().latitude);
                        mLocation.setLongitude(place.getLatLng().longitude);
                        currentLocation = mLocation;
                        SupportMapFragment supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
                        assert supportMapFragment != null;
                        supportMapFragment.getMapAsync(Ced_MapActivity.this);

//                        startIntentService(mLocation);
                        latitude = String.valueOf(place.getLatLng().latitude);
                        longitude = String.valueOf(place.getLatLng().longitude);
                        getPlaceInfo(place.getLatLng().latitude, place.getLatLng().longitude);

                    } else {
                        mMap.animateCamera(CameraUpdateFactory.newLatLng(place.getLatLng()));
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(place.getLatLng(), 18f));
                        Location mLocation = new Location("");
                        latitude = String.valueOf(place.getLatLng().latitude);
                        longitude = String.valueOf(place.getLatLng().longitude);
                        mLocation.setLatitude(place.getLatLng().latitude);
                        mLocation.setLongitude(place.getLatLng().longitude);
//                        startIntentService(mLocation);
                        getPlaceInfo(place.getLatLng().latitude, place.getLatLng().longitude);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
                // TODO: Handle the error.
                Status status = Autocomplete.getStatusFromIntent(data);
                Log.i(TAG, status.getStatusMessage());
            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }
        }
        if (requestCode == 100) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                fetchLocation();
            } else {
                showGPSDisabledAlertToUser();
            }
        }
    }

    private void getPlaceInfo(double lat, double lon) throws IOException {
        Geocoder mGeocoder = new Geocoder(this);
        List<Address> addresses = mGeocoder.getFromLocation(lat, lon, 1);

        if (addresses.get(0).getPostalCode() != null) {
            String ZIP = addresses.get(0).getPostalCode();
            Log.d("ZIP CODE", ZIP);
        }

        if (addresses.get(0).getLocality() != null) {
            String city = addresses.get(0).getLocality();
            Log.d("CITY", city);
        }

        if (addresses.get(0).getAdminArea() != null) {
            String state = addresses.get(0).getAdminArea();
            Log.d("STATE", state);
        }

        if (addresses.get(0).getCountryName() != null) {
            String country = addresses.get(0).getCountryName();
            Log.d("COUNTRY", country);
        }

        try {
            if (addresses.get(0).getAddressLine(0) != null) {
                String getAddressLine = addresses.get(0).getAddressLine(0).replace(addresses.get(0).getFeatureName(), "");
                setdeliverylocation = addresses.get(0).getAddressLine(0);

                Log.d("REpo", "getAddressLine: " + getAddressLine);
                mLocationMarkerText.setText(setdeliverylocation.replace(", null", ""));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (addresses.get(0).getCountryCode() != null) {
            String CountryCode = addresses.get(0).getCountryCode();
            Log.d("CountryCode", CountryCode);
        }
    }

}

