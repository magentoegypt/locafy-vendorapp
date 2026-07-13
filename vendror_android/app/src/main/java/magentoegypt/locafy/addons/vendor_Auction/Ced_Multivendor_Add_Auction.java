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
import  magentoegypt.locafy_constant.AppConstant;
import  magentoegypt.locafy.base_app.Ced_MultiVendor_FontSetting;
import  magentoegypt.locafy.base_app.Ced_MultiVendor_VendorSplash;
import  magentoegypt.locafy.navigation_drawer.Activity.Ced_MultiVendor_NavigationActivity;
import  magentoegypt.locafy.api_request_response_section.network_error_handling.Ced_MultiVendor_ConnectionDetector;
import  magentoegypt.locafy.api_request_response_section.network_error_handling.Ced_MultiVendor_NoInternetconnection;
import  magentoegypt.locafy.api_request_response_section.AsyncResponse;
import  magentoegypt.locafy.api_request_response_section.Ced_MultiVendor_ClientRequestResponse;
import  magentoegypt.locafy.vendor_session.Ced_MultiVendor_VendorSessionManagement;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by cedcoss on 21/1/18.
 */

public class Ced_Multivendor_Add_Auction extends Ced_MultiVendor_NavigationActivity
{
    Ced_MultiVendor_ConnectionDetector connectionDetector;
    Ced_MultiVendor_VendorSessionManagement vendorSessionManagement;
    String url;
    HashMap<String, String> dataforproducts;

    Dialog listDialog;
    String id ;
    String product_id ;
    Ced_MultiVendor_FontSetting fontSetting;
    String product_name ;
    String product_type ;
    String price ;
    String quantity ;
    Boolean load = true;
    String datafilterjson="";
    ArrayList<HashMap<String,String>> adapter_data;
    int current = 1;
    ListView list_auction_listing;
    LinearLayout MultiVendor_filtersection;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        connectionDetector=new Ced_MultiVendor_ConnectionDetector(getApplicationContext());
        vendorSessionManagement=new Ced_MultiVendor_VendorSessionManagement(getApplicationContext());

