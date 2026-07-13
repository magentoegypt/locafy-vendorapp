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

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import magentoegypt.locafy.R;
import magentoegypt.locafy.base_app.Ced_MultiVendor_FontSetting;
import magentoegypt.locafy.base_app.Ced_MultiVendor_VendorSplash;
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
import java.util.Iterator;

public class Ced_MultiVendor_GroupItems extends Ced_MultiVendor_NavigationActivity {
    Ced_MultiVendor_ConnectionDetector connectionDetector;
    Ced_MultiVendor_VendorFunctionalityList functionalityList;
    Ced_MultiVendor_VendorSessionManagement vendorSessionManagement;
    TextView save;
    String tabs;
    String product_id;
    String type;
    LinearLayout manageproductlist;
    String Currenturl;
    String updateurl;
    HashMap<String, String> dataforproducts;
    HashMap<String, String> dataforrelated;
    String Jstring;
    LinearLayout filtersection;
    Dialog listDialog;
    String datafilterjson = "";
    boolean load = true;
    Ced_MultiVendor_GroupedProductAdapter relatedListAdapter;
    String current = "1";
    ArrayList<String> attributenames;
    HashMap<String, String> attributeidvalue;
    HashMap<String, String> attributevalueid;
    ArrayList<HashMap<String, String>> relatedproductlist;
    Spinner sortby;
    Ced_MultiVendor_FontSetting fontSetting;
    TextView pages;
    TextView products;
    TextView totalpro;
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
        connectionDetector = new Ced_MultiVendor_ConnectionDetector(getApplicationContext());
        functionalityList = new Ced_MultiVendor_VendorFunctionalityList(getApplicationContext());
        vendorSessionManagement = new Ced_MultiVendor_VendorSessionManagement(getApplicationContext());
        fontSetting = new Ced_MultiVendor_FontSetting();
        dataforproducts = new HashMap<>();
        dataforrelated = new HashMap<>();
        attributeidvalue = new HashMap<>();
        attributevalueid = new HashMap<>();
        attributenames = new ArrayList<>();
        relatedproductlist = new ArrayList<>();
        selected_websitetopost = getIntent().getStringExtra("selectedwebsite");
        if (connectionDetector.isConnectingToInternet()) {
            try {
                setname(vendorSessionManagement.getvendorname());
                setprofilepic(vendorSessionManagement.getvendorpic());
                setTitle(getString(R.string.associatedproducts));
                ViewGroup content = findViewById(R.id.MultiVendor_frame_container);
                getLayoutInflater().inflate(R.layout.ced_multivendor_grouped, content, true);
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                getSupportActionBar().setHomeAsUpIndicator(R.drawable.backbutton);
                Currenturl = session.getBase_Url() + "vproductapi/vproducts/grouped";
                updateurl = session.getBase_Url() + "vendorapi/vproducts/update";
                Bundle bundle = getIntent().getExtras();

                if (bundle != null) {
                    if (getIntent().getStringExtra("filter") != null) {
                        datafilterjson = getIntent().getStringExtra("filter");
                        dataforproducts.put("filter", datafilterjson);
                    }
                    tabs = getIntent().getStringExtra("tabs");
                    product_id = getIntent().getStringExtra("product_id");
                    type = getIntent().getStringExtra("type");
                }
                /**************************************************************/
                pages = findViewById(R.id.MultiVendor_pages);
                totalpro = findViewById(R.id.MultiVendor_totalpro);
                products = findViewById(R.id.MultiVendor_products);
                save = findViewById(R.id.MultiVendor_save);
                sortby = findViewById(R.id.MultiVendor_sortby);
                fontSetting.setFontforTextviews(save, "Roboto-Medium.ttf", getApplicationContext());
                fontSetting.setFontforTextviews(pages, "Roboto-Medium.ttf", getApplicationContext());
                fontSetting.setFontforTextviews(products, "Roboto-Medium.ttf", getApplicationContext());
                fontSetting.setFontforTextviews(totalpro, "Roboto-Regular.ttf", getApplicationContext());
                manageproductlist = findViewById(R.id.MultiVendor_manageproductlist);
                filtersection = findViewById(R.id.MultiVendor_filtersection);
                filtersection.setOnClickListener(v -> showfilter());

                dataforproducts.put("vendor_id", vendorSessionManagement.getVendorid());
                dataforproducts.put("product_id", product_id);
                dataforproducts.put("hashkey", vendorSessionManagement.getHahkey());
                if (getResources().getString(R.string.Enable_Log).equals("yes")) {
                    Log.i("REpo", "" + dataforproducts);
                }

                sortby.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        current = sortby.getSelectedItem().toString();
                        if (manageproductlist.getChildCount() > 0) {
                            manageproductlist.removeAllViews();
                        }
                        try {
                            applydata(current);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });

                save.setOnClickListener(v -> {

                    dataforrelated.put("product_id", product_id);
                    dataforrelated.put("websites", selected_websitetopost);
                    dataforrelated.put("vendor_id", vendorSessionManagement.getVendorid());
                    dataforrelated.put("hashkey", vendorSessionManagement.getHahkey());

                    try {
                        if (Ced_MultiVendor_GlobalVariables.groupedproductids.size() > 0) {
                            String dataforrelatedstring = "";
                            Iterator iterator = new Ced_MultiVendor_GlobalVariables().groupedproductids.iterator();
                            while (iterator.hasNext()) {
                                String value = (String) iterator.next();
                                dataforrelatedstring = value + dataforrelatedstring;

                            }
                            JSONObject object = new JSONObject();
                            object.put("related", dataforrelatedstring);
                            JSONObject object1 = new JSONObject();
                            object1.put("links", object);
                            dataforrelated.put("groupedproducts", object1.toString());
                            dataforrelated.put("groupedqty", Ced_MultiVendor_GlobalVariables.grpupedproductqty.toString());
                        }
                        Ced_MultiVendor_ClientRequestResponse crr = new Ced_MultiVendor_ClientRequestResponse(output -> {
                            Jstring = output.toString();
                            if (functionalityList.getExtensionAddon()) {
                                JSONObject jsonObject = new JSONObject(Jstring);
                                if (jsonObject.has("vendor_approved")) {
                                    logout();
                                } else {
                                    String success = jsonObject.getJSONObject("data").getString("success");
                                    if (success.equals("true")) {
                                        Ced_MultiVendor_GlobalVariables globalVariables = new Ced_MultiVendor_GlobalVariables();
                                        globalVariables.clearallvalues();
                                        Intent related = new Intent(getApplicationContext(), Ced_MultiVendor_ManageProducts.class);
                                        related.putExtra("Navigation", "productcreate");
                                        startActivity(related);
                                        overridePendingTransition(R.anim.ced_multivendor_slide_in_sequence, R.anim.ced_multivendor_slide_out_sequence);
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


                        }, Ced_MultiVendor_GroupItems.this, "POST", dataforrelated);
                        crr.execute(updateurl);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                });

                /********************************************************************************/
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            Intent nointernet = new Intent(Ced_MultiVendor_GroupItems.this, Ced_MultiVendor_NoInternetconnection.class);
            nointernet.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            nointernet.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(nointernet);
            overridePendingTransition(R.anim.ced_multivendor_slide_in, R.anim.ced_multivendor_slide_out);
        }

    }

    private void applydata(String current) throws JSONException {
        /**************************************GetRelatedProducts*************************************************/
        Ced_MultiVendor_ClientRequestResponse crr = new Ced_MultiVendor_ClientRequestResponse(output -> {
            Jstring = output.toString();
            if (functionalityList.getExtensionAddon()) {
                JSONObject jsonObject = new JSONObject(Jstring);
                if (jsonObject.has("vendor_approved")) {
                    logout();
                } else {
                    String success = jsonObject.getJSONObject("data").getString("success");
                    if (success.equals("true")) {
                        JSONArray latest_order = jsonObject.getJSONObject("data").getJSONArray("products");
                        totalpro.setText(jsonObject.getJSONObject("data").getString("count"));
                        for (int l = 0; l < latest_order.length(); l++) {
                            JSONObject orderrow = latest_order.getJSONObject(l);
                            View related_row = View.inflate(getApplicationContext(), R.layout.ced_multivendor_groupedproductlist, null);
                            TextView productIDtag = related_row.findViewById(R.id.MultiVendor_productIDtag);
                            TextView skutag = related_row.findViewById(R.id.MultiVendor_skutag);
                            TextView defaultqty = related_row.findViewById(R.id.MultiVendor_defaultqty);
                            TextView RegularPricetag = related_row.findViewById(R.id.MultiVendor_RegularPricetag);
                            TextView ProductNametag = related_row.findViewById(R.id.MultiVendor_ProductNametag);
                            TextView product_id = related_row.findViewById(R.id.MultiVendor_product_id);
                            TextView sku = related_row.findViewById(R.id.MultiVendor_sku);
                            TextView RegularPrice = related_row.findViewById(R.id.MultiVendor_RegularPrice);
                            TextView product_name = related_row.findViewById(R.id.MultiVendor_product_name);
                            EditText qty = related_row.findViewById(R.id.MultiVendor_qty);
                            final CheckBox selectrelated = related_row.findViewById(R.id.MultiVendor_selectrelated);
                            /********************************fonts***********************************/
                            fontSetting.setFontforTextviews(productIDtag, "Roboto-Medium.ttf", getApplicationContext());
                            fontSetting.setFontforTextviews(skutag, "Roboto-Medium.ttf", getApplicationContext());
                            fontSetting.setFontforTextviews(defaultqty, "Roboto-Medium.ttf", getApplicationContext());
                            fontSetting.setFontforTextviews(RegularPricetag, "Roboto-Medium.ttf", getApplicationContext());
                            fontSetting.setFontforTextviews(ProductNametag, "Roboto-Medium.ttf", getApplicationContext());

                            fontSetting.setFontforTextviews(product_id, "Roboto-Regular.ttf", getApplicationContext());
                            fontSetting.setFontforTextviews(sku, "Roboto-Regular.ttf", getApplicationContext());
                            fontSetting.setFontforTextviews(RegularPrice, "Roboto-Regular.ttf", getApplicationContext());
                            fontSetting.setFontforTextviews(product_name, "Roboto-Regular.ttf", getApplicationContext());
                            fontSetting.setfontforCheckbox(selectrelated, "Roboto-Medium.ttf", getApplicationContext());

                            /********************************fonts***********************************/
                            if (Ced_MultiVendor_GlobalVariables.groupedproductids.contains("#" + orderrow.getString("product_id"))) {
                                Ced_MultiVendor_GlobalVariables.grpupedproductqty.put(orderrow.getString("product_id"), orderrow.getString("qty"));
                                selectrelated.setChecked(true);
                                qty.setEnabled(false);
                            }
                            if (orderrow.getString("selected").equals("true")) {
                                if (!(Ced_MultiVendor_GlobalVariables.groupedproductids.contains("#" + orderrow.getString("product_id")))) {
                                    Ced_MultiVendor_GlobalVariables.groupedproductids.add("#" + orderrow.getString("product_id"));
                                    Ced_MultiVendor_GlobalVariables.grpupedproductqty.put(orderrow.getString("product_id"), orderrow.getString("qty"));
                                    selectrelated.setChecked(true);
                                    qty.setEnabled(false);

                                }

                            }
                            selectrelated.setOnCheckedChangeListener((buttonView, isChecked) -> {
                                LinearLayout linearLayout = (LinearLayout) selectrelated.getParent();
                                LinearLayout firstchild = (LinearLayout) linearLayout.getChildAt(1);
                                LinearLayout thirdchild = (LinearLayout) firstchild.getChildAt(2);
                                TextView proid = (TextView) thirdchild.getChildAt(0);
                                EditText text = (EditText) thirdchild.getChildAt(4);
                                if (isChecked) {

                                    if (!(Ced_MultiVendor_GlobalVariables.groupedproductids.contains(proid.getText().toString()))) {
                                        String[] parts = proid.getText().toString().split("#");
                                        Ced_MultiVendor_GlobalVariables.groupedproductids.add(proid.getText().toString());
                                        try {
                                            Ced_MultiVendor_GlobalVariables.grpupedproductqty.put(parts[1], text.getText().toString());
                                            text.setEnabled(false);
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                    if (getResources().getString(R.string.Enable_Log).equals("yes")) {
                                        Log.i("groupedproductids", "" + Ced_MultiVendor_GlobalVariables.groupedproductids);
                                        Log.i("grpupedproductqty", "" + Ced_MultiVendor_GlobalVariables.grpupedproductqty);
                                    }
                                }
                                else {
                                    if ((Ced_MultiVendor_GlobalVariables.groupedproductids.contains(proid.getText().toString()))) {
                                        Ced_MultiVendor_GlobalVariables.groupedproductids.remove(proid.getText().toString());
                                        try {
                                            String[] parts = proid.getText().toString().split("#");
                                            Ced_MultiVendor_GlobalVariables.grpupedproductqty.remove(parts[1]);
                                            text.setEnabled(true);
                                        }
                                        catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
                                    if (getResources().getString(R.string.Enable_Log).equals("yes")) {
                                        Log.i("groupedproductids", "" + Ced_MultiVendor_GlobalVariables.groupedproductids);
                                        Log.i("grpupedproductqty", "" + Ced_MultiVendor_GlobalVariables.grpupedproductqty);
                                    }
                                }
                            });
                            selectrelated.setText("#" + orderrow.getString("product_name"));
                            product_id.setText("#" + orderrow.getString("product_id"));
                            sku.setText(orderrow.getString("sku"));
                            RegularPrice.setText(orderrow.getString("regular_price"));
                            product_name.setText(orderrow.getString("product_name"));
                            qty.setText(orderrow.getString("qty"));
                            manageproductlist.addView(related_row);
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
        }, Ced_MultiVendor_GroupItems.this, "POST", dataforproducts);
        crr.execute(Currenturl + "/page/" + current);
        /***************************************************************************************/
    }

    @Override
    protected void onResume() {
        Ced_MultiVendor_ConnectionDetector connectionDetector = new Ced_MultiVendor_ConnectionDetector(Ced_MultiVendor_GroupItems.this);
        if (connectionDetector.isConnectingToInternet()) {
            setname(vendorSessionManagement.getvendorname());
            setprofilepic(vendorSessionManagement.getvendorpic());
            changetitle("Associated Products");
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

    private void showfilter() {
        try {
            listDialog = new Dialog(this, R.style.PauseDialog);
            listDialog.setTitle(getResources().getString(R.string.alert_name));
            LayoutInflater li = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View v = li.inflate(R.layout.ced_multivendor_header_at_groupedproductlisting, null, false);
            final TextView setfilter = v.findViewById(R.id.MultiVendor_setfilter);
            final TextView unsetfilter = v.findViewById(R.id.MultiVendor_unsetfilter);
            final EditText from_price = v.findViewById(R.id.MultiVendor_from_price);
            final EditText to_price = v.findViewById(R.id.MultiVendor_to_price);
            final EditText from_qty = v.findViewById(R.id.MultiVendor_from_qty);
            final EditText to_qty = v.findViewById(R.id.MultiVendor_to_qty);
            final EditText from_product_id = v.findViewById(R.id.MultiVendor_from_product_id);
            final EditText to_product_id = v.findViewById(R.id.MultiVendor_to_product_id);
            final EditText edt_productname = v.findViewById(R.id.MultiVendor_edt_productname);
            final EditText edt_productsku = v.findViewById(R.id.MultiVendor_edt_productsku);
            if (!(datafilterjson.isEmpty())) {
                JSONObject object = new JSONObject(datafilterjson);
                from_price.setText(object.getJSONObject("price").getString("from"));
                to_price.setText(object.getJSONObject("price").getString("to"));
                from_qty.setText(object.getJSONObject("qty").getString("from"));
                to_qty.setText(object.getJSONObject("qty").getString("to"));
                from_product_id.setText(object.getJSONObject("entity_id").getString("from"));
                to_product_id.setText(object.getJSONObject("entity_id").getString("to"));
                edt_productname.setText(object.getString("name"));
                edt_productsku.setText(object.getString("sku"));

            }
            unsetfilter.setOnClickListener(v12 -> {
                from_price.setText("");
                to_price.setText("");
                from_qty.setText("");
                to_qty.setText("");
                from_product_id.setText("");
                to_product_id.setText("");
                edt_productname.setText("");
                edt_productsku.setText("");
                datafilterjson = "";
                final JSONObject mainfilter = new JSONObject();
                JSONObject pricejsonObject = new JSONObject();
                try {
                    listDialog.dismiss();
                    pricejsonObject.put("from", from_price.getText().toString());
                    pricejsonObject.put("to", to_price.getText().toString());
                    mainfilter.put("price", pricejsonObject);
                    JSONObject qtyjsonObject = new JSONObject();
                    qtyjsonObject.put("from", from_qty.getText().toString());
                    qtyjsonObject.put("to", to_qty.getText().toString());
                    mainfilter.put("qty", qtyjsonObject);
                    JSONObject proidjsonObject = new JSONObject();
                    proidjsonObject.put("from", from_product_id.getText().toString());
                    proidjsonObject.put("to", to_product_id.getText().toString());
                    mainfilter.put("entity_id", proidjsonObject);
                    mainfilter.put("name", edt_productname.getText().toString());
                    mainfilter.put("sku", edt_productsku.getText().toString());
                    Intent intent = new Intent(getApplicationContext(), Ced_MultiVendor_GroupItems.class);
                    intent.putExtra("filter", mainfilter.toString());
                    intent.putExtra("tabs", tabs);
                    intent.putExtra("product_id", product_id);
                    intent.putExtra("type", type);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);

                } catch (JSONException e) {
                    Intent main = new Intent(Ced_MultiVendor_GroupItems.this, Ced_MultiVendor_VendorSplash.class);
                    main.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    main.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(main);
                    overridePendingTransition(R.anim.ced_multivendor_slide_in, R.anim.ced_multivendor_slide_out);
                }


            });

            setfilter.setOnClickListener(v1 -> {
                final JSONObject mainfilter = new JSONObject();
                JSONObject pricejsonObject = new JSONObject();
                try {
                    listDialog.dismiss();
                    pricejsonObject.put("from", from_price.getText().toString());
                    pricejsonObject.put("to", to_price.getText().toString());
                    mainfilter.put("price", pricejsonObject);
                    JSONObject qtyjsonObject = new JSONObject();
                    qtyjsonObject.put("from", from_qty.getText().toString());
                    qtyjsonObject.put("to", to_qty.getText().toString());
                    mainfilter.put("qty", qtyjsonObject);
                    JSONObject proidjsonObject = new JSONObject();
                    proidjsonObject.put("from", from_product_id.getText().toString());
                    proidjsonObject.put("to", to_product_id.getText().toString());
                    mainfilter.put("entity_id", proidjsonObject);
                    mainfilter.put("name", edt_productname.getText().toString());
                    mainfilter.put("sku", edt_productsku.getText().toString());
                    Intent intent = new Intent(getApplicationContext(), Ced_MultiVendor_GroupItems.class);
                    intent.putExtra("filter", mainfilter.toString());
                    intent.putExtra("tabs", tabs);
                    intent.putExtra("product_id", product_id);
                    intent.putExtra("type", type);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            });
            listDialog.setContentView(v);
            listDialog.setCancelable(true);
            listDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
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
