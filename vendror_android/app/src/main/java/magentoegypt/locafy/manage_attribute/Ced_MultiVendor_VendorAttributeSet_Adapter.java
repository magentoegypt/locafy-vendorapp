package magentoegypt.locafy.manage_attribute;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import magentoegypt.locafy.base_app.Ced_MultiVendor_FontSetting;
import magentoegypt.locafy.api_request_response_section.AsyncResponse;
import magentoegypt.locafy.api_request_response_section.Ced_MultiVendor_ClientRequestResponse;
import magentoegypt.locafy.vendor_session.Ced_MultiVendor_VendorSessionManagement;
import magentoegypt.locafy.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by developer on 21/11/16.
 */
public class Ced_MultiVendor_VendorAttributeSet_Adapter extends BaseAdapter {
    Activity act;
    Ced_MultiVendor_FontSetting fontSetting;
    private ArrayList<HashMap<String, String>> data;
    private static LayoutInflater inflater = null;
    String hashkey, vendor_id;
    String out;
    String url = "";
    HashMap<String, String> postdata;
    Ced_MultiVendor_VendorSessionManagement vendorSessionManagement;

    public Ced_MultiVendor_VendorAttributeSet_Adapter(Activity ced_multiVendor_vendorProductAttributeSet, ArrayList<HashMap<String, String>> prod_attr_info) {
        act = ced_multiVendor_vendorProductAttributeSet;
        data = prod_attr_info;
        fontSetting = new Ced_MultiVendor_FontSetting();
        postdata = new HashMap<>();
        vendorSessionManagement = new Ced_MultiVendor_VendorSessionManagement(act);
        url = vendorSessionManagement.getBase_Url()+ "vproductattributeapi/set/delete";
        final HashMap<String, String> user = vendorSessionManagement.getUserDetails();
        hashkey = user.get(Ced_MultiVendor_VendorSessionManagement.Key_HAsh);
        vendor_id = vendorSessionManagement.getVendorid();
        postdata.put("vendor_id", vendor_id);
        postdata.put("hashkey", hashkey);
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            inflater = (LayoutInflater) act.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.ced_multivendor_product_attributeset_list_adapter, null);


        }
        final TextView attribute_setname = (TextView) convertView.findViewById(R.id.attribute_setname);
        final TextView attribute_setid = (TextView) convertView.findViewById(R.id.attribute_setid);
        fontSetting.setFontforTextviews(attribute_setname, "Roboto-Medium.ttf", act);
        HashMap<String, String> attr = new HashMap<String, String>();
        attr = data.get(position);
        attribute_setname.setText(attr.get(Ced_MultiVendor_VendorProductAttributeSet.KEY_SET_CODE));
        attribute_setid.setText(attr.get(Ced_MultiVendor_VendorProductAttributeSet.KEY_SET_ID));
        Button delete_attr_set = (Button) convertView.findViewById(R.id.delete_attr_set);
        attribute_setname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(act, Ced_MultiVendor_VendorProductAttributeSet_Edit.class);
                intent.putExtra("attribute_setid", attribute_setid.getText().toString());
                intent.putExtra("attribute_setname", attribute_setname.getText().toString());
                act.startActivity(intent);
            }
        });

        delete_attr_set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(
                        act);
                builder.setTitle(R.string.alert_name);
                builder.setIcon(R.mipmap.ic_launcher);
                builder.setMessage(R.string.confirm_first);
                builder.setNegativeButton(R.string.cancel,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int which) {

                            }
                        });
                builder.setPositiveButton(R.string.yes,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                postdata.put("id", attribute_setid.getText().toString());
                                Ced_MultiVendor_ClientRequestResponse ced_multiVendor_clientRequestResponse = new Ced_MultiVendor_ClientRequestResponse(new AsyncResponse() {
                                    @Override
                                    public void processFinish(Object output) throws JSONException {
                                        out = output.toString();
                                        JSONObject object = new JSONObject(out);
                                        if (object.getJSONObject("data").getBoolean("success")) {
                                            Toast.makeText(act, object.getJSONObject("data").getString("message").toString(), Toast.LENGTH_LONG).show();
                                            Intent intent = new Intent(act, Ced_MultiVendor_VendorProductAttributeSet.class);
                                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                            act.startActivity(intent);


                                        } else {
                                            Toast.makeText(act, object.getJSONObject("data").getString("message").toString(), Toast.LENGTH_LONG).show();
                                        }

                                    }
                                }, act, "POST", postdata);
                                ced_multiVendor_clientRequestResponse.execute(url);


                            }
                        });
                builder.show();
            }
        });
        return convertView;
    }
}
