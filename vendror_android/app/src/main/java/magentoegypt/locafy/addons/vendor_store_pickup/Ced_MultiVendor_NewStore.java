package magentoegypt.locafy.addons.vendor_store_pickup;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import magentoegypt.locafy.base_app.Ced_MultiVendor_VendorFunctionalityList;
import magentoegypt.locafy.navigation_drawer.Activity.Ced_MultiVendor_NavigationActivity;
import magentoegypt.locafy.api_request_response_section.network_error_handling.Ced_MultiVendor_ConnectionDetector;
import magentoegypt.locafy.api_request_response_section.network_error_handling.Ced_MultiVendor_NoInternetconnection;
import magentoegypt.locafy.api_request_response_section.network_error_handling.Ced_MultiVendor_UnAuthourizedRequestError;
import magentoegypt.locafy.api_request_response_section.AsyncResponse;
import magentoegypt.locafy.api_request_response_section.Ced_MultiVendor_ClientRequestResponse;
import magentoegypt.locafy.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Ced_MultiVendor_NewStore extends Ced_MultiVendor_NavigationActivity implements OnMapReadyCallback {

    private final int PLACE_PICKER_REQUEST = 1;
    Ced_MultiVendor_ConnectionDetector connectionDetector;
    Ced_MultiVendor_VendorFunctionalityList functionalityList;
    String createstore_url = "";
    String hourinterval_url = "";
    String countries_url = "";
    String is_active = "";
    TextView tab_store_basicinfo, tab_store_hourinfo, savenewstore, resetvalues, pick_my_location;
    ScrollView scrollView_tab_store_basicinfo;
    LinearLayout linearView_tab_store_hourinfo;
    LinearLayout storehourspinners;
    EditText store_name, store_manager_name, getStore_manager_email, country;
    Spinner status;
    EditText store_address, store_latitude, store_longitude, store_city, store_state, postal_code, contact_number, shipping_price;
    HashMap<String, String> storeHashmap;
    String country_code = "";
    String country_label = "";
    HashMap<String, String> mapcountrylabel;
    List<String> countrylabellist;
    List<String> statelabellist;
    List<String> countrycodelist;
    List<String> statecodelist;
    JSONArray listweek, liststart, listend, listinterval, liststatus;
    ArrayList<String> weeks = new ArrayList<>();
    String weekstatus = "";
    JSONObject object_store;
    private String TAG = "LOCATION_STORE_PICKUP";
    boolean location = true;
    private AddressResultReceiver mResultReceiver;
    private GoogleMap mMap;
    private LatLng mCenterLatLong;
    private Location currentLocation;
    String setdeliverylocation = "";
    TextView location_field,MultiVendor_Store_selected_location;
    LatLng latLng;
    static Ced_MultiVendor_NewStore ced_multiVendor_newStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        connectionDetector = new Ced_MultiVendor_ConnectionDetector(getApplicationContext());
        functionalityList = new Ced_MultiVendor_VendorFunctionalityList(getApplicationContext());
        createstore_url = session.getBase_Url() + "vstorepickupapi/store/save";
        hourinterval_url = session.getBase_Url() + "vstorepickupapi/store/storehour";
        countries_url = session.getBase_Url() + "vendorapi/index/getCountry";
        getcountry();
        ced_multiVendor_newStore = this;
        if (!Places.isInitialized()) {
            Places.initialize(getApplicationContext(), "AIzaSyBIdrfaSZo7xo-OswhSnvOTtn3v_7QXlPs");
//            Places.initialize(getApplicationContext(), "AIzaSyBkjXPO8Vz_TTyfNCdihN2qfOtZJRTO6q4");
        }
        storeHashmap = new HashMap<>();
        mapcountrylabel = new HashMap<>();
        countrylabellist = new ArrayList<>();
        statelabellist = new ArrayList<>();
        countrycodelist = new ArrayList<>();
        statecodelist = new ArrayList<>();

        liststart = new JSONArray();
        listend = new JSONArray();
        listinterval = new JSONArray();
        liststatus = new JSONArray();
        object_store = new JSONObject();
        listweek = new JSONArray();// remove

        weeks.add("MONDAY");
        weeks.add("TUESDAY");
        weeks.add("WEDNESDAY");
        weeks.add("THURSDAY");
        weeks.add("FRIDAY");
        weeks.add("SATURDAY");
        weeks.add("SUNDAY");

        if (connectionDetector.isConnectingToInternet()) {
            final ViewGroup content = findViewById(R.id.MultiVendor_frame_container);
            getLayoutInflater().inflate(R.layout.ced_multivendor_newstore, content, true);
            Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.backbutton);
            store_name = findViewById(R.id.MultiVendor_store_name);
            store_manager_name = findViewById(R.id.MultiVendor_storemanager_name);
            getStore_manager_email = findViewById(R.id.MultiVendor_storemanager_email);
            store_address = findViewById(R.id.MultiVendor_Store_address);
            MultiVendor_Store_selected_location = findViewById(R.id.MultiVendor_Store_selected_location);
            pick_my_location = findViewById(R.id.MultiVendor_Store_pickmylocation);
            country = findViewById(R.id.MultiVendor_store_country);
            status = findViewById(R.id.MultiVendor_store_status);
            store_latitude = findViewById(R.id.MultiVendor_store_latitude);
            store_longitude = findViewById(R.id.MultiVendor_store_longitude);
            store_city = findViewById(R.id.MultiVendor_storecity);
            store_state = findViewById(R.id.MultiVendor_storestate);
            postal_code = findViewById(R.id.MultiVendor_postal_code);
            contact_number = findViewById(R.id.MultiVendor_contactnumber);
            shipping_price = findViewById(R.id.MultiVendor_shippingprice);
            storehourspinners = findViewById(R.id.linearlayout_storehour);
            savenewstore = findViewById(R.id.MultiVendor_store_savenew);
            resetvalues = findViewById(R.id.MultiVendor_store_resetvalues);

            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

            tab_store_basicinfo = findViewById(R.id.tabMultiVendor_store_basicinfo);
            tab_store_hourinfo = findViewById(R.id.tabMultiVendor_store_hourinfo);
            linearView_tab_store_hourinfo = findViewById(R.id.store_hourinfo_linearview);
            scrollView_tab_store_basicinfo = findViewById(R.id.store_basicinfo_scrollview);

            tab_store_hourinfo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    handleTabStoreHourInfoVisibility();
                }
            });

            tab_store_basicinfo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    handleTabBasicInfoVisibility();
                }
            });

            //============COUNTRY=================================
            country.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final CharSequence[] arrayOfInt = countrycodelist.toArray(new CharSequence[countrycodelist.size()]);
                    final CharSequence[] arrayOfInt2 = countrylabellist.toArray(new CharSequence[countrylabellist.size()]);
                    Dialog levelDialog1;
                    final AlertDialog.Builder builder = new AlertDialog.Builder(Ced_MultiVendor_NewStore.this);
                    builder.setTitle(Html.fromHtml("<font color='#000000'>Select Your  Country:</font>"));
                    builder.setSingleChoiceItems(arrayOfInt2, -1, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int postion) {
                                    country_code = (String) arrayOfInt[postion];
                                    country_label = (String) arrayOfInt2[postion];
                                    country.setText(country_label);
                                    dialog.dismiss();
                                }
                            }
                    );
                    levelDialog1 = builder.create();
                    levelDialog1.show();
                }
            });

