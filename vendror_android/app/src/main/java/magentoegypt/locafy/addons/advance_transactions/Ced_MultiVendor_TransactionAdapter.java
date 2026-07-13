package magentoegypt.locafy.addons.advance_transactions;

import android.app.Activity;
import android.content.Intent;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import magentoegypt.locafy.R;
import magentoegypt.locafy_constant.AppConstant;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by cedcoss on 18/1/18.
 */

public class Ced_MultiVendor_TransactionAdapter extends RecyclerView.Adapter<Ced_MultiVendor_TransactionAdapter.ViewHolder> {
    Activity context;
    ArrayList<HashMap<String, String>> transactionArraylist;

    public Ced_MultiVendor_TransactionAdapter(Activity context, ArrayList<HashMap<String, String>> transactionArraylist ){
        this.context=context;
        this.transactionArraylist=transactionArraylist;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.ced_multivendor_transaction_item, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        HashMap<String, String> transactionhash=new HashMap<String, String>();
        transactionhash = transactionArraylist.get(position);
        holder.paymentid.setText(transactionhash.get("paymentid"));
        holder.trans_createdat.setText(transactionhash.get("createdat"));
        holder.trans_paymentmode.setText(transactionhash.get("paymentmethod"));
        holder.trans_transactionid.setText(transactionhash.get("transactionid"));
        holder.trans_amount.setText(transactionhash.get("amount"));
        holder.trans_adjustmentamt.setText(transactionhash.get("adjustableamount"));
        holder.trans_netamount.setText(transactionhash.get("netamount"));
        holder.viewtransaction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppConstant.lockButton(view);
                Intent intent =new Intent(context, Ced_MultiVendor_TransactionView.class);
                intent.putExtra("payment_id",holder.paymentid.getText().toString());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return transactionArraylist.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
       TextView trans_createdat,trans_paymentmode,trans_transactionid,trans_amount,trans_adjustmentamt,trans_netamount,paymentid;
        LinearLayout viewtransaction;
        public ViewHolder(View itemView) {
            super(itemView);
            viewtransaction=(LinearLayout)itemView.findViewById(R.id.linearlayout_transactionview);
            trans_createdat=(TextView)itemView.findViewById(R.id.MultiVendor_transaction_date);
            trans_paymentmode=(TextView)itemView.findViewById(R.id.MultiVendor_paymentmode);
            trans_transactionid=(TextView)itemView.findViewById(R.id.MultiVendor_transactionid);
            trans_amount=(TextView)itemView.findViewById(R.id.MultiVendor_amount);
            trans_adjustmentamt=(TextView)itemView.findViewById(R.id.MultiVendor_adjustmentamount);
            trans_netamount=(TextView)itemView.findViewById(R.id.MultiVendor_netamount);
            paymentid=(TextView)itemView.findViewById(R.id.MultiVendor_payment_id);
        }
    }
}
