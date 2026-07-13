/*
 *
 *   Copyright/* *
 *             * CedCommerce
 *             *
 *             * NOTICE OF LICENSE
 *             *
 *             * This source file is subject to the End User License Agreement (EULA)
 *             * that is bundled with this package in the file LICENSE.txt.
 *             * It is also available through the world-wide-web at this URL:
 *             * http://cedcommerce.com/license-agreement.txt
 *             *
 *             * @category  Ced
 *             * @package   MultiVendor
 *             * @author    CedCommerce Core Team <connect@cedcommerce.com >
 *             * @copyright Copyright CEDCOMMERCE (http://cedcommerce.com/)
 *             * @license   http://cedcommerce.com/license-agreement.txt
 *
 *
 *
 */

package magentoegypt.locafy.manage_products_section;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import magentoegypt.locafy.R;
import magentoegypt.locafy.base_app.Ced_MultiVendor_FontSetting;
import magentoegypt.locafy_constant.Ced_MultiVendor_GlobalVariables;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class Ced_MultiVendor_BundleProductAdapter extends BaseAdapter
{
    private Activity activity;
    private ArrayList<HashMap<String, String>> data;
    private static LayoutInflater inflater=null;
    Ced_MultiVendor_FontSetting fontSetting;

    ArrayList<String> arrayList;
    static class ViewHolder
    {
        public TextView productIDtag;//
        public TextView skutag;//
        public TextView RegularPricetag;
        public TextView ProductNametag;
        public TextView defaultqty;
        public TextView product_id;
        public TextView product_name;
        public TextView sku;
        public TextView RegularPrice;
        public EditText qty;
        public EditText price;
        public CheckBox selectrelated;
        public LinearLayout defaultqtysection;
        public LinearLayout pricesection;
        public  TextView type_spinner_dropdown;
        public  TextView price_type;
        public  TextView index;
        public Spinner usercanchangeqty;
        public Spinner type;



    }
    public Ced_MultiVendor_BundleProductAdapter(Activity a, ArrayList<HashMap<String, String>> d)
    {
        fontSetting=new Ced_MultiVendor_FontSetting();
        activity = a;
        data=d;
        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        arrayList=new ArrayList<String>();

    }

    public int getCount()
    {
        return data.size();
    }

    public Object getItem(int position)
    {
        return position;
    }

    public long getItemId(int position)
    {
        return position;
    }
    @SuppressLint("NewApi")
    public View getView(int position, View convertView, ViewGroup parent)
    {
        View vi=convertView;
        if(vi==null)
        {
            vi = inflater.inflate(R.layout.ced_multivendor_bundleproductlist, null);
            ViewHolder viewHolder = new ViewHolder();
            viewHolder.productIDtag = (TextView)vi.findViewById(R.id.MultiVendor_productIDtag);
            viewHolder.defaultqty = (TextView)vi.findViewById(R.id.MultiVendor_defaultqty);
            viewHolder.skutag = (TextView)vi.findViewById(R.id.MultiVendor_skutag);
            viewHolder.qty = (EditText)vi.findViewById(R.id.MultiVendor_qty);
            viewHolder.price = (EditText)vi.findViewById(R.id.MultiVendor_price);
            viewHolder.RegularPricetag = (TextView)vi.findViewById(R.id.MultiVendor_RegularPricetag);
            viewHolder.ProductNametag = (TextView)vi.findViewById(R.id.MultiVendor_ProductNametag);
            viewHolder.product_id = (TextView)vi.findViewById(R.id.MultiVendor_product_id);
            viewHolder.product_name = (TextView)vi.findViewById(R.id.MultiVendor_product_name);
            viewHolder.RegularPrice = (TextView)vi.findViewById(R.id.MultiVendor_RegularPrice);
            viewHolder.type_spinner_dropdown = (TextView)vi.findViewById(R.id.MultiVendor_type_spinner_dropdown);
            viewHolder.price_type = (TextView)vi.findViewById(R.id.MultiVendor_price_type);
            viewHolder.sku = (TextView)vi.findViewById(R.id.MultiVendor_sku);
            viewHolder.index = (TextView)vi.findViewById(R.id.MultiVendor_index);
            viewHolder.selectrelated = (CheckBox)vi.findViewById(R.id.MultiVendor_selectrelated);
            viewHolder.defaultqtysection = (LinearLayout)vi.findViewById(R.id.MultiVendor_defaultqtysection);
            viewHolder.pricesection = (LinearLayout)vi.findViewById(R.id.MultiVendor_pricesection);
            viewHolder.usercanchangeqty = (Spinner)vi.findViewById(R.id.MultiVendor_usercanchangeqty);
            viewHolder.type = (Spinner)vi.findViewById(R.id.MultiVendor_type);
            /**************************Regular*****************************/
            fontSetting.setFontforTextviews(viewHolder.product_id,"Roboto-Regular.ttf",activity);
            fontSetting.setFontforTextviews(viewHolder.qty,"Roboto-Regular.ttf",activity);
            fontSetting.setFontforTextviews(viewHolder.product_name,"Roboto-Regular.ttf",activity);
            fontSetting.setFontforTextviews(viewHolder.RegularPrice,"Roboto-Regular.ttf",activity);
            fontSetting.setFontforTextviews(viewHolder.sku,"Roboto-Regular.ttf",activity);
            /*******************************************************/
            /**************************Medium*****************************/
            fontSetting.setFontforTextviews(viewHolder.productIDtag,"Roboto-Medium.ttf",activity);
            fontSetting.setFontforTextviews(viewHolder.defaultqty,"Roboto-Medium.ttf",activity);
            fontSetting.setFontforTextviews(viewHolder.skutag,"Roboto-Medium.ttf",activity);
            fontSetting.setFontforTextviews(viewHolder.RegularPricetag,"Roboto-Medium.ttf",activity);
            fontSetting.setFontforTextviews(viewHolder.ProductNametag,"Roboto-Medium.ttf",activity);


            /*******************************************************/
            vi.setTag(viewHolder);
        }
        HashMap<String, String> song =new HashMap<String, String>();
        song = data.get(position);
        final ViewHolder holder = (ViewHolder) vi.getTag();
        holder.product_id.setText(song.get("product_id"));
        holder.product_name.setText(song.get("product_name"));
        holder.RegularPrice.setText(song.get("regular_price"));
        holder.sku.setText(song.get("sku"));
        holder.price_type.setText(song.get("bundle_price_type"));
        holder.type_spinner_dropdown.setText(song.get("type_spinner"));
        holder.index.setText(song.get("index"));
        if(song.get("bundle_price_type").equals("1"))
        {
            holder.pricesection.setVisibility(View.VISIBLE);
        }
        else
        {
            holder.pricesection.setVisibility(View.GONE);
        }
        if(song.get("type_spinner").equals("Drop-Down")||song.get("type_spinner").equals("Radio Buttons"))
        {
            holder.defaultqtysection.setVisibility(View.VISIBLE);
        }
        else
        {
            holder.defaultqtysection.setVisibility(View.GONE);
        }
        try
        {
            if(Ced_MultiVendor_GlobalVariables.bundle_selections_data.length()>0)
            {
                if(Ced_MultiVendor_GlobalVariables.bundle_selections_data.has(holder.index.getText().toString()))
                {
                    try
                    {
                        JSONArray jsonArray= Ced_MultiVendor_GlobalVariables.bundle_selections_data.getJSONArray(holder.index.getText().toString());
                        for(int i=0;i<jsonArray.length();i++)
                        {
                            JSONObject object=jsonArray.getJSONObject(i);
                            if(object.getString("product_id").equals(holder.product_id.getText().toString()))
                            {
                                holder.selectrelated.setChecked(true);
                            }
                            else
                            {
                                holder.selectrelated.setChecked(true);
                            }
                        }
                    }
                    catch (JSONException e)
                    {
                        e.printStackTrace();
                    }
                }
                else
                {
                    holder.selectrelated.setChecked(false);
                }
            }
            else
            {
                holder.selectrelated.setChecked(false);
            }
            holder.selectrelated.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
            {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
                {
                    if(isChecked)
                    {
                        if(Ced_MultiVendor_GlobalVariables.bundle_selections_data.length()>0)
                        {
                            if(Ced_MultiVendor_GlobalVariables.bundle_selections_data.has(holder.index.getText().toString()))
                            {
                                try
                                {
                                    JSONArray jsonArray= Ced_MultiVendor_GlobalVariables.bundle_selections_data.getJSONArray(holder.index.getText().toString());
                                    if(jsonArray.length()>0)
                                    {
                                        for(int i=0;i<jsonArray.length();i++)
                                        {
                                            JSONObject jsonObject=jsonArray.getJSONObject(i);
                                            if(jsonObject.getString("product_id").equals(holder.product_id.getText().toString()))
                                            {

                                            }
                                            else
                                            {
                                                JSONObject object = new JSONObject();
                                                try
                                                {
                                                    object.put("selection_id", "");
                                                    object.put("option_id", "");
                                                    object.put("product_id", holder.product_id.getText().toString());
                                                    object.put("delete", "");
                                                    if (holder.price_type.getText().equals("1"))
                                                    {
                                                        object.put("selection_price_value", holder.price.getText().toString());
                                                        object.put("selection_price_type", holder.type.getSelectedItem().toString());
                                                    }
                                                    else
                                                    {
                                                        object.put("selection_price_value", "");
                                                        object.put("selection_price_type", "");
                                                    }
                                                    object.put("selection_qty", holder.qty.getText().toString());
                                                   /* if (holder.type_spinner_dropdown.getText().toString().equals("Drop-Down") || holder.type_spinner_dropdown.getText().toString().equals("Radio Buttons"))
                                                    {*/
                                                        if (holder.usercanchangeqty.getSelectedItem().equals("Yes"))
                                                        {
                                                            object.put("selection_can_change_qty", 1);
                                                        }
                                                        else
                                                        {
                                                            object.put("selection_can_change_qty", 0);
                                                        }

                                                   /* }
                                                    else
                                                    {
                                                        object.put("selection_can_change_qty", "");
                                                    }*/

                                                    jsonArray.put(object);
                                                    Ced_MultiVendor_GlobalVariables.bundle_selections_data.put(holder.index.getText().toString(), jsonArray);
                                                }
                                                catch (JSONException e)
                                                {
                                                    e.printStackTrace();
                                                }
                                            }
                                        }
                                    }
                                    else
                                    {
                                        JSONObject object = new JSONObject();
                                        try
                                        {
                                            object.put("selection_id", "");
                                            object.put("option_id", "");
                                            object.put("product_id", holder.product_id.getText().toString());
                                            object.put("delete", "");
                                            if (holder.price_type.getText().equals("1"))
                                            {
                                                object.put("selection_price_value", holder.price.getText().toString());
                                                object.put("selection_price_type", holder.type.getSelectedItem().toString());
                                            }
                                            else
                                            {
                                                object.put("selection_price_value", "");
                                                object.put("selection_price_type", "");
                                            }
                                            object.put("selection_qty", holder.qty.getText().toString());
                                           /* if (holder.type_spinner_dropdown.getText().toString().equals("Drop-Down") || holder.type_spinner_dropdown.getText().toString().equals("Radio Buttons"))
                                            {*/
                                                if (holder.usercanchangeqty.getSelectedItem().equals("Yes"))
                                                {
                                                    object.put("selection_can_change_qty", 1);
                                                }
                                                else
                                                {
                                                    object.put("selection_can_change_qty", 0);
                                                }

                                          /*  }
                                            else
                                            {
                                                object.put("selection_can_change_qty", "");
                                            }*/
                                            JSONArray array=new JSONArray();
                                            array.put(object);
                                            Ced_MultiVendor_GlobalVariables.bundle_selections_data.put(holder.index.getText().toString(), array);
                                        }
                                        catch (JSONException e)
                                        {
                                            e.printStackTrace();
                                        }
                                    }

                                }
                                catch (JSONException e)
                                {
                                    e.printStackTrace();
                                }
                            }
                            else
                            {
                                JSONObject object = new JSONObject();
                                try
                                {
                                    object.put("selection_id", "");
                                    object.put("option_id", "");
                                    object.put("product_id", holder.product_id.getText().toString());
                                    object.put("delete", "");
                                    if (holder.price_type.getText().equals("1"))
                                    {
                                        object.put("selection_price_value", holder.price.getText().toString());
                                        object.put("selection_price_type", holder.type.getSelectedItem().toString());
                                    }
                                    else
                                    {
                                        object.put("selection_price_value", "");
                                        object.put("selection_price_type", "");
                                    }
                                    object.put("selection_qty", holder.qty.getText().toString());
                                   /* if (holder.type_spinner_dropdown.getText().toString().equals("Drop-Down") || holder.type_spinner_dropdown.getText().toString().equals("Radio Buttons"))
                                    {*/
                                        if (holder.usercanchangeqty.getSelectedItem().equals("Yes"))
                                        {
                                            object.put("selection_can_change_qty", 1);
                                        }
                                        else
                                        {
                                            object.put("selection_can_change_qty", 0);
                                        }

                                  /*  }
                                    else
                                    {
                                        object.put("selection_can_change_qty", "");
                                    }*/
                                    JSONArray array=new JSONArray();
                                    array.put(object);
                                    Ced_MultiVendor_GlobalVariables.bundle_selections_data.put(holder.index.getText().toString(), array);
                                }
                                catch (JSONException e)
                                {
                                    e.printStackTrace();
                                }
                            }
                        }
                        else
                        {
                            JSONObject object = new JSONObject();
                            try
                            {
                                object.put("selection_id", "");
                                object.put("option_id", "");
                                object.put("product_id", holder.product_id.getText().toString());
                                object.put("delete", "");
                                if (holder.price_type.getText().equals("1"))
                                {
                                    object.put("selection_price_value", holder.price.getText().toString());
                                    object.put("selection_price_type", holder.type.getSelectedItem().toString());
                                }
                                else
                                {
                                    object.put("selection_price_value", "");
                                    object.put("selection_price_type", "");
                                }
                                object.put("selection_qty", holder.qty.getText().toString());
                                /*if (holder.type_spinner_dropdown.getText().toString().equals("Drop-Down") || holder.type_spinner_dropdown.getText().toString().equals("Radio Buttons"))
                                {*/
                                    if (holder.usercanchangeqty.getSelectedItem().equals("Yes"))
                                    {
                                        object.put("selection_can_change_qty", 1);
                                    }
                                    else
                                    {
                                        object.put("selection_can_change_qty", 0);
                                    }

                              /*  }
                                else
                                {
                                    object.put("selection_can_change_qty", "");
                                }*/
                                JSONArray array=new JSONArray();
                                array.put(object);
                                Ced_MultiVendor_GlobalVariables.bundle_selections_data.put(holder.index.getText().toString(), array);
                            }
                            catch (JSONException e)
                            {
                                e.printStackTrace();
                            }
                        }

                    }
                    else
                    {
                        try
                        {
                            JSONArray array=new JSONArray();
                            JSONArray jsonArray= Ced_MultiVendor_GlobalVariables.bundle_selections_data.getJSONArray(holder.index.getText().toString());
                            for(int i=0;i<jsonArray.length();i++)
                            {
                                JSONObject object=jsonArray.getJSONObject(i);
                                if(object.getString("product_id").equals(holder.product_id.getText().toString()))
                                {

                                }
                                else
                                {
                                    array.put(object);
                                }

                            }
                            Ced_MultiVendor_GlobalVariables.bundle_selections_data.put(holder.index.getText().toString(), array);
                        }
                        catch (JSONException e)
                        {
                            e.printStackTrace();
                        }
                    }
                    if (activity.getResources().getString(R.string.Enable_Log).equals("yes"))

                    {
                        Log.i("bundle_data", "" + Ced_MultiVendor_GlobalVariables.bundle_selections_data);
                    }
                }
            });
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return vi;
    }
    @Override
    public boolean isEnabled(int position)
    {
        return true;
    }


}