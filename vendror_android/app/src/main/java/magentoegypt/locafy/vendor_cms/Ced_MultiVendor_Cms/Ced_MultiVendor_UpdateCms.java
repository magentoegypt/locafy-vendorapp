package magentoegypt.locafy.vendor_cms.Ced_MultiVendor_Cms;

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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatSpinner;
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

public class Ced_MultiVendor_UpdateCms extends Ced_MultiVendor_NavigationActivity {

    Ced_MultiVendor_ConnectionDetector connectionDetector;
    Ced_MultiVendor_VendorFunctionalityList functionalityList;
    String updatecms_url = "";
    String viewcms_url = "";
    String cmspage_ID = "";
    JSONObject createHashmap;
    EditText cms_title;
    EditText cms_identifier;
    AppCompatTextView heading;
    LinearLayout cms_storeview;
    AppCompatSpinner cms_sethomepage;
    AppCompatSpinner cms_status;
    EditText cms_contentheading;
    EditText cms_contentdes;
    AppCompatSpinner cms_designlayout;
    private int selectedLayoutPosition=0;
    EditText cms_layoutupdate_xml;
    EditText cms_metadata_keyword;
    EditText cms_metadata_description;
    Button cms_savepage;
    int id;
    String page_layout = "";
    String is_home = "";
    String cmsstatus = "";
    CheckBox storeviewbox;
    String storeview_url = "";
    JSONArray storeview;
    LinearLayout pageinformationtag;
    LinearLayout pageinformation_layout;
    LinearLayout contentinfotag;
    LinearLayout contentinfo_layout;
    LinearLayout designinfotag;
    LinearLayout designinfo_layout;
    LinearLayout metadatatag;
    LinearLayout metadata_layout;
    List<String> stringList;
    JSONArray layout_data, store_ids;
    private ArrayList<String> layout_list = new ArrayList<>();
    private HashMap<String, String> hashMap, pageLayout_data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewcms_url = session.getBase_Url() + "rest/all/V1/vcmspage/view";
        updatecms_url = session.getBase_Url() + "rest/all/V1/vcmspage/update";
        storeview_url = session.getBase_Url() + "rest/all/V1/vcms/dropdownoptions";
        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        createHashmap = new JSONObject();
        storeview = new JSONArray();
        layout_data = new JSONArray();
        store_ids = new JSONArray();
        stringList = new ArrayList<>();
        connectionDetector = new Ced_MultiVendor_ConnectionDetector(getApplicationContext());
        functionalityList = new Ced_MultiVendor_VendorFunctionalityList(getApplicationContext());

