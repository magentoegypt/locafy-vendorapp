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

package magentoegypt.locafy.manage_attribute;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.databinding.DataBindingUtil;

import magentoegypt.locafy.base_app.Ced_MultiVendor_VendorFunctionalityList;
import magentoegypt.locafy.databinding.CreateProductAttributePageBinding;
import magentoegypt.locafy.databinding.OptionValueLayoutBinding;
import magentoegypt.locafy.databinding.StorewiseOptionValuesBinding;
import magentoegypt.locafy.navigation_drawer.Activity.Ced_MultiVendor_NavigationActivity;
import magentoegypt.locafy.api_request_response_section.network_error_handling.Ced_MultiVendor_ConnectionDetector;
import magentoegypt.locafy.api_request_response_section.network_error_handling.Ced_MultiVendor_NoInternetconnection;
import magentoegypt.locafy.api_request_response_section.network_error_handling.Ced_MultiVendor_UnAuthourizedRequestError;
import magentoegypt.locafy.api_request_response_section.Ced_MultiVendor_ClientRequestResponse;
import magentoegypt.locafy.vendor_session.Ced_MultiVendor_VendorSessionManagement;
import magentoegypt.locafy.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

/**
 * Created by developer on 3/6/16.
 */

public class Ced_MultiVendor_CreateProductAttribute extends Ced_MultiVendor_NavigationActivity {
    CreateProductAttributePageBinding createProductAttributePageBinding;
    Ced_MultiVendor_ConnectionDetector connectionDetector;
    Ced_MultiVendor_VendorFunctionalityList functionalityList;
    HashMap<String, String> postdata;
    JSONArray  selected_attribute_array, frontend_label;
    JSONObject optionsObject;
    String out, saveAttributeUrl, hashkey, vendor_id, type_prod = "";
    HashMap<String, String> defaultattributehash;
    HashMap<String, String> frontendTypeMap, scopeTypeMap, fronetendClassMap, yesNoType;
    JSONArray attributeArray;
    JSONArray defaultOptions;
    JSONObject req, default_value_of_attribute;
    JSONObject jsonObject;
    String attributeSetUrl = "";
    String attributeOptionsUrl = "";
    HashMap<String, HashMap<String, String>> AddNew_collection;
    private ArrayList<String> yesNoList;
    private boolean multiSelect=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewGroup content = findViewById(R.id.MultiVendor_frame_container);
        createProductAttributePageBinding= DataBindingUtil.inflate(getLayoutInflater(),R.layout.create_product_attribute_page,content,true);
        connectionDetector = new Ced_MultiVendor_ConnectionDetector(getApplicationContext());
        functionalityList = new Ced_MultiVendor_VendorFunctionalityList(getApplicationContext());

        postdata = new HashMap<>();
        defaultattributehash = new HashMap<>();
        frontendTypeMap = new HashMap<>();
        scopeTypeMap = new HashMap<>();
        fronetendClassMap = new HashMap<>();
        yesNoType = new HashMap<>();
        req = new JSONObject();
        defaultOptions = new JSONArray();
        default_value_of_attribute = new JSONObject();
        jsonObject = new JSONObject();
        AddNew_collection = new HashMap<>();
        saveAttributeUrl = session.getBase_Url() + "vproductattributeapi/index/saveAttribute";
        attributeSetUrl = session.getBase_Url() + "vproductattributeapi/set/info";
        attributeOptionsUrl = session.getBase_Url() + "vproductattributeapi/index/getAttributeOptions";

