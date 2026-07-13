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

package magentoegypt.locafy.vendor_transaction;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import magentoegypt.locafy.R;
import magentoegypt.locafy.base_app.Ced_MultiVendor_FontSetting;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by developer on 5/5/16.
 */
public class Ced_MultiVendor_Transactionadapter extends BaseAdapter {
    private Context act;
    private ArrayList<HashMap<String, String>> data;
    private static LayoutInflater inflater = null;
    Ced_MultiVendor_FontSetting fontSetting;

    public Ced_MultiVendor_Transactionadapter(Activity viewTransaction, ArrayList<HashMap<String, String>> transaction_array) {

        act = viewTransaction;
        data = transaction_array;
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

    @SuppressLint("SetTextI18n")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            inflater = (LayoutInflater) act.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.ced_multivendor_transaction_list_item, null);

        }
        TextView payment_id = (TextView) convertView.findViewById(R.id.MultiVendor_payment_id);
        TextView transaction_date_header = (TextView) convertView.findViewById(R.id.MultiVendor_transaction_date_header);
        TextView CreatedAt = (TextView) convertView.findViewById(R.id.MultiVendor_CreatedAt);
        TextView TransactionId_head = (TextView) convertView.findViewById(R.id.MultiVendor_TransactionId_head);
        TextView TransactionId = (TextView) convertView.findViewById(R.id.MultiVendor_TransactionId);
        TextView Amount_head = (TextView) convertView.findViewById(R.id.MultiVendor_Amount_head);
        TextView Amount = (TextView) convertView.findViewById(R.id.MultiVendor_Amount);
        TextView AdjustmentAmount_head = (TextView) convertView.findViewById(R.id.MultiVendor_AdjustmentAmount_head);
        TextView AdjustmentAmount = (TextView) convertView.findViewById(R.id.MultiVendor_AdjustmentAmount);
        TextView NetAmount_head = (TextView) convertView.findViewById(R.id.MultiVendor_NetAmount_head);
        TextView NetAmount = (TextView) convertView.findViewById(R.id.MultiVendor_NetAmount);
        TextView PaymentMode_head = (TextView) convertView.findViewById(R.id.MultiVendor_PaymentMode_head);
        TextView PaymentMode = (TextView) convertView.findViewById(R.id.MultiVendor_PaymentMode);
        TextView MultiVendor_Amount_Description_Text = (TextView) convertView.findViewById(R.id.MultiVendor_Amount_Description_Text);
        TextView MultiVendor_Amount_Description_Label = (TextView) convertView.findViewById(R.id.MultiVendor_Amount_Description_Label);
        LinearLayout MultiVendor_Amount_Description_LL = (LinearLayout) convertView.findViewById(R.id.MultiVendor_Amount_Description_LL);

        fontSetting.setFontforTextviews(transaction_date_header, "Roboto-Medium.ttf", act);
        fontSetting.setFontforTextviews(TransactionId_head, "Roboto-Medium.ttf", act);
        fontSetting.setFontforTextviews(Amount_head, "Roboto-Medium.ttf", act);
        fontSetting.setFontforTextviews(AdjustmentAmount_head, "Roboto-Medium.ttf", act);
        fontSetting.setFontforTextviews(NetAmount_head, "Roboto-Medium.ttf", act);
        fontSetting.setFontforTextviews(PaymentMode_head, "Roboto-Medium.ttf", act);
        fontSetting.setFontforTextviews(MultiVendor_Amount_Description_Label, "Roboto-Bold.ttf", act);

        fontSetting.setFontforTextviews(CreatedAt, "Roboto-Regular.ttf", act);
        fontSetting.setFontforTextviews(TransactionId, "Roboto-Regular.ttf", act);
        fontSetting.setFontforTextviews(Amount, "Roboto-Regular.ttf", act);
        fontSetting.setFontforTextviews(AdjustmentAmount, "Roboto-Regular.ttf", act);
        fontSetting.setFontforTextviews(NetAmount, "Roboto-Regular.ttf", act);
        fontSetting.setFontforTextviews(PaymentMode, "Roboto-Regular.ttf", act);
        fontSetting.setFontforTextviews(PaymentMode, "Roboto-Regular.ttf", act);

        HashMap<String, String> trans = new HashMap<String, String>();
        trans = data.get(position);
        payment_id.setText(trans.get(Ced_MultiVendor_ListTransaction.KEY_PAYMENT_ID));
        CreatedAt.setText(trans.get(Ced_MultiVendor_ListTransaction.KEY_CREATED_AT));
        TransactionId.setText(trans.get(Ced_MultiVendor_ListTransaction.KEY_TRANSACTION_ID));
        Amount.setText(trans.get(Ced_MultiVendor_ListTransaction.KEY_AMOUNT));
        AdjustmentAmount.setText(trans.get(Ced_MultiVendor_ListTransaction.KEY_ADJUSTMENT_AMOUNT));
        NetAmount.setText(trans.get(Ced_MultiVendor_ListTransaction.KEY_NET_AMOUNT));
        PaymentMode.setText(trans.get(Ced_MultiVendor_ListTransaction.KEY_PAYMENT_MODE));
        String data = trans.get(Ced_MultiVendor_ListTransaction.KEY_AMOUNT_DESC);
        assert data != null;
        String data1 = data.replace(",O", "###");
        String[] arr = data1.split("###");
        for (int i = 0; i < arr.length; i++) {
            String arr1[] = arr[i].split(" ");
            LinearLayout linearLayout = new LinearLayout(act.getApplicationContext());
            linearLayout.setOrientation(LinearLayout.VERTICAL);
            TextView order_number = new TextView(act.getApplicationContext());
            TextView order_amount = new TextView(act.getApplicationContext());

            if (i == 0) order_number.setText(arr1[0]);
            else order_number.setText("O" + arr1[0]);

            order_amount.setText(Html.fromHtml("<b>Amount</b>  ") + arr1[3]);
            order_number.setTypeface(Typeface.DEFAULT_BOLD);
            order_number.setTextColor(act.getResources().getColor(R.color.black));
            order_number.setPadding(15, 0, 0, 0);

            order_amount.setTextColor(act.getResources().getColor(R.color.black));
            order_amount.setPadding(15, 5, 0, 5);

            linearLayout.addView(order_number, 0);
            linearLayout.addView(order_amount, 1);
            MultiVendor_Amount_Description_LL.addView(linearLayout);
        }

//        MultiVendor_Amount_Description_Text.setText(trans.get(Ced_MultiVendor_ListTransaction.KEY_AMOUNT_DESC));


        return convertView;
    }
}
