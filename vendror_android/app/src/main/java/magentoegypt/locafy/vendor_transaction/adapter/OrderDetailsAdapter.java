package magentoegypt.locafy.vendor_transaction.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import magentoegypt.locafy.R;
import magentoegypt.locafy.vendor_transaction.model.OrderDetail;

import java.util.List;

public class OrderDetailsAdapter extends RecyclerView.Adapter<OrderDetailsAdapter.ViewHolder> {

    private List<OrderDetail> orderDetailList;
    private Context context;

    public OrderDetailsAdapter(List<OrderDetail> orderDetailList, Context context) {
        this.orderDetailList = orderDetailList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.order_details_view, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        try {
            OrderDetail data = orderDetailList.get(i);
            viewHolder.order_id.setText(data.getOrderId());
            viewHolder.commission_fee.setText(data.getCommissionFee());
            viewHolder.grand_total.setText(data.getVendorPayment());
            viewHolder.payment_mode.setText(data.getOrderPaymode());
            viewHolder.store_total_payment.setText(data.getVendorPayment());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return orderDetailList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView order_id, grand_total,
                commission_fee, payment_mode,
                store_total_payment;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            order_id = itemView.findViewById(R.id.order_id);
            grand_total = itemView.findViewById(R.id.grand_total);
            commission_fee = itemView.findViewById(R.id.commission_fee);
            payment_mode = itemView.findViewById(R.id.payment_mode);
            store_total_payment = itemView.findViewById(R.id.total_payment);
        }
    }
}
