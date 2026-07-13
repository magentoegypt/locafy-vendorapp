package magentoegypt.locafy.manage_products_section;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.READ_MEDIA_AUDIO;
import static android.Manifest.permission.READ_MEDIA_IMAGES;
import static android.Manifest.permission.READ_MEDIA_VIDEO;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static magentoegypt.locafy_constant.AppConstant.KEY_is_required;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.CalendarContract;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.SwitchCompat;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.widget.CompoundButtonCompat;

import magentoegypt.locafy.addons.DecimalDigitsInputFilter;
import magentoegypt.locafy.gallary.ImagePickerManager;
import magentoegypt.locafy.manage_products_section.model.AttributeModelConfig;
import com.google.android.gms.common.util.ArrayUtils;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import magentoegypt.locafy.R;
import magentoegypt.locafy.api_request_response_section.Ced_ClientRequestResponseRest;
import magentoegypt.locafy.api_request_response_section.Ced_MultiVendor_ClientRequestResponse;
import magentoegypt.locafy.api_request_response_section.network_error_handling.Ced_MultiVendor_UnAuthourizedRequestError;
import magentoegypt.locafy.base_app.Ced_MultiVendor_VendorFunctionalityList;
import magentoegypt.locafy.navigation_drawer.Activity.Ced_MultiVendor_NavigationActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Set;

public class ProductCreationNew extends Ced_MultiVendor_NavigationActivity implements View.OnClickListener {
    private static final String TAG = "ProductCreationNew";
    private final String POST_FIELD = "attribute_code";
    private final String FIELD_NAME = "name";
    private final int galerry_code = 1001;
    private String selected_websitetopost;
    private String producturi = "";
    private String productpicname = "";
    private String encodedproductImage = "";
    private String image_id;
    private String Jstring;
    private Ced_MultiVendor_VendorFunctionalityList functionalityList;
    private JSONArray category;
    private JSONObject websites_r, storeViews_r, stores_r;
    private LinearLayout categoryselection,lin_child_config;
    private int sku_position = 0;
    private int selectImage_position = 0;
    private String sku_txt = "";
    private LinearLayout parent_view, imageselection, websiteselection;
    private String product_form_url = "";
    private String attribute_set, type;
    private HashMap<String, String> params;
    private LinkedHashMap<String, String> post_param,updateParams;
    private JSONObject requiredFields, tabs;
    private TextView MultiVendor_save, savecontinue;
    private JSONArray multiselect_data;
    private SimpleDateFormat sdf;
    private ImageView product_image;
    private String CurrentUrl;
    private Button addimagees;
    private int count = 0;
    private JSONArray multiple_images_array;
    private boolean is_default_check = false,isNeedRefreshConfig = false, isProductApiCall = false;
    private CheckBox website;
    private HashMap<String, String> formainwebsites;
    private HashMap<String, ArrayList<String>> forstores;
    private HashMap<String, ArrayList<String>> forstoreviews;
    private ArrayList<String> idsforwebsites;
    private HashSet<String> selected_category_ids;
    private int idd;
    private EditText weightField=null,priceField=null;
    String priceName="",weightName="";
    private int noweightPosition=0,totalImageApiCall=0;
    List<String> associatedProductIdsArr = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewGroup content = findViewById(R.id.MultiVendor_frame_container);
        getLayoutInflater().inflate(R.layout.activity_product_creation_new, content, true);
        ConfigAttributeSelectionActivity.attributeModelListSelected.clear();
        ConfigAttributeSelectionActivity.attributeModelListFinal.clear();
        ConfigAttributeSelectionActivity.attributeModelEditListFinal.clear();
        initUi();

        if (getIntent().hasExtra("category")) {
            try {
                String str = getIntent().getStringExtra("category");
                category = new JSONArray(str);
            }
            catch (JSONException e) {
                e.printStackTrace();
            }
        }

        if (getIntent().hasExtra("websites")) {
            try {
                String str = getIntent().getStringExtra("websites");
                websites_r = new JSONObject(str);
            }
            catch (JSONException e) {
                e.printStackTrace();
            }
        }

        if (getIntent().hasExtra("storeViews")) {
            try {
                String str = getIntent().getStringExtra("storeViews");
                storeViews_r = new JSONObject(str);
            }
            catch (JSONException e) {
                e.printStackTrace();
            }
        }

        if (getIntent().hasExtra("stores")) {
            try {
                String str = getIntent().getStringExtra("stores");
                stores_r = new JSONObject(str);
            }
            catch (JSONException e) {
                e.printStackTrace();
            }
        }

