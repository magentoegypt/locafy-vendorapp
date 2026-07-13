package magentoegypt.locafy.addons.faq.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import magentoegypt.locafy.R;
import magentoegypt.locafy.addons.faq.EditAndDeleteListener;
import magentoegypt.locafy.base_app.base.AppViewHolder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FaqListAdapter extends RecyclerView.Adapter<AppViewHolder> {
    List<JSONObject> list = new ArrayList<>();
    EditAndDeleteListener editAndDeleteListener;
    Context context;

    public FaqListAdapter(EditAndDeleteListener editAndDeleteListener,Context mcontext) {
        this.editAndDeleteListener = editAndDeleteListener;
        context = mcontext;
    }

    @NonNull
    @Override
    public AppViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater mInflater = LayoutInflater.from(viewGroup.getContext());
        ViewGroup mainGroup = (ViewGroup) mInflater.inflate(R.layout.faq_list_view, viewGroup, false);
        return new AppViewHolder(mainGroup);
    }

    @Override
    public void onBindViewHolder(@NonNull AppViewHolder appViewHolder, int i) {
        Log.d("onBindViewHolder", ": " + list.get(i));
        JSONObject jsonObject = list.get(i);
        if (jsonObject.has("is_active")) {
            String enable = "";
            try {
                List<String> list = Arrays.asList(context.getResources().getStringArray(R.array.store_enabledis));
                if (jsonObject.getString("is_active").equalsIgnoreCase("1"))
                    enable = list.get(1);
                else enable = list.get(0);

            } catch (JSONException e) {
                e.printStackTrace();
            }
            appViewHolder.status.setText(enable);

        }
        appViewHolder.pId.setText(getTextFromObject(jsonObject, "product_id"));
        appViewHolder.title.setText(getTextFromObject(jsonObject, "title"));
        appViewHolder.customerEmail.setText(getTextFromObject(jsonObject, "email_id"));
        appViewHolder.createdAt.setText(getTextFromObject(jsonObject, "publish_date"));
        appViewHolder.faqId.setText(String.format("%s : %s",context.getString(R.string.faq_id), getTextFromObject(jsonObject, "id")));


        //Listeners
        appViewHolder.btnEdit.setOnClickListener(view -> handleEditClick(jsonObject));
        appViewHolder.btnDelete.setOnClickListener(view -> handleDeleteClick(jsonObject));

        if (i == getItemCount() - 1 && getItemCount() > 2) {
            editAndDeleteListener.onLoadMoreClick();
            Log.d("Amir", "onloadMore Called: ");
        }
    }

    private void handleDeleteClick(JSONObject jsonObject) {
        editAndDeleteListener.onClick(jsonObject, 1);
    }

    private void handleEditClick(JSONObject jsonObject) {
        //0 for edit
        editAndDeleteListener.onClick(jsonObject, 0);
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