        if (connectionDetector.isConnectingToInternet()) {

            final HashMap<String, String> user = session.getUserDetails();
            hashkey = user.get(Ced_MultiVendor_VendorSessionManagement.Key_HAsh);
            vendor_id = session.getVendorid();
            postdata.put("vendor_id", vendor_id);
            postdata.put("hashkey", hashkey);

            if (session.getvendorname() != null) {
                setname(session.getvendorname());
                setprofilepic(session.getvendorpic());
                changetitle("Vendor Product Attribute");
            }

            getAttributeOptions();
            getAttributeSet();

            createProductAttributePageBinding.defaultDateAttributeValue.setOnClickListener(view -> {
//                selectdate_fromcalender(createProductAttributePageBinding.defaultDateAttributeValue,"dd-MM-yyyy");
                selectdate_fromcalender(createProductAttributePageBinding.defaultDateAttributeValue,"MM/dd/yyyy");
            });

            createProductAttributePageBinding.MultiVendorAddOptions.setOnClickListener(v -> addLayoutonclick());

            createProductAttributePageBinding.MultiVendorSaveAttribute.setOnClickListener(v -> {
                if (Objects.requireNonNull(createProductAttributePageBinding.MultiVendorEdtAttributeCode.getText()).toString().isEmpty() || Objects.requireNonNull(createProductAttributePageBinding.MultiVendorEdtAdmin.getText()).toString().isEmpty()) {
                    Toast.makeText(getApplicationContext(), R.string.emptyAttrvalues, Toast.LENGTH_LONG).show();
                }
                else {
                    try {
                        selected_attribute_array = new JSONArray();
                        for (int i = 0; i < createProductAttributePageBinding.attributeSetLinear.getChildCount(); i++) {
                            LinearLayout linearLayout = (LinearLayout) createProductAttributePageBinding.attributeSetLinear.getChildAt(i);
                            CheckBox checkBox = (CheckBox) linearLayout.getChildAt(0);
                            if (checkBox.isChecked()) {
                                selected_attribute_array.put(checkBox.getTag().toString());
                            }
                        }
                        req.put("attribute_set_ids", selected_attribute_array);

                        optionsObject = new JSONObject();
                        LinearLayoutCompat storewiseOption;
                        EditText storeOptionValue;
                        JSONArray tmpArray;
                        CheckBox defaultCheckbox;
                        for (int i = 0; i < createProductAttributePageBinding.MultiVendorDynamicLayout.getChildCount(); i++) {
                            LinearLayout linearLayout = (LinearLayout) createProductAttributePageBinding.MultiVendorDynamicLayout.getChildAt(i);
                            defaultCheckbox=(CheckBox) linearLayout.getChildAt(1);
                            if (defaultCheckbox.isChecked()){
                                defaultOptions.put(new JSONObject().put(""+i,"option_"+i));
                            }
                            LinearLayoutCompat linearLayout1 = (LinearLayoutCompat) linearLayout.getChildAt(0);
                            LinearLayoutCompat storeOptionValues = (LinearLayoutCompat) linearLayout1.getChildAt(0);
                            tmpArray=new JSONArray();
                            for (int i1=0;i1<storeOptionValues.getChildCount();i1++){
                                storewiseOption = (LinearLayoutCompat) storeOptionValues.getChildAt(i1);
                                storeOptionValue = (EditText) storewiseOption.getChildAt(1);
                                tmpArray.put(new JSONObject().put(storeOptionValue.getTag().toString(),storeOptionValue.getText().toString()));
                            }
                            optionsObject.put("option_" + i, tmpArray);
                        }
                        if (optionsObject.length()>0) {
                            req.put("option", optionsObject);
                        }

                        frontend_label = new JSONArray();
                        frontend_label.put(createProductAttributePageBinding.MultiVendorEdtAdmin.getText().toString());
                        req.put("frontend_label", frontend_label);

                        JSONArray store_frontend_label = new JSONArray();
                        if (createProductAttributePageBinding.storeLabel.getChildCount() > 0) {
                            for (int i = 0; i < createProductAttributePageBinding.storeLabel.getChildCount(); i++) {
                                ConstraintLayout constraintLayout = (ConstraintLayout) createProductAttributePageBinding.storeLabel.getChildAt(i);
                                AppCompatEditText store_label = (AppCompatEditText) constraintLayout.getChildAt(1);
                                JSONObject store = new JSONObject();
                                store.put("key", store_label.getTag().toString());
                                store.put("label", store_label.getText().toString());
                                store_frontend_label.put(store);
                            }
                        }
                        req.put("store_frontend_label", store_frontend_label);

                        req.put("frontend_input", frontendTypeMap.get(createProductAttributePageBinding.MultiVendorCatalogInputType.getSelectedItem().toString()));
                        if (req.getString("frontend_input").equals("text")){
                            req.put("default_value_text", Objects.requireNonNull(createProductAttributePageBinding.defaultTextAttributeValue.getText()).toString());
                        }
                        else if (req.getString("frontend_input").equals("textarea")){
                            req.put("default_value_textarea", createProductAttributePageBinding.defaultTextAreaAttributeValue.getText().toString());
                        }
                        else if (req.getString("frontend_input").equals("date")){
                            req.put("default_value_date", createProductAttributePageBinding.defaultDateAttributeValue.getText().toString());
                        }
                        else if (req.getString("frontend_input").equals("boolean")){
                            req.put("default_value_yesno", yesNoType.get(createProductAttributePageBinding.defaultBooleanAttribute.getSelectedItem().toString()));
                        }
                        else if (req.getString("frontend_input").equals("multiselect")){
                            if (defaultOptions.length()>0) {
                                req.put("default", defaultOptions);
                            }
                        }
                        else if (req.getString("frontend_input").equals("select")){
                            if (defaultOptions.length()>0) {
                                req.put("default", defaultOptions);
                            }
                        }
                        req.put("is_required", yesNoType.get(createProductAttributePageBinding.MultiVendorValuesRequired.getSelectedItem().toString()));
                        req.put("attribute_code", createProductAttributePageBinding.MultiVendorEdtAttributeCode.getText().toString());
                        req.put("sort_order", createProductAttributePageBinding.sortOrderEditText.getText().toString());
                        req.put("is_unique", yesNoType.get(createProductAttributePageBinding.MultiVendorUniqueValue.getSelectedItem().toString()));

                        req.put("is_searchable", yesNoType.get(createProductAttributePageBinding.useInSearchYesNo.getSelectedItem().toString()));
                        req.put("is_used_for_promo_rules", yesNoType.get(createProductAttributePageBinding.useforPromoRule.getSelectedItem().toString()));
                        req.put("used_in_product_listing", yesNoType.get(createProductAttributePageBinding.usedinProductListing.getSelectedItem().toString()));
                        req.put("is_html_allowed_on_front", yesNoType.get(createProductAttributePageBinding.allowHTMLTags.getSelectedItem().toString()));
                        req.put("is_comparable", yesNoType.get(createProductAttributePageBinding.comparableOnStorefront.getSelectedItem().toString()));
                        req.put("is_visible_on_front", yesNoType.get(createProductAttributePageBinding.visibleonCatalogPages.getSelectedItem().toString()));
                        req.put("used_for_sort_by", yesNoType.get(createProductAttributePageBinding.UsedforSorting.getSelectedItem().toString()));


                        req.put("is_global", scopeTypeMap.get(createProductAttributePageBinding.scopeSpinner.getSelectedItem().toString()));

                        if (createProductAttributePageBinding.MultiVendorInputValidation.isEnabled()) {
                            req.put("frontend_class", fronetendClassMap.get(createProductAttributePageBinding.MultiVendorInputValidation.getSelectedItem().toString()));
                        }
                        saveAttributeRequest();
                    }
                    catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
        else {
            Intent nointernet = new Intent(getApplicationContext(), Ced_MultiVendor_NoInternetconnection.class);
            nointernet.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(nointernet);
            overridePendingTransition(R.anim.ced_multivendor_slide_in, R.anim.ced_multivendor_slide_out);
        }
    }

    private void getAttributeSet() {
        Ced_MultiVendor_ClientRequestResponse response = new Ced_MultiVendor_ClientRequestResponse(output -> {
            prod_attr_listdata(output.toString());
        }, Ced_MultiVendor_CreateProductAttribute.this, "POST", postdata);
        response.execute(attributeSetUrl);
    }

    private void getAttributeOptions() {
        Ced_MultiVendor_ClientRequestResponse response = new Ced_MultiVendor_ClientRequestResponse(output -> {
            if (functionalityList.getExtensionAddon()) {
                JSONObject obj = new JSONObject(output.toString());
                JSONObject data = obj.getJSONObject("data");
                JSONObject objects;
                if (data.getBoolean("success")) {
                    List<String> spn_label_list;
                    JSONArray optionArray;

                    if (data.has("scope_type")) {
                        JSONArray scope_type = data.getJSONArray("scope_type");
                        spn_label_list = new ArrayList<>();
                        for (int i = 0; i < scope_type.length(); i++) {
                             objects = scope_type.getJSONObject(i);
                            scopeTypeMap.put(objects.getString("label"), objects.getString("value"));
                            spn_label_list.add(objects.getString("label"));
                        }
                        ArrayAdapter<String> adapter = new ArrayAdapter<>(Ced_MultiVendor_CreateProductAttribute.this,R.layout.simple_spinner_dropdown_item, spn_label_list);
                        createProductAttributePageBinding.scopeSpinner.setAdapter(adapter);
                    }

                    if (data.has("frontend_type")) {
                        optionArray = data.getJSONArray("frontend_type");
                        spn_label_list = new ArrayList<>();
                        for (int i = 0; i < optionArray.length(); i++) {
                            objects = optionArray.getJSONObject(i);
                            frontendTypeMap.put(objects.getString("label"), objects.getString("value"));
                            spn_label_list.add(objects.getString("label"));
                        }
                        ArrayAdapter<String> adapter = new ArrayAdapter<>(Ced_MultiVendor_CreateProductAttribute.this,R.layout.simple_spinner_dropdown_item, spn_label_list);
                        createProductAttributePageBinding.MultiVendorCatalogInputType.setAdapter(adapter);

                        createProductAttributePageBinding.MultiVendorCatalogInputType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                if (frontendTypeMap.get(parent.getSelectedItem().toString()).equals("text")) {
                                    createProductAttributePageBinding.manageOptionsLayout.setVisibility(View.GONE);
                                    createProductAttributePageBinding.defaultTextSection.setVisibility(View.VISIBLE);
                                    createProductAttributePageBinding.defaultBooleanAttributeSection.setVisibility(View.GONE);
                                    createProductAttributePageBinding.defaultTextAttributeValue.setVisibility(View.VISIBLE);
                                    createProductAttributePageBinding.defaultDateAttributeValue.setVisibility(View.GONE);
                                    createProductAttributePageBinding.defaultTextAreaAttributeValue.setVisibility(View.GONE);
                                    createProductAttributePageBinding.MultiVendorInputValidation.setEnabled(true);

                                }
                                else if (frontendTypeMap.get(parent.getSelectedItem().toString()).equals("textarea")) {
                                    createProductAttributePageBinding.manageOptionsLayout.setVisibility(View.GONE);
                                    createProductAttributePageBinding.defaultTextSection.setVisibility(View.VISIBLE);
                                    createProductAttributePageBinding.defaultBooleanAttributeSection.setVisibility(View.GONE);
                                    createProductAttributePageBinding.defaultTextAttributeValue.setVisibility(View.GONE);
                                    createProductAttributePageBinding.defaultTextAreaAttributeValue.setVisibility(View.VISIBLE);
                                    createProductAttributePageBinding.defaultDateAttributeValue.setVisibility(View.GONE);
                                    createProductAttributePageBinding.MultiVendorInputValidation.setEnabled(false);
                                }
                                else if (frontendTypeMap.get(parent.getSelectedItem().toString()).equals("date")) {
                                    createProductAttributePageBinding.manageOptionsLayout.setVisibility(View.GONE);
                                    createProductAttributePageBinding.defaultTextSection.setVisibility(View.VISIBLE);
                                    createProductAttributePageBinding.defaultBooleanAttributeSection.setVisibility(View.GONE);
                                    createProductAttributePageBinding.defaultTextAttributeValue.setVisibility(View.GONE);
                                    createProductAttributePageBinding.defaultTextAreaAttributeValue.setVisibility(View.GONE);
                                    createProductAttributePageBinding.defaultDateAttributeValue.setVisibility(View.VISIBLE);
                                    createProductAttributePageBinding.MultiVendorInputValidation.setEnabled(false);
                                }
                                else if (frontendTypeMap.get(parent.getSelectedItem().toString()).equals("boolean")) {
                                    createProductAttributePageBinding.manageOptionsLayout.setVisibility(View.GONE);
                                    createProductAttributePageBinding.defaultTextSection.setVisibility(View.GONE);
                                    createProductAttributePageBinding.defaultBooleanAttributeSection.setVisibility(View.VISIBLE);
                                    createProductAttributePageBinding.MultiVendorInputValidation.setEnabled(false);
                                }
                                else if (frontendTypeMap.get(parent.getSelectedItem().toString()).equals("multiselect")) {
                                    multiSelect=true;
                                    createProductAttributePageBinding.manageOptionsLayout.setVisibility(View.VISIBLE);
                                    createProductAttributePageBinding.defaultBooleanAttributeSection.setVisibility(View.GONE);
                                    createProductAttributePageBinding.defaultTextSection.setVisibility(View.GONE);
                                    createProductAttributePageBinding.MultiVendorInputValidation.setEnabled(false);
                                }
                                else if (frontendTypeMap.get(parent.getSelectedItem().toString()).equals("select")) {
                                    multiSelect=false;
                                    createProductAttributePageBinding.manageOptionsLayout.setVisibility(View.VISIBLE);
                                    createProductAttributePageBinding.defaultBooleanAttributeSection.setVisibility(View.GONE);
                                    createProductAttributePageBinding.defaultTextSection.setVisibility(View.GONE);
                                    createProductAttributePageBinding.MultiVendorInputValidation.setEnabled(false);
                                }
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });
                    }

                    if (data.has("fronetend_class")) {
                        optionArray = data.getJSONArray("fronetend_class");
                        spn_label_list = new ArrayList<>();
                        for (int i = 0; i < optionArray.length(); i++) {
                            objects = optionArray.getJSONObject(i);
                            fronetendClassMap.put(objects.getString("label"), objects.getString("value"));
                            spn_label_list.add(objects.getString("label"));
                        }
                        ArrayAdapter<String> adapter = new ArrayAdapter<>(Ced_MultiVendor_CreateProductAttribute.this, R.layout.simple_spinner_dropdown_item, spn_label_list);
                        createProductAttributePageBinding.MultiVendorInputValidation.setAdapter(adapter);
                    }

                    if (data.has("store_array")) {
                        JSONArray store_array = data.getJSONArray("store_array");
                        if (store_array.length() > 0) {
                            createProductAttributePageBinding.storeLabel.setVisibility(View.VISIBLE);
                            for (int i = 0; i < store_array.length(); i++) {
                                JSONObject store = store_array.getJSONObject(i);
                                String store_id = store.getString("store_id");
                                if (!store_id.equals("0")) {
                                    String store_name = store.getString("store_name");
                                    View v = View.inflate(Ced_MultiVendor_CreateProductAttribute.this, R.layout.dyn_store_label, null);
                                    AppCompatTextView store_label_head = v.findViewById(R.id.store_label_head);
                                    AppCompatEditText store_label = v.findViewById(R.id.store_label);
                                    store_label_head.setText(store_name);
                                    store_label.setTag(store_id);
                                    createProductAttributePageBinding.storeLabel.addView(v);
                                }
                            }
                        }
                    }

                    if (data.has("yesno_type")) {
                        optionArray = data.getJSONArray("yesno_type");
                        yesNoList = new ArrayList<>();
                        for (int i = 0; i < optionArray.length(); i++) {
                            objects = optionArray.getJSONObject(i);
                            yesNoType.put(objects.getString("label"), objects.getString("value"));
                            yesNoList.add(objects.getString("label"));
                        }
                        createProductAttributePageBinding.MultiVendorUniqueValue.setAdapter(new ArrayAdapter<>(Ced_MultiVendor_CreateProductAttribute.this, R.layout.simple_spinner_dropdown_item, yesNoList));
                        createProductAttributePageBinding.MultiVendorValuesRequired.setAdapter(new ArrayAdapter<>(Ced_MultiVendor_CreateProductAttribute.this, R.layout.simple_spinner_dropdown_item, yesNoList));
                        createProductAttributePageBinding.defaultBooleanAttribute.setAdapter(new ArrayAdapter<>(Ced_MultiVendor_CreateProductAttribute.this, R.layout.simple_spinner_dropdown_item, yesNoList));


                        createProductAttributePageBinding.useInSearchYesNo.setAdapter(new ArrayAdapter<>(Ced_MultiVendor_CreateProductAttribute.this, R.layout.simple_spinner_dropdown_item, yesNoList));
                        createProductAttributePageBinding.useforPromoRule.setAdapter(new ArrayAdapter<>(Ced_MultiVendor_CreateProductAttribute.this, R.layout.simple_spinner_dropdown_item, yesNoList));
                        createProductAttributePageBinding.usedinProductListing.setAdapter(new ArrayAdapter<>(Ced_MultiVendor_CreateProductAttribute.this, R.layout.simple_spinner_dropdown_item, yesNoList));
                        createProductAttributePageBinding.allowHTMLTags.setAdapter(new ArrayAdapter<>(Ced_MultiVendor_CreateProductAttribute.this, R.layout.simple_spinner_dropdown_item, yesNoList));
                        createProductAttributePageBinding.comparableOnStorefront.setAdapter(new ArrayAdapter<>(Ced_MultiVendor_CreateProductAttribute.this, R.layout.simple_spinner_dropdown_item, yesNoList));
                        createProductAttributePageBinding.visibleonCatalogPages.setAdapter(new ArrayAdapter<>(Ced_MultiVendor_CreateProductAttribute.this, R.layout.simple_spinner_dropdown_item, yesNoList));
                        createProductAttributePageBinding.UsedforSorting.setAdapter(new ArrayAdapter<>(Ced_MultiVendor_CreateProductAttribute.this, R.layout.simple_spinner_dropdown_item, yesNoList));

                    }
                }
            }
            else {
                Intent intent = new Intent(Ced_MultiVendor_CreateProductAttribute.this, Ced_MultiVendor_UnAuthourizedRequestError.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                overridePendingTransition(R.anim.ced_multivendor_slide_in, R.anim.ced_multivendor_slide_out);
            }
        }, Ced_MultiVendor_CreateProductAttribute.this);
        response.execute(attributeOptionsUrl);
    }

    private void prod_attr_listdata(String s) throws JSONException {
        jsonObject = new JSONObject(s);
        if (jsonObject.getJSONObject("data").getBoolean("success")) {
            if (jsonObject.getJSONObject("data").has("attribute")) {
                if (jsonObject.getJSONObject("data").getJSONArray("attribute").length() > 0) {
                    createProductAttributePageBinding.attributeSetSection.setVisibility(View.VISIBLE);
                    attributeArray = jsonObject.getJSONObject("data").getJSONArray("attribute");
                    CheckBox attributeCheckbox;
                    View view2;
                    JSONObject tmpObj;
                    for (int i = 0; i < attributeArray.length(); i++) {
                        tmpObj = attributeArray.getJSONObject(i);
                        view2 = View.inflate(this, R.layout.simple_checkbox_layout, null);
                        attributeCheckbox = view2.findViewById(R.id.attributeCheckbox);
                        attributeCheckbox.setText(tmpObj.getString("set_code"));
                        attributeCheckbox.setTag(tmpObj.getString("set_id"));
                        createProductAttributePageBinding.attributeSetLinear.addView(view2);
                    }
                }
                else {
                    createProductAttributePageBinding.attributeSetSection.setVisibility(View.GONE);
                }
            }
        }
    }

    private void addLayoutonclick() {

        OptionValueLayoutBinding  optionValueLayoutBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.option_value_layout, null,false);
        StorewiseOptionValuesBinding storewiseOptionValuesBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.storewise_option_values, null,false);
        storewiseOptionValuesBinding.storeOptionValue.setTag("0");
        optionValueLayoutBinding.storeOptionValues.addView(storewiseOptionValuesBinding.getRoot());

        if (createProductAttributePageBinding.storeLabel.getChildCount()>0) {
            for (int i = 0; i <createProductAttributePageBinding.storeLabel.getChildCount();i++){
                storewiseOptionValuesBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.storewise_option_values, null,false);
                ConstraintLayout constraintLayout=(ConstraintLayout) createProductAttributePageBinding.storeLabel.getChildAt(i);
                AppCompatTextView store_label_head =(AppCompatTextView) constraintLayout.getChildAt(0);
                AppCompatEditText store_label = (AppCompatEditText) constraintLayout.getChildAt(1);;
                if (!store_label.getTag().toString().equals("0")) {
                    storewiseOptionValuesBinding.storeOptionLabel.setText(store_label_head.getText().toString());
                    storewiseOptionValuesBinding.storeOptionValue.setTag(store_label.getTag().toString());
                    optionValueLayoutBinding.storeOptionValues.addView(storewiseOptionValuesBinding.getRoot());
                }
            }
        }

