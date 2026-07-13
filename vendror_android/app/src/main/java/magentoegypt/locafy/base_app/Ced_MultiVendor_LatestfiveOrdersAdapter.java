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

package magentoegypt.locafy.base_app;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import magentoegypt.locafy.manage_orders.Ced_MultiVendor_ManageOrderview;
import magentoegypt.locafy.R;
import magentoegypt.locafy_constant.AppConstant;

import java.util.ArrayList;
import java.util.HashMap;

public class Ced_MultiVendor_LatestfiveOrdersAdapter extends PagerAdapter {
    Context context;
    LayoutInflater inflater;
    Activity activity;
    Ced_MultiVendor_FontSetting fontSetting;
    private ArrayList<HashMap<String, String>> data;

    public Ced_MultiVendor_LatestfiveOrdersAdapter(Context context, ArrayList<HashMap<String, String>> img, Activity activity) {
        this.context = context;
        data = img;
        this.activity = activity;
        fontSetting = new Ced_MultiVendor_FontSetting();

    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object instantiateItem(final View container, final int position) {

        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView = inflater.inflate(R.layout.ced_multivendor_vendorlatestfiveorder_comp_list, (ViewGroup) container, false);
        try {
            final LinearLayout order = itemView.findViewById(R.id.MultiVendor_order);
            final CardView section = itemView.findViewById(R.id.MultiVendor_section);
            View back = itemView.findViewById(R.id.MultiVendor_viewback);
            final TextView orderIdTag = itemView.findViewById(R.id.MultiVendor_orderIdTag);
            final TextView orderId = itemView.findViewById(R.id.MultiVendor_orderId);
            final TextView nerearnedTag = itemView.findViewById(R.id.MultiVendor_nerearnedTag);
            final TextView nerearned = itemView.findViewById(R.id.MultiVendor_nerearned);
            final TextView orderstattusTag = itemView.findViewById(R.id.MultiVendor_orderstattusTag);
            final TextView orderstattus = itemView.findViewById(R.id.MultiVendor_orderstattus);
            final TextView billingnameTag = itemView.findViewById(R.id.MultiVendor_billingnameTag);
            final TextView billingname = itemView.findViewById(R.id.MultiVendor_billingname);
            final TextView purchasedonTag = itemView.findViewById(R.id.MultiVendor_purchasedonTag);
            final TextView purchasedon = itemView.findViewById(R.id.MultiVendor_purchasedon);
            /**************************Regular*****************************/
            fontSetting.setFontforTextviews(orderId, "Roboto-Medium.ttf", activity);
            fontSetting.setFontforTextviews(purchasedon, "Roboto-Regular.ttf", activity);
            fontSetting.setFontforTextviews(billingname, "Roboto-Regular.ttf", activity);
            fontSetting.setFontforTextviews(orderstattus, "Roboto-Medium.ttf", activity);
            fontSetting.setFontforTextviews(nerearned, "Roboto-Regular.ttf", activity);
            /******************************Regular*************************/
            /**************************Medium*****************************/
            fontSetting.setFontforTextviews(orderIdTag, "Roboto-Medium.ttf", activity);
            fontSetting.setFontforTextviews(purchasedonTag, "Roboto-Medium.ttf", activity);
            fontSetting.setFontforTextviews(nerearnedTag, "Roboto-Medium.ttf", activity);
            fontSetting.setFontforTextviews(orderstattusTag, "Roboto-Medium.ttf", activity);
            fontSetting.setFontforTextviews(billingnameTag, "Roboto-Medium.ttf", activity);
            /***************************Medium****************************/

            order.setOnClickListener(v -> {
                AppConstant.lockButton(v);
                String orderid = orderId.getText().toString();
                String[] parts = orderid.split("#");
                Intent intent = new Intent(activity, Ced_MultiVendor_ManageOrderview.class);
                intent.putExtra("order_id", parts[1]);
                activity.startActivity(intent);
                activity.overridePendingTransition(R.anim.ced_multivendor_slide_in, R.anim.ced_multivendor_slide_out);

            });


            HashMap<String, String> song = new HashMap<String, String>();
            song = data.get(position);
            ((ViewPager) container).addView(itemView);
            orderId.setText(song.get("order_id"));
            purchasedon.setText(song.get("purchase_on"));
            billingname.setText(song.get("billing_name"));
            nerearned.setText(song.get("net_earned"));
            orderstattus.setText("#" + song.get("order_status").toUpperCase());
            orderstattus.setTextColor(activity.getResources().getColor(R.color.AppTheme));
            if (song.get("order_status").equals("closed")) {
                back.setBackgroundColor(activity.getResources().getColor(R.color.closedback));
                orderstattus.setTextColor(activity.getResources().getColor(R.color.closedback));
            }
            if (song.get("order_status").equals("pending")) {
                back.setBackgroundColor(activity.getResources().getColor(R.color.closedback));
                orderstattus.setTextColor(activity.getResources().getColor(R.color.closedback));
            }
            if (song.get("order_status").equals("processing")) {
                back.setBackgroundColor(activity.getResources().getColor(R.color.processingback));
                orderstattus.setTextColor(activity.getResources().getColor(R.color.processingback));
            }
            if (song.get("order_status").equals("complete")) {
                back.setBackgroundColor(activity.getResources().getColor(R.color.completeback));
                orderstattus.setTextColor(activity.getResources().getColor(R.color.completeback));
            }
            if (song.get("order_status").equals("canceled")) {
                back.setBackgroundColor(activity.getResources().getColor(R.color.cancledback));
                orderstattus.setTextColor(activity.getResources().getColor(R.color.cancledback));
            }
            if (song.get("order_status").equals("holded")) {
                back.setBackgroundColor(activity.getResources().getColor(R.color.holded));
                orderstattus.setTextColor(activity.getResources().getColor(R.color.holded));
            }
        }
        catch (Exception e) {
            Intent intent = new Intent(context, Ced_MultiVendor_VendorSplash.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
            activity.overridePendingTransition(R.anim.ced_multivendor_slide_in, R.anim.ced_multivendor_slide_out);
        }

        return itemView;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((RelativeLayout) object);
    }
}