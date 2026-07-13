package magentoegypt.locafy.vendor_product_review_and_rating.ManageRating;

import android.content.Context;
import android.content.Intent;
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

public class RatingListAdapter extends RecyclerView.Adapter<RatingListAdapter.RatingItemHolder> {
  //  List<RatingItemModel> ratingModelList;
    JSONArray ratingModelList;
Context context;

    public RatingListAdapter(/*List<RatingItemModel>*/
            JSONArray ratingModelList, Context context) {
        this.ratingModelList=ratingModelList;
        this.context=context;
    }

    @NonNull
    @Override
    public RatingItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       /* LayoutInflater mInflater = LayoutInflater.from(parent.getContext());
        ViewGroup mainGroup = (ViewGroup) mInflater.inflate(R.layout.rating_list_item, parent, false);
        RatingItemHolder listHolder = new RatingItemHolder(mainGroup);
        return listHolder;*/
        LayoutInflater mInflater = LayoutInflater.from(parent.getContext());
        ViewGroup mainGroup = (ViewGroup) mInflater.inflate(R.layout.rating_list_item, parent, false);
        return new RatingItemHolder(mainGroup);
    }

    @Override
    public void onBindViewHolder(@NonNull final RatingItemHolder holder, int position) {
     //   holder.createView(ratingModelList.get(position));
        try {
            JSONObject object=ratingModelList.getJSONObject(position);
        Log.d("REpo", "createView: "+object);
        holder.ratingId.setText(object.getString("rating_id"));
            holder.assessment.setText(object.getString("rating_code"));
            holder.order.setText(object.getString("sort_order"));
        if (object.getString("is_active").trim().equals("1")) {
            holder.activeStatus.setText(context.getString(R.string.active_txt));
        }
        else {
            holder.activeStatus.setText(object.getString("is_active"));
        }
        holder.main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ratingViewIntent = new Intent(context,RatingItemView.class);
                ratingViewIntent.putExtra("RATING_ID", holder.ratingId.getText().toString().trim());
                context.startActivity(ratingViewIntent);
            }
        });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return ratingModelList.length();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public class RatingItemHolder extends RecyclerView.ViewHolder {
        ConstraintLayout main;
        private AppCompatTextView ratingId,assessment,order,activeStatus;
        public RatingItemHolder(@NonNull View itemView) {
            super(itemView);
            ratingId=itemView.findViewById(R.id.ratingId);
            assessment=itemView.findViewById(R.id.assessment);
            order=itemView.findViewById(R.id.order);
            activeStatus=itemView.findViewById(R.id.activeStatus);
            main=itemView.findViewById(R.id.main);
        }

        /*public void createView(RatingItemModel ratingItemModel) {
            Log.d("REpo", "createView: "+ratingItemModel.getRating_id());
            ratingId.setText(ratingItemModel.getRating_id());
            assessment.setText(ratingItemModel.getRating_code());
            order.setText(ratingItemModel.getSort_order());
            if (ratingItemModel.getIs_active().trim().equals("1")) {
                activeStatus.setText("Active");
            }
            else {
                activeStatus.setText(ratingItemModel.getIs_active());
            }

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent ratingViewIntent = new Intent(itemView.getContext(), RatingItemView.class);
                    ratingViewIntent.putExtra("RATING_ID", ratingId.getText().toString().trim());
                    itemView.getContext().startActivity(ratingViewIntent);
                }
            });
        }*/
    }
}