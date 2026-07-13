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

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.constraintlayout.widget.ConstraintLayout;

import magentoegypt.locafy.R;
import magentoegypt.locafy.api_request_response_section.Ced_MultiVendor_ClientRequestResponse;
import magentoegypt.locafy.api_request_response_section.network_error_handling.Ced_MultiVendor_ConnectionDetector;
import magentoegypt.locafy.api_request_response_section.network_error_handling.Ced_MultiVendor_NoInternetconnection;
import magentoegypt.locafy.api_request_response_section.network_error_handling.Ced_MultiVendor_UnAuthourizedRequestError;
import magentoegypt.locafy.base_app.Ced_MultiVendor_FontSetting;
import magentoegypt.locafy.base_app.Ced_MultiVendor_VendorFunctionalityList;
import magentoegypt.locafy.navigation_drawer.Activity.Ced_MultiVendor_NavigationActivity;
import magentoegypt.locafy.vendor_session.Ced_MultiVendor_VendorSessionManagement;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

public class Ced_MultiVendor_EditProductAttribute_old extends Ced_MultiVendor_NavigationActivity {
    private Ced_MultiVendor_ConnectionDetector connectionDetector;
    private Ced_MultiVendor_VendorSessionManagement vendorSessionManagement;
    private Ced_MultiVendor_VendorFunctionalityList functionalityList;
    private Ced_MultiVendor_FontSetting fontSetting;
    private HashMap<String, String> frontend_type_list, scope_type_list, fronetend_class_list, yesno_type_list;
    private HashMap<String, String> postdata;
    private String attribute_code, url, vendor_id, hashkey, out, message, attribute_options_url, saveurl, type_prod = "";
    private JSONObject jsonObject;
    private JSONArray  option_data, attribute_set_ids;
    private JSONObject attribute_data;
    private String attribute_id, attributes_code, is_global, frontend_input,
            default_value, is_unique, is_required, apply_to, is_searchable, is_filterable,
            is_comparable, is_visible_on_front, is_html_allowed_on_front,
            is_used_for_price_rules, is_filterable_in_search, used_in_product_listing,
            used_for_sort_by, is_configurable, is_visible_in_advanced_search,
            position, is_wysiwyg_enabled, is_used_for_promo_rules,
            search_weight, include_in_attribute_set, frontend_label, checked, intype,
            option_name, option_id, sort_order, delete_value, is_used_in_grid, is_filterable_in_grid, value, label, selected, default_store_label;

    private TextView Atributecode_head, Scope_head, CatalogInputType_head, DefaultValue_head, UniqueValue_head,
            ValuesRequired_head, InputValidation_head, ApplyTo_head, QuickSearch_head,
            AdvancedSearch_head, Front_end_head, LayeredNavigation_head,
            SearchResultsLayeredNavigation_head, PromoRule_head, Position_head, HTML_tag_head,
            AttributeSet_head, ProductviewPage_head,
            ProductListing_head, Sorting_ProductListing_head,
            WYSIWYG_head, Note_manage_label, head_Admin, UsetoCreateConfig, delete_prod_attr;

    private EditText edt_AttributeCode, edt_DefaultValueDate, edt_DefaultValue, Position, edt_Admin, MultiVendor_edt_Default_Store_View;
    private Spinner Scope, CatalogInputType, default_spinner, UniqueValue, ValuesRequired,
            InputValidation, ApplyTo, QuickSearch, AdvancedSearch, Front_end,
            LayeredNavigation, SearchResultsLayeredNavigation, PromoRule,
            HTML_tag, AttributeSet, ProductviewPage,
            Productlisting, Sorting_Productlisting, WYSIWYG, isConfigurable, MultiVendor_Use_to_filter, MultiVendor_Add_to_column;
    private LinearLayout front_end_section_linear, scope_linear, Default_linear, Unique_linear,
            ValuesRequired_linear, InputValidation_linear, ApplyTo_linear, QuickSearch_linear,
            AdvancedSearch_linear, Front_end_linear, LayeredNavigation_linear, store_label_section,
            SearchResultsLayeredNavigation_linear, PromoRule_linear,
            Position_linear, HTML_tag_linear, AttributeSet_linear, ProductviewPage_linear, Productlisting_linear, Sorting_Productlisting_linear,
            WYSIWYG_linear, dynamic_layout, apply_check_linear,
            Manage_Labels_linear, Add_button_linear, isConfigurable_linear;
    private Button FrontendProperties, Manage_Labels, AddOptions, SaveAttribute;
    private CheckBox Simple_Prod, Grouped_Prod, Config_Prod, Virtual_Prod, Bundle_Prod, Downloadable_Prod;

    private HashMap<String, String> defaultattributehash;
    private HashMap<String, ArrayList> AddNew_collection;
    private JSONObject req, default_value_of_attribute;
    private JSONArray def_values, selected_attribute_array, delete_sub, frontend_labels;
    private JSONObject array;
    private Calendar newCalendar;
    private JSONObject jsonObjects;
    private LinearLayout dynamic_attributes;
    private HashMap<String, HashMap<String, String>> AddNew_collection2;
    private SimpleDateFormat dateFormatter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        connectionDetector = new Ced_MultiVendor_ConnectionDetector(getApplicationContext());
        vendorSessionManagement = new Ced_MultiVendor_VendorSessionManagement(getApplicationContext());
        functionalityList = new Ced_MultiVendor_VendorFunctionalityList(getApplicationContext());
        fontSetting = new Ced_MultiVendor_FontSetting();
        postdata = new HashMap<>();
        defaultattributehash = new HashMap<>();
        def_values = new JSONArray();
        default_value_of_attribute = new JSONObject();
        jsonObjects = new JSONObject();
        req = new JSONObject();
        frontend_type_list = new HashMap<>();
        scope_type_list = new HashMap<>();
        fronetend_class_list = new HashMap<>();
        yesno_type_list = new HashMap<>();
        delete_sub = new JSONArray();
        frontend_labels = new JSONArray();
        AddNew_collection2 = new HashMap<>();
        url = session.getBase_Url() + "vproductattributeapi/index/getVProductAttributeData/";
        saveurl = session.getBase_Url() + "vproductattributeapi/index/saveAttribute";
        attribute_options_url = session.getBase_Url() + "vproductattributeapi/index/getAttributeOptions";

        if (connectionDetector.isConnectingToInternet()) {
            ViewGroup content = findViewById(R.id.MultiVendor_frame_container);
            getLayoutInflater().inflate(R.layout.ced_multivendor_create_product_attribute_page, content, true);
            Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.backbutton);
            getAttributeOptions();
            if (vendorSessionManagement.getvendorname() != null) {
                setname(vendorSessionManagement.getvendorname());
                setprofilepic(vendorSessionManagement.getvendorpic());
                changetitle("Edit Product Attribute");
            }
            attribute_code = getIntent().getStringExtra("attribute_code");
            final HashMap<String, String> user = vendorSessionManagement.getUserDetails();
            hashkey = user.get(Ced_MultiVendor_VendorSessionManagement.Key_HAsh);
            vendor_id = vendorSessionManagement.getVendorid();
            postdata.put("vendor_id", vendor_id);
            postdata.put("hashkey", hashkey);
            postdata.put("attribute_id", attribute_code);
            request();

