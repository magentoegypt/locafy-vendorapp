package magentoegypt.locafy.addons.vendor_member_ship_plans;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import magentoegypt.locafy.R;
import magentoegypt.locafy.api_request_response_section.AsyncResponse;
import magentoegypt.locafy.api_request_response_section.Ced_ClientRequestResponseRest_Data_Array;
import magentoegypt.locafy.api_request_response_section.Ced_MultiVendor_ClientRequestResponse;
import magentoegypt.locafy.vendor_session.Ced_MultiVendor_VendorSessionManagement;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MembershipPlan_ItemViewHolder extends RecyclerView.ViewHolder {
    Context context;
    AppCompatButton addToCart;
    AppCompatTextView membership_id, membership_name, special_price, price;
    AppCompatImageView membership_image;
    LinearLayout product_section;
    Ced_MultiVendor_VendorSessionManagement session;

    public MembershipPlan_ItemViewHolder(View itemView, Context context) {
        super(itemView);
        this.context = context;
        product_section = itemView.findViewById(R.id.product_section);
        membership_id = itemView.findViewById(R.id.membership_id);
        membership_image = itemView.findViewById(R.id.membership_image);
        membership_name = itemView.findViewById(R.id.membership_name);
        special_price = itemView.findViewById(R.id.special_price);
        price = itemView.findViewById(R.id.payment_method_value);
        addToCart = itemView.findViewById(R.id.addToCart);
        session = new Ced_MultiVendor_VendorSessionManagement(context);
    }

    public void createView(final MembershipPlan_ItemDataModel dataModel) {
        membership_id.setText(dataModel.getMembership_id());
        membership_name.setText(dataModel.getMembership_name());

        Glide.with(context)
                .load(dataModel.getMembership_image())
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.placeholder)
                .into(membership_image);

        if (!dataModel.getSpecial_price().equalsIgnoreCase("no_special")) {
            special_price.setText(dataModel.getSpecial_price());
            price.setText(dataModel.getPrice());
            special_price.setVisibility(View.VISIBLE);
            price.setTextColor(context.getResources().getColor(R.color.main_color_gray));
            price.setPaintFlags(price.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        } else {
            special_price.setVisibility(View.GONE);
            price.setPaintFlags(price.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
            price.setTextColor(context.getResources().getColor(R.color.red));
            price.setText(dataModel.getPrice());
        }
        if (dataModel.getAlready_subscribed().equalsIgnoreCase("true")) {
            addToCart.setText(R.string.already_subscribed);
            addToCart.setEnabled(false);
        } else if (dataModel.getShow_addtocart().equalsIgnoreCase("true"))
            addToCart.setText(R.string.addtocart);

        product_section.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent membershipplan = new Intent(context, Ced_MultiVendor_Membership_View.class);
                membershipplan.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                membershipplan.putExtra("membership_id", membership_id.getText().toString());
                membershipplan.putExtra("membership_name", membership_name.getText().toString());
                context.startActivity(membershipplan);
            }
        });
        addToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    String addMembership_url = session.getBase_Url()+ "/rest/V1/addMembership";
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("vendor_id", session.getVendorid());
//                    jsonObject.put("store_id", session.getStoreId());
                    jsonObject.put("membership_id", membership_id.getText().toString());
                    Ced_ClientRequestResponseRest_Data_Array crr = new Ced_ClientRequestResponseRest_Data_Array(new AsyncResponse() {
                        @Override
                        public void processFinish(Object output) throws JSONException {
                            Log.i("membership", output.toString());
                            JSONArray jsonArray = new JSONArray(output.toString());
                            JSONObject jsonObject = jsonArray.getJSONObject(0);
                            Toast.makeText(context, jsonObject.getJSONObject("data").getString("message"), Toast.LENGTH_SHORT).show();
                            if (jsonObject.getJSONObject("data").getBoolean("status")) {
                                goToWebCheckout(jsonObject.getJSONObject("data").getString("customer_id"));
                            }
                        }
                    }, (Activity) context, "POST", jsonObject.toString());
                    crr.execute(addMembership_url);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void goToWebCheckout(String cart_id) {
        String webCheckout_url = session.getBase_Url() + "vmembershipapi/onepage/index/customer_id/" + cart_id + "/check/true";
        Ced_MultiVendor_ClientRequestResponse crr = new Ced_MultiVendor_ClientRequestResponse(new AsyncResponse() {
            @Override
            public void processFinish(Object output) throws JSONException {
                String Jstring = output.toString();
                JSONObject jsonObject = new JSONObject(Jstring);
                String success = jsonObject.getString("success");
                if (success.equals("true")) {
                    Intent MemberShip = new Intent(context, Ced_Weblink.class);
                    MemberShip.putExtra("link", jsonObject.getString("message"));
                    MemberShip.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    context.startActivity(MemberShip);
                } else {
                    Toast.makeText(context, jsonObject.getString("message"), Toast.LENGTH_LONG).show();
                }
            }
        }, (Activity) context);
        crr.execute(webCheckout_url);
    }
}