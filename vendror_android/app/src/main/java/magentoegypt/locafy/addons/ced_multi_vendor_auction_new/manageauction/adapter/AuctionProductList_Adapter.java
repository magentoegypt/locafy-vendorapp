package magentoegypt.locafy.addons.ced_multi_vendor_auction_new.manageauction.adapter;

/*public class AuctionProductList_Adapter {
}*/


import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import magentoegypt.locafy.R;
import magentoegypt.locafy.addons.ced_multi_vendor_auction_new.manageauction.activity.ManageAuction_ItemView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class AuctionProductList_Adapter extends RecyclerView.Adapter<AuctionProductList_Adapter.ReviewItemHolder> {
    //  List<RatingItemModel> arrayList;
    JSONArray arrayList;
    Context context;
    JSONArray status_label;
    public AuctionProductList_Adapter(JSONArray arrayList, Context context,JSONArray status_label) {
        this.arrayList=arrayList;
        this.context=context;
        this.status_label=status_label;
    }

    @NonNull
    @Override
    public ReviewItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater mInflater = LayoutInflater.from(parent.getContext());
        ViewGroup mainGroup = (ViewGroup) mInflater.inflate(R.layout.auction_productlist_item, parent, false);
        ReviewItemHolder listHolder = new ReviewItemHolder(mainGroup, context);
        return listHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ReviewItemHolder holder, int position) {
        //   holder.createView(arrayList.get(position));
        try {
            final JSONObject object=arrayList.getJSONObject(position);
            Log.d("REpo", "createView: "+object);
            holder.name.setText(object.getString("product_name"));
            holder.sku.setText(object.getString("product_sku"));
            holder.price.setText(object.getString("price"));
            holder.type.setText(object.getString("type"));
            holder.edit.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    Intent ratingViewIntent = new Intent(context, ManageAuction_ItemView.class);
                    ratingViewIntent.putExtra("name", holder.name.getText().toString());
                    ratingViewIntent.putExtra("price", holder.price.getText().toString());
                    try {
                        ratingViewIntent.putExtra("product_id",object.getString("product_id"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    ratingViewIntent.putExtra("status_label", status_label.toString());
                    ratingViewIntent.putExtra("frompage","addauction_product");
                    context.startActivity(ratingViewIntent);
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount()
    {
        return arrayList.length();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }


    public class ReviewItemHolder extends RecyclerView.ViewHolder {
        Button edit;
        private AppCompatTextView price,name,sku,type;
        public ReviewItemHolder(@NonNull View itemView, Context context) {
            super(itemView);
            edit=itemView.findViewById(R.id.edit);
            name=itemView.findViewById(R.id.name);
           price=itemView.findViewById(R.id.payment_method_value);
            sku=itemView.findViewById(R.id.sku);
            type=itemView.findViewById(R.id.type);
        }
    }
}

