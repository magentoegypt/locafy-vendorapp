package magentoegypt.locafy.manage_attribute;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import magentoegypt.locafy.base_app.Ced_MultiVendor_VendorSplash;
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
 * Created by developer on 21/11/16.
 */
public class Ced_MultiVendor_VendorProductAttributeSet extends Ced_MultiVendor_NavigationActivity {
    static final String KEY_SET_ID = "set_id";
    static final String KEY_SET_CODE = "set_code";
    private final int visible = 1;
    Ced_MultiVendor_ConnectionDetector connectionDetector;
    Ced_MultiVendor_VendorFunctionalityList functionalityList;
    String out, url, message, hashkey, vendor_id;
    int current = 1;
    HashMap<String, String> postdata;
    boolean load = true;
    ListView product_attribute_list;
    LinearLayout filtersection;
    TextView text_msg, Count_total;
    FloatingActionButton fab;
    String datafilterjson = "";
    JSONObject jsonObject;
    JSONArray attribute;
    String set_id, set_code;
    Dialog listDialog;
    ArrayList<HashMap<String, String>> prod_attr_info;
    Ced_MultiVendor_VendorAttributeSet_Adapter ced_multiVendor_attributeSet_adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        connectionDetector = new Ced_MultiVendor_ConnectionDetector(getApplicationContext());
        functionalityList = new Ced_MultiVendor_VendorFunctionalityList(getApplicationContext());
        postdata = new HashMap<>();
        prod_attr_info = new ArrayList<>();
        url = session.getBase_Url() + "vproductattributeapi/set/info";
        if (connectionDetector.isConnectingToInternet()) {
            ViewGroup content = findViewById(R.id.MultiVendor_frame_container);
            getLayoutInflater().inflate(R.layout.ced_multivendor_activity_vendor_product_attribute_set, content, true);
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
                    Intent create_attribute = new Intent(Ced_MultiVendor_VendorProductAttributeSet.this, Ced_MultiVendor_CreateProductAttributeSet.class);
                    startActivity(create_attribute);
                    overridePendingTransition(R.anim.ced_multivendor_slide_in, R.anim.ced_multivendor_slide_out);
                }
            });
            filtersection.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showfilter();
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

    private void showfilter() {
        try {
            listDialog = new Dialog(this, R.style.PauseDialog);
            listDialog.setTitle(getResources().getString(R.string.alert_name));
            LayoutInflater li = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View v = li.inflate(R.layout.ced_multivendor_attribute_set_filter, null, false);
            final EditText edt_productname = v.findViewById(R.id.MultiVendor_edt_productname);
            final TextView setfilter = v.findViewById(R.id.MultiVendor_setfilter);
            final TextView unsetfilter = v.findViewById(R.id.MultiVendor_unsetfilter);
            if (!(datafilterjson.isEmpty())) {
                JSONObject object = new JSONObject(datafilterjson);
                edt_productname.setText(object.getString("attribute_set_code"));
            }
            unsetfilter.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    edt_productname.setText("");
                    datafilterjson = "";
                    final JSONObject mainfilter = new JSONObject();
                    try {
                        listDialog.dismiss();
                        mainfilter.put("attribute_set_code", edt_productname.getText().toString());
                        Intent intent = new Intent(getApplicationContext(), Ced_MultiVendor_VendorProductAttributeSet.class);
                        intent.putExtra("filter", mainfilter.toString());
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);

                    } catch (JSONException e) {
                        Intent main = new Intent(Ced_MultiVendor_VendorProductAttributeSet.this, Ced_MultiVendor_VendorSplash.class);
                        main.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        main.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(main);
                        overridePendingTransition(R.anim.ced_multivendor_slide_in, R.anim.ced_multivendor_slide_out);
                    }
                }
            });
            setfilter.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final JSONObject mainfilter = new JSONObject();
                    try {
                        listDialog.dismiss();
                        mainfilter.put("attribute_set_code", edt_productname.getText().toString());

                        Intent intent = new Intent(Ced_MultiVendor_VendorProductAttributeSet.this, Ced_MultiVendor_VendorProductAttributeSet.class);
                        intent.putExtra("filter", mainfilter.toString());
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
            listDialog.setContentView(v);
            listDialog.setCancelable(true);
            listDialog.show();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void request() {
        Ced_MultiVendor_ClientRequestResponse response = new Ced_MultiVendor_ClientRequestResponse(new AsyncResponse() {
            @Override
            public void processFinish(Object output) throws JSONException {
                if (functionalityList.getExtensionAddon()) {
                    out = output.toString();
                    if (out.contains("NO_ORDER")) {
                        text_msg.setText(R.string.NoOrdersToList);
                        text_msg.setVisibility(View.VISIBLE);
                        //Toast.makeText(getApplicationContext(), R.string.NoOrdersToList, Toast.LENGTH_LONG).show();
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
        }, Ced_MultiVendor_VendorProductAttributeSet.this, "POST", postdata);
        response.execute(url + "/page/" + current);
    }

    private void prod_attr_listdata() throws JSONException {
        jsonObject = new JSONObject(out);
        if (jsonObject.getJSONObject("data").getBoolean("success")) {
            if (jsonObject.getJSONObject("data").has("attribute")) {
                if (jsonObject.getJSONObject("data").getJSONArray("attribute").length() > 0) {
                    attribute = jsonObject.getJSONObject("data").getJSONArray("attribute");
                    for (int i = 0; i < attribute.length(); i++) {
                        JSONObject c = null;
                        c = attribute.getJSONObject(i);
                        set_id = c.getString(KEY_SET_ID);
                        set_code = c.getString(KEY_SET_CODE);
                        HashMap<String, String> sets = new HashMap<>();
                        sets.put("set_code", set_code);
                        sets.put("set_id", set_id);
                        prod_attr_info.add(sets);
                    }
                    ced_multiVendor_attributeSet_adapter = new Ced_MultiVendor_VendorAttributeSet_Adapter(Ced_MultiVendor_VendorProductAttributeSet.this, prod_attr_info);
                    product_attribute_list.setAdapter(ced_multiVendor_attributeSet_adapter);
                    product_attribute_list.setDivider(new ColorDrawable(getResources().getColor(R.color.transparent)));
                    product_attribute_list.setDividerHeight(0);
               /*     product_attribute_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            TextView attribute_setid= (TextView) view.findViewById(R.id.attribute_setid);
                            TextView attribute_setname= (TextView) view.findViewById(R.id.attribute_setname);
                            Log.i("asasa",attribute_setid.getText().toString());
                            Intent intent= new Intent(Ced_MultiVendor_VendorProductAttributeSet.this,Ced_MultiVendor_VendorProductAttributeSet_Edit.class);
                            intent.putExtra("attribute_setid",attribute_setid.getText().toString());
                            intent.putExtra("attribute_setname",attribute_setname.getText().toString());
                            startActivity(intent);
                        }
                    });*/
                } else {
                    text_msg.setText(R.string.NoAttributeSetAvailable);
                    text_msg.setVisibility(View.VISIBLE);
                    /*Toast.makeText(getApplicationContext(), R.string.NoAttributeSetAvailable, Toast.LENGTH_LONG).show();*/
                }
            }

        }else if (jsonObject.getJSONObject("data").has("message")) {
            text_msg.setText(jsonObject.getJSONObject("data").getString("message"));
            text_msg.setVisibility(View.VISIBLE);
        }

    }

    @Override
    protected void onResume() {
        Ced_MultiVendor_ConnectionDetector connectionDetector = new Ced_MultiVendor_ConnectionDetector(getApplicationContext());
        if (connectionDetector.isConnectingToInternet()) {
            setname(session.getvendorname());
            setprofilepic(session.getvendorpic());
            changetitle("Vendor Product Attribute");
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
