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
import android.widget.TextView;
import magentoegypt.locafy.R;
import magentoegypt.locafy.base_app.Ced_MultiVendor_FontSetting;
import magentoegypt.locafy_constant.Ced_MultiVendor_GlobalVariables;

import java.util.ArrayList;
import java.util.HashMap;
public class Ced_MultiVendor_RelatedProductAdapter extends BaseAdapter
{
    private Activity activity;
    private ArrayList<HashMap<String, String>> data;
    private static LayoutInflater inflater=null;
    Ced_MultiVendor_FontSetting fontSetting;
    public static class ViewHolder
    {
        public TextView productIDtag;
        public TextView skutag;
        public TextView Productypetag;
        public TextView RegularPricetag;
        public TextView ProductNametag;
        public TextView statustag;
        public TextView product_id;
        public TextView Productype;
        public TextView product_name;
        public TextView sku;
        public TextView RegularPrice;
        public TextView Status;
        public TextView attribuetsetname;
        public TextView attributesetnametag;
        public TextView selected;
        public CheckBox selectrelated;
    }
    public Ced_MultiVendor_RelatedProductAdapter(Activity a, ArrayList<HashMap<String, String>> d)
    {
        fontSetting=new Ced_MultiVendor_FontSetting();
        activity = a;
        data=d;
        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);


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
            vi = inflater.inflate(R.layout.ced_multivendor_relatedproductlist, null);
            ViewHolder viewHolder = new ViewHolder();
            viewHolder.productIDtag = (TextView)vi.findViewById(R.id.MultiVendor_productIDtag);
            viewHolder.attribuetsetname = (TextView)vi.findViewById(R.id.MultiVendor_attribuetsetname);
            viewHolder.skutag = (TextView)vi.findViewById(R.id.MultiVendor_skutag);
            viewHolder.Productypetag = (TextView)vi.findViewById(R.id.MultiVendor_Productypetag);
            viewHolder.RegularPricetag = (TextView)vi.findViewById(R.id.MultiVendor_RegularPricetag);
            viewHolder.ProductNametag = (TextView)vi.findViewById(R.id.MultiVendor_ProductNametag);
            viewHolder.statustag = (TextView)vi.findViewById(R.id.MultiVendor_statustag);
            viewHolder.product_id = (TextView)vi.findViewById(R.id.MultiVendor_product_id);
            viewHolder.attributesetnametag = (TextView)vi.findViewById(R.id.MultiVendor_attributesetnametag);
            viewHolder.Productype = (TextView)vi.findViewById(R.id.MultiVendor_Productype);
            viewHolder.product_name = (TextView)vi.findViewById(R.id.MultiVendor_product_name);
            viewHolder.RegularPrice = (TextView)vi.findViewById(R.id.MultiVendor_RegularPrice);
            viewHolder.sku = (TextView)vi.findViewById(R.id.MultiVendor_sku);
            viewHolder.Status = (TextView)vi.findViewById(R.id.MultiVendor_Status);
            viewHolder.selected = (TextView)vi.findViewById(R.id.MultiVendor_selected);
            viewHolder.selectrelated = (CheckBox)vi.findViewById(R.id.MultiVendor_selectrelated);

            /**************************Regular*****************************/
            fontSetting.setFontforTextviews(viewHolder.product_id,"Roboto-Regular.ttf",activity);
            fontSetting.setFontforTextviews(viewHolder.Productype,"Roboto-Regular.ttf",activity);
            fontSetting.setFontforTextviews(viewHolder.product_name,"Roboto-Regular.ttf",activity);
            fontSetting.setFontforTextviews(viewHolder.RegularPrice,"Roboto-Regular.ttf",activity);
            fontSetting.setFontforTextviews(viewHolder.attribuetsetname,"Roboto-Regular.ttf",activity);
            fontSetting.setFontforTextviews(viewHolder.sku,"Roboto-Regular.ttf",activity);
            fontSetting.setFontforTextviews(viewHolder.Status,"Roboto-Regular.ttf",activity);
            /*******************************************************/
            /**************************Medium*****************************/
            fontSetting.setFontforTextviews(viewHolder.productIDtag,"Roboto-Medium.ttf",activity);
            fontSetting.setFontforTextviews(viewHolder.attributesetnametag,"Roboto-Medium.ttf",activity);
            fontSetting.setFontforTextviews(viewHolder.skutag,"Roboto-Medium.ttf",activity);
            fontSetting.setFontforTextviews(viewHolder.Productypetag,"Roboto-Medium.ttf",activity);
            fontSetting.setFontforTextviews(viewHolder.RegularPricetag,"Roboto-Medium.ttf",activity);
            fontSetting.setFontforTextviews(viewHolder.statustag,"Roboto-Medium.ttf",activity);
            fontSetting.setFontforTextviews(viewHolder.ProductNametag,"Roboto-Medium.ttf",activity);


            /*******************************************************/
            vi.setTag(viewHolder);
        }
        HashMap<String, String> song =new HashMap<String, String>();
        song = data.get(position);
        final ViewHolder holder = (ViewHolder) vi.getTag();
        holder.product_id.setText(song.get("product_id"));
        holder.Productype.setText(song.get("type"));
        holder.product_name.setText(song.get("product_name"));
        holder.RegularPrice.setText(song.get("regular_price"));
        holder.Status.setText(song.get("status"));
        holder.attribuetsetname.setText(song.get("set_name"));
        holder.sku.setText(song.get("sku"));
        holder.selected.setText(song.get("selected"));
        if(holder.selected.getText().equals("true"))
        {
            holder.selectrelated.setChecked(true);
        }
        else
        {
            holder.selectrelated.setChecked(false);
        }
        if(Ced_MultiVendor_GlobalVariables.relatedproductids.size()>0)
        {
            if(Ced_MultiVendor_GlobalVariables.relatedproductids.contains(holder.product_id.getText()))
            {
                holder.selectrelated.setChecked(true);
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
                    if(!(Ced_MultiVendor_GlobalVariables.relatedproductids.contains(holder.product_id.getText().toString())))
                    {
                        Ced_MultiVendor_GlobalVariables.relatedproductids.add(holder.product_id.getText().toString());
                    }

                }
                else
                {
                    if((Ced_MultiVendor_GlobalVariables.relatedproductids.contains(holder.product_id.getText().toString())))
                    {
                        Ced_MultiVendor_GlobalVariables.relatedproductids.remove(holder.product_id.getText().toString());
                    }
                }
                if (activity.getResources().getString(R.string.Enable_Log).equals("yes"))
                {
                    Log.i("relatedproductids",""+ Ced_MultiVendor_GlobalVariables.relatedproductids);
                }

            }
        });
        return vi;
    }
    @Override
    public boolean isEnabled(int position)
    {
        return true;
    }


}