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
public class Ced_MultiVendor_ShipmentViewAdapter extends BaseAdapter{
    private Context act;
    private ArrayList<HashMap<String,String>> data;
    private static LayoutInflater inflater=null;
    Ced_MultiVendor_FontSetting fontSetting;
    public Ced_MultiVendor_ShipmentViewAdapter(Activity shipmentView, ArrayList<HashMap<String, String>> invoiceinfo) {

        act=shipmentView;
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
            convertView=inflater.inflate(R.layout.ced_multivendor_shipmentproduct_items_list,null);
        }
        TextView prod_name_head = (TextView) convertView.findViewById(R.id.MultiVendor_prod_name_head);
        TextView prod_name = (TextView) convertView.findViewById(R.id.MultiVendor_prod_name);
        TextView prod_qty_head = (TextView) convertView.findViewById(R.id.MultiVendor_prod_qty_head);
        TextView prod_qty = (TextView) convertView.findViewById(R.id.MultiVendor_prod_qty);
        TextView prod_sku = (TextView) convertView.findViewById(R.id.MultiVendor_prod_sku);
        TextView prod_sku_head = (TextView) convertView.findViewById(R.id.MultiVendor_prod_sku_head);


        fontSetting.setFontforTextviews(prod_name_head,"Roboto-Medium.ttf",act);

        fontSetting.setFontforTextviews(prod_qty_head,"Roboto-Medium.ttf",act);
        fontSetting.setFontforTextviews(prod_sku_head,"Roboto-Medium.ttf",act);


        fontSetting.setFontforTextviews(prod_name,"Roboto-Regular.ttf",act);
        fontSetting.setFontforTextviews(prod_qty,"Roboto-Regular.ttf",act);
        fontSetting.setFontforTextviews(prod_sku,"Roboto-Regular.ttf",act);


        HashMap <String,String> order_prodlisting=new HashMap<String,String>();
        order_prodlisting=data.get(position);
        prod_name.setText(order_prodlisting.get(Ced_MultiVendor_ShipmentView.KEY_NAME));
        prod_qty.setText(order_prodlisting.get(Ced_MultiVendor_ShipmentView.KEY_QTY));
        prod_sku.setText(order_prodlisting.get(Ced_MultiVendor_ShipmentView.KEY_SKU));


        return convertView;
    }
}
