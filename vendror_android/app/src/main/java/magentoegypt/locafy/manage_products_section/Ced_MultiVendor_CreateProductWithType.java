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
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
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
import android.provider.Settings;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.DialogFragment;

import magentoegypt.locafy.base_app.Ced_MultiVendor_FontSetting;
import magentoegypt.locafy_constant.Ced_MultiVendor_GlobalVariables;
import magentoegypt.locafy.base_app.Ced_MultiVendor_VendorFunctionalityList;
import magentoegypt.locafy.navigation_drawer.Activity.Ced_MultiVendor_NavigationActivity;
import magentoegypt.locafy.api_request_response_section.network_error_handling.Ced_MultiVendor_ConnectionDetector;
import magentoegypt.locafy.api_request_response_section.network_error_handling.Ced_MultiVendor_NoInternetconnection;
import magentoegypt.locafy.api_request_response_section.network_error_handling.Ced_MultiVendor_UnAuthourizedRequestError;
import magentoegypt.locafy.vendor_profile_section.Ced_MultiVendor_ProfileStatus;
import magentoegypt.locafy.api_request_response_section.Ced_MultiVendor_ClientRequestResponse;
import magentoegypt.locafy.R;
import magentoegypt.locafy.vendor_notification.Notification;

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
import java.util.Random;

public class Ced_MultiVendor_CreateProductWithType extends Ced_MultiVendor_NavigationActivity {
    public static EditText MultiVendor_edt_start_date;
    public static EditText MultiVendor_edt_end_date;
    final int idd = Resources.getSystem().getIdentifier("btn_check_holo_light", "drawable", "android");
    Ced_MultiVendor_ConnectionDetector connectionDetector;
    Ced_MultiVendor_VendorFunctionalityList functionalityList;
    String Jstring;
    String type;
    String attribute_set;
    HashMap<String, String> forstock;
    HashMap<String, String> fortaxes;
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
    Button addimagees;
    ArrayList<String> idsforwebsites;
    HashMap<String, String> dataforproductsave;
    HashMap<String, String> dataforproductsave_image;
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
    Button button;
    int id;
    int writeresult;
    int readresult;
    int level = 1;
    ScrollView mainvendoreditprofilescroll;
    HashMap<Integer, String> sequence;
    JSONObject tabs;
    JSONArray attributes;
    ArrayList<HashMap<String, String>> attributesarraylist;
    HashMap<String, String> select_label_value;
    HashMap<String, String> label_value;
    HashMap<String, ArrayList<String>> multiselect_select;
    Spinner product_weight_type;
    Spinner product_price_type;
    Spinner product_sku_type;
    Ced_MultiVendor_FontSetting fontSetting;
    LinearLayout url_key_linear;
    EditText MultiVendor_url_key;
    Calendar newCalendar;
    String has_subcategory = "no";
    int margin = 0;
    Boolean select_date = false;
    HashSet<String> selected_category_ids;
    JSONArray saved_category_ids;
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
    JSONArray mainsubcategory_array;
    HashMap<String, String> parent_child;
    HashMap<String, String> parent_child2;
    LinearLayout priceSection;
    TextView MultiVendor_taxclasstag;
    LinearLayout category_section;
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
        fontSetting = new Ced_MultiVendor_FontSetting();
        selected_websitetopost = getIntent().getStringExtra("selectedwebsite");
        if (connectionDetector.isConnectingToInternet()) {
            try {
                ViewGroup content = findViewById(R.id.MultiVendor_frame_container);
                getLayoutInflater().inflate(R.layout.ced_multivendor_activity_create_product_with_type, content, true);
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                getSupportActionBar().setHomeAsUpIndicator(R.drawable.backbutton);
                category_section = findViewById(R.id.category_section);
                mainvendoreditprofilescroll = findViewById(R.id.MultiVendor_mainvendoreditprofilescroll);
                writeresult = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
                readresult = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE);
                setname(session.getvendorname());
                setprofilepic(session.getvendorpic());
                changetitle("Add Product");
                Intent intent = getIntent();
                CurrentUrl = session.getBase_Url() + "vendorapi/vproducts/create";
                updateurl = session.getBase_Url() + "vendorapi/vproducts/update";
                categoryfilterdataarraylist = new ArrayList<String>();
                uris = new ArrayList<String>();
                sequence = new HashMap<Integer, String>();
                catfilterdata = new HashMap<String, ArrayList<String>>();
                forstores = new HashMap<String, ArrayList<String>>();
                multiselect_select = new HashMap<String, ArrayList<String>>();
                forstoreviews = new HashMap<String, ArrayList<String>>();
                attributesarraylist = new ArrayList<HashMap<String, String>>();
                forstock = new HashMap<String, String>();
                formainwebsites = new HashMap<String, String>();
                defaultimagehash = new HashMap<String, String>();
                label_value = new HashMap<String, String>();
                select_label_value = new HashMap<String, String>();
                fortaxes = new HashMap<String, String>();
                dataforproductsave = new HashMap<String, String>();
                dataforproductsave_image = new HashMap<String, String>();
                taxlist = new ArrayList<String>();
                idsforwebsites = new ArrayList<String>();
                stockavaillist = new ArrayList<String>();
                Jstring = intent.getStringExtra("dataforproductcreation");
                type = intent.getStringExtra("type");
                multiple_images_array = new JSONArray();
                if (functionalityList.getProductAddon()) {
                    attribute_set = intent.getStringExtra("attribute_set");
                }
                JSONObject object = new JSONObject(Jstring);
                saved_category_ids = new JSONArray();
                selected_category_ids = new HashSet<>();
                parent_child = new HashMap<>();
                parent_child2 = new HashMap<>();
                final int idd = Resources.getSystem().getIdentifier("btn_check_holo_light", "drawable", "android");
                /**************************initializingViews****************************/
                product_weight_type = findViewById(R.id.MultiVendor_product_weight_type);
                product_price_type = findViewById(R.id.MultiVendor_product_price_type);
                product_sku_type = findViewById(R.id.MultiVendor_product_sku_type);
                stockavail = findViewById(R.id.MultiVendor_stockavail);
                taxclass = findViewById(R.id.MultiVendor_taxclass);
                categoryselection = findViewById(R.id.MultiVendor_categoryselection);
                websiteselection = findViewById(R.id.MultiVendor_websiteselection);
                weightsection = findViewById(R.id.MultiVendor_weightsection);
                sku_type_linear = findViewById(R.id.MultiVendor_sku_type_linear);
                price_type_linear = findViewById(R.id.MultiVendor_price_type_linear);
                weight_type_linear = findViewById(R.id.MultiVendor_weight_type_linear);
                productattribute = findViewById(R.id.MultiVendor_productattribute);
                imageselection = findViewById(R.id.MultiVendor_imageselection);
                categorytag = findViewById(R.id.MultiVendor_categorytag);
                website = findViewById(R.id.MultiVendor_website);
                save = findViewById(R.id.MultiVendor_save);
                savecontinue = findViewById(R.id.MultiVendor_savecontinue);

                product_name = findViewById(R.id.MultiVendor_product_name);
                product_sku = findViewById(R.id.MultiVendor_product_sku);
                description = findViewById(R.id.MultiVendor_description);
                shortdescription = findViewById(R.id.MultiVendor_shortdescription);
                stock = findViewById(R.id.MultiVendor_stock);
                price = findViewById(R.id.MultiVendor_price);
                weight = findViewById(R.id.MultiVendor_weight);
                specialprice = findViewById(R.id.MultiVendor_specialprice);
                url_key_linear = findViewById(R.id.url_key_linear);
                MultiVendor_url_key = findViewById(R.id.MultiVendor_url_key);
                MultiVendor_edt_end_date = findViewById(R.id.MultiVendor_specialprice_To);
                MultiVendor_edt_start_date = findViewById(R.id.MultiVendor_specialprice_From);
                getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);


            /*    MultiVendor_edt_start_date.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        newCalendar = Calendar.getInstance();
                        dateFormatter = new SimpleDateFormat("MM/dd/yyyy", Locale.US);
                        new DatePickerDialog(Ced_MultiVendor_CreateProductWithType.this, new DatePickerDialog.OnDateSetListener() {

                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                newCalendar.set(Calendar.YEAR, year);
                                newCalendar.set(Calendar.MONTH, monthOfYear);
                                newCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                                MultiVendor_edt_start_date.setText(dateFormatter.format(newCalendar.getTime()));
                               // MultiVendor_edt_end_date.setText(dateFormatter.format(newCalendar.getTime()));

                            }

                        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH)).show();
                    }
                });

                MultiVendor_edt_end_date.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        newCalendar = Calendar.getInstance();
                        dateFormatter = new SimpleDateFormat("MM/dd/yyyy", Locale.US);
                        new DatePickerDialog(Ced_MultiVendor_CreateProductWithType.this, new DatePickerDialog.OnDateSetListener() {

                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                newCalendar.set(Calendar.YEAR, year);
                                newCalendar.set(Calendar.MONTH, monthOfYear);
                                newCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                                MultiVendor_edt_end_date.setText(dateFormatter.format(newCalendar.getTime()));
                                // MultiVendor_edt_end_date.setText(dateFormatter.format(newCalendar.getTime()));

                            }

                        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH)).show();
                    }
                });*/

                MultiVendor_edt_start_date.setOnClickListener(view -> {
                    select_date = true;
                    showTruitonDatePickerDialog(view);
                });


