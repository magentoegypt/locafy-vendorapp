package magentoegypt.locafy.vendor_dashboard;

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
import magentoegypt.locafy.vendor_transaction.Ced_MultiVendor_ViewTransaction;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class LatestTransactionAdapter extends PagerAdapter {
    JSONArray jsonArray=null;
    public LatestTransactionAdapter(JSONArray jsonArray) {
        this.jsonArray=jsonArray;
    }

    @Override
    public int getCount() {
        return jsonArray.length();
    }

    @Override
    public Object instantiateItem(final View container, final int position) {
        View itemView = LayoutInflater.from(container.getContext()).inflate(R.layout.latest_transaction_item, (ViewGroup) container, false);

        try {
            JSONObject jsonObject=jsonArray.getJSONObject(position);
            LinearLayout order = itemView.findViewById(R.id.MultiVendor_order);
            TextView created_at = itemView.findViewById(R.id.created_at);
            TextView payment_mode = itemView.findViewById(R.id.payment_mode);
            TextView transaction_id = itemView.findViewById(R.id.transaction_id);
            TextView amount = itemView.findViewById(R.id.amount);
            TextView payment_id = itemView.findViewById(R.id.payment_id);

            created_at.setText(jsonObject.getString("created_at"));
            payment_mode.setText(jsonObject.getString("payment_mode"));
            transaction_id.setText(jsonObject.getString("transaction_id"));
            amount.setText(jsonObject.getString("amount"));
            payment_id.setText(jsonObject.getString("payment_id"));

            order.setOnClickListener(v -> {
                try {
                    Intent intent = new Intent(container.getContext(), Ced_MultiVendor_ViewTransaction.class);
                    intent.putExtra("payment_id",jsonObject.getString("payment_id"));
                    container.getContext().startActivity(intent);
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
