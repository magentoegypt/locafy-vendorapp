package magentoegypt.locafy.addons.messaging;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.bumptech.glide.Glide;
import magentoegypt.locafy.R;

class Image_Viewholder extends RecyclerView.ViewHolder {

    private final AppCompatImageView appCompatImageView;
    private final Context context;

    public Image_Viewholder(View itemView, Context context) {
        super(itemView);
        this.context=context;
        appCompatImageView=itemView.findViewById(R.id.image);
    }

    public void createView(final String s) {
        Glide.with(context)
                .load(s)
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.placeholder)
                .into(appCompatImageView);

        appCompatImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alert = new AlertDialog.Builder(context);
                alert.setTitle(s.split("/")[s.split("/").length-1]);
                WebView wv = new WebView(context);
                wv.loadUrl(s);
                wv.getSettings().setBuiltInZoomControls(true);
                wv.setWebViewClient(new WebViewClient() {
                    @Override
                    public boolean shouldOverrideUrlLoading(WebView view, String url) {
                        view.loadUrl(url);
                        return true;
                    }
                });
                alert.setView(wv);
                alert.setNegativeButton("Close", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });
                alert.show();
            }
        });
    }
}