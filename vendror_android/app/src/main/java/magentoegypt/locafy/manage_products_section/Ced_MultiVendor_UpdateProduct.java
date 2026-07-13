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

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import magentoegypt.locafy.base_app.Ced_MultiVendor_VendorSplash;
import magentoegypt.locafy.base_app.Ced_MultiVendor_VendorFunctionalityList;
import magentoegypt.locafy.navigation_drawer.Activity.Ced_MultiVendor_NavigationActivity;
import magentoegypt.locafy.api_request_response_section.network_error_handling.Ced_MultiVendor_ConnectionDetector;
import magentoegypt.locafy.api_request_response_section.network_error_handling.Ced_MultiVendor_NoInternetconnection;
import magentoegypt.locafy.api_request_response_section.network_error_handling.Ced_MultiVendor_UnAuthourizedRequestError;
import magentoegypt.locafy.api_request_response_section.AsyncResponse;
import magentoegypt.locafy.api_request_response_section.Ced_MultiVendor_ClientRequestResponse;
import magentoegypt.locafy.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

public class Ced_MultiVendor_UpdateProduct extends Ced_MultiVendor_NavigationActivity {
    Ced_MultiVendor_ConnectionDetector connectionDetector;
    Ced_MultiVendor_VendorFunctionalityList functionalityList;
    String Jstring;
    String type;
    HashMap<String, String> forstock;
    HashMap<String, String> productwebsiteshash;
    ArrayList<String> changedefaultimage;
    HashMap<String, String> fortaxes;
    HashMap<Integer, String> fortaxesdrop;
    HashMap<String, String> formainwebsites;
    HashMap<String, ArrayList<String>> forstores;
    HashMap<String, ArrayList<String>> forstoreviews;
    ArrayList<String> taxlist;
    ArrayList<String> stockavaillist;
    Spinner stockavail;
    Spinner taxclass;
    LinearLayout categoryselection;
    LinearLayout websiteselection;
    LinearLayout imageselection;
    LinearLayout deleteimagesection;
    JSONArray catfilters;
    HashMap<String, ArrayList<String>> catfilterdata;
    ArrayList<String> categoryfilterdataarraylist;
    CheckBox categorytag;
    CheckBox website;
    TextView save;
    TextView savecontinue;
    EditText product_name;
    EditText product_sku;
    EditText description;
    EditText shortdescription;
    EditText stock;
    EditText price;
    EditText specialprice;
    EditText weight;
    EditText url_key;
    Button addimagees;
    ArrayList<String> idsforwebsites;
    HashMap<String, String> dataforproductsave;
    String CurrentUrl;
    String updateurl;
    LinearLayout weightsection;
    LinearLayout sku_type_linear;
    LinearLayout price_type_linear;
    LinearLayout weight_type_linear;
    LinearLayout productattribute;
    ArrayList<String> uris;
    TextView imageuri;
    TextView uriurl;
    String product_id;
    HashMap<String, String> defaultimagehash;
    String deleteimageurl = "";
    int writeresult;
    int readresult;
    int id;
    String attribute_set;
    ScrollView mainvendoreditprofilescroll;
    HashMap<Integer, String> sequence;
    JSONObject tabs;
    JSONArray attributes;
    ArrayList<HashMap<String, String>> attributesarraylist;
    HashMap<String, String> select_label_value;
    HashMap<String, String> select_value_label;
    HashMap<String, String> label_value;
    HashMap<String, ArrayList<String>> multiselect_select;
    Spinner product_weight_type;
    Spinner product_price_type;
    Spinner product_sku_type;
    Button button;
    EditText MultiVendor_specialprice_To, MultiVendor_specialprice_From;
    Calendar newCalendar;
    ImageView product_image;
    Button browse_product_image;
    int count = 0;
    JSONArray multiple_images_array;
    boolean is_default_check = false;
    int SELECT_PHOTO = 0;
    String image_id;
    String producturi = "";
    String productpicname = "";
    String encodedproductImage = "";
    HashMap<String, String> parent_child;
    HashMap<String, String> parent_child2;
    HashSet<String> selected_category_ids;
    JSONArray saved_category_ids;
    TextView MultiVendor_regularpricetag;
    TextView MultiVendor_weighttag;
    LinearLayout priceSection;
    TextView MultiVendor_taxclasstag;
    String selected_websitetopost;
    private SimpleDateFormat dateFormatter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (android.os.Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(this.getResources().getColor(R.color.status_bar_color));
        }
        connectionDetector = new Ced_MultiVendor_ConnectionDetector(getApplicationContext());
        functionalityList = new Ced_MultiVendor_VendorFunctionalityList(getApplicationContext());

        /*Toast.makeText(this, "asdasdasd", Toast.LENGTH_SHORT).show();*/

        selected_websitetopost = getIntent().getStringExtra("selectedwebsite");
        if (connectionDetector.isConnectingToInternet()) {
            try {
                ViewGroup content = findViewById(R.id.MultiVendor_frame_container);
                getLayoutInflater().inflate(R.layout.ced_multivendor_activity_update_product, content, true);
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                getSupportActionBar().setHomeAsUpIndicator(R.drawable.backbutton);
                mainvendoreditprofilescroll = findViewById(R.id.MultiVendor_mainvendoreditprofilescroll);
                writeresult = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
                readresult = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE);
                setname(session.getvendorname());
                setprofilepic(session.getvendorpic());
                changetitle("Edit Product");
                Intent intent = getIntent();
                CurrentUrl = session.getBase_Url() + "vendorapi/vproducts/update";
                updateurl = session.getBase_Url() + "vendorapi/vproducts/update";
                deleteimageurl = session.getBase_Url() + "vendorapi/vproducts/deleteImage";
                categoryfilterdataarraylist = new ArrayList<>();
                uris = new ArrayList<>();
                sequence = new HashMap<>();
                catfilterdata = new HashMap<>();
                multiselect_select = new HashMap<>();
                attributesarraylist = new ArrayList<>();
                label_value = new HashMap<>();
                select_label_value = new HashMap<>();
                select_value_label = new HashMap<>();
                forstores = new HashMap<>();
                forstoreviews = new HashMap<>();
                forstock = new HashMap<>();
                productwebsiteshash = new HashMap<>();
                changedefaultimage = new ArrayList<>();
                formainwebsites = new HashMap<>();
                fortaxes = new HashMap<>();
                fortaxesdrop = new HashMap<>();
                dataforproductsave = new HashMap<>();
                taxlist = new ArrayList<>();
                idsforwebsites = new ArrayList<>();
                stockavaillist = new ArrayList<>();
                defaultimagehash = new HashMap<>();
                Jstring = intent.getStringExtra("dataforproductcreation");
                type = intent.getStringExtra("type");
                product_id = intent.getStringExtra("product_id");

                saved_category_ids = new JSONArray();
                selected_category_ids = new HashSet<>();

                parent_child = new HashMap<>();
                parent_child2 = new HashMap<>();

                getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);


                if (functionalityList.getProductAddon()) {
                    attribute_set = intent.getStringExtra("attribute_set");
                }
                JSONObject object = new JSONObject(Jstring);
                final int idd = Resources.getSystem().getIdentifier("btn_check_holo_light", "drawable", "android");
                /**************************initializingViews****************************/
                product_weight_type = findViewById(R.id.MultiVendor_product_weight_type);
                product_price_type = findViewById(R.id.MultiVendor_product_price_type);
                product_sku_type = findViewById(R.id.MultiVendor_product_sku_type);
                savecontinue = findViewById(R.id.MultiVendor_savecontinue);
                stockavail = findViewById(R.id.MultiVendor_stockavail);
                taxclass = findViewById(R.id.MultiVendor_taxclass);
                categoryselection = findViewById(R.id.MultiVendor_categoryselection);
                websiteselection = findViewById(R.id.MultiVendor_websiteselection);
                weightsection = findViewById(R.id.MultiVendor_weightsection);
                imageselection = findViewById(R.id.MultiVendor_imageselection);
                deleteimagesection = findViewById(R.id.MultiVendor_deleteimagesection);
                categorytag = findViewById(R.id.MultiVendor_categorytag);
                website = findViewById(R.id.MultiVendor_website);
                save = findViewById(R.id.MultiVendor_save);
                product_name = findViewById(R.id.MultiVendor_product_name);
                product_sku = findViewById(R.id.MultiVendor_product_sku);
                description = findViewById(R.id.MultiVendor_description);
                shortdescription = findViewById(R.id.MultiVendor_shortdescription);
                stock = findViewById(R.id.MultiVendor_stock);
                price = findViewById(R.id.MultiVendor_price);
                weight = findViewById(R.id.MultiVendor_weight);
                specialprice = findViewById(R.id.MultiVendor_specialprice);
                sku_type_linear = findViewById(R.id.MultiVendor_sku_type_linear);
                price_type_linear = findViewById(R.id.MultiVendor_price_type_linear);
                weight_type_linear = findViewById(R.id.MultiVendor_weight_type_linear);
                productattribute = findViewById(R.id.MultiVendor_productattribute);
                MultiVendor_specialprice_To = findViewById(R.id.MultiVendor_specialpriceTo);
                MultiVendor_specialprice_From = findViewById(R.id.MultiVendor_specialprice_From);
                url_key = findViewById(R.id.MultiVendor_product_url_key);
                MultiVendor_regularpricetag = findViewById(R.id.MultiVendor_regularpricetag);
                MultiVendor_weighttag = findViewById(R.id.MultiVendor_weighttag);

                priceSection = findViewById(R.id.productSection);
                MultiVendor_taxclasstag = findViewById(R.id.MultiVendor_taxclasstag);

                multiple_images_array = new JSONArray();
                if (type.equals("simple") || type.equals("bundle") && object.getJSONObject("data").getJSONObject("productdata").getString("weight_type").equalsIgnoreCase("1")) {
                    weightsection.setVisibility(View.VISIBLE);
                    weight.setText(object.getJSONObject("data").getJSONObject("productdata").getString("weight"));
                }
                if (type.equals("bundle")) {
                    sku_type_linear.setVisibility(View.VISIBLE);
                    price_type_linear.setVisibility(View.VISIBLE);
                    weight_type_linear.setVisibility(View.VISIBLE);
                    final int price_type_value = Integer.parseInt(object.getJSONObject("data").getJSONObject("productdata").getString("price_type"));
                    int sku_type_value = Integer.parseInt(object.getJSONObject("data").getJSONObject("productdata").getString("sku_type"));
                    int weight_type_value = Integer.parseInt(object.getJSONObject("data").getJSONObject("productdata").getString("weight_type"));
                    product_weight_type.setSelection(weight_type_value);
                    product_price_type.setSelection(price_type_value);
                    product_sku_type.setSelection(sku_type_value);
                    product_price_type.setEnabled(false);

                    product_price_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            if (product_price_type.getSelectedItem().toString().equalsIgnoreCase("dynamic")) {
                                price.setText("");
                                /*price.setText(price.getTag().toString());*/
                                price.setVisibility(View.GONE);
                                MultiVendor_regularpricetag.setVisibility(View.GONE);
                            }
                            if (product_price_type.getSelectedItem().toString().equalsIgnoreCase("fixed")) {
                                price.setVisibility(View.VISIBLE);
                                MultiVendor_regularpricetag.setVisibility(View.VISIBLE);
                                price.setTag(price.getText());
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {

                        }
                    });

