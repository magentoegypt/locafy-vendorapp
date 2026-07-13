package magentoegypt.locafy.addons.vendor_Auction;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import  magentoegypt.locafy.base_app.Ced_MultiVendor_FontSetting;
import  magentoegypt.locafy.vendor_session.Ced_MultiVendor_VendorSessionManagement;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by cedcoss on 23/1/18.
 */

public class Ced_Mutlivendor_Add_Auction_Adapter extends BaseAdapter
{

    private static LayoutInflater inflater=null;
    private Activity activity;
    Ced_MultiVendor_FontSetting fontSetting;

    private ArrayList<HashMap<String, String>> datalist;

    String delete_url;
    HashMap data;
    Ced_MultiVendor_VendorSessionManagement vendorSessionManagement;


    public Ced_Mutlivendor_Add_Auction_Adapter(Activity a, ArrayList<HashMap<String, String>> datalist)
    {
        activity =a;
        this.datalist = datalist;

    }

    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        return null;
    }
}
