package magentoegypt.locafy.vendor_product_review_and_rating.ManageReview;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import magentoegypt.locafy.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

public class ReviewListAdapter extends RecyclerView.Adapter<ReviewListAdapter.ReviewItemHolder> {
    private final JSONArray arrayList;
    private final Context context;
    private final JSONObject stores;

    public ReviewListAdapter(JSONArray arrayList, Context context, JSONObject stores) {
        this.arrayList = arrayList;
        this.context = context;
        this.stores = stores;
    }

    @NonNull
    @Override
    public ReviewItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater mInflater = LayoutInflater.from(parent.getContext());
        ViewGroup mainGroup = (ViewGroup) mInflater.inflate(R.layout.review_list_item, parent, false);
        return new ReviewItemHolder(mainGroup);
    }

    @Override
    public void onBindViewHolder(@NonNull final ReviewItemHolder holder, int position) {
        try {
            JSONObject object = arrayList.getJSONObject(position);
            holder.id.setText(object.getString("review_id"));
            holder.createdat.setText(object.getString("review_created_at"));
            holder.status.setText(object.getString("status"));
            holder.title.setText(object.getString("title"));
            holder.nickname.setText(object.getString("nickname"));
            holder.review.setText(object.getString("detail"));
            holder.type.setText(object.getString("type"));
            holder.product.setText(object.getString("name"));
            holder.sku.setText(object.getString("sku"));
            holder.action.setTag(object.getString("review_id"));
            JSONArray store_ids = object.getJSONArray("store_id");
            String visible_stores = "";
            JSONArray names = stores.names();
            for (int s = 0; s < store_ids.length(); s++) {
                visible_stores = visible_stores.concat(stores.getString(Objects.requireNonNull(names).getString(Integer.parseInt(store_ids.getString(s)) - 1)) + "\n");
            }
            holder.visibility.setText(visible_stores.trim());
            holder.action.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent ratingViewIntent = new Intent(context, ReviewItemView.class);
                    ratingViewIntent.putExtra("REVIEW_ID", holder.action.getTag().toString().trim());
                    context.startActivity(ratingViewIntent);
                    ((Activity) context).finish();
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
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

    public static class ReviewItemHolder extends RecyclerView.ViewHolder {
        private final AppCompatTextView id, createdat, status, title, nickname, review, visibility, type, product, sku;
        private final AppCompatButton action;

        public ReviewItemHolder(@NonNull View itemView) {
            super(itemView);
            id = itemView.findViewById(R.id.id);
            createdat = itemView.findViewById(R.id.createdat);
            status = itemView.findViewById(R.id.status);
            title = itemView.findViewById(R.id.title);
            nickname = itemView.findViewById(R.id.nickname);
            review = itemView.findViewById(R.id.review);
            visibility = itemView.findViewById(R.id.visibility);
            type = itemView.findViewById(R.id.type);
            product = itemView.findViewById(R.id.product);
            sku = itemView.findViewById(R.id.sku);
            action = itemView.findViewById(R.id.action);
        }
    }
}