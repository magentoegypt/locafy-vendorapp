package magentoegypt.locafy.addons.ced_multi_vendor_auction_new.auctionwinner;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;


import magentoegypt.locafy.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class AcuctionWinner_Adapter extends RecyclerView.Adapter<AcuctionWinner_Adapter.AWItemHolder> {
    //  List<RatingItemModel> arrayList;
    JSONArray arrayList;
    Context context;
    public AcuctionWinner_Adapter(JSONArray arrayList, Context context) {
        this.arrayList=arrayList;
        this.context=context;
    }

    @NonNull
    @Override
    public AWItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater mInflater = LayoutInflater.from(parent.getContext());
        ViewGroup mainGroup = (ViewGroup) mInflater.inflate(R.layout.auctionwinners_list_item, parent, false);
        AWItemHolder listHolder = new AWItemHolder(mainGroup, context);
        return listHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final AWItemHolder holder, int position) {
        //   holder.createView(arrayList.get(position));
        try {
            JSONObject object=arrayList.getJSONObject(position);
            Log.d("REpo", "createView: "+object);
            String product_id=object.getString("product_id");
            holder.id.setText(object.getString("id"));
            if(object.has("product_sku"))
            {
                holder.sku.setText(object.getString("product_sku"));
            }
            holder.auctionprice.setText(object.getString("auction_price"));
            holder.winningprice.setText(object.getString("winning_price"));
            holder.status.setText(object.getString("status"));
            holder.binding_date.setText(object.getString("bid_date"));
            holder.customer_email.setText(object.getString("customer_email"));
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


    public class AWItemHolder extends RecyclerView.ViewHolder
    {
        ConstraintLayout main;
        private AppCompatTextView id,sku,auctionprice,winningprice,status,binding_date,customer_email;
        public AWItemHolder(@NonNull View itemView, Context context)
        {
            super(itemView);
            main=itemView.findViewById(R.id.main);
            id=itemView.findViewById(R.id.id);
            sku=itemView.findViewById(R.id.sku);
            auctionprice=itemView.findViewById(R.id.auctionprice);
            winningprice=itemView.findViewById(R.id.winningprice);
            status=itemView.findViewById(R.id.status);
            binding_date=itemView.findViewById(R.id.binding_date);
            customer_email=itemView.findViewById(R.id.customer_email);
        }
    }

}


