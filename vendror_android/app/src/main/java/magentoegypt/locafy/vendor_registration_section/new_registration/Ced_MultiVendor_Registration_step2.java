package magentoegypt.locafy.vendor_registration_section.new_registration;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.annotation.Nullable;

import android.text.Editable;
import android.text.Html;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import magentoegypt.locafy.base_app.Ced_MultiVendor_FontSetting;
import magentoegypt.locafy.vendor_dashboard.Ced_MultiVendor_VendorDashboard;
import magentoegypt.locafy.base_app.Ced_MultiVendor_VendorFunctionalityList;
import magentoegypt.locafy.api_request_response_section.network_error_handling.Ced_MultiVendor_ConnectionDetector;
import magentoegypt.locafy.api_request_response_section.network_error_handling.Ced_MultiVendor_NoInternetconnection;
import magentoegypt.locafy.api_request_response_section.network_error_handling.Ced_MultiVendor_UnAuthourizedRequestError;
import magentoegypt.locafy.api_request_response_section.AsyncResponse;
import magentoegypt.locafy.api_request_response_section.Ced_MultiVendor_ClientRequestResponse;
import magentoegypt.locafy.vendor_session.Ced_MultiVendor_VendorSessionManagement;
import magentoegypt.locafy.R;
import magentoegypt.locafy.base_app.GPSLocationProvider;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

public class Ced_MultiVendor_Registration_step2 extends Activity {
    Ced_MultiVendor_ConnectionDetector connectionDetector;
    Ced_MultiVendor_VendorSessionManagement vendorSessionManagement;
    Ced_MultiVendor_VendorFunctionalityList functionalityList;
    Ced_MultiVendor_FontSetting fontSetting;
    String validate_pincode_url, get_category_url, pickupsave_url;
    HashMap<String, String> post_data;
    ArrayList<String> statelabellist;
    ArrayList<String> statecodelist;
    String state_code, state_label;
    Timer timer;
    int level_value = 0;
    ArrayList<String> trackSelectedChecks;
    HashSet<String> selected_category_ids;
    String last_category;
    LinearLayout category_layout;
    String selected_categoies = "";
    Boolean pincode_verified = false;
    HashMap<String, String> post_params;
    String isConfirmationRequired;
    String message, firstname, lastname;
    private EditText MultiVendor_zipcode, MultiVendor_vendor_street, MultiVendor_GSTIN, MultiVendor_PAN, MultiVendor_AccountHolderName, MultiVendor_AccountNumber, MultiVendor_IFSCCode, MultiVendor_BankName, MultiVendor_BankBranch;
    private Button btn_verify_number, btn_submit;
    private EditText MultiVendor_country_spnr, MultiVendor_state_spnr;
    private Spinner MultiVendor_city_spnr;
    private TextView MultiVendor_message_output, MultiVendor_catgory_spnr;
    private GPSLocationProvider gpsLocationProvider;
    private final boolean isRunning = true;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        connectionDetector = new Ced_MultiVendor_ConnectionDetector(getApplicationContext());
        vendorSessionManagement = new Ced_MultiVendor_VendorSessionManagement(getApplicationContext());
        functionalityList = new Ced_MultiVendor_VendorFunctionalityList(Ced_MultiVendor_Registration_step2.this);
        fontSetting = new Ced_MultiVendor_FontSetting();
        post_data = new HashMap<>();
        post_params = new HashMap<>();
        timer = new Timer();
        trackSelectedChecks = new ArrayList<>();
        selected_category_ids = new HashSet<>();
        validate_pincode_url = vendorSessionManagement.getBase_Url() + "vendorapi/seller/validatepickuppincode";
        get_category_url = vendorSessionManagement.getBase_Url() + "vendorapi/seller/categories";
        pickupsave_url = vendorSessionManagement.getBase_Url() + "vendorapi/seller/pickupsave";

        if (getIntent().hasExtra("isConfirmationRequired")) {
            isConfirmationRequired = getIntent().getStringExtra("isConfirmationRequired");
        }
        if (isConfirmationRequired.equals("YES")) {
            message = getIntent().getStringExtra("message");
            firstname = getIntent().getStringExtra("firstname");
            lastname = getIntent().getStringExtra("lastname");
        }

