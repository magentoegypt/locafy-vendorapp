package magentoegypt.locafy.addons.vendor_store_pickup;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import magentoegypt.locafy.R;

public class MapDialogFragment extends DialogFragment implements OnMapReadyCallback {

    GoogleMap mMap;
    SupportMapFragment mapFragment;
    LatLng latLng;

    public MapDialogFragment(LatLng latLng) {
        this.latLng = latLng;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        try {
//            if (inflater != null) {
            Log.i("TAG", "onCreateView: ");
            View view = inflater.inflate(R.layout.map_view, container, false);
            mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map_views);
            assert mapFragment != null;
            mapFragment.getMapAsync(this);
            return view;
//            }
//            return view;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        try {
            mMap = googleMap;
            setUpMap();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setUpMap() //If the setUpMapIfNeeded(); is needed then...
    {
        //            latLng = new LatLng(20.000, 30.000);
        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        MarkerOptions markerOptions2 = new MarkerOptions();
        markerOptions2.position(latLng);
        mMap.addMarker(markerOptions2);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
        mMap.animateCamera(CameraUpdateFactory.zoomIn());
        mMap.animateCamera(CameraUpdateFactory.zoomTo(15), 2000, null);
    }

    @Override
    public void onDestroy() {
        try {
            super.onDestroy();
            getParentFragmentManager().beginTransaction().remove(mapFragment).commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
