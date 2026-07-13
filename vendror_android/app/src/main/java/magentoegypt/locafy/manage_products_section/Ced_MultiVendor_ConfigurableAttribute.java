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
import android.content.res.Resources;
import android.os.Bundle;
import androidx.cardview.widget.CardView;
import android.util.Log;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.Toast;

import magentoegypt.locafy.R;
import magentoegypt.locafy.base_app.Ced_MultiVendor_FontSetting;
import magentoegypt.locafy_constant.Ced_MultiVendor_GlobalVariables;
import magentoegypt.locafy.base_app.Ced_MultiVendor_VendorFunctionalityList;
import magentoegypt.locafy.navigation_drawer.Activity.Ced_MultiVendor_NavigationActivity;
import magentoegypt.locafy.api_request_response_section.network_error_handling.Ced_MultiVendor_ConnectionDetector;
import magentoegypt.locafy.api_request_response_section.network_error_handling.Ced_MultiVendor_NoInternetconnection;
import magentoegypt.locafy.vendor_session.Ced_MultiVendor_VendorSessionManagement;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.HashSet;

public class Ced_MultiVendor_ConfigurableAttribute extends Ced_MultiVendor_NavigationActivity {
    Ced_MultiVendor_FontSetting fontSetting;
    Ced_MultiVendor_ConnectionDetector connectionDetector;
    Ced_MultiVendor_VendorFunctionalityList functionalityList;
    Ced_MultiVendor_VendorSessionManagement vendorSessionManagement;
    String dataforproductcreation;
    String type;
    String attribute_set;
    CardView cardcontinue;
    LinearLayout configoptions;
    JSONObject object;
    HashMap<String, String> label_attributecode;
    HashMap<String, String> attributecode_value;

    String category;
    String websites;
    String storeViews;
    String stores;
    String last_cat;


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
        label_attributecode = new HashMap<String, String>();
        attributecode_value = new HashMap<String, String>();
        if (getIntent().hasExtra("last_cat_id")) {
            last_cat = getIntent().getStringExtra("last_cat_id");
        }

        if (getIntent().hasExtra("category")) {
            category = getIntent().getStringExtra("category");
        }
        if (getIntent().hasExtra("websites")) {
            websites = getIntent().getStringExtra("websites");
        }
        if (getIntent().hasExtra("stores")) {
            stores = getIntent().getStringExtra("stores");
        }
        if (getIntent().hasExtra("storeViews")) {
            storeViews = getIntent().getStringExtra("storeViews");
        }

        if (connectionDetector.isConnectingToInternet()) {
            ViewGroup content = (ViewGroup) findViewById(R.id.MultiVendor_frame_container);
            getLayoutInflater().inflate(R.layout.ced_multivendor_configurable_attribute, content, true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.backbutton);
            cardcontinue = (CardView) findViewById(R.id.MultiVendor_cardcontinue);
            configoptions = (LinearLayout) findViewById(R.id.MultiVendor_configoptions);
            setname(vendorSessionManagement.getvendorname());
            setprofilepic(vendorSessionManagement.getvendorpic());
            setTitle(getString(R.string.Addproduct));
            Intent intent = getIntent();
            dataforproductcreation = intent.getStringExtra("dataforproductcreation");
            type = intent.getStringExtra("type");
            attribute_set = intent.getStringExtra("attribute_set");
            final HashSet<String> ids = (HashSet<String>) intent.getSerializableExtra("cat_data");
            cardcontinue.setOnClickListener(v -> {
                if (Ced_MultiVendor_GlobalVariables.config_attribute_value.length() > 0) {
                    Intent intent1 = new Intent(Ced_MultiVendor_ConfigurableAttribute.this, ProductCreationNew.class);
                    intent1.putExtra("dataforproductcreation", dataforproductcreation);
                    intent1.putExtra("type", type);
                    intent1.putExtra("last_cat_id", last_cat);
                    intent1.putExtra("attribute_set", attribute_set);
                    intent1.putExtra("cat_data", ids);
                    intent1.putExtra("category", category);
                    intent1.putExtra("websites", websites);
                    intent1.putExtra("stores", stores);
                    intent1.putExtra("storeViews", storeViews);
                    startActivity(intent1);
                    overridePendingTransition(R.anim.ced_multivendor_slide_in, R.anim.ced_multivendor_slide_out);
                }
                else {
                    Toast.makeText(getApplicationContext(), "Please Select Attribute", Toast.LENGTH_LONG).show();

                }
            });
            try {
                object = new JSONObject(dataforproductcreation);
                JSONArray config = object.getJSONObject("data").getJSONArray("config");
                if (config.length() > 0) {
                    int id = Resources.getSystem().getIdentifier("btn_check_holo_light", "drawable", "android");
                    for (int i = 0; i < config.length(); i++) {
                        JSONObject jsonObject = config.getJSONObject(i);
                        final CheckBox checkBox = new CheckBox(getApplicationContext());
                        checkBox.setText(jsonObject.getString("label"));
                        checkBox.setTextColor(getResources().getColor(R.color.black));
                        checkBox.setButtonDrawable(id);
                        checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
                            if (isChecked) {
                                try {
                                    Ced_MultiVendor_GlobalVariables.config_attribute_value.put(label_attributecode.get(checkBox.getText().toString()), attributecode_value.get(label_attributecode.get(checkBox.getText().toString())));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            } else {
                                Ced_MultiVendor_GlobalVariables.config_attribute_value.remove(label_attributecode.get(checkBox.getText().toString()));
                            }
                            if (getResources().getString(R.string.Enable_Log).equals("yes")) {
                                Log.i("config_attribute_value", "" + Ced_MultiVendor_GlobalVariables.config_attribute_value);
                            }
                        });
                        label_attributecode.put(jsonObject.getString("label"), jsonObject.getString("attribute_code"));
                        attributecode_value.put(jsonObject.getString("attribute_code"), jsonObject.getString("value"));
                        configoptions.addView(checkBox);

                    }

                } else {
                    Toast.makeText(getApplicationContext(), "This attribute set does not have attributes which we can use for configurable product", Toast.LENGTH_LONG).show();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
        else {
            Intent nointernet = new Intent(Ced_MultiVendor_ConfigurableAttribute.this, Ced_MultiVendor_NoInternetconnection.class);
            nointernet.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            nointernet.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(nointernet);
            overridePendingTransition(R.anim.ced_multivendor_slide_in, R.anim.ced_multivendor_slide_out);
        }


    }

    @Override
    protected void onResume() {
        Ced_MultiVendor_ConnectionDetector connectionDetector = new Ced_MultiVendor_ConnectionDetector(Ced_MultiVendor_ConfigurableAttribute.this);
        if (connectionDetector.isConnectingToInternet()) {
            setname(vendorSessionManagement.getvendorname());
            setprofilepic(vendorSessionManagement.getvendorpic());
            changetitle("Add Product");
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
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
