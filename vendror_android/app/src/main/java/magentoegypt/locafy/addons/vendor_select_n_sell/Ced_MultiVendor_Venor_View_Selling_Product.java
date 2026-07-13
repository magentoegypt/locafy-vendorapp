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

package magentoegypt.locafy.addons.vendor_select_n_sell;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import magentoegypt.locafy.base_app.Ced_MultiVendor_FontSetting;
import magentoegypt.locafy.base_app.Ced_MultiVendor_VendorFunctionalityList;
import magentoegypt.locafy.navigation_drawer.Activity.Ced_MultiVendor_NavigationActivity;
import magentoegypt.locafy.api_request_response_section.network_error_handling.Ced_MultiVendor_ConnectionDetector;
import magentoegypt.locafy.api_request_response_section.network_error_handling.Ced_MultiVendor_NoInternetconnection;
import magentoegypt.locafy.api_request_response_section.network_error_handling.Ced_MultiVendor_UnAuthourizedRequestError;
import magentoegypt.locafy.api_request_response_section.AsyncResponse;
import magentoegypt.locafy.api_request_response_section.Ced_MultiVendor_ClientRequestResponse;
import magentoegypt.locafy.vendor_session.Ced_MultiVendor_VendorSessionManagement;
import magentoegypt.locafy.R;
import magentoegypt.locafy_constant.AppConstant;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Created by cedcoss on 2/2/17.
 */
