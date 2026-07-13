package magentoegypt.locafy.vendor_registration_section.new_registration;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.READ_MEDIA_IMAGES;
import static magentoegypt.locafy.base_app.UtilityMethods.getFileName;
import static magentoegypt.locafy.base_app.UtilityMethods.getMimeType;
import static magentoegypt.locafy.base_app.UtilityMethods.getRealPathFromURI;
import static magentoegypt.locafy.base_app.UtilityMethods.loadFile;
import static magentoegypt.locafy.navigation_drawer.Activity.Ced_MultiVendor_NavigationActivity.calculateFileSize;
import static magentoegypt.locafy.navigation_drawer.Activity.Ced_MultiVendor_NavigationActivity.encodeImage;
import static magentoegypt.locafy.navigation_drawer.Activity.Ced_MultiVendor_NavigationActivity.getResizedBitmap;
import static magentoegypt.locafy.navigation_drawer.Activity.Ced_MultiVendor_NavigationActivity.rotateBitmap;
import static com.bumptech.glide.load.resource.bitmap.TransformationUtils.rotateImage;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.DatePickerDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.lifecycle.ViewModelProviders;

import magentoegypt.locafy.api_request_response_section.RestNotificatioRequest;
import magentoegypt.locafy.R;
import magentoegypt.locafy.api_request_response_section.AsyncResponse;
import magentoegypt.locafy.api_request_response_section.Ced_ClientRequestResponse;
import magentoegypt.locafy.api_request_response_section.Ced_MultiVendor_ClientRequestResponse;
import magentoegypt.locafy.api_request_response_section.Ced_NewLoader;
import magentoegypt.locafy.api_request_response_section.network_error_handling.Ced_MultiVendor_UnAuthourizedRequestError;
import magentoegypt.locafy_constant.Ced_MultiVendor_GlobalVariables;
import magentoegypt.locafy_constant.FileUtils;
import magentoegypt.locafy.base_app.Ced_MultiVendor_VendorFunctionalityList;
import magentoegypt.locafy.databinding.ActivityRegistrationDynamicBinding;
import magentoegypt.locafy.base_app.UtilityMethods;
import magentoegypt.locafy.gallary.GalleryActivity;
import magentoegypt.locafy.gallary.Image;
import magentoegypt.locafy.manage_products_section.Ced_MultiVendor_UpdateProduct;
import magentoegypt.locafy.manage_products_section.EditProductDynamic;
import magentoegypt.locafy.manage_products_section.ProductCreationNew;
import magentoegypt.locafy.vendor_dashboard.Ced_MultiVendor_VendorDashboard;
import magentoegypt.locafy.vendor_login_section.mvvm.LoginViewModel;
import magentoegypt.locafy.vendor_registration_section.ValidateAndSendOtp;
import magentoegypt.locafy.vendor_session.Ced_MultiVendor_VendorSessionManagement;
import com.hbb20.CountryCodePicker;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.text.Normalizer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegistrationDynamic extends AppCompatActivity {
    private final String TAG = "RegistrationDynamic";
    private final Calendar calendar = Calendar.getInstance();
    String created_vendor_id;
    private JSONObject requiredfields;
    private ArrayList IsRequired_FieldsNames;
    private LinearLayout viewContainer;
    private JSONObject registrationParams;
    private Ced_MultiVendor_VendorFunctionalityList functionalityList;
    private Ced_MultiVendor_VendorSessionManagement vendorSessionManagement;
    private String email = "",fullname = "", firstName = "", lastName = "";
    private String outputString;
    private String jstring;
    private String saveurl;
    private String file_value;
    private String step = "1";
    Ced_NewLoader cedNewLoader;
    View cityDropView;
    String CityfieldToPost;
    JSONArray cityOptions;
    JSONArray stateOptions;

    View stateDropView;
    String statefieldToPost;
    String my_shop_url = "";

    String passwordString = "";
    String confirmPassword = "";
    String encodedPicture = "";
    int random = 0;
    private ImageView pictureField;
    ValidateAndSendOtp loginWithNumber;
    CountryCodePicker codePicker;
    EditText edtUserNumber;
    List<Uri> allImages = new ArrayList<>();
    List<Uri> allVideo = new ArrayList<>();
    List<Uri> alldocuments = new ArrayList<>();
    Uri imageToUploadUri;
    String imagePath = "";
    ActivityRegistrationDynamicBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_dynamic);
        GalleryActivity.limitImage = 0;
        viewContainer = findViewById(R.id.viewContainer);
        findViewById(R.id.weblink).setOnClickListener(view -> {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://vendors.magento2.click/global/create-vendor-account"));
            startActivity(browserIntent);
        });
        registrationParams = new JSONObject();
        functionalityList = new Ced_MultiVendor_VendorFunctionalityList(this);
        vendorSessionManagement = new Ced_MultiVendor_VendorSessionManagement(this);
        if (getIntent().getBooleanExtra("fromsociallogin", false)) {
            created_vendor_id = getIntent().getStringExtra("customer_id");
            email = getIntent().getStringExtra("email");
            firstName = getIntent().getStringExtra("firstname");
            lastName = getIntent().getStringExtra("lastname");
            fullname = firstName + " " + lastName;
            saveurl = vendorSessionManagement.getBase_Url() + "vendorapi/index/approvalPost";
        } else {
            if (getIntent() != null && getIntent().getExtras() != null){
                created_vendor_id = getIntent().getStringExtra("vendor_id");
                jstring =  getIntent().getStringExtra("Jstring");
                findViewById(R.id.weblink).setVisibility(View.GONE);
                String hashkey =  getIntent().getStringExtra("hashkey");
                saveurl = vendorSessionManagement.getBase_Url() + "vmultistepregapi/index/save?vendor_id="+created_vendor_id+"&hashkey="+hashkey+"";
                step = "2";
            }else{
                saveurl = vendorSessionManagement.getBase_Url() + "vmultistepregapi/index/create";
            }
        }

        mobileNumberSignUp();
        getRegistrationFields();
    }

    private void mobileNumberSignUp() {
        LoginViewModel loginViewModel = ViewModelProviders.of(RegistrationDynamic.this).get((LoginViewModel.class));
        cedNewLoader = new Ced_NewLoader(RegistrationDynamic.this);

        loginWithNumber = new ValidateAndSendOtp(this, loginViewModel) {
            @Override
            public void onError(@Nullable String msg) {
                Toast.makeText(RegistrationDynamic.this, msg, Toast.LENGTH_SHORT).show();
            }

            @Override
            protected void onOtpVerified(@Nullable String number) {
                registerUser(number);
            }
        };
        loginWithNumber.setRegistrationDynamic(RegistrationDynamic.this);
        loginViewModel.error.observe(RegistrationDynamic.this, s -> Toast.makeText(RegistrationDynamic.this, s, Toast.LENGTH_SHORT).show());

        loginViewModel.loading.observe(RegistrationDynamic.this, this::updateLoader);


    }

    private void updateLoader(Boolean isLoading) {
        if (isLoading) {
            if (null != cedNewLoader)
                cedNewLoader.show();
        } else {
            if ((cedNewLoader != null) && cedNewLoader.isShowing()) {
                cedNewLoader.dismiss();
            }
        }
    }

    private void getRegistrationFields() {
        String getRegistrationFieldsURL = vendorSessionManagement.getBase_Url() + "vmultistepregapi/index/registrationFields";
        Ced_ClientRequestResponse response = new Ced_ClientRequestResponse(new AsyncResponse() {
            @Override
            public void processFinish(Object output) throws JSONException {
                JSONObject jsonObject = new JSONObject(output.toString());
                if (jsonObject.getString("success").equals("true")) {
                    createFields(jsonObject.getJSONArray("data"));
                }
            }
        }, RegistrationDynamic.this);
        response.execute(getRegistrationFieldsURL);
    }

    private void createFields(JSONArray jsonArray) throws JSONException {
        Log.d(TAG, "createFields: " + jsonArray);
        requiredfields = new JSONObject();
        IsRequired_FieldsNames = new ArrayList();
        createHeaderview();
        JSONObject stateJson = null,cityJson = null;
        for (int index = 0; index < jsonArray.length(); index++) {
            JSONObject jsonObject = jsonArray.getJSONObject(index);
            if(jsonObject.getString("field_to_post").equalsIgnoreCase("region")){
                stateJson = jsonObject;
            }else if(jsonObject.getString("field_to_post").equalsIgnoreCase("city")){
                cityJson = jsonObject;
            }
        }
        for (int index = 0; index < jsonArray.length(); index++) {

            JSONObject jsonObject = jsonArray.getJSONObject(index);
            if(step.equalsIgnoreCase("1") && (
                    //!jsonObject.getString("field_to_post").equalsIgnoreCase("public_name")
                   // && !jsonObject.getString("field_to_post").equalsIgnoreCase("shop_url")
                     !jsonObject.getString("field_to_post").equalsIgnoreCase("product_sample")
                    && !jsonObject.getString("field_to_post").equalsIgnoreCase("regdoc"))){

            }else if(step.equalsIgnoreCase("2") && (
                    //jsonObject.getString("field_to_post").equalsIgnoreCase("public_name")
                  //  || jsonObject.getString("field_to_post").equalsIgnoreCase("shop_url")
                     jsonObject.getString("field_to_post").equalsIgnoreCase("product_sample")
                    // || viewData.getString("field_to_post").equalsIgnoreCase("regdoc")
            )){

            }else{
                continue;
            }
            if(jsonObject.getString("field_to_post").equalsIgnoreCase("shop_url")
                    || jsonObject.getString("field_to_post").equalsIgnoreCase("is_subscribed"))
                continue;
            if (jsonObject.getString("is_required").equals("1")) {
                requiredfields.put(jsonObject.getString("field_to_post"), jsonObject.getString("field_name"));
                IsRequired_FieldsNames.add(jsonObject.getString("field_to_post"));
            }
//            if(jsonObject.getString("field_to_post").equalsIgnoreCase("country_id")){
//                countryJson = jsonObject;
//            }else
                if(jsonObject.getString("field_to_post").equalsIgnoreCase("region")){
                stateJson = jsonObject;
            }else if(jsonObject.getString("field_to_post").equalsIgnoreCase("city")){
                cityJson = jsonObject;
            }else if(jsonObject.getString("field_to_post").equalsIgnoreCase("address")){
                    createView(jsonObject, jsonArray);
                    if(stateJson != null && cityJson != null){
                        createView(stateJson, jsonArray);
                        createView(cityJson, jsonArray);
                    }
                }else {
                createView(jsonObject, jsonArray);
            }
        }

        createFooterView();
    }


    private void createView(JSONObject viewData,JSONArray jsonArray) throws JSONException {
        Log.d(TAG, "viewData: " + viewData);

        switch (viewData.getString("type")) {
            case "text":
                if(viewData.getString("field_to_post").equalsIgnoreCase("contact_number"))
                    addMobileNumber(viewData.getString("field_name"), viewData.getString("field_to_post"), viewData.getString("is_required"));
                else
                    createTextField(viewData.getString("field_name"), viewData.getString("field_to_post"), viewData.getString("is_required"));
                break;
            case "checkbox":
                createCheckboxField(viewData.getString("field_name"), viewData.getString("field_to_post"), viewData.getString("is_required"));
                break;
            case "password":
                createPasswordField(viewData.getString("field_name"), viewData.getString("field_to_post"), viewData.getString("is_required"));
                createPasswordField(getString(R.string.confirmPassword), "confirm_password", viewData.getString("is_required"));
                break;
            case "textarea":
                createTextAreaField(viewData.getString("field_name"), viewData.getString("field_to_post"), viewData.getString("is_required"));
                break;
            case "date":
                createDateField(viewData.getString("field_name"), viewData.getString("field_to_post"), viewData.getString("is_required"));
                break;
            case "multiselect":
                createMultiselectField(viewData.getString("field_name"), viewData.getString("field_to_post"), viewData.getJSONArray("options"), viewData.getString("is_required"));
                break;
            case "select":
                if(viewData.getString("field_to_post").equalsIgnoreCase("region")) {
//                    for (int index = 0; index < jsonArray.length(); index++) {
//                        JSONObject jsonObject = jsonArray.getJSONObject(index);
//                        if(jsonObject.getString("field_to_post").equalsIgnoreCase("country_id")){
//                            createSelectField(viewData.getString("field_name"), viewData.getString("field_to_post"), viewData.getJSONArray("options"), jsonObject.getJSONArray("options"),null, viewData.getString("is_required"));
//                            break;
//                        }
//                    }
                    createSelectField(viewData.getString("field_name"), viewData.getString("field_to_post"), viewData.getJSONArray("options"),null,null, viewData.getString("is_required"));
                }else if(viewData.getString("field_to_post").equalsIgnoreCase("city")) {
                    for (int index = 0; index < jsonArray.length(); index++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(index);
                        if(jsonObject.getString("field_to_post").equalsIgnoreCase("region")){
                            createSelectField(viewData.getString("field_name"), viewData.getString("field_to_post"), viewData.getJSONArray("options"),null, jsonObject.getJSONArray("options"), viewData.getString("is_required"));
                            break;
                        }
                    }
                }
                else
                    createSelectField(viewData.getString("field_name"), viewData.getString("field_to_post"), viewData.getJSONArray("options"),null,null, viewData.getString("is_required"));
                break;
            case "image":
                createImageField(viewData.getString("field_name"), viewData.getString("field_to_post"), viewData.getString("is_required"));
                break;
            case "file":
                createFileField(viewData.getString("field_name"), viewData.getString("field_to_post"), viewData.getString("is_required"));
                break;

        }
    }


    private String generateRandomChars (int length) {
        String candidateChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        StringBuilder sb = new StringBuilder ();
        Random random = new Random ();
        for (int i = 0; i < length; i ++) {
            sb.append (candidateChars.charAt (random.nextInt (candidateChars
                    .length ())));
        }
        return sb.toString ();
    }
    private void registerUser(String number)  {
        try {
            JSONObject registrationParamObj = new JSONObject();
            JSONArray vendorParams = new JSONArray();

            if (registrationParams.has("email")) {
                email = registrationParams.getString("email");
            }
            if (registrationParams.has("name")) {
                fullname = registrationParams.getString("name");
            }
            if (registrationParams.has("firstname")) {
                firstName = registrationParams.getString("firstname");
            }
            if (registrationParams.has("lastname")) {
                lastName = registrationParams.getString("lastname");
            }

            /********************************other params**********************************************/
            if (registrationParams.length() > 0) {
                Iterator<String> keys = registrationParams.keys();
                String key;
                JSONObject obj;
                while (keys.hasNext()) {
                    key = keys.next();
                    if(key.equals("contact_number"))
                    {
                        obj = new JSONObject();
                        obj.put("key", key);
                        obj.put("value",number); //"923003377181"
                        obj.put("type", "text");
                        vendorParams.put(obj);
                    }else if (key.equals("firstname") || key.equals("name") || key.equals("email") || key.equals("is_subscribed") || key.equals("password") || key.equals("mobile")) {
                        registrationParamObj.put(key, registrationParams.getString(key));
                    } else {
                        obj = new JSONObject();
                        obj.put("key", key);
                        obj.put("value", registrationParams.getString(key));
                        obj.put("type", "text");
                        vendorParams.put(obj);
                    }
                }
            }
            if(step.equalsIgnoreCase("1")){
                JSONObject shop_url = new JSONObject();
                shop_url.put("key", "shop_url");
                shop_url.put("value", "my-shop-url"+generateRandomChars(10));
                shop_url.put("type", "text");
               // vendorParams.put(shop_url);

//                JSONObject public_name = new JSONObject();
//                public_name.put("key", "public_name");
//                public_name.put("value", generateRandomChars(10));
//                public_name.put("type", "text");
//                vendorParams.put(public_name);
            }else{
                if(step.equalsIgnoreCase("2") && !number.isEmpty())
                {
                    JSONObject product_sample = new JSONObject();
                    product_sample.put("key", "product_sample");
                    product_sample.put("value", number);
                    product_sample.put("type", "text");
                    vendorParams.put(product_sample);
                }
                JSONObject obj = new JSONObject();
                obj.put("key", "multistep_done");
                obj.put("value", 1);
                obj.put("type", "text");
                 vendorParams.put(obj);
            }

            registrationParamObj.put("vendor", vendorParams);
            //  registrationParamObj.put("contact_number", "923003377181");


            HashMap<String, String> postData = new HashMap<>();
            if (getIntent().getBooleanExtra("fromsociallogin", false)) {
                postData.put("customer_id", created_vendor_id);
                postData.put("approveaccount", registrationParamObj.toString());
            }
            else {

                postData.put("createaccount", registrationParamObj.toString());
            }
            Log.i(TAG, "registerUser_registrationParams: " + registrationParamObj);
            Log.i(TAG, "saveurl: " + saveurl);
            Ced_MultiVendor_ClientRequestResponse crr = new Ced_MultiVendor_ClientRequestResponse(new AsyncResponse() {
                @Override
                public void processFinish(Object output) throws JSONException {
                    Log.i(TAG, "processFinish: " + output.toString());
                    outputString = output.toString();
                    if (functionalityList.getExtensionAddon()) {
                        JSONObject mainobject = new JSONObject(outputString);
                        JSONObject vendorobject = mainobject.getJSONObject("data");
                        JSONArray vendorarray = vendorobject.getJSONArray("customer");
                        JSONObject vendordata = vendorarray.getJSONObject(0);
                        if (vendordata.getString("success").equals("true")) {
                            if (step.equalsIgnoreCase("2") && !jstring.isEmpty()) {
                                JSONObject login_object = new JSONObject(jstring);
                                JSONArray login_array = login_object.getJSONObject("data").getJSONArray("customer");
                                String[] array = login_array.getJSONObject(0).getString("vendor_name").split(" ");
                                if (login_array.getJSONObject(0).has("vendor_id")) {
                                    vendorSessionManagement.saveCustomerId(login_array.getJSONObject(0).getString("customer_id"));
                                    vendorSessionManagement.createLoginSession(login_array.getJSONObject(0).getString("hashkey"), "");
                                    Log.i("logout", "vendor id ");
                                    vendorSessionManagement.saveVendorId(login_array.getJSONObject(0).getString("vendor_id"));
                                    if (login_array.getJSONObject(0).has("sub_vendor_id")) {
                                        Log.i("logout", "sub vendor id ");
                                        vendorSessionManagement.saveSubVendorId(login_array.getJSONObject(0).getString("sub_vendor_id"));
                                        vendorSessionManagement.savevendorpic(login_array.getJSONObject(0).getString("profile_picture"));
                                    }
                                    vendorSessionManagement.savevendorname(array[0]);
                                    vendorSessionManagement.savevendorpic(login_array.getJSONObject(0).getString("profile_picture"));
                                }
                                sendRegistrationToServer(vendorSessionManagement.getDeviceToken(), login_array.getJSONObject(0).getString("vendor_id"));
                                Ced_MultiVendor_GlobalVariables.progress = Integer.parseInt(login_array.getJSONObject(0).getString("profile_complete"));
                                Ced_MultiVendor_GlobalVariables.noti = login_array.getJSONObject(0).getString("valerts");
                                Intent intent = new Intent(getApplicationContext(), Ced_MultiVendor_VendorDashboard.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                intent.putExtra("name", array[0]);
                                intent.putExtra("picurl", login_array.getJSONObject(0).getString("profile_picture"));
                                intent.putExtra("vendor_id", login_array.getJSONObject(0).getString("vendor_id"));
                                startActivity(intent);
                                overridePendingTransition(R.anim.ced_multivendor_slide_in, R.anim.ced_multivendor_slide_out);
                            }else if (vendordata.has("hashkey")) {
                                if (vendordata.has("isConfirmationRequired")) {
                                    if (vendordata.getString("isConfirmationRequired").equals("NO")) {
                                        Toast.makeText(getApplicationContext(), vendordata.getString("message"), Toast.LENGTH_LONG).show();
                                        vendorSessionManagement.createLoginSession(vendordata.getString("hashkey"), email);
                                        vendorSessionManagement.saveVendorId(vendordata.getString("vendor_id"));
                                        vendorSessionManagement.saveCustomerId(vendordata.getString("customer_id"));
                                        vendorSessionManagement.savevendorname(vendordata.getString("vendor_name"));
                                        vendorSessionManagement.savevendorpic(vendordata.getString("profile_picture"));
                                        sendRegistrationToServer(vendorSessionManagement.getDeviceToken(), vendordata.getString("vendor_id"));
                                        Ced_MultiVendor_GlobalVariables.progress = Integer.parseInt(vendordata.getString("profile_complete"));
                                        Ced_MultiVendor_GlobalVariables.noti = vendordata.getString("valerts");
                                        Intent intent = new Intent(RegistrationDynamic.this, Ced_MultiVendor_VendorDashboard.class);
//                                    Intent intent = new Intent(getApplicationContext(), Ced_MultiVendor_Registration_step2.class);
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
                                        //Intent intent = new Intent(getApplicationContext(), Ced_MultiVendor_Registration_step2.class);
                                    }
                                }
                                Intent intent = new Intent(RegistrationDynamic.this, Ced_MultiVendor_VendorApprovalRequired.class);
//                                intent.putExtra("message", vendordata.getString("message"));
//                                intent.putExtra("firstname", firstName);
//                                intent.putExtra("lastname", lastName);
//                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                                startActivity(intent);
//                                overridePendingTransition(R.anim.ced_multivendor_slide_in, R.anim.ced_multivendor_slide_out);
                                UtilityMethods.showDialogOk(RegistrationDynamic.this,vendordata.getString("message"), new UtilityMethods.OnSelectedListener() {
                                    @Override
                                    public void onSelected(String value) {
                                            finish();
                                    }
                                });
                            }
                        } else {
//                            if (vendordata.has("isConfirmationRequired")) {
//                                if (vendordata.getString("isConfirmationRequired").equals("YES")) {
//                                    //Intent intent = new Intent(getApplicationContext(), Ced_MultiVendor_Registration_step2.class);
//                                }
//                            }
//                            Intent intent = new Intent(RegistrationDynamic.this, Ced_MultiVendor_VendorApprovalRequired.class);
//                            intent.putExtra("message", vendordata.getString("message"));
//                            intent.putExtra("firstname", firstName);
//                            intent.putExtra("lastname", lastName);
//                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                            startActivity(intent);
//                            overridePendingTransition(R.anim.ced_multivendor_slide_in, R.anim.ced_multivendor_slide_out);
                            Toast.makeText(getApplicationContext(), vendordata.getString("message"), Toast.LENGTH_LONG).show();
                        }

                    } else {
                        Intent intent = new Intent(getApplicationContext(), Ced_MultiVendor_UnAuthourizedRequestError.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        overridePendingTransition(R.anim.ced_multivendor_slide_in, R.anim.ced_multivendor_slide_out);
                    }
                }
            }, RegistrationDynamic.this, "POST", postData);
            crr.execute(saveurl);
        }catch (Exception e){}
    }

    private  long getFileSize(File file) {
// Get length of file in bytes
        long fileSizeInBytes = file.length();
// Convert the bytes to Kilobytes (1 KB = 1024 Bytes)
        long fileSizeInKB = fileSizeInBytes / 1024;
// Convert the KB to MegaBytes (1 MB = 1024 KBytes)
        return fileSizeInKB / 1024;
    }

   void addImageView(Uri mUri,String filetype){
        View layout = getLayoutInflater().inflate(R.layout.bind_image_layout, null, false);
        layout.findViewById(R.id.default_checkbox).setVisibility(View.GONE);
       AppCompatTextView imagetitle = layout.findViewById(R.id.imagetitle);
       imagetitle.setVisibility(View.VISIBLE);
        layout.findViewById(R.id.remove_image).setOnClickListener(view -> {
            viewContainer.removeView(layout);
            if(filetype.equalsIgnoreCase("images"))
              allImages.remove(mUri);
            GalleryActivity.limitImage -= 1;
        });
        ImageView imageView = layout.findViewById(R.id.MultiVendor_productimage);
        imageView.setImageURI(mUri);
        if(filetype.equalsIgnoreCase("images"))
            allImages.add(mUri);
//        else if(filetype.equalsIgnoreCase("video"))
//            allImages.add(mUri);
//        else if(filetype.equalsIgnoreCase("documents"))
//            allImages.add(mUri);
       imagetitle.setText(getFileName(mUri, RegistrationDynamic.this));
       String type = getMimeType(mUri, RegistrationDynamic.this);
        viewContainer.addView(layout,viewContainer.getChildCount()-2);
   }
    /************************************View Creation Methods*************************************/
    private void createFileField(String title, String fieldToPost, String is_required) {
        View view = getLayoutInflater().inflate(R.layout.file_layout_dny, null, false);
        TextView text_title = view.findViewById(R.id.text_title);
        view.findViewById(R.id.link_value).setVisibility(View.GONE);
        if (is_required.equals("1"))
            text_title.setText(title + "*");
        else
            text_title.setText(title);


        final TextView browse_picture1 = view.findViewById(R.id.browse_picture);
        final TextView camera_picture = view.findViewById(R.id.camera_picture);
        browse_picture1.setTag(fieldToPost);
        browse_picture1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                file_value = browse_picture1.getTag().toString();

                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
                    Dexter.withActivity(RegistrationDynamic.this)
                            .withPermissions(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE).
                            withListener(new MultiplePermissionsListener() {
                                @Override
                                public void onPermissionsChecked(MultiplePermissionsReport report) {
                                    if (report.areAllPermissionsGranted()) {
                                        showImageOptionDialog();
                                    }
                                }
                                @Override
                                public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                                    token.continuePermissionRequest();
                                }
                            }).onSameThread().check();
                }else{
                    Dexter.withActivity(RegistrationDynamic.this)
                            .withPermissions(CAMERA,READ_MEDIA_IMAGES).
                            withListener(new MultiplePermissionsListener() {
                                @Override
                                public void onPermissionsChecked(MultiplePermissionsReport report) {
                                    if (report.areAllPermissionsGranted()) {
                                        showImageOptionDialog();
                                    }
                                }
                                @Override
                                public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                                    token.continuePermissionRequest();
                                }
                            }).onSameThread().check();
                }
            }
        });

        camera_picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                file_value = browse_picture1.getTag().toString();
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
                    Dexter.withActivity(RegistrationDynamic.this)
                            .withPermissions(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE).
                            withListener(new MultiplePermissionsListener() {
                                @Override
                                public void onPermissionsChecked(MultiplePermissionsReport report) {
                                    if (report.areAllPermissionsGranted()) {
                                        Intent pictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                        File photoFile = null;
                                        try {
                                            photoFile = createImageFile();
                                        } catch (IOException ex) {
                                            ex.printStackTrace();
                                        }
                                        if (photoFile != null) {
                                            Log.e("REpo", "photoFile== " + photoFile.toString());
                                            // Uri photoURI = FileProvider.getUriForFile(this, "com.beenfix.sellerapp.provider", photoFile);
                                            imageToUploadUri = FileProvider.getUriForFile(RegistrationDynamic.this, "magentoegypt.locafy.provider", photoFile);
                                            pictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageToUploadUri);
                                            if(GalleryActivity.limitImage < 10) {
                                                startActivityForResult(pictureIntent, 3);
                                            }else{
                                                Toast.makeText(RegistrationDynamic.this, getString(R.string.you_can_not_select_more_than_10_images_please_deselect_another_image_before_tring_to_select_again), Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    }
                                }
                                @Override
                                public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                                    token.continuePermissionRequest();
                                }
                            }).onSameThread().check();
                }else{
                    Dexter.withActivity(RegistrationDynamic.this)
                            .withPermissions(CAMERA,READ_MEDIA_IMAGES).
                            withListener(new MultiplePermissionsListener() {
                                @Override
                                public void onPermissionsChecked(MultiplePermissionsReport report) {
                                    if (report.areAllPermissionsGranted()) {
                                        Intent pictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                        File photoFile = null;
                                        try {
                                            photoFile = createImageFile();
                                        } catch (IOException ex) {
                                            ex.printStackTrace();
                                        }
                                        if (photoFile != null) {
                                            Log.e("REpo", "photoFile== " + photoFile.toString());
                                            // Uri photoURI = FileProvider.getUriForFile(this, "com.beenfix.sellerapp.provider", photoFile);
                                            imageToUploadUri = FileProvider.getUriForFile(RegistrationDynamic.this, "magentoegypt.locafy.provider", photoFile);
                                            pictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageToUploadUri);
                                            if(GalleryActivity.limitImage < 10) {
                                                startActivityForResult(pictureIntent, 3);
                                            }else{
                                                Toast.makeText(RegistrationDynamic.this, getString(R.string.you_can_not_select_more_than_10_images_please_deselect_another_image_before_tring_to_select_again), Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    }
                                }
                                @Override
                                public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                                    token.continuePermissionRequest();
                                }
                            }).onSameThread().check();
                }

            }
        });

        if (view != null)
            viewContainer.addView(view);
        Log.d(TAG, "view created : ");

    }

    void showImageOptionDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(RegistrationDynamic.this);
        builder.setTitle("Option to select");
        builder.setItems(new CharSequence[]
                        {"Select images", "Select video", "Select documents file", "Cancel"},
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // The 'which' argument contains the index position
                        // of the selected item
                        switch (which) {
                            case 0:
                                Intent intent = new Intent(RegistrationDynamic.this, GalleryActivity.class);
                                startActivityForResult(intent,4);
                                break;
                            case 1:
                                Intent photoPickerIntent = new Intent(Intent.ACTION_GET_CONTENT);
                                photoPickerIntent.setType("video/*");
                                photoPickerIntent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
                                startActivityForResult(photoPickerIntent, 3);
                                break;
                            case 2:
                                // Intent photoPickerIntent1 = new Intent(Intent.ACTION_GET_CONTENT);
                                /*   photoPickerIntent.setType("image/*");*/
                                // photoPickerIntent1.setType("application/*");
                                //
                                // photoPickerIntent.setType("application/pdf");
                                Intent photoPickerIntent1 = new Intent();
                                photoPickerIntent1.setAction(Intent.ACTION_OPEN_DOCUMENT);
                                photoPickerIntent1.addCategory(Intent.CATEGORY_OPENABLE);
                                photoPickerIntent1.setType("*/*");
                                String[] extraMimeTypes = {"application/pdf","application/docx","application/xlsx","application/pptx","application/pptx","application/txt", "text/comma-separated-values","application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", "application/vnd.ms-excel"};
                                photoPickerIntent1.putExtra(Intent.EXTRA_MIME_TYPES, extraMimeTypes);
                                photoPickerIntent1.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
                                // photoPickerIntent1.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                                startActivityForResult(photoPickerIntent1, 3);
                                break;
                            case 3:
                                //    Toast.makeText(context, "clicked 4", Toast.LENGTH_SHORT).show();
                                break;
                        }
                    }
                });
        // builder.create().show();
        Intent intent = new Intent(RegistrationDynamic.this, GalleryActivity.class);
        startActivityForResult(intent,4);
    }

    public File createImageFile() throws IOException {

        String imageFileName = "IMG_" + generateRandomChars(10) + "_";
        File storageDir =
                getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imageFileName, ".jpg", storageDir);
        imagePath = image.getAbsolutePath();
        return image;
    }

    private void createImageField(String title, String fieldToPost, String is_required) {
        View view = getLayoutInflater().inflate(R.layout.view_image_field, null, false);
        TextView text_title = view.findViewById(R.id.text_title);

        if (is_required.equals("1"))
            text_title.setText(title + "*");
        else
            text_title.setText(title);

        final ImageView imageView = view.findViewById(R.id.picture_field);
        imageView.setTag(fieldToPost);


        TextView browse_picture = view.findViewById(R.id.browse_picture);
        browse_picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pictureField = imageView;
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
                    Dexter.withActivity(RegistrationDynamic.this)
                            .withPermissions(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE).
                            withListener(new MultiplePermissionsListener() {
                                @Override
                                public void onPermissionsChecked(MultiplePermissionsReport report) {
                                    if (report.areAllPermissionsGranted()) {
                                        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                                        photoPickerIntent.setType("image/*");
                                        startActivityForResult(photoPickerIntent, 2);
                                    }
                                }
                                @Override
                                public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                                    token.continuePermissionRequest();
                                }
                            }).onSameThread().check();
                }else{
                    Dexter.withActivity(RegistrationDynamic.this)
                            .withPermissions(CAMERA,READ_MEDIA_IMAGES).
                            withListener(new MultiplePermissionsListener() {
                                @Override
                                public void onPermissionsChecked(MultiplePermissionsReport report) {
                                    if (report.areAllPermissionsGranted()) {
                                        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                                        photoPickerIntent.setType("image/*");
                                        startActivityForResult(photoPickerIntent, 2);
                                    }
                                }
                                @Override
                                public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                                    token.continuePermissionRequest();
                                }
                            }).onSameThread().check();
                }
            }
        });
        if (view != null)
            viewContainer.addView(view);
    }

    private void  setStateDropView(String counryValue) throws JSONException {
        // Check if no view has focus:
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
        final Spinner spinner = stateDropView.findViewById(R.id.spinner);
        spinner.setTag(statefieldToPost);
        ArrayList<String> optionList = new ArrayList<>();
        final HashMap<String, String> optionMap = new HashMap<>();

        for (int i = 0; i < stateOptions.length(); i++) {
            if(stateOptions.getJSONObject(i).getString("country_id").equalsIgnoreCase(counryValue)) {
                optionList.add(stateOptions.getJSONObject(i).getString("label"));
                optionMap.put(stateOptions.getJSONObject(i).getString("label"), stateOptions.getJSONObject(i).getString("value"));
            }
        }
        if(optionList.size() == 0){
            spinner.setVisibility(View.GONE);
            stateDropView.findViewById(R.id.text_data).setVisibility(View.VISIBLE);
        }else{
            spinner.setVisibility(View.VISIBLE);
            stateDropView.findViewById(R.id.text_data).setVisibility(View.GONE);
        }
        ArrayAdapter arrayAdapter = new ArrayAdapter(RegistrationDynamic.this, R.layout.spinner_item, optionList);
        spinner.setAdapter(arrayAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Log.i(TAG, "onItemSelected: " + spinner.getSelectedItem());
                try {
                    setCityDropView(optionMap.get(spinner.getSelectedItem().toString()));
                    requiredfields.remove(statefieldToPost);
                    IsRequired_FieldsNames.remove(statefieldToPost);
                    registrationParams.put(spinner.getTag().toString(), optionMap.get(spinner.getSelectedItem()));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void  setCityDropView(String state) throws JSONException {
        // Check if no view has focus:
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
        final Spinner spinner = cityDropView.findViewById(R.id.spinner);
        spinner.setTag(CityfieldToPost);
        ArrayList<String> optionList = new ArrayList<>();
        final HashMap<String, String> optionMap = new HashMap<>();

        for (int i = 0; i < cityOptions.length(); i++) {
            if(cityOptions.getJSONObject(i).getString("states_name").equalsIgnoreCase(state)) {
                optionList.add(cityOptions.getJSONObject(i).getString("label"));
                optionMap.put(cityOptions.getJSONObject(i).getString("label"), cityOptions.getJSONObject(i).getString("value"));
            }
        }
        if(optionList.size() == 0){
            spinner.setVisibility(View.GONE);
            cityDropView.findViewById(R.id.text_data).setVisibility(View.VISIBLE);
        }else{
            spinner.setVisibility(View.VISIBLE);
            cityDropView.findViewById(R.id.text_data).setVisibility(View.GONE);
        }
        ArrayAdapter arrayAdapter = new ArrayAdapter(RegistrationDynamic.this, R.layout.spinner_item, optionList);
        spinner.setAdapter(arrayAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Log.i(TAG, "onItemSelected: " + spinner.getSelectedItem());
                try {

                    requiredfields.remove(CityfieldToPost);
                    IsRequired_FieldsNames.remove(CityfieldToPost);
                    registrationParams.put(spinner.getTag().toString(), optionMap.get(spinner.getSelectedItem()));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }
    private void createSelectField(final String title, final String fieldToPost, JSONArray options, JSONArray country, JSONArray state, final String is_required) throws JSONException {

        View child = getLayoutInflater().inflate(R.layout.select_field_layout, null);
        String countryValue = "";
        String stateValue = "";
        //if (country != null && country.length() > 0) {
        if (fieldToPost.equalsIgnoreCase("region")) {
            stateDropView = child;
            statefieldToPost = fieldToPost;
            stateOptions = options;
            countryValue = "EG";//country.getJSONObject(0).getString("value");
        }else if (state != null && state.length() > 0) {
            cityDropView = child;
            CityfieldToPost = fieldToPost;
            cityOptions = options;
            stateValue = state.getJSONObject(0).getString("value");
        }
        TextView text_title = child.findViewById(R.id.text_title);
        if(is_required.equalsIgnoreCase("1")){
            text_title.setText(title+"*");
        }else{
            text_title.setText(title);
        }
        final Spinner spinner = child.findViewById(R.id.spinner);
        spinner.setTag(fieldToPost);
        ArrayList<String> optionList = new ArrayList<>();
        final HashMap<String, String> optionMap = new HashMap<>();

        for (int i = 0; i < options.length(); i++) {
            if(!countryValue.isEmpty()){
                if(options.getJSONObject(i).getString("country_id").equalsIgnoreCase(countryValue)) {
                    optionList.add(options.getJSONObject(i).getString("label"));
                    optionMap.put(options.getJSONObject(i).getString("label"), options.getJSONObject(i).getString("value"));
                }
            }else if(!stateValue.isEmpty()){
                if(options.getJSONObject(i).getString("states_name").equalsIgnoreCase(stateValue)) {
                    optionList.add(options.getJSONObject(i).getString("label"));
                    optionMap.put(options.getJSONObject(i).getString("label"), options.getJSONObject(i).getString("value"));
                }
            }else{
                optionList.add(options.getJSONObject(i).getString("label"));
                optionMap.put(options.getJSONObject(i).getString("label"), options.getJSONObject(i).getString("value"));
            }
        }
        if(optionList.size() == 0){
            spinner.setVisibility(View.GONE);
        }else{
            child.findViewById(R.id.text_data).setVisibility(View.GONE);
        }
        ArrayAdapter arrayAdapter = new ArrayAdapter(RegistrationDynamic.this, R.layout.spinner_item, optionList);
        spinner.setAdapter(arrayAdapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Log.i(TAG, "onItemSelected: " + spinner.getSelectedItem());
                try {
                    if(fieldToPost.equalsIgnoreCase("country_id")){
                        setStateDropView(optionMap.get(spinner.getSelectedItem().toString()));
                    }else if(fieldToPost.equalsIgnoreCase("region")){
                        setCityDropView(optionMap.get(spinner.getSelectedItem().toString()));
                    }
                    requiredfields.remove(fieldToPost);
                    IsRequired_FieldsNames.remove(fieldToPost);
                    registrationParams.put(spinner.getTag().toString(), optionMap.get(spinner.getSelectedItem()));
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                /*if (requiredfields.has(spinner.getTag().toString())) {
                    requiredfields.remove(spinner.getTag().toString());
                }*/
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        final EditText text_data = child.findViewById(R.id.text_data);
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
                            requiredfields.put(fieldToPost, title);
                            IsRequired_FieldsNames.add(fieldToPost);
                            registrationParams.remove(text_data.getTag().toString());
                        }
                    } else {
                        registrationParams.put(text_data.getTag().toString(), text_data.getText().toString());
                        requiredfields.remove(fieldToPost);
                        IsRequired_FieldsNames.remove(fieldToPost);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        if (child != null)
            viewContainer.addView(child);
    }

    private void createMultiselectField(final String title, final String fieldToPost, JSONArray options, final String is_required) throws JSONException {
        final ArrayList<String> selection = new ArrayList<>();
        View child = getLayoutInflater().inflate(R.layout.view_multiselect, null);
        TextView text_title = child.findViewById(R.id.text_title);
        if(is_required.equalsIgnoreCase("1")){
            text_title.setText(title+"*");
        }else{
            text_title.setText(title);
        }
        final GridLayout multiselect_container = child.findViewById(R.id.multioptionsgrid);
        multiselect_container.setTag(fieldToPost);

        for (int i = 0; i < options.length(); i++) {
            JSONObject jsonObject = options.getJSONObject(i);

            if (!jsonObject.getString("value").equals("")) {

                final CheckBox checkBox = new CheckBox(RegistrationDynamic.this);

                checkBox.setText(jsonObject.getString("label"));
                checkBox.setTag(jsonObject.getString("value"));

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    checkBox.setButtonTintList(ColorStateList.valueOf(getResources().getColor(R.color.black)));
                }

                checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        try {
                            if (checkBox.isChecked()) {
                                if (!selection.contains(checkBox.getTag().toString()))
                                    selection.add(checkBox.getTag().toString());
                            } else {
                                selection.remove(checkBox.getTag().toString());
                            }
                            registrationParams.put(multiselect_container.getTag().toString(), selection.toString().replace("[", "").replace("]", ""));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
/*
                        if (requiredfields.has(multiselect_container.getTag().toString()) && selection.size() > 0) {
                            requiredfields.remove(multiselect_container.getTag().toString());
                        }*/

                     /*   else {
                            try {
                                requiredfields.put(multiselect_container.getTag().toString(), "isRequired");
                            }
                            catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }*/
                    }
                });

                multiselect_container.addView(checkBox);
            }
        }

        if (child != null)
            viewContainer.addView(child);

    }

    private void createDateField(final String title, final String fieldToPost, final String is_required) {
        View child = getLayoutInflater().inflate(R.layout.view_datetime, null);
        TextView text_title = child.findViewById(R.id.text_title);
        if(is_required.equalsIgnoreCase("1")){
            text_title.setText(title+"*");
        }else{
            text_title.setText(title);
        }
        final TextView text_date = child.findViewById(R.id.text_date);
        text_date.setTag(fieldToPost);
        text_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if (is_required.equals("1")) {
                        requiredfields.put(fieldToPost, title);
                        IsRequired_FieldsNames.add(fieldToPost);
                        registrationParams.remove(text_date.getTag().toString());
                    }
                    setDate(text_date);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        if (child != null)
            viewContainer.addView(child);

    }

    private void createTextAreaField(final String title, final String fieldToPost, final String is_required) {

        View child = getLayoutInflater().inflate(R.layout.view_text_area, null);
        TextView text_title = child.findViewById(R.id.text_title);
        if(is_required.equalsIgnoreCase("1")){
            text_title.setText(title+"*");
        }else{
            text_title.setText(title);
        }
        final EditText text_area = child.findViewById(R.id.text_area);
        text_area.setTag(fieldToPost);
        text_area.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try {
                    if (s.length() > 0 && !s.toString().isEmpty()) {
                        registrationParams.put(text_area.getTag().toString(), text_area.getText().toString());
                        requiredfields.remove(fieldToPost);
                        IsRequired_FieldsNames.remove(fieldToPost);
                    } else {
                        if (is_required.equals("1")) {
                            requiredfields.put(fieldToPost, title);
                            IsRequired_FieldsNames.add(fieldToPost);
                            registrationParams.remove(text_area.getTag().toString());
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
       /* text_area.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try {
                    if(s.length()>0 && !s.toString().isEmpty()) {
                        registrationParams.put(text_area.getTag().toString(), text_area.getText().toString());
                    }else
                    {
                        if (is_required.equals("1")){
                            requiredfields.put(fieldToPost,title);
                            IsRequired_FieldsNames.add(fieldToPost);
                            registrationParams.remove(text_area.getTag().toString());
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
               *//* if (requiredfields.has(text_area.getTag().toString())) {
                    requiredfields.remove(text_area.getTag().toString());
                }*//*

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
*/
        if (child != null)
            viewContainer.addView(child);
    }

    private void createPasswordField(final String title, final String fieldToPost, final String is_required) {
        View child = getLayoutInflater().inflate(R.layout.view_text_password, null);
        TextView text_title = child.findViewById(R.id.text_title);
        if(is_required.equalsIgnoreCase("1")){
            text_title.setText(title+"*");
        }else{
            text_title.setText(title);
        }

        final EditText password = child.findViewById(R.id.password);
        final ImageView maskpass = child.findViewById(R.id.maskpass);
        password.setTag(fieldToPost);
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
        password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try {
                    if (s.length() > 0 && !s.toString().isEmpty()) {
                        registrationParams.put(password.getTag().toString(), password.getText().toString());
                        requiredfields.remove(fieldToPost);
                        IsRequired_FieldsNames.remove(fieldToPost);
                    } else {
                        if (is_required.equals("1")) {
                            requiredfields.put(fieldToPost, title);
                            IsRequired_FieldsNames.add(fieldToPost);
                            registrationParams.remove(password.getTag().toString());
                        }
                    }
                    if(title.equalsIgnoreCase(getString(R.string.confirmPassword)))
                        confirmPassword = password.getText().toString();
                    else
                        passwordString = password.getText().toString();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
       /* password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try {
                    if(s.length()>0 && !s.toString().isEmpty())
                    {
                        registrationParams.put(password.getTag().toString(), password.getText().toString());
                    }else
                    {
                        if (is_required.equals("1")){
                            requiredfields.put(fieldToPost,title);
                            IsRequired_FieldsNames.add(fieldToPost);
                            registrationParams.remove(password.getTag().toString());
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
            @Override
            public void afterTextChanged(Editable s) {

            }
        });*/

        if (child != null)
            viewContainer.addView(child);
    }

    private void createCheckboxField(final String title, final String fieldToPost, final String is_required) {

        View child = getLayoutInflater().inflate(R.layout.shipping_country_checkboxes, null);
        final CheckBox checkbox_field = child.findViewById(R.id.MultiVendor_country_checkbox);
        checkbox_field.setTag(fieldToPost);
        if(is_required.equalsIgnoreCase("1")){
            checkbox_field.setText(title+"*");
        }else{
            checkbox_field.setText(title);
        }

        checkbox_field.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                try {
                    if (checkbox_field.isChecked()) {
                        registrationParams.put(checkbox_field.getTag().toString(), "1");
                        requiredfields.remove(fieldToPost);
                        IsRequired_FieldsNames.remove(fieldToPost);
                    } else {
                        if (is_required.equals("1")) {
                            requiredfields.put(fieldToPost, title);
                            IsRequired_FieldsNames.add(fieldToPost);
                            registrationParams.remove(checkbox_field.getTag().toString());
                        }
                        registrationParams.put(checkbox_field.getTag().toString(), "0");
                        requiredfields.remove(fieldToPost);
                        IsRequired_FieldsNames.remove(fieldToPost);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
       /* checkbox_field.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                try {
                    if (checkbox_field.isChecked()) {
                        registrationParams.put(checkbox_field.getTag().toString(), "1");
                    } else {
                        registrationParams.put(checkbox_field.getTag().toString(), "0");
                        if (is_required.equals("1")){
                            requiredfields.put(fieldToPost,title);
                            IsRequired_FieldsNames.add(fieldToPost);
                            registrationParams.remove(checkbox_field.getTag().toString());
                        }
                    }
                }
                catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });*/

        if (child != null)
            viewContainer.addView(child);
    }

    private void addMobileNumber(final String title, final String fieldToPost, final String is_required) {
        View child = getLayoutInflater().inflate(R.layout.view_text, null);
        TextView text_title = child.findViewById(R.id.text_title);
        codePicker = child.findViewById(R.id.countryPicker);
        codePicker.setCountryForNameCode("eg");
        edtUserNumber = child.findViewById(R.id.edtUserNumber);
        if(is_required.equalsIgnoreCase("1")){
            text_title.setText(title+"*");
        }else{
            text_title.setText(title);
        }
        child.findViewById(R.id.text_data).setVisibility(View.GONE);
        child.findViewById(R.id.countryCodelin).setVisibility(View.VISIBLE);
        if (child != null)
            viewContainer.addView(child);
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
                            requiredfields.put(fieldToPost, title);
                            IsRequired_FieldsNames.add(fieldToPost);
                            registrationParams.remove(text_data.getTag().toString());
                        }
                    } else {
                        registrationParams.put(text_data.getTag().toString(), text_data.getText().toString());
                        requiredfields.remove(fieldToPost);
                        IsRequired_FieldsNames.remove(fieldToPost);
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
    private void createTextField(final String title, final String fieldToPost, final String is_required) {
        View child = getLayoutInflater().inflate(R.layout.view_text, null);
        TextView text_title = child.findViewById(R.id.text_title);
        if(is_required.equalsIgnoreCase("1")){
            text_title.setText(title+"*");
        }else{
            text_title.setText(title);
        }
        final EditText text_data = child.findViewById(R.id.text_data);
        text_data.setTag(fieldToPost);
        if (fieldToPost.equalsIgnoreCase("email")) {
            text_data.setText(email);
            try {
                registrationParams.put(text_data.getTag().toString(), text_data.getText().toString());
                requiredfields.remove(fieldToPost);
                IsRequired_FieldsNames.remove(fieldToPost);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if (fieldToPost.equalsIgnoreCase("firstname")) {
            text_data.setText(firstName);
            try {
                registrationParams.put(text_data.getTag().toString(), text_data.getText().toString());
                requiredfields.remove(fieldToPost);
                IsRequired_FieldsNames.remove(fieldToPost);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if (fieldToPost.equalsIgnoreCase("lastname")) {
            text_data.setText(lastName);
            try {
                registrationParams.put(text_data.getTag().toString(), text_data.getText().toString());
                requiredfields.remove(fieldToPost);
                IsRequired_FieldsNames.remove(fieldToPost);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        text_data.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try {
                    if (s.length() < 0 && s.toString().isEmpty()) {
                        if (is_required.equals("1")) {
                            requiredfields.put(fieldToPost, title);
                            IsRequired_FieldsNames.add(fieldToPost);
                            registrationParams.remove(text_data.getTag().toString());
                        }
                    } else {
                        registrationParams.put(text_data.getTag().toString(), text_data.getText().toString());
                        requiredfields.remove(fieldToPost);
                        IsRequired_FieldsNames.remove(fieldToPost);
                    }
                    if(fieldToPost.equalsIgnoreCase("shop_url"))
                        my_shop_url = text_data.getText().toString();
                    else if (fieldToPost.equalsIgnoreCase("email")) {
                        email = text_data.getText().toString();
                    }else  if (fieldToPost.equalsIgnoreCase("name")) {
                        fullname = text_data.getText().toString();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        if (child != null)
            viewContainer.addView(child);
    }

    /********************************Static Views**************************************************/
    private void createHeaderview() {
        View child = getLayoutInflater().inflate(R.layout.title_text_layout, null);

        if (child != null)
            viewContainer.addView(child);
    }

    private void createFooterView() {
        View child = getLayoutInflater().inflate(R.layout.footer_button_layout, null);
        Button button = child.findViewById(R.id.submit_button);
        button.setOnClickListener(view -> {
            String msg = "";
            for (int k = 0; k < IsRequired_FieldsNames.size(); k++) {
                if (registrationParams.has(String.valueOf(IsRequired_FieldsNames.get(k)))) {
                    requiredfields.remove(String.valueOf(IsRequired_FieldsNames.get(k)));
                    IsRequired_FieldsNames.remove(k);
                }
            }
            for (int k = 0; k < IsRequired_FieldsNames.size(); k++) {
                try {
                    msg += requiredfields.getString(String.valueOf((IsRequired_FieldsNames.get(k)))) + "\n";
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            if (requiredfields.length() > 0 || (confirmPassword.isEmpty() && step.equalsIgnoreCase("1"))) {
                Toast.makeText(RegistrationDynamic.this, getResources().getString(R.string.required_field_validation) + ":\n" + msg, Toast.LENGTH_SHORT).show();
                Log.i(TAG, "onClick_requiredfields: " + requiredfields);
            } else {
                if(step.equalsIgnoreCase("1") && !isValidateEmail(email)) {
//                    if(!isLink(my_shop_url)){
//                        UtilityMethods.showDialogOk(RegistrationDynamic.this,getResources().getString(R.string.enter_a_valid_shop_url_example_my_shop_url) + "  @#$%&*", new UtilityMethods.OnSelectedListener() {
//                            @Override
//                            public void onSelected(String value) {
//
//                            }
//                        });
//                        return;
//                    }
                    if(email.trim().isEmpty()){
                        Toast.makeText(RegistrationDynamic.this, getResources().getString(R.string.requiredemail), Toast.LENGTH_SHORT).show();
                        return;
                    }
                    Toast.makeText(RegistrationDynamic.this, getResources().getString(R.string.invalidemail), Toast.LENGTH_SHORT).show();
                }else if(step.equalsIgnoreCase("1") && !isValidFullName(fullname)) {
                    Toast.makeText(RegistrationDynamic.this, getResources().getString(R.string.the_full_name_is_incorrect), Toast.LENGTH_SHORT).show();
                }else if(step.equalsIgnoreCase("2")) {
                    List<Uri> allFiles = new ArrayList<>();
                    allFiles.addAll(allImages);
                    allFiles.addAll(allVideo);
                    allFiles.addAll(alldocuments);
                    if(allFiles.size() > 0){
                        String hashkey =  getIntent().getStringExtra("hashkey");
                        Ced_MultiVendor_ClientRequestResponse crr = new Ced_MultiVendor_ClientRequestResponse(new AsyncResponse() {
                            @Override
                            public void processFinish(Object output) throws JSONException {
                                Log.i(TAG, "processFinish: " + output.toString());
                                JSONObject jsonObject = new JSONObject(output.toString());
                                if (jsonObject.getString("success").equals("true")) {
                                    JSONObject vendorobject = jsonObject.getJSONObject("data");
                                    String file = vendorobject.getString("file");
                                    registerUser(file);
                                }else{
                                    registerUser("");
                                }
                            }
                        }, RegistrationDynamic.this, allFiles);
                        crr.execute(vendorSessionManagement.getBase_Url() + "vmultistepregapi/index/upload?vendor_id="+created_vendor_id+"&hashkey="+hashkey+"");
                    }else
                        registerUser("");
                } else {
                    if(edtUserNumber.getText().toString().startsWith("0")){
                        Toast.makeText(RegistrationDynamic.this, getResources().getString(R.string.please_enter_the_number_without_0), Toast.LENGTH_SHORT).show();
                        return;
                    }else if(edtUserNumber.getText().toString().length() != 10){
                        Toast.makeText(RegistrationDynamic.this, getResources().getString(R.string.please_enter_the_valid_number), Toast.LENGTH_SHORT).show();
                        return;
                    }else if(!passwordString.equals(confirmPassword)){
                        Toast.makeText(RegistrationDynamic.this, getResources().getString(R.string.passnotmatch), Toast.LENGTH_SHORT).show();
                        return;
                    }
                    loginWithNumber.showEnterNumberDialog(edtUserNumber.getText().toString(), codePicker.getSelectedCountryNameCode());
                }
            }
        });

        if (child != null)
            viewContainer.addView(child);
    }

    private void openEnterMobileNumberDialog() {
        loginWithNumber.showEnterNumberDialog("","");
    }

    /*********************************Utility Methods**********************************************/
    private void setDate(final TextView text_data) {
        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                String myFormat = "dd-MM-yyyy"; //In which you need put here
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
                text_data.setText(sdf.format(calendar.getTime()));
                try {
                    registrationParams.put(text_data.getTag().toString(), text_data.getText().toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                /*if (requiredfields.has(text_data.getTag().toString())) {
                    requiredfields.remove(text_data.getTag().toString());
                }*/
            }
        };

        new DatePickerDialog(RegistrationDynamic.this, date, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
        if (resultCode != RESULT_CANCELED) {
            if (requestCode == 3) {
                if (resultCode == RESULT_OK && imageToUploadUri != null) {
                    try {
                        GalleryActivity.limitImage += 1;
                        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
                        Bitmap photo = BitmapFactory.decodeFile(imagePath, bmOptions);
                        String pathfile = MediaStore.Images.Media.insertImage(getContentResolver(), photo, generateRandomChars(10), null);
                        Uri uri = Uri.parse(pathfile);

                        final InputStream imageStream = getContentResolver().openInputStream(uri);

                        Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);

                        ExifInterface exif = null;
                        try {
                            File pictureFile = new File(imagePath);
                            exif = new ExifInterface(pictureFile.getAbsolutePath());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        int orientation = ExifInterface.ORIENTATION_NORMAL;

                        if (exif != null)
                            orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);

                        switch (orientation) {
                            case ExifInterface.ORIENTATION_ROTATE_90:
                                selectedImage = rotateBitmap(selectedImage, 90);
                                break;
                            case ExifInterface.ORIENTATION_ROTATE_180:
                                selectedImage = rotateBitmap(selectedImage, 180);
                                break;

                            case ExifInterface.ORIENTATION_ROTATE_270:
                                selectedImage = rotateBitmap(selectedImage, 270);
                                break;
                        }
                        selectedImage = getResizedBitmap(selectedImage, 300, 300);
                        String path = MediaStore.Images.Media.insertImage(getContentResolver(), selectedImage, generateRandomChars(10), null);
                        Uri tempUri = Uri.parse(path);
                        String filePath = tempUri.getPath();//getRealPathFromURI(RegistrationDynamic.this, imageToUploadUri);
                        // ContentResolver cR = getContentResolver();
                        String type = getMimeType(tempUri, RegistrationDynamic.this);//cR.getType(imageToUploadUri);
                        String producturi = getRealPathFromURI(RegistrationDynamic.this, tempUri);
                        Uri newUri = Uri.parse(producturi);
                        if (calculateFileSize(filePath) > 10.0) {
                            Toast.makeText(this, getString(R.string.image_should_be_less_than_10_mb), Toast.LENGTH_SHORT).show();
                        } else {
                            if(type.startsWith("image/")){
                                addImageView(newUri,"images");
                            }else if(type.startsWith("video/")){

                            }else{

                            }
//                            JSONObject fileobject = new JSONObject();
//                            assert imageUri != null;
//                            file_name = getFileName(imageUri, RegistrationDynamic.this);
//                            File file1 = new File(filePath);
//                            byte[] bytes = loadFile(file1);
//                            String base64file = Base64.encodeToString(bytes, Base64.DEFAULT);
//                            fileobject.put("type", "file");
//                            fileobject.put("name", file_name);
//                            fileobject.put("base64_encoded_data", base64file);

//                            registrationParams.put(file_value, fileobject.toString());
//                            if (requiredfields.has(file_value))
//                                requiredfields.remove(file_value);
                        }
                    } catch (Exception e) {
                        Toast.makeText(this, "Not allowed from this location", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                }
            }else if (requestCode == 4) {
                if (resultCode == RESULT_OK) {
                    try {
                        for(Image image:GalleryActivity.mSelectedImages){
                            addImageView(image.mUri,"images");
                        }
                    } catch (Exception e) {
                        Toast.makeText(this, "Not allowed from this location", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                }
            } else {
                if (resultCode == RESULT_OK) {
                    try {
                        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
                        if(imageReturnedIntent != null){
                            Uri selectedImageUri = imageReturnedIntent.getData();
                            if (selectedImageUri != null) {
                                imagePath = getRealPathFromURI(RegistrationDynamic.this,selectedImageUri);
                                Log.d("ImagePath", "Image path: " + imagePath);
                            }
                        }
                        Bitmap photo = BitmapFactory.decodeFile(imagePath, bmOptions);
                        String path = MediaStore.Images.Media.insertImage(getContentResolver(), photo, "Title", null);
                        Uri tempUri = Uri.parse(path);
                        Log.e("REpo", "tempUri: " + tempUri.toString());

                        // CALL THIS METHOD TO GET THE ACTUAL PATH
                        String producturi = getRealPathFromURI(RegistrationDynamic.this, tempUri);
                        String[] imagename = getRealPathFromURI(RegistrationDynamic.this, tempUri).split("/");
                        Log.i("REpo", "filename==" + producturi);
                        Log.i("REpo", "filename==" + imagename[imagename.length - 1]);
                        String productpicname = imagename[imagename.length - 1];
                        Log.i("REpo", "9044_name==" + productpicname);
                        if (calculateFileSize(producturi) > 5.0) {
                            Toast.makeText(this, "Image should be less than 5 MB", Toast.LENGTH_SHORT).show();
                        } else {
                            Log.e("REpo", "FileSize==" + calculateFileSize(producturi));
                            if (getResources().getString(R.string.Enable_Log).equals("yes")) {
                                Log.i("filename", "" + imagename[imagename.length - 1]);
                            }
                            productpicname = imagename[imagename.length - 1];

                            Log.d("REpo", "CreateProductWithType==" + productpicname);
                            Log.d("REpo", "CreateProductWithType==" + productpicname.substring(productpicname.indexOf('.')));

                            final InputStream imageStream = getContentResolver().openInputStream(tempUri);

                            Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);

                            ExifInterface exif = null;
                            try {
                                File pictureFile = new File(imagePath);
                                exif = new ExifInterface(pictureFile.getAbsolutePath());
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            int orientation = ExifInterface.ORIENTATION_NORMAL;

                            if (exif != null)
                                orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);

                            switch (orientation) {
                                case ExifInterface.ORIENTATION_ROTATE_90:
                                    selectedImage = rotateBitmap(selectedImage, 90);
                                    break;
                                case ExifInterface.ORIENTATION_ROTATE_180:
                                    selectedImage = rotateBitmap(selectedImage, 180);
                                    break;

                                case ExifInterface.ORIENTATION_ROTATE_270:
                                    selectedImage = rotateBitmap(selectedImage, 270);
                                    break;
                            }

                            selectedImage = getResizedBitmap(selectedImage, 300, 300);

                            pictureField.setImageBitmap(selectedImage);
                            Log.i("REpo", "onActivityresult_pictureTAG : " + pictureField.getTag());

                            encodedPicture = encodeImage(selectedImage, "mvp_bank_detials");

                            Log.i("REpo", "onActivityresult_encodedPicture : " + encodedPicture);

                            if (!encodedPicture.equalsIgnoreCase("")) {
                                JSONObject logo = new JSONObject();
                                try {
                                    logo.put("type", "file");
                                    int r = new Random().nextInt(2) + 2; // [0, 60] + 20 => [20, 80]
                                    random = random + r;
                                    logo.put("name", "image" + random + ".jpeg");
                                    Log.e("frozen", "random2== " + random);
                                    logo.put("base64_encoded_data", encodedPicture);
                                    Log.i("REpo", "imagejson : " + pictureField.getTag() + " " + logo.toString());
                                    registrationParams.put(pictureField.getTag().toString(), logo.toString());
                                    if (requiredfields.has(pictureField.getTag().toString()))
                                        requiredfields.remove(pictureField.getTag().toString());
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
            }
        }
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

    public static boolean isLink(String url) {
        // Regular expression to match a complete and valid URL
//        Pattern special = Pattern.compile ("[!@#$%&*()+=|<>?{}\\[\\]~]");
//        Matcher hasSpecial = special.matcher(url);
//        if(hasSpecial.find()){
//            //url.matches(".*\\d.*")
//            return false;
//        }else if(url.contains(" ")) {
//            return false;
//        }
//        else {
//           return true;
//        }
        String allowedChars = "abcdefghijklmnopqrstuvwxyz-";

        for (int i = 0; i < url.length(); i++) {
            char c = url.charAt(i);
            if (allowedChars.indexOf(c) == -1) {
                return false; // Found a disallowed character
            }
        }
        return true; // All characters are valid
    }

    private boolean isValidateEmail(String username) {
        if(username.contains(" "))
            return  false;
//        else if(username.contains(".co") && !username.contains(".com"))
//            return  false;
        String EMAIL_PATTERN = "[\\w.-]*[a-zA-Z0-9_]@[\\w.-]*[a-zA-Z0-9]\\.(com)";//"[\\w.-]*[a-zA-Z0-9_]@[\\w.-]*[a-zA-Z0-9]\\.(com|org|net|edu)";//"^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(username);
        return matcher.matches();
    }

    public static boolean isValidFullName(String name) {
        if (name == null) return false;
        String s = name.trim();
        if (s.isEmpty()) return false;

        s = s.replace('\u00A0', ' ');      // NBSP -> normal space
        s = s.replace("\u200C", "");       // remove ZWNJ (optional)
        s = s.replace("\u200D", "");       // remove ZWJ (optional)
        s = Normalizer.normalize(s, Normalizer.Form.NFC);

        String[] parts = s.split("\\s+");
        if (parts.length != 2) return false;

        String wordRegex = "^[\\p{L}\\p{M}]+$";
        for (String p : parts) {
            if (!p.matches(wordRegex)) return false;
        }
        return true;
    }

}
