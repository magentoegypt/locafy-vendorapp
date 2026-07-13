package magentoegypt.locafy.addons.inventory.adapters;

import android.annotation.SuppressLint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import magentoegypt.locafy.R;
import magentoegypt.locafy.addons.faq.EditAndDeleteListener;
import magentoegypt.locafy.addons.faq.viewHolder.OutOfStockViewHolder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class LowStockProductAdapter extends RecyclerView.Adapter<OutOfStockViewHolder> {
    List<JSONObject> list = new ArrayList<>();
    EditAndDeleteListener editAndDeleteListener;

    public LowStockProductAdapter(EditAndDeleteListener editAndDeleteListener) {
        this.editAndDeleteListener = editAndDeleteListener;
    }

    @NonNull
    @Override
    public OutOfStockViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater mInflater = LayoutInflater.from(viewGroup.getContext());
        ViewGroup mainGroup = (ViewGroup) mInflater.inflate(R.layout.out_of_product_view, viewGroup, false);
        return new OutOfStockViewHolder(mainGroup);
    }

    @Override
    public void onBindViewHolder(@NonNull OutOfStockViewHolder appViewHolder, int i) {
        JSONObject jsonObject = list.get(i);
        Log.d("onBindViewHolder", ": " + jsonObject);

        appViewHolder.product.setText(getTextFromObject(jsonObject, "sku"));
        appViewHolder.name.setText(getTextFromObject(jsonObject, "name"));
        appViewHolder.type.setText(getTextFromObject(jsonObject, "type"));
        appViewHolder.price.setText(getTextFromObject(jsonObject, "vendor_price"));
        appViewHolder.quantity.setText(getTextFromObject(jsonObject, "qty"));


        appViewHolder.btnEdit.setOnClickListener(view -> editAndDeleteListener.onClick(jsonObject, 1));
        if (i == getItemCount() - 1 && getItemCount() > 2) {
            editAndDeleteListener.onLoadMoreClick();
            Log.d("Amir", "onLoadMore Called: ");
        }
    }

    private String getTextFromObject(JSONObject jsonObject, String key) {
        if (jsonObject.has(key)) {
            try {
                return jsonObject.getString(key);
            } catch (JSONException e) {
                e.printStackTrace();
                return "";
            }
        } else return "no " + key + " found";
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void addList(JSONArray list) {
        for (int a = 0; a < list.length(); a++) {
            try {
                JSONObject jsonObject = list.getJSONObject(a);
                this.list.add(jsonObject);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        notifyDataSetChanged();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void clear() {
        list.clear();
        notifyDataSetChanged();
    }
}
