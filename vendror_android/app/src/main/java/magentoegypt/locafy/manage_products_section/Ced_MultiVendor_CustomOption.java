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

package magentoegypt.locafy.manage_products_section;

import android.content.Intent;
import android.os.Bundle;
import androidx.cardview.widget.CardView;
import android.util.Log;
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
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import magentoegypt.locafy.R;
import magentoegypt.locafy.base_app.Ced_MultiVendor_FontSetting;
import magentoegypt.locafy_constant.Ced_MultiVendor_GlobalVariables;
import magentoegypt.locafy.base_app.Ced_MultiVendor_VendorFunctionalityList;
import magentoegypt.locafy.navigation_drawer.Activity.Ced_MultiVendor_NavigationActivity;
import magentoegypt.locafy.api_request_response_section.network_error_handling.Ced_MultiVendor_ConnectionDetector;
import magentoegypt.locafy.api_request_response_section.network_error_handling.Ced_MultiVendor_NoInternetconnection;
import magentoegypt.locafy.api_request_response_section.network_error_handling.Ced_MultiVendor_UnAuthourizedRequestError;
import magentoegypt.locafy.api_request_response_section.Ced_MultiVendor_ClientRequestResponse;
import magentoegypt.locafy.vendor_session.Ced_MultiVendor_VendorSessionManagement;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class Ced_MultiVendor_CustomOption extends Ced_MultiVendor_NavigationActivity {
    private Ced_MultiVendor_FontSetting fontSetting;
    private Ced_MultiVendor_ConnectionDetector connectionDetector;
    private Ced_MultiVendor_VendorFunctionalityList functionalityList;
    private Ced_MultiVendor_VendorSessionManagement vendorSessionManagement;
    private RelativeLayout addcustomoptions;
    private LinearLayout customoptions;
    private ScrollView mainvendorscroll;
    private TextView savecontinue;
    private TextView save;
    private String tabs;
    private String product_id;
    private String type;
    private JSONObject custom_option_data;
    private String updateurl;
    public String Jstring;
    private HashMap<String, String> dataforcustomoptions;
    private HashMap<String, String> datatogetcustomoptions;
    private String attribute_set;
    private String getcustomoptionurl;
    private JSONObject optiondeletehash;
    private JSONObject optiontypedeletehash;
    private String selected_websitetopost;
    private TextView customOptionsNotProvided;
    private ArrayList<String> optionTypeList,priceTypeList;
    private HashMap<String,String> optionTypeMap,pricetypeMap;
    private ArrayAdapter<String> OptionTypeAdapter,pricetypeadapter ;

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
        vendorSessionManagement = new Ced_MultiVendor_VendorSessionManagement(getApplicationContext());
        custom_option_data = new JSONObject();
        dataforcustomoptions = new HashMap<>();
        datatogetcustomoptions = new HashMap<>();
        optiondeletehash = new JSONObject();
        optiontypedeletehash = new JSONObject();

        updateurl = session.getBase_Url() + "vendorapi/vproducts/update";
        getcustomoptionurl = session.getBase_Url()+ "vproductapi/vproducts/customOptionData";

        selected_websitetopost=getIntent().getStringExtra("selectedwebsite");
        dataforcustomoptions.put("websites", selected_websitetopost);

        if (connectionDetector.isConnectingToInternet()) {
            ViewGroup content = findViewById(R.id.MultiVendor_frame_container);
            getLayoutInflater().inflate(R.layout.ced_multivendor_activity_custom_option, content, true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.backbutton);

            addcustomoptions = findViewById(R.id.MultiVendor_addcustomoptions);
            customOptionsNotProvided = findViewById(R.id.customOptionsNotProvided);
            customoptions = findViewById(R.id.MultiVendor_customoptions);
            mainvendorscroll = findViewById(R.id.MultiVendor_mainvendorscroll);
            save = findViewById(R.id.MultiVendor_save);
            savecontinue = findViewById(R.id.MultiVendor_savecontinue);
            fontSetting.setFontforTextviews(save, "Roboto-Medium.ttf", getApplicationContext());
            fontSetting.setFontforTextviews(savecontinue, "Roboto-Medium.ttf", getApplicationContext());

            setname(vendorSessionManagement.getvendorname());
            setprofilepic(vendorSessionManagement.getvendorpic());
            setTitle(getString(R.string.customoptions));
            /**************************************************************/
            tabs = getIntent().getStringExtra("tabs");
            product_id = getIntent().getStringExtra("product_id");
            type = getIntent().getStringExtra("type");
            attribute_set = getIntent().getStringExtra("attribute_set");
            /*************************Edit Section*************************************/

            if (type.equals("bundle")){
                addcustomoptions.setVisibility(View.INVISIBLE);
                customOptionsNotProvided.setVisibility(View.VISIBLE);
            }
            else {
                addcustomoptions.setVisibility(View.VISIBLE);
                customOptionsNotProvided.setVisibility(View.GONE);
            }

            datatogetcustomoptions.put("vendor_id", vendorSessionManagement.getVendorid());
            datatogetcustomoptions.put("hashkey", vendorSessionManagement.getHahkey());
            datatogetcustomoptions.put("product_id", product_id);
            Ced_MultiVendor_ClientRequestResponse crr = new Ced_MultiVendor_ClientRequestResponse(output -> {
                Jstring = output.toString();
                if (functionalityList.getExtensionAddon()) {
                    JSONObject jsonObject = new JSONObject(Jstring);
                    if (jsonObject.has("vendor_approved")) {
                        logout();
                    }
                    else {
                        if (jsonObject.getJSONObject("data").has("option_types")) {
                            JSONArray optionTypes = jsonObject.getJSONObject("data").getJSONArray("option_types");
                            optionTypeMap=new HashMap<>();
                            optionTypeList=new ArrayList<>();
                            optionTypeList.add(getString(R.string.select));
                            for (int i=0;i<optionTypes.length();i++){
                                optionTypeMap.put(optionTypes.getJSONObject(i).getString("label"),optionTypes.getJSONObject(i).getString("code"));
                                optionTypeList.add(optionTypes.getJSONObject(i).getString("label"));
                            }
                            OptionTypeAdapter = new ArrayAdapter<>(Ced_MultiVendor_CustomOption.this, R.layout.spinner_textview, optionTypeList);
                        }

                        if (jsonObject.getJSONObject("data").has("price_type")){
                            JSONArray priceType = jsonObject.getJSONObject("data").getJSONArray("price_type");
                            pricetypeMap=new HashMap<>();
                            priceTypeList=new ArrayList<>();
                            for (int i=0;i<priceType.length();i++){
                                pricetypeMap.put(priceType.getJSONObject(i).getString("label"),priceType.getJSONObject(i).getString("value"));
                                priceTypeList.add(priceType.getJSONObject(i).getString("label"));
                            }
                            pricetypeadapter = new ArrayAdapter<>(Ced_MultiVendor_CustomOption.this, R.layout.spinner_textview, priceTypeList);
                        }

                        String success = jsonObject.getJSONObject("data").getString("success");

                        if (success.equals("true")) {
                            JSONArray custom_option = jsonObject.getJSONObject("data").getJSONArray("custom_option");
                            for (int c = 0; c < custom_option.length(); c++) {
                                final boolean[] firstSelect = {true};
                                JSONObject object = custom_option.getJSONObject(c);
                                String custom_option_type = object.getString("type");
                                if (custom_option_type.equals("field") || custom_option_type.equals("area")) {
                                    View custom_option_layout_row = View.inflate(getApplicationContext(), R.layout.ced_multivendor_custom_option_layout_row, null);
                                    EditText tittle = custom_option_layout_row.findViewById(R.id.MultiVendor_tittle);
                                    EditText sortorder = custom_option_layout_row.findViewById(R.id.MultiVendor_sortorder);
                                    TextView option_id = custom_option_layout_row.findViewById(R.id.MultiVendor_option_id);
                                    final LinearLayout foroptions = custom_option_layout_row.findViewById(R.id.MultiVendor_foroptions);
                                    final RelativeLayout deleteoptions = custom_option_layout_row.findViewById(R.id.MultiVendor_deleteoptions);
                                    deleteoptions.setOnClickListener(v -> {
                                        CardView cardView = (CardView) deleteoptions.getParent().getParent().getParent();
                                        TextView deletedoption_id = (TextView) cardView.getChildAt(1);
                                        try {
                                            optiondeletehash.put(deletedoption_id.getText().toString(), "true");
                                            dataforcustomoptions.put("deletedoptions", optiondeletehash.toString());
                                            dataforcustomoptions.put("websites", selected_websitetopost);
                                            dataforcustomoptions.put("deletedoptiontypes", optiontypedeletehash.toString());
                                        }
                                        catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                        customoptions.removeView(cardView);
                                    });
                                    final Spinner optionTypeSpinner = custom_option_layout_row.findViewById(R.id.MultiVendor_type);
                                    optionTypeSpinner.setAdapter(OptionTypeAdapter);
                                    /********************************************innerlayout************************************************/
                                    final View texttypelayout = View.inflate(getApplicationContext(), R.layout.ced_multivendor_texttypelayout, null);
                                    final Spinner required = custom_option_layout_row.findViewById(R.id.MultiVendor_required);
                                    EditText price = texttypelayout.findViewById(R.id.MultiVendor_Price);
                                    Spinner priceTypeSpinner = texttypelayout.findViewById(R.id.MultiVendor_type);
                                    priceTypeSpinner.setAdapter(pricetypeadapter);
                                    EditText SKU = texttypelayout.findViewById(R.id.MultiVendor_SKU);
                                    EditText maxchar = texttypelayout.findViewById(R.id.MultiVendor_maxchar);
                                    price.setText(object.getString("default_price"));
                                    maxchar.setText(object.getString("max_characters"));
                                    SKU.setText(object.getString("sku"));

                                    if (object.getString("default_price_type").equals("fixed")) {
                                        priceTypeSpinner.setSelection(0);
                                    }
                                    else {
                                        priceTypeSpinner.setSelection(1);
                                    }

                                    foroptions.addView(texttypelayout);

                                    /********************************************************************************************/
                                    if (custom_option_type.equals("field")){
                                        optionTypeSpinner.setSelection(1);
                                    }
                                    else{
                                        optionTypeSpinner.setSelection(2);
                                    }
                                    optionTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                        @Override
                                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                            if (firstSelect[0]) {
                                                firstSelect[0] =false;
                                            }
                                            else {
                                                createOptionValueLayout(optionTypeSpinner, foroptions);
                                            }
                                        }

                                        @Override
                                        public void onNothingSelected(AdapterView<?> parent) {

                                        }
                                    });
                                    tittle.setText(object.getString("default_title"));
                                    option_id.setText(object.getString("option_id"));
                                    sortorder.setText(object.getString("sort_order"));
                                    int is_require = Integer.parseInt(object.getString("is_require"));
                                    required.setSelection(is_require);
                                    customoptions.addView(custom_option_layout_row);
                                    mainvendorscroll.post(() -> mainvendorscroll.fullScroll(ScrollView.FOCUS_DOWN));

                                }
                                else if (custom_option_type.equals("file")) {
                                    View custom_option_layout_row = View.inflate(getApplicationContext(), R.layout.ced_multivendor_custom_option_layout_row, null);
                                    EditText tittle = custom_option_layout_row.findViewById(R.id.MultiVendor_tittle);
                                    EditText sortorder = custom_option_layout_row.findViewById(R.id.MultiVendor_sortorder);
                                    TextView option_id = custom_option_layout_row.findViewById(R.id.MultiVendor_option_id);
                                    final LinearLayout foroptions = custom_option_layout_row.findViewById(R.id.MultiVendor_foroptions);
                                    final RelativeLayout deleteoptions = custom_option_layout_row.findViewById(R.id.MultiVendor_deleteoptions);
                                    deleteoptions.setOnClickListener(v -> {
                                        CardView cardView = (CardView) deleteoptions.getParent().getParent().getParent();
                                        TextView deletedoption_id = (TextView) cardView.getChildAt(1);
                                        try {
                                            optiondeletehash.put(deletedoption_id.getText().toString(), "true");
                                            dataforcustomoptions.put("deletedoptions", optiondeletehash.toString());
                                            dataforcustomoptions.put("deletedoptiontypes", optiontypedeletehash.toString());
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                        customoptions.removeView(cardView);
                                    });
                                    final Spinner optionTypeSpinner = custom_option_layout_row.findViewById(R.id.MultiVendor_type);
                                    optionTypeSpinner.setAdapter(OptionTypeAdapter);
                                    /********************************************innerlayout************************************************/
                                    final View filetypelayout = View.inflate(getApplicationContext(), R.layout.ced_multivendor_filetypelayout, null);
                                    final EditText price = filetypelayout.findViewById(R.id.MultiVendor_Price);
                                    final Spinner priceTypeSpinner = filetypelayout.findViewById(R.id.MultiVendor_type);
                                    priceTypeSpinner.setAdapter(pricetypeadapter);
                                    EditText SKU = filetypelayout.findViewById(R.id.MultiVendor_SKU);
                                    EditText allowfiletype = filetypelayout.findViewById(R.id.MultiVendor_allowfiletype);
                                    EditText length = filetypelayout.findViewById(R.id.MultiVendor_length);
                                    EditText height = filetypelayout.findViewById(R.id.MultiVendor_height);
                                    price.setText(object.getString("default_price"));
                                    allowfiletype.setText(object.getString("file_extension"));
                                    length.setText(object.getString("image_size_x"));
                                    height.setText(object.getString("image_size_y"));
                                    SKU.setText(object.getString("sku"));

                                    if (object.getString("default_price_type").equals("fixed")) {
                                        priceTypeSpinner.setSelection(0);
                                    }
                                    else {
                                        priceTypeSpinner.setSelection(1);
                                    }

                                    foroptions.addView(filetypelayout);

                                    /********************************************************************************************/
                                    if (custom_option_type.equals("file")) {
                                        optionTypeSpinner.setSelection(3);
                                    }
                                    optionTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                        @Override
                                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                            if (firstSelect[0]) {
                                                firstSelect[0] =false;
                                            }
                                            else {
                                                createOptionValueLayout(optionTypeSpinner, foroptions);
                                            }
                                        }
                                        @Override
                                        public void onNothingSelected(AdapterView<?> parent) {
                                        }
                                    });
                                    final Spinner required = custom_option_layout_row.findViewById(R.id.MultiVendor_required);

                                    tittle.setText(object.getString("default_title"));
                                    option_id.setText(object.getString("option_id"));
                                    sortorder.setText(object.getString("sort_order"));
                                    int is_require = Integer.parseInt(object.getString("is_require"));
                                    required.setSelection(is_require);
                                    customoptions.addView(custom_option_layout_row);
                                    mainvendorscroll.post(() -> mainvendorscroll.fullScroll(ScrollView.FOCUS_DOWN));
                                }
                                else if (custom_option_type.equals("drop_down") || custom_option_type.equals("radio") || custom_option_type.equals("checkbox") || custom_option_type.equals("multiple")) {
                                    View custom_option_layout_row = View.inflate(getApplicationContext(), R.layout.ced_multivendor_custom_option_layout_row, null);
                                    EditText tittle = custom_option_layout_row.findViewById(R.id.MultiVendor_tittle);
                                    EditText sortorder = custom_option_layout_row.findViewById(R.id.MultiVendor_sortorder);
                                    TextView option_id = custom_option_layout_row.findViewById(R.id.MultiVendor_option_id);
                                    final LinearLayout foroptions = custom_option_layout_row.findViewById(R.id.MultiVendor_foroptions);
                                    final RelativeLayout deleteoptions = custom_option_layout_row.findViewById(R.id.MultiVendor_deleteoptions);
                                    deleteoptions.setOnClickListener(v -> {
                                        CardView cardView = (CardView) deleteoptions.getParent().getParent().getParent();
                                        TextView deletedoption_id = (TextView) cardView.getChildAt(1);
                                        try {
                                            optiondeletehash.put(deletedoption_id.getText().toString(), "true");
                                            dataforcustomoptions.put("deletedoptions", optiondeletehash.toString());
                                            dataforcustomoptions.put("deletedoptiontypes", optiontypedeletehash.toString());
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                        customoptions.removeView(cardView);
                                    });
                                    final Spinner optionTypeSpinner = custom_option_layout_row.findViewById(R.id.MultiVendor_type);
                                    optionTypeSpinner.setAdapter(OptionTypeAdapter);
                                    /********************************************innerlayout************************************************/
                                    final View selecttypelayout = View.inflate(getApplicationContext(), R.layout.ced_multivendor_selecttypelayout, null);
                                    RelativeLayout addoptions = selecttypelayout.findViewById(R.id.MultiVendor_addoptions);
                                    final LinearLayout options = selecttypelayout.findViewById(R.id.MultiVendor_options);
                                    JSONArray select_type_options = object.getJSONArray("option");
                                    for (int j = 0; j < select_type_options.length(); j++) {
                                        final View selectitem_layout = View.inflate(getApplicationContext(), R.layout.ced_multivendor_selectitem_layout, null);
                                        JSONObject jsonObject1 = select_type_options.getJSONObject(j);
                                        final RelativeLayout delete_options = selectitem_layout.findViewById(R.id.MultiVendor_deleteoptions);
                                        delete_options.setOnClickListener(v -> {
                                            CardView cardView = (CardView) delete_options.getParent().getParent().getParent();
                                            TextView deletedoption_id = (TextView) cardView.getChildAt(1);
                                            try {
                                                optiontypedeletehash.put(deletedoption_id.getText().toString(), "true");
                                                dataforcustomoptions.put("deletedoptions", optiondeletehash.toString());
                                                dataforcustomoptions.put("deletedoptiontypes", optiontypedeletehash.toString());
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                            options.removeView(cardView);
                                        });
                                        EditText select_tittle = selectitem_layout.findViewById(R.id.MultiVendor_tittle);
                                        EditText Price_select = selectitem_layout.findViewById(R.id.MultiVendor_Price);
                                        EditText SKU_select = selectitem_layout.findViewById(R.id.MultiVendor_SKU);
                                        EditText sortorder_select = selectitem_layout.findViewById(R.id.MultiVendor_sortorder);
                                        TextView option_type_id = selectitem_layout.findViewById(R.id.MultiVendor_option_type_id);
                                        Spinner priceTypeSpinner = selectitem_layout.findViewById(R.id.MultiVendor_type);
                                        priceTypeSpinner.setAdapter(pricetypeadapter);
                                        option_type_id.setText(jsonObject1.getString("option_type_id"));
                                        select_tittle.setText(jsonObject1.getString("default_title"));
                                        Price_select.setText(jsonObject1.getString("default_price"));
                                        SKU_select.setText(jsonObject1.getString("sku"));
                                        sortorder_select.setText(jsonObject1.getString("sort_order"));
                                        if (jsonObject1.getString("default_price_type").equals("fixed")) {
                                            priceTypeSpinner.setSelection(0);
                                        }
                                        else {
                                            priceTypeSpinner.setSelection(1);
                                        }
                                        options.addView(selectitem_layout);
                                    }
                                    addoptions.setOnClickListener(v -> {
                                        final View selectitem_layout = View.inflate(getApplicationContext(), R.layout.ced_multivendor_selectitem_layout, null);
                                        final RelativeLayout deleteoptions17 = selectitem_layout.findViewById(R.id.MultiVendor_deleteoptions);
                                        deleteoptions17.setOnClickListener(v110 -> {
                                            CardView cardView = (CardView) deleteoptions17.getParent().getParent().getParent();
                                            TextView deletedoption_id = (TextView) cardView.getChildAt(1);
                                            try {
                                                optiontypedeletehash.put(deletedoption_id.getText().toString(), "true");
                                                dataforcustomoptions.put("deletedoptions", optiondeletehash.toString());
                                                dataforcustomoptions.put("deletedoptiontypes", optiontypedeletehash.toString());
                                            }
                                            catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                            options.removeView(cardView);
                                        });
                                        options.addView(selectitem_layout);
                                    });
                                    foroptions.addView(selecttypelayout);
                                    /********************************************************************************************/

                                    if (custom_option_type.equals("drop_down")) {
                                        optionTypeSpinner.setSelection(4);
                                    }
                                    else if (custom_option_type.equals("radio")) {
                                        optionTypeSpinner.setSelection(5);
                                    }
                                    else if (custom_option_type.equals("checkbox")) {
                                        optionTypeSpinner.setSelection(6);
                                    }
                                    else if (custom_option_type.equals("multiple")) {
                                        optionTypeSpinner.setSelection(7);
                                    }

                                    optionTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                        @Override
                                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                            if (firstSelect[0]) {
                                                firstSelect[0] =false;
                                            }
                                            else {
                                                createOptionValueLayout(optionTypeSpinner, foroptions);
                                            }
                                        }

                                        @Override
                                        public void onNothingSelected(AdapterView<?> parent) {

                                        }
                                    });
                                    final Spinner required = custom_option_layout_row.findViewById(R.id.MultiVendor_required);

                                    option_id.setText(object.getString("option_id"));
                                    tittle.setText(object.getString("default_title"));
                                    sortorder.setText(object.getString("sort_order"));

                                    int is_require = Integer.parseInt(object.getString("is_require"));
                                    required.setSelection(is_require);

                                    customoptions.addView(custom_option_layout_row);
                                    mainvendorscroll.post(() -> mainvendorscroll.fullScroll(ScrollView.FOCUS_DOWN));

                                }
                                else if (custom_option_type.equals("date") || custom_option_type.equals("date_time") || custom_option_type.equals("time")) {
                                    View custom_option_layout_row = View.inflate(getApplicationContext(), R.layout.ced_multivendor_custom_option_layout_row, null);
                                    EditText tittle = custom_option_layout_row.findViewById(R.id.MultiVendor_tittle);
                                    EditText sortorder = custom_option_layout_row.findViewById(R.id.MultiVendor_sortorder);
                                    TextView option_id = custom_option_layout_row.findViewById(R.id.MultiVendor_option_id);
                                    final LinearLayout foroptions = custom_option_layout_row.findViewById(R.id.MultiVendor_foroptions);
                                    final RelativeLayout deleteoptions = custom_option_layout_row.findViewById(R.id.MultiVendor_deleteoptions);
                                    deleteoptions.setOnClickListener(v -> {
                                        CardView cardView = (CardView) deleteoptions.getParent().getParent().getParent();
                                        TextView deletedoption_id = (TextView) cardView.getChildAt(1);
                                        try {
                                            optiondeletehash.put(deletedoption_id.getText().toString(), "true");
                                            dataforcustomoptions.put("deletedoptions", optiondeletehash.toString());
                                            dataforcustomoptions.put("deletedoptiontypes", optiontypedeletehash.toString());
                                        }
                                        catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                        customoptions.removeView(cardView);
                                    });
                                    final Spinner optionTypeSpinner = custom_option_layout_row.findViewById(R.id.MultiVendor_type);
                                    optionTypeSpinner.setAdapter(OptionTypeAdapter);
                                    /********************************************innerlayout************************************************/
                                    final View datetyprlayout = View.inflate(getApplicationContext(), R.layout.ced_multivendor_datetyprlayout, null);
                                    final EditText price = datetyprlayout.findViewById(R.id.MultiVendor_Price);
                                    final Spinner text_type = datetyprlayout.findViewById(R.id.MultiVendor_type);
                                    EditText SKU = datetyprlayout.findViewById(R.id.MultiVendor_SKU);
                                    price.setText(object.getString("default_price"));
                                    SKU.setText(object.getString("sku"));
                                    if (object.getString("default_price_type").equals("fixed")) {
                                        text_type.setSelection(0);
                                    }
                                    else {
                                        text_type.setSelection(1);
                                    }
                                    foroptions.addView(datetyprlayout);
                                    /********************************************************************************************/
                                    if (custom_option_type.equals("date")) {
                                        optionTypeSpinner.setSelection(8);
                                    }
                                    if (custom_option_type.equals("date_time")) {
                                        optionTypeSpinner.setSelection(9);
                                    }
                                    if (custom_option_type.equals("time")) {
                                        optionTypeSpinner.setSelection(10);
                                    }

                                    optionTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                        @Override
                                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                            if (firstSelect[0]) {
                                                firstSelect[0] =false;
                                            }
                                            else {
                                                createOptionValueLayout(optionTypeSpinner, foroptions);
                                            }
                                        }
                                        @Override
                                        public void onNothingSelected(AdapterView<?> parent) {
                                        }
                                    });
                                    final Spinner required = custom_option_layout_row.findViewById(R.id.MultiVendor_required);

                                    option_id.setText(object.getString("option_id"));
                                    tittle.setText(object.getString("default_title"));
                                    sortorder.setText(object.getString("sort_order"));
                                    int is_require = Integer.parseInt(object.getString("is_require"));
                                    required.setSelection(is_require);

                                    customoptions.addView(custom_option_layout_row);
                                    mainvendorscroll.post(() -> mainvendorscroll.fullScroll(ScrollView.FOCUS_DOWN));

                                }
                                else {
                                    Log.i("REpo", "onCreate_843: "+custom_option_type);
                                }
                            }
                        }
                    }
                }
                else {
                    Intent intent = new Intent(getApplicationContext(), Ced_MultiVendor_UnAuthourizedRequestError.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    overridePendingTransition(R.anim.ced_multivendor_slide_in, R.anim.ced_multivendor_slide_out);
                }
            }, Ced_MultiVendor_CustomOption.this, "POST", datatogetcustomoptions);
            crr.execute(getcustomoptionurl);
            /**************************************************************/

            dataforcustomoptions.put("product_id", product_id);
            dataforcustomoptions.put("websites", selected_websitetopost);
            dataforcustomoptions.put("vendor_id", vendorSessionManagement.getVendorid());
            dataforcustomoptions.put("hashkey", vendorSessionManagement.getHahkey());

            addcustomoptions.setOnClickListener(v -> {
                View custom_option_layout_row = View.inflate(getApplicationContext(), R.layout.ced_multivendor_custom_option_layout_row, null);
                final LinearLayout foroptions = custom_option_layout_row.findViewById(R.id.MultiVendor_foroptions);
                final RelativeLayout deleteoptions = custom_option_layout_row.findViewById(R.id.MultiVendor_deleteoptions);
                deleteoptions.setOnClickListener(v15 -> {
                    CardView cardView = (CardView) deleteoptions.getParent().getParent().getParent();
                    TextView deletedoption_id = (TextView) cardView.getChildAt(1);
                    try {
                        optiondeletehash.put(deletedoption_id.getText().toString(), "true");
                        dataforcustomoptions.put("deletedoptions", optiondeletehash.toString());
                        dataforcustomoptions.put("deletedoptiontypes", optiontypedeletehash.toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    customoptions.removeView(cardView);
                });
                final Spinner optionTypeSpinner = custom_option_layout_row.findViewById(R.id.MultiVendor_type);
                optionTypeSpinner.setAdapter(OptionTypeAdapter);
                optionTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        createOptionValueLayout(optionTypeSpinner,foroptions);
                    }
                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                });
                customoptions.addView(custom_option_layout_row);
                mainvendorscroll.post(() -> mainvendorscroll.fullScroll(ScrollView.FOCUS_DOWN));
            });
            save.setOnClickListener(v -> {
                try {
                    int count = customoptions.getChildCount();
                    JSONObject data_options = new JSONObject();
                    for (int j = 0; j < count; j++) {
                        JSONObject object = new JSONObject();
                        CardView options = (CardView) customoptions.getChildAt(j);
                        LinearLayout layout = (LinearLayout) options.getChildAt(0);
                        TextView option_id = (TextView) options.getChildAt(1);
                        LinearLayout for_options = (LinearLayout) layout.getChildAt(1);
                        LinearLayout layout2 = (LinearLayout) layout.getChildAt(0);
                        LinearLayout layout3 = (LinearLayout) layout2.getChildAt(2);
                        LinearLayout layout4 = (LinearLayout) layout2.getChildAt(1);
                        LinearLayout layout5 = (LinearLayout) layout2.getChildAt(3);
                        LinearLayout layout6 = (LinearLayout) layout2.getChildAt(4);
                        Spinner type = (Spinner) layout3.getChildAt(1);
                        Spinner isrequired = (Spinner) layout5.getChildAt(1);
                        EditText tittle = (EditText) layout4.getChildAt(1);
                        EditText sortorder = (EditText) layout6.getChildAt(1);
                        /****************************creating data***********************/
                        object.put("is_delete", "");
                        object.put("title", tittle.getText().toString());
                        object.put("type", getItemfromEntry(type.getSelectedItem().toString()));
                        object.put("is_require", isrequired.getSelectedItemPosition());
                        object.put("sort_order", sortorder.getText().toString());
                        /***************************************************/

                        if (getItemfromEntry(type.getSelectedItem().toString()).equals("field") || getItemfromEntry(type.getSelectedItem().toString()).equals("area")) {
                            CardView view = (CardView) for_options.getChildAt(0);
                            LinearLayout text_layout = (LinearLayout) view.getChildAt(0);
                            LinearLayout text_linear_layout = (LinearLayout) text_layout.getChildAt(0);
                            LinearLayout price_linear_layout = (LinearLayout) text_linear_layout.getChildAt(0);
                            EditText price_edittext = (EditText) price_linear_layout.getChildAt(1);
                            LinearLayout pricetype_linear_layout = (LinearLayout) text_linear_layout.getChildAt(1);
                            RelativeLayout pricetype_layout = (RelativeLayout) pricetype_linear_layout.getChildAt(1);
                            Spinner pricetype_spinner = (Spinner) pricetype_layout.getChildAt(0);
                            pricetype_spinner.setAdapter(pricetypeadapter);
                            LinearLayout sku_linear_layout = (LinearLayout) text_linear_layout.getChildAt(2);
                            EditText sku_edittext = (EditText) sku_linear_layout.getChildAt(1);
                            LinearLayout maxchar_linear_layout = (LinearLayout) text_linear_layout.getChildAt(3);
                            EditText maxchar_edittext = (EditText) maxchar_linear_layout.getChildAt(1);
                            object.put("price", price_edittext.getText().toString());
                            object.put("price_type", pricetypeMap.get(pricetype_spinner.getSelectedItem()));
                            object.put("sku", sku_edittext.getText().toString());
                            object.put("max_characters", maxchar_edittext.getText().toString());
                        }
                        if (getItemfromEntry(type.getSelectedItem().toString()).equals("file")) {
                            CardView view = (CardView) for_options.getChildAt(0);
                            LinearLayout text_layout = (LinearLayout) view.getChildAt(0);
                            LinearLayout text_linear_layout = (LinearLayout) text_layout.getChildAt(0);
                            LinearLayout price_linear_layout = (LinearLayout) text_linear_layout.getChildAt(0);
                            EditText price_edittext = (EditText) price_linear_layout.getChildAt(1);
                            LinearLayout pricetype_linear_layout = (LinearLayout) text_linear_layout.getChildAt(1);
                            Spinner pricetype_spinner = (Spinner) pricetype_linear_layout.getChildAt(1);
                            pricetype_spinner.setAdapter(pricetypeadapter);
                            LinearLayout sku_linear_layout = (LinearLayout) text_linear_layout.getChildAt(2);
                            EditText sku_edittext = (EditText) sku_linear_layout.getChildAt(1);
                            LinearLayout allowfiletype_linear_layout = (LinearLayout) text_linear_layout.getChildAt(3);
                            EditText allowfiletype_edittext = (EditText) allowfiletype_linear_layout.getChildAt(1);
                            LinearLayout maximage_linear_layout = (LinearLayout) text_linear_layout.getChildAt(4);
                            EditText height = (EditText) maximage_linear_layout.getChildAt(1);
                            EditText width = (EditText) maximage_linear_layout.getChildAt(2);
                            object.put("price", price_edittext.getText().toString());
                            object.put("price_type", pricetypeMap.get(pricetype_spinner.getSelectedItem()));
                            object.put("sku", sku_edittext.getText().toString());
                            object.put("file_extension", allowfiletype_edittext.getText().toString());
                            object.put("image_size_x", height.getText().toString());
                            object.put("image_size_y", width.getText().toString());
                        }
                        if (getItemfromEntry(type.getSelectedItem().toString()).equals("date") || getItemfromEntry(type.getSelectedItem().toString()).equals("date_time") || getItemfromEntry(type.getSelectedItem().toString()).equals("time")) {
                            CardView view = (CardView) for_options.getChildAt(0);
                            LinearLayout text_layout = (LinearLayout) view.getChildAt(0);
                            LinearLayout text_linear_layout = (LinearLayout) text_layout.getChildAt(0);
                            LinearLayout price_linear_layout = (LinearLayout) text_linear_layout.getChildAt(0);
                            EditText price_edittext = (EditText) price_linear_layout.getChildAt(1);
                            LinearLayout pricetype_linear_layout = (LinearLayout) text_linear_layout.getChildAt(1);
                            Spinner pricetype_spinner = (Spinner) pricetype_linear_layout.getChildAt(1);
                            pricetype_spinner.setAdapter(pricetypeadapter);
                            LinearLayout sku_linear_layout = (LinearLayout) text_linear_layout.getChildAt(2);
                            EditText sku_edittext = (EditText) sku_linear_layout.getChildAt(1);
                            object.put("price", price_edittext.getText().toString());
                            object.put("price_type", pricetypeMap.get(pricetype_spinner.getSelectedItem()));
                            object.put("sku", sku_edittext.getText().toString());
                        }
                        if (getItemfromEntry(type.getSelectedItem().toString()).equals("drop_down") || getItemfromEntry(type.getSelectedItem().toString()).equals("radio") || getItemfromEntry(type.getSelectedItem().toString()).equals("checkbox") || getItemfromEntry(type.getSelectedItem().toString()).equals("multiple")) {
                            ScrollView view = (ScrollView) for_options.getChildAt(0);
                            RelativeLayout relativeLayout = (RelativeLayout) view.getChildAt(0);
                            LinearLayout select_options = (LinearLayout) relativeLayout.getChildAt(0);
                            JSONArray array = new JSONArray();
                            JSONObject object1 = new JSONObject();
                            for (int k = 0; k < select_options.getChildCount(); k++) {
                                JSONObject object2 = new JSONObject();
                                CardView optionview = (CardView) select_options.getChildAt(k);
                                LinearLayout text_layout = (LinearLayout) optionview.getChildAt(0);
                                TextView option_type_id = (TextView) optionview.getChildAt(1);
                                object2.put("is_delete", "");
                                LinearLayout text_linear_layout = (LinearLayout) text_layout.getChildAt(0);
                                LinearLayout tittle_linear_layout = (LinearLayout) text_linear_layout.getChildAt(1);
                                EditText tittle_edittext = (EditText) tittle_linear_layout.getChildAt(1);
                                object2.put("title", tittle_edittext.getText().toString());
                                LinearLayout price_linear_layout = (LinearLayout) text_linear_layout.getChildAt(2);
                                EditText price_spinner = (EditText) price_linear_layout.getChildAt(1);
                                object2.put("price", price_spinner.getText().toString());
                                LinearLayout pricetype_linear_layout = (LinearLayout) text_linear_layout.getChildAt(3);
                                Spinner price_type_spinner = (Spinner) pricetype_linear_layout.getChildAt(1);
                                object2.put("price_type", pricetypeMap.get(price_type_spinner.getSelectedItem().toString()));
                                LinearLayout sku_linear_layout = (LinearLayout) text_linear_layout.getChildAt(4);
                                EditText sku_edittext = (EditText) sku_linear_layout.getChildAt(1);
                                object2.put("sku", sku_edittext.getText().toString());
                                LinearLayout sortorder_linear_layout = (LinearLayout) text_linear_layout.getChildAt(5);
                                EditText sortorder_edittext = (EditText) sortorder_linear_layout.getChildAt(1);
                                object2.put("sort_order", sortorder_edittext.getText().toString());
                                object1.put(String.valueOf(k), object2);
                            }
                            object.put("values", object1);
                        }
                        data_options.put(String.valueOf(j), object);

                    }
                    custom_option_data.put("options", data_options);
                    dataforcustomoptions.put("custom_options", custom_option_data.toString());
                    Ced_MultiVendor_ClientRequestResponse crr12 = new Ced_MultiVendor_ClientRequestResponse(output -> {
                        Jstring = output.toString();
                        if (functionalityList.getExtensionAddon()) {
                            JSONObject jsonObject = new JSONObject(Jstring);
                            if (jsonObject.has("vendor_approved")) {
                                logout();
                            }
                            else {
                                String success = jsonObject.getJSONObject("data").getString("success");
                                if (success.equals("true")) {
                                    Ced_MultiVendor_GlobalVariables globalVariables = new Ced_MultiVendor_GlobalVariables();
                                    globalVariables.clearallvalues();
                                    Intent related = new Intent(getApplicationContext(), Ced_MultiVendor_ManageProducts.class);
                                    related.putExtra("Navigation", "productcreate");
                                    startActivity(related);
                                    overridePendingTransition(R.anim.ced_multivendor_slide_in_sequence, R.anim.ced_multivendor_slide_out_sequence);
                                }
                                else {
                                    Toast.makeText(getApplicationContext(), jsonObject.getJSONObject("data").getString("message"), Toast.LENGTH_LONG).show();
                                }
                            }
                        }
                        else {
                            Intent intent = new Intent(getApplicationContext(), Ced_MultiVendor_UnAuthourizedRequestError.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            overridePendingTransition(R.anim.ced_multivendor_slide_in, R.anim.ced_multivendor_slide_out);
                        }


                    }, Ced_MultiVendor_CustomOption.this, "POST", dataforcustomoptions);
                    crr12.execute(updateurl);
                }
                catch (JSONException e) {
                    e.printStackTrace();
                }
            });
            savecontinue.setOnClickListener(v -> {
                try {
                    int count = customoptions.getChildCount();
                    JSONObject data_options = new JSONObject();
                    for (int j = 0; j < count; j++) {
                        JSONObject object = new JSONObject();
                        CardView options = (CardView) customoptions.getChildAt(j);
                        LinearLayout layout = (LinearLayout) options.getChildAt(0);
                        TextView option_id = (TextView) options.getChildAt(1);
                        LinearLayout for_options = (LinearLayout) layout.getChildAt(1);
                        LinearLayout layout2 = (LinearLayout) layout.getChildAt(0);
                        LinearLayout layout3 = (LinearLayout) layout2.getChildAt(2);
                        LinearLayout layout4 = (LinearLayout) layout2.getChildAt(1);
                        LinearLayout layout5 = (LinearLayout) layout2.getChildAt(3);
                        LinearLayout layout6 = (LinearLayout) layout2.getChildAt(4);
                        Spinner type = (Spinner) layout3.getChildAt(1);
                        Spinner isrequired = (Spinner) layout5.getChildAt(1);
                        EditText tittle = (EditText) layout4.getChildAt(1);
                        EditText sortorder = (EditText) layout6.getChildAt(1);
                        /****************************creating data***********************/
                        object.put("is_delete", "");
                        object.put("title", tittle.getText().toString());
                        object.put("type",  getItemfromEntry(type.getSelectedItem().toString()));
                        object.put("is_require", isrequired.getSelectedItemPosition());
                        object.put("sort_order", sortorder.getText().toString());
                        /***************************************************/
                        if (getItemfromEntry(type.getSelectedItem().toString()).equals("field") || getItemfromEntry(type.getSelectedItem().toString()).equals("area")) {
                            CardView view = (CardView) for_options.getChildAt(0);
                            LinearLayout text_layout = (LinearLayout) view.getChildAt(0);
                            LinearLayout text_linear_layout = (LinearLayout) text_layout.getChildAt(0);
                            LinearLayout price_linear_layout = (LinearLayout) text_linear_layout.getChildAt(0);
                            EditText price_edittext = (EditText) price_linear_layout.getChildAt(1);
                            LinearLayout pricetype_linear_layout = (LinearLayout) text_linear_layout.getChildAt(1);
                            RelativeLayout pricetype_layout = (RelativeLayout) pricetype_linear_layout.getChildAt(1);
                            Spinner pricetype_spinner = (Spinner) pricetype_layout.getChildAt(0);
                            pricetype_spinner.setAdapter(pricetypeadapter);
                            LinearLayout sku_linear_layout = (LinearLayout) text_linear_layout.getChildAt(2);
                            EditText sku_edittext = (EditText) sku_linear_layout.getChildAt(1);
                            LinearLayout maxchar_linear_layout = (LinearLayout) text_linear_layout.getChildAt(3);
                            EditText maxchar_edittext = (EditText) maxchar_linear_layout.getChildAt(1);
                            object.put("price", price_edittext.getText().toString());
                            object.put("price_type", pricetypeMap.get(pricetype_spinner.getSelectedItem()));
                            object.put("sku", sku_edittext.getText().toString());
                            object.put("max_characters", maxchar_edittext.getText().toString());
                        }
                        if (getItemfromEntry(type.getSelectedItem().toString()).equals("file")) {
                            CardView view = (CardView) for_options.getChildAt(0);
                            LinearLayout text_layout = (LinearLayout) view.getChildAt(0);
                            LinearLayout text_linear_layout = (LinearLayout) text_layout.getChildAt(0);
                            LinearLayout price_linear_layout = (LinearLayout) text_linear_layout.getChildAt(0);
                            EditText price_edittext = (EditText) price_linear_layout.getChildAt(1);
                            LinearLayout pricetype_linear_layout = (LinearLayout) text_linear_layout.getChildAt(1);
                            Spinner pricetype_spinner = (Spinner) pricetype_linear_layout.getChildAt(1);
                            pricetype_spinner.setAdapter(pricetypeadapter);
                            LinearLayout sku_linear_layout = (LinearLayout) text_linear_layout.getChildAt(2);
                            EditText sku_edittext = (EditText) sku_linear_layout.getChildAt(1);
                            LinearLayout allowfiletype_linear_layout = (LinearLayout) text_linear_layout.getChildAt(3);
                            EditText allowfiletype_edittext = (EditText) allowfiletype_linear_layout.getChildAt(1);
                            LinearLayout maximage_linear_layout = (LinearLayout) text_linear_layout.getChildAt(4);
                            EditText height = (EditText) maximage_linear_layout.getChildAt(1);
                            EditText width = (EditText) maximage_linear_layout.getChildAt(2);
                            object.put("price", price_edittext.getText().toString());
                            object.put("price_type", pricetypeMap.get(pricetype_spinner.getSelectedItem()));
                            object.put("sku", sku_edittext.getText().toString());
                            object.put("file_extension", allowfiletype_edittext.getText().toString());
                            object.put("image_size_x", height.getText().toString());
                            object.put("image_size_y", width.getText().toString());
                        }
                        if (getItemfromEntry(type.getSelectedItem().toString()).equals("date") || getItemfromEntry(type.getSelectedItem().toString()).equals("date_time") || getItemfromEntry(type.getSelectedItem().toString()).equals("time")) {
                            CardView view = (CardView) for_options.getChildAt(0);
                            LinearLayout text_layout = (LinearLayout) view.getChildAt(0);
                            LinearLayout text_linear_layout = (LinearLayout) text_layout.getChildAt(0);
                            LinearLayout price_linear_layout = (LinearLayout) text_linear_layout.getChildAt(0);
                            EditText price_edittext = (EditText) price_linear_layout.getChildAt(1);
                            LinearLayout pricetype_linear_layout = (LinearLayout) text_linear_layout.getChildAt(1);
                            Spinner pricetype_spinner = (Spinner) pricetype_linear_layout.getChildAt(1);
                            pricetype_spinner.setAdapter(pricetypeadapter);
                            LinearLayout sku_linear_layout = (LinearLayout) text_linear_layout.getChildAt(2);
                            EditText sku_edittext = (EditText) sku_linear_layout.getChildAt(1);
                            object.put("price", price_edittext.getText().toString());
                            object.put("price_type", pricetypeMap.get(pricetype_spinner.getSelectedItem()));
                            object.put("sku", sku_edittext.getText().toString());
                        }
                        if (getItemfromEntry(type.getSelectedItem().toString()).equals("drop_down") || getItemfromEntry(type.getSelectedItem().toString()).equals("radio") || getItemfromEntry(type.getSelectedItem().toString()).equals("checkbox") || getItemfromEntry(type.getSelectedItem().toString()).equals("multiple")) {
                            ScrollView view = (ScrollView) for_options.getChildAt(0);
                            RelativeLayout relativeLayout = (RelativeLayout) view.getChildAt(0);
                            LinearLayout select_options = (LinearLayout) relativeLayout.getChildAt(0);
                            JSONArray array = new JSONArray();
                            JSONObject object1 = new JSONObject();
                            for (int k = 0; k < select_options.getChildCount(); k++) {
                                JSONObject object2 = new JSONObject();
                                CardView optionview = (CardView) select_options.getChildAt(k);
                                LinearLayout text_layout = (LinearLayout) optionview.getChildAt(0);
                                TextView option_type_id = (TextView) optionview.getChildAt(1);
                                object2.put("is_delete", "");
                                LinearLayout text_linear_layout = (LinearLayout) text_layout.getChildAt(0);
                                LinearLayout tittle_linear_layout = (LinearLayout) text_linear_layout.getChildAt(1);
                                EditText tittle_edittext = (EditText) tittle_linear_layout.getChildAt(1);
                                object2.put("title", tittle_edittext.getText().toString());
                                LinearLayout price_linear_layout = (LinearLayout) text_linear_layout.getChildAt(2);
                                EditText price_spinner = (EditText) price_linear_layout.getChildAt(1);
                                object2.put("price", price_spinner.getText().toString());
                                LinearLayout pricetype_linear_layout = (LinearLayout) text_linear_layout.getChildAt(3);
                                Spinner price_type_spinner = (Spinner) pricetype_linear_layout.getChildAt(1);
                                object2.put("price_type", pricetypeMap.get(price_type_spinner.getSelectedItem()));
                                LinearLayout sku_linear_layout = (LinearLayout) text_linear_layout.getChildAt(4);
                                EditText sku_edittext = (EditText) sku_linear_layout.getChildAt(1);
                                object2.put("sku", sku_edittext.getText().toString());
                                LinearLayout sortorder_linear_layout = (LinearLayout) text_linear_layout.getChildAt(5);
                                EditText sortorder_edittext = (EditText) sortorder_linear_layout.getChildAt(1);
                                object2.put("sort_order", sortorder_edittext.getText().toString());
                                object1.put(String.valueOf(k), object2);
                            }
                            object.put("values", object1);
                        }
                        data_options.put(String.valueOf(j), object);
                    }

                    custom_option_data.put("options", data_options);
                    dataforcustomoptions.put("custom_options", custom_option_data.toString());
                    Ced_MultiVendor_ClientRequestResponse crr1 = new Ced_MultiVendor_ClientRequestResponse(output -> {
                        Jstring = output.toString();
                        if (functionalityList.getExtensionAddon()) {
                            JSONObject jsonObject = new JSONObject(Jstring);
                            if (jsonObject.has("vendor_approved")) {
                                logout();
                            } else {
                                String success = jsonObject.getJSONObject("data").getString("success");
                                if (success.equals("true")) {
                                    if (type.equals("configurable")) {
                                        Intent related = new Intent(getApplicationContext(), Ced_MultiVendor_Configurable.class);
                                        related.putExtra("tabs", tabs.toString());
                                        related.putExtra("product_id", product_id);
                                        related.putExtra("selectedwebsite", selected_websitetopost);
                                        related.putExtra("type", type);
                                        related.putExtra("attribute_set", attribute_set);
                                        startActivity(related);
                                        overridePendingTransition(R.anim.ced_multivendor_slide_in_sequence, R.anim.ced_multivendor_slide_out_sequence);
                                    }
                                    if (type.equals("downloadable")) {
                                        Intent related = new Intent(getApplicationContext(), Ced_MultiVendor_Downloadable.class);
                                        related.putExtra("tabs", tabs.toString());
                                        related.putExtra("product_id", product_id);
                                        related.putExtra("selectedwebsite", selected_websitetopost);
                                        related.putExtra("type", type);
                                        related.putExtra("attribute_set", attribute_set);
                                        startActivity(related);
                                        overridePendingTransition(R.anim.ced_multivendor_slide_in_sequence, R.anim.ced_multivendor_slide_out_sequence);
                                    }
                                    if (type.equals("bundle")) {
                                        Intent related = new Intent(getApplicationContext(), Ced_MultiVendor_BundleItems.class);
                                        related.putExtra("tabs", tabs.toString());
                                        related.putExtra("product_id", product_id);
                                        related.putExtra("selectedwebsite", selected_websitetopost);
                                        related.putExtra("type", type);
                                        related.putExtra("attribute_set", attribute_set);
                                        startActivity(related);
                                        overridePendingTransition(R.anim.ced_multivendor_slide_in_sequence, R.anim.ced_multivendor_slide_out_sequence);
                                    }
                                    if (type.equals("grouped")) {
                                        Intent related = new Intent(getApplicationContext(), Ced_MultiVendor_GroupItems.class);
                                        related.putExtra("tabs", tabs.toString());
                                        related.putExtra("product_id", product_id);
                                        related.putExtra("selectedwebsite", selected_websitetopost);
                                        related.putExtra("type", type);
                                        related.putExtra("attribute_set", attribute_set);
                                        startActivity(related);
                                        overridePendingTransition(R.anim.ced_multivendor_slide_in_sequence, R.anim.ced_multivendor_slide_out_sequence);
                                    }
                                    if (type.equals("simple")) {
                                        Ced_MultiVendor_GlobalVariables globalVariables = new Ced_MultiVendor_GlobalVariables();
                                        globalVariables.clearallvalues();
                                        Intent related = new Intent(getApplicationContext(), Ced_MultiVendor_ManageProducts.class);
                                        related.putExtra("Navigation", "productcreate");
                                        startActivity(related);
                                        overridePendingTransition(R.anim.ced_multivendor_slide_in_sequence, R.anim.ced_multivendor_slide_out_sequence);
                                    }
                                    if (type.equals("virtual")) {
                                        Ced_MultiVendor_GlobalVariables globalVariables = new Ced_MultiVendor_GlobalVariables();
                                        globalVariables.clearallvalues();
                                        Intent related = new Intent(getApplicationContext(), Ced_MultiVendor_ManageProducts.class);
                                        related.putExtra("Navigation", "productcreate");
                                        startActivity(related);
                                        overridePendingTransition(R.anim.ced_multivendor_slide_in_sequence, R.anim.ced_multivendor_slide_out_sequence);
                                    }
                                } else {
                                    Toast.makeText(getApplicationContext(), jsonObject.getJSONObject("data").getString("message"), Toast.LENGTH_LONG).show();
                                }
                            }
                        } else {
                            Intent intent = new Intent(getApplicationContext(), Ced_MultiVendor_UnAuthourizedRequestError.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            overridePendingTransition(R.anim.ced_multivendor_slide_in, R.anim.ced_multivendor_slide_out);
                        }


                    }, Ced_MultiVendor_CustomOption.this, "POST", dataforcustomoptions);
                    crr1.execute(updateurl);

                }
                catch (JSONException e) {
                    e.printStackTrace();
                }
            });

        }
        else {
            Intent nointernet = new Intent(Ced_MultiVendor_CustomOption.this, Ced_MultiVendor_NoInternetconnection.class);
            nointernet.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            nointernet.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(nointernet);
            overridePendingTransition(R.anim.ced_multivendor_slide_in, R.anim.ced_multivendor_slide_out);
        }

    }

    private void createOptionValueLayout(Spinner optionType, LinearLayout foroptions) {
        if (getItemfromEntry(optionType.getSelectedItem().toString()).equals("field") || getItemfromEntry(optionType.getSelectedItem().toString()).equals("area")) {
            if (foroptions.getChildCount() > 0) {
                foroptions.removeAllViews();
                View texttypelayout = View.inflate(getApplicationContext(), R.layout.ced_multivendor_texttypelayout, null);
                Spinner priceType=texttypelayout.findViewById(R.id.MultiVendor_type);
                priceType.setAdapter(pricetypeadapter);
                foroptions.addView(texttypelayout);
            }
            else {
                View texttypelayout = View.inflate(getApplicationContext(), R.layout.ced_multivendor_texttypelayout, null);
                Spinner priceType=texttypelayout.findViewById(R.id.MultiVendor_type);
                priceType.setAdapter(pricetypeadapter);
                foroptions.addView(texttypelayout);
            }
        }
        else if (getItemfromEntry(optionType.getSelectedItem().toString()).equals("file")) {
            if (foroptions.getChildCount() > 0) {
                foroptions.removeAllViews();
                View filetypelayout = View.inflate(getApplicationContext(), R.layout.ced_multivendor_filetypelayout, null);
                Spinner priceType=filetypelayout.findViewById(R.id.MultiVendor_type);
                priceType.setAdapter(pricetypeadapter);
                foroptions.addView(filetypelayout);
            }
            else {
                View filetypelayout = View.inflate(getApplicationContext(), R.layout.ced_multivendor_filetypelayout, null);
                Spinner priceType=filetypelayout.findViewById(R.id.MultiVendor_type);
                priceType.setAdapter(pricetypeadapter);
                foroptions.addView(filetypelayout);
            }
        }
        else if (getItemfromEntry(optionType.getSelectedItem().toString()).equals("drop_down") || getItemfromEntry(optionType.getSelectedItem().toString()).equals("radio") || getItemfromEntry(optionType.getSelectedItem().toString()).equals("checkbox") || getItemfromEntry(optionType.getSelectedItem().toString()).equals("multiple")) {
            if (foroptions.getChildCount() > 0) {
                foroptions.removeAllViews();
                View selecttypelayout = View.inflate(getApplicationContext(), R.layout.ced_multivendor_selecttypelayout, null);
                RelativeLayout addoptions = selecttypelayout.findViewById(R.id.MultiVendor_addoptions);
                final LinearLayout options = selecttypelayout.findViewById(R.id.MultiVendor_options);
                addoptions.setOnClickListener(v14 -> {
                    View selectitem_layout = View.inflate(getApplicationContext(), R.layout.ced_multivendor_selectitem_layout, null);
                    Spinner priceType=selectitem_layout.findViewById(R.id.MultiVendor_type);
                    priceType.setAdapter(pricetypeadapter);
                    final RelativeLayout deleteoptions12 = selectitem_layout.findViewById(R.id.MultiVendor_deleteoptions);
                    deleteoptions12.setOnClickListener(v13 -> {
                        CardView cardView = (CardView) deleteoptions12.getParent().getParent().getParent();
                        TextView deletedoption_id = (TextView) cardView.getChildAt(1);
                        try {
                            optiontypedeletehash.put(deletedoption_id.getText().toString(), "true");
                            dataforcustomoptions.put("deletedoptions", optiondeletehash.toString());
                            dataforcustomoptions.put("deletedoptiontypes", optiontypedeletehash.toString());
                        }
                        catch (JSONException e) {
                            e.printStackTrace();
                        }
                        options.removeView(cardView);
                    });
                    options.addView(selectitem_layout);

                });
                foroptions.addView(selecttypelayout);
                mainvendorscroll.post(() -> mainvendorscroll.fullScroll(ScrollView.FOCUS_DOWN));
            }
            else {
                View selecttypelayout = View.inflate(getApplicationContext(), R.layout.ced_multivendor_selecttypelayout, null);
                RelativeLayout addoptions = selecttypelayout.findViewById(R.id.MultiVendor_addoptions);
                final LinearLayout options = selecttypelayout.findViewById(R.id.MultiVendor_options);
                addoptions.setOnClickListener(v12 -> {
                    View selectitem_layout = View.inflate(getApplicationContext(), R.layout.ced_multivendor_selectitem_layout, null);
                    Spinner priceType=selectitem_layout.findViewById(R.id.MultiVendor_type);
                    priceType.setAdapter(pricetypeadapter);
                    final RelativeLayout deleteoptions1 = selectitem_layout.findViewById(R.id.MultiVendor_deleteoptions);
                    deleteoptions1.setOnClickListener(v1 -> {
                        CardView cardView = (CardView) deleteoptions1.getParent().getParent().getParent();
                        options.removeView(cardView);
                        TextView deletedoption_id = (TextView) cardView.getChildAt(1);
                        try {
                            optiontypedeletehash.put(deletedoption_id.getText().toString(), "true");
                            dataforcustomoptions.put("deletedoptions", optiondeletehash.toString());
                            dataforcustomoptions.put("deletedoptiontypes", optiontypedeletehash.toString());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    });
                    options.addView(selectitem_layout);
                });
                foroptions.addView(selecttypelayout);
                mainvendorscroll.post(() -> mainvendorscroll.fullScroll(ScrollView.FOCUS_DOWN));
            }
        }
        else if (getItemfromEntry(optionType.getSelectedItem().toString()).equals("date") || getItemfromEntry(optionType.getSelectedItem().toString()).equals("date_time") || getItemfromEntry(optionType.getSelectedItem().toString()).equals("time")) {
            if (foroptions.getChildCount() > 0) {
                foroptions.removeAllViews();
                View datetyprlayout = View.inflate(getApplicationContext(), R.layout.ced_multivendor_datetyprlayout, null);
                Spinner priceType=datetyprlayout.findViewById(R.id.MultiVendor_type);
                priceType.setAdapter(pricetypeadapter);
                foroptions.addView(datetyprlayout);
            }
            else {
                View datetyprlayout = View.inflate(getApplicationContext(), R.layout.ced_multivendor_datetyprlayout, null);
                Spinner priceType=datetyprlayout.findViewById(R.id.MultiVendor_type);
                priceType.setAdapter(pricetypeadapter);
                foroptions.addView(datetyprlayout);
            }
        }
        else{
            Log.i("REpo", "onItemSelected_977: "+getItemfromEntry(optionType.getSelectedItem().toString()));
        }
    }

    private String getItemfromEntry(String selectedItem) {
        if (optionTypeMap.containsKey(selectedItem)){
            return optionTypeMap.get(selectedItem);
        }
        else if (selectedItem.equals(getString(R.string.select))){
            return "select";
        }
        else {
            return "";
        }
    }

    @Override
    protected void onResume() {
        Ced_MultiVendor_ConnectionDetector connectionDetector = new Ced_MultiVendor_ConnectionDetector(Ced_MultiVendor_CustomOption.this);
        if (connectionDetector.isConnectingToInternet()) {
            setname(vendorSessionManagement.getvendorname());
            setprofilepic(vendorSessionManagement.getvendorpic());
            changetitle("Custom Options");
            super.onResume();
        }
        else {
            Intent intent = new Intent(getApplicationContext(), Ced_MultiVendor_NoInternetconnection.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            super.onResume();
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

}