                    product_weight_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            if (product_weight_type.getSelectedItem().toString().equalsIgnoreCase("dynamic")) {
                                weight.setText("");
                                /*weight.setText(weight.getTag().toString());*/
                                weight.setVisibility(View.GONE);
                                MultiVendor_weighttag.setVisibility(View.GONE);
                            }
                            if (product_weight_type.getSelectedItem().toString().equalsIgnoreCase("fixed")) {
                                weight.setVisibility(View.VISIBLE);
                                MultiVendor_weighttag.setVisibility(View.VISIBLE);
                                weight.setTag(weight.getText());
                                weightsection.setVisibility(View.VISIBLE);
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {

                        }
                    });

                }

                if (type.equalsIgnoreCase("grouped")) {
                    taxclass.setVisibility(View.GONE);
                    priceSection.setVisibility(View.GONE);
                    MultiVendor_taxclasstag.setVisibility(View.GONE);
                }

                addimagees = findViewById(R.id.MultiVendor_addimagees);
                id = Resources.getSystem().getIdentifier("btn_check_holo_light", "drawable", "android");
                categorytag.setButtonDrawable(id);
                website.setButtonDrawable(id);
                if (functionalityList.getProductAddon()) {
                    savecontinue.setVisibility(View.VISIBLE);
                } else {
                    savecontinue.setVisibility(View.GONE);
                }
                /**************************initializingViews****************************/
                /***************************************************************************/
                product_name.setText(object.getJSONObject("data").getJSONObject("productdata").getString("name"));
                product_sku.setText(object.getJSONObject("data").getJSONObject("productdata").getString("sku"));

                if (object.getJSONObject("data").getJSONObject("productdata").has("description")) {
                    description.setText(object.getJSONObject("data").getJSONObject("productdata").getString("description"));
                }
                if (object.getJSONObject("data").getJSONObject("productdata").has("short_description")) {
                    shortdescription.setText(object.getJSONObject("data").getJSONObject("productdata").getString("short_description"));
                }
                url_key.setText(object.getJSONObject("data").getJSONObject("productdata").getString("url_key"));

                if (type.equals("grouped")) {

                } else {
                    price.setText(object.getJSONObject("data").getJSONObject("productdata").getString("price"));
                    if (!(object.getJSONObject("data").getJSONObject("productdata").getString("special_price").equals("null"))) {
                        specialprice.setText(object.getJSONObject("data").getJSONObject("productdata").getString("special_price"));
                        if (object.getJSONObject("data").getJSONObject("productdata").has("special_to_date")) {
                            MultiVendor_specialprice_To.setText(object.getJSONObject("data").getJSONObject("productdata").getString("special_to_date"));
                        }
                        if (object.getJSONObject("data").getJSONObject("productdata").has("special_from_date")) {
                            MultiVendor_specialprice_From.setText(object.getJSONObject("data").getJSONObject("productdata").getString("special_from_date"));
                        }
                    }

                }
                /****************************taxes***************************/

                JSONObject taxesobject = object.getJSONObject("data").getJSONObject("taxes");
                JSONArray taxesvalues = taxesobject.names();
                for (int t = 0; t < taxesvalues.length(); t++) {
                    fortaxes.put(taxesobject.getString((String) taxesvalues.get(t)), (String) taxesvalues.get(t));
                    fortaxesdrop.put(Integer.valueOf((String) taxesvalues.get(t)), taxesobject.getString((String) taxesvalues.get(t)));
                }
                if ((type.equals("grouped"))) {
                    Iterator tax = fortaxes.entrySet().iterator();
                    while (tax.hasNext()) {
                        Map.Entry pair = (Map.Entry) tax.next();
                        String key = String.valueOf(pair.getKey());
                        taxlist.add(key);

                    }
                    ArrayAdapter<String> taxlistadp = new ArrayAdapter<>(Ced_MultiVendor_UpdateProduct.this, android.R.layout.simple_dropdown_item_1line, taxlist);
                    taxclass.setAdapter(taxlistadp);

                } else {
                    int tax_class_id = Integer.parseInt(object.getJSONObject("data").getJSONObject("productdata").getString("tax_class_id"));
                    if (getResources().getString(R.string.Enable_Log).equals("yes")) {
                        Log.i("tax_class_id", "" + tax_class_id);
                    }
                    taxlist.add(fortaxesdrop.get(tax_class_id));
                    if (getResources().getString(R.string.Enable_Log).equals("yes")) {
                        Log.i("value_tax", fortaxesdrop.get(tax_class_id));
                    }
                    Iterator tax = fortaxes.entrySet().iterator();
                    while (tax.hasNext()) {
                        Map.Entry pair = (Map.Entry) tax.next();
                        String key = String.valueOf(pair.getKey());
                        if (!(key.equals(fortaxesdrop.get(tax_class_id)))) {
                            taxlist.add(key);
                        }

                    }
                    ArrayAdapter<String> taxlistadp = new ArrayAdapter<>(Ced_MultiVendor_UpdateProduct.this, android.R.layout.simple_dropdown_item_1line, taxlist);
                    taxclass.setAdapter(taxlistadp);
                }
                /****************************taxes***************************/
                JSONArray productcategories = object.getJSONObject("data").getJSONArray("productcategories");
                for (int i = 0; i < productcategories.length(); i++) {
                    categoryfilterdataarraylist.add((String) productcategories.get(i));
                }
                JSONArray productwebsites = object.getJSONObject("data").getJSONArray("productwebsites");
                for (int i = 0; i < productwebsites.length(); i++) {
                    productwebsiteshash.put((String) productwebsites.get(i), (String) productwebsites.get(i));
                    if (getResources().getString(R.string.Enable_Log).equals("yes")) {
                        Log.i("productwebsiteshash", "" + productwebsiteshash);
                    }
                }
                /**************************************************************************************************************************************************************/
                /****************************tabs***************************/
                if (functionalityList.getProductAddon()) {
                    tabs = object.getJSONObject("data").getJSONObject("tabs");
                    if (tabs.has("websites")) {
                        website.setVisibility(View.VISIBLE);
                    } else {
                        website.setVisibility(View.GONE);
                    }
                    if (tabs.has("categories")) {
                        // categorytag.setVisibility(View.VISIBLE);
                    } else {
                        categorytag.setVisibility(View.GONE);
                    }
                }
                /****************************tabs***************************/
                /*****************************Categories***********************************************************************************************************************/
/*
                catfilters = object.getJSONObject("data").getJSONArray("category");
                saved_category_ids = object.getJSONObject("data").getJSONObject("productdata").getJSONArray("category_ids");

                */
/**************************//*


                for (int i = 0; i < saved_category_ids.length(); i++) {
                    selected_category_ids.add(saved_category_ids.getString(i));
                }

                Log.i("saved_ids", selected_category_ids.toString());

                */
/***********************//*


                 */
/******************Categories************************************************//*

                if (catfilters.length() > 0) {
                    JSONObject current_object = null;
                    View category_view = null;
                    TextView subcategories = null;
                    CheckBox main_cat = null;
                    ImageView cat_icon = null;
                    String maincatname = null;
                    String maincatid = null;
                    String parts[] = new String[2];
                    for (int i = 0; i < catfilters.length(); i++) {
                        current_object = catfilters.getJSONObject(i);
                        category_view = View.inflate(Ced_MultiVendor_UpdateProduct.this, R.layout.ced_multivendor_maincategory, null);
                        subcategories = (TextView) category_view.findViewById(R.id.subcategories);
                        main_cat = (CheckBox) category_view.findViewById(R.id.main_cat);
                        cat_icon = (ImageView) category_view.findViewById(R.id.cat_icon);
                        parts = current_object.getString("main_category").split("#");
                        maincatname = parts[1];
                        maincatid = parts[0];
                        if (current_object.has("sub_categories")) {
                            subcategories.setText(current_object.getJSONArray("sub_categories").toString());
                        } else {
                            cat_icon.setVisibility(View.GONE);
                        }
                        main_cat.setText(maincatname);
                        main_cat.setId(Integer.parseInt(maincatid));
                        final CheckBox finalMain_cat = main_cat;
                        main_cat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                            @Override
                            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                if (isChecked) {
                                    selected_category_ids.add(String.valueOf(finalMain_cat.getId()));
                                } else {
                                    selected_category_ids.remove(String.valueOf(finalMain_cat.getId()));
                                }
                            }
                        });
                        if (selected_category_ids.contains(maincatid)) {
                            main_cat.setChecked(true);
                        }
                        final boolean[] open = {false};
                        final ImageView finalCat_icon = cat_icon;
                        final ImageView finalCat_icon1 = cat_icon;
                        cat_icon.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                LinearLayout parent = (LinearLayout) finalCat_icon.getParent();
                                LinearLayout subparent = (LinearLayout) parent.getParent();
                                RelativeLayout mainparent = (RelativeLayout) subparent.getParent();
                                TextView subcatjson = (TextView) mainparent.getChildAt(0);
                                LinearLayout subcats = (LinearLayout) subparent.getChildAt(1);
                                if (open[0]) {
                                    subcats.removeAllViews();
                                    open[0] = false;
                                    finalCat_icon1.setImageResource(R.drawable.expand);
                                } else {
                                    try {
                                        if (!(subcatjson.getText().toString().isEmpty())) {
                                            JSONArray array = new JSONArray(subcatjson.getText().toString());
                                            createsubcat(array, subcats);
                                            open[0] = true;
                                            finalCat_icon1.setImageResource(R.drawable.collapse);
                                        }

                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        });
                        categoryselection.addView(category_view);
                    }
                }

                */
/******************Categories************************************************//*

                 */
/******************Categories************************************************//*


                /**************************stock****************************/
                JSONObject stockobject = object.getJSONObject("data").getJSONObject("stock");
                JSONArray values = stockobject.names();
                for (int v = 0; v < values.length(); v++) {
                    forstock.put(stockobject.getString((String) values.get(v)), (String) values.get(v));
                    stockavaillist.add(stockobject.getString((String) values.get(v)));
                }
                ArrayAdapter<String> adp = new ArrayAdapter<>(Ced_MultiVendor_UpdateProduct.this, android.R.layout.simple_dropdown_item_1line, stockavaillist);
                stockavail.setAdapter(adp);
                String qty = object.getJSONObject("data").getJSONObject("productdata").getJSONArray("stock_item").getJSONObject(0).getString("qty");
                stock.setText(qty);
                if (object.getJSONObject("data").getJSONObject("productdata").getString("is_in_stock").equals("1")) {
                    stockavail.setSelection(0);
                } else {
                    stockavail.setSelection(1);
                }
                /**************************stock****************************/
                /****************************websites***************************/
                JSONObject websitesobject = object.getJSONObject("data").getJSONObject("websites");
                JSONObject storesobject = object.getJSONObject("data").getJSONObject("stores");
                JSONObject storeviewssobject = object.getJSONObject("data").getJSONObject("storeViews");
                JSONArray websitesvalues = websitesobject.names();
                for (int t = 0; t < websitesvalues.length(); t++) {
                    formainwebsites.put(websitesobject.getString((String) websitesvalues.get(t)), (String) websitesvalues.get(t));

                    JSONArray stores = storesobject.getJSONArray(websitesobject.getString((String) websitesvalues.get(t)));
                    ArrayList<String> storestring = new ArrayList<>();
                    for (int s = 0; s < stores.length(); s++) {

                        storestring.add((String) stores.get(s));
                    }
                    forstores.put(websitesobject.getString((String) websitesvalues.get(t)), storestring);
                    JSONArray storeviews = storeviewssobject.getJSONObject(websitesobject.getString((String) websitesvalues.get(t))).names();
                    for (int view = 0; view < storeviews.length(); view++) {
                        ArrayList<String> views = new ArrayList<>();
                        JSONArray storeviewjsonarray = storeviewssobject.getJSONObject(websitesobject.getString((String) websitesvalues.get(t))).getJSONArray((String) storeviews.get(view));
                        for (int j = 0; j < storeviewjsonarray.length(); j++) {
                            views.add((String) storeviewjsonarray.get(j));
                        }
                        forstoreviews.put((String) storeviews.get(view), views);
                    }
                }
                /****************************websites***************************/
                try {
                    Iterator iterator = formainwebsites.keySet().iterator();
                    while (iterator.hasNext()) {
                        final LinearLayout mainwebsite = new LinearLayout(Ced_MultiVendor_UpdateProduct.this);
                        mainwebsite.setOrientation(LinearLayout.VERTICAL);
                        final CheckBox mainwebsitename = new CheckBox(Ced_MultiVendor_UpdateProduct.this);
                        mainwebsitename.setButtonDrawable(id);
                        try {
                            mainwebsitename.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                @Override
                                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                    if (isChecked) {

                                        if (idsforwebsites.contains(formainwebsites.get(mainwebsitename.getText().toString()))) {

                                        } else {
                                            idsforwebsites.add(formainwebsites.get(mainwebsitename.getText().toString()));
                                        }
                                    } else {

                                        idsforwebsites.remove(formainwebsites.get(mainwebsitename.getText().toString()));

                                    }
                                }
                            });
                        } catch (Exception e) {
                            Intent main = new Intent(Ced_MultiVendor_UpdateProduct.this, Ced_MultiVendor_VendorSplash.class);
                            main.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            main.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(main);
                            overridePendingTransition(R.anim.ced_multivendor_slide_in, R.anim.ced_multivendor_slide_out);
                        }
                        String websitename = iterator.next().toString();
                        mainwebsitename.setText(websitename);
                        String key = formainwebsites.get(websitename);
                        if (productwebsiteshash.containsKey(key)) {
                            mainwebsitename.setChecked(true);
                        }
                        mainwebsitename.setTypeface(Typeface.DEFAULT_BOLD);
                        mainwebsite.addView(mainwebsitename, 0);
                        ArrayList<String> storeandview = forstores.get(websitename);
                        Iterator iterator2 = storeandview.iterator();
                        while (iterator2.hasNext()) {
                            LinearLayout storelayout = new LinearLayout(Ced_MultiVendor_UpdateProduct.this);
                            storelayout.setOrientation(LinearLayout.VERTICAL);
                            TextView storename = new TextView(Ced_MultiVendor_UpdateProduct.this);
                            String storenametag = iterator2.next().toString();
                            storename.setText(storenametag);
                            storename.setTypeface(Typeface.DEFAULT_BOLD);
                            storename.setTextColor(getResources().getColor(R.color.black));
                            storename.setPadding(15, 0, 0, 0);
                            storelayout.addView(storename, 0);
                            ArrayList<String> storeviews = forstoreviews.get(storenametag);
                            Iterator iterator3 = storeviews.iterator();
                            LinearLayout storeviewlayout = new LinearLayout(Ced_MultiVendor_UpdateProduct.this);
                            storeviewlayout.setOrientation(LinearLayout.VERTICAL);
                            storeviewlayout.setPadding(25, 0, 0, 0);
                            while (iterator3.hasNext()) {
                                try {
                                    String Storeviewname = iterator3.next().toString();
                                    TextView view = new TextView(Ced_MultiVendor_UpdateProduct.this);
                                    view.setText(Storeviewname);
                                    view.setTextColor(getResources().getColor(R.color.black));
                                    storeviewlayout.addView(view);
                                } catch (Exception e) {
                                    Intent main = new Intent(Ced_MultiVendor_UpdateProduct.this, Ced_MultiVendor_VendorSplash.class);
                                    main.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    main.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(main);
                                    overridePendingTransition(R.anim.ced_multivendor_slide_in, R.anim.ced_multivendor_slide_out);
                                }

                            }
                            try {
                                storelayout.addView(storeviewlayout, 1);
                                mainwebsite.addView(storelayout);
                            } catch (Exception e) {
                                Intent main = new Intent(Ced_MultiVendor_UpdateProduct.this, Ced_MultiVendor_VendorSplash.class);
                                main.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                main.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(main);
                                overridePendingTransition(R.anim.ced_multivendor_slide_in, R.anim.ced_multivendor_slide_out);
                            }
                        }
                        websiteselection.addView(mainwebsite);
                    }
                    website.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                            if (isChecked) {
                                websiteselection.setVisibility(View.VISIBLE);
                            } else {
                                websiteselection.setVisibility(View.GONE);
                            }
                        }
                    });

                } catch (Exception e) {
                    Intent main = new Intent(Ced_MultiVendor_UpdateProduct.this, Ced_MultiVendor_VendorSplash.class);
                    main.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    main.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(main);
                    overridePendingTransition(R.anim.ced_multivendor_slide_in, R.anim.ced_multivendor_slide_out);
                }
                /****************************websites***************************/
                /****************************deleteimages***************************/
                JSONArray media_image = object.getJSONObject("data").getJSONObject("productdata").getJSONArray("media_image");
                Log.i("vaibhavimage", object.getJSONObject("data").getJSONObject("productdata").getJSONArray("media_image") + "");

                if (getResources().getString(R.string.Enable_Log).equals("yes")) {
                    Log.i("media_image", "" + media_image);
                }

