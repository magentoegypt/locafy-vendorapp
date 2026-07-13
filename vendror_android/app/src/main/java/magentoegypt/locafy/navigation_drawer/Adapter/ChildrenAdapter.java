package magentoegypt.locafy.navigation_drawer.Adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import magentoegypt.locafy.R;
import magentoegypt.locafy.databinding.ChildrenNavigationDrawerBinding;
import magentoegypt.locafy.databinding.NavigationDrawerViewBinding;
import magentoegypt.locafy.navigation_drawer.models.NavigationChild;

import java.util.List;

public class ChildrenAdapter extends RecyclerView.Adapter<ChildrenAdapter.ChildrenViewHolder> {
    List<NavigationChild> list;

    SideNavigationClickListener sideNavigationClickListener;

    public ChildrenAdapter(List<NavigationChild> list, SideNavigationClickListener sideNavigationClickListener) {
        this.list = list;
        this.sideNavigationClickListener = sideNavigationClickListener;
    }

    @NonNull
    @Override
    public ChildrenAdapter.ChildrenViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ChildrenNavigationDrawerBinding layoutOrderItemBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.children_navigation_drawer, parent, false);
        return new ChildrenViewHolder(layoutOrderItemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ChildrenAdapter.ChildrenViewHolder holder, int position) {

        NavigationChild child = list.get(position);
        holder.childrenNavigationDrawerBinding.setChildren(child);
        holder.childrenNavigationDrawerBinding.mainChildrenView.setOnClickListener(view -> {
            sideNavigationClickListener.onNavigationItemClick(child);
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ChildrenViewHolder extends RecyclerView.ViewHolder {
        ChildrenNavigationDrawerBinding childrenNavigationDrawerBinding;

        public ChildrenViewHolder(ChildrenNavigationDrawerBinding childrenNavigationDrawerBinding) {
            super(childrenNavigationDrawerBinding.getRoot());
            this.childrenNavigationDrawerBinding = childrenNavigationDrawerBinding;
        }
    }
}
