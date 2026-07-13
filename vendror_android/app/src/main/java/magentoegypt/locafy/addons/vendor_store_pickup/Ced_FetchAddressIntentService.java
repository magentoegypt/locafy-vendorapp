package magentoegypt.locafy.addons.vendor_store_pickup;

import android.app.IntentService;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.text.TextUtils;
import android.util.Log;

import magentoegypt.locafy.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Ced_FetchAddressIntentService extends IntentService {
    private static final String TAG = "REpoLoc";
    private ResultReceiver mReceiver;

    public Ced_FetchAddressIntentService() {
        // Use the TAG to name the worker thread.
        super(TAG);
        Log.i(TAG, "Ced_FetchAddressIntentService: ");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.d(TAG, "onHandleIntent_32: ");
        String errorMessage = "";

        mReceiver = intent.getParcelableExtra(Constants.RECEIVER);
        if (mReceiver == null) {
            Log.wtf(TAG, "No receiver received. There is nowhere to send the results.");
            return;
        }

        // Get the location passed to this service through an extra.
        Location location = intent.getParcelableExtra(Constants.LOCATION_DATA_EXTRA);
        if (location == null) {
            errorMessage = getString(R.string.no_location_data_provided);
            Log.wtf(TAG, errorMessage);
            deliverResultToReceiver(1, errorMessage,0,0);
            return;
        }

        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        List<Address> addresses = null;

        try {
            addresses = geocoder.getFromLocation(
                    location.getLatitude(),
                    location.getLongitude(),
                    1);
        }
        catch (IOException ioException) {
            // Catch network or other I/O problems.

            Log.e(TAG, errorMessage, ioException);
        }
        catch (IllegalArgumentException illegalArgumentException) {

            Log.e(TAG, errorMessage + ". " +
                    "Latitude = " + location.getLatitude() +
                    ", Longitude = " + location.getLongitude(), illegalArgumentException);
        }

        // Handle case where no address was found.
        if (addresses == null || addresses.size() == 0) {
            if (errorMessage.isEmpty()) {

            }
            deliverResultToReceiver(1, errorMessage,0,0);
        }
        else {
            Address address = addresses.get(0);
            ArrayList<String> addressFragments = new ArrayList<>();
            if(address.getSubLocality() !=null){
                addressFragments.add(address.getFeatureName() + ", " + address.getSubLocality()+", "+address.getSubAdminArea()+", "+address.getCountryName()+", "+address.getPostalCode());
                // addressFragments.add(address.getSubLocality()+", "+address.getSubAdminArea()+", "+address.getCountryName()+", "+address.getPostalCode());
            }
            else{
                addressFragments.add(address.getFeatureName() + ", "+address.getSubAdminArea()+", "+address.getCountryName()+", "+address.getPostalCode());
                //  addressFragments.add(address.getSubAdminArea()+", "+address.getCountryName()+", "+address.getPostalCode());
            }
            deliverResultToReceiver(0,TextUtils.join(System.getProperty("line.separator"), addressFragments),address.getLatitude(),address.getLongitude());
        }
    }

    private void deliverResultToReceiver(int resultCode, String message,double lat,double lang) {
        Log.d(TAG, "deliverResultToReceiver_93: ");
        Bundle bundle = new Bundle();
        bundle.putString("location_result", message);
        bundle.putDouble("latitude",lat);
        bundle.putDouble("longitude",lang);
        mReceiver.send(resultCode, bundle);
    }
}
