package magentoegypt.locafy.vendor_deals;

import android.content.Context;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import magentoegypt.locafy.R;

/**
 * Created by cedcoss on 9/1/18.
 */

class Create_Product_list_RecyclerViewHolder extends RecyclerView.ViewHolder {
    TextView product_id, product_status, product_name, product_price, product_qty, product_type, create_deal;
    Context context;
    RelativeLayout deal_item, outer_wall;

    public Create_Product_list_RecyclerViewHolder(View vi, final Context context) {
        super(vi);
        this.product_id = vi.findViewById(R.id.product_id);
        this.product_status = vi.findViewById(R.id.product_status);
        this.product_name = vi.findViewById(R.id.product_name);
        this.product_price = vi.findViewById(R.id.payment_method_value);
        this.product_qty = vi.findViewById(R.id.product_qty);
        this.product_type = vi.findViewById(R.id.product_type);
        this.create_deal = vi.findViewById(R.id.create_deal);
        this.deal_item = vi.findViewById(R.id.product_list_item);
        this.outer_wall = vi.findViewById(R.id.outer_wall);
        this.context = context;
    }
}