package magentoegypt.locafy.addons.vendor_Auction;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import  magentoegypt.locafy.R;
import magentoegypt.locafy.api_request_response_section.Ced_MultiVendor_ClientRequestResponse;
import  magentoegypt.locafy_constant.AppConstant;
import  magentoegypt.locafy.base_app.Ced_MultiVendor_FontSetting;
import  magentoegypt.locafy.api_request_response_section.AsyncResponse;
import  magentoegypt.locafy.vendor_session.Ced_MultiVendor_VendorSessionManagement;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by cedcoss on 25/1/18.
 */

public class Ced_Multivendor_view_Bid_Adapter extends BaseAdapter

{
    private static LayoutInflater inflater=null;
    private Activity activity;
    Ced_MultiVendor_FontSetting fontSetting;
    private ArrayList<HashMap<String, String>> datalist;

    String updt_url;
    String delete_url;
    HashMap data;
    Ced_MultiVendor_VendorSessionManagement vendorSessionManagement;

    static class ViewHolder
    {
        public TextView id;
        public TextView product_id;
        public TextView customer_id;
        public TextView customer_name;
        public TextView bid_price;
        public TextView bid_date;
        public TextView bid_time;
        public TextView winner;
        public TextView status;
        public Spinner MultiVendor_action;


        public TextView idtag;
        public TextView product_idtag;
        public TextView customer_idtag;
        public TextView customer_nametag;
        public TextView bid_pricetag;
        public TextView bid_datetag;
        public TextView bid_timetag;
        public TextView winnertag;
        public TextView statustag;
        public TextView MultiVendor_actiontag;



        public TextView MultiVendor_save_bid;



    }
    public Ced_Multivendor_view_Bid_Adapter(Activity a,ArrayList<HashMap<String, String>> datalist)
    {
        activity=a;
        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        fontSetting=new Ced_MultiVendor_FontSetting();
        this.datalist=datalist;
        data = new HashMap();
        vendorSessionManagement = new Ced_MultiVendor_VendorSessionManagement(a);
        data.put("vendor_id",vendorSessionManagement.getVendorid());
        data.put("hashkey",vendorSessionManagement.getHahkey());

    }

