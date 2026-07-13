package magentoegypt.locafy.addons.vendor_member_ship_plans;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import magentoegypt.locafy.R;
import magentoegypt.locafy.api_request_response_section.AsyncResponse;
import magentoegypt.locafy.api_request_response_section.Ced_ClientRequestResponseRest_Data_Array;
import magentoegypt.locafy.api_request_response_section.network_error_handling.Ced_MultiVendor_ConnectionDetector;
import magentoegypt.locafy.navigation_drawer.Activity.Ced_MultiVendor_NavigationActivity;
import magentoegypt.locafy.api_request_response_section.network_error_handling.Ced_MultiVendor_NoInternetconnection;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

/**
 * Created by Frozen on 14/09/21.
 */

public class Ced_Multivendor_Membership_Plan extends Ced_MultiVendor_NavigationActivity {
    Boolean paginate = true;
    private String membershipplan_url = "";
    private HashMap<String, String> dataformembershipplan;
    private TextView items_count;
    //    private ListView runningplanitem_listview;
    private CardView runningPlanSection, VendorMembershipPlans;
    private RecyclerView recycler, runningplanitem_listview;
    private int current = 1;
    private boolean runningplanfirst=true,membershipPlanFirst=true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (android.os.Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(this.getResources().getColor(R.color.status_bar_color));
        }
        Ced_MultiVendor_ConnectionDetector connectionDetector = new Ced_MultiVendor_ConnectionDetector(getApplicationContext());
        if (connectionDetector.isConnectingToInternet()) {
            try {
                ViewGroup content = findViewById(R.id.MultiVendor_frame_container);
                getLayoutInflater().inflate(R.layout.ced_multivendor_membership_plan, content, true);
                Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
                getSupportActionBar().setHomeAsUpIndicator(R.drawable.backbutton);
                runningPlanSection = findViewById(R.id.runningPlanSection);
                VendorMembershipPlans = findViewById(R.id.VendorMembershipPlans);
                items_count = findViewById(R.id.MultiVendor_ItemCount);
                recycler = findViewById(R.id.vendorMembershipPlans_recycler);
                recycler.setHasFixedSize(false);
                GridLayoutManager gridLayoutManager = new GridLayoutManager(Ced_Multivendor_Membership_Plan.this, 2, RecyclerView.VERTICAL, false);
                recycler.setLayoutManager(gridLayoutManager);
                runningplanitem_listview = findViewById(R.id.MultiVendor_RunningPlanListView);
                membershipplan_url = session.getBase_Url() + "rest/V1/plan/items";
                dataformembershipplan = new HashMap<>();
                dataformembershipplan.put("vendor_id", session.getVendorid());
//                dataformembershipplan.put("store_id", session.getStoreId());
                request_for_membershipplan();
                recycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
                    @Override
                    public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                        super.onScrolled(recyclerView, dx, dy);
                        Log.i("Scroll-", "" + recyclerView.canScrollVertically(1));

                        if (!recyclerView.canScrollVertically(1) && paginate) {
                            current = current + 1;
                            paginate = false;
                            request_for_membershipplan();
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

    public void request_for_membershipplan() {
        dataformembershipplan.put("page", String.valueOf(current));
        Log.i("senddata", "postdata " + dataformembershipplan.toString());
        Ced_ClientRequestResponseRest_Data_Array crr = new Ced_ClientRequestResponseRest_Data_Array(new AsyncResponse() {
            @Override
            public void processFinish(Object output) throws JSONException {
                Log.i("membership", output.toString());
                create_Running_Plan_Items(output.toString());
            }
        }, Ced_Multivendor_Membership_Plan.this, "POST", dataformembershipplan.toString());
        crr.execute(membershipplan_url);
    }

    public void create_Running_Plan_Items(String output) throws JSONException {
        JSONArray jsonArray = new JSONArray(output);
        JSONObject jsonObject = jsonArray.getJSONObject(0);
        if (jsonObject.has("vendor_data")) {
            JSONObject vendor_data = jsonObject.getJSONObject("vendor_data");
            if (vendor_data.getBoolean("status")) {
                if (vendor_data.has("running_plans")) {
                    JSONArray items = vendor_data.getJSONArray("running_plans");
                    if (items.length() > 0)
                        createRunningPlans(items);
                }
                else if (runningplanfirst)
                    runningplanitem_listview.setVisibility(View.GONE);

                if (vendor_data.has("membership_plans")) {
                    JSONArray membership_plans = vendor_data.getJSONArray("membership_plans");
                    if (membership_plans.length() > 0)
                        createMembershipPlans(membership_plans);
                }
                else if(membershipPlanFirst)
                    VendorMembershipPlans.setVisibility(View.GONE);
            }
            else {
                if (vendor_data.has("message")) {
                    if (current == 1)
                        items_count.setText(vendor_data.getString("message"));
                }
                if (vendor_data.has("running_plans")) {
                    JSONArray items = vendor_data.getJSONArray("running_plans");
                    if (items.length() > 0)
                        createRunningPlans(items);
                }
                else {
                    if (current > 1)
                        runningplanitem_listview.setVisibility(View.VISIBLE);
                }

                if (vendor_data.has("membership_plans")) {
                    JSONArray membership_plans = vendor_data.getJSONArray("membership_plans");
                    if (membership_plans.length() > 0) {
                        createMembershipPlans(membership_plans);
                    }
                }
                else if(membershipPlanFirst)
                    VendorMembershipPlans.setVisibility(View.GONE);
//                Toast.makeText(this, "" + vendor_data.getString("message"), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void createRunningPlans(JSONArray items) {
        try {
            runningplanitem_listview.setVisibility(View.VISIBLE);
            runningPlanSection.setVisibility(View.VISIBLE);
            VendorMembershipPlans.setVisibility(View.VISIBLE);
            String itemcounttext = items.length() + " " + getString(R.string.items_txt);
            items_count.setText(itemcounttext);
            ArrayList<HashMap<String, String>> runningplan_arraylist = new ArrayList<>();
            for (int i = 0; i < items.length(); i++) {
                HashMap<String, String> runningplan_hashmap = new HashMap<>();
                JSONObject item_jsonObject = items.getJSONObject(i);
                String id = item_jsonObject.getString("id");
                String name = item_jsonObject.getString("name");
                String start_date = item_jsonObject.getString("start_date");
                String expiry_date = item_jsonObject.getString("expire_date");
                String order_id = item_jsonObject.getString("order_id");
                String payment_name = item_jsonObject.getString("payment_method");
                String transaction_id = item_jsonObject.getString("transaction_id");
                String status = item_jsonObject.getString("status");
                runningplan_hashmap.put("id", id);
                runningplan_hashmap.put("name", name);
                runningplan_hashmap.put("status", status);
                runningplan_hashmap.put("start_date", start_date);
                runningplan_hashmap.put("expiry_date", expiry_date);
                runningplan_hashmap.put("order_id", order_id);
                runningplan_hashmap.put("payment_name", payment_name);
                runningplan_hashmap.put("transaction_id", transaction_id);
                runningplan_arraylist.add(runningplan_hashmap);
            }
            if (runningplanfirst) {
                runningplanfirst=false;
                RunningPlansAdapter adapter = new RunningPlansAdapter(Ced_Multivendor_Membership_Plan.this, runningplan_arraylist);
                runningplanitem_listview.setAdapter(adapter);
            }
            else if (runningplanitem_listview.getAdapter()!=null){
                ((RunningPlansAdapter)runningplanitem_listview.getAdapter()).data.addAll(runningplan_arraylist);
                runningplanitem_listview.getAdapter().notifyDataSetChanged();
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void createMembershipPlans(JSONArray membership_plans) {
        try {
            List<MembershipPlan_ItemDataModel> itemDataModels = new ArrayList<>();
            for (int i = 0; i < membership_plans.length(); i++) {
                JSONObject c = membership_plans.getJSONObject(i);
                String already_subscribed, show_addtocart, special_price;
                if (c.has("special_price"))
                    special_price = c.getString("special_price");
                else
                    special_price = "no_special";
                if (c.has("already_subscribed"))
                    already_subscribed = c.getString("already_subscribed");
                else
                    already_subscribed = "false";
                if (c.has("show_addtocart"))
                    show_addtocart = c.getString("show_addtocart");
                else
                    show_addtocart = "false";
                itemDataModels.add(new MembershipPlan_ItemDataModel(
                        c.getString("membership_id"),
                        c.getString("membership_name"),
                        c.getString("membership_image"),
                        c.getString("price"),
                        special_price,
                        already_subscribed,
                        show_addtocart));
            }
            if (membershipPlanFirst) {
                membershipPlanFirst=false;
                MembershipPlan_ItemAdapter itemAdapter = new MembershipPlan_ItemAdapter(Ced_Multivendor_Membership_Plan.this, itemDataModels);
                recycler.setAdapter(itemAdapter);
            }
            else if (recycler.getAdapter()!=null){
                ((MembershipPlan_ItemAdapter)recycler.getAdapter()).dataModelList.addAll(itemDataModels);
                recycler.getAdapter().notifyDataSetChanged();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}