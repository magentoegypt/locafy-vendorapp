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
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import magentoegypt.locafy.R;
import magentoegypt.locafy_constant.AppConstant;
import magentoegypt.locafy.base_app.Ced_MultiVendor_FontSetting;
import magentoegypt.locafy.base_app.Ced_MultiVendor_VendorFunctionalityList;
import magentoegypt.locafy.vendor_session.Ced_MultiVendor_VendorSessionManagement;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

public class New_Ced_MultiVendor_AddProductListSelectNSellAdapter extends RecyclerView.Adapter<New_Ced_MultiVendor_AddProductListSelectNSellAdapter.ViewHolder> {

    private Activity activity;
    public ArrayList<HashMap<String, String>> data;
    private static LayoutInflater inflater = null;
    String Jstring;
    Ced_MultiVendor_VendorFunctionalityList functionalityList;
    Ced_MultiVendor_VendorSessionManagement vendorSessionManagement;
    HashMap<String, String> dataforproducts;
    String Currenturl;
    String editCurrenturl;
    Ced_MultiVendor_FontSetting fontSetting;
    public static float density;
    private View view;

    public New_Ced_MultiVendor_AddProductListSelectNSellAdapter(Activity a, ArrayList<HashMap<String, String>> d) {
        activity = a;
        data = d;
        fontSetting = new Ced_MultiVendor_FontSetting();
        inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        functionalityList = new Ced_MultiVendor_VendorFunctionalityList(activity);
        vendorSessionManagement = new Ced_MultiVendor_VendorSessionManagement(activity);
        dataforproducts = new HashMap<String, String>();
        density = activity.getResources().getDisplayMetrics().densityDpi;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.ced_multivendor_addproductlist, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        HashMap<String, String> map_data = data.get(position);
        holder.product_id.setText(map_data.get("product_id"));

        holder.product_name.setText(map_data.get("product_name"));
        holder.RegularPrice.setText(map_data.get("regular_price"));
        holder.website.setText(map_data.get("website"));

        holder.sku.setText(map_data.get("sku"));
        holder.picurl.setText(map_data.get("product_image"));
        Picasso.with(activity)
                .load(map_data.get("product_image"))
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
        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppConstant.lockButton(v);
                final String product_id[] = holder.product_id.getText().toString().split("#");
                Intent add_product_list = new Intent(activity, Ced_MultiVendor_Sell_This_Product.class);
                add_product_list.putExtra("product_id", product_id[1]);
                add_product_list.putExtra("product_name", holder.product_name.getText().toString());
                activity.startActivity(add_product_list);

            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size() > 0 ? data.size() : 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
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

        public ViewHolder(@NonNull View vi) {
            super(vi);
            productIDtag = (TextView) vi.findViewById(R.id.MultiVendor_productIDtag);

            skutag = (TextView) vi.findViewById(R.id.MultiVendor_skutag);

            RegularPricetag = (TextView) vi.findViewById(R.id.MultiVendor_RegularPricetag);
            ProductNametag = (TextView) vi.findViewById(R.id.MultiVendor_ProductNametag);

            product_id = (TextView) vi.findViewById(R.id.MultiVendor_product_id);
            edit = (TextView) vi.findViewById(R.id.MultiVendor_edit);


            product_name = (TextView) vi.findViewById(R.id.MultiVendor_product_name);
            RegularPrice = (TextView) vi.findViewById(R.id.MultiVendor_RegularPrice);

            sku = (TextView) vi.findViewById(R.id.MultiVendor_sku);

            picurl = (TextView) vi.findViewById(R.id.MultiVendor_picurl);
            prductimage = (ImageView) vi.findViewById(R.id.MultiVendor_prductimage);
            website = (TextView) vi.findViewById(R.id.MultiVendor_website);

            /**************************Regular*****************************/
            fontSetting.setFontforTextviews(product_id, "Roboto-Regular.ttf", activity);
            fontSetting.setFontforTextviews(product_name, "Roboto-Regular.ttf", activity);
            fontSetting.setFontforTextviews(RegularPrice, "Roboto-Regular.ttf", activity);

            fontSetting.setFontforTextviews(sku, "Roboto-Regular.ttf", activity);

            /*******************************************************/
            /**************************Medium*****************************/

            fontSetting.setFontforTextviews(edit, "Roboto-Medium.ttf", activity);
            fontSetting.setFontforTextviews(productIDtag, "Roboto-Medium.ttf", activity);

            fontSetting.setFontforTextviews(skutag, "Roboto-Medium.ttf", activity);

            fontSetting.setFontforTextviews(RegularPricetag, "Roboto-Medium.ttf", activity);

            fontSetting.setFontforTextviews(ProductNametag, "Roboto-Medium.ttf", activity);

            fontSetting.setFontforTextviews(website, "Roboto-Medium.ttf", activity);
        }
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

        if (density <= 160) {
            builder.addContentView(imageView, new RelativeLayout.LayoutParams(
                    200,
                    400));
        }
        if (density > 160 && density <= 240) {
            builder.addContentView(imageView, new RelativeLayout.LayoutParams(
                    300,
                    500));
        }
        if (density > 240 && density <= 360) {
            builder.addContentView(imageView, new RelativeLayout.LayoutParams(
                    400,
                    600));
        }
        if (density > 360 && density <= 480) {
            builder.addContentView(imageView, new RelativeLayout.LayoutParams(
                    600,
                    800));
        }
        if (density > 480) {
            builder.addContentView(imageView, new RelativeLayout.LayoutParams(
                    700,
                    900));
        }

        builder.show();

    }
}
