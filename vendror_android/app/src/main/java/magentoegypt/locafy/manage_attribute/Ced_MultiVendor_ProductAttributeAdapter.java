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
 * Created by developer on 2/6/16.
 */
public class Ced_MultiVendor_ProductAttributeAdapter extends BaseAdapter {
    private static LayoutInflater inflater = null;
    Activity act;
    Ced_MultiVendor_FontSetting fontSetting;
    String url = "";
    HashMap<String, String> postdata;
    String hashkey, vendor_id;
    String out;
    Ced_MultiVendor_VendorSessionManagement vendorSessionManagement;
    private final ArrayList<HashMap<String, String>> data;

    public Ced_MultiVendor_ProductAttributeAdapter(Activity vendorProductAttribute, ArrayList<HashMap<String, String>> prod_attr_info) {
        act = vendorProductAttribute;
        data = prod_attr_info;
        fontSetting = new Ced_MultiVendor_FontSetting();
        postdata = new HashMap<>();
        vendorSessionManagement = new Ced_MultiVendor_VendorSessionManagement(act);
        url = vendorSessionManagement.getBase_Url() + "vproductattributeapi/attribute/delete";
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
        View vi = convertView;
        if (convertView == null) {
            inflater = (LayoutInflater) act.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            vi = inflater.inflate(R.layout.ced_multivendor_product_attribute_list_adapter, null);

        }
        TextView AttributeCode_head = (TextView) vi.findViewById(R.id.MultiVendor_AttributeCode_head);
        TextView AttributeCode = (TextView) vi.findViewById(R.id.MultiVendor_AttributeCode);
        TextView AttributeLabel_head = (TextView) vi.findViewById(R.id.MultiVendor_AttributeLabel_head);
        TextView AttributeLabel = (TextView) vi.findViewById(R.id.MultiVendor_AttributeLabel);
        TextView Required_head = (TextView) vi.findViewById(R.id.MultiVendor_Required_head);
        TextView Required = (TextView) vi.findViewById(R.id.MultiVendor_Required);
        TextView System_head = (TextView) vi.findViewById(R.id.MultiVendor_System_head);
        TextView System = (TextView) vi.findViewById(R.id.MultiVendor_System);
        TextView Visible_head = (TextView) vi.findViewById(R.id.MultiVendor_Visible_head);
        TextView Visible = (TextView) vi.findViewById(R.id.MultiVendor_Visible);
        TextView scope_head = (TextView) vi.findViewById(R.id.MultiVendor_scope_head);
        TextView scope = (TextView) vi.findViewById(R.id.MultiVendor_scope);
        TextView Searchable_head = (TextView) vi.findViewById(R.id.MultiVendor_Searchable_head);
        TextView Searchable = (TextView) vi.findViewById(R.id.MultiVendor_Searchable);
        TextView UseInLayeredNavigation_head = (TextView) vi.findViewById(R.id.MultiVendor_UseInLayeredNavigation_head);
        TextView UseInLayeredNavigation = (TextView) vi.findViewById(R.id.MultiVendor_UseInLayeredNavigation);
        TextView Comparable_head = (TextView) vi.findViewById(R.id.MultiVendor_Comparable_head);
        TextView Comparable = (TextView) vi.findViewById(R.id.MultiVendor_Comparable);
        final TextView attr_id = (TextView) vi.findViewById(R.id.MultiVendor_attr_id);
        Button Delete_Attr = (Button) vi.findViewById(R.id.Delete_Attr);
        Delete_Attr.setOnClickListener(new View.OnClickListener() {
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
                                postdata.put("id", attr_id.getText().toString());
                                Ced_MultiVendor_ClientRequestResponse ced_multiVendor_clientRequestResponse = new Ced_MultiVendor_ClientRequestResponse(new AsyncResponse() {
                                    @Override
                                    public void processFinish(Object output) throws JSONException {
                                        out = output.toString();
                                        JSONObject object = new JSONObject(out);
                                        if (object.getJSONObject("data").getBoolean("success")) {
                                            Toast.makeText(act, object.getJSONObject("data").getString("message"), Toast.LENGTH_LONG).show();
                                            Intent intent = new Intent(act, Ced_MultiVendor_VendorProductAttribute.class);
                                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                            act.startActivity(intent);


                                        } else {
                                            Toast.makeText(act, object.getJSONObject("data").getString("message"), Toast.LENGTH_LONG).show();
                                        }

                                    }
                                }, act, "POST", postdata);
                                ced_multiVendor_clientRequestResponse.execute(url);


                            }
                        });
                builder.show();

            }
        });

        System_head.setVisibility(View.GONE);
        System.setVisibility(View.GONE);
        Comparable_head.setVisibility(View.GONE);
        Comparable.setVisibility(View.GONE);
        Searchable_head.setVisibility(View.GONE);
        Searchable.setVisibility(View.GONE);
        Visible_head.setVisibility(View.GONE);
        Visible.setVisibility(View.GONE);
        UseInLayeredNavigation_head.setVisibility(View.GONE);
        UseInLayeredNavigation.setVisibility(View.GONE);


        fontSetting.setFontforTextviews(AttributeCode_head, "Roboto-Medium.ttf", act);
        fontSetting.setFontforTextviews(AttributeLabel_head, "Roboto-Medium.ttf", act);
        fontSetting.setFontforTextviews(Required_head, "Roboto-Medium.ttf", act);
        fontSetting.setFontforTextviews(System_head, "Roboto-Medium.ttf", act);
        fontSetting.setFontforTextviews(Visible_head, "Roboto-Medium.ttf", act);
        fontSetting.setFontforTextviews(scope_head, "Roboto-Medium.ttf", act);
        fontSetting.setFontforTextviews(Searchable_head, "Roboto-Medium.ttf", act);
        fontSetting.setFontforTextviews(UseInLayeredNavigation_head, "Roboto-Medium.ttf", act);
        fontSetting.setFontforTextviews(Comparable_head, "Roboto-Medium.ttf", act);

        fontSetting.setFontforTextviews(AttributeCode, "Roboto-Regular.ttf", act);
        fontSetting.setFontforTextviews(AttributeLabel, "Roboto-Regular.ttf", act);
        fontSetting.setFontforTextviews(Required, "Roboto-Regular.ttf", act);
        fontSetting.setFontforTextviews(System, "Roboto-Regular.ttf", act);
        fontSetting.setFontforTextviews(Visible, "Roboto-Regular.ttf", act);
        fontSetting.setFontforTextviews(scope, "Roboto-Regular.ttf", act);
        fontSetting.setFontforTextviews(Searchable, "Roboto-Regular.ttf", act);
        fontSetting.setFontforTextviews(UseInLayeredNavigation, "Roboto-Regular.ttf", act);
        fontSetting.setFontforTextviews(Comparable, "Roboto-Regular.ttf", act);


        HashMap<String, String> attr = new HashMap<String, String>();
        attr = data.get(position);
        AttributeCode.setText(attr.get(Ced_MultiVendor_VendorProductAttribute.KEY_ATTRIBUTE_CODE));
        AttributeLabel.setText(attr.get(Ced_MultiVendor_VendorProductAttribute.KEY_ATTRIBUTE_LABEL));
        Required.setText(attr.get(Ced_MultiVendor_VendorProductAttribute.KEY_REQUIRED));
        System.setText(attr.get(Ced_MultiVendor_VendorProductAttribute.KEY_SYSTEM));
        Visible.setText(attr.get(Ced_MultiVendor_VendorProductAttribute.KEY_VISIBLE));
        scope.setText(attr.get(Ced_MultiVendor_VendorProductAttribute.KEY_SCOPE));
        Searchable.setText(attr.get(Ced_MultiVendor_VendorProductAttribute.KEY_SEARCHABLE));
        UseInLayeredNavigation.setText(attr.get(Ced_MultiVendor_VendorProductAttribute.KEY_USE_IN_LAYERED));
        Comparable.setText(attr.get(Ced_MultiVendor_VendorProductAttribute.KEY_COMPARABLE));
        attr_id.setText(attr.get(Ced_MultiVendor_VendorProductAttribute.KEY_ATTRIBUTE_ID));

        return vi;
    }

}
