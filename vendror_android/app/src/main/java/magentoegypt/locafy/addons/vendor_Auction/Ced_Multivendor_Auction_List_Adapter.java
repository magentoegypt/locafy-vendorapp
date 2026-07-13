package magentoegypt.locafy.addons.vendor_Auction;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import  magentoegypt.locafy.R;
import  magentoegypt.locafy_constant.AppConstant;
import  magentoegypt.locafy.base_app.Ced_MultiVendor_FontSetting;
import  magentoegypt.locafy.api_request_response_section.AsyncResponse;
import  magentoegypt.locafy.api_request_response_section.Ced_MultiVendor_ClientRequestResponse;
import  magentoegypt.locafy.vendor_session.Ced_MultiVendor_VendorSessionManagement;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by cedcoss on 22/1/18.
 */

public class Ced_Multivendor_Auction_List_Adapter extends BaseAdapter
{

    private static LayoutInflater inflater=null;
    private Activity activity;
    Ced_MultiVendor_FontSetting fontSetting;
    private ArrayList<HashMap<String, String>> datalist;

    String delete_url;
    HashMap data;
    Ced_MultiVendor_VendorSessionManagement vendorSessionManagement;

    static class ViewHolder
    {
        public TextView id;
        public TextView product_id;
        public TextView product_name;
        public TextView starting_price;
        public TextView max_price;
        public TextView start_datetime;
        public TextView end_datetime;
        public TextView sell_product;
        public TextView status;
        public TextView vendor_auction_status;

        public TextView product_id_Tag;
        public TextView product_name_Tag;
        public TextView starting_price_Tag;
        public TextView max_price_Tag;
        public TextView start_datetime_Tag;
        public TextView end_datetime_Tag;
        public TextView sell_product_Tag;
        public TextView status_Tag;
        public TextView vendor_auction_status_Tag;

        public TextView MultiVendor_delete;
        public TextView MultiVendor_edit;
        public TextView MultiVendor_view_bid;

    }

    public Ced_Multivendor_Auction_List_Adapter(Activity a,ArrayList<HashMap<String, String>> datalist)
    {
        activity=a;
        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        fontSetting=new Ced_MultiVendor_FontSetting();
        this.datalist=datalist;
        delete_url = vendorSessionManagement.getBase_Url()+"vauctionapi/auction/delete";
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
            vi = inflater.inflate(R.layout.ced_multivendor_auction_listi_single_row, null);
            ViewHolder viewHolder = new ViewHolder();

            viewHolder.id = (TextView)vi.findViewById(R.id.MultiVendor_IDtag);
            viewHolder.product_id = (TextView)vi.findViewById(R.id.MultiVendor_product_id);
            viewHolder.product_name = (TextView)vi.findViewById(R.id.MultiVendor_prod_name);
            viewHolder.starting_price = (TextView)vi.findViewById(R.id.MultiVendor_startingprice);
            viewHolder.max_price = (TextView)vi.findViewById(R.id.MultiVendor_maxprice);
            viewHolder.start_datetime = (TextView)vi.findViewById(R.id.MultiVendor_start_date);
            viewHolder.end_datetime = (TextView)vi.findViewById(R.id.MultiVendor_end_date);
            viewHolder.sell_product = (TextView)vi.findViewById(R.id.MultiVendor_sellproduct);
            viewHolder.status = (TextView)vi.findViewById(R.id.MultiVendor_status);
            viewHolder.vendor_auction_status = (TextView)vi.findViewById(R.id.MultiVendor_vendorproductauctionstatus);

            viewHolder.MultiVendor_delete = (TextView)vi.findViewById(R.id.MultiVendor_delete);
            viewHolder.MultiVendor_edit = (TextView)vi.findViewById(R.id.MultiVendor_edit);
            viewHolder.MultiVendor_view_bid = (TextView)vi.findViewById(R.id.MultiVendor_view_bid);

            viewHolder.product_id_Tag = (TextView)vi.findViewById(R.id.MultiVendor_productIDtag);
            viewHolder.product_name_Tag = (TextView)vi.findViewById(R.id.MultiVendor_prod_nametag);
            viewHolder.starting_price_Tag = (TextView)vi.findViewById(R.id.MultiVendor_startingpricetag);
            viewHolder.max_price_Tag = (TextView)vi.findViewById(R.id.MultiVendor_maxpricetag);
            viewHolder.start_datetime_Tag = (TextView)vi.findViewById(R.id.MultiVendor_Startdatetag);
            viewHolder.end_datetime_Tag = (TextView)vi.findViewById(R.id.MultiVendor_end_datetag);
            viewHolder.sell_product_Tag = (TextView)vi.findViewById(R.id.MultiVendor_sellproducttag);
            viewHolder.status_Tag = (TextView)vi.findViewById(R.id.MultiVendor_sratustag);
            viewHolder.vendor_auction_status_Tag = (TextView)vi.findViewById(R.id.MultiVendor_vendorauctionprodstatustag);

            /**************************Regular*****************************/

            fontSetting.setFontforTextviews(viewHolder.product_id,"Roboto-Regular.ttf",activity);
            fontSetting.setFontforTextviews(viewHolder.product_name,"Roboto-Regular.ttf",activity);
            fontSetting.setFontforTextviews(viewHolder.starting_price,"Roboto-Regular.ttf",activity);
            fontSetting.setFontforTextviews(viewHolder.max_price,"Roboto-Regular.ttf",activity);
            fontSetting.setFontforTextviews(viewHolder.start_datetime,"Roboto-Regular.ttf",activity);
            fontSetting.setFontforTextviews(viewHolder.end_datetime,"Roboto-Regular.ttf",activity);
            fontSetting.setFontforTextviews(viewHolder.sell_product,"Roboto-Regular.ttf",activity);
            fontSetting.setFontforTextviews(viewHolder.status,"Roboto-Regular.ttf",activity);
            fontSetting.setFontforTextviews(viewHolder.vendor_auction_status,"Roboto-Regular.ttf",activity);

            /*******************************************************/
            /**************************Medium*****************************/
            fontSetting.setFontforTextviews(viewHolder.product_id_Tag,"Roboto-Medium.ttf",activity);
            fontSetting.setFontforTextviews(viewHolder.product_name_Tag,"Roboto-Medium.ttf",activity);
            fontSetting.setFontforTextviews(viewHolder.starting_price_Tag,"Roboto-Medium.ttf",activity);
            fontSetting.setFontforTextviews(viewHolder.max_price_Tag,"Roboto-Medium.ttf",activity);
            fontSetting.setFontforTextviews(viewHolder.start_datetime_Tag,"Roboto-Medium.ttf",activity);
            fontSetting.setFontforTextviews(viewHolder.end_datetime_Tag,"Roboto-Medium.ttf",activity);
            fontSetting.setFontforTextviews(viewHolder.sell_product_Tag,"Roboto-Medium.ttf",activity);
            fontSetting.setFontforTextviews(viewHolder.status_Tag,"Roboto-Medium.ttf",activity);
            fontSetting.setFontforTextviews(viewHolder.vendor_auction_status_Tag,"Roboto-Medium.ttf",activity);
            /*******************************************************/
            vi.setTag(viewHolder);
        }

