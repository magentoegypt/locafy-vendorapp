package magentoegypt.locafy.base_app;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import magentoegypt.locafy.R;
import magentoegypt.locafy.api_request_response_section.Ced_ClientRequestResponseRest_New;
import magentoegypt.locafy.api_request_response_section.network_error_handling.Ced_MultiVendor_ConnectionDetector;
import magentoegypt.locafy.api_request_response_section.network_error_handling.Ced_MultiVendor_NoInternetconnection;
import magentoegypt.locafy.vendor_login_section.Ced_Multivendor_New_Login;
import magentoegypt.locafy.vendor_session.Ced_MultiVendor_VendorSessionManagement;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class StoreSelection extends AppCompatActivity {
    String getUrl = "";
    String result;
    JSONObject jsonObj, vendor_data;
    String store_id, name, store_code;
    ArrayList<String> storename;
    ArrayList<String> storeid, storeCode;
    Ced_Load_Language cedLoad_language;
    int Selected = -1;
    private Ced_MultiVendor_VendorSessionManagement ced_sessionManagement;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ced_sessionManagement = new Ced_MultiVendor_VendorSessionManagement(getApplicationContext());
//        getUrl = getResources().getString(R.string.base_url) + "rest/V1/getStores";
        getUrl = getResources().getString(R.string.base_url) + "ar/rest/V1/vendorapi/stores/storelist";
        storename = new ArrayList<>();
        cedLoad_language = new Ced_Load_Language();
        storeid = new ArrayList<>();
        storeCode = new ArrayList<>();

        Ced_ClientRequestResponseRest_New response = new Ced_ClientRequestResponseRest_New(output -> {
            Log.d("REpo", "onCreate: "+output);
            result = output.toString();
            getdata();
        }, StoreSelection.this);
        response.execute(getUrl);

//        bindChangeStoreDialog();
    }

    private void bindChangeStoreDialog() {
        Dialog levelDialog1 = new Dialog(StoreSelection.this);
        AlertDialog.Builder builder = new AlertDialog.Builder(StoreSelection.this);
        builder.setTitle(Html.fromHtml("<font color='#000000'>" + getResources().getString(R.string.selectwebstore) + "</font>"));
        builder.setIcon(getResources().getDrawable(R.drawable.placeholder));
        final Dialog finalLevelDialog = levelDialog1;

        List<String> sCodes = new ArrayList<>();
        List<String> sIds = new ArrayList<>();
        sCodes.add("en");
        sCodes.add("ar");
        sIds.add("eng");
        sIds.add("arb");
        builder.setCancelable(false);
        builder.setSingleChoiceItems(R.array.store_name, Selected, (dialog, postion) -> {
            finalLevelDialog.dismiss();
            ced_sessionManagement.saveBaseUrl(getResources().getString(R.string.base_url));
            String storeId = (String) sIds.get(postion);
            String localecode = (String) sCodes.get(postion);
            ced_sessionManagement.saveStorelocale(localecode);
            ced_sessionManagement.saveStoreId(storeId);
            ced_sessionManagement.saveBaseUrl(ced_sessionManagement.getBase_Url() + ced_sessionManagement.getStoreLocale() + "/");
            cedLoad_language.setLanguagetoLoad(localecode, getApplicationContext());

            Intent intent = new Intent(getApplicationContext(), Ced_MultiVendor_VendorSplash.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            overridePendingTransition(R.anim.ced_multivendor_slide_in, R.anim.ced_multivendor_slide_out);
        });
        levelDialog1 = builder.create();
        levelDialog1.show();
    }

    private void getdata() {
        try {
            jsonObj = new JSONObject(result);
//            vendor_data = jsonObj.getJSONObject("vendor_data");
//            if (vendor_data.getBoolean("success")) {
                JSONArray store_list = jsonObj.getJSONArray("store_data");
                for (int i = 0; i < store_list.length(); i++) {
                    JSONObject c = store_list.getJSONObject(i);
                    store_id = c.getString("store_id");
                    store_code = c.getString("code");
                    if(store_code.equalsIgnoreCase("eg_en") || store_code.equalsIgnoreCase("eg_ar")) {
                        storeid.add(store_id);
                        storeCode.add(store_code);
                        name = c.getString("name");
                        storename.add(name);
                    }
                }

                final CharSequence[] storenames = storename.toArray(new CharSequence[storename.size()]);
                final CharSequence[] storeids = storeid.toArray(new CharSequence[storeid.size()]);
                final CharSequence[] storeCodes = storeCode.toArray(new CharSequence[storeCode.size()]);

                if (ced_sessionManagement.getStoreLocale() != null) {
                    Selected = storeCode.indexOf(ced_sessionManagement.getStoreLocale());
                }
                Dialog levelDialog1 = new Dialog(StoreSelection.this);
                AlertDialog.Builder builder = new AlertDialog.Builder(StoreSelection.this);
                builder.setTitle(Html.fromHtml("<font color='#000000'>" + getResources().getString(R.string.selectwebstore) + "</font>"));
                builder.setIcon(getResources().getDrawable(R.drawable.placeholder));
                final Dialog finalLevelDialog = levelDialog1;
                builder.setCancelable(false);
                builder.setSingleChoiceItems(storenames, Selected, (dialog, postion) -> {
                    finalLevelDialog.dismiss();
                    ced_sessionManagement.saveBaseUrl(getResources().getString(R.string.base_url));
                    String storeId = (String) storeids[postion];
                    String localecode = (String) storeCodes[postion];
                    if(localecode.equalsIgnoreCase("eg_en")){
                        localecode = "eg-en";
                    }else{
                        localecode = "eg";
                    }
                    ced_sessionManagement.saveStorelocale(localecode);
                    ced_sessionManagement.saveStoreId(storeId);
                    ced_sessionManagement.saveBaseUrl(ced_sessionManagement.getBase_Url() + ced_sessionManagement.getStoreLocale() + "/");
                    cedLoad_language.setLanguagetoLoad(localecode, getApplicationContext());
                    if(getIntent() != null && getIntent().hasExtra("isComingFrom")){
                        Intent intent = new Intent(getApplicationContext(), Ced_Multivendor_New_Login.class);
                        startActivity(intent);
                        overridePendingTransition(R.anim.ced_multivendor_slide_in, R.anim.ced_multivendor_slide_out);
                    }else{
                        Intent intent = new Intent(getApplicationContext(), Ced_MultiVendor_VendorSplash.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        startActivity(intent);
                        overridePendingTransition(R.anim.ced_multivendor_slide_in, R.anim.ced_multivendor_slide_out);
                    }
                });
                levelDialog1 = builder.create();
                levelDialog1.show();
//            }
        }
        catch (JSONException e) {
            e.printStackTrace();
            Intent main = new Intent(getApplicationContext(), Ced_MultiVendor_VendorSplash.class);
            main.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            main.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(main);
            overridePendingTransition(R.anim.ced_multivendor_slide_in, R.anim.ced_multivendor_slide_out);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    protected void onResume() {
        Ced_MultiVendor_ConnectionDetector connectionDetector = new Ced_MultiVendor_ConnectionDetector(getApplicationContext());
        if (connectionDetector.isConnectingToInternet()) {
            invalidateOptionsMenu();
            super.onResume();
        }
        else {
            Intent intent = new Intent(getApplicationContext(), Ced_MultiVendor_NoInternetconnection.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            overridePendingTransition(R.anim.ced_multivendor_slide_in, R.anim.ced_multivendor_slide_out);
            super.onResume();
        }
    }
}