                MultiVendor_edt_end_date.setOnClickListener(view -> {
                    if (select_date) {
                        showToDatePickerDialog(view);

                    } else {
                        Toast.makeText(Ced_MultiVendor_CreateProductWithType.this, R.string.please_fill_both_dates, Toast.LENGTH_SHORT).show();
                    }
                });

                if (type.equals("simple") || type.equals("bundle")) {
                    weightsection.setVisibility(View.VISIBLE);
                }
                if (type.equals("bundle")) {
                    sku_type_linear.setVisibility(View.VISIBLE);
                    price_type_linear.setVisibility(View.VISIBLE);
                    weight_type_linear.setVisibility(View.VISIBLE);
                }
                addimagees = findViewById(R.id.MultiVendor_addimagees);
                //categorytag.setButtonDrawable(idd);
                website.setButtonDrawable(idd);
                if (functionalityList.getProductAddon()) {
                    savecontinue.setVisibility(View.VISIBLE);
                } else {
                    savecontinue.setVisibility(View.GONE);
                }
                /**************************initializingViews****************************/
                /**************************fonts****************************/
                TextView ProductNametag = findViewById(R.id.MultiVendor_ProductNametag);
                TextView SkuTag = findViewById(R.id.MultiVendor_SkuTag);
                TextView SkuType = findViewById(R.id.MultiVendor_SkuType);
                TextView DesTag = findViewById(R.id.MultiVendor_DesTag);
                TextView shortdesTag = findViewById(R.id.MultiVendor_shortdesTag);
                TextView stockavailtag = findViewById(R.id.MultiVendor_stockavailtag);
                TextView Stocktag = findViewById(R.id.MultiVendor_Stocktag);
                TextView taxclasstag = findViewById(R.id.MultiVendor_taxclasstag);
                TextView regularpricetag = findViewById(R.id.MultiVendor_regularpricetag);
                TextView priceType = findViewById(R.id.MultiVendor_priceType);
                TextView specialpricetag = findViewById(R.id.MultiVendor_specialpricetag);
                TextView weighttag = findViewById(R.id.MultiVendor_weighttag);
                TextView weightType = findViewById(R.id.MultiVendor_weightType);
                fontSetting.setFontforTextviews(save, "Roboto-Medium.ttf", getApplicationContext());
                fontSetting.setFontforTextviews(savecontinue, "Roboto-Medium.ttf", getApplicationContext());
                fontSetting.setFontforTextviews(ProductNametag, "Roboto-Medium.ttf", getApplicationContext());
                fontSetting.setFontforTextviews(SkuTag, "Roboto-Medium.ttf", getApplicationContext());
                fontSetting.setFontforTextviews(SkuType, "Roboto-Medium.ttf", getApplicationContext());
                fontSetting.setFontforTextviews(DesTag, "Roboto-Medium.ttf", getApplicationContext());
                fontSetting.setFontforTextviews(shortdesTag, "Roboto-Medium.ttf", getApplicationContext());
                fontSetting.setFontforTextviews(stockavailtag, "Roboto-Medium.ttf", getApplicationContext());
                fontSetting.setFontforTextviews(regularpricetag, "Roboto-Medium.ttf", getApplicationContext());
                fontSetting.setFontforTextviews(taxclasstag, "Roboto-Medium.ttf", getApplicationContext());
                fontSetting.setFontforTextviews(priceType, "Roboto-Medium.ttf", getApplicationContext());
                fontSetting.setFontforTextviews(specialpricetag, "Roboto-Medium.ttf", getApplicationContext());
                fontSetting.setFontforTextviews(weighttag, "Roboto-Medium.ttf", getApplicationContext());
                fontSetting.setFontforTextviews(Stocktag, "Roboto-Medium.ttf", getApplicationContext());
                fontSetting.setFontforTextviews(weightType, "Roboto-Medium.ttf", getApplicationContext());
                fontSetting.setfontforCheckbox(website, "Roboto-Medium.ttf", getApplicationContext());
                fontSetting.setFontforTextviews(categorytag, "Roboto-Medium.ttf", getApplicationContext());
                fontSetting.setfontforButtons(addimagees, "Roboto-Medium.ttf", getApplicationContext());
                fontSetting.setfontforEditText(product_name, "Roboto-Regular.ttf", getApplicationContext());
                fontSetting.setfontforEditText(product_sku, "Roboto-Regular.ttf", getApplicationContext());
                fontSetting.setfontforEditText(description, "Roboto-Regular.ttf", getApplicationContext());
                fontSetting.setfontforEditText(shortdescription, "Roboto-Regular.ttf", getApplicationContext());
                fontSetting.setfontforEditText(stock, "Roboto-Regular.ttf", getApplicationContext());
                fontSetting.setfontforEditText(price, "Roboto-Regular.ttf", getApplicationContext());
                fontSetting.setfontforEditText(specialprice, "Roboto-Regular.ttf", getApplicationContext());
                fontSetting.setfontforEditText(weight, "Roboto-Regular.ttf", getApplicationContext());
                /**************************fonts****************************/
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
                /******************Categories************************************************/

                if (object.getJSONObject("data").has("category")) {
                    category_section.setVisibility(View.VISIBLE);
                    catfilters = object.getJSONObject("data").getJSONArray("category");
                    if (catfilters.length() > 0) {
                        JSONObject current_object = null;
                        View category_view = null;
                        TextView subcategories = null;
                        CheckBox main_cat = null;
                        ImageView cat_icon = null;
                        String maincatname = null;
                        String maincatid = null;
                        String[] parts = new String[2];
                        for (int i = 0; i < catfilters.length(); i++) {
                            current_object = catfilters.getJSONObject(i);
                            category_view = View.inflate(Ced_MultiVendor_CreateProductWithType.this, R.layout.ced_multivendor_maincategory, null);
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
                            main_cat.setOnCheckedChangeListener((buttonView, isChecked) -> {
                                if (isChecked) {
                                    selected_category_ids.add(String.valueOf(finalMain_cat.getId()));
                                } else {
                                    selected_category_ids.remove(String.valueOf(finalMain_cat.getId()));
                                }
                            });
                            final boolean[] open = {false};
                            final ImageView finalCat_icon = cat_icon;
                            final ImageView finalCat_icon1 = cat_icon;
                            cat_icon.setOnClickListener(v -> {
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
                            });
                            categoryselection.addView(category_view);
                        }
                    }
                }

                /******************Categories************************************************/

                /**************************stock****************************/
                JSONObject stockobject = object.getJSONObject("data").getJSONObject("stock");
                JSONArray values = stockobject.names();
                for (int v = 0; v < values.length(); v++) {
                    forstock.put(stockobject.getString((String) values.get(v)), (String) values.get(v));
                    stockavaillist.add(stockobject.getString((String) values.get(v)));
                }
                ArrayAdapter<String> adp = new ArrayAdapter<String>(Ced_MultiVendor_CreateProductWithType.this, android.R.layout.simple_dropdown_item_1line, stockavaillist);
                stockavail.setAdapter(adp);
                /**************************stock****************************/
                /****************************taxes***************************/

                JSONObject taxesobject = object.getJSONObject("data").getJSONObject("taxes");
                JSONArray taxesvalues = taxesobject.names();

                for (int t = 0; t < taxesvalues.length(); t++) {
                    fortaxes.put(taxesobject.getString((String) taxesvalues.get(t)), (String) taxesvalues.get(t));
                    taxlist.add(taxesobject.getString((String) taxesvalues.get(t)));
                }
                ArrayAdapter<String> taxlistadp = new ArrayAdapter<String>(Ced_MultiVendor_CreateProductWithType.this, android.R.layout.simple_dropdown_item_1line, taxlist);
                taxclass.setAdapter(taxlistadp);

