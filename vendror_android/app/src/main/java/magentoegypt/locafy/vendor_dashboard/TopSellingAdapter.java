package magentoegypt.locafy.vendor_dashboard;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import magentoegypt.locafy.R;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class TopSellingAdapter extends PagerAdapter {
    JSONArray jsonArray=null;
    public TopSellingAdapter(JSONArray jsonArray) {
        this.jsonArray=jsonArray;
    }

    @Override
    public int getCount() {
        return jsonArray.length();
    }

    @Override
    public Object instantiateItem(final View container, final int position) {
        View itemView = LayoutInflater.from(container.getContext()).inflate(R.layout.top_selling_item, (ViewGroup) container, false);

        try {
            JSONObject jsonObject=jsonArray.getJSONObject(position);
            LinearLayout order = itemView.findViewById(R.id.MultiVendor_order);

            TextView product_name = itemView.findViewById(R.id.product_name);
            TextView qty_ordered = itemView.findViewById(R.id.qty_ordered);
            TextView product_price = itemView.findViewById(R.id.product_price);

            product_name.setText(jsonObject.getString("product_name"));
            qty_ordered.setText(jsonObject.getString("qty_ordered"));
            product_price.setText(jsonObject.getString("vendor_price"));


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