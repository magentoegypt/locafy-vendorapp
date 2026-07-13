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
package magentoegypt.locafy.vendor_registration_section.new_registration;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.Editable;
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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import magentoegypt.locafy.api_request_response_section.RestNotificatioRequest;
import magentoegypt.locafy_constant.Ced_MultiVendor_GlobalVariables;
import magentoegypt.locafy.vendor_dashboard.Ced_MultiVendor_VendorDashboard;
import magentoegypt.locafy.base_app.Ced_MultiVendor_VendorFunctionalityList;
import magentoegypt.locafy.api_request_response_section.network_error_handling.Ced_MultiVendor_ConnectionDetector;
import magentoegypt.locafy.api_request_response_section.network_error_handling.Ced_MultiVendor_NoInternetconnection;
import magentoegypt.locafy.api_request_response_section.network_error_handling.Ced_MultiVendor_UnAuthourizedRequestError;
import magentoegypt.locafy.api_request_response_section.AsyncResponse;
import magentoegypt.locafy.api_request_response_section.Ced_ClientRequestResponse;
import magentoegypt.locafy.api_request_response_section.Ced_ClientRequestResponseRest;
import magentoegypt.locafy.api_request_response_section.Ced_MultiVendor_ClientRequestResponse;
import magentoegypt.locafy.vendor_session.Ced_MultiVendor_VendorSessionManagement;
import magentoegypt.locafy.R;
import magentoegypt.locafy_constant.AppConstant;
import magentoegypt.locafy.base_app.PasswordStrength;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Ced_MultiVendor_VendorRegistration extends Activity implements TextWatcher {
    CheckBox signupfornews;
    EditText vendor_firstname;
    EditText vendor_lastname;
    EditText vendor_email;
    EditText vendor_publicname;
    EditText vendor_shopurl;
    EditText vendor_password;
    EditText vendor_confirmpassword;
    TextView vendor_shopurltag;
    TextView MultiVendor_vendor_phone;
    Button submit;
    LinearLayout verify_resend_section;
    JSONObject registerjson;
    JSONArray insideregister;
    String Jstring;
    Ced_MultiVendor_VendorFunctionalityList functionalityList;
    HashMap<String, String> dataforregister;
    String CurrrentUrl;
    Ced_MultiVendor_ConnectionDetector connectionDetector;
    Ced_MultiVendor_VendorSessionManagement vendorSessionManagement;
    String get_login_page_data = "";
    JSONObject jsonObject;
    JSONArray data;
    String name, label, type, options;
    ArrayList<HashMap<String, String>> dynamic_values;
    HashMap<String, ArrayList> create_data;
    LinearLayout linea_dynamic;
    EditText MultiVendor_vendor_DOB, MultiVendor_vendor_taxvat, MultiVendor_vendor_middlename;
    RadioButton radioButton;
    LinearLayout linearLayout;
    RadioGroup gender_group;
    Calendar newCalendar;
    String validate_phone_number;
    CheckBox confirmation_check;
    Button btn_finish;
    String email, phone_number;
    Button btn_sendotp;
    Button btn_resendotp;
    Button verifyotp;
    Button btn_verify_number;
    EditText MultiVendor_vendor_otp;
    HashMap<String, String> map_reques_otp;
    HashMap<String, String> map_send_otp;
    HashMap<String, String> map_verify_otp;
    String send_otp_url;
    String verify_otp_url;
    Boolean termsconditios = false;
    String phonenumber;
    String emaill;
    String otp;
    Spinner country_spinner;
    ArrayList<String> list_country;
    HashMap<String, String> map_country;
    String country_id;
    String getCountry;
    ProgressBar progressBar;
    TextView strengthView;
    LinearLayout strenth_lyt;
    private SimpleDateFormat dateFormatter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        connectionDetector = new Ced_MultiVendor_ConnectionDetector(getApplicationContext());
        list_country = new ArrayList<>();
        map_country = new HashMap<>();

        if (connectionDetector.isConnectingToInternet()) {
            setContentView(R.layout.ced_multivendor_activity_vendor_registration);
            signupfornews = findViewById(R.id.MultiVendor_signupfornews);
            vendorSessionManagement = new Ced_MultiVendor_VendorSessionManagement(getApplicationContext());
            int id = Resources.getSystem().getIdentifier("btn_check_holo_light", "drawable", "android");
            signupfornews.setButtonDrawable(id);
            registerjson = new JSONObject();
            insideregister = new JSONArray();
            functionalityList = new Ced_MultiVendor_VendorFunctionalityList(Ced_MultiVendor_VendorRegistration.this);
            CurrrentUrl = vendorSessionManagement.getBase_Url() + "vendorapi/index/create";
            get_login_page_data = vendorSessionManagement.getBase_Url() + "rest/V1/mobiconnect/customer/getRequiredFields";

            validate_phone_number = vendorSessionManagement.getBase_Url() + "vendorapi/index/validateNumber";

            send_otp_url = vendorSessionManagement.getBase_Url() + "vendorapi/index/sendotp";

            verify_otp_url = vendorSessionManagement.getBase_Url() + "vendorapi/index/verifyOtp";

            getCountry = vendorSessionManagement.getBase_Url() + "rest/V1/mobiconnect/customer/getRequiredFields";

            country_spinner = findViewById(R.id.MultiVendor_country_spnr);
            dataforregister = new HashMap<String, String>();
            dynamic_values = new ArrayList<>();
            progressBar = findViewById(R.id.progressBar);
            strengthView = findViewById(R.id.password_strength);
            strenth_lyt = findViewById(R.id.strenth_lyt);
            vendor_firstname = findViewById(R.id.MultiVendor_vendor_firstname);
            vendor_lastname = findViewById(R.id.MultiVendor_vendor_lastname);
            vendor_email = findViewById(R.id.MultiVendor_vendor_email);
            linea_dynamic = findViewById(R.id.linea_dynamic);

            vendor_publicname = findViewById(R.id.MultiVendor_vendor_publicname);
            vendor_shopurl = findViewById(R.id.MultiVendor_vendor_shopurl);
            vendor_password = findViewById(R.id.MultiVendor_vendor_password);
            vendor_password.addTextChangedListener(this);
            vendor_confirmpassword = findViewById(R.id.MultiVendor_vendor_confirmpassword);
            vendor_shopurltag = findViewById(R.id.MultiVendor_vendor_shopurltag);
            MultiVendor_vendor_DOB = findViewById(R.id.MultiVendor_vendor_DOB);
            MultiVendor_vendor_taxvat = findViewById(R.id.MultiVendor_vendor_taxvat);
            linearLayout = findViewById(R.id.gender_layout);
            gender_group = findViewById(R.id.gender_group);
            MultiVendor_vendor_middlename = findViewById(R.id.MultiVendor_vendor_middlename);
            MultiVendor_vendor_DOB.setFocusable(false);
            MultiVendor_vendor_DOB.setClickable(true);
            btn_sendotp = findViewById(R.id.btn_sendotp);
            btn_verify_number = findViewById(R.id.btn_verify_number);
            btn_resendotp = findViewById(R.id.btn_resendotp);
            verifyotp = findViewById(R.id.verifyotp);
            MultiVendor_vendor_phone = (EditText) findViewById(R.id.MultiVendor_vendor_phone);
            MultiVendor_vendor_otp = findViewById(R.id.MultiVendor_vendor_otp);
            submit = findViewById(R.id.MultiVendor_submit);

            verify_resend_section = findViewById(R.id.verify_resend_section);


            //  submit.setEnabled(false);
            btn_resendotp.setEnabled(false);
            btn_resendotp.setBackgroundColor(getResources().getColor(R.color.ebebebe));

            map_reques_otp = new HashMap<String, String>();
            map_send_otp = new HashMap<String, String>();
            map_verify_otp = new HashMap<String, String>();

            email = getIntent().getStringExtra("email");
            phone_number = getIntent().getStringExtra("number");
            country_id = getIntent().getStringExtra("country_id");


            bind_country_request();

            Log.d("Email", "->" + phone_number);
            Log.d("Phone Number", "->" + phone_number);

            vendor_email.setText(email);
            MultiVendor_vendor_phone.setText(phone_number);

            btn_verify_number.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AppConstant.lockButton(view);
                    phonenumber = MultiVendor_vendor_phone.getText().toString();
                    emaill = vendor_email.getText().toString();


                    if (MultiVendor_vendor_phone.getText().toString().length() > 0) {
                        //Toast.makeText(Ced_MultiVendor_VendorRegistration.this, ""+phonenumber, Toast.LENGTH_SHORT).show();
                        map_reques_otp.put("mobile_number", phonenumber);
                        map_reques_otp.put("country_id", "IN");
                        validate_number();

                    } else {
                        Toast.makeText(Ced_MultiVendor_VendorRegistration.this, R.string.please_enter_mobile_num_validation, Toast.LENGTH_SHORT).show();
                    }
                }
            });


            btn_resendotp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AppConstant.lockButton(view);
                    send_otp();
                }
            });

            btn_sendotp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AppConstant.lockButton(view);
                    phonenumber = MultiVendor_vendor_phone.getText().toString();
                    emaill = vendor_email.getText().toString();


                    if (phonenumber.length() > 0) {
                        //Toast.makeText(Ced_MultiVendor_VendorRegistration.this, ""+phonenumber, Toast.LENGTH_SHORT).show();

                       /* map_reques_otp.put("mobile",phonenumber);

                        validate_number()*/

                        MultiVendor_vendor_otp.setVisibility(View.VISIBLE);


                        map_send_otp.put("mobile_number", phonenumber);
                        //  map_send_otp.put("email", emaill);
                        //map_send_otp.put("country_id", country_id);
                        map_send_otp.put("country_id", "IN");

                        send_otp();

                    } else {
                        Toast.makeText(Ced_MultiVendor_VendorRegistration.this, "Please provide Email and Phone Number ", Toast.LENGTH_SHORT).show();
                    }

                }
            });

            verifyotp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AppConstant.lockButton(view);
                    otp = MultiVendor_vendor_otp.getText().toString().trim();

                    if (otp.length() > 0) {
                        map_verify_otp.put("mobile_number", phonenumber);
                        map_verify_otp.put("country_id", "IN");
                        map_verify_otp.put("email", emaill);
                        map_verify_otp.put("otp", otp);
                        verifyotp();
                    } else {
                        Toast.makeText(Ced_MultiVendor_VendorRegistration.this, "Please enter OTP", Toast.LENGTH_SHORT).show();
                    }
                }
            });

            vendor_publicname.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View view, boolean hasFocus) {
                    if (!hasFocus && vendor_publicname.length() > 0) {
                        // Toast.makeText(Ced_MultiVendor_VendorRegistration.this, "-"+getSaltString(), Toast.LENGTH_SHORT).show();
                        String public_url = vendor_publicname.getText().toString().trim();
                        vendor_shopurl.setText(public_url.toLowerCase() + "-" + getSaltString());
                    } else {
                        vendor_shopurl.setText("");
                    }
                }
            });

            if (linearLayout.getVisibility() == View.VISIBLE) {
                gender_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup group, int checkedId) {
                        radioButton = findViewById(checkedId);
                        if (radioButton.getText().toString().equals("Female")) {
                            try {
                                registerjson.put("gender", "2");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        if (radioButton.getText().toString().equals("Male")) {
                            try {
                                registerjson.put("gender", "1");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });
            }
            MultiVendor_vendor_DOB.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AppConstant.lockButton(v);
                    newCalendar = Calendar.getInstance();
                    dateFormatter = new SimpleDateFormat("MM/dd/yyyy", Locale.US);
                    new DatePickerDialog(Ced_MultiVendor_VendorRegistration.this, new DatePickerDialog.OnDateSetListener() {

                        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                            newCalendar.set(Calendar.YEAR, year);
                            newCalendar.set(Calendar.MONTH, monthOfYear);
                            newCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                            MultiVendor_vendor_DOB.setText(dateFormatter.format(newCalendar.getTime()));


                        }

                    }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH)).show();
                }
            });
            request_data();

            submit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AppConstant.lockButton(v);
                    if (vendor_firstname.getText().toString().trim().isEmpty()) {
                        vendor_firstname.setError(getResources().getString(R.string.pleaseprovidefirstname));
                        vendor_firstname.requestFocus();
                    } else {
                        if (vendor_lastname.getText().toString().trim().isEmpty()) {
                            vendor_lastname.setError(getResources().getString(R.string.pleaseprovidelastname));
                            vendor_lastname.requestFocus();
                        } else {
                            if (vendor_email.getText().toString().trim().isEmpty()) {
                                vendor_email.setError(getResources().getString(R.string.pleaseprovideemail));
                                vendor_email.requestFocus();
                            } else {
                                if (isValidateEmail(vendor_email.getText().toString().trim())) {
                                    if (vendor_publicname.getText().toString().trim().isEmpty()) {
                                        vendor_publicname.setError(getResources().getString(R.string.pleaseprovidepublicname));
                                        vendor_publicname.requestFocus();
                                    } else {
                                        if (vendor_shopurl.getText().toString().trim().isEmpty()) {
                                            vendor_shopurl.setError(getResources().getString(R.string.pleaseprovideshopurl));
                                            vendor_shopurl.requestFocus();
                                        } else {
                                            if (vendor_password.getText().toString().trim().isEmpty()) {
                                                vendor_password.setError(getResources().getString(R.string.pleaseenterpassword));
                                                vendor_password.requestFocus();
                                            } else {
                                                if (vendor_password.getText().toString().trim().isEmpty()) {
                                                    vendor_password.setError(getResources().getString(R.string.shortpass));
                                                    vendor_password.requestFocus();
                                                } else {
                                                    if (vendor_confirmpassword.getText().toString().trim().isEmpty()) {
                                                        vendor_confirmpassword.setError(getResources().getString(R.string.pleaseenterpassword));
                                                        vendor_confirmpassword.requestFocus();
                                                    } else {
                                                        if (vendor_confirmpassword.getText().toString().trim().equals(vendor_password.getText().toString())) {
                                                            try {
                                                                if (MultiVendor_vendor_DOB.getVisibility() == View.VISIBLE) {
                                                                    registerjson.put("dob", MultiVendor_vendor_DOB.getText().toString());
                                                                }
                                                                if (MultiVendor_vendor_middlename.getVisibility() == View.VISIBLE) {
                                                                    registerjson.put("middlename", MultiVendor_vendor_middlename.getText().toString());
                                                                }
                                                                if (MultiVendor_vendor_taxvat.getVisibility() == View.VISIBLE) {
                                                                    registerjson.put("taxvat", MultiVendor_vendor_taxvat.getText().toString());
                                                                }
                                                                registerjson.put("email", vendor_email.getText().toString());
                                                                registerjson.put("firstname", vendor_firstname.getText().toString());
                                                                registerjson.put("lastname", vendor_lastname.getText().toString());
                                                                registerjson.put("password", vendor_password.getText().toString());
                                                                registerjson.put("is_subscribed", signupfornews.isChecked());
                                                                registerjson.put("mobile", phonenumber);
                                                                registerjson.put("is_vendor", 1);
                                                                JSONArray insideregister = new JSONArray();
                                                                JSONObject publicnamejson = new JSONObject();
                                                                publicnamejson.put("key", "public_name");
                                                                publicnamejson.put("value", vendor_publicname.getText().toString());
                                                                insideregister.put(publicnamejson);
                                                                JSONObject shopurljson = new JSONObject();
                                                                shopurljson.put("key", "shop_url");
                                                                shopurljson.put("value", vendor_shopurl.getText().toString());
                                                                insideregister.put(shopurljson);
                                                                JSONObject mobile_details = new JSONObject();
                                                                mobile_details.put("key", "contact_number");
                                                                mobile_details.put("value", MultiVendor_vendor_phone.getText().toString());
                                                                insideregister.put(mobile_details);
                                                                JSONObject country_details = new JSONObject();
                                                                country_details.put("key", "country_id");
                                                                //country_details.put("value", country_id);
                                                                country_details.put("value", "IN");
                                                                insideregister.put(country_details);
                                                                registerjson.put("vendor", insideregister);
                                                                dataforregister.put("createaccount", registerjson.toString());
                                                                try {
                                                                    Ced_MultiVendor_ClientRequestResponse crr = new Ced_MultiVendor_ClientRequestResponse(new AsyncResponse() {
                                                                        @Override
                                                                        public void processFinish(Object output) throws JSONException {
                                                                            Jstring = output.toString();
                                                                            if (functionalityList.getExtensionAddon()) {
                                                                                JSONObject mainobject = new JSONObject(Jstring);
                                                                                JSONObject vendorobject = mainobject.getJSONObject("data");
                                                                                JSONArray vendorarray = vendorobject.getJSONArray("customer");
                                                                                JSONObject vendordata = vendorarray.getJSONObject(0);
                                                                                if (vendordata.getString("success").equals("true")) {
                                                                                    if (vendordata.has("hashkey")) {
                                                                                        if (vendordata.has("isConfirmationRequired")) {
                                                                                            if (vendordata.getString("isConfirmationRequired").equals("NO")) {
                                                                                                Toast.makeText(getApplicationContext(), vendordata.getString("message"), Toast.LENGTH_LONG).show();
                                                                                                vendorSessionManagement.createLoginSession(vendordata.getString("hashkey"), vendor_email.getText().toString());
                                                                                                vendorSessionManagement.saveVendorId(vendordata.getString("vendor_id"));
                                                                                                vendorSessionManagement.saveCustomerId(vendordata.getString("customer_id"));
                                                                                                vendorSessionManagement.savevendorname(vendordata.getString("vendor_name"));
                                                                                                vendorSessionManagement.savevendorpic(vendordata.getString("profile_picture"));
                                                                                                sendRegistrationToServer(vendorSessionManagement.getDeviceToken(), vendordata.getString("vendor_id"));
                                                                                                Ced_MultiVendor_GlobalVariables.progress = Integer.parseInt(vendordata.getString("profile_complete"));
                                                                                                Ced_MultiVendor_GlobalVariables.noti = vendordata.getString("valerts");
                                                                                                Intent intent = new Intent(getApplicationContext(), Ced_MultiVendor_VendorDashboard.class);
                                                                                                intent.putExtra("isConfirmationRequired", "NO");
                                                                                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                                                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                                                                                startActivity(intent);
                                                                                                overridePendingTransition(R.anim.ced_multivendor_slide_in, R.anim.ced_multivendor_slide_out);
                                                                                            }
                                                                                        }
                                                                                    } else {
                                                                                        if (vendordata.has("isConfirmationRequired")) {
                                                                                            if (vendordata.getString("isConfirmationRequired").equals("YES")) {
//                                                                                               // Intent intent = new Intent(getApplicationContext(), Ced_MultiVendor_VendorApprovalRequired.class);
                                                                                                //Intent intent = new Intent(getApplicationContext(), Ced_MultiVendor_Registration_step2.class);
                                                                                                Intent intent = new Intent(getApplicationContext(), Ced_MultiVendor_VendorApprovalRequired.class);
                                                                                                intent.putExtra("isConfirmationRequired", "YES");
                                                                                                intent.putExtra("message", vendordata.getString("message"));
                                                                                                intent.putExtra("firstname", vendor_firstname.getText().toString());
                                                                                                intent.putExtra("lastname", vendor_lastname.getText().toString());
                                                                                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                                                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                                                                                startActivity(intent);
                                                                                                overridePendingTransition(R.anim.ced_multivendor_slide_in, R.anim.ced_multivendor_slide_out);
                                                                                            }
                                                                                        } else {
                                                                                            Intent intent = new Intent(getApplicationContext(), Ced_MultiVendor_VendorApprovalRequired.class);
                                                                                            intent.putExtra("isConfirmationRequired", "YES");
                                                                                            intent.putExtra("message", vendordata.getString("message"));
                                                                                            intent.putExtra("firstname", vendor_firstname.getText().toString());
                                                                                            intent.putExtra("lastname", vendor_lastname.getText().toString());
                                                                                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                                                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                                                                            startActivity(intent);
                                                                                            overridePendingTransition(R.anim.ced_multivendor_slide_in, R.anim.ced_multivendor_slide_out);
                                                                                        }
                                                                                    }
                                                                                } else {
                                                                                    Toast.makeText(getApplicationContext(), vendordata.getString("message"), Toast.LENGTH_LONG).show();
                                                                                    /*Intent intent = new Intent(getApplicationContext(), Ced_MultiVendor_VendorApprovalRequired.class);
                                                                                    intent.putExtra("isConfirmationRequired", "YES");
                                                                                    intent.putExtra("message", vendordata.getString("message"));
                                                                                    intent.putExtra("firstname", vendor_firstname.getText().toString());
                                                                                    intent.putExtra("lastname", vendor_lastname.getText().toString());
                                                                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                                                                    startActivity(intent);
                                                                                    overridePendingTransition(R.anim.ced_multivendor_slide_in, R.anim.ced_multivendor_slide_out);*/
                                                                                }

                                                                            } else {
                                                                                Intent intent = new Intent(getApplicationContext(), Ced_MultiVendor_UnAuthourizedRequestError.class);
                                                                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                                                                startActivity(intent);
                                                                                overridePendingTransition(R.anim.ced_multivendor_slide_in, R.anim.ced_multivendor_slide_out);
                                                                            }
                                                                        }
                                                                    }, Ced_MultiVendor_VendorRegistration.this, "POST", dataforregister);
                                                                    crr.execute(CurrrentUrl);

                                                                } catch (Exception e) {
                                                                    e.printStackTrace();
                                                                }
                                                            } catch (JSONException e) {
                                                                e.printStackTrace();
                                                            }
                                                        } else {
                                                            vendor_password.setError(getResources().getString(R.string.passnotmatch));
                                                            vendor_confirmpassword.setError(getResources().getString(R.string.passnotmatch));
                                                            vendor_confirmpassword.requestFocus();
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                } else {
                                    vendor_email.setError(getResources().getString(R.string.invalidemail));
                                }
                            }
                        }
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

    private void request_data() {
        Ced_ClientRequestResponseRest response = new Ced_ClientRequestResponseRest(new AsyncResponse() {
            @Override
            public void processFinish(Object output) throws JSONException {
                Jstring = output.toString();
                getLogin_page_data();
            }
        }, Ced_MultiVendor_VendorRegistration.this);
        response.execute(get_login_page_data);

    }


    private void getLogin_page_data() throws JSONException {
        jsonObject = new JSONObject(Jstring);
        if (jsonObject.getBoolean("success")) {
            data = jsonObject.getJSONArray("data");
            create_data = new HashMap<>();
            for (int i = 0; i < data.length(); i++) {
                JSONObject c = null;
                c = data.getJSONObject(i);

                if (c.has("name")) {
                    String name = c.getString("name");
                    String value = c.getString(name);


                    if (name.equals("dob")) {

                        if (value.equals("true")) {

                            MultiVendor_vendor_DOB.setVisibility(View.VISIBLE);
                        } else {

                            MultiVendor_vendor_DOB.setVisibility(View.GONE);
                        }
                    }
                    if (name.equals("taxvat")) {

                        if (value.equals("true")) {

                            MultiVendor_vendor_taxvat.setVisibility(View.VISIBLE);
                        } else {

                            MultiVendor_vendor_taxvat.setVisibility(View.GONE);
                        }
                    }
                    if (name.equals("gender")) {

                        if (value.equals("true")) {

                            linearLayout.setVisibility(View.VISIBLE);
                        } else {
                            linearLayout.setVisibility(View.GONE);
                        }
                    }

                    if (name.equals("middlename")) {
                        if (value.equals("true")) {
                            MultiVendor_vendor_middlename.setVisibility(View.VISIBLE);
                        } else {
                            MultiVendor_vendor_middlename.setVisibility(View.GONE);
                        }

                    }
                }

                if (c.has(label)) {
                    label = c.getString("label");

                }
                if (c.has(name + "_options")) {
                    options = c.getString(name + "_options");

                }

            }

        }
    }

    private boolean isValidateEmail(String username) {
        String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(username);
        return matcher.matches();
    }

    private boolean isValidatePassword(String username) {
        String EMAIL_PATTERN = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$";
        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(username);
        return matcher.matches();
    }

    @Override
    protected void onResume() {
        Ced_MultiVendor_ConnectionDetector connectionDetector = new Ced_MultiVendor_ConnectionDetector(getApplicationContext());
        if (connectionDetector.isConnectingToInternet()) {

            super.onResume();
        } else {
            Intent intent = new Intent(getApplicationContext(), Ced_MultiVendor_NoInternetconnection.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            super.onResume();
        }
    }

    private String getSaltString() {
        String SALTCHARS = "abcdefghijklmnopqrstuvwxyz1234567890";
        StringBuilder salt = new StringBuilder();
        Random rnd = new Random();
        while (salt.length() <= 4) { // length of the random string.
            int index = (int) (rnd.nextFloat() * SALTCHARS.length());
            salt.append(SALTCHARS.charAt(index));

        }
        String saltStr = salt.toString();

        Log.d("Vaibhav89606", "" + saltStr);

        return saltStr;

    }

    private void validate_number() {
        Ced_ClientRequestResponse response = new Ced_ClientRequestResponse(new AsyncResponse() {
            @Override
            public void processFinish(Object output) throws JSONException {
                JSONObject main = new JSONObject(output.toString());
                JSONObject data = main.getJSONObject("data");
                JSONArray customer = data.getJSONArray("customer");


                if (customer.getJSONObject(0).getString("status").equalsIgnoreCase("true")) {
                   /* map_send_otp.put("mobile",phonenumber);
                    map_send_otp.put("email",emaill);
                    map_send_otp.put("country_id","IN");*/

                    /***********************************************************/
                    // btn_verify_number.setBackgroundColor(getResources().getColor(R.color.green));
                    btn_verify_number.setText("Validated");
                    btn_verify_number.setClickable(false);
                    // btn_sendotp.setVisibility(View.VISIBLE);

                    /***********************************************************/

                    send_otp();
                } else {
                    Toast.makeText(Ced_MultiVendor_VendorRegistration.this, "" + customer.getJSONObject(0).getString("message"), Toast.LENGTH_SHORT).show();
                }

            }
        }, Ced_MultiVendor_VendorRegistration.this, "POST", map_reques_otp);
        response.execute(validate_phone_number);
    }

    private void send_otp() {
        Ced_ClientRequestResponse response = new Ced_ClientRequestResponse(new AsyncResponse() {
            @Override
            public void processFinish(Object output) throws JSONException {
                JSONObject main = new JSONObject(output.toString());
                JSONObject data = main.getJSONObject("data");
                JSONArray customer = data.getJSONArray("customer");


                if (customer.getJSONObject(0).getString("status").equalsIgnoreCase("true")) {
                    Toast.makeText(Ced_MultiVendor_VendorRegistration.this, "" + customer.getJSONObject(0).getString("message"), Toast.LENGTH_SHORT).show();
                    btn_sendotp.setVisibility(View.GONE);
                    verify_resend_section.setVisibility(View.VISIBLE);
                    MultiVendor_vendor_otp.setVisibility(View.VISIBLE);
                    btn_resendotp.setEnabled(true);
                    btn_resendotp.setBackgroundColor(getResources().getColor(R.color.AppTheme));

                } else {
                    Toast.makeText(Ced_MultiVendor_VendorRegistration.this, "" + customer.getJSONObject(0).getString("message"), Toast.LENGTH_SHORT).show();
                }
            }
        }, Ced_MultiVendor_VendorRegistration.this, "POST", map_reques_otp);
        response.execute(send_otp_url);
    }

    private void verifyotp() {
        Ced_ClientRequestResponse response = new Ced_ClientRequestResponse(new AsyncResponse() {
            @Override
            public void processFinish(Object output) throws JSONException {
                JSONObject main = new JSONObject(output.toString());
                JSONObject data = main.getJSONObject("data");
                JSONArray customer = data.getJSONArray("customer");


                if (customer.getJSONObject(0).getString("status").equalsIgnoreCase("true")) {
                    // showSellerAgreementPolicy();

                    MultiVendor_vendor_phone.setEnabled(false);

                    if (termsconditios) {
                      /*  submit.setBackground(getResources().getDrawable(R.drawable.button_style));
                        submit.setEnabled(true);*/

                        Log.d("89606inif", "" + termsconditios);
                    } else {
                        submit.setBackgroundColor(getResources().getColor(R.color.ebebebe));
                        // submit.setEnabled(false);
                        Log.d("89606inelse", "" + termsconditios);
                        showSellerAgreementPolicy();
                    }
                    Toast.makeText(Ced_MultiVendor_VendorRegistration.this, "" + customer.getJSONObject(0).getString("message"), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(Ced_MultiVendor_VendorRegistration.this, "" + customer.getJSONObject(0).getString("message"), Toast.LENGTH_SHORT).show();
                }
            }
        }, Ced_MultiVendor_VendorRegistration.this, "POST", map_verify_otp);
        response.execute(verify_otp_url);
    }


    private void showSellerAgreementPolicy() {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.magenative_seller_agreement_policy);
        dialog.setTitle(getResources().getString(R.string.termsconditions));
        confirmation_check = dialog.findViewById(R.id.confirmation_check);
        btn_finish = dialog.findViewById(R.id.btn_finish);

        btn_finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (confirmation_check.isChecked()) {
                    AppConstant.lockButton(view);
                    dialog.dismiss();
                }
            }
        });


        confirmation_check.setChecked(termsconditios);

        confirmation_check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {

                /*Toast.makeText(Ced_MultiVendor_VendorRegistration.this, ""+isChecked, Toast.LENGTH_SHORT).show();*/

                if (isChecked) {
                    termsconditios = isChecked;

                    submit.setBackground(getResources().getDrawable(R.drawable.button_style));
                    submit.setEnabled(true);
                } else {
                    termsconditios = isChecked;
                }
            }
        });

        dialog.show();
    }

    private void sendRegistrationToServer(String token, String vendor_id) {
        vendorSessionManagement = new Ced_MultiVendor_VendorSessionManagement(getApplicationContext());
        HashMap<String, String> jsonObject = new HashMap<>();
        jsonObject.put("Token", token);
        jsonObject.put("vendor_id", vendor_id);
        jsonObject.put("type", "2");
        RestNotificatioRequest request = new RestNotificatioRequest(new AsyncResponse() {
            @Override
            public void processFinish(Object output) throws JSONException {

            }
        }, getApplicationContext(), "POST", jsonObject);
        request.execute(vendorSessionManagement.getBase_Url() + "vendorapi/index/vendordevice");
    }

    private void bind_country_request() {
        Ced_ClientRequestResponseRest response = new Ced_ClientRequestResponseRest(new AsyncResponse() {
            @Override
            public void processFinish(Object output) throws JSONException {
                JSONObject main = new JSONObject(output.toString());
                JSONArray data;
                JSONObject jo;
                JSONArray Country;

                if (main.getString("success").equals("true")) {
                    data = main.getJSONArray("data");

                    for (int i = 0; i < data.length(); i++) {
                        jo = data.getJSONObject(i);
                        if (jo.has("country")) {
                            Country = jo.getJSONArray("country");

                            for (int j = 0; j < Country.length(); j++) {
                                list_country.add(Country.getJSONObject(j).getString("key"));
                                map_country.put(Country.getJSONObject(j).getString("key"), Country.getJSONObject(j).getString("value"));
                            }
                        }
                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(Ced_MultiVendor_VendorRegistration.this, R.layout.ced_multivendor_citytextview, R.id.text1, list_country);
                    country_spinner.setAdapter(adapter);

                    country_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            Iterator it = map_country.entrySet().iterator();
                            while (it.hasNext()) {
                                Map.Entry pair = (Map.Entry) it.next();
                                if (country_spinner.getSelectedItem().toString().equals(pair.getKey())) {
                                    country_id = pair.getValue().toString();
                                    break;
                                }
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {

                        }
                    });
                }

            }
        }, Ced_MultiVendor_VendorRegistration.this);
        response.execute(getCountry);
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        updatePasswordStrengthView(charSequence.toString());
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }

    private void updatePasswordStrengthView(String password) {
        if (password.length() > 0) {
            strenth_lyt.setVisibility(View.VISIBLE);
        } else {
            strenth_lyt.setVisibility(View.GONE);
        }
        if (TextView.VISIBLE != strengthView.getVisibility())
            return;
        if (password.isEmpty()) {
            strengthView.setText("");
            progressBar.setProgress(0);
            return;
        }
        PasswordStrength str = PasswordStrength.calculateStrength(password);
        strengthView.setText(str.getText(this));
        strengthView.setTextColor(str.getColor());

        progressBar.getProgressDrawable().setColorFilter(str.getColor(), android.graphics.PorterDuff.Mode.SRC_IN);
        if (str.getText(this).equals("Weak")) {
            progressBar.setProgress(25);
        } else if (str.getText(this).equals("Medium")) {
            progressBar.setProgress(50);
        } else if (str.getText(this).equals("Strong")) {
            progressBar.setProgress(75);
        } else {
            progressBar.setProgress(100);
        }
    }
}