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

package magentoegypt.locafy.vendor_transaction;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import magentoegypt.locafy.R;
import magentoegypt.locafy.base_app.Ced_MultiVendor_FontSetting;
import magentoegypt.locafy.base_app.Ced_MultiVendor_VendorFunctionalityList;
import magentoegypt.locafy.api_request_response_section.network_error_handling.Ced_MultiVendor_UnAuthourizedRequestError;
import magentoegypt.locafy.api_request_response_section.AsyncResponse;
import magentoegypt.locafy.api_request_response_section.Ced_MultiVendor_ClientRequestResponse;
import magentoegypt.locafy.vendor_session.Ced_MultiVendor_VendorSessionManagement;
import magentoegypt.locafy_constant.AppConstant;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by developer on 9/5/16.
 */
public class Ced_MultiVendor_RequestPaymentAdapter extends BaseAdapter {

    private Activity act;
    private ArrayList<HashMap<String, String>> data;
    private static LayoutInflater inflater = null;
    String out, vendor_id, order_id, hashkey;
    String url = "";
    HashMap<String, String> postdata;
    Ced_MultiVendor_VendorSessionManagement vendorSessionManagement;
    Ced_MultiVendor_VendorFunctionalityList functionalityList;
    Ced_MultiVendor_FontSetting fontSetting;

    public Ced_MultiVendor_RequestPaymentAdapter(Activity requestPayment, ArrayList<HashMap<String, String>> payment_array) {
        act = requestPayment;
        data = payment_array;
        postdata = new HashMap<>();
        functionalityList = new Ced_MultiVendor_VendorFunctionalityList(act);
        vendorSessionManagement = new Ced_MultiVendor_VendorSessionManagement(act);
        vendor_id = vendorSessionManagement.getVendorid();
        postdata.put("vendor_id", vendor_id);
        final HashMap<String, String> user = vendorSessionManagement.getUserDetails();
        hashkey = user.get(Ced_MultiVendor_VendorSessionManagement.Key_HAsh);
        postdata.put("hashkey", hashkey);
        fontSetting = new Ced_MultiVendor_FontSetting();
        url = vendorSessionManagement.getBase_Url() + "vendorapi/vtransaction/requestPayment";
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
            convertView = inflater.inflate(R.layout.ced_multivendor_payment_list_item, null);
        }
        TextView date_head = (TextView) convertView.findViewById(R.id.MultiVendor_date_head);
        TextView OrderDate = (TextView) convertView.findViewById(R.id.MultiVendor_OrderDate);
        TextView OrderId_head = (TextView) convertView.findViewById(R.id.MultiVendor_OrderId_head);
        TextView OrderId = (TextView) convertView.findViewById(R.id.MultiVendor_OrderId);
        TextView PendingAmount_head = (TextView) convertView.findViewById(R.id.MultiVendor_PendingAmount_head);
        TextView PendingAmount = (TextView) convertView.findViewById(R.id.MultiVendor_PendingAmount);
        TextView Action_head = (TextView) convertView.findViewById(R.id.MultiVendor_Action_head);
        TextView Action = (TextView) convertView.findViewById(R.id.MultiVendor_Action);
        final TextView pament_id = (TextView) convertView.findViewById(R.id.MultiVendor_pament_id);
        Button action_button = (Button) convertView.findViewById(R.id.MultiVendor_action_button);

        fontSetting.setFontforTextviews(date_head, "Roboto-Medium.ttf", act);
        fontSetting.setFontforTextviews(OrderId_head, "Roboto-Medium.ttf", act);
        fontSetting.setFontforTextviews(PendingAmount_head, "Roboto-Medium.ttf", act);
        fontSetting.setFontforTextviews(Action_head, "Roboto-Medium.ttf", act);

        fontSetting.setFontforTextviews(OrderDate, "Roboto-Regular.ttf", act);
        fontSetting.setFontforTextviews(OrderId, "Roboto-Regular.ttf", act);
        fontSetting.setFontforTextviews(PendingAmount, "Roboto-Regular.ttf", act);
        fontSetting.setFontforTextviews(Action, "Roboto-Regular.ttf", act);

        fontSetting.setfontforButtons(action_button, "Roboto-Bold.ttf", act);


        HashMap<String, String> pay = new HashMap<String, String>();
        pay = data.get(position);
        OrderDate.setText(pay.get(Ced_MultiVendor_Requestpayment.KEY_CREATED_AT));
        OrderId.setText(pay.get(Ced_MultiVendor_Requestpayment.KEY_ORDER_ID));
        PendingAmount.setText(pay.get(Ced_MultiVendor_Requestpayment.KEY_PENDING_AMOUNT));
        Action.setText(pay.get(Ced_MultiVendor_Requestpayment.KEY_ACTION));
        action_button.setText(pay.get(Ced_MultiVendor_Requestpayment.KEY_ACTION));
        pament_id.setText(pay.get(Ced_MultiVendor_Requestpayment.KEY_PAYMENT_ID));
        if (Action.getText().toString().equals("Request Payment")) {
            action_button.setVisibility(View.VISIBLE);
            Action.setVisibility(View.GONE);
        } else {
            action_button.setVisibility(View.GONE);
            Action.setVisibility(View.VISIBLE);
        }
        action_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppConstant.lockButton(v);
                order_id = pament_id.getText().toString();
                postdata.put("payment_id", order_id);
                Ced_MultiVendor_ClientRequestResponse response = new Ced_MultiVendor_ClientRequestResponse(new AsyncResponse() {
                    @Override
                    public void processFinish(Object output) throws JSONException {
                        if (functionalityList.getExtensionAddon()) {
                            out = output.toString();
                            JSONObject object = new JSONObject(out);
                            if (object.getJSONObject("data").getBoolean("success")) {
                                Intent req_pay = new Intent(act, Ced_MultiVendor_Requestpayment.class);
                                req_pay.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                                req_pay.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                act.startActivity(req_pay);
                                act.overridePendingTransition(R.anim.ced_multivendor_slide_in, R.anim.ced_multivendor_slide_out);
                            } else {
                                Toast.makeText(act, object.getJSONObject("data").getString("message"), Toast.LENGTH_LONG);
                            }
                        } else {
                            Intent intent = new Intent(act, Ced_MultiVendor_UnAuthourizedRequestError.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            act.startActivity(intent);
                            act.overridePendingTransition(R.anim.ced_multivendor_slide_in, R.anim.ced_multivendor_slide_out);

                        }


                    }
                }, act, "POST", postdata);
                response.execute(url);
            }
        });


        return convertView;
    }
}
