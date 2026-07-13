package magentoegypt.locafy.addons.vendor_select_n_sell;

import android.app.Activity;
import android.app.AlertDialog;
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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import magentoegypt.locafy.R;
import magentoegypt.locafy.api_request_response_section.AsyncResponse;
import magentoegypt.locafy.api_request_response_section.Ced_MultiVendor_ClientRequestResponse;
import magentoegypt.locafy.api_request_response_section.network_error_handling.Ced_MultiVendor_UnAuthourizedRequestError;
import magentoegypt.locafy_constant.AppConstant;
import magentoegypt.locafy.base_app.Ced_MultiVendor_FontSetting;
import magentoegypt.locafy.base_app.Ced_MultiVendor_VendorFunctionalityList;
import magentoegypt.locafy.vendor_session.Ced_MultiVendor_VendorSessionManagement;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class New_Ced_MultiVendor_ProductListSelectNSellAdapter extends RecyclerView.Adapter<New_Ced_MultiVendor_ProductListSelectNSellAdapter.ViewHolder> {
    private View view;
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
    private JSONObject product_status_json_object;

    public New_Ced_MultiVendor_ProductListSelectNSellAdapter(Activity act, ArrayList<HashMap<String, String>> d, JSONObject status_json_object) {
        fontSetting = new Ced_MultiVendor_FontSetting();
        activity = act;
        data = d;
        inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        functionalityList = new Ced_MultiVendor_VendorFunctionalityList(activity);
        vendorSessionManagement = new Ced_MultiVendor_VendorSessionManagement(activity);
        dataforproducts = new HashMap<String, String>();
        Currenturl = vendorSessionManagement.getBase_Url() + "vmultisellerapi/product/delete";
        editCurrenturl = vendorSessionManagement.getBase_Url() + "";
        density = activity.getResources().getDisplayMetrics().densityDpi;
        product_status_json_object = status_json_object;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.ced_multivendor_productlist, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        try {
            HashMap<String, String> song = data.get(position);
            holder.product_id.setText(song.get("product_id"));
            /*holder.Productype.setText(song.get("type"));*/
            holder.product_name.setText(song.get("product_name"));
            holder.RegularPrice.setText(song.get("regular_price"));
            holder.Status.setText(song.get("status"));
            holder.website.setText(song.get("website"));
            if (holder.Status.getText().toString().toLowerCase().equals("pending")) {
                holder.Status.setTextColor(activity.getResources().getColor(R.color.pending));
            }

            if (holder.Status.getText().toString().toLowerCase().equals("approved")) {
                holder.Status.setTextColor(activity.getResources().getColor(R.color.approved));
            }

            if (holder.Status.getText().toString().toLowerCase().equals("not approved")) {
                holder.Status.setTextColor(activity.getResources().getColor(R.color.disapproved));
            }
            holder.qty.setText(song.get("qty"));
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
            holder.delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AppConstant.lockButton(v);
                    final AlertDialog.Builder builder = new AlertDialog.Builder(
                            activity);
                    builder.setTitle(R.string.alert_name);
                    builder.setIcon(R.mipmap.ic_launcher);
                    builder.setMessage(R.string.confirm_first);
                    builder.setNegativeButton(R.string.no,
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            });
                    builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {

                                    String product_id[] = holder.product_id.getText().toString().split("#");
                                    dataforproducts.put("id", product_id[1]);
                                    dataforproducts.put("vendor_id", vendorSessionManagement.getVendorid());
                                    dataforproducts.put("hashkey", vendorSessionManagement.getHahkey());
                              /*  dataforproducts.put("vendor_id", "1");
                                dataforproducts.put("hashkey", "2a1f678300166f9b941de29a25f4a4bf");*/
                                    Ced_MultiVendor_ClientRequestResponse crr = new Ced_MultiVendor_ClientRequestResponse(new AsyncResponse() {
                                        @Override
                                        public void processFinish(Object output) throws JSONException {
                                            Jstring = output.toString();
                                            if (functionalityList.getExtensionAddon()) {

                                                JSONObject object = new JSONObject(Jstring);
                                                String success = object.getJSONObject("data").getString("success");
                                                if (success.equals("true")) {
                                                    Toast.makeText(activity, object.getJSONObject("data").getString("message"), Toast.LENGTH_LONG).show();
                                                    Intent intent = new Intent(activity, Product_list.class);
                                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                    activity.startActivity(intent);
                                                    activity.overridePendingTransition(R.anim.ced_multivendor_slide_in, R.anim.ced_multivendor_slide_out);
                                                } else {
                                                    Toast.makeText(activity, object.getJSONObject("data").getString("message"), Toast.LENGTH_LONG).show();
                                                }
                                            } else {
                                                Intent intent = new Intent(activity, Ced_MultiVendor_UnAuthourizedRequestError.class);
                                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                                activity.startActivity(intent);
                                                activity.overridePendingTransition(R.anim.ced_multivendor_slide_in, R.anim.ced_multivendor_slide_out);
                                            }
                                        }
                                    }, activity, "POST", dataforproducts);
                                    crr.execute(Currenturl);
                                }
                            }
                    );
                    builder.show();
                }
            });
            holder.edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AppConstant.lockButton(v);
                    final String product_id[] = holder.product_id.getText().toString().split("#");

                    //dataforproducts.put("product_id", product_id[1]);
                /*dataforproducts.put("vendor_id", vendorSessionManagement.getVendorid());
                dataforproducts.put("hashkey", vendorSessionManagement.getHahkey());*/
                    Intent intent = new Intent(activity, Ced_MultiVendor_Venor_View_Selling_Product.class);
                    intent.putExtra("product_id", product_id[1]);
                    intent.putExtra("product_name", holder.product_name.getText().toString());
                    if (product_status_json_object != null) {
                        intent.putExtra("product_status_json_object", product_status_json_object.toString());
                    }
                    activity.startActivity(intent);

               /* Ced_MultiVendor_ClientRequestResponse crr = new Ced_MultiVendor_ClientRequestResponse(new AsyncResponse() {

                    @Override
                    public void processFinish(Object output) throws JSONException
                    {
                        Jstring = output.toString();
                        if (functionalityList.getExtensionAddon())
                        {
                            JSONObject object=new JSONObject(Jstring);
                            String success=object.getJSONObject("data").getString("success");
                            if(success.equals("true"))
                            {
                                JSONObject productdata=object.getJSONObject("data").getJSONObject("productdata");
                                String type=productdata.getString("type_id");
                                Intent update=new Intent(activity,Ced_MultiVendor_UpdateProduct.class);
                                update.putExtra("type",type);
                                update.putExtra("dataforproductcreation",Jstring);
                                update.putExtra("product_id", product_id[1]);
                                if(functionalityList.getProductAddon())
                                {
                                    if(type.equals("configurable"))
                                    {
                                        Ced_MultiVendor_GlobalVariables.config_attribute_value=object.getJSONObject("data").getJSONObject("config_selected_attr");
                                    }
                                    String attribute_set=object.getJSONObject("data").getString("attribute_set");
                                    update.putExtra("attribute_set",attribute_set);
                                }
                                activity.startActivity(update);
                                activity.overridePendingTransition(R.anim.ced_multivendor_slide_in, R.anim.ced_multivendor_slide_out);

                            }


                        }
                        else
                        {
                            Intent intent = new Intent(activity, Ced_MultiVendor_UnAuthourizedRequestError.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            activity.startActivity(intent);
                            activity.overridePendingTransition(R.anim.ced_multivendor_slide_in, R.anim.ced_multivendor_slide_out);
                        }

                    }
                }, activity, "POST", dataforproducts);
                crr.execute(editCurrenturl);*/
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return data.size() > 0 ? data.size() : 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView productIDtag;
        public TextView qtytag;
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
        public TextView qty;
        public TextView Status;
        public TextView website;
        public TextView delete;
        public TextView edit;
        public TextView picurl;
        public ImageView prductimage;

        public ViewHolder(@NonNull View vi) {
            super(vi);
            productIDtag = (TextView) vi.findViewById(R.id.MultiVendor_productIDtag);
            qtytag = (TextView) vi.findViewById(R.id.MultiVendor_qtytag);
            skutag = (TextView) vi.findViewById(R.id.MultiVendor_skutag);
            Productypetag = (TextView) vi.findViewById(R.id.MultiVendor_Productypetag);
            Productypetag.setVisibility(View.GONE);
            RegularPricetag = (TextView) vi.findViewById(R.id.MultiVendor_RegularPricetag);
            ProductNametag = (TextView) vi.findViewById(R.id.MultiVendor_ProductNametag);
            statustag = (TextView) vi.findViewById(R.id.MultiVendor_statustag);
            product_id = (TextView) vi.findViewById(R.id.MultiVendor_product_id);
            delete = (TextView) vi.findViewById(R.id.MultiVendor_delete);
            edit = (TextView) vi.findViewById(R.id.MultiVendor_edit);
            Productype = (TextView) vi.findViewById(R.id.MultiVendor_Productype);
            Productype.setVisibility(View.GONE);
            product_name = (TextView) vi.findViewById(R.id.MultiVendor_product_name);
            RegularPrice = (TextView) vi.findViewById(R.id.MultiVendor_RegularPrice);
            qty = (TextView) vi.findViewById(R.id.MultiVendor_qty);
            sku = (TextView) vi.findViewById(R.id.MultiVendor_sku);
            Status = (TextView) vi.findViewById(R.id.MultiVendor_Status);
            picurl = (TextView) vi.findViewById(R.id.MultiVendor_picurl);
            prductimage = (ImageView) vi.findViewById(R.id.MultiVendor_prductimage);
            website = (TextView) vi.findViewById(R.id.MultiVendor_website_text);

            /**************************Regular*****************************/
            fontSetting.setFontforTextviews(product_id, "Roboto-Regular.ttf", activity);
            fontSetting.setFontforTextviews(Productype, "Roboto-Regular.ttf", activity);
            fontSetting.setFontforTextviews(product_name, "Roboto-Regular.ttf", activity);
            fontSetting.setFontforTextviews(RegularPrice, "Roboto-Regular.ttf", activity);
            fontSetting.setFontforTextviews(qty, "Roboto-Regular.ttf", activity);
            fontSetting.setFontforTextviews(sku, "Roboto-Regular.ttf", activity);
            fontSetting.setFontforTextviews(Status, "Roboto-Regular.ttf", activity);
            /*******************************************************/
            /**************************Medium*****************************/
            fontSetting.setFontforTextviews(delete, "Roboto-Medium.ttf", activity);
            fontSetting.setFontforTextviews(edit, "Roboto-Medium.ttf", activity);
            fontSetting.setFontforTextviews(productIDtag, "Roboto-Medium.ttf", activity);
            fontSetting.setFontforTextviews(qtytag, "Roboto-Medium.ttf", activity);
            fontSetting.setFontforTextviews(skutag, "Roboto-Medium.ttf", activity);
            fontSetting.setFontforTextviews(Productypetag, "Roboto-Medium.ttf", activity);
            fontSetting.setFontforTextviews(RegularPricetag, "Roboto-Medium.ttf", activity);
            fontSetting.setFontforTextviews(statustag, "Roboto-Medium.ttf", activity);
            fontSetting.setFontforTextviews(ProductNametag, "Roboto-Medium.ttf", activity);
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