    @Override
    public int getCount() {
        return datalist.size();
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup)
    {
        View vi=convertView;
        if(vi==null)
        {
            vi = inflater.inflate(R.layout.ced_multivendor_view_bid_single_row, null);
            ViewHolder viewHolder = new ViewHolder();

            viewHolder.id = (TextView)vi.findViewById(R.id.MultiVendor_IDtag);
            viewHolder.product_id = (TextView)vi.findViewById(R.id.MultiVendor_product_id);
            viewHolder.customer_id = (TextView)vi.findViewById(R.id.MultiVendor_cust_id);
            viewHolder.customer_name = (TextView)vi.findViewById(R.id.MultiVendor_cust_name);
            viewHolder.bid_price = (TextView)vi.findViewById(R.id.MultiVendor_bidprice);
            viewHolder.bid_date = (TextView)vi.findViewById(R.id.MultiVendor_bid_date);
            viewHolder.bid_time = (TextView)vi.findViewById(R.id.MultiVendor_bidtime);
            viewHolder.winner = (TextView)vi.findViewById(R.id.MultiVendor_winner);
            viewHolder.status = (TextView)vi.findViewById(R.id.MultiVendor_staus);
            viewHolder.MultiVendor_action=(Spinner)vi.findViewById(R.id.MultiVendor_action);



            viewHolder.MultiVendor_save_bid = (TextView)vi.findViewById(R.id.MultiVendor_save_bid) ;

            viewHolder.product_idtag = (TextView)vi.findViewById(R.id.MultiVendor_productIDtag);
            viewHolder.customer_idtag = (TextView)vi.findViewById(R.id.MultiVendor_cust_idtag);
            viewHolder.customer_nametag = (TextView)vi.findViewById(R.id.MultiVendor_custnametag);
            viewHolder.bid_pricetag = (TextView)vi.findViewById(R.id.MultiVendor_bidpricetag);
            viewHolder.bid_datetag = (TextView)vi.findViewById(R.id.MultiVendor_biddatetag);
            viewHolder.bid_timetag = (TextView)vi.findViewById(R.id.MultiVendor_bidtimetag);
            viewHolder.winnertag = (TextView)vi.findViewById(R.id.MultiVendor_winnertag);
            viewHolder.statustag = (TextView)vi.findViewById(R.id.MultiVendor_statustag);
            viewHolder.MultiVendor_actiontag = (TextView)vi.findViewById(R.id.MultiVendor_actiontag);


            /**************************Regular*****************************/
            fontSetting.setFontforTextviews(viewHolder.product_id,"Roboto-Regular.ttf",activity);
            fontSetting.setFontforTextviews(viewHolder.id,"Roboto-Regular.ttf",activity);
            fontSetting.setFontforTextviews(viewHolder.customer_id,"Roboto-Regular.ttf",activity);
            fontSetting.setFontforTextviews(viewHolder.customer_name,"Roboto-Regular.ttf",activity);
            fontSetting.setFontforTextviews(viewHolder.bid_price,"Roboto-Regular.ttf",activity);
            fontSetting.setFontforTextviews(viewHolder.bid_date,"Roboto-Regular.ttf",activity);
            fontSetting.setFontforTextviews(viewHolder.bid_time,"Roboto-Regular.ttf",activity);
            fontSetting.setFontforTextviews(viewHolder.winner,"Roboto-Regular.ttf",activity);
            fontSetting.setFontforTextviews(viewHolder.status,"Roboto-Regular.ttf",activity);
            /*******************************************************/

            /**************************Medium*****************************/
            fontSetting.setFontforTextviews(viewHolder.product_idtag,"Roboto-Medium.ttf",activity);
            fontSetting.setFontforTextviews(viewHolder.customer_idtag,"Roboto-Medium.ttf",activity);
            fontSetting.setFontforTextviews(viewHolder.customer_nametag,"Roboto-Medium.ttf",activity);
            fontSetting.setFontforTextviews(viewHolder.bid_pricetag,"Roboto-Medium.ttf",activity);
            fontSetting.setFontforTextviews(viewHolder.bid_datetag,"Roboto-Medium.ttf",activity);
            fontSetting.setFontforTextviews(viewHolder.bid_timetag,"Roboto-Medium.ttf",activity);
            fontSetting.setFontforTextviews(viewHolder.winnertag,"Roboto-Medium.ttf",activity);
            fontSetting.setFontforTextviews(viewHolder.statustag,"Roboto-Medium.ttf",activity);
            fontSetting.setFontforTextviews(viewHolder.MultiVendor_actiontag,"Roboto-Medium.ttf",activity);

            /*******************************************************/
            vi.setTag(viewHolder);
        }

        HashMap<String,String> map_setdata = new HashMap<>();
        map_setdata = datalist.get(position);
        final ViewHolder holder = (ViewHolder)vi.getTag();

        holder.id.setText(map_setdata.get("id"));
        holder.product_id.setText(map_setdata.get("product_id"));
        holder.customer_id.setText(map_setdata.get("customer_id"));
        holder.customer_name.setText(map_setdata.get("customer_name"));
        holder.bid_price.setText(map_setdata.get("bid_price"));
        holder.bid_date.setText(map_setdata.get("bid_date"));
        holder.bid_time.setText(map_setdata.get("bid_time"));
        holder.winner.setText(map_setdata.get("winner"));
        holder.status.setText(map_setdata.get("status"));







        holder.MultiVendor_save_bid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                AppConstant.lockButton(view);
                data.put("bid_id",holder.id.getText().toString());

                if(holder.MultiVendor_action.getSelectedItem().equals("Approved") && !holder.MultiVendor_action.getSelectedItem().equals("Select"))
                {
                    updt_url = vendorSessionManagement.getBase_Url()+"vauctionapi/bid/approved";
                    update(updt_url,data,holder.id.getText().toString(),holder.product_id.getText().toString());
                }

                else if(holder.MultiVendor_action.getSelectedItem().equals("Disapproved") && !holder.MultiVendor_action.getSelectedItem().equals("Select"))
                {
                    updt_url = vendorSessionManagement.getBase_Url()+"vauctionapi/bid/disapproved";
                    update(updt_url,data,holder.id.getText().toString(),holder.product_id.getText().toString());
                }
                else
                {
                    Toast.makeText(activity, "Please select proper Action", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return  vi;
    }

    private void update(String updt_url, HashMap data, final String auction_id , final String product_id)
    {
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
                    Intent in = new Intent(activity,Ced_Multivendor_View_Bid.class);
                    in.putExtra("auction_id",auction_id);
                    in.putExtra("product_id",product_id);
                    activity.startActivity(in);
                }

                else
                {
                    Toast.makeText(activity, data.getString("message"), Toast.LENGTH_LONG).show();
                }
            }
        }, activity, "POST", data);
        crr.execute(updt_url);
    }
}
