package magentoegypt.locafy.vendor_deals;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import magentoegypt.locafy.R;
import magentoegypt.locafy_constant.AppConstant;

import java.util.ArrayList;

public class Deals_List_RecycleViewAdapter extends RecyclerView.Adapter<Deal_list_RecyclerViewHolder> {

    private final Context context;
    public ArrayList<Deal_List_Model> arrayList;

    public Deals_List_RecycleViewAdapter(Context context, ArrayList<Deal_List_Model> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @Override
    public int getItemCount() {
        return (null != arrayList ? arrayList.size() : 0);
    }

    @Override
    public void onBindViewHolder(@NonNull Deal_list_RecyclerViewHolder holder, int position) {
        final Deal_List_Model model = arrayList.get(position);
        final Deal_list_RecyclerViewHolder mainHolder = holder;
        if (!model.getDeal_id_s().equals("null"))
            mainHolder.deal_id.setText(model.getDeal_id_s());

        if (!model.getProduct_name_s().equals("null"))
            mainHolder.deal_product_name.setText(model.getProduct_name_s());

        if (!model.getEnd_date_s().equals("null"))
            mainHolder.end_date.setText(model.getEnd_date_s());

        if (!model.getStart_date_s().equals("null"))
            mainHolder.start_date.setText(model.getStart_date_s());

        if (!model.getAdmin_status_s().equals("null"))
            if (model.getAdmin_status_s().equals("0")) {
                mainHolder.admin_status.setText(context.getString(R.string.disapprove));
            } else if (model.getAdmin_status_s().equals("1")) {
                mainHolder.admin_status.setText(context.getString(R.string.approved));
            } else if (model.getAdmin_status_s().equals("2")) {
                mainHolder.admin_status.setText(context.getString(R.string.approval_pending));
            } else {
                mainHolder.admin_status.setText(model.getAdmin_status_s());
            }

        if (!model.getStatus_s().equals("null"))
            mainHolder.status.setText(model.getStatus_s());

        if (!model.getDeal_price_s().equals("null"))
            mainHolder.deal_price.setText(model.getDeal_price_s());

        if (!model.getProduct_id_s().equals("null"))
            mainHolder.deal_prod_id.setText(model.getProduct_id_s());

        mainHolder.edit_deal.setOnClickListener(v -> {
            AppConstant.lockButton(v);
            v.startAnimation(AnimationUtils.loadAnimation(context, R.anim.click_effect));
            Intent intent = new Intent(context, Ced_MultiVendor_Create_Deal.class);
            intent.putExtra("navigate", "update_deal");
            intent.putExtra("deal_id", mainHolder.deal_id.getText());
            intent.putExtra("product_name", mainHolder.deal_product_name.getText());
            intent.putExtra("deal_price", mainHolder.deal_price.getText());
            intent.putExtra("deal_status", mainHolder.status.getText());
            intent.putExtra("deal_to", mainHolder.end_date.getText());
            intent.putExtra("deal_from", mainHolder.start_date.getText());
            intent.putExtra("prod_id", mainHolder.deal_prod_id.getText());
            context.startActivity(intent);
        });
    }

    @NonNull
    public Deal_list_RecyclerViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        LayoutInflater mInflater = LayoutInflater.from(viewGroup.getContext());
        ViewGroup mainGroup = (ViewGroup) mInflater.inflate(R.layout.single_deal_layout, viewGroup, false);
        return new Deal_list_RecyclerViewHolder(mainGroup, context);
    }
}