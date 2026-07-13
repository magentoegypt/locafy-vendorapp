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
public class Ced_MultiVendor_ShipmentListingAdapter extends BaseAdapter {
    Activity act;
    Ced_MultiVendor_FontSetting fontSetting;
    private ArrayList<HashMap<String, String>> data;
    private static LayoutInflater inflater = null;

    public Ced_MultiVendor_ShipmentListingAdapter(Activity shipmentListing, ArrayList<HashMap<String, String>> shippinginfo) {
        act = shipmentListing;
        data = shippinginfo;
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
            convertView = inflater.inflate(R.layout.ced_multivendor_shipment_item_list, null);
        }
        TextView Shipment_id_txt = (TextView) convertView.findViewById(R.id.MultiVendor_Shipment_id_txt);
        TextView Shipment_id = (TextView) convertView.findViewById(R.id.MultiVendor_Shipment_id);
        TextView shipment_date_txt = (TextView) convertView.findViewById(R.id.MultiVendor_shipment_date_txt);
        TextView shipment_date = (TextView) convertView.findViewById(R.id.MultiVendor_shipment_date);
        TextView ship_to = (TextView) convertView.findViewById(R.id.MultiVendor_ship_to);
        TextView prodname = (TextView) convertView.findViewById(R.id.MultiVendor_prodname);
        TextView orderid_txt = (TextView) convertView.findViewById(R.id.MultiVendor_orderid_txt);
        TextView order_id = (TextView) convertView.findViewById(R.id.MultiVendor_order_id);
        TextView orderdate_txt = (TextView) convertView.findViewById(R.id.MultiVendor_orderdate_txt);
        TextView orderdate = (TextView) convertView.findViewById(R.id.MultiVendor_orderdate);
        TextView shiping_qty_head = convertView.findViewById(R.id.shiping_qty_head);
        TextView shiping_qty_txt = convertView.findViewById(R.id.shiping_qty_txt);

        fontSetting.setFontforTextviews(ship_to, "Roboto-Medium.ttf", act);
        fontSetting.setFontforTextviews(shiping_qty_head, "Roboto-Medium.ttf", act);
        fontSetting.setFontforTextviews(Shipment_id_txt, "Roboto-Medium.ttf", act);
        fontSetting.setFontforTextviews(shipment_date_txt, "Roboto-Medium.ttf", act);
        fontSetting.setFontforTextviews(orderid_txt, "Roboto-Medium.ttf", act);
        fontSetting.setFontforTextviews(orderdate_txt, "Roboto-Medium.ttf", act);

        fontSetting.setFontforTextviews(Shipment_id, "Roboto-Regular.ttf", act);
        fontSetting.setFontforTextviews(shipment_date, "Roboto-Regular.ttf", act);
        fontSetting.setFontforTextviews(order_id, "Roboto-Regular.ttf", act);
        fontSetting.setFontforTextviews(prodname, "Roboto-Regular.ttf", act);
        fontSetting.setFontforTextviews(orderdate, "Roboto-Regular.ttf", act);

        HashMap<String, String> shipmentlisting = new HashMap<String, String>();
        shipmentlisting = data.get(position);

        Shipment_id.setText(shipmentlisting.get(Ced_MultiVendor_ShipmentListing.KEY_INCREMENT_ID));
        shipment_date.setText(shipmentlisting.get(Ced_MultiVendor_ShipmentListing.KEY_CREATED_AT));
        prodname.setText(shipmentlisting.get(Ced_MultiVendor_ShipmentListing.KEY_BILLING_NAME));
        order_id.setText(shipmentlisting.get(Ced_MultiVendor_ShipmentListing.KEY_ORDER_INCREMENT_ID));
        orderdate.setText(shipmentlisting.get(Ced_MultiVendor_ShipmentListing.KEY_ORDER_CREATED_AT));
        shiping_qty_txt.setText(shipmentlisting.get(Ced_MultiVendor_ShipmentListing.TOTAL_QTY));

        return convertView;
    }
}
