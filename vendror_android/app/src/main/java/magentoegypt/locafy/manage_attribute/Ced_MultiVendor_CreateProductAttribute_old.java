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
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.constraintlayout.widget.ConstraintLayout;

import magentoegypt.locafy.R;
import magentoegypt.locafy.api_request_response_section.AsyncResponse;
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

/**
 * Created by developer on 3/6/16.
 */
public class Ced_MultiVendor_CreateProductAttribute_old extends Ced_MultiVendor_NavigationActivity {
    static final String KEY_SET_ID = "set_id";
    static final String KEY_SET_CODE = "set_code";
    Ced_MultiVendor_ConnectionDetector connectionDetector;
    Ced_MultiVendor_VendorFunctionalityList functionalityList;
    Ced_MultiVendor_FontSetting fontSetting;
    HashMap<String, String> postdata;
    JSONArray  selected_attribute_array, frontend_label;
    JSONObject optionsObject;
    int current = 0;
    String out, url, hashkey, vendor_id, type_prod = "";
    HashMap<String, String> defaultattributehash;
    HashMap<String, String> frontend_type_list, scope_type_list, fronetend_class_list, yesno_type_list;
    JSONArray attribute;
    String set_id, set_code;
    TextView Atributecode_head, Scope_head, CatalogInputType_head, DefaultValue_head, UniqueValue_head,
            ValuesRequired_head, InputValidation_head, ApplyTo_head, QuickSearch_head,
            AdvancedSearch_head, Front_end_head, LayeredNavigation_head,
            SearchResultsLayeredNavigation_head, PromoRule_head, Position_head, HTML_tag_head,
            AttributeSet_head, ProductviewPage_head,
            ProductListing_head, Sorting_ProductListing_head,
            WYSIWYG_head, Note_manage_label, head_Admin, UsetoCreateConfig;

    AppCompatEditText edt_AttributeCode, edt_DefaultValue, edt_DefaultValueDate, Position, edt_Admin, MultiVendor_edt_Default_Store_View;
    Spinner Scope, CatalogInputType, default_spinner, UniqueValue, ValuesRequired,
            InputValidation, ApplyTo, QuickSearch, AdvancedSearch, Front_end,
            LayeredNavigation, SearchResultsLayeredNavigation, PromoRule,
            HTML_tag, AttributeSet, ProductviewPage, Productlisting,
            Sorting_Productlisting, WYSIWYG, isConfigurable, MultiVendor_Add_to_column, MultiVendor_Use_to_filter;
    LinearLayout front_end_section_linear, scope_linear, Default_linear, Unique_linear,
            ValuesRequired_linear, InputValidation_linear, ApplyTo_linear, QuickSearch_linear,
            AdvancedSearch_linear, Front_end_linear, LayeredNavigation_linear,
            SearchResultsLayeredNavigation_linear, PromoRule_linear,
            Position_linear, HTML_tag_linear, AttributeSet_linear, ProductviewPage_linear, Productlisting_linear, Sorting_Productlisting_linear, apply_check_linear,
            WYSIWYG_linear, dynamic_layout, store_label_section,
            Manage_Labels_linear, Add_button_linear, isConfigurable_linear;
    Button FrontendProperties, Manage_Labels, AddOptions, SaveAttribute;

    CheckBox Simple_Prod, Grouped_Prod, Config_Prod, Virtual_Prod, Bundle_Prod, Downloadable_Prod;
    JSONArray defaultOptions;
    JSONObject req, default_value_of_attribute;
    Calendar newCalendar;
    JSONObject jsonObject;
    LinearLayout dynamic_attributes;
    String attribute_set_url = "";
    String attribute_options_url = "";
    HashMap<String, HashMap<String, String>> AddNew_collection;
    String[] MultiVendor_Scope = {"Select", "Store View", "Website", "Global"};
    String[] MultiVendor_CatalogInputType = {"Text Field", "Text Area", "Date", "Yes/No", "Multiple Select", "Dropdown", "Price", "Media Image"};
    //MultiVendor_default_spinner,MultiVendor_UniqueValue,MultiVendor_ValuesRequired,MultiVendor_Add_to_column,MultiVendor_Use_to_filter,MultiVendor_isConfigurable,
    //MultiVendor_QuickSearch,MultiVendor_AdvancedSearch,MultiVendor_Front_end,MultiVendor_SearchResultsLayeredNavigation,MultiVendor_PromoRule,MultiVendor_WYSIWYG
    //MultiVendor_HTML_tag,MultiVendor_AttributeSet,MultiVendor_ProductviewPage,MultiVendor_Productlisting,MultiVendor_Sorting_Productlisting
    String[] MultiVendor_default_spinner = {"Select", "Yes", "No"};
    String[] MultiVendor_LayeredNavigation = {"Select", "Filterable(with results)", "Filterable(no results)", "No"};
    String[] MultiVendor_ApplyTo = {"All Product Types", "Selected Product Types"};
    String[] MultiVendor_InputValidation = {"None", "Decimal Number", "Integer Number", "Email", "URL", "Letters", "Letters (a-z,A-Z) or Numbers(0-9)"};
    private SimpleDateFormat dateFormatter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        connectionDetector = new Ced_MultiVendor_ConnectionDetector(getApplicationContext());
        functionalityList = new Ced_MultiVendor_VendorFunctionalityList(getApplicationContext());
        fontSetting = new Ced_MultiVendor_FontSetting();
        postdata = new HashMap<>();
        defaultattributehash = new HashMap<>();
        frontend_type_list = new HashMap<>();
        scope_type_list = new HashMap<>();
        fronetend_class_list = new HashMap<>();
        yesno_type_list = new HashMap<>();
        req = new JSONObject();
        defaultOptions = new JSONArray();
        default_value_of_attribute = new JSONObject();
        jsonObject = new JSONObject();
        AddNew_collection = new HashMap<>();

