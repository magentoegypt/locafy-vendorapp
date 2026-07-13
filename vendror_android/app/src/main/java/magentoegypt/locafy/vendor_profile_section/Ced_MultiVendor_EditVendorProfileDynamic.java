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

package magentoegypt.locafy.vendor_profile_section;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.READ_MEDIA_IMAGES;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.MenuItemCompat;
import androidx.lifecycle.ViewModelProviders;
import magentoegypt.locafy_constant.Ced_MultiVendor_GlobalVariables;
import magentoegypt.locafy.base_app.Ced_MultiVendor_VendorFunctionalityList;
import magentoegypt.locafy.base_app.UtilityMethods;
import magentoegypt.locafy.navigation_drawer.Activity.Ced_MultiVendor_NavigationActivity;
import magentoegypt.locafy.api_request_response_section.network_error_handling.Ced_MultiVendor_ConnectionDetector;
import magentoegypt.locafy.api_request_response_section.network_error_handling.Ced_MultiVendor_NoInternetconnection;
import magentoegypt.locafy.api_request_response_section.network_error_handling.Ced_MultiVendor_UnAuthourizedRequestError;
import magentoegypt.locafy.api_request_response_section.Ced_MultiVendor_ClientRequestResponse;
import magentoegypt.locafy.R;
import magentoegypt.locafy.vendor_login_section.mvvm.LoginViewModel;
import magentoegypt.locafy.vendor_notification.Notification;
import magentoegypt.locafy_constant.AppConstant;
import magentoegypt.locafy_constant.FileUtils;
import magentoegypt.locafy.base_app.PasswordStrength;
import magentoegypt.locafy.vendor_registration_section.ValidateAndSendOtp;
import magentoegypt.locafy.vendor_registration_section.new_registration.RegistrationDynamic;
import com.hbb20.CountryCodePicker;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static magentoegypt.locafy_constant.AppConstant.KEY_data;
import static magentoegypt.locafy_constant.AppConstant.KEY_field_name;
import static magentoegypt.locafy_constant.AppConstant.KEY_field_to_post;
import static magentoegypt.locafy_constant.AppConstant.KEY_is_required;
import static magentoegypt.locafy_constant.AppConstant.KEY_label;
import static magentoegypt.locafy_constant.AppConstant.KEY_options;
import static magentoegypt.locafy_constant.AppConstant.KEY_saved_value;
import static magentoegypt.locafy_constant.AppConstant.KEY_success;
import static magentoegypt.locafy_constant.AppConstant.KEY_type;
import static magentoegypt.locafy_constant.AppConstant.KEY_value;
import static magentoegypt.locafy_constant.AppConstant.pictureField;

public class Ced_MultiVendor_EditVendorProfileDynamic extends Ced_MultiVendor_NavigationActivity {
    private static final int REQUESTCODE_FILE_BROWSE = 3;
    private static final int REQUESTCODE_IMAGEBROWSE = 2;
    Ced_MultiVendor_ConnectionDetector connectionDetector;
    Ced_MultiVendor_VendorFunctionalityList functionalityList;

    HashMap<String, String> dataforvendorprofile;
    HashMap<String, String> dataforStates;
    LinearLayout profilefields;
    JSONObject statesJSONdata;

    TextView save;
    Calendar newCalendar;
    SimpleDateFormat dateFormatter;
    String encodedPicture = "";
    int checkboxDrawable;
    JSONObject requiredFields = null;

    Spinner bank_city, business_city;

    CheckBox change_password_checkbox;
    LinearLayout change_password_layout;
    EditText current_pass, new_pass, confirm_password;
    ScrollView edit_profile_scroll;
    ProgressBar progressbar;
    ProgressBar progressbar1;

    TextView strengthView, login_instructions;
    LinearLayout strenth_lyt;
    String file_value;
    String my_shop_url = "";
    String oldMobileNumber = "";
    boolean is_shop_url_done = false;
    ArrayList<String> statelabellist;
    ArrayList<String> statecodelist;
    HashMap<String, String> label_codes_states, id_codes_states;
    private String logouri;
    private String savedState="";
    CountryCodePicker codePicker;
    EditText edtUserNumber;
    ValidateAndSendOtp loginWithNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (android.os.Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(this.getResources().getColor(R.color.status_bar_color));
        }
        connectionDetector = new Ced_MultiVendor_ConnectionDetector(getApplicationContext());
        functionalityList = new Ced_MultiVendor_VendorFunctionalityList(getApplicationContext());
        dataforvendorprofile = new HashMap<>();
        dataforStates = new HashMap<>();
        requiredFields = new JSONObject();
        statesJSONdata = new JSONObject();

        dataforvendorprofile.put("vendor_id", session.getVendorid());
        dataforvendorprofile.put("hashkey", session.getHahkey());