        if(connectionDetector.isConnectingToInternet())
        {
            ViewGroup content = (ViewGroup) findViewById(R.id.MultiVendor_frame_container);
                getLayoutInflater().inflate(R.layout.cedmultivendor_add_auction, content, true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.backbutton);
            url=session.getBase_Url()+"vauctionapi/auction/productlist";
            dataforproducts = new HashMap<String, String>();
            adapter_data = new ArrayList<>();
            dataforproducts.put("hashkey",vendorSessionManagement.getHahkey());
            dataforproducts.put("vendor_id",vendorSessionManagement.getVendorid());
            list_auction_listing = (ListView)findViewById(R.id.list_auction_listing);
            MultiVendor_filtersection = (LinearLayout)findViewById(R.id.MultiVendor_filtersection);
            fontSetting= new Ced_MultiVendor_FontSetting();
            MultiVendor_filtersection.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    AppConstant.lockButton(v);
                    showfilter();
                }
            });

            if(getIntent().getStringExtra("filter")!=null)
            {
                datafilterjson=getIntent().getStringExtra("filter");
                dataforproducts.put("filter",datafilterjson);
                Log.i("REpo","filters-- "+dataforproducts.toString());
            }
            Log.i("REpo","filters-- "+dataforproducts.toString());
            requestData();
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
            View v = li.inflate(R.layout.ced_multivendor_add_auction_filter,null, false);

            final TextView MultiVendor_txt_id= (TextView) v.findViewById(R.id.MultiVendor_txt_id);
            final TextView MultiVendor_txt_product_id= (TextView) v.findViewById(R.id.MultiVendor_txt_product_id);
            final TextView MultiVendor_txt_product_name= (TextView) v.findViewById(R.id.MultiVendor_txt_product_name);
            final TextView MultiVendor_txt_product_type= (TextView) v.findViewById(R.id.MultiVendor_txt_product_type);
            final TextView MultiVendor_txt_price= (TextView) v.findViewById(R.id.MultiVendor_txt_price);
            final TextView MultiVendor_txt_qty= (TextView) v.findViewById(R.id.MultiVendor_txt_qty);


            final EditText MultiVendor_edt_id = (EditText)v.findViewById(R.id.MultiVendor_edt_id);
            final EditText MultiVendor_edt_product_id = (EditText)v.findViewById(R.id.MultiVendor_edt_product_id);
            final EditText MultiVendor_edt_product_name = (EditText)v.findViewById(R.id.MultiVendor_edt_product_name);
            final EditText MultiVendor_edt_producttype = (EditText)v.findViewById(R.id.MultiVendor_edt_producttype);
            final EditText MultiVendor_edt_price = (EditText)v.findViewById(R.id.MultiVendor_edt_price);
            final EditText MultiVendor_edt_qty = (EditText)v.findViewById(R.id.MultiVendor_edt_qty);

            final TextView MultiVendor_setfilter = (TextView) v.findViewById(R.id.MultiVendor_setfilter);
            final TextView MultiVendor_unsetfilter = (TextView) v.findViewById(R.id.MultiVendor_unsetfilter);


            fontSetting.setFontforTextviews(MultiVendor_txt_id,"Roboto-Medium.ttf",getApplicationContext());
            fontSetting.setFontforTextviews(MultiVendor_txt_product_id,"Roboto-Medium.ttf",getApplicationContext());
            fontSetting.setFontforTextviews(MultiVendor_txt_product_name,"Roboto-Medium.ttf",getApplicationContext());
            fontSetting.setFontforTextviews(MultiVendor_txt_product_type,"Roboto-Medium.ttf",getApplicationContext());
            fontSetting.setFontforTextviews(MultiVendor_txt_price,"Roboto-Medium.ttf",getApplicationContext());
            fontSetting.setFontforTextviews(MultiVendor_txt_qty,"Roboto-Medium.ttf",getApplicationContext());
            ;
            fontSetting.setFontforTextviews(MultiVendor_setfilter,"Roboto-Medium.ttf",getApplicationContext());
            fontSetting.setFontforTextviews(MultiVendor_unsetfilter,"Roboto-Medium.ttf",getApplicationContext());

            if(!(datafilterjson.isEmpty()))
            {
                JSONObject object=new JSONObject(datafilterjson);

                MultiVendor_edt_id.setText(object.getString("id"));
                MultiVendor_edt_product_id.setText(object.getString("product_id"));
                MultiVendor_edt_product_name.setText(object.getString("name"));
                MultiVendor_edt_producttype.setText(object.getString("type"));
                MultiVendor_edt_price.setText(object.getString("price"));
                MultiVendor_edt_qty.setText(object.getString("qty"));

                Toast.makeText(this, "Not Empty", Toast.LENGTH_SHORT).show();

            }

            MultiVendor_unsetfilter.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    AppConstant.lockButton(v);
                    MultiVendor_edt_id.setText("");
                    MultiVendor_edt_product_id.setText("");
                    MultiVendor_edt_product_name.setText("");
                    MultiVendor_edt_producttype.setText("");
                    MultiVendor_edt_price.setText("");
                    MultiVendor_edt_qty.setText("");

                    datafilterjson="";
                    final JSONObject mainfilter=new JSONObject();
                    JSONObject pricejsonObject=new JSONObject();

                    try
                    {
                        listDialog.dismiss();

                        mainfilter.put("id",MultiVendor_edt_id.getText());
                        mainfilter.put("product_id",MultiVendor_edt_product_id.getText());
                        mainfilter.put("name",MultiVendor_edt_product_name.getText());
                        mainfilter.put("type",MultiVendor_edt_producttype.getText());
                        mainfilter.put("price",MultiVendor_edt_price.getText());
                        mainfilter.put("qty",MultiVendor_edt_qty.getText());


                        Intent intent=new Intent(getApplicationContext(),Ced_Multivendor_Add_Auction.class);
                        intent.putExtra("filter",mainfilter.toString());
                        startActivity(intent);

                    }
                    catch (JSONException e)
                    {
                        Intent main = new Intent(Ced_Multivendor_Add_Auction.this, Ced_MultiVendor_VendorSplash.class);
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

                        mainfilter.put("id",MultiVendor_edt_id.getText());
                        mainfilter.put("product_id",MultiVendor_edt_product_id.getText());
                        mainfilter.put("name",MultiVendor_edt_product_name.getText());
                        mainfilter.put("type",MultiVendor_edt_producttype.getText());
                        mainfilter.put("price",MultiVendor_edt_price.getText());
                        mainfilter.put("qty",MultiVendor_edt_qty.getText());;



                        Intent intent1=new Intent(Ced_Multivendor_Add_Auction.this,Ced_Multivendor_Add_Auction.class);
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

    private void requestData()
    {

         Log.d("postdata","postdata "+dataforproducts.toString());
        final Ced_MultiVendor_ClientRequestResponse crr = new Ced_MultiVendor_ClientRequestResponse(new AsyncResponse()
        {
            @Override
            public void processFinish(Object output) throws JSONException
            {
                JSONObject root = new JSONObject(output.toString());
                final JSONObject data = root.getJSONObject("data");
                HashMap<String,String> inner_data ;


                if(data.getString("success").equalsIgnoreCase("true"))
                {
                    JSONArray product_list = data.getJSONArray("product_list");
                    JSONObject jo_inner;

                    for (int i=0;i<product_list.length();i++)
                    {
                        inner_data = new HashMap<>();
                        jo_inner = product_list.getJSONObject(i);

                        id = jo_inner.getString("id");
                        product_id = jo_inner.getString("product_id");
                        product_name = jo_inner.getString("name");
                        product_type = jo_inner.getString("type");
                        price = jo_inner.getString("price");
                        quantity = jo_inner.getString("qty");



                        inner_data.put("id",id);
                        inner_data.put("product_id",product_id);
                        inner_data.put("product_name",product_name);
                        inner_data.put("product_type",product_type);
                        inner_data.put("price",price);
                        inner_data.put("quantity",quantity);

                        Log.d("listdata-id",""+jo_inner.getString("id"));
                        Log.d("listdata-product_id",""+jo_inner.getString("product_id"));
                        Log.d("listdata-product_name",""+jo_inner.getString("name"));
                        Log.d("listdata-product_price",""+jo_inner.getString("price"));
                        Log.d("listdata-type",""+jo_inner.getString("type"));
                        Log.d("listdata-quantity",""+jo_inner.getString("qty"));


                        adapter_data.add(inner_data);
                    }
                    Ced_Multivendor_Add_Auction_Adapter adapter = new Ced_Multivendor_Add_Auction_Adapter(Ced_Multivendor_Add_Auction.this,adapter_data);
                    list_auction_listing.setAdapter(adapter);

                    /****************************************************************************************/

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

                                        if(data.getString("success").equalsIgnoreCase("true"))
                                        {
                                            JSONArray product_list = data.getJSONArray("product_list");
                                            JSONObject jo_inner;

                                            for (int i=0;i<product_list.length();i++)
                                            {
                                                inner_data = new HashMap<>();
                                                jo_inner = product_list.getJSONObject(i);

                                                id = jo_inner.getString("id");
                                                product_id = jo_inner.getString("product_id");
                                                product_name = jo_inner.getString("name");
                                                product_type = jo_inner.getString("type");
                                                price = jo_inner.getString("price");
                                                quantity = jo_inner.getString("qty");



                                                inner_data.put("id",id);
                                                inner_data.put("product_id",product_id);
                                                inner_data.put("product_name",product_name);
                                                inner_data.put("product_type",product_type);
                                                inner_data.put("price",price);
                                                inner_data.put("quantity",quantity);

                                                Log.d("listdata-id",""+jo_inner.getString("id"));
                                                Log.d("listdata-product_id",""+jo_inner.getString("product_id"));
                                                Log.d("listdata-product_name",""+jo_inner.getString("name"));
                                                Log.d("listdata-product_price",""+jo_inner.getString("price"));
                                                Log.d("listdata-type",""+jo_inner.getString("type"));
                                                Log.d("listdata-quantity",""+jo_inner.getString("qty"));


                                                adapter_data.add(inner_data);
                                            }
                                            Ced_Multivendor_Add_Auction_Adapter adapter = new Ced_Multivendor_Add_Auction_Adapter(Ced_Multivendor_Add_Auction.this,adapter_data);
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
                                },Ced_Multivendor_Add_Auction.this,"POST",dataforproducts);
                                crr.execute(url+"/page/"+current);
                            }



                        }
                    });

                    /****************************************************************************************/
                }

                else
                {
                    Toast.makeText(getApplicationContext(), data.getString("message"), Toast.LENGTH_LONG).show();
                }
            }
        }, Ced_Multivendor_Add_Auction.this, "POST", dataforproducts);
        crr.execute(url+"/page/"+current);





    }
}
