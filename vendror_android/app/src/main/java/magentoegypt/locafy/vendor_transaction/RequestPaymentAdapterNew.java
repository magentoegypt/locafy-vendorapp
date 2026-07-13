package magentoegypt.locafy.vendor_transaction;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import magentoegypt.locafy.api_request_response_section.AsyncResponse;
import magentoegypt.locafy.api_request_response_section.Ced_MultiVendor_ClientRequestResponse;
import magentoegypt.locafy.vendor_session.Ced_MultiVendor_VendorSessionManagement;
import magentoegypt.locafy.R;
import magentoegypt.locafy.base_app.OnBottomReachedListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class RequestPaymentAdapterNew extends RecyclerView.Adapter<RequestPaymentAdapterNew.ViewHolder> {
    Context context;
    JSONArray item_list;
    Ced_MultiVendor_VendorSessionManagement sessionManagement;
    String url_request;
    OnBottomReachedListener onBottomReachedListener;

    public RequestPaymentAdapterNew(Context context, JSONArray item_list) {
        this.context = context;
        this.item_list = item_list;
        sessionManagement = new Ced_MultiVendor_VendorSessionManagement(context);
        url_request = sessionManagement.getBase_Url()+ "vendorapi/vtransaction/request";
    }

    public void setOnBottomReachedListener(OnBottomReachedListener onBottomReachedListener) {
        this.onBottomReachedListener = onBottomReachedListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.request_payment_item_list, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        try {
            if (position == item_list.length() - 1) {
                try {
                    onBottomReachedListener.onBottomReached(position);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
            holder.setIsRecyclable(false);
            final JSONObject list = item_list.getJSONObject(position);
            holder.order_date.setText(list.getString("created_at"));
            holder.order_id.setText(list.getString("order_increment_id"));
            holder.pending_amt.setText(list.getString("pending_amount"));
            holder.cancelled_amt.setText(list.getString("cancelled_amount"));

            if (list.getString("action").equalsIgnoreCase("Request")) {
                holder.request_but.setVisibility(View.VISIBLE);
                holder.request_but.setText(list.getString("action"));
                holder.request_but.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        HashMap<String, String> post_param = new HashMap<>();
                        post_param.put("vendor_id", sessionManagement.getVendorid());
                        post_param.put("hashkey", sessionManagement.getHahkey());
                        try {
                            post_param.put("payment_id", list.getString("request_id"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        requestData(post_param);
                    }
                });
                holder.action.setVisibility(View.GONE);
            } else {
                holder.action.setVisibility(View.VISIBLE);
                holder.action.setText(list.getString("action"));
                holder.request_but.setVisibility(View.GONE);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void requestData(HashMap<String, String> postparam) {

        Ced_MultiVendor_ClientRequestResponse requestResponse = new Ced_MultiVendor_ClientRequestResponse(new AsyncResponse() {
            @Override
            public void processFinish(Object output) throws JSONException {
                JSONObject jsonObject = new JSONObject(output.toString()).getJSONObject("data");
                if (jsonObject.getBoolean("success")) {
                    Toast.makeText(context, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                    ((RequestPaymentNew) context).recreate();
                    ((RequestPaymentNew) context).overridePendingTransition(R.anim.ced_multivendor_slide_in, R.anim.ced_multivendor_slide_out);
                } else {
                    Toast.makeText(context, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                }

            }
        }, ((RequestPaymentNew) context), "POST", postparam);
        requestResponse.execute(url_request);
    }

    @Override
    public int getItemCount() {
        return item_list.length();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView order_date, order_id, pending_amt, cancelled_amt, action, request_but;

        public ViewHolder(View itemView) {
            super(itemView);
            order_date = itemView.findViewById(R.id.order_date);
            order_id = itemView.findViewById(R.id.order_id);
            pending_amt = itemView.findViewById(R.id.pending_amt);
            cancelled_amt = itemView.findViewById(R.id.cancelled_amt);
            action = itemView.findViewById(R.id.action);
            request_but = itemView.findViewById(R.id.request_but);
        }
    }
}
