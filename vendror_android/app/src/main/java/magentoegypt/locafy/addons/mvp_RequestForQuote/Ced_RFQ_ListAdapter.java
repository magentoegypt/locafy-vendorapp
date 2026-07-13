package magentoegypt.locafy.addons.mvp_RequestForQuote;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import magentoegypt.locafy.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

/**
 * Created by cedcoss on 13/02/21.
 */

class Ced_RFQ_ListAdapter extends RecyclerView.Adapter<Ced_RFQ_ListAdapter.RFQ_ViewHolder> {
    Activity act;
    ArrayList<HashMap<String, String>> data;
    JSONArray status_drpdwn;

    public Ced_RFQ_ListAdapter(Activity ced_multiVendor_vendorQuoteManagement, ArrayList<HashMap<String, String>> prod_attr_info, JSONArray status) {
        act = ced_multiVendor_vendorQuoteManagement;
        data = prod_attr_info;
        status_drpdwn = status;
    }

    @NonNull
    @Override
    public RFQ_ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater mInflater = LayoutInflater.from(parent.getContext());
        ViewGroup mainGroup = (ViewGroup) mInflater.inflate(R.layout.ced_multivendor_product_rfq_list_adapter, parent, false);
        return new RFQ_ViewHolder(mainGroup);
    }

    @Override
    public void onBindViewHolder(@NonNull final RFQ_ViewHolder holder, int position) {
        HashMap<String, String> attr;
        attr = data.get(position);

        holder.QuoteIncrementId.setText(attr.get(Ced_MultiVendor_VendorQuoteManagement.KEY_quote_increment_id));
        holder.CreatedAt.setText(attr.get(Ced_MultiVendor_VendorQuoteManagement.KEY_created_at));
        holder.CustomerEmail.setText(attr.get(Ced_MultiVendor_VendorQuoteManagement.KEY_customer_email));
        holder.MultiVendor_quote_id.setText(attr.get(Ced_MultiVendor_VendorQuoteManagement.KEY_quote_id));
        try {
            for (int i = 0; i < status_drpdwn.length(); i++) {
                JSONObject jsonObject = status_drpdwn.getJSONObject(i);
                if (Objects.requireNonNull(attr.get("status")).equals(jsonObject.getString("value")))
                    holder.Status.setText(jsonObject.getString("label"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        holder.item_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent edt_attr = new Intent(act, Ced_MultiVendor_VendorQuote_View.class);
                edt_attr.putExtra("quote_id", holder.MultiVendor_quote_id.getText().toString());
                edt_attr.putExtra("response_status_array", status_drpdwn.toString());
                act.startActivity(edt_attr);
                act.overridePendingTransition(R.anim.ced_multivendor_slide_in, R.anim.ced_multivendor_slide_out);
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class RFQ_ViewHolder extends RecyclerView.ViewHolder {
        AppCompatTextView QuoteIncrementId, CreatedAt, CustomerEmail, Status, MultiVendor_quote_id;
        CardView item_card;

        public RFQ_ViewHolder(View vi) {
            super(vi);
            item_card = vi.findViewById(R.id.item_card);
            QuoteIncrementId = vi.findViewById(R.id.QuoteIncrementId);
            CreatedAt = vi.findViewById(R.id.CreatedAt);
            CustomerEmail = vi.findViewById(R.id.CustomerEmail);
            Status = vi.findViewById(R.id.Status);
            MultiVendor_quote_id = vi.findViewById(R.id.MultiVendor_quote_id);
        }
    }
}