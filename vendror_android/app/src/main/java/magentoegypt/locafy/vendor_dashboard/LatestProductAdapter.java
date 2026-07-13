package magentoegypt.locafy.vendor_dashboard;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import magentoegypt.locafy.R;
import magentoegypt.locafy.api_request_response_section.AsyncResponse;
import magentoegypt.locafy.api_request_response_section.Ced_MultiVendor_ClientRequestResponse;
import magentoegypt.locafy.api_request_response_section.network_error_handling.Ced_MultiVendor_UnAuthourizedRequestError;
import magentoegypt.locafy.manage_products_section.Ced_MultiVendor_ManageProducts;
import magentoegypt.locafy_constant.Ced_MultiVendor_GlobalVariables;
import magentoegypt.locafy.base_app.Ced_MultiVendor_VendorFunctionalityList;
import magentoegypt.locafy.manage_products_section.EditProductDynamic;
import magentoegypt.locafy.vendor_session.Ced_MultiVendor_VendorSessionManagement;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class LatestProductAdapter extends PagerAdapter {
    JSONArray jsonArray=null;
    public LatestProductAdapter(JSONArray jsonArray) {
        this.jsonArray=jsonArray;
    }

    @Override
    public int getCount() {
        return jsonArray.length();
    }

    @Override
    public Object instantiateItem(final View container, final int position) {
        View itemView = LayoutInflater.from(container.getContext()).inflate(R.layout.latest_products, (ViewGroup) container, false);

        try {
            JSONObject jsonObject=jsonArray.getJSONObject(position);
            LinearLayout order = itemView.findViewById(R.id.MultiVendor_order);
            TextView productID = itemView.findViewById(R.id.productID);
            TextView productName = itemView.findViewById(R.id.productName);
            TextView prodQty = itemView.findViewById(R.id.prodQty);
            TextView price = itemView.findViewById(R.id.price);
            TextView prodStatus = itemView.findViewById(R.id.prodStatus);

            productID.setText(jsonObject.getString("product_id"));
            productName.setText(jsonObject.getString("product_name"));
            price.setText(jsonObject.getString("vendor_price"));
            prodQty.setText(jsonObject.getString("product_qty"));
            prodStatus.setText(jsonObject.getString("product_status"));

            order.setOnClickListener(v -> {
                try {
                    Ced_MultiVendor_VendorFunctionalityList functionalityList = new Ced_MultiVendor_VendorFunctionalityList(container.getContext());
                    HashMap<String, String> dataforproducts = new HashMap<>();
                    Ced_MultiVendor_VendorSessionManagement vendorSessionManagement = new Ced_MultiVendor_VendorSessionManagement(container.getContext());
                    String editCurrenturl = vendorSessionManagement.getBase_Url() + "vendorapi/vproducts/info";
                    dataforproducts.put("product_id", jsonObject.getString("product_id"));
                    dataforproducts.put("store_id", Ced_MultiVendor_ManageProducts.store_id);
                    if (vendorSessionManagement.getStoreId() != null) {
                        dataforproducts.put("store_id", vendorSessionManagement.getStoreId());
                    }
                    dataforproducts.put("vendor_id", vendorSessionManagement.getVendorid());
                    dataforproducts.put("hashkey", vendorSessionManagement.getHahkey());
                    Ced_MultiVendor_ClientRequestResponse crr = new Ced_MultiVendor_ClientRequestResponse((AsyncResponse) output -> {
                        String Jstring = output.toString();
                        if (functionalityList.getExtensionAddon()) {
                            JSONObject object = new JSONObject(Jstring);
                            String success = object.getJSONObject("data").getString("success");
                            if (success.equals("true")) {
                                JSONObject productdata = object.getJSONObject("data").getJSONObject("productdata");
                                String type = productdata.getString("type_id");

                                Intent update = new Intent(container.getContext(), EditProductDynamic.class);
                                update.putExtra("type", type);
                                update.putExtra("dataforproductcreation", Jstring);
                                update.putExtra("product_id",  jsonObject.getString("product_id"));
                                update.putExtra("selectedwebsite", "");
                                if (functionalityList.getProductAddon()) {
                                    if (type.equals("configurable") && object.getJSONObject("data").get("config_selected_attr") instanceof JSONObject) {
                                        Ced_MultiVendor_GlobalVariables.config_attribute_value = object.getJSONObject("data").getJSONObject("config_selected_attr");
                                    }
                                    String attribute_set = productdata.getString("attribute_set_id");
                                    update.putExtra("attribute_set", attribute_set);
                                }
                                container.getContext().startActivity(update);

                            }


                        }
                        else {
                            Intent intent = new Intent(container.getContext(), Ced_MultiVendor_UnAuthourizedRequestError.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            container.getContext().startActivity(intent);
                        }

                    }, (Activity) container.getContext(), "POST", dataforproducts);
                    crr.execute(editCurrenturl);

                }
                catch (JSONException e) {
                    e.printStackTrace();
                }

            });

            ((ViewPager) container).addView(itemView);
        }
        catch (JSONException e) {
            e.printStackTrace();
        }

        return itemView;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((RelativeLayout) object);
    }

}