        if (connectionDetector.isConnectingToInternet()) {
            ViewGroup content = findViewById(R.id.MultiVendor_frame_container);
            getLayoutInflater().inflate(R.layout.ced_multivendor_newcms, content, true);
            cmspage_ID = getIntent().getStringExtra("page_id");
            getCmsViewData();
            id = Resources.getSystem().getIdentifier("btn_check_holo_light", "drawable", "android");
            heading = findViewById(R.id.heading);
            heading.setText(getString(R.string.updatecmsPage));
            cms_title = findViewById(R.id.MultiVendor_cms_title);
            cms_identifier = findViewById(R.id.MultiVendor_cms_identifier);
            cms_storeview = findViewById(R.id.MultiVendor_cms_storeview_layout);
            storeviewbox = findViewById(R.id.MultiVendor_cms_storeviewtext);
            storeviewbox.setButtonDrawable(id);
            cms_sethomepage = findViewById(R.id.MultiVendor_cms_setashpage);
            cms_status = findViewById(R.id.MultiVendor_cms_status);
            cms_contentheading = findViewById(R.id.MultiVendor_cms_contentheading);
            cms_contentdes = findViewById(R.id.MultiVendor_cms_contentdes);
            cms_designlayout = findViewById(R.id.MultiVendor_cms_layout);
            cms_layoutupdate_xml = findViewById(R.id.MultiVendor_cms_layoutdesign);
            cms_metadata_keyword = findViewById(R.id.MultiVendor_cms_keyword);
            cms_metadata_description = findViewById(R.id.MultiVendor_cms_description);
            cms_savepage = findViewById(R.id.MultiVendor_cms_savepage);
            cms_savepage.setText(getString(R.string.update_txt));
            pageinformationtag = findViewById(R.id.MultiVendor_pageinfotexttag);
            pageinformation_layout = findViewById(R.id.MultiVendor_pageinformation_layout);
            contentinfotag = findViewById(R.id.MultiVendor_contentinfotexttag);
            contentinfo_layout = findViewById(R.id.MultiVendor_contentinfotext_layout);
            designinfotag = findViewById(R.id.MultiVendor_designinfotexttag);
            designinfo_layout = findViewById(R.id.MultiVendor_designinfo_layout);
            metadatatag = findViewById(R.id.MultiVendor_metadatatexttag);
            metadata_layout = findViewById(R.id.MultiVendor_metadata_layout);
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
            final boolean[] show = {false};
            pageinformationtag.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AppConstant.lockButton(v);
                    if (show[0]) {
                        pageinformation_layout.setVisibility(View.GONE);
                        show[0] = false;
                    } else {
                        pageinformation_layout.setVisibility(View.VISIBLE);
                        show[0] = true;
                    }
                }
            });

            final boolean[] show1 = {false};
            contentinfotag.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AppConstant.lockButton(v);
                    if (show1[0]) {
                        contentinfo_layout.setVisibility(View.GONE);
                        show1[0] = false;
                    } else {
                        contentinfo_layout.setVisibility(View.VISIBLE);
                        show1[0] = true;
                    }
                }
            });

            final boolean[] show2 = {false};
            designinfotag.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AppConstant.lockButton(v);
                    if (show2[0]) {
                        designinfo_layout.setVisibility(View.GONE);
                        show2[0] = false;
                    } else {
                        designinfo_layout.setVisibility(View.VISIBLE);
                        show2[0] = true;
                    }
                }
            });


            final boolean[] show3 = {false};
            metadatatag.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AppConstant.lockButton(v);
                    if (show3[0]) {
                        metadata_layout.setVisibility(View.GONE);
                        show3[0] = false;
                    } else {
                        metadata_layout.setVisibility(View.VISIBLE);
                        show3[0] = true;
                    }
                }
            });

            storeviewbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        cms_storeview.setVisibility(View.VISIBLE);
                    } else {
                        cms_storeview.setVisibility(View.GONE);
                    }
                }
            });
            cms_designlayout.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    page_layout = hashMap.get(adapterView.getSelectedItem().toString());
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });
            cms_sethomepage.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    if (adapterView.getSelectedItem().toString().equals("No")) {
                        is_home = "false";
                    } else {
                        if (adapterView.getSelectedItem().toString().equals("Yes")) {
                            is_home = "true";
                        }
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });
            cms_status.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    if (adapterView.getSelectedItem().toString().equals("Enabled")) {
                        cmsstatus = "true";
                    } else {
                        if (adapterView.getSelectedItem().toString().equals("Disabled")) {
                            cmsstatus = "false";
                        }
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });

            cms_savepage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AppConstant.lockButton(view);
                    if (!page_layout.equals(getString(R.string.select_txt))) {
                        if (is_home.equals("false") || is_home.equals("true") && cmsstatus.equals("false") || cmsstatus.equals("true")) {
                            try {
                                createHashmap.put("page_id", cmspage_ID);
                                createHashmap.put("title", cms_title.getText().toString());
                                createHashmap.put("page_layout", page_layout);
                                createHashmap.put("vendor_id", session.getVendorid());
                                createHashmap.put("meta_keywords", cms_metadata_keyword.getText().toString());
                                createHashmap.put("meta_description", cms_metadata_description.getText().toString());
                                createHashmap.put("identifier", cms_identifier.getText().toString());
                                createHashmap.put("content_heading", cms_contentheading.getText().toString());
                                createHashmap.put("content", cms_contentdes.getText().toString());
                                createHashmap.put("is_active", cmsstatus);
                                createHashmap.put("is_home", is_home);
                                createHashmap.put("layout_update_xml", cms_layoutupdate_xml.getText().toString());
                                createHashmap.put("store_id", storeview);
                                Log.w("updatecmspage", "postdata-- " + createHashmap);
                                JSONObject post = new JSONObject();
                                post.put("cmsPageData", createHashmap);
                                Ced_ClientRequestResponseRest_New response = new Ced_ClientRequestResponseRest_New(new AsyncResponse() {
                                    @Override
                                    public void processFinish(Object output) {
                                        try {
                                            String string = output.toString();
                                            JSONObject object = new JSONObject(string);
                                            boolean status = object.getBoolean("success");
                                            if (status) {
                                                Toast.makeText(Ced_MultiVendor_UpdateCms.this, object.getString("message"), Toast.LENGTH_SHORT).show();
                                                Intent in = new Intent(Ced_MultiVendor_UpdateCms.this, Ced_MultiVendor_CmsListing.class);
                                                startActivity(in);
                                            } else {
                                                Toast.makeText(Ced_MultiVendor_UpdateCms.this, object.getString("message"), Toast.LENGTH_LONG).show();
                                            }
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }, Ced_MultiVendor_UpdateCms.this, "POST", post.toString(), false, false);
                                response.execute(updatecms_url);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    } else {
                        Toast.makeText(Ced_MultiVendor_UpdateCms.this, "Select value", Toast.LENGTH_SHORT).show();
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

    private void getCmsViewData() {
        try {
            JSONObject postdata = new JSONObject();
            JSONObject underdata = new JSONObject();
            underdata.put("page_id", cmspage_ID);
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
                            JSONObject viewcms = cms_list.getJSONObject(0);
                            cms_title.setText(viewcms.getString("title"));
                            cms_identifier.setText(viewcms.getString("identifier"));
                            JSONArray store_id = viewcms.getJSONArray("store_id");
                            String isactive_homepage = viewcms.getString("is_home");
                            String isactive = viewcms.getString("is_active");
                            cms_contentdes.setText(viewcms.getString("content"));
                            cms_contentheading.setText(viewcms.getString("content_heading"));
                            String saved_page_layout = viewcms.getString("page_layout");
                            Log.e("updatecmspage", "saved_page_layout= " + saved_page_layout);
                            cms_layoutupdate_xml.setText(viewcms.getString("layout_update_xml"));
                            cms_metadata_keyword.setText(viewcms.getString("meta_keywords"));
                            cms_metadata_description.setText(viewcms.getString("meta_description"));
                            if (isactive.equals("true")) {
                                cms_status.setSelection(2);
                            } else {
                                cms_status.setSelection(1);
                            }
                            if (isactive_homepage.equals("true"))
                                cms_sethomepage.setSelection(2);
                            else
                                cms_sethomepage.setSelection(1);
                            if (viewcms.getString("is_home").equals("false"))
                                cms_sethomepage.setSelection(1);
                            else
                                cms_sethomepage.setSelection(2);
                            for (int s = 0; s < store_id.length(); s++)
                                stringList.add(store_id.getString(s));
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
                                                            layout_data = dropdown_data.getJSONArray(names.getString(n));
                                                            try {
                                                                layout_list = new ArrayList<>();
                                                                hashMap = new HashMap<>();
                                                                pageLayout_data = new HashMap<>();
                                                                for (int k = 0; k < layout_data.length(); k++) {
                                                                    JSONObject jsonObject = layout_data.getJSONObject(k);
                                                                    layout_list.add(jsonObject.getString("label"));
                                                                    hashMap.put(jsonObject.getString("label"), jsonObject.getString("key"));
                                                                    pageLayout_data.put(jsonObject.getString("key"), jsonObject.getString("label"));
                                                                    if (jsonObject.getString("key").equals(saved_page_layout)){
                                                                        selectedLayoutPosition=k;
                                                                    }
                                                                }
                                                                Log.w("updatecmspage","pageLayout_data= "+pageLayout_data);
                                                                cms_designlayout.setAdapter(new ArrayAdapter<>(getApplicationContext(), R.layout.simple_list_item_1, layout_list));
                                                                cms_designlayout.setSelection(selectedLayoutPosition);
                                                            } catch (Exception e) {
                                                                e.printStackTrace();
                                                            }
                                                            break;
                                                        case "store_ids":
                                                            store_ids = dropdown_data.getJSONArray(names.getString(n));
                                                            break;
                                                        case "status":
                                                            Log.i("updatecmspage", "status = " + dropdown_data.getJSONArray(names.getString(n)));
                                                            break;
                                                        default:
                                                            Log.e("updatecmspage", "extra data = " + dropdown_data.getJSONArray(names.getString(n)));
                                                    }
                                                }
                                            HashMap<String, String> hashMap2;
                                            for (int i = 0; i < store_ids.length(); i++) {
                                                JSONObject stores = store_ids.getJSONObject(i);
                                                hashMap2 = new HashMap<>();
                                                final LinearLayout mainwebsite = new LinearLayout(Ced_MultiVendor_UpdateCms.this);
                                                mainwebsite.setOrientation(LinearLayout.VERTICAL);
                                                if (stores.get("value") instanceof JSONArray) {
                                                    JSONArray jsonArray = stores.getJSONArray("value");
                                                    if (jsonArray.isNull(0)) {
                                                        final TextView defaultbox = new TextView(Ced_MultiVendor_UpdateCms.this);
                                                        defaultbox.setText(stores.getString("label"));
                                                        defaultbox.setTypeface(Typeface.DEFAULT_BOLD);
                                                        mainwebsite.addView(defaultbox, 0);
                                                    } else {
                                                        final TextView magetext = new TextView(Ced_MultiVendor_UpdateCms.this);
                                                        magetext.setText(stores.getString("label"));
                                                        magetext.setTypeface(Typeface.DEFAULT_BOLD);
                                                        mainwebsite.addView(magetext, 0);
                                                        for (int s = 0; s < jsonArray.length(); s++) {
                                                            JSONObject jsonObject = jsonArray.getJSONObject(s);
                                                            final String objlabel = jsonObject.getString("label");
                                                            String objvalue = jsonObject.getString("value");
                                                            hashMap2.put(objlabel, objvalue);
                                                            final LinearLayout checkboxlinear = new LinearLayout(Ced_MultiVendor_UpdateCms.this);
                                                            checkboxlinear.setOrientation(LinearLayout.VERTICAL);
                                                            final CheckBox magebox = new CheckBox(Ced_MultiVendor_UpdateCms.this);
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
                                                    final CheckBox mainwebsitename = new CheckBox(Ced_MultiVendor_UpdateCms.this);
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
                                                cms_storeview.addView(mainwebsite);
                                            }
                                        } else {
                                            Toast.makeText(Ced_MultiVendor_UpdateCms.this, object.getString("message"), Toast.LENGTH_LONG).show();
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }, Ced_MultiVendor_UpdateCms.this);
                            response.execute(storeview_url);
                        } else {
                            String message = object.getString("message");
                            Toast.makeText(Ced_MultiVendor_UpdateCms.this, message, Toast.LENGTH_LONG).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, Ced_MultiVendor_UpdateCms.this, "POST", postdata.toString(), false, false);
            response.execute(viewcms_url);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}