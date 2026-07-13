package magentoegypt.locafy.addons.vendor_store_pickup;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import magentoegypt.locafy.base_app.Ced_MultiVendor_VendorFunctionalityList;
import magentoegypt.locafy.api_request_response_section.AsyncResponse;
import magentoegypt.locafy.api_request_response_section.Ced_MultiVendor_ClientRequestResponse;
import magentoegypt.locafy.vendor_session.Ced_MultiVendor_VendorSessionManagement;
import magentoegypt.locafy.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

/**
 * Created by cedcoss on 11/1/18.
 */

public class Ced_MultiVendor_StoreListing_Adapter extends RecyclerView.Adapter<Ced_MultiVendor_StoreListing_Adapter.ViewHolder> {
    Activity context;
    ArrayList<HashMap<String, String>> StoreArraylist;
    HashMap<String, String> datahashmap;
    String deletestore_url;
    Ced_MultiVendor_VendorSessionManagement vendorSessionManagement;
    Ced_MultiVendor_VendorFunctionalityList functionalityList;

    public Ced_MultiVendor_StoreListing_Adapter(Activity context, ArrayList<HashMap<String, String>> StoreArraylist) {
        this.context = context;
        this.StoreArraylist = StoreArraylist;
        datahashmap = new HashMap<>();
        vendorSessionManagement = new Ced_MultiVendor_VendorSessionManagement(context);
        functionalityList = new Ced_MultiVendor_VendorFunctionalityList(context);
        deletestore_url = vendorSessionManagement.getBase_Url() + "vstorepickupapi/store/delete";
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.ced_multivendor_store_item, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        HashMap<String, String> storehash;
        storehash = StoreArraylist.get(position);

        holder.storeid.setText(storehash.get("store_id"));
        holder.storename.setText(storehash.get("store_name"));
        holder.storemanager_name.setText(storehash.get("store_manager"));
        holder.storemanager_email.setText(storehash.get("store_manager_email"));
        if (Objects.requireNonNull(storehash.get("store_status")).equalsIgnoreCase("0"))
            holder.storestatus.setText(context.getString(R.string.disabled));
        else if (Objects.requireNonNull(storehash.get("store_status")).equalsIgnoreCase("1"))
            holder.storestatus.setText(context.getString(R.string.enabled));
        holder.storedelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle(R.string.alert_name);
                builder.setIcon(R.mipmap.ic_launcher);
                builder.setMessage(R.string.confirm_first);
                builder.setNegativeButton(R.string.no,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        });
                builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                datahashmap.put("pickup_id", holder.storeid.getText().toString());
                                datahashmap.put("vendor_id", vendorSessionManagement.getVendorid());
                                datahashmap.put("hashkey", vendorSessionManagement.getHahkey());
                                Ced_MultiVendor_ClientRequestResponse crr = new Ced_MultiVendor_ClientRequestResponse(new AsyncResponse() {
                                    @Override
                                    public void processFinish(Object output) throws JSONException {
                                        String Jstring = output.toString();
                                        JSONObject object = new JSONObject(Jstring);
                                        Log.i("hello", "" + object);
                                        JSONObject data = object.getJSONObject("data");
                                        String success = data.getString("success");
                                        if (success.equals("true")) {
                                            Toast.makeText(context, data.getString("message"), Toast.LENGTH_LONG).show();
                                            Intent intent = new Intent(context, Ced_MultiVendor_StoreListing.class);
                                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                            context.startActivity(intent);
                                        } else {
                                            Toast.makeText(context, data.getString("message"), Toast.LENGTH_LONG).show();
                                        }
                                    }
                                }, context, "POST", datahashmap);
                                crr.execute(deletestore_url);
                            }
                        }
                );
                builder.show();
            }
        });

        holder.storeedit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, Ced_MultiVendor_UpdateStore.class);
                intent.putExtra("pickup_id", holder.storeid.getText().toString());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return StoreArraylist.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView storeid, storename, storemanager_name, storemanager_email, storestatus;
        TextView storeedit, storedelete;

        public ViewHolder(View itemView) {
            super(itemView);
            storeid = itemView.findViewById(R.id.MultiVendor_store_id);
            storename = itemView.findViewById(R.id.MultiVendor_store_name);
            storemanager_name = itemView.findViewById(R.id.MultiVendor_store_manager_name);
            storemanager_email = itemView.findViewById(R.id.MultiVendor_store_manager_email);
            storestatus = itemView.findViewById(R.id.MultiVendor_store_status);
            storeedit = itemView.findViewById(R.id.MultiVendor_edit_store);
            storedelete = itemView.findViewById(R.id.MultiVendor_delete_store);
        }
    }
}