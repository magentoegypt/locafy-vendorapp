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
import android.widget.TextView;

import magentoegypt.locafy.R;
import magentoegypt.locafy.base_app.Ced_MultiVendor_FontSetting;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by developer on 25/6/16.
 */
public class Ced_MultiVendor_InvoiceViewAdapter extends BaseAdapter {
    private Context act;
    private ArrayList<HashMap<String, String>> data;
    private static LayoutInflater inflater = null;
    Ced_MultiVendor_FontSetting fontSetting;

    public Ced_MultiVendor_InvoiceViewAdapter(Activity invoiceView, ArrayList<HashMap<String, String>> invoiceinfo) {

        act = invoiceView;
        data = invoiceinfo;
        fontSetting = new Ced_MultiVendor_FontSetting();
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
            convertView = inflater.inflate(R.layout.ced_multivendor_orderplaceditems_list, null);
        }
        TextView prod_name_head = (TextView) convertView.findViewById(R.id.MultiVendor_prod_name_head);
        TextView prod_name = (TextView) convertView.findViewById(R.id.MultiVendor_prod_name);
        TextView prod_price_head = (TextView) convertView.findViewById(R.id.MultiVendor_prod_price_head);
        TextView prod_price = (TextView) convertView.findViewById(R.id.MultiVendor_prod_price);
        TextView prod_qty_head = (TextView) convertView.findViewById(R.id.MultiVendor_prod_qty_head);
        TextView prod_qty = (TextView) convertView.findViewById(R.id.MultiVendor_prod_qty);
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
        TextView txt_igst = convertView.findViewById(R.id.txt_igst);
        TextView txt_sgst_amt = convertView.findViewById(R.id.txt_sgst_amt);
        TextView txt_cgst_amt = convertView.findViewById(R.id.txt_cgst_amt);


        fontSetting.setFontforTextviews(prod_name_head, "Roboto-Medium.ttf", act);
        fontSetting.setFontforTextviews(prod_price_head, "Roboto-Medium.ttf", act);
        fontSetting.setFontforTextviews(prod_qty_head, "Roboto-Medium.ttf", act);
        fontSetting.setFontforTextviews(prod_sku_head, "Roboto-Medium.ttf", act);
        fontSetting.setFontforTextviews(Subtotal_head, "Roboto-Medium.ttf", act);
        fontSetting.setFontforTextviews(TaxAmount_head, "Roboto-Medium.ttf", act);
        fontSetting.setFontforTextviews(DiscountAmount_head, "Roboto-Medium.ttf", act);
        fontSetting.setFontforTextviews(RowTotal_head, "Roboto-Medium.ttf", act);

        fontSetting.setFontforTextviews(prod_name, "Roboto-Regular.ttf", act);
        fontSetting.setFontforTextviews(prod_price, "Roboto-Regular.ttf", act);
        fontSetting.setFontforTextviews(prod_qty, "Roboto-Regular.ttf", act);
        fontSetting.setFontforTextviews(prod_sku, "Roboto-Regular.ttf", act);
        fontSetting.setFontforTextviews(Subtotal, "Roboto-Regular.ttf", act);
        fontSetting.setFontforTextviews(TaxAmount, "Roboto-Regular.ttf", act);
        fontSetting.setFontforTextviews(DiscountAmount, "Roboto-Regular.ttf", act);
        fontSetting.setFontforTextviews(RowTotal, "Roboto-Regular.ttf", act);

        HashMap<String, String> order_prodlisting = new HashMap<String, String>();
        order_prodlisting = data.get(position);
        prod_name.setText(order_prodlisting.get(Ced_MultiVendor_InvoiceView.KEY_NAME));
        prod_price.setText(order_prodlisting.get(Ced_MultiVendor_InvoiceView.KEY_PRICE));
        prod_qty.setText(order_prodlisting.get(Ced_MultiVendor_InvoiceView.KEY_QTY));
        prod_sku.setText(order_prodlisting.get(Ced_MultiVendor_InvoiceView.KEY_SKU));
        Subtotal.setText(order_prodlisting.get(Ced_MultiVendor_InvoiceView.KEY_SUBTOTAL));
        TaxAmount.setText(order_prodlisting.get(Ced_MultiVendor_InvoiceView.KEY_TAX_AMOUNT));
        DiscountAmount.setText(order_prodlisting.get(Ced_MultiVendor_InvoiceView.KEY_DISCOUNT));
        RowTotal.setText(order_prodlisting.get(Ced_MultiVendor_InvoiceView.KEY_ROW_TOTAL));

        txt_igst.setText(order_prodlisting.get(Ced_MultiVendor_InvoiceView.IGST_AMT));
        txt_sgst_amt.setText(order_prodlisting.get(Ced_MultiVendor_InvoiceView.SGST_AMT));
        txt_cgst_amt.setText(order_prodlisting.get(Ced_MultiVendor_InvoiceView.CGST_AMT));

        return convertView;
    }
}
