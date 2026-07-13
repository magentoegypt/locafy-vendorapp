package magentoegypt.locafy.addons.messaging;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import magentoegypt.locafy.R;

import java.util.List;

public class Chat_Image_Adapter extends RecyclerView.Adapter<Image_Viewholder> {
    public List<String> list;
    private final Context context;

    public Chat_Image_Adapter(Context context, List<String> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getItemCount() {
        return (null != list ? list.size() : 0);
    }

    @Override
    public void onBindViewHolder(@NonNull Image_Viewholder holder, final int position) {
        try {
            holder.createView(list.get(position));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @NonNull
    public Image_Viewholder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        LayoutInflater mInflater = LayoutInflater.from(viewGroup.getContext());
        ViewGroup mainGroup = (ViewGroup) mInflater.inflate(R.layout.chat_image_item, viewGroup, false);
        return new Image_Viewholder(mainGroup, context);
    }
}