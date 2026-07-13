package magentoegypt.locafy.vendor_transaction.adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import magentoegypt.locafy.R;
import magentoegypt.locafy.base_app.Ced_MultiVendor_FontSetting;
import magentoegypt.locafy.vendor_transaction.Ced_MultiVendor_ViewTransaction;
import magentoegypt.locafy.vendor_transaction.model.listing.TransactionDataModel;

import java.util.List;

public class Ced_MultiVendor_TransactionListingAdapter extends RecyclerView.Adapter<Ced_MultiVendor_TransactionListingAdapter.ViewHolder> {

    private Activity act;
    public List<TransactionDataModel> data;
    Ced_MultiVendor_FontSetting fontSetting;

    public Ced_MultiVendor_TransactionListingAdapter(Activity context, List<TransactionDataModel> data) {
        this.act = context;
        this.data = data;
        fontSetting = new Ced_MultiVendor_FontSetting();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.ced_multivendor_transaction_list_item, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        try {
            TransactionDataModel transactionDataModel = data.get(i);
            viewHolder.payment_id.setText(transactionDataModel.getPaymentId());
            viewHolder.CreatedAt.setText(transactionDataModel.getCreatedAt());
            viewHolder.TransactionId.setText(transactionDataModel.getTransactionId());
            viewHolder.Amount.setText(transactionDataModel.getAmount());
            viewHolder.AdjustmentAmount.setText(transactionDataModel.getAdjustmentAmount());
            viewHolder.NetAmount.setText(transactionDataModel.getNetAmount());
            viewHolder.PaymentMode.setText(transactionDataModel.getPaymentMode());
            viewHolder.AmountValue.setText(transactionDataModel.getAmountDesc());


            /*String data = transactionDataModel.getAmountDesc();
            assert data != null;
            String data1 = data.replace(",O", "###");
            String[] arr = data1.split("###");
            for (int j = 0; j < arr.length; j++) {
                String arr1[] = arr[j].split(" ");
                LinearLayout linearLayout = new LinearLayout(act.getApplicationContext());
                linearLayout.setOrientation(LinearLayout.VERTICAL);
                TextView order_number = new TextView(act.getApplicationContext());
                TextView order_amount = new TextView(act.getApplicationContext());

                if (j == 0) order_number.setText(arr1[0]);
                else order_number.setText("O" + arr1[0]);
                order_amount.setText(Html.fromHtml("<b>Amount</b>  ") + arr1[3]);
                order_number.setTypeface(Typeface.DEFAULT_BOLD);
                order_number.setTextColor(act.getResources().getColor(R.color.black));
                order_number.setPadding(15, 0, 0, 0);

                order_amount.setTextColor(act.getResources().getColor(R.color.black));
                order_amount.setPadding(15, 5, 0, 5);

                linearLayout.addView(order_number, 0);
                linearLayout.addView(order_amount, 1);
                viewHolder.MultiVendor_Amount_Description_LL.addView(linearLayout);
            }*/

            viewHolder.parent_item_view.setOnClickListener(view -> {
                TextView postid = view.findViewById(R.id.MultiVendor_payment_id);
                Intent intent = new Intent(act, Ced_MultiVendor_ViewTransaction.class);
                intent.putExtra("payment_id", postid.getText().toString());
                act.startActivity(intent);
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView payment_id, transaction_date_header, CreatedAt, TransactionId_head,
                TransactionId, Amount_head, Amount, AdjustmentAmount_head, AdjustmentAmount,
                NetAmount_head, NetAmount, MultiVendor_Amount_Description_Text, PaymentMode_head, AmountValue, PaymentMode, MultiVendor_Amount_Description_Label;
        LinearLayout MultiVendor_Amount_Description_LL, parent_item_view;

        public ViewHolder(@NonNull View convertView) {
            super(convertView);
            payment_id = (TextView) convertView.findViewById(R.id.MultiVendor_payment_id);
            transaction_date_header = (TextView) convertView.findViewById(R.id.MultiVendor_transaction_date_header);
            CreatedAt = (TextView) convertView.findViewById(R.id.MultiVendor_CreatedAt);
            TransactionId_head = (TextView) convertView.findViewById(R.id.MultiVendor_TransactionId_head);
            TransactionId = (TextView) convertView.findViewById(R.id.MultiVendor_TransactionId);
            Amount_head = (TextView) convertView.findViewById(R.id.MultiVendor_Amount_head);
            Amount = (TextView) convertView.findViewById(R.id.MultiVendor_Amount);
            AdjustmentAmount_head = (TextView) convertView.findViewById(R.id.MultiVendor_AdjustmentAmount_head);
            AdjustmentAmount = (TextView) convertView.findViewById(R.id.MultiVendor_AdjustmentAmount);
            NetAmount_head = (TextView) convertView.findViewById(R.id.MultiVendor_NetAmount_head);
            NetAmount = (TextView) convertView.findViewById(R.id.MultiVendor_NetAmount);
            PaymentMode_head = (TextView) convertView.findViewById(R.id.MultiVendor_PaymentMode_head);
            AmountValue = (TextView) convertView.findViewById(R.id.MultiVendor_Amount_Description_Label_Value);
            PaymentMode = (TextView) convertView.findViewById(R.id.MultiVendor_PaymentMode);
            MultiVendor_Amount_Description_Text = (TextView) convertView.findViewById(R.id.MultiVendor_Amount_Description_Text);
            MultiVendor_Amount_Description_Label = (TextView) convertView.findViewById(R.id.MultiVendor_Amount_Description_Label);
            MultiVendor_Amount_Description_LL = (LinearLayout) convertView.findViewById(R.id.MultiVendor_Amount_Description_LL);
            parent_item_view = (LinearLayout) convertView.findViewById(R.id.parent_item_view);

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

        }
    }
}