                /****************************taxes***************************/
                /****************************websites***************************/
                try {
                    JSONObject websitesobject = object.getJSONObject("data").getJSONObject("websites");
                    JSONObject storesobject = object.getJSONObject("data").getJSONObject("stores");

                    JSONObject storeviewssobject = object.getJSONObject("data").getJSONObject("storeViews");
                    JSONArray websitesvalues = websitesobject.names();
                    for (int t = 0; t < websitesvalues.length(); t++) {
                        formainwebsites.put(websitesobject.getString((String) websitesvalues.get(t)), (String) websitesvalues.get(t));
                        JSONArray stores = storesobject.getJSONArray(websitesobject.getString((String) websitesvalues.get(t)));
                        ArrayList<String> storestring = new ArrayList<String>();
                        for (int s = 0; s < stores.length(); s++) {
                            storestring.add((String) stores.get(s));
                        }
                        forstores.put(websitesobject.getString((String) websitesvalues.get(t)), storestring);
                        JSONArray storeviews = storeviewssobject.getJSONObject(websitesobject.getString((String) websitesvalues.get(t))).names();
                        for (int view = 0; view < storeviews.length(); view++) {
                            ArrayList<String> views = new ArrayList<String>();
                            JSONArray storeviewjsonarray = storeviewssobject.getJSONObject(websitesobject.getString((String) websitesvalues.get(t))).getJSONArray((String) storeviews.get(view));
                            for (int j = 0; j < storeviewjsonarray.length(); j++) {
                                views.add((String) storeviewjsonarray.get(j));
                            }
                            forstoreviews.put((String) storeviews.get(view), views);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                /****************************websites***************************/
                Iterator iterator = formainwebsites.keySet().iterator();
                while (iterator.hasNext()) {
                    final LinearLayout mainwebsite = new LinearLayout(Ced_MultiVendor_CreateProductWithType.this);
                    mainwebsite.setOrientation(LinearLayout.VERTICAL);
                    final CheckBox mainwebsitename = new CheckBox(Ced_MultiVendor_CreateProductWithType.this);
                    mainwebsitename.setButtonDrawable(idd);
                    mainwebsitename.setOnCheckedChangeListener((buttonView, isChecked) -> {
                        if (isChecked) {
                            if (idsforwebsites.contains(formainwebsites.get(mainwebsitename.getText().toString()))) {
                            } else {
                                idsforwebsites.add(formainwebsites.get(mainwebsitename.getText().toString()));
                            }
                        } else {
                            idsforwebsites.remove(formainwebsites.get(mainwebsitename.getText().toString()));
                        }
                    });
                    String websitename = iterator.next().toString();
                    mainwebsitename.setText(websitename);
                    mainwebsitename.setTypeface(Typeface.DEFAULT_BOLD);
                    mainwebsite.addView(mainwebsitename, 0);
                    ArrayList<String> storeandview = forstores.get(websitename);
                    Iterator iterator2 = storeandview.iterator();
                    while (iterator2.hasNext()) {
                        LinearLayout storelayout = new LinearLayout(Ced_MultiVendor_CreateProductWithType.this);
                        storelayout.setOrientation(LinearLayout.VERTICAL);
                        TextView storename = new TextView(Ced_MultiVendor_CreateProductWithType.this);
                        String storenametag = iterator2.next().toString();
                        storename.setText(storenametag);
                        storename.setTypeface(Typeface.DEFAULT_BOLD);
                        storename.setTextColor(getResources().getColor(R.color.black));
                        storename.setPadding(15, 0, 0, 0);
                        storelayout.addView(storename, 0);

                        ArrayList<String> storeviews = forstoreviews.get(storenametag);
                        Iterator iterator3 = storeviews.iterator();
                        LinearLayout storeviewlayout = new LinearLayout(Ced_MultiVendor_CreateProductWithType.this);
                        storeviewlayout.setOrientation(LinearLayout.VERTICAL);
                        storeviewlayout.setPadding(25, 0, 0, 0);
                        while (iterator3.hasNext()) {
                            TextView view = new TextView(Ced_MultiVendor_CreateProductWithType.this);
                            String Storeviewname = iterator3.next().toString();
                            view.setText(Storeviewname);
                            view.setTextColor(getResources().getColor(R.color.black));
                            storeviewlayout.addView(view);
                        }
                        storelayout.addView(storeviewlayout, 1);
                        mainwebsite.addView(storelayout);


                    }


                    websiteselection.addView(mainwebsite);
                }
                website.setOnCheckedChangeListener((buttonView, isChecked) -> {
                    if (isChecked) {
                        websiteselection.setVisibility(View.VISIBLE);
                    } else {
                        websiteselection.setVisibility(View.GONE);
                    }
                });

                /****************************websites******************************/
                /****************************saveproduct***************************/


                product_name.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        product_sku.setText(product_name.getText().toString() + getSaltString());

                    }

                    @Override
                    public void afterTextChanged(Editable editable) {

                    }
                });

