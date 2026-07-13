package magentoegypt.locafy.addons.mvp_RequestForQuote;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import magentoegypt.locafy.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by cedcoss on 24/1/18.
 */

class Ced_ViewPO_ListAdapter extends BaseAdapter {
    private static LayoutInflater inflater = null;
    Activity act;
    ArrayList<HashMap<String, String>> data;
    JSONArray status_json_arr;

    public Ced_ViewPO_ListAdapter(Activity ced_multiVendor_vendor_viewPO_list, ArrayList<HashMap<String, String>> prod_attr_info) {
        data = prod_attr_info;
        act = ced_multiVendor_vendor_viewPO_list;
    }

    public Ced_ViewPO_ListAdapter(Activity ced_multiVendor_vendor_viewPO_list, ArrayList<HashMap<String, String>> prod_attr_info, JSONArray status_json_arr) {
        data = prod_attr_info;
        act = ced_multiVendor_vendor_viewPO_list;
        this.status_json_arr = status_json_arr;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View vi = convertView;
        if (convertView == null) {
            inflater = (LayoutInflater) act.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            vi = inflater.inflate(R.layout.ced_multivendor_product_po_list_adapter, null);

        }

        TextView MultiVendor_POIncrementId = vi.findViewById(R.id.MultiVendor_POIncrementId);
        TextView POIncrementId = vi.findViewById(R.id.POIncrementId);
        TextView MultiVendor_CreatedAt = vi.findViewById(R.id.MultiVendor_CreatedAt);
        TextView CreatedAt = vi.findViewById(R.id.CreatedAt);
//        TextView MultiVendor_CustomerId = (TextView) vi.findViewById(R.id.MultiVendor_CustomerId);
//        TextView CustomerId = (TextView) vi.findViewById(R.id.CustomerId);
        TextView MultiVendor_Status = vi.findViewById(R.id.MultiVendor_Status);
        TextView Status = vi.findViewById(R.id.Status);
//        TextView MultiVendor_POPrice = (TextView) vi.findViewById(R.id.MultiVendor_POPrice);
//        TextView POPrice = (TextView) vi.findViewById(R.id.POPrice);
//        TextView MultiVendor_POQty = (TextView) vi.findViewById(R.id.MultiVendor_POQty);
//        TextView POQty = (TextView) vi.findViewById(R.id.POQty);
        TextView MultiVendor_QuoteIncrementId = vi.findViewById(R.id.MultiVendor_QuoteIncrementId);
        TextView QuoteIncrementId = vi.findViewById(R.id.QuoteIncrementId);
        TextView QuoteId = vi.findViewById(R.id.QuoteId);
        TextView MultiVendor_po_id = vi.findViewById(R.id.MultiVendor_po_id);
        HashMap<String, String> attr = data.get(position);
        POIncrementId.setText(attr.get(Ced_MultiVendor_Vendor_ViewPO_List.KEY_po_increment_id));
        CreatedAt.setText(attr.get(Ced_MultiVendor_Vendor_ViewPO_List.KEY_created_at));
//        CustomerId.setText(attr.get(Ced_MultiVendor_Vendor_ViewPO_List.KEY_po_customer_id));
        try {
            for (int i = 0; i < status_json_arr.length(); i++) {
                JSONObject jsonObject = status_json_arr.getJSONObject(i);
                if (jsonObject.getString("value").equals(attr.get(Ced_MultiVendor_Vendor_ViewPO_List.KEY_status))){
                    Status.setText(jsonObject.getString("label"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
//        if (attr.get(Ced_MultiVendor_Vendor_ViewPO_List.KEY_status).equals("0")) {
//            Status.setText("Pending");
//        } else if (attr.get(Ced_MultiVendor_Vendor_ViewPO_List.KEY_status).equals("1")) {
//            Status.setText("Not Yet Ordered");
//        } else if (attr.get(Ced_MultiVendor_Vendor_ViewPO_List.KEY_status).equals("2")) {
//            Status.setText("Declined");
//        } else if (attr.get(Ced_MultiVendor_Vendor_ViewPO_List.KEY_status).equals("3")) {
//            Status.setText("Ordered");
//        }

//        POPrice.setText(attr.get(Ced_MultiVendor_Vendor_ViewPO_List.KEY_po_price));
        QuoteIncrementId.setText(attr.get(Ced_MultiVendor_Vendor_ViewPO_List.KEY_quote_increment_id));
        QuoteId.setText(attr.get("quote_id"));
        MultiVendor_po_id.setText(attr.get(Ced_MultiVendor_Vendor_ViewPO_List.KEY_po_id));
//        POQty.setText(attr.get(Ced_MultiVendor_Vendor_ViewPO_List.KEY_po_qty));
        return vi;
    }
}