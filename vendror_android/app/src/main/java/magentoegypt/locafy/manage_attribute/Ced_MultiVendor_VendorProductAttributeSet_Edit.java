package magentoegypt.locafy.manage_attribute;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import magentoegypt.locafy.base_app.Ced_MultiVendor_VendorFunctionalityList;
import magentoegypt.locafy.navigation_drawer.Activity.Ced_MultiVendor_NavigationActivity;
import magentoegypt.locafy.api_request_response_section.network_error_handling.Ced_MultiVendor_ConnectionDetector;
import magentoegypt.locafy.api_request_response_section.network_error_handling.Ced_MultiVendor_NoInternetconnection;
import magentoegypt.locafy.api_request_response_section.AsyncResponse;
import magentoegypt.locafy.api_request_response_section.Ced_MultiVendor_ClientRequestResponse;
import magentoegypt.locafy.vendor_session.Ced_MultiVendor_VendorSessionManagement;
import magentoegypt.locafy.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by developer on 23/11/16.
 */
public class Ced_MultiVendor_VendorProductAttributeSet_Edit extends Ced_MultiVendor_NavigationActivity {
    static final String KEY_SET_ID = "set_id";
    static final String KEY_SET_CODE = "set_code";
    Ced_MultiVendor_ConnectionDetector connectionDetector;
    Ced_MultiVendor_VendorFunctionalityList functionalityList;
    String out, url, message, hashkey, vendor_id;
    HashMap<String, String> postdata;
    ArrayList<HashMap<String, String>> prod_attr_info;
    Ced_MultiVendor_VendorAttributeSet_Adapter ced_multiVendor_attributeSet_adapter;
    EditText MutiVendor_Set_Name, MutiVendor_Set_BasedOn;
    Button MutiVendor_Save;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        connectionDetector = new Ced_MultiVendor_ConnectionDetector(getApplicationContext());
        functionalityList = new Ced_MultiVendor_VendorFunctionalityList(getApplicationContext());
        postdata = new HashMap<>();
        prod_attr_info = new ArrayList<>();
        url = session.getBase_Url() + "vproductattributeapi/set/update";
        if (connectionDetector.isConnectingToInternet()) {
            ViewGroup content = (ViewGroup) findViewById(R.id.MultiVendor_frame_container);
            getLayoutInflater().inflate(R.layout.product_attribute_set_edit, content, true);
            if (session.getvendorname() != null) {
                setname(session.getvendorname());
                setprofilepic(session.getvendorpic());
                changetitle("Vendor Product Attribute");
            }
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.backbutton);
            MutiVendor_Set_Name = (EditText) findViewById(R.id.MutiVendor_Set_Name);
            MutiVendor_Set_BasedOn = (EditText) findViewById(R.id.MutiVendor_Set_BasedOn);
            MutiVendor_Save = (Button) findViewById(R.id.MutiVendor_Save);

            MutiVendor_Set_BasedOn.setVisibility(View.GONE);

            if (getIntent().hasExtra("attribute_setid")) {
                postdata.put("set_id", getIntent().getStringExtra("attribute_setid"));

            }
            if (getIntent().hasExtra("attribute_setname")) {
                MutiVendor_Set_Name.setText(getIntent().getStringExtra("attribute_setname"));
            }

            final HashMap<String, String> user = session.getUserDetails();
            hashkey = user.get(Ced_MultiVendor_VendorSessionManagement.Key_HAsh);
            vendor_id = session.getVendorid();
            postdata.put("vendor_id", vendor_id);
            postdata.put("hashkey", hashkey);
            MutiVendor_Save.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    postdata.put("attribute_set_name", MutiVendor_Set_Name.getText().toString());
                    request();

                }
            });


        } else {
            Intent nointernet = new Intent(getApplicationContext(), Ced_MultiVendor_NoInternetconnection.class);
            nointernet.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            nointernet.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(nointernet);
            overridePendingTransition(R.anim.ced_multivendor_slide_in, R.anim.ced_multivendor_slide_out);
        }

    }

    private void request() {
        Ced_MultiVendor_ClientRequestResponse ced_multiVendor_clientRequestResponse = new Ced_MultiVendor_ClientRequestResponse(new AsyncResponse() {
            @Override
            public void processFinish(Object output) throws JSONException {
                JSONObject object = new JSONObject(output.toString());
                if (object.getJSONObject("data").getBoolean("success")) {
                    Toast.makeText(getApplicationContext(), object.getJSONObject("data").getString("message"), Toast.LENGTH_LONG).show();
                    Intent V_P_Atribiute_set = new Intent(Ced_MultiVendor_VendorProductAttributeSet_Edit.this, Ced_MultiVendor_VendorProductAttributeSet.class);
                    V_P_Atribiute_set.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    startActivity(V_P_Atribiute_set);
                    overridePendingTransition(R.anim.ced_multivendor_slide_in, R.anim.ced_multivendor_slide_out);

                } else {
                    Toast.makeText(getApplicationContext(), object.getJSONObject("data").getString("message"), Toast.LENGTH_LONG).show();
                }

            }
        }, Ced_MultiVendor_VendorProductAttributeSet_Edit.this, "POST", postdata);
        ced_multiVendor_clientRequestResponse.execute(url);
    }

    @Override
    protected void onResume() {
        Ced_MultiVendor_ConnectionDetector connectionDetector = new Ced_MultiVendor_ConnectionDetector(getApplicationContext());
        if (connectionDetector.isConnectingToInternet()) {
            setname(session.getvendorname());
            setprofilepic(session.getvendorpic());
            changetitle("Vendor Product Attribute");
            //      invalidateOptionsMenu();
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
