package magentoegypt.locafy.addons.ced_multi_vendor_auction_new.manageauction.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import magentoegypt.locafy.R;
import magentoegypt.locafy.addons.ced_multi_vendor_auction_new.manageauction.activity.Ced_Multivendor_ManageAuction;
import magentoegypt.locafy.addons.ced_multi_vendor_auction_new.manageauction.activity.ManageAuction_ItemView;
import magentoegypt.locafy.api_request_response_section.Ced_ClientRequestResponseRest_New;
import magentoegypt.locafy.api_request_response_section.AsyncResponse;
import magentoegypt.locafy.vendor_session.Ced_MultiVendor_VendorSessionManagement;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ManageAuction_Adapter extends RecyclerView.Adapter<ManageAuction_Adapter.ReviewItemHolder> {
    //  List<RatingItemModel> arrayList;
    JSONArray arrayList;
    Context context;
    JSONArray status_label;

    public ManageAuction_Adapter(JSONArray arrayList, Context context, JSONArray status_label) {
        this.arrayList = arrayList;
        this.context = context;
        this.status_label = status_label;
    }

    @NonNull
    @Override
    public ReviewItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater mInflater = LayoutInflater.from(parent.getContext());
        ViewGroup mainGroup = (ViewGroup) mInflater.inflate(R.layout.manageauction_list_item, parent, false);
        ReviewItemHolder listHolder = new ReviewItemHolder(mainGroup, context);
        return listHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ReviewItemHolder holder, int position) {
        //   holder.createView(arrayList.get(position));
        try {
            JSONObject object = arrayList.getJSONObject(position);
            Log.d("REpo", "createView: " + object);
            final String id = object.getString("id");
            holder.prod_id.setText(object.getString("product_id"));
            holder.name.setText(object.getString("product_name"));
            holder.startprice.setText(object.getString("starting_price"));
            holder.max_price.setText(object.getString("max_price"));
            holder.status.setText(object.getString("status"));
            if (object.has("start_datetime"))
                holder.startdate.setText(object.getString("start_datetime"));
            if (object.has("end_datetime"))
                holder.enddate.setText(object.getString("end_datetime"));

            if (object.has("sellproduct"))
                holder.sellproduct.setText(object.getString("sellproduct"));

            holder.view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent ratingViewIntent = new Intent(context, ManageAuction_ItemView.class);
                    ratingViewIntent.putExtra("id", id);
                    ratingViewIntent.putExtra("status_label", status_label.toString());
                    ratingViewIntent.putExtra("frompage", "manageauction_adpter");
                    context.startActivity(ratingViewIntent);
                }
            });
            holder.delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        Ced_MultiVendor_VendorSessionManagement session = new Ced_MultiVendor_VendorSessionManagement(context);
                        JSONObject param = new JSONObject();
                        param.put("id", id);
                        param.put("vendor_id", session.getVendorid());
                        Ced_ClientRequestResponseRest_New response = new Ced_ClientRequestResponseRest_New(new AsyncResponse() {
                            @Override
                            public void processFinish(Object output) throws JSONException {
                                JSONObject vendor_data = new JSONObject(output.toString()).getJSONObject("vendor_data");
                                if (vendor_data.has("message")) {
                                    Toast.makeText(context, vendor_data.getString("message"), Toast.LENGTH_SHORT).show();
                                }
                                if (vendor_data.getBoolean("success")) {
                                    ((Ced_Multivendor_ManageAuction) context).page = 1;
                                    ((Ced_Multivendor_ManageAuction) context).datalist = new JSONArray();
                                    ((Ced_Multivendor_ManageAuction) context).request_page_date(((Ced_Multivendor_ManageAuction) context).page);
                                }
                            }
                        }, ((Ced_Multivendor_ManageAuction) context), "POST", param.toString());
                        response.execute(session.getBase_Url() + "rest/V1/vauctionapi/deleteAuction");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
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


    public class ReviewItemHolder extends RecyclerView.ViewHolder {
        ConstraintLayout main;
        Button view;
        Button delete;
        // private AppCompatTextView id,createdat,condition,qualification,review,nickname,visibility,guy,product,sku,action;
        private AppCompatTextView prod_id, name, startprice, max_price, status, startdate, enddate, sellproduct;

        public ReviewItemHolder(@NonNull View itemView, Context context) {
            super(itemView);
            view = itemView.findViewById(R.id.view);
            delete = itemView.findViewById(R.id.delete);
            main = itemView.findViewById(R.id.main);
            prod_id = itemView.findViewById(R.id.prod_id);
            name = itemView.findViewById(R.id.name);
            startprice = itemView.findViewById(R.id.startprice);
            max_price = itemView.findViewById(R.id.max_price);
            status = itemView.findViewById(R.id.status);
            startdate = itemView.findViewById(R.id.startdate);
            enddate = itemView.findViewById(R.id.enddate);
            sellproduct = itemView.findViewById(R.id.sellproduct);
           /* condition=itemView.findViewById(R.id.condition);
            qualification=itemView.findViewById(R.id.qualification);
            nickname=itemView.findViewById(R.id.nickname);
            review=itemView.findViewById(R.id.review);
            visibility=itemView.findViewById(R.id.visibility);
            guy=itemView.findViewById(R.id.guy);
            product=itemView.findViewById(R.id.product);
            sku=itemView.findViewById(R.id.sku);
            action=itemView.findViewById(R.id.action);*/
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