        if (connectionDetector.isConnectingToInternet()) {
            setContentView(R.layout.registration_second_step);

            MultiVendor_zipcode = findViewById(R.id.MultiVendor_zipcode);
            MultiVendor_vendor_street = findViewById(R.id.MultiVendor_vendor_street);
            MultiVendor_GSTIN = findViewById(R.id.MultiVendor_GSTIN);
            MultiVendor_PAN = findViewById(R.id.MultiVendor_PAN);
            MultiVendor_AccountHolderName = findViewById(R.id.MultiVendor_AccountHolderName);
            MultiVendor_AccountNumber = findViewById(R.id.MultiVendor_AccountNumber);
            MultiVendor_IFSCCode = findViewById(R.id.MultiVendor_IFSCCode);
            MultiVendor_BankName = findViewById(R.id.MultiVendor_BankName);
            MultiVendor_BankBranch = findViewById(R.id.MultiVendor_BankBranch);

            btn_verify_number = findViewById(R.id.btn_verify_number);
            btn_submit = findViewById(R.id.btn_submit);

            MultiVendor_country_spnr = findViewById(R.id.MultiVendor_country_spnr);
            MultiVendor_state_spnr = findViewById(R.id.MultiVendor_state_spnr);
            MultiVendor_city_spnr = findViewById(R.id.MultiVendor_city_spnr);
            MultiVendor_catgory_spnr = findViewById(R.id.MultiVendor_catgory_spnr);

            MultiVendor_message_output = findViewById(R.id.MultiVendor_message_output);
            category_layout = findViewById(R.id.category_layout);

            fontSetting.setFontforTextviews(MultiVendor_catgory_spnr, "Roboto-Medium.ttf", Ced_MultiVendor_Registration_step2.this);
            fontSetting.setFontforTextviews(MultiVendor_message_output, "Roboto-Medium.ttf", Ced_MultiVendor_Registration_step2.this);


            fontSetting.setfontforEditText(MultiVendor_zipcode, "Roboto-Medium.ttf", Ced_MultiVendor_Registration_step2.this);
            fontSetting.setfontforEditText(MultiVendor_vendor_street, "Roboto-Medium.ttf", Ced_MultiVendor_Registration_step2.this);
            fontSetting.setfontforEditText(MultiVendor_GSTIN, "Roboto-Medium.ttf", Ced_MultiVendor_Registration_step2.this);
            fontSetting.setfontforEditText(MultiVendor_PAN, "Roboto-Medium.ttf", Ced_MultiVendor_Registration_step2.this);
            fontSetting.setfontforEditText(MultiVendor_AccountHolderName, "Roboto-Medium.ttf", Ced_MultiVendor_Registration_step2.this);
            fontSetting.setfontforEditText(MultiVendor_AccountNumber, "Roboto-Medium.ttf", Ced_MultiVendor_Registration_step2.this);
            fontSetting.setfontforEditText(MultiVendor_IFSCCode, "Roboto-Medium.ttf", Ced_MultiVendor_Registration_step2.this);
            fontSetting.setfontforEditText(MultiVendor_BankName, "Roboto-Medium.ttf", Ced_MultiVendor_Registration_step2.this);
            fontSetting.setfontforEditText(MultiVendor_BankBranch, "Roboto-Medium.ttf", Ced_MultiVendor_Registration_step2.this);

            fontSetting.setfontforButtons(btn_verify_number, "Roboto-Medium.ttf", Ced_MultiVendor_Registration_step2.this);
            fontSetting.setfontforButtons(btn_submit, "Roboto-Medium.ttf", Ced_MultiVendor_Registration_step2.this);


            getlocation(MultiVendor_vendor_street);
            getState();
            fetchCategory();

            MultiVendor_state_spnr.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final CharSequence[] arrayOfInt = (CharSequence[]) statecodelist.toArray(new CharSequence[statecodelist.size()]);
                    final CharSequence[] arrayOfInt2 = (CharSequence[]) statelabellist.toArray(new CharSequence[statelabellist.size()]);
                    Dialog levelDialog1 = new Dialog(Ced_MultiVendor_Registration_step2.this);
                    final AlertDialog.Builder builder = new AlertDialog.Builder(Ced_MultiVendor_Registration_step2.this);
                    builder.setTitle(Html.fromHtml("<font color='#000000'>Select Your  STATE:</font>"));
                    builder.setSingleChoiceItems(arrayOfInt2, -1, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int postion) {
                                    state_code = (String) arrayOfInt[postion];
                                    state_label = (String) arrayOfInt2[postion];
                                    MultiVendor_state_spnr.setText(state_label);
                                    dialog.dismiss();
                                    getCity(state_code);
                                }
                            }

                    );
                    levelDialog1 = builder.create();
                    levelDialog1.show();
                }
            });

            MultiVendor_city_spnr.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    Log.d("params", "" + MultiVendor_city_spnr.getSelectedItem().toString());
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });

            MultiVendor_AccountNumber.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    if (s.length() < 7) {
                        MultiVendor_AccountNumber.setError(getResources().getString(R.string.min_char_account_number));
                        MultiVendor_AccountNumber.requestFocus();
                    }
                }
            });

            MultiVendor_AccountHolderName.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    if (s.length() < 5) {
                        MultiVendor_AccountHolderName.setError(getResources().getString(R.string.min_char_account_holdername));
                        MultiVendor_AccountHolderName.requestFocus();
                    }
                }
            });
            MultiVendor_BankName.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    if (s.length() < 4) {
                        MultiVendor_BankName.setError(getResources().getString(R.string.min_char_account_bankname));
                        MultiVendor_BankName.requestFocus();
                    }
                }
            });


            btn_verify_number.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!(MultiVendor_zipcode.getText().length() < 6)) {
                        post_data.put("pincode", MultiVendor_zipcode.getText().toString());

                        Ced_MultiVendor_ClientRequestResponse response = new Ced_MultiVendor_ClientRequestResponse(new AsyncResponse() {
                            @Override
                            public void processFinish(Object output) throws JSONException {

                                JSONObject main = new JSONObject(output.toString());
                                JSONObject data = main.getJSONObject("data");
                                JSONArray customer = data.getJSONArray("customer");


                                if (customer.getJSONObject(0).getString("success").equalsIgnoreCase("true")) {
                                    Toast.makeText(Ced_MultiVendor_Registration_step2.this, "" + customer.getJSONObject(0).getString("message"), Toast.LENGTH_SHORT).show();
                                    MultiVendor_message_output.setText(customer.getJSONObject(0).getString("message"));
                                    MultiVendor_message_output.setTextColor(getResources().getColor(R.color.green));
                                    MultiVendor_message_output.setVisibility(View.VISIBLE);
                                    pincode_verified = true;

                                } else {
                                    Toast.makeText(Ced_MultiVendor_Registration_step2.this, "" + customer.getJSONObject(0).getString("message"), Toast.LENGTH_SHORT).show();
                                    MultiVendor_message_output.setText(customer.getJSONObject(0).getString("message"));
                                    MultiVendor_message_output.setTextColor(getResources().getColor(R.color.red));
                                    MultiVendor_message_output.setVisibility(View.VISIBLE);
                                }

                            }
                        }, Ced_MultiVendor_Registration_step2.this, "POST", post_data);

                        response.execute(validate_pincode_url);
                    } else {
                        MultiVendor_message_output.setText(getResources().getString(R.string.invalidpincode));
                        MultiVendor_message_output.setTextColor(getResources().getColor(R.color.red));
                        MultiVendor_message_output.setVisibility(View.VISIBLE);
                    }
                }
            });

        } else {
            Intent intent = new Intent(getApplicationContext(), Ced_MultiVendor_NoInternetconnection.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);

        }


        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!pincode_verified) {
                    Toast.makeText(Ced_MultiVendor_Registration_step2.this, getResources().getString(R.string.invalidpincode), Toast.LENGTH_LONG).show();
                } else if (MultiVendor_vendor_street.getText().toString().isEmpty()) {
                    Toast.makeText(Ced_MultiVendor_Registration_step2.this, getResources().getString(R.string.empty_address), Toast.LENGTH_LONG).show();
                } else if (MultiVendor_country_spnr.getText().toString().isEmpty()) {
                } else if (MultiVendor_state_spnr.getText().toString().isEmpty()) {
                    Toast.makeText(Ced_MultiVendor_Registration_step2.this, getResources().getString(R.string.pleaseselectastate), Toast.LENGTH_LONG).show();
                } else if (MultiVendor_city_spnr.getSelectedItem().toString().isEmpty()) {
                    Toast.makeText(Ced_MultiVendor_Registration_step2.this, getResources().getString(R.string.pleaseselectacity), Toast.LENGTH_LONG).show();
                } else if (selected_categoies.isEmpty()) {
                    Toast.makeText(Ced_MultiVendor_Registration_step2.this, getResources().getString(R.string.pleaseselectcategory), Toast.LENGTH_LONG).show();
                } else if (MultiVendor_GSTIN.getText().toString().isEmpty()) {
                    Toast.makeText(Ced_MultiVendor_Registration_step2.this, getResources().getString(R.string.pleaseenterGSTIN), Toast.LENGTH_LONG).show();
                } else if (MultiVendor_PAN.getText().toString().isEmpty()) {
                    Toast.makeText(Ced_MultiVendor_Registration_step2.this, getResources().getString(R.string.pleaseenterPAN), Toast.LENGTH_LONG).show();
                } else if (MultiVendor_AccountHolderName.getText().toString().isEmpty()) {
                    Toast.makeText(Ced_MultiVendor_Registration_step2.this, getResources().getString(R.string.pleaseenteAccountHolderName), Toast.LENGTH_LONG).show();
                } else if (MultiVendor_AccountNumber.getText().toString().isEmpty()) {
                    Toast.makeText(Ced_MultiVendor_Registration_step2.this, getResources().getString(R.string.pleaseenteAccountNumber), Toast.LENGTH_LONG).show();
                } else if (MultiVendor_IFSCCode.getText().toString().isEmpty()) {
                    Toast.makeText(Ced_MultiVendor_Registration_step2.this, getResources().getString(R.string.pleaseenterIFSC), Toast.LENGTH_LONG).show();
                } else if (MultiVendor_BankName.getText().toString().isEmpty()) {
                    Toast.makeText(Ced_MultiVendor_Registration_step2.this, getResources().getString(R.string.pleaseenterBankName), Toast.LENGTH_LONG).show();
                } else if (MultiVendor_BankBranch.getText().toString().isEmpty()) {
                    Toast.makeText(Ced_MultiVendor_Registration_step2.this, getResources().getString(R.string.pleaseenterBankBranch), Toast.LENGTH_LONG).show();
                } else {
                    post_params.put("zip_code", MultiVendor_zipcode.getText().toString());
                    post_params.put("address", MultiVendor_vendor_street.getText().toString());
                    post_params.put("country_id", "IN");
                    post_params.put("region_id", state_code);
                    post_params.put("city-select", MultiVendor_city_spnr.getSelectedItem().toString());
                    post_params.put("catagories", selected_categoies);
                    post_params.put("gstin", MultiVendor_GSTIN.getText().toString());
                    post_params.put("personal_pan", MultiVendor_PAN.getText().toString());
                    post_params.put("account_holder_name", MultiVendor_AccountHolderName.getText().toString());
                    post_params.put("account_number", MultiVendor_AccountNumber.getText().toString());
                    post_params.put("ifsc_code", MultiVendor_IFSCCode.getText().toString());
                    post_params.put("vendor_id", vendorSessionManagement.getVendorid());
                    post_params.put("hashkey", vendorSessionManagement.getHahkey());
                    post_params.put("bank_name", MultiVendor_BankName.getText().toString());
                    post_params.put("bank_branch", MultiVendor_BankBranch.getText().toString());


                    Ced_MultiVendor_ClientRequestResponse response = new Ced_MultiVendor_ClientRequestResponse(new AsyncResponse() {
                        @Override
                        public void processFinish(Object output) throws JSONException {

                            JSONObject object = new JSONObject(output.toString());
                            if (object.getJSONObject("data").getJSONArray("customer").getJSONObject(0).getString("success").equalsIgnoreCase("true")) {
                                if (isConfirmationRequired.equals("YES")) {

                                    Intent intent = new Intent(getApplicationContext(), Ced_MultiVendor_VendorApprovalRequired.class);

                                    intent.putExtra("message", message);
                                    intent.putExtra("firstname", firstname);
                                    intent.putExtra("lastname", lastname);

                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);
                                    overridePendingTransition(R.anim.ced_multivendor_slide_in, R.anim.ced_multivendor_slide_out);
                                } else {
                                    Intent intent = new Intent(getApplicationContext(), Ced_MultiVendor_VendorDashboard.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);
                                    overridePendingTransition(R.anim.ced_multivendor_slide_in, R.anim.ced_multivendor_slide_out);
                                }
                            } else {
                                Toast.makeText(Ced_MultiVendor_Registration_step2.this, object.getJSONObject("data").getJSONArray("customer").getJSONObject(0).getString("message"), Toast.LENGTH_LONG).show();
                            }

                        }
                    }, Ced_MultiVendor_Registration_step2.this, "POST", post_params);
                    response.execute(pickupsave_url);
                }
            }
        });


    }

    private void fetchCategory() {

        Ced_MultiVendor_ClientRequestResponse response = new Ced_MultiVendor_ClientRequestResponse(new AsyncResponse() {
            @Override
            public void processFinish(Object output) throws JSONException {
                JSONArray category = new JSONArray(output.toString());
                if (category.length() > 0) {
                    JSONObject current_object = null;
                    View category_view = null;
                    TextView subcategories = null;
                    TextView level = null;
                    CheckBox main_cat = null;
                    ImageView cat_icon = null;
                    String maincatname = null;
                    String maincatid = null;
                    String[] parts = new String[2];

                    for (int i = 0; i < category.length(); i++) {
                        current_object = category.getJSONObject(i);
                        category_view = View.inflate(Ced_MultiVendor_Registration_step2.this, R.layout.ced_multivendor_maincategory, null);
                        subcategories = (TextView) category_view.findViewById(R.id.subcategories);
                        level = (TextView) category_view.findViewById(R.id.level);
                        main_cat = (CheckBox) category_view.findViewById(R.id.main_cat);
                        cat_icon = (ImageView) category_view.findViewById(R.id.cat_icon);
                        maincatname = current_object.getString("name");
                        maincatid = current_object.getString("id");
                        fontSetting.setfontforCheckbox(main_cat, "Roboto-Medium.ttf", Ced_MultiVendor_Registration_step2.this);
                        level.setText(level_value + "");

                        if (current_object.has("sub_categories")) {
                            subcategories.setText(current_object.getJSONArray("sub_categories").toString());

                        } else {
                            cat_icon.setVisibility(View.GONE);

                        }
                        main_cat.setText(maincatname);
                        main_cat.setId(Integer.parseInt(maincatid));
                        final CheckBox finalMain_cat = main_cat;
                        final TextView finalLevel = level;
                        final CheckBox finalMain_cat1 = main_cat;
                        final TextView finalLevel1 = level;
                        final boolean[] open = {false};
                        final ImageView finalCat_icon = cat_icon;
                        final ImageView finalCat_icon1 = cat_icon;

                        main_cat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                            @Override
                            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                if (isChecked) {

                                    selected_category_ids.add(String.valueOf(finalMain_cat.getId()));
                                    trackSelectedChecks.add(finalLevel1.getText().toString() + "");
                                    last_category = String.valueOf(finalMain_cat.getId());


                                } else {
                                    selected_category_ids.remove(String.valueOf(finalMain_cat.getId()));
                                    trackSelectedChecks.remove(finalLevel1.getText().toString() + "");
                                }


                                selected_categoies = TextUtils.join(",", selected_category_ids);

                                Log.d("check", "" + trackSelectedChecks);
                                Log.d("check ids", "" + selected_category_ids);
                                Log.i("asdasd", TextUtils.join(",", selected_category_ids));


                            }
                        });

                        category_layout.addView(category_view);
                    }
                }


            }
        }, Ced_MultiVendor_Registration_step2.this);
        response.execute(get_category_url);
    }

    private void getCity(String state_code) {
        String get_city = vendorSessionManagement.getBase_Url() + "rest/V1/mobiconnect/module/getcity";

        Ced_MultiVendor_ClientRequestResponse crr = new Ced_MultiVendor_ClientRequestResponse(new AsyncResponse() {
            @Override
            public void processFinish(Object output) throws JSONException {

                ArrayList citylabellist;
                ArrayList citycodelist;
                final HashMap label_codes_city;
                HashMap id_codes_city;

                citylabellist = new ArrayList<String>();
                citycodelist = new ArrayList<String>();
                label_codes_city = new HashMap<String, String>();
                id_codes_city = new HashMap<String, String>();
                JSONArray main = new JSONArray(output.toString());
                JSONObject inner = main.getJSONObject(0);
                JSONArray cities = inner.getJSONArray("cities");
                JSONObject inner_json;

                for (int i = 0; i < cities.length(); i++) {
                    inner_json = cities.getJSONObject(i);
                    citylabellist.add(inner_json.getString("label"));
                    citycodelist.add(inner_json.getString("city_id"));
                    label_codes_city.put(inner_json.getString("label"), inner_json.getString("city_id"));
                }
                ArrayAdapter<String> dropdown = new ArrayAdapter<String>(Ced_MultiVendor_Registration_step2.this, R.layout.spinner_textview, citylabellist);
                MultiVendor_city_spnr.setAdapter(dropdown);
            }


        }, Ced_MultiVendor_Registration_step2.this);
        crr.execute(get_city + "/" + state_code);


    }

    private void getlocation(final EditText multiVendor_vendor_street) {
        LocationManager service = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        boolean enabled = service
                .isProviderEnabled(LocationManager.GPS_PROVIDER);
        if (enabled) {
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    gpsLocationProvider = new GPSLocationProvider(Ced_MultiVendor_Registration_step2.this);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (isRunning) {
                                getAddressFromLocation(GPSLocationProvider.latitude, GPSLocationProvider.longitude, multiVendor_vendor_street);

                            } else {
                                timer.cancel();
                                timer.purge();
                            }

                        }
                    });

                }
            }, 100, 500);

        }

        /*gpsLocationProvider = new GPSLocationProvider(Ced_MultiVendor_Registration_step2.this);*/

    }

    private void getAddressFromLocation(double latitude, double longitude, EditText multiVendor_vendor_street) {

        Geocoder geocoder = new Geocoder(this, Locale.ENGLISH);

        try {
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);

            if (addresses.size() > 0) {

                Address fetchedAddress = addresses.get(0);
                String address_data = fetchedAddress.getAddressLine(0) + " , " +
                        fetchedAddress.getAdminArea() + " , " + fetchedAddress.getLocality() + " , " + fetchedAddress.getPostalCode();

                // isRunning = false;
                multiVendor_vendor_street.setText(address_data);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    private void getState() {
        String countryurl = vendorSessionManagement.getBase_Url() + "vendorapi/index/getcountry/";

        HashMap<String, String> dataforStates = new HashMap<>();
        dataforStates.put("vendor_id", vendorSessionManagement.getVendorid());
        dataforStates.put("hashkey", vendorSessionManagement.getHahkey());
        dataforStates.put("country_code", "IN");

        Ced_MultiVendor_ClientRequestResponse response = new Ced_MultiVendor_ClientRequestResponse(new AsyncResponse() {
            @Override
            public void processFinish(Object output) throws JSONException {
                if (functionalityList.getExtensionAddon()) {
                    statelabellist = new ArrayList<String>();
                    statecodelist = new ArrayList<String>();
                    JSONObject object = new JSONObject(output.toString());
                    Boolean status = object.getBoolean("success");
                    if (status) {
                        JSONArray jsonArray = object.getJSONArray("states");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject c = jsonArray.getJSONObject(i);
                            statecodelist.add(c.getString("region_id"));
                            statelabellist.add(c.getString("default_name"));

                        }

                    }
                } else {
                    Intent intent = new Intent(getApplicationContext(), Ced_MultiVendor_UnAuthourizedRequestError.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
            }
        }, Ced_MultiVendor_Registration_step2.this, "POST", dataforStates);
        response.execute(countryurl);
    }
}
