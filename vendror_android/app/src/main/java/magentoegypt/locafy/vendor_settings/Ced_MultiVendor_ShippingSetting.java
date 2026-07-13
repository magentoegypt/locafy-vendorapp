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

package magentoegypt.locafy.vendor_settings;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.text.Html;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.cardview.widget.CardView;
import androidx.core.view.MenuItemCompat;

import magentoegypt.locafy.R;
import magentoegypt.locafy.api_request_response_section.AsyncResponse;
import magentoegypt.locafy.api_request_response_section.Ced_MultiVendor_ClientRequestResponse;
import magentoegypt.locafy.api_request_response_section.network_error_handling.Ced_MultiVendor_ConnectionDetector;
import magentoegypt.locafy.api_request_response_section.network_error_handling.Ced_MultiVendor_NoInternetconnection;
import magentoegypt.locafy.api_request_response_section.network_error_handling.Ced_MultiVendor_UnAuthourizedRequestError;
import magentoegypt.locafy_constant.Ced_MultiVendor_GlobalVariables;
import magentoegypt.locafy.base_app.Ced_MultiVendor_FontSetting;
import magentoegypt.locafy.base_app.Ced_MultiVendor_VendorFunctionalityList;
import magentoegypt.locafy.base_app.Ced_MultiVendor_VendorSplash;
import magentoegypt.locafy.base_app.GPSLocationProvider;
import magentoegypt.locafy.navigation_drawer.Activity.Ced_MultiVendor_NavigationActivity;
import magentoegypt.locafy.vendor_notification.Notification;
import magentoegypt.locafy.vendor_profile_section.Ced_MultiVendor_ProfileStatus;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;


