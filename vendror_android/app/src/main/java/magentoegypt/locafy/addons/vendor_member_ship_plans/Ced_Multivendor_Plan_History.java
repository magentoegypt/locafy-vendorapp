package magentoegypt.locafy.addons.vendor_member_ship_plans;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import magentoegypt.locafy.api_request_response_section.AsyncResponse;
import magentoegypt.locafy.api_request_response_section.Ced_ClientRequestResponseRest_Data_Array;
import magentoegypt.locafy.navigation_drawer.Activity.Ced_MultiVendor_NavigationActivity;
import magentoegypt.locafy.api_request_response_section.network_error_handling.Ced_MultiVendor_ConnectionDetector;
import magentoegypt.locafy.api_request_response_section.network_error_handling.Ced_MultiVendor_NoInternetconnection;
import magentoegypt.locafy.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

/**
 * Created by Frozen on 13/09/21.
 */

public class Ced_Multivendor_Plan_History extends Ced_MultiVendor_NavigationActivity {
    Ced_MultiVendor_ConnectionDetector connectionDetector;
    String planhistory_url = "";
    HashMap<String, String> dataforplanhistory;
    TextView items_count;
    RecyclerView recyclerView;
    HashMap<String, String> planhistory_hashmap;
    ArrayList<HashMap<String, String>> planhistory_arraylist;
    TextView product_limit;
    TextView product_remaining;
    Boolean paginate = true;
    private int current = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (android.os.Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(this.getResources().getColor(R.color.status_bar_color));
        }
        connectionDetector = new Ced_MultiVendor_ConnectionDetector(getApplicationContext());
        if (connectionDetector.isConnectingToInternet()) {
            try {
                ViewGroup content = findViewById(R.id.MultiVendor_frame_container);
                getLayoutInflater().inflate(R.layout.ced_multivendor_plan_history, content, true);
                Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
                getSupportActionBar().setHomeAsUpIndicator(R.drawable.backbutton);
                items_count = findViewById(R.id.MultiVendor_ItemCount);
                recyclerView = findViewById(R.id.MultiVendor_SubscriptionHistoryListView);
                product_limit = findViewById(R.id.MultiVendor_ProductLimit);
                product_remaining = findViewById(R.id.MultiVendor_RemainingProd);
                planhistory_url = session.getBase_Url() + "rest/V1/plan/history";
                dataforplanhistory = new HashMap<>();
                dataforplanhistory.put("vendor_id", session.getVendorid());
//                dataforplanhistory.put("store_id", session.getStoreId());
                request_for_Subscription_History();
                recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                    @Override
                    public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                        super.onScrolled(recyclerView, dx, dy);
                        Log.i("Scroll-", "" + recyclerView.canScrollVertically(1));
                        if (!recyclerView.canScrollVertically(1) && paginate) {
                            current = current + 1;
                            paginate = false;
                            request_for_Subscription_History();
                        }
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            Intent nointernet = new Intent(getApplicationContext(), Ced_MultiVendor_NoInternetconnection.class);
            nointernet.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            nointernet.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(nointernet);
            overridePendingTransition(R.anim.ced_multivendor_slide_in, R.anim.ced_multivendor_slide_out);
        }
    }

    public void request_for_Subscription_History() {
        dataforplanhistory.put("page", String.valueOf(current));
        Log.i("senddata", "postdata " + dataforplanhistory.toString());
        Ced_ClientRequestResponseRest_Data_Array crr = new Ced_ClientRequestResponseRest_Data_Array(new AsyncResponse() {

            @Override
            public void processFinish(Object output) throws JSONException {
                Log.i("membership", output.toString());
                create_Subscription_History_Items(output.toString());
            }
        }, Ced_Multivendor_Plan_History.this, "POST", dataforplanhistory.toString());
        crr.execute(planhistory_url);
    }

    public void create_Subscription_History_Items(String output) throws JSONException {
        JSONArray jsonArray = new JSONArray(output);
        JSONObject jsonObject = jsonArray.getJSONObject(0);
        JSONObject vendor_data = jsonObject.getJSONObject("vendor_data");
        if (vendor_data.has("product_limit")) {
            String prod_limit = vendor_data.getString("product_limit");
            product_limit.setText(prod_limit);
        }
        if (vendor_data.has("remaining_products")) {
            int remaining_products = vendor_data.getInt("remaining_products");
            product_remaining.setText(String.valueOf(remaining_products));
        }
        if (vendor_data.getBoolean("status")) {
            if (vendor_data.has("membership_history")) {
                JSONArray items = vendor_data.getJSONArray("membership_history");
                if (items.length() > 0) {
                    createMembershipHistory(items);
                } else
                    recyclerView.setVisibility(View.GONE);
            } else
                recyclerView.setVisibility(View.GONE);
        } else {
            if (current == 1)
                items_count.setText(vendor_data.getString("message"));
            else
                Toast.makeText(this, vendor_data.getString("message"), Toast.LENGTH_SHORT).show();
        }
    }

    private void createMembershipHistory(JSONArray items) {
        try {
            recyclerView.setVisibility(View.VISIBLE);
            String itemscounttxt = items.length() + " " + getString(R.string.items_txt);
            items_count.setText(itemscounttxt);
            planhistory_arraylist = new ArrayList<>();
            for (int i = 0; i < items.length(); i++) {
                planhistory_hashmap = new HashMap<>();
                JSONObject item_jsonObject = items.getJSONObject(i);
                String id = item_jsonObject.getString("id");
                String name = item_jsonObject.getString("name");
                String start_date = item_jsonObject.getString("start_date");
                String expiry_date = item_jsonObject.getString("expiry_date");
                String order_id = item_jsonObject.getString("order_id");
                String payment_name = item_jsonObject.getString("payment_name");
                String transaction_id = item_jsonObject.getString("transaction_id");
                String status = item_jsonObject.getString("status");
                planhistory_hashmap.put("id", id);
                planhistory_hashmap.put("name", name);
                planhistory_hashmap.put("status", status);
                planhistory_hashmap.put("start_date", start_date);
                planhistory_hashmap.put("expiry_date", expiry_date);
                planhistory_hashmap.put("order_id", order_id);
                planhistory_hashmap.put("payment_name", payment_name);
                planhistory_hashmap.put("transaction_id", transaction_id);
                planhistory_arraylist.add(planhistory_hashmap);
            }
            RunningPlansAdapter adapter = new RunningPlansAdapter(Ced_Multivendor_Plan_History.this, planhistory_arraylist);
            recyclerView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}