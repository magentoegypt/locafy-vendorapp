package magentoegypt.locafy.addons.vendor_member_ship_plans;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import magentoegypt.locafy.R;

import java.util.List;

public class MembershipPlan_ItemAdapter extends RecyclerView.Adapter<MembershipPlan_ItemViewHolder> {

    public final List<MembershipPlan_ItemDataModel> dataModelList;
    private final Context context;

    public MembershipPlan_ItemAdapter(Context context, List<MembershipPlan_ItemDataModel> dataModelList) {
        this.context = context;
        this.dataModelList = dataModelList;
    }

    @NonNull
    @Override
    public MembershipPlan_ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        ViewGroup mainGroup = (ViewGroup) layoutInflater.inflate(R.layout.membership_plan_item_adapter, parent, false);
        return new MembershipPlan_ItemViewHolder(mainGroup, context);
    }

    @Override
    public void onBindViewHolder(@NonNull MembershipPlan_ItemViewHolder holder, int position) {
        try {
            holder.createView(dataModelList.get(position));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return dataModelList.size();
    }
}