/*                if(media_image.length()>0)
                {
                    for(int media=0;media<media_image.length();media++)
                    {
                        JSONObject jsonObject=media_image.getJSONObject(media);
                        LinearLayout layout=new LinearLayout(getApplicationContext());
                        layout.setOrientation(LinearLayout.HORIZONTAL);
                        final ImageView imageView=new ImageView(getApplicationContext());
                        final TextView image_name=new TextView(getApplicationContext());

                        image_name.setText(jsonObject.getString("image_path"));
                        image_name.setVisibility(View.GONE);

                        Glide.with(getApplicationContext())
                                .load(jsonObject.getString("image_path"))
                                .placeholder(R.drawable.placeholder)
                                .error(R.drawable.placeholder)
                                .override(50,50)
                                .into(imageView);

                        imageView.setOnClickListener(new View.OnClickListener()
                        {
                            @Override
                            public void onClick(View v)
                            {
                                final LinearLayout parentlayout= (LinearLayout) imageView.getParent();
                                TextView name= (TextView) parentlayout.getChildAt(4);
                                showImagePOP(imageView);

                            }
                        });

                        TextView imagename=new TextView(getApplicationContext());
                        final CheckBox deleteimage=new CheckBox(getApplicationContext());

                        final CheckBox defaultimage=new CheckBox(getApplicationContext());
                        if(jsonObject.getString("default_image").equals("true"))
                        {
                            defaultimage.setChecked(true);
                            JSONArray jsonArray=new JSONArray();
                            changedefaultimage.add(jsonArray.put(jsonObject.getString("image_name")).toString());
                            defaultimagehash.put(jsonObject.getString("image_name").toString(),"1");
                        }
                        defaultimage.setText("default");
                        defaultimage.setTextColor(getResources().getColor(R.color.black));
                        defaultimage.setTypeface(Typeface.DEFAULT_BOLD);
                        defaultimage.setButtonDrawable(id);
                        defaultimage.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                            @Override
                            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                if (isChecked)
                                {
                                    if(defaultimagehash.size()==0)
                                    {
                                        if (changedefaultimage.size() == 0)
                                        {
                                            final LinearLayout parentlayout = (LinearLayout) defaultimage.getParent();
                                            TextView name = (TextView) parentlayout.getChildAt(1);
                                            JSONArray jsonArray = new JSONArray();
                                            changedefaultimage.add(jsonArray.put(name.getText().toString()).toString());
                                            defaultimagehash.put(name.getText().toString(),"1");
                                        } else {
                                            Toast.makeText(Ced_MultiVendor_UpdateProduct.this, "Default image can only be one", Toast.LENGTH_LONG).show();
                                            defaultimage.setChecked(false);
                                        }
                                    }
                                    else
                                    {

                                        Toast.makeText(Ced_MultiVendor_UpdateProduct.this, "Default image can only be one", Toast.LENGTH_LONG).show();
                                        defaultimage.setChecked(false);
                                    }

                                }
                                else
                                {
                                    changedefaultimage.clear();
                                    defaultimagehash.clear();
                                }
                            }
                        });
                        deleteimage.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
                        {
                            @Override
                            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
                            {
                                if(isChecked)
                                {
                                    HashMap<String,String>deleteimagehash=new HashMap<String, String>();
                                    deleteimagehash.put("vendor_id",session.getVendorid());
                                    deleteimagehash.put("product_id",product_id);
                                    deleteimagehash.put("hashkey",session.getHahkey());
                                    deleteimagehash.put("storeid","0");
                                    final LinearLayout parentlayout= (LinearLayout) deleteimage.getParent();
                                    final TextView name= (TextView) parentlayout.getChildAt(1);
                                    JSONObject nameobject=new JSONObject();
                                    try
                                    {
                                        nameobject.put("imagename",name.getText().toString());
                                    }
                                    catch (JSONException e)
                                    {
                                        e.printStackTrace();
                                    }
                                    deleteimagehash.put("imagename", nameobject.toString());
                                    Ced_MultiVendor_ClientRequestResponse crr = new Ced_MultiVendor_ClientRequestResponse(new AsyncResponse()
                                    {
                                        @Override
                                        public void processFinish(Object output) throws JSONException
                                        {
                                            Jstring = output.toString();
                                            if(functionalityList.getExtensionAddon())
                                            {
                                                JSONObject jsonObject=new JSONObject(Jstring);
                                                if(jsonObject.has("vendor_approved"))
                                                {
                                                    logout();
                                                }else
                                                {
                                                    String success=jsonObject.getJSONObject("data").getString("success");
                                                    if(success.equals("true"))
                                                    {
                                                        deleteimagesection.removeView(parentlayout);
                                                        Toast.makeText(getApplicationContext(),jsonObject.getJSONObject("data").getString("message"),Toast.LENGTH_LONG).show();
                                                        if (defaultimagehash.containsKey(name.getText().toString())) {

                                                            defaultimagehash.clear();
                                                            changedefaultimage.clear();
                                                        }

                                                    }
                                                    else
                                                    {
                                                        Toast.makeText(getApplicationContext(),jsonObject.getJSONObject("data").getString("message"),Toast.LENGTH_LONG).show();
                                                    }
                                                }

                                            }
                                            else
                                            {
                                                Intent intent=new Intent(getApplicationContext(), Ced_MultiVendor_UnAuthourizedRequestError.class);
                                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                                startActivity(intent);
                                                overridePendingTransition(R.anim.ced_multivendor_slide_in, R.anim.ced_multivendor_slide_out);
                                            }
                                        }
                                    },Ced_MultiVendor_UpdateProduct.this,"POST",deleteimagehash);
                                    crr.execute(deleteimageurl);

                                }
                            }
                        });
                        deleteimage.setTypeface(Typeface.DEFAULT_BOLD);
                        deleteimage.setText("delete");
                        deleteimage.setTextColor(getResources().getColor(R.color.black));
                        deleteimage.setButtonDrawable(id);
                        imagename.setText(jsonObject.getString("image_name"));
                        imagename.setVisibility(View.GONE);
                        layout.addView(imageView, 0);
                        layout.addView(imagename, 1);
                        layout.addView(deleteimage,2);
                        layout.addView(defaultimage,3);
                        layout.addView(image_name,4);
                        deleteimagesection.addView(layout);
                    }
                }*/

                if (media_image.length() > 0) {
                    Log.i("savedimage", "length " + media_image);
                    for (int i = 0; i < media_image.length(); i++) {
                        try {
                            JSONObject image_object = media_image.getJSONObject(i);
                            Log.i("savedimage", image_object.toString());
                            View layout = View.inflate(Ced_MultiVendor_UpdateProduct.this, R.layout.ced_multivendor_add_multiple_images, null);
                            final LinearLayout add_multiple_images_layout = layout.findViewById(R.id.multiple_images_layout);
                            product_image = layout.findViewById(R.id.MultiVendor_productimage);
                            browse_product_image = layout.findViewById(R.id.MultiVendor_browseproductimage);
                            final ImageView remove_image = layout.findViewById(R.id.remove_image);
                            final CheckBox default_checkbox = layout.findViewById(R.id.default_checkbox);
                            final TextView layuot_id = layout.findViewById(R.id.layout_id);
                            layuot_id.setText(String.valueOf(i));
                            Log.i("savedimage", "image--" + image_object.getString("image_path"));

                            Glide.with(getApplicationContext())
                                    .load(image_object.getString("image_path"))
                                    .placeholder(R.drawable.placeholder)
                                    .error(R.drawable.placeholder)
                                    .override(120, 120)
                                    .into(product_image);

                            if (!image_object.getString("default_image").equalsIgnoreCase("")) {

                                if (image_object.has("default_image") && image_object.getBoolean("default_image")) {


                                    default_checkbox.setChecked(true);
                                    is_default_check = true;

                                }
                            }

                            browse_product_image.setOnClickListener(new View.OnClickListener() {

                                @Override
                                public void onClick(View view) {
                                    image_id = layuot_id.getText().toString();
                                    if (writeresult == PackageManager.PERMISSION_GRANTED && readresult == PackageManager.PERMISSION_GRANTED) {
                                        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                                        photoPickerIntent.setType("image/*");
                                        SELECT_PHOTO = 1;
                                        startActivityForResult(photoPickerIntent, 1);

                                    } else {

                                        if (ActivityCompat.shouldShowRequestPermissionRationale(Ced_MultiVendor_UpdateProduct.this, Manifest.permission.READ_EXTERNAL_STORAGE) && ActivityCompat.shouldShowRequestPermissionRationale(Ced_MultiVendor_UpdateProduct.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

                                            Toast.makeText(Ced_MultiVendor_UpdateProduct.this, "Storage permission allows us to access data. Please allow in App Settings for additional functionality.", Toast.LENGTH_LONG).show();

                                        } else {

                                            ActivityCompat.requestPermissions(Ced_MultiVendor_UpdateProduct.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                                        }
                                    }
                                }
                            });

                            remove_image.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    /*add_multiple_images_layout.removeViewAt(Integer.parseInt(layuot_id.getText().toString()));*/

                                    LinearLayout parent_ll = (LinearLayout) remove_image.getParent();

                                    imageselection.removeView(parent_ll);

                                    Log.i("vaibhavarray", "" + multiple_images_array.toString());

                                    /*String current = layuot_id.getText().toString();
                                    try {
                                        for(int i = 0 ;i<multiple_images_array.length();i++)
                                        {
                                            JSONObject current_object = multiple_images_array.getJSONObject(i);

                                            if (current == current_object.getString("image_id"))
                                            {
                                                multiple_images_array.remove(Integer.parseInt(layuot_id.getText().toString()));
                                                Toast.makeText(Ced_MultiVendor_UpdateProduct.this, current_object.getString("image_id")+"up in"+layuot_id.getText(), Toast.LENGTH_SHORT).show();
                                                Log.i("vaibhavarray",""+multiple_images_array.toString());
                                            }

                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }*/
                                }
                            });

                            default_checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                @Override
                                public void onCheckedChanged(CompoundButton compoundButton, boolean ischecked) {
                                    if (ischecked) {
                                        if (is_default_check) {
                                            Toast.makeText(Ced_MultiVendor_UpdateProduct.this, R.string.onlyoneimagecanbeselected, Toast.LENGTH_SHORT).show();
                                            default_checkbox.setChecked(false);
                                            is_default_check = false;
                                        } else {
                                            /*try {
                                                for(int i = 0 ;i<multiple_images_array.length();i++)
                                                {
                                                    JSONObject current_object = multiple_images_array.getJSONObject(i);
                                                    if(current_object.getString("image_id").equals(layuot_id.getText().toString()))
                                                    {
                                                        current_object.put("default",true);
                                                        Log.i("vaibhavcheck","default"+current_object);
                                                    }
                                                    else
                                                    {
                                                        if(current_object.has("deafult"))
                                                        {
                                                            current_object.remove("default");
                                                            Log.i("vaibhavcheck","remove_default");
                                                        }
                                                    }
                                        *//*JSONObject current_object = (JSONObject) multiple_images_array.get(Integer.parseInt(layuot_id.getText().toString()));
                                        current_object.put("default", true);*//*
                                                }
                                    *//*Log.i("currentobject","current object "+current_object.toString());
                                    Log.i("currentobject","length-- "+multiple_images_array.length());*//*
                                            }
                                            catch (JSONException e)
                                            {
                                                e.printStackTrace();
                                            }*/

                                            Log.i("imagechecked", "" + layuot_id.getText().toString());
                                            is_default_check = true;
                                        }
                                    }
                        /*else{
                            is_default_check = false;
                        }*/
                                }
                            });

                            //S  Log.i("savedimage","saved image "+image_object.getString("product_image"));
                            imageselection.addView(add_multiple_images_layout);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    count = media_image.length() + 1;
                } else {
                    Log.i("savedimage", "length 0 ");
                }
                /****************************deleteimages***************************/
                /****************************saveproduct***************************/
                MultiVendor_specialprice_To.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        newCalendar = Calendar.getInstance();
                        dateFormatter = new SimpleDateFormat("MM/dd/yyyy", Locale.US);
                        new DatePickerDialog(Ced_MultiVendor_UpdateProduct.this, new DatePickerDialog.OnDateSetListener() {

                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                newCalendar.set(Calendar.YEAR, year);
                                newCalendar.set(Calendar.MONTH, monthOfYear);
                                newCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                                MultiVendor_specialprice_To.setText(dateFormatter.format(newCalendar.getTime()));
                                // MultiVendor_specialprice_To.setText(dateFormatter.format(newCalendar.getTime()));

                            }

                        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH)).show();
                    }
                });
                MultiVendor_specialprice_From.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        newCalendar = Calendar.getInstance();
                        dateFormatter = new SimpleDateFormat("MM/dd/yyyy", Locale.US);
                        new DatePickerDialog(Ced_MultiVendor_UpdateProduct.this, new DatePickerDialog.OnDateSetListener() {

                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                newCalendar.set(Calendar.YEAR, year);
                                newCalendar.set(Calendar.MONTH, monthOfYear);
                                newCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                                MultiVendor_specialprice_From.setText(dateFormatter.format(newCalendar.getTime()));
                                // MultiVendor_specialprice_To.setText(dateFormatter.format(newCalendar.getTime()));

                            }

                        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH)).show();
                    }
                });
                save.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dataforproductsave.put("vendor_id", session.getVendorid());
                        dataforproductsave.put("product_id", product_id);
                        dataforproductsave.put("hashkey", session.getHahkey());
                        dataforproductsave.put("type", type);

                       /* if (changedefaultimage.size() > 0)
                        {
                            dataforproductsave.put("defaultimage", changedefaultimage.get(0));

                        }*/

                        if (imageselection.getChildCount() > 0) {
                            LinearLayout child_ll_container;
                            ImageView img_product;
                            TextView layou_id;
                            CheckBox checkBox;

                            for (int i = 0; i < imageselection.getChildCount(); i++) {
                                child_ll_container = (LinearLayout) imageselection.getChildAt(i);


                                layou_id = (TextView) child_ll_container.getChildAt(4);
                                img_product = (ImageView) child_ll_container.getChildAt(0);
                                checkBox = (CheckBox) child_ll_container.getChildAt(2);

                                try {
                                    if (img_product.getDrawable() != null) {


                                        JSONObject selected_images = new JSONObject();
                                        selected_images.put("type", "image/png");
                                        selected_images.put("name", "abc" + new Random().nextInt(100) + 1 + ".png");
                                        if (checkBox.isChecked()) {
                                            selected_images.put("default", true);
                                        }
                                        //   Bitmap image_bm = ((BitmapDrawable) img_product.getDrawable()).getBitmap();
                                        selected_images.put("base64_encoded_data", img_product.getTag());
                                        selected_images.put("image_id", i);
                                        multiple_images_array.put(selected_images);

                                            /*JSONObject selected_images = new JSONObject();
                                            selected_images.put("type", "file");
                                            selected_images.put("name", "name" + i + ".jpeg");
                                            if(checkBox.isChecked())
                                            {
                                                selected_images.put("default",true);
                                            }
                                            Bitmap image_bm = ((BitmapDrawable) img_product.getDrawable()).getBitmap();
                                           selected_images.put("base64_encoded_data", encodeImage(image_bm));
                                            selected_images.put("image_id", i);
                                            multiple_images_array.put(selected_images);*/
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                                //  Log.i("vaibhav_media",layou_id.getText() +"--"+((BitmapDrawable)product_image.getDrawable()).getBitmap());


                            }
                            Log.i("checkmultipleimages", "Array-- " + multiple_images_array.toString());


                            dataforproductsave.put("images", multiple_images_array.toString());
                        }
                        Log.i("vaibhavlength", imageselection.getChildCount() + "");


                        if (product_name.getText().toString().isEmpty()) {
                            product_name.setError(getResources().getString(R.string.empty));
                            product_name.requestFocus();
                        } else {
                            dataforproductsave.put("product_name", product_name.getText().toString());
                            if (product_sku.getText().toString().isEmpty()) {
                                product_sku.setError(getResources().getString(R.string.empty));
                                product_sku.requestFocus();
                            } else {
                                dataforproductsave.put("product_sku", product_sku.getText().toString());
                                if (description.getText().toString().isEmpty()) {
                                    description.setError(getResources().getString(R.string.empty));
                                    description.requestFocus();
                                } else {
                                    dataforproductsave.put("description", description.getText().toString());
                                    if (shortdescription.getText().toString().isEmpty()) {
                                        shortdescription.setError(getResources().getString(R.string.empty));
                                        shortdescription.requestFocus();
                                    } else {
                                        dataforproductsave.put("shortdescription", shortdescription.getText().toString());
                                        if (stock.getText().toString().isEmpty()) {
                                            stock.setError(getResources().getString(R.string.empty));
                                            stock.requestFocus();
                                        } else {

                                               /* if (type.equals("simple") || type.equals("bundle"))
                                                {
                                                    if (weight.getText().toString().isEmpty()) {
                                                        weight.setError(getResources().getString(R.string.empty));
                                                        weight.requestFocus();
                                                    } else {
                                                        dataforproductsave.put("weight", weight.getText().toString());
                                                    }
                                                }
                                                if (type.equals("simple") || type.equals("bundle")) {
                                                    dataforproductsave.put("weight_type", String.valueOf(product_weight_type.getSelectedItem()));
                                                    dataforproductsave.put("price_type", String.valueOf(product_price_type.getSelectedItem()));
                                                    dataforproductsave.put("sku_type", String.valueOf(product_sku_type.getSelectedItem()));

                                                }
                                                dataforproductsave.put("price", price.getText().toString());
                                                dataforproductsave.put("tax_class", fortaxes.get(taxclass.getSelectedItem()));
                                                dataforproductsave.put("stock_avail", forstock.get(stockavail.getSelectedItem()));*/

                                            /****************************************************************************************************/

                                            if (type.equals("simple")) {

                                                dataforproductsave.put("weight", weight.getText().toString());

                                                if (price.getText().toString().isEmpty()) {
                                                    price.setError(getResources().getString(R.string.empty_price));
                                                    price.requestFocus();
                                                } else {
                                                    dataforproductsave.put("price", price.getText().toString());
                                                }

                                                dataforproductsave.put("tax_class", fortaxes.get(taxclass.getSelectedItem()));
                                                dataforproductsave.put("stock_avail", forstock.get(stockavail.getSelectedItem()));

                                                if (stock.getText().toString().isEmpty()) {
                                                    stock.setError(getResources().getString(R.string.empty_stock));
                                                    stock.requestFocus();
                                                } else {


                                                    dataforproductsave.put("stock", stock.getText().toString());
                                                }

                                          /*  if (product_sku.getText().toString().isEmpty()) {
                                                product_sku.setError(getResources().getString(R.string.emptyweight));
                                                product_sku.requestFocus();
                                            } else {
                                                dataforproductsave.put("product_sku", product_sku.getText().toString());
                                            }*/


                                            }
                                            if (type.equals("bundle")) {

                                                dataforproductsave.put("weight_type", String.valueOf(product_weight_type.getSelectedItem()));
                                                dataforproductsave.put("price_type", String.valueOf(product_price_type.getSelectedItem()));
                                                dataforproductsave.put("sku_type", String.valueOf(product_sku_type.getSelectedItem()));

                                                if (product_price_type.getSelectedItem().equals("fixed")) {
                                                    if (price.getText().toString().isEmpty()) {
                                                        price.setError(getResources().getString(R.string.empty_price));
                                                        price.requestFocus();
                                                    } else {
                                                        dataforproductsave.put("price", price.getText().toString());
                                                    }
                                                }

                                                if (product_weight_type.getSelectedItem().equals("fixed")) {
                                                    if (weight.getText().toString().isEmpty()) {
                                                        //weight.setError(getResources().getString(R.string.emptyweight));
                                                        weight.requestFocus();
                                                    } else {
                                                        dataforproductsave.put("weight", weight.getText().toString());
                                                    }
                                                }

                                                if (product_sku_type.getSelectedItem().equals("fixed")) {
                                                    if (product_sku.getText().toString().isEmpty()) {
                                                        product_sku.setError(getResources().getString(R.string.emptyweight));
                                                        product_sku.requestFocus();
                                                    } else {
                                                        dataforproductsave.put("product_sku", product_sku.getText().toString());
                                                    }
                                                }

                                                if (stock.getText().toString().isEmpty()) {
                                                    stock.setError(getResources().getString(R.string.empty_stock));
                                                    stock.requestFocus();
                                                } else {

                                                    dataforproductsave.put("stock", stock.getText().toString());
                                                }

                                            }

                                            /****************************************************************************************************/


                                            if (!(specialprice.getText().toString().isEmpty())) {
                                                dataforproductsave.put("special_price", specialprice.getText().toString());
                                                if (MultiVendor_specialprice_To.getText().toString().isEmpty() || MultiVendor_specialprice_From.getText().toString().isEmpty()) {
                                                    //Toast.makeText(getApplicationContext(),"Please fill Special Prices",Toast.LENGTH_LONG).show();
                                                } else {
                                                    dataforproductsave.put("special_from_date", MultiVendor_specialprice_From.getText().toString());
                                                    dataforproductsave.put("special_to_date", MultiVendor_specialprice_To.getText().toString());
                                                }
                                            }
                                            if (selected_category_ids.size() > 0) {
                                                Iterator categoryiditerator = selected_category_ids.iterator();
                                                JSONArray object1 = new JSONArray();

                                                while (categoryiditerator.hasNext()) {
                                                    String name = (String) categoryiditerator.next();
                                                    object1.put(name);

                                                }
                                                dataforproductsave.put("category", object1.toString());
                                            } else {
                                                dataforproductsave.put("category", "");
                                            }
                                            if (idsforwebsites.size() > 0) {
                                                Iterator idsforwebsite = idsforwebsites.iterator();
                                                JSONArray object1 = new JSONArray();

                                                while (idsforwebsite.hasNext()) {
                                                    String name = (String) idsforwebsite.next();
                                                    object1.put(name);

                                                }
                                                dataforproductsave.put("websites", object1.toString());
                                            } else {
                                                dataforproductsave.put("websites", "1");
                                            }
                                            if (functionalityList.getProductAddon()) {
                                                if (productattribute.getChildCount() > 0) {
                                                    int count = productattribute.getChildCount();
                                                    JSONObject object1 = new JSONObject();
                                                    for (int c = 0; c < count; c++) {
                                                        try {
                                                            LinearLayout linearLayout = (LinearLayout) productattribute.getChildAt(c);
                                                            TextView type_attribute = (TextView) linearLayout.getChildAt(2);
                                                            TextView name_attribute = (TextView) linearLayout.getChildAt(1);
                                                            String type_string = type_attribute.getText().toString();
                                                            String name_string = name_attribute.getText().toString();
                                                            if (type_string.equals("text")) {
                                                                EditText editText = (EditText) linearLayout.getChildAt(3);
                                                                if (editText.getText().toString().isEmpty()) {
                                                                    editText.setError(getResources().getString(R.string.empty));
                                                                    editText.requestFocus();
                                                                } else {
                                                                    object1.put(name_string, editText.getText().toString());
                                                                }

                                                            }
                                                            if (type_string.equals("textarea")) {
                                                                EditText editText = (EditText) linearLayout.getChildAt(3);
                                                                if (editText.getText().toString().isEmpty()) {
                                                                    editText.setError(getResources().getString(R.string.empty));
                                                                    editText.requestFocus();
                                                                } else {

                                                                    object1.put(name_string, editText.getText().toString());
                                                                }

                                                            }
                                                            if (type_string.equals("date") || type.equals("datetime")) {
                                                                DatePicker datepicker = (DatePicker) linearLayout.getChildAt(3);
                                                                int day = datepicker.getDayOfMonth();
                                                                int month = datepicker.getMonth() + 1;
                                                                int year = datepicker.getYear();
                                                                String date_string = day + "/" + month + "/" + year;
                                                                object1.put(name_string, date_string);
                                                            }
                                                            if (type_string.equals("select")) {
                                                                Spinner spinner = (Spinner) linearLayout.getChildAt(3);
                                                                object1.put(name_string, select_label_value.get(spinner.getSelectedItem()));
                                                            }
                                                            if (type_string.equals("multiselect")) {
                                                                LinearLayout multiselect = (LinearLayout) linearLayout.getChildAt(3);
                                                                JSONArray array = new JSONArray();
                                                                for (int m = 0; m < multiselect.getChildCount(); m++) {
                                                                    CheckBox checkBox = (CheckBox) multiselect.getChildAt(m);
                                                                    if (checkBox.isChecked()) {
                                                                        array.put(label_value.get(checkBox.getText().toString()));
                                                                    }

                                                                }
                                                                object1.put(name_string, array.toString());
                                                            }

                                                        } catch (Exception e) {
                                                            e.printStackTrace();
                                                        }
                                                        dataforproductsave.put("attribute", object1.toString());
                                                    }
                                                }

                                            }
                                            Log.i("vaibhav89", "params" + dataforproductsave);
                                            Ced_MultiVendor_ClientRequestResponse crr = new Ced_MultiVendor_ClientRequestResponse(new AsyncResponse() {
                                                @Override
                                                public void processFinish(Object output) throws JSONException {

                                                    Log.i("Vaibhav89", "Save");
                                                    Jstring = output.toString();
                                                    if (functionalityList.getExtensionAddon()) {
                                                        JSONObject object1 = new JSONObject(Jstring);
                                                        if (object1.has("vendor_approved")) {
                                                            logout();
                                                        } else {
                                                            String success = object1.getJSONObject("data").getString("success");
                                                            if (success.equals("true")) {
                                                                  /*  if (uris.size() > 0)
                                                                    {
                                                                        product_id = object1.getJSONObject("data").getString("product_id");
                                                                        Ced_MultiVendor_RequestForProductCreation crr = new Ced_MultiVendor_RequestForProductCreation(new AsyncResponse()
                                                                        {
                                                                            @Override
                                                                            public void processFinish(Object output) throws JSONException {
                                                                                Jstring = output.toString();
                                                                                if (functionalityList.getExtensionAddon())
                                                                                {

                                                                                    JSONObject object1 = new JSONObject(Jstring);
                                                                                    String successimage = object1.getJSONObject("data").getString("success");
                                                                                    if (successimage.equals("true")) {
                                                                                        Toast.makeText(Ced_MultiVendor_UpdateProduct.this, object1.getJSONObject("data").getString("message"), Toast.LENGTH_LONG).show();
                                                                                        Intent intent1 = new Intent(Ced_MultiVendor_UpdateProduct.this, Ced_MultiVendor_ManageProducts.class);
                                                                                        intent1.putExtra("Navigation", "productcreate");
                                                                                        intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                                                        startActivity(intent1);
                                                                                        overridePendingTransition(R.anim.ced_multivendor_slide_in, R.anim.ced_multivendor_slide_out);
                                                                                        Toast.makeText(Ced_MultiVendor_UpdateProduct.this, object1.getJSONObject("data").getString("message"), Toast.LENGTH_LONG).show();
                                                                                        finish();
                                                                                    }

                                                                                } else {
                                                                                    Intent intent = new Intent(getApplicationContext(), Ced_MultiVendor_UnAuthourizedRequestError.class);
                                                                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                                                                    startActivity(intent);
                                                                                    overridePendingTransition(R.anim.ced_multivendor_slide_in, R.anim.ced_multivendor_slide_out);
                                                                                }

                                                                                Log.i("vaiparams",""+defaultimagehash+"--"+uris);

                                                                            }
                                                                        }, Ced_MultiVendor_UpdateProduct.this, uris, product_id, defaultimagehash);
                                                                        crr.execute(updateurl);
                                                                    } */
                                                                //else {
                                                                Intent intent1 = new Intent(Ced_MultiVendor_UpdateProduct.this, Ced_MultiVendor_ManageProducts.class);
                                                                intent1.putExtra("Navigation", "productcreate");
                                                                intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                                startActivity(intent1);
                                                                overridePendingTransition(R.anim.ced_multivendor_slide_in, R.anim.ced_multivendor_slide_out);
                                                                //}
                                                            } else {
                                                                Toast.makeText(Ced_MultiVendor_UpdateProduct.this, object1.getJSONObject("data").getString("message"), Toast.LENGTH_LONG).show();
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
                                            }, Ced_MultiVendor_UpdateProduct.this, "POST", dataforproductsave);
                                            crr.execute(CurrentUrl);

                                        }
                                    }
                                }
                            }
                        }
                    }
                });
                /****************************saveproduct***************************/
                /****************************continue***************************/
                if (functionalityList.getProductAddon()) {
                    savecontinue.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            /************************************************************************/
                            dataforproductsave.put("vendor_id", session.getVendorid());
                            dataforproductsave.put("hashkey", session.getHahkey());
                            dataforproductsave.put("type", type);
                            dataforproductsave.put("product_id", product_id);
                            dataforproductsave.put("set", attribute_set);
                            if (product_name.getText().toString().isEmpty()) {
                                product_name.setError(getResources().getString(R.string.empty));
                                product_name.requestFocus();
                            } else {
                                dataforproductsave.put("product_name", product_name.getText().toString());
                                if (product_sku.getText().toString().isEmpty()) {
                                    product_sku.setError(getResources().getString(R.string.empty));
                                    product_sku.requestFocus();
                                } else {
                                    dataforproductsave.put("product_sku", product_sku.getText().toString());
                                    if (description.getText().toString().isEmpty()) {
                                        description.setError(getResources().getString(R.string.empty));
                                        description.requestFocus();
                                    } else {
                                        dataforproductsave.put("description", description.getText().toString());
                                        if (shortdescription.getText().toString().isEmpty()) {
                                            shortdescription.setError(getResources().getString(R.string.empty));
                                            shortdescription.requestFocus();
                                        } else {
                                            dataforproductsave.put("shortdescription", shortdescription.getText().toString());
                                            if (stock.getText().toString().isEmpty()) {
                                                stock.setError(getResources().getString(R.string.empty));
                                                stock.requestFocus();
                                            } else {


               /*                                     if (type.equals("simple")||type.equals("bundle"))
                                                    {
                                                        if (weight.getText().toString().isEmpty())
                                                        {
                                                            weight.setError(getResources().getString(R.string.empty));
                                                            weight.requestFocus();
                                                        }
                                                        else
                                                        {
                                                            dataforproductsave.put("weight", weight.getText().toString());
                                                        }
                                                    }
                                                    if (type.equals("simple")||type.equals("bundle"))
                                                    {
                                                        dataforproductsave.put("weight_type", String.valueOf(product_weight_type.getSelectedItem()));
                                                        dataforproductsave.put("price_type", String.valueOf(product_price_type.getSelectedItem()));
                                                        dataforproductsave.put("sku_type", String.valueOf(product_sku_type.getSelectedItem()));
*//* Log.i("checkmultipleimages","Array-- "+multiple_images_array.toString());
                Log.i("encodedimage",encodedproductImage);*//*
                                                    }


                                                    dataforproductsave.put("price", price.getText().toString());
                                                    dataforproductsave.put("tax_class", fortaxes.get(taxclass.getSelectedItem()));
                                                    dataforproductsave.put("stock_avail", forstock.get(stockavail.getSelectedItem()));*/


                                                /****************************************************************************************************/

                                                if (type.equals("simple")) {

                                                    dataforproductsave.put("weight", weight.getText().toString());

                                                    if (price.getText().toString().isEmpty()) {
                                                        price.setError(getResources().getString(R.string.empty_price));
                                                        price.requestFocus();
                                                    } else {
                                                        dataforproductsave.put("price", price.getText().toString());
                                                    }

                                                    dataforproductsave.put("tax_class", fortaxes.get(taxclass.getSelectedItem()));
                                                    dataforproductsave.put("stock_avail", forstock.get(stockavail.getSelectedItem()));

                                                    if (stock.getText().toString().isEmpty()) {
                                                        stock.setError(getResources().getString(R.string.empty_stock));
                                                        stock.requestFocus();
                                                    } else {


                                                        dataforproductsave.put("stock", stock.getText().toString());
                                                    }

                                          /*  if (product_sku.getText().toString().isEmpty()) {
                                                product_sku.setError(getResources().getString(R.string.emptyweight));
                                                product_sku.requestFocus();
                                            } else {
                                                dataforproductsave.put("product_sku", product_sku.getText().toString());
                                            }*/


                                                }
                                                if (type.equals("bundle")) {

                                                    dataforproductsave.put("weight_type", String.valueOf(product_weight_type.getSelectedItem()));
                                                    dataforproductsave.put("price_type", String.valueOf(product_price_type.getSelectedItem()));
                                                    dataforproductsave.put("sku_type", String.valueOf(product_sku_type.getSelectedItem()));

                                                    if (product_price_type.getSelectedItem().equals("fixed")) {
                                                        if (price.getText().toString().isEmpty()) {
                                                            price.setError(getResources().getString(R.string.empty_price));
                                                            price.requestFocus();
                                                        } else {
                                                            dataforproductsave.put("price", price.getText().toString());
                                                        }
                                                    }

                                                    if (product_weight_type.getSelectedItem().equals("fixed")) {
                                                        if (weight.getText().toString().isEmpty()) {
                                                            // weight.setError(getResources().getString(R.string.emptyweight));
                                                            weight.requestFocus();
                                                        } else {
                                                            dataforproductsave.put("weight", weight.getText().toString());
                                                        }
                                                    }

                                                    if (product_sku_type.getSelectedItem().equals("fixed")) {
                                                        if (product_sku.getText().toString().isEmpty()) {
                                                            product_sku.setError(getResources().getString(R.string.emptyweight));
                                                            product_sku.requestFocus();
                                                        } else {
                                                            dataforproductsave.put("product_sku", product_sku.getText().toString());
                                                        }
                                                    }

                                                    if (stock.getText().toString().isEmpty()) {
                                                        stock.setError(getResources().getString(R.string.empty_stock));
                                                        stock.requestFocus();
                                                    } else {

                                                        dataforproductsave.put("stock", stock.getText().toString());
                                                    }

                                                }

                                                /****************************************************************************************************/


                                                if (!(specialprice.getText().toString().isEmpty())) {
                                                    dataforproductsave.put("special_price", specialprice.getText().toString());
                                                    if (MultiVendor_specialprice_To.getText().toString().isEmpty() || MultiVendor_specialprice_From.getText().toString().isEmpty()) {
                                                        //  Toast.makeText(getApplicationContext(),"Please fill Special Prices",Toast.LENGTH_LONG).show();
                                                    } else {
                                                        dataforproductsave.put("special_from_date", MultiVendor_specialprice_From.getText().toString());
                                                        dataforproductsave.put("special_to_date", MultiVendor_specialprice_To.getText().toString());
                                                    }
                                                }
                                                if (selected_category_ids.size() > 0) {
                                                    Iterator categoryiditerator = selected_category_ids.iterator();
                                                    JSONArray object1 = new JSONArray();

                                                    while (categoryiditerator.hasNext()) {
                                                        String name = (String) categoryiditerator.next();
                                                        object1.put(name);

                                                    }
                                                    dataforproductsave.put("category", object1.toString());
                                                }
                                                if (idsforwebsites.size() > 0) {
                                                    Iterator idsforwebsite = idsforwebsites.iterator();
                                                    JSONArray object1 = new JSONArray();

                                                    while (idsforwebsite.hasNext()) {
                                                        String name = (String) idsforwebsite.next();
                                                        object1.put(name);

                                                    }
                                                    dataforproductsave.put("websites", object1.toString());
                                                }
                                                if (productattribute.getChildCount() > 0) {
                                                    int count = productattribute.getChildCount();
                                                    JSONObject object1 = new JSONObject();
                                                    for (int c = 0; c < count; c++) {
                                                        try {
                                                            LinearLayout linearLayout = (LinearLayout) productattribute.getChildAt(c);
                                                            TextView type_attribute = (TextView) linearLayout.getChildAt(2);
                                                            TextView name_attribute = (TextView) linearLayout.getChildAt(1);
                                                            String type_string = type_attribute.getText().toString();
                                                            String name_string = name_attribute.getText().toString();
                                                            if (type_string.equals("text")) {
                                                                EditText editText = (EditText) linearLayout.getChildAt(3);
                                                                if (editText.getText().toString().isEmpty()) {
                                                                    editText.setError(getResources().getString(R.string.empty));
                                                                    editText.requestFocus();
                                                                } else {
                                                                    object1.put(name_string, editText.getText().toString());
                                                                }

                                                            }
                                                            if (type_string.equals("textarea")) {
                                                                EditText editText = (EditText) linearLayout.getChildAt(3);
                                                                if (editText.getText().toString().isEmpty()) {
                                                                    editText.setError(getResources().getString(R.string.empty));
                                                                    editText.requestFocus();
                                                                } else {

                                                                    object1.put(name_string, editText.getText().toString());
                                                                }

                                                            }
                                                            if (type_string.equals("date") || type.equals("datetime")) {
                                                                DatePicker datepicker = (DatePicker) linearLayout.getChildAt(3);
                                                                int day = datepicker.getDayOfMonth();
                                                                int month = datepicker.getMonth() + 1;
                                                                int year = datepicker.getYear();
                                                                String date_string = day + "/" + month + "/" + year;
                                                                object1.put(name_string, date_string);
                                                            }
                                                            if (type_string.equals("select")) {
                                                                Spinner spinner = (Spinner) linearLayout.getChildAt(3);
                                                                object1.put(name_string, select_label_value.get(spinner.getSelectedItem()));
                                                            }
                                                            if (type_string.equals("multiselect")) {
                                                                LinearLayout multiselect = (LinearLayout) linearLayout.getChildAt(3);
                                                                JSONArray array = new JSONArray();
                                                                for (int m = 0; m < multiselect.getChildCount(); m++) {
                                                                    CheckBox checkBox = (CheckBox) multiselect.getChildAt(m);
                                                                    if (checkBox.isChecked()) {
                                                                        array.put(label_value.get(checkBox.getText().toString()));
                                                                    }

                                                                }
                                                                object1.put(name_string, array.toString());
                                                            }

                                                        } catch (Exception e) {
                                                            e.printStackTrace();
                                                        }
                                                        dataforproductsave.put("attribute", object1.toString());
                                                    }
                                                }
                                                Ced_MultiVendor_ClientRequestResponse crr = new Ced_MultiVendor_ClientRequestResponse(new AsyncResponse() {

                                                    @Override
                                                    public void processFinish(Object output) throws JSONException {
                                                        Log.i("Vaibhav89", "continue");
                                                        Jstring = output.toString();
                                                        if (functionalityList.getExtensionAddon()) {
                                                            JSONObject object1 = new JSONObject(Jstring);
                                                            if (object1.has("vendor_approved")) {
                                                                logout();
                                                            } else {
                                                                String success = object1.getJSONObject("data").getString("success");
                                                                if (success.equals("true")) {
                                                                    product_id = object1.getJSONObject("data").getString("product_id");

                                                                    if (imageselection.getChildCount() > 0) {

                                                                        /******************************************* *Images Section* (22-March-2018) ******************************/

                                                                        LinearLayout child_ll_container;
                                                                        ImageView img_product;
                                                                        TextView layou_id;
                                                                        CheckBox checkBox;

                                                                        for (int i = 0; i < imageselection.getChildCount(); i++) {
                                                                            child_ll_container = (LinearLayout) imageselection.getChildAt(i);

                                                                            layou_id = (TextView) child_ll_container.getChildAt(4);
                                                                            img_product = (ImageView) child_ll_container.getChildAt(0);
                                                                            checkBox = (CheckBox) child_ll_container.getChildAt(2);

                                                                            try {
                                                                                if (img_product.getDrawable() != null) {

                                                                                    JSONObject selected_images = new JSONObject();
                                                                                    selected_images.put("type", "image/png");
                                                                                    selected_images.put("name", "abc" + new Random().nextInt(100) + 1 + ".png");
                                                                                    if (checkBox.isChecked()) {
                                                                                        selected_images.put("default", true);
                                                                                    }
                                                                                    // Bitmap image_bm = ((BitmapDrawable) img_product.getDrawable()).getBitmap();
                                                                                    selected_images.put("base64_encoded_data", img_product.getTag());
                                                                                    selected_images.put("image_id", i);
                                                                                    multiple_images_array.put(selected_images);


                                                                                        /*JSONObject selected_images = new JSONObject();
                                                                                        selected_images.put("type", "file");
                                                                                        selected_images.put("name", "name" + i + ".jpeg");
                                                                                        if(checkBox.isChecked())
                                                                                        {
                                                                                            selected_images.put("default",true);
                                                                                        }
                                                                                        Bitmap image_bm = ((BitmapDrawable) img_product.getDrawable()).getBitmap();
                                                                                        selected_images.put("base64_encoded_data", encodeImage(image_bm));
                                                                                        selected_images.put("image_id", i);
                                                                                        multiple_images_array.put(selected_images);*/
                                                                                }
                                                                            } catch (Exception e) {
                                                                                e.printStackTrace();
                                                                            }

                                                                            //  Log.i("vaibhav_media",layou_id.getText() +"--"+((BitmapDrawable)product_image.getDrawable()).getBitmap());


                                                                        }
                                                                        Log.i("checkmultipleimages", "Array-- " + multiple_images_array.toString());


                                                                        dataforproductsave.put("images", multiple_images_array.toString());

                                                                        /****************************************************************************************/
                                                                        product_id = object1.getJSONObject("data").getString("product_id");

                                                                        Ced_MultiVendor_ClientRequestResponse crr = new Ced_MultiVendor_ClientRequestResponse(new AsyncResponse() {
                                                                            @Override
                                                                            public void processFinish(Object output) throws JSONException {
                                                                                Jstring = output.toString();
                                                                                if (functionalityList.getExtensionAddon()) {
                                                                                    JSONObject object1 = new JSONObject(Jstring);
                                                                                    String successimage = object1.getJSONObject("data").getString("success");
                                                                                    if (successimage.equals("true")) {
                                                                                        if (tabs.has("related")) {
                                                                                            Intent related = new Intent(Ced_MultiVendor_UpdateProduct.this, Ced_MultiVendor_Related.class);
                                                                                            related.putExtra("tabs", tabs.toString());
                                                                                            related.putExtra("product_id", product_id);
                                                                                            related.putExtra("type", type);
                                                                                            if (getResources().getString(R.string.Enable_Log).equals("yes")) {
                                                                                                Log.i("attribute_set", "" + attribute_set);
                                                                                            }
                                                                                            related.putExtra("attribute_set", attribute_set);
                                                                                            startActivity(related);
                                                                                            overridePendingTransition(R.anim.ced_multivendor_slide_in_sequence, R.anim.ced_multivendor_slide_out_sequence);
                                                                                        } else {
                                                                                            if (tabs.has("upsell")) {
                                                                                                Intent related = new Intent(Ced_MultiVendor_UpdateProduct.this, Ced_MultiVendor_upsell.class);
                                                                                                related.putExtra("tabs", tabs.toString());
                                                                                                related.putExtra("product_id", product_id);
                                                                                                related.putExtra("type", type);
                                                                                                related.putExtra("selectedwebsite", selected_websitetopost);
                                                                                                related.putExtra("attribute_set", attribute_set);
                                                                                                startActivity(related);
                                                                                                overridePendingTransition(R.anim.ced_multivendor_slide_in_sequence, R.anim.ced_multivendor_slide_out_sequence);
                                                                                            } else {
                                                                                                if (tabs.has("crosssell")) {
                                                                                                    Intent related = new Intent(Ced_MultiVendor_UpdateProduct.this, Ced_MultiVendor_crosssell.class);
                                                                                                    related.putExtra("tabs", tabs.toString());
                                                                                                    related.putExtra("product_id", product_id);
                                                                                                    related.putExtra("type", type);
                                                                                                    related.putExtra("selectedwebsite", selected_websitetopost);
                                                                                                    related.putExtra("attribute_set", attribute_set);
                                                                                                    startActivity(related);
                                                                                                    overridePendingTransition(R.anim.ced_multivendor_slide_in_sequence, R.anim.ced_multivendor_slide_out_sequence);
                                                                                                } else {
                                                                                                    if (tabs.has("customer_options")) {
                                                                                                        Intent related = new Intent(Ced_MultiVendor_UpdateProduct.this, Ced_MultiVendor_CustomOption.class);
                                                                                                        related.putExtra("tabs", tabs.toString());
                                                                                                        related.putExtra("product_id", product_id);
                                                                                                        related.putExtra("type", type);
                                                                                                        related.putExtra("selectedwebsite", selected_websitetopost);
                                                                                                        related.putExtra("attribute_set", attribute_set);
                                                                                                        startActivity(related);
                                                                                                        overridePendingTransition(R.anim.ced_multivendor_slide_in_sequence, R.anim.ced_multivendor_slide_out_sequence);
                                                                                                    } else {
                                                                                                        if (type.equals("configurable")) {
                                                                                                            Intent related = new Intent(Ced_MultiVendor_UpdateProduct.this, Ced_MultiVendor_Configurable.class);
                                                                                                            related.putExtra("tabs", tabs.toString());
                                                                                                            related.putExtra("product_id", product_id);
                                                                                                            related.putExtra("type", type);
                                                                                                            related.putExtra("selectedwebsite", selected_websitetopost);
                                                                                                            related.putExtra("attribute_set", attribute_set);
                                                                                                            startActivity(related);
                                                                                                            overridePendingTransition(R.anim.ced_multivendor_slide_in_sequence, R.anim.ced_multivendor_slide_out_sequence);
                                                                                                        }
                                                                                                        if (type.equals("downloadable")) {
                                                                                                            Intent related = new Intent(Ced_MultiVendor_UpdateProduct.this, Ced_MultiVendor_Downloadable.class);
                                                                                                            related.putExtra("tabs", tabs.toString());
                                                                                                            related.putExtra("product_id", product_id);
                                                                                                            related.putExtra("type", type);
                                                                                                            related.putExtra("selectedwebsite", selected_websitetopost);
                                                                                                            related.putExtra("attribute_set", attribute_set);
                                                                                                            startActivity(related);
                                                                                                            overridePendingTransition(R.anim.ced_multivendor_slide_in_sequence, R.anim.ced_multivendor_slide_out_sequence);
                                                                                                        }
                                                                                                        if (type.equals("bundle")) {
                                                                                                            Intent related = new Intent(Ced_MultiVendor_UpdateProduct.this, Ced_MultiVendor_BundleItems.class);
                                                                                                            related.putExtra("tabs", tabs.toString());
                                                                                                            related.putExtra("product_id", product_id);
                                                                                                            related.putExtra("type", type);
                                                                                                            related.putExtra("selectedwebsite", selected_websitetopost);
                                                                                                            related.putExtra("attribute_set", attribute_set);
                                                                                                            startActivity(related);
                                                                                                            overridePendingTransition(R.anim.ced_multivendor_slide_in_sequence, R.anim.ced_multivendor_slide_out_sequence);
                                                                                                        }
                                                                                                        if (type.equals("grouped")) {
                                                                                                            Intent related = new Intent(Ced_MultiVendor_UpdateProduct.this, Ced_MultiVendor_GroupItems.class);
                                                                                                            related.putExtra("tabs", tabs.toString());
                                                                                                            related.putExtra("product_id", product_id);
                                                                                                            related.putExtra("type", type);
                                                                                                            related.putExtra("selectedwebsite", selected_websitetopost);
                                                                                                            related.putExtra("attribute_set", attribute_set);
                                                                                                            startActivity(related);
                                                                                                            overridePendingTransition(R.anim.ced_multivendor_slide_in_sequence, R.anim.ced_multivendor_slide_out_sequence);
                                                                                                        }
                                                                                                        if (type.equals("simple")) {
                                                                                                            Intent related = new Intent(Ced_MultiVendor_UpdateProduct.this, Ced_MultiVendor_ManageProducts.class);
                                                                                                            related.putExtra("Navigation", "productcreate");
                                                                                                            related.putExtra("selectedwebsite", selected_websitetopost);
                                                                                                            startActivity(related);
                                                                                                            overridePendingTransition(R.anim.ced_multivendor_slide_in_sequence, R.anim.ced_multivendor_slide_out_sequence);
                                                                                                        }
                                                                                                        if (type.equals("virtual")) {
                                                                                                            Intent related = new Intent(Ced_MultiVendor_UpdateProduct.this, Ced_MultiVendor_ManageProducts.class);
                                                                                                            related.putExtra("Navigation", "productcreate");
                                                                                                            related.putExtra("selectedwebsite", selected_websitetopost);
                                                                                                            startActivity(related);
                                                                                                            overridePendingTransition(R.anim.ced_multivendor_slide_in_sequence, R.anim.ced_multivendor_slide_out_sequence);
                                                                                                        }
                                                                                                    }
                                                                                                }
                                                                                            }
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
                                                                        }, Ced_MultiVendor_UpdateProduct.this, "POST", dataforproductsave);
                                                                        crr.execute(updateurl);
                                                                    } else {
                                                                        if (tabs.has("related")) {
                                                                            Intent related = new Intent(Ced_MultiVendor_UpdateProduct.this, Ced_MultiVendor_Related.class);
                                                                            related.putExtra("tabs", tabs.toString());
                                                                            related.putExtra("product_id", product_id);
                                                                            related.putExtra("type", type);
                                                                            if (getResources().getString(R.string.Enable_Log).equals("yes")) {
                                                                                Log.i("attribute_set", "" + attribute_set);
                                                                            }
                                                                            related.putExtra("attribute_set", attribute_set);
                                                                            startActivity(related);
                                                                            overridePendingTransition(R.anim.ced_multivendor_slide_in_sequence, R.anim.ced_multivendor_slide_out_sequence);
                                                                        } else {
                                                                            if (tabs.has("upsell")) {
                                                                                Intent related = new Intent(Ced_MultiVendor_UpdateProduct.this, Ced_MultiVendor_upsell.class);
                                                                                related.putExtra("tabs", tabs.toString());
                                                                                related.putExtra("product_id", product_id);
                                                                                related.putExtra("type", type);
                                                                                related.putExtra("selectedwebsite", selected_websitetopost);
                                                                                if (getResources().getString(R.string.Enable_Log).equals("yes")) {
                                                                                    Log.i("attribute_set", "" + attribute_set);
                                                                                }
                                                                                related.putExtra("attribute_set", attribute_set);
                                                                                startActivity(related);
                                                                                overridePendingTransition(R.anim.ced_multivendor_slide_in_sequence, R.anim.ced_multivendor_slide_out_sequence);
                                                                            } else {
                                                                                if (tabs.has("crosssell")) {
                                                                                    Intent related = new Intent(Ced_MultiVendor_UpdateProduct.this, Ced_MultiVendor_crosssell.class);
                                                                                    related.putExtra("tabs", tabs.toString());
                                                                                    related.putExtra("product_id", product_id);
                                                                                    related.putExtra("type", type);
                                                                                    if (getResources().getString(R.string.Enable_Log).equals("yes")) {
                                                                                        Log.i("attribute_set", "" + attribute_set);
                                                                                    }
                                                                                    related.putExtra("attribute_set", attribute_set);
                                                                                    startActivity(related);
                                                                                    overridePendingTransition(R.anim.ced_multivendor_slide_in_sequence, R.anim.ced_multivendor_slide_out_sequence);
                                                                                } else {
                                                                                    if (tabs.has("customer_options")) {
                                                                                        Intent related = new Intent(Ced_MultiVendor_UpdateProduct.this, Ced_MultiVendor_CustomOption.class);
                                                                                        related.putExtra("tabs", tabs.toString());
                                                                                        related.putExtra("product_id", product_id);
                                                                                        related.putExtra("type", type);
                                                                                        if (getResources().getString(R.string.Enable_Log).equals("yes")) {
                                                                                            Log.i("attribute_set", "" + attribute_set);
                                                                                        }
                                                                                        related.putExtra("attribute_set", attribute_set);
                                                                                        startActivity(related);
                                                                                        overridePendingTransition(R.anim.ced_multivendor_slide_in_sequence, R.anim.ced_multivendor_slide_out_sequence);
                                                                                    } else {
                                                                                        if (type.equals("configurable")) {
                                                                                            Intent related = new Intent(Ced_MultiVendor_UpdateProduct.this, Ced_MultiVendor_Configurable.class);
                                                                                            related.putExtra("tabs", tabs.toString());
                                                                                            related.putExtra("product_id", product_id);
                                                                                            related.putExtra("type", type);
                                                                                            if (getResources().getString(R.string.Enable_Log).equals("yes")) {
                                                                                                Log.i("attribute_set", "" + attribute_set);
                                                                                            }
                                                                                            related.putExtra("attribute_set", attribute_set);
                                                                                            startActivity(related);
                                                                                            overridePendingTransition(R.anim.ced_multivendor_slide_in_sequence, R.anim.ced_multivendor_slide_out_sequence);
                                                                                        }
                                                                                        if (type.equals("downloadable")) {
                                                                                            Intent related = new Intent(Ced_MultiVendor_UpdateProduct.this, Ced_MultiVendor_Downloadable.class);
                                                                                            related.putExtra("tabs", tabs.toString());
                                                                                            related.putExtra("product_id", product_id);
                                                                                            related.putExtra("type", type);
                                                                                            if (getResources().getString(R.string.Enable_Log).equals("yes")) {
                                                                                                Log.i("attribute_set", "" + attribute_set);
                                                                                            }
                                                                                            related.putExtra("attribute_set", attribute_set);
                                                                                            startActivity(related);
                                                                                            overridePendingTransition(R.anim.ced_multivendor_slide_in_sequence, R.anim.ced_multivendor_slide_out_sequence);
                                                                                        }

                                                                                        if (type.equals("bundle")) {
                                                                                            Intent related = new Intent(Ced_MultiVendor_UpdateProduct.this, Ced_MultiVendor_BundleItems.class);
                                                                                            related.putExtra("tabs", tabs.toString());
                                                                                            related.putExtra("product_id", product_id);
                                                                                            related.putExtra("type", type);
                                                                                            if (getResources().getString(R.string.Enable_Log).equals("yes")) {
                                                                                                Log.i("attribute_set", "" + attribute_set);
                                                                                            }
                                                                                            related.putExtra("attribute_set", attribute_set);
                                                                                            startActivity(related);
                                                                                            overridePendingTransition(R.anim.ced_multivendor_slide_in_sequence, R.anim.ced_multivendor_slide_out_sequence);
                                                                                        }

                                                                                        if (type.equals("grouped")) {
                                                                                            Intent related = new Intent(Ced_MultiVendor_UpdateProduct.this, Ced_MultiVendor_GroupItems.class);
                                                                                            related.putExtra("tabs", tabs.toString());
                                                                                            related.putExtra("product_id", product_id);
                                                                                            related.putExtra("type", type);
                                                                                            if (getResources().getString(R.string.Enable_Log).equals("yes")) {
                                                                                                Log.i("attribute_set", "" + attribute_set);
                                                                                            }
                                                                                            related.putExtra("attribute_set", attribute_set);
                                                                                            startActivity(related);
                                                                                            overridePendingTransition(R.anim.ced_multivendor_slide_in_sequence, R.anim.ced_multivendor_slide_out_sequence);
                                                                                        }
                                                                                        if (type.equals("simple")) {
                                                                                            Intent related = new Intent(Ced_MultiVendor_UpdateProduct.this, Ced_MultiVendor_ManageProducts.class);
                                                                                            related.putExtra("Navigation", "productcreate");
                                                                                            startActivity(related);
                                                                                            overridePendingTransition(R.anim.ced_multivendor_slide_in_sequence, R.anim.ced_multivendor_slide_out_sequence);
                                                                                        }
                                                                                        if (type.equals("virtual")) {
                                                                                            Intent related = new Intent(Ced_MultiVendor_UpdateProduct.this, Ced_MultiVendor_ManageProducts.class);
                                                                                            related.putExtra("Navigation", "productcreate");
                                                                                            startActivity(related);
                                                                                            overridePendingTransition(R.anim.ced_multivendor_slide_in_sequence, R.anim.ced_multivendor_slide_out_sequence);
                                                                                        }
                                                                                    }
                                                                                }
                                                                            }
                                                                        }
                                                                    }
                                                                } else {
                                                                    Toast.makeText(Ced_MultiVendor_UpdateProduct.this, object1.getJSONObject("data").getString("message"), Toast.LENGTH_LONG).show();
                                                                }
                                                            }
                                                        } else {
                                                            Intent intent = new Intent(getApplicationContext(), Ced_MultiVendor_UnAuthourizedRequestError.class);
                                                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                                            startActivity(intent);
                                                            overridePendingTransition(R.anim.ced_multivendor_slide_in, R.anim.ced_multivendor_slide_out);
                                                        }
                                                        Log.i("vaibhavParams", "" + dataforproductsave);
                                                    }
                                                }, Ced_MultiVendor_UpdateProduct.this, "POST", dataforproductsave);
                                                crr.execute(CurrentUrl);

                                            }
                                        }
                                    }
                                }
                            }
                            /************************************************************************/

                        }
                    });
                }

                /****************************continue***************************/
                /****************************addimages***************************/

                addimagees.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //    addimageLayout();

                        View layout = View.inflate(Ced_MultiVendor_UpdateProduct.this, R.layout.ced_multivendor_add_multiple_images, null);
                        final LinearLayout add_multiple_images_layout = layout.findViewById(R.id.multiple_images_layout);
                        product_image = layout.findViewById(R.id.MultiVendor_productimage);
                        browse_product_image = layout.findViewById(R.id.MultiVendor_browseproductimage);
                        final ImageView remove_image = layout.findViewById(R.id.remove_image);
                        final CheckBox default_checkbox = layout.findViewById(R.id.default_checkbox);
                        final TextView layuot_id = layout.findViewById(R.id.layout_id);
                        layuot_id.setText(String.valueOf(count - 1));
                        imageselection.addView(add_multiple_images_layout);
                        count++;

                        remove_image.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                //add_multiple_images_layout.removeViewAt(Integer.parseInt(layuot_id.getText().toString()));
                               /* Toast.makeText(Ced_MultiVendor_UpdateProduct.this, "lo remove"+layuot_id.getText(), Toast.LENGTH_SHORT).show();
                                imageselection.removeViewAt(Integer.parseInt(layuot_id.getText().toString()));
                                Log.i("vaibhavarray",""+multiple_images_array.toString());*/

                                LinearLayout parent_ll = (LinearLayout) remove_image.getParent();

                                imageselection.removeView(parent_ll);

                                Log.i("vaibhavarray", "" + multiple_images_array.toString());

                               /* String current = layuot_id.getText().toString();
                                try {
                                    for(int i = 0 ;i<multiple_images_array.length();i++)
                                    {
                                        JSONObject current_object = multiple_images_array.getJSONObject(i);
                                            if (current == current_object.getString("image_id"))
                                            {
                                                multiple_images_array.remove(Integer.parseInt(layuot_id.getText().toString()));
                                                Toast.makeText(Ced_MultiVendor_UpdateProduct.this, current_object.getString("image_id")+"lo in"+layuot_id.getText(), Toast.LENGTH_SHORT).show();
                                                Log.i("vaibhavarray",""+multiple_images_array.toString());
                                            }


                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }*/
                            }
                        });

                        default_checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                            @Override
                            public void onCheckedChanged(CompoundButton compoundButton, boolean ischecked) {
                                if (ischecked) {
                                    if (is_default_check) {
                                        Toast.makeText(Ced_MultiVendor_UpdateProduct.this, R.string.onlyoneimagecanbeselected, Toast.LENGTH_SHORT).show();
                                        default_checkbox.setChecked(false);
                                        is_default_check = false;
                                    } else {
                                        /*try {
                                            for(int i = 0 ;i<multiple_images_array.length();i++)
                                            {
                                                JSONObject current_object = multiple_images_array.getJSONObject(i);
                                                if(current_object.getString("image_id").equals(layuot_id.getText().toString()))
                                                {
                                                    current_object.put("default",true);
                                                    Log.i("vaibhavcheck","default"+current_object);
                                                }
                                                else
                                                {
                                                    if(current_object.has("deafult"))
                                                    {
                                                        current_object.remove("default");
                                                        Log.i("vaibhavcheck","remove_default");
                                                    }
                                                }
                                        *//*JSONObject current_object = (JSONObject) multiple_images_array.get(Integer.parseInt(layuot_id.getText().toString()));
                                        current_object.put("default", true);*//*
                                            }
                                    *//*Log.i("currentobject","current object "+current_object.toString());
                                    Log.i("currentobject","length-- "+multiple_images_array.length());*//*
                                        }
                                        catch (JSONException e)
                                        {
                                            e.printStackTrace();
                                        }*/

                                        Log.i("imagechecked", "" + layuot_id.getText().toString());
                                        is_default_check = true;
                                    }
                                }
                        /*else{
                            is_default_check = false;
                        }*/
                            }
                        });
                        browse_product_image.setOnClickListener(new View.OnClickListener() {

                            @Override
                            public void onClick(View view) {
                                image_id = layuot_id.getText().toString();
                                if (writeresult == PackageManager.PERMISSION_GRANTED && readresult == PackageManager.PERMISSION_GRANTED) {
                                    Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                                    photoPickerIntent.setType("image/*");
                                    SELECT_PHOTO = 1;
                                    startActivityForResult(photoPickerIntent, 1);

                                } else {

                                    if (ActivityCompat.shouldShowRequestPermissionRationale(Ced_MultiVendor_UpdateProduct.this, Manifest.permission.READ_EXTERNAL_STORAGE) && ActivityCompat.shouldShowRequestPermissionRationale(Ced_MultiVendor_UpdateProduct.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

                                        Toast.makeText(Ced_MultiVendor_UpdateProduct.this, "Storage permission allows us to access data. Please allow in App Settings for additional functionality.", Toast.LENGTH_LONG).show();

                                    } else {

                                        ActivityCompat.requestPermissions(Ced_MultiVendor_UpdateProduct.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                                    }
                                }
                            }
                        });

                    }
                });
                /****************************addimages***************************/
                /****************************addproductattribute***************************/
                if (functionalityList.getProductAddon()) {
                    attributes = object.getJSONObject("data").getJSONArray("attributes");
                    if (attributes.length() > 0) {
                        for (int a = 0; a < attributes.length(); a++) {
                            HashMap<String, String> attribute = new HashMap<>();
                            JSONObject jsonObject = attributes.getJSONObject(a);
                            attribute.put("name", jsonObject.getString("name"));
                            attribute.put("label", jsonObject.getString("label"));
                            attribute.put("type", jsonObject.getString("type"));
                            if (jsonObject.has("value")) {
                                attribute.put("value", jsonObject.getString("value"));
                            }
                            if (jsonObject.has("att_value")) {
                                attribute.put("att_value", jsonObject.getString("att_value"));
                            }
                            if (jsonObject.getString("type").equals("select")) {
                                JSONArray attribute_values = jsonObject.getJSONArray("values");
                                ArrayList<String> val = new ArrayList<>();
                                for (int v = 0; v < attribute_values.length(); v++) {
                                    JSONObject object1 = attribute_values.getJSONObject(v);
                                    select_label_value.put(object1.getString("label"), object1.getString("value"));
                                    select_value_label.put(object1.getString("value"), object1.getString("label"));
                                    val.add(object1.getString("label"));
                                }
                                multiselect_select.put(jsonObject.getString("name"), val);
                            }
                            if (jsonObject.getString("type").equals("multiselect")) {
                                JSONArray attribute_values = jsonObject.getJSONArray("values");
                                ArrayList<String> val = new ArrayList<>();
                                for (int v = 0; v < attribute_values.length(); v++) {
                                    JSONObject object1 = attribute_values.getJSONObject(v);
                                    label_value.put(object1.getString("label"), object1.getString("value"));
                                    val.add(object1.getString("label"));

                                }
                                multiselect_select.put(jsonObject.getString("name"), val);
                            }
                            attributesarraylist.add(attribute);
                        }

                        Log.i("vaibhavtest", "attriarray" + attributesarraylist.toString());

                        Iterator attributelistiterator = attributesarraylist.iterator();
                        while (attributelistiterator.hasNext()) {
                            HashMap<String, String> attributehash = (HashMap<String, String>) attributelistiterator.next();

                            Log.i("vaibhavtest", "attrihash" + attributehash);

                            String type = attributehash.get("type");
                            if (type.equals("select")) {
                                View view = View.inflate(getApplicationContext(), R.layout.ced_multivendor_attribute_select_layout, null);
                                TextView name = view.findViewById(R.id.MultiVendor_name);
                                TextView label = view.findViewById(R.id.MultiVendor_label);
                                Spinner spinattribuite = view.findViewById(R.id.MultiVendor_spinattribuite);
                                if (attributehash.get("att_value").isEmpty()) {

                                    ArrayAdapter<String> adp_attribute = new ArrayAdapter<>(getApplicationContext(), R.layout.ced_multivendor_textview, multiselect_select.get(attributehash.get("name")));
                                    spinattribuite.setAdapter(adp_attribute);
                                } else {
                                    ArrayList<String> arrayList = new ArrayList<>();
                                    arrayList.add(attributehash.get("att_value"));
                                    Iterator iterator = multiselect_select.get(attributehash.get("name")).iterator();
                                    while (iterator.hasNext()) {
                                        String val_label = (String) iterator.next();
                                        if (val_label.equals(attributehash.get("att_value"))) {
                                            continue;
                                        } else {
                                            arrayList.add(val_label);
                                        }
                                    }
                                    ArrayAdapter<String> adp_attribute = new ArrayAdapter<>(getApplicationContext(), R.layout.ced_multivendor_textview, arrayList);
                                    spinattribuite.setAdapter(adp_attribute);
                                }
                                name.setText(attributehash.get("name"));
                                label.setText(attributehash.get("label"));
                                TextView type_attribute = view.findViewById(R.id.MultiVendor_type);
                                type_attribute.setText(type);
                                productattribute.addView(view);
                            }
                            if (type.equals("text") || type.equals("textarea")) {
                                View view = View.inflate(getApplicationContext(), R.layout.ced_multivendor_attribute_text_layout, null);
                                TextView name = view.findViewById(R.id.MultiVendor_name);
                                TextView label = view.findViewById(R.id.MultiVendor_label);
                                EditText textattribuite = view.findViewById(R.id.MultiVendor_textattribuite);
                                textattribuite.setText(attributehash.get("value"));
                                name.setText(attributehash.get("name"));
                                label.setText(attributehash.get("label"));
                                TextView type_attribute = view.findViewById(R.id.MultiVendor_type);
                                type_attribute.setText(type);
                                productattribute.addView(view);
                            }
                            if (type.equals("date") || type.equals("datetime")) {
                                View view = View.inflate(getApplicationContext(), R.layout.ced_multivendor_attribute_date_layout, null);
                                TextView name = view.findViewById(R.id.MultiVendor_name);
                                TextView label = view.findViewById(R.id.MultiVendor_label);
                                String dtStart = attributehash.get("value");
                                if (dtStart.isEmpty()) {
                                    if (getResources().getString(R.string.Enable_Log).equals("yes")) {
                                        Log.i("NoDate", "In");
                                    }
                                } else {
                                    String[] dateparts = dtStart.split(" ");
                                    if (getResources().getString(R.string.Enable_Log).equals("yes")) {
                                        Log.i("picker_date", "" + dateparts[0]);
                                    }
                                    String[] parts = dateparts[0].split("-");
                                    DatePicker picker = view.findViewById(R.id.MultiVendor_datePicker);
                                    picker.updateDate(Integer.parseInt(parts[2]), Integer.parseInt(parts[1]), Integer.parseInt(parts[0]));
                                }
                                name.setText(attributehash.get("name"));
                                label.setText(attributehash.get("label"));
                                TextView type_attribute = view.findViewById(R.id.MultiVendor_type);
                                type_attribute.setText(type);
                                productattribute.addView(view);
                            }
                            if (type.equals("multiselect")) {
                                View view = View.inflate(getApplicationContext(), R.layout.ced_multivendor_attribute_multiselect_layout, null);
                                TextView name = view.findViewById(R.id.MultiVendor_name);
                                TextView label = view.findViewById(R.id.MultiVendor_label);
                                LinearLayout multiselectlayout = view.findViewById(R.id.MultiVendor_multiselectlayout);
                                if (attributehash.containsKey("value")) //Change 10-Apr-2018
                                {
                                    ArrayList<String> list = new ArrayList<>();
                                    String[] parts = attributehash.get("value").split(",");
                                    for (int i = 0; i < parts.length; i++) {
                                        list.add(parts[i]);
                                    }
                                    Iterator iterator2 = multiselect_select.get(attributehash.get("name")).iterator();
                                    int id = Resources.getSystem().getIdentifier("btn_check_holo_light", "drawable", "android");
                                    while (iterator2.hasNext()) {
                                        String checkboxlabel = (String) iterator2.next();
                                        CheckBox checkBox = new CheckBox(getApplicationContext());
                                        checkBox.setText(checkboxlabel);
                                        if (list.contains(label_value.get(checkboxlabel))) {
                                            checkBox.setChecked(true);
                                        }
                                        checkBox.setTextColor(getResources().getColor(R.color.black));
                                        checkBox.setButtonDrawable(id);
                                        multiselectlayout.addView(checkBox);
                                    }

                                } else {
                                    Iterator iterator2 = multiselect_select.get(attributehash.get("name")).iterator();
                                    int id = Resources.getSystem().getIdentifier("btn_check_holo_light", "drawable", "android");
                                    while (iterator2.hasNext()) {
                                        CheckBox checkBox = new CheckBox(getApplicationContext());
                                        checkBox.setText((CharSequence) iterator2.next());
                                        checkBox.setTextColor(getResources().getColor(R.color.black));
                                        checkBox.setButtonDrawable(id);
                                        multiselectlayout.addView(checkBox);
                                    }
                                }
                                name.setText(attributehash.get("name"));
                                label.setText(attributehash.get("label"));
                                TextView type_attribute = view.findViewById(R.id.MultiVendor_type);
                                type_attribute.setText(type);
                                productattribute.addView(view);
                            }

                        }
                    }
                }
                /****************************addproductattribute***************************/
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            Intent nointernet = new Intent(Ced_MultiVendor_UpdateProduct.this, Ced_MultiVendor_NoInternetconnection.class);
            nointernet.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            nointernet.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(nointernet);
            overridePendingTransition(R.anim.ced_multivendor_slide_in, R.anim.ced_multivendor_slide_out);
        }

    }

    @Override
    protected void onResume() {
        Ced_MultiVendor_ConnectionDetector connectionDetector = new Ced_MultiVendor_ConnectionDetector(Ced_MultiVendor_UpdateProduct.this);
        if (connectionDetector.isConnectingToInternet()) {
            setname(session.getvendorname());
            setprofilepic(session.getvendorpic());
            changetitle("Edit Product");
            writeresult = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
            readresult = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE);
            //   invalidateOptionsMenu();
            super.onResume();
        } else {
            Intent intent = new Intent(getApplicationContext(), Ced_MultiVendor_NoInternetconnection.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            super.onResume();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);

        if (resultCode == RESULT_OK) {
            try {
                final Uri imageUri = imageReturnedIntent.getData();
                producturi = getRealPathFromURI(Ced_MultiVendor_UpdateProduct.this, imageUri);
                Log.d("FileSize", "onActivityResult: " + calculateFileSize(producturi));
                if (calculateFileSize(producturi) > 5.0) {
                    Toast.makeText(this, "Image should be less than 5 MB", Toast.LENGTH_SHORT).show();
                } else {
                    String[] imagename = getRealPathFromURI(Ced_MultiVendor_UpdateProduct.this, imageUri).split("/");
                    if (getResources().getString(R.string.Enable_Log).equals("yes")) {
                        Log.i("filename", "" + imagename[imagename.length - 1]);
                    }
                    productpicname = imagename[imagename.length - 1];
                    final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                    Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
//                    selectedImage = getResizedBitmap(selectedImage, 100, 100);
                    product_image.setImageBitmap(selectedImage);
                    encodedproductImage = encodeImage(selectedImage);
                    product_image.setTag(encodedproductImage);
                    try {
                   /* if(!encodedproductImage.equals("")) {
                        JSONObject selected_images = new JSONObject();
                        selected_images.put("type", "file");
                        selected_images.put("name", "name"+image_id+".jpeg");
                        selected_images.put("base64_encoded_data", encodedproductImage);
                        selected_images.put("image_id",image_id);
                        multiple_images_array.put(selected_images);
                        Log.i("checkmultipleimages", "Array-- " + multiple_images_array.toString());
                    }*/
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    Log.i("checkmultipleimages", "Array-- " + multiple_images_array.toString());
                    Log.i("encodedimage", encodedproductImage);
                }

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

/*    public Bitmap getResizedBitmap(Bitmap bm, int newWidth, int newHeight) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // CREATE A MATRIX FOR THE MANIPULATION
        Matrix matrix = new Matrix();
        // RESIZE THE BIT MAP
        matrix.postScale(scaleWidth, scaleHeight);

        // "RECREATE" THE NEW BITMAP
        Bitmap resizedBitmap = Bitmap.createBitmap(
                bm, 0, 0, width, height, matrix, false);
        bm.recycle();
        return resizedBitmap;
    }*/

   /* public float calculateFileSize(String filepath) {
        //String filepathstr=filepath.toString();
        File file = new File(filepath);
        long fileSizeInBytes = file.length();

        float fileSizeInKB = fileSizeInBytes / 1024;
        // Convert the KB to MegaBytes (1 MB = 1024 KBytes)
        float fileSizeInMB = fileSizeInKB / 1024;

        return fileSizeInMB;
    }*/

    public String getRealPathFromURI(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = {MediaStore.Images.Media.DATA};
            cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
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

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
            photoPickerIntent.setType("image/*");
            SELECT_PHOTO = 1;
            startActivityForResult(photoPickerIntent, 1);
        } else {
            Toast.makeText(getApplicationContext(), "Permission Denied", Toast.LENGTH_LONG).show();
        }

    }

    private String encodeImage(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 40, baos);
        byte[] b = baos.toByteArray();

        Bitmap compressedBitmap = BitmapFactory.decodeByteArray(b, 0, b.length);
        compressedBitmap.compress(Bitmap.CompressFormat.PNG, 50, baos);
        byte[] bytearray = baos.toByteArray();
        String encImage = Base64.encodeToString(bytearray, Base64.DEFAULT);

        return encImage;
    }

    private void createsubcat(JSONArray array, LinearLayout subcats) {
        try {
            JSONObject current_object = null;
            View category_view = null;
            TextView subcategories = null;
            CheckBox main_cat = null;
            ImageView cat_icon = null;
            String maincatname = null;
            String maincatid = null;
            String[] parts = new String[2];
            for (int i = 0; i < array.length(); i++) {
                current_object = array.getJSONObject(i);
                category_view = View.inflate(Ced_MultiVendor_UpdateProduct.this, R.layout.ced_multivendor_maincategory, null);
                subcategories = category_view.findViewById(R.id.subcategories);
                main_cat = category_view.findViewById(R.id.main_cat);
                cat_icon = category_view.findViewById(R.id.cat_icon);
                parts = current_object.getString("main_category").split("#");
                maincatname = parts[1];
                maincatid = parts[0];
                if (current_object.has("sub_categories")) {
                    subcategories.setText(current_object.getJSONArray("sub_categories").toString());
                } else {
                    cat_icon.setVisibility(View.GONE);
                }
                main_cat.setText(maincatname);
                main_cat.setId(Integer.parseInt(maincatid));
                final CheckBox finalMain_cat = main_cat;
                main_cat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked) {
                            selected_category_ids.add(String.valueOf(finalMain_cat.getId()));
                        } else {
                            selected_category_ids.remove(String.valueOf(finalMain_cat.getId()));
                        }
                    }
                });
                if (selected_category_ids.contains(maincatid)) {
                    main_cat.setChecked(true);
                }
                final boolean[] open = {false};
                final ImageView finalCat_icon = cat_icon;
                final ImageView finalCat_icon1 = cat_icon;
                cat_icon.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        LinearLayout parent = (LinearLayout) finalCat_icon.getParent();
                        LinearLayout subparent = (LinearLayout) parent.getParent();
                        RelativeLayout mainparent = (RelativeLayout) subparent.getParent();
                        TextView subcatjson = (TextView) mainparent.getChildAt(0);
                        LinearLayout subcats = (LinearLayout) subparent.getChildAt(1);
                        if (open[0]) {
                            subcats.removeAllViews();
                            open[0] = false;
                            finalCat_icon1.setImageResource(R.drawable.expand);
                        } else {
                            try {
                                if (!(subcatjson.getText().toString().isEmpty())) {
                                    JSONArray array = new JSONArray(subcatjson.getText().toString());
                                    createsubcat(array, subcats);
                                    open[0] = true;
                                    finalCat_icon1.setImageResource(R.drawable.collapse);
                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });
                subcats.addView(category_view);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
