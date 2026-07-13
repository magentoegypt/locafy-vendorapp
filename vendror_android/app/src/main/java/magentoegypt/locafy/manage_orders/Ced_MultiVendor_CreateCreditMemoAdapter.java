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

package magentoegypt.locafy.manage_orders;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import magentoegypt.locafy.base_app.Ced_MultiVendor_FontSetting;
import magentoegypt.locafy.base_app.Ced_MultiVendor_VendorFunctionalityList;
import magentoegypt.locafy.api_request_response_section.network_error_handling.Ced_MultiVendor_UnAuthourizedRequestError;
import magentoegypt.locafy.api_request_response_section.AsyncResponse;
import magentoegypt.locafy.api_request_response_section.Ced_MultiVendor_ClientRequestResponse;
import magentoegypt.locafy.vendor_session.Ced_MultiVendor_VendorSessionManagement;
import magentoegypt.locafy.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by developer on 29/6/16.
 */
public class Ced_MultiVendor_CreateCreditMemoAdapter extends BaseAdapter {
    private static LayoutInflater inflater = null;
    private final Activity act;
    private final ArrayList<HashMap<String, String>> data;
    Ced_MultiVendor_FontSetting fontSetting;
    HashMap<String, String> postdata;
    Ced_MultiVendor_VendorFunctionalityList functionalityList;
    Ced_MultiVendor_VendorSessionManagement vendorSessionManagement;
    String out, url, vendor_id, hashkey = "";
    TextView order_id;

