/*
 *
 *   Copyright/* *
 *             * CedCommerce
 *             *
 *             * NOTICE OF LICENSE
 *             *
 *             * This source file is subject to the End User License Agreement (EULA)
 *             * that is bundled with this package in the file LICENSE.txt.
 *             * It is also available through the world-wide-web at this URL:
 *             * http://cedcommerce.com/license-agreement.txt
 *             *
 *             * @category  Ced
 *             * @package   MultiVendor
 *             * @author    CedCommerce Core Team <connect@cedcommerce.com >
 *             * @copyright Copyright CEDCOMMERCE (http://cedcommerce.com/)
 *             * @license   http://cedcommerce.com/license-agreement.txt
 *
 *
 *
 */

package magentoegypt.locafy.base_app;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;

import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import magentoegypt.locafy.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Ced_MultiVendor_VendorOrderMarkersMap extends FragmentActivity implements OnMapReadyCallback {

    ArrayList<HashMap<String, String>> googlemaplist;
    HashMap<String, String> countrycodehash;
    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ced_multivendor_activity_vendor_order_markers_map);
/*        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);*/
        if (getIntent().hasExtra("googlemapdata")) {
            googlemaplist = (ArrayList<HashMap<String, String>>) getIntent().getExtras().get("googlemapdata");
        }
        if (getIntent().hasExtra("countrycodehash")) {
            countrycodehash = (HashMap<String, String>) getIntent().getExtras().get("countrycodehash");
        }
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.MultiVendor_map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.clear();
        for (int i = 0; i < googlemaplist.size(); i++) {
            HashMap<String, String> data = googlemaplist.get(i);
            Double lat = null;
            Double lang = null;
            try {
                Geocoder selected_place_geocoder = new Geocoder(getApplicationContext());
                List<Address> address;
                address = selected_place_geocoder.getFromLocationName(countrycodehash.get(data.get("country")), 5);
                if (address.size() <= 0) {
                    //do nothing
                } else {
                    Address location = address.get(0);
                    lat = location.getLatitude();
                    lang = location.getLongitude();
                    LatLng sydney = new LatLng(lat, lang);
                    mMap.addMarker(new MarkerOptions().position(sydney).title(countrycodehash.get(data.get("country"))).snippet("Orders:" + data.get("total")).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))).showInfoWindow();
                    mMap.getUiSettings().setZoomGesturesEnabled(false);
                    // mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
                    //  mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat, lang), 10.0f));
                    //  mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 80f));
                    //---------------------------------
                    //---------------------------------
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}