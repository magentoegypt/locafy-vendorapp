package magentoegypt.locafy.addons.vendor_pincode_checker.PincodeList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import magentoegypt.locafy.base_app.Ced_MultiVendor_VendorFunctionalityList;
import magentoegypt.locafy.api_request_response_section.AsyncResponse;
import magentoegypt.locafy.api_request_response_section.Ced_ClientRequestResponseRest_New;
import magentoegypt.locafy.vendor_session.Ced_MultiVendor_VendorSessionManagement;
import magentoegypt.locafy.R;
import magentoegypt.locafy.addons.vendor_pincode_checker.AssignedPincode.mvp_AssignedPincodeList;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class mvp_PincodeList_Adapter extends RecyclerView.Adapter<mvp_PincodeList_Adapter.ViewHolder> {
    Activity context;
    ArrayList<HashMap<String, String>> StoreArraylist;
    JSONObject datahashmap;
    String addPincode, getPincodeDelete;
    Ced_MultiVendor_VendorSessionManagement vendorSessionManagement;
    Ced_MultiVendor_VendorFunctionalityList functionalityList;

    public mvp_PincodeList_Adapter(Activity context, ArrayList<HashMap<String, String>> StoreArraylist) {
        this.context = context;
        this.StoreArraylist = StoreArraylist;
        datahashmap = new JSONObject();
        vendorSessionManagement = new Ced_MultiVendor_VendorSessionManagement(context);
        functionalityList = new Ced_MultiVendor_VendorFunctionalityList(context);
        addPincode = vendorSessionManagement.getBase_Url() + "rest/V1/pcapi/addPincode";
        getPincodeDelete = vendorSessionManagement.getBase_Url() + "rest/V1/pcapi/getPincodeDelete";
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_pincode_list_item, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        HashMap<String, String> storehash = StoreArraylist.get(position);

        if (context instanceof mvp_AssignedPincodeList) {
            holder.addPincode.setText(context.getResources().getString(R.string.delete));
            holder.id.setTag(storehash.get("allowed_id"));
        } else
            holder.id.setTag(storehash.get("pincode_id"));
        holder.zipcode.setText(storehash.get("zipcode"));
        if (Objects.requireNonNull(storehash.get("can_cod")).equals("0"))
            holder.can_cod.setText(context.getResources().getString(R.string.no));
        else
            holder.can_cod.setText(context.getResources().getString(R.string.yes));
        if (Objects.requireNonNull(storehash.get("can_ship")).equals("0"))
            holder.can_ship.setText(context.getResources().getString(R.string.no));
        else
            holder.can_ship.setText(context.getResources().getString(R.string.yes));
        holder.days_to_deliver.setText(storehash.get("days_to_deliver"));
        holder.addPincode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle(R.string.alert_name);
                builder.setIcon(R.mipmap.ic_launcher);
                if (context instanceof mvp_AssignedPincodeList)
                    builder.setMessage("Are You Sure You want to delete this Pincode");
                else
                    builder.setMessage("Are You Sure You want to add this Pincode");
                builder.setNegativeButton("No",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        });
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                try {
                                    datahashmap.put("vendor_id", vendorSessionManagement.getVendorid());
                                    if (context instanceof mvp_AssignedPincodeList) {
                                        datahashmap.put("allowed_id", holder.id.getTag().toString());
                                        add_Pincode(getPincodeDelete);
                                    } else {
                                        datahashmap.put("pincode_id", holder.id.getTag().toString());
                                        add_Pincode(addPincode);
                                    }
//                                datahashmap.put("hashkey", vendorSessionManagement.getHahkey());
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                );
                builder.show();
            }
        });
    }

    private void add_Pincode(String url) {
        Ced_ClientRequestResponseRest_New crr = new Ced_ClientRequestResponseRest_New(new AsyncResponse() {
            @Override
            public void processFinish(Object output) throws JSONException {
                String Jstring = output.toString();
                JSONObject object = new JSONObject(Jstring);
                JSONObject data = object.getJSONObject("vendor_data");
                String success = data.getString("success");
                if (success.equals("true")) {
                    Toast.makeText(context, data.getString("message"), Toast.LENGTH_LONG).show();
                    Intent intent;
                    if (context instanceof mvp_AssignedPincodeList)
                        intent = new Intent(context, mvp_AssignedPincodeList.class);
                    else
                        intent = new Intent(context, mvp_PincodeListing.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    context.startActivity(intent);
                } else {
                    Toast.makeText(context, data.getString("message"), Toast.LENGTH_LONG).show();
                }
            }
        }, context, "POST", datahashmap.toString(), false, true);
        crr.execute(url);
    }

    @Override
    public int getItemCount() {
        return StoreArraylist.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView zipcode, can_ship, can_cod, days_to_deliver;
        Button addPincode;
        LinearLayout id;

        public ViewHolder(View itemView) {
            super(itemView);
            id = itemView.findViewById(R.id.MultiVendor_storeitem_layout);
            zipcode = itemView.findViewById(R.id.MultiVendor_zipcode);
            can_ship = itemView.findViewById(R.id.MultiVendor_canship);
            can_cod = itemView.findViewById(R.id.MultiVendor_cancod);
            days_to_deliver = itemView.findViewById(R.id.MultiVendor_daystodeliver);
            addPincode = itemView.findViewById(R.id.addPincode);
        }
    }
}