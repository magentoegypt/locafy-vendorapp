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
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import magentoegypt.locafy.base_app.Ced_MultiVendor_FontSetting;
import magentoegypt.locafy.base_app.Ced_MultiVendor_VendorFunctionalityList;
import magentoegypt.locafy.navigation_drawer.Activity.Ced_MultiVendor_NavigationActivity;
import magentoegypt.locafy.api_request_response_section.network_error_handling.Ced_MultiVendor_ConnectionDetector;
import magentoegypt.locafy.api_request_response_section.network_error_handling.Ced_MultiVendor_NoInternetconnection;
import magentoegypt.locafy.api_request_response_section.AsyncResponse;
import magentoegypt.locafy.api_request_response_section.Ced_MultiVendor_ClientRequestResponse;
import magentoegypt.locafy.vendor_session.Ced_MultiVendor_VendorSessionManagement;
import magentoegypt.locafy.R;
import magentoegypt.locafy_constant.AppConstant;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by cedcoss on 2/2/17.
 */
public class Ced_MultiVendor_Sell_This_Product extends Ced_MultiVendor_NavigationActivity {
    Ced_MultiVendor_ConnectionDetector connectionDetector;
    Ced_MultiVendor_VendorFunctionalityList functionalityList;
    Ced_MultiVendor_VendorSessionManagement vendorSessionManagement;
    Ced_MultiVendor_FontSetting fontSetting;
    String id, prod_name;
    HashMap<String, String> post_data;
    Button MultiVendor_header_txt;
    EditText edt_sku, edt_price, edt_stock;
    Spinner edt_stock_availability;
    Button save;
    String save_url;

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
        post_data = new HashMap<>();
        save_url = session.getBase_Url() + "vmultisellerapi/product/saveDuplicate";


        if (connectionDetector.isConnectingToInternet()) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.backbutton);
            setname(vendorSessionManagement.getvendorname());
            setprofilepic(vendorSessionManagement.getvendorpic());
            changetitle("Assign Product");
            ViewGroup content = (ViewGroup) findViewById(R.id.MultiVendor_frame_container);
            getLayoutInflater().inflate(R.layout.ced_multivendor_activity_assign_product, content, true);

            id = getIntent().getStringExtra("product_id");
            prod_name = getIntent().getStringExtra("product_name");

            post_data.put("vendor_id", vendorSessionManagement.getVendorid());
            post_data.put("hashkey", vendorSessionManagement.getHahkey());

            post_data.put("id", id);

            MultiVendor_header_txt = (Button) findViewById(R.id.MultiVendor_header_txt);
            edt_sku = (EditText) findViewById(R.id.edt_sku);
            edt_price = (EditText) findViewById(R.id.edt_price);
            edt_stock = (EditText) findViewById(R.id.edt_stock);
            edt_stock_availability = (Spinner) findViewById(R.id.edt_stock_availability);
            save = (Button) findViewById(R.id.save);
            save.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AppConstant.lockButton(v);
                    try {
                        if (edt_sku.getText().toString().isEmpty()) {
                            edt_sku.setError("Please enter SKU");
                        } else if (edt_price.getText().toString().isEmpty()) {
                            edt_price.setError("Please enter price");
                        } else if (edt_stock.getText().toString().isEmpty()) {
                            edt_stock.setError("Please enter stock value");
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
                            post_data.put("product", object1.toString());
                            Log.i("selectnsellpostdata", " post data-> " + post_data.toString());
                            Ced_MultiVendor_ClientRequestResponse response = new Ced_MultiVendor_ClientRequestResponse(new AsyncResponse() {
                                @Override
                                public void processFinish(Object output) throws JSONException {
                                    JSONObject object3 = new JSONObject(output.toString());
                                    if (object3.getJSONObject("data").getBoolean("success")) {
                                        Intent intent = new Intent(Ced_MultiVendor_Sell_This_Product.this, Add_Product_list.class);
                                        //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP );
                                        // intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                                        startActivity(intent);
                                        finish();
                                    } else {
                                        Toast.makeText(getApplicationContext(), object3.getJSONObject("data").getString("message"), Toast.LENGTH_LONG).show();
                                    }

                                }
                            }, Ced_MultiVendor_Sell_This_Product.this, "POST", post_data);
                            response.execute(save_url);
                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            });
            MultiVendor_header_txt.setText("ASSIGN PRODUCT - " + prod_name);


        } else {
            Intent nointernet = new Intent(Ced_MultiVendor_Sell_This_Product.this, Ced_MultiVendor_NoInternetconnection.class);
            nointernet.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            nointernet.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(nointernet);
            overridePendingTransition(R.anim.ced_multivendor_slide_in, R.anim.ced_multivendor_slide_out);
        }

    }

    @Override
    protected void onResume() {
        Ced_MultiVendor_ConnectionDetector connectionDetector = new Ced_MultiVendor_ConnectionDetector(Ced_MultiVendor_Sell_This_Product.this);
        if (connectionDetector.isConnectingToInternet()) {
            setname(vendorSessionManagement.getvendorname());
            setprofilepic(vendorSessionManagement.getvendorpic());
            changetitle("Assign Product");
            //     invalidateOptionsMenu();
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
