package magentoegypt.locafy.addons.messaging.admin_inbox;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import magentoegypt.locafy.R;

import java.util.List;

public class Admin_Inbox_ChatView_Adapter extends RecyclerView.Adapter<Admin_Inbox_ChatView_ViewHolder> {
    private final Context context;
    public List<Admin_Inbox_Chatview_Model> list;

    public Admin_Inbox_ChatView_Adapter(Context context, List<Admin_Inbox_Chatview_Model> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getItemCount() {
        return (null != list ? list.size() : 0);
    }

    @Override
    public void onBindViewHolder(@NonNull Admin_Inbox_ChatView_ViewHolder holder, final int position) {
        try {
            holder.createView(list.get(list.size() - (position + 1)));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @NonNull
    public Admin_Inbox_ChatView_ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        LayoutInflater mInflater = LayoutInflater.from(viewGroup.getContext());
        ViewGroup mainGroup = (ViewGroup) mInflater.inflate(R.layout.admin_inbox_chat_item, viewGroup, false);
        return new Admin_Inbox_ChatView_ViewHolder(mainGroup, context);
    }
}