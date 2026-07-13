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
 * Created by developer on 23/6/16.
 */
public class Ced_MultiVendor_InvoiceListingAdapter extends BaseAdapter {
    Activity act;
    Ced_MultiVendor_FontSetting fontSetting;
    private ArrayList<HashMap<String,String>> data;
    private static LayoutInflater inflater=null;
    public Ced_MultiVendor_InvoiceListingAdapter(Activity invoiceListing, ArrayList<HashMap<String, String>> invoiceinfo) {
        act=invoiceListing;
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
            convertView=inflater.inflate(R.layout.ced_multivendor_invoice_list_item,null);
        }
        TextView Invoice_id_txt= (TextView) convertView.findViewById(R.id.MultiVendor_Invoice_id_txt);
        TextView Invoice_id= (TextView) convertView.findViewById(R.id.MultiVendor_Invoice_id);
        TextView invoice_date_txt= (TextView) convertView.findViewById(R.id.MultiVendor_invoice_date_txt);
        TextView invoice_date= (TextView) convertView.findViewById(R.id.MultiVendor_invoice_date);
        TextView ship_to= (TextView) convertView.findViewById(R.id.MultiVendor_ship_to);
        TextView prodname= (TextView) convertView.findViewById(R.id.MultiVendor_prodname);
        TextView orderid_txt= (TextView) convertView.findViewById(R.id.MultiVendor_orderid_txt);
        TextView order_id= (TextView) convertView.findViewById(R.id.MultiVendor_order_id);
        TextView orderdate_txt= (TextView) convertView.findViewById(R.id.MultiVendor_orderdate_txt);
        TextView orderdate= (TextView) convertView.findViewById(R.id.MultiVendor_orderdate);
        TextView status_txt= (TextView) convertView.findViewById(R.id.MultiVendor_status_txt);
        TextView status= (TextView) convertView.findViewById(R.id.MultiVendor_status);
        TextView Amount_text= (TextView) convertView.findViewById(R.id.MultiVendor_Amount_text);
        TextView Amount= (TextView) convertView.findViewById(R.id.MultiVendor_Amount);

        fontSetting.setFontforTextviews(ship_to,"Roboto-Medium.ttf",act);
        fontSetting.setFontforTextviews(Invoice_id_txt,"Roboto-Medium.ttf",act);
        fontSetting.setFontforTextviews(invoice_date_txt,"Roboto-Medium.ttf",act);
        fontSetting.setFontforTextviews(orderid_txt,"Roboto-Medium.ttf",act);
        fontSetting.setFontforTextviews(orderdate_txt,"Roboto-Medium.ttf",act);
        fontSetting.setFontforTextviews(status_txt,"Roboto-Medium.ttf",act);
        fontSetting.setFontforTextviews(Amount_text,"Roboto-Medium.ttf",act);

        fontSetting.setFontforTextviews(Invoice_id,"Roboto-Regular.ttf",act);
        fontSetting.setFontforTextviews(invoice_date,"Roboto-Regular.ttf",act);
        fontSetting.setFontforTextviews(order_id,"Roboto-Regular.ttf",act);
        fontSetting.setFontforTextviews(status,"Roboto-Regular.ttf",act);
        fontSetting.setFontforTextviews(prodname,"Roboto-Regular.ttf",act);
        fontSetting.setFontforTextviews(orderdate,"Roboto-Regular.ttf",act);
        fontSetting.setFontforTextviews(Amount,"Roboto-Regular.ttf",act);

        HashMap <String,String> invoicelisting=new HashMap<String,String>();
        invoicelisting=data.get(position);

        Invoice_id.setText(invoicelisting.get(Ced_MultiVendor_InvoiceListing.KEY_INCREMENT_ID));
        invoice_date.setText(invoicelisting.get(Ced_MultiVendor_InvoiceListing.KEY_CREATED_AT));
        prodname.setText(invoicelisting.get(Ced_MultiVendor_InvoiceListing.KEY_BILLING_NAME));
        order_id.setText(invoicelisting.get(Ced_MultiVendor_InvoiceListing.KEY_ORDER_INCREMENT_ID));
        orderdate.setText(invoicelisting.get(Ced_MultiVendor_InvoiceListing.KEY_ORDER_CREATED_AT));
        status.setText(invoicelisting.get(Ced_MultiVendor_InvoiceListing.KEY_STATE));
        Amount.setText(invoicelisting.get(Ced_MultiVendor_InvoiceListing.KEY_GRAND_TOTAL));

        return convertView;
    }
}