        url = session.getBase_Url() + "vproductattributeapi/index/saveAttribute";
        attribute_set_url = session.getBase_Url() + "vproductattributeapi/set/info";
        attribute_options_url = session.getBase_Url() + "vproductattributeapi/index/getAttributeOptions";
        if (connectionDetector.isConnectingToInternet()) {
            ViewGroup content = findViewById(R.id.MultiVendor_frame_container);
            getLayoutInflater().inflate(R.layout.ced_multivendor_create_product_attribute_page, content, true);
            if (session.getvendorname() != null) {
                setname(session.getvendorname());
                setprofilepic(session.getvendorpic());
                changetitle("Vendor Product Attribute");
            }
            getAttributeOptions();
            final HashMap<String, String> user = session.getUserDetails();
            hashkey = user.get(Ced_MultiVendor_VendorSessionManagement.Key_HAsh);
            vendor_id = session.getVendorid();
            postdata.put("vendor_id", vendor_id);
            postdata.put("hashkey", hashkey);
            getAttributeSet();
            Atributecode_head = findViewById(R.id.MultiVendor_Atributecode_head);
            Scope_head = findViewById(R.id.MultiVendor_Scope_head);
            store_label_section = findViewById(R.id.store_label);
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
            dynamic_attributes = findViewById(R.id.dynamic_attributes);
            fontSetting.setFontforTextviews(Atributecode_head, "Roboto-Medium.ttf", Ced_MultiVendor_CreateProductAttribute_old.this);
            fontSetting.setFontforTextviews(head_Admin, "Roboto-Medium.ttf", Ced_MultiVendor_CreateProductAttribute_old.this);
            fontSetting.setFontforTextviews(Scope_head, "Roboto-Medium.ttf", Ced_MultiVendor_CreateProductAttribute_old.this);
            fontSetting.setFontforTextviews(CatalogInputType_head, "Roboto-Medium.ttf", Ced_MultiVendor_CreateProductAttribute_old.this);
            fontSetting.setFontforTextviews(DefaultValue_head, "Roboto-Medium.ttf", Ced_MultiVendor_CreateProductAttribute_old.this);
            fontSetting.setFontforTextviews(UniqueValue_head, "Roboto-Medium.ttf", Ced_MultiVendor_CreateProductAttribute_old.this);
            fontSetting.setFontforTextviews(ValuesRequired_head, "Roboto-Medium.ttf", Ced_MultiVendor_CreateProductAttribute_old.this);
            fontSetting.setFontforTextviews(InputValidation_head, "Roboto-Medium.ttf", Ced_MultiVendor_CreateProductAttribute_old.this);
            fontSetting.setFontforTextviews(ApplyTo_head, "Roboto-Medium.ttf", Ced_MultiVendor_CreateProductAttribute_old.this);
            fontSetting.setFontforTextviews(QuickSearch_head, "Roboto-Medium.ttf", Ced_MultiVendor_CreateProductAttribute_old.this);
            fontSetting.setFontforTextviews(AdvancedSearch_head, "Roboto-Medium.ttf", Ced_MultiVendor_CreateProductAttribute_old.this);
            fontSetting.setFontforTextviews(Front_end_head, "Roboto-Medium.ttf", Ced_MultiVendor_CreateProductAttribute_old.this);
            fontSetting.setFontforTextviews(LayeredNavigation_head, "Roboto-Medium.ttf", Ced_MultiVendor_CreateProductAttribute_old.this);
            fontSetting.setFontforTextviews(SearchResultsLayeredNavigation_head, "Roboto-Medium.ttf", Ced_MultiVendor_CreateProductAttribute_old.this);
            fontSetting.setFontforTextviews(Position_head, "Roboto-Medium.ttf", Ced_MultiVendor_CreateProductAttribute_old.this);
            fontSetting.setFontforTextviews(HTML_tag_head, "Roboto-Medium.ttf", Ced_MultiVendor_CreateProductAttribute_old.this);
            fontSetting.setFontforTextviews(AttributeSet_head, "Roboto-Medium.ttf", Ced_MultiVendor_CreateProductAttribute_old.this);
            fontSetting.setFontforTextviews(ProductviewPage_head, "Roboto-Medium.ttf", Ced_MultiVendor_CreateProductAttribute_old.this);
            fontSetting.setFontforTextviews(ProductListing_head, "Roboto-Medium.ttf", Ced_MultiVendor_CreateProductAttribute_old.this);
            fontSetting.setFontforTextviews(Sorting_ProductListing_head, "Roboto-Medium.ttf", Ced_MultiVendor_CreateProductAttribute_old.this);
            fontSetting.setFontforTextviews(WYSIWYG_head, "Roboto-Medium.ttf", Ced_MultiVendor_CreateProductAttribute_old.this);
            fontSetting.setFontforTextviews(Note_manage_label, "Roboto-Medium.ttf", Ced_MultiVendor_CreateProductAttribute_old.this);
            fontSetting.setFontforTextviews(PromoRule_head, "Roboto-Medium.ttf", Ced_MultiVendor_CreateProductAttribute_old.this);

            edt_AttributeCode = findViewById(R.id.MultiVendor_edt_AttributeCode);
            edt_DefaultValue = findViewById(R.id.MultiVendor_edt_DefaultValue);
            edt_DefaultValueDate = findViewById(R.id.MultiVendor_edt_DefaultValueDate);
            Position = findViewById(R.id.MultiVendor_Position);
            edt_Admin = findViewById(R.id.MultiVendor_edt_Admin);
            MultiVendor_edt_Default_Store_View = findViewById(R.id.MultiVendor_edt_Default_Store_View);
            Scope = findViewById(R.id.MultiVendor_Scope);
            CatalogInputType = findViewById(R.id.MultiVendor_CatalogInputType);
            UniqueValue = findViewById(R.id.MultiVendor_UniqueValue);
            ValuesRequired = findViewById(R.id.MultiVendor_ValuesRequired);
            InputValidation = findViewById(R.id.MultiVendor_InputValidation);
            MultiVendor_Add_to_column = findViewById(R.id.MultiVendor_Add_to_column);
            MultiVendor_Use_to_filter = findViewById(R.id.MultiVendor_Use_to_filter);
            QuickSearch = findViewById(R.id.MultiVendor_QuickSearch);
            AdvancedSearch = findViewById(R.id.MultiVendor_AdvancedSearch);
            Front_end = findViewById(R.id.MultiVendor_Front_end);
            LayeredNavigation = findViewById(R.id.MultiVendor_LayeredNavigation);
            ArrayAdapter<String> adapter9 = new ArrayAdapter<>(this, R.layout.simple_spinner_dropdown_item, MultiVendor_LayeredNavigation);
            LayeredNavigation.setAdapter(adapter9);
            SearchResultsLayeredNavigation = findViewById(R.id.MultiVendor_SearchResultsLayeredNavigation);
            PromoRule = findViewById(R.id.MultiVendor_PromoRule);
            WYSIWYG = findViewById(R.id.MultiVendor_WYSIWYG);
            HTML_tag = findViewById(R.id.MultiVendor_HTML_tag);
            AttributeSet = findViewById(R.id.MultiVendor_AttributeSet);
            ProductviewPage = findViewById(R.id.MultiVendor_ProductviewPage);
            Productlisting = findViewById(R.id.MultiVendor_Productlisting);
            Sorting_Productlisting = findViewById(R.id.MultiVendor_Sorting_Productlisting);
            isConfigurable = findViewById(R.id.MultiVendor_isConfigurable);
            default_spinner = findViewById(R.id.MultiVendor_default_spinner);
            ApplyTo = findViewById(R.id.MultiVendor_ApplyTo);


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


            fontSetting.setfontforButtons(FrontendProperties, "Roboto-Medium.ttf", Ced_MultiVendor_CreateProductAttribute_old.this);
            fontSetting.setfontforButtons(Manage_Labels, "Roboto-Medium.ttf", Ced_MultiVendor_CreateProductAttribute_old.this);


            CatalogInputType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if (parent.getSelectedItem().toString().equals("Media Image")) {
                        front_end_section_linear.setVisibility(View.GONE);
                        Default_linear.setVisibility(View.GONE);
                        Unique_linear.setVisibility(View.GONE);
                        ValuesRequired_linear.setVisibility(View.GONE);
                        InputValidation_linear.setVisibility(View.GONE);
                        WYSIWYG_linear.setVisibility(View.GONE);
                        AddOptions.setVisibility(View.GONE);
                        Add_button_linear.setVisibility(View.GONE);
                        isConfigurable_linear.setVisibility(View.GONE);
                    } else if (parent.getSelectedItem().toString().equals("Price")) {
                        scope_linear.setVisibility(View.GONE);
                        InputValidation_linear.setVisibility(View.GONE);
                        HTML_tag_linear.setVisibility(View.GONE);
                        WYSIWYG_linear.setVisibility(View.GONE);
                        AddOptions.setVisibility(View.GONE);
                        Add_button_linear.setVisibility(View.GONE);
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
                        isConfigurable_linear.setVisibility(View.GONE);
                    } else if (parent.getSelectedItem().toString().equals("Dropdown")) {
                        InputValidation_linear.setVisibility(View.GONE);
                        Default_linear.setVisibility(View.GONE);
                        WYSIWYG_linear.setVisibility(View.GONE);
                        AddOptions.setVisibility(View.VISIBLE);
                        Add_button_linear.setVisibility(View.VISIBLE);
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
                                if (parent.getSelectedItem().toString().equals("Global"))
                                    isConfigurable_linear.setVisibility(View.VISIBLE);
                                else
                                    isConfigurable_linear.setVisibility(View.GONE);
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
                    } else if (parent.getSelectedItem().toString().equals("Multiple Select")) {
                        InputValidation_linear.setVisibility(View.GONE);
                        Default_linear.setVisibility(View.GONE);
                        edt_DefaultValueDate.setVisibility(View.GONE);
                        Sorting_Productlisting_linear.setVisibility(View.GONE);
                        WYSIWYG_linear.setVisibility(View.GONE);
                        AddOptions.setVisibility(View.VISIBLE);
                        Add_button_linear.setVisibility(View.VISIBLE);
                        scope_linear.setVisibility(View.VISIBLE);
                        HTML_tag_linear.setVisibility(View.VISIBLE);
                        front_end_section_linear.setVisibility(View.VISIBLE);
                        isConfigurable_linear.setVisibility(View.GONE);
                        Unique_linear.setVisibility(View.VISIBLE);
                        ValuesRequired_linear.setVisibility(View.VISIBLE);
                        LayeredNavigation_linear.setVisibility(View.VISIBLE);
                        SearchResultsLayeredNavigation_linear.setVisibility(View.VISIBLE);
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
                    } else if (parent.getSelectedItem().toString().equals("Yes/No")) {
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
                        Add_button_linear.setVisibility(View.GONE);
                        Default_linear.setVisibility(View.VISIBLE);
                        scope_linear.setVisibility(View.VISIBLE);
                        isConfigurable_linear.setVisibility(View.GONE);
                        front_end_section_linear.setVisibility(View.VISIBLE);
                        Unique_linear.setVisibility(View.VISIBLE);
                        ValuesRequired_linear.setVisibility(View.VISIBLE);
                        Sorting_Productlisting_linear.setVisibility(View.VISIBLE);
                    } else if (parent.getSelectedItem().toString().equals("Date")) {
                        edt_DefaultValue.setVisibility(View.GONE);
                        edt_DefaultValueDate.setVisibility(View.VISIBLE);
                        default_spinner.setVisibility(View.GONE);
                        InputValidation_linear.setVisibility(View.GONE);
                        LayeredNavigation_linear.setVisibility(View.GONE);
                        SearchResultsLayeredNavigation_linear.setVisibility(View.GONE);
                        Position_linear.setVisibility(View.GONE);
                        HTML_tag_linear.setVisibility(View.GONE);
                        WYSIWYG_linear.setVisibility(View.GONE);
                        AddOptions.setVisibility(View.GONE);
                        Add_button_linear.setVisibility(View.GONE);
                        Default_linear.setVisibility(View.VISIBLE);
                        scope_linear.setVisibility(View.VISIBLE);
                        front_end_section_linear.setVisibility(View.VISIBLE);
                        Unique_linear.setVisibility(View.VISIBLE);
                        ValuesRequired_linear.setVisibility(View.VISIBLE);
                        Sorting_Productlisting_linear.setVisibility(View.VISIBLE);
                        edt_DefaultValueDate.setFocusable(false);
                        edt_DefaultValueDate.setClickable(true);
                        edt_DefaultValueDate.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                newCalendar = Calendar.getInstance();
                                dateFormatter = new SimpleDateFormat("MM/dd/yyyy", Locale.US);
                                new DatePickerDialog(Ced_MultiVendor_CreateProductAttribute_old.this, new DatePickerDialog.OnDateSetListener() {

                                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                        newCalendar.set(Calendar.YEAR, year);
                                        newCalendar.set(Calendar.MONTH, monthOfYear);
                                        newCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                                        edt_DefaultValue.setText(dateFormatter.format(newCalendar.getTime()));
                                        edt_DefaultValueDate.setText(dateFormatter.format(newCalendar.getTime()));
                                    }

                                }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH)).show();
                            }
                        });
                    } else if (parent.getSelectedItem().toString().equals("Text Area")) {
                        edt_DefaultValue.setVisibility(View.VISIBLE);
                        edt_DefaultValue.setFocusable(true);
                        edt_DefaultValueDate.setVisibility(View.GONE);
                        default_spinner.setVisibility(View.GONE);
                        isConfigurable_linear.setVisibility(View.GONE);
                        InputValidation_linear.setVisibility(View.GONE);
                        LayeredNavigation_linear.setVisibility(View.GONE);
                        SearchResultsLayeredNavigation_linear.setVisibility(View.GONE);
                        Position_linear.setVisibility(View.GONE);
                        Sorting_Productlisting_linear.setVisibility(View.GONE);
                        AddOptions.setVisibility(View.GONE);
                        Add_button_linear.setVisibility(View.GONE);
                        HTML_tag_linear.setVisibility(View.VISIBLE);
                        WYSIWYG_linear.setVisibility(View.VISIBLE);
                        Default_linear.setVisibility(View.VISIBLE);
                        scope_linear.setVisibility(View.VISIBLE);
                        front_end_section_linear.setVisibility(View.VISIBLE);
                        Unique_linear.setVisibility(View.VISIBLE);
                        ValuesRequired_linear.setVisibility(View.VISIBLE);
                    } else if (parent.getSelectedItem().toString().equals("Text Field")) {
                        InputValidation_linear.setVisibility(View.VISIBLE);
                        edt_DefaultValue.setVisibility(View.VISIBLE);
                        edt_DefaultValueDate.setVisibility(View.GONE);
                        default_spinner.setVisibility(View.GONE);
                        LayeredNavigation_linear.setVisibility(View.GONE);
                        SearchResultsLayeredNavigation_linear.setVisibility(View.GONE);
                        Position_linear.setVisibility(View.GONE);
                        AddOptions.setVisibility(View.GONE);
                        isConfigurable_linear.setVisibility(View.GONE);
                        Add_button_linear.setVisibility(View.GONE);
                        HTML_tag_linear.setVisibility(View.VISIBLE);
                        Default_linear.setVisibility(View.VISIBLE);
                        scope_linear.setVisibility(View.VISIBLE);
                        front_end_section_linear.setVisibility(View.VISIBLE);
                        Unique_linear.setVisibility(View.VISIBLE);
                        ValuesRequired_linear.setVisibility(View.VISIBLE);
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

            ApplyTo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if (parent.getSelectedItem().equals("Selected Product types")) {
                        apply_check_linear.setVisibility(View.VISIBLE);
                        Simple_Prod.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                            @Override
                            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                if (Simple_Prod.isChecked()) {
                                    try {
                                        type_prod += Simple_Prod.getText();
                                        jsonObject.put("Simple", Simple_Prod.getText().toString());
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                } else
                                    jsonObject.remove("Simple");
                                Log.i("apply_to", jsonObject.toString());
                            }

                        });
                        Grouped_Prod.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                            @Override
                            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                if (Grouped_Prod.isChecked()) {
                                    try {
                                        type_prod += Grouped_Prod.getText();
                                        jsonObject.put("Grouped", Grouped_Prod.getText().toString());
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                } else
                                    jsonObject.remove("Grouped");
                                Log.i("apply_to", jsonObject.toString());
                            }
                        });
                        Config_Prod.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                            @Override
                            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                if (Config_Prod.isChecked()) {
                                    try {
                                        jsonObject.put("Configurable", Config_Prod.getText().toString());
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    Log.i("apply_to", jsonObject.toString());
                                } else
                                    jsonObject.remove("Configurable");
                            }
                        });
                        Virtual_Prod.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                            @Override
                            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                if (Virtual_Prod.isChecked()) {
                                    try {
                                        jsonObject.put("Virtual", Virtual_Prod.getText().toString());
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    Log.i("apply_to", jsonObject.toString());
                                } else
                                    jsonObject.remove("Virtual");
                            }
                        });

                        Bundle_Prod.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                            @Override
                            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                if (Bundle_Prod.isChecked()) {
                                    try {
                                        jsonObject.put("Bundle", Bundle_Prod.getText().toString());
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    Log.i("apply_to", jsonObject.toString());
                                } else
                                    jsonObject.remove("Bundle");
                            }
                        });
                        Downloadable_Prod.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                            @Override
                            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                if (Downloadable_Prod.isChecked()) {
                                    try {
                                        jsonObject.put("Downloadable", Downloadable_Prod.getText().toString());
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    Log.i("apply_to", jsonObject.toString());
                                } else
                                    jsonObject.remove("Downloadable");
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

            AddOptions.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    addLayoutonclick();
                }
            });

            SaveAttribute.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (Objects.requireNonNull(edt_AttributeCode.getText()).toString().isEmpty() || Objects.requireNonNull(edt_Admin.getText()).toString().isEmpty()) {
                        Toast.makeText(getApplicationContext(), R.string.emptyAttrvalues, Toast.LENGTH_LONG).show();
                    } else {
                        optionsObject = new JSONObject();
                        selected_attribute_array = new JSONArray();
                        frontend_label = new JSONArray();
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
                            AppCompatEditText admin_values = (AppCompatEditText) linearLayout1.getChildAt(1);
//                            optionsObject.put(Objects.requireNonNull(admin_values.getText()).toString());
                            try {
                                optionsObject.put("option_" + i, new JSONArray().put(new JSONObject().put("0",admin_values.getText().toString())));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                        try {
                            frontend_label.put(edt_Admin.getText().toString());
                            //frontend_label.put(Objects.requireNonNull(MultiVendor_edt_Default_Store_View.getText()).toString());
                            req.put("frontend_label", frontend_label);
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
                            req.put("default_value_text", Objects.requireNonNull(edt_DefaultValue.getText()).toString());
                            req.put("default_value_date", edt_DefaultValue.getText().toString());
                            req.put("default_value_textarea", edt_DefaultValue.getText().toString());
                            req.put("is_unique", yesno_type_list.get(UniqueValue.getSelectedItem().toString()));
                            req.put("frontend_class", fronetend_class_list.get(InputValidation.getSelectedItem().toString()));
                            req.put("is_searchable", fronetend_class_list.get(QuickSearch.getSelectedItem().toString()));
                            req.put("is_comparable", fronetend_class_list.get(Front_end.getSelectedItem().toString()));
                            req.put("is_used_for_promo_rules", fronetend_class_list.get(PromoRule.getSelectedItem().toString()));
                            req.put("is_html_allowed_on_front", fronetend_class_list.get(HTML_tag.getSelectedItem().toString()));
                            req.put("include_in_attribute_set", fronetend_class_list.get(AttributeSet.getSelectedItem().toString()));
                            req.put("attribute_set_ids", selected_attribute_array);
                            req.put("is_visible_on_front", fronetend_class_list.get(ProductviewPage.getSelectedItem().toString()));
                            req.put("used_in_product_listing", fronetend_class_list.get(Productlisting.getSelectedItem().toString()));
                            req.put("used_for_sort_by", yesno_type_list.get(Sorting_Productlisting.getSelectedItem().toString()));

                            req.put("default_value_yesno", yesno_type_list.get(default_spinner.getSelectedItem().toString()));
//                            req.put("apply_to", jsonObject.toString());
                            req.put("is_visible_in_advanced_search", fronetend_class_list.get(AdvancedSearch.getSelectedItem().toString()));
                            req.put("is_wysiwyg_enabled", fronetend_class_list.get(WYSIWYG.getSelectedItem().toString()));
/*
                            if (optionsObject.length()>0) {
                              req.put("option", optionsObject.toString());
                            }
                            if (defaultOptions.length()>0) {
                              req.put("default", defaultOptions.toString());
                            }*/

                            if (optionsObject.length()>0) {
                                req.put("option", optionsObject);
                            }
                            if (defaultOptions.length()>0) {
                                req.put("default", defaultOptions);
                            }


                            req.put("is_configurable", yesno_type_list.get(isConfigurable.getSelectedItem().toString()));
//                            req.put("is_filterable", LayeredNavigation.getSelectedItem().toString());
                            req.put("is_filterable_in_search", fronetend_class_list.get(SearchResultsLayeredNavigation.getSelectedItem().toString()));
                            req.put("position", Objects.requireNonNull(Position.getText()).toString());
                            req.put("is_visible_in_grid", yesno_type_list.get(MultiVendor_Add_to_column.getSelectedItem().toString()));
                            req.put("is_filterable_in_grid", yesno_type_list.get(MultiVendor_Use_to_filter.getSelectedItem().toString()));
                            if (getResources().getString(R.string.Enable_Log).equals("yes"))
                                Log.i("send_data", req.toString());
                            request();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
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

    private void getAttributeSet() {
        Ced_MultiVendor_ClientRequestResponse response = new Ced_MultiVendor_ClientRequestResponse(new AsyncResponse() {
            @Override
            public void processFinish(Object output) throws JSONException {
                if (functionalityList.getExtensionAddon()) {
                    out = output.toString();
                    if (out.contains("NO_ORDER"))
                        Toast.makeText(getApplicationContext(), R.string.NoOrdersToList, Toast.LENGTH_LONG).show();
                    else
                        prod_attr_listdata();
                } else {
                    Intent intent = new Intent(Ced_MultiVendor_CreateProductAttribute_old.this, Ced_MultiVendor_UnAuthourizedRequestError.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    overridePendingTransition(R.anim.ced_multivendor_slide_in, R.anim.ced_multivendor_slide_out);
                }
            }
        }, Ced_MultiVendor_CreateProductAttribute_old.this, "POST", postdata);
        response.execute(attribute_set_url + "/page/" + current);
    }

    private void prod_attr_listdata() throws JSONException {
        jsonObject = new JSONObject(out);
        if (jsonObject.getJSONObject("data").getBoolean("success")) {
            if (jsonObject.getJSONObject("data").has("attribute")) {
                if (jsonObject.getJSONObject("data").getJSONArray("attribute").length() > 0) {
                    attribute = jsonObject.getJSONObject("data").getJSONArray("attribute");
                    for (int i = 0; i < attribute.length(); i++) {
                        JSONObject c = attribute.getJSONObject(i);
                        set_id = c.getString(KEY_SET_ID);
                        set_code = c.getString(KEY_SET_CODE);
                        HashMap sets = new HashMap();
                        sets.put("set_code", set_code);
                        sets.put("set_id", set_id);
                        AddNew_collection.put(set_code + "#" + set_id, sets);
                    }
                    if (getResources().getString(R.string.Enable_Log).equals("yes"))
                        Log.i("add_new_data", "" + AddNew_collection);
                    for (Map.Entry<String, HashMap<String, String>> stringHashMapEntry : AddNew_collection.entrySet()) {
                        View view2 = View.inflate(this, R.layout.ced_multivendor_manage_attribute_set_dynamic_layout, null);
                        TextView MultiVendor_text_attribute_set = view2.findViewById(R.id.MultiVendor_text_attribute_set);
                        CheckBox MultiVendor_checkbox_attribute_set = view2.findViewById(R.id.MultiVendor_checkbox_attribute_set);
                        Map.Entry entry = stringHashMapEntry;
                        String key = String.valueOf(entry.getKey());
                        String[] parts = key.split("#");
                        MultiVendor_text_attribute_set.setText(parts[1]);
                        MultiVendor_checkbox_attribute_set.setText(parts[0]);
                        dynamic_attributes.addView(view2);
                    }
                }
            }
        }
    }

    private void request() {
        postdata.put("attribute_data", req.toString());
        Ced_MultiVendor_ClientRequestResponse response = new Ced_MultiVendor_ClientRequestResponse(new AsyncResponse() {
            @Override
            public void processFinish(Object output) throws JSONException {
                if (functionalityList.getExtensionAddon()) {
                    out = output.toString();
                    JSONObject object = new JSONObject(out);
                    if (object.getJSONObject("data").getBoolean("success")) {
                        Toast.makeText(getApplicationContext(), object.getJSONObject("data").getString("message"), Toast.LENGTH_LONG).show();
                        Intent prod_attr_page = new Intent(Ced_MultiVendor_CreateProductAttribute_old.this, Ced_MultiVendor_VendorProductAttribute.class);
                        prod_attr_page.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(prod_attr_page);
                        overridePendingTransition(R.anim.ced_multivendor_slide_in, R.anim.ced_multivendor_slide_out);
                    } else {
                        Toast.makeText(getApplicationContext(), object.getJSONObject("data").getString("message"), Toast.LENGTH_LONG).show();
                    }

                } else {
                    Intent intent = new Intent(getApplicationContext(), Ced_MultiVendor_UnAuthourizedRequestError.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    overridePendingTransition(R.anim.ced_multivendor_slide_in, R.anim.ced_multivendor_slide_out);
                }
            }
        }, Ced_MultiVendor_CreateProductAttribute_old.this, "POST", postdata);
        response.execute(url);
    }

    private void addLayoutonclick() {
        View view = View.inflate(this, R.layout.ced_multivendor_manage_label_dynamic_layout, null);

        final CheckBox default_chckbox = view.findViewById(R.id.MultiVendor_default_chckbox);
        final Button delete_row = view.findViewById(R.id.MultiVendor_delete_row);
        final AppCompatEditText default_value = view.findViewById(R.id.MultiVendor_default_value);

        delete_row.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LinearLayout linearLayout = (LinearLayout) delete_row.getParent();
                LinearLayout linearLayout1 = (LinearLayout) linearLayout.getParent();
                if (defaultattributehash.containsKey(Objects.requireNonNull(default_value.getText()).toString())) {
                    defaultattributehash.clear();
                }
                dynamic_layout.removeView(linearLayout1);
            }
        });
        default_chckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    if (defaultattributehash.size() > 0) {
                        default_chckbox.setChecked(false);
                        Toast.makeText(Ced_MultiVendor_CreateProductAttribute_old.this, R.string.default_value_can_be_one, Toast.LENGTH_LONG).show();
                    } else {
                        if (defaultattributehash.size() == 0) {
                            if (Objects.requireNonNull(default_value.getText()).toString().equals(""))
                                Toast.makeText(getApplicationContext(), R.string.Pleasefillvalue, Toast.LENGTH_LONG).show();
                            else {
                                defaultattributehash.put(default_value.getText().toString(), "1");
//                                defaultOptions.put(default_value.getText().toString());
                                try {
                                    defaultOptions.put(new JSONObject().put("0",default_value.getText().toString()));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                } else
                    defaultattributehash.clear();
            }
        });
        dynamic_layout.addView(view);
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

    private void getAttributeOptions() {
        Ced_MultiVendor_ClientRequestResponse response = new Ced_MultiVendor_ClientRequestResponse(new AsyncResponse() {
            @Override
            public void processFinish(Object output) throws JSONException {
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
                            ArrayAdapter<String> adapter = new ArrayAdapter<>(Ced_MultiVendor_CreateProductAttribute_old.this,
                                    R.layout.simple_spinner_dropdown_item, spn_label_list);
                            CatalogInputType.setAdapter(adapter);
                        }
                        if (data.has("scope_type")) {
                            JSONArray scope_type = data.getJSONArray("scope_type");
                            List<String> spn_label_list = new ArrayList<>();
                            for (int i = 0; i < scope_type.length(); i++) {
                                JSONObject objects = scope_type.getJSONObject(i);
                                scope_type_list.put(objects.getString("label"), objects.getString("value"));
                                spn_label_list.add(objects.getString("label"));
                            }
                            ArrayAdapter<String> adapter = new ArrayAdapter<>(Ced_MultiVendor_CreateProductAttribute_old.this,
                                    R.layout.simple_spinner_dropdown_item, spn_label_list);
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
                            ArrayAdapter<String> adapter = new ArrayAdapter<>(Ced_MultiVendor_CreateProductAttribute_old.this, R.layout.simple_spinner_dropdown_item, spn_label_list);
                            InputValidation.setAdapter(adapter);
                            ArrayAdapter<String> adapter2 = new ArrayAdapter<>(Ced_MultiVendor_CreateProductAttribute_old.this, R.layout.simple_spinner_dropdown_item, spn_label_list);
                            AdvancedSearch.setAdapter(adapter2);
                            ArrayAdapter<String> adapter10 = new ArrayAdapter<>(Ced_MultiVendor_CreateProductAttribute_old.this, R.layout.simple_spinner_dropdown_item, spn_label_list);
                            SearchResultsLayeredNavigation.setAdapter(adapter10);
                            ArrayAdapter<String> adapter12 = new ArrayAdapter<>(Ced_MultiVendor_CreateProductAttribute_old.this, R.layout.simple_spinner_dropdown_item, spn_label_list);
                            WYSIWYG.setAdapter(adapter12);
                            ArrayAdapter<String> adapter14 = new ArrayAdapter<>(Ced_MultiVendor_CreateProductAttribute_old.this, R.layout.simple_spinner_dropdown_item, spn_label_list);
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
                                        View v = View.inflate(Ced_MultiVendor_CreateProductAttribute_old.this, R.layout.dyn_store_label, null);
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
                            ArrayAdapter<String> adapter = new ArrayAdapter<>(Ced_MultiVendor_CreateProductAttribute_old.this, R.layout.simple_spinner_dropdown_item, spn_label_list);
                            QuickSearch.setAdapter(adapter);
                            ArrayAdapter<String> adapter1 = new ArrayAdapter<>(Ced_MultiVendor_CreateProductAttribute_old.this, R.layout.simple_spinner_dropdown_item, spn_label_list);
                            UniqueValue.setAdapter(adapter1);
                            ArrayAdapter<String> adapter2 = new ArrayAdapter<>(Ced_MultiVendor_CreateProductAttribute_old.this, R.layout.simple_spinner_dropdown_item, spn_label_list);
                            ValuesRequired.setAdapter(adapter2);
                            ArrayAdapter<String> adapter4 = new ArrayAdapter<>(Ced_MultiVendor_CreateProductAttribute_old.this, R.layout.simple_spinner_dropdown_item, spn_label_list);
                            MultiVendor_Add_to_column.setAdapter(adapter4);
                            ArrayAdapter<String> adapter5 = new ArrayAdapter<>(Ced_MultiVendor_CreateProductAttribute_old.this, R.layout.simple_spinner_dropdown_item, spn_label_list);
                            MultiVendor_Use_to_filter.setAdapter(adapter5);
                            ArrayAdapter<String> adapter6 = new ArrayAdapter<>(Ced_MultiVendor_CreateProductAttribute_old.this, R.layout.simple_spinner_dropdown_item, spn_label_list);
                            default_spinner.setAdapter(adapter6);
                            ArrayAdapter<String> adapter7 = new ArrayAdapter<>(Ced_MultiVendor_CreateProductAttribute_old.this, R.layout.simple_spinner_dropdown_item, spn_label_list);
                            Sorting_Productlisting.setAdapter(adapter7);
                            ArrayAdapter<String> adapter8 = new ArrayAdapter<>(Ced_MultiVendor_CreateProductAttribute_old.this, R.layout.simple_spinner_dropdown_item, spn_label_list);
                            isConfigurable.setAdapter(adapter8);
                            ArrayAdapter<String> adapter9 = new ArrayAdapter<>(Ced_MultiVendor_CreateProductAttribute_old.this, R.layout.simple_spinner_dropdown_item, spn_label_list);
                            Front_end.setAdapter(adapter9);
                            ArrayAdapter<String> adapter11 = new ArrayAdapter<>(Ced_MultiVendor_CreateProductAttribute_old.this, R.layout.simple_spinner_dropdown_item, spn_label_list);
                            PromoRule.setAdapter(adapter11);
                            ArrayAdapter<String> adapter13 = new ArrayAdapter<>(Ced_MultiVendor_CreateProductAttribute_old.this, R.layout.simple_spinner_dropdown_item, spn_label_list);
                            HTML_tag.setAdapter(adapter13);
                            ArrayAdapter<String> adapter15 = new ArrayAdapter<>(Ced_MultiVendor_CreateProductAttribute_old.this, R.layout.simple_spinner_dropdown_item, spn_label_list);
                            ProductviewPage.setAdapter(adapter15);
                            ArrayAdapter<String> adapter16 = new ArrayAdapter<>(Ced_MultiVendor_CreateProductAttribute_old.this, R.layout.simple_spinner_dropdown_item, spn_label_list);
                            Productlisting.setAdapter(adapter16);
                        }
                    }
                } else {
                    Intent intent = new Intent(Ced_MultiVendor_CreateProductAttribute_old.this, Ced_MultiVendor_UnAuthourizedRequestError.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    overridePendingTransition(R.anim.ced_multivendor_slide_in, R.anim.ced_multivendor_slide_out);
                }
            }
        }, Ced_MultiVendor_CreateProductAttribute_old.this);
        response.execute(attribute_options_url);
    }
}