        if (getIntent().hasExtra("dataforproductcreation")) {
            Jstring = getIntent().getStringExtra("dataforproductcreation");
            try {
                JSONObject object = new JSONObject(Jstring);
                tabs = object.getJSONObject("data").getJSONObject("tabs");

                /****************************taxes***************************/
                /****************************websites***************************/

                JSONArray websitesvalues = websites_r.names();
                for (int t = 0; t < websitesvalues.length(); t++) {
                    formainwebsites.put(websites_r.getString((String) websitesvalues.get(t)), (String) websitesvalues.get(t));
                       /* if(object.getJSONObject("data").has("stores"))
                        {*/
                    /* storesobject = object.getJSONObject("data").getJSONObject("stores");*/
                    if (stores_r.has(websites_r.getString((String) websitesvalues.get(t)))) {
                        JSONArray stores = stores_r.getJSONArray(websites_r.getString((String) websitesvalues.get(t)));
                        ArrayList<String> storestring = new ArrayList<>();
                        for (int s = 0; s < stores.length(); s++) {
                            storestring.add((String) stores.get(s));
                        }
                        forstores.put(websites_r.getString((String) websitesvalues.get(t)), storestring);
                    }

                            /*if(object.getJSONObject("data").has("storeViews"))
                            {*/
                    /* storeviewssobject = object.getJSONObject("data").getJSONObject("storeViews");*/

                    if (storeViews_r.has(websites_r.getString((String) websitesvalues.get(t)))) {
                        JSONArray storeviews = storeViews_r.getJSONObject(websites_r.getString((String) websitesvalues.get(t))).names();
                        for (int view = 0; view < storeviews.length(); view++) {
                            ArrayList<String> views = new ArrayList<>();
                            JSONArray storeviewjsonarray = storeViews_r.getJSONObject(websites_r.getString((String) websitesvalues.get(t))).getJSONArray((String) storeviews.get(view));
                            for (int j = 0; j < storeviewjsonarray.length(); j++) {
                                views.add((String) storeviewjsonarray.get(j));
                            }
                            forstoreviews.put((String) storeviews.get(view), views);
//                    Bayer Retail Store -> {ArrayList@18750}  size = 1
                        }
                    }
                    /* }*/
                    /* }*/
                }

                /****************************websites***************************/
                Iterator iterator = formainwebsites.keySet().iterator();
                while (iterator.hasNext()) {
                    final LinearLayout mainwebsite = new LinearLayout(ProductCreationNew.this);
                    mainwebsite.setOrientation(LinearLayout.VERTICAL);
                    final CheckBox mainwebsitename = new CheckBox(ProductCreationNew.this);
                    mainwebsitename.setButtonDrawable(idd);
                    String websitename = iterator.next().toString();
                    mainwebsitename.setText(websitename);
                    mainwebsitename.setTypeface(Typeface.DEFAULT_BOLD);
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
                    mainwebsite.addView(mainwebsitename, 0);
                    try {
                        if (forstores.containsKey(websitename)) {
                            ArrayList<String> storeandview = forstores.get(websitename);
                            Iterator iterator2 = storeandview.iterator();
                            while (iterator2.hasNext()) {
                                LinearLayout storelayout = new LinearLayout(ProductCreationNew.this);
                                storelayout.setOrientation(LinearLayout.VERTICAL);
                                TextView storename = new TextView(ProductCreationNew.this);
                                String storenametag = iterator2.next().toString();
                                storename.setText(storenametag);
                                storename.setTypeface(Typeface.DEFAULT_BOLD);
                                storename.setTextColor(getResources().getColor(R.color.Red_700));
                                storename.setPadding(15, 0, 0, 0);
                                storelayout.addView(storename, 0);

                                ArrayList<String> storeviews = forstoreviews.get(storenametag);
                                if (storeviews != null) {
                                    Iterator iterator3 = storeviews.iterator();
                                    LinearLayout storeviewlayout = new LinearLayout(ProductCreationNew.this);
                                    storeviewlayout.setOrientation(LinearLayout.VERTICAL);
                                    storeviewlayout.setPadding(25, 0, 0, 0);
                                    while (iterator3.hasNext()) {
                                        TextView view = new TextView(ProductCreationNew.this);
                                        String Storeviewname = iterator3.next().toString();
                                        view.setText(Storeviewname);
                                        view.setTextColor(getResources().getColor(R.color.black));
                                        storeviewlayout.addView(view);
                                    }
                                    storelayout.addView(storeviewlayout, 1);
                                  //  mainwebsite.addView(storelayout);
                                }
                            }
                        }
                        websiteselection.addView(mainwebsite);
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            }
            catch (JSONException e) {
                e.printStackTrace();
            }
        }

        website.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                websiteselection.setVisibility(View.VISIBLE);
            }
            else {
                websiteselection.setVisibility(View.GONE);
            }
        });
        createForm();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(isNeedRefreshConfig){
            isNeedRefreshConfig = false;
            populateConfigForm();
        }
    }

    void populateConfigForm(){
        lin_child_config.removeAllViews();
        for (AttributeModelConfig config : ConfigAttributeSelectionActivity.attributeModelListFinal) {
            View summaryView = LayoutInflater.from(this).inflate(R.layout.item_config_summary, null);
            summaryView.findViewById(R.id.weightSection).setVisibility(View.VISIBLE);
            summaryView.findViewById(R.id.statusSection).setVisibility(View.VISIBLE);
            TextView attributeLabel = summaryView.findViewById(R.id.attributeLabel);
            EditText nameTxtField = summaryView.findViewById(R.id.nameTxtField);
            EditText skuField = summaryView.findViewById(R.id.skuTxtField);
            EditText quantityField = summaryView.findViewById(R.id.quantityTxtField);
            EditText priceField = summaryView.findViewById(R.id.priceTxtField);
            EditText weightTxtField = summaryView.findViewById(R.id.weightTxtField);
            ImageView imageView = summaryView.findViewById(R.id.imageView);
            TextView statusValue = summaryView.findViewById(R.id.statusValue);
            TextView selectButton = summaryView.findViewById(R.id.selectButton);
            imageView.setImageBitmap(config.getImage());
            statusValue.setText(config.isEnable() ? getString(R.string.enabled):getString(R.string.disabled));
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ImagePickerManager.presentImagePicker(ProductCreationNew.this,0, new ImagePickerManager.ImagePickerCallback() {
               @Override
               public void onImagePicked(@Nullable Bitmap bitmap) {
                   if (bitmap != null) {
                       config.setImage(bitmap);
                       imageView.setImageBitmap(bitmap);  // set in your ImageView
                   }
               }
           });}
            });
            skuField.setText(config.getSkulabel());
            nameTxtField.setText(config.getNamelabel());
            attributeLabel.setText(config.getTitle());
            quantityField.setText(config.getQuantity());
            priceField.setText(config.getPrice());
            selectButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showOptionsDialog(config,summaryView,statusValue);
                }
            });
            addChangeListener(config,skuField,"skuField");
            addChangeListener(config,nameTxtField,"nameTxtField");
            addChangeListener(config,quantityField,"quantityField");
            addChangeListener(config,priceField,"priceField");
            addChangeListener(config,weightTxtField,"weightTxtField");
            if (config.getImage() != null) {
                imageView.setImageBitmap(config.getImage());
            }
            // Make fields non-editable
            lin_child_config.addView(summaryView);
        }
    }

    void  addChangeListener(AttributeModelConfig attributeModel,EditText inputField,String caseString){
        inputField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (caseString.equalsIgnoreCase("skuField")) {
                    attributeModel.setSkulabel(s.toString());
                }else if (caseString.equalsIgnoreCase("nameTxtField")) {
                    attributeModel.setNamelabel(s.toString());
                }else if (caseString.equalsIgnoreCase("priceField")) {
                    attributeModel.setPrice(s.toString());
                }else if (caseString.equalsIgnoreCase("weightTxtField")) {
                    attributeModel.setWeight(s.toString());
                } else {
                    attributeModel.setQuantity(s.toString());
                }
            }
        });
    }

    public void showOptionsDialog(AttributeModelConfig attributeModel,View summaryView,TextView statusValue) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.alert_name));
        List<String> stringList = new ArrayList<>();
        if(attributeModel.isEnable()){
            stringList.add(getString(R.string.disabled));
        }else{
            stringList.add(getString(R.string.enabled));
        }
        stringList.add(getString(R.string.remove_product));
        String[] items = stringList.toArray(new String[0]);
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int position) {
                if(position == 0){
                    if(attributeModel.isEnable()){
                        attributeModel.setEnable(false);
                    }else{
                        attributeModel.setEnable(true);
                    }
                    statusValue.setText(attributeModel.isEnable() ? getString(R.string.enabled):getString(R.string.disabled));
                }else if(position == 1){
                    ConfigAttributeSelectionActivity.attributeModelListFinal.remove(attributeModel);
                    lin_child_config.removeView(summaryView);
                }
            }
        });
        builder.setNegativeButton(getString(R.string.cancel), null);
        builder.show();
    }

    private void initUi() {
        product_form_url = session.getBase_Url() + "vproductapi/vproducts/productform";
        CurrentUrl = session.getBase_Url() + "vproductapi/vproducts/createproduct";
        requiredFields = new JSONObject();
        post_param = new LinkedHashMap<>();
        updateParams = new LinkedHashMap<>();
        formainwebsites = new HashMap<>();
        forstores = new HashMap<>();
        forstoreviews = new HashMap<>();
        idsforwebsites = new ArrayList<>();
        functionalityList = new Ced_MultiVendor_VendorFunctionalityList(getApplicationContext());
        multiselect_data = new JSONArray();
        selected_category_ids = new HashSet<>();
        categoryselection = findViewById(R.id.MultiVendor_categoryselection);
        websiteselection = findViewById(R.id.MultiVendor_websiteselection);
        website = findViewById(R.id.MultiVendor_website);
        idd = Resources.getSystem().getIdentifier("btn_check_holo_light", "drawable", "android");
        savecontinue = findViewById(R.id.MultiVendor_savecontinue);
        savecontinue.setOnClickListener(this);
        sdf = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
//        sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        parent_view = findViewById(R.id.parent_view);
        params = new HashMap<>();
        addimagees = findViewById(R.id.MultiVendor_addimagees);
        imageselection = findViewById(R.id.MultiVendor_imageselection);
        addimagees.setOnClickListener(this);
        MultiVendor_save = findViewById(R.id.MultiVendor_save);
        MultiVendor_save.setOnClickListener(this);
        multiple_images_array = new JSONArray();

        if (getIntent().hasExtra("type") && getIntent().hasExtra("attribute_set")) {
            type = getIntent().getStringExtra("type");
            attribute_set = getIntent().getStringExtra("attribute_set");
        }

    }

    private void createForm() {
        if (session.getStoreId() != null)
            params.put("store_id", session.getStoreId());
        params.put("vendor_id", session.getVendorid());
        params.put("set", attribute_set);
        params.put("type", type);
        params.put("hashkey", session.getHahkey());
        /*params.put("last_category", last_category_id);*/
        Ced_MultiVendor_ClientRequestResponse requestResponse = new Ced_MultiVendor_ClientRequestResponse(output -> {
            Log.d(TAG, "processFinish: " + output);
            JSONArray response_arr = new JSONArray(output.toString());
            productForm(response_arr);
        }, ProductCreationNew.this, "POST", params);
        requestResponse.execute(product_form_url);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void productForm(JSONArray response_arr) throws JSONException {

        TextView selected_label = findViewById(R.id.selected_label);
        if (response_arr.getJSONObject(0).has("category_tree")) {
            selected_label.setText(response_arr.getJSONObject(0).getString("category_tree"));
        }
        else {
            /* selected_label.setText(category.toString());*/
        }
        //---------------

        /* catfilters = object.getJSONObject("data").getJSONArray("category");*/
        if (category.length() > 0) {
            JSONObject current_object = null;
            View category_view = null;
            TextView subcategories = null;
            CheckBox main_cat = null;
            ImageView cat_icon = null;
            String maincatname = null;
            String maincatid = null;
            String[] parts = new String[2];
            for (int i = 0; i < category.length(); i++) {
                current_object = category.getJSONObject(i);
                category_view = View.inflate(ProductCreationNew.this, R.layout.ced_multivendor_maincategory2, null);
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


                /*for (int h = 0; h < productcategories.length(); h++) {
                    if (String.valueOf(main_cat.getId()).equals(productcategories.getString(h))) {
                        main_cat.setChecked(true);
                    }
                    // productcategories_list.add(productcategories.get(h));
                }*/

                /*if(String.valueOf(main_cat.getId()).equals("2"))
                {
                    main_cat.setChecked(true);
                }*/
                Log.d(TAG, "selected_category_ids" + selected_category_ids.toString());
                final CheckBox finalMain_cat = main_cat;
               // main_cat.setChecked(false);
              //  main_cat.setEnabled(false);

//                main_cat.setOnCheckedChangeListener((buttonView, isChecked) -> {
//                    if (isChecked) {
//                        selected_category_ids.add(String.valueOf(finalMain_cat.getId()));
//                    } else {
//                        selected_category_ids.remove(String.valueOf(finalMain_cat.getId()));
//                    }
//                });
                final boolean[] open = {false};
                final ImageView finalCat_icon = cat_icon;
                final ImageView finalCat_icon1 = cat_icon;
                if(maincatname.equalsIgnoreCase("Default Category") || maincatname.equalsIgnoreCase("صنف المنتج")){
                    cat_icon.setVisibility(View.INVISIBLE);
                    int [][] states = {{android.R.attr.state_checked}, {}};
                    int [] colors = {Color.WHITE, Color.WHITE};
                    CompoundButtonCompat.setButtonTintList(main_cat, new ColorStateList(states, colors));
                  //  main_cat.setButtonDrawable(null);
                }
                cat_icon.setOnClickListener(v -> {
                    LinearLayout parent = (LinearLayout) finalCat_icon.getParent();
                    LinearLayout subparent = (LinearLayout) parent.getParent();
                    RelativeLayout mainparent = (RelativeLayout) subparent.getParent();
                    TextView subcatjson = (TextView) mainparent.getChildAt(0);
                    LinearLayout subcats = (LinearLayout) subparent.getChildAt(1);
                    Log.e("REpo", "cat_icon.setOnClickListener0");
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
                if(i == 0){
                    LinearLayout parent = (LinearLayout) finalCat_icon.getParent();
                    LinearLayout subparent = (LinearLayout) parent.getParent();
                    RelativeLayout mainparent = (RelativeLayout) subparent.getParent();
                    TextView subcatjson = (TextView) mainparent.getChildAt(0);
                    LinearLayout subcats = (LinearLayout) subparent.getChildAt(1);
                    JSONArray array = new JSONArray(subcatjson.getText().toString());
                    createsubcat(array, subcats);
                    open[0] = true;
                    finalCat_icon1.setImageResource(R.drawable.collapse);
                }
                categoryselection.addView(category_view);
            }
        }

        //----------
        for (int i = 0; i < response_arr.length(); i++) {
            Log.d(TAG, "processFinish: " + response_arr.get(i));
            JSONObject jsonObject = response_arr.getJSONObject(i);
            //obj_array.remove(0);
            Iterator<?> keys = jsonObject.keys();
            String attributeSetKey = "";
            String[] generalKeys = {"#General","#عام"};
          //  String[] attributeSetKeys = {"#Attribute Set","#أباجورات و لمباديرات"};
            String[] advancedPricingKeys = {"#Advanced Pricing","#التسعير المتقدم"};
            String[] contentKeys = {"#Content","#المحتوى"};
            String[] seoKeys = {"#Search Engine Optimization","#تحسين محرك البحث"};
            String[] eliminateKeys = {"#Facebook Attribute Group","#Subscriptions by Stripe","#Subscriptions by APS"};

            for(String name:generalKeys){
                if(jsonObject.has(name)){
                    JSONArray obj_inner_array = jsonObject.getJSONArray(name);
                    popuLateForm(obj_inner_array,name);
                }
            }
//            if(!attributeSetKey.isEmpty()){
//                if(jsonObject.has(attributeSetKey)){
//                    JSONArray obj_inner_array = jsonObject.getJSONArray(attributeSetKey);
//                    popuLateForm(obj_inner_array,attributeSetKey);
//                }
//            }else{
//                for(String name:attributeSetKeys){
//                    if(jsonObject.has(name)){
//                        JSONArray obj_inner_array = jsonObject.getJSONArray(name);
//                        popuLateForm(obj_inner_array,name);
//                    }
//                }
//            }
            while( keys.hasNext()) {
                String key = (String) keys.next();
                System.out.println("Key: " + key);
                if(!ArrayUtils.contains(generalKeys,key)
                        && !ArrayUtils.contains(advancedPricingKeys,key)
                        && !ArrayUtils.contains(contentKeys,key) && !ArrayUtils.contains(seoKeys,key)
                        && !ArrayUtils.contains(eliminateKeys,key)){
                    JSONArray obj_inner_array = jsonObject.getJSONArray(key);
                    popuLateForm(obj_inner_array,key);
                }
            }

//            for(String name:advancedPricingKeys){
//                if(jsonObject.has(name)){
//                    JSONArray obj_inner_array = jsonObject.getJSONArray(name);
//                    popuLateForm(obj_inner_array,name);
//                }
//            }
            for(String name:contentKeys){
                if(jsonObject.has(name)){
                    JSONArray obj_inner_array = jsonObject.getJSONArray(name);
                    popuLateForm(obj_inner_array,name);
                }
            }
            if(type.equalsIgnoreCase("configurable")){
                View layout = View.inflate(this, R.layout.configurations_card, null);
                lin_child_config = layout.findViewById(R.id.lin_child_config);
                parent_view.addView(layout);
                layout.findViewById(R.id.textCreateConfigurations).setOnClickListener(v -> {
                    isNeedRefreshConfig = true;
                    Intent intent = new Intent(ProductCreationNew.this, ConfigAttributeSelectionActivity.class);
                    intent.putExtra("dataforproductcreation", Jstring);
                    startActivity(intent);
                    overridePendingTransition(R.anim.ced_multivendor_slide_in, R.anim.ced_multivendor_slide_out);
                });
            }
//            for(String name:seoKeys){
//                if(jsonObject.has(name)){
//                    JSONArray obj_inner_array = jsonObject.getJSONArray(name);
//                    popuLateForm(obj_inner_array,name);
//                }
//            }
        }
    }

    private void popuLateForm(JSONArray obj_inner_array,String headerTitle){
        /*Layout fields*/
        View view;
        TextView header_title, MultiVendor_label, date_text;
        LinearLayout.LayoutParams params;
        ArrayList<String> arrayList;
        HashMap<String, String> selected_values;
        Spinner spn_delivery;
        SwitchCompat switch_but;
        LinearLayout parent_lin_lyt;
        CheckBox brand_chk, default_checkbox;
        EditText MultiVendor_textattribuite;
        ImageView MultiVendor_productimage, remove_image;
        Button MultiVendor_browseproductimage;
        try {

            params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            params.setMargins(0, 10, 0, 10);
            header_title = new TextView(ProductCreationNew.this);
            header_title.setTextColor(getResources().getColor(R.color.white));
            header_title.setAllCaps(true);
            header_title.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            header_title.setTypeface(null, Typeface.BOLD);
            header_title.setTextSize(20f);
            header_title.setLayoutParams(params);
            header_title.setText(headerTitle.replace("#", ""));
            header_title.setBackgroundColor(getResources().getColor(R.color.fb_color));
            float paddingDp = 10f;
            // Convert to pixels
            int paddingPx = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, paddingDp, getResources().getDisplayMetrics());
            header_title.setPadding(paddingPx,paddingPx,paddingPx,paddingPx);
                parent_view.addView(header_title);
                for (int k = 0; k < obj_inner_array.length(); k++) {
                    final JSONObject jsonObject = obj_inner_array.getJSONObject(k);
                    if(jsonObject.getString(POST_FIELD).equalsIgnoreCase("admin_sku")
                            || jsonObject.getString(POST_FIELD).equalsIgnoreCase("status")
                            || jsonObject.getString(POST_FIELD).equalsIgnoreCase("ced_rpoint")
                            || jsonObject.getString(POST_FIELD).equalsIgnoreCase("is_featured")
                            || jsonObject.getString(POST_FIELD).equalsIgnoreCase("tax_class_id")
                            || jsonObject.getString(POST_FIELD).equalsIgnoreCase("visibility")
                            || jsonObject.getString(POST_FIELD).equalsIgnoreCase("clothes_color")
                            || jsonObject.getString(POST_FIELD).equalsIgnoreCase("price")
                            || jsonObject.getString(POST_FIELD).equalsIgnoreCase("vendor_id")
                            || jsonObject.getString(POST_FIELD).equalsIgnoreCase("Wood_Color")
                            || jsonObject.getString(POST_FIELD).equalsIgnoreCase("attr_5b419564")
                            || jsonObject.getString(POST_FIELD).equalsIgnoreCase("Wood_Type")
                            || jsonObject.getString(POST_FIELD).equalsIgnoreCase("deposit_percent")
                            || jsonObject.getString(POST_FIELD).equalsIgnoreCase("meta_title")
                            || jsonObject.getString(POST_FIELD).equalsIgnoreCase("meta_keyword")
                            || jsonObject.getString(POST_FIELD).equalsIgnoreCase("meta_description")
                            || jsonObject.getString(POST_FIELD).equalsIgnoreCase("m_seo_canonical")
                            || jsonObject.getString(POST_FIELD).equalsIgnoreCase("description")
                            || jsonObject.getString(POST_FIELD).equalsIgnoreCase("delivery_time")
                            || jsonObject.getString(POST_FIELD).equalsIgnoreCase("special_price")
                    ){
                        continue;
                    }
                    if (jsonObject.has(KEY_is_required) && jsonObject.getString(KEY_is_required).equals("1")) {
                        requiredFields.put(jsonObject.getString(POST_FIELD), jsonObject.getString(FIELD_NAME));
                    }

                    if (jsonObject.getString("frontend_input").equalsIgnoreCase("text")) {

                        view = View.inflate(ProductCreationNew.this, R.layout.dny_edittext_lyt, null);
                        MultiVendor_label = view.findViewById(R.id.MultiVendor_label);
                        MultiVendor_textattribuite = view.findViewById(R.id.MultiVendor_textattribuite);
                        if(jsonObject.getString(POST_FIELD).equalsIgnoreCase("sku")){
                            MultiVendor_textattribuite.setEnabled(false);
                            // Date date = new Date();
                            // MultiVendor_textattribuite.setText(session.getVendorid()+"_"+date.getTime());
                        }else if(jsonObject.getString(POST_FIELD).equalsIgnoreCase("url_key")){
                            MultiVendor_textattribuite.setEnabled(false);
                        }
                        if (jsonObject.getString("input_type").equalsIgnoreCase("text")) {
                            MultiVendor_textattribuite.setInputType(InputType.TYPE_CLASS_TEXT);
                        }
                        if (jsonObject.getString("input_type").equalsIgnoreCase("int")) {
                            MultiVendor_textattribuite.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_SIGNED);
                        }
                        if (jsonObject.getString("input_type").equalsIgnoreCase("decimal")) {
                            MultiVendor_textattribuite.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
                            MultiVendor_textattribuite.setFilters(new InputFilter[] {new DecimalDigitsInputFilter(20,2)});
                        }
                        if (jsonObject.getString("is_required").equalsIgnoreCase("1")) {
                            MultiVendor_label.setText(jsonObject.getString("name") + "*");
                        } else {
                            MultiVendor_label.setText(jsonObject.getString("name"));
                        }

                        if (jsonObject.getString(POST_FIELD).equals("sku")) {
                            Log.e("frozen", "sku_position=1=" + parent_view.getChildCount());
                            sku_position = parent_view.getChildCount();
                        }
                        if (jsonObject.getString(POST_FIELD).equals("url_key")) {
                            Log.e("frozen", "urlKey_position=1=" + parent_view.getChildCount());
                        }

                        /*Get Field value*/
                        parent_view.addView(view);
                        final EditText finalMultiVendor_textattribuite = MultiVendor_textattribuite;
                        finalMultiVendor_textattribuite.setTag(jsonObject.getString(POST_FIELD));

                        if (jsonObject.getString(POST_FIELD).equals("weight")) {
                            weightField=finalMultiVendor_textattribuite;
                        }
                        else if (jsonObject.getString(POST_FIELD).equals("price")) {
                            priceField=finalMultiVendor_textattribuite;
                        }

                        if (jsonObject.getString(POST_FIELD).equals("qty")&&type.equals("grouped")) {
                            MultiVendor_textattribuite.setEnabled(false);
                            if (requiredFields.has(jsonObject.getString(POST_FIELD))) {
                                requiredFields.remove(jsonObject.getString(POST_FIELD));
                            }
                        }

                        if (type.equals("configurable")) {
                            if (jsonObject.getString(POST_FIELD).equals("qty")||jsonObject.getString(POST_FIELD).equals("price")) {
                                MultiVendor_textattribuite.setText("0");
                                MultiVendor_textattribuite.setEnabled(false);
                                if (requiredFields.has(jsonObject.getString(POST_FIELD))) {
                                    requiredFields.remove(jsonObject.getString(POST_FIELD));
                                }
                            }
                        }

                        MultiVendor_textattribuite.addTextChangedListener(new TextWatcher() {
                            @Override
                            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                            }

                            @Override
                            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                                if (charSequence.length() > 0) {
                                    try {
                                        post_param.put(jsonObject.getString(POST_FIELD), finalMultiVendor_textattribuite.getText().toString());
                                        if (finalMultiVendor_textattribuite.getTag().toString().equals("name")) {
                                            sku_txt = finalMultiVendor_textattribuite.getText().toString();
                                        }
                                        if (requiredFields.has(jsonObject.getString(POST_FIELD))) {
                                            requiredFields.remove(jsonObject.getString(POST_FIELD));
                                        }
                                    }
                                    catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }

                            @Override
                            public void afterTextChanged(Editable editable) {

                            }

                        });

                        MultiVendor_textattribuite.setOnFocusChangeListener((v, hasFocus) -> {
                            if (finalMultiVendor_textattribuite.getTag().toString().equals("name")) {
                                LinearLayout sku_layout = (LinearLayout) parent_view.getChildAt(sku_position);
                                EditText sku = (EditText) sku_layout.getChildAt(4);
                                sku.setText(sku_txt);
                                Log.e("frozen", "sku=" + sku_txt);
//                                LinearLayout urlkey_layout = (LinearLayout) parent_view.getChildAt(urlKey_position);
//                                EditText urlKey = (EditText) urlkey_layout.getChildAt(4);
//                                urlKey.setText(url_key_txt);
//                                Log.e("frozen", "url_key=" + url_key_txt);
                            }
                        });
                    }

                    if (jsonObject.getString("frontend_input").equalsIgnoreCase("select")) {
                        view = View.inflate(ProductCreationNew.this, R.layout.dny_spn_layout, null);
                        MultiVendor_label = view.findViewById(R.id.label_txt);
                        spn_delivery = view.findViewById(R.id.spn_delivery);
                        MultiVendor_label.setText(jsonObject.getString("name"));
                        JSONArray options_array = jsonObject.getJSONArray("options");
                        if (jsonObject.getString("is_required").equalsIgnoreCase("1")) {
                            MultiVendor_label.setText(jsonObject.getString("name") + "*");
                        }
                        else {
                            MultiVendor_label.setText(jsonObject.getString("name"));
                        }
                        arrayList = new ArrayList<>();
                        selected_values = new HashMap<>();
                        for (int l = 0; l < options_array.length(); l++) {
                            JSONObject options_obj = options_array.getJSONObject(l);
                            arrayList.add(options_obj.getString("label"));
                            selected_values.put(options_obj.getString("label"), options_obj.getString("value"));
                            spn_delivery.setTag(options_obj.getString("value"));
                            if (options_obj.getString("value").equals("0")){
                                noweightPosition=l;
                            }
                        }
                        spn_delivery.setAdapter(new ArrayAdapter<>(ProductCreationNew.this, R.layout.simple_list_item_1, arrayList));
                        final Spinner finalSpn_delivery = spn_delivery;
                        final HashMap<String, String> finalSelected_values = selected_values;
                        spn_delivery.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                try {
                                    if (weightField!=null&&jsonObject.getString(POST_FIELD).equals("product_has_weight")){
                                        if (finalSelected_values.get(finalSpn_delivery.getSelectedItem().toString()).equals("1")){
                                            weightField.setEnabled(true);
                                        }
                                        else {
                                            weightField.setEnabled(false);
                                        }
                                    }

                                    post_param.put(jsonObject.getString(POST_FIELD), finalSelected_values.get(finalSpn_delivery.getSelectedItem().toString()));
                                    try {
                                        if (requiredFields.has(jsonObject.getString(POST_FIELD))) {
                                            requiredFields.remove(jsonObject.getString(POST_FIELD));
                                        }
                                    }
                                    catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                                catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> adapterView) {

                            }
                        });

                        if (jsonObject.getString(POST_FIELD).equals("product_has_weight")) {
                            spn_delivery.setSelection(noweightPosition);
                        }
                        else {
                            spn_delivery.setSelection(0);
                        }

                        parent_view.addView(view);
                    }
                    if (jsonObject.getString("frontend_input").equalsIgnoreCase("textarea")) {
                        view = View.inflate(ProductCreationNew.this, R.layout.dny_textaria_lyt, null);
                        MultiVendor_label = view.findViewById(R.id.MultiVendor_label);
                        MultiVendor_label.setText(jsonObject.getString("name"));
                        MultiVendor_textattribuite = view.findViewById(R.id.MultiVendor_textattribuite);

                        if (jsonObject.getString("is_required").equalsIgnoreCase("1")) {
                            MultiVendor_label.setText(jsonObject.getString("name") + "*");
                        }
                        else {
                            MultiVendor_label.setText(jsonObject.getString("name"));
                        }

                        final EditText finalMultiVendor_textattribuite1 = MultiVendor_textattribuite;
                        MultiVendor_textattribuite.addTextChangedListener(new TextWatcher() {
                            @Override
                            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                            }

                            @Override
                            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                                if (charSequence.length() > 0) {
                                    try {
                                        post_param.put(jsonObject.getString(POST_FIELD), finalMultiVendor_textattribuite1.getText().toString());
                                        if (requiredFields.has(jsonObject.getString(POST_FIELD))) {
                                            requiredFields.remove(jsonObject.getString(POST_FIELD));
                                        }
                                    }
                                    catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }

                            @Override
                            public void afterTextChanged(Editable editable) {
                            }

                        });
                        parent_view.addView(view);
                    }

                    if (jsonObject.getString("frontend_input").equalsIgnoreCase("boolean")) {
                        view = View.inflate(ProductCreationNew.this, R.layout.dny_boolean_lyt, null);
                        switch_but = view.findViewById(R.id.switch_but);
                        MultiVendor_label = view.findViewById(R.id.MultiVendor_label);

                        if (jsonObject.getString("is_required").equalsIgnoreCase("1")) {
                            MultiVendor_label.setText(jsonObject.getString("name") + "*");
                        }
                        else {
                            MultiVendor_label.setText(jsonObject.getString("name"));
                        }

                        if (requiredFields.has(jsonObject.getString(POST_FIELD))) {
                            requiredFields.remove(jsonObject.getString(POST_FIELD));
                            post_param.put(jsonObject.getString(POST_FIELD), "0");
                        }

                        if (jsonObject.getString(POST_FIELD).equals("price_type")){
                            post_param.put(jsonObject.getString(POST_FIELD), "1");
                            post_param.put("price", "0");
                        }
                        else if (jsonObject.getString(POST_FIELD).equals("weight_type")){
                            post_param.put(jsonObject.getString(POST_FIELD), "1");
                            post_param.put("weight", "0");
                        }
                        else if (jsonObject.getString(POST_FIELD).equals("sku_type")){
                            post_param.put(jsonObject.getString(POST_FIELD), "1");
                        }

                        switch_but.setOnCheckedChangeListener((compoundButton, b) -> {
                            try {
                                if (b) {
                                    post_param.put(jsonObject.getString(POST_FIELD), "1");

                                    if (jsonObject.getString(POST_FIELD).equals("price_type")){
                                        post_param.put(jsonObject.getString(POST_FIELD), "0");
                                        post_param.put("price", "0");
                                        if (priceField!=null){
                                            priceField.setEnabled(false);
                                            if (requiredFields.has("price")){
                                                priceName=requiredFields.getString("price");
                                                requiredFields.remove("price");

                                            }
                                        }
                                    }
                                    else if (jsonObject.getString(POST_FIELD).equals("weight_type")){
                                        post_param.put(jsonObject.getString(POST_FIELD), "0");
                                        post_param.put("weight", "0");
                                        if (weightField!=null){
                                            weightField.setEnabled(false);
                                            if (requiredFields.has("weight")){
                                                weightName=requiredFields.getString("weight");
                                                requiredFields.remove("weight");
                                            }
                                        }
                                    }
                                    else if (jsonObject.getString(POST_FIELD).equals("sku_type")){
                                        post_param.put(jsonObject.getString(POST_FIELD), "0");
//                                        post_param.put("sku", "");
                                    }

                                }
                                else {
                                    post_param.put(jsonObject.getString(POST_FIELD), "0");

                                    if (jsonObject.getString(POST_FIELD).equals("price_type")){
                                        post_param.put(jsonObject.getString(POST_FIELD), "1");
                                        if (priceField!=null){
                                            priceField.setEnabled(true);
                                            if (!priceName.equals("")){
                                                requiredFields.put("price",priceName);
                                            }
                                        }
                                    }
                                    else if (jsonObject.getString(POST_FIELD).equals("weight_type")){
                                        post_param.put(jsonObject.getString(POST_FIELD), "1");
                                        if (weightField!=null){
                                            weightField.setEnabled(true);
                                            if (!weightName.equals("")){
                                                requiredFields.put("weight",weightName);
                                            }
                                        }
                                    }
                                    else if (jsonObject.getString(POST_FIELD).equals("sku_type")){
                                        post_param.put(jsonObject.getString(POST_FIELD), "1");
                                    }


                                }
                            }
                            catch (JSONException e) {
                                e.printStackTrace();
                            }
                        });
                        MultiVendor_label.setText(jsonObject.getString("name"));
                        parent_view.addView(view);
                        switch_but.setChecked(false);
                    }

                    if (jsonObject.getString("frontend_input").equalsIgnoreCase("multiselect")) {
                        view = View.inflate(ProductCreationNew.this, R.layout.dny_checkbox_lyt, null);
                        MultiVendor_label = view.findViewById(R.id.MultiVendor_label);
                        parent_lin_lyt = view.findViewById(R.id.parent_lin_lyt);
                        MultiVendor_label.setText(jsonObject.getString("name"));

                        if (jsonObject.getString("is_required").equalsIgnoreCase("1")) {
                            MultiVendor_label.setText(jsonObject.getString("name") + "*");
                        }
                        else {
                            MultiVendor_label.setText(jsonObject.getString("name"));
                        }
                        JSONArray options_array = jsonObject.getJSONArray("options");
                        for (int l = 0; l < options_array.length(); l++) {
                            JSONObject chk_obj = options_array.getJSONObject(l);
                            brand_chk = new CheckBox(ProductCreationNew.this);
                            brand_chk.setButtonTintList(getResources().getColorStateList(R.color.AppTheme));
                            brand_chk.setText(chk_obj.getString("label"));
                            brand_chk.setTag(chk_obj.getString("value"));
                            parent_lin_lyt.addView(brand_chk);
                            final CheckBox finalBrand_chk = brand_chk;
                            brand_chk.setOnCheckedChangeListener((compoundButton, b) -> {
                                try {
                                    if(post_param.containsKey(jsonObject.getString(POST_FIELD))){
                                        String jsonString = post_param.get(jsonObject.getString(POST_FIELD));
                                        JSONArray selectedOptions = new JSONArray(jsonString);
                                        boolean exists = false;
                                        for (int i = 0; i < selectedOptions.length(); i++) {
                                            if (selectedOptions.getString(i).equals(finalBrand_chk.getTag().toString())) {
                                                exists = true;
                                                break;
                                            }
                                        }
                                        if (!exists) {
                                            selectedOptions.put(finalBrand_chk.getTag().toString());
                                            post_param.put(jsonObject.getString(POST_FIELD),selectedOptions.toString());
                                        }else{
                                            JSONArray jsonArray = new JSONArray();
                                            for (int i = 0; i < selectedOptions.length(); i++) {
                                                if (!selectedOptions.getString(i).equals(finalBrand_chk.getTag().toString())) {
                                                   jsonArray.put(selectedOptions.getString(i));
                                                }
                                            }
                                            post_param.put(jsonObject.getString(POST_FIELD),jsonArray.toString());
                                        }
                                    }else{
                                        JSONArray jsonArray = new JSONArray();
                                        jsonArray.put(finalBrand_chk.getTag().toString());
                                        post_param.put(jsonObject.getString(POST_FIELD),jsonArray.toString());
                                    }
                                }catch (Exception e){}
                                if (b) {
                                    multiselect_data.put(finalBrand_chk.getTag().toString());
                                    try {
                                        if (requiredFields.has(jsonObject.getString(POST_FIELD))) {
                                            requiredFields.remove(jsonObject.getString(POST_FIELD));
                                        }
                                    }
                                    catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                        }
                        parent_view.addView(view);
                    }

                    if (jsonObject.getString("frontend_input").equalsIgnoreCase("date")) {
                        view = View.inflate(ProductCreationNew.this, R.layout.dny_textview_lyt, null);
                        date_text = view.findViewById(R.id.date_text);
                        MultiVendor_label = view.findViewById(R.id.MultiVendor_label);
                        MultiVendor_label.setText(jsonObject.getString("name"));

                        if (jsonObject.getString("is_required").equalsIgnoreCase("1")) {
                            MultiVendor_label.setText(jsonObject.getString("name") + "*");
                        } else {
                            MultiVendor_label.setText(jsonObject.getString("name"));
                        }
                        date_text.setOnClickListener(view1 -> {
                            try {
                                showDatePicker(view1, jsonObject.getString(POST_FIELD));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        });
                        parent_view.addView(view);
                    }
                }
        }catch (Exception e){}
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
                category_view = View.inflate(ProductCreationNew.this, R.layout.ced_multivendor_maincategory, null);
                subcategories = category_view.findViewById(R.id.subcategories);
                main_cat = category_view.findViewById(R.id.main_cat);
                cat_icon = category_view.findViewById(R.id.cat_icon);
                parts = current_object.getString("main_category").split("#");
                maincatname = parts[1];
                maincatid = parts[0];
                if (current_object.has("sub_categories")) {
                    Log.e(TAG, "current_object has sub_categories");
                    subcategories.setText(current_object.getJSONArray("sub_categories").toString());
                    cat_icon.setVisibility(View.VISIBLE);
                } else {
                    Log.e(TAG, "current_object does not has sub_categories");
                    cat_icon.setVisibility(View.GONE);
                }
                main_cat.setText(maincatname);
                main_cat.setId(Integer.parseInt(maincatid));
                final CheckBox finalMain_cat = main_cat;
                main_cat.setOnCheckedChangeListener((buttonView, isChecked) -> {
                    if (isChecked) {
                        if(selected_category_ids.size() >= 3){
                            finalMain_cat.setChecked(false);
                            Toast.makeText(this, R.string.you_cannot_select_categories_more_than_3, Toast.LENGTH_SHORT).show();
                        }else {
                            selected_category_ids.add(String.valueOf(finalMain_cat.getId()));
                        }
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
                    Log.e("REpo", "cat_icon.setOnClickListener1");
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

    private void showDatePicker(final View view_txt, final String attribute) {
        final Calendar myCalendar = Calendar.getInstance();
        final DatePickerDialog datePickerDialog = new DatePickerDialog(this, (view, year, monthOfYear, dayOfMonth) -> {
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            if (view_txt instanceof TextView) {
                ((TextView) view_txt).setText(sdf.format(myCalendar.getTime()));
                post_param.put(attribute, sdf.format(myCalendar.getTime()));
                if (requiredFields.has(attribute)) {
                    requiredFields.remove(attribute);
                }
            }
        }, Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DAY_OF_MONTH));

        datePickerDialog.show();
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.MultiVendor_save){
            //                AppConstant.lockButton(view);
            MultiVendor_save.setEnabled(false);
            savecontinue.setEnabled(false);
            if (requiredFields.length() > 0) {
                JSONArray required_values = requiredFields.names();
                ArrayList<String> required_field_values = new ArrayList<>();
                for (int i = 0; i < required_values.length(); i++) {
                    try {
                        String values = requiredFields.getString(required_values.get(i).toString());
                        required_field_values.add(values);
                    }
                    catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                Toast.makeText(this, getString(R.string.please_check_these_fields_are_empty) + required_field_values, Toast.LENGTH_SHORT).show();
                MultiVendor_save.setEnabled(true);
                savecontinue.setEnabled(true);
            }
            else {
                createProduct(0);
            }
        }else if(view.getId() == R.id.MultiVendor_savecontinue){
//                AppConstant.lockButton(view);
            MultiVendor_save.setEnabled(false);
            savecontinue.setEnabled(false);
            if (requiredFields.length() > 0) {
                JSONArray required_values = requiredFields.names();
                ArrayList<String> required_field_values = new ArrayList<>();
                for (int i = 0; i < required_values.length(); i++) {
                    try {
                        String values = requiredFields.getString(required_values.get(i).toString());
                        required_field_values.add(values);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                Toast.makeText(this, getString(R.string.please_check_these_fields_are_empty) + required_field_values, Toast.LENGTH_SHORT).show();
                MultiVendor_save.setEnabled(true);
                savecontinue.setEnabled(true);

            }
            else {
                createProduct(0);
            }
        }else if(view.getId() == R.id.MultiVendor_addimagees){
            imageAddSection();
        }
    }

    private void imageAddSection() {
        View layout = View.inflate(ProductCreationNew.this, R.layout.create_product_add_multiple_images, null);
        final ConstraintLayout add_multiple_images_layout = layout.findViewById(R.id.multiple_images_layout);

        AppCompatButton click_product_image = layout.findViewById(R.id.MultiVendor_clickproductimage);
        click_product_image.setVisibility(View.VISIBLE);
        Button browse_product_image = layout.findViewById(R.id.MultiVendor_browseproductimage);
        final ImageView remove_image = layout.findViewById(R.id.remove_image);
        final CheckBox default_checkbox = layout.findViewById(R.id.default_checkbox);
        remove_image.setTag(String.valueOf(count));
        Log.e("REpo", "image_id= " + remove_image.getTag().toString());
        imageselection.addView(add_multiple_images_layout);
        count++;

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
            click_product_image.setOnClickListener(view -> Dexter.withActivity(ProductCreationNew.this)
                    .withPermissions(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE).
                    withListener(new MultiplePermissionsListener() {
                        @Override
                        public void onPermissionsChecked(MultiplePermissionsReport report) {
                            if (report.areAllPermissionsGranted()) {
                                selectImage_position = Integer.parseInt(remove_image.getTag().toString());
                                product_image = layout.findViewById(R.id.MultiVendor_productimage);
                                image_id = remove_image.getTag().toString();
                                capture_image();
                            }
                        }
                        @Override
                        public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                            token.continuePermissionRequest();
                        }
                    }).onSameThread().check());
        }else {
//            Dexter.withContext(this)
//                    .withPermissions(
//                            CAMERA,
//                            READ_MEDIA_IMAGES,
//                            READ_MEDIA_VIDEO,
//                            READ_MEDIA_AUDIO
//                    ).withListener(new MultiplePermissionsListener() {
//                        @Override public void onPermissionsChecked(MultiplePermissionsReport report) {/* ... */}
//
//                        @Override public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {/*
//        ... */}
//                    }).check();
            click_product_image.setOnClickListener(view -> Dexter.withActivity(ProductCreationNew.this)
                    .withPermissions(CAMERA,READ_MEDIA_IMAGES).
                    withListener(new MultiplePermissionsListener() {
                        @Override
                        public void onPermissionsChecked(MultiplePermissionsReport report) {
                            if (report.areAllPermissionsGranted()) {
                                selectImage_position = Integer.parseInt(remove_image.getTag().toString());
                                product_image = layout.findViewById(R.id.MultiVendor_productimage);
                                image_id = remove_image.getTag().toString();
                                capture_image();
                            }
                        }
                        @Override
                        public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                            token.continuePermissionRequest();
                        }
                    }).onSameThread().check());
        }



        remove_image.setOnClickListener(view -> {
            ConstraintLayout parent = (ConstraintLayout) remove_image.getParent();
            Log.e("REpo", "image_id= " + remove_image.getTag().toString());
            try {
                if (default_checkbox.isChecked()) {
                    is_default_check = false;
                }
                for (int i = 0; i < multiple_images_array.length(); i++) {
                    JSONObject current_object = multiple_images_array.getJSONObject(i);
                    if (current_object.getString("image_id").equals(remove_image.getTag().toString())) {
                        multiple_images_array.remove(i);
                        if (post_param.containsKey("images")) {
                            if (multiple_images_array.length()>0) {
                                post_param.put("images", multiple_images_array.toString());
                            }
                            else {
                                post_param.remove("images");
                            }
                        }
                    }
                }
            }
            catch (JSONException e) {
                e.printStackTrace();
            }
            imageselection.removeView(parent);
        });
        default_checkbox.setOnCheckedChangeListener((compoundButton, ischecked) -> {
            if (ischecked) {
                if (is_default_check) {
                    Toast.makeText(ProductCreationNew.this, R.string.onlyoneimagecanbeselected, Toast.LENGTH_SHORT).show();
                    default_checkbox.setChecked(false);
                }
                else {
                    try {
                        for (int i = 0; i < multiple_images_array.length(); i++) {
                            JSONObject current_object = multiple_images_array.getJSONObject(i);
                            if (current_object.getString("image_id").equals(remove_image.getTag().toString())) {
                                current_object.put("is_default", "true");
                            }
                            else {
                                if (current_object.has("is_default")) {
                                    current_object.remove("is_default");
                                }
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    Log.i("REpo", "imagechecked=" + (remove_image.getTag().toString()));
                    is_default_check = true;
                }
            } else {
                is_default_check = false;
            }
        });
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
            browse_product_image.setOnClickListener(view -> Dexter.withActivity(ProductCreationNew.this)
                    .withPermissions(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE).
                    withListener(new MultiplePermissionsListener() {
                        @Override
                        public void onPermissionsChecked(MultiplePermissionsReport report) {
                            if (report.areAllPermissionsGranted()) {
                                selectImage_position = Integer.parseInt(remove_image.getTag().toString());
                                product_image = layout.findViewById(R.id.MultiVendor_productimage);
                                image_id = remove_image.getTag().toString();
                                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                                photoPickerIntent.setType("image/*");
                                startActivityForResult(photoPickerIntent, galerry_code);
                            }
                        }

                        @Override
                        public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                            token.continuePermissionRequest();
                        }
                    }).onSameThread().check());
        }else{
            browse_product_image.setOnClickListener(view -> Dexter.withActivity(ProductCreationNew.this)
                    .withPermissions(READ_MEDIA_IMAGES).
                    withListener(new MultiplePermissionsListener() {
                        @Override
                        public void onPermissionsChecked(MultiplePermissionsReport report) {
                            if (report.areAllPermissionsGranted()) {
                                selectImage_position = Integer.parseInt(remove_image.getTag().toString());
                                product_image = layout.findViewById(R.id.MultiVendor_productimage);
                                image_id = remove_image.getTag().toString();
                                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                                photoPickerIntent.setType("image/*");
                                startActivityForResult(photoPickerIntent, galerry_code);
                            }
                        }

                        @Override
                        public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                            token.continuePermissionRequest();
                        }
                    }).onSameThread().check());
        }

    }

    private void createProduct(final int clickType) {
        updateWebsite();
        isProductApiCall = false;
        totalImageApiCall = 0;
        post_param.put("product_brand", multiselect_data.toString());
        if(type.equalsIgnoreCase("configurable") && ConfigAttributeSelectionActivity.attributeModelListFinal.size() > 0 && associatedProductIdsArr.size() <= 0){
            String mainProductName = "";
            LinkedHashMap<String, String> config_param = new LinkedHashMap<>();
            for (int index=0;index<ConfigAttributeSelectionActivity.attributeModelListFinal.size();index++){
                for (LinkedHashMap.Entry<String, String> entry : post_param.entrySet()) {
                    String key = entry.getKey();
                    String value = entry.getValue();
                    if (!key.equalsIgnoreCase("sku") &&
                            !key.equalsIgnoreCase("qty") &&
                            !key.equalsIgnoreCase("name") &&
                            !key.equalsIgnoreCase("status") &&
                            !key.equalsIgnoreCase("weight") &&
                            !key.equalsIgnoreCase("vendor_price") &&
                            !ConfigAttributeSelectionActivity.attributeModelListFinal.get(index).getAttributeWithValue().contains(key)){
                        config_param.put("product[" + index + "][" + key + "]", value);
                    }else if (key.equalsIgnoreCase("name")){
                        mainProductName = value;
                    }
                }
                config_param.put("product[" + index + "][sku]", ConfigAttributeSelectionActivity.attributeModelListFinal.get(index).getSkulabel() != null ? ConfigAttributeSelectionActivity.attributeModelListFinal.get(index).getSkulabel() : "");
                config_param.put("product[" + index + "][qty]", ConfigAttributeSelectionActivity.attributeModelListFinal.get(index).getQuantity() != null ? ConfigAttributeSelectionActivity.attributeModelListFinal.get(index).getQuantity() : "");
                config_param.put("product[" + index + "][name]", mainProductName + "-" + (ConfigAttributeSelectionActivity.attributeModelListFinal.get(index).getNamelabel() != null ? ConfigAttributeSelectionActivity.attributeModelListFinal.get(index).getNamelabel() : ""));
                config_param.put("product[" + index + "][weight]", ConfigAttributeSelectionActivity.attributeModelListFinal.get(index).getWeight() != null ? ConfigAttributeSelectionActivity.attributeModelListFinal.get(index).getWeight() : "");
                config_param.put("product[" + index + "][vendor_price]", ConfigAttributeSelectionActivity.attributeModelListFinal.get(index).getPrice() != null ? ConfigAttributeSelectionActivity.attributeModelListFinal.get(index).getPrice() : "");
                config_param.put("product[" + index + "][status]", ConfigAttributeSelectionActivity.attributeModelListFinal.get(index).isEnable() ? "1" : "0");
                config_param.put("product[" + index + "][type]", "simple");
                config_param.put("product[" + index + "][set]", attribute_set);
// Parse attributeWithValue
                String attributeWithValue = ConfigAttributeSelectionActivity.attributeModelListFinal.get(index).getAttributeWithValue();
                if (attributeWithValue != null && !attributeWithValue.isEmpty()) {
                    String[] arr = attributeWithValue.split("::!");
                    for (String attribute : arr) {
                        String[] subArr = attribute.split(":;");
                        if (subArr.length > 1) {
                            config_param.put("product[" + index + "][" + subArr[0] + "]", subArr[1]);
                        }
                    }
                }
            }
            config_param.put("vendor_id", session.getVendorid());
            config_param.put("hashkey", session.getHahkey());
            Ced_MultiVendor_ClientRequestResponse crr = new Ced_MultiVendor_ClientRequestResponse(output -> {
                JSONObject jsonObject = new JSONObject(output.toString()).getJSONObject("data");
                if (jsonObject.getBoolean("success")) {
                    Toast.makeText(ProductCreationNew.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                    JSONArray associatedProductIds = jsonObject.optJSONArray("associated_product_ids");

                    if (associatedProductIds != null) {
                        for (int index = 0; index < associatedProductIds.length(); index++) {
                            String productId = associatedProductIds.optString(index, "");
                            associatedProductIdsArr.add(productId);
                            if (index < ConfigAttributeSelectionActivity.attributeModelListFinal.size()) {
                                AttributeModelConfig model = ConfigAttributeSelectionActivity.attributeModelListFinal.get(index);
                                model.setAssicoiatProductId(productId);
                                JSONObject products = jsonObject.getJSONObject("products");
                                JSONObject product = products.optJSONObject(productId);
                                if (product != null) {
                                    model.setAssicoiatProductSku(product.optString("sku", ""));
                                }
                            }
                        }
                    }
                    LinkedHashMap<String, Object> configurableAttributesData = new LinkedHashMap<>();
                    for (int index = 0; index < ConfigAttributeSelectionActivity.attributeModelListSelected.size(); index++) {
                        configurableAttributesData.put(ConfigAttributeSelectionActivity.attributeModelListSelected.get(index).getAttributeId(), index);
                    }
                    // Step 2: Create JSON Object for full payload
                    JSONObject configjsonObject = new JSONObject();
                    configjsonObject.put("configurable_attributes_data", new JSONObject(configurableAttributesData));
                    // Create associated_product_ids JSONArray
                    JSONArray associatedProductIdsJson = new JSONArray();
                    for (String id : associatedProductIdsArr) {
                        associatedProductIdsJson.put(id);
                    }
                    configjsonObject.put("associated_product_ids", associatedProductIdsJson);
                    // Step 3: Convert to JSON string and append to params
                    String configAttribute = configjsonObject.toString(4); // Pretty print (indent 4 spaces)
                    post_param.put("config_attribute",configAttribute);
                    for(AttributeModelConfig config:ConfigAttributeSelectionActivity.attributeModelListFinal){
                        if(config.getImage() != null){
                            totalImageApiCall += 1;
                            uploadSinleProductImage(config,clickType);
                        }
                    }
                    if(totalImageApiCall <= 0){
                        callcreateProductApi(clickType);
                    }
                }
                else {
                    Toast.makeText(ProductCreationNew.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                    MultiVendor_save.setEnabled(true);
                    savecontinue.setEnabled(true);
                }
            }, ProductCreationNew.this, "POST", config_param);
            crr.execute(session.getBase_Url() +"vproductapi/vproducts/associatedproduct");
        }else{
            callcreateProductApi(clickType);
        }

        Log.d("test_params", "" + post_param);

    }

    private void callcreateProductApi(final int clickType){
        if(!isProductApiCall){
            isProductApiCall = true;
        }else{
            return;
        }
        post_param.put("vendor_id", session.getVendorid());
        post_param.put("hashkey", session.getHahkey());
        post_param.put("type", type);
        post_param.put("set", attribute_set);
        post_param.put("create_prod", "true");
        Ced_MultiVendor_ClientRequestResponse crr = new Ced_MultiVendor_ClientRequestResponse(output -> {
            JSONObject jsonObject = new JSONObject(output.toString()).getJSONObject("data");
            if (jsonObject.getBoolean("success")) {
                Toast.makeText(ProductCreationNew.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();


                if (multiple_images_array.length() > 0) {
                    String uploadImageUrl=session.getBase_Url() +"rest/V1/vproducts/media";
                    JSONObject uploadImage=new JSONObject();
                    uploadImage.put("sku",jsonObject.getString("sku"));
                    for (int i=0;i<multiple_images_array.length();i++){
                        multiple_images_array.getJSONObject(i).remove("image_id");
                    }
                    uploadImage.put("images", multiple_images_array);
                    Ced_ClientRequestResponseRest ced_clientRequestResponseRest=new Ced_ClientRequestResponseRest(output1 -> {
                        JSONObject jsonObject1=new JSONObject(output1.toString());
                        if (jsonObject1.has("success")&&jsonObject1.getBoolean("success")){
                            if (jsonObject1.has("message")) {
                                Toast.makeText(ProductCreationNew.this, jsonObject1.getString("message"), Toast.LENGTH_SHORT).show();
                            }
                            updateProduct(jsonObject.getString("product_id"), clickType);
                        }
                        else{
                            Toast.makeText(ProductCreationNew.this, jsonObject1.getString("message"), Toast.LENGTH_SHORT).show();
                        }
                    },ProductCreationNew.this,"POST",uploadImage.toString());
                    ced_clientRequestResponseRest.execute(uploadImageUrl);
                }
                else {
                    updateProduct(jsonObject.getString("product_id"), clickType);
                }

               /* if (isCreatingNewPro) {
                    Intent intent1 = new Intent(ProductCreationNew.this, Ced_MultiVendor_ManageProducts.class);
                    intent1.putExtra("Navigation", "productcreate");
                    intent1.putExtra("selectedwebsite", selected_websitetopost);
                    intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent1);
                    overridePendingTransition(R.anim.ced_multivendor_slide_in, R.anim.ced_multivendor_slide_out);
                    finish();
                }
                else {*/
//                    updateProduct(jsonObject.getString("product_id"), clickType);
//                }
            }
            else {
                Toast.makeText(ProductCreationNew.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                MultiVendor_save.setEnabled(true);
                savecontinue.setEnabled(true);
            }
        }, ProductCreationNew.this, "POST", post_param);

        crr.execute(CurrentUrl);
    }

    private void uploadSinleProductImage(AttributeModelConfig attributeModelConfig,final int clickType){
        try {
            String uploadImageUrl=session.getBase_Url() +"rest/V1/vproducts/media";
            JSONObject uploadImage=new JSONObject();
            uploadImage.put("sku",attributeModelConfig.getAssicoiatProductSku());

            JSONObject selected_images = new JSONObject();
            selected_images.put("media_type", "image");
            selected_images.put("position", 200+1);
            selected_images.put("label", "true");

            JSONObject imagedata=new JSONObject();
            imagedata.put("type", "image/png");
            imagedata.put("name", "abc" + new Date().getTime() + 1 + ".png");
            imagedata.put("base64_encoded_data", encodeProductImage(attributeModelConfig.getImage()));
            selected_images.put("content", imagedata);
            selected_images.put("is_default", "true");
            JSONArray images = new JSONArray();
            images.put(selected_images);
            uploadImage.put("images", images);
            Ced_ClientRequestResponseRest ced_clientRequestResponseRest=new Ced_ClientRequestResponseRest(output1 -> {
                JSONObject jsonObject1=new JSONObject(output1.toString());
                if (jsonObject1.has("success")&&jsonObject1.getBoolean("success")){
                    totalImageApiCall -= 1;
                    if(totalImageApiCall <= 0){
                        callcreateProductApi(clickType);
                    }
                    if (jsonObject1.has("message")) {
                        Toast.makeText(ProductCreationNew.this, jsonObject1.getString("message"), Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    Toast.makeText(ProductCreationNew.this, jsonObject1.getString("message"), Toast.LENGTH_SHORT).show();
                }
            },ProductCreationNew.this,"POST",uploadImage.toString());
            ced_clientRequestResponseRest.execute(uploadImageUrl);
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    private void updateWebsite() {
        if (selected_category_ids.size() > 0) {
            Iterator categoryiditerator = selected_category_ids.iterator();
            JSONArray object1 = new JSONArray();
            while (categoryiditerator.hasNext()) {
                String name = (String) categoryiditerator.next();
                object1.put(name);
            }
            updateParams.put("category", object1.toString());
            post_param.put("category", object1.toString());
        }
        else {
            updateParams.put("category", " ");
            post_param.put("category", " ");
        }
        JSONArray object1;
        if (idsforwebsites.size() > 0) {
            Iterator idsforwebsite = idsforwebsites.iterator();
            object1 = new JSONArray();
            while (idsforwebsite.hasNext()) {
                String name = (String) idsforwebsite.next();
                object1.put(name);
            }
            updateParams.put("websites", object1.toString());
            post_param.put("websites", object1.toString());
            selected_websitetopost = object1.toString();
        }
        else {
            object1=new JSONArray();
            object1.put("1");
            updateParams.put("websites", object1.toString());
            post_param.put("websites", object1.toString());
            selected_websitetopost =  object1.toString();
        }
    }

    private void updateProduct(final String prod_id, final int click_type) {
        updateParams.put("product_id", prod_id);
        updateParams.put("vendor_id", session.getVendorid());
        updateParams.put("hashkey", session.getHahkey());

        Ced_MultiVendor_ClientRequestResponse crr = new Ced_MultiVendor_ClientRequestResponse(output -> {
            if (functionalityList.getExtensionAddon()) {
                JSONObject object1 = new JSONObject(output.toString());
                String success = object1.getJSONObject("data").getString("success");
                if (success.equals("true")) {
                    if (click_type == 1) {
                        gottoNextPage(prod_id);
                    }
                    else {
                        Toast.makeText(ProductCreationNew.this, object1.getJSONObject("data").getString("message"), Toast.LENGTH_LONG).show();
                        Intent intent1 = new Intent(ProductCreationNew.this, Ced_MultiVendor_ManageProducts.class);
                        intent1.putExtra("Navigation", "productcreate");
                        intent1.putExtra("selectedwebsite", selected_websitetopost);
                        intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent1);
                        overridePendingTransition(R.anim.ced_multivendor_slide_in, R.anim.ced_multivendor_slide_out);
                        finish();
                    }
                }
                else {
                    Toast.makeText(ProductCreationNew.this, object1.getJSONObject("data").getString("message"), Toast.LENGTH_SHORT).show();
                }
            }
            else {
                Intent intent = new Intent(getApplicationContext(), Ced_MultiVendor_UnAuthourizedRequestError.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                overridePendingTransition(R.anim.ced_multivendor_slide_in, R.anim.ced_multivendor_slide_out);
            }

        }, ProductCreationNew.this, "POST", updateParams);
      //  crr.execute(updateurl);
        if (functionalityList.getExtensionAddon()) {
            Intent intent1 = new Intent(ProductCreationNew.this, Ced_MultiVendor_ManageProducts.class);
            intent1.putExtra("Navigation", "productcreate");
            intent1.putExtra("selectedwebsite", selected_websitetopost);
            intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent1);
            overridePendingTransition(R.anim.ced_multivendor_slide_in, R.anim.ced_multivendor_slide_out);
            finish();
        }
        else {
            Intent intent = new Intent(getApplicationContext(), Ced_MultiVendor_UnAuthourizedRequestError.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            overridePendingTransition(R.anim.ced_multivendor_slide_in, R.anim.ced_multivendor_slide_out);
        }
    }

    private void gottoNextPage(String prod_id) {
        if (tabs.has("related")) {
            Intent related = new Intent(ProductCreationNew.this, Ced_MultiVendor_Related.class);
            related.putExtra("tabs", tabs.toString());
            related.putExtra("product_id", prod_id);
            related.putExtra("type", type);
            related.putExtra("attribute_set", attribute_set);
            related.putExtra("selectedwebsite", selected_websitetopost);
            startActivity(related);
            overridePendingTransition(R.anim.ced_multivendor_slide_in_sequence, R.anim.ced_multivendor_slide_out_sequence);
            finish();
        }
        else {
            if (tabs.has("upsell")) {
                Intent related = new Intent(ProductCreationNew.this, Ced_MultiVendor_upsell.class);
                related.putExtra("tabs", tabs.toString());
                related.putExtra("product_id", prod_id);
                related.putExtra("type", type);
                related.putExtra("attribute_set", attribute_set);
                related.putExtra("selectedwebsite", selected_websitetopost);
                related.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(related);
                overridePendingTransition(R.anim.ced_multivendor_slide_in_sequence, R.anim.ced_multivendor_slide_out_sequence);
                finish();
            } else {
                if (tabs.has("crosssell")) {
                    Intent related = new Intent(ProductCreationNew.this, Ced_MultiVendor_crosssell.class);
                    related.putExtra("tabs", tabs.toString());
                    related.putExtra("product_id", prod_id);
                    related.putExtra("type", type);
                    related.putExtra("selectedwebsite", selected_websitetopost);
                    related.putExtra("attribute_set", attribute_set);
                    related.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(related);
                    overridePendingTransition(R.anim.ced_multivendor_slide_in_sequence, R.anim.ced_multivendor_slide_out_sequence);
                    finish();
                } else {
                    if (tabs.has("customer_options")) {
                        Intent related = new Intent(ProductCreationNew.this, Ced_MultiVendor_CustomOption.class);
                        related.putExtra("tabs", tabs.toString());
                        related.putExtra("product_id", prod_id);
                        related.putExtra("type", type);
                        related.putExtra("selectedwebsite", selected_websitetopost);
                        related.putExtra("attribute_set", attribute_set);
                        related.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(related);
                        overridePendingTransition(R.anim.ced_multivendor_slide_in_sequence, R.anim.ced_multivendor_slide_out_sequence);
                        finish();
                    } else {
                        if (type.equals("configurable")) {
                            Intent related = new Intent(ProductCreationNew.this, Ced_MultiVendor_Configurable.class);
                            related.putExtra("tabs", tabs.toString());
                            related.putExtra("product_id", prod_id);
                            related.putExtra("selectedwebsite", selected_websitetopost);
                            related.putExtra("type", type);
                            related.putExtra("attribute_set", attribute_set);
                            related.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(related);
                            overridePendingTransition(R.anim.ced_multivendor_slide_in_sequence, R.anim.ced_multivendor_slide_out_sequence);
                            finish();
                        }
                        if (type.equals("downloadable")) {
                            Intent related = new Intent(ProductCreationNew.this, Ced_MultiVendor_Downloadable.class);
                            related.putExtra("tabs", tabs.toString());
                            related.putExtra("product_id", prod_id);
                            related.putExtra("type", type);
                            related.putExtra("selectedwebsite", selected_websitetopost);
                            related.putExtra("attribute_set", attribute_set);
                            related.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(related);
                            overridePendingTransition(R.anim.ced_multivendor_slide_in_sequence, R.anim.ced_multivendor_slide_out_sequence);
                            finish();
                        }
                        if (type.equals("bundle")) {
                            Intent related = new Intent(ProductCreationNew.this, Ced_MultiVendor_BundleItems.class);
                            related.putExtra("tabs", tabs.toString());
                            related.putExtra("product_id", prod_id);
                            related.putExtra("type", type);
                            related.putExtra("selectedwebsite", selected_websitetopost);
                            related.putExtra("attribute_set", attribute_set);
                            related.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(related);
                            overridePendingTransition(R.anim.ced_multivendor_slide_in_sequence, R.anim.ced_multivendor_slide_out_sequence);
                            finish();
                        }
                        if (type.equals("grouped")) {
                            Intent related = new Intent(ProductCreationNew.this, Ced_MultiVendor_GroupItems.class);
                            related.putExtra("tabs", tabs.toString());
                            related.putExtra("product_id", prod_id);
                            related.putExtra("type", type);
                            related.putExtra("selectedwebsite", selected_websitetopost);
                            related.putExtra("attribute_set", attribute_set);
                            related.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(related);
                            overridePendingTransition(R.anim.ced_multivendor_slide_in_sequence, R.anim.ced_multivendor_slide_out_sequence);
                            finish();
                        }
                        if (type.equals("simple")) {
                            Intent related = new Intent(ProductCreationNew.this, Ced_MultiVendor_ManageProducts.class);
                            related.putExtra("Navigation", "productcreate");
                            related.putExtra("selectedwebsite", selected_websitetopost);
                            startActivity(related);
                            overridePendingTransition(R.anim.ced_multivendor_slide_in_sequence, R.anim.ced_multivendor_slide_out_sequence);
                        }
                        if (type.equals("virtual")) {
                            Intent related = new Intent(ProductCreationNew.this, Ced_MultiVendor_ManageProducts.class);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
        try {
            if (resultCode != RESULT_CANCELED) {
                 if (requestCode == ImagePickerManager.REQUEST_CAMERA || requestCode == ImagePickerManager.REQUEST_GALLERY) {
                     ImagePickerManager.handleActivityResult(requestCode, resultCode, imageReturnedIntent, this);
                }else if (requestCode == CAMERA_REQUEST) {
                    try {
                        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
                        Bitmap photo = BitmapFactory.decodeFile(imagePath, bmOptions);
                      //  Toast.makeText(this, "imagePath "+imagePath, Toast.LENGTH_SHORT).show();
                        ContentValues values = new ContentValues();
                        values.put(MediaStore.Images.Media.DISPLAY_NAME, new Date().getTime()); // File name
                        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg"); // MIME type
                        values.put(MediaStore.Images.Media.RELATIVE_PATH, Environment.DIRECTORY_PICTURES); // Directory
                        ContentResolver resolver = getContentResolver();
                        Uri tempUri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

                        if (tempUri != null) {
                            try (OutputStream out = resolver.openOutputStream(tempUri)) {
                                photo.compress(Bitmap.CompressFormat.JPEG, 20, out);
                               // photo.recycle(); // Safe to recycle AFTER compressing
                               // Toast.makeText(this, "Image saved successfully!", Toast.LENGTH_SHORT).show();
                            } catch (IOException e) {
                                e.printStackTrace();
                              //  Toast.makeText(this, "Error saving image!" + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        } else {
                          //  Toast.makeText(this, "Failed to create MediaStore entry!", Toast.LENGTH_SHORT).show();
                        }
                      //  String path = MediaStore.Images.Media.insertImage(getContentResolver(), photo, "Title", null);
                      //  Uri tempUri = Uri.parse(path);
                        Log.e("REpo", "tempUri: " + tempUri.toString());

                        // CALL THIS METHOD TO GET THE ACTUAL PATH
                        String producturi = getRealPathFromURI(ProductCreationNew.this, tempUri);
                        String[] imagename = getRealPathFromURI(ProductCreationNew.this, tempUri).split("/");
                        Log.i("REpo", "filename==" + producturi);
                        Log.i("REpo", "filename==" + imagename[imagename.length - 1]);
                        String productpicname = imagename[imagename.length - 1];
                        Log.i("REpo", "9044_name==" + productpicname);
                        if (calculateFileSize(producturi) > 10.0) {
                            Toast.makeText(this, "Image should be less than 10 MB", Toast.LENGTH_SHORT).show();
                        } else {
                            Log.e("REpo", "FileSize==" + calculateFileSize(producturi));
                            if (getResources().getString(R.string.Enable_Log).equals("yes")) {
                                Log.i("filename", "" + imagename[imagename.length - 1]);
                            }
                            productpicname = imagename[imagename.length - 1];

                            Log.d("REpo", "CreateProductWithType==" + productpicname);
                            Log.d("REpo", "CreateProductWithType==" + productpicname.substring(productpicname.indexOf('.')));

                            final InputStream imageStream = getContentResolver().openInputStream(tempUri);

                            Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);

                            ExifInterface exif = null;
                            try {
                                File pictureFile = new File(imagePath);
                                exif = new ExifInterface(pictureFile.getAbsolutePath());
                            } catch (Exception e) {
                                e.printStackTrace();
                              //  Toast.makeText(this,"imagePath " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                            int orientation = ExifInterface.ORIENTATION_NORMAL;

                            if (exif != null)
                                orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);

                            switch (orientation) {
                                case ExifInterface.ORIENTATION_ROTATE_90:
                                    selectedImage = rotateBitmap(selectedImage, 90);
                                    break;
                                case ExifInterface.ORIENTATION_ROTATE_180:
                                    selectedImage = rotateBitmap(selectedImage, 180);
                                    break;

                                case ExifInterface.ORIENTATION_ROTATE_270:
                                    selectedImage = rotateBitmap(selectedImage, 270);
                                    break;
                            }

                            selectedImage = getResizedBitmap(selectedImage, 300, 300);

                            product_image.setImageBitmap(selectedImage);

                            encodedproductImage = encodeProductImage(selectedImage);
                            try {
                                if (!encodedproductImage.equals("")) {
                             /*   JSONObject selected_images = new JSONObject();
                                selected_images.put("type", "image/png");
                                selected_images.put("name", "abc" + new Date().getTime() + 1 + ".png");
                                selected_images.put("base64_encoded_data", encodedproductImage);
                                selected_images.put("image_id", image_id);
                                multiple_images_array.put(selected_images);*/
                                    for (int i = 0; i < multiple_images_array.length(); i++) {
                                        JSONObject current_object = multiple_images_array.getJSONObject(i);
                                        if (current_object.getString("image_id").equals(image_id)) {
                                            multiple_images_array.remove(i);
                                            if (post_param.containsKey("images")) {
                                                if (multiple_images_array.length()>0) {
                                                    post_param.put("images", multiple_images_array.toString());
                                                }
                                                else {
                                                    post_param.remove("images");
                                                }
                                            }
                                        }
                                    }
                                    JSONObject selected_images = new JSONObject();
                                    selected_images.put("media_type", "image");
                                    selected_images.put("position", 200+selectImage_position);
                                    selected_images.put("label", "true");
                                    selected_images.put("image_id", image_id);

                                    JSONObject imagedata=new JSONObject();
                                    imagedata.put("type", "image/png");
                                    imagedata.put("name", "abc" + new Date().getTime() + 1 + ".png");
                                    imagedata.put("base64_encoded_data", encodedproductImage);
                                    selected_images.put("content", imagedata);
                                    for (int i = 0; i < multiple_images_array.length(); i++) {
                                        JSONObject current_object = multiple_images_array.getJSONObject(i);
                                        if (current_object.getString("image_id").equals(image_id)) {
                                            multiple_images_array.remove(i);
                                        }
                                    }
                                    multiple_images_array.put(selected_images);

                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                               // Toast.makeText(this,"encodedproductImage" + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                            Log.i("checkmultipleimages", "Array-- " + multiple_images_array.toString());
                            Log.i("encodedimage", encodedproductImage);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                       // Toast.makeText(this,"CAMERA_REQUEST " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else if (resultCode == RESULT_OK) {
                    try {
                        final Uri imageUri = imageReturnedIntent.getData();
                        producturi = getRealPathFromURI(ProductCreationNew.this, imageUri);
                        if (calculateFileSize(producturi) > 5.0) {
                            Toast.makeText(this, "Image should be less than 5 MB", Toast.LENGTH_SHORT).show();
                        } else {
                            String[] imagename = getRealPathFromURI(ProductCreationNew.this, imageUri).split("/");
                            if (getResources().getString(R.string.Enable_Log).equals("yes")) {
                                Log.i("filename", "" + imagename[imagename.length - 1]);
                            }
                            productpicname = imagename[imagename.length - 1];

                            Log.d("CreateProductWithType", "" + productpicname);
                            Log.d("CreateProductWithType", "" + productpicname.substring(productpicname.indexOf('.')));

                            final InputStream imageStream = getContentResolver().openInputStream(imageUri);

                            Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                            ExifInterface exif = null;
                            try {
                                File pictureFile = new File(producturi);
                                exif = new ExifInterface(pictureFile.getAbsolutePath());
                            } catch (Exception e) {
                                e.printStackTrace();
                                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                            int orientation = ExifInterface.ORIENTATION_NORMAL;

                            if (exif != null)
                                orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);

                            switch (orientation) {
                                case ExifInterface.ORIENTATION_ROTATE_90:
                                    selectedImage = rotateBitmap(selectedImage, 90);
                                    break;
                                case ExifInterface.ORIENTATION_ROTATE_180:
                                    selectedImage = rotateBitmap(selectedImage, 180);
                                    break;

                                case ExifInterface.ORIENTATION_ROTATE_270:
                                    selectedImage = rotateBitmap(selectedImage, 270);
                                    break;
                            }
                            selectedImage = getResizedBitmap(selectedImage, 300, 300);
                            product_image.setImageBitmap(selectedImage);
                            encodedproductImage = encodeProductImage(selectedImage);
                            try {
                                if (!encodedproductImage.equals("")) {
                              /*  JSONObject selected_images = new JSONObject();
                                selected_images.put("type", "image/png");
                                selected_images.put("name", "abc" + new Date().getTime() + 3 + ".png");
                                selected_images.put("base64_encoded_data", encodedproductImage);
                                selected_images.put("image_id", image_id);
                                multiple_images_array.put(selected_images);*/
                                    for (int i = 0; i < multiple_images_array.length(); i++) {
                                        JSONObject current_object = multiple_images_array.getJSONObject(i);
                                        if (current_object.getString("image_id").equals(image_id)) {
                                            multiple_images_array.remove(i);
                                            if (post_param.containsKey("images")) {
                                                if (multiple_images_array.length()>0) {
                                                    post_param.put("images", multiple_images_array.toString());
                                                }
                                                else {
                                                    post_param.remove("images");
                                                }
                                            }
                                        }
                                    }
                                    JSONObject selected_images = new JSONObject();
                                    selected_images.put("media_type", "image");
                                    selected_images.put("position", 200+selectImage_position); //new Date().getTime() + 1
                                    selected_images.put("label", "true");
                                    selected_images.put("image_id", image_id);

                                    JSONObject imagedata=new JSONObject();
                                    imagedata.put("type", "image/png");
                                    imagedata.put("name", "abc" + new Date().getTime() + 1 + ".png");
                                    imagedata.put("base64_encoded_data", encodedproductImage);
                                    selected_images.put("content", imagedata);
                                    for (int i = 0; i < multiple_images_array.length(); i++) {
                                        JSONObject current_object = multiple_images_array.getJSONObject(i);
                                        if (current_object.getString("image_id").equals(image_id)) {
                                            multiple_images_array.remove(i);
                                        }
                                    }
                                    multiple_images_array.put(selected_images);

                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                            Log.i("checkmultipleimages", "Array-- " + multiple_images_array.toString());
                            Log.i("encodedimage", encodedproductImage);
                        }
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                        Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }catch (Exception e){
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }

//    private String encodeImage(Bitmap bm) {
//        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//        bm.compress(Bitmap.CompressFormat.PNG, 40, baos);
//        byte[] b = baos.toByteArray();
//
//        Bitmap compressedBitmap = BitmapFactory.decodeByteArray(b, 0, b.length);
//        compressedBitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
//        byte[] bytearray = baos.toByteArray();
//        String encImage = Base64.encodeToString(bytearray, Base64.DEFAULT);
//        return encImage;
//
//    }

    private String encodeProductImage(Bitmap bitmap) {
        if (bitmap == null || bitmap.isRecycled()) {
            Toast.makeText(this, "Attempted to compress a null or recycled bitmap.", Toast.LENGTH_SHORT).show();
            return null;
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 80, baos);
     //   bm.recycle(); // Safe to recycle AFTER compressing
 //       byte[] b = baos.toByteArray();
//        Bitmap compressedBitmap = BitmapFactory.decodeByteArray(b, 0, b.length);
//        compressedBitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
//        compressedBitmap.recycle(); // Safe to recycle AFTER compressing
        byte[] bytearray = baos.toByteArray();
        String encImage = Base64.encodeToString(bytearray, Base64.DEFAULT);
        return encImage;

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

}