        HashMap<String,String> map_setdata = new HashMap<>();
        map_setdata = datalist.get(position);
        final ViewHolder holder = (ViewHolder)vi.getTag();

        holder.id.setText(map_setdata.get("id"));
        holder.product_id.setText(map_setdata.get("product_id"));
        holder.product_name.setText(map_setdata.get("product_name"));
        holder.starting_price.setText(map_setdata.get("starting_price"));
        holder.max_price.setText(map_setdata.get("max_price"));
        holder.start_datetime.setText(map_setdata.get("start_date_time"));
        holder.end_datetime.setText(map_setdata.get("end_date_time"));
        holder.sell_product.setText(map_setdata.get("sellproduct"));
        holder.status.setText(map_setdata.get("status"));
        holder.vendor_auction_status.setText(map_setdata.get("vendor_prod_auction_status"));


        holder.MultiVendor_delete.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                AppConstant.lockButton(view);
                data.put("id",holder.id.getText());

                Log.d("itemdelte",holder.id.getText().toString());

                Ced_MultiVendor_ClientRequestResponse crr = new Ced_MultiVendor_ClientRequestResponse(new AsyncResponse()
                {

                    @Override
                    public void processFinish(Object output) throws JSONException
                    {
                        HashMap<String,String> inner_data ;

                        JSONObject jsonObject=new JSONObject(output.toString());


                        if(jsonObject.getJSONObject("data").getString("success").equals("true"))
                        {
                            Intent in = new Intent(activity,Ced_Multivendor_List_Auction.class);
                            activity.startActivity(in);

                        }

                    }
                },activity,"POST",data);
                crr.execute(delete_url);

            }
        });

        holder.MultiVendor_edit.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                AppConstant.lockButton(view);
                Intent edit = new Intent(activity,Ced_Multivendor_Edit_Auction.class);
                edit.putExtra("id",holder.id.getText().toString());
                edit.putExtra("activity","list_auction");
                activity.startActivity(edit);
            }
        });

        holder.MultiVendor_view_bid.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                AppConstant.lockButton(view);
                Intent viewBid = new Intent(activity,Ced_Multivendor_View_Bid.class);
                viewBid.putExtra("auction_id",holder.id.getText().toString());
                viewBid.putExtra("product_id",holder.product_id.getText().toString());
                activity.startActivity(viewBid);
            }
        });


        return  vi;
    }
}