        optionValueLayoutBinding.MultiVendorDeleteRow.setOnClickListener(v -> {
            LinearLayout linearLayout = (LinearLayout) optionValueLayoutBinding.MultiVendorDeleteRow.getParent().getParent();
            if (defaultattributehash.containsKey(Objects.requireNonNull(optionValueLayoutBinding.MultiVendorDefaultChckbox.getText()).toString())) {
                defaultattributehash.clear();
            }
            createProductAttributePageBinding.MultiVendorDynamicLayout.removeView(linearLayout);
        });

        /*optionValueLayoutBinding.MultiVendorDefaultChckbox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                if (defaultattributehash.size() > 0) {
                    optionValueLayoutBinding.MultiVendorDefaultChckbox.setChecked(false);
                    Toast.makeText(Ced_MultiVendor_CreateProductAttribute.this, R.string.default_value_can_be_one, Toast.LENGTH_LONG).show();
                }
                else {
                    if (defaultattributehash.size() == 0) {
                        if (Objects.requireNonNull(optionValueLayoutBinding.MultiVendorDefaultChckbox.getText()).toString().equals(""))
                            Toast.makeText(getApplicationContext(), R.string.Pleasefillvalue, Toast.LENGTH_LONG).show();
                        else {
                            defaultattributehash.put(optionValueLayoutBinding.MultiVendorDefaultChckbox.getText().toString(), "1");
//                                defaultOptions.put(default_value.getText().toString());
                            try {
                                defaultOptions.put(new JSONObject().put("0",optionValueLayoutBinding.MultiVendorDefaultChckbox.getText().toString()));
                            }
                            catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
            else
                defaultattributehash.clear();
        });*/