public class Ced_MultiVendor_ShippingSetting extends Ced_MultiVendor_NavigationActivity {
    private final boolean isPhone = false;
    private final boolean isRunning = true;
    Ced_MultiVendor_FontSetting fontSetting;
    Ced_MultiVendor_ConnectionDetector connectionDetector;
    Ced_MultiVendor_VendorFunctionalityList functionalityList;
    String currenturl = "";
    String savesetting = "";
    String Jstring = "";
    LinkedHashMap<String, ArrayList<HashMap<String, String>>> finaldataforsetting;
    LinearLayout forpaymentmethoods;
    RelativeLayout MultiVendor_paymentmethodtagsrelative;
    TextView save;
    AppCompatTextView message;
    TextView transactionsettingstag;
    TextView paymentmethodtags;
    HashMap<String, String> label_values;
    HashMap<String, String> values_label;
    HashMap<String, String> datatopost;
    String countryurl = "";
    List<String> statecodelist;
    List<String> statelabellist;
    HashMap<String, String> label_codes_states;
    String valuecheck = "";
    String regionvalue = "";
    HashMap<String, String> statenandregion;
    ArrayList<Boolean> isVerified = new ArrayList<>();
    Timer timer;
    private GPSLocationProvider gpsLocationProvider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(this.getResources().getColor(R.color.status_bar_color));
        }
        fontSetting = new Ced_MultiVendor_FontSetting();

        connectionDetector = new Ced_MultiVendor_ConnectionDetector(getApplicationContext());
        functionalityList = new Ced_MultiVendor_VendorFunctionalityList(getApplicationContext());
        finaldataforsetting = new LinkedHashMap<>();
        label_values = new HashMap<>();
        values_label = new HashMap<>();
        datatopost = new HashMap<>();
        statenandregion = new HashMap<>();

        currenturl = session.getBase_Url() + "vmultishippingApi/setting/address/vendor_id/" + session.getVendorid() + "/hashkey/" + session.getHahkey();
        savesetting = session.getBase_Url() + "vmultishippingApi/setting/saveShippingAddress" + "/hashkey/" + session.getHahkey();
        countryurl = session.getBase_Url() + "vendorapi/index/getCountry";
        timer = new Timer();

        if (connectionDetector.isConnectingToInternet()) {
            try {
                ViewGroup content = findViewById(R.id.MultiVendor_frame_container);
                getLayoutInflater().inflate(R.layout.ced_multivendor_activity_shipping_setting, content, true);
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                getSupportActionBar().setHomeAsUpIndicator(R.drawable.backbutton);
                setname(session.getvendorname());
                setprofilepic(session.getvendorpic());
                changetitle(getResources().getString(R.string.title_activity_shipping_setting));
                message = findViewById(R.id.message);
                forpaymentmethoods = findViewById(R.id.MultiVendor_forpaymentmethoods);
                MultiVendor_paymentmethodtagsrelative = findViewById(R.id.MultiVendor_paymentmethodtagsrelative);
                save = findViewById(R.id.MultiVendor_save);
                transactionsettingstag = findViewById(R.id.MultiVendor_transactionsettingstag);
                paymentmethodtags = findViewById(R.id.MultiVendor_paymentmethodtags);
                fontSetting.setFontforTextviews(transactionsettingstag, "Roboto-Medium.ttf", getApplicationContext());
                fontSetting.setFontforTextviews(save, "Roboto-Medium.ttf", getApplicationContext());
                fontSetting.setFontforTextviews(paymentmethodtags, "Roboto-Medium.ttf", getApplicationContext());
                save.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            JSONArray mainfield = new JSONArray();
                            if (isVerified.size() > 0) {
                                isVerified.clear();
                            }

                            for (int c = 0; c < forpaymentmethoods.getChildCount(); c++) {
                                CardView card = (CardView) forpaymentmethoods.getChildAt(c);
                                LinearLayout sublinear = (LinearLayout) card.getChildAt(0);
                                TextView name = (TextView) sublinear.getChildAt(1);
                                JSONObject subfields = new JSONObject();
                                for (int sub = 2; sub < sublinear.getChildCount(); sub++) {
                                    LinearLayout layout = (LinearLayout) sublinear.getChildAt(sub);
                                    TextView type = (TextView) layout.getChildAt(1);
                                    TextView label = (TextView) layout.getChildAt(0);
                                    TextView tag = (TextView) layout.getChildAt(2);
                                    if (type.getText().toString().equals("text")) {
                                        EditText editText = (EditText) layout.getChildAt(3);
                                        if (!TextUtils.isEmpty(editText.getText().toString().trim())) {
                                            subfields.put(tag.getText().toString(), editText.getText().toString());
                                        } else {
                                            isVerified.add(false);
                                        }
                                        // Toast.makeText(getApplicationContext(),tag.getText().toString()+name.getText().toString()+view.getText().toString()+type.getText().toString()+label.getText().toString()+editText.getText().toString(),Toast.LENGTH_SHORT).show();
                                    }
                                    if (type.getText().toString().equals("select")) {
                                        if (tag.getText().toString().equals("region_id")) {
                                            final LinearLayout linearLayout = (LinearLayout) layout.getChildAt(3);
                                            Spinner spinner = (Spinner) linearLayout.getChildAt(0);
                                            EditText editText = (EditText) layout.getChildAt(4);
                                            if (editText.getText().toString().equals("spinner")) {
                                                if (statenandregion.containsKey(spinner.getSelectedItem())) {
                                                    subfields.put(tag.getText().toString(), statenandregion.get(spinner.getSelectedItem()));
                                                } else {
                                                    subfields.put(tag.getText().toString(), label_codes_states.get(spinner.getSelectedItem()));
                                                }
                                                subfields.put("region", "");
                                            } else {
                                                subfields.put("region_id", "");
                                                subfields.put("region", editText.getText().toString());
                                            }
                                        } else {
                                            final LinearLayout linearLayout = (LinearLayout) layout.getChildAt(3);
                                            Spinner spinner = (Spinner) linearLayout.getChildAt(0);
                                            /*Toast.makeText(getApplicationContext(), label_values.get(spinner.getSelectedItem()), Toast.LENGTH_SHORT).show();*/
                                            subfields.put(tag.getText().toString(), label_values.get(spinner.getSelectedItem()));
                                        }
                                    }
                                }
                                mainfield.put(subfields);
                            }
                            if (getResources().getString(R.string.Enable_Log).equals("yes")) {
                                Log.i("mainfields", "" + mainfield);
                            }
                            datatopost.put("shipping_settings_data", mainfield.toString());
                            datatopost.put("vendor_id", session.getVendorid());
                            if (isVerified.size() == 0) {
                                Ced_MultiVendor_ClientRequestResponse crr = new Ced_MultiVendor_ClientRequestResponse(new AsyncResponse() {
                                    @Override
                                    public void processFinish(Object output) throws JSONException {
                                        Jstring = output.toString();
                                        if (functionalityList.getExtensionAddon()) {
                                            JSONObject object1 = new JSONObject(Jstring);
                                            if (object1.has("vendor_approved")) {
                                                logout();
                                            } else {
                                                String success = object1.getJSONObject("data").getString("success");
                                                if (success.equals("true")) {
                                                    startActivity(new Intent(Ced_MultiVendor_ShippingSetting.this, Ced_MultiVendor_ShippingSetting.class));
                                                    overridePendingTransition(R.anim.ced_multivendor_slide_in, R.anim.ced_multivendor_slide_out);
                                                    finish();
                                                }
                                                Toast.makeText(getApplicationContext(), object1.getJSONObject("data").getString("message"), Toast.LENGTH_LONG).show();
                                            }
                                        } else {
                                            Intent intent = new Intent(getApplicationContext(), Ced_MultiVendor_UnAuthourizedRequestError.class);
                                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                            startActivity(intent);
                                            overridePendingTransition(R.anim.ced_multivendor_slide_in, R.anim.ced_multivendor_slide_out);
                                        }
                                    }
                                }, Ced_MultiVendor_ShippingSetting.this, "POST", datatopost);
                                crr.execute(savesetting);
                            } else {
                                Toast.makeText(Ced_MultiVendor_ShippingSetting.this, "Please fill all the fields", Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
                Ced_MultiVendor_ClientRequestResponse crr = new Ced_MultiVendor_ClientRequestResponse(new AsyncResponse() {
                    @Override
                    public void processFinish(Object output) throws JSONException {
                        Jstring = output.toString();
                        if (functionalityList.getExtensionAddon()) {
                            JSONObject jsonObject = new JSONObject(Jstring);
                            if (jsonObject.has("vendor_approved")) {
                                logout();
                            } else {
                                JSONObject data = new JSONObject(Jstring).getJSONObject("data");
                                if (data.has("success")) {
                                    if (data.getString("success").equals("false")) {
                                        message.setVisibility(View.VISIBLE);
                                        message.setText(data.getString("message"));
                                    }
                                } else {
                                    JSONArray fieldset = data.getJSONArray("address");
                                    for (int i = 0; i < fieldset.length(); i++) {
                                        JSONObject object = fieldset.getJSONObject(i);
                                        ArrayList<HashMap<String, String>> fieldsdata = new ArrayList<>();
                                        JSONObject fieldobject = object;
                                        if (fieldobject.has("label")) {
                                            String label = fieldobject.getString("label");
                                            if (getResources().getString(R.string.Enable_Log).equals("yes")) {
                                                Log.i("label", "" + label);
                                            }
                                            String value = fieldobject.getString("value");
                                            if (getResources().getString(R.string.Enable_Log).equals("yes")) {
                                                Log.i("value", "" + value);
                                            }
                                            String type = fieldobject.getString("type");
                                            String name = fieldobject.getString("name");
                                            String tag = fieldobject.getString("tag");
                                            String region_id = fieldobject.getString("region_id");
                                            if (tag.equals("region_id")) {
                                                if (!(value.isEmpty())) {
                                                    statenandregion.put(value, region_id);
                                                }
                                            }
                                            if (getResources().getString(R.string.Enable_Log).equals("yes")) {
                                                Log.i("type", "" + type);
                                            }
                                            if (!(label.isEmpty())) {
                                                HashMap<String, String> fileds = new HashMap<>();
                                                fileds.put("label", label);
                                                fileds.put("value", value);
                                                fileds.put("type", type);
                                                fileds.put("name", name);
                                                fileds.put("tag", tag);
                                                fileds.put("region_id", region_id);
                                                if (type.equals("select")) {
                                                    JSONArray values = fieldobject.getJSONArray("values");
                                                    String valuesdrop = "";
                                                    for (int v = 0; v < values.length(); v++) {
                                                        JSONObject object1 = values.getJSONObject(v);
                                                        valuesdrop = valuesdrop + object1.getString("label") + "#";
                                                        label_values.put(object1.getString("label"), object1.getString("value"));
                                                        values_label.put(object1.getString("value"), object1.getString("label"));
                                                    }
                                                    if (getResources().getString(R.string.Enable_Log).equals("yes")) {
                                                        Log.i("valuesdrop", i + "==" + valuesdrop);
                                                        Log.i("label_values", i + "==" + label_values);
                                                        Log.i("values_label", i + "==" + values_label);
                                                    }
                                                    fileds.put("values", valuesdrop);
                                                }
                                                fieldsdata.add(fileds);
                                                finaldataforsetting.put(label, fieldsdata);
                                            } else {
                                                regionvalue = fieldobject.getString("value");
                                            }
                                        }
                                    }
                                    if (getResources().getString(R.string.Enable_Log).equals("yes")) {
//                                    Log.i("finaldataforsetting", "" + finaldataforsetting);
                                    }
                                    Createform(finaldataforsetting);
                                    save.setVisibility(View.VISIBLE);
                                    forpaymentmethoods.setVisibility(View.VISIBLE);
                                    MultiVendor_paymentmethodtagsrelative.setVisibility(View.VISIBLE);
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
                }, Ced_MultiVendor_ShippingSetting.this);
                crr.execute(currenturl);
            } catch (Exception e) {
                if (getResources().getString(R.string.Enable_Log).equals("yes")) {
                    Log.i("ExceptionSettings", e.getMessage());
                }
                e.printStackTrace();
                Intent main = new Intent(getApplicationContext(), Ced_MultiVendor_VendorSplash.class);
                main.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                main.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(main);
                overridePendingTransition(R.anim.ced_multivendor_slide_in, R.anim.ced_multivendor_slide_out);
            }
        } else {
            Intent nointernet = new Intent(getApplicationContext(), Ced_MultiVendor_NoInternetconnection.class);
            nointernet.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            nointernet.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(nointernet);
            overridePendingTransition(R.anim.ced_multivendor_slide_in, R.anim.ced_multivendor_slide_out);
        }
    }

    public void Createform(HashMap<String, ArrayList<HashMap<String, String>>> finaldataforsetting) {
        try {
            Log.i("finaldataforsetting", "" + finaldataforsetting);
            Iterator iterator = finaldataforsetting.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry pair = (Map.Entry) iterator.next();
                String PaymentMethod = String.valueOf(pair.getKey());
                if (getResources().getString(R.string.Enable_Log).equals("yes")) {
                    Log.i("PaymentMethod", "" + PaymentMethod);
                }
                CardView cardView = new CardView(getApplicationContext());
                cardView.setCardBackgroundColor(getResources().getColor(R.color.white));
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                if (Build.VERSION_CODES.LOLLIPOP >= Build.VERSION.SDK_INT) {
                    params.setMargins(5, 5, 5, 5);
                } else {
                    cardView.setMaxCardElevation(5);
                }
                cardView.setCardElevation(5);
                cardView.setLayoutParams(params);
                LinearLayout payment = new LinearLayout(getApplicationContext());
                payment.setOrientation(LinearLayout.VERTICAL);
                TextView paymentmethodname = new TextView(getApplicationContext());
                fontSetting.setFontforTextviews(paymentmethodname, "Roboto-Medium.ttf", getApplicationContext());
                TextView name = new TextView(getApplicationContext());
                name.setVisibility(View.GONE);
                RelativeLayout layout = new RelativeLayout(getApplicationContext());
                layout.setVisibility(View.GONE);
                layout.setBackgroundColor(getResources().getColor(R.color.AppTheme));
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 70);
                layout.setLayoutParams(layoutParams);
                paymentmethodname.setText(PaymentMethod.toUpperCase());
                paymentmethodname.setTypeface(Typeface.DEFAULT_BOLD);
                paymentmethodname.setTextColor(getResources().getColor(R.color.white));
                payment.setPadding(0, 10, 0, 10);
                paymentmethodname.setPadding(10, 10, 10, 10);
                layout.addView(paymentmethodname);
                payment.addView(layout, 0);
                payment.addView(name, 1);
                ArrayList<HashMap<String, String>> fields = (ArrayList<HashMap<String, String>>) pair.getValue();
                Iterator iterator1 = fields.iterator();
                while (iterator1.hasNext()) {
                    final HashMap<String, String> fielddata = (HashMap<String, String>) iterator1.next();
                    LinearLayout fieldlayout = new LinearLayout(getApplicationContext());
                    fieldlayout.setOrientation(LinearLayout.VERTICAL);
                    TextView label = new TextView(getApplicationContext());
                    fontSetting.setFontforTextviews(label, "Roboto-Medium.ttf", getApplicationContext());
                    label.setText(Html.fromHtml(fielddata.get("label").toUpperCase()));
                    if (fielddata.get("label").contains("#")) {
                        label.setText(Html.fromHtml(fielddata.get("label").replace("#", "").toUpperCase()));
                    } else
                        label.setText(Html.fromHtml(fielddata.get("label").toUpperCase()));
                    name.setText(fielddata.get("name").toUpperCase());
                    label.setPadding(10, 5, 5, 5);
                    label.setTextColor(getResources().getColor(R.color.shadow));
                    String type = fielddata.get("type");
                    String tagstring = fielddata.get("tag");
                    fieldlayout.addView(label);
                    TextView typetextview = new TextView(getApplicationContext());
                    TextView tag = new TextView(getApplicationContext());
                    tag.setVisibility(View.GONE);
                    typetextview.setText(type);
                    tag.setText(tagstring);
                    typetextview.setVisibility(View.GONE);
                    fieldlayout.addView(typetextview);
                    fieldlayout.addView(tag);
                    if (type.equals("text")) {
                        final EditText view = new EditText(getApplicationContext());
                        view.setText(fielddata.get("value"));
                        view.setGravity(Gravity.CENTER);
                        view.setBackground(getResources().getDrawable(R.drawable.editext_border));
                        view.setPadding(5, 5, 5, 5);
                        if (paymentmethodname.getText().toString().equalsIgnoreCase("Phone No") || paymentmethodname.getText().toString().equalsIgnoreCase("Zip/Postal Code")) {
                            view.setInputType(InputType.TYPE_CLASS_NUMBER);
                        } else {
                            view.setInputType(InputType.TYPE_CLASS_TEXT);
                        }
                        if (fielddata.get("tag").equalsIgnoreCase("warehouseaddress")) {
                            getAddress(view, 0);
                        }
                       /* if (fielddata.get("tag").equalsIgnoreCase("postcode")) {
                            getAddress(view, 1);
                        }
                        if (fielddata.get("tag").equalsIgnoreCase("city")) {
                            getAddress(view, 2);
                        }*/
                        view.setTextColor(getResources().getColor(R.color.shadow));
                        view.setEnabled(true);
                        fieldlayout.addView(view);
                    }
                    if (type.equals("select")) {
                        View view = View.inflate(getApplicationContext(), R.layout.ced_multivendor_spinner, null);
                        final Spinner spinner = view.findViewById(R.id.MultiVendor_settingspinner);
                        ArrayList<String> valuesfordropdown = new ArrayList<>();
                        String values = fielddata.get("values");
                        if (getResources().getString(R.string.Enable_Log).equals("yes")) {
                            Log.i("valuedropdown", "" + fielddata.get("value"));
                        }
                        String[] parts = values.split("#");
                        if (!(fielddata.get("value").isEmpty())) {
                            if (values_label.containsKey(fielddata.get("value"))) {
                                if (tagstring.equals("country_id")) {
                                    valuecheck = values_label.get(fielddata.get("value"));
                                    if (getResources().getString(R.string.Enable_Log).equals("yes")) {
                                        Log.i("valuecheck", "" + valuecheck);
                                    }
                                }
                                valuesfordropdown.add(values_label.get(fielddata.get("value")));
                                for (int j = 0; j < parts.length; j++) {
                                    if (!(parts[j].equals(values_label.get(fielddata.get("value"))))) {
                                        valuesfordropdown.add(parts[j]);
                                    }
                                }
                            } else {
                                for (int j = 0; j < parts.length; j++) {
                                    valuesfordropdown.add(parts[j]);
                                }
                            }
                        } else {
                            for (int j = 0; j < parts.length; j++) {
                                valuesfordropdown.add(parts[j]);
                            }
                        }
                        ArrayAdapter<String> dropdown = new ArrayAdapter<>(Ced_MultiVendor_ShippingSetting.this, R.layout.ced_multivendor_textview, valuesfordropdown);
                        spinner.setAdapter(dropdown);
                        //   getState(label_values.get(spinner.getSelectedItem()));
                        if (tagstring.equals("country_id")) {
                            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                    if ((valuecheck.isEmpty())) {
                                        if (getResources().getString(R.string.Enable_Log).equals("yes")) {
                                            Log.i("valuecheck2", "" + valuecheck);
                                        }
                                        getState(label_values.get(spinner.getSelectedItem()));
                                    } else {
                                        if (getResources().getString(R.string.Enable_Log).equals("yes")) {
                                            Log.i("valuecheck3", "" + valuecheck);
                                        }
                                        valuecheck = "";
                                        if (getResources().getString(R.string.Enable_Log).equals("yes")) {
                                            Log.i("valuecheck4", "" + valuecheck);
                                        }
                                    }
                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> parent) {
                                }
                            });
                        }
                        fieldlayout.addView(view);
//                        fieldlayout.addView(spinner);
                        if (tagstring.equals("region_id")) {
                            if (!(regionvalue.isEmpty())) {
                                EditText editText = new EditText(getApplicationContext());
                                editText.setVisibility(View.VISIBLE);
                                editText.setText(regionvalue);
                                editText.setTextColor(getResources().getColor(R.color.shadow));
                                spinner.setVisibility(View.GONE);
                                fieldlayout.addView(editText);
                            } else {
                                //  Toast.makeText(this, "else", Toast.LENGTH_SHORT).show();
                                EditText editText = new EditText(getApplicationContext());
                                editText.setText("spinner");
                                editText.setVisibility(View.GONE);
                                editText.setTextColor(getResources().getColor(R.color.shadow));
                                if (!(fielddata.get("value").isEmpty())) {
                                    ArrayList<String> valuesforspinnerstate = new ArrayList<>();
                                    valuesforspinnerstate.add(fielddata.get("value"));
                                    Log.d("test-vaibhav", valuesforspinnerstate + "");
                                    ArrayAdapter<String> statedropdown = new ArrayAdapter<>(Ced_MultiVendor_ShippingSetting.this, R.layout.ced_multivendor_textview, valuesforspinnerstate);
                                    spinner.setAdapter(statedropdown);
                                }
                                spinner.setVisibility(View.VISIBLE);
                                fieldlayout.addView(editText);
                            }
                        }
                    }
                    fieldlayout.setPadding(5, 5, 5, 5);
                    payment.addView(fieldlayout);
                }
                cardView.addView(payment);
                forpaymentmethoods.addView(cardView);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getAddress(final EditText view, final int type) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle(R.string.current_location).setMessage(R.string.current_location_message).setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                LocationManager service = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                boolean enabled = service
                        .isProviderEnabled(LocationManager.GPS_PROVIDER);
                if (enabled) {
                    timer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            gpsLocationProvider = new GPSLocationProvider(Ced_MultiVendor_ShippingSetting.this);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if (isRunning) {
                                        if (type == 0) {
                                            getAddressFromLocation(GPSLocationProvider.latitude, GPSLocationProvider.longitude, view);
                                        } else if (type == 1) {
                                            getzipcode(GPSLocationProvider.latitude, GPSLocationProvider.longitude, view);
                                        } else if (type == 2) {
                                            getCity(GPSLocationProvider.latitude, GPSLocationProvider.longitude, view);
                                        }
                                    } else {
                                        timer.cancel();
                                        timer.purge();
                                    }
                                }
                            });
                        }
                    }, 100, 500);
                } else {
                    Toast.makeText(Ced_MultiVendor_ShippingSetting.this, "Please turn on GPS", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivity(intent);
                }
            }
        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).show();
    }

    private void getAddressFromLocation(double latitude, double longitude, EditText editText) {
        Geocoder geocoder = new Geocoder(this, Locale.ENGLISH);
        try {
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
            if (addresses.size() > 0) {
                Address fetchedAddress = addresses.get(0);
                String address_data = fetchedAddress.getAddressLine(0) + " , " +
                        fetchedAddress.getAdminArea() + " , " + fetchedAddress.getLocality() + " , " + fetchedAddress.getPostalCode();
                editText.setText(address_data);
                // isRunning = false;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void getzipcode(double latitude, double longitude, EditText editText) {
        Geocoder geocoder = new Geocoder(this, Locale.ENGLISH);
        try {
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
            if (addresses.size() > 0) {
                Address fetchedAddress = addresses.get(0);
                String address_data = fetchedAddress.getPostalCode();
                editText.setText(address_data);
                //   isRunning = false;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void getCity(double latitude, double longitude, EditText editText) {
        Geocoder geocoder = new Geocoder(this, Locale.ENGLISH);
        try {
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
            if (addresses.size() > 0) {
                Address fetchedAddress = addresses.get(0);
                String address_data = fetchedAddress.getLocality();
                editText.setText(address_data);
                //    isRunning = false;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        Ced_MultiVendor_ConnectionDetector connectionDetector = new Ced_MultiVendor_ConnectionDetector(getApplicationContext());
        if (connectionDetector.isConnectingToInternet()) {
            setname(session.getvendorname());
            setprofilepic(session.getvendorpic());
            changetitle("Shipping Settings");
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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.ced_multivendor_menu_login, menu);
        MenuItem item = menu.findItem(R.id.MultiVendor_notification);
        MenuItem item2 = menu.findItem(R.id.MultiVendor_profile);
        MenuItemCompat.setActionView(item, R.layout.ced_multivendor_feed_update_count);
        MenuItemCompat.setActionView(item2, R.layout.ced_multivendor_profilestatus);
        View profilecount = MenuItemCompat.getActionView(item2);
        profilecount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Toast.makeText(getApplicationContext(), "Profile", Toast.LENGTH_LONG).show();*/
                Intent intent = new Intent(getApplicationContext(), Ced_MultiVendor_ProfileStatus.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
            }
        });
        View notifCount = MenuItemCompat.getActionView(item);
        TextView notification = notifCount.findViewById(R.id.MultiVendor_notitext);
        notification.setText(Ced_MultiVendor_GlobalVariables.noti);
        notifCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Toast.makeText(getApplicationContext(), "Notification", Toast.LENGTH_LONG).show();*/
                Intent intent = new Intent(getApplicationContext(), Notification.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
            }
        });
        return true;
    }

    private void getState(String country_code) {
        Ced_MultiVendor_ClientRequestResponse response = new Ced_MultiVendor_ClientRequestResponse(new AsyncResponse() {
            @Override
            public void processFinish(Object output) throws JSONException {
                if (functionalityList.getExtensionAddon()) {
                    statelabellist = new ArrayList<>();
                    statecodelist = new ArrayList<>();
                    label_codes_states = new HashMap<>();
                    JSONObject object = new JSONObject(output.toString());
                    Boolean status = object.getBoolean("success");
                    if (status) {
                        JSONArray jsonArray = object.getJSONArray("states");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject c = jsonArray.getJSONObject(i);
                            statecodelist.add(c.getString("region_id"));
                            statelabellist.add(c.getString("default_name"));
                            label_codes_states.put(c.getString("default_name"), c.getString("region_id"));
                        }

                        for (int c = 0; c < forpaymentmethoods.getChildCount(); c++) {
                            CardView card = (CardView) forpaymentmethoods.getChildAt(c);
                            LinearLayout sublinear = (LinearLayout) card.getChildAt(0);
                            for (int sub = 2; sub < sublinear.getChildCount(); sub++) {
                                LinearLayout layout = (LinearLayout) sublinear.getChildAt(sub);
                                TextView tag = (TextView) layout.getChildAt(2);
                                if (tag.getText().toString().equals("region_id")) {
                                    final LinearLayout linearLayout = (LinearLayout) layout.getChildAt(3);
                                    final Spinner spinner = (Spinner) linearLayout.getChildAt(0);
                                    spinner.setVisibility(View.VISIBLE);
                                    ArrayAdapter<String> dropdown = new ArrayAdapter<>(Ced_MultiVendor_ShippingSetting.this, R.layout.ced_multivendor_textview, statelabellist);
                                    spinner.setAdapter(dropdown);
                                    EditText editText = (EditText) layout.getChildAt(4);
                                    editText.setText("spinner");
                                    editText.setVisibility(View.GONE);
                                }
                            }
                        }
                    } else {
                        for (int c = 0; c < forpaymentmethoods.getChildCount(); c++) {
                            CardView card = (CardView) forpaymentmethoods.getChildAt(c);
                            LinearLayout sublinear = (LinearLayout) card.getChildAt(0);
                            for (int sub = 2; sub < sublinear.getChildCount(); sub++) {
                                LinearLayout layout = (LinearLayout) sublinear.getChildAt(sub);
                                TextView tag = (TextView) layout.getChildAt(2);
                                if (tag.getText().toString().equals("region_id")) {
                                    final LinearLayout linearLayout = (LinearLayout) layout.getChildAt(3);
                                    final Spinner spinner = (Spinner) linearLayout.getChildAt(0);
                                    spinner.setVisibility(View.GONE);
                                    EditText editText = (EditText) layout.getChildAt(4);
                                    editText.setText("");
                                    editText.setHint(getResources().getString(R.string.selectstate));
                                    editText.setTextColor(getResources().getColor(R.color.background_dark));
                                    editText.setVisibility(View.VISIBLE);
                                }
                            }
                        }
                    }
                } else {
                    Intent intent = new Intent(getApplicationContext(), Ced_MultiVendor_UnAuthourizedRequestError.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
            }
        }, Ced_MultiVendor_ShippingSetting.this, "ShippingSetting");
        response.execute(countryurl + "/country_code/" + country_code);
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

    private void changecount() {
        invalidateOptionsMenu();
    }
}