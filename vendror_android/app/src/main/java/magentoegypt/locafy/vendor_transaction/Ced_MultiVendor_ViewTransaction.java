/*
 *
 *   Copyright/* *
 *             * CedCommerce
 *             *
 *             * NOTICE OF LICENSE
 *             *
 *             * This source file is subject to the End User License Agreement (EULA)
 *             * that is bundled with this package in the file LICENSE.txt.
 *             * It is also available through the world-wide-web at this URL:
 *             * http://cedcommerce.com/license-agreement.txt
 *             *
 *             * @category  Ced
 *             * @package   MultiVendor
 *             * @author    CedCommerce Core Team <connect@cedcommerce.com >
 *             * @copyright Copyright CEDCOMMERCE (http://cedcommerce.com/)
 *             * @license   http://cedcommerce.com/license-agreement.txt
 *
 *
 *
 */

package magentoegypt.locafy.vendor_transaction;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.GsonBuilder;
import magentoegypt.locafy.base_app.Ced_MultiVendor_FontSetting;
import magentoegypt.locafy.base_app.Ced_MultiVendor_VendorFunctionalityList;
import magentoegypt.locafy.navigation_drawer.Activity.Ced_MultiVendor_NavigationActivity;
import magentoegypt.locafy.api_request_response_section.network_error_handling.Ced_MultiVendor_ConnectionDetector;
import magentoegypt.locafy.api_request_response_section.network_error_handling.Ced_MultiVendor_NoInternetconnection;
import magentoegypt.locafy.api_request_response_section.network_error_handling.Ced_MultiVendor_UnAuthourizedRequestError;
import magentoegypt.locafy.api_request_response_section.AsyncResponse;
import magentoegypt.locafy.api_request_response_section.Ced_MultiVendor_ClientRequestResponse;
import magentoegypt.locafy.vendor_session.Ced_MultiVendor_VendorSessionManagement;
import magentoegypt.locafy.R;
import magentoegypt.locafy.vendor_transaction.adapter.OrderDetailsAdapter;
import magentoegypt.locafy.vendor_transaction.model.Data;
import magentoegypt.locafy.vendor_transaction.model.OrderDetail;
import magentoegypt.locafy.vendor_transaction.model.PaymentDetailDataModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by developer on 5/5/16.
 */
public class Ced_MultiVendor_ViewTransaction extends Ced_MultiVendor_NavigationActivity {
    Ced_MultiVendor_ConnectionDetector connectionDetector;
    Ced_MultiVendor_VendorFunctionalityList functionalityList;
    String url = "";
    HashMap<String, String> postdata;
    String vendor_id, value, out, hashkey;
    JSONObject jsonObject;
    JSONArray payment_detail;
    String paymentmethod, paymentdetail, transactionId, createdat, transactionmode,
            transactiontype, amount, adjustment_amount, net_amount, notes, vendorname, order_detail;
    TextView VendorName_head, VendorName, PaymentMethod_head, PaymentMethod, BeneficiaryDetails_head, BeneficiaryDetails, order_details_head,
            TransactionID_head, TransactionID, TransactionDate_head,
            TransactionDate, TransactionMode_head, TransactionMode, TransactionType_head, TransactionType,
            Amount_head, Amount, AdjustmentAmount_head, AdjustmentAmount, NetAmount_head, NetAmount, Notes_head, Notes,pdf_head, pdfView;
    Button BenificiaryDetails_head, TRANSACTIONDETAILS_head, Orderdetails_head;
    Ced_MultiVendor_FontSetting fontSetting;
    WebView order_details;
    RecyclerView order_details_rv;

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
        connectionDetector = new Ced_MultiVendor_ConnectionDetector(getApplicationContext());
        functionalityList = new Ced_MultiVendor_VendorFunctionalityList(getApplicationContext());
        postdata = new HashMap<>();
        fontSetting = new Ced_MultiVendor_FontSetting();
        url = session.getBase_Url() + "vendorapi/vtransaction/viewpayment";

