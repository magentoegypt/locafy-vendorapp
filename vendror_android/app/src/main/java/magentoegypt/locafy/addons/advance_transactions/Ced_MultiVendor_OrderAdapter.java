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
 * Created by cedcoss on 23/1/18.
 */

public class Ced_MultiVendor_OrderAdapter extends RecyclerView.Adapter<Ced_MultiVendor_OrderAdapter.ViewHolder> {

    Activity context;
    ArrayList<HashMap<String, String>> orderArraylist;

    public Ced_MultiVendor_OrderAdapter(Activity context, ArrayList<HashMap<String, String>> orderArraylist) {
        this.context = context;
        this.orderArraylist = orderArraylist;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.ced_multivendor_order_item, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        HashMap<String, String> orderhash = new HashMap<String, String>();
        orderhash = orderArraylist.get(position);
        holder.paymentid.setText(orderhash.get("paymentid"));
        holder.orderid.setText(orderhash.get("orderid"));
        holder.grandtotal.setText(orderhash.get("grandtotal"));
        holder.commissionfees.setText(orderhash.get("commissionfee"));
        holder.paymentmode.setText(orderhash.get("orderpaymode"));
        holder.totalpayment.setText(orderhash.get("vendorpayment"));
        holder.vieworder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppConstant.lockButton(view);
                Intent intent = new Intent(context, Ced_MultiVendor_OrderView.class);
                intent.putExtra("orderid", holder.orderid.getText().toString());
                intent.putExtra("paymentid", holder.paymentid.getText().toString());
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return orderArraylist.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView orderid, grandtotal, commissionfees, paymentmode, totalpayment, paymentid;
        LinearLayout vieworder;

        public ViewHolder(View itemView) {
            super(itemView);
            vieworder = (LinearLayout) itemView.findViewById(R.id.MultiVendor_orderitem_layout);
            paymentid = (TextView) itemView.findViewById(R.id.MultiVendor_order_paymentid);
            orderid = (TextView) itemView.findViewById(R.id.MultiVendor_order_id);
            grandtotal = (TextView) itemView.findViewById(R.id.MultiVendor_order_grandtotal);
            commissionfees = (TextView) itemView.findViewById(R.id.MultiVendor_order_commissionfee);
            paymentmode = (TextView) itemView.findViewById(R.id.MultiVendor_order_paymentmode);
            totalpayment = (TextView) itemView.findViewById(R.id.MultiVendor_order_totalpayment);
        }
    }
}
