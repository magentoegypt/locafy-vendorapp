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

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Html;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
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
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.MenuItemCompat;

import com.bumptech.glide.Glide;
import magentoegypt.locafy_constant.Ced_MultiVendor_GlobalVariables;
import magentoegypt.locafy.base_app.Ced_MultiVendor_VendorFunctionalityList;
import magentoegypt.locafy.navigation_drawer.Activity.Ced_MultiVendor_NavigationActivity;
import magentoegypt.locafy.api_request_response_section.network_error_handling.Ced_MultiVendor_ConnectionDetector;
import magentoegypt.locafy.api_request_response_section.network_error_handling.Ced_MultiVendor_NoInternetconnection;
import magentoegypt.locafy.api_request_response_section.network_error_handling.Ced_MultiVendor_UnAuthourizedRequestError;
import magentoegypt.locafy.api_request_response_section.AsyncResponse;
import magentoegypt.locafy.api_request_response_section.Ced_MultiVendor_ClientRequestResponse;
import magentoegypt.locafy.R;
import magentoegypt.locafy.vendor_notification.Notification;
import magentoegypt.locafy_constant.AppConstant;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Ced_MultiVendor_EditVendorProfileSection extends Ced_MultiVendor_NavigationActivity {

    CheckBox changepass;
    Ced_MultiVendor_ConnectionDetector connectionDetector;
    String Jstring;
    Ced_MultiVendor_VendorFunctionalityList functionalityList;
    LinearLayout changepasslinear;
    ScrollView mainvendoreditprofilescroll;
    Button browseprofilepic;
    Button browsecompanylogo;
    Button browsecompanybabber;
    EditText publicname;
    EditText Name;
    Spinner location;
    Spinner Gender;
    EditText Email;
    EditText ContactNumber;
    EditText CompanyName;
    EditText About;
    EditText supportnumber;
    EditText supportemail;
    EditText metakeyword;
    EditText MetaDescription;
    EditText fbfind;
    EditText findtwitter;
    EditText address;
    Spinner city;
    EditText zipcode;
    EditText State;
    EditText country;
    EditText currentpassword;
    EditText newpassword;
    EditText confirmnewpass;
    String CurrentUrl;
    TextView save;
    HashMap<String, String> dataforvendorprofile;
    String profilepicname = "nodata";
    String companylogoname = "nodata";
    String companybannername = "nodata";
    String profileuri = "nouri";
    String logouri = "nouri";
    String banneruri = "nouri";
    String countryurl = "";
    List<String> countrylabellist;
    List<String> statelabellist;
    List<String> countrycodelist;
    List<String> statecodelist;
    String country_code = "", state_code = "";
    String country_label, state_label = "";
    HashMap<String, String> mapcountrylabel;
    EditText complete_location;
    EditText latitude;
    EditText longitude;
    String city_url = "";
    HashMap<String, String> city_idval;
    HashMap<String, String> location_idval;
    HashMap<String, String> locationid_cityid;
    HashMap<String, String> city_val_id;
    HashMap<String, String> location_val_id;
    ArrayList<String> city_values;
    ArrayList<String> location_values;
    String selected_cityid = "";
    String selected_locationid = "";
    String saved_city = "";
    String saved_location = "";
    boolean is_saved_location = true;
    String encodedLogoImage = "";
    String encodedcompaneyLogoImage = "";
    String encodedcompaneyBannerImage = "";
    TextView MultiVendor_cnfrmpasstag;
    EditText MultiVendor_cnfrmpassword;
    private int SELECT_PHOTO = 1;
    private ImageView profilepicture;
    private ImageView companylogo;
    private ImageView companybabber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (android.os.Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

// add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

// finally change the color
            window.setStatusBarColor(this.getResources().getColor(R.color.status_bar_color));
        }
        connectionDetector = new Ced_MultiVendor_ConnectionDetector(getApplicationContext());
        functionalityList = new Ced_MultiVendor_VendorFunctionalityList(getApplicationContext());
        dataforvendorprofile = new HashMap<String, String>();
        mapcountrylabel = new HashMap<String, String>();
        if (connectionDetector.isConnectingToInternet()) {
            ViewGroup content = (ViewGroup) findViewById(R.id.MultiVendor_frame_container);
            getLayoutInflater().inflate(R.layout.ced_multivendor_activity_edit_vendor_profile_section, content, true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.backbutton);
            final int writeresult = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
            final int readresult = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE);
            countrylabellist = new ArrayList<String>();
            statelabellist = new ArrayList<String>();
            countrycodelist = new ArrayList<String>();
            statecodelist = new ArrayList<String>();
            location_val_id = new HashMap<>();
            city_idval = new HashMap<>();
            location_idval = new HashMap<>();
            locationid_cityid = new HashMap<>();
            city_val_id = new HashMap<>();
            city_values = new ArrayList<>();
            location_values = new ArrayList<>();

            MultiVendor_cnfrmpasstag = (TextView) findViewById(R.id.MultiVendor_cnfrmpasstag);
            MultiVendor_cnfrmpassword = (EditText) findViewById(R.id.MultiVendor_cnfrmpassword);

            if (session.getvendorname() != null) {
                setname(session.getvendorname());
                setprofilepic(session.getvendorpic());
                changetitle("Edit Profile");
            }
            CurrentUrl = session.getBase_Url() + "vendorapi/index/update";
            countryurl = session.getBase_Url() + "vendorapi/index/getCountry";
            city_url = session.getBase_Url() + "vproductfilterapi/index/configval";
            getcountry();
            //  getcity();
            final Intent intent = getIntent();
            mainvendoreditprofilescroll = (ScrollView) findViewById(R.id.MultiVendor_mainvendoreditprofilescroll);
            changepass = (CheckBox) findViewById(R.id.MultiVendor_changepass);
            browseprofilepic = (Button) findViewById(R.id.MultiVendor_browseprofilepic);
            browsecompanylogo = (Button) findViewById(R.id.MultiVendor_browsecompanylogo);
            browsecompanybabber = (Button) findViewById(R.id.MultiVendor_browsecompanybabber);
            changepasslinear = (LinearLayout) findViewById(R.id.MultiVendor_changepasslinear);
            int id = Resources.getSystem().getIdentifier("btn_check_holo_light", "drawable", "android");
            changepass.setButtonDrawable(id);
            State = (EditText) findViewById(R.id.MultiVendor_State);
            country = (EditText) findViewById(R.id.MultiVendor_country);
            zipcode = (EditText) findViewById(R.id.MultiVendor_zipcode);
            city = (Spinner) findViewById(R.id.MultiVendor_city);
            publicname = (EditText) findViewById(R.id.MultiVendor_publicname);
            address = (EditText) findViewById(R.id.MultiVendor_address);
            fbfind = (EditText) findViewById(R.id.MultiVendor_fbfind);
            findtwitter = (EditText) findViewById(R.id.MultiVendor_findtwitter);
            Name = (EditText) findViewById(R.id.MultiVendor_Name);
            location = (Spinner) findViewById(R.id.MultiVendor_location);
            metakeyword = (EditText) findViewById(R.id.MultiVendor_metakeyword);
            Email = (EditText) findViewById(R.id.MultiVendor_Email);
            CompanyName = (EditText) findViewById(R.id.MultiVendor_CompanyName);
            ContactNumber = (EditText) findViewById(R.id.MultiVendor_ContactNumber);
            currentpassword = (EditText) findViewById(R.id.MultiVendor_currentpassword);
            supportemail = (EditText) findViewById(R.id.MultiVendor_supportemail);
            MetaDescription = (EditText) findViewById(R.id.MultiVendor_MetaDescription);
            About = (EditText) findViewById(R.id.MultiVendor_About);
            supportnumber = (EditText) findViewById(R.id.MultiVendor_supportnumber);
            newpassword = (EditText) findViewById(R.id.MultiVendor_newpassword);
            profilepicture = (ImageView) findViewById(R.id.MultiVendor_profilepicture);
            companylogo = (ImageView) findViewById(R.id.MultiVendor_companylogo);
            companybabber = (ImageView) findViewById(R.id.MultiVendor_companybabber);
            Gender = (Spinner) findViewById(R.id.MultiVendor_Gender);
            save = (TextView) findViewById(R.id.MultiVendor_save);
            complete_location = (EditText) findViewById(R.id.MultiVendor_CompleteLocation);
            latitude = (EditText) findViewById(R.id.MultiVendor_Latitude);
            longitude = (EditText) findViewById(R.id.MultiVendor_Longitude);
            profilepicture.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showImagePOP(profilepicture);
                }
            });
            companylogo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showImagePOP(companylogo);

                }
            });
            companybabber.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showImagePOP(companybabber);
                }
            });
            changepass.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        changepasslinear.setVisibility(View.VISIBLE);
                        newpassword.requestFocus();

                    } else {
                        changepasslinear.setVisibility(View.GONE);

                    }

                }
            });

            browseprofilepic.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {

                    if (writeresult == PackageManager.PERMISSION_GRANTED && readresult == PackageManager.PERMISSION_GRANTED) {
                        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                        photoPickerIntent.setType("image/*");
                        SELECT_PHOTO = 1;
                        startActivityForResult(photoPickerIntent, 1);

                    } else {

                        if (ActivityCompat.shouldShowRequestPermissionRationale(Ced_MultiVendor_EditVendorProfileSection.this, Manifest.permission.READ_EXTERNAL_STORAGE) && ActivityCompat.shouldShowRequestPermissionRationale(Ced_MultiVendor_EditVendorProfileSection.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

                            Toast.makeText(Ced_MultiVendor_EditVendorProfileSection.this, "Storage permission allows us to access data. Please allow in App Settings for additional functionality.", Toast.LENGTH_LONG).show();

                        } else {

                            ActivityCompat.requestPermissions(Ced_MultiVendor_EditVendorProfileSection.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                        }

                    }

                }
            });
            browsecompanylogo.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    if (writeresult == PackageManager.PERMISSION_GRANTED && readresult == PackageManager.PERMISSION_GRANTED) {
                        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                        photoPickerIntent.setType("image/*");
                        SELECT_PHOTO = 2;
                        startActivityForResult(photoPickerIntent, 2);

                    } else {

                        if (ActivityCompat.shouldShowRequestPermissionRationale(Ced_MultiVendor_EditVendorProfileSection.this, Manifest.permission.READ_EXTERNAL_STORAGE) && ActivityCompat.shouldShowRequestPermissionRationale(Ced_MultiVendor_EditVendorProfileSection.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

                            Toast.makeText(Ced_MultiVendor_EditVendorProfileSection.this, "Storage permission allows us to access data. Please allow in App Settings for additional functionality.", Toast.LENGTH_LONG).show();

                        } else {

                            ActivityCompat.requestPermissions(Ced_MultiVendor_EditVendorProfileSection.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 2);
                        }

                    }
                }
            });
            browsecompanybabber.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    if (writeresult == PackageManager.PERMISSION_GRANTED && readresult == PackageManager.PERMISSION_GRANTED) {
                        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                        photoPickerIntent.setType("image/*");
                        SELECT_PHOTO = 3;
                        startActivityForResult(photoPickerIntent, 3);

                    } else {

                        if (ActivityCompat.shouldShowRequestPermissionRationale(Ced_MultiVendor_EditVendorProfileSection.this, Manifest.permission.READ_EXTERNAL_STORAGE) && ActivityCompat.shouldShowRequestPermissionRationale(Ced_MultiVendor_EditVendorProfileSection.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

                            Toast.makeText(Ced_MultiVendor_EditVendorProfileSection.this, "Storage permission allows us to access data. Please allow in App Settings for additional functionality.", Toast.LENGTH_LONG).show();

                        } else {

                            ActivityCompat.requestPermissions(Ced_MultiVendor_EditVendorProfileSection.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 3);
                        }

                    }
                }
            });
            publicname.setText(intent.getStringExtra("publicname"));
            Name.setText(intent.getStringExtra("Name"));
            Email.setText(intent.getStringExtra("email").trim());
            ContactNumber.setText(intent.getStringExtra("ContactNumber"));
            CompanyName.setText(intent.getStringExtra("CompanyName"));
            About.setText(intent.getStringExtra("About"));
            //location.setText(intent.getStringExtra("location"));
            saved_location = intent.getStringExtra("location");
            supportnumber.setText(intent.getStringExtra("supportnumber"));
            supportemail.setText(intent.getStringExtra("supportemail").trim());
            metakeyword.setText(intent.getStringExtra("metakeywords"));
            MetaDescription.setText(intent.getStringExtra("metaDescription"));
            fbfind.setText(intent.getStringExtra("fbid"));
            findtwitter.setText(intent.getStringExtra("twitterid"));
            address.setText(intent.getStringExtra("address"));
            //  city.setText(intent.getStringExtra("city"));
            saved_city = intent.getStringExtra("city");
            State.setText(intent.getStringExtra("State"));
            complete_location.setText(intent.getStringExtra("completelocation"));
            latitude.setText(intent.getStringExtra("latitude"));
            longitude.setText(intent.getStringExtra("longitude"));
            if (getResources().getString(R.string.Enable_Log).equals("yes")) {
                Log.i("Countryedit", "" + intent.getStringExtra("Country"));
            }
            country.setText(intent.getStringExtra("Country").trim());
            country.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final CharSequence[] arrayOfInt = (CharSequence[]) countrycodelist.toArray(new CharSequence[countrycodelist.size()]);
                    final CharSequence[] arrayOfInt2 = (CharSequence[]) countrylabellist.toArray(new CharSequence[countrylabellist.size()]);
                    Dialog levelDialog1 = new Dialog(Ced_MultiVendor_EditVendorProfileSection.this);
                    final AlertDialog.Builder builder = new AlertDialog.Builder(Ced_MultiVendor_EditVendorProfileSection.this);
                    builder.setTitle(Html.fromHtml("<font color='#000000'>Select Your  Country:</font>"));
                    builder.setSingleChoiceItems(arrayOfInt2, -1, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int postion) {
                                    country_code = (String) arrayOfInt[postion];
                                    country_label = (String) arrayOfInt2[postion];
                                    country.setText(country_label);
                                    getState(country_code);
                                    dialog.dismiss();
                                }


                            }
                    );
                    levelDialog1 = builder.create();
                    levelDialog1.show();
                }
            });
            zipcode.setText(intent.getStringExtra("zipcode"));
            if (intent.getStringExtra("Gender").equals("Male")) {
                Gender.setSelection(1);
            } else {
                Gender.setSelection(0);
            }
            Glide.with(getApplicationContext())
                    .load(intent.getStringExtra("profilepic"))
                    .placeholder(R.drawable.placeholder)   // optional
                    .error(R.drawable.placeholder)      // optional
                    .into(profilepicture);
            Glide.with(getApplicationContext())
                    .load(intent.getStringExtra("comapnylogo"))
                    .placeholder(R.drawable.placeholder)   // optional
                    .error(R.drawable.placeholder)      // optional
                    .into(companylogo);
            Glide.with(getApplicationContext())
                    .load(intent.getStringExtra("companybanner"))
                    .placeholder(R.drawable.placeholder)   // optional
                    .error(R.drawable.placeholder)      // optional
                    .into(companybabber);
            save.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AppConstant.lockButton(v);
                    if (isValidateEmail(Email.getText().toString()) && (!(Email.getText().toString().isEmpty()))) {
                        Log.i("dataforvendorprofile", profileuri.trim());
                        Log.i("dataforvendorprofile", logouri.replace(" ", ""));
                        Log.i("dataforvendorprofile", banneruri.trim());


                        dataforvendorprofile.put("vendor_id", session.getVendorid());
                        dataforvendorprofile.put("hashkey", session.getHahkey());
                        dataforvendorprofile.put("public_name", publicname.getText().toString());
                        dataforvendorprofile.put("name", Name.getText().toString());
                        dataforvendorprofile.put("email", Email.getText().toString());
                        dataforvendorprofile.put("contact_number", ContactNumber.getText().toString());
                        dataforvendorprofile.put("company_name", CompanyName.getText().toString());
                        dataforvendorprofile.put("about", About.getText().toString());
                        dataforvendorprofile.put("vendor_location", selected_locationid);
                        dataforvendorprofile.put("support_number", supportnumber.getText().toString());
                        dataforvendorprofile.put("support_email", supportemail.getText().toString());
                        dataforvendorprofile.put("meta_keywords", metakeyword.getText().toString());
                        dataforvendorprofile.put("meta_description", MetaDescription.getText().toString());
                        dataforvendorprofile.put("facebook_id", fbfind.getText().toString());
                        dataforvendorprofile.put("twitter_id", findtwitter.getText().toString());
                        dataforvendorprofile.put("address", address.getText().toString());
                        dataforvendorprofile.put("vendor_city", selected_cityid);
                        dataforvendorprofile.put("vendor_complete_location", complete_location.getText().toString());
                        dataforvendorprofile.put("vendor_latitude", latitude.getText().toString());
                        dataforvendorprofile.put("vendor_longitude", longitude.getText().toString());

                        /***************************************Images JSON OBJECTS***************************************************/

                        try {
                            if (!encodedLogoImage.equalsIgnoreCase("")) {
                                Log.i("vaibhav89606", "inprofilepic");
                                JSONObject logo = new JSONObject();
                                logo.put("type", "file");
                                logo.put("name", "profile.jpeg");
                                logo.put("base64_encoded_data", encodedLogoImage);
                                dataforvendorprofile.put("profile_picture", logo.toString());
                                Log.i("testimage", logo.toString());
                            }

                            if (!encodedcompaneyLogoImage.equalsIgnoreCase("")) {
                                Log.i("vaibhav89606", "inlogo");
                                JSONObject company_logo = new JSONObject();
                                company_logo.put("type", "file");
                                company_logo.put("name", "company_logo.jpeg");
                                company_logo.put("base64_encoded_data", encodedcompaneyLogoImage);
                                dataforvendorprofile.put("company_logo", company_logo.toString());
                                Log.i("testimage", company_logo.toString());
                            }

                            if (!encodedcompaneyBannerImage.equalsIgnoreCase("")) {
                                Log.i("vaibhav89606", "in_banner");
                                JSONObject company_banner = new JSONObject();
                                company_banner.put("type", "file");
                                company_banner.put("name", "company_banner.jpeg");
                                company_banner.put("base64_encoded_data", encodedcompaneyBannerImage);
                                dataforvendorprofile.put("company_banner", company_banner.toString());
                                Log.i("testimage", company_banner.toString());
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        /***************************************Images JSON OBJECTS***************************************************/


                        if (state_code.isEmpty()) {
                            dataforvendorprofile.put("region", State.getText().toString());
                            dataforvendorprofile.put("region_id", " ");
                        } else {
                            dataforvendorprofile.put("region", " ");
                            dataforvendorprofile.put("region_id", state_code);
                        }
                        dataforvendorprofile.put("country_id", country_code);
                        dataforvendorprofile.put("zip_code", zipcode.getText().toString());
                        dataforvendorprofile.put("gender", (String) Gender.getSelectedItem());
                        if (changepass.isChecked()) {
                            if (currentpassword.getText().toString().isEmpty()) {
                                currentpassword.setError(getResources().getString(R.string.empty));
                                currentpassword.requestFocus();
                            } else {
                                //  Toast.makeText(Ced_MultiVendor_EditVendorProfileSection.this, ""+isValidatePassword(newpassword.getText().toString()), Toast.LENGTH_SHORT).show();

                                if (newpassword.getText().toString().isEmpty() && !isValidatePassword(newpassword.getText().toString())) {
                                    newpassword.setError(getResources().getString(R.string.shortpass));
                                    newpassword.requestFocus();
                                } else {
                                    if (MultiVendor_cnfrmpassword.getText().toString().equals(newpassword.getText().toString())) {
                                        dataforvendorprofile.put("currentpassword", currentpassword.getText().toString());
                                        dataforvendorprofile.put("newpassword", newpassword.getText().toString());

                                        Ced_MultiVendor_ClientRequestResponse crr = new Ced_MultiVendor_ClientRequestResponse(new AsyncResponse() {

                                            @Override
                                            public void processFinish(Object output) throws JSONException {
                                                Jstring = output.toString();

                                                if (functionalityList.getExtensionAddon()) {
                                                    JSONObject data = new JSONObject(Jstring);
                                                    if (data.has("vendor_approved")) {
                                                        logout();
                                                    } else {
                                                        String sucess = data.getJSONObject("data").getString("success");
                                                        if (sucess.equals("true")) {
                                                            Ced_MultiVendor_GlobalVariables.noti = data.getJSONObject("data").getString("valerts");
                                                            Ced_MultiVendor_GlobalVariables.progress = data.getJSONObject("data").getInt("profile_complete");
                                                            changecount();
                                                            Toast.makeText(Ced_MultiVendor_EditVendorProfileSection.this, data.getJSONObject("data").getString("message"), Toast.LENGTH_LONG).show();
                                                            Intent intent1 = new Intent(Ced_MultiVendor_EditVendorProfileSection.this, Ced_MultiVendor_VendorProfile.class);
                                                            intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                            startActivity(intent1);
                                                            overridePendingTransition(R.anim.ced_multivendor_slide_in, R.anim.ced_multivendor_slide_out);
                                                        } else {
                                                            Toast.makeText(Ced_MultiVendor_EditVendorProfileSection.this, data.getJSONObject("data").getString("message"), Toast.LENGTH_LONG).show();

                                                        }
                                                    }

                                                } else {
                                                    Intent intent = new Intent(getApplicationContext(), Ced_MultiVendor_UnAuthourizedRequestError.class);
                                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                                    startActivity(intent);
                                                    overridePendingTransition(R.anim.ced_multivendor_slide_in, R.anim.ced_multivendor_slide_out);
                                                }
                                            }
                                        }, Ced_MultiVendor_EditVendorProfileSection.this, "POST", dataforvendorprofile);
                                        crr.execute(CurrentUrl);
                                    } else {
                                        newpassword.setError(getResources().getString(R.string.passworddontmatch));


                                        MultiVendor_cnfrmpassword.setError(getResources().getString(R.string.passworddontmatch));
                                        MultiVendor_cnfrmpassword.requestFocus();
                                    }
                                }
                            }
                        } else {
                            Log.i("citydropdown", "post data " + dataforvendorprofile.toString());

                            Log.i("vaibhav89696", dataforvendorprofile.toString());
                            Ced_MultiVendor_ClientRequestResponse crr = new Ced_MultiVendor_ClientRequestResponse(new AsyncResponse() {

                                @Override
                                public void processFinish(Object output) throws JSONException {
                                    Jstring = output.toString();
                                    if (functionalityList.getExtensionAddon()) {
                                        JSONObject data = new JSONObject(Jstring);
                                        if (data.has("vendor_approved")) {
                                            logout();
                                        } else {
                                            String sucess = data.getJSONObject("data").getString("success");
                                            if (sucess.equals("true")) {
                                                Ced_MultiVendor_GlobalVariables.noti = data.getJSONObject("data").getString("valerts");
                                                Ced_MultiVendor_GlobalVariables.progress = data.getJSONObject("data").getInt("profile_complete");
                                                changecount();
                                                Toast.makeText(Ced_MultiVendor_EditVendorProfileSection.this, data.getJSONObject("data").getString("message"), Toast.LENGTH_LONG).show();
                                                Intent intent1 = new Intent(Ced_MultiVendor_EditVendorProfileSection.this, Ced_MultiVendor_VendorProfile.class);
                                                intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                startActivity(intent1);
                                                overridePendingTransition(R.anim.ced_multivendor_slide_in, R.anim.ced_multivendor_slide_out);
                                            } else {
                                                Toast.makeText(Ced_MultiVendor_EditVendorProfileSection.this, data.getJSONObject("data").getString("message"), Toast.LENGTH_LONG).show();

                                            }
                                        }

                                    } else {
                                        Intent intent = new Intent(getApplicationContext(), Ced_MultiVendor_UnAuthourizedRequestError.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                        overridePendingTransition(R.anim.ced_multivendor_slide_in, R.anim.ced_multivendor_slide_out);
                                    }


                                }
                            }, Ced_MultiVendor_EditVendorProfileSection.this, "POST", dataforvendorprofile);
                            crr.execute(CurrentUrl);
                        }
                    } else {
                        Email.setError(getResources().getString(R.string.invalidemail));
                        Email.requestFocus();
                    }
                }
            });
        } else {
            Intent intent = new Intent(getApplicationContext(), Ced_MultiVendor_NoInternetconnection.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            overridePendingTransition(R.anim.ced_multivendor_slide_in, R.anim.ced_multivendor_slide_out);
        }


    }

    private void changecount() {
        //     invalidateOptionsMenu();
    }

    @Override
    protected void onResume() {
        Ced_MultiVendor_ConnectionDetector connectionDetector = new Ced_MultiVendor_ConnectionDetector(getApplicationContext());
        if (connectionDetector.isConnectingToInternet()) {
            setname(session.getvendorname());
            setprofilepic(session.getvendorpic());
            changetitle("Edit Profile");
            //   invalidateOptionsMenu();
            super.onResume();

        } else {
            Intent intent = new Intent(getApplicationContext(), Ced_MultiVendor_NoInternetconnection.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            overridePendingTransition(R.anim.ced_multivendor_slide_in, R.anim.ced_multivendor_slide_out);
            super.onResume();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);

        switch (SELECT_PHOTO) {
            case 1:
                if (resultCode == RESULT_OK) {
                    try {
                        final Uri imageUri = imageReturnedIntent.getData();
                        profileuri = getRealPathFromURI(Ced_MultiVendor_EditVendorProfileSection.this, imageUri);
                        profileuri = profileuri.trim();
                        String[] imagename = getRealPathFromURI(Ced_MultiVendor_EditVendorProfileSection.this, imageUri).split("/");
                        if (getResources().getString(R.string.Enable_Log).equals("yes")) {
                            Log.i("filename", "" + imagename[imagename.length - 1].trim());
                        }
                        profilepicname = imagename[imagename.length - 1];
                        final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                        final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                        profilepicture.setImageBitmap(selectedImage);
                        encodedLogoImage = encodeImage(selectedImage);
                        Log.i("encodedimage", encodedLogoImage);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
                break;
            case 2:
                if (resultCode == RESULT_OK) {
                    try {
                        final Uri imageUri = imageReturnedIntent.getData();
                        logouri = getRealPathFromURI(Ced_MultiVendor_EditVendorProfileSection.this, imageUri);
                        logouri = logouri.trim();
                        String[] imagename = getRealPathFromURI(Ced_MultiVendor_EditVendorProfileSection.this, imageUri).split("/");
                        if (getResources().getString(R.string.Enable_Log).equals("yes")) {
                            Log.i("filename", "" + imagename[imagename.length - 1].trim());
                        }
                        companylogoname = imagename[imagename.length - 1];
                        final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                        final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                        companylogo.setImageBitmap(selectedImage);
                        encodedcompaneyLogoImage = encodeImage(selectedImage);
                        Log.i("encodedimage", encodedcompaneyLogoImage);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
                break;
            case 3:
                if (resultCode == RESULT_OK) {
                    try {
                        final Uri imageUri = imageReturnedIntent.getData();
                        banneruri = getRealPathFromURI(Ced_MultiVendor_EditVendorProfileSection.this, imageUri);
                        banneruri = banneruri.trim();
                        String[] imagename = getRealPathFromURI(Ced_MultiVendor_EditVendorProfileSection.this, imageUri).split("/");
                        if (getResources().getString(R.string.Enable_Log).equals("yes")) {
                            Log.i("filename", "" + imagename[imagename.length - 1]);
                        }
                        companybannername = imagename[imagename.length - 1];
                        final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                        final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                        companybabber.setImageBitmap(selectedImage);
                        encodedcompaneyBannerImage = encodeImage(selectedImage);
                        Log.i("encodedimage", encodedcompaneyBannerImage);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }

                }
                break;
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


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.ced_multivendor_menu_login, menu);
        MenuItem item = menu.findItem(R.id.MultiVendor_notification);
        MenuItem item2 = menu.findItem(R.id.MultiVendor_profile);
        MenuItemCompat.setActionView(item, R.layout.ced_multivendor_feed_update_count);
        MenuItemCompat.setActionView(item2, R.layout.ced_multivendor_profilestatus);
        View profilecount = (View) MenuItemCompat.getActionView(item2);
        profilecount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Profile", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getApplicationContext(), Ced_MultiVendor_ProfileStatus.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);

            }
        });
        View notifCount = (View) MenuItemCompat.getActionView(item);
        TextView notification = (TextView) notifCount.findViewById(R.id.MultiVendor_notitext);
        notification.setText(Ced_MultiVendor_GlobalVariables.noti);
        notifCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Notification", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getApplicationContext(), Notification.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
            }
        });

        return true;
    }

    private void getcountry() {
        try {
            Ced_MultiVendor_ClientRequestResponse crr = new Ced_MultiVendor_ClientRequestResponse(new AsyncResponse() {
                @Override
                public void processFinish(Object output) throws JSONException {
                    if (functionalityList.getExtensionAddon()) {
                        JSONObject jsonObject = new JSONObject(output.toString());
                        if (jsonObject.has("header") && jsonObject.getString("header").equals("false")) {
                            // Toast.makeText(getApplicationContext(), getResources().getString(R.string.url_not_found), Toast.LENGTH_LONG).show();
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
            }, Ced_MultiVendor_EditVendorProfileSection.this, "EditVendorProfile");
            crr.execute(countryurl);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getState(String country_code) {
        Ced_MultiVendor_ClientRequestResponse response = new Ced_MultiVendor_ClientRequestResponse(new AsyncResponse() {
            @Override
            public void processFinish(Object output) throws JSONException {
                if (functionalityList.getExtensionAddon()) {
                    JSONObject object = new JSONObject(output.toString());
                    Boolean status = object.getBoolean("success");
                    if (status.equals(true)) {
                        State.setText("");
                        // State.setKeyListener(null);
                        State.setHint(getResources().getString(R.string.taptoselectstate));
                        State.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                final CharSequence[] arrayOfInt = (CharSequence[]) statecodelist.toArray(new CharSequence[statecodelist.size()]);
                                final CharSequence[] arrayOfInt2 = (CharSequence[]) statelabellist.toArray(new CharSequence[statelabellist.size()]);
                                Dialog levelDialog1 = new Dialog(Ced_MultiVendor_EditVendorProfileSection.this);
                                final AlertDialog.Builder builder = new AlertDialog.Builder(Ced_MultiVendor_EditVendorProfileSection.this);
                                builder.setTitle(Html.fromHtml("<font color='#000000'>Select Your  STATE:</font>"));
                                builder.setSingleChoiceItems(arrayOfInt2, -1, new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int postion) {
                                                state_code = (String) arrayOfInt[postion];
                                                state_label = (String) arrayOfInt2[postion];
                                                State.setText(state_label);
                                                dialog.dismiss();
                                            }
                                        }

                                );
                                levelDialog1 = builder.create();
                                levelDialog1.show();
                            }
                        });
                        JSONArray jsonArray = object.getJSONArray("states");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject c = jsonArray.getJSONObject(i);
                            statecodelist.add(c.getString("region_id"));
                            statelabellist.add(c.getString("default_name"));
                        }
                    } else {
                        State.setText("");
                        State.setHint(getResources().getString(R.string.selectstate));
                        State.setOnClickListener(null);
                    }
                } else {
                    Intent intent = new Intent(getApplicationContext(), Ced_MultiVendor_UnAuthourizedRequestError.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
            }
        }, Ced_MultiVendor_EditVendorProfileSection.this, "EditVendorProfile");
        response.execute(countryurl + "/country_code/" + country_code);

    }

    private void getcity() {
        Ced_MultiVendor_ClientRequestResponse response = new Ced_MultiVendor_ClientRequestResponse(new AsyncResponse() {
            @Override
            public void processFinish(Object output) throws JSONException {
                JSONObject city_jsonObject = new JSONObject(output.toString());
                if (city_jsonObject.getJSONObject("data").getBoolean("success")) {
                    JSONArray city_jsonarray = city_jsonObject.getJSONObject("data").getJSONArray("city");
                    if (city_jsonarray.length() > 0) {
                        for (int i = 0; i < city_jsonarray.length(); i++) {
                            JSONObject city_object = city_jsonarray.getJSONObject(i);
                            city_idval.put(city_object.getString("city_id"), city_object.getString("city_name"));
                            city_val_id.put(city_object.getString("city_name"), city_object.getString("city_id"));
                            city_values.add(city_object.getString("city_name"));
                        }
                        /*city_values.remove(city_values.indexOf(saved_city));
                        city_values.add(0,saved_city);*/
                        if (!saved_city.equals(city_values.get(0)) && !saved_city.isEmpty()) {
                            Log.i("selectedcity", "saved city " + saved_city + " value at 0 " + city_values.get(0));
                            Collections.swap(city_values, 0, city_values.indexOf(saved_city));
                        }
                        ArrayAdapter<String> city_adapter = new ArrayAdapter<String>(Ced_MultiVendor_EditVendorProfileSection.this, R.layout.ced_multivendor_textview, R.id.text1, city_values);
                        Log.i("vaibhavtest", "city" + city_values);
                        city.setAdapter(city_adapter);
                        city.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                            @Override
                            public void onItemSelected(AdapterView<?> adapterView, View view,
                                                       int position, long id) {
                                String selected_city = city_values.get(position);
                                selected_cityid = city_val_id.get(selected_city);
                                Log.i("cityspinner", "selected city " + selected_city);
                                Log.i("cityspinner", "city id " + selected_city);
                                location_values.clear();
                                Iterator location_itr = locationid_cityid.entrySet().iterator();
                                while (location_itr.hasNext()) {
                                    Map.Entry pair = (Map.Entry) location_itr.next();
                                    if (pair.getValue().equals(selected_cityid)) {
                                        location_values.add(location_idval.get(pair.getKey()));
                                    }
                                }
                                Log.i("Vaibhavtest", "Location" + location_values);
                                if (!saved_location.isEmpty() && !saved_location.equals(location_values.get(0)) && is_saved_location) {
                                    Log.i("selectedcity", "saved location if " + saved_location + " value at 0 " + location_values.get(0));
                                    Collections.swap(location_values, 0, location_values.indexOf(saved_location));
                                }
                                Log.i("locationvalues", location_values.toString());
                                ArrayAdapter<String> location_adapter = new ArrayAdapter<String>(Ced_MultiVendor_EditVendorProfileSection.this, R.layout.ced_multivendor_textview, R.id.text1, location_values);
                                location.setAdapter(location_adapter);
                                is_saved_location = false;
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> adapterView) {
                                // TODO Auto-generated method stub

                            }
                        });
                        location.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                            @Override
                            public void onItemSelected(AdapterView<?> adapterView, View view,
                                                       int position, long id) {
                                String selected_location = location_values.get(position);
                                selected_locationid = location_val_id.get(selected_location);

                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> adapterView) {
                                // TODO Auto-generated method stub

                            }
                        });
                    }
                    JSONArray location_jsonarray = city_jsonObject.getJSONObject("data").getJSONArray("location");
                    if (location_jsonarray.length() > 0) {
                        for (int i = 0; i < location_jsonarray.length(); i++) {
                            JSONObject location_object = location_jsonarray.getJSONObject(i);
                            location_idval.put(location_object.getString("id"), location_object.getString("city_location"));
                            location_val_id.put(location_object.getString("city_location"), location_object.getString("id"));
                            locationid_cityid.put(location_object.getString("id"), location_object.getString("city_id"));
                        }
                    }
                }
            }
        }, Ced_MultiVendor_EditVendorProfileSection.this, "EditVendorProfile");
        response.execute(city_url);
    }

    private boolean isValidateEmail(String username) {
        String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(username);
        return matcher.matches();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                    photoPickerIntent.setType("image/*");
                    SELECT_PHOTO = 1;
                    startActivityForResult(photoPickerIntent, 1);
                } else {
                    Toast.makeText(getApplicationContext(), "Permission Denied", Toast.LENGTH_LONG).show();
                }
                break;
            case 2:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                    photoPickerIntent.setType("image/*");
                    SELECT_PHOTO = 2;
                    startActivityForResult(photoPickerIntent, 2);

                } else {
                    Toast.makeText(getApplicationContext(), "Permission Denied", Toast.LENGTH_LONG).show();
                }
                break;
            case 3:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {


                    Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                    photoPickerIntent.setType("image/*");
                    SELECT_PHOTO = 3;
                    startActivityForResult(photoPickerIntent, 3);

                } else {

                    Toast.makeText(getApplicationContext(), "Permission Denied", Toast.LENGTH_LONG).show();
                }
                break;
        }
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

    private String encodeImage(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] b = baos.toByteArray();
        String encImage = Base64.encodeToString(b, Base64.DEFAULT);

        return encImage;
    }

    private boolean isValidatePassword(String username) {
        String EMAIL_PATTERN = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$";
        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(username);
        return matcher.matches();
    }
}
