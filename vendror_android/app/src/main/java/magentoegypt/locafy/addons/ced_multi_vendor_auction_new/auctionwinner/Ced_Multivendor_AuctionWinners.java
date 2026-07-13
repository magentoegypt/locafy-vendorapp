package magentoegypt.locafy.addons.ced_multi_vendor_auction_new.auctionwinner;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import magentoegypt.locafy.R;
import magentoegypt.locafy.api_request_response_section.Ced_ClientRequestResponseRest_New;
import magentoegypt.locafy.navigation_drawer.Activity.Ced_MultiVendor_NavigationActivity;
import magentoegypt.locafy.api_request_response_section.AsyncResponse;
import magentoegypt.locafy.vendor_session.Ced_MultiVendor_VendorSessionManagement;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Ced_Multivendor_AuctionWinners extends Ced_MultiVendor_NavigationActivity {

    Boolean load = true;
    private int page = 1;
    private String get_auctionwinerUrl = "";
    private JSONObject postdata = new JSONObject();
    private RecyclerView recyclerView;
    TextView msg;
    JSONArray datalist = new JSONArray();
    private boolean filterSelected = false;
    ImageView filter_button;
    String selected_filter_status = "";

    Ced_MultiVendor_VendorSessionManagement vendorSessionManagement;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewGroup content = findViewById(R.id.MultiVendor_frame_container);
        getLayoutInflater().inflate(R.layout.activity_ced__multivendor__auction_winners, content, true);
        vendorSessionManagement = new Ced_MultiVendor_VendorSessionManagement(Ced_Multivendor_AuctionWinners.this);
        recyclerView = findViewById(R.id.recyclerView);
        filter_button = findViewById(R.id.filter_button);
        msg = findViewById(R.id.msg);
        get_auctionwinerUrl = session.getBase_Url() + "rest/V1/vauctionapi/getWinnerList";
        try {
            postdata.put("vendor_id", vendorSessionManagement.getVendorid());
            request_page_data(page);
            recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                    super.onScrollStateChanged(recyclerView, newState);
                    if (!recyclerView.canScrollVertically(1) && newState == RecyclerView.SCROLL_STATE_IDLE && load) {
                        load = false;
                        ++page;
                        request_page_data(page);
                    }
                }

                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                }
            });

            filter_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    final View filterlayout = getLayoutInflater().inflate(R.layout.filter_auctionwinner_layout, null);
                    final AlertDialog.Builder alertdialog = new AlertDialog.Builder(Ced_Multivendor_AuctionWinners.this);
                    alertdialog.setView(filterlayout);
                    final Dialog dialog = alertdialog.create();
                    AppCompatButton filter = filterlayout.findViewById(R.id.filter);
                    AppCompatButton clearfilter = filterlayout.findViewById(R.id.clearfilter);

                    final AppCompatEditText id_from = filterlayout.findViewById(R.id.id_from);
                    final AppCompatEditText id_to = filterlayout.findViewById(R.id.id_to);
                    final AppCompatEditText auctionprice_from = filterlayout.findViewById(R.id.auctionprice_from);
                    final AppCompatEditText auctionprice_to = filterlayout.findViewById(R.id.auctionprice_to);
                    final AppCompatEditText winningprice__from = filterlayout.findViewById(R.id.winningprice__from);
                    final AppCompatEditText winningprice__to = filterlayout.findViewById(R.id.winningprice__to);
                    final AppCompatEditText status = filterlayout.findViewById(R.id.status);
                    final AppCompatTextView bindingdate = filterlayout.findViewById(R.id.bindingdate);

                    try {
                        if (postdata.has("filter")) {
                            JSONObject object = new JSONObject(postdata.getString("filter"));
                            if (object.has("id")) {
                                JSONObject id = object.getJSONObject("id");
                                id_from.setText(id.getString("from"));
                                id_to.setText(id.getString("to"));
                            }
                            if (object.has("auction_price")) {
                                JSONObject auction_price = object.getJSONObject("auction_price");
                                auctionprice_from.setText(auction_price.getString("from"));
                                auctionprice_to.setText(auction_price.getString("to"));
                            }
                            if (object.has("winning_price")) {
                                JSONObject winning_price = object.getJSONObject("winning_price");
                                winningprice__from.setText(winning_price.getString("from"));
                                winningprice__to.setText(winning_price.getString("to"));
                            }
                            if (object.has("status")) {
                                status.setText(object.getString("status"));
                            }
                            if (object.has("bid_date")) {
                                bindingdate.setText(object.getString("bid_date"));
                            }

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                    bindingdate.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            selectdate_fromcalender(bindingdate, "yyyy-MM-dd HH:mm:ss");
                        }
                    });
                    filter.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //----------------------------------------------------------------------
                            try {

                                final JSONObject filterparam = new JSONObject();
                                JSONObject id_param = new JSONObject();
                                JSONObject auctionprice_param = new JSONObject();
                                JSONObject winningprice_param = new JSONObject();

                                id_param.put("from", id_from.getText().toString());
                                id_param.put("to", id_to.getText().toString());
                                filterparam.put("id", id_param);

                                auctionprice_param.put("from", auctionprice_from.getText().toString());
                                auctionprice_param.put("to", auctionprice_to.getText().toString());
                                filterparam.put("auction_price", auctionprice_param);

                                winningprice_param.put("from", winningprice__from.getText().toString());
                                winningprice_param.put("to", winningprice__to.getText().toString());
                                filterparam.put("winning_price", winningprice_param);

                                filterparam.put("status", status.getText().toString());
                                filterparam.put("bid_date", bindingdate.getText().toString());

                                postdata.put("filter", filterparam.toString());
                                page = 1;
                                datalist = new JSONArray();
                                request_page_data(page);
                                dialog.cancel();

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            //----------------------------------------------------------------------

                        }
                    });

                    clearfilter.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            id_from.setText("");
                            id_to.setText("");
                            auctionprice_from.setText("");
                            auctionprice_to.setText("");
                            winningprice__from.setText("");
                            winningprice__to.setText("");
                            status.setText("");
                            bindingdate.setText("");
                            postdata.remove("filter");
                            page = 1;
                            datalist = new JSONArray();
                            request_page_data(page);
                            dialog.dismiss();
                        }
                    });

                    dialog.show();
                }
            });

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void request_page_data(final int page) {
        try {
            postdata.put("page", page);
            Ced_ClientRequestResponseRest_New response = new Ced_ClientRequestResponseRest_New(new AsyncResponse() {
                @Override
                public void processFinish(Object output) throws JSONException {
                    JSONObject vendor_data = new JSONObject(output.toString()).getJSONObject("vendor_data");
                    if (vendor_data.getBoolean("success") && vendor_data.get("winner_list") != null) {
                        load = true;
                        msg.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.VISIBLE);
                        JSONArray winner_listArray = vendor_data.getJSONArray("winner_list");
                        for (int k = 0; k < winner_listArray.length(); k++) {
                            datalist.put(winner_listArray.getJSONObject(k));
                        }
                        AcuctionWinner_Adapter adapter = new AcuctionWinner_Adapter(datalist, Ced_Multivendor_AuctionWinners.this);
                        recyclerView.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                    } else {
                        load = false;
                        if (page == 1) {
                            msg.setVisibility(View.VISIBLE);
                            recyclerView.setVisibility(View.GONE);
                            if (vendor_data.has("message")) {
                                msg.setText(vendor_data.getString("message"));
                            }
                        }
                        Log.d("REpo", "request_page_date_else: ");
                    }
                }
            }, this, "POST", "" + postdata, true);
            response.execute(get_auctionwinerUrl);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}