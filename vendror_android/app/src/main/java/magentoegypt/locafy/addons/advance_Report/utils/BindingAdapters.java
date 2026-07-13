package magentoegypt.locafy.addons.advance_Report.utils;

import android.view.View;

import androidx.databinding.BindingAdapter;

public class BindingAdapters {
    @BindingAdapter("android:customVisibility")
    public static void customVisibility(View view, String data) {
        view.setVisibility(data == null ? View.GONE : data.isEmpty() ? View.GONE : View.VISIBLE);
    }
}
