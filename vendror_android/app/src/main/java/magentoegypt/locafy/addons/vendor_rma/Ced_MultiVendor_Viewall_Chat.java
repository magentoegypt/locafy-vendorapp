package magentoegypt.locafy.addons.vendor_rma;

import android.os.Bundle;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ListView;

import magentoegypt.locafy.navigation_drawer.Activity.Ced_MultiVendor_NavigationActivity;
import magentoegypt.locafy.R;


/**
 * Created by cedcoss on 17/1/18.
 */

public class Ced_MultiVendor_Viewall_Chat extends Ced_MultiVendor_NavigationActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (android.os.Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

            // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

            // finally change the color
            window.setStatusBarColor(this.getResources().getColor(R.color.status_bar_color));
        }

        ViewGroup content = (ViewGroup) findViewById(R.id.MultiVendor_frame_container);
        getLayoutInflater().inflate(R.layout.ced_multivendor_viewall_chat, content, true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.backbutton);

        ListView chat_info_list=(ListView)findViewById(R.id.chat_info_list);
        Ced_MultiVendor_ChatHistoryAdapter chatHistoryAdapter = new Ced_MultiVendor_ChatHistoryAdapter(Ced_MultiVendor_Viewall_Chat.this, Ced_MultiVendor_RMA_RequestView.chat_history);
        chat_info_list.setAdapter(chatHistoryAdapter);
        chatHistoryAdapter.notifyDataSetChanged();
    }
}
