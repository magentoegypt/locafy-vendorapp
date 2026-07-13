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

package magentoegypt.locafy.vendor_dashboard;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import magentoegypt.locafy.dashboard_enhancements.Ced_Multivendor_bank_detials;
import magentoegypt.locafy.dashboard_enhancements.Ced_Multivendor_bussiness_details;
import magentoegypt.locafy.dashboard_enhancements.Ced_Multivendor_store_details;
import magentoegypt.locafy.base_app.Ced_MultiVendor_FontSetting;
import magentoegypt.locafy.base_app.Ced_MultiVendor_VendorSplash;
import magentoegypt.locafy.manage_orders.Ced_MultiVendor_ManageOrdersList;
import magentoegypt.locafy.vendor_reports_section.Ced_MultiVendor_ProductReport;
import magentoegypt.locafy.vendor_transaction.Ced_MultiVendor_ListTransaction;
import magentoegypt.locafy.R;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class Ced_MultiVendor_cardsAdapter extends PagerAdapter {
    Context context;
    LayoutInflater inflater;
    Activity activity;
    Ced_MultiVendor_FontSetting fontSetting;
    private ArrayList<HashMap<String, String>> data;

    public Ced_MultiVendor_cardsAdapter(Context context, ArrayList<HashMap<String, String>> img, Activity activity) {
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
        View itemView = inflater.inflate(R.layout.ced_multivendor_slider_layout, (ViewGroup) container, false);
        try {
            final RelativeLayout backgroundcolors = itemView.findViewById(R.id.MultiVendor_backgroundcolors);
            final CardView section = itemView.findViewById(R.id.MultiVendor_section);
            final ImageView icon = itemView.findViewById(R.id.MultiVendor_icon);
            final TextView tittle = itemView.findViewById(R.id.MultiVendor_tittle);
            final TextView hint = itemView.findViewById(R.id.MultiVendor_hint);
            final TextView link = itemView.findViewById(R.id.MultiVendor_link);
            fontSetting.setFontforTextviews(tittle, "Roboto-Medium.ttf", activity);
            fontSetting.setFontforTextviews(hint, "Roboto-Light.ttf", activity);

            backgroundcolors.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (link.getText().toString().equals("pending")) {
                        try {
                            JSONObject req = new JSONObject();
                            JSONObject purchasejsonObject = new JSONObject();
                            purchasejsonObject.put("from", "");
                            purchasejsonObject.put("to", "");
                            req.put("increment_id", "");
                            req.put("created_at", purchasejsonObject);
                            req.put("billing_name", "");
                            JSONObject grand_total_jsonObject = new JSONObject();
                            grand_total_jsonObject.put("from", "");
                            grand_total_jsonObject.put("to", "");
                            req.put("order_total", grand_total_jsonObject);
                            JSONObject commission_jsonObject = new JSONObject();
                            commission_jsonObject.put("from", "");
                            commission_jsonObject.put("to", "");
                            req.put("shop_commission_fee", commission_jsonObject);
                            JSONObject netearned_jsonObject = new JSONObject();
                            netearned_jsonObject.put("from", "");
                            netearned_jsonObject.put("to", "");
                            req.put("net_vendor_earn", netearned_jsonObject);
                            req.put("order_payment_state", "Paid");
                            req.put("payment_state", "Pending");
                            Intent intent = new Intent(context, Ced_MultiVendor_ListTransaction.class);
                            //  intent.putExtra("filter", req.toString());
                            activity.startActivity(intent);
                            activity.overridePendingTransition(R.anim.ced_multivendor_slide_in, R.anim.ced_multivendor_slide_out);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    if (link.getText().toString().equals("earned")) {
                        Intent intent = new Intent(activity, Ced_MultiVendor_ListTransaction.class);
                        activity.startActivity(intent);
                        activity.overridePendingTransition(R.anim.ced_multivendor_slide_in, R.anim.ced_multivendor_slide_out);
                    }
                    if (link.getText().toString().equals("orderplace")) {
                        Intent intent = new Intent(activity, Ced_MultiVendor_ManageOrdersList.class);
                        activity.startActivity(intent);
                        activity.overridePendingTransition(R.anim.ced_multivendor_slide_in, R.anim.ced_multivendor_slide_out);
                    }
                    if (link.getText().toString().equals("sold")) {
                        Intent intent = new Intent(activity, Ced_MultiVendor_ProductReport.class);
                        activity.startActivity(intent);
                        activity.overridePendingTransition(R.anim.ced_multivendor_slide_in, R.anim.ced_multivendor_slide_out);
                    }

                    if (link.getText().toString().equals("store_details")) {
                        Intent intent = new Intent(activity, Ced_Multivendor_store_details.class);
                        activity.startActivity(intent);
                        activity.overridePendingTransition(R.anim.ced_multivendor_slide_in, R.anim.ced_multivendor_slide_out);
                    }

                    if (link.getText().toString().equals("bank_details")) {
                        Intent intent = new Intent(activity, Ced_Multivendor_bank_detials.class);
                        activity.startActivity(intent);
                        activity.overridePendingTransition(R.anim.ced_multivendor_slide_in, R.anim.ced_multivendor_slide_out);
                    }

                    if (link.getText().toString().equals("business_details")) {
                        Intent intent = new Intent(activity, Ced_Multivendor_bussiness_details.class);
                        activity.startActivity(intent);
                        activity.overridePendingTransition(R.anim.ced_multivendor_slide_in, R.anim.ced_multivendor_slide_out);
                    }
                }
            });

            HashMap<String, String> song = new HashMap<String, String>();
            song = data.get(position);
            ((ViewPager) container).addView(itemView);
            tittle.setText(song.get("title"));
            link.setText(song.get("link"));
            hint.setText(song.get("hint"));

            if (link.getText().toString().equals("pending")) {
                backgroundcolors.setBackgroundColor(activity.getResources().getColor(R.color.pendingcard));
                icon.setImageDrawable(activity.getResources().getDrawable(R.drawable.pendingcard));
                section.setCardBackgroundColor(activity.getResources().getColor(R.color.pendingcard));
            }
            if (link.getText().toString().equals("earned")) {
                backgroundcolors.setBackgroundColor(activity.getResources().getColor(R.color.earnedcard));
                icon.setImageDrawable(activity.getResources().getDrawable(R.drawable.earnedcard));
                section.setCardBackgroundColor(activity.getResources().getColor(R.color.earnedcard));
            }
            if (link.getText().toString().equals("orderplace")) {
                backgroundcolors.setBackgroundColor(activity.getResources().getColor(R.color.ordercard));
                icon.setImageDrawable(activity.getResources().getDrawable(R.drawable.ordercard));
                section.setCardBackgroundColor(activity.getResources().getColor(R.color.ordercard));
            }
            if (link.getText().toString().equals("sold")) {
                backgroundcolors.setBackgroundColor(activity.getResources().getColor(R.color.soldcard));
                icon.setImageDrawable(activity.getResources().getDrawable(R.drawable.barchartcart));
                section.setCardBackgroundColor(activity.getResources().getColor(R.color.soldcard));

            }
            if (link.getText().toString().equals("dashboard")) {
                backgroundcolors.setBackgroundColor(activity.getResources().getColor(R.color.secondary_color));
                section.setCardBackgroundColor(activity.getResources().getColor(R.color.secondary_color));
            }

            if (link.getText().toString().equals("business_details")) {
                backgroundcolors.setBackgroundColor(activity.getResources().getColor(R.color.white));
                section.setCardBackgroundColor(activity.getResources().getColor(R.color.white));
                icon.setImageDrawable(activity.getResources().getDrawable(R.drawable.business_details));
                tittle.setTextColor(activity.getResources().getColor(R.color.badge));
                tittle.setTextSize(30);
                hint.setTextColor(activity.getResources().getColor(R.color.badge));
            }

            if (link.getText().toString().equals("bank_details")) {
                backgroundcolors.setBackgroundColor(activity.getResources().getColor(R.color.white));
                section.setCardBackgroundColor(activity.getResources().getColor(R.color.white));
                icon.setImageDrawable(activity.getResources().getDrawable(R.drawable.bank_details));
                tittle.setTextColor(activity.getResources().getColor(R.color.badge));
                tittle.setTextSize(30);
                hint.setTextColor(activity.getResources().getColor(R.color.badge));
            }

            if (link.getText().toString().equals("store_details")) {
                backgroundcolors.setBackgroundColor(activity.getResources().getColor(R.color.white));
                section.setCardBackgroundColor(activity.getResources().getColor(R.color.white));
                icon.setImageDrawable(activity.getResources().getDrawable(R.drawable.store_details));
                tittle.setTextColor(activity.getResources().getColor(R.color.badge));
                tittle.setTextSize(30);
                hint.setTextColor(activity.getResources().getColor(R.color.badge));
            }

        } catch (Exception e) {
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
        container.removeView((CardView) object);
    }
}