                save.setOnClickListener(v -> {
                    dataforproductsave.put("vendor_id", session.getVendorid());
                    dataforproductsave.put("hashkey", session.getHahkey());
                    dataforproductsave.put("type", type);
                    /*if(url_key_linear.getVisibility()==View.VISIBLE)
                    {
                        if (!MultiVendor_url_key.getText().toString().isEmpty())
                        {
                            dataforproductsave.put("url_key", MultiVendor_url_key.getText().toString());
                        }
                    }*/


                    if (functionalityList.getProductAddon()) {
                        dataforproductsave.put("set", attribute_set);
                    }

                    if (product_name.getText().toString().isEmpty()) {
                        product_name.setError(getResources().getString(R.string.empty_product_name));
                        product_name.requestFocus();
                    } else {
                        dataforproductsave.put("product_name", product_name.getText().toString().trim());

                        if (product_sku.getText().toString().isEmpty()) {
                            product_sku.setError(getResources().getString(R.string.empty_sku));
                            product_sku.requestFocus();
                        } else {
                            dataforproductsave.put("product_sku", product_sku.getText().toString().trim());
                            dataforproductsave.put("url_key", product_sku.getText().toString());
                            if (description.getText().toString().isEmpty()) {
                                description.setError(getResources().getString(R.string.empty_desc));
                                description.requestFocus();
                            } else {
                                dataforproductsave.put("description", description.getText().toString());
                                if (shortdescription.getText().toString().isEmpty()) {
                                    shortdescription.setError(getResources().getString(R.string.empty_sdesc));
                                    shortdescription.requestFocus();
                                } else {
                                    dataforproductsave.put("shortdescription", shortdescription.getText().toString());
                                    if (stock.getText().toString().isEmpty()) {
                                        stock.setError(getResources().getString(R.string.empty_stock));
                                        stock.requestFocus();
                                    } else {

                                        dataforproductsave.put("stock", stock.getText().toString());
                                        if (price.getText().toString().isEmpty()) {
                                            price.setError(getResources().getString(R.string.empty_price));
                                            price.requestFocus();
                                        } else {
                                            if (type.equals("simple") || type.equals("bundle")) {

                                                dataforproductsave.put("weight", weight.getText().toString());

                                            }
                                            if (type.equals("bundle")) {
                                                dataforproductsave.put("weight_type", String.valueOf(product_weight_type.getSelectedItem()));
                                                dataforproductsave.put("price_type", String.valueOf(product_price_type.getSelectedItem()));
                                                dataforproductsave.put("sku_type", String.valueOf(product_sku_type.getSelectedItem()));

                                            }
                                            dataforproductsave.put("price", price.getText().toString());
                                            dataforproductsave.put("tax_class", fortaxes.get(taxclass.getSelectedItem()));
                                            dataforproductsave.put("stock_avail", forstock.get(stockavail.getSelectedItem()));
                                            if (!(specialprice.getText().toString().isEmpty())) {
                                                dataforproductsave.put("special_price", specialprice.getText().toString());
                                                if (MultiVendor_edt_end_date.getText().toString().isEmpty() || MultiVendor_edt_start_date.getText().toString().isEmpty()) {
                                                    //  Toast.makeText(getApplicationContext(),"Please fill Special Prices",Toast.LENGTH_LONG).show();
                                                } else {
                                                    dataforproductsave.put("special_from_date", MultiVendor_edt_start_date.getText().toString());
                                                    dataforproductsave.put("special_to_date", MultiVendor_edt_end_date.getText().toString());
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
                                                dataforproductsave.put("category", " ");
                                            }
                                            /*if (categoryfilterdataarraylist.size() > 0)
                                            {
                                                Iterator categoryiditerator = categoryfilterdataarraylist.iterator();
                                                JSONArray object1 = new JSONArray();

                                                while (categoryiditerator.hasNext()) {
                                                    String name = (String) categoryiditerator.next();
                                                    object1.put(name);

                                                }
                                                dataforproductsave.put("category", object1.toString());
                                            }
                                            else
                                            {
                                                dataforproductsave.put("category", " ");
                                            }*/
                                            if (idsforwebsites.size() > 0) {
                                                Iterator idsforwebsite = idsforwebsites.iterator();
                                                JSONArray object1 = new JSONArray();
                                                while (idsforwebsite.hasNext()) {
                                                    String name = (String) idsforwebsite.next();
                                                    object1.put(name);

                                                }
                                                dataforproductsave.put("websites", object1.toString());
                                                if (getResources().getString(R.string.Enable_Log).equals("yes")) {
                                                    Log.i("websitesssss", "" + object1.toString());
                                                }
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
                                                            if (type_string.equals("date")) {
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
                                                        if (multiple_images_array.length() > 0) {
                                                            //dataforproductsave_image.put("product_id", product_id);
                                                            //   dataforproductsave_image.put("vendor_id", session.getVendorid());
                                                            dataforproductsave.put("hashkey", session.getHahkey());
                                                            Log.i("9044_data", "" + multiple_images_array);
                                                            dataforproductsave.put("images", multiple_images_array.toString());
                                                        }
                                                    }
                                                }

                                            }
                                            Ced_MultiVendor_ClientRequestResponse crr = new Ced_MultiVendor_ClientRequestResponse(output -> {
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
                                                            product_id = object1.getJSONObject("data").getString("product_id");
                                                            Log.i("9044_prod_id", product_id);
                                                            Toast.makeText(Ced_MultiVendor_CreateProductWithType.this, object1.getJSONObject("data").getString("message"), Toast.LENGTH_LONG).show();
                                                            Intent intent1 = new Intent(Ced_MultiVendor_CreateProductWithType.this, Ced_MultiVendor_ManageProducts.class);
                                                            intent1.putExtra("Navigation", "productcreate");
                                                            intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                            startActivity(intent1);
                                                            overridePendingTransition(R.anim.ced_multivendor_slide_in, R.anim.ced_multivendor_slide_out);
                                                            Toast.makeText(Ced_MultiVendor_CreateProductWithType.this, object1.getJSONObject("data").getString("message"), Toast.LENGTH_LONG).show();
                                                            finish();




/*
                                                                if (multiple_images_array.length() > 0)
                                                                {
                                                                    dataforproductsave_image.put("product_id",product_id);
                                                                    dataforproductsave_image.put("vendor_id", session.getVendorid());
                                                                    dataforproductsave_image.put("hashkey", session.getHahkey());
                                                                    dataforproductsave_image.put("images",multiple_images_array.toString());

                                                                    Ced_MultiVendor_ClientRequestResponse crr = new Ced_MultiVendor_ClientRequestResponse(new AsyncResponse()
                                                                    {

                                                                        @Override
                                                                        public void processFinish(Object output) throws JSONException {
                                                                            Jstring = output.toString();
                                                                            if (functionalityList.getExtensionAddon())
                                                                            {
                                                                                JSONObject object1 = new JSONObject(Jstring);
                                                                                String successimage = object1.getJSONObject("data").getString("success");
                                                                                if (successimage.equals("true"))
                                                                                {
                                                                                    Toast.makeText(Ced_MultiVendor_CreateProductWithType.this, object1.getJSONObject("data").getString("message"), Toast.LENGTH_LONG).show();
                                                                                    Intent intent1 = new Intent(Ced_MultiVendor_CreateProductWithType.this, Ced_MultiVendor_ManageProducts.class);
                                                                                    intent1.putExtra("Navigation","productcreate");
                                                                                    intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                                                    startActivity(intent1);
                                                                                    overridePendingTransition(R.anim.ced_multivendor_slide_in, R.anim.ced_multivendor_slide_out);
                                                                                    Toast.makeText(Ced_MultiVendor_CreateProductWithType.this, object1.getJSONObject("data").getString("message"), Toast.LENGTH_LONG).show();
                                                                                    finish();
                                                                                }

                                                                            }
                                                                            else
                                                                            {
                                                                                Intent intent = new Intent(getApplicationContext(), Ced_MultiVendor_UnAuthourizedRequestError.class);
                                                                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                                                                startActivity(intent);
                                                                                overridePendingTransition(R.anim.ced_multivendor_slide_in, R.anim.ced_multivendor_slide_out);
                                                                            }


                                                                        }
                                                                    }, Ced_MultiVendor_CreateProductWithType.this,"POST",dataforproductsave_image);
                                                                    crr.execute(updateurl);
                                                                }
*/
                                                               /* else
                                                                {
                                                                    Intent intent1 = new Intent(Ced_MultiVendor_CreateProductWithType.this, Ced_MultiVendor_ManageProducts.class);
                                                                    intent1.putExtra("Navigation", "productcreate");
                                                                    intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                                    startActivity(intent1);
                                                                    overridePendingTransition(R.anim.ced_multivendor_slide_in, R.anim.ced_multivendor_slide_out);
                                                                    finish();
                                                                }*/
                                                        } else {
                                                            Toast.makeText(Ced_MultiVendor_CreateProductWithType.this, object1.getJSONObject("data").getString("message"), Toast.LENGTH_LONG).show();
                                                            if (object1.getJSONObject("data").getString("message").equals("URL key for specified store already exists.")) {
                                                                url_key_linear.setVisibility(View.VISIBLE);
                                                                MultiVendor_url_key.setError("please specify a new urkl key");
                                                            }
                                                        }
                                                    }

                                                } else {
                                                    Intent intent14 = new Intent(getApplicationContext(), Ced_MultiVendor_UnAuthourizedRequestError.class);
                                                    intent14.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                    intent14.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                                    startActivity(intent14);
                                                    overridePendingTransition(R.anim.ced_multivendor_slide_in, R.anim.ced_multivendor_slide_out);
                                                }


                                            }, Ced_MultiVendor_CreateProductWithType.this, "POST", dataforproductsave);
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
                    savecontinue.setOnClickListener(v -> {
                        /************************************************************************/
                        dataforproductsave.put("vendor_id", session.getVendorid());
                        dataforproductsave.put("hashkey", session.getHahkey());
                        dataforproductsave.put("type", type);
                        /*if(url_key_linear.getVisibility()==View.VISIBLE)
                        {
                            if (!MultiVendor_url_key.getText().toString().isEmpty())
                            {
                                dataforproductsave.put("url_key", MultiVendor_url_key.getText().toString());
                            }
                        }*/
                        dataforproductsave.put("set", attribute_set);
                        if (product_name.getText().toString().isEmpty()) {
                            product_name.setError(getResources().getString(R.string.empty));
                            product_name.requestFocus();
                        } else {

                            dataforproductsave.put("product_name", product_name.getText().toString().trim());

                            if (product_sku.getText().toString().isEmpty()) {
                                product_sku.setError(getResources().getString(R.string.empty));
                                product_sku.requestFocus();
                            } else {
                                dataforproductsave.put("product_sku", product_sku.getText().toString().trim());
                                dataforproductsave.put("url_key", product_sku.getText().toString());
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

                                            dataforproductsave.put("stock", stock.getText().toString());
                                            if (price.getText().toString().isEmpty()) {
                                                price.setError(getResources().getString(R.string.empty));
                                                price.requestFocus();
                                            } else {
                                                if (type.equals("simple") || type.equals("bundle")) {

                                                    dataforproductsave.put("weight", weight.getText().toString());

                                                }
                                                if (type.equals("bundle")) {
                                                    dataforproductsave.put("weight_type", String.valueOf(product_weight_type.getSelectedItem()));
                                                    dataforproductsave.put("price_type", String.valueOf(product_price_type.getSelectedItem()));
                                                    dataforproductsave.put("sku_type", String.valueOf(product_sku_type.getSelectedItem()));

                                                }
                                                dataforproductsave.put("price", price.getText().toString());
                                                dataforproductsave.put("tax_class", fortaxes.get(taxclass.getSelectedItem()));
                                                dataforproductsave.put("stock_avail", forstock.get(stockavail.getSelectedItem()));
                                                if (!(specialprice.getText().toString().isEmpty())) {
                                                    dataforproductsave.put("special_price", specialprice.getText().toString());
                                                    if (MultiVendor_edt_end_date.getText().toString().isEmpty() || MultiVendor_edt_start_date.getText().toString().isEmpty()) {
                                                        // Toast.makeText(getApplicationContext(),"Please fill Special Prices",Toast.LENGTH_LONG).show();
                                                    } else {
                                                        dataforproductsave.put("special_from_date", MultiVendor_edt_start_date.getText().toString());
                                                        dataforproductsave.put("special_to_date", MultiVendor_edt_end_date.getText().toString());
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
                                                    dataforproductsave.put("category", " ");
                                                }
                                                /*if (categoryfilterdataarraylist.size() > 0)
                                                {
                                                    Iterator categoryiditerator = categoryfilterdataarraylist.iterator();
                                                    JSONArray object1 = new JSONArray();

                                                    while (categoryiditerator.hasNext())
                                                    {
                                                        String name = (String) categoryiditerator.next();
                                                        object1.put(name);

                                                    }
                                                    dataforproductsave.put("category", object1.toString());
                                                }
                                                else
                                                {
                                                    dataforproductsave.put("category", " ");
                                                }*/
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
                                                                if (type_string.equals("date")) {
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

                                                Ced_MultiVendor_ClientRequestResponse crr = new Ced_MultiVendor_ClientRequestResponse(output -> {
                                                    Jstring = output.toString();
                                                    if (functionalityList.getExtensionAddon()) {
                                                        JSONObject object1 = new JSONObject(Jstring);
                                                        if (object1.has("vendor_approved")) {
                                                            logout();
                                                        } else {
                                                            String success = object1.getJSONObject("data").getString("success");
                                                            if (success.equals("true")) {
                                                                product_id = object1.getJSONObject("data").getString("product_id");
                                                                Ced_MultiVendor_GlobalVariables.noti = object1.getJSONObject("data").getString("valerts");
                                                                changecount();

                                                                if (multiple_images_array.length() > 0) {
                                                                    product_id = object1.getJSONObject("data").getString("product_id");

                                                                    dataforproductsave_image.put("product_id", product_id);
                                                                    dataforproductsave_image.put("vendor_id", session.getVendorid());
                                                                    dataforproductsave_image.put("hashkey", session.getHahkey());
                                                                    dataforproductsave_image.put("images", multiple_images_array.toString());


                                                                    Ced_MultiVendor_ClientRequestResponse crr1 = new Ced_MultiVendor_ClientRequestResponse(output1 -> {
                                                                        Jstring = output1.toString();
                                                                        if (functionalityList.getExtensionAddon()) {
                                                                            JSONObject object11 = new JSONObject(Jstring);
                                                                            String successimage = object11.getJSONObject("data").getString("success");
                                                                            if (successimage.equals("true")) {
                                                                                if (tabs.has("related")) {
                                                                                    Intent related = new Intent(Ced_MultiVendor_CreateProductWithType.this, Ced_MultiVendor_Related.class);
                                                                                    related.putExtra("tabs", tabs.toString());
                                                                                    related.putExtra("product_id", product_id);
                                                                                    related.putExtra("type", type);
                                                                                    if (getResources().getString(R.string.Enable_Log).equals("yes")) {
                                                                                        Log.i("attribute_set", "" + attribute_set);
                                                                                    }
                                                                                    related.putExtra("attribute_set", attribute_set);
                                                                                    startActivity(related);
                                                                                    overridePendingTransition(R.anim.ced_multivendor_slide_in_sequence, R.anim.ced_multivendor_slide_out_sequence);
                                                                                    finish();
                                                                                } else {
                                                                                    if (tabs.has("upsell")) {
                                                                                        Intent related = new Intent(Ced_MultiVendor_CreateProductWithType.this, Ced_MultiVendor_upsell.class);
                                                                                        related.putExtra("tabs", tabs.toString());
                                                                                        related.putExtra("product_id", product_id);
                                                                                        related.putExtra("selectedwebsite", selected_websitetopost);
                                                                                        related.putExtra("type", type);
                                                                                        related.putExtra("attribute_set", attribute_set);
                                                                                        related.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                                                        startActivity(related);
                                                                                        overridePendingTransition(R.anim.ced_multivendor_slide_in_sequence, R.anim.ced_multivendor_slide_out_sequence);
                                                                                        finish();
                                                                                    } else {
                                                                                        if (tabs.has("crosssell")) {
                                                                                            Intent related = new Intent(Ced_MultiVendor_CreateProductWithType.this, Ced_MultiVendor_crosssell.class);
                                                                                            related.putExtra("tabs", tabs.toString());
                                                                                            related.putExtra("product_id", product_id);
                                                                                            related.putExtra("type", type);
                                                                                            related.putExtra("attribute_set", attribute_set);
                                                                                            related.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                                                            startActivity(related);
                                                                                            overridePendingTransition(R.anim.ced_multivendor_slide_in_sequence, R.anim.ced_multivendor_slide_out_sequence);
                                                                                            finish();
                                                                                        } else {
                                                                                            if (tabs.has("customer_options")) {
                                                                                                Intent related = new Intent(Ced_MultiVendor_CreateProductWithType.this, Ced_MultiVendor_CustomOption.class);
                                                                                                related.putExtra("tabs", tabs.toString());
                                                                                                related.putExtra("product_id", product_id);
                                                                                                related.putExtra("type", type);
                                                                                                related.putExtra("attribute_set", attribute_set);
                                                                                                related.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                                                                startActivity(related);
                                                                                                overridePendingTransition(R.anim.ced_multivendor_slide_in_sequence, R.anim.ced_multivendor_slide_out_sequence);
                                                                                                finish();
                                                                                            } else {
                                                                                                if (type.equals("configurable")) {
                                                                                                    Intent related = new Intent(Ced_MultiVendor_CreateProductWithType.this, Ced_MultiVendor_Configurable.class);
                                                                                                    related.putExtra("tabs", tabs.toString());
                                                                                                    related.putExtra("product_id", product_id);
                                                                                                    related.putExtra("type", type);
                                                                                                    related.putExtra("attribute_set", attribute_set);
                                                                                                    related.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                                                                    startActivity(related);
                                                                                                    overridePendingTransition(R.anim.ced_multivendor_slide_in_sequence, R.anim.ced_multivendor_slide_out_sequence);
                                                                                                    finish();
                                                                                                }
                                                                                                if (type.equals("downloadable")) {
                                                                                                    Intent related = new Intent(Ced_MultiVendor_CreateProductWithType.this, Ced_MultiVendor_Downloadable.class);
                                                                                                    related.putExtra("tabs", tabs.toString());
                                                                                                    related.putExtra("product_id", product_id);
                                                                                                    related.putExtra("type", type);
                                                                                                    related.putExtra("attribute_set", attribute_set);
                                                                                                    related.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                                                                    startActivity(related);
                                                                                                    overridePendingTransition(R.anim.ced_multivendor_slide_in_sequence, R.anim.ced_multivendor_slide_out_sequence);
                                                                                                    finish();
                                                                                                }
                                                                                                if (type.equals("bundle")) {
                                                                                                    Intent related = new Intent(Ced_MultiVendor_CreateProductWithType.this, Ced_MultiVendor_BundleItems.class);
                                                                                                    related.putExtra("tabs", tabs.toString());
                                                                                                    related.putExtra("product_id", product_id);
                                                                                                    related.putExtra("type", type);
                                                                                                    related.putExtra("attribute_set", attribute_set);
                                                                                                    related.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                                                                    startActivity(related);
                                                                                                    overridePendingTransition(R.anim.ced_multivendor_slide_in_sequence, R.anim.ced_multivendor_slide_out_sequence);
                                                                                                    finish();
                                                                                                }
                                                                                                if (type.equals("grouped")) {
                                                                                                    Intent related = new Intent(Ced_MultiVendor_CreateProductWithType.this, Ced_MultiVendor_GroupItems.class);
                                                                                                    related.putExtra("tabs", tabs.toString());
                                                                                                    related.putExtra("product_id", product_id);
                                                                                                    related.putExtra("type", type);
                                                                                                    related.putExtra("attribute_set", attribute_set);
                                                                                                    related.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                                                                    startActivity(related);
                                                                                                    overridePendingTransition(R.anim.ced_multivendor_slide_in_sequence, R.anim.ced_multivendor_slide_out_sequence);
                                                                                                    finish();
                                                                                                }
                                                                                                if (type.equals("simple")) {
                                                                                                    Intent related = new Intent(Ced_MultiVendor_CreateProductWithType.this, Ced_MultiVendor_ManageProducts.class);
                                                                                                    related.putExtra("Navigation", "productcreate");
                                                                                                    startActivity(related);
                                                                                                    overridePendingTransition(R.anim.ced_multivendor_slide_in_sequence, R.anim.ced_multivendor_slide_out_sequence);
                                                                                                }
                                                                                                if (type.equals("virtual")) {
                                                                                                    Intent related = new Intent(Ced_MultiVendor_CreateProductWithType.this, Ced_MultiVendor_ManageProducts.class);
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
                                                                            Intent intent12 = new Intent(getApplicationContext(), Ced_MultiVendor_UnAuthourizedRequestError.class);
                                                                            intent12.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                                            intent12.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                                                            startActivity(intent12);
                                                                            overridePendingTransition(R.anim.ced_multivendor_slide_in, R.anim.ced_multivendor_slide_out);
                                                                        }

                                                                        Log.i("9044_sending_updatedatd", dataforproductsave_image + "");


                                                                    }, Ced_MultiVendor_CreateProductWithType.this, "POST", dataforproductsave_image);
                                                                    crr1.execute(updateurl);
                                                                } else {
                                                                    if (tabs.has("related")) {
                                                                        Intent related = new Intent(Ced_MultiVendor_CreateProductWithType.this, Ced_MultiVendor_Related.class);
                                                                        related.putExtra("tabs", tabs.toString());
                                                                        related.putExtra("product_id", product_id);
                                                                        related.putExtra("type", type);
                                                                        if (getResources().getString(R.string.Enable_Log).equals("yes")) {
                                                                            Log.i("attribute_set", "" + attribute_set);
                                                                        }
                                                                        related.putExtra("attribute_set", attribute_set);
                                                                        related.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                                        startActivity(related);
                                                                        overridePendingTransition(R.anim.ced_multivendor_slide_in_sequence, R.anim.ced_multivendor_slide_out_sequence);
                                                                    } else {
                                                                        if (tabs.has("upsell")) {
                                                                            Intent related = new Intent(Ced_MultiVendor_CreateProductWithType.this, Ced_MultiVendor_upsell.class);
                                                                            related.putExtra("tabs", tabs.toString());
                                                                            related.putExtra("product_id", product_id);
                                                                            related.putExtra("selectedwebsite", selected_websitetopost);
                                                                            related.putExtra("type", type);
                                                                            if (getResources().getString(R.string.Enable_Log).equals("yes")) {
                                                                                Log.i("attribute_set", "" + attribute_set);
                                                                            }
                                                                            related.putExtra("attribute_set", attribute_set);
                                                                            related.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                                            startActivity(related);
                                                                            overridePendingTransition(R.anim.ced_multivendor_slide_in_sequence, R.anim.ced_multivendor_slide_out_sequence);
                                                                        } else {
                                                                            if (tabs.has("crosssell")) {
                                                                                Intent related = new Intent(Ced_MultiVendor_CreateProductWithType.this, Ced_MultiVendor_crosssell.class);
                                                                                related.putExtra("tabs", tabs.toString());
                                                                                related.putExtra("product_id", product_id);
                                                                                related.putExtra("type", type);
                                                                                if (getResources().getString(R.string.Enable_Log).equals("yes")) {
                                                                                    Log.i("attribute_set", "" + attribute_set);
                                                                                }
                                                                                related.putExtra("attribute_set", attribute_set);
                                                                                related.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                                                startActivity(related);
                                                                                overridePendingTransition(R.anim.ced_multivendor_slide_in_sequence, R.anim.ced_multivendor_slide_out_sequence);
                                                                            } else {
                                                                                if (tabs.has("customer_options")) {
                                                                                    Intent related = new Intent(Ced_MultiVendor_CreateProductWithType.this, Ced_MultiVendor_CustomOption.class);
                                                                                    related.putExtra("tabs", tabs.toString());
                                                                                    related.putExtra("product_id", product_id);
                                                                                    related.putExtra("type", type);
                                                                                    if (getResources().getString(R.string.Enable_Log).equals("yes")) {
                                                                                        Log.i("attribute_set", "" + attribute_set);
                                                                                    }
                                                                                    related.putExtra("attribute_set", attribute_set);
                                                                                    related.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                                                    startActivity(related);
                                                                                    overridePendingTransition(R.anim.ced_multivendor_slide_in_sequence, R.anim.ced_multivendor_slide_out_sequence);
                                                                                } else {
                                                                                    if (type.equals("configurable")) {
                                                                                        Intent related = new Intent(Ced_MultiVendor_CreateProductWithType.this, Ced_MultiVendor_Configurable.class);
                                                                                        related.putExtra("tabs", tabs.toString());
                                                                                        related.putExtra("product_id", product_id);
                                                                                        related.putExtra("type", type);
                                                                                        if (getResources().getString(R.string.Enable_Log).equals("yes")) {
                                                                                            Log.i("attribute_set", "" + attribute_set);
                                                                                        }
                                                                                        related.putExtra("attribute_set", attribute_set);
                                                                                        related.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                                                        startActivity(related);
                                                                                        overridePendingTransition(R.anim.ced_multivendor_slide_in_sequence, R.anim.ced_multivendor_slide_out_sequence);
                                                                                    }
                                                                                    if (type.equals("downloadable")) {
                                                                                        Intent related = new Intent(Ced_MultiVendor_CreateProductWithType.this, Ced_MultiVendor_Downloadable.class);
                                                                                        related.putExtra("tabs", tabs.toString());
                                                                                        related.putExtra("product_id", product_id);
                                                                                        related.putExtra("type", type);
                                                                                        if (getResources().getString(R.string.Enable_Log).equals("yes")) {
                                                                                            Log.i("attribute_set", "" + attribute_set);
                                                                                        }
                                                                                        related.putExtra("attribute_set", attribute_set);
                                                                                        related.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                                                        startActivity(related);
                                                                                        overridePendingTransition(R.anim.ced_multivendor_slide_in_sequence, R.anim.ced_multivendor_slide_out_sequence);
                                                                                    }
                                                                                    if (type.equals("bundle")) {
                                                                                        Intent related = new Intent(Ced_MultiVendor_CreateProductWithType.this, Ced_MultiVendor_BundleItems.class);
                                                                                        related.putExtra("tabs", tabs.toString());
                                                                                        related.putExtra("product_id", product_id);
                                                                                        related.putExtra("type", type);
                                                                                        if (getResources().getString(R.string.Enable_Log).equals("yes")) {
                                                                                            Log.i("attribute_set", "" + attribute_set);
                                                                                        }
                                                                                        related.putExtra("attribute_set", attribute_set);
                                                                                        related.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                                                        startActivity(related);
                                                                                        overridePendingTransition(R.anim.ced_multivendor_slide_in_sequence, R.anim.ced_multivendor_slide_out_sequence);
                                                                                    }
                                                                                    if (type.equals("grouped")) {
                                                                                        Intent related = new Intent(Ced_MultiVendor_CreateProductWithType.this, Ced_MultiVendor_GroupItems.class);
                                                                                        related.putExtra("tabs", tabs.toString());
                                                                                        related.putExtra("product_id", product_id);
                                                                                        related.putExtra("type", type);
                                                                                        if (getResources().getString(R.string.Enable_Log).equals("yes")) {
                                                                                            Log.i("attribute_set", "" + attribute_set);
                                                                                        }
                                                                                        related.putExtra("attribute_set", attribute_set);
                                                                                        related.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                                                        startActivity(related);
                                                                                        overridePendingTransition(R.anim.ced_multivendor_slide_in_sequence, R.anim.ced_multivendor_slide_out_sequence);
                                                                                    }
                                                                                    if (type.equals("simple")) {
                                                                                        Intent related = new Intent(Ced_MultiVendor_CreateProductWithType.this, Ced_MultiVendor_ManageProducts.class);
                                                                                        related.putExtra("Navigation", "productcreate");
                                                                                        startActivity(related);
                                                                                        overridePendingTransition(R.anim.ced_multivendor_slide_in_sequence, R.anim.ced_multivendor_slide_out_sequence);
                                                                                    }
                                                                                    if (type.equals("virtual")) {
                                                                                        Intent related = new Intent(Ced_MultiVendor_CreateProductWithType.this, Ced_MultiVendor_ManageProducts.class);
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
                                                                Toast.makeText(Ced_MultiVendor_CreateProductWithType.this, object1.getJSONObject("data").getString("message"), Toast.LENGTH_LONG).show();
                                                                if (object1.getJSONObject("data").getString("message").equals("URL key for specified store already exists.")) {
                                                                    url_key_linear.setVisibility(View.VISIBLE);
                                                                    MultiVendor_url_key.setError("please specify a new urkl key");
                                                                }

                                                            }
                                                        }

                                                    } else {
                                                        Intent intent13 = new Intent(getApplicationContext(), Ced_MultiVendor_UnAuthourizedRequestError.class);
                                                        intent13.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                        intent13.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                                        startActivity(intent13);
                                                        overridePendingTransition(R.anim.ced_multivendor_slide_in, R.anim.ced_multivendor_slide_out);
                                                    }


                                                }, Ced_MultiVendor_CreateProductWithType.this, "POST", dataforproductsave);
                                                crr.execute(CurrentUrl);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        /************************************************************************/

                    });
                    /****************************continue***************************/
                }

                /****************************addimages***************************/

                addimagees.setOnClickListener(v -> {
                    // addImageLayoutWithPredefinedLayout();

                    View layout = View.inflate(Ced_MultiVendor_CreateProductWithType.this, R.layout.ced_multivendor_add_multiple_images, null);
                    final LinearLayout add_multiple_images_layout = layout.findViewById(R.id.multiple_images_layout);
                    product_image = layout.findViewById(R.id.MultiVendor_productimage);
                    browse_product_image = layout.findViewById(R.id.MultiVendor_browseproductimage);
                    final ImageView remove_image = layout.findViewById(R.id.remove_image);
                    final CheckBox default_checkbox = layout.findViewById(R.id.default_checkbox);
                    final TextView layuot_id = layout.findViewById(R.id.layout_id);
                    layuot_id.setText(String.valueOf(count));
                    imageselection.addView(add_multiple_images_layout);
                    count++;
                    remove_image.setOnClickListener(view -> {
                        add_multiple_images_layout.setVisibility(View.GONE);
                        try {
                            for (int i = 0; i < multiple_images_array.length(); i++) {
                                JSONObject current_object = multiple_images_array.getJSONObject(i);
                                if (current_object.getString("image_id").equals(layuot_id.getText().toString())) {
                                    multiple_images_array.remove(i);
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    });
                    default_checkbox.setOnCheckedChangeListener((compoundButton, ischecked) -> {
                        if (ischecked) {
                            if (is_default_check) {
                                Toast.makeText(Ced_MultiVendor_CreateProductWithType.this, R.string.onlyoneimagecanbeselected, Toast.LENGTH_SHORT).show();
                                default_checkbox.setChecked(false);
                                is_default_check = false;
                            } else {
                                try {
                                    for (int i = 0; i < multiple_images_array.length(); i++) {
                                        JSONObject current_object = multiple_images_array.getJSONObject(i);
                                        if (current_object.getString("image_id").equals(layuot_id.getText().toString())) {
                                            current_object.put("default", true);
                                        } else {
                                            if (current_object.has("deafult")) {
                                                current_object.remove("default");
                                            }
                                        }
                                    /*JSONObject current_object = (JSONObject) multiple_images_array.get(Integer.parseInt(layuot_id.getText().toString()));
                                    current_object.put("default", true);*/
                                    }
                                /*Log.i("currentobject","current object "+current_object.toString());
                                Log.i("currentobject","length-- "+multiple_images_array.length());*/
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                                Log.i("imagechecked", "" + layuot_id.getText().toString());
                                is_default_check = true;
                            }
                        }
                    /*else{
                        is_default_check = false;
                    }*/
                    });
                    browse_product_image.setOnClickListener(view -> {
                        image_id = layuot_id.getText().toString();
                        if (writeresult == PackageManager.PERMISSION_GRANTED && readresult == PackageManager.PERMISSION_GRANTED) {
                            Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                            photoPickerIntent.setType("image/*");
                            SELECT_PHOTO = 1;
                            startActivityForResult(photoPickerIntent, 1);

                        } else {

                            if (ActivityCompat.shouldShowRequestPermissionRationale(Ced_MultiVendor_CreateProductWithType.this, Manifest.permission.READ_EXTERNAL_STORAGE) && ActivityCompat.shouldShowRequestPermissionRationale(Ced_MultiVendor_CreateProductWithType.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                                Toast.makeText(Ced_MultiVendor_CreateProductWithType.this, "Storage permission allows us to access data. Please allow in App Settings for additional functionality.", Toast.LENGTH_LONG).show();
                            } else {
                                ActivityCompat.requestPermissions(Ced_MultiVendor_CreateProductWithType.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                            }
                        }
                    });
                });
                /****************************addimages***************************/
                /****************************addproductattribute***************************/
                if (functionalityList.getProductAddon()) {
                    attributes = object.getJSONObject("data").getJSONArray("attributes");
                    if (attributes.length() > 0) {
                        for (int a = 0; a < attributes.length(); a++) {
                            HashMap<String, String> attribute = new HashMap<String, String>();
                            JSONObject jsonObject = attributes.getJSONObject(a);
                            attribute.put("name", jsonObject.getString("name"));
                            attribute.put("label", jsonObject.getString("label"));
                            attribute.put("type", jsonObject.getString("type"));
                            if (jsonObject.getString("type").equals("select")) {
                                JSONArray attribute_values = jsonObject.getJSONArray("values");
                                ArrayList<String> val = new ArrayList<String>();
                                for (int v = 0; v < attribute_values.length(); v++) {
                                    JSONObject object1 = attribute_values.getJSONObject(v);
                                    select_label_value.put(object1.getString("label"), object1.getString("value"));
                                    val.add(object1.getString("label"));
                                }
                                multiselect_select.put(jsonObject.getString("name"), val);
                            }
                            if (jsonObject.getString("type").equals("multiselect")) {
                                JSONArray attribute_values = jsonObject.getJSONArray("values");
                                ArrayList<String> val = new ArrayList<String>();
                                for (int v = 0; v < attribute_values.length(); v++) {
                                    JSONObject object1 = attribute_values.getJSONObject(v);
                                    label_value.put(object1.getString("label"), object1.getString("value"));
                                    val.add(object1.getString("label"));

                                }
                                multiselect_select.put(jsonObject.getString("name"), val);
                            }
                            attributesarraylist.add(attribute);
                        }
                        Iterator attributelistiterator = attributesarraylist.iterator();
                        while (attributelistiterator.hasNext()) {
                            HashMap<String, String> attributehash = (HashMap<String, String>) attributelistiterator.next();
                            String type = attributehash.get("type");
                            if (type.equals("select")) {
                                View view = View.inflate(getApplicationContext(), R.layout.ced_multivendor_attribute_select_layout, null);
                                TextView name = view.findViewById(R.id.MultiVendor_name);
                                TextView label = view.findViewById(R.id.MultiVendor_label);
                                fontSetting.setFontforTextviews(label, "Roboto-Medium.ttf", getApplicationContext());
                                Spinner spinattribuite = view.findViewById(R.id.MultiVendor_spinattribuite);
                                ArrayAdapter<String> adp_attribute = new ArrayAdapter<String>(getApplicationContext(), R.layout.ced_multivendor_textview, multiselect_select.get(attributehash.get("name")));
                                spinattribuite.setAdapter(adp_attribute);
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
                                name.setText(attributehash.get("name"));
                                label.setText(attributehash.get("label"));
                                fontSetting.setFontforTextviews(label, "Roboto-Medium.ttf", getApplicationContext());
                                TextView type_attribute = view.findViewById(R.id.MultiVendor_type);
                                type_attribute.setText(type);
                                productattribute.addView(view);
                            }
                            if (type.equals("date")) {
                                View view = View.inflate(getApplicationContext(), R.layout.ced_multivendor_attribute_date_layout, null);
                                TextView name = view.findViewById(R.id.MultiVendor_name);
                                TextView label = view.findViewById(R.id.MultiVendor_label);
                                name.setText(attributehash.get("name"));
                                label.setText(attributehash.get("label"));
                                TextView type_attribute = view.findViewById(R.id.MultiVendor_type);
                                fontSetting.setFontforTextviews(label, "Roboto-Medium.ttf", getApplicationContext());
                                type_attribute.setText(type);
                                productattribute.addView(view);
                            }
                            if (type.equals("multiselect")) {
                                View view = View.inflate(getApplicationContext(), R.layout.ced_multivendor_attribute_multiselect_layout, null);
                                TextView name = view.findViewById(R.id.MultiVendor_name);
                                TextView label = view.findViewById(R.id.MultiVendor_label);
                                fontSetting.setFontforTextviews(label, "Roboto-Medium.ttf", getApplicationContext());
                                LinearLayout multiselectlayout = view.findViewById(R.id.MultiVendor_multiselectlayout);
                                Iterator iterator2 = multiselect_select.get(attributehash.get("name")).iterator();
                                int id = Resources.getSystem().getIdentifier("btn_check_holo_light", "drawable", "android");
                                while (iterator2.hasNext()) {
                                    CheckBox checkBox = new CheckBox(getApplicationContext());
                                    checkBox.setText((CharSequence) iterator2.next());
                                    checkBox.setTextColor(getResources().getColor(R.color.black));
                                    checkBox.setButtonDrawable(id);
                                    fontSetting.setfontforCheckbox(checkBox, "Roboto-Regular.ttf", getApplicationContext());
                                    multiselectlayout.addView(checkBox);
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
            Intent nointernet = new Intent(Ced_MultiVendor_CreateProductWithType.this, Ced_MultiVendor_NoInternetconnection.class);
            nointernet.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            nointernet.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(nointernet);
            overridePendingTransition(R.anim.ced_multivendor_slide_in, R.anim.ced_multivendor_slide_out);
        }

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
                category_view = View.inflate(Ced_MultiVendor_CreateProductWithType.this, R.layout.ced_multivendor_maincategory, null);
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
                main_cat.setOnCheckedChangeListener((buttonView, isChecked) -> {
                    if (isChecked) {
                        selected_category_ids.add(String.valueOf(finalMain_cat.getId()));
                    } else {
                        selected_category_ids.remove(String.valueOf(finalMain_cat.getId()));
                    }
                });
                final boolean[] open = {false};
                final ImageView finalCat_icon = cat_icon;
                final ImageView finalCat_icon1 = cat_icon;
                cat_icon.setOnClickListener(v -> {
                    LinearLayout parent = (LinearLayout) finalCat_icon.getParent();
                    LinearLayout subparent = (LinearLayout) parent.getParent();
                    RelativeLayout mainparent = (RelativeLayout) subparent.getParent();
                    TextView subcatjson = (TextView) mainparent.getChildAt(0);
                    LinearLayout subcats1 = (LinearLayout) subparent.getChildAt(1);
                    if (open[0]) {
                        subcats1.removeAllViews();
                        open[0] = false;
                        finalCat_icon1.setImageResource(R.drawable.expand);
                    } else {
                        try {
                            if (!(subcatjson.getText().toString().isEmpty())) {
                                JSONArray array1 = new JSONArray(subcatjson.getText().toString());
                                createsubcat(array1, subcats1);
                                open[0] = true;
                                finalCat_icon1.setImageResource(R.drawable.collapse);
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
                subcats.addView(category_view);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

   /* private void bindSubcategory(JSONArray catfilters) throws JSONException
    {
        try
        {
            final CheckBox cb[] = new CheckBox[catfilters.length()];
            //add_categories(categories);
            if(catfilters.length()>0)
            {
                for (int i = 0; i < catfilters.length(); i++)
                {

                    final JSONObject current_object = catfilters.getJSONObject(i);
                    //if(current_object.has("main_category")){
                    String main_categories = current_object.getString("main_category");
                    String category_array[] = main_categories.split("#");
                    final String category_id = category_array[0];
                    String category_title = category_array[1];
                    View main_cat_section = null;
                    main_cat_section = View.inflate(this, R.layout.ced_multivendor_maincategory, null);
                    LinearLayout maincat = (LinearLayout) main_cat_section.findViewById(R.id.multivendor_maincat);

                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    layoutParams.setMargins(margin, 0, 0, 0);
                    maincat.setLayoutParams(layoutParams);


                    TextView cat_title = (TextView) main_cat_section.findViewById(R.id.main_cat_title);
                    final TextView cat_id = (TextView) main_cat_section.findViewById(R.id.main_cat_id);
                    final TextView maincat_expand = (TextView) main_cat_section.findViewById(R.id.maincat_expand);
                    final ImageView cat_icon1 = (ImageView) main_cat_section.findViewById(R.id.cat_icon1);
                    LinearLayout checkboxlayout = (LinearLayout) main_cat_section.findViewById(R.id.checkboxlayout);

                    cb[i] = new CheckBox(this);
                    cb[i].setId(Integer.parseInt(category_id));
                    cb[i].setButtonDrawable(idd);
                    checkboxlayout.addView(cb[i]);
                    for (int l = 0; l < saved_category_ids.length(); l++)
                    {
                        if (saved_category_ids.getString(l).equals(category_id))
                        {
                            cb[i].setChecked(true);
                        }
                    }

                    final int finalI = i;
                    cb[i].setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
                    {
                        @Override
                        public void onCheckedChanged(CompoundButton compoundButton, boolean ischecked)
                        {

                            if (ischecked)
                            {
                                Log.i("checkedid", "checked id " + cb[finalI].getId());
                                selected_category_ids.add(String.valueOf(cb[finalI].getId()));
                            }
                            else
                            {
                                selected_category_ids.remove(String.valueOf(cb[finalI].getId()));
                            }
                        }
                    });
                    final LinearLayout subcat_layout = (LinearLayout) main_cat_section.findViewById(R.id.subcategory_section);
                    cat_title.setText(category_title);
                    cat_id.setText(category_id);

                    categoryselection.addView(maincat);

                    Log.i("addproduct", "id-- " + category_array[0] + "title-- " + category_array[1]);
                    if (current_object.has("sub_categories"))
                    {
                        Log.i("category"," -> "+current_object.getString("main_category")+"Lvl: "+level);
                        bindSubcategory(current_object.getJSONArray("sub_categories"));
                        level++;
                    }
                }

            }
        }
        catch (Exception e)
        {
        e.printStackTrace();
        }

    }*/


    @Override
    protected void onResume() {
        Ced_MultiVendor_ConnectionDetector connectionDetector = new Ced_MultiVendor_ConnectionDetector(Ced_MultiVendor_CreateProductWithType.this);
        if (connectionDetector.isConnectingToInternet()) {
            setname(session.getvendorname());
            setprofilepic(session.getvendorpic());
            changetitle("Add Product");
            writeresult = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
            readresult = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE);
            invalidateOptionsMenu();
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
                producturi = getRealPathFromURI(Ced_MultiVendor_CreateProductWithType.this, imageUri);
                String[] imagename = getRealPathFromURI(Ced_MultiVendor_CreateProductWithType.this, imageUri).split("/");
                if (getResources().getString(R.string.Enable_Log).equals("yes")) {
                    Log.i("filename", "" + imagename[imagename.length - 1]);
                }
                productpicname = imagename[imagename.length - 1];
                Log.i("9044_name", "" + productpicname);
                Log.i("9044_length", "" + productpicname.length());
                String[] parts = productpicname.split("\\.");
                String firstSubString = parts[0];
                String secondSubString = parts[1];
                Log.i("9044_part", "" + secondSubString);
                final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                product_image.setImageBitmap(selectedImage);
                encodedproductImage = encodeImage(selectedImage);
                try {
                    if (!encodedproductImage.equals("")) {
                        JSONObject selected_images = new JSONObject();
                        selected_images.put("type", "image/" + "png");
                        selected_images.put("name", firstSubString + ".png");
                        selected_images.put("base64_encoded_data", encodedproductImage);
                        selected_images.put("image_id", image_id);
                        multiple_images_array.put(selected_images);
                        Log.i("checkmultipleimages", "Array-- " + multiple_images_array.toString());
                        Log.i("sending_val", "Array-- " + selected_images.toString());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Log.i("checkmultipleimages", "Array-- " + multiple_images_array.toString());
                Log.i("encodedimage", encodedproductImage);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

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
        bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] b = baos.toByteArray();
        String encImage = Base64.encodeToString(b, Base64.DEFAULT);

        return encImage;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.ced_multivendor_menu_login, menu);
        MenuItem item = menu.findItem(R.id.MultiVendor_notification);
        MenuItem item2 = menu.findItem(R.id.MultiVendor_profile);
        MenuItemCompat.setActionView(item, R.layout.ced_multivendor_feed_update_count);
        MenuItemCompat.setActionView(item2, R.layout.ced_multivendor_profilestatus);
        View profilecount = MenuItemCompat.getActionView(item2);
        profilecount.setOnClickListener(v -> {
            /*Toast.makeText(getApplicationContext(), "Profile", Toast.LENGTH_LONG).show();*/
            Intent intent = new Intent(getApplicationContext(), Ced_MultiVendor_ProfileStatus.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);


        });
        View notifCount = MenuItemCompat.getActionView(item);
        TextView notification = notifCount.findViewById(R.id.MultiVendor_notitext);
        notification.setText(Ced_MultiVendor_GlobalVariables.noti);
        notifCount.setOnClickListener(v -> {
            /*Toast.makeText(getApplicationContext(), "Notification", Toast.LENGTH_LONG).show();*/
            Intent intent = new Intent(getApplicationContext(), Notification.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
        });

        return true;
    }

    private void changecount() {
        invalidateOptionsMenu();
    }

    public void addImageLayoutWithPredefinedLayout() {
        if (writeresult == PackageManager.PERMISSION_GRANTED && readresult == PackageManager.PERMISSION_GRANTED) {
            View view = View.inflate(this, R.layout.ced_multivendor_addimagelayout, null);
            final CheckBox defaultimage = view.findViewById(R.id.MultiVendor_defaultimage);
            defaultimage.setButtonDrawable(id);
            final Button browse = view.findViewById(R.id.MultiVendor_browse);

            browse.setOnClickListener(v -> {
                button = browse;
                final CharSequence[] items = {"Take Photo", "Choose from Gallery", "Cancel"};
                AlertDialog.Builder builder = new AlertDialog.Builder(Ced_MultiVendor_CreateProductWithType.this);
                builder.setTitle(getResources().getString(R.string.takephoto));
                builder.setItems(items, (dialog, item) -> {

                    if (items[item].equals("Take Photo")) {
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        RelativeLayout RelativeLayout = (RelativeLayout) browse.getParent();
                        imageuri = (TextView) RelativeLayout.getChildAt(0);
                        uriurl = (TextView) RelativeLayout.getChildAt(3);
                        startActivityForResult(intent, 2);
                    } else if (items[item].equals("Choose from Gallery")) {
                        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                        photoPickerIntent.setType("image/*");
                        RelativeLayout RelativeLayout = (RelativeLayout) browse.getParent();
                        imageuri = (TextView) RelativeLayout.getChildAt(0);
                        uriurl = (TextView) RelativeLayout.getChildAt(3);
                        startActivityForResult(photoPickerIntent, 1);
                    } else if (items[item].equals("Cancel")) {
                        dialog.dismiss();
                    }
                });
                builder.show();


            });
            final ImageView cancel = view.findViewById(R.id.MultiVendor_cancel);
            cancel.setOnClickListener(v -> {

                RelativeLayout relativeLayout = (RelativeLayout) cancel.getParent();
                CardView cardView = (CardView) relativeLayout.getParent();
                TextView view1 = (TextView) relativeLayout.getChildAt(0);
                TextView viewurl = (TextView) relativeLayout.getChildAt(3);
                if (view1.getText().toString().equals("Images")) {
                    imageselection.removeView(cardView);
                } else {
                    uris.remove(viewurl.getText().toString());
                    if (defaultimagehash.containsKey(viewurl.getText().toString())) {
                        defaultimagehash.clear();
                    }
                    imageselection.removeView(cardView);

                }

            });
            defaultimage.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (isChecked) {
                    if (defaultimagehash.size() > 0) {
                        defaultimage.setChecked(false);
                        Toast.makeText(Ced_MultiVendor_CreateProductWithType.this, "Default image can only be one", Toast.LENGTH_LONG).show();
                    } else {
                        if (defaultimagehash.size() == 0) {
                            RelativeLayout parentlayout = (RelativeLayout) defaultimage.getParent();
                            TextView uriView = (TextView) parentlayout.getChildAt(0);
                            TextView uriViewurl = (TextView) parentlayout.getChildAt(3);
                            if (uriView.getText().toString().equals("Images")) {
                                defaultimage.setChecked(false);
                                Toast.makeText(Ced_MultiVendor_CreateProductWithType.this, "Please Select some image first", Toast.LENGTH_LONG).show();
                            } else {
                                defaultimagehash.put(uriViewurl.getText().toString(), "1");
                            }
                        }
                    }
                } else {
                    defaultimagehash.clear();
                }

            });
            imageselection.addView(view);
            /*mainvendoreditprofilescroll.post(new Runnable()
            {
                @Override
                public void run()
                {
                    mainvendoreditprofilescroll.fullScroll(ScrollView.FOCUS_DOWN);

                }
            });*/
        } else {
            Toast.makeText(Ced_MultiVendor_CreateProductWithType.this, "Storage permission allows us to access data. Please allow in App Settings for additional functionality.", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            Uri uri = Uri.fromParts("package", getPackageName(), null);
            intent.setData(uri);
            startActivityForResult(intent, 1);

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

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }


    public void showTruitonDatePickerDialog(View v) {
        DialogFragment newFragment = new Ced_MultiVendor_CreateProductWithType.DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    public void showToDatePickerDialog(View v) {
        DialogFragment newFragment = new Ced_MultiVendor_CreateProductWithType.ToDatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    protected String getSaltString() {
        String SALTCHARS = /*product_name.getText().toString().trim() + product_sku.getText().toString().trim() +*/ "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
        StringBuilder salt = new StringBuilder();
        Random rnd = new Random();
        while (salt.length() < 9) { // length of the random string.
            int index = (int) (rnd.nextFloat() * SALTCHARS.length());
            salt.append(SALTCHARS.charAt(index));
        }
        String saltStr = salt.toString();
        return saltStr;

    }

    public static class DatePickerFragment extends DialogFragment implements
            DatePickerDialog.OnDateSetListener {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);
            DatePickerDialog datePickerDialog;
            datePickerDialog = new DatePickerDialog(getActivity(), this, year,
                    month, day);
            return datePickerDialog;
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            // Do something with the date chosen by the user
            MultiVendor_edt_start_date.setText(year + "/" + (month + 1) + "/" + day);
        }

    }

    public static class ToDatePickerFragment extends DialogFragment implements
            DatePickerDialog.OnDateSetListener {
        // Calendar startDateCalendar=Calendar.getInstance();
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {

            // Use the current date as the default date in the picker
            String getfromdate = MultiVendor_edt_start_date.getText().toString().trim();
            String[] getfrom = getfromdate.split("/");
            int year, month, day;
            year = Integer.parseInt(getfrom[0]);
            int temp = Integer.parseInt(getfrom[1]);
            month = temp - 1;
            day = Integer.parseInt(getfrom[2]);
            final Calendar c = Calendar.getInstance();
            c.set(year, month, day + 1);
            DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), this, year, month, day);
            datePickerDialog.getDatePicker().setMinDate(c.getTimeInMillis());
            return datePickerDialog;
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            month = month + 1;
            MultiVendor_edt_end_date.setText(year + "/" + month + "/" + day);
        }
    }
}