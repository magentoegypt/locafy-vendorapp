package magentoegypt.locafy.manage_attribute;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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
import java.util.List;

/**
 * Created by developer on 21/11/16.
 */
public class Ced_MultiVendor_CreateProductAttributeSet extends Ced_MultiVendor_NavigationActivity {
    Ced_MultiVendor_ConnectionDetector connectionDetector;
    Ced_MultiVendor_VendorFunctionalityList functionalityList;
    HashMap<String, String> postdata;
    String out, url, create_url, hashkey, vendor_id;
    List<String> valuelist;
    List<String> labellist;
    EditText MutiVendor_Set_Name, MutiVendor_Set_BasedOn;
    Button MutiVendor_Save;
    String value, label = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        connectionDetector = new Ced_MultiVendor_ConnectionDetector(getApplicationContext());
        functionalityList = new Ced_MultiVendor_VendorFunctionalityList(getApplicationContext());
        postdata = new HashMap<>();
        valuelist = new ArrayList<>();
        labellist = new ArrayList<>();
        url = session.getBase_Url() + "vproductattributeapi/set/GetParentSet";
        create_url = session.getBase_Url() + "vproductattributeapi/set/create";
        if (connectionDetector.isConnectingToInternet()) {
            ViewGroup content = findViewById(R.id.MultiVendor_frame_container);
            getLayoutInflater().inflate(R.layout.ced_multivendor_activity_vendor_product_attribute_set_create, content, true);
            if (session.getvendorname() != null) {
                setname(session.getvendorname());
                setprofilepic(session.getvendorpic());
                changetitle("Vendor Product Attribute");
            }
            final HashMap<String, String> user = session.getUserDetails();
            hashkey = user.get(Ced_MultiVendor_VendorSessionManagement.Key_HAsh);
            vendor_id = session.getVendorid();
            postdata.put("vendor_id", vendor_id);
            postdata.put("hashkey", hashkey);
            getDropdown();
            MutiVendor_Set_Name = findViewById(R.id.MutiVendor_Set_Name);
            MutiVendor_Set_BasedOn = findViewById(R.id.MutiVendor_Set_BasedOn);
            MutiVendor_Save = findViewById(R.id.MutiVendor_Save);

            MutiVendor_Set_BasedOn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final CharSequence[] arrayOfInt = valuelist.toArray(new CharSequence[valuelist.size()]);
                    final CharSequence[] arrayOfInt2 = labellist.toArray(new CharSequence[labellist.size()]);
                    Dialog levelDialog1 = new Dialog(Ced_MultiVendor_CreateProductAttributeSet.this);
                    final AlertDialog.Builder builder = new AlertDialog.Builder(Ced_MultiVendor_CreateProductAttributeSet.this);
                    builder.setTitle(Html.fromHtml("<font color='#000000'>Based On:</font>"));
                    builder.setSingleChoiceItems(arrayOfInt2, -1, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int postion) {
                                    value = (String) arrayOfInt[postion];
                                    label = (String) arrayOfInt2[postion];
                                    MutiVendor_Set_BasedOn.setText(label);
                                    postdata.put("skeleton_set", value);
                                    dialog.dismiss();
                                }


                            }
                    );
                    levelDialog1 = builder.create();
                    levelDialog1.show();
                }
            });
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
                    Intent V_P_Atribiute_set = new Intent(Ced_MultiVendor_CreateProductAttributeSet.this, Ced_MultiVendor_VendorProductAttributeSet.class);
                    V_P_Atribiute_set.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    V_P_Atribiute_set.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(V_P_Atribiute_set);
                    overridePendingTransition(R.anim.ced_multivendor_slide_in, R.anim.ced_multivendor_slide_out);

                } else {
                    Toast.makeText(getApplicationContext(), object.getJSONObject("data").getString("message"), Toast.LENGTH_LONG).show();
                }

            }
        }, Ced_MultiVendor_CreateProductAttributeSet.this, "POST", postdata);
        ced_multiVendor_clientRequestResponse.execute(create_url);
    }

    private void getDropdown() {
        Ced_MultiVendor_ClientRequestResponse ced_multiVendor_clientRequestResponse = new Ced_MultiVendor_ClientRequestResponse(new AsyncResponse() {
            @Override
            public void processFinish(Object output) throws JSONException {
                if (functionalityList.getExtensionAddon()) {
                    JSONObject object = new JSONObject(output.toString());
                    JSONArray array = object.getJSONObject("data").getJSONArray("attribute_set");
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject c = null;
                        c = array.getJSONObject(i);
                        valuelist.add(c.getString("value"));
                        labellist.add(c.getString("label"));
                    }
                } else {
                    Intent intent = new Intent(getApplicationContext(), Ced_MultiVendor_UnAuthourizedRequestError.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    overridePendingTransition(R.anim.ced_multivendor_slide_in, R.anim.ced_multivendor_slide_out);
                }
            }
        }, Ced_MultiVendor_CreateProductAttributeSet.this, "POST", postdata);
        ced_multiVendor_clientRequestResponse.execute(url);
    }
}