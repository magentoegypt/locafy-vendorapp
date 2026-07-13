package magentoegypt.locafy.vendor_product_review_and_rating.ManageRating;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.reflect.TypeToken;
import magentoegypt.locafy.navigation_drawer.Activity.Ced_MultiVendor_NavigationActivity;
import magentoegypt.locafy.api_request_response_section.AsyncResponse;
import magentoegypt.locafy.api_request_response_section.Ced_ClientRequestResponseRest_New;
import magentoegypt.locafy.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ManageProductRating extends Ced_MultiVendor_NavigationActivity {
    private final int page = 1;
    private final JSONObject postdata = new JSONObject();
    private final Type ratingConversionType = new TypeToken<List<RatingItemModel>>() {
    }.getType();
    private final boolean filterSelected = false;
    private final JSONObject status_list = new JSONObject();
    private String getRatingListUrl = "";
    private RecyclerView ratingListRecycler;
    private TextView msg;
    private List<RatingItemModel> ratingModelList;
    private ImageView filter_button;
    private String selected_filter_status = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewGroup content = findViewById(R.id.MultiVendor_frame_container);
        getLayoutInflater().inflate(R.layout.activity_manage_product_rating, content, true);
        ratingListRecycler = findViewById(R.id.ratingListRecycler);
        filter_button = findViewById(R.id.filter_button);
        msg = findViewById(R.id.msg);
        getRatingListUrl = session.getBase_Url() + "rest/V1/vproductreviewapi/getRatingList";
        try {
            postdata.put("vendor_id", session.getVendorid());
            request_page_date();

            filter_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //  AlertDialog.Builder filter_dialog = new  AlertDialog.Builder(ManageProductRating.this);
                    //  filter_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    //  filter_dialog.setView(R.layout.product_rating_filter);
                    final View filter_dialog = ManageProductRating.this.getLayoutInflater().inflate(R.layout.product_rating_filter, null);
                    final AlertDialog.Builder alert = new AlertDialog.Builder(ManageProductRating.this);
                    alert.setView(filter_dialog);
                    final Dialog d = alert.create();
                    d.show();
                    final EditText id_edt = filter_dialog.findViewById(R.id.id_edt);
                    final EditText assesment_edt = filter_dialog.findViewById(R.id.assesment_edt);
                    final EditText order_edt = filter_dialog.findViewById(R.id.order_edt);
                    final Spinner isactive_spn = filter_dialog.findViewById(R.id.isactive_spn);

                    ArrayList<String> values_list = new ArrayList<>();
                    values_list.add("Select Status ");
                    if (status_list.length() > 0) {
                        final JSONArray status_keys = status_list.names();
                        for (int i = 0; i < Objects.requireNonNull(status_keys).length(); i++) {
                            try {
                                values_list.add(status_list.getString(status_keys.getString(i)));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        isactive_spn.setAdapter(new ArrayAdapter<>(ManageProductRating.this, R.layout.simple_list_item_1, values_list));
                        isactive_spn.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                if (position == 1) {
                                    selected_filter_status = "";
                                } else {
                                    try {
                                        selected_filter_status = status_keys.getString(position - 1);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {
                                selected_filter_status = "";
                            }
                        });
                    }

                    filter_dialog.findViewById(R.id.dialog_reset_btn).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v12) {
                            id_edt.setText("");
                            assesment_edt.setText("");
                            order_edt.setText("");
                            isactive_spn.setSelection(0);
                            d.dismiss();
                        }
                    });

                    filter_dialog.findViewById(R.id.dialog_apply_btn).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v1) {
                            try {
                                JSONObject filters = new JSONObject();
                                filters.put("is_active", selected_filter_status);
                                filters.put("sort_order", order_edt.getText().toString());
                                filters.put("rating_code", assesment_edt.getText().toString());
                                filters.put("rating_id", id_edt.getText().toString());
                                postdata.put("filter", filters.toString());
                                ManageProductRating.this.request_page_date();
                                d.dismiss();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                    d.show();
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void request_page_date() throws JSONException {
        postdata.put("page", "" + page);
        Ced_ClientRequestResponseRest_New response = new Ced_ClientRequestResponseRest_New(new AsyncResponse() {
            @Override
            public void processFinish(Object output) throws JSONException {
                Log.d("REpo", "request_page_date_58: " + output);
                JSONObject vendor_data = new JSONObject(output.toString()).getJSONObject("vendor_data");
                if (vendor_data.getBoolean("status")) {
                    msg.setVisibility(View.GONE);
                    JSONArray ratingLisLtArray = vendor_data.getJSONArray("rating_list");
                    /*ratingModelList = converter.fromJson(ratingLisLtArray.toString(), ratingConversionType);*/
                    RatingListAdapter ratingListAdapter = new RatingListAdapter(/*ratingModelList*/ratingLisLtArray, ManageProductRating.this);
                    ratingListRecycler.setAdapter(ratingListAdapter);
                    ratingListAdapter.notifyDataSetChanged();
//                status_list=vendor_data.getJSONObject("status_list");
                } else {
                    msg.setVisibility(View.VISIBLE);
                    if (vendor_data.has("message")) {
                        msg.setText(vendor_data.getString("message"));
                    }
                    Log.d("REpo", "request_page_date_else: ");
                }
            }
        }, this, "POST", postdata.toString(), true);
        response.execute(getRatingListUrl);
    }
}