public class Ced_MultiVendor_Venor_View_Selling_Product extends Ced_MultiVendor_NavigationActivity {
    Ced_MultiVendor_ConnectionDetector connectionDetector;
    Ced_MultiVendor_VendorFunctionalityList functionalityList;
    Ced_MultiVendor_VendorSessionManagement vendorSessionManagement;
    Ced_MultiVendor_FontSetting fontSetting;
    String Currenturl;
    HashMap<String, String> post_data;
    String product_id;
    String out, prod_name;
    EditText edt_sku, edt_price, edt_stock;
    Spinner edt_stock_availability, edt_status;
    Button MultiVendor_header_txt, save;
    String save_url;
    LinearLayout linear_status;
    private JSONObject product_status_json_object;
    private List<String> prodduct_status_list;
    private ArrayAdapter<String> product_status_array_adapter;
    private Map<String, String> product_status_map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            fontSetting = new Ced_MultiVendor_FontSetting();
            connectionDetector = new Ced_MultiVendor_ConnectionDetector(getApplicationContext());
            functionalityList = new Ced_MultiVendor_VendorFunctionalityList(getApplicationContext());
            vendorSessionManagement = new Ced_MultiVendor_VendorSessionManagement(getApplicationContext());
            post_data = new HashMap<>();
            if (connectionDetector.isConnectingToInternet()) {
                Currenturl = session.getBase_Url() + "vmultisellerapi/product/viewSellingProduct";
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                getSupportActionBar().setHomeAsUpIndicator(R.drawable.backbutton);
                ViewGroup content = (ViewGroup) findViewById(R.id.MultiVendor_frame_container);
                getLayoutInflater().inflate(R.layout.ced_multivendor_activity_assign_product, content, true);
                save_url = session.getBase_Url() + "vmultisellerapi/product/update";
                MultiVendor_header_txt = (Button) findViewById(R.id.MultiVendor_header_txt);
                prod_name = getIntent().getStringExtra("product_name");
                MultiVendor_header_txt.setText("ASSIGN PRODUCT - " + prod_name);
                edt_sku = (EditText) findViewById(R.id.edt_sku);
                edt_price = (EditText) findViewById(R.id.edt_price);
                edt_stock = (EditText) findViewById(R.id.edt_stock);
                edt_stock_availability = (Spinner) findViewById(R.id.edt_stock_availability);
                edt_status = (Spinner) findViewById(R.id.edt_status);
                linear_status = (LinearLayout) findViewById(R.id.linear_status);
                linear_status.setVisibility(View.VISIBLE);
                save = (Button) findViewById(R.id.save);
                save.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AppConstant.lockButton(v);
                        try {
                            if (edt_sku.getText().toString().isEmpty()) {


                                edt_sku.setError("This is empty");
                            } else if (edt_price.getText().toString().isEmpty()) {

                                edt_price.setError("This is empty");
                            } else if (edt_stock.getText().toString().isEmpty()) {


                                edt_stock.setError("This is empty");
                            } else if (edt_stock_availability.getSelectedItem().equals("Please Select")) {

                                Toast.makeText(getApplicationContext(), "Please Select Stock Availability", Toast.LENGTH_LONG).show();
                            } else {


                                JSONObject object = new JSONObject();

                                if (edt_stock_availability.getSelectedItem().equals("In Stock")) {
                                    object.put("is_in_stock", "1");

                                } else {
                                    object.put("is_in_stock", "0");
                                }

                                object.put("qty", edt_stock.getText().toString());
                                JSONObject object1 = new JSONObject();
                                object1.put("stock_data", object);
                                object1.put("sku", edt_sku.getText().toString());
                                object1.put("price", edt_price.getText().toString());
                                JSONObject object2 = new JSONObject();
                                object2.put("product", object1);
                                Log.i("hello", object2.toString());

                                for (int i = 0; i < product_status_map.size(); i++) {
                                    if (product_status_map.containsKey(edt_status.getSelectedItem().toString().trim())) {
                                        object1.put("status", product_status_map.get(edt_status.getSelectedItem().toString().trim()));
//                                        object1.put("product_status", product_status_map.get(edt_status.getSelectedItem().toString().trim()));
                                    }
                                }

//                                if (edt_status.getSelectedItem().equals("Enable")) {
//                                    object1.put("status", "1");
//                                } else {
//                                    object1.put("status", "0");
//                                }
                                post_data.put("product", object1.toString());
                                Ced_MultiVendor_ClientRequestResponse response = new Ced_MultiVendor_ClientRequestResponse(new AsyncResponse() {
                                    @Override
                                    public void processFinish(Object output) throws JSONException {
                                        JSONObject object3 = new JSONObject(output.toString());
                                        if (object3.getJSONObject("data").getBoolean("success")) {
                                            Toast.makeText(getApplicationContext(), object3.getJSONObject("data").getString("message"), Toast.LENGTH_LONG).show();
                                            Intent intent = new Intent(Ced_MultiVendor_Venor_View_Selling_Product.this, Product_list.class);
                                            startActivity(intent);
                                            finish();
                                        } else {
                                            Toast.makeText(getApplicationContext(), object3.getJSONObject("data").getString("message"), Toast.LENGTH_LONG).show();
                                        }

                                    }
                                }, Ced_MultiVendor_Venor_View_Selling_Product.this, "POST", post_data);
                                response.execute(save_url);
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                });
                setname(vendorSessionManagement.getvendorname());
                setprofilepic(vendorSessionManagement.getvendorpic());
                changetitle("Select and Sell");
                prodduct_status_list = new ArrayList<>();
                product_status_map = new HashMap<>();
                if (getIntent().hasExtra("product_status_json_object")) {
                    product_status_json_object = new JSONObject(getIntent().getStringExtra("product_status_json_object"));
                    Iterator<String> keys = product_status_json_object.keys();
                    while (keys.hasNext()) {
                        String key = keys.next();
                        prodduct_status_list.add(product_status_json_object.getString(key));
                        product_status_map.put(product_status_json_object.getString(key), key);
                    }
                    product_status_array_adapter = new ArrayAdapter<>(Ced_MultiVendor_Venor_View_Selling_Product.this, R.layout.simple_list_item_1, prodduct_status_list);
                    edt_status.setAdapter(product_status_array_adapter);
                }

                product_id = getIntent().getStringExtra("product_id");

                post_data.put("vendor_id", vendorSessionManagement.getVendorid());
                post_data.put("hashkey", vendorSessionManagement.getHahkey());
          /*  post_data.put("vendor_id", "1");
            post_data.put("hashkey", "2a1f678300166f9b941de29a25f4a4bf");*/
                post_data.put("id", product_id);

                request();


            } else {
                Intent nointernet = new Intent(Ced_MultiVendor_Venor_View_Selling_Product.this, Ced_MultiVendor_NoInternetconnection.class);
                nointernet.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                nointernet.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(nointernet);
                overridePendingTransition(R.anim.ced_multivendor_slide_in, R.anim.ced_multivendor_slide_out);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void request() {
        Ced_MultiVendor_ClientRequestResponse response = new Ced_MultiVendor_ClientRequestResponse(new AsyncResponse() {
            @Override
            public void processFinish(Object output) throws JSONException {
                out = output.toString();
                get_data();


            }
        }, Ced_MultiVendor_Venor_View_Selling_Product.this, "POST", post_data);
        response.execute(Currenturl);
    }

    private void get_data() throws JSONException {

        if (functionalityList.getExtensionAddon()) {
            JSONObject jsonObject = new JSONObject(out);
            if (jsonObject.has("vendor_approved")) {
                logout();
            } else {
                if (jsonObject.getJSONObject("data").getBoolean("success")) {
                    edt_sku.setText(jsonObject.getJSONObject("data").getJSONObject("prod_info").getString("sku"));
                    edt_price.setText(jsonObject.getJSONObject("data").getJSONObject("prod_info").getString("price"));
                    edt_stock.setText(jsonObject.getJSONObject("data").getJSONObject("prod_info").getString("stock_qty"));
                    if (jsonObject.getJSONObject("data").getJSONObject("prod_info").getString("is_in_stock").equals("true")) {
                        edt_stock_availability.setSelection(1);
                    } else if (jsonObject.getJSONObject("data").getJSONObject("prod_info").getString("is_in_stock").equals("0")) {
                        edt_stock_availability.setSelection(2);
                    } else if (jsonObject.getJSONObject("data").getJSONObject("prod_info").getString("is_in_stock").equals("1")) {
//                        edt_stock_availability.setSelection(0);
                        edt_stock_availability.setSelection(1);
                    } else {
                        edt_stock_availability.setSelection(0);
                    }
                    String selected_product_status_key = jsonObject.getJSONObject("data").getJSONObject("prod_info").getString("status");
                    Iterator<String> keys = product_status_json_object.keys();
                    while (keys.hasNext()) {
                        if (Objects.equals(keys.next().trim(), selected_product_status_key.trim())) {
                            if (prodduct_status_list.contains(product_status_json_object.getString(selected_product_status_key))) {
                                if (edt_status.getSelectedItem().toString().equalsIgnoreCase(product_status_json_object.getString(selected_product_status_key))) {
                                    int int_key = Integer.parseInt(selected_product_status_key);
                                    edt_status.setSelection(--int_key);
                                }
                            }
                        }
                    }

                    if (jsonObject.getJSONObject("data").getJSONObject("prod_info").getString("status").equals("1")) {
                        edt_status.setSelection(0);
                    } else {
                        edt_status.setSelection(1);
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

    @Override
    protected void onResume() {
        Ced_MultiVendor_ConnectionDetector connectionDetector = new Ced_MultiVendor_ConnectionDetector(Ced_MultiVendor_Venor_View_Selling_Product.this);
        if (connectionDetector.isConnectingToInternet()) {
            setname(vendorSessionManagement.getvendorname());
            setprofilepic(vendorSessionManagement.getvendorpic());
            changetitle("Select and Sell");
            //  invalidateOptionsMenu();
            super.onResume();
        } else {
            Intent intent = new Intent(getApplicationContext(), Ced_MultiVendor_NoInternetconnection.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            super.onResume();
        }
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