//            PlaceAutocompleteFragment autocompleteFragment = (PlaceAutocompleteFragment) getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);
//            autocompleteFragment.setHint("");
//            autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
//                @Override
//                public void onPlaceSelected(Place place) {
//                    Log.i("TAG", "Place: " + place.getName());
//                    CharSequence address = place.getAddress();
//                    store_address.setText(address);
//                    LatLng getlatLng = place.getLatLng();
//                    store_latitude.setText("" + getlatLng.latitude);
//                    store_longitude.setText("" + getlatLng.longitude);
//                }
//
//                @Override
//                public void onError(Status status) {
//                }
//            });


            AutocompleteSupportFragment autocompleteFragment1 = (AutocompleteSupportFragment) getSupportFragmentManager().findFragmentById(R.id.autocomplete_fragment);
            autocompleteFragment1.setPlaceFields(Arrays.asList(com.google.android.libraries.places.api.model.Place.Field.ID, com.google.android.libraries.places.api.model.Place.Field.NAME, com.google.android.libraries.places.api.model.Place.Field.ADDRESS, com.google.android.libraries.places.api.model.Place.Field.LAT_LNG, com.google.android.libraries.places.api.model.Place.Field.ADDRESS_COMPONENTS));
            View root = (View) getLayoutInflater().inflate(R.layout.change_place, null);
            location_field = root.findViewById(R.id.location_field);

            autocompleteFragment1.setOnPlaceSelectedListener(new com.google.android.libraries.places.widget.listener.PlaceSelectionListener() {
                @Override
                public void onPlaceSelected(com.google.android.libraries.places.api.model.Place place) {
                    Log.i(TAG, "pl getAttributions = " + place.getAttributions());
                    Log.i(TAG, "pl getAddressComponents= " + place.getAddressComponents());
                    Log.i(TAG, "pl getAddress= " + place.getAddress());
                    Log.i(TAG, "pl getName= " + place.getName());
                    Log.i(TAG, "pl getName= " + place.getLatLng());
                    String choosenplace, PostalCode, City, State, CountryName, lat, lng;
                    choosenplace = place.getName() + "," + place.getAddress();

                    Log.d(TAG, "choosenplace: " + place.getName() + "" + place.getAddress());

                    latLng = place.getLatLng();
                    Geocoder geocoder = new Geocoder(Ced_MultiVendor_NewStore.this, Locale.getDefault());
                    List<Address> addresses = null;
                    try {
                        addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
                        if (addresses != null) {
                            if (addresses.size() > 0) {
                                Address returnedAddr = addresses.get(0);
                                Log.d(TAG, "returnedAddr: " + returnedAddr.getCountryName());
                                Log.d(TAG, "returnedAddr: " + returnedAddr.getPostalCode());
                                Log.d(TAG, "returnedAddr: " + returnedAddr.getLocality());
                                Log.d(TAG, "returnedAddr: " + returnedAddr.getAddressLine(1));
                                Log.d(TAG, "returnedAddr: " + returnedAddr.getAdminArea());

                                PostalCode = returnedAddr.getPostalCode();
                                City = returnedAddr.getLocality();
                                State = returnedAddr.getAdminArea();
                                CountryName = returnedAddr.getCountryName();
                                lat = String.valueOf(latLng.latitude);
                                lng = String.valueOf(latLng.longitude);

                                if (choosenplace != null) {
                                    Log.d(TAG, "PostalCode: " + PostalCode);
                                    Log.d(TAG, "city: " + City);
                                    Log.d(TAG, "State: " + State);
                                    Log.d(TAG, "CountryName: " + CountryName);
                                    Log.d(TAG, "lat: " + lat);
                                    Log.d(TAG, "lng: " + lng);
                                    store_latitude.setText("" + lat);
                                    store_longitude.setText("" + lng);
//                                    store_city.setText(City);
//                                    store_state.setText(State);
//                                    postal_code.setText(PostalCode);
//                                    location_field.setText(City + "," + State);
//                                    location_field.setText(choosenplace);
                                    store_address.setText(choosenplace);
                                    MultiVendor_Store_selected_location.setText(choosenplace);
                                }
                            }
                        } else {
                            Log.w(TAG, "No Address returned!");
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onError(Status status) {
                    // TODO: Handle the error.

                }
            });


            //====================IS ACTIVE=================================
            status.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    if (adapterView.getSelectedItem().toString().equals("Enabled")) {
                        is_active = "1";
                    } else {
                        if (adapterView.getSelectedItem().toString().equals("Disabled")) {
                            is_active = "0";
                        }
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });

            JSONObject obj;
            View view;
            TextView weekday;
            Spinner enable_disable_staus;
            Spinner start_spinner;
            Spinner end_spinner;
            Spinner interval_spinner;

            for (int x = 0; x < 7; x++) {
                obj = new JSONObject();
                view = View.inflate(Ced_MultiVendor_NewStore.this, R.layout.ced_multivendor_storehour_spinners, null);
                interval_spinner = view.findViewById(R.id.MultiVendor_store_interval_spinner);
                start_spinner = view.findViewById(R.id.MultiVendor_store_start_spinner);
                enable_disable_staus = view.findViewById(R.id.MultiVendor_store_status_spinner);
                end_spinner = view.findViewById(R.id.MultiVendor_store_end_spinner);
                weekday = view.findViewById(R.id.MultiVendor_store_weekday);
                weekday.setText(weeks.get(x));
                final JSONObject finalObj = obj;
                final int finalX = x;

                start_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() { //START SPINNER
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        try {
                            finalObj.put("start", adapterView.getSelectedItem().toString());
                            object_store.put(weeks.get(finalX).substring(0, 3).toLowerCase(), finalObj);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });

                end_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() { //END SPINNER
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        try {
                            finalObj.put("end", adapterView.getSelectedItem().toString());
                            object_store.put(weeks.get(finalX).substring(0, 3).toLowerCase(), finalObj);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });

                interval_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() { //INTERVAL SPINNER
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        try {
                            finalObj.put("interval", adapterView.getSelectedItem().toString());
                            object_store.put(weeks.get(finalX).substring(0, 3).toLowerCase(), finalObj);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });

                enable_disable_staus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() { //ENABLE DISABLE SPINNER
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        try {
                            if (adapterView.getSelectedItem().toString().equals("Enabled")) {
                                weekstatus = "1";
                                finalObj.put("status", weekstatus);
                                object_store.put(weeks.get(finalX).substring(0, 3).toLowerCase(), finalObj);
                            } else {
                                if (adapterView.getSelectedItem().toString().equals("Disabled")) {
                                    weekstatus = "0";
                                    finalObj.put("status", weekstatus);
                                    object_store.put(weeks.get(finalX).substring(0, 3).toLowerCase(), finalObj);
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });
                storehourspinners.addView(view);
            }
            resetvalues.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    store_name.setText("");
                    store_manager_name.setText("");
                    getStore_manager_email.setText("");
                    store_address.setText("");
                    location_field.setText("");
                    MultiVendor_Store_selected_location.setText("");
                    country.setText("");
                    status.setSelection(0);
                    store_latitude.setText("");
                    store_longitude.setText("");
                    store_city.setText("");
                    store_state.setText("");
                    postal_code.setText("");
                    contact_number.setText("");
//                    shipping_price.setText("");
                    storehourspinners.removeAllViews();
                    JSONObject obj1;
                    View view1;
                    TextView weekday1;
                    Spinner enable_disable_staus1;
                    Spinner start_spinner1;
                    Spinner end_spinner1;
                    Spinner interval_spinner1;

                    for (int x = 0; x < 7; x++) {
                        obj1 = new JSONObject();
                        view1 = View.inflate(Ced_MultiVendor_NewStore.this, R.layout.ced_multivendor_storehour_spinners, null);
                        interval_spinner1 = view1.findViewById(R.id.MultiVendor_store_interval_spinner);
                        start_spinner1 = view1.findViewById(R.id.MultiVendor_store_start_spinner);
                        enable_disable_staus1 = view1.findViewById(R.id.MultiVendor_store_status_spinner);
                        end_spinner1 = view1.findViewById(R.id.MultiVendor_store_end_spinner);
                        weekday1 = view1.findViewById(R.id.MultiVendor_store_weekday);
                        weekday1.setText(weeks.get(x));

                        final JSONObject finalObj1 = obj1;
                        final int finalX = x;

                        start_spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() { //START SPINNER
                            @Override
                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                try {
                                    finalObj1.put("start", adapterView.getSelectedItem().toString());
                                    object_store.put(weeks.get(finalX).substring(0, 3).toLowerCase(), finalObj1);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> adapterView) {

                            }
                        });

                        try {
                            finalObj1.put("start", start_spinner1.getSelectedItem().toString());
                            object_store.put(weeks.get(finalX).substring(0, 3).toLowerCase(), finalObj1);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        end_spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() { //END SPINNER
                            @Override
                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                try {
                                    finalObj1.put("end", adapterView.getSelectedItem().toString());
                                    object_store.put(weeks.get(finalX).substring(0, 3).toLowerCase(), finalObj1);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> adapterView) {

                            }
                        });

                        try {
                            finalObj1.put("end", end_spinner1.getSelectedItem().toString());
                            object_store.put(weeks.get(finalX).substring(0, 3).toLowerCase(), finalObj1);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        interval_spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() { //INTERVAL SPINNER
                            @Override
                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                try {
                                    finalObj1.put("interval", adapterView.getSelectedItem().toString());
                                    object_store.put(weeks.get(finalX).substring(0, 3).toLowerCase(), finalObj1);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> adapterView) {

                            }
                        });

                        try {
                            finalObj1.put("interval", interval_spinner1.getSelectedItem().toString());
                            object_store.put(weeks.get(finalX).substring(0, 3).toLowerCase(), finalObj1);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        try {
                            weekstatus = "0";
                            finalObj1.put("status", weekstatus);
                            object_store.put(weeks.get(finalX).substring(0, 3).toLowerCase(), finalObj1);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        enable_disable_staus1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() { //ENABLE DISABLE SPINNER
                            @Override
                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                try {
                                    if (adapterView.getSelectedItem().toString().equals("Enabled")) {
                                        weekstatus = "1";
                                        finalObj1.put("status", weekstatus);
                                        object_store.put(weeks.get(finalX).substring(0, 3).toLowerCase(), finalObj1);
                                    } else {
                                        if (adapterView.getSelectedItem().toString().equals("Disabled")) {
                                            weekstatus = "0";
                                            finalObj1.put("status", weekstatus);
                                            object_store.put(weeks.get(finalX).substring(0, 3).toLowerCase(), finalObj1);
                                        }
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> adapterView) {

                            }
                        });
                        storehourspinners.addView(view1);
                    }
                }
            });

            //PICK LOCATION
            pick_my_location.setOnClickListener(view12 -> {
                try {
                    Intent location = new Intent(getApplicationContext(), Ced_MapActivity.class);
                    location.putExtra("from","Ced_MultiVendor_NewStore");
                    location.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    startActivity(location);
                    overridePendingTransition(R.anim.ced_multivendor_slide_in, R.anim.ced_multivendor_slide_out);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            });
            savenewstore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (is_active.equals("0") || is_active.equals("1")) {
                        if (isValidateEmail(getStore_manager_email.getText().toString())) {
                            Iterator iterator = object_store.keys();
                            while (iterator.hasNext()) {
                                try {
                                    String key = (String) iterator.next();
                                    listweek.put(key);
                                    JSONObject obj = object_store.getJSONObject(key);
                                    liststart.put(obj.getString("start"));
                                    listend.put(obj.getString("end"));
                                    listinterval.put(obj.getString("interval"));
                                    liststatus.put(obj.getString("status"));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                            storeHashmap.put("vendor_id", session.getVendorid());
//                            storeHashmap.put("hashkey", getResources().getString(R.string.header));
                            storeHashmap.put("hashkey", session.getHahkey());
                            if (store_address.getText().toString().trim().isEmpty()) {
                                store_name.setError(getString(R.string.please_provide_store_address));
                                store_name.requestFocus();
                                return;
                            }
                            if (store_name.getText().toString().trim().isEmpty()) {
                                store_name.setError("please provide store name");
                                store_name.requestFocus();
                            } else {
                                if (store_manager_name.getText().toString().trim().isEmpty()) {
                                    store_manager_name.setError("please provide store manager name");
                                    store_manager_name.requestFocus();
                                } else {
                                    if (store_city.getText().toString().trim().isEmpty()) {
                                        store_city.setError("please provide store city name");
                                        store_city.requestFocus();
                                    } else {
                                        if (store_state.getText().toString().trim().isEmpty()) {
                                            store_state.setError("please provide store state name");
                                            store_state.requestFocus();
                                        } else {
                                            if (postal_code.getText().toString().trim().isEmpty()) {
                                                postal_code.setError("please provide Postal Code.");
                                                postal_code.requestFocus();
                                            } else {
                                                if (contact_number.getText().toString().trim().isEmpty()) {
                                                    contact_number.setError("please provide Contact Number Code.");
                                                    contact_number.requestFocus();
                                                } else {
                                                    /*if (shipping_price.getText().toString().trim().isEmpty()) {
                                                        shipping_price.setError("please provide Shipping Price.");
                                                        shipping_price.requestFocus();
                                                    } else {*/
                                                    storeHashmap.put("store_name", store_name.getText().toString());
                                                    storeHashmap.put("store_manager_name", store_manager_name.getText().toString());
                                                    storeHashmap.put("store_manager_email", getStore_manager_email.getText().toString());
                                                    storeHashmap.put("store_city", store_city.getText().toString());
                                                    storeHashmap.put("store_state", store_state.getText().toString());
                                                    //storeHashmap.put("store_address",store_address.getText().toString());
                                                    storeHashmap.put("store_address", store_address.getText().toString());
                                                    if (listend.length() > 0 && liststart.length() > 0 && liststatus.length() > 0 && listweek.length() > 0 && listinterval.length() > 0) {
                                                        storeHashmap.put("days", listweek.toString());
                                                        storeHashmap.put("start", liststart.toString());
                                                        storeHashmap.put("end", listend.toString());
                                                        storeHashmap.put("interval", listinterval.toString());
                                                        storeHashmap.put("status", liststatus.toString());
                                                    } else {
                                                        handleTabStoreHourInfoVisibility();
                                                        return;
                                                    }
                                                    storeHashmap.put("store_zcode", postal_code.getText().toString());
                                                    storeHashmap.put("latitude", store_latitude.getText().toString());
                                                    storeHashmap.put("longitude", store_longitude.getText().toString());
                                                    storeHashmap.put("store_phone", contact_number.getText().toString());
//                                                        storeHashmap.put("shipping_price", shipping_price.getText().toString());
                                                    storeHashmap.put("store_country", country_code);
                                                    storeHashmap.put("is_active", is_active);
                                                    Ced_MultiVendor_ClientRequestResponse response = new Ced_MultiVendor_ClientRequestResponse(new AsyncResponse() {
                                                        @Override
                                                        public void processFinish(Object output) {
                                                            try {
                                                                String string = output.toString();
                                                                JSONObject object = new JSONObject(string);
                                                                JSONObject data = object.getJSONObject("data");
                                                                String success = "";
                                                                if (data.has("success") && data.getBoolean("success")) {
                                                                    success = data.getString("success");
                                                                    Toast.makeText(Ced_MultiVendor_NewStore.this, data.getString("message"), Toast.LENGTH_SHORT).show();
                                                                    Intent storelisting = new Intent(Ced_MultiVendor_NewStore.this, Ced_MultiVendor_StoreListing.class);
                                                                    storelisting.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                                                    startActivity(storelisting);
                                                                    overridePendingTransition(R.anim.ced_multivendor_slide_in, R.anim.ced_multivendor_slide_out);
                                                                    finish();
                                                                }
                                                                Toast.makeText(Ced_MultiVendor_NewStore.this, data.getString("message"), Toast.LENGTH_SHORT).show();
                                                            } catch (JSONException e) {
                                                                e.printStackTrace();
                                                            }
                                                        }
                                                    }, Ced_MultiVendor_NewStore.this, "POST", storeHashmap);
                                                    response.execute(createstore_url);
//                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        } else {
                            Log.e("frozen", "isValidateEmail=false= " + isValidateEmail(getStore_manager_email.getText().toString()));
                            getStore_manager_email.setError(getResources().getString(R.string.invalidemail));
                            getStore_manager_email.requestFocus();
                        }
                    } else {
                        Toast.makeText(Ced_MultiVendor_NewStore.this, "Select status", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } else {
            Intent nointernet = new Intent(getApplicationContext(), Ced_MultiVendor_NoInternetconnection.class);
            nointernet.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            nointernet.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(nointernet);
            overridePendingTransition(R.anim.ced_multivendor_slide_in, R.anim.ced_multivendor_slide_out);
        }
    }

    private void handleTabStoreHourInfoVisibility() {
        tab_store_hourinfo.setTextColor(getResources().getColor(R.color.white));
        tab_store_hourinfo.setBackgroundColor(getResources().getColor(R.color.AppTheme));
        tab_store_basicinfo.setBackgroundColor(getResources().getColor(R.color.white));
        tab_store_basicinfo.setTextColor(getResources().getColor(R.color.textcolor));
        scrollView_tab_store_basicinfo.setVisibility(View.GONE);
        linearView_tab_store_hourinfo.setVisibility(View.VISIBLE);
    }

    private void handleTabBasicInfoVisibility() {
        tab_store_basicinfo.setTextColor(getResources().getColor(R.color.white));
        tab_store_basicinfo.setBackgroundColor(getResources().getColor(R.color.AppTheme));
        tab_store_hourinfo.setBackgroundColor(getResources().getColor(R.color.white));
        tab_store_hourinfo.setTextColor(getResources().getColor(R.color.textcolor));
        scrollView_tab_store_basicinfo.setVisibility(View.VISIBLE);
        linearView_tab_store_hourinfo.setVisibility(View.GONE);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                PlacePicker.getPlace(imageReturnedIntent, this);
//                LatLng getlatLng = place.getLatLng();
//                store_latitude.setText("" + getlatLng.latitude);
//                store_longitude.setText("" + getlatLng.longitude);
            }
        }
        if (resultCode == RESULT_OK) {
            try {
                if (location) {
                    location = false;
                    Place place = Autocomplete.getPlaceFromIntent(imageReturnedIntent);
                    Log.i(TAG, "Place: " + place.getName() + ", " + place.getId());
                    if (mMap == null) {
                        Location mLocation = new Location("");
                        mLocation.setLatitude(place.getLatLng().latitude);
                        mLocation.setLongitude(place.getLatLng().longitude);
                        currentLocation = mLocation;
/*                        SupportMapFragment supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
                        assert supportMapFragment != null;
                        supportMapFragment.getMapAsync(Ced_MultiVendor_EditVendorProfileDynamic.this);*/
                        startIntentService(mLocation);
                        getPlaceInfo(place.getLatLng().latitude, place.getLatLng().longitude);
                        mResultReceiver = new AddressResultReceiver(new Handler());
                    } else {
                        mMap.animateCamera(CameraUpdateFactory.newLatLng(place.getLatLng()));
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(place.getLatLng(), 18f));
                        Location mLocation = new Location("");
                        mLocation.setLatitude(place.getLatLng().latitude);
                        mLocation.setLongitude(place.getLatLng().longitude);
                        startIntentService(mLocation);
                        getPlaceInfo(place.getLatLng().latitude, place.getLatLng().longitude);
                    }
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    protected void startIntentService(Location mLocation) {
        Intent intent = new Intent(this, Ced_FetchAddressIntentService.class);
        intent.putExtra(Constants.RECEIVER, mResultReceiver);
        intent.putExtra(Constants.LOCATION_DATA_EXTRA, mLocation);
        startService(intent);
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(GoogleMap googleMap) {
        Log.d(TAG, "OnMapReady");
        mMap = googleMap;
        mMap.setMyLocationEnabled(true);
        mMap.isMyLocationEnabled();
        mCenterLatLong = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
        mMap.animateCamera(CameraUpdateFactory.newLatLng(mCenterLatLong));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(mCenterLatLong, 18f));
        mMap.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener() {
            @Override
            public void onCameraIdle() {
                mCenterLatLong = mMap.getCameraPosition().target;
                Location mLocation = new Location("");
                mLocation.setLatitude(mCenterLatLong.latitude);
                mLocation.setLongitude(mCenterLatLong.longitude);
                startIntentService(mLocation);
            }
        });
    }

    private void getPlaceInfo(double lat, double lon) throws IOException {
        Geocoder mGeocoder = new Geocoder(this);
        List<Address> addresses = mGeocoder.getFromLocation(lat, lon, 1);

        if (addresses.get(0).getPostalCode() != null) {
            String ZIP = addresses.get(0).getPostalCode();
            Log.d("ZIP CODE", ZIP);
        }

        if (addresses.get(0).getLocality() != null) {
            String City = addresses.get(0).getLocality();
            Log.d("CITY", City);
        }

        if (addresses.get(0).getAdminArea() != null) {
            String state = addresses.get(0).getAdminArea();
            Log.d("STATE", state);
        }

        if (addresses.get(0).getCountryName() != null) {
            String country = addresses.get(0).getCountryName();
            Log.d("COUNTRY", country);
        }

        if (addresses.get(0).getAddressLine(0) != null) {
            String getAddressLine = addresses.get(0).getAddressLine(0).toString().replace(addresses.get(0).getFeatureName(), "");
            Log.d("getAddressLine", getAddressLine);
        }

        if (addresses.get(0).getCountryCode() != null) {
            String CountryCode = addresses.get(0).getCountryCode();
            Log.d("CountryCode", CountryCode);
        }
    }

    class AddressResultReceiver extends ResultReceiver {
        AddressResultReceiver(Handler handler) {
            super(handler);
        }

        @SuppressLint("SetTextI18n")
        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {
            super.onReceiveResult(resultCode, resultData);
            if (!setdeliverylocation.equals("")) {
                Log.d(TAG, "txt_location1:" + setdeliverylocation.replace(", null", ""));
//                txt_location.setText(setdeliverylocation.replace(", null", ""));
            } else {
                Log.d(TAG, "txt_location2:" + resultData.getString("location_result").replace(", null", ""));
//                txt_location.setText(resultData.getString("location_result").replace(", null", ""));
            }
//            ced_sessionManagement.save_tool_address(txt_location.getText().toString());
            if (resultData.getDouble("latitude") != 0 && resultData.getDouble("longitude") != 0) {
                try {
                    getPlaceInfo(resultData.getDouble("latitude"), resultData.getDouble("longitude"));
                    mResultReceiver = new AddressResultReceiver(new Handler());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            //  Toast.makeText(mContext, "" + resultData.getString("location_result"), Toast.LENGTH_SHORT).show();
        }
    }

    private void getcountry() {
        try {
            Ced_MultiVendor_ClientRequestResponse crr = new Ced_MultiVendor_ClientRequestResponse(new AsyncResponse() {
                @Override
                public void processFinish(Object output) throws JSONException {
                    if (functionalityList.getExtensionAddon()) {
                        JSONObject jsonObject = new JSONObject(output.toString());
                        if (jsonObject.has("header") && jsonObject.getString("header").equals("false")) {
                            Toast.makeText(getApplicationContext(), getResources().getString(R.string.url_not_found), Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(getApplicationContext(), Ced_MultiVendor_UnAuthourizedRequestError.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        } else {
                            JSONArray jsonArray = jsonObject.getJSONArray("country");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject object = jsonArray.getJSONObject(i);
                                if (!(object.getString("value").isEmpty())) {
                                    countrycodelist.add(object.getString("value"));
                                    countrylabellist.add(object.getString("label"));
                                    mapcountrylabel.put(object.getString("label").trim(), object.getString("value"));
                                    if (getResources().getString(R.string.Enable_Log).equals("yes")) {
                                        Log.i("countrycodelist", "" + countrycodelist);
                                        Log.i("countrylabellist", "" + countrylabellist);
                                        Log.i("mapcountrylabel", "" + mapcountrylabel);
                                    }
                                }
                            }
                            if (mapcountrylabel.containsKey(country.getText().toString())) {
                                country_code = mapcountrylabel.get(country.getText().toString());
                            }
                            if (getResources().getString(R.string.Enable_Log).equals("yes")) {
                                Log.i("country_code_first", "" + country_code);
                            }
                        }
                    } else {
                        Intent intent = new Intent(getApplicationContext(), Ced_MultiVendor_UnAuthourizedRequestError.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                }
            }, Ced_MultiVendor_NewStore.this);
            crr.execute(countries_url);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean isValidateEmail(String username) {
        String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(username);
        return matcher.matches();
    }

    public static Ced_MultiVendor_NewStore getInstance() {
        return ced_multiVendor_newStore;
    }

    public void updateLocationAndLatitudeAndLongitude(String location, String latitude, String longitude) {
        store_address.setText(location);
        MultiVendor_Store_selected_location.setText(location);
        store_latitude.setText(latitude);
        store_longitude.setText(longitude);
    }
}