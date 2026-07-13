package magentoegypt.locafy.addons.vendor_Auction;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import  magentoegypt.locafy.R;
import magentoegypt.locafy.api_request_response_section.Ced_MultiVendor_ClientRequestResponse;
import magentoegypt.locafy.api_request_response_section.network_error_handling.Ced_MultiVendor_NoInternetconnection;
import  magentoegypt.locafy_constant.AppConstant;
import  magentoegypt.locafy.base_app.Ced_MultiVendor_FontSetting;
import  magentoegypt.locafy.base_app.Ced_MultiVendor_VendorSplash;
import  magentoegypt.locafy.navigation_drawer.Activity.Ced_MultiVendor_NavigationActivity;
import  magentoegypt.locafy.api_request_response_section.network_error_handling.Ced_MultiVendor_ConnectionDetector;
import  magentoegypt.locafy.api_request_response_section.AsyncResponse;
import magentoegypt.locafy.vendor_session.Ced_MultiVendor_VendorSessionManagement;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by cedcoss on 25/1/18.
 */


public class Ced_Multivendor_View_Bid extends Ced_MultiVendor_NavigationActivity

{
    Ced_MultiVendor_ConnectionDetector connectionDetector;
    Ced_MultiVendor_VendorSessionManagement vendorSessionManagement;
    String url;
    Ced_MultiVendor_FontSetting fontSetting;
    HashMap<String, String> dataforproducts;

    Dialog listDialog;
    String datafilterjson="";
    String id ;
    String product_id ;
    String customer_id ;
    String customer_name ;
    String bid_price ;
    String bid_date ;
    String bid_time ;
    String winner ;
    String status ;
    LinearLayout MultiVendor_filtersection;
    Boolean load = true;
    ArrayList<HashMap<String,String>> adapter_data;
    int current = 1;
    ListView list_auction_listing;
    ListView list_bid_view;

    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        connectionDetector = new Ced_MultiVendor_ConnectionDetector(getApplicationContext());
        vendorSessionManagement = new Ced_MultiVendor_VendorSessionManagement(getApplicationContext());


