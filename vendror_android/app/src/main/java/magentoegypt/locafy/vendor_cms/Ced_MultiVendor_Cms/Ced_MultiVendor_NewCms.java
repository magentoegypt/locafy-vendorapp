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
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import magentoegypt.locafy.base_app.Ced_MultiVendor_VendorFunctionalityList;
import magentoegypt.locafy.navigation_drawer.Activity.Ced_MultiVendor_NavigationActivity;
import magentoegypt.locafy.api_request_response_section.network_error_handling.Ced_MultiVendor_ConnectionDetector;
import magentoegypt.locafy.api_request_response_section.network_error_handling.Ced_MultiVendor_NoInternetconnection;
import magentoegypt.locafy.api_request_response_section.Ced_ClientRequestResponseRest_New;
import magentoegypt.locafy.R;
import magentoegypt.locafy_constant.AppConstant;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class Ced_MultiVendor_NewCms extends Ced_MultiVendor_NavigationActivity {

    Ced_MultiVendor_ConnectionDetector connectionDetector;
    Ced_MultiVendor_VendorFunctionalityList functionalityList;
    String createcms_url = "";
    String storeview_url = "";
    JSONObject createHashmap;
    LinearLayout cms_storeview;
    JSONArray storeview;
    int id;
    Spinner cms_sethomepage;
    Spinner cms_status;
    String status = "";
    String is_homepage = "";
    String page_layout = "";
    Spinner cms_designlayout;
    EditText cms_layoutupdate_xml, cms_metadata_keyword, cms_metadata_description, cms_contentheading, cms_contentdes, cms_title, cms_identifier;
    Button cms_savepage;
    CheckBox storeviewbox;
    LinearLayout pageinformationtag, pageinformation_layout, contentinfotag, contentinfo_layout, designinfotag, designinfo_layout, metadatatag, metadata_layout;
    String layout_repo;
    JSONArray layout_data, store_ids;
    private ArrayList<String> layout_list = new ArrayList<>();
    private HashMap<String, String> hashMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        createcms_url = session.getBase_Url() + "rest/all/V1/vcmspage/save";
        storeview_url = session.getBase_Url() + "rest/all/V1/vcms/dropdownoptions";
        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        createHashmap = new JSONObject();
        connectionDetector = new Ced_MultiVendor_ConnectionDetector(getApplicationContext());
        functionalityList = new Ced_MultiVendor_VendorFunctionalityList(getApplicationContext());
        storeview = new JSONArray();
        layout_data = new JSONArray();
        store_ids = new JSONArray();

        if (connectionDetector.isConnectingToInternet()) {
            ViewGroup content = findViewById(R.id.MultiVendor_frame_container);
            getLayoutInflater().inflate(R.layout.ced_multivendor_newcms, content, true);
            if (getIntent().hasExtra("layout_data")) {
                try {
                    layout_list = new ArrayList<>();
                    hashMap = new HashMap<>();
                    HashMap<String, String> pageLayout_data = new HashMap<>();
                    layout_repo = getIntent().getStringExtra("layout_data");
                    JSONArray layout_data = new JSONArray(layout_repo);
                    for (int k = 0; k < layout_data.length(); k++) {
                        JSONObject jsonObject = layout_data.getJSONObject(k);
                        layout_list.add(jsonObject.getString("label"));
                        hashMap.put(jsonObject.getString("label"), jsonObject.getString("key"));
                        pageLayout_data.put(jsonObject.getString("key"), jsonObject.getString("label"));
                    }
                    Log.w("newcmspage", "pageLayout_data= " + pageLayout_data);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            id = Resources.getSystem().getIdentifier("btn_check_holo_light", "drawable", "android");
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
            cms_designlayout.setAdapter(new ArrayAdapter<>(getApplicationContext(), R.layout.simple_list_item_1, layout_list));
//            cms_designlayout.setAdapter(new ArrayAdapter<>(this, R.layout.simple_list_item_1, layout_list));
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

            pageinformationtag = findViewById(R.id.MultiVendor_pageinfotexttag);
            pageinformation_layout = findViewById(R.id.MultiVendor_pageinformation_layout);
            final boolean[] show = {false};
            pageinformationtag.setOnClickListener(v -> {
//                AppConstant.lockButton(v);
                if (show[0]) {
                    pageinformation_layout.setVisibility(View.GONE);
                    show[0] = false;
                } else {
                    pageinformation_layout.setVisibility(View.VISIBLE);
                    show[0] = true;
                }
            });


            contentinfotag = findViewById(R.id.MultiVendor_contentinfotexttag);
            contentinfo_layout = findViewById(R.id.MultiVendor_contentinfotext_layout);


            contentinfotag.setOnClickListener(v -> {
                if (contentinfo_layout.getVisibility()==View.VISIBLE) {
                    contentinfo_layout.setVisibility(View.GONE);
                }
                else {
                    contentinfo_layout.setVisibility(View.VISIBLE);
                }
            });

            designinfotag = findViewById(R.id.MultiVendor_designinfotexttag);
            designinfo_layout = findViewById(R.id.MultiVendor_designinfo_layout);


            designinfotag.setOnClickListener(v -> {
                if (designinfo_layout.getVisibility()==View.VISIBLE) {
                    designinfo_layout.setVisibility(View.GONE);
                }
                else {
                    designinfo_layout.setVisibility(View.VISIBLE);
                }
            });


            metadatatag = findViewById(R.id.MultiVendor_metadatatexttag);
            metadata_layout = findViewById(R.id.MultiVendor_metadata_layout);


            metadatatag.setOnClickListener(v -> {
                if (metadata_layout.getVisibility()==View.VISIBLE) {
                    metadata_layout.setVisibility(View.GONE);
                }
                else {
                    metadata_layout.setVisibility(View.VISIBLE);
                }
            });


            // store view checkbox
            storeviewbox.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (isChecked) {
                    cms_storeview.setVisibility(View.VISIBLE);
                } else {
                    cms_storeview.setVisibility(View.GONE);
                }
            });


            //storeview --show stores
            Ced_ClientRequestResponseRest_New response = new Ced_ClientRequestResponseRest_New(output -> {
                try {
                    Log.d("REpo", "processFinish: "+output.toString());
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
                                        break;
                                    case "store_ids":
                                        store_ids = dropdown_data.getJSONArray(names.getString(n));
                                        break;
                                    case "status":
                                        Log.i("newcmspage", "status= " + dropdown_data.getJSONArray(names.getString(n)));
                                        break;
                                    default:
                                        Log.e("newcmspage", "extra data = " + dropdown_data.getJSONArray(names.getString(n)));
                                }
                            }
                        HashMap<String, String> hashMap2;
                        for (int i = 0; i < store_ids.length(); i++) {
                            JSONObject stores = store_ids.getJSONObject(i);
                            hashMap2 = new HashMap<>();
                            final LinearLayout mainwebsite = new LinearLayout(Ced_MultiVendor_NewCms.this);
                            mainwebsite.setOrientation(LinearLayout.VERTICAL);
                            if (stores.get("value") instanceof JSONArray) {
                                JSONArray jsonArray = stores.getJSONArray("value");
                                if (jsonArray.isNull(0)) {
                                    final TextView defaultbox = new TextView(Ced_MultiVendor_NewCms.this);
                                    defaultbox.setText(stores.getString("label"));
                                    defaultbox.setTypeface(Typeface.DEFAULT_BOLD);
                                    mainwebsite.addView(defaultbox, 0);
                                } else {
                                    final TextView magetext = new TextView(Ced_MultiVendor_NewCms.this);
                                    magetext.setText(stores.getString("label"));
                                    magetext.setTypeface(Typeface.DEFAULT_BOLD);
                                    mainwebsite.addView(magetext, 0);
                                    for (int s = 0; s < jsonArray.length(); s++) {
                                        JSONObject jsonObject = jsonArray.getJSONObject(s);
                                        final String objlabel = jsonObject.getString("label");
                                        String objvalue = jsonObject.getString("value");
                                        hashMap2.put(objlabel, objvalue);
                                        final LinearLayout checkboxlinear = new LinearLayout(Ced_MultiVendor_NewCms.this);
                                        checkboxlinear.setOrientation(LinearLayout.VERTICAL);
                                        final CheckBox magebox = new CheckBox(Ced_MultiVendor_NewCms.this);
                                        magebox.setButtonDrawable(id);
                                        magebox.setText(objlabel);
                                        final HashMap<String, String> finalHashMap = hashMap2;
                                        magebox.setOnCheckedChangeListener((buttonView, isChecked) -> {
                                            if (isChecked) {
                                                storeview.put(finalHashMap.get(objlabel));
                                            } else {
                                                try {
                                                    for (int i12 = 0; i12 < storeview.length(); i12++) {
                                                        if (Objects.requireNonNull(finalHashMap.get(objlabel)).equalsIgnoreCase(storeview.getString(i12))) {
                                                            storeview.remove(i12);
                                                        }
                                                    }
                                                } catch (Exception e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        });

                                        checkboxlinear.addView(magebox);
                                        mainwebsite.addView(checkboxlinear);
                                    }
                                }
                            } else {
                                final CheckBox mainwebsitename = new CheckBox(Ced_MultiVendor_NewCms.this);
                                mainwebsitename.setButtonDrawable(id);
                                final String key = stores.getString("label");
                                String value = stores.getString("value");
                                hashMap2.put(key, value);
                                mainwebsitename.setText(stores.getString("label"));
                                final HashMap<String, String> finalHashMap1 = hashMap2;
                                mainwebsitename.setOnCheckedChangeListener((buttonView, isChecked) -> {
                                    if (isChecked) {
                                        storeview.put(finalHashMap1.get(key));
                                    } else {
                                        try {
                                            for (int i1 = 0; i1 < storeview.length(); i1++) {
                                                if (Objects.requireNonNull(finalHashMap1.get(key)).equalsIgnoreCase(storeview.getString(i1))) {
                                                    storeview.remove(i1);
                                                }
                                            }
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
                                });
                                mainwebsite.addView(mainwebsitename, 0);
                            }
                            cms_storeview.addView(mainwebsite);
                        }
                    } else {
                        Toast.makeText(Ced_MultiVendor_NewCms.this, object.getString("message"), Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }, Ced_MultiVendor_NewCms.this);
            response.execute(storeview_url);

            cms_status.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    if (adapterView.getSelectedItem().toString().equals("Enabled"))
                        status = "true";
                    else {
                        if (adapterView.getSelectedItem().toString().equals("Disabled"))
                            status = "false";
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });

            cms_sethomepage.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    if (adapterView.getSelectedItem().toString().equals("No"))
                        is_homepage = "false";
                    else {
                        if (adapterView.getSelectedItem().toString().equals("Yes"))
                            is_homepage = "true";
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

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

            cms_savepage.setOnClickListener(view -> {
                AppConstant.lockButton(view);
                if (!page_layout.equals(getString(R.string.select_txt))) {
                    if (is_homepage.equals("true") || is_homepage.equals("false") && status.equals("true") || status.equals("false")) {
                        try {
                            createHashmap.put("title", cms_title.getText().toString());
                            createHashmap.put("page_layout", page_layout);
                            createHashmap.put("vendor_id", session.getVendorid());
                            if (!cms_metadata_keyword.getText().toString().isEmpty())
                                createHashmap.put("meta_keywords", cms_metadata_keyword.getText().toString());
                            if (!cms_metadata_description.getText().toString().isEmpty())
                                createHashmap.put("meta_description", cms_metadata_description.getText().toString());
                            createHashmap.put("identifier", cms_identifier.getText().toString());
                            if (!cms_contentheading.getText().toString().isEmpty())
                                createHashmap.put("content_heading", cms_contentheading.getText().toString());
                            if (!cms_contentdes.getText().toString().isEmpty())
                                createHashmap.put("content", cms_contentdes.getText().toString());
                            createHashmap.put("is_active", status);
                            createHashmap.put("is_home", is_homepage);
                            createHashmap.put("layout_update_xml", cms_layoutupdate_xml.getText().toString());
                            createHashmap.put("store_id", storeview);
                            Log.i("newcmspage", "postdata-- " + createHashmap.toString());
                            JSONObject post = new JSONObject();
                            post.put("cmsPageData", createHashmap);
                            Ced_ClientRequestResponseRest_New response1 = new Ced_ClientRequestResponseRest_New(output -> {
                                try {
                                    String string = output.toString();
                                    JSONObject object = new JSONObject(string);
                                    String success = object.getString("success");
                                    if (success.equals("true")) {
                                        Toast.makeText(Ced_MultiVendor_NewCms.this, object.getString("message"), Toast.LENGTH_SHORT).show();
                                        Intent in = new Intent(Ced_MultiVendor_NewCms.this, Ced_MultiVendor_CmsListing.class);
                                        in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        in.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(in);
                                    } else {
                                        String message = object.getString("message");
                                        Toast.makeText(Ced_MultiVendor_NewCms.this, message, Toast.LENGTH_LONG).show();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }, Ced_MultiVendor_NewCms.this, "POST", post.toString(), false, false);
                            response1.execute(createcms_url);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        Toast.makeText(Ced_MultiVendor_NewCms.this, "Select status & homepage", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(Ced_MultiVendor_NewCms.this, "Select page layout", Toast.LENGTH_SHORT).show();
                }
            });
        }
        else {
            Intent nointernet = new Intent(getApplicationContext(), Ced_MultiVendor_NoInternetconnection.class);
            nointernet.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            nointernet.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(nointernet);
            overridePendingTransition(R.anim.ced_multivendor_slide_in, R.anim.ced_multivendor_slide_out);
        }
    }
}