            store_label_section = findViewById(R.id.store_label);
            Atributecode_head = findViewById(R.id.MultiVendor_Atributecode_head);
            Scope_head = findViewById(R.id.MultiVendor_Scope_head);
            CatalogInputType_head = findViewById(R.id.MultiVendor_CatalogInputType_head);
            DefaultValue_head = findViewById(R.id.MultiVendor_DefaultValue_head);
            UniqueValue_head = findViewById(R.id.MultiVendor_UniqueValue_head);
            ValuesRequired_head = findViewById(R.id.MultiVendor_ValuesRequired_head);
            InputValidation_head = findViewById(R.id.MultiVendor_InputValidation_head);
            ApplyTo_head = findViewById(R.id.MultiVendor_ApplyTo_head);
            QuickSearch_head = findViewById(R.id.MultiVendor_QuickSearch_head);
            AdvancedSearch_head = findViewById(R.id.MultiVendor_AdvancedSearch_head);
            Front_end_head = findViewById(R.id.MultiVendor_Front_end_head);
            LayeredNavigation_head = findViewById(R.id.MultiVendor_LayeredNavigation_head);
            SearchResultsLayeredNavigation_head = findViewById(R.id.MultiVendor_SearchResultsLayeredNavigation_head);
            PromoRule_head = findViewById(R.id.MultiVendor_PromoRule_head);
            Position_head = findViewById(R.id.MultiVendor_Position_head);
            HTML_tag_head = findViewById(R.id.MultiVendor_HTML_tag_head);
            AttributeSet_head = findViewById(R.id.MultiVendor_AttributeSet_head);
            ProductviewPage_head = findViewById(R.id.MultiVendor_ProductviewPage_head);
            ProductListing_head = findViewById(R.id.MultiVendor_ProductListing_head);
            Sorting_ProductListing_head = findViewById(R.id.MultiVendor_Sorting_ProductListing_head);
            WYSIWYG_head = findViewById(R.id.MultiVendor_WYSIWYG_head);
            Note_manage_label = findViewById(R.id.MultiVendor_Note_manage_label);
            head_Admin = findViewById(R.id.MultiVendor_head_Admin);
            UsetoCreateConfig = findViewById(R.id.MultiVendor_UsetoCreateConfig);

            fontSetting.setFontforTextviews(Atributecode_head, "Roboto-Medium.ttf", Ced_MultiVendor_EditProductAttribute_old.this);
            fontSetting.setFontforTextviews(Scope_head, "Roboto-Medium.ttf", Ced_MultiVendor_EditProductAttribute_old.this);
            fontSetting.setFontforTextviews(CatalogInputType_head, "Roboto-Medium.ttf", Ced_MultiVendor_EditProductAttribute_old.this);
            fontSetting.setFontforTextviews(DefaultValue_head, "Roboto-Medium.ttf", Ced_MultiVendor_EditProductAttribute_old.this);
            fontSetting.setFontforTextviews(UniqueValue_head, "Roboto-Medium.ttf", Ced_MultiVendor_EditProductAttribute_old.this);
            fontSetting.setFontforTextviews(ValuesRequired_head, "Roboto-Medium.ttf", Ced_MultiVendor_EditProductAttribute_old.this);
            fontSetting.setFontforTextviews(InputValidation_head, "Roboto-Medium.ttf", Ced_MultiVendor_EditProductAttribute_old.this);
            fontSetting.setFontforTextviews(ApplyTo_head, "Roboto-Medium.ttf", Ced_MultiVendor_EditProductAttribute_old.this);
            fontSetting.setFontforTextviews(QuickSearch_head, "Roboto-Medium.ttf", Ced_MultiVendor_EditProductAttribute_old.this);
            fontSetting.setFontforTextviews(AdvancedSearch_head, "Roboto-Medium.ttf", Ced_MultiVendor_EditProductAttribute_old.this);
            fontSetting.setFontforTextviews(Front_end_head, "Roboto-Medium.ttf", Ced_MultiVendor_EditProductAttribute_old.this);
            fontSetting.setFontforTextviews(LayeredNavigation_head, "Roboto-Medium.ttf", Ced_MultiVendor_EditProductAttribute_old.this);
            fontSetting.setFontforTextviews(SearchResultsLayeredNavigation_head, "Roboto-Medium.ttf", Ced_MultiVendor_EditProductAttribute_old.this);
            fontSetting.setFontforTextviews(Position_head, "Roboto-Medium.ttf", Ced_MultiVendor_EditProductAttribute_old.this);
            fontSetting.setFontforTextviews(HTML_tag_head, "Roboto-Medium.ttf", Ced_MultiVendor_EditProductAttribute_old.this);
            fontSetting.setFontforTextviews(AttributeSet_head, "Roboto-Medium.ttf", Ced_MultiVendor_EditProductAttribute_old.this);
            fontSetting.setFontforTextviews(ProductviewPage_head, "Roboto-Medium.ttf", Ced_MultiVendor_EditProductAttribute_old.this);
            fontSetting.setFontforTextviews(ProductListing_head, "Roboto-Medium.ttf", Ced_MultiVendor_EditProductAttribute_old.this);
            fontSetting.setFontforTextviews(Sorting_ProductListing_head, "Roboto-Medium.ttf", Ced_MultiVendor_EditProductAttribute_old.this);
            fontSetting.setFontforTextviews(WYSIWYG_head, "Roboto-Medium.ttf", Ced_MultiVendor_EditProductAttribute_old.this);
            fontSetting.setFontforTextviews(Note_manage_label, "Roboto-Medium.ttf", Ced_MultiVendor_EditProductAttribute_old.this);
            fontSetting.setFontforTextviews(PromoRule_head, "Roboto-Medium.ttf", Ced_MultiVendor_EditProductAttribute_old.this);


            edt_AttributeCode = findViewById(R.id.MultiVendor_edt_AttributeCode);
            edt_DefaultValue = findViewById(R.id.MultiVendor_edt_DefaultValue);
            edt_DefaultValueDate = findViewById(R.id.MultiVendor_edt_DefaultValueDate);
            Position = findViewById(R.id.MultiVendor_Position);
            edt_Admin = findViewById(R.id.MultiVendor_edt_Admin);
            MultiVendor_edt_Default_Store_View = findViewById(R.id.MultiVendor_edt_Default_Store_View);

            Scope = findViewById(R.id.MultiVendor_Scope);
            CatalogInputType = findViewById(R.id.MultiVendor_CatalogInputType);
            default_spinner = findViewById(R.id.MultiVendor_default_spinner);
            UniqueValue = findViewById(R.id.MultiVendor_UniqueValue);
            ValuesRequired = findViewById(R.id.MultiVendor_ValuesRequired);
            InputValidation = findViewById(R.id.MultiVendor_InputValidation);
            ApplyTo = findViewById(R.id.MultiVendor_ApplyTo);
            QuickSearch = findViewById(R.id.MultiVendor_QuickSearch);
            AdvancedSearch = findViewById(R.id.MultiVendor_AdvancedSearch);
            Front_end = findViewById(R.id.MultiVendor_Front_end);
            LayeredNavigation = findViewById(R.id.MultiVendor_LayeredNavigation);
            SearchResultsLayeredNavigation = findViewById(R.id.MultiVendor_SearchResultsLayeredNavigation);
            PromoRule = findViewById(R.id.MultiVendor_PromoRule);
            HTML_tag = findViewById(R.id.MultiVendor_HTML_tag);
            AttributeSet = findViewById(R.id.MultiVendor_AttributeSet);
            ProductviewPage = findViewById(R.id.MultiVendor_ProductviewPage);
            Productlisting = findViewById(R.id.MultiVendor_Productlisting);
            Sorting_Productlisting = findViewById(R.id.MultiVendor_Sorting_Productlisting);
            WYSIWYG = findViewById(R.id.MultiVendor_WYSIWYG);
            isConfigurable = findViewById(R.id.MultiVendor_isConfigurable);
            MultiVendor_Add_to_column = findViewById(R.id.MultiVendor_Add_to_column);
            MultiVendor_Use_to_filter = findViewById(R.id.MultiVendor_Use_to_filter);

            front_end_section_linear = findViewById(R.id.MultiVendor_front_end_section_linear);
            scope_linear = findViewById(R.id.MultiVendor_scope_linear);
            Default_linear = findViewById(R.id.MultiVendor_Default_linear);
            Unique_linear = findViewById(R.id.MultiVendor_Unique_linear);
            ValuesRequired_linear = findViewById(R.id.MultiVendor_ValuesRequired_linear);
            InputValidation_linear = findViewById(R.id.MultiVendor_InputValidation_linear);
            ApplyTo_linear = findViewById(R.id.MultiVendor_ApplyTo_linear);
            QuickSearch_linear = findViewById(R.id.MultiVendor_QuickSearch_linear);
            AdvancedSearch_linear = findViewById(R.id.MultiVendor_AdvancedSearch_linear);
            Front_end_linear = findViewById(R.id.MultiVendor_Front_end_linear);
            LayeredNavigation_linear = findViewById(R.id.MultiVendor_LayeredNavigation_linear);
            SearchResultsLayeredNavigation_linear = findViewById(R.id.MultiVendor_SearchResultsLayeredNavigation_linear);
            PromoRule_linear = findViewById(R.id.MultiVendor_PromoRule_linear);
            Position_linear = findViewById(R.id.MultiVendor_Position_linear);
            HTML_tag_linear = findViewById(R.id.MultiVendor_HTML_tag_linear);
            AttributeSet_linear = findViewById(R.id.MultiVendor_AttributeSet_linear);
            ProductviewPage_linear = findViewById(R.id.MultiVendor_ProductviewPage_linear);
            Productlisting_linear = findViewById(R.id.MultiVendor_Productlisting_linear);
            Sorting_Productlisting_linear = findViewById(R.id.MultiVendor_Sorting_Productlisting_linear);
            WYSIWYG_linear = findViewById(R.id.MultiVendor_WYSIWYG_linear);
            dynamic_layout = findViewById(R.id.MultiVendor_dynamic_layout);
            apply_check_linear = findViewById(R.id.MultiVendor_apply_check_linear);
            Manage_Labels_linear = findViewById(R.id.MultiVendor_Manage_Labels_linear);
            Add_button_linear = findViewById(R.id.MultiVendor_Add_button_linear);
            isConfigurable_linear = findViewById(R.id.MultiVendor_isConfigurable_linear);
            dynamic_attributes = findViewById(R.id.dynamic_attributes);

