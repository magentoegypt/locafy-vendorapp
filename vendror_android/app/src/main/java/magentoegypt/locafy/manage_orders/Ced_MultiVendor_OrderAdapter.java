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
import android.text.Html;
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
 * Created by developer on 2/5/16.
 */
public class Ced_MultiVendor_OrderAdapter extends BaseAdapter {
    Activity act;
    Ced_MultiVendor_FontSetting fontSetting;
    private ArrayList<HashMap<String,String>> data;
    private static LayoutInflater inflater=null;
    public Ced_MultiVendor_OrderAdapter(Activity ordersList, ArrayList<HashMap<String, String>> orderinfo) {
        act=ordersList;
        data=orderinfo;
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
            convertView=inflater.inflate(R.layout.ced_multivendor_order_list_item,null);
        }
        TextView ship_to= (TextView) convertView.findViewById(R.id.MultiVendor_ship_to);
        TextView date= (TextView) convertView.findViewById(R.id.MultiVendor_order_date);
        TextView date_txt= (TextView) convertView.findViewById(R.id.MultiVendor_date_txt);
        TextView prod_name= (TextView) convertView.findViewById(R.id.MultiVendor_prodname);
        TextView order_id= (TextView) convertView.findViewById(R.id.MultiVendor_order_id);
        TextView price_txt= (TextView) convertView.findViewById(R.id.MultiVendor_price_txt);
        TextView status= (TextView) convertView.findViewById(R.id.MultiVendor_order_status);
        TextView amount= (TextView) convertView.findViewById(R.id.MultiVendor_order_amount);
        TextView order_primaryid= (TextView) convertView.findViewById(R.id.MultiVendor_order_primaryid);
        TextView orderid_txt= (TextView) convertView.findViewById(R.id.MultiVendor_orderid_txt);
        TextView commission_amount_text= (TextView) convertView.findViewById(R.id.MultiVendor_commission_amount_text);
        TextView commission_amount= (TextView) convertView.findViewById(R.id.MultiVendor_commission_amount);
        TextView netearned_text= (TextView) convertView.findViewById(R.id.MultiVendor_netearned_text);
        TextView netearned= (TextView) convertView.findViewById(R.id.MultiVendor_netearned);
        TextView vendorpaymentstatus_text= (TextView) convertView.findViewById(R.id.MultiVendor_vendorpaymentstatus_text);
        TextView vendorpaymentstatus= (TextView) convertView.findViewById(R.id.MultiVendor_vendorpaymentstatus);
        TextView order_pay_status= (TextView) convertView.findViewById(R.id.MultiVendor_order_pay_status);
        TextView storename_txt= (TextView) convertView.findViewById(R.id.MultiVendor_storename_txt);
        TextView storename= (TextView) convertView.findViewById(R.id.MultiVendor_storename);

        HashMap <String,String> orderlisting=new HashMap<String,String>();
        orderlisting=data.get(position);

        fontSetting.setFontforTextviews(ship_to,"Roboto-Medium.ttf",act);
        fontSetting.setFontforTextviews(date_txt,"Roboto-Medium.ttf",act);
        fontSetting.setFontforTextviews(price_txt,"Roboto-Medium.ttf",act);
        fontSetting.setFontforTextviews(orderid_txt,"Roboto-Medium.ttf",act);
        fontSetting.setFontforTextviews(commission_amount_text,"Roboto-Medium.ttf",act);
        fontSetting.setFontforTextviews(netearned_text,"Roboto-Medium.ttf",act);
        fontSetting.setFontforTextviews(vendorpaymentstatus_text,"Roboto-Medium.ttf",act);
        fontSetting.setFontforTextviews(order_pay_status,"Roboto-Medium.ttf",act);
        fontSetting.setFontforTextviews(storename_txt,"Roboto-Medium.ttf",act);

        fontSetting.setFontforTextviews(date,"Roboto-Regular.ttf",act);
        fontSetting.setFontforTextviews(prod_name,"Roboto-Regular.ttf",act);
        fontSetting.setFontforTextviews(order_id,"Roboto-Regular.ttf",act);
        fontSetting.setFontforTextviews(status,"Roboto-Regular.ttf",act);
        fontSetting.setFontforTextviews(amount,"Roboto-Regular.ttf",act);
        fontSetting.setFontforTextviews(commission_amount,"Roboto-Regular.ttf",act);
        fontSetting.setFontforTextviews(netearned,"Roboto-Regular.ttf",act);
        fontSetting.setFontforTextviews(vendorpaymentstatus, "Roboto-Regular.ttf", act);
        fontSetting.setFontforTextviews(storename, "Roboto-Regular.ttf", act);


        date.setText(orderlisting.get(Ced_MultiVendor_OrdersList.KEY_CREATED_AT));
        prod_name.setText(orderlisting.get(Ced_MultiVendor_OrdersList.KEY_BILLING_NAME));
        order_id.setText(orderlisting.get(Ced_MultiVendor_OrdersList.KEY_ORDER_ID));
        status.setText(orderlisting.get(Ced_MultiVendor_OrdersList.KEY_ORDER_PAYMENT_STATE));
        amount.setText(orderlisting.get(Ced_MultiVendor_OrdersList.KEY_GRAND_TOTAL));
        commission_amount.setText(orderlisting.get(Ced_MultiVendor_OrdersList.KEY_COMMISSION));
        netearned.setText(orderlisting.get(Ced_MultiVendor_OrdersList.KEY_NET_Earned));
        vendorpaymentstatus.setText(orderlisting.get(Ced_MultiVendor_OrdersList.KEY_PAYMENT_STATE));
        order_primaryid.setText(orderlisting.get(Ced_MultiVendor_OrdersList.KEY_ID));
        if (!(orderlisting.get(Ced_MultiVendor_OrdersList.KEY_STORE_NAME)== null))
        {
            storename.setText(Html.fromHtml(orderlisting.get(Ced_MultiVendor_OrdersList.KEY_STORE_NAME)));
        }
        else
        {
            storename_txt.setVisibility(View.GONE);
            storename.setVisibility(View.GONE);
        }

        return convertView;
    }
}
