package magentoegypt.locafy.navigation_drawer.utils;

import static magentoegypt.locafy.addons.advance_Report.appBase.AppConstant.ACTIVITY_NAME;
import static magentoegypt.locafy.addons.advance_Report.appBase.AppConstant.OUT_OF_STOCK_PRODUCT_ACTIVITY;
import static magentoegypt.locafy.addons.advance_Report.appBase.AppConstant.PAYMENT_REPORT_ACTIVITY;
import static magentoegypt.locafy.addons.advance_Report.appBase.AppConstant.PRODUCT_SALES_ACTIVITY;
import static magentoegypt.locafy.addons.advance_Report.appBase.AppConstant.PRODUCT_VIEWS_ACTIVITY;
import static magentoegypt.locafy.addons.advance_Report.appBase.AppConstant.RETURN_REPORT_ACTIVITY;
import static magentoegypt.locafy.addons.advance_Report.appBase.AppConstant.SOLD_PRODUCT_ACTIVITY;

import android.app.Activity;
import android.content.Intent;
import android.widget.Toast;

import magentoegypt.locafy.R;
import magentoegypt.locafy.addons.advance_Report.AdvanceReportHomeActivity;
import magentoegypt.locafy.addons.faq.FaqListScreen;
import magentoegypt.locafy.addons.messaging.admin_inbox.Admin_Inbox;
import magentoegypt.locafy.addons.messaging.customer_inbox.Customer_Inbox;
import magentoegypt.locafy.addons.mvp_RequestForQuote.Ced_MultiVendor_VendorQuoteManagement;
import magentoegypt.locafy.addons.mvp_RequestForQuote.Ced_MultiVendor_Vendor_ViewPO_List;
import magentoegypt.locafy.addons.vendor_member_ship_plans.Ced_Multivendor_Membership_Plan;
import magentoegypt.locafy.addons.vendor_member_ship_plans.Ced_Multivendor_Plan_History;
import magentoegypt.locafy.addons.vendor_pincode_checker.AssignedPincode.mvp_AssignedPincodeList;
import magentoegypt.locafy.addons.vendor_pincode_checker.PincodeList.mvp_PincodeListing;
import magentoegypt.locafy.addons.vendor_rma.Ced_MultiVendor_ManageRMARequest;
import magentoegypt.locafy.addons.vendor_select_n_sell.Add_Product_list;
import magentoegypt.locafy.base_app.StoreSelection;
import magentoegypt.locafy.manage_attribute.Ced_MultiVendor_VendorProductAttribute;
import magentoegypt.locafy.manage_attribute.Ced_MultiVendor_VendorProductAttributeSet;
import magentoegypt.locafy.manage_orders.Ced_MultiVendor_CreditMemoListing;
import magentoegypt.locafy.manage_orders.Ced_MultiVendor_InvoiceListing;
import magentoegypt.locafy.manage_orders.Ced_MultiVendor_ManageOrdersList;
import magentoegypt.locafy.manage_orders.Ced_MultiVendor_OrdersList;
import magentoegypt.locafy.manage_orders.Ced_MultiVendor_ShipmentListing;
import magentoegypt.locafy.manage_products_section.Ced_MultiVendor_AddVendorProduct;
import magentoegypt.locafy.manage_products_section.Ced_MultiVendor_ManageProducts;
import magentoegypt.locafy.vendor_cms.Ced_MultiVendor_Cms.Ced_MultiVendor_CmsListing;
import magentoegypt.locafy.vendor_cms.Ced_MultiVendor_StaticBlock.Ced_MultiVendor_BlockListing;
import magentoegypt.locafy.vendor_dashboard.Ced_MultiVendor_VendorDashboard;
import magentoegypt.locafy.vendor_deals.Ced_MultiVendor_Create_and_Edit_deal;
import magentoegypt.locafy.vendor_deals.Ced_MultiVendor_Deal_Settings;
import magentoegypt.locafy.vendor_profile_section.Ced_MultiVendor_VendorProfile;
import magentoegypt.locafy.vendor_reports_section.Ced_MultiVendor_OrderReport;
import magentoegypt.locafy.vendor_reports_section.Ced_MultiVendor_ProductReport;
import magentoegypt.locafy.vendor_settings.Ced_MultiVendor_Settings;
import magentoegypt.locafy.vendor_settings.Ced_MultiVendor_ShippingMethod;
import magentoegypt.locafy.vendor_settings.Ced_MultiVendor_ShippingSetting;
import magentoegypt.locafy.vendor_transaction.Ced_MultiVendor_ListTransaction;
import magentoegypt.locafy.vendor_transaction.mvp_adv_req_payments;

public class ScreenNavigation {
    String key;
    Activity activity;

    public ScreenNavigation(String key, Activity activity) {
        this.key = key;
        this.activity = activity;
        doNavigation();
    }

