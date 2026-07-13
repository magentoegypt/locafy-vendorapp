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

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import magentoegypt.locafy.base_app.Ced_MultiVendor_VendorFunctionalityList;
import magentoegypt.locafy.navigation_drawer.Activity.Ced_MultiVendor_NavigationActivity;
import magentoegypt.locafy.api_request_response_section.network_error_handling.Ced_MultiVendor_ConnectionDetector;
import magentoegypt.locafy.api_request_response_section.network_error_handling.Ced_MultiVendor_NoInternetconnection;
import magentoegypt.locafy.api_request_response_section.network_error_handling.Ced_MultiVendor_UnAuthourizedRequestError;
import magentoegypt.locafy.api_request_response_section.AsyncResponse;
import magentoegypt.locafy.api_request_response_section.Ced_MultiVendor_ClientRequestResponse;
import magentoegypt.locafy.vendor_session.Ced_MultiVendor_VendorSessionManagement;
import magentoegypt.locafy.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by developer on 2/6/16.
 */
public class Ced_MultiVendor_VendorProductAttribute extends Ced_MultiVendor_NavigationActivity {
    static final String KEY_ATTRIBUTE_CODE = "attribute_code";
    static final String KEY_ATTRIBUTE_LABEL = "frontend_label";
    static final String KEY_REQUIRED = "is_required";
    static final String KEY_VISIBLE = "is_visible_on_front";
    static final String KEY_SEARCHABLE = "is_searchable";
    static final String KEY_COMPARABLE = "is_comparable";
    static final String KEY_USE_IN_LAYERED = "is_filterable";
    static final String KEY_SCOPE = "is_global";
    static final String KEY_SYSTEM = "is_user_defined";
    static final String KEY_ATTRIBUTE_ID = "attribute_id";
    private static LayoutInflater inflater = null;
    Ced_MultiVendor_ConnectionDetector connectionDetector;
    Ced_MultiVendor_VendorFunctionalityList functionalityList;
    ListView product_attribute_list;
    String out, url, message, hashkey, vendor_id;
    HashMap<String, String> postdata;
    int current = 1;
    boolean load = true;
    JSONObject jsonObject;
    JSONArray prod_attrdetail;
    String attribute_code, frontend_label, is_required, is_visible,
            is_searchable, is_comparable, layered, scope, system, attribute_id;
    ArrayList<HashMap<String, String>> prod_attr_info;
    Ced_MultiVendor_ProductAttributeAdapter productAttributeAdapter;
    LinearLayout filtersection;
    Dialog listDialog;
    String datafilterjson = "";
    TextView txt_attribute_code, txt_attribute_label, txt_required, txt_system,
            txt_visible, txt_scope, txt_searchable, txt_layered_navigation,
            txt_Comparable;
    EditText edt_attribute_code, edt_attribute_label;
    Spinner edt_required, edt_system, edt_visible, edt_scope, edt_searchable,
            edt_layered_navigation, edt_comparable;
    Button setfilter, unsetfilter;
    JSONObject data, req;
    FloatingActionButton fab;
    TextView text_msg, Count_total;
    private int visible = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        connectionDetector = new Ced_MultiVendor_ConnectionDetector(getApplicationContext());
        functionalityList = new Ced_MultiVendor_VendorFunctionalityList(getApplicationContext());
        postdata = new HashMap<>();
        prod_attr_info = new ArrayList<>();
        data = new JSONObject();
        req = new JSONObject();
        url = session.getBase_Url() + "vproductattributeapi/index/getVProductAttribute";
        if (connectionDetector.isConnectingToInternet()) {
            ViewGroup content = findViewById(R.id.MultiVendor_frame_container);
            getLayoutInflater().inflate(R.layout.ced_multivendor_activity_vendor_product_attribute, content, true);
            if (session.getvendorname() != null) {
                setname(session.getvendorname());
                setprofilepic(session.getvendorpic());
                changetitle("Vendor Product Attribute");
            }
            product_attribute_list = findViewById(R.id.MultiVendor_product_attribute_list);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.backbutton);
            filtersection = findViewById(R.id.MultiVendor_filtersection);
            text_msg = findViewById(R.id.MultiVendor_text_msg);
            Count_total = findViewById(R.id.MultiVendor_Count_total);
            fab = findViewById(R.id.MultiVendor_fab);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent create_attribute = new Intent(Ced_MultiVendor_VendorProductAttribute.this, Ced_MultiVendor_CreateProductAttribute.class);
                    startActivity(create_attribute);
                    overridePendingTransition(R.anim.ced_multivendor_slide_in, R.anim.ced_multivendor_slide_out);
                }
            });
            filtersection.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        showfilter();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
            final HashMap<String, String> user = session.getUserDetails();
            hashkey = user.get(Ced_MultiVendor_VendorSessionManagement.Key_HAsh);
            vendor_id = session.getVendorid();
            postdata.put("vendor_id", vendor_id);
            postdata.put("hashkey", hashkey);
            if (getIntent().getStringExtra("filter") != null) {
                datafilterjson = getIntent().getStringExtra("filter");
                postdata.put("filter", datafilterjson);

            }

            request();
        } else {
            Intent nointernet = new Intent(getApplicationContext(), Ced_MultiVendor_NoInternetconnection.class);
            nointernet.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            nointernet.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(nointernet);
            overridePendingTransition(R.anim.ced_multivendor_slide_in, R.anim.ced_multivendor_slide_out);
        }
    }

    private void showfilter() throws JSONException {
        listDialog = new Dialog(this, R.style.PauseDialog);
        listDialog.setTitle(getResources().getString(R.string.alert_name));
        listDialog.setCanceledOnTouchOutside(true);
        inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View vi = inflater.inflate(R.layout.ced_multivendor_prod_attr_filter, null);

        txt_attribute_code = vi.findViewById(R.id.MultiVendor_txt_attribute_code);
        txt_attribute_label = vi.findViewById(R.id.MultiVendor_txt_attribute_label);
        txt_required = vi.findViewById(R.id.MultiVendor_txt_required);
        txt_system = vi.findViewById(R.id.MultiVendor_txt_system);
        txt_visible = vi.findViewById(R.id.MultiVendor_txt_visible);
        txt_scope = vi.findViewById(R.id.MultiVendor_txt_scope);
        txt_searchable = vi.findViewById(R.id.MultiVendor_txt_searchable);
        txt_layered_navigation = vi.findViewById(R.id.MultiVendor_txt_layered_navigation);
        txt_Comparable = vi.findViewById(R.id.MultiVendor_txt_Comparable);

        edt_attribute_code = vi.findViewById(R.id.MultiVendor_edt_attribute_code);
        edt_attribute_label = vi.findViewById(R.id.MultiVendor_edt_attribute_label);
        edt_required = vi.findViewById(R.id.MultiVendor_edt_required);
        edt_system = vi.findViewById(R.id.MultiVendor_edt_system);
        edt_visible = vi.findViewById(R.id.MultiVendor_edt_visible);
        edt_scope = vi.findViewById(R.id.MultiVendor_edt_scope);
        edt_searchable = vi.findViewById(R.id.MultiVendor_edt_searchable);
        edt_layered_navigation = vi.findViewById(R.id.MultiVendor_edt_layered_navigation);
        edt_comparable = vi.findViewById(R.id.MultiVendor_edt_comparable);
        setfilter = vi.findViewById(R.id.MultiVendor_setfilter);
        unsetfilter = vi.findViewById(R.id.MultiVendor_unsetfilter);
        if (!(datafilterjson.isEmpty())) {
            JSONObject object = null;
            object = new JSONObject(datafilterjson);
            edt_attribute_code.setText(object.getString("attribute_code"));
            edt_attribute_code.setSelection(edt_attribute_code.getText().toString().length());
            edt_attribute_label.setText(object.getString("frontend_label"));
            if (object.getString("is_required").equals("Yes")) {
                edt_required.setSelection(1);
            } else if (object.getString("is_required").equals("No")) {
                edt_required.setSelection(2);
            } else {
                edt_required.setSelection(0);
            }
            if (object.getString("is_user_defined").equals("Yes")) {
                edt_system.setSelection(1);
            } else if (object.getString("is_user_defined").equals("No")) {
                edt_system.setSelection(2);
            } else {
                edt_system.setSelection(0);
            }
            if (object.getString("is_visible").equals("Yes")) {
                edt_visible.setSelection(1);
            } else if (object.getString("is_visible").equals("No")) {
                edt_visible.setSelection(2);
            } else {
                edt_visible.setSelection(0);
            }
            if (object.getString("is_global").equals("Store View")) {
                edt_scope.setSelection(1);
            } else if (object.getString("is_global").equals("Website")) {
                edt_scope.setSelection(2);
            } else if (object.getString("is_global").equals("Global")) {
                edt_scope.setSelection(3);

            } else {
                edt_scope.setSelection(0);
            }
            if (object.getString("is_searchable").equals("Yes")) {
                edt_searchable.setSelection(1);
            } else if (object.getString("is_searchable").equals("No")) {
                edt_searchable.setSelection(2);
            } else {
                edt_searchable.setSelection(0);
            }
            if (object.getString("is_filterable").equals("Filterable(with results)")) {
                edt_layered_navigation.setSelection(1);
            } else if (object.getString("is_filterable").equals("Filterable(no results)")) {
                edt_layered_navigation.setSelection(2);
            } else if (object.getString("is_filterable").equals("No")) {
                edt_layered_navigation.setSelection(3);
            } else {
                edt_layered_navigation.setSelection(0);
            }
            if (object.getString("is_comparable").equals("Yes")) {
                edt_comparable.setSelection(1);
            } else if (object.getString("is_comparable").equals("No")) {
                edt_comparable.setSelection(2);
            } else {
                edt_comparable.setSelection(0);
            }


        }
        setfilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edt_attribute_code.getText().toString().isEmpty() &&
                        edt_attribute_label.getText().toString().isEmpty() &&
                        edt_required.getSelectedItem().toString().isEmpty() &&
                        edt_system.getSelectedItem().toString().isEmpty() &&
                        edt_visible.getSelectedItem().toString().isEmpty() &&
                        edt_scope.getSelectedItem().toString().isEmpty() &&
                        edt_searchable.getSelectedItem().toString().isEmpty() &&
                        edt_layered_navigation.getSelectedItem().toString().isEmpty() &&
                        edt_comparable.getSelectedItem().toString().isEmpty()
                ) {
                    Toast.makeText(Ced_MultiVendor_VendorProductAttribute.this, "Please fill the filter", Toast.LENGTH_SHORT).show();
                } else {
                    try {

                        req.put("attribute_code", edt_attribute_code.getText().toString());
                        req.put("frontend_label", edt_attribute_label.getText().toString());
                        if (edt_required.getSelectedItem().toString().equals("Select")) {
                            req.put("is_required", "");
                        } else {
                            req.put("is_required", edt_required.getSelectedItem().toString());
                        }
                        if (edt_system.getSelectedItem().toString().equals("Select")) {
                            req.put("is_user_defined", "");
                        } else {
                            req.put("is_user_defined", edt_system.getSelectedItem().toString());
                        }
                        if (edt_visible.getSelectedItem().toString().equals("Select")) {
                            req.put("is_visible", "");
                        } else {
                            req.put("is_visible", edt_visible.getSelectedItem().toString());
                        }
                        if (edt_scope.getSelectedItem().toString().equals("Select")) {
                            req.put("is_global", "");
                        } else {
                            req.put("is_global", edt_scope.getSelectedItem().toString());
                        }
                        if (edt_searchable.getSelectedItem().toString().equals("Select")) {
                            req.put("is_searchable", "");
                        } else {
                            req.put("is_searchable", edt_searchable.getSelectedItem().toString());
                        }
                        if (edt_layered_navigation.getSelectedItem().toString().equals("Select")) {
                            req.put("is_filterable", "");
                        } else {
                            req.put("is_filterable", edt_layered_navigation.getSelectedItem().toString());
                        }
                        if (edt_comparable.getSelectedItem().toString().equals("Select")) {
                            req.put("is_comparable", "");
                        } else {
                            req.put("is_comparable", edt_comparable.getSelectedItem().toString());
                        }
                        listDialog.dismiss();
                        if (getResources().getString(R.string.Enable_Log).equals("yes")) {
                            Log.i("data_json", req.toString());
                        }
                        Intent intent = new Intent(getApplicationContext(), Ced_MultiVendor_VendorProductAttribute.class);
                        intent.putExtra("filter", req.toString());
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        overridePendingTransition(R.anim.ced_multivendor_slide_in, R.anim.ced_multivendor_slide_out);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }
            }
        });
        unsetfilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postdata.remove("filter");
                datafilterjson = "";
                listDialog.dismiss();
                Intent intent = new Intent(getApplicationContext(), Ced_MultiVendor_VendorProductAttribute.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                overridePendingTransition(R.anim.ced_multivendor_slide_in, R.anim.ced_multivendor_slide_out);
            }
        });
        listDialog.setContentView(vi);
        listDialog.setCancelable(true);
        listDialog.show();

    }

    private void request() {
        Ced_MultiVendor_ClientRequestResponse response = new Ced_MultiVendor_ClientRequestResponse(new AsyncResponse() {
            @Override
            public void processFinish(Object output) throws JSONException {
                if (functionalityList.getExtensionAddon()) {
                    out = output.toString();
                    if (out.equals("NO_ORDER")) {

                        Toast.makeText(getApplicationContext(), R.string.NoOrdersToList, Toast.LENGTH_LONG).show();
                        //  Toast.makeText(getApplicationContext(),"You have no more orders to see",Toast.LENGTH_SHORT).show();
                    } else {
                        prod_attr_listdata();
                    }
                } else {
                    Intent intent = new Intent(getApplicationContext(), Ced_MultiVendor_UnAuthourizedRequestError.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    overridePendingTransition(R.anim.ced_multivendor_slide_in, R.anim.ced_multivendor_slide_out);
                }
            }
        }, Ced_MultiVendor_VendorProductAttribute.this, "POST", postdata);
        response.execute(url + "/page/" + current);
    }

    private void prod_attr_listdata() throws JSONException {
        jsonObject = new JSONObject(out);

        if (jsonObject.getJSONObject("data").getBoolean("success")) {
            prod_attrdetail = jsonObject.getJSONObject("data").getJSONArray("vendor_attributes");
            Count_total.setText(getString(R.string.you_have_txt) + " " + jsonObject.getJSONObject("data").getString("count")  + " " + getResources().getString(R.string.productattribute));
            for (int i = 0; i < prod_attrdetail.length(); i++) {
                JSONObject c = null;
                c = prod_attrdetail.getJSONObject(i);
                attribute_code = c.getString(KEY_ATTRIBUTE_CODE);
                frontend_label = c.getString(KEY_ATTRIBUTE_LABEL);
                is_required = c.getString(KEY_REQUIRED);
                is_visible = c.getString(KEY_VISIBLE);
                is_searchable = c.getString(KEY_SEARCHABLE);
                is_comparable = c.getString(KEY_COMPARABLE);
                layered = c.getString(KEY_USE_IN_LAYERED);
                scope = c.getString(KEY_SCOPE);
                system = c.getString(KEY_SYSTEM);
                attribute_id = c.getString(KEY_ATTRIBUTE_ID);
                HashMap<String, String> listing = new HashMap<String, String>();
                listing.put(KEY_ATTRIBUTE_CODE, attribute_code);
                listing.put(KEY_ATTRIBUTE_LABEL, frontend_label);
                listing.put(KEY_REQUIRED, is_required);
                listing.put(KEY_VISIBLE, is_visible);
                listing.put(KEY_SEARCHABLE, is_searchable);
                listing.put(KEY_COMPARABLE, is_comparable);
                listing.put(KEY_USE_IN_LAYERED, layered);
                listing.put(KEY_SCOPE, scope);
                listing.put(KEY_SYSTEM, system);
                listing.put(KEY_ATTRIBUTE_ID, attribute_id);
                prod_attr_info.add(listing);

            }
            productAttributeAdapter = new Ced_MultiVendor_ProductAttributeAdapter(Ced_MultiVendor_VendorProductAttribute.this, prod_attr_info);
            product_attribute_list.setAdapter(productAttributeAdapter);
            product_attribute_list.setDivider(new ColorDrawable(getResources().getColor(R.color.transparent)));
            product_attribute_list.setDividerHeight(0);

            product_attribute_list.setOnScrollListener(new AbsListView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(AbsListView view, int scrollState) {

                }

                @Override
                public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

                    if ((firstVisibleItem + visibleItemCount) != 0) {

                        if (((firstVisibleItem + visibleItemCount) == totalItemCount) && load) {
                            current = current + 1;
                            load = false;
                            visible = firstVisibleItem;
                            Ced_MultiVendor_ClientRequestResponse response = new Ced_MultiVendor_ClientRequestResponse(new AsyncResponse() {
                                @Override
                                public void processFinish(Object output) throws JSONException {
                                    out = output.toString();
                                    if (out.equals("NO_ORDER")) {

                                        load = false;
                                    } else {

                                        scrolldata();
                                    }
                                }
                            }, Ced_MultiVendor_VendorProductAttribute.this, "POST", postdata);
                            response.execute(url + "/page/" + current);
                        }
                    }
                }
            });

            product_attribute_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    Log.i("hello", "hello");
                    TextView attr_id = view.findViewById(R.id.MultiVendor_attr_id);
                    Intent edt_attr = new Intent(Ced_MultiVendor_VendorProductAttribute.this, Ced_MultiVendor_EditProductAttribute.class);
                    edt_attr.putExtra("attribute_code", attr_id.getText().toString());
                    startActivity(edt_attr);
                    overridePendingTransition(R.anim.ced_multivendor_slide_in, R.anim.ced_multivendor_slide_out);
                }
            });

        } else {

            message = jsonObject.getJSONObject("data").getString("message");
            if (!datafilterjson.isEmpty()) {
                fab.setVisibility(View.GONE);
            }
            text_msg.setText(message);
            text_msg.setVisibility(View.VISIBLE);
            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
        }
    }

    private void scrolldata() throws JSONException {
        jsonObject = new JSONObject(out);

        if (jsonObject.getJSONObject("data").getBoolean("success")) {
            prod_attrdetail = jsonObject.getJSONObject("data").getJSONArray("vendor_attributes");
            for (int i = 0; i < prod_attrdetail.length(); i++) {
                JSONObject c = null;
                c = prod_attrdetail.getJSONObject(i);
                attribute_code = c.getString(KEY_ATTRIBUTE_CODE);
                frontend_label = c.getString(KEY_ATTRIBUTE_LABEL);
                is_required = c.getString(KEY_REQUIRED);
                is_visible = c.getString(KEY_VISIBLE);
                is_searchable = c.getString(KEY_SEARCHABLE);
                is_comparable = c.getString(KEY_COMPARABLE);
                layered = c.getString(KEY_USE_IN_LAYERED);
                scope = c.getString(KEY_SCOPE);
                system = c.getString(KEY_SYSTEM);
                attribute_id = c.getString(KEY_ATTRIBUTE_ID);
                HashMap<String, String> listing = new HashMap<String, String>();
                listing.put(KEY_ATTRIBUTE_CODE, attribute_code);
                listing.put(KEY_ATTRIBUTE_LABEL, frontend_label);
                listing.put(KEY_REQUIRED, is_required);
                listing.put(KEY_VISIBLE, is_visible);
                listing.put(KEY_SEARCHABLE, is_searchable);
                listing.put(KEY_COMPARABLE, is_comparable);
                listing.put(KEY_USE_IN_LAYERED, layered);
                listing.put(KEY_SCOPE, scope);
                listing.put(KEY_SYSTEM, system);
                listing.put(KEY_ATTRIBUTE_ID, attribute_id);
                prod_attr_info.add(listing);

            }
            productAttributeAdapter = new Ced_MultiVendor_ProductAttributeAdapter(Ced_MultiVendor_VendorProductAttribute.this, prod_attr_info);
            int cp = product_attribute_list.getFirstVisiblePosition();
            product_attribute_list.setAdapter(productAttributeAdapter);
            product_attribute_list.setDivider(new ColorDrawable(getResources().getColor(R.color.transparent)));
            product_attribute_list.setDividerHeight(0);
            product_attribute_list.setSelectionFromTop(cp + 1, 0);
            productAttributeAdapter.notifyDataSetChanged();
            load = true;
        } else {
            /*message = jsonObject.getJSONObject("data").getString("message");
            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();*/

        }

    }

    @Override
    protected void onResume() {
        Ced_MultiVendor_ConnectionDetector connectionDetector = new Ced_MultiVendor_ConnectionDetector(getApplicationContext());
        if (connectionDetector.isConnectingToInternet()) {
            setname(session.getvendorname());
            setprofilepic(session.getvendorpic());
            changetitle(getString(R.string.VendorProductAttribute));
            //    invalidateOptionsMenu();
            super.onResume();

        } else {
            Intent intent = new Intent(getApplicationContext(), Ced_MultiVendor_NoInternetconnection.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            overridePendingTransition(R.anim.ced_multivendor_slide_in, R.anim.ced_multivendor_slide_out);
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
