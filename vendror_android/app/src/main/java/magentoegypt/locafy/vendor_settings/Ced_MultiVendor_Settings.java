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

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.core.view.MenuItemCompat;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import magentoegypt.locafy.base_app.Ced_MultiVendor_FontSetting;
import magentoegypt.locafy.base_app.Ced_MultiVendor_VendorSplash;
import magentoegypt.locafy_constant.Ced_MultiVendor_GlobalVariables;
import magentoegypt.locafy.base_app.Ced_MultiVendor_VendorFunctionalityList;
import magentoegypt.locafy.navigation_drawer.Activity.Ced_MultiVendor_NavigationActivity;
import magentoegypt.locafy.api_request_response_section.network_error_handling.Ced_MultiVendor_ConnectionDetector;
import magentoegypt.locafy.api_request_response_section.network_error_handling.Ced_MultiVendor_NoInternetconnection;
import magentoegypt.locafy.api_request_response_section.network_error_handling.Ced_MultiVendor_UnAuthourizedRequestError;
import magentoegypt.locafy.vendor_profile_section.Ced_MultiVendor_ProfileStatus;
import magentoegypt.locafy.api_request_response_section.AsyncResponse;
import magentoegypt.locafy.api_request_response_section.Ced_MultiVendor_ClientRequestResponse;
import magentoegypt.locafy.R;
import magentoegypt.locafy.vendor_notification.Notification;
import magentoegypt.locafy.vendor_transaction.Ced_MultiVendor_ListTransaction;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Ced_MultiVendor_Settings extends Ced_MultiVendor_NavigationActivity {
    Ced_MultiVendor_FontSetting fontSetting;
    Ced_MultiVendor_ConnectionDetector connectionDetector;
    Ced_MultiVendor_VendorFunctionalityList functionalityList;
    String currenturl = "";
    String savesetting = "";
    String Jstring = "";
    HashMap<String, ArrayList<HashMap<String, String>>> finaldataforsetting;
    LinearLayout forpaymentmethoods;
    TextView save;
    TextView transactionsettingstag;
    TextView paymentmethodtags;
    HashMap<String, String> label_values;
    HashMap<String, String> values_label;
    HashMap<String, String> datatopost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (android.os.Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(this.getResources().getColor(R.color.status_bar_color));
        }
        fontSetting = new Ced_MultiVendor_FontSetting();
        connectionDetector = new Ced_MultiVendor_ConnectionDetector(getApplicationContext());
        functionalityList = new Ced_MultiVendor_VendorFunctionalityList(getApplicationContext());
        finaldataforsetting = new HashMap<>();
        label_values = new HashMap<>();
        values_label = new HashMap<>();
        datatopost = new HashMap<>();
        currenturl = session.getBase_Url() + "vendorapi/setting/availableMethod/vendor_id/" + session.getVendorid() + "/hashkey/" + session.getHahkey();
        savesetting = session.getBase_Url() + "vendorapi/setting/index" + "/hashkey/" + session.getHahkey();
        if (connectionDetector.isConnectingToInternet()) {
            try {
                ViewGroup content = findViewById(R.id.MultiVendor_frame_container);
                getLayoutInflater().inflate(R.layout.ced_multivendor_activity_settings, content, true);
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                getSupportActionBar().setHomeAsUpIndicator(R.drawable.backbutton);
                setname(session.getvendorname());
                setprofilepic(session.getvendorpic());
                changetitle("Settings");
                final SwipeRefreshLayout pullToRefresh = findViewById(R.id.pullToRefresh);
                pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        pullToRefresh.setRefreshing(true);
                        Intent intent = new Intent(Ced_MultiVendor_Settings.this, Ced_MultiVendor_Settings.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                        startActivity(intent);
                        overridePendingTransition(R.anim.ced_multivendor_slide_in, R.anim.ced_multivendor_slide_out);
                    }
                });
                forpaymentmethoods = findViewById(R.id.MultiVendor_forpaymentmethoods);
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
                            JSONObject mainfield = new JSONObject();
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
                                        //Toast.makeText(getApplicationContext(),tag.getText().toString()+name.getText().toString()+view.getText().toString()+type.getText().toString()+label.getText().toString()+editText.getText().toString(),Toast.LENGTH_SHORT).show();
                                        subfields.put(tag.getText().toString(), editText.getText().toString());
                                    }
                                    if (type.getText().toString().equals("select")) {
                                        LinearLayout linearLayout = (LinearLayout) layout.getChildAt(3);
                                        Spinner spinner = (Spinner) linearLayout.getChildAt(0);
                                        //Toast.makeText(getApplicationContext(),tag.getText().toString()+name.getText().toString()+view.getText().toString()+type.getText().toString()+label.getText().toString()+spinner.getSelectedItem(),Toast.LENGTH_SHORT).show();
                                        subfields.put(tag.getText().toString(), label_values.get(spinner.getSelectedItem()));
                                    }
                                }
                                mainfield.put(name.getText().toString(), subfields);
                            }
                            if (getResources().getString(R.string.Enable_Log).equals("yes")) {
                                Log.i("mainfields", "" + mainfield);
                            }
                            datatopost.put("settings_data", mainfield.toString());
                            datatopost.put("vendor_id", session.getVendorid());
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
                                                Ced_MultiVendor_GlobalVariables.noti = object1.getJSONObject("data").getString("valerts");
                                                changecount();
                                                Toast.makeText(getApplicationContext(), object1.getJSONObject("data").getString("message"), Toast.LENGTH_LONG).show();
                                            } else {
                                                Toast.makeText(getApplicationContext(), object1.getJSONObject("data").getString("message"), Toast.LENGTH_LONG).show();
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
                            }, Ced_MultiVendor_Settings.this, "POST", datatopost);
                            crr.execute(savesetting);
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
                                JSONArray fieldset = data.getJSONArray("fieldset");
                                for (int i = 0; i < fieldset.length(); i++) {
                                    JSONObject object = fieldset.getJSONObject(i);
                                    JSONArray names = object.names();
                                    String paymentmethodname = names.getString(0);
                                    if(paymentmethodname.equalsIgnoreCase("Check/Money Order") || paymentmethodname.equalsIgnoreCase("PayPal"))
                                        continue;
                                    if (getResources().getString(R.string.Enable_Log).equals("yes")) {
                                        Log.i("namepayment", "" + paymentmethodname);
                                    }
                                    JSONArray fieldarray = object.getJSONArray(paymentmethodname);
                                    ArrayList<HashMap<String, String>> fieldsdata = new ArrayList<HashMap<String, String>>();
                                    for (int j = 0; j < fieldarray.length(); j++) {
                                        JSONObject fieldobject = fieldarray.getJSONObject(j);
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

                                        if (getResources().getString(R.string.Enable_Log).equals("yes")) {
                                            Log.i("type", "" + type);
                                        }
                                        HashMap<String, String> fileds = new HashMap<String, String>();
                                        fileds.put("label", label);
                                        fileds.put("value", value);
                                        fileds.put("type", type);
                                        fileds.put("name", name);
                                        fileds.put("tag", tag);
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
                                                Log.i("valuesdrop", "" + valuesdrop);
                                                Log.i("label_values", "" + label_values);
                                                Log.i("values_label", "" + values_label);
                                            }

                                            fileds.put("values", valuesdrop);
                                        }
                                        fieldsdata.add(fileds);
                                    }
                                    if(currenturl.contains("/ar")){
                                        if(paymentmethodname.equalsIgnoreCase("Check/Money Order"))
                                            paymentmethodname = "تحقق/امر الدفع";
                                        else if(paymentmethodname.equalsIgnoreCase("PayPal"))
                                            paymentmethodname = "باي بال";
                                        else if(paymentmethodname.equalsIgnoreCase("Bank Transfer"))
                                            paymentmethodname = "تحويل بنكي";
                                    }
                                    finaldataforsetting.put(paymentmethodname, fieldsdata);

                                }
                                if (getResources().getString(R.string.Enable_Log).equals("yes")) {
                                    Log.i("finaldataforsetting", "" + finaldataforsetting);
                                }
                                Createform(finaldataforsetting);
                            }
                        } else {
                            Intent intent = new Intent(getApplicationContext(), Ced_MultiVendor_UnAuthourizedRequestError.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            overridePendingTransition(R.anim.ced_multivendor_slide_in, R.anim.ced_multivendor_slide_out);
                        }


                    }
                }, Ced_MultiVendor_Settings.this);
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
                    params.setMargins(5, 5, 5,
                            5);
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
                layout.setBackgroundColor(getResources().getColor(R.color.white));
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 70);
                layoutParams.setMargins(15, 10, 10, 5);

                layout.setLayoutParams(layoutParams);
                if (PaymentMethod.contains("#")) {
                    paymentmethodname.setText(PaymentMethod.replace("#", "").toUpperCase());
                } else
                    paymentmethodname.setText(PaymentMethod.toUpperCase());
                paymentmethodname.setTextColor(getResources().getColor(R.color.shadow));
                payment.setPadding(0, 10, 0, 10);
                paymentmethodname.setPadding(10, 10, 10, 10);
                layout.addView(paymentmethodname);
                payment.addView(layout, 0);
                payment.addView(name, 1);
                ArrayList<HashMap<String, String>> fields = (ArrayList<HashMap<String, String>>) pair.getValue();
                Iterator iterator1 = fields.iterator();
                while (iterator1.hasNext()) {
                    HashMap<String, String> fielddata = (HashMap<String, String>) iterator1.next();
                    LinearLayout.LayoutParams params_sub = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    params_sub.setMargins(25, 0, 0, 0);
                    LinearLayout fieldlayout = new LinearLayout(getApplicationContext());
                    fieldlayout.setLayoutParams(params_sub);
                    fieldlayout.setOrientation(LinearLayout.VERTICAL);
                    TextView label = new TextView(getApplicationContext());
                    fontSetting.setFontforTextviews(label, "Roboto-Medium.ttf", getApplicationContext());
                    label.setText(fielddata.get("label"));
                    name.setText(fielddata.get("name"));
                    label.setPadding(10, 5, 5, 5);
                    label.setTextColor(getResources().getColor(R.color.shadow));
                    String type = fielddata.get("type");
                    String tagstring = fielddata.get("tag");
                    fieldlayout.addView(label, 0);
                    TextView typetextview = new TextView(getApplicationContext());
                    TextView tag = new TextView(getApplicationContext());
                    tag.setVisibility(View.GONE);
                    typetextview.setText(type);
                    tag.setText(tagstring);
                    typetextview.setVisibility(View.GONE);
                    fieldlayout.addView(typetextview, 1);
                    fieldlayout.addView(tag, 2);
                    if (type.equals("text")) {
                        View edittext_layout = View.inflate(getApplicationContext(), R.layout.ced_multivendor_edittext_layout, null);
                        EditText view = edittext_layout.findViewById(R.id.MultiVendor_edittextfield);
                        //  EditText view=new EditText(getApplicationContext());
                        view.setText(fielddata.get("value"));
                        //view.setPadding(10, 5, 5, 5);
                        view.setTextColor(getResources().getColor(R.color.black));
                        //view.setEnabled(true);
                        /*LinearLayout.LayoutParams edittextparams = new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.MATCH_PARENT, 70);
                        view.setLayoutParams(edittextparams);*/
                        fieldlayout.addView(edittext_layout, 3);
                    }
                    if (type.equals("select")) {
                        View view = View.inflate(getApplicationContext(), R.layout.ced_multivendor_spinner, null);
                        Spinner spinner = view.findViewById(R.id.MultiVendor_settingspinner);
                        // Spinner spinner=new Spinner(getApplicationContext());
                        /*spinner.setPopupBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.white)));*/

                        String values = fielddata.get("values");
                        Log.d("vaibhavfieldtest", "" + fielddata);
                        if (getResources().getString(R.string.Enable_Log).equals("yes")) {
                            Log.i("valuedropdown", "" + fielddata.get("value"));
                        }
                        String[] parts = values.split("#");
                        ArrayList<String> valuesfordropdown = new ArrayList<String>();
                        if (!(fielddata.get("value").isEmpty())) {
                            if (values_label.containsKey(fielddata.get("value"))) {

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
                        Log.i("asd", valuesfordropdown.toString());
                        ArrayAdapter<String> dropdown = new ArrayAdapter<String>(Ced_MultiVendor_Settings.this, R.layout.ced_multivendor_textview, valuesfordropdown);
                        spinner.setAdapter(dropdown);
                        String value = fielddata.get("value");
                        if (valuesfordropdown.contains(value)) {
                            spinner.setSelection(valuesfordropdown.indexOf(value));
                        }
                        //spinner.setSelection(dropdown.getPosition(fielddata.get("value")));
                        fieldlayout.addView(view, 3);
//                        fieldlayout.addView(spinner, 3);
                    }
                    fieldlayout.setPadding(5, 5, 5, 5);
                    payment.addView(fieldlayout);
                }
                cardView.addView(payment, 0);
                forpaymentmethoods.addView(cardView);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void onResume() {
        Ced_MultiVendor_ConnectionDetector connectionDetector = new Ced_MultiVendor_ConnectionDetector(getApplicationContext());
        if (connectionDetector.isConnectingToInternet()) {

            setname(session.getvendorname());
            setprofilepic(session.getvendorpic());
            changetitle("Settings");
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
                Toast.makeText(getApplicationContext(), "Profile", Toast.LENGTH_LONG).show();
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
                Toast.makeText(getApplicationContext(), "Notification", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getApplicationContext(), Notification.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
            }
        });

        return true;
    }

    private void changecount() {
        //  invalidateOptionsMenu();
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
}
