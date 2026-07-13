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

package magentoegypt.locafy.addons.vendor_select_n_sell;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import magentoegypt.locafy.base_app.Ced_MultiVendor_FontSetting;
import magentoegypt.locafy.base_app.Ced_MultiVendor_VendorFunctionalityList;
import magentoegypt.locafy.vendor_session.Ced_MultiVendor_VendorSessionManagement;
import magentoegypt.locafy.R;
import com.squareup.picasso.Picasso;
import magentoegypt.locafy_constant.AppConstant;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by cedcoss on 2/2/17.
 */
public class Ced_MultiVendor_AddProductListSelectNSellAdapter extends BaseAdapter {
    private Activity activity;
    public ArrayList<HashMap<String, String>> data;
    private static LayoutInflater inflater=null;
    String Jstring;
    Ced_MultiVendor_VendorFunctionalityList functionalityList;
    Ced_MultiVendor_VendorSessionManagement vendorSessionManagement;
    HashMap<String,String> dataforproducts;
    String Currenturl;
    String editCurrenturl;
    Ced_MultiVendor_FontSetting fontSetting;
    public static float  density;
    public Ced_MultiVendor_AddProductListSelectNSellAdapter(Activity a, ArrayList<HashMap<String, String>> d)
    {
        activity=a;
        data=d;
        fontSetting=new Ced_MultiVendor_FontSetting();
        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        functionalityList=new Ced_MultiVendor_VendorFunctionalityList(activity);
        vendorSessionManagement=new Ced_MultiVendor_VendorSessionManagement(activity);
        dataforproducts=new HashMap<String,String>();
        density= activity.getResources().getDisplayMetrics().densityDpi;
    }
    static class ViewHolder {
        public TextView productIDtag;

        public TextView skutag;
        public TextView website;
        public TextView RegularPricetag;
        public TextView ProductNametag;

        public TextView product_id;

        public TextView product_name;
        public TextView sku;
        public TextView RegularPrice;


        public TextView edit;
        public TextView picurl;
        public ImageView prductimage;
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
        View vi=convertView;
        if(vi==null)
        {
            vi = inflater.inflate(R.layout.ced_multivendor_addproductlist, null);
            ViewHolder viewHolder = new ViewHolder();
            viewHolder.productIDtag = (TextView)vi.findViewById(R.id.MultiVendor_productIDtag);

            viewHolder.skutag = (TextView)vi.findViewById(R.id.MultiVendor_skutag);

            viewHolder.RegularPricetag = (TextView)vi.findViewById(R.id.MultiVendor_RegularPricetag);
            viewHolder.ProductNametag = (TextView)vi.findViewById(R.id.MultiVendor_ProductNametag);

            viewHolder.product_id = (TextView)vi.findViewById(R.id.MultiVendor_product_id);
            viewHolder.edit = (TextView)vi.findViewById(R.id.MultiVendor_edit);


            viewHolder.product_name = (TextView)vi.findViewById(R.id.MultiVendor_product_name);
            viewHolder.RegularPrice = (TextView)vi.findViewById(R.id.MultiVendor_RegularPrice);

            viewHolder.sku = (TextView)vi.findViewById(R.id.MultiVendor_sku);

            viewHolder.picurl = (TextView)vi.findViewById(R.id.MultiVendor_picurl);
            viewHolder.prductimage = (ImageView)vi.findViewById(R.id.MultiVendor_prductimage);
            viewHolder.website = (TextView)vi.findViewById(R.id.MultiVendor_website);
            /**************************Regular*****************************/
            fontSetting.setFontforTextviews(viewHolder.product_id,"Roboto-Regular.ttf",activity);
            fontSetting.setFontforTextviews(viewHolder.product_name,"Roboto-Regular.ttf",activity);
            fontSetting.setFontforTextviews(viewHolder.RegularPrice,"Roboto-Regular.ttf",activity);

            fontSetting.setFontforTextviews(viewHolder.sku,"Roboto-Regular.ttf",activity);

            /*******************************************************/
            /**************************Medium*****************************/

            fontSetting.setFontforTextviews(viewHolder.edit,"Roboto-Medium.ttf",activity);
            fontSetting.setFontforTextviews(viewHolder.productIDtag,"Roboto-Medium.ttf",activity);

            fontSetting.setFontforTextviews(viewHolder.skutag,"Roboto-Medium.ttf",activity);

            fontSetting.setFontforTextviews(viewHolder.RegularPricetag,"Roboto-Medium.ttf",activity);

            fontSetting.setFontforTextviews(viewHolder.ProductNametag,"Roboto-Medium.ttf",activity);

            fontSetting.setFontforTextviews(viewHolder.website,"Roboto-Medium.ttf",activity);
            /*******************************************************/
            vi.setTag(viewHolder);
        }
        HashMap<String, String> song =new HashMap<String, String>();
        song = data.get(position);
        final ViewHolder holder = (ViewHolder) vi.getTag();
        holder.product_id.setText(song.get("product_id"));

        holder.product_name.setText(song.get("product_name"));
        holder.RegularPrice.setText(song.get("regular_price"));
        holder.website.setText(song.get("website"));

        holder.sku.setText(song.get("sku"));
        holder.picurl.setText(song.get("product_image"));
        Picasso.with(activity)
                .load(song.get("product_image"))
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.placeholder)
                .into(holder.prductimage);
        holder.prductimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppConstant.lockButton(v);
                showImage(holder.prductimage, holder.picurl.getText().toString());
            }
        });
        holder.edit.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                AppConstant.lockButton(v);
                final String product_id[] = holder.product_id.getText().toString().split("#");

                Intent add_product_list = new Intent(activity,Ced_MultiVendor_Sell_This_Product.class);
                add_product_list.putExtra("product_id",product_id[1]);
                add_product_list.putExtra("product_name", holder.product_name.getText().toString());
                activity.startActivity(add_product_list);

            }
        });
        return vi;
    }
    @Override
    public boolean isEnabled(int position)
    {
        return true;
    }

    public void showImage(ImageView image, String s) {
        Dialog builder = new Dialog(activity);
        builder.requestWindowFeature(Window.FEATURE_NO_TITLE);
        builder.getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.TRANSPARENT));
        builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                //nothing;
            }
        });

        ImageView imageView = new ImageView(activity);
        Picasso.with(activity)
                .load(s)
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.placeholder)
                .into(imageView);
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);

        if (density <= 160)
        {
            builder.addContentView(imageView, new RelativeLayout.LayoutParams(
                    200,
                    400));
        }
        if (density>160&&density<= 240)
        {
            builder.addContentView(imageView, new RelativeLayout.LayoutParams(
                    300,
                    500));
        }
        if (density>240&&density <= 360)
        {
            builder.addContentView(imageView, new RelativeLayout.LayoutParams(
                    400,
                    600));
        }
        if (density>360&&density <= 480)
        {
            builder.addContentView(imageView, new RelativeLayout.LayoutParams(
                    600,
                    800));
        }
        if (density > 480)
        {
            builder.addContentView(imageView, new RelativeLayout.LayoutParams(
                    700,
                    900));
        }

        builder.show();

    }
}
