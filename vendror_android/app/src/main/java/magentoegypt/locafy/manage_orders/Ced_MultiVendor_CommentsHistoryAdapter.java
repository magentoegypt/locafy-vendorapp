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
 * Created by developer on 28/6/16.
 */
public class Ced_MultiVendor_CommentsHistoryAdapter extends BaseAdapter {
    private static LayoutInflater inflater = null;
    Activity act;
    Ced_MultiVendor_FontSetting fontSetting;
    private final ArrayList<HashMap<String, String>> data;

    public Ced_MultiVendor_CommentsHistoryAdapter(Activity commentsHistory, ArrayList<HashMap<String, String>> invoiceinfo) {
        act = commentsHistory;
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
            convertView = inflater.inflate(R.layout.ced_multivendor_comment_list_item, null);
        }
        TextView created_at_txt = (TextView) convertView.findViewById(R.id.MultiVendor_created_at_txt);
        TextView created_at = (TextView) convertView.findViewById(R.id.MultiVendor_created_at);
        TextView notified_txt = (TextView) convertView.findViewById(R.id.MultiVendor_notified_txt);
        TextView notified = (TextView) convertView.findViewById(R.id.MultiVendor_notified);
        TextView title_text = (TextView) convertView.findViewById(R.id.MultiVendor_title_text);
        TextView title = (TextView) convertView.findViewById(R.id.MultiVendor_title);
        TextView comment_text = (TextView) convertView.findViewById(R.id.MultiVendor_comment_text);
        TextView comment = (TextView) convertView.findViewById(R.id.MultiVendor_comment);

        fontSetting.setFontforTextviews(created_at_txt, "Roboto-Medium.ttf", act);
        fontSetting.setFontforTextviews(notified_txt, "Roboto-Medium.ttf", act);
        fontSetting.setFontforTextviews(title_text, "Roboto-Medium.ttf", act);
        fontSetting.setFontforTextviews(comment_text, "Roboto-Medium.ttf", act);
        fontSetting.setFontforTextviews(created_at, "Roboto-Regular.ttf", act);
        fontSetting.setFontforTextviews(notified, "Roboto-Regular.ttf", act);
        fontSetting.setFontforTextviews(title, "Roboto-Regular.ttf", act);
        fontSetting.setFontforTextviews(comment, "Roboto-Regular.ttf", act);

        HashMap<String, String> commenting = new HashMap<String, String>();
        commenting = data.get(position);
        created_at.setText(commenting.get(Ced_MultiVendor_CommentsHistory.KEY_CREATED_AT));
        notified.setText(commenting.get(Ced_MultiVendor_CommentsHistory.KEY_NOTIFIED));
        title.setText(commenting.get(Ced_MultiVendor_CommentsHistory.KEY_TITLE));
        comment.setText(commenting.get(Ced_MultiVendor_CommentsHistory.KEY_COMMENT));

        return convertView;
    }
}