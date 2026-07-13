package magentoegypt.locafy.addons.vendor_member_ship_plans;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.LinearLayoutCompat;

import magentoegypt.locafy.api_request_response_section.AsyncResponse;
import magentoegypt.locafy.api_request_response_section.Ced_ClientRequestResponseRest_Data_Array;
import magentoegypt.locafy.api_request_response_section.Ced_MultiVendor_ClientRequestResponse;
import com.bumptech.glide.Glide;
import magentoegypt.locafy.navigation_drawer.Activity.Ced_MultiVendor_NavigationActivity;
import magentoegypt.locafy.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Ced_MultiVendor_Membership_View extends Ced_MultiVendor_NavigationActivity {

    private AppCompatImageView image;
    private AppCompatTextView membershipName, allowed_prod, duration_txt, price, special_price, specialPrice_txt;
    private AppCompatButton addToCart;
    private String membership_id;
    private LinearLayoutCompat dyn_allowedCategories;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewGroup content = findViewById(R.id.MultiVendor_frame_container);
        getLayoutInflater().inflate(R.layout.activity_membership_view, content, true);
        initview();
        try {
            membership_id = getIntent().getStringExtra("membership_id");
            String membership_name = getIntent().getStringExtra("membership_name");
            membershipName.setText(membership_name);
            String addMembership_url = session.getBase_Url() + "rest/V1/viewMembership";
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("vendor_id", session.getVendorid());
//            jsonObject.put("store_id", session.getStoreId());
            jsonObject.put("membership_id", membership_id);
            Ced_ClientRequestResponseRest_Data_Array crr = new Ced_ClientRequestResponseRest_Data_Array(new AsyncResponse() {
                @Override
                public void processFinish(Object output) throws JSONException {
                    Log.i("membership", output.toString());
                    JSONArray jsonArray = new JSONArray(output.toString());
                    JSONObject jsonObject = jsonArray.getJSONObject(0);
                    if (jsonObject.getJSONObject("data").getBoolean("status")) {
                        JSONObject membership_info = jsonObject.getJSONObject("data").getJSONObject("membership_info");
                        String image_url = membership_info.getString("image");
                        Glide.with(Ced_MultiVendor_Membership_View.this)
                                .load(image_url)
                                .placeholder(R.drawable.placeholder)
                                .error(R.drawable.placeholder)
                                .into(image);
                        String allowed_products = membership_info.getString("allowed_products");
                        allowed_prod.setText(allowed_products);
                        String duration = membership_info.getString("duration");
                        duration_txt.setText(duration);
                        String price_txt = membership_info.getString("price");
                        price.setText(price_txt);
                        if (membership_info.has("special_price")) {
                            String special_price_txt = membership_info.getString("special_price");
                            special_price.setText(special_price_txt);
                            special_price.setVisibility(View.VISIBLE);
                            specialPrice_txt.setVisibility(View.VISIBLE);
                        }
                        if (membership_info.has("already_subscribed")) {
                            addToCart.setText(getResources().getString(R.string.already_subscribed));
                            addToCart.setEnabled(false);
                        } else if (membership_info.has("show_addtocart")) {
                            addToCart.setText(getResources().getString(R.string.addtocart));
                            addToCart.setEnabled(true);
                        }
                        JSONArray allowed_categories = membership_info.getJSONArray("allowed_categories");
                        for (int i = 0; i < allowed_categories.length(); i++) {
                            View v = View.inflate(Ced_MultiVendor_Membership_View.this, R.layout.ced_multivendor_citytextview, null);
                            TextView category = v.findViewById(R.id.text1);
                            category.setText(allowed_categories.getString(i));
                            dyn_allowedCategories.addView(v);
                        }
                    }
                }
            }, this, "POST", jsonObject.toString());
            crr.execute(addMembership_url);
        } catch (Exception e) {
            e.printStackTrace();
        }
        addToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    String addMembership_url = session.getBase_Url() + "rest/V1/addMembership";
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("vendor_id", session.getVendorid());
//                    jsonObject.put("store_id", session.getStoreId());
                    jsonObject.put("membership_id", membership_id);
                    Ced_ClientRequestResponseRest_Data_Array crr = new Ced_ClientRequestResponseRest_Data_Array(new AsyncResponse() {
                        @Override
                        public void processFinish(Object output) throws JSONException {
                            Log.i("membership", output.toString());
                            JSONArray jsonArray = new JSONArray(output.toString());
                            JSONObject jsonObject = jsonArray.getJSONObject(0);
                            Toast.makeText(Ced_MultiVendor_Membership_View.this, jsonObject.getJSONObject("data").getString("message"), Toast.LENGTH_SHORT).show();
                            if (jsonObject.getJSONObject("data").getBoolean("status")) {
                                goToWebCheckout(jsonObject.getJSONObject("data").getString("customer_id"));
                            }
                        }
                    }, Ced_MultiVendor_Membership_View.this, "POST", jsonObject.toString());
                    crr.execute(addMembership_url);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void initview() {
        membershipName = findViewById(R.id.membership_name);
        image = findViewById(R.id.membership);
        allowed_prod = findViewById(R.id.order_id_value);
        duration_txt = findViewById(R.id.duration);
        price = findViewById(R.id.payment_method_value);
        specialPrice_txt = findViewById(R.id.specialPrice_txt);
        special_price = findViewById(R.id.specialPrice);
        addToCart = findViewById(R.id.addToCart);
        dyn_allowedCategories = findViewById(R.id.dyn_allowedCategories);
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
                    Intent MemberShip = new Intent(Ced_MultiVendor_Membership_View.this, Ced_Weblink.class);
                    MemberShip.putExtra("link", jsonObject.getString("message"));
                    MemberShip.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    startActivity(MemberShip);
                } else {
                    Toast.makeText(Ced_MultiVendor_Membership_View.this, jsonObject.getString("message"), Toast.LENGTH_LONG).show();
                }
            }
        }, this);
        crr.execute(webCheckout_url);
    }
}