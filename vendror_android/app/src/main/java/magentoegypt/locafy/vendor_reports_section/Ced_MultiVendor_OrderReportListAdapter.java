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

package magentoegypt.locafy.vendor_reports_section;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import magentoegypt.locafy.base_app.Ced_MultiVendor_FontSetting;
import magentoegypt.locafy.R;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by developer on 10/5/16.
 */
public class Ced_MultiVendor_OrderReportListAdapter extends BaseAdapter {
    private static LayoutInflater inflater = null;
    Ced_MultiVendor_OrderReport_list act;
    Ced_MultiVendor_FontSetting fontSetting;
    private ArrayList<HashMap<String, String>> data;

    public Ced_MultiVendor_OrderReportListAdapter(Ced_MultiVendor_OrderReport_list orderReport_list, ArrayList<HashMap<String, String>> orderinfo) {
        act = orderReport_list;
        data = orderinfo;
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
            convertView = inflater.inflate(R.layout.ced_multivendor_orderreport_list_item, null);
        }
        TextView Period_head = convertView.findViewById(R.id.MultiVendor_Period_head);
        TextView Period = convertView.findViewById(R.id.MultiVendor_Period);
        TextView Orders_head = convertView.findViewById(R.id.MultiVendor_Orders_head);
        TextView Orders = convertView.findViewById(R.id.MultiVendor_Orders);
        TextView SalesItems_head = convertView.findViewById(R.id.MultiVendor_SalesItems_head);
        TextView SalesItems = convertView.findViewById(R.id.MultiVendor_SalesItems);
        TextView TotalSales_head = convertView.findViewById(R.id.MultiVendor_TotalSales_head);
        TextView TotalSales = convertView.findViewById(R.id.MultiVendor_TotalSales);
        TextView TotalCommission_head = convertView.findViewById(R.id.MultiVendor_TotalCommission_head);
        TextView TotalCommission = convertView.findViewById(R.id.MultiVendor_TotalCommission);
        TextView NetSales_head = convertView.findViewById(R.id.MultiVendor_NetSales_head);
        TextView NetSales = convertView.findViewById(R.id.MultiVendor_NetSales);
        fontSetting.setFontforTextviews(Period_head, "Roboto-Medium.ttf", act);
        fontSetting.setFontforTextviews(Orders_head, "Roboto-Medium.ttf", act);
        fontSetting.setFontforTextviews(SalesItems_head, "Roboto-Medium.ttf", act);
        fontSetting.setFontforTextviews(TotalSales_head, "Roboto-Medium.ttf", act);
        fontSetting.setFontforTextviews(TotalCommission_head, "Roboto-Medium.ttf", act);
        fontSetting.setFontforTextviews(NetSales_head, "Roboto-Medium.ttf", act);

        fontSetting.setFontforTextviews(Period, "Roboto-Regular.ttf", act);
        fontSetting.setFontforTextviews(Orders, "Roboto-Regular.ttf", act);
        fontSetting.setFontforTextviews(SalesItems, "Roboto-Regular.ttf", act);
        fontSetting.setFontforTextviews(TotalSales, "Roboto-Regular.ttf", act);
        fontSetting.setFontforTextviews(TotalCommission, "Roboto-Regular.ttf", act);
        fontSetting.setFontforTextviews(NetSales, "Roboto-Regular.ttf", act);

        HashMap<String, String> orderlisting = new HashMap<String, String>();
        orderlisting = data.get(position);
        Period.setText(orderlisting.get(Ced_MultiVendor_OrderReport_list.KEY_PERIOD));
        Orders.setText(orderlisting.get(Ced_MultiVendor_OrderReport_list.KEY_ORDERS));
        SalesItems.setText(orderlisting.get(Ced_MultiVendor_OrderReport_list.KEY_SALES_ITEMS));
        TotalSales.setText(orderlisting.get(Ced_MultiVendor_OrderReport_list.KEY_TOTAL_SALES));
        TotalCommission.setText(orderlisting.get(Ced_MultiVendor_OrderReport_list.KEY_TOTAL_COMMISION));
        NetSales.setText(orderlisting.get(Ced_MultiVendor_OrderReport_list.KEY_NET_SALES));
        return convertView;
    }
}
