package magentoegypt.locafy.addons.vendor_Auction;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import  magentoegypt.locafy.R;
import  magentoegypt.locafy_constant.AppConstant;
import  magentoegypt.locafy.base_app.Ced_MultiVendor_FontSetting;
import  magentoegypt.locafy.vendor_session.Ced_MultiVendor_VendorSessionManagement;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by cedcoss on 25/1/18.
 */

public class Ced_Multivendor_Add_Auction_Adapter extends BaseAdapter
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
        public TextView product_type;
        public TextView price;
        public TextView qty;


        public TextView idtag;
        public TextView product_idtag;
        public TextView product_nametag;
        public TextView product_typetag;
        public TextView pricetag;
        public TextView qtytag;


        public TextView Add_Auction;

    }
    public Ced_Multivendor_Add_Auction_Adapter(Activity a,ArrayList<HashMap<String, String>> datalist)
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
            vi = inflater.inflate(R.layout.ced_multivendor_add_auction_single_row, null);
            ViewHolder viewHolder = new ViewHolder();

            viewHolder.id = (TextView)vi.findViewById(R.id.MultiVendor_IDtag);
            viewHolder.product_id = (TextView)vi.findViewById(R.id.MultiVendor_product_id);
            viewHolder.product_name = (TextView)vi.findViewById(R.id.MultiVendor_prod_name);
            viewHolder.price = (TextView)vi.findViewById(R.id.MultiVendor_price);
            viewHolder.product_type = (TextView)vi.findViewById(R.id.MultiVendor_product_type);
            viewHolder.qty = (TextView)vi.findViewById(R.id.MultiVendor_qty);

            viewHolder.Add_Auction = (TextView)vi.findViewById(R.id.MultiVendor_add_auction) ;

            viewHolder.product_idtag = (TextView)vi.findViewById(R.id.MultiVendor_productIDtag);
            viewHolder.product_nametag = (TextView)vi.findViewById(R.id.MultiVendor_prod_nametag);
            viewHolder.pricetag = (TextView)vi.findViewById(R.id.MultiVendor_pricetag);
            viewHolder.product_typetag = (TextView)vi.findViewById(R.id.MultiVendor_producttypetag);
            viewHolder.qtytag = (TextView)vi.findViewById(R.id.MultiVendor_qtytag);

            /**************************Regular*****************************/

            fontSetting.setFontforTextviews(viewHolder.product_id,"Roboto-Regular.ttf",activity);
            fontSetting.setFontforTextviews(viewHolder.product_name,"Roboto-Regular.ttf",activity);
            fontSetting.setFontforTextviews(viewHolder.price,"Roboto-Regular.ttf",activity);
            fontSetting.setFontforTextviews(viewHolder.product_type,"Roboto-Regular.ttf",activity);
            fontSetting.setFontforTextviews(viewHolder.qty,"Roboto-Regular.ttf",activity);

            /*******************************************************/
            /**************************Medium*****************************/
            fontSetting.setFontforTextviews(viewHolder.product_idtag,"Roboto-Medium.ttf",activity);
            fontSetting.setFontforTextviews(viewHolder.product_nametag,"Roboto-Medium.ttf",activity);
            fontSetting.setFontforTextviews(viewHolder.pricetag,"Roboto-Medium.ttf",activity);
            fontSetting.setFontforTextviews(viewHolder.product_typetag,"Roboto-Medium.ttf",activity);
            fontSetting.setFontforTextviews(viewHolder.qtytag,"Roboto-Medium.ttf",activity);

            /*******************************************************/
            vi.setTag(viewHolder);
        }

        HashMap<String,String> map_setdata = new HashMap<>();
        map_setdata = datalist.get(position);
        final ViewHolder holder = (ViewHolder)vi.getTag();

        holder.id.setText(map_setdata.get("id"));
        holder.product_id.setText(map_setdata.get("product_id"));
        holder.product_name.setText(map_setdata.get("product_name"));
        holder.price.setText(map_setdata.get("price"));
        holder.product_type.setText(map_setdata.get("product_type"));
        holder.qty.setText(map_setdata.get("quantity"));

        holder.Add_Auction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                AppConstant.lockButton(view);
                Intent edit = new Intent(activity,Ced_Multivendor_Edit_Auction.class);
                edit.putExtra("product_id",holder.product_id.getText().toString());
                edit.putExtra("product_name",holder.product_name.getText().toString());
                edit.putExtra("id",holder.id.getText().toString());
                edit.putExtra("activity","add_auction");
                activity.startActivity(edit);
            }
        });

        return  vi;
    }

}
