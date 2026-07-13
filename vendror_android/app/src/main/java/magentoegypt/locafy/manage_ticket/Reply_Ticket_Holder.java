package magentoegypt.locafy.manage_ticket;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import magentoegypt.locafy.R;

class Reply_Ticket_Holder extends RecyclerView.ViewHolder {

    AppCompatTextView from,createdat,message,image;
    View view;
    Context context;

    public Reply_Ticket_Holder(ViewGroup mainGroup, Context context) {
        super(mainGroup);
        view=mainGroup;
        this.context=context;
        this.from =  mainGroup.findViewById(R.id.from);
        this.createdat =  mainGroup.findViewById(R.id.createdat);
        this.message =  mainGroup.findViewById(R.id.message);
        this.image =  mainGroup.findViewById(R.id.image);
    }

    public void createView(Ticket_ChatModel ticket_chatModel) {

        createdat.setText(ticket_chatModel.getMessage_time());
        message.setText(ticket_chatModel.getMessage());
        from.setText("From : "+ticket_chatModel.getFrom());
        if (ticket_chatModel.getImage()!=null) {
            image.setVisibility(View.VISIBLE);
            final String imageString=ticket_chatModel.getImage();
            image.setText(imageString.split("/")[imageString.split("/").length-1]);
            image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder alert = new AlertDialog.Builder(context);
                    alert.setTitle(imageString.split("/")[imageString.split("/").length-1]);

                    WebView wv = new WebView(context);
                    wv.loadUrl(imageString);
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
        else {
            image.setVisibility(View.GONE);
        }

//        Log.i("REpo","model.getFrom() : "+ticket_chatModel.getMessage());
//        Log.i("REpo","model.getimage() : "+ticket_chatModel.getImage());

        if (ticket_chatModel.getFrom()!=null&&ticket_chatModel.getFrom().equals("vendor")){
            view.setBackgroundColor(context.getResources().getColor(R.color.tovendor));
        }
        else {
            view.setBackgroundColor(context.getResources().getColor(R.color.fromvendor));
        }



    }
}
