package magentoegypt.locafy.vendor_deals;

import android.content.Context;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import magentoegypt.locafy.R;

/**
 * Created by cedcoss on 9/1/18.
 */

class Deal_list_RecyclerViewHolder extends RecyclerView.ViewHolder {
    TextView deal_id;
    AppCompatButton admin_status;
    TextView deal_product_name;
    TextView deal_price;
    TextView end_date;
    TextView start_date;
    TextView status;
    TextView edit_deal;
    TextView deal_prod_id;
    Context context;
    RelativeLayout deal_item;
    RelativeLayout outer_wall;

    public Deal_list_RecyclerViewHolder(View vi, final Context context) {
        super(vi);
        this.deal_id = vi.findViewById(R.id.deal_id);
        this.deal_prod_id = vi.findViewById(R.id.deal_prod_id);
        this.admin_status = vi.findViewById(R.id.admin_status);
        this.deal_product_name = vi.findViewById(R.id.deal_product_name);
        this.deal_price = vi.findViewById(R.id.deal_price);
        this.start_date = vi.findViewById(R.id.start_date);
        this.end_date = vi.findViewById(R.id.end_date);
        this.status = vi.findViewById(R.id.status);
        this.edit_deal = vi.findViewById(R.id.edit_deal);
        this.deal_item = vi.findViewById(R.id.deal_item);
        this.outer_wall = vi.findViewById(R.id.outer_wall);
        this.context = context;
    }
}