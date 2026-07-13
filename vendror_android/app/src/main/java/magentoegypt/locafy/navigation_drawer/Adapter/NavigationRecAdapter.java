package magentoegypt.locafy.navigation_drawer.Adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import magentoegypt.locafy.R;
import magentoegypt.locafy.databinding.NavigationDrawerViewBinding;
import magentoegypt.locafy.navigation_drawer.models.NavigationModel;

import java.util.List;

public class NavigationRecAdapter extends RecyclerView.Adapter<NavigationRecAdapter.NavigationViewHolder> {

    List<NavigationModel> navigationModels;
    Activity activity;
    SideNavigationClickListener sideNavigationClickListener;

    public NavigationRecAdapter(Activity activity, List<NavigationModel> navigationItemList, SideNavigationClickListener sideNavigationClickListener) {
        this.activity = activity;
        this.sideNavigationClickListener = sideNavigationClickListener;
        navigationModels = navigationItemList;
    }

    @NonNull
    @Override

    public NavigationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        NavigationDrawerViewBinding layoutOrderItemBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.navigation_drawer_view, parent, false);
        return new NavigationViewHolder(layoutOrderItemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull NavigationViewHolder holder, int position) {
        NavigationModel navData = navigationModels.get(position);
        holder.navigationDrawerViewBinding.setNavigationItem(navData);
        setUpChildrenAdapter(holder.navigationDrawerViewBinding, navData, sideNavigationClickListener);
        holder.navigationDrawerViewBinding.mainView.setOnClickListener(view -> {
            if (navData.getChildren().isEmpty()) {
                sideNavigationClickListener.onNavigationItemClick(navData);
                return;
            }

            float rotation = holder.navigationDrawerViewBinding.arrowIcon.getRotation();
            rotation = rotation == 0 ? 90 : 0;
            holder.navigationDrawerViewBinding.arrowIcon.setRotation(rotation);
            holder.navigationDrawerViewBinding.childrenRec.setVisibility(rotation != 0 ? View.VISIBLE : View.GONE);
        });


    }

    private void setUpChildrenAdapter(NavigationDrawerViewBinding navigationDrawerViewBinding,
                                      NavigationModel navData,
                                      SideNavigationClickListener sideNavigationClickListener) {
        ChildrenAdapter childrenAdapter = new ChildrenAdapter(navData.getChildren(), sideNavigationClickListener);
        navigationDrawerViewBinding.childrenRec.setAdapter(childrenAdapter);
    }

    @Override
    public int getItemCount() {
        return navigationModels.size();
    }

    public class NavigationViewHolder extends RecyclerView.ViewHolder {
        NavigationDrawerViewBinding navigationDrawerViewBinding;

        public NavigationViewHolder(NavigationDrawerViewBinding navigationDrawerViewBinding) {
            super(navigationDrawerViewBinding.getRoot());
            this.navigationDrawerViewBinding = navigationDrawerViewBinding;
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    public void addData(List<NavigationModel> list) {
        navigationModels.addAll(list);
        notifyDataSetChanged();

    }
}
