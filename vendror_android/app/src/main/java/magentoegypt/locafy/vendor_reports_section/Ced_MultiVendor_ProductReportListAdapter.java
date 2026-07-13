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

import magentoegypt.locafy.R;
import magentoegypt.locafy.base_app.Ced_MultiVendor_FontSetting;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by developer on 10/5/16.
 */
public class Ced_MultiVendor_ProductReportListAdapter extends BaseAdapter{
    Activity act;
    private ArrayList<HashMap<String,String>> data;
    private static LayoutInflater inflater=null;
    Ced_MultiVendor_FontSetting fontSetting;
    public Ced_MultiVendor_ProductReportListAdapter(Ced_MultiVendor_ProductReport_list productReport_list, ArrayList<HashMap<String, String>> productinfo) {
        act=productReport_list;
        data=productinfo;
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
            convertView=inflater.inflate(R.layout.ced_multivendor_productreport_list_item,null);
        }

        TextView SalesItems_head= (TextView) convertView.findViewById(R.id.MultiVendor_SalesItems_head);
        TextView SalesItems= (TextView) convertView.findViewById(R.id.MultiVendor_SalesItems);
        TextView TotalSales_head= (TextView) convertView.findViewById(R.id.MultiVendor_TotalSales_head);
        TextView TotalSales= (TextView) convertView.findViewById(R.id.MultiVendor_TotalSales);
        TextView product_name_head= (TextView) convertView.findViewById(R.id.MultiVendor_product_name_head);
        TextView product_name= (TextView) convertView.findViewById(R.id.MultiVendor_product_name);
        TextView SKU_head= (TextView) convertView.findViewById(R.id.MultiVendor_SKU_head);
        TextView SKU= (TextView) convertView.findViewById(R.id.MultiVendor_SKU);

        fontSetting.setFontforTextviews(SalesItems_head,"Roboto-Medium.ttf", act);
        fontSetting.setFontforTextviews(TotalSales_head,"Roboto-Medium.ttf", act);
        fontSetting.setFontforTextviews(SKU_head,"Roboto-Medium.ttf", act);
        fontSetting.setFontforTextviews(product_name_head,"Roboto-Medium.ttf", act);

        fontSetting.setFontforTextviews(SalesItems,"Roboto-Regular.ttf", act);
        fontSetting.setFontforTextviews(TotalSales,"Roboto-Regular.ttf", act);
        fontSetting.setFontforTextviews(product_name,"Roboto-Regular.ttf", act);
        fontSetting.setFontforTextviews(SKU,"Roboto-Regular.ttf",act);

        HashMap <String,String> prodlisting=new HashMap<String,String>();
        prodlisting=data.get(position);
        SalesItems.setText(prodlisting.get(Ced_MultiVendor_ProductReport_list.KEY_SALES_ITEM));
        TotalSales.setText(prodlisting.get(Ced_MultiVendor_ProductReport_list.KEY_TOTAL_SALES));
        product_name.setText(prodlisting.get(Ced_MultiVendor_ProductReport_list.KEY_PRODUCT_NAME));
        SKU.setText(prodlisting.get(Ced_MultiVendor_ProductReport_list.KEY_SKU));

        return convertView;
    }
}
