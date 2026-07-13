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
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import magentoegypt.locafy.R;
import magentoegypt.locafy.api_request_response_section.Ced_MultiVendor_ClientRequestResponse;
import magentoegypt.locafy.api_request_response_section.network_error_handling.Ced_MultiVendor_ConnectionDetector;
import magentoegypt.locafy.api_request_response_section.network_error_handling.Ced_MultiVendor_NoInternetconnection;
import magentoegypt.locafy.api_request_response_section.network_error_handling.Ced_MultiVendor_UnAuthourizedRequestError;
import magentoegypt.locafy.base_app.Ced_MultiVendor_FontSetting;
import magentoegypt.locafy.base_app.Ced_MultiVendor_VendorFunctionalityList;
import magentoegypt.locafy.navigation_drawer.Activity.Ced_MultiVendor_NavigationActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class Ced_MultiVendor_AddVendorProduct extends Ced_MultiVendor_NavigationActivity {
    Ced_MultiVendor_FontSetting fontSetting;
    Ced_MultiVendor_ConnectionDetector connectionDetector;
    Ced_MultiVendor_VendorFunctionalityList functionalityList;
    String CurrentUrl = "";
    String withoutproductaddonUrl = "";
    String urlforsequence = "";
    String Jstring;
    Spinner producttypedropdown;
    Spinner attributesetdropdown;
    ArrayList<String> producttypelist;
    ArrayList<String> attributetypelist;
    TextView continuewithtype;
    TextView selectproducttypetext;
    TextView producttype;
    TextView attributeset;
    HashMap<String, String> typeandlabel;
    HashMap<String, String> attributetypeandlabel;
    String dataforproductcreation = "";
    boolean valuetoreturn = false;
    HashMap<String, String> allowdata;
    String type = "";

    JSONArray Category;
    int level_value = 0;
    ArrayList<String> trackSelectedChecks;

    LinearLayout categoryselection;
    JSONArray catfilters;
    HashSet<String> selected_category_ids;
    String last_category;
    TextView MultiVendor_selectproductcattxt;

    JSONArray category;
    JSONObject websites, stores, storeViews;
    String selected_websitetopost;

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

        selected_websitetopost = getIntent().getStringExtra("selectedwebsite");
        if (connectionDetector.isConnectingToInternet()) {
            ViewGroup content = findViewById(R.id.MultiVendor_frame_container);
            getLayoutInflater().inflate(R.layout.ced_multivendor_activity_add_vendor_product, content, true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.backbutton);
            setname(session.getvendorname());
            setprofilepic(session.getvendorpic());
            changetitle("Add Product");
            typeandlabel = new HashMap<>();
            allowdata = new HashMap<>();
            if (session.getStoreId() != null)
                allowdata.put("store_id", session.getStoreId());
            allowdata.put("vendor_id", session.getVendorid());
            allowdata.put("hashkey", session.getHahkey());
            attributetypeandlabel = new HashMap<>();


            continuewithtype = findViewById(R.id.MultiVendor_continuewithtype);
            producttype = findViewById(R.id.MultiVendor_producttype);
            attributeset = findViewById(R.id.MultiVendor_attributeset);
            selectproducttypetext = findViewById(R.id.MultiVendor_selectproducttypetext);
            CurrentUrl = session.getBase_Url() + "vproductapi/vproducts/alloweddata";
            withoutproductaddonUrl = session.getBase_Url() + "vendorapi/vproducts/allowedprodata";
            urlforsequence = session.getBase_Url() + "vproductapi/vproducts/allowedAttribute";
            producttypedropdown = findViewById(R.id.MultiVendor_producttypedropdown);
            attributesetdropdown = findViewById(R.id.MultiVendor_attributesetdropdown);
            producttypelist = new ArrayList<>();
            attributetypelist = new ArrayList<>();

            continuewithtype.setOnClickListener(v -> {
                System.out.println(typeandlabel.get(String.valueOf(producttypedropdown.getSelectedItem())));
                if (functionalityList.getProductAddon()) {
                    HashMap<String, String> dataforsequence = new HashMap<>();
                    dataforsequence.put("type", typeandlabel.get(String.valueOf(producttypedropdown.getSelectedItem())));
                  //  dataforsequence.put("type", "simple");
                    dataforsequence.put("set", attributetypeandlabel.get(String.valueOf(attributesetdropdown.getSelectedItem())));
                    dataforsequence.put("vendor_id", session.getVendorid());
                    dataforsequence.put("hashkey", session.getHahkey());
                    getSequence(dataforsequence);
                } else {
                    Intent intent = new Intent(Ced_MultiVendor_AddVendorProduct.this, ProductCreationNew.class);
                    intent.putExtra("dataforproductcreation", dataforproductcreation);
                    intent.putExtra("selectedwebsite", selected_websitetopost);
                    intent.putExtra("newProduct", true);
                    intent.putExtra("type", "simple");
                    intent.putExtra("category", category.toString());
                    intent.putExtra("websites", websites.toString());
                    intent.putExtra("storeViews", storeViews.toString());
                    intent.putExtra("stores", stores.toString());
                    startActivity(intent);
                    overridePendingTransition(R.anim.ced_multivendor_slide_in, R.anim.ced_multivendor_slide_out);
                }
            });
            fontSetting.setFontforTextviews(selectproducttypetext, "Roboto-Medium.ttf", Ced_MultiVendor_AddVendorProduct.this);
            fontSetting.setFontforTextviews(producttype, "Roboto-Medium.ttf", Ced_MultiVendor_AddVendorProduct.this);
            fontSetting.setFontforTextviews(continuewithtype, "Roboto-Medium.ttf", Ced_MultiVendor_AddVendorProduct.this);
            fontSetting.setFontforTextviews(attributeset, "Roboto-Medium.ttf", Ced_MultiVendor_AddVendorProduct.this);
            if (functionalityList.getProductAddon()) {
                attributesetdropdown.setVisibility(View.VISIBLE);
            } else {
                attributesetdropdown.setVisibility(View.GONE);
                attributeset.setVisibility(View.GONE);
            }
            if (functionalityList.getProductAddon()) {
                Ced_MultiVendor_ClientRequestResponse crr = new Ced_MultiVendor_ClientRequestResponse(output -> {
                    Jstring = output.toString();
                    if (functionalityList.getExtensionAddon()) {
                        JSONObject jsonObject = new JSONObject(Jstring);
                        String success = jsonObject.getJSONObject("data").getString("success");
                        if (success.equals("true")) {
                            JSONArray jsonArray = jsonObject.getJSONObject("data").getJSONArray("allowed_pro_type");
                            category = jsonObject.getJSONObject("data").getJSONArray("category");
                            websites = jsonObject.getJSONObject("data").getJSONObject("websites");
                            stores = jsonObject.getJSONObject("data").getJSONObject("stores");
                            storeViews = jsonObject.getJSONObject("data").getJSONObject("storeViews");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject object = jsonArray.getJSONObject(i);
                                typeandlabel.put(object.getString("label"), object.getString("value"));
                                producttypelist.add(object.getString("label"));
                            }
                            ArrayAdapter<String> adp = new ArrayAdapter<String>(Ced_MultiVendor_AddVendorProduct.this, R.layout.ced_multivendor_textview, producttypelist);
                            producttypedropdown.setAdapter(adp);
                            JSONArray json_Array = jsonObject.getJSONObject("data").getJSONArray("allowed_attribute_set");
                            for (int i = 0; i < json_Array.length(); i++) {
                                JSONObject object = json_Array.getJSONObject(i);
                                attributetypeandlabel.put(object.getString("label"), object.getString("value"));
                                attributetypelist.add(object.getString("label"));
                            }
                            ArrayAdapter<String> adp_attribute = new ArrayAdapter<String>(Ced_MultiVendor_AddVendorProduct.this, R.layout.ced_multivendor_textview, attributetypelist);
                            attributesetdropdown.setAdapter(adp_attribute);
                        }
                    } else {
                        Intent intent = new Intent(getApplicationContext(), Ced_MultiVendor_UnAuthourizedRequestError.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        overridePendingTransition(R.anim.ced_multivendor_slide_in, R.anim.ced_multivendor_slide_out);
                    }
                }, Ced_MultiVendor_AddVendorProduct.this, "POST", allowdata);
                crr.execute(CurrentUrl);
            } else {
                Ced_MultiVendor_ClientRequestResponse crr = new Ced_MultiVendor_ClientRequestResponse(output -> {
                    Jstring = output.toString();
                    dataforproductcreation = Jstring;
                    if (functionalityList.getExtensionAddon()) {
                        JSONObject jsonObject = new JSONObject(Jstring);
                        String success = jsonObject.getJSONObject("data").getString("success");
                        if (success.equals("true")) {
                            JSONArray jsonArray = jsonObject.getJSONObject("data").getJSONArray("allowed_pro_type");
                            for (int i = 1; i < jsonArray.length(); i++) {
                                JSONObject object = jsonArray.getJSONObject(i);
                                typeandlabel.put(object.getString("label"), object.getString("value"));
                                producttypelist.add(object.getString("label"));
                            }
                            ArrayAdapter<String> adp = new ArrayAdapter<String>(Ced_MultiVendor_AddVendorProduct.this, R.layout.ced_multivendor_textview, producttypelist);
                            producttypedropdown.setAdapter(adp);
                        }

                    } else {
                        Intent intent = new Intent(getApplicationContext(), Ced_MultiVendor_UnAuthourizedRequestError.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        overridePendingTransition(R.anim.ced_multivendor_slide_in, R.anim.ced_multivendor_slide_out);
                    }


                }, Ced_MultiVendor_AddVendorProduct.this, "POST", allowdata);
                crr.execute(withoutproductaddonUrl);
            }

        } else {
            Intent nointernet = new Intent(Ced_MultiVendor_AddVendorProduct.this, Ced_MultiVendor_NoInternetconnection.class);
            nointernet.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            nointernet.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(nointernet);
            overridePendingTransition(R.anim.ced_multivendor_slide_in, R.anim.ced_multivendor_slide_out);
        }


    }

    @Override
    protected void onResume() {
        Ced_MultiVendor_ConnectionDetector connectionDetector = new Ced_MultiVendor_ConnectionDetector(Ced_MultiVendor_AddVendorProduct.this);
        if (connectionDetector.isConnectingToInternet()) {
            setname(session.getvendorname());
            setprofilepic(session.getvendorpic());
            changetitle("Add Product");
            invalidateOptionsMenu();
        } else {
            Intent intent = new Intent(getApplicationContext(), Ced_MultiVendor_NoInternetconnection.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
        super.onResume();
    }

    public boolean getSequence(HashMap<String, String> data) {
        if (session.getStoreId() != null)
            data.put("store_id", session.getStoreId());
        Ced_MultiVendor_ClientRequestResponse crr = new Ced_MultiVendor_ClientRequestResponse(output -> {
            Jstring = output.toString();
            if (functionalityList.getExtensionAddon()) {
                JSONObject jsonObject = new JSONObject(Jstring);
                String success = jsonObject.getJSONObject("data").getString("success");
                if (success.equals("true")) {
                    dataforproductcreation = Jstring;
                    valuetoreturn = true;
                    if (typeandlabel.get(String.valueOf(producttypedropdown.getSelectedItem())).equals("configurable")) {
                       // Intent intent = new Intent(Ced_MultiVendor_AddVendorProduct.this, Ced_MultiVendor_ConfigurableAttribute.class);
                      //  Intent intent = new Intent(Ced_MultiVendor_AddVendorProduct.this, ConfigAttributeSelectionActivity.class);
                        Intent intent = new Intent(Ced_MultiVendor_AddVendorProduct.this, ProductCreationNew.class);
                        intent.putExtra("dataforproductcreation", dataforproductcreation);
                        intent.putExtra("type", typeandlabel.get(String.valueOf(producttypedropdown.getSelectedItem())));
                        intent.putExtra("category", category.toString());
                        intent.putExtra("websites", websites.toString());
                        intent.putExtra("selectedwebsite", selected_websitetopost);
                        intent.putExtra("stores", stores.toString());
                        intent.putExtra("storeViews", storeViews.toString());
                        intent.putExtra("attribute_set", attributetypeandlabel.get(String.valueOf(attributesetdropdown.getSelectedItem())));
                        startActivity(intent);
                        overridePendingTransition(R.anim.ced_multivendor_slide_in, R.anim.ced_multivendor_slide_out);
                    } else {
                        Intent intent = new Intent(Ced_MultiVendor_AddVendorProduct.this, ProductCreationNew.class);
                        intent.putExtra("dataforproductcreation", dataforproductcreation);
                        intent.putExtra("type", "simple");
                        intent.putExtra("attribute_set", attributetypeandlabel.get(String.valueOf(attributesetdropdown.getSelectedItem())));
                        intent.putExtra("category", category.toString());
                        intent.putExtra("websites", websites.toString());
                        intent.putExtra("storeViews", storeViews.toString());
                        intent.putExtra("stores", stores.toString());
                        intent.putExtra("newProduct", true);
                        intent.putExtra("selectedwebsite", selected_websitetopost);
                        startActivity(intent);
                        overridePendingTransition(R.anim.ced_multivendor_slide_in, R.anim.ced_multivendor_slide_out);
                    }

                } else {
                    valuetoreturn = false;
                }
            } else {
                Intent intent = new Intent(getApplicationContext(), Ced_MultiVendor_UnAuthourizedRequestError.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                overridePendingTransition(R.anim.ced_multivendor_slide_in, R.anim.ced_multivendor_slide_out);
            }


        }, Ced_MultiVendor_AddVendorProduct.this, "POST", data);
        crr.execute(urlforsequence);
        return valuetoreturn;
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