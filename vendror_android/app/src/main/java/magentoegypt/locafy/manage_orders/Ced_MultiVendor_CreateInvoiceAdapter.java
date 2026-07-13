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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import magentoegypt.locafy.R;
import magentoegypt.locafy.base_app.Ced_MultiVendor_FontSetting;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by developer on 29/6/16.
 */
public class Ced_MultiVendor_CreateInvoiceAdapter extends BaseAdapter {
    private Activity act;
    private ArrayList<HashMap<String,String>> data;
    private static LayoutInflater inflater=null;
    Ced_MultiVendor_FontSetting fontSetting;
    public Ced_MultiVendor_CreateInvoiceAdapter(Activity createInvoice, ArrayList<HashMap<String, String>> invoiceinfo) {
        act=createInvoice;
        data=invoiceinfo;
        fontSetting=new Ced_MultiVendor_FontSetting();
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
        if (convertView==null)
        {
            inflater= (LayoutInflater) act.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView=inflater.inflate(R.layout.ced_multivendor_createcreditmemoitems_list,null);
        }
        TextView prod_name_head = (TextView) convertView.findViewById(R.id.MultiVendor_prod_name_head);
        TextView prod_name = (TextView) convertView.findViewById(R.id.MultiVendor_prod_name);
        TextView prod_price_head = (TextView) convertView.findViewById(R.id.MultiVendor_prod_price_head);
        TextView prod_price = (TextView) convertView.findViewById(R.id.MultiVendor_prod_price);
        TextView prod_qty_head = (TextView) convertView.findViewById(R.id.MultiVendor_prod_qty_head);
        final EditText prod_qty_refund = (EditText) convertView.findViewById(R.id.MultiVendor_prod_qty_refund);
        TextView prod_sku = (TextView) convertView.findViewById(R.id.MultiVendor_prod_sku);
        TextView prod_sku_head = (TextView) convertView.findViewById(R.id.MultiVendor_prod_sku_head);
        TextView Subtotal_head = (TextView) convertView.findViewById(R.id.MultiVendor_Subtotal_head);
        TextView Subtotal = (TextView) convertView.findViewById(R.id.MultiVendor_Subtotal);
        TextView TaxAmount_head = (TextView) convertView.findViewById(R.id.MultiVendor_TaxAmount_head);
        TextView TaxAmount = (TextView) convertView.findViewById(R.id.MultiVendor_TaxAmount);
        TextView DiscountAmount_head = (TextView) convertView.findViewById(R.id.MultiVendor_DiscountAmount_head);
        TextView DiscountAmount = (TextView) convertView.findViewById(R.id.MultiVendor_DiscountAmount);
        TextView RowTotal_head = (TextView) convertView.findViewById(R.id.MultiVendor_RowTotal_head);
        TextView RowTotal = (TextView) convertView.findViewById(R.id.MultiVendor_RowTotal);
        TextView prod_qty_invoiced_head = (TextView) convertView.findViewById(R.id.MultiVendor_prod_qty_invoiced_head);
        TextView prod_qty_invoiced = (TextView) convertView.findViewById(R.id.MultiVendor_prod_qty_invoiced);
        TextView prod_qty_shipped_head = (TextView) convertView.findViewById(R.id.MultiVendor_prod_qty_shipped_head);
        TextView prod_qty_shipped = (TextView) convertView.findViewById(R.id.MultiVendor_prod_qty_shipped);
        TextView prod_qty_refunded_head = (TextView) convertView.findViewById(R.id.MultiVendor_prod_qty_refunded_head);
        TextView prod_qty_refunded = (TextView) convertView.findViewById(R.id.MultiVendor_prod_qty_refunded);
        TextView prod_qty_canceled_head = (TextView) convertView.findViewById(R.id.MultiVendor_prod_qty_canceled_head);
        TextView prod_qty_canceled = (TextView) convertView.findViewById(R.id.MultiVendor_prod_qty_canceled);
        final TextView item_id = (TextView) convertView.findViewById(R.id.MultiVendor_item_id);
        TextView order_id = (TextView) convertView.findViewById(R.id.MultiVendor_order_id);
        final CheckBox returntostock= (CheckBox) convertView.findViewById(R.id.MultiVendor_returntostock);
        final Button updateQuantity= (Button) convertView.findViewById(R.id.MultiVendor_updateQuantity);
        LinearLayout canceled_linear= (LinearLayout) convertView.findViewById(R.id.MultiVendor_canceled_linear);
        LinearLayout refund_linear= (LinearLayout) convertView.findViewById(R.id.MultiVendor_refund_linear);
        LinearLayout Shipped_linear= (LinearLayout) convertView.findViewById(R.id.MultiVendor_Shipped_linear);
        LinearLayout invoiced_linear= (LinearLayout) convertView.findViewById(R.id.MultiVendor_invoiced_linear);

        fontSetting.setFontforTextviews(prod_name_head,"Roboto-Medium.ttf",act);
        fontSetting.setFontforTextviews(prod_price_head,"Roboto-Medium.ttf",act);
        fontSetting.setFontforTextviews(prod_qty_head,"Roboto-Medium.ttf",act);
        fontSetting.setFontforTextviews(prod_sku_head,"Roboto-Medium.ttf",act);
        fontSetting.setFontforTextviews(Subtotal_head,"Roboto-Medium.ttf",act);
        fontSetting.setFontforTextviews(TaxAmount_head,"Roboto-Medium.ttf",act);
        fontSetting.setFontforTextviews(DiscountAmount_head,"Roboto-Medium.ttf",act);
        fontSetting.setFontforTextviews(RowTotal_head,"Roboto-Medium.ttf",act);
        fontSetting.setFontforTextviews(prod_qty_invoiced_head,"Roboto-Medium.ttf",act);
        fontSetting.setFontforTextviews(prod_qty_shipped_head,"Roboto-Medium.ttf",act);
        fontSetting.setFontforTextviews(prod_qty_refunded_head,"Roboto-Medium.ttf",act);
        fontSetting.setFontforTextviews(prod_qty_canceled_head,"Roboto-Medium.ttf",act);

        fontSetting.setFontforTextviews(prod_name,"Roboto-Regular.ttf",act);
        fontSetting.setFontforTextviews(prod_price,"Roboto-Regular.ttf",act);
        fontSetting.setFontforTextviews(prod_sku,"Roboto-Regular.ttf",act);
        fontSetting.setFontforTextviews(Subtotal,"Roboto-Regular.ttf",act);
        fontSetting.setFontforTextviews(TaxAmount,"Roboto-Regular.ttf",act);
        fontSetting.setFontforTextviews(DiscountAmount,"Roboto-Regular.ttf",act);
        fontSetting.setFontforTextviews(RowTotal,"Roboto-Regular.ttf",act);
        fontSetting.setFontforTextviews(prod_qty_shipped,"Roboto-Regular.ttf",act);
        fontSetting.setFontforTextviews(prod_qty_invoiced,"Roboto-Regular.ttf",act);
        fontSetting.setFontforTextviews(prod_qty_refunded,"Roboto-Regular.ttf",act);
        fontSetting.setFontforTextviews(prod_qty_canceled,"Roboto-Regular.ttf",act);
        fontSetting.setfontforEditText(prod_qty_refund, "Roboto-Regular.ttf", act);




        HashMap <String,String> order_prodlisting=new HashMap<String,String>();
        order_prodlisting=data.get(position);
        prod_name.setText(order_prodlisting.get(Ced_MultiVendor_CreateInvoice.KEY_NAME));
        prod_price.setText(order_prodlisting.get(Ced_MultiVendor_CreateInvoice.KEY_PRICE));
        prod_qty_refund.setText(order_prodlisting.get(Ced_MultiVendor_CreateInvoice.KEY_QTY));
        prod_sku.setText(order_prodlisting.get(Ced_MultiVendor_CreateInvoice.KEY_SKU));
        Subtotal.setText(order_prodlisting.get(Ced_MultiVendor_CreateInvoice.KEY_SUBTOTAL));
        TaxAmount.setText(order_prodlisting.get(Ced_MultiVendor_CreateInvoice.KEY_TAX_AMOUNT));
        DiscountAmount.setText(order_prodlisting.get(Ced_MultiVendor_CreateInvoice.KEY_DISCOUNT));
        RowTotal.setText(order_prodlisting.get(Ced_MultiVendor_CreateInvoice.KEY_ROW_TOTAL));
        prod_qty_invoiced.setText(order_prodlisting.get(Ced_MultiVendor_CreateInvoice.KEY_QTY_INVOICED));
        prod_qty_shipped.setText(order_prodlisting.get(Ced_MultiVendor_CreateInvoice.KEY_QTY_SHIPPED));
        prod_qty_refunded.setText(order_prodlisting.get(Ced_MultiVendor_CreateInvoice.KEY_QTY_REFUNDED));
        prod_qty_canceled.setText(order_prodlisting.get(Ced_MultiVendor_CreateInvoice.KEY_QTY_CANCELED));
        item_id.setText(order_prodlisting.get(Ced_MultiVendor_CreateInvoice.KEY_ITEM_ID));
        order_id.setText(order_prodlisting.get(Ced_MultiVendor_CreateInvoice.KEY_ORDER_ID));

        if (prod_qty_invoiced.getText().toString().equals("0"))
        {
            prod_qty_invoiced.setVisibility(View.GONE);
            prod_qty_invoiced_head.setVisibility(View.GONE);
            invoiced_linear.setVisibility(View.GONE);
        }
        if (prod_qty_shipped.getText().toString().equals("0"))
        {
            prod_qty_shipped.setVisibility(View.GONE);
            prod_qty_shipped_head.setVisibility(View.GONE);
            Shipped_linear.setVisibility(View.GONE);
        }
        if (prod_qty_refunded.getText().toString().equals("0"))
        {
            prod_qty_refunded.setVisibility(View.GONE);
            prod_qty_refunded_head.setVisibility(View.GONE);
            refund_linear.setVisibility(View.GONE);
        }
        if (prod_qty_canceled.getText().toString().equals("0"))
        {
            prod_qty_canceled.setVisibility(View.GONE);
            prod_qty_canceled_head.setVisibility(View.GONE);
            canceled_linear.setVisibility(View.GONE);
        }




        return convertView;
    }



}