        if (connectionDetector.isConnectingToInternet()) {
            ViewGroup content = findViewById(R.id.MultiVendor_frame_container);
            getLayoutInflater().inflate(R.layout.ced_multivendor_viewpage_transaction, content, true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.backbutton);
            if (session.getvendorname() != null) {
                setname(session.getvendorname());
                setprofilepic(session.getvendorpic());
                changetitle("Transactions");
            }
            vendor_id = session.getVendorid();
            final HashMap<String, String> user = session.getUserDetails();
            hashkey = user.get(Ced_MultiVendor_VendorSessionManagement.Key_HAsh);
            postdata.put("hashkey", hashkey);
            value = getIntent().getStringExtra("payment_id");
            postdata.put("vendor_id", vendor_id);
            postdata.put("payment_id", value);

            BenificiaryDetails_head = findViewById(R.id.MultiVendor_BenificiaryDetails_head);
            TRANSACTIONDETAILS_head = findViewById(R.id.MultiVendor_TRANSACTIONDETAILS_head);
            Orderdetails_head = findViewById(R.id.MultiVendor_Orderdetails_head);

            VendorName_head = findViewById(R.id.MultiVendor_VendorName_head);
            VendorName = findViewById(R.id.MultiVendor_VendorName);
            PaymentMethod_head = findViewById(R.id.MultiVendor_PaymentMethod_head);
            PaymentMethod = findViewById(R.id.MultiVendor_PaymentMethod);
            BeneficiaryDetails_head = findViewById(R.id.MultiVendor_BeneficiaryDetails_head);
            BeneficiaryDetails = findViewById(R.id.MultiVendor_BeneficiaryDetails);
            order_details_head = findViewById(R.id.MultiVendor_order_details_head);
            order_details_rv = findViewById(R.id.order_details_rv);
            TransactionID_head = findViewById(R.id.MultiVendor_TransactionID_head);
            TransactionID = findViewById(R.id.MultiVendor_TransactionID);
            TransactionDate_head = findViewById(R.id.MultiVendor_TransactionDate_head);
            TransactionDate = findViewById(R.id.MultiVendor_TransactionDate);
            TransactionMode_head = findViewById(R.id.MultiVendor_TransactionMode_head);
            TransactionMode = findViewById(R.id.MultiVendor_TransactionMode);
            TransactionType_head = findViewById(R.id.MultiVendor_TransactionType_head);
            TransactionType = findViewById(R.id.MultiVendor_TransactionType);
            Amount_head = findViewById(R.id.MultiVendor_Amount_head);
            Amount = findViewById(R.id.MultiVendor_Amount);
            AdjustmentAmount = findViewById(R.id.MultiVendor_AdjustmentAmount);
            AdjustmentAmount_head = findViewById(R.id.MultiVendor_AdjustmentAmount_head);
            NetAmount_head = findViewById(R.id.MultiVendor_NetAmount_head);
            NetAmount = findViewById(R.id.MultiVendor_NetAmount);
            Notes_head = findViewById(R.id.MultiVendor_Notes_head);
            Notes = findViewById(R.id.MultiVendor_Notes);
            pdf_head = findViewById(R.id.MultiVendor_pdf_head);
            pdfView = findViewById(R.id.MultiVendor_pdf);

            fontSetting.setfontforButtons(BenificiaryDetails_head, "Roboto-Bold.ttf", getApplicationContext());
            fontSetting.setfontforButtons(TRANSACTIONDETAILS_head, "Roboto-Bold.ttf", getApplicationContext());
            fontSetting.setfontforButtons(Orderdetails_head, "Roboto-Bold.ttf", getApplicationContext());

            fontSetting.setFontforTextviews(VendorName_head, "Roboto-Medium.ttf", getApplicationContext());
            fontSetting.setFontforTextviews(PaymentMethod_head, "Roboto-Medium.ttf", getApplicationContext());
            fontSetting.setFontforTextviews(BeneficiaryDetails_head, "Roboto-Medium.ttf", getApplicationContext());
            fontSetting.setFontforTextviews(order_details_head, "Roboto-Medium.ttf", getApplicationContext());
            fontSetting.setFontforTextviews(TransactionID_head, "Roboto-Medium.ttf", getApplicationContext());
            fontSetting.setFontforTextviews(TransactionDate_head, "Roboto-Medium.ttf", getApplicationContext());
            fontSetting.setFontforTextviews(TransactionMode_head, "Roboto-Medium.ttf", getApplicationContext());
            fontSetting.setFontforTextviews(TransactionType_head, "Roboto-Medium.ttf", getApplicationContext());
            fontSetting.setFontforTextviews(Amount_head, "Roboto-Medium.ttf", getApplicationContext());
            fontSetting.setFontforTextviews(AdjustmentAmount_head, "Roboto-Medium.ttf", getApplicationContext());
            fontSetting.setFontforTextviews(NetAmount_head, "Roboto-Medium.ttf", getApplicationContext());
            fontSetting.setFontforTextviews(Notes_head, "Roboto-Medium.ttf", getApplicationContext());

            fontSetting.setFontforTextviews(VendorName, "Roboto-Regular.ttf", getApplicationContext());
            fontSetting.setFontforTextviews(PaymentMethod, "Roboto-Regular.ttf", getApplicationContext());
            fontSetting.setFontforTextviews(BeneficiaryDetails, "Roboto-Regular.ttf", getApplicationContext());
            // fontSetting.setFontforTextviews(order_details, "Roboto-Regular.ttf", getApplicationContext());
            fontSetting.setFontforTextviews(TransactionID, "Roboto-Regular.ttf", getApplicationContext());
            fontSetting.setFontforTextviews(TransactionDate, "Roboto-Regular.ttf", getApplicationContext());
            fontSetting.setFontforTextviews(TransactionMode, "Roboto-Regular.ttf", getApplicationContext());
            fontSetting.setFontforTextviews(TransactionType, "Roboto-Regular.ttf", getApplicationContext());
            fontSetting.setFontforTextviews(Amount, "Roboto-Regular.ttf", getApplicationContext());
            fontSetting.setFontforTextviews(AdjustmentAmount, "Roboto-Regular.ttf", getApplicationContext());
            fontSetting.setFontforTextviews(NetAmount, "Roboto-Regular.ttf", getApplicationContext());
            fontSetting.setFontforTextviews(Notes, "Roboto-Regular.ttf", getApplicationContext());

            request();
        } else {
            Intent nointernet = new Intent(getApplicationContext(), Ced_MultiVendor_NoInternetconnection.class);
            nointernet.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            nointernet.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(nointernet);
            overridePendingTransition(R.anim.ced_multivendor_slide_in, R.anim.ced_multivendor_slide_out);
        }
    }

    private void request() {
        Ced_MultiVendor_ClientRequestResponse response = new Ced_MultiVendor_ClientRequestResponse(new AsyncResponse() {
            @Override
            public void processFinish(Object output) throws JSONException {
                if (functionalityList.getExtensionAddon()) {
                    out = output.toString();
                    transactionviewdata();
                } else {
                    Intent intent = new Intent(getApplicationContext(), Ced_MultiVendor_UnAuthourizedRequestError.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    overridePendingTransition(R.anim.ced_multivendor_slide_in, R.anim.ced_multivendor_slide_out);
                }
            }
        }, Ced_MultiVendor_ViewTransaction.this, "POST", postdata);
        response.execute(url);

    }

    @SuppressLint("NotifyDataSetChanged")
    private void transactionviewdata() {
        try {
            jsonObject = new JSONObject(out);
            if (jsonObject.has("vendor_approved")) {
                logout();
            } else {
                if (jsonObject.getJSONObject("data").getBoolean("success")) {
                    payment_detail = jsonObject.getJSONObject("data").getJSONArray("payment_detail");
                    Data data = new GsonBuilder().create().fromJson(jsonObject.getJSONObject("data").toString(), Data.class);
                    List<PaymentDetailDataModel> paymentDetailDataModel = data.getPaymentDetail();
                    if (paymentDetailDataModel != null) {
                        List<OrderDetail> orderDetails = paymentDetailDataModel.get(0).getOrderDetail();
                        LinearLayoutManager lm = new LinearLayoutManager(this);
                        order_details_rv.setLayoutManager(lm);
                        if (paymentDetailDataModel.get(0).getOrderDetail() != null && paymentDetailDataModel.get(0).getOrderDetail().size() > 0) {
                            OrderDetailsAdapter orderDetailsAdapter = new OrderDetailsAdapter(orderDetails, this);
                            order_details_rv.setAdapter(orderDetailsAdapter);
                            orderDetailsAdapter.notifyDataSetChanged();
                        } else {
                            order_details_rv.setVisibility(View.GONE);
                            order_details_head.setVisibility(View.GONE);
                        }
                    }
                    for (int i = 0; i < payment_detail.length(); i++) {
                        JSONObject c = null;
                        c = payment_detail.getJSONObject(i);
                        paymentmethod = c.getString("paymentmethod");
                        vendorname = c.getString("vendorname");
                        order_detail = c.getString("order_detail");
                        paymentdetail = c.getString("paymentdetail");
                        transactionId = c.getString("transactionId");
                        createdat =  convertToEgyptTimeZone(c.getString("createdat"));
                        transactionmode = c.getString("transactionmode");
                        transactiontype = c.getString("transactiontype");
                        amount = c.getString("amount");
                        adjustment_amount = c.getString("adjustment_amount");
                        net_amount = c.getString("net_amount");
                        notes = c.getString("notes");
                        String pdfDocument = "";
                        if(c.has("transaction_doc"))
                        pdfDocument = c.getString("transaction_doc");
                        if (vendorname.equals("null")) {
                            VendorName.setVisibility(View.GONE);
                            VendorName_head.setVisibility(View.GONE);
                        } else {
                            VendorName.setText(Html.fromHtml(vendorname));
                        }
                        if (paymentmethod.equals("null")) {
                            PaymentMethod.setVisibility(View.GONE);
                            PaymentMethod_head.setVisibility(View.GONE);
                        } else {
                            PaymentMethod.setText(paymentmethod);
                        }
                        if (transactionmode.equals("null")) {
                            BeneficiaryDetails.setVisibility(View.GONE);
                            BeneficiaryDetails_head.setVisibility(View.GONE);
                        } else {
                            BeneficiaryDetails.setText(Html.fromHtml(transactionmode));
                        }
                        if (order_detail.equals("null")) {
                            order_details.setVisibility(View.GONE);
                            order_details_head.setVisibility(View.GONE);
                        } else {
                            //TODO ITERATE AND
//                            order_details.loadDataWithBaseURL(null, order_detail, "text/html", "utf-8", null);
                        }
                        if (transactionId.equals("null")) {
                            TransactionID.setVisibility(View.GONE);
                            TransactionID_head.setVisibility(View.GONE);
                        } else {
                            TransactionID.setText(transactionId);
                        }
                        if (createdat.equals("null")) {
                            TransactionDate.setVisibility(View.GONE);
                            TransactionDate_head.setVisibility(View.GONE);
                        } else {
                            TransactionDate.setText(createdat);
                        }
                        if (transactionmode.equals("null")) {
                            TransactionMode.setVisibility(View.GONE);
                            TransactionMode_head.setVisibility(View.GONE);
                        } else {
                            TransactionMode.setText(transactionmode);
                        }
                        if (transactiontype.equals("null")) {
                            TransactionType.setVisibility(View.GONE);
                            TransactionType_head.setVisibility(View.GONE);
                        } else {
                            TransactionType.setText(transactiontype);
                        }
                        if (amount.equals("null")) {
                            Amount.setVisibility(View.GONE);
                            Amount_head.setVisibility(View.GONE);
                        } else {
                            Amount.setText(amount);
                        }
                        if (adjustment_amount.equals("null")) {
                            AdjustmentAmount.setVisibility(View.GONE);
                            AdjustmentAmount_head.setVisibility(View.GONE);
                        } else {
                            AdjustmentAmount.setText(adjustment_amount);
                        }
                        if (net_amount.equals("null")) {
                            NetAmount.setVisibility(View.GONE);
                            NetAmount_head.setVisibility(View.GONE);
                        } else {
                            NetAmount.setText(net_amount);
                        }
                        if (notes.equals("null")) {
                            Notes.setVisibility(View.GONE);
                            Notes_head.setVisibility(View.GONE);
                        } else {
                            Notes.setText(notes);
                        }
                        if (pdfDocument.isEmpty()) {
                            pdfView.setVisibility(View.GONE);
                            pdf_head.setVisibility(View.GONE);
                        } else {
                            String finalPdfDocument = pdfDocument;
                            pdfView.setOnClickListener(view -> {
                               //
                                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(finalPdfDocument));
                                startActivity(browserIntent);
                            });
                        }
                    }
                } else {
                    Toast.makeText(getApplicationContext(), jsonObject.getJSONObject("data").getString("message"), Toast.LENGTH_LONG).show();
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        Ced_MultiVendor_ConnectionDetector connectionDetector = new Ced_MultiVendor_ConnectionDetector(getApplicationContext());
        if (connectionDetector.isConnectingToInternet()) {

            if (connectionDetector.isConnectingToInternet()) {

                setname(session.getvendorname());
                setprofilepic(session.getvendorpic());
                changetitle("Transactions");
                //     invalidateOptionsMenu();
                super.onResume();

            } else {
                Intent intent = new Intent(getApplicationContext(), Ced_MultiVendor_NoInternetconnection.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                overridePendingTransition(R.anim.ced_multivendor_slide_in, R.anim.ced_multivendor_slide_out);
                super.onResume();
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private String convertToEgyptTimeZone(String dateString) {
         try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
            dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
            Date date = dateFormat.parse(dateString);
             dateFormat.setTimeZone(TimeZone.getDefault()); //getTimeZone("Africa/Cairo")
            dateFormat.applyPattern("yyyy-MM-dd HH:mm:ss");
           return dateFormat.format(date);
        }catch (Exception e){
         return dateString;
        }
    }

}