        if (connectionDetector.isConnectingToInternet())
        {
            ViewGroup content = (ViewGroup) findViewById(R.id.MultiVendor_frame_container);
            getLayoutInflater().inflate(R.layout.cedmultivendor_view_bid, content, true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.backbutton);
            list_bid_view = (ListView) findViewById(R.id.list_bid_view);
            dataforproducts = new HashMap<>();
            adapter_data = new ArrayList<>();
            id = getIntent().getStringExtra("auction_id");
            product_id=getIntent().getStringExtra("product_id");
            MultiVendor_filtersection = (LinearLayout)findViewById(R.id.MultiVendor_filtersection);
            fontSetting = new Ced_MultiVendor_FontSetting();


            list_auction_listing = (ListView)findViewById(R.id.list_bid_view);
            dataforproducts.put("hashkey",vendorSessionManagement.getHahkey());
            dataforproducts.put("vendor_id",vendorSessionManagement.getVendorid());
            dataforproducts.put("auction_id",id);
            dataforproducts.put("product_id",product_id);

            url=session.getBase_Url()+"vauctionapi/bid/view";

            if(getIntent().getStringExtra("filter")!=null)
            {
                dataforproducts.put("filter",getIntent().getStringExtra("filter"));
            }

            requestData(dataforproducts);

            MultiVendor_filtersection.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view)
                {
                    AppConstant.lockButton(view);
                    showfilter();
                }
            });

            list_auction_listing.setOnScrollListener(new AbsListView.OnScrollListener()
            {
                @Override
                public void onScrollStateChanged(AbsListView absListView, int i)
                {

                }

                @Override
                public void onScroll(AbsListView absListView, int firstVisibleItem, int visibleItemCount, int totalItemCount)
                {

                    if(firstVisibleItem==0)
                    {
                        getSupportActionBar().show();
                    }
                    else
                    {
                        getSupportActionBar().hide();
                    }

                    if (((firstVisibleItem + visibleItemCount) == totalItemCount) && load)
                    {
                        current = current + 1;
                        load = false;

                        Ced_MultiVendor_ClientRequestResponse crr = new Ced_MultiVendor_ClientRequestResponse(new AsyncResponse()
                        {

                            @Override
                            public void processFinish(Object output) throws JSONException
                            {
                                JSONObject root = new JSONObject(output.toString());
                                final JSONObject data = root.getJSONObject("data");
                                HashMap<String,String> inner_data ;

                                Log.d("vaibhav",""+data.toString());

                                if(data.getString("success").equalsIgnoreCase("true"))
                                {
                                    JSONArray bid_detail = data.getJSONArray("bid_detail");
                                    JSONObject jo_inner;

                                    for (int i=0;i<bid_detail.length();i++)
                                    {
                                        inner_data = new HashMap<>();
                                        jo_inner = bid_detail.getJSONObject(i);

                                        id = jo_inner.getString("id");
                                        product_id = jo_inner.getString("product_id");
                                        customer_id = jo_inner.getString("customer_id");
                                        customer_name = jo_inner.getString("customer_name");
                                        bid_price = jo_inner.getString("bid_price");
                                        bid_date =jo_inner.getString("bid_date");
                                        bid_time = jo_inner.getString("bid_time");
                                        winner = jo_inner.getString("winner");
                                        status = jo_inner.getString("status");

                                        inner_data.put("id",id);
                                        inner_data.put("product_id",product_id);
                                        inner_data.put("customer_id",customer_id);
                                        inner_data.put("customer_name",customer_name);
                                        inner_data.put("bid_price",bid_price);
                                        inner_data.put("bid_date",bid_date);
                                        inner_data.put("bid_time",bid_time);
                                        inner_data.put("winner",winner);
                                        inner_data.put("status",status);

                                        Log.d("listdata-id",""+jo_inner.getString("id"));
                                        Log.d("listdata-product_id",""+jo_inner.getString("product_id"));
                                        Log.d("listdata-customer_id",""+jo_inner.getString("customer_id"));
                                        Log.d("listdata-customer_name",""+jo_inner.getString("customer_name"));
                                        Log.d("listdata-bid_price",""+jo_inner.getString("bid_price"));
                                        Log.d("listdata-bid_date",""+jo_inner.getString("bid_date"));
                                        Log.d("listdata-bid_time",""+jo_inner.getString("bid_time"));
                                        Log.d("listdata-winner",""+jo_inner.getString("winner"));
                                        Log.d("listdata-status",""+jo_inner.getString("status"));


                                        adapter_data.add(inner_data);
                                    }
                                    Ced_Multivendor_view_Bid_Adapter adapter = new Ced_Multivendor_view_Bid_Adapter(Ced_Multivendor_View_Bid.this,adapter_data);
                                    list_auction_listing.setAdapter(adapter);
                                    int cp = list_auction_listing.getFirstVisiblePosition();
                                    list_auction_listing.setAdapter(adapter);
                                    list_auction_listing.setSelectionFromTop(cp+1, 0);
                                    adapter.notifyDataSetChanged();
                                    load = true;

                                    /****************************************************************************************/

                                    /****************************************************************************************/
                                }

                            }
                        },Ced_Multivendor_View_Bid.this,"POST",dataforproducts);
                        crr.execute(url+"/page/"+current);
                    }



                }
            });


        }
        else

        {
                Intent nointernet=new Intent(getApplicationContext(), Ced_MultiVendor_NoInternetconnection.class);
                nointernet.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                nointernet.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(nointernet);
                overridePendingTransition(R.anim.ced_multivendor_slide_in, R.anim.ced_multivendor_slide_out);


        }
    }


    private void showfilter()
    {
        try
        {
            listDialog = new Dialog(this,R.style.PauseDialog);
            listDialog.setTitle(getResources().getString(R.string.alert_name));
            LayoutInflater li = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View v = li.inflate(R.layout.ced_multivendor_view_bid_filter,null, false);

            final TextView MultiVendor_txt_product_id= (TextView) v.findViewById(R.id.MultiVendor_txt_product_id);
            final TextView MultiVendor_txt_customer_id= (TextView) v.findViewById(R.id.MultiVendor_txt_customer_id);
            final TextView MultiVendor_txt_customer_name= (TextView) v.findViewById(R.id.MultiVendor_txt_customer_name);
            final TextView MultiVendor_txt_bid_price= (TextView) v.findViewById(R.id.MultiVendor_txt_bid_price);
            final TextView MultiVendor_txt_bid_date= (TextView) v.findViewById(R.id.MultiVendor_txt_bid_date);
            final TextView MultiVendor_txt_bid_time= (TextView) v.findViewById(R.id.MultiVendor_txt_bid_time);
            final TextView MultiVendor_txt_winner= (TextView) v.findViewById(R.id.MultiVendor_txt_winner);
            final TextView MultiVendor_txt_status= (TextView) v.findViewById(R.id.MultiVendor_txt_status);



            final EditText MultiVendor_edt_product_id = (EditText)v.findViewById(R.id.MultiVendor_edt_product_id);
            final EditText MultiVendor_edt_customer_id = (EditText)v.findViewById(R.id.MultiVendor_edt_customer_id);
            final EditText MultiVendor_edt_customer_name = (EditText)v.findViewById(R.id.MultiVendor_edt_customer_name);
            final EditText MultiVendor_edt_bid_price = (EditText)v.findViewById(R.id.MultiVendor_edt_bid_price);
            final EditText MultiVendor_edt_bid_date = (EditText)v.findViewById(R.id.MultiVendor_edt_bid_date);
            final EditText MultiVendor_edt_bid_time = (EditText)v.findViewById(R.id.MultiVendor_edt_bid_time);
            final EditText MultiVendor_edt_winner = (EditText)v.findViewById(R.id.MultiVendor_edt_winner);
            final EditText MultiVendor_edt_status = (EditText)v.findViewById(R.id.MultiVendor_edt_status);

            final TextView MultiVendor_setfilter = (TextView) v.findViewById(R.id.MultiVendor_setfilter);
            final TextView MultiVendor_unsetfilter = (TextView) v.findViewById(R.id.MultiVendor_unsetfilter);



            fontSetting.setFontforTextviews(MultiVendor_txt_product_id,"Roboto-Medium.ttf",getApplicationContext());
            fontSetting.setFontforTextviews(MultiVendor_txt_customer_id,"Roboto-Medium.ttf",getApplicationContext());
            fontSetting.setFontforTextviews(MultiVendor_txt_customer_name,"Roboto-Medium.ttf",getApplicationContext());
            fontSetting.setFontforTextviews(MultiVendor_txt_bid_price,"Roboto-Medium.ttf",getApplicationContext());
            fontSetting.setFontforTextviews(MultiVendor_txt_bid_date,"Roboto-Medium.ttf",getApplicationContext());
            fontSetting.setFontforTextviews(MultiVendor_txt_bid_time,"Roboto-Medium.ttf",getApplicationContext());
            fontSetting.setFontforTextviews(MultiVendor_txt_winner,"Roboto-Medium.ttf",getApplicationContext());
            fontSetting.setFontforTextviews(MultiVendor_txt_status,"Roboto-Medium.ttf",getApplicationContext());


            fontSetting.setFontforTextviews(MultiVendor_setfilter,"Roboto-Medium.ttf",getApplicationContext());
            fontSetting.setFontforTextviews(MultiVendor_unsetfilter,"Roboto-Medium.ttf",getApplicationContext());

            if(!(datafilterjson.isEmpty()))
            {
                JSONObject object=new JSONObject(datafilterjson);



                MultiVendor_edt_product_id.setText(object.getString("product_id"));
                MultiVendor_edt_customer_id.setText(object.getString("customer_id"));
                MultiVendor_edt_customer_name.setText(object.getString("customer_name"));
                MultiVendor_edt_bid_price.setText(object.getString("bid_price"));
                MultiVendor_edt_bid_date.setText(object.getString("bid_date"));
                MultiVendor_edt_bid_time.setText(object.getString("bid_time"));
                MultiVendor_edt_winner.setText(object.getString("winner"));
                MultiVendor_edt_status.setText(object.getString("status"));



                Toast.makeText(this, "Not Empty", Toast.LENGTH_SHORT).show();

            }

            MultiVendor_unsetfilter.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    AppConstant.lockButton(v);
                    MultiVendor_edt_product_id.setText("");
                    MultiVendor_edt_customer_id.setText("");
                    MultiVendor_edt_customer_name.setText("");
                    MultiVendor_edt_bid_price.setText("");
                    MultiVendor_edt_bid_date.setText("");
                    MultiVendor_edt_bid_time.setText("");
                    MultiVendor_edt_winner.setText("");
                    MultiVendor_edt_status.setText("");

                    datafilterjson="";
                    final JSONObject mainfilter=new JSONObject();
                    JSONObject pricejsonObject=new JSONObject();

                    try
                    {
                        listDialog.dismiss();

                        //   mainfilter.put("id",MultiVendor_edt_id.getText());
                        mainfilter.put("product_id",MultiVendor_edt_product_id.getText());
                        mainfilter.put("customer_id",MultiVendor_edt_customer_id.getText());
                        mainfilter.put("customer_name",MultiVendor_edt_customer_name.getText());
                        mainfilter.put("bid_price",MultiVendor_edt_bid_price.getText());
                        mainfilter.put("bid_date", MultiVendor_edt_bid_date.getText());
                        mainfilter.put("bid_time",MultiVendor_edt_bid_time.getText());
                        mainfilter.put("winner",MultiVendor_edt_winner.getText());
                        mainfilter.put("status",MultiVendor_edt_status.getText());


                        Intent intent=new Intent(getApplicationContext(),Ced_Multivendor_View_Bid.class);
                        intent.putExtra("filter",mainfilter.toString());
                        startActivity(intent);

                    }
                    catch (JSONException e)
                    {
                        Intent main = new Intent(Ced_Multivendor_View_Bid.this, Ced_MultiVendor_VendorSplash.class);
                        main.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        main.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(main);
                        overridePendingTransition(R.anim.ced_multivendor_slide_in, R.anim.ced_multivendor_slide_out);
                    }


                }
            });

            MultiVendor_setfilter.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    AppConstant.lockButton(v);
                    final JSONObject mainfilter=new JSONObject();
                    JSONObject pricejsonObject=new JSONObject();
                    try
                    {
                        listDialog.dismiss();

                        // mainfilter.put("id",MultiVendor_edt_id.getText());
                        mainfilter.put("product_id",MultiVendor_edt_product_id.getText());
                        mainfilter.put("customer_id",MultiVendor_edt_customer_id.getText());
                        mainfilter.put("customer_name",MultiVendor_edt_customer_name.getText());
                        mainfilter.put("bid_price",MultiVendor_edt_bid_price.getText());
                        mainfilter.put("bid_date", MultiVendor_edt_bid_date.getText());
                        mainfilter.put("bid_time",MultiVendor_edt_bid_time.getText());
                        mainfilter.put("winner",MultiVendor_edt_winner.getText());
                        mainfilter.put("status",MultiVendor_edt_status.getText());



                        Intent intent1=new Intent(Ced_Multivendor_View_Bid.this,Ced_Multivendor_View_Bid.class);
                        intent1.putExtra("filter",mainfilter.toString());
                        //intent1.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        startActivity(intent1);


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            });
            listDialog.setContentView(v);
            listDialog.setCancelable(true);
            listDialog.show();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

    }

    private void requestData(HashMap<String, String> dataforproducts)

    {
        final Ced_MultiVendor_ClientRequestResponse crr = new Ced_MultiVendor_ClientRequestResponse(new AsyncResponse()
        {
            @Override
            public void processFinish(Object output) throws JSONException
            {
                JSONObject root = new JSONObject(output.toString());
                final JSONObject data = root.getJSONObject("data");
                HashMap<String,String> inner_data ;

                Log.d("vaibhav",""+data.toString());

                if(data.getString("success").equalsIgnoreCase("true"))
                {
                    JSONArray bid_detail = data.getJSONArray("bid_detail");
                    JSONObject jo_inner;

                    for (int i=0;i<bid_detail.length();i++)
                    {
                        inner_data = new HashMap<>();
                        jo_inner = bid_detail.getJSONObject(i);

                         id = jo_inner.getString("id");
                         product_id = jo_inner.getString("product_id");
                         customer_id = jo_inner.getString("customer_id");
                         customer_name = jo_inner.getString("customer_name");
                         bid_price = jo_inner.getString("bid_price");
                         bid_date =jo_inner.getString("bid_date");
                         bid_time = jo_inner.getString("bid_time");
                         winner = jo_inner.getString("winner");
                         status = jo_inner.getString("status");

                        inner_data.put("id",id);
                        inner_data.put("product_id",product_id);
                        inner_data.put("customer_id",customer_id);
                        inner_data.put("customer_name",customer_name);
                        inner_data.put("bid_price",bid_price);
                        inner_data.put("bid_date",bid_date);
                        inner_data.put("bid_time",bid_time);
                        inner_data.put("winner",winner);
                        inner_data.put("status",status);

                        Log.d("listdata-id",""+jo_inner.getString("id"));
                        Log.d("listdata-product_id",""+jo_inner.getString("product_id"));
                        Log.d("listdata-customer_id",""+jo_inner.getString("customer_id"));
                        Log.d("listdata-customer_name",""+jo_inner.getString("customer_name"));
                        Log.d("listdata-bid_price",""+jo_inner.getString("bid_price"));
                        Log.d("listdata-bid_date",""+jo_inner.getString("bid_date"));
                        Log.d("listdata-bid_time",""+jo_inner.getString("bid_time"));
                        Log.d("listdata-winner",""+jo_inner.getString("winner"));
                        Log.d("listdata-status",""+jo_inner.getString("status"));


                        adapter_data.add(inner_data);
                    }
                    Ced_Multivendor_view_Bid_Adapter adapter = new Ced_Multivendor_view_Bid_Adapter(Ced_Multivendor_View_Bid.this,adapter_data);
                    list_auction_listing.setAdapter(adapter);


                }

                else
                {
                    Toast.makeText(getApplicationContext(), data.getString("message"), Toast.LENGTH_LONG).show();
                }
            }
        }, Ced_Multivendor_View_Bid.this, "POST", dataforproducts);
        crr.execute(url+"/page/"+current);
    }

}
