package magentoegypt.locafy.vendor_cms.Ced_MultiVendor_StaticBlock;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatSpinner;

import magentoegypt.locafy.base_app.Ced_MultiVendor_VendorFunctionalityList;
import magentoegypt.locafy.navigation_drawer.Activity.Ced_MultiVendor_NavigationActivity;
import magentoegypt.locafy.api_request_response_section.network_error_handling.Ced_MultiVendor_ConnectionDetector;
import magentoegypt.locafy.api_request_response_section.network_error_handling.Ced_MultiVendor_NoInternetconnection;
import magentoegypt.locafy.api_request_response_section.AsyncResponse;
import magentoegypt.locafy.api_request_response_section.Ced_ClientRequestResponseRest_New;
import magentoegypt.locafy.vendor_session.Ced_MultiVendor_VendorSessionManagement;
import magentoegypt.locafy.R;
import magentoegypt.locafy_constant.AppConstant;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class Ced_MultiVendor_NewBlock extends Ced_MultiVendor_NavigationActivity {

    Ced_MultiVendor_ConnectionDetector connectionDetector;
    Ced_MultiVendor_VendorFunctionalityList functionalityList;
    Ced_MultiVendor_VendorSessionManagement vendorSessionManagement;
    String createblock_url = "";
    int id;
    List<String> storeview;
    JSONArray storeView_data, store_ids;
    String storeview_url = "";
    String blockstatus = "";
    JSONObject createHashmap;
    CheckBox storeviewbox;
    EditText block_title;
    EditText block_identifier;
    Button block_savepage;
    LinearLayout generalinfo_layout;
    LinearLayout generalinfotexttag;
    LinearLayout block_storeview;
    AppCompatSpinner block_status;
    EditText block_content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        storeview_url = session.getBase_Url() + "rest/all/V1/vcms/dropdownoptions";
        createblock_url = session.getBase_Url() + "rest/all/V1/vcmsblock/save";
        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        createHashmap = new JSONObject();
        storeview = new ArrayList<>();
        storeView_data = new JSONArray();
        store_ids = new JSONArray();
        connectionDetector = new Ced_MultiVendor_ConnectionDetector(getApplicationContext());
        functionalityList = new Ced_MultiVendor_VendorFunctionalityList(getApplicationContext());
        vendorSessionManagement = new Ced_MultiVendor_VendorSessionManagement(getApplicationContext());
        if (connectionDetector.isConnectingToInternet()) {
            ViewGroup content = findViewById(R.id.MultiVendor_frame_container);
            getLayoutInflater().inflate(R.layout.ced_multivendor_newblock, content, true);
            id = Resources.getSystem().getIdentifier("btn_check_holo_light", "drawable", "android");
            block_title = findViewById(R.id.MultiVendor_Block_title);
            block_identifier = findViewById(R.id.MultiVendor_Block_identifier);
            block_content = findViewById(R.id.MultiVendor_block_content);
            block_savepage = findViewById(R.id.MultiVendor_block_savepage);
            storeviewbox = findViewById(R.id.MultiVendor_Block_storeviewtext);
            storeviewbox.setButtonDrawable(id);
            block_storeview = findViewById(R.id.MultiVendor_Block_storeview);
            block_status = findViewById(R.id.MultiVendor_block_status_spinner);
            generalinfo_layout = findViewById(R.id.layout_generalinfotag);
            generalinfotexttag = findViewById(R.id.MultiVendor_generalinfotexttag);
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
            final boolean[] show = {false};
            generalinfotexttag.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AppConstant.lockButton(v);
                    if (show[0]) {
                        generalinfo_layout.setVisibility(View.GONE);
                        show[0] = false;
                    } else {
                        generalinfo_layout.setVisibility(View.VISIBLE);
                        show[0] = true;
                    }
                }
            });

            storeviewbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked)
                        block_storeview.setVisibility(View.VISIBLE);
                    else
                        block_storeview.setVisibility(View.GONE);
                }
            });

            Ced_ClientRequestResponseRest_New response = new Ced_ClientRequestResponseRest_New(new AsyncResponse() {
                @Override
                public void processFinish(Object output) {
                    try {
                        String string = output.toString();
                        JSONObject object = new JSONObject(string);
                        String success = object.getString("is_success");
                        if (success.equals("true")) {
                            JSONObject dropdown_data = object.getJSONObject("dropdown_data");
                            JSONArray names = dropdown_data.names();
                            if (Objects.requireNonNull(names).length() > 0)
                                for (int n = 0; n < names.length(); n++) {
                                    switch (names.getString(n)) {
                                        case "layout_data":
                                            Log.i("frozen", "layout_data = " + dropdown_data.getJSONArray(names.getString(n)));
                                            break;
                                        case "store_ids":
                                            store_ids = dropdown_data.getJSONArray(names.getString(n));
                                            break;
                                        case "status":
                                            Log.i("frozen", "status = " + dropdown_data.getJSONArray(names.getString(n)));
                                            break;
                                        default:
                                            Log.e("frozen", "extra data = " + dropdown_data.getJSONArray(names.getString(n)));
                                    }
                                }
                            HashMap<String, String> hashMap2;
                            for (int i = 0; i < store_ids.length(); i++) {
                                JSONObject stores = store_ids.getJSONObject(i);
                                hashMap2 = new HashMap<>();
                                final LinearLayout mainwebsite = new LinearLayout(Ced_MultiVendor_NewBlock.this);
                                mainwebsite.setOrientation(LinearLayout.VERTICAL);
                                if (stores.get("value") instanceof JSONArray) {
                                    JSONArray jsonArray = stores.getJSONArray("value");
                                    if (jsonArray.isNull(0)) {
                                        final TextView defaultbox = new TextView(Ced_MultiVendor_NewBlock.this);
                                        defaultbox.setText(stores.getString("label"));
                                        defaultbox.setTypeface(Typeface.DEFAULT_BOLD);
                                        mainwebsite.addView(defaultbox, 0);
                                    } else {
                                        final TextView magetext = new TextView(Ced_MultiVendor_NewBlock.this);
                                        magetext.setText(stores.getString("label"));
                                        magetext.setTypeface(Typeface.DEFAULT_BOLD);
                                        mainwebsite.addView(magetext, 0);
                                        for (int s = 0; s < jsonArray.length(); s++) {
                                            JSONObject jsonObject = jsonArray.getJSONObject(s);
                                            final String objlabel = jsonObject.getString("label");
                                            String objvalue = jsonObject.getString("value");
                                            hashMap2.put(objlabel, objvalue);
                                            final LinearLayout checkboxlinear = new LinearLayout(Ced_MultiVendor_NewBlock.this);
                                            checkboxlinear.setOrientation(LinearLayout.VERTICAL);
                                            final CheckBox magebox = new CheckBox(Ced_MultiVendor_NewBlock.this);
                                            magebox.setButtonDrawable(id);
                                            magebox.setText(objlabel);
                                            final HashMap<String, String> finalHashMap = hashMap2;
                                            magebox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                                @Override
                                                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                                    if (isChecked) {
                                                        storeView_data.put(finalHashMap.get(objlabel));
                                                    } else {
                                                        try {
                                                            for (int i = 0; i < storeView_data.length(); i++) {
                                                                if (Objects.requireNonNull(finalHashMap.get(objlabel)).equalsIgnoreCase(storeView_data.getString(i))) {
                                                                    storeView_data.remove(i);
                                                                }
                                                            }
                                                        } catch (Exception e) {
                                                            e.printStackTrace();
                                                        }
                                                    }
                                                }
                                            });

                                            checkboxlinear.addView(magebox);
                                            mainwebsite.addView(checkboxlinear);
                                        }
                                    }
                                } else {
                                    final CheckBox mainwebsitename = new CheckBox(Ced_MultiVendor_NewBlock.this);
                                    mainwebsitename.setButtonDrawable(id);
                                    final String key = stores.getString("label");
                                    String value = stores.getString("value");
                                    hashMap2.put(key, value);
                                    mainwebsitename.setText(stores.getString("label"));
                                    final HashMap<String, String> finalHashMap1 = hashMap2;
                                    mainwebsitename.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                        @Override
                                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                            if (isChecked) {
                                                storeView_data.put(finalHashMap1.get(key));
                                            } else {
                                                try {
                                                    for (int i = 0; i < storeView_data.length(); i++) {
                                                        if (Objects.requireNonNull(finalHashMap1.get(key)).equalsIgnoreCase(storeView_data.getString(i))) {
                                                            storeView_data.remove(i);
                                                        }
                                                    }
                                                } catch (Exception e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        }
                                    });
                                    mainwebsite.addView(mainwebsitename, 0);
                                }
                                block_storeview.addView(mainwebsite);
                            }
                        } else
                            Toast.makeText(Ced_MultiVendor_NewBlock.this, object.getString("message"), Toast.LENGTH_LONG).show();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, Ced_MultiVendor_NewBlock.this);
            response.execute(storeview_url);
            block_status.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    if (adapterView.getSelectedItem().toString().equals("Enabled"))
                        blockstatus = "true";
                    else {
                        if (adapterView.getSelectedItem().toString().equals("Disabled"))
                            blockstatus = "false";
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });

            block_savepage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AppConstant.lockButton(view);
                    if (blockstatus.equals("false") || blockstatus.equals("true")) {
                        try {
                            createHashmap.put("title", block_title.getText().toString());
                            createHashmap.put("vendor_id", vendorSessionManagement.getVendorid());
                            createHashmap.put("identifier", block_identifier.getText().toString());
                            createHashmap.put("content", block_content.getText().toString());
                            createHashmap.put("is_active", blockstatus);
                            createHashmap.put("store_id", storeView_data);
                            Log.i("staticblock", "postdata-- " + createHashmap);
                            JSONObject post = new JSONObject();
                            post.put("cmsBlockData", createHashmap);
                            Ced_ClientRequestResponseRest_New response = new Ced_ClientRequestResponseRest_New(new AsyncResponse() {
                                @Override
                                public void processFinish(Object output) {
                                    try {
                                        String string = output.toString();
                                        JSONObject object = new JSONObject(string);
                                        String success = object.getString("success");
                                        if (success.equals("true")) {
                                            Toast.makeText(Ced_MultiVendor_NewBlock.this, object.getString("message"), Toast.LENGTH_SHORT).show();
                                            Intent in = new Intent(Ced_MultiVendor_NewBlock.this, Ced_MultiVendor_BlockListing.class);
                                            in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                            in.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                            startActivity(in);
                                        } else {
                                            String message = object.getString("message");
                                            Toast.makeText(Ced_MultiVendor_NewBlock.this, message, Toast.LENGTH_LONG).show();
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }, Ced_MultiVendor_NewBlock.this, "POST", post.toString(), false, false);
                            response.execute(createblock_url);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        Toast.makeText(Ced_MultiVendor_NewBlock.this, "Select value", Toast.LENGTH_SHORT).show();
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
}