package magentoegypt.locafy.vendor_deals;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import magentoegypt.locafy.R;

import java.util.ArrayList;

public class Create_Product_Listing_RecycleViewAdapter extends RecyclerView.Adapter<Create_Product_list_RecyclerViewHolder> {

    private final Context context;
    public ArrayList<Create_Deal_Product_List_Model> arrayList;

    public Create_Product_Listing_RecycleViewAdapter(Context context, ArrayList<Create_Deal_Product_List_Model> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @Override
    public int getItemCount() {
        return (null != arrayList ? arrayList.size() : 0);
    }

    @Override
    public void onBindViewHolder(@NonNull Create_Product_list_RecyclerViewHolder holder, int position) {
        final Create_Deal_Product_List_Model model = arrayList.get(position);
        final Create_Product_list_RecyclerViewHolder mainHolder = holder;

        if (!model.getProduct_id_s().equals("null"))
            mainHolder.product_id.setText(model.getProduct_id_s());

        if (!model.getProduct_name_s().equals("null"))
            mainHolder.product_name.setText(model.getProduct_name_s());

        if (!model.getType_s().equals("null"))
            mainHolder.product_type.setText(model.getType_s());

        if (!model.getCheck_status().equals("null"))
            if (model.getCheck_status().equals("2")) {
                mainHolder.product_status.setText(context.getResources().getText(R.string.disabled));
            } else if (model.getCheck_status().equals("1")) {
                mainHolder.product_status.setText(context.getResources().getText(R.string.enabled));
            } else if (model.getCheck_status().equals("3")) {
                mainHolder.product_status.setText(context.getString(R.string.expired));
            } else {
                mainHolder.product_status.setText(model.getCheck_status());
            }
        if (!model.getQty_s().equals("null"))
            mainHolder.product_qty.setText(model.getQty_s());

        if (!model.getPrice_s().equals("null"))
            mainHolder.product_price.setText(model.getPrice_s());

        mainHolder.create_deal.setOnClickListener(v -> {
            v.startAnimation(AnimationUtils.loadAnimation(context, R.anim.click_effect));
            Intent intent = new Intent(context, Ced_MultiVendor_Create_Deal.class);
            intent.putExtra("navigate", "create_deal");
            Log.i("9044_extra_data", "" + mainHolder.product_id.getText().toString() + "\n" + mainHolder.product_name.getText().toString());
            intent.putExtra("product_id", mainHolder.product_id.getText().toString());
            intent.putExtra("product_name", mainHolder.product_name.getText().toString());
            intent.putExtra("product_price", mainHolder.product_price.getText().toString());
            context.startActivity(intent);
        });
    }

    @NonNull
    public Create_Product_list_RecyclerViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        LayoutInflater mInflater = LayoutInflater.from(viewGroup.getContext());
        ViewGroup mainGroup = (ViewGroup) mInflater.inflate(R.layout.single_create_deal_product_layout, viewGroup, false);
        return new Create_Product_list_RecyclerViewHolder(mainGroup, context);
    }
}