    public Ced_MultiVendor_CreateCreditMemoAdapter(Activity createCreditMemo, ArrayList<HashMap<String, String>> invoiceinfo) {
        act = createCreditMemo;
        data = invoiceinfo;
        fontSetting = new Ced_MultiVendor_FontSetting();
        postdata = new HashMap<>();
        functionalityList = new Ced_MultiVendor_VendorFunctionalityList(act);
        vendorSessionManagement = new Ced_MultiVendor_VendorSessionManagement(act);
        url = vendorSessionManagement.getBase_Url() + "vorderapi/vcreditmemo/updateQty";
        vendor_id = vendorSessionManagement.getVendorid();
        final HashMap<String, String> user = vendorSessionManagement.getUserDetails();
        hashkey = user.get(Ced_MultiVendor_VendorSessionManagement.Key_HAsh);
        postdata.put("hashkey", hashkey);
        postdata.put("vendor_id", vendor_id);
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            inflater = (LayoutInflater) act.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.ced_multivendor_createcreditmemoitems_list, null);
        }
        TextView prod_name_head = convertView.findViewById(R.id.MultiVendor_prod_name_head);
        TextView prod_name = convertView.findViewById(R.id.MultiVendor_prod_name);
        TextView prod_price_head = convertView.findViewById(R.id.MultiVendor_prod_price_head);
        TextView prod_price = convertView.findViewById(R.id.MultiVendor_prod_price);
        TextView prod_qty_head = convertView.findViewById(R.id.MultiVendor_prod_qty_head);
        final EditText prod_qty_refund = convertView.findViewById(R.id.MultiVendor_prod_qty_refund);
        TextView prod_sku = convertView.findViewById(R.id.MultiVendor_prod_sku);
        TextView prod_sku_head = convertView.findViewById(R.id.MultiVendor_prod_sku_head);
        TextView Subtotal_head = convertView.findViewById(R.id.MultiVendor_Subtotal_head);
        TextView Subtotal = convertView.findViewById(R.id.MultiVendor_Subtotal);
        TextView TaxAmount_head = convertView.findViewById(R.id.MultiVendor_TaxAmount_head);
        TextView TaxAmount = convertView.findViewById(R.id.MultiVendor_TaxAmount);
        TextView DiscountAmount_head = convertView.findViewById(R.id.MultiVendor_DiscountAmount_head);
        TextView DiscountAmount = convertView.findViewById(R.id.MultiVendor_DiscountAmount);
        TextView RowTotal_head = convertView.findViewById(R.id.MultiVendor_RowTotal_head);
        TextView RowTotal = convertView.findViewById(R.id.MultiVendor_RowTotal);
        TextView prod_qty_invoiced_head = convertView.findViewById(R.id.MultiVendor_prod_qty_invoiced_head);
        TextView prod_qty_invoiced = convertView.findViewById(R.id.MultiVendor_prod_qty_invoiced);
        TextView prod_qty_shipped_head = convertView.findViewById(R.id.MultiVendor_prod_qty_shipped_head);
        TextView prod_qty_shipped = convertView.findViewById(R.id.MultiVendor_prod_qty_shipped);
        TextView prod_qty_refunded_head = convertView.findViewById(R.id.MultiVendor_prod_qty_refunded_head);
        TextView prod_qty_refunded = convertView.findViewById(R.id.MultiVendor_prod_qty_refunded);
        TextView prod_qty_canceled_head = convertView.findViewById(R.id.MultiVendor_prod_qty_canceled_head);
        TextView prod_qty_canceled = convertView.findViewById(R.id.MultiVendor_prod_qty_canceled);
        final TextView item_id = convertView.findViewById(R.id.MultiVendor_item_id);
        order_id = convertView.findViewById(R.id.MultiVendor_order_id);
        final CheckBox returntostock = convertView.findViewById(R.id.MultiVendor_returntostock);
        final Button updateQuantity = convertView.findViewById(R.id.MultiVendor_updateQuantity);
        LinearLayout canceled_linear = convertView.findViewById(R.id.MultiVendor_canceled_linear);
        LinearLayout refund_linear = convertView.findViewById(R.id.MultiVendor_refund_linear);
        LinearLayout Shipped_linear = convertView.findViewById(R.id.MultiVendor_Shipped_linear);
        LinearLayout invoiced_linear = convertView.findViewById(R.id.MultiVendor_invoiced_linear);

        fontSetting.setFontforTextviews(prod_name_head, "Roboto-Medium.ttf", act);
        fontSetting.setFontforTextviews(prod_price_head, "Roboto-Medium.ttf", act);
        fontSetting.setFontforTextviews(prod_qty_head, "Roboto-Medium.ttf", act);
        fontSetting.setFontforTextviews(prod_sku_head, "Roboto-Medium.ttf", act);
        fontSetting.setFontforTextviews(Subtotal_head, "Roboto-Medium.ttf", act);
        fontSetting.setFontforTextviews(TaxAmount_head, "Roboto-Medium.ttf", act);
        fontSetting.setFontforTextviews(DiscountAmount_head, "Roboto-Medium.ttf", act);
        fontSetting.setFontforTextviews(RowTotal_head, "Roboto-Medium.ttf", act);
        fontSetting.setFontforTextviews(prod_qty_invoiced_head, "Roboto-Medium.ttf", act);
        fontSetting.setFontforTextviews(prod_qty_shipped_head, "Roboto-Medium.ttf", act);
        fontSetting.setFontforTextviews(prod_qty_refunded_head, "Roboto-Medium.ttf", act);
        fontSetting.setFontforTextviews(prod_qty_canceled_head, "Roboto-Medium.ttf", act);

        fontSetting.setFontforTextviews(prod_name, "Roboto-Regular.ttf", act);
        fontSetting.setFontforTextviews(prod_price, "Roboto-Regular.ttf", act);
        fontSetting.setFontforTextviews(prod_sku, "Roboto-Regular.ttf", act);
        fontSetting.setFontforTextviews(Subtotal, "Roboto-Regular.ttf", act);
        fontSetting.setFontforTextviews(TaxAmount, "Roboto-Regular.ttf", act);
        fontSetting.setFontforTextviews(DiscountAmount, "Roboto-Regular.ttf", act);
        fontSetting.setFontforTextviews(RowTotal, "Roboto-Regular.ttf", act);
        fontSetting.setFontforTextviews(prod_qty_shipped, "Roboto-Regular.ttf", act);
        fontSetting.setFontforTextviews(prod_qty_invoiced, "Roboto-Regular.ttf", act);
        fontSetting.setFontforTextviews(prod_qty_refunded, "Roboto-Regular.ttf", act);
        fontSetting.setFontforTextviews(prod_qty_canceled, "Roboto-Regular.ttf", act);
        fontSetting.setfontforEditText(prod_qty_refund, "Roboto-Regular.ttf", act);


        HashMap<String, String> order_prodlisting = new HashMap<String, String>();
        order_prodlisting = data.get(position);
        prod_name.setText(order_prodlisting.get(Ced_MultiVendor_CreateCreditMemo.KEY_NAME));
        prod_price.setText(order_prodlisting.get(Ced_MultiVendor_CreateCreditMemo.KEY_PRICE));
        prod_qty_refund.setText(order_prodlisting.get(Ced_MultiVendor_CreateCreditMemo.KEY_QTY));
        prod_sku.setText(order_prodlisting.get(Ced_MultiVendor_CreateCreditMemo.KEY_SKU));
        Subtotal.setText(order_prodlisting.get(Ced_MultiVendor_CreateCreditMemo.KEY_SUBTOTAL));
        TaxAmount.setText(order_prodlisting.get(Ced_MultiVendor_CreateCreditMemo.KEY_TAX_AMOUNT));
        DiscountAmount.setText(order_prodlisting.get(Ced_MultiVendor_CreateCreditMemo.KEY_DISCOUNT));
        RowTotal.setText(order_prodlisting.get(Ced_MultiVendor_CreateCreditMemo.KEY_ROW_TOTAL));
        prod_qty_invoiced.setText(order_prodlisting.get(Ced_MultiVendor_CreateCreditMemo.KEY_QTY_INVOICED));
        prod_qty_shipped.setText(order_prodlisting.get(Ced_MultiVendor_CreateCreditMemo.KEY_QTY_SHIPPED));
        prod_qty_refunded.setText(order_prodlisting.get(Ced_MultiVendor_CreateCreditMemo.KEY_QTY_REFUNDED));
        prod_qty_canceled.setText(order_prodlisting.get(Ced_MultiVendor_CreateCreditMemo.KEY_QTY_CANCELED));
        item_id.setText(order_prodlisting.get(Ced_MultiVendor_CreateCreditMemo.KEY_ITEM_ID));
        order_id.setText(order_prodlisting.get(Ced_MultiVendor_CreateCreditMemo.KEY_ORDER_ID));

        if (prod_qty_invoiced.getText().toString().equals("0")) {
            prod_qty_invoiced.setVisibility(View.GONE);
            prod_qty_invoiced_head.setVisibility(View.GONE);
            invoiced_linear.setVisibility(View.GONE);
        }
        if (prod_qty_shipped.getText().toString().equals("0")) {
            prod_qty_shipped.setVisibility(View.GONE);
            prod_qty_shipped_head.setVisibility(View.GONE);
            Shipped_linear.setVisibility(View.GONE);
        }
        if (prod_qty_refunded.getText().toString().equals("0")) {
            prod_qty_refunded.setVisibility(View.GONE);
            prod_qty_refunded_head.setVisibility(View.GONE);
            refund_linear.setVisibility(View.GONE);
        }
        if (prod_qty_canceled.getText().toString().equals("0")) {
            prod_qty_canceled.setVisibility(View.GONE);
            prod_qty_canceled_head.setVisibility(View.GONE);
            canceled_linear.setVisibility(View.GONE);
        }

        returntostock.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (returntostock.isChecked()) {
                    postdata.put("back_to_stock", "true");
                }
            }
        });
        updateQuantity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (prod_qty_refund.getText().toString().isEmpty()) {
                    prod_qty_refund.setError(act.getResources().getString(R.string.plsfillqtyforreturn));
                    prod_qty_refund.requestFocus();
                } else {
                    postdata.put("qty", prod_qty_refund.getText().toString());
                    postdata.put("item_id", item_id.getText().toString());
                    postdata.put("order_id", order_id.getText().toString());

                    request();
                }

            }
        });


        return convertView;
    }

    private void request() {
        Ced_MultiVendor_ClientRequestResponse response = new Ced_MultiVendor_ClientRequestResponse(new AsyncResponse() {
            @Override
            public void processFinish(Object output) throws JSONException {
                if (functionalityList.getExtensionAddon()) {
                    out = output.toString();
                    JSONObject object = new JSONObject(out);
                    if (object.getJSONObject("data").getBoolean("success")) {
                        Intent backintent = new Intent(act, Ced_MultiVendor_CreateCreditMemo.class);
                        backintent.putExtra("order_id", order_id.getText().toString());
                        backintent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        act.startActivity(backintent);

                    }

                    fetched_output();

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

    private void fetched_output() {

    }


}
