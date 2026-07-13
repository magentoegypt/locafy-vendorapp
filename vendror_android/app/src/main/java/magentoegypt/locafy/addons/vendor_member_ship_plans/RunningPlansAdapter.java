package magentoegypt.locafy.addons.vendor_member_ship_plans;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import magentoegypt.locafy.R;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Frozen on 13/09/21.
 * updated by Eragon
 */

public class RunningPlansAdapter extends RecyclerView.Adapter<RunningPlansAdapter.ViewHolder> {
    private static LayoutInflater inflater = null;
    public final ArrayList<HashMap<String, String>> data;

    public RunningPlansAdapter(Activity act, ArrayList<HashMap<String, String>> data) {
        this.data = data;
        inflater = (LayoutInflater) act.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
        ViewGroup mainGroup = (ViewGroup) layoutInflater.inflate(R.layout.new_planhistory_runingplan_item, viewGroup, false);
        return new ViewHolder(mainGroup);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        try {
            HashMap<String, String> item_data = data.get(position);
            holder.itemid.setText(item_data.get("id"));
            holder.itemname.setText(item_data.get("name"));
            holder.membership_img.setVisibility(View.GONE);
            if (item_data.get("status") != "")
                holder.status.setText(item_data.get("status"));
//            holder.start_date.setText(item_data.get("start_date"));
//            holder.expiry_date.setText(item_data.get("expiry_date"));
            holder.duration.setText(item_data.get("start_date") + " - " + item_data.get("expiry_date"));
            holder.orderid.setText(item_data.get("order_id"));
            holder.paymentname.setText(item_data.get("payment_name"));
            if (!item_data.get("transaction_id").equals(""))
                holder.transaction_id.setText(item_data.get("transaction_id"));
            else {
                holder.transaction_id.setVisibility(View.GONE);
                holder.transaction_id_txt.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView itemid;
        public TextView itemname;
        public TextView status;
        //        public TextView start_date;
//        public TextView expiry_date;
        public TextView duration;
        public TextView orderid;
        public TextView paymentname;
        public TextView transaction_id;
        public ImageView membership_img;
        public TextView transaction_id_txt;
        public ViewHolder(ViewGroup vi) {
            super(vi);
//            itemid = vi.findViewById(R.id.MultiVendor_ItemId);
//            itemname = vi.findViewById(R.id.MultiVendor_ItemName);
//            status = vi.findViewById(R.id.MultiVendor_Status);
//            start_date = vi.findViewById(R.id.MultiVendor_StartDate);
//            expiry_date = vi.findViewById(R.id.MultiVendor_ExpiryDate);
//            orderid = vi.findViewById(R.id.MultiVendor_OrderId);
//            paymentname = vi.findViewById(R.id.MultiVendor_PaymentName);
//            transaction_id = vi.findViewById(R.id.MultiVendor_Transaction_Id);
//
            itemid = vi.findViewById(R.id.MultiVendor_ItemId);
            itemname = vi.findViewById(R.id.membership_name);
            membership_img = vi.findViewById(R.id.membership);
            status = vi.findViewById(R.id.status_value);
            duration = vi.findViewById(R.id.duration);
            orderid = vi.findViewById(R.id.order_id_value);
            paymentname = vi.findViewById(R.id.payment_method_value);
            transaction_id = vi.findViewById(R.id.transaction_id_value);
            transaction_id_txt = vi.findViewById(R.id.transaction_id_txt);
        }
    }
}