        createProductAttributePageBinding.MultiVendorDynamicLayout.addView(optionValueLayoutBinding.getRoot());
    }

    private void saveAttributeRequest() {
        postdata.put("attribute_data", req.toString());
//        Log.i("REpo", "saveAttributeRequest_468: "+postdata);

        Ced_MultiVendor_ClientRequestResponse response = new Ced_MultiVendor_ClientRequestResponse(output -> {
            if (functionalityList.getExtensionAddon()) {
                out = output.toString();
                JSONObject object = new JSONObject(out);
                if (object.getJSONObject("data").getBoolean("success")) {
                    Toast.makeText(getApplicationContext(), object.getJSONObject("data").getString("message"), Toast.LENGTH_LONG).show();
                    Intent prod_attr_page = new Intent(Ced_MultiVendor_CreateProductAttribute.this, Ced_MultiVendor_VendorProductAttribute.class);
                    prod_attr_page.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(prod_attr_page);
                    overridePendingTransition(R.anim.ced_multivendor_slide_in, R.anim.ced_multivendor_slide_out);
                }
                else {
                    Toast.makeText(getApplicationContext(), object.getJSONObject("data").getString("message"), Toast.LENGTH_LONG).show();
                }
            }
            else {
                Intent intent = new Intent(getApplicationContext(), Ced_MultiVendor_UnAuthourizedRequestError.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                overridePendingTransition(R.anim.ced_multivendor_slide_in, R.anim.ced_multivendor_slide_out);
            }
        }, Ced_MultiVendor_CreateProductAttribute.this, "POST", postdata);
        response.execute(saveAttributeUrl);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Ced_MultiVendor_ConnectionDetector connectionDetector = new Ced_MultiVendor_ConnectionDetector(getApplicationContext());
        if (connectionDetector.isConnectingToInternet()) {
            setname(session.getvendorname());
            setprofilepic(session.getvendorpic());
            changetitle(getString(R.string.createproductattribute));
        }
        else {
            Intent intent = new Intent(getApplicationContext(), Ced_MultiVendor_NoInternetconnection.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            overridePendingTransition(R.anim.ced_multivendor_slide_in, R.anim.ced_multivendor_slide_out);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
