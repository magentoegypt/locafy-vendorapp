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

import androidx.appcompat.widget.AppCompatTextView;

import magentoegypt.locafy.base_app.Ced_MultiVendor_VendorFunctionalityList;
import magentoegypt.locafy.navigation_drawer.Activity.Ced_MultiVendor_NavigationActivity;
import magentoegypt.locafy.api_request_response_section.network_error_handling.Ced_MultiVendor_ConnectionDetector;
import magentoegypt.locafy.api_request_response_section.network_error_handling.Ced_MultiVendor_NoInternetconnection;
import magentoegypt.locafy.api_request_response_section.AsyncResponse;
import magentoegypt.locafy.api_request_response_section.Ced_ClientRequestResponseRest_New;
import magentoegypt.locafy.R;
import magentoegypt.locafy_constant.AppConstant;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class Ced_MultiVendor_UpdateBlock extends Ced_MultiVendor_NavigationActivity {

    Ced_MultiVendor_ConnectionDetector connectionDetector;
    Ced_MultiVendor_VendorFunctionalityList functionalityList;
    String updateblock_url = "";
    String viewblock_url = "";
    String block_ID = "";
    int id;
    List<String> storeview_list;
    String blockstatus = "";
    CheckBox storeviewbox;
    String storeview_url = "";
    LinearLayout block_storeview;
    JSONObject createHashmap;
    EditText block_title;
    EditText block_identifier;
    Button block_savepage;
    LinearLayout generalinfo_layout;
    LinearLayout generalinfotexttag;
    androidx.appcompat.widget.AppCompatSpinner block_status;
    EditText block_content;
    List<String> stringList;
    JSONArray storeview, store_ids;
    AppCompatTextView heading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewblock_url = session.getBase_Url() + "rest/all/V1/vcmsblock/view";
        updateblock_url = session.getBase_Url() + "rest/all/V1/vcmsblock/update";
        storeview_url = session.getBase_Url() + "rest/all/V1/vcms/dropdownoptions";

        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        createHashmap = new JSONObject();
        storeview_list = new ArrayList<>();
        storeview = new JSONArray();
        store_ids = new JSONArray();
        stringList = new ArrayList<>();
        connectionDetector = new Ced_MultiVendor_ConnectionDetector(getApplicationContext());
        functionalityList = new Ced_MultiVendor_VendorFunctionalityList(getApplicationContext());

        if (connectionDetector.isConnectingToInternet()) {
            ViewGroup content = findViewById(R.id.MultiVendor_frame_container);
            getLayoutInflater().inflate(R.layout.ced_multivendor_newblock, content, true);

            block_ID = getIntent().getStringExtra("blockid");
            getCmsBlockViewData();
            getStoreViewData();
            id = Resources.getSystem().getIdentifier("btn_check_holo_light", "drawable", "android");
            heading = findViewById(R.id.heading);
            heading.setText(getString(R.string.update_cms_block));
            block_title = findViewById(R.id.MultiVendor_Block_title);
            block_identifier = findViewById(R.id.MultiVendor_Block_identifier);
            block_content = findViewById(R.id.MultiVendor_block_content);
            storeviewbox = findViewById(R.id.MultiVendor_Block_storeviewtext);
            storeviewbox.setButtonDrawable(id);
            block_storeview = findViewById(R.id.MultiVendor_Block_storeview);
            block_status = findViewById(R.id.MultiVendor_block_status_spinner);
            block_savepage = findViewById(R.id.MultiVendor_block_savepage);
            block_savepage.setText(getString(R.string.update_txt));
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
                    if (blockstatus.equals("true") || blockstatus.equals("false")) {
                        if (storeview.length() > 0) {
                            try {
                                createHashmap.put("block_id", block_ID);
                                createHashmap.put("title", block_title.getText().toString());
                                createHashmap.put("vendor_id", session.getVendorid());
                                createHashmap.put("identifier", block_identifier.getText().toString());
                                createHashmap.put("content", block_content.getText().toString());
                                createHashmap.put("is_active", blockstatus);
                                createHashmap.put("store_id", storeview);
                                Log.w("updatecmsblock", "postdata-- " + createHashmap);
                                JSONObject post = new JSONObject();
                                post.put("cmsBlockData", createHashmap);

                                Ced_ClientRequestResponseRest_New response = new Ced_ClientRequestResponseRest_New(new AsyncResponse() {
                                    @Override
                                    public void processFinish(Object output) {
                                        try {
                                            String string = output.toString();
                                            JSONObject object = new JSONObject(string);
                                            boolean status = object.getBoolean("success");
                                            if (status) {
                                                Toast.makeText(Ced_MultiVendor_UpdateBlock.this, object.getString("message"), Toast.LENGTH_SHORT).show();
                                                Intent in = new Intent(Ced_MultiVendor_UpdateBlock.this, Ced_MultiVendor_BlockListing.class);
                                                startActivity(in);
                                            } else {
                                                Toast.makeText(Ced_MultiVendor_UpdateBlock.this, object.getString("message"), Toast.LENGTH_LONG).show();
                                            }
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }, Ced_MultiVendor_UpdateBlock.this, "POST", post.toString(), false, false);
                                response.execute(updateblock_url);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        } else {
                            Toast.makeText(Ced_MultiVendor_UpdateBlock.this, "Select Store", Toast.LENGTH_SHORT).show();
                        }

                    } else {
                        Toast.makeText(Ced_MultiVendor_UpdateBlock.this, "Select value", Toast.LENGTH_SHORT).show();
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

    private void getStoreViewData() {
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
                                        Log.i("updatecmsblock", "layout_data = " + dropdown_data.getJSONArray(names.getString(n)));
                                        break;
                                    case "store_ids":
                                        store_ids = dropdown_data.getJSONArray(names.getString(n));
                                        break;
                                    case "status":
                                        Log.i("updatecmsblock", "status = " + dropdown_data.getJSONArray(names.getString(n)));
                                        break;
                                    default:
                                        Log.e("updatecmsblock", "extra data = " + dropdown_data.getJSONArray(names.getString(n)));
                                }
                            }
                        HashMap<String, String> hashMap2;
                        for (int i = 0; i < store_ids.length(); i++) {
                            JSONObject stores = store_ids.getJSONObject(i);
                            hashMap2 = new HashMap<>();
                            final LinearLayout mainwebsite = new LinearLayout(Ced_MultiVendor_UpdateBlock.this);
                            mainwebsite.setOrientation(LinearLayout.VERTICAL);
                            if (stores.get("value") instanceof JSONArray) {
                                JSONArray jsonArray = stores.getJSONArray("value");
                                if (jsonArray.isNull(0)) {
                                    final TextView defaultbox = new TextView(Ced_MultiVendor_UpdateBlock.this);
                                    defaultbox.setText(stores.getString("label"));
                                    defaultbox.setTypeface(Typeface.DEFAULT_BOLD);
                                    mainwebsite.addView(defaultbox, 0);
                                } else {
                                    final TextView magetext = new TextView(Ced_MultiVendor_UpdateBlock.this);
                                    magetext.setText(stores.getString("label"));
                                    magetext.setTypeface(Typeface.DEFAULT_BOLD);
                                    mainwebsite.addView(magetext, 0);
                                    for (int s = 0; s < jsonArray.length(); s++) {
                                        JSONObject jsonObject = jsonArray.getJSONObject(s);
                                        final String objlabel = jsonObject.getString("label");
                                        String objvalue = jsonObject.getString("value");
                                        hashMap2.put(objlabel, objvalue);
                                        final LinearLayout checkboxlinear = new LinearLayout(Ced_MultiVendor_UpdateBlock.this);
                                        checkboxlinear.setOrientation(LinearLayout.VERTICAL);
                                        final CheckBox magebox = new CheckBox(Ced_MultiVendor_UpdateBlock.this);
                                        magebox.setButtonDrawable(id);
                                        magebox.setText(objlabel);
                                        if (stringList.contains(objvalue)) {
                                            magebox.setChecked(true);
                                            storeview.put(hashMap2.get(objlabel));
                                        }
                                        final HashMap<String, String> finalHashMap = hashMap2;
                                        magebox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                            @Override
                                            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                                if (isChecked) {
                                                    storeview.put(finalHashMap.get(objlabel));
                                                } else {
                                                    try {
                                                        for (int i = 0; i < storeview.length(); i++) {
                                                            if (Objects.requireNonNull(finalHashMap.get(objlabel)).equalsIgnoreCase(storeview.getString(i))) {
                                                                storeview.remove(i);
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
                                final CheckBox mainwebsitename = new CheckBox(Ced_MultiVendor_UpdateBlock.this);
                                mainwebsitename.setButtonDrawable(id);
                                final String key = stores.getString("label");
                                String value = String.valueOf(stores.getInt("value"));
                                hashMap2.put(key, value);
                                mainwebsitename.setText(stores.getString("label"));
                                if (stringList.contains(value)) {
                                    mainwebsitename.setChecked(true);
                                    storeview.put(hashMap2.get(key));
                                }
                                final HashMap<String, String> finalHashMap1 = hashMap2;
                                mainwebsitename.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                    @Override
                                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                        if (isChecked) {
                                            storeview.put(finalHashMap1.get(key));
                                        } else {

                                            try {
                                                for (int i = 0; i < storeview.length(); i++) {
                                                    if (Objects.requireNonNull(finalHashMap1.get(key)).equalsIgnoreCase(storeview.getString(i))) {
                                                        storeview.remove(i);
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
                    } else {
                        Toast.makeText(Ced_MultiVendor_UpdateBlock.this, object.getString("message"), Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, Ced_MultiVendor_UpdateBlock.this);
        response.execute(storeview_url);
    }

    private void getCmsBlockViewData() {
        try {
            JSONObject postdata = new JSONObject();
            JSONObject underdata = new JSONObject();
            underdata.put("block_id", block_ID);
            underdata.put("vendor_id", session.getVendorid());
            postdata.put("data", underdata);
            Ced_ClientRequestResponseRest_New response = new Ced_ClientRequestResponseRest_New(new AsyncResponse() {
                @Override
                public void processFinish(Object output) {
                    try {
                        String string = output.toString();
                        JSONObject object = new JSONObject(string);
                        String success = object.getString("success");
                        if (success.equals("true")) {
                            JSONArray cms_list = object.getJSONArray("cms_list");
                            JSONObject viewblock = cms_list.getJSONObject(0);
                            block_title.setText(viewblock.getString("title"));
                            block_identifier.setText(viewblock.getString("identifier"));
                            block_content.setText(viewblock.getString("content"));
                            if (viewblock.getString("is_active").equals("true")) {
                                block_status.setSelection(2);
                            } else {
                                block_status.setSelection(1);
                            }
                            JSONArray store_id = viewblock.getJSONArray("store_id");
                            for (int s = 0; s < store_id.length(); s++)
                                stringList.add(store_id.getString(s));
                        } else {
                            String message = object.getString("message");
                            Toast.makeText(Ced_MultiVendor_UpdateBlock.this, message, Toast.LENGTH_LONG).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, Ced_MultiVendor_UpdateBlock.this, "POST", postdata.toString(), false, false);
            response.execute(viewblock_url);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}