        if (connectionDetector.isConnectingToInternet()) {
            ViewGroup content = findViewById(R.id.MultiVendor_frame_container);
            getLayoutInflater().inflate(R.layout.vendor_profile_update, content, true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.backbutton);

            if (session.getvendorname() != null) {
                setname(session.getvendorname());
                setprofilepic(session.getvendorpic());
                changetitle("Edit Profile");
            }

            checkboxDrawable = Resources.getSystem().getIdentifier("btn_check_holo_light", "drawable", "android");
            profilefields = findViewById(R.id.profilefields);
            save = findViewById(R.id.MultiVendor_save);

            edit_profile_scroll = findViewById(R.id.edit_profile_scroll);
            progressbar = findViewById(R.id.progressbar);
            progressbar1 = findViewById(R.id.progressBar);

            strengthView = findViewById(R.id.password_strength);
            strenth_lyt = findViewById(R.id.strenth_lyt);
            login_instructions = findViewById(R.id.login_instructions);
            change_password_checkbox = findViewById(R.id.change_password_checkbox);
            change_password_layout = findViewById(R.id.change_password_layout);
            current_pass = findViewById(R.id.current_pass);
            new_pass = findViewById(R.id.new_pass);
            confirm_password = findViewById(R.id.confirm_password);
            showHidePassword(findViewById(R.id.maskcurrent_pass),current_pass);
            showHidePassword(findViewById(R.id.masknew_pass),new_pass);
            showHidePassword(findViewById(R.id.maskconfirm_password),confirm_password);
            new_pass.addTextChangedListener(new TextWatcher() {
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
            });

            change_password_checkbox.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (isChecked) {
                    change_password_layout.setVisibility(View.VISIBLE);
                    current_pass.requestFocus();
                }
                else {
                    change_password_layout.setVisibility(View.GONE);
                    strenth_lyt.setVisibility(View.GONE);
                    login_instructions.setVisibility(View.GONE);

                }
            });

            save.setOnClickListener(v -> {
                AppConstant.lockButton(v);
                if (change_password_checkbox.isChecked()) {
                    if (current_pass.getText().toString().trim().isEmpty()) {
                        current_pass.setError(getResources().getString(R.string.empty));
                        current_pass.requestFocus();
                        try {
                            requiredFields.put("current", "Current Password");
                        }
                        catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    else if (new_pass.getText().toString().trim().isEmpty()) {
                        new_pass.setError(getResources().getString(R.string.empty));
                        new_pass.requestFocus();
                        try {
                            requiredFields.put("newpassword", "New Password");
                        }
                        catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    else if (!new_pass.getText().toString().trim().equalsIgnoreCase(confirm_password.getText().toString().trim())) {
                        confirm_password.setError(getResources().getString(R.string.passnotmatch));
                        confirm_password.requestFocus();
                        try {
                            requiredFields.put("confirm_pass", "Confirm Password");
                        }
                        catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    else {
                        requiredFields.remove("current");
                        requiredFields.remove("newpassword");
                        requiredFields.remove("confirm_pass");
                        dataforvendorprofile.put("currentpassword", current_pass.getText().toString());
                        dataforvendorprofile.put("newpassword", new_pass.getText().toString());
                    }
                }
                else {
                    requiredFields.remove("current");
                    requiredFields.remove("newpassword");
                    requiredFields.remove("confirm_pass");
                    dataforvendorprofile.remove("currentpassword");
                    dataforvendorprofile.remove("newpassword");
                }

                Log.i("REpo", "requiredFields=" + requiredFields);

                if (requiredFields.length() > 0) {
                    Log.i("REpo", "requiredFields" + requiredFields);
                    JSONArray required_values = requiredFields.names();
                    ArrayList<String> required_field_values = new ArrayList<>();
                    for (int i = 0; i < required_values.length(); i++) {
                        try {
                            String values = requiredFields.getString(required_values.get(i).toString());
                            required_field_values.add(values);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    //getString(R.string.please_check_these_fields_are_empty) + required_field_values
                    Toast.makeText(Ced_MultiVendor_EditVendorProfileDynamic.this,getString(R.string.required_field_validation) , Toast.LENGTH_SHORT).show();
                } else {
                    Log.i("REpo", "else requiredFields" + requiredFields);
                    if(!isLink(my_shop_url)){
                        UtilityMethods.showDialogOk(this,getResources().getString(R.string.enter_a_valid_shop_url_example_my_shop_url) + "  @#$%&*", new UtilityMethods.OnSelectedListener() {
                            @Override
                            public void onSelected(String value) {

                            }
                        });
                        return;
                    }
                    String newNumber = codePicker.getSelectedCountryCode()+edtUserNumber.getText().toString();
                    if(newNumber.equalsIgnoreCase(oldMobileNumber)) {
                        dataforvendorprofile.put("contact_number", oldMobileNumber);
                        updateprofile();
                    }
                    else if (edtUserNumber.getText().toString().isEmpty()) {
                        edtUserNumber.setError(getString(R.string.mobile_number_validation));
                    }else if (edtUserNumber.getText().toString().charAt(0) == '0') {
                        edtUserNumber.setError(getString(R.string.please_enter_mobile_number));
                    }else if (edtUserNumber.getText().toString().length() != 10) {
                        edtUserNumber.setError(getString(R.string.please_enter_the_valid_number));
                    }else{
                        loginWithNumber.showEnterNumberDialog(edtUserNumber.getText().toString(), codePicker.getSelectedCountryNameCode());
                    }
                }
                /*if (requiredFields.length() > 0) {
                    JSONArray required_values = requiredFields.names();
                    ArrayList<String> required_field_values = new ArrayList<>();
                    for (int i = 0; i < required_values.length(); i++) {
                        try {
                            required_field_values.add(requiredFields.getString(required_values.get(i).toString()) + "\n");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    //-----------------------
                    for (int k = 0; k < profilefields.getChildCount(); k++) {
                        Log.d("REpo", "" + profilefields.getChildAt(k));
                        if (profilefields.getChildAt(k) instanceof LinearLayout) {
                            LinearLayout linearLayout = (LinearLayout) profilefields.getChildAt(k);
                            for (int j = 0; j < linearLayout.getChildCount(); j++) {
                                if (linearLayout.getChildAt(j) instanceof TextView) {
                                    TextView textView_title = (TextView) linearLayout.getChildAt(j);
                                    for (int i = 0; i < required_field_values.size(); i++) {
                                        try {
                                            if (textView_title.getText().toString().equalsIgnoreCase(requiredFields.getString(required_values.get(i).toString()) + "*")) {
                                                if (linearLayout.getChildAt(j + 1) instanceof EditText) {
                                                    EditText text_data = (EditText) linearLayout.getChildAt(j + 1);
                                                    text_data.setError("Please fill " + textView_title.getText().toString());
                                                    text_data.requestFocus();
                                                }

                                            }
                                           *//* else
                                            {
                                                if(linearLayout.getChildAt(j+1) instanceof EditText)
                                                {
                                                    EditText text_data=(EditText)linearLayout.getChildAt(j+1);
                                                    text_data.setError(null);
                                                    text_data.clearFocus();
                                                }
                                            }*//*
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }
                            }
                        }
                    }
                    //-----------------------
                    Toast.makeText(Ced_MultiVendor_EditVendorProfileDynamic.this, "Please Check these fields are empty: \n" + required_field_values.toString().replace(",", "").replace("[", "").replace("]", ""), Toast.LENGTH_SHORT).show();
                } else {
                    Log.i("REpo", "else requiredFields" + requiredFields);
                    updateprofile();
                }*/
            });

            // getState("IN");
            getProfilefields();
            mobileNumberSignUp();
        }
    }

    void showHidePassword(ImageView maskpass,EditText password){
        maskpass.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View view) {
                boolean isPasswordVisible = password.getTransformationMethod() instanceof HideReturnsTransformationMethod;
                if (isPasswordVisible) {
                    maskpass.setImageDrawable(getDrawable(R.drawable.hide_eye));
                    password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                } else {
                    maskpass.setImageDrawable(getDrawable(R.drawable.eye));
                    password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }
                password.setSelection(password.getText().length());
            }
        });
    }

    private void mobileNumberSignUp() {
        LoginViewModel loginViewModel = ViewModelProviders.of(this).get((LoginViewModel.class));
        loginWithNumber = new ValidateAndSendOtp(this, loginViewModel) {
            @Override
            public void onError(@Nullable String msg) {
                Toast.makeText(Ced_MultiVendor_EditVendorProfileDynamic.this, msg, Toast.LENGTH_SHORT).show();
            }

            @Override
            protected void onOtpVerified(@Nullable String number) {
                dataforvendorprofile.put("contact_number", number);
               updateprofile();
            }
        };
        loginWithNumber.setCed_MultiVendor_EditVendorProfileDynamic(this);
        loginViewModel.error.observe(Ced_MultiVendor_EditVendorProfileDynamic.this, s -> Toast.makeText(Ced_MultiVendor_EditVendorProfileDynamic.this, s, Toast.LENGTH_SHORT).show());
        loginViewModel.loading.observe(Ced_MultiVendor_EditVendorProfileDynamic.this, this::updateLoader);


    }

    private void updateLoader(Boolean isLoading) {
//        if (isLoading) {
//            if (null != cedNewLoader)
//                cedNewLoader.show();
//        } else {
//            if ((cedNewLoader != null) && cedNewLoader.isShowing()) {
//                cedNewLoader.dismiss();
//            }
//        }
    }

    private void updateprofile() {
        Log.d("REpo", "updateprofile: "+dataforvendorprofile.toString());
        dataforvendorprofile.put("shop_url_done", "1");
        if (dataforvendorprofile.containsKey("region_id")&&!dataforvendorprofile.get("region_id").matches("-?(0|[1-9]\\d*)")){
            String tmp=dataforvendorprofile.get("region_id");
            dataforvendorprofile.remove("region_id");
            dataforvendorprofile.put("region",tmp);
        }
        else if (dataforvendorprofile.containsKey("region_id")&&dataforvendorprofile.get("region_id").matches("-?(0|[1-9]\\d*)")){
            dataforvendorprofile.remove("region");
        }

        String updateURL = session.getBase_Url() + "vendorapi/index/update";

        Ced_MultiVendor_ClientRequestResponse crr = new Ced_MultiVendor_ClientRequestResponse(output -> {
            if (functionalityList.getExtensionAddon()) {
                JSONObject data = new JSONObject(output.toString());
                if (data.has("vendor_approved")) {
                    logout();
                }
                else {
                    String sucess = data.getJSONObject("data").getString("success");
                    if (sucess.equals("true")) {
                        Ced_MultiVendor_GlobalVariables.noti = data.getJSONObject("data").getString("valerts");
                        Ced_MultiVendor_GlobalVariables.progress = data.getJSONObject("data").getInt("profile_complete");
                        //  invalidateOptionsMenu();
                        Toast.makeText(Ced_MultiVendor_EditVendorProfileDynamic.this, data.getJSONObject("data").getString("message"), Toast.LENGTH_LONG).show();
                        Intent intent1 = new Intent(Ced_MultiVendor_EditVendorProfileDynamic.this, Ced_MultiVendor_VendorProfile.class);
                        intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent1);
                        overridePendingTransition(R.anim.ced_multivendor_slide_in, R.anim.ced_multivendor_slide_out);
                    }
                    else {
                        Toast.makeText(Ced_MultiVendor_EditVendorProfileDynamic.this, data.getJSONObject("data").getString("message"), Toast.LENGTH_LONG).show();
                    }
                }
            }
            else {
                Intent intent = new Intent(getApplicationContext(), Ced_MultiVendor_UnAuthourizedRequestError.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                overridePendingTransition(R.anim.ced_multivendor_slide_in, R.anim.ced_multivendor_slide_out);
            }
        }, Ced_MultiVendor_EditVendorProfileDynamic.this, "POST", dataforvendorprofile);
        crr.execute(updateURL);

    }

    private void getProfilefields() {
        //String profileFieldUrl = session.getBase_Url() + "vendorapi/index/profileFields";
        String profileFieldUrl = session.getBase_Url() + "vendorapi/index/getProfileFields";
        HashMap<String, String> postdata = new HashMap<>();
        postdata.put("vendor_id", session.getVendorid());
        postdata.put("hashkey", session.getHahkey());

        Ced_MultiVendor_ClientRequestResponse crr = new Ced_MultiVendor_ClientRequestResponse(output -> {
            JSONObject outputObj = new JSONObject(output.toString()).getJSONObject(KEY_data);
            if (outputObj.getString(KEY_success).equals("true")) {
                JSONObject vendor_attributes = outputObj.getJSONObject("vendor_attributes");
                JSONArray vendor_attribute_names = vendor_attributes.names();
                for (int i = 0; i < vendor_attribute_names.length(); i++) {

                    //headings of each section
                    String heading_name = vendor_attribute_names.get(i).toString();
                    TextView header_text = new TextView(Ced_MultiVendor_EditVendorProfileDynamic.this);
                    LinearLayout.LayoutParams lm = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    header_text.setPadding(10, 10, 10, 10);
                    header_text.setBackgroundColor(getResources().getColor(R.color.secondary_color));
                    header_text.setGravity(Gravity.CENTER);
                    header_text.setLayoutParams(lm);
                    header_text.setTextColor(getResources().getColor(R.color.white));
                    if(heading_name.equalsIgnoreCase("general_information")){
                        header_text.setText(R.string.general_information);
                    }else{
                        header_text.setText(heading_name);
                    }

                    profilefields.addView(header_text);

                    JSONArray arr_value = vendor_attributes.getJSONArray(heading_name); // one-one each section
                    for (int j = 0; j < arr_value.length(); j++) {
                        JSONObject jsonObject = arr_value.getJSONObject(j);
                        if(jsonObject.getString(KEY_field_to_post).equalsIgnoreCase("shop_url_done"))
                        {
                            if(jsonObject.getString(KEY_saved_value).equalsIgnoreCase("1"))
                            {
                                is_shop_url_done = true;
                            }
                            break;
                        }

                    }
                    for (int j = 0; j < arr_value.length(); j++) {
                        JSONObject jsonObject = arr_value.getJSONObject(j);
                        if(jsonObject.getString(KEY_field_to_post).equalsIgnoreCase("shop_url_done"))
                            continue;
                        else if(jsonObject.getString(KEY_field_to_post).equalsIgnoreCase("shop_url"))
                            my_shop_url = jsonObject.getString(KEY_saved_value);
                        createField(jsonObject);
                    }
                }
                edit_profile_scroll.setVisibility(View.VISIBLE);
                progressbar.setVisibility(View.GONE);

            } else {
                Log.i("REpo", "Success : " + outputObj.getJSONObject(KEY_data).getString(KEY_success));
            }

        }, Ced_MultiVendor_EditVendorProfileDynamic.this, "POST", postdata);
        crr.execute(profileFieldUrl);
    }

    private void createField(final JSONObject jsonObject) throws JSONException {
        LayoutInflater li = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = null;
        TextView text_title = null;
        TextView id = null;
        Boolean isrequired = false;
        String field_type = jsonObject.getString(KEY_type);

        if (jsonObject.has(KEY_is_required) && jsonObject.getString(KEY_is_required).equals("1") && !field_type.equalsIgnoreCase("password")) {
            requiredFields.put(jsonObject.getString(KEY_field_to_post), jsonObject.getString(KEY_field_name));
            isrequired = true;
        }


        switch (field_type) {
            case "datetime":
                view = li.inflate(R.layout.view_datetime, null, false);
                final TextView text_date = view.findViewById(R.id.text_date);
                text_title = view.findViewById(R.id.text_title);
                ifrequired_putstart_totitle(isrequired, text_title, jsonObject.getString(KEY_field_name));
                if (jsonObject.has(KEY_saved_value) && jsonObject.getString(KEY_saved_value) != null) {
                    text_date.setText(jsonObject.getString(KEY_saved_value));
                }
                text_date.setOnClickListener(v -> {
                    newCalendar = Calendar.getInstance();
                    dateFormatter = new SimpleDateFormat("MM/dd/yyyy", Locale.US);
                    new DatePickerDialog(Ced_MultiVendor_EditVendorProfileDynamic.this, (view12, year, monthOfYear, dayOfMonth) -> {
                        newCalendar.set(Calendar.YEAR, year);
                        newCalendar.set(Calendar.MONTH, monthOfYear);
                        newCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        text_date.setText(dateFormatter.format(newCalendar.getTime()));
                        try {
                            dataforvendorprofile.put(jsonObject.getString(KEY_field_to_post), text_date.getText().toString());
                            if (requiredFields.has(jsonObject.getString(KEY_field_to_post))) {
                                requiredFields.remove(jsonObject.getString(KEY_field_to_post));
                            }
                        }
                        catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH)).show();
                });
                break;

            case "file":
                view = li.inflate(R.layout.file_layout_dny, null, false);
                text_title = view.findViewById(R.id.text_title);
                TextView link_value = view.findViewById(R.id.link_value);
                final TextView browse_picture1 = view.findViewById(R.id.browse_picture);
                ifrequired_putstart_totitle(isrequired, text_title, jsonObject.getString(KEY_field_name));
                if (jsonObject.has("saved_value")) {
                    if (jsonObject.has("saved_value") && !TextUtils.isEmpty(jsonObject.getString("saved_value"))) {
                        // link_value.setText(jsonObject.getString("saved_value"));
                        link_value.setOnClickListener(view1 -> {
                            try {
                                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(jsonObject.getString("saved_value")));
                                startActivity(intent);
                            }
                            catch (JSONException e) {
                                e.printStackTrace();
                            }
                        });
                    }
                    else {
                        link_value.setText("No File Attached");
                    }
                }

                browse_picture1.setTag(jsonObject.getString(KEY_field_to_post));
                browse_picture1.setOnClickListener(v -> {
                    file_value = browse_picture1.getTag().toString();
                    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
                        Dexter.withActivity(Ced_MultiVendor_EditVendorProfileDynamic.this)
                                .withPermissions(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE).
                                withListener(new MultiplePermissionsListener() {
                                    @Override
                                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                                        if (report.areAllPermissionsGranted()) {
                                            Intent photoPickerIntent = new Intent(Intent.ACTION_GET_CONTENT);
                                            photoPickerIntent.setType("application/*");
                                            startActivityForResult(photoPickerIntent, REQUESTCODE_FILE_BROWSE);
                                        }
                                    }
                                    @Override
                                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                                        token.continuePermissionRequest();
                                    }
                                }).onSameThread().check();
                    }else{
                        Dexter.withActivity(Ced_MultiVendor_EditVendorProfileDynamic.this)
                                .withPermissions(CAMERA,READ_MEDIA_IMAGES).
                                withListener(new MultiplePermissionsListener() {
                                    @Override
                                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                                        if (report.areAllPermissionsGranted()) {
                                            Intent photoPickerIntent = new Intent(Intent.ACTION_GET_CONTENT);
                                            photoPickerIntent.setType("application/*");
                                            startActivityForResult(photoPickerIntent, REQUESTCODE_FILE_BROWSE);
                                        }
                                    }
                                    @Override
                                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                                        token.continuePermissionRequest();
                                    }
                                }).onSameThread().check();
                    }
                });

                break;

            case "text":
                if(jsonObject.getString("field_to_post").equalsIgnoreCase("contact_number"))
                    addMobileNumber(jsonObject,jsonObject.getString("field_name"), jsonObject.getString("field_to_post"), jsonObject.getString("is_required"));
               else if (!jsonObject.getString(KEY_field_to_post).equalsIgnoreCase("region")) {
                    view = li.inflate(R.layout.view_text, null, false);
                    final EditText text_data = view.findViewById(R.id.text_data);
                    text_title = view.findViewById(R.id.text_title);
                    ifrequired_putstart_totitle(isrequired, text_title, jsonObject.getString(KEY_field_name));

                    if (jsonObject.getString(KEY_field_to_post).equalsIgnoreCase("email")
                            || jsonObject.getString(KEY_field_to_post).equalsIgnoreCase("latitude")
                            || jsonObject.getString(KEY_field_to_post).equalsIgnoreCase("longitude")) {
                        text_data.setEnabled(false);
                    }

                    if (jsonObject.has(KEY_saved_value) && jsonObject.getString(KEY_saved_value) != null) {
                        text_data.setText(jsonObject.getString(KEY_saved_value));
                    }

                    if (jsonObject.has("input_type") && jsonObject.getString("input_type").equalsIgnoreCase("int")) {
                        text_data.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_SIGNED);
                    }

                    if (text_data.getText().toString().length() > 0) {
                        dataforvendorprofile.put(jsonObject.getString(KEY_field_to_post), text_data.getText().toString());
                        if (requiredFields.has(jsonObject.getString(KEY_field_to_post)))
                            requiredFields.remove(jsonObject.getString(KEY_field_to_post));
                    }
                    final Boolean finalIsrequired = isrequired;
                    final TextView finalText_title1 = text_title;
                    if(jsonObject.getString(KEY_field_to_post).equalsIgnoreCase("shop_url"))
                        text_data.setEnabled(!is_shop_url_done);
                    text_data.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {
                            try {
                            /*if (text_data.getText().toString().length() > 0) {
                                dataforvendorprofile.put(jsonObject.getString(KEY_field_to_post), text_data.getText().toString());
                                text_data.setError(null);
                            } else if (finalIsrequired) {
                                dataforvendorprofile.remove(jsonObject.getString(KEY_field_to_post));
                                text_data.setError("Please fill " + finalText_title1.getText().toString());
                                text_data.requestFocus();
                            }
                            Log.i("REpo", "calender : " + jsonObject.getString(KEY_field_to_post) + " " + text_data.getText().toString());*/
                                dataforvendorprofile.put(jsonObject.getString(KEY_field_to_post), text_data.getText().toString());

                                if (requiredFields.has(jsonObject.getString(KEY_field_to_post)))
                                    requiredFields.remove(jsonObject.getString(KEY_field_to_post));
                                if(jsonObject.getString(KEY_field_to_post).equalsIgnoreCase("shop_url"))
                                    my_shop_url = text_data.getText().toString();
                                Log.i("REpo", "case text: " + jsonObject.getString(KEY_field_to_post) + " " + text_data.getText().toString());
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void afterTextChanged(Editable s) {

                        }
                    });
                }
                else {
                    Log.d("REpo", "createField_541: " + jsonObject);
                    savedState=jsonObject.getString("saved_value");
                }
                break;

            case "select":
                if (jsonObject.getString(KEY_field_to_post).equalsIgnoreCase("region_id") || jsonObject.getString(KEY_field_name).equalsIgnoreCase("State*") || jsonObject.getString(KEY_field_name).equalsIgnoreCase("State")) {
                    statesJSONdata = jsonObject;
                }
                if (jsonObject.has("saved_value")) {
                    Log.d("REpo", "createField_2044: " + jsonObject);
                }
                view = li.inflate(R.layout.view_select_one, null, false);
                text_title = view.findViewById(R.id.text_title);
                id = view.findViewById(R.id.text_id);
                ifrequired_putstart_totitle(isrequired, text_title, jsonObject.getString(KEY_field_name));
                final Spinner select_option = view.findViewById(R.id.select_option);

                if (jsonObject.getString(KEY_field_to_post).equalsIgnoreCase("bank_city")) {
                    bank_city = select_option;
                }
                else if (jsonObject.getString(KEY_field_to_post).equalsIgnoreCase("business_city")) {
                    business_city = select_option;
                }

                if (jsonObject.has(KEY_options)) {
                    JSONArray optionsArray = jsonObject.getJSONArray(KEY_options);
                    final HashMap<String, String> optionmap = new HashMap<>();
                    final ArrayList<String> optionList = new ArrayList<>();
                    final ArrayList<String> saved_value_list = new ArrayList<>();
                    for (int i = 0; i < optionsArray.length(); i++) {
                        optionList.add(optionsArray.getJSONObject(i).getString(KEY_label));
                        saved_value_list.add(optionsArray.getJSONObject(i).getString(KEY_value));
                        optionmap.put(optionsArray.getJSONObject(i).getString(KEY_label), optionsArray.getJSONObject(i).getString(KEY_value));
                    }

                    final String saved_value = jsonObject.getString("saved_value");

                    ArrayAdapter<String> dropdown = new ArrayAdapter<>(Ced_MultiVendor_EditVendorProfileDynamic.this, R.layout.spinner_textview, optionList);
                    select_option.setAdapter(dropdown);

                    if (saved_value_list.contains(saved_value)) {
                        select_option.setSelection(saved_value_list.indexOf(saved_value));
                        // optionList.get(saved_value_list.indexOf(saved_value))
                    }

                    final TextView finalText_title = text_title;
                    select_option.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            try {
                                dataforvendorprofile.put(jsonObject.getString(KEY_field_to_post), optionmap.get(select_option.getSelectedItem()));
                                if (jsonObject.getString(KEY_field_to_post).equals("country_id") || finalText_title.getText().toString().equalsIgnoreCase("Country") || finalText_title.getText().toString().equalsIgnoreCase("Country*")) {
                                    getState(optionmap.get(select_option.getSelectedItem()), statesJSONdata);
                                }
                                if (requiredFields.has(jsonObject.getString(KEY_field_to_post)))
                                    requiredFields.remove(jsonObject.getString(KEY_field_to_post));
                            }
                            catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {

                        }
                    });
                }
                else {
                    if (jsonObject.getString(KEY_field_to_post).equalsIgnoreCase("region_id") || jsonObject.getString(KEY_field_to_post).equalsIgnoreCase("bank_state") || jsonObject.getString(KEY_field_to_post).equalsIgnoreCase("business_state")) {
                        String saved_value = jsonObject.getString("saved_value");
                        ArrayAdapter<String> dropdown = new ArrayAdapter<>(Ced_MultiVendor_EditVendorProfileDynamic.this, R.layout.spinner_textview, statelabellist);
                        select_option.setAdapter(dropdown);

                        if (statecodelist.contains(saved_value)) {
                            select_option.setSelection(statelabellist.indexOf(id_codes_states.get(saved_value)));
                        }

                        final TextView finalId = id;
                        final int[] bank = {0};
                        final int[] business = {0};
                        select_option.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                try {

                                    dataforvendorprofile.put(jsonObject.getString(KEY_field_to_post), label_codes_states.get(select_option.getSelectedItem()));
                                    if (requiredFields.has(jsonObject.getString(KEY_field_to_post)))
                                        requiredFields.remove(jsonObject.getString(KEY_field_to_post));

                                    if (jsonObject.getString(KEY_field_to_post).equalsIgnoreCase("bank_state")) {
                                        city_request(label_codes_states.get(select_option.getSelectedItem()), jsonObject.getString(KEY_field_to_post), jsonObject.getString("saved_value"), bank[0]++);
                                    }

                                    if (jsonObject.getString(KEY_field_to_post).equalsIgnoreCase("business_state")) {
                                        city_request(label_codes_states.get(select_option.getSelectedItem()), jsonObject.getString(KEY_field_to_post), jsonObject.getString("saved_value"), business[0]++);
                                    }
                                   /* LinearLayout city_main = (LinearLayout) profilefields.getChildAt(Integer.parseInt(finalId.getText().toString())+1);
                                    Toast.makeText(Ced_MultiVendor_EditVendorProfileDynamic.this, ""+Integer.parseInt(finalId.getText().toString()), Toast.LENGTH_SHORT).show();*/
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> adapterView) {
                            }
                        });
                    }
                }
                break;

            case "multiselect":
                view = li.inflate(R.layout.view_multiselect, null, false);
                text_title = view.findViewById(R.id.text_title);
                ifrequired_putstart_totitle(isrequired, text_title, jsonObject.getString(KEY_field_name));
                GridLayout multioptionsgrid = view.findViewById(R.id.multioptionsgrid);
                JSONArray multiOptionsArray = jsonObject.getJSONArray(KEY_options);
                final ArrayList<String> selectedOptions = new ArrayList<>();
                for (int i = 0; i < multiOptionsArray.length(); i++) {
                    final CheckBox option = new CheckBox(Ced_MultiVendor_EditVendorProfileDynamic.this);
                    option.setButtonDrawable(checkboxDrawable);
                    option.setTag(multiOptionsArray.getJSONObject(i).getString(KEY_value));
                    option.setText(multiOptionsArray.getJSONObject(i).getString(KEY_label));
                    option.setOnCheckedChangeListener((buttonView, isChecked) -> {
                        if (option.isChecked()) {
                            if (!selectedOptions.contains(option.getTag().toString()))
                                selectedOptions.add(option.getTag().toString());
                        }
                        else {
                            selectedOptions.remove(option.getTag().toString());
                        }
                        try {
                            dataforvendorprofile.put(jsonObject.getString(KEY_field_to_post), new JSONArray(selectedOptions).toString());
                           /* if (requiredFields.has(jsonObject.getString(KEY_field_to_post)))
                                requiredFields.remove(jsonObject.getString(KEY_field_to_post));*/
                            Log.i("REpo", "multiselect : " + jsonObject.getString(KEY_field_to_post) + " " + new JSONArray(selectedOptions).toString());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    });
                    multioptionsgrid.addView(option);
                }

                break;

            case "image":

                view = li.inflate(R.layout.view_image_field, null, false);
                text_title = view.findViewById(R.id.text_title);
                ifrequired_putstart_totitle(isrequired, text_title, jsonObject.getString(KEY_field_name));
                final ImageView imageView = view.findViewById(R.id.picture_field);
                imageView.setTag(jsonObject.getString(KEY_field_to_post));
                if (jsonObject.has("saved_value")) {
                    if (jsonObject.has("saved_value") && !TextUtils.isEmpty(jsonObject.getString("saved_value"))) {
                        Picasso.with(this).
                                load(jsonObject.getString("saved_value")).
                                error(getResources().getDrawable(R.drawable.placeholder))
                                .placeholder(getResources().getDrawable(R.drawable.placeholder))
                                .into(imageView);
                    }
                }
/*
                if (jsonObject.has(KEY_saved_value)&&jsonObject.getString(KEY_saved_value)!=null){
                    Glide.with(Ced_MultiVendor_EditVendorProfileDynamic.this)
                            .load(jsonObject.getString(KEY_saved_value))
                            .into(imageView);
                }
*/
                TextView browse_picture = view.findViewById(R.id.browse_picture);
                browse_picture.setOnClickListener(v -> {
                    pictureField = imageView;
                    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
                        Dexter.withActivity(Ced_MultiVendor_EditVendorProfileDynamic.this)
                                .withPermissions(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE).
                                withListener(new MultiplePermissionsListener() {
                                    @Override
                                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                                        if (report.areAllPermissionsGranted()) {
                                            Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                                            photoPickerIntent.setType("image/*");
                                            startActivityForResult(photoPickerIntent, REQUESTCODE_IMAGEBROWSE);
                                        }
                                    }
                                    @Override
                                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                                        token.continuePermissionRequest();
                                    }
                                }).onSameThread().check();
                    }else{
                        Dexter.withActivity(Ced_MultiVendor_EditVendorProfileDynamic.this)
                                .withPermissions(CAMERA,READ_MEDIA_IMAGES).
                                withListener(new MultiplePermissionsListener() {
                                    @Override
                                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                                        if (report.areAllPermissionsGranted()) {
                                            Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                                            photoPickerIntent.setType("image/*");
                                            startActivityForResult(photoPickerIntent, REQUESTCODE_IMAGEBROWSE);
                                        }
                                    }
                                    @Override
                                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                                        token.continuePermissionRequest();
                                    }
                                }).onSameThread().check();
                    }
                });

                break;

            case "textarea":

                view = li.inflate(R.layout.view_text_area, null, false);
                text_title = view.findViewById(R.id.text_title);
                ifrequired_putstart_totitle(isrequired, text_title, jsonObject.getString(KEY_field_name));
                final EditText text_area = view.findViewById(R.id.text_area);
                if (jsonObject.has(KEY_saved_value) && jsonObject.getString(KEY_saved_value) != null) {
                    text_area.setText(jsonObject.getString(KEY_saved_value));
                }
                text_area.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        try {
                            dataforvendorprofile.put(jsonObject.getString(KEY_field_to_post), text_area.getText().toString());
                            /*if (requiredFields.has(jsonObject.getString(KEY_field_to_post)))
                                requiredFields.remove(jsonObject.getString(KEY_field_to_post));*/
                            Log.i("REpo", "textarea : " + jsonObject.getString(KEY_field_to_post) + " " + text_area.getText().toString());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                    }
                });

                break;

            default:
                Log.i("REpo", "key_string : " + field_type);

        }
        if (view != null)
            profilefields.addView(view);
    }

    private void ifrequired_putstart_totitle(boolean isrequired, TextView text_title, String labeltodisplay) {
        if (isrequired) {
            text_title.setText(labeltodisplay + "*");
        } else {
            text_title.setText(labeltodisplay);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
        if (resultCode != RESULT_CANCELED) {

            switch (requestCode) {
                case REQUESTCODE_FILE_BROWSE: {
                    if (resultCode == RESULT_OK) {
                        Toast.makeText(this, "Document Attached", Toast.LENGTH_SHORT).show();
                        try {
                            final Uri imageUri = imageReturnedIntent.getData();
                            imageUri.getPath();
                            String filePath = "";
                            String file_name = "";
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                File file = new File(imageUri.getPath());//create path from uri
                                final String[] split = file.getPath().split(":");//split the path.
                                filePath = split[1];//assign it to a string(your choice).
                            } else {
                                filePath = FileUtils.getPath(getApplicationContext(), imageUri);
                            }

                            if (calculateFileSize(filePath) > 5.0) {
                                Toast.makeText(this, "Image should be less than 5 MB", Toast.LENGTH_SHORT).show();
                            } else {
                                JSONObject fileobject = new JSONObject();
                                assert imageUri != null;
                                file_name = getFileName(imageUri);

                                File file1 = new File(filePath);
                                String[] imagename = imageUri.getPath().split("/");
                                byte[] bytes = loadFile(file1);
                                String base64file = Base64.encodeToString(bytes, Base64.DEFAULT);
                                fileobject.put("type", "file");
                                fileobject.put("name", file_name);
                                fileobject.put("base64_encoded_data", base64file);
                                dataforvendorprofile.put(file_value, fileobject.toString());
                                    /*if (requiredFields.has(file_value))
                                        requiredFields.remove(file_value);*/
                            }

                        } catch (Exception e) {
                            Toast.makeText(this, "Not allowed from this location", Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }
                    }
                }
                break;
                case REQUESTCODE_IMAGEBROWSE:
                    if (resultCode == RESULT_OK) {
                        try {
                            Uri imageUri = imageReturnedIntent.getData();
                            String[] imagename = getRealPathFromURI(Ced_MultiVendor_EditVendorProfileDynamic.this, imageUri).split("/");
                            logouri = getRealPathFromURI(Ced_MultiVendor_EditVendorProfileDynamic.this, imageUri);
                            Log.d("FileSize", "onActivityResult: " + calculateFileSize(logouri));
                            if (calculateFileSize(logouri) > 5.0) {
                                Toast.makeText(this, "Image should be less than 5 MB", Toast.LENGTH_SHORT).show();
                            } else {
                                Log.i("REpo", "onActivityresult_filename : " + imagename[imagename.length - 1].trim() + " :: " + imageUri);

                                InputStream imageStream = getContentResolver().openInputStream(imageUri);
                                Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);

                                pictureField.setImageBitmap(selectedImage);
                                Log.i("REpo", "onActivityresult_pictureTAG : " + pictureField.getTag());

                                encodedPicture = encodeImage(selectedImage);

                                Log.i("REpo", "onActivityresult_encodedPicture : " + encodedPicture);

                                if (!encodedPicture.equalsIgnoreCase("")) {
                                    JSONObject logo = new JSONObject();
                                    try {
                                        logo.put("type", "file");
//                                        logo.put("name", "image.jpeg");
                                        logo.put("name", "image" + new Date().getTime() + 3 + ".png");
                                        logo.put("base64_encoded_data", encodedPicture);
                                        Log.i("REpo", "imagejson : " + pictureField.getTag() + " " + logo.toString());
                                        dataforvendorprofile.put(pictureField.getTag().toString(), logo.toString());
                                        /*if (requiredFields.has(pictureField.getTag().toString()))
                                            requiredFields.remove(pictureField.getTag().toString());*/
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                } else {
                                    Log.i("REpo", "encodedPicture : " + encodedPicture);
                                }
                            }

                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                default:
                    Log.d("REpo", "onActivityResult: requestCode" + requestCode);
            }
        }

    }

    private void addMobileNumber(JSONObject jsonObject,final String title, final String fieldToPost, final String is_required) {
        View child = getLayoutInflater().inflate(R.layout.view_text, null);
        TextView text_title = child.findViewById(R.id.text_title);
        codePicker = child.findViewById(R.id.countryPicker);
        codePicker.setCountryForNameCode("eg");
        edtUserNumber = child.findViewById(R.id.edtUserNumber);
        try {
            if (jsonObject.has(KEY_saved_value) && jsonObject.getString(KEY_saved_value) != null) {
                oldMobileNumber = jsonObject.getString(KEY_saved_value);
                String value = jsonObject.getString(KEY_saved_value);
                value = value.substring(codePicker.getSelectedCountryCode().length());
                edtUserNumber.setText(value);
                requiredFields.remove(fieldToPost);
            }
        }catch (Exception e){}
        text_title.setText(title);
        child.findViewById(R.id.text_data).setVisibility(View.GONE);
        child.findViewById(R.id.countryCodelin).setVisibility(View.VISIBLE);
        if (child != null)
            profilefields.addView(child);
        final EditText text_data = child.findViewById(R.id.edtUserNumber);
        text_data.setTag(fieldToPost);
        text_data.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try {
                    if (s.length() < 0 && s.toString().isEmpty()) {
                        if (is_required.equals("1")) {
                            requiredFields.put(fieldToPost, title);
                        }
                    } else {
                        requiredFields.remove(fieldToPost);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    public  boolean isLink(String url) {
        // Regular expression to match a complete and valid URL
        if(is_shop_url_done){
            return true;
        }
      //  Pattern p = Pattern.compile("[^a-z0-9 ]", Pattern.CASE_INSENSITIVE);
//        Pattern letter = Pattern.compile("[a-zA-z]");
//        Pattern digit = Pattern.compile("[0-9]");
//        Pattern special = Pattern.compile ("[!@#$%&*()+=|<>?{}\\[\\]~]");
//        Matcher hasSpecial = special.matcher(url);
//        if(hasSpecial.find()){
//            //url.matches(".*\\d.*")
//            return false;
//        }else if(url.contains(" ")) {
//            return false;
//        }
//        else {
//            return true;
//        }
        // Define allowed characters: a-z, A-Z, and dash (-)
        String allowedChars = "abcdefghijklmnopqrstuvwxyz-";

        for (int i = 0; i < url.length(); i++) {
            char c = url.charAt(i);
            if (allowedChars.indexOf(c) == -1) {
                return false; // Found a disallowed character
            }
        }
        return true; // All characters are valid
    }

    @SuppressLint("Range")
    public String getFileName(Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            }
            finally {
                cursor.close();
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUESTCODE_FILE_BROWSE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //Toast.makeText(getApplicationContext(), "Permission Granted!", Toast.LENGTH_SHORT).show();
                    Intent photoPickerIntent = new Intent(Intent.ACTION_GET_CONTENT);
                    photoPickerIntent.setType("application/*");
                    startActivityForResult(photoPickerIntent, REQUESTCODE_FILE_BROWSE);
                } else {
                    Toast.makeText(getApplicationContext(), "Permission Denied!", Toast.LENGTH_SHORT).show();
                }
            case REQUESTCODE_IMAGEBROWSE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Toast.makeText(getApplicationContext(), "Permission Granted!", Toast.LENGTH_SHORT).show();
                    Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                    photoPickerIntent.setType("image/*");
                    startActivityForResult(photoPickerIntent, REQUESTCODE_IMAGEBROWSE);
                } else {
                    Toast.makeText(getApplicationContext(), "Permission Denied!", Toast.LENGTH_SHORT).show();
                }
            default:
                Log.d("REpo", "onRequestPermissionsResult:requestCode " + requestCode);
        }


    }

    public String getRealPathFromURI(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = {MediaStore.Images.Media.DATA};
            cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    private String encodeImage(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] b = baos.toByteArray();
        String encImage = Base64.encodeToString(b, Base64.DEFAULT);
        return encImage;
    }


    @Override
    protected void onResume() {
        Ced_MultiVendor_ConnectionDetector connectionDetector = new Ced_MultiVendor_ConnectionDetector(getApplicationContext());
        if (connectionDetector.isConnectingToInternet()) {
            setname(session.getvendorname());
            setprofilepic(session.getvendorpic());
            changetitle("Edit Profile");
            super.onResume();
        }
        else {
            Intent intent = new Intent(getApplicationContext(), Ced_MultiVendor_NoInternetconnection.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            overridePendingTransition(R.anim.ced_multivendor_slide_in, R.anim.ced_multivendor_slide_out);
            super.onResume();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.ced_multivendor_menu_login, menu);
        MenuItem item = menu.findItem(R.id.MultiVendor_notification);
        MenuItem item2 = menu.findItem(R.id.MultiVendor_profile);
        MenuItemCompat.setActionView(item, R.layout.ced_multivendor_feed_update_count);
        MenuItemCompat.setActionView(item2, R.layout.ced_multivendor_profilestatus);
        View profilecount = MenuItemCompat.getActionView(item2);
        profilecount.setOnClickListener(v -> {
            Toast.makeText(getApplicationContext(), "Profile", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(getApplicationContext(), Ced_MultiVendor_ProfileStatus.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);

        });
        View notifCount = MenuItemCompat.getActionView(item);
        TextView notification = notifCount.findViewById(R.id.MultiVendor_notitext);
        notification.setText(Ced_MultiVendor_GlobalVariables.noti);
        notifCount.setOnClickListener(v -> {
            Toast.makeText(getApplicationContext(), "Notification", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(getApplicationContext(), Notification.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void updatePasswordStrengthView(String password) {

        if (password.length() > 0) {
            strenth_lyt.setVisibility(View.VISIBLE);
            login_instructions.setVisibility(View.VISIBLE);

        } else {
            strenth_lyt.setVisibility(View.GONE);
        }
        if (TextView.VISIBLE != strengthView.getVisibility())
            return;

        if (password.isEmpty()) {
            strengthView.setText("");
            progressbar1.setProgress(0);
            return;
        }

        PasswordStrength str = PasswordStrength.calculateStrength(password);
        strengthView.setText(str.getText(this));
        strengthView.setTextColor(str.getColor());

        progressbar1.getProgressDrawable().setColorFilter(str.getColor(), android.graphics.PorterDuff.Mode.SRC_IN);
        if (str.getText(this).equals("Weak")) {
            progressbar1.setProgress(25);
        } else if (str.getText(this).equals("Medium")) {
            progressbar1.setProgress(50);
        } else if (str.getText(this).equals("Strong")) {
            progressbar1.setProgress(75);
        } else {
            progressbar1.setProgress(100);
        }
    }

    private void getState(String countrycode, final JSONObject jsonObject) {
        String countryurl = session.getBase_Url() + "vendorapi/index/getcountry/";

        dataforStates.put("vendor_id", session.getVendorid());
        dataforStates.put("hashkey", session.getHahkey());
        dataforStates.put("country_code", countrycode);

        Ced_MultiVendor_ClientRequestResponse response = new Ced_MultiVendor_ClientRequestResponse(output -> {
            if (functionalityList.getExtensionAddon()) {
                statelabellist = new ArrayList<>();
                statecodelist = new ArrayList<>();
                label_codes_states = new HashMap<>();
                id_codes_states = new HashMap<>();
                JSONObject object = new JSONObject(output.toString());
                Boolean status = object.getBoolean("success");
                if (status) {
                    JSONArray jsonArray = object.getJSONArray("states");
                    JSONArray mystates = new JSONArray();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject myobject = new JSONObject();
                        JSONObject c = jsonArray.getJSONObject(i);
                        statecodelist.add(c.getString("region_id"));
                        statelabellist.add(c.getString("default_name"));
                        label_codes_states.put(c.getString("default_name"), c.getString("region_id"));
                        id_codes_states.put(c.getString("region_id"), c.getString("default_name"));
                        myobject.put("label", c.getString("default_name"));
                        myobject.put("value", c.getString("region_id"));
                        mystates.put(myobject);
                    }
                    createField2(jsonObject, "selectstate", mystates);
                }
                else {
                    createField2(jsonObject, "textstate", new JSONArray());
                }
            }
            else {
                createField2(jsonObject, "textstate", new JSONArray());
                Intent intent = new Intent(getApplicationContext(), Ced_MultiVendor_UnAuthourizedRequestError.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        }, Ced_MultiVendor_EditVendorProfileDynamic.this, "POST", dataforStates);
        response.execute(countryurl);
    }

    private void createField2(final JSONObject jsonObject, String string, JSONArray mystates) throws JSONException {
        LayoutInflater li = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = null;
        TextView text_title = null;
        TextView id = null;
        TextView link_value = null;
        boolean isrequired = false;

        if (jsonObject.has(KEY_is_required) && jsonObject.getString(KEY_is_required).equals("1") && !string.equalsIgnoreCase("password")) {
            requiredFields.put(jsonObject.getString(KEY_field_to_post), jsonObject.getString(KEY_field_name));
            isrequired = true;
        }
        Log.d("REpo", "createField2_1178: "+string+" # "+savedState);
        Log.d("REpo", "createField2_1179: "+jsonObject);
        switch (string) {

            case "selectstate":
                view = li.inflate(R.layout.view_select_one, null, false);

                text_title = view.findViewById(R.id.text_title);
                id = view.findViewById(R.id.text_id);

                if (isrequired)
                    text_title.setText(jsonObject.getString(KEY_field_name) + "*");
                else
                    text_title.setText(jsonObject.getString(KEY_field_name));
                final Spinner select_option = view.findViewById(R.id.select_option);

                if (jsonObject.getString(KEY_field_to_post).equalsIgnoreCase("bank_city")) {
                    bank_city = select_option;
                }
                else if (jsonObject.getString(KEY_field_to_post).equalsIgnoreCase("business_city")) {
                    business_city = select_option;
                }
                if (mystates.length() > 0) {
                    // JSONArray optionsArray = jsonObject.getJSONArray(KEY_options);
                    final HashMap<String, String> optionmap = new HashMap<>();
                    final ArrayList<String> optionList = new ArrayList<>();
                    final ArrayList<String> saved_value_list = new ArrayList<>();

                    for (int i = 0; i < mystates.length(); i++) {
                        optionList.add(mystates.getJSONObject(i).getString(KEY_label));
                        saved_value_list.add(mystates.getJSONObject(i).getString(KEY_value));
                        optionmap.put(mystates.getJSONObject(i).getString(KEY_label), mystates.getJSONObject(i).getString(KEY_value));

                    }
                    String saved_value = jsonObject.getString("saved_value");

                    ArrayAdapter<String> dropdown = new ArrayAdapter<>(Ced_MultiVendor_EditVendorProfileDynamic.this, R.layout.spinner_textview, optionList);
                    select_option.setAdapter(dropdown);

                    if (saved_value_list.contains(saved_value)) {
                        select_option.setSelection(saved_value_list.indexOf(saved_value));
                    }

                    final TextView finalText_title = text_title;
                    select_option.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            try {
                                dataforvendorprofile.put(jsonObject.getString(KEY_field_to_post), optionmap.get(select_option.getSelectedItem()));
                               /* if(finalText_title.getText().toString().equalsIgnoreCase("Country") )
                                {
                                    getState(optionmap.get(select_option.getSelectedItem()),jsonObject);
                                }*/
                                /*if (requiredFields.has(jsonObject.getString(KEY_field_to_post)))
                                    requiredFields.remove(jsonObject.getString(KEY_field_to_post));*/
//                            Log.i("REpo","Select : "+jsonObject.getString(KEY_field_to_post)+" "+optionmap.get(select_option.getSelectedItem()));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {

                        }
                    });
                }
                else {
                    if (jsonObject.getString(KEY_field_to_post).equalsIgnoreCase("region_id") || jsonObject.getString(KEY_field_to_post).equalsIgnoreCase("bank_state") || jsonObject.getString(KEY_field_to_post).equalsIgnoreCase("business_state")) {
                        String saved_value = jsonObject.getString("saved_value");
                        ArrayAdapter<String> dropdown = new ArrayAdapter<>(Ced_MultiVendor_EditVendorProfileDynamic.this, R.layout.spinner_textview, statelabellist);
                        select_option.setAdapter(dropdown);

                        if (statecodelist.contains(saved_value)) {
                            select_option.setSelection(statelabellist.indexOf(id_codes_states.get(saved_value)));
                        }

                        final TextView finalId = id;
                        final int[] bank = {0};
                        final int[] business = {0};
                        select_option.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                try {
                                    dataforvendorprofile.put(jsonObject.getString(KEY_field_to_post), label_codes_states.get(select_option.getSelectedItem()));
                                   /* if (requiredFields.has(jsonObject.getString(KEY_field_to_post)))
                                        requiredFields.remove(jsonObject.getString(KEY_field_to_post));*/

                                    if (jsonObject.getString(KEY_field_to_post).equalsIgnoreCase("bank_state")) {
                                        city_request(label_codes_states.get(select_option.getSelectedItem()), jsonObject.getString(KEY_field_to_post), jsonObject.getString("saved_value"), bank[0]++);

                                    }

                                    if (jsonObject.getString(KEY_field_to_post).equalsIgnoreCase("business_state")) {
                                        city_request(label_codes_states.get(select_option.getSelectedItem()), jsonObject.getString(KEY_field_to_post), jsonObject.getString("saved_value"), business[0]++);
                                    }


                                   /* LinearLayout city_main = (LinearLayout) profilefields.getChildAt(Integer.parseInt(finalId.getText().toString())+1);
                                    Toast.makeText(Ced_MultiVendor_EditVendorProfileDynamic.this, ""+Integer.parseInt(finalId.getText().toString()), Toast.LENGTH_SHORT).show();
*/


                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> adapterView) {

                            }
                        });
                    }
                }

                break;

            case "textstate":
                view = li.inflate(R.layout.view_text_state, null, false);

                text_title = view.findViewById(R.id.text_title);
                id = view.findViewById(R.id.text_id);

                if (isrequired)
                    text_title.setText(jsonObject.getString(KEY_field_name) + "*");
                else
                    text_title.setText(jsonObject.getString(KEY_field_name));
                final EditText select_option_text = view.findViewById(R.id.select_option);

               /* if (jsonObject.getString(KEY_field_to_post).equalsIgnoreCase("bank_city")) {
                    bank_city = select_option;
                } else if (jsonObject.getString(KEY_field_to_post).equalsIgnoreCase("business_city")) {
                    business_city = select_option;
                }*/
                if (!savedState.equals("")){
                    select_option_text.setText(savedState);
                }

                if (mystates.length() > 0) {
                    // JSONArray optionsArray = jsonObject.getJSONArray(KEY_options);
                    final HashMap<String, String> optionmap = new HashMap<>();
                    final ArrayList<String> optionList = new ArrayList<>();
                    final ArrayList<String> saved_value_list = new ArrayList<>();

                    for (int i = 0; i < mystates.length(); i++) {
                        optionList.add(mystates.getJSONObject(i).getString(KEY_label));
                        saved_value_list.add(mystates.getJSONObject(i).getString(KEY_value));
                        optionmap.put(mystates.getJSONObject(i).getString(KEY_label), mystates.getJSONObject(i).getString(KEY_value));

                    }
                    String saved_value = jsonObject.getString("saved_value");

                    if (!saved_value.equals("")) {
                        select_option_text.setText(saved_value);
                    }

                    dataforvendorprofile.put(jsonObject.getString(KEY_field_to_post), select_option_text.getText().toString());

                    select_option_text.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                        }

                        @Override
                        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                            try {
                                dataforvendorprofile.remove(jsonObject.getString(KEY_field_to_post));
                                dataforvendorprofile.put(jsonObject.getString(KEY_field_to_post), select_option_text.getText().toString());
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void afterTextChanged(Editable editable) {
                        }
                    });
                }
                else {
                    if (jsonObject.getString(KEY_field_to_post).equalsIgnoreCase("region_id") || jsonObject.getString(KEY_field_to_post).equalsIgnoreCase("bank_state") || jsonObject.getString(KEY_field_to_post).equalsIgnoreCase("business_state")) {

                        String saved_value =savedState.equals("")?savedState:jsonObject.getString("saved_value");

                        if (!saved_value.equals("")) {
                            if (statecodelist.contains(saved_value)) {
                                select_option_text.setText(saved_value);
                                select_option_text.setText(statelabellist.get(statecodelist.indexOf(saved_value)));
                            }
                        }

                       /*if (requiredFields.has(jsonObject.getString(KEY_field_to_post)))
                            requiredFields.remove(jsonObject.getString(KEY_field_to_post));*/

                        if (jsonObject.getString(KEY_field_to_post).equalsIgnoreCase("bank_state")) {
                            city_request(select_option_text.getText().toString()/*label_codes_states.get(select_option.getSelectedItem())*/, jsonObject.getString(KEY_field_to_post), jsonObject.getString("saved_value"), 0/*bank[0]++*/);
                        }

                        if (jsonObject.getString(KEY_field_to_post).equalsIgnoreCase("business_state")) {
                            city_request(select_option_text.getText().toString()/*label_codes_states.get(select_option.getSelectedItem())*/, jsonObject.getString(KEY_field_to_post), jsonObject.getString("saved_value"), 0/*business[0]++*/);
                        }

                        dataforvendorprofile.put(jsonObject.getString(KEY_field_to_post), select_option_text.getText().toString());

                        select_option_text.addTextChangedListener(new TextWatcher() {
                            @Override
                            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                            }

                            @Override
                            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                                try {
                                    dataforvendorprofile.remove(jsonObject.getString(KEY_field_to_post));
                                    dataforvendorprofile.put(jsonObject.getString(KEY_field_to_post), select_option_text.getText().toString());
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }

                            @Override
                            public void afterTextChanged(Editable editable) {
                            }
                        });

                    }
                }


                break;

            case "textstate1":
                view = li.inflate(R.layout.view_text, null, false);
                text_title = view.findViewById(R.id.text_title);
                if (isrequired)
                    text_title.setText(jsonObject.getString(KEY_field_name) + "*");
                else
                    text_title.setText(jsonObject.getString(KEY_field_name));

                final EditText text_data = view.findViewById(R.id.text_data);

                if (jsonObject.has(KEY_saved_value) && jsonObject.getString(KEY_saved_value) != null) {
                    text_data.setText(jsonObject.getString(KEY_saved_value));
                }

                if (jsonObject.has("input_type") && jsonObject.getString("input_type").equalsIgnoreCase("int")) {
                    text_data.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_SIGNED);
                }

                /*if (text_data.getText().toString().length() > 0) {
                    if (requiredFields.has(jsonObject.getString(KEY_field_to_post)))
                        requiredFields.remove(jsonObject.getString(KEY_field_to_post));
                }*/
                text_data.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        try {
                            dataforvendorprofile.put(jsonObject.getString(KEY_field_to_post), text_data.getText().toString());
                            /*if (requiredFields.has(jsonObject.getString(KEY_field_to_post)))
                                requiredFields.remove(jsonObject.getString(KEY_field_to_post));*/
                            Log.i("REpo", "calender : " + jsonObject.getString(KEY_field_to_post) + " " + text_data.getText().toString());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });
                break;
        }

        for (int k = 0; k < profilefields.getChildCount(); k++) {
            if (profilefields.getChildAt(k) instanceof LinearLayout) {
                LinearLayout linearLayout = (LinearLayout) profilefields.getChildAt(k);
                for (int j = 0; j < linearLayout.getChildCount(); j++) {
                    if (linearLayout.getChildAt(j) instanceof TextView && !(linearLayout.getChildAt(j) instanceof EditText)) {
                        TextView textView_title = (TextView) linearLayout.getChildAt(j);
                        if (textView_title.getTag().toString().equalsIgnoreCase("text_title")) {
                            if (textView_title.getText().toString().equalsIgnoreCase("State") || textView_title.getText().toString().equalsIgnoreCase("State*")) {
                                linearLayout.addView(view);
                                textView_title.setVisibility(View.GONE);
                                linearLayout.removeViewAt(2);
                            }
                        }
                    }
                }
            }
        }
    }

    private void city_request(String code, final String field, final String saved, final int value) {
        String get_city = session.getBase_Url() + "rest/V1/mobiconnect/module/getcity";
        int bool_bank_city, bool_business_city = 0;
        Ced_MultiVendor_ClientRequestResponse crr = new Ced_MultiVendor_ClientRequestResponse(output -> {
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
            if (field.equalsIgnoreCase("bank_state")) {
                if (value >= 1) {
                    ArrayAdapter<String> dropdown = new ArrayAdapter<String>(Ced_MultiVendor_EditVendorProfileDynamic.this, R.layout.spinner_textview, citylabellist);
                    bank_city.setAdapter(dropdown);
                    Log.d("saved", "" + saved);
                }
                if (citylabellist.contains(saved)) {
                    bank_city.setSelection(citylabellist.indexOf(saved));
                }
                bank_city.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        dataforvendorprofile.put("bank_city", label_codes_city.get(bank_city.getSelectedItem().toString()).toString());
                           /* if (requiredFields.has(field))
                                requiredFields.remove(field);*/
//                            Log.i("REpo","Select : "+jsonObject.getString(KEY_field_to_post)+" "+optionmap.get(select_option.getSelectedItem()));
                        Log.d("frozen", "params== " + dataforvendorprofile);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });
            }

            if (field.equalsIgnoreCase("business_state")) {
                if (value >= 1) {
                    ArrayAdapter<String> dropdown = new ArrayAdapter<String>(Ced_MultiVendor_EditVendorProfileDynamic.this, R.layout.spinner_textview, citylabellist);
                    business_city.setAdapter(dropdown);
                    Log.d("saved", "" + saved);
                }
                business_city.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        dataforvendorprofile.put("business_city", label_codes_city.get(business_city.getSelectedItem().toString()).toString());
                        if (requiredFields.has(field))
                            requiredFields.remove(field);
//                            Log.i("REpo","Select : "+jsonObject.getString(KEY_field_to_post)+" "+optionmap.get(select_option.getSelectedItem()));
                        Log.d("frozen", "params== " + dataforvendorprofile);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });
            }
        }, Ced_MultiVendor_EditVendorProfileDynamic.this);
        crr.execute(get_city + "/" + code);
    }
}