    private void doNavigation() {
        Intent intent = null;
        switch (key) {
            case "store_switcher":
                intent = new Intent(activity, StoreSelection.class);
                break;
            case "manage_rma_request":
                intent = new Intent(activity, Ced_MultiVendor_ManageRMARequest.class);
                break;
            case "vendor_dashboard":
                intent = new Intent(activity, Ced_MultiVendor_VendorDashboard.class);
                break;
            case "vendor_profile":
                intent = new Intent(activity, Ced_MultiVendor_VendorProfile.class);
                break;
            case "vendor_new_product":
                intent = new Intent(activity, Ced_MultiVendor_AddVendorProduct.class);
                break;
            case "vendor_products":
                intent = new Intent(activity, Ced_MultiVendor_ManageProducts.class);
                break;
            case "manage_product_attribute":
                intent = new Intent(activity, Ced_MultiVendor_VendorProductAttribute.class);
                break;
            case "manage_attribute_set":
                intent = new Intent(activity, Ced_MultiVendor_VendorProductAttributeSet.class);
                break;
            case "csorder_manage_order":
                intent = new Intent(activity, Ced_MultiVendor_ManageOrdersList.class);
                break;
            case "csorder_cancel_order":
                intent = new Intent(activity, Ced_MultiVendor_ManageOrdersList.class);
                break;
            case "csorder_manage_invoice":
                intent = new Intent(activity, Ced_MultiVendor_InvoiceListing.class);
                break;
            case "csorder_manage_shipment":
                intent = new Intent(activity, Ced_MultiVendor_ShipmentListing.class);
                break;
            case "csorder_manage_creditmemo":
                intent = new Intent(activity, Ced_MultiVendor_CreditMemoListing.class);
                break;
            case "vendor_payment_settings":
                intent = new Intent(activity, Ced_MultiVendor_Settings.class);
                break;
            case "vendor_shipping_settings":
                intent = new Intent(activity, Ced_MultiVendor_ShippingSetting.class);
                break;
            case "vendor_shipping_methods":
                intent = new Intent(activity, Ced_MultiVendor_ShippingMethod.class);
                break;
            case "csmessaging_vendor_admin":
                intent = new Intent(activity, Admin_Inbox.class);
                break;
            case "csmessaging_vendor_customer":
                intent = new Intent(activity, Customer_Inbox.class);
                break;
            case "cms_block":
                intent = new Intent(activity, Ced_MultiVendor_BlockListing.class);
                break;
            case "vendor_orders":
                intent = new Intent(activity, Ced_MultiVendor_OrdersList.class);
                break;
            case "vendor_productfaq":
                intent = new Intent(activity, FaqListScreen.class);
                break;
            case "membership-plans":
                intent = new Intent(activity, Ced_Multivendor_Membership_Plan.class);
                break;
            case "plan-history":
                intent = new Intent(activity, Ced_Multivendor_Plan_History.class);
                break;
            case "manage_quotes":
                intent = new Intent(activity, Ced_MultiVendor_VendorQuoteManagement.class);
                break;
            case "manage_po":
                intent = new Intent(activity, Ced_MultiVendor_Vendor_ViewPO_List.class);
                break;
            case "order_reports":
                intent = new Intent(activity, Ced_MultiVendor_OrderReport.class);
                break;
            case "product_reports":
                intent = new Intent(activity, Ced_MultiVendor_ProductReport.class);
                break;
            case "vendor_csadvtransaction_request":
                intent = new Intent(activity, mvp_adv_req_payments.class);
                break;
            case "vendor_csadvtransaction_all":
                intent = new Intent(activity, Ced_MultiVendor_ListTransaction.class);
                break;
            case "create":
                intent = new Intent(activity, Ced_MultiVendor_Create_and_Edit_deal.class);
                break;
            case "setting":
                intent = new Intent(activity, Ced_MultiVendor_Deal_Settings.class);
                break;
            case "cms_page":
                intent = new Intent(activity, Ced_MultiVendor_CmsListing.class);
                break;
            case "pincode-lists":
                intent = new Intent(activity, mvp_PincodeListing.class);
                break;
            case "assigned-pincode":
                intent = new Intent(activity, mvp_AssignedPincodeList.class);
                break;
            case "simple":
            case "virtual":
            case "bundle":
            case "grouped":
            case "configurable":
                intent = new Intent(activity, Add_Product_list.class);
                break;
            case "vendor_csreport_productsales":
                intent = new Intent(activity, AdvanceReportHomeActivity.class);
                intent.putExtra(ACTIVITY_NAME, PRODUCT_SALES_ACTIVITY);
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                break;
            case "vendor_csreport_productviews":
                intent = new Intent(activity, AdvanceReportHomeActivity.class);
                intent.putExtra(ACTIVITY_NAME, PRODUCT_VIEWS_ACTIVITY);
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                break;
            case "vendor_csreport_paymentreport":
                intent = new Intent(activity, AdvanceReportHomeActivity.class);
                intent.putExtra(ACTIVITY_NAME, PAYMENT_REPORT_ACTIVITY);
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                break;
            case "vendor_csreport_outofstock":
                intent = new Intent(activity, AdvanceReportHomeActivity.class);
                intent.putExtra(ACTIVITY_NAME, OUT_OF_STOCK_PRODUCT_ACTIVITY);
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                break;
            case "vendor_csreport_soldproducts":
                intent = new Intent(activity, AdvanceReportHomeActivity.class);
                intent.putExtra(ACTIVITY_NAME, SOLD_PRODUCT_ACTIVITY);
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                break;
            case "vendor_csreport_returnreport":
                intent = new Intent(activity, AdvanceReportHomeActivity.class);
                intent.putExtra(ACTIVITY_NAME, RETURN_REPORT_ACTIVITY);
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                break;
            default:
                Toast.makeText(activity, "invalid selection", Toast.LENGTH_SHORT).show();

        }

        if (null == intent)
            return;

        activity.startActivity(intent);
        activity.overridePendingTransition(R.anim.ced_multivendor_slide_in, R.anim.ced_multivendor_slide_out);

    }
}
