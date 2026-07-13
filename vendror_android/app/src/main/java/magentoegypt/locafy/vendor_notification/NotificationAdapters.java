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

package magentoegypt.locafy.vendor_notification;

import android.annotation.SuppressLint;
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

public class NotificationAdapters extends BaseAdapter {
    private Activity activity;
    private ArrayList<HashMap<String, String>> data;
    private static LayoutInflater inflater = null;
    Ced_MultiVendor_FontSetting fontSetting;

    static class ViewHolder {
        public TextView link, order_link;
        public TextView tag;


    }

    public NotificationAdapters(Activity a, ArrayList<HashMap<String, String>> d) {
        activity = a;
        data = d;
        inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        fontSetting = new Ced_MultiVendor_FontSetting();

    }

    public int getCount() {
        return data.size();
    }

    public Object getItem(int position) {

        return position;
    }

    public long getItemId(int position) {
        return position;
    }

    @SuppressLint("NewApi")
    public View getView(int position, View convertView, ViewGroup parent) {
        View vi = convertView;
        if (vi == null) {
            vi = inflater.inflate(R.layout.ced_multivendor_notificationlist, null);
            ViewHolder viewHolder = new ViewHolder();
            viewHolder.link = (TextView) vi.findViewById(R.id.MultiVendor_link);
            viewHolder.order_link = (TextView) vi.findViewById(R.id.MultiVendor_link_order);
            viewHolder.tag = (TextView) vi.findViewById(R.id.MultiVendor_tag);
            fontSetting.setFontforTextviews(viewHolder.tag, "Roboto-Medium.ttf", activity);
            fontSetting.setFontforTextviews(viewHolder.link, "Roboto-Medium.ttf", activity);
            fontSetting.setFontforTextviews(viewHolder.order_link, "Roboto-Medium.ttf", activity);
            vi.setTag(viewHolder);
        }
        HashMap<String, String> song = new HashMap<String, String>();
        song = data.get(position);
        final ViewHolder holder = (ViewHolder) vi.getTag();
        holder.link.setText(song.get("link_to"));
        holder.tag.setText(song.get("tag"));
        try {
            holder.order_link.setText(song.get("link_to_order"));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return vi;
    }

    @Override
    public boolean isEnabled(int position) {
        return true;
    }
}