            FrontendProperties = findViewById(R.id.MultiVendor_FrontendProperties);
            Manage_Labels = findViewById(R.id.MultiVendor_Manage_Labels);
            AddOptions = findViewById(R.id.MultiVendor_AddOptions);
            SaveAttribute = findViewById(R.id.MultiVendor_SaveAttribute);

            Simple_Prod = findViewById(R.id.MultiVendor_Simple_Prod);
            Grouped_Prod = findViewById(R.id.MultiVendor_Grouped_Prod);
            Config_Prod = findViewById(R.id.MultiVendor_Config_Prod);
            Virtual_Prod = findViewById(R.id.MultiVendor_Virtual_Prod);
            Bundle_Prod = findViewById(R.id.MultiVendor_Bundle_Prod);
            Downloadable_Prod = findViewById(R.id.MultiVendor_Downloadable_Prod);
            fontSetting.setfontforButtons(FrontendProperties, "Roboto-Medium.ttf", Ced_MultiVendor_EditProductAttribute_old.this);
            fontSetting.setfontforButtons(Manage_Labels, "Roboto-Medium.ttf", Ced_MultiVendor_EditProductAttribute_old.this);
        }
        else {
            Intent nointernet = new Intent(getApplicationContext(), Ced_MultiVendor_NoInternetconnection.class);
            nointernet.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            nointernet.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(nointernet);
            overridePendingTransition(R.anim.ced_multivendor_slide_in, R.anim.ced_multivendor_slide_out);
        }

    }

    private void request() {
        Ced_MultiVendor_ClientRequestResponse response = new Ced_MultiVendor_ClientRequestResponse(output -> {
            try {
                if (functionalityList.getExtensionAddon()) {
                    out = output.toString();
                    if (out.equals("NO_ORDER")) {
                        Toast.makeText(getApplicationContext(), R.string.NoOrdersToList, Toast.LENGTH_LONG).show();
                    }
                    else {
                        edt_prod_attribute();
                    }
                }
                else {
                    Intent intent = new Intent(getApplicationContext(), Ced_MultiVendor_UnAuthourizedRequestError.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    overridePendingTransition(R.anim.ced_multivendor_slide_in, R.anim.ced_multivendor_slide_out);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, Ced_MultiVendor_EditProductAttribute_old.this, "POST", postdata);
        response.execute(url);

    }

    private void edt_prod_attribute() {
        try {
            jsonObject = new JSONObject(out);
            if (jsonObject.getJSONObject("data").getBoolean("success")) {
                attribute_data = jsonObject.getJSONObject("data").getJSONObject("attribute_data");
                ArrayList<HashMap<String, String>> add_button_data = new ArrayList<>();
                AddNew_collection = new HashMap<>();
                if (attribute_data.length() > 0) {

                    default_value = attributes_code = attribute_id = is_global = frontend_input = sort_order = frontend_label = is_required = is_unique =is_filterable = "";
                    is_searchable = is_visible_in_advanced_search = is_comparable =is_used_for_promo_rules = is_html_allowed_on_front = is_visible_on_front =used_in_product_listing =used_for_sort_by =is_used_for_price_rules ="";
                    is_filterable_in_search = "";
                    is_configurable = "";
                    is_wysiwyg_enabled = "";
                    include_in_attribute_set = "";
                    is_used_in_grid = "";
                    is_filterable_in_grid = "";
                    apply_to = "";
                    position = "";
                    search_weight ="";
                    default_store_label = "";

//                    for (int i = 0; i < attribute_data.length(); i++) {
//                        JSONObject c = attribute_data.getJSONObject(i);

                    JSONObject attribute_properties = attribute_data.getJSONObject("attribute_properties");
                    if (attribute_properties.has("attribute_set_ids")) {
                    attribute_set_ids = attribute_properties.getJSONArray("attribute_set_ids");
                    if (attribute_set_ids.length() > 0) {
                        for (int a = 0; a < attribute_set_ids.length(); a++) {
                            JSONObject dynamic_attr = attribute_set_ids.getJSONObject(a);
                            value = dynamic_attr.getString("value");
                            label = dynamic_attr.getString("label");
                            selected = dynamic_attr.getString("selected");
                            HashMap map = new HashMap();
                            map.put("value", value);
                            map.put("label", label);
                            map.put("selected", selected);
                            AddNew_collection2.put(label + "#" + value + "#" + selected, map);
                        }
                    }
                    for (Map.Entry<String, HashMap<String, String>> stringHashMapEntry : AddNew_collection2.entrySet()) {
                        View view2 = View.inflate(this, R.layout.ced_multivendor_manage_attribute_set_dynamic_layout, null);
                        TextView MultiVendor_text_attribute_set = view2.findViewById(R.id.MultiVendor_text_attribute_set);
                        CheckBox MultiVendor_checkbox_attribute_set = view2.findViewById(R.id.MultiVendor_checkbox_attribute_set);
                        Map.Entry entry = stringHashMapEntry;
                        String key = String.valueOf(entry.getKey());
                        String[] parts = key.split("#");
                        MultiVendor_text_attribute_set.setText(parts[1]);
                        MultiVendor_checkbox_attribute_set.setText(parts[0]);
                        if (parts[2].equals("true")) {
                            MultiVendor_checkbox_attribute_set.setChecked(true);
                        }
                        dynamic_attributes.addView(view2);
                    }
                    }

                    default_value = attribute_properties.getString("default_value");
                    attributes_code = attribute_properties.getString("attribute_code");
                    attribute_id = attribute_properties.getString("attribute_id");
                    is_global = attribute_properties.getString("is_global");
                    frontend_input = attribute_properties.getString("frontend_input");
                    sort_order = attribute_properties.getString("sort_order");
                    frontend_label = attribute_properties.getString("frontend_label");
                    is_required = attribute_properties.getString("is_required");
                    is_unique = attribute_properties.getString("is_unique");

                    JSONObject storefront_properties=attribute_data.getJSONObject("storefront_properties");
                    is_searchable = storefront_properties.getString("is_searchable");
                    is_visible_in_advanced_search = storefront_properties.getString("is_visible_in_advanced_search");
                    is_comparable = storefront_properties.getString("is_comparable");
                    is_used_for_promo_rules = storefront_properties.getString("is_used_for_promo_rules");
                    is_html_allowed_on_front = storefront_properties.getString("is_html_allowed_on_front");
                    is_visible_on_front = storefront_properties.getString("is_visible_on_front");
                    used_in_product_listing = storefront_properties.getString("used_in_product_listing");
                    used_for_sort_by = storefront_properties.getString("used_for_sort_by");

/*

                    is_filterable = c.getString("is_filterable");
                    is_used_for_price_rules = c.getString("is_used_for_price_rules");
                    is_filterable_in_search = c.getString("is_filterable_in_search");
                    is_configurable = c.getString("is_configurable");
                    is_wysiwyg_enabled = c.getString("is_wysiwyg_enabled");
                    include_in_attribute_set = c.getString("include_in_attribute_set");
                    is_used_in_grid = c.getString("is_used_in_grid");
                    is_filterable_in_grid = c.getString("is_filterable_in_grid");
                    apply_to = c.getString("apply_to");
                    position = c.getString("position");
                    search_weight = c.getString("search_weight");
                    default_store_label = c.getString("default_store_label");
                    if (c.has("store_labels")) {
                        JSONObject store_labels = c.getJSONObject("store_labels");
                        if (store_labels.length() > 0) {
                            JSONArray store_ids = store_labels.names();
                            for (int w = 0; w < Objects.requireNonNull(store_ids).length(); w++)
                                for (int s = 0; s < store_label_section.getChildCount(); s++) {
                                    ConstraintLayout constraintLayout = (ConstraintLayout) store_label_section.getChildAt(s);
                                    AppCompatEditText store_label = (AppCompatEditText) constraintLayout.getChildAt(1);
                                    if (store_label.getTag().toString().equals(store_ids.getString(w))) {
                                        store_label.setText(store_labels.getString(store_ids.getString(w)));
                                    }
                                }
                        }
                    }
*/

                    MultiVendor_edt_Default_Store_View.setText(default_store_label);

                    if (jsonObject.getJSONObject("data").has("option_data")) {
                        option_data = jsonObject.getJSONObject("data").getJSONArray("option_data");
                        if (option_data.length() > 0) {
                            for (int j = 0; j < option_data.length(); j++) {
                                JSONObject dynamic_options = option_data.getJSONObject(j);
                                checked = dynamic_options.getString("checked");
                                intype = dynamic_options.getString("intype");
                                option_name = dynamic_options.getString("option_name");
                                option_id = dynamic_options.getString("id");
                                HashMap dynamic_hash = new HashMap();
                                dynamic_hash.put("checked", checked);
                                dynamic_hash.put("option_name", option_name);
                                dynamic_hash.put("option_id", option_id);
                                add_button_data.add(dynamic_hash);
                                AddNew_collection.put(checked + "#" + option_name + "#" + option_id, add_button_data);
                            }
                            if (getResources().getString(R.string.Enable_Log).equals("yes")) {
                                Log.i("add_new_data", "" + AddNew_collection);
                            }
                        }

                        for (Map.Entry<String, ArrayList> stringArrayListEntry : AddNew_collection.entrySet()) {
                            View view2 = View.inflate(this, R.layout.ced_multivendor_manage_label_dynamic_layout, null);
                            final CheckBox default_chckbox = view2.findViewById(R.id.MultiVendor_default_chckbox);
                            final Button delete_row = view2.findViewById(R.id.MultiVendor_delete_row);
                            final EditText default_value = view2.findViewById(R.id.MultiVendor_default_value);
                            final TextView heading = view2.findViewById(R.id.MultiVendor_heading);
                            final TextView option_id = view2.findViewById(R.id.MultiVendor_option_id);
                            Map.Entry entry = stringArrayListEntry;
                            String key = String.valueOf(entry.getKey());
                            String[] parts = key.split("#");
                            default_value.setText(parts[1]);
                            option_id.setText(parts[2]);
                            delete_row.setOnClickListener(v -> {
                                LinearLayout linearLayout = (LinearLayout) delete_row.getParent();
                                LinearLayout linearLayout1 = (LinearLayout) linearLayout.getParent();
                                if (defaultattributehash.containsKey(default_value.getText().toString())) {
                                    defaultattributehash.clear();
                                }
                                dynamic_layout.removeView(linearLayout1);
                                delete_prod_attr = (TextView) linearLayout1.getChildAt(2);

                                delete_value = delete_prod_attr.getText().toString();
                                delete_sub.put(delete_value);
                            });
                            if (parts[0].equalsIgnoreCase("checked")) {
                                default_chckbox.setChecked(true);
                                defaultattributehash.put(default_value.getText().toString(), "1");
                            }
                            default_chckbox.setOnCheckedChangeListener((buttonView, isChecked) -> {
                                if (isChecked) {
                                    if (defaultattributehash.size() > 0) {
                                        default_chckbox.setChecked(false);
                                        Toast.makeText(Ced_MultiVendor_EditProductAttribute_old.this, R.string.default_value_can_be_one, Toast.LENGTH_LONG).show();
                                    } else {
                                        if (defaultattributehash.size() == 0) {
                                            if (default_value.getText().toString().equals("")) {
                                                Toast.makeText(getApplicationContext(), R.string.Pleasefillvalue, Toast.LENGTH_LONG).show();
                                            } else {
                                                defaultattributehash.put(default_value.getText().toString(), "1");
                                                try {
                                                    default_value_of_attribute.put("deafult", default_value.getText().toString());
                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        }
                                    }
                                } else {
                                    defaultattributehash.clear();
                                }
                            });
                            dynamic_layout.addView(view2);
                        }
                    }

//                    }
                }
                edt_AttributeCode.setText(attributes_code);
                edt_Admin.setText(frontend_label);
                edt_AttributeCode.setClickable(false);
                edt_AttributeCode.setKeyListener(null);

              /*  if (is_global.equals("Store View")) {
                    Scope.setSelection(1);
                } else if (is_global.equals("Website")) {
                    Scope.setSelection(2);
                } else {
                    Scope.setSelection(3);
                }*/

                if (frontend_input.equalsIgnoreCase("text")) {
                    CatalogInputType.setSelection(0);

                    InputValidation_linear.setVisibility(View.VISIBLE);
                    edt_DefaultValueDate.setVisibility(View.GONE);
                    edt_DefaultValue.setVisibility(View.VISIBLE);
                    default_spinner.setVisibility(View.GONE);
                    LayeredNavigation_linear.setVisibility(View.GONE);
                    SearchResultsLayeredNavigation_linear.setVisibility(View.GONE);
                    Position_linear.setVisibility(View.GONE);
                    AddOptions.setVisibility(View.GONE);

                    isConfigurable_linear.setVisibility(View.GONE);
                    HTML_tag_linear.setVisibility(View.VISIBLE);
                    Default_linear.setVisibility(View.VISIBLE);
                    scope_linear.setVisibility(View.VISIBLE);
                    front_end_section_linear.setVisibility(View.VISIBLE);
                    Unique_linear.setVisibility(View.VISIBLE);
                    ValuesRequired_linear.setVisibility(View.VISIBLE);
                }
                else if (frontend_input.equalsIgnoreCase("textarea")) {
                    CatalogInputType.setSelection(1);
                    edt_DefaultValue.setVisibility(View.VISIBLE);
                    edt_DefaultValueDate.setVisibility(View.GONE);
                    default_spinner.setVisibility(View.GONE);
                    InputValidation_linear.setVisibility(View.GONE);
                    LayeredNavigation_linear.setVisibility(View.GONE);
                    SearchResultsLayeredNavigation_linear.setVisibility(View.GONE);
                    Position_linear.setVisibility(View.GONE);
                    Sorting_Productlisting_linear.setVisibility(View.GONE);
                    AddOptions.setVisibility(View.GONE);

                    isConfigurable_linear.setVisibility(View.GONE);
                    HTML_tag_linear.setVisibility(View.VISIBLE);
                    WYSIWYG_linear.setVisibility(View.VISIBLE);
                    Default_linear.setVisibility(View.VISIBLE);
                    scope_linear.setVisibility(View.VISIBLE);
                    front_end_section_linear.setVisibility(View.VISIBLE);
                    Unique_linear.setVisibility(View.VISIBLE);
                    ValuesRequired_linear.setVisibility(View.VISIBLE);
                }
                else if (frontend_input.equalsIgnoreCase("date")) {
                    CatalogInputType.setSelection(2);
                    edt_DefaultValueDate.setVisibility(View.VISIBLE);
                    edt_DefaultValue.setVisibility(View.GONE);
                    default_spinner.setVisibility(View.GONE);
                    InputValidation_linear.setVisibility(View.GONE);
                    LayeredNavigation_linear.setVisibility(View.GONE);
                    SearchResultsLayeredNavigation_linear.setVisibility(View.GONE);
                    Position_linear.setVisibility(View.GONE);
                    HTML_tag_linear.setVisibility(View.GONE);
                    WYSIWYG_linear.setVisibility(View.GONE);
                    AddOptions.setVisibility(View.GONE);

                    isConfigurable_linear.setVisibility(View.GONE);
                    Default_linear.setVisibility(View.VISIBLE);
                    scope_linear.setVisibility(View.VISIBLE);
                    front_end_section_linear.setVisibility(View.VISIBLE);
                    Unique_linear.setVisibility(View.VISIBLE);
                    ValuesRequired_linear.setVisibility(View.VISIBLE);
                    Sorting_Productlisting_linear.setVisibility(View.VISIBLE);
                    edt_DefaultValueDate.setFocusable(false);
                    edt_DefaultValueDate.setClickable(true);
                    edt_DefaultValueDate.setOnClickListener(v -> {
                        newCalendar = Calendar.getInstance();
                        dateFormatter = new SimpleDateFormat("MM/dd/yyyy", Locale.US);
                        new DatePickerDialog(Ced_MultiVendor_EditProductAttribute_old.this, (view, year, monthOfYear, dayOfMonth) -> {
                            newCalendar.set(Calendar.YEAR, year);
                            newCalendar.set(Calendar.MONTH, monthOfYear);
                            newCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                            edt_DefaultValue.setText(dateFormatter.format(newCalendar.getTime()));
                            edt_DefaultValueDate.setText(dateFormatter.format(newCalendar.getTime()));
                        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH)).show();
                    });
                }
                else if (frontend_input.equalsIgnoreCase("boolean")) {
                    CatalogInputType.setSelection(3);
                    edt_DefaultValue.setVisibility(View.GONE);
                    edt_DefaultValueDate.setVisibility(View.GONE);
                    default_spinner.setVisibility(View.VISIBLE);
                    InputValidation_linear.setVisibility(View.GONE);
                    LayeredNavigation_linear.setVisibility(View.GONE);
                    SearchResultsLayeredNavigation_linear.setVisibility(View.GONE);
                    Position_linear.setVisibility(View.GONE);
                    HTML_tag_linear.setVisibility(View.GONE);
                    WYSIWYG_linear.setVisibility(View.GONE);
                    AddOptions.setVisibility(View.GONE);

                    isConfigurable_linear.setVisibility(View.GONE);
                    Default_linear.setVisibility(View.VISIBLE);
                    scope_linear.setVisibility(View.VISIBLE);
                    front_end_section_linear.setVisibility(View.VISIBLE);
                    Unique_linear.setVisibility(View.VISIBLE);
                    ValuesRequired_linear.setVisibility(View.VISIBLE);
                    Sorting_Productlisting_linear.setVisibility(View.VISIBLE);
                    if (default_value.equalsIgnoreCase("yes")) {
                        default_spinner.setSelection(1);
                    } else if (default_value.equalsIgnoreCase("no")) {
                        default_spinner.setSelection(2);
                    }
                }
                else if (frontend_input.equalsIgnoreCase("multiselect")) {
                    CatalogInputType.setSelection(4);
                    InputValidation_linear.setVisibility(View.GONE);
                    Default_linear.setVisibility(View.GONE);
                    Sorting_Productlisting_linear.setVisibility(View.GONE);
                    WYSIWYG_linear.setVisibility(View.GONE);
                    AddOptions.setVisibility(View.VISIBLE);
                    edt_DefaultValueDate.setVisibility(View.GONE);

                    isConfigurable_linear.setVisibility(View.GONE);
                    scope_linear.setVisibility(View.VISIBLE);
                    HTML_tag_linear.setVisibility(View.VISIBLE);
                    front_end_section_linear.setVisibility(View.VISIBLE);
                    Unique_linear.setVisibility(View.VISIBLE);
                    ValuesRequired_linear.setVisibility(View.VISIBLE);

                    LayeredNavigation_linear.setVisibility(View.VISIBLE);
                    SearchResultsLayeredNavigation_linear.setVisibility(View.VISIBLE);
                    LayeredNavigation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            if (parent.getSelectedItem().equals("No")) {
                                Position_linear.setVisibility(View.GONE);
                            } else {
                                Position_linear.setVisibility(View.VISIBLE);
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                }
                else if (frontend_input.equals("select")) {
                    CatalogInputType.setSelection(5);
                    AddOptions.setVisibility(View.VISIBLE);
                    Manage_Labels_linear.setVisibility(View.VISIBLE);
                    InputValidation_linear.setVisibility(View.GONE);
                    Default_linear.setVisibility(View.GONE);
                    WYSIWYG_linear.setVisibility(View.GONE);
                    edt_DefaultValueDate.setVisibility(View.GONE);
                    LayeredNavigation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            if (parent.getSelectedItem().equals("No"))
                                Position_linear.setVisibility(View.GONE);
                            else
                                Position_linear.setVisibility(View.VISIBLE);
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                    Scope.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                            if (Scope.getSelectedItem().toString().equals("Global")) {
                                isConfigurable_linear.setVisibility(View.VISIBLE);
                                if (is_configurable.equals("Yes"))
                                    isConfigurable.setSelection(1);
                                else if (is_configurable.equals("No"))
                                    isConfigurable.setSelection(0);
                            }
                            else {
                                isConfigurable_linear.setVisibility(View.GONE);
                            }

                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                    scope_linear.setVisibility(View.VISIBLE);
                    HTML_tag_linear.setVisibility(View.VISIBLE);
                    front_end_section_linear.setVisibility(View.VISIBLE);
                    Unique_linear.setVisibility(View.VISIBLE);
                    ValuesRequired_linear.setVisibility(View.VISIBLE);
                    Sorting_Productlisting_linear.setVisibility(View.VISIBLE);
                    LayeredNavigation_linear.setVisibility(View.VISIBLE);
                    SearchResultsLayeredNavigation_linear.setVisibility(View.VISIBLE);
                }
                else if (frontend_input.equalsIgnoreCase("price")) {
                    CatalogInputType.setSelection(6);
                    isConfigurable_linear.setVisibility(View.GONE);
                    scope_linear.setVisibility(View.GONE);
                    InputValidation_linear.setVisibility(View.GONE);
                    HTML_tag_linear.setVisibility(View.GONE);
                    WYSIWYG_linear.setVisibility(View.GONE);

                    AddOptions.setVisibility(View.GONE);
                    LayeredNavigation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            if (parent.getSelectedItem().equals("No"))
                                Position_linear.setVisibility(View.GONE);
                            else
                                Position_linear.setVisibility(View.VISIBLE);
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                    edt_DefaultValue.setVisibility(View.VISIBLE);
                    edt_DefaultValueDate.setVisibility(View.GONE);
                    default_spinner.setVisibility(View.GONE);
                    front_end_section_linear.setVisibility(View.VISIBLE);
                    Default_linear.setVisibility(View.VISIBLE);
                    Unique_linear.setVisibility(View.VISIBLE);
                    ValuesRequired_linear.setVisibility(View.VISIBLE);
                    Sorting_Productlisting_linear.setVisibility(View.VISIBLE);
                    LayeredNavigation_linear.setVisibility(View.VISIBLE);
                    SearchResultsLayeredNavigation_linear.setVisibility(View.VISIBLE);
                }
                else if (frontend_input.equalsIgnoreCase("media_image")) {
                    CatalogInputType.setSelection(7);
                    front_end_section_linear.setVisibility(View.GONE);
                    Default_linear.setVisibility(View.GONE);
                    Unique_linear.setVisibility(View.GONE);
                    AddOptions.setVisibility(View.GONE);
                    isConfigurable_linear.setVisibility(View.GONE);
                    edt_DefaultValueDate.setVisibility(View.GONE);
                    ValuesRequired_linear.setVisibility(View.GONE);
                    InputValidation_linear.setVisibility(View.GONE);
                    WYSIWYG_linear.setVisibility(View.GONE);
                }

                CatalogInputType.setEnabled(false);
                CatalogInputType.setClickable(false);
                edt_DefaultValue.setText(default_value);
                edt_DefaultValueDate.setText(default_value);

             /*   if (is_unique.equals("Yes"))
                    UniqueValue.setSelection(0);
                else
                    UniqueValue.setSelection(1);

                if (is_required.equals("Yes"))
                    ValuesRequired.setSelection(0);
                else
                    ValuesRequired.setSelection(1);

                if (is_searchable.equals("Yes"))
                    QuickSearch.setSelection(0);
                else
                    QuickSearch.setSelection(1);

                if (is_visible_in_advanced_search.equals("Yes"))
                    AdvancedSearch.setSelection(0);
                else
                    AdvancedSearch.setSelection(1);

                if (is_comparable.equals("Yes"))
                    Front_end.setSelection(0);
                else
                    Front_end.setSelection(1);

                if (is_used_for_promo_rules.equals("Yes"))
                    PromoRule.setSelection(0);
                else
                    PromoRule.setSelection(1);

                if (is_filterable.equals("Filterable (with results)"))
                    LayeredNavigation.setSelection(0);
                else if (is_filterable.equals("Filterable (no results)"))
                    LayeredNavigation.setSelection(1);
                else
                    LayeredNavigation.setSelection(2);

                if (is_filterable_in_search.equals("Yes"))
                    SearchResultsLayeredNavigation.setSelection(0);
                else
                    SearchResultsLayeredNavigation.setSelection(1);

                Position.setText(position);

                if (is_html_allowed_on_front.equals("Yes"))
                    HTML_tag.setSelection(0);
                else
                    HTML_tag.setSelection(1);

                if (include_in_attribute_set.equals("Yes"))
                    AttributeSet.setSelection(0);
                else
                    AttributeSet.setSelection(1);

                if (is_visible_on_front.equals("Yes"))
                    ProductviewPage.setSelection(0);
                else
                    ProductviewPage.setSelection(1);

                if (used_in_product_listing.equals("Yes"))
                    Productlisting.setSelection(0);
                else
                    Productlisting.setSelection(1);

                if (used_for_sort_by.equals("Yes"))
                    Sorting_Productlisting.setSelection(0);
                else
                    Sorting_Productlisting.setSelection(1);

                if (is_wysiwyg_enabled.equals("Yes"))
                    WYSIWYG.setSelection(0);
                else
                    WYSIWYG.setSelection(1);

                if (is_used_in_grid.equalsIgnoreCase("Yes"))
                    MultiVendor_Add_to_column.setSelection(0);
                else
                    MultiVendor_Add_to_column.setSelection(1);

                if (is_filterable_in_grid.equalsIgnoreCase("Yes"))
                    MultiVendor_Use_to_filter.setSelection(0);
                else
                    MultiVendor_Use_to_filter.setSelection(1);
*/
                ApplyTo.setSelection(1);
                String[] x = apply_to.split(",");
                for (int i = 0; i < x.length; i++) {
                    if (x[i].equalsIgnoreCase("simple")) {
                        Simple_Prod.setChecked(true);
                        type_prod += Simple_Prod.getText();
                        jsonObjects.put("Simple", Simple_Prod.getText().toString());
                    } else if (x[i].equalsIgnoreCase("configurable")) {
                        Config_Prod.setChecked(true);
                        jsonObjects.put("Configurable", Config_Prod.getText().toString());
                        if (type_prod.isEmpty()) {
                            type_prod += Config_Prod.getText();
                        } else {
                            type_prod += "," + Config_Prod.getText();
                        }
                    } else if (x[i].equalsIgnoreCase("virtual")) {
                        Virtual_Prod.setChecked(true);
                        jsonObjects.put("Virtual", Virtual_Prod.getText().toString());
                        if (type_prod.isEmpty()) {
                            type_prod += Virtual_Prod.getText();
                        } else {
                            type_prod += "," + Virtual_Prod.getText();
                        }
                    } else if (x[i].equalsIgnoreCase("bundle")) {
                        Bundle_Prod.setChecked(true);
                        jsonObjects.put("Bundle", Bundle_Prod.getText().toString());
                        if (type_prod.isEmpty()) {
                            type_prod += Bundle_Prod.getText();
                        } else {
                            type_prod += "," + Bundle_Prod.getText();
                        }
                    } else if (x[i].equalsIgnoreCase("grouped")) {
                        Grouped_Prod.setChecked(true);
                        jsonObjects.put("Grouped", Grouped_Prod.getText().toString());
                        if (type_prod.isEmpty()) {
                            type_prod += Grouped_Prod.getText();
                        } else {
                            type_prod += "," + Grouped_Prod.getText();
                        }
                    } else if (x[i].equalsIgnoreCase("downloadable")) {
                        Downloadable_Prod.setChecked(true);
                        jsonObjects.put("Downloadable", Downloadable_Prod.getText().toString());
                        if (type_prod.isEmpty()) {
                            type_prod += Downloadable_Prod.getText();
                        } else {
                            type_prod += "," + Downloadable_Prod.getText();
                        }
                    } else if (x[i].equalsIgnoreCase("null")) {
                        Downloadable_Prod.setChecked(true);
                        Grouped_Prod.setChecked(true);
                        Config_Prod.setChecked(true);
                        Virtual_Prod.setChecked(true);
                        Bundle_Prod.setChecked(true);
                        Simple_Prod.setChecked(true);
                    }
                }

                ApplyTo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        if (parent.getSelectedItem().equals("Selected Product types")) {
                            apply_check_linear.setVisibility(View.VISIBLE);
                            Simple_Prod.setOnCheckedChangeListener((buttonView, isChecked) -> {
                                if (Simple_Prod.isChecked()) {
                                    try {
                                        jsonObjects.put("Simple", Simple_Prod.getText().toString());
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                } else {
                                    jsonObjects.remove("Simple");
                                }
                            });
                            Grouped_Prod.setOnCheckedChangeListener((buttonView, isChecked) -> {
                                if (Grouped_Prod.isChecked()) {
                                    try {
                                        jsonObjects.put("Grouped", Grouped_Prod.getText().toString());
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                } else {
                                    jsonObjects.remove("Grouped");
                                }
                            });
                            Config_Prod.setOnCheckedChangeListener((buttonView, isChecked) -> {
                                if (Config_Prod.isChecked()) {
                                    try {
                                        jsonObjects.put("Configurable", Config_Prod.getText().toString());
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                } else {
                                    jsonObjects.remove("Configurable");
                                }
                            });
                            Virtual_Prod.setOnCheckedChangeListener((buttonView, isChecked) -> {
                                if (Virtual_Prod.isChecked()) {
                                    try {
                                        jsonObjects.put("Virtual", Virtual_Prod.getText().toString());
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                } else {
                                    jsonObjects.remove("Virtual");
                                }
                            });

                            Bundle_Prod.setOnCheckedChangeListener((buttonView, isChecked) -> {
                                if (Bundle_Prod.isChecked()) {
                                    try {
                                        jsonObjects.put("Bundle", Bundle_Prod.getText().toString());
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                } else {
                                    jsonObjects.remove("Bundle");
                                }
                            });
                            Downloadable_Prod.setOnCheckedChangeListener((buttonView, isChecked) -> {
                                if (Downloadable_Prod.isChecked()) {
                                    try {
                                        jsonObjects.put("Downloadable", Downloadable_Prod.getText().toString());
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                } else {
                                    jsonObjects.remove("Downloadable");
                                }
                            });
                        } else {
                            apply_check_linear.setVisibility(View.GONE);
                            type_prod = "";
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
                ApplyTo.setEnabled(false);
                AddOptions.setOnClickListener(v -> addLayoutonclick());

                SaveAttribute.setOnClickListener(v -> {
                    Log.d("REpo", "edt_prod_attribute_1004: ");
                    if (edt_AttributeCode.getText().toString().isEmpty() || edt_Admin.getText().toString().isEmpty()) {
                        Toast.makeText(getApplicationContext(), R.string.emptyAttrvalues, Toast.LENGTH_LONG).show();
                    }
                    else {
                        Log.d("REpo", "edt_prod_attribute_972: ");
                        array = new JSONObject();
                        selected_attribute_array = new JSONArray();
                        for (int i = 0; i < dynamic_attributes.getChildCount(); i++) {
                            LinearLayout linearLayout = (LinearLayout) dynamic_attributes.getChildAt(i);
                            CheckBox checkBox = (CheckBox) linearLayout.getChildAt(0);
                            if (checkBox.isChecked()) {
                                TextView textView = (TextView) linearLayout.getChildAt(1);
                                selected_attribute_array.put(textView.getText().toString());
                            }
                        }
                        for (int i = 0; i < dynamic_layout.getChildCount(); i++) {
                            LinearLayout linearLayout = (LinearLayout) dynamic_layout.getChildAt(i);
                            LinearLayout linearLayout1 = (LinearLayout) linearLayout.getChildAt(0);
                            CheckBox checkBox = (CheckBox) linearLayout.getChildAt(1);
                            EditText admin_values = (EditText) linearLayout1.getChildAt(1);
                            TextView textView = (TextView) linearLayout.getChildAt(2);
                            if (textView.getText().toString().length() > 0) {
                                try {
                                    array.put(textView.getText().toString(), admin_values.getText().toString());
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            } else {
                                try {
                                    if (!admin_values.getText().toString().isEmpty()) {
                                        array.put("option_" + i, admin_values.getText().toString());
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                            if (checkBox.isChecked()) {
                                defaultattributehash.put(admin_values.getText().toString(), "1");
                                def_values.put(admin_values.getText().toString());
                            }
                        }
                        try {
                            frontend_labels.put(edt_Admin.getText().toString());
//                            frontend_labels.put(MultiVendor_edt_Default_Store_View.getText().toString());
                            req.put("frontend_label", frontend_labels);
                            JSONArray store_frontend_label = new JSONArray();
                            if (store_label_section.getChildCount() > 0)
                                for (int i = 0; i < store_label_section.getChildCount(); i++) {
                                    ConstraintLayout constraintLayout = (ConstraintLayout) store_label_section.getChildAt(i);
                                    AppCompatEditText store_label = (AppCompatEditText) constraintLayout.getChildAt(1);
                                    JSONObject store = new JSONObject();
                                    store.put("key", store_label.getTag().toString());
                                    store.put("label", store_label.getText().toString());
                                    store_frontend_label.put(store);
                                }
                            req.put("store_frontend_label", store_frontend_label);
                            req.put("frontend_input", frontend_type_list.get(CatalogInputType.getSelectedItem().toString()));
                            req.put("is_required", yesno_type_list.get(ValuesRequired.getSelectedItem().toString()));
                            req.put("attribute_code", edt_AttributeCode.getText().toString());
                            req.put("is_global", scope_type_list.get(Scope.getSelectedItem().toString()));
                            req.put("default_value_text", edt_DefaultValue.getText().toString());
                            req.put("default_value_date", edt_DefaultValue.getText().toString());
                            req.put("default_value_textarea", edt_DefaultValue.getText().toString());

                            req.put("default_value_yesno", yesno_type_list.get(default_spinner.getSelectedItem().toString()));
                            req.put("is_unique", yesno_type_list.get(UniqueValue.getSelectedItem().toString()));
                            req.put("frontend_class", fronetend_class_list.get(InputValidation.getSelectedItem().toString()));
//                            req.put("apply_to", jsonObjects.toString());
                            req.put("is_searchable", fronetend_class_list.get(QuickSearch.getSelectedItem().toString()));
                            req.put("is_visible_in_advanced_search", fronetend_class_list.get(AdvancedSearch.getSelectedItem().toString()));
                            req.put("is_comparable", fronetend_class_list.get(Front_end.getSelectedItem().toString()));
                            req.put("is_used_for_promo_rules", fronetend_class_list.get(PromoRule.getSelectedItem().toString()));
                            req.put("is_wysiwyg_enabled", fronetend_class_list.get(WYSIWYG.getSelectedItem().toString()));
                            req.put("is_html_allowed_on_front", fronetend_class_list.get(HTML_tag.getSelectedItem().toString()));
                            req.put("include_in_attribute_set", fronetend_class_list.get(AttributeSet.getSelectedItem().toString()));
                            req.put("is_visible_on_front", fronetend_class_list.get(ProductviewPage.getSelectedItem().toString()));
                            req.put("used_in_product_listing", fronetend_class_list.get(Productlisting.getSelectedItem().toString()));
                            req.put("used_for_sort_by", yesno_type_list.get(Sorting_Productlisting.getSelectedItem().toString()));

                            if (array.length()>0) {
                                req.put("option", array.toString());
                            }
                            if (def_values.length()>0) {
                                req.put("default", def_values.toString());
                            }

                            req.put("is_configurable", yesno_type_list.get(isConfigurable.getSelectedItem().toString()));
//                                req.put("is_filterable", LayeredNavigation.getSelectedItem().toString());
                            req.put("is_filterable_in_search", fronetend_class_list.get(SearchResultsLayeredNavigation.getSelectedItem().toString()));
                            req.put("position", Position.getText().toString());
                            req.put("is_visible_in_grid", yesno_type_list.get(MultiVendor_Add_to_column.getSelectedItem().toString()));
                            req.put("is_filterable_in_grid", yesno_type_list.get(MultiVendor_Use_to_filter.getSelectedItem().toString()));
                            req.put("attribute_set_ids", selected_attribute_array);
                            if (delete_sub.toString() != null) {
                                req.put("delete_item", delete_sub.toString());
                            }
                            if (getResources().getString(R.string.Enable_Log).equals("yes")) {
                                Log.i("send_data", req.toString());
                            }
                            saverequest();
                        }
                        catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
            else {
                message = jsonObject.getJSONObject("data").getString("message");
                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addLayoutonclick() {
        View view = View.inflate(this, R.layout.ced_multivendor_manage_label_dynamic_layout, null);
        final CheckBox default_chckbox = view.findViewById(R.id.MultiVendor_default_chckbox);
        final Button delete_row = view.findViewById(R.id.MultiVendor_delete_row);
        final EditText default_value = view.findViewById(R.id.MultiVendor_default_value);
        final TextView heading = view.findViewById(R.id.MultiVendor_heading);
        delete_row.setOnClickListener(v -> {
            LinearLayout linearLayout = (LinearLayout) delete_row.getParent();
            LinearLayout linearLayout1 = (LinearLayout) linearLayout.getParent();
            if (defaultattributehash.containsKey(default_value.getText().toString())) {
                defaultattributehash.clear();
            }
            dynamic_layout.removeView(linearLayout1);
        });
        default_chckbox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                if (defaultattributehash.size() > 0) {
                    default_chckbox.setChecked(false);
                    Toast.makeText(Ced_MultiVendor_EditProductAttribute_old.this, R.string.default_value_can_be_one, Toast.LENGTH_LONG).show();
                } else {
                    if (defaultattributehash.size() == 0) {
                        if (default_value.getText().toString().equals(""))
                            Toast.makeText(getApplicationContext(), R.string.Pleasefillvalue, Toast.LENGTH_LONG).show();
                        else
                            defaultattributehash.put(default_value.getText().toString(), "1");
                    }
                }
            } else {
                defaultattributehash.clear();
            }
        });
        dynamic_layout.addView(view);
    }

    private void saverequest() {
        postdata.put("attribute_data", req.toString());
        Ced_MultiVendor_ClientRequestResponse response = new Ced_MultiVendor_ClientRequestResponse(output -> {
            try {
                if (functionalityList.getExtensionAddon()) {
                    out = output.toString();
                    JSONObject object = new JSONObject(out);
                    if (object.getJSONObject("data").getBoolean("success")) {
                        Toast.makeText(getApplicationContext(), object.getJSONObject("data").getString("message"), Toast.LENGTH_LONG).show();
                        Intent prod_attr_page = new Intent(Ced_MultiVendor_EditProductAttribute_old.this, Ced_MultiVendor_VendorProductAttribute.class);
                        prod_attr_page.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(prod_attr_page);
                        overridePendingTransition(R.anim.ced_multivendor_slide_in, R.anim.ced_multivendor_slide_out);
                    } else
                        Toast.makeText(getApplicationContext(), object.getJSONObject("data").getString("message"), Toast.LENGTH_LONG).show();
                } else {
                    Intent intent = new Intent(getApplicationContext(), Ced_MultiVendor_UnAuthourizedRequestError.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    overridePendingTransition(R.anim.ced_multivendor_slide_in, R.anim.ced_multivendor_slide_out);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, Ced_MultiVendor_EditProductAttribute_old.this, "POST", postdata);
        response.execute(saveurl);
    }

    @Override
    protected void onResume() {
        Ced_MultiVendor_ConnectionDetector connectionDetector = new Ced_MultiVendor_ConnectionDetector(getApplicationContext());
        if (connectionDetector.isConnectingToInternet()) {
            setname(vendorSessionManagement.getvendorname());
            setprofilepic(vendorSessionManagement.getvendorpic());
            changetitle("Edit Product Attribute");
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
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void getAttributeOptions() {
        Ced_MultiVendor_ClientRequestResponse response = new Ced_MultiVendor_ClientRequestResponse(output -> {
            try {
                if (functionalityList.getExtensionAddon()) {
                    out = output.toString();
                    JSONObject obj = new JSONObject(out);
                    JSONObject data = obj.getJSONObject("data");
                    boolean success = data.getBoolean("success");
                    if (success) {
                        if (data.has("frontend_type")) {
                            JSONArray frontend_type = data.getJSONArray("frontend_type");
                            List<String> spn_label_list = new ArrayList<>();
                            for (int i = 0; i < frontend_type.length(); i++) {
                                JSONObject objects = frontend_type.getJSONObject(i);
                                frontend_type_list.put(objects.getString("label"), objects.getString("value"));
                                spn_label_list.add(objects.getString("label"));
                            }
                            ArrayAdapter<String> adapter = new ArrayAdapter<String>(Ced_MultiVendor_EditProductAttribute_old.this,
                                    R.layout.simple_spinner_dropdown_item, spn_label_list);
                            CatalogInputType.setAdapter(adapter);
                        }
                        if (data.has("scope_type")) {
                            JSONArray scope_type = data.getJSONArray("scope_type");
                            Log.d("REpo", "processFinish_1234: "+scope_type);
                            List<String> spn_label_list = new ArrayList<>();
                            for (int i = 0; i < scope_type.length(); i++) {
                                JSONObject objects = scope_type.getJSONObject(i);
                                scope_type_list.put(objects.getString("label"), objects.getString("value"));
                                spn_label_list.add(objects.getString("label"));
                            }
                            ArrayAdapter<String> adapter = new ArrayAdapter<String>(Ced_MultiVendor_EditProductAttribute_old.this,R.layout.simple_spinner_dropdown_item, spn_label_list);
                            Scope.setAdapter(adapter);
                        }
                        if (data.has("fronetend_class")) {
                            JSONArray fronetend_class = data.getJSONArray("fronetend_class");
                            List<String> spn_label_list = new ArrayList<>();
                            for (int i = 0; i < fronetend_class.length(); i++) {
                                JSONObject objects = fronetend_class.getJSONObject(i);
                                fronetend_class_list.put(objects.getString("label"), objects.getString("value"));
                                spn_label_list.add(objects.getString("label"));
                            }
                            ArrayAdapter<String> adapter = new ArrayAdapter<>(Ced_MultiVendor_EditProductAttribute_old.this, R.layout.simple_spinner_dropdown_item, spn_label_list);
                            InputValidation.setAdapter(adapter);
                            ArrayAdapter<String> adapter2 = new ArrayAdapter<>(Ced_MultiVendor_EditProductAttribute_old.this, R.layout.simple_spinner_dropdown_item, spn_label_list);
                            AdvancedSearch.setAdapter(adapter2);
                            ArrayAdapter<String> adapter10 = new ArrayAdapter<>(Ced_MultiVendor_EditProductAttribute_old.this, R.layout.simple_spinner_dropdown_item, spn_label_list);
                            SearchResultsLayeredNavigation.setAdapter(adapter10);
                            ArrayAdapter<String> adapter12 = new ArrayAdapter<>(Ced_MultiVendor_EditProductAttribute_old.this, R.layout.simple_spinner_dropdown_item, spn_label_list);
                            WYSIWYG.setAdapter(adapter12);
                            ArrayAdapter<String> adapter14 = new ArrayAdapter<>(Ced_MultiVendor_EditProductAttribute_old.this, R.layout.simple_spinner_dropdown_item, spn_label_list);
                            AttributeSet.setAdapter(adapter14);
                        }
                        if (data.has("store_array")) {
                            JSONArray store_array = data.getJSONArray("store_array");
                            if (store_array.length() > 0) {
                                store_label_section.setVisibility(View.VISIBLE);
                                for (int i = 0; i < store_array.length(); i++) {
                                    JSONObject store = store_array.getJSONObject(i);
                                    String store_id = store.getString("store_id");
                                    if (!store_id.equals("0")) {
                                        String store_name = store.getString("store_name");
                                        View v = View.inflate(Ced_MultiVendor_EditProductAttribute_old.this, R.layout.dyn_store_label, null);
                                        AppCompatTextView store_label_head = v.findViewById(R.id.store_label_head);
                                        AppCompatEditText store_label = v.findViewById(R.id.store_label);
                                        store_label_head.setText(store_name);
                                        store_label.setTag(store_id);
                                        store_label_section.addView(v);
                                    }
                                }
                            }
                        }
                        if (data.has("yesno_type")) {
                            JSONArray yesno_type = data.getJSONArray("yesno_type");
                            List<String> spn_label_list = new ArrayList<>();
                            for (int i = 0; i < yesno_type.length(); i++) {
                                JSONObject objects = yesno_type.getJSONObject(i);
                                yesno_type_list.put(objects.getString("label"), objects.getString("value"));
                                spn_label_list.add(objects.getString("label"));
                            }
                            ArrayAdapter<String> adapter = new ArrayAdapter<>(Ced_MultiVendor_EditProductAttribute_old.this,
                                    R.layout.simple_spinner_dropdown_item, spn_label_list);
                            QuickSearch.setAdapter(adapter);
                            ArrayAdapter<String> adapter1 = new ArrayAdapter<>(Ced_MultiVendor_EditProductAttribute_old.this, R.layout.simple_spinner_dropdown_item, spn_label_list);
                            UniqueValue.setAdapter(adapter1);
                            ArrayAdapter<String> adapter2 = new ArrayAdapter<>(Ced_MultiVendor_EditProductAttribute_old.this, R.layout.simple_spinner_dropdown_item, spn_label_list);
                            ValuesRequired.setAdapter(adapter2);
                            ArrayAdapter<String> adapter4 = new ArrayAdapter<>(Ced_MultiVendor_EditProductAttribute_old.this, R.layout.simple_spinner_dropdown_item, spn_label_list);
                            MultiVendor_Add_to_column.setAdapter(adapter4);
                            ArrayAdapter<String> adapter5 = new ArrayAdapter<>(Ced_MultiVendor_EditProductAttribute_old.this, R.layout.simple_spinner_dropdown_item, spn_label_list);
                            MultiVendor_Use_to_filter.setAdapter(adapter5);
                            ArrayAdapter<String> adapter6 = new ArrayAdapter<>(Ced_MultiVendor_EditProductAttribute_old.this, R.layout.simple_spinner_dropdown_item, spn_label_list);
                            default_spinner.setAdapter(adapter6);
                            ArrayAdapter<String> adapter7 = new ArrayAdapter<>(Ced_MultiVendor_EditProductAttribute_old.this, R.layout.simple_spinner_dropdown_item, spn_label_list);
                            Sorting_Productlisting.setAdapter(adapter7);
                            ArrayAdapter<String> adapter8 = new ArrayAdapter<>(Ced_MultiVendor_EditProductAttribute_old.this, R.layout.simple_spinner_dropdown_item, spn_label_list);
                            isConfigurable.setAdapter(adapter8);
                            ArrayAdapter<String> adapter9 = new ArrayAdapter<>(Ced_MultiVendor_EditProductAttribute_old.this, R.layout.simple_spinner_dropdown_item, spn_label_list);
                            Front_end.setAdapter(adapter9);
                            ArrayAdapter<String> adapter11 = new ArrayAdapter<>(Ced_MultiVendor_EditProductAttribute_old.this, R.layout.simple_spinner_dropdown_item, spn_label_list);
                            PromoRule.setAdapter(adapter11);
                            ArrayAdapter<String> adapter13 = new ArrayAdapter<>(Ced_MultiVendor_EditProductAttribute_old.this, R.layout.simple_spinner_dropdown_item, spn_label_list);
                            HTML_tag.setAdapter(adapter13);
                            ArrayAdapter<String> adapter15 = new ArrayAdapter<>(Ced_MultiVendor_EditProductAttribute_old.this, R.layout.simple_spinner_dropdown_item, spn_label_list);
                            ProductviewPage.setAdapter(adapter15);
                            ArrayAdapter<String> adapter16 = new ArrayAdapter<>(Ced_MultiVendor_EditProductAttribute_old.this, R.layout.simple_spinner_dropdown_item, spn_label_list);
                            Productlisting.setAdapter(adapter16);
                        }
                    }
                } else {
                    Intent intent = new Intent(Ced_MultiVendor_EditProductAttribute_old.this, Ced_MultiVendor_UnAuthourizedRequestError.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    overridePendingTransition(R.anim.ced_multivendor_slide_in, R.anim.ced_multivendor_slide_out);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, Ced_MultiVendor_EditProductAttribute_old.this);
        response.execute(attribute_options_url);
    }
}
