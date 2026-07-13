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


package magentoegypt.locafy.base_app;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

public class Ced_MultiVendor_VendorFunctionalityList {
    public static final String Extension = "Extension";
    private static final String PREF_NAME = "FUCTIONALITY_LIST";
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    int Private_Mode = 0;
    Context con;

    public Ced_MultiVendor_VendorFunctionalityList(Context context) {
        this.con = context;
        pref = con.getSharedPreferences(PREF_NAME, Private_Mode);
        editor = pref.edit();
    }

    public void Extension(boolean extension) {
        editor.putBoolean(Extension, extension);
        editor.commit();
    }

    public boolean getExtensionAddon() {
        return pref.getBoolean(Extension, false);
    }

    public void Vendor_Profile(boolean extension) {
        Log.i("HERE", "profile saved " + extension);
        editor.putBoolean("VendorProfile", extension);
        editor.commit();
    }

    public boolean getVendor_Profile() {
        Log.i("HERE", "get vendor profile " + pref.getBoolean("VendorProfile", false));
        return pref.getBoolean("VendorProfile", false);
    }

    public void Vendor_Deals(boolean extension) {
        editor.putBoolean("Vendor_Deals", extension);
        editor.commit();
    }

    public boolean getVendor_Deals() {
        return pref.getBoolean("Vendor_Deals", false);
    }

    public void Vendor_Advance_Transaction(boolean extension) {
        editor.putBoolean("Vendor_Advance_Transaction", extension);
        editor.commit();
    }

    public boolean getVendor_Advance_Transaction() {
        return pref.getBoolean("Vendor_Advance_Transaction", false);
    }

    public void New_Product(boolean extension) {
        editor.putBoolean("New_Product", extension);
        editor.commit();
    }

    public boolean getNew_Product() {
        return pref.getBoolean("New_Product", false);
    }

    public void Manage_Products(boolean extension) {
        editor.putBoolean("Manage_Products", extension);
        editor.commit();
    }

    public boolean getManage_Products() {
        return pref.getBoolean("Manage_Products", false);
    }

    public void Orders(boolean extension) {
        editor.putBoolean("Orders", extension);
        editor.commit();
    }

    public boolean getOrders() {
        return pref.getBoolean("Orders", false);
    }

    public void Request_Payments(boolean extension) {
        editor.putBoolean("Request_Payments", extension);
        editor.commit();
    }

    public boolean getRequest_Payments() {
        return pref.getBoolean("Request_Payments", false);
    }

    public void View_Transactions(boolean extension) {
        editor.putBoolean("View_Transactions", extension);
        editor.commit();
    }

    public boolean getView_Transactions() {
        return pref.getBoolean("View_Transactions", false);
    }

    public void Order_Reports(boolean extension) {
        editor.putBoolean("Order_Reports", extension);
        editor.commit();
    }

    public boolean getOrder_Reports() {
        return pref.getBoolean("Order_Reports", false);
    }

    public void Product_Reports(boolean extension) {
        editor.putBoolean("Product_Reports", extension);
        editor.commit();
    }

    public boolean getProduct_Reports() {
        return pref.getBoolean("Product_Reports", false);
    }

    public void Transaction_Settings(boolean extension) {
        editor.putBoolean("Transaction_Settings", extension);
        editor.commit();
    }

    public boolean getTransaction_Settings() {
        return pref.getBoolean("Transaction_Settings", false);
    }

    public void Vendor_Product_Attribute(boolean extension) {
        editor.putBoolean("Vendor_Product_Attribute", extension);
        editor.commit();
    }

    public void Manage_Orders(boolean extension) {
        editor.putBoolean("Manage_Orders", extension);
        editor.commit();
    }

    public boolean getManage_Orders() {
        return pref.getBoolean("Manage_Orders", false);
    }

    public void Manage_Invoice(boolean extension) {
        editor.putBoolean("Manage_Invoice", extension);
        editor.commit();
    }

    public boolean getManage_Invoice() {
        return pref.getBoolean("Manage_Invoice", false);
    }

    public void Manage_Shipment(boolean extension) {
        editor.putBoolean("Manage_Shipment", extension);
        editor.commit();
    }

    public boolean getManage_Shipment() {
        return pref.getBoolean("Manage_Shipment", false);
    }

    public void Settings(boolean extension) {
        editor.putBoolean("Settings", extension);
        editor.commit();
    }

    public void Reports(boolean extension) {
        editor.putBoolean("Reports", extension);
        editor.commit();
    }

    public boolean get_Transactions() {
        return pref.getBoolean("Transactions", false);
    }

    public boolean get_Reports() {
        return pref.getBoolean("Reports", false);
    }

    public void Transactions(boolean extension) {
        editor.putBoolean("Transactions", extension);
        editor.commit();
    }

    public boolean get_Settings() {
        return pref.getBoolean("Settings", false);
    }

    public void Manage_Credit_Memo(boolean extension) {
        editor.putBoolean("Manage_Credit_Memo", extension);
        editor.commit();
    }

    public boolean getManage_Credit_Memo() {
        return pref.getBoolean("Manage_Credit_Memo", false);
    }

    public boolean getVendor_Product_Attribute() {
        return pref.getBoolean("Vendor_Product_Attribute", false);
    }

    public void Manage_Attribute(boolean extension) {
        editor.putBoolean("Manage_Attribute", extension);
        editor.commit();
    }

    public boolean getManage_Attribute() {
        return pref.getBoolean("Manage_Attribute", false);
    }

    public void Manage_Attribute_Set(boolean extension) {
        editor.putBoolean("Manage_Attribute_Set", extension);
        editor.commit();
    }

    public boolean getManage_Attribute_Set() {
        return pref.getBoolean("Manage_Attribute_Set", false);
    }

    public void Shipping_Settings(boolean extension) {
        editor.putBoolean("Shipping_Settings", extension);
        editor.commit();
    }

    public boolean getShipping_Settings() {
        return pref.getBoolean("Shipping_Settings", false);
    }

    public void Shipping_Methods(boolean extension) {
        editor.putBoolean("Shipping_Methods", extension);
        editor.commit();
    }

    public boolean getShipping_Methods() {
        return pref.getBoolean("Shipping_Methods", false);
    }

    public void clearedaddons() {
        editor.clear();
        editor.commit();
    }

    public void Social_Login(boolean extention) {
        editor.putBoolean("Social_Login", extention);
        editor.commit();
    }

    public boolean getSocialLogin() {
        return pref.getBoolean("Social_Login", false);
    }

    public void ProductAddon(boolean extention) {
        editor.putBoolean("Product_Addon", extention);
        editor.commit();
    }

    public boolean getProductAddon() {
        return pref.getBoolean("Product_Addon", false);
    }

    public void SimpleProduct(boolean extention) {
        editor.putBoolean("Simple_Product", extention);
    }

    public void VirtualProduct(boolean extention) {
        editor.putBoolean("Virtual_Product", extention);
    }

    public void BundleProduct(boolean extention) {
        editor.putBoolean("Bundle_Product", extention);
    }

    public void GroupedProduct(boolean extention) {
        editor.putBoolean("Grouped_Product", extention);
    }

    public void ConfigurableProduct(boolean extention) {
        editor.putBoolean("Configurable_Product", extention);
    }

    public boolean getSimpleProductAddon() {
        return pref.getBoolean("Simple_Product", false);
    }

    public boolean getVirtualProductAddon() {
        return pref.getBoolean("Virtual_Product", false);
    }

    public boolean getBundleProductAddon() {
        return pref.getBoolean("Bundle_Product", false);
    }

    public boolean getGroupedProductAddon() {
        return pref.getBoolean("Grouped_Product", false);
    }

    public boolean getConfigurableProductAddon() {
        return pref.getBoolean("Configurable_Product", false);
    }

    public void RequestForQuote(boolean rfq) {
        editor.putBoolean("RequestForQuote", rfq);
    }

    public boolean getRequestForQuote() {
        return pref.getBoolean("RequestForQuote", false);
    }

    public void RMA(boolean rma) {
        editor.putBoolean("RMA", rma);
    }

    public boolean getRMA() {
        return pref.getBoolean("RMA", false);
    }

    public void Messaging(boolean messaging) {
        editor.putBoolean("Messaging", messaging);
    }

    public boolean getMessaging() {
        return pref.getBoolean("Messaging", false);
    }

    public void MemberShipPlans(boolean membershipplan) {
        editor.putBoolean("MemberShipPlans", membershipplan);
    }

    public boolean getMemberShipPlans() {
        return pref.getBoolean("MemberShipPlans", false);
    }

    public void MemberShipPlan(boolean membershipplan) {
        editor.putBoolean("MemberShipPlan", membershipplan);
    }

    public boolean MemberShip_Plan() {
        return pref.getBoolean("MemberShipPlan", false);
    }

    public void PlanHistory(boolean planhistory) {
        editor.putBoolean("planhistory", planhistory);
    }

    public boolean Plan_History() {
        return pref.getBoolean("planhistory", false);
    }

    public void OrderReports(boolean orderreports) {
        editor.putBoolean("orderreports", orderreports);
    }

    public boolean Order_Reports() {
        return pref.getBoolean("orderreports", false);
    }

    public void ProductsReports(boolean productsreports) {
        editor.putBoolean("productsreport", productsreports);
    }

    public boolean Products_Report() {
        return pref.getBoolean("productsreport", false);
    }

    public void ManageVendorCMS(boolean managevendorcms) {
        editor.putBoolean("managevendorcms", managevendorcms);
    }

    public boolean Manage_Vendor_CMS() {
        return pref.getBoolean("managevendorcms", false);
    }

    public void ManageStaticBlocks(boolean managestaticblocks) {
        editor.putBoolean("managestaticblocks", managestaticblocks);
    }

    public boolean Manage_Static_Blocks() {
        return pref.getBoolean("managestaticblocks", false);
    }

    public void AddProduct(boolean addproduct) {
        editor.putBoolean("addproduct", addproduct);
    }

    public boolean Add_Product() {
        return pref.getBoolean("addproduct", false);
    }

    public void ProductList(boolean productlist) {
        editor.putBoolean("productlist", productlist);
    }

    public boolean Product_List() {
        return pref.getBoolean("productlist", false);
    }

    public void Promotions(boolean promotions) {
        editor.putBoolean("Promotions", promotions);
    }

    public boolean getPromotions() {
        return pref.getBoolean("Promotions", false);
    }

    public void AssoiciatedSubVendors(boolean asv) {
        editor.putBoolean("AssoiciatedSubVendors", asv);
        editor.commit();
    }

    public boolean getAssoiciatedSubVendors() {
        return pref.getBoolean("AssoiciatedSubVendors", false);
    }

    public void StorePickup(boolean storepickup) {
        editor.putBoolean("Store_Pickup", storepickup);
    }

    public boolean getStorePickup() {
        return pref.getBoolean("Store_Pickup", false);
    }

    public void AdvanceReport(boolean advancereport) {
        editor.putBoolean("AdvanceReport", advancereport);
    }

    public boolean getAdvanceReport() {
        return pref.getBoolean("AdvanceReport", false);
    }

    public void VedndorCms(boolean vendorcms) {
        editor.putBoolean("VedndorCms", vendorcms);
    }

    public boolean getVedndorCms() {
        return pref.getBoolean("VedndorCms", false);
    }

    public void SelectAndSell(boolean selectandsell) {
        editor.putBoolean("SelectAndSell", selectandsell);
    }

    public boolean getSelectAndSell() {
        return pref.getBoolean("SelectAndSell", false);
    }

    public void CatalogPriceRule(boolean catalogpricerule) {
        editor.putBoolean("catalogpricerule", catalogpricerule);
    }

    public boolean Catalog_Price_Rule() {
        return pref.getBoolean("catalogpricerule", false);
    }

    public void ShoppingCartPriceRule(boolean shoppingcartpricerule) {
        editor.putBoolean("shoppingcartpricerule", shoppingcartpricerule);
    }

    public boolean Shopping_Cart_Price_Rule() {
        return pref.getBoolean("shoppingcartpricerule", false);
    }

    public void AuctionApi(boolean auction) {
        editor.putBoolean("Auction_Set", auction);
        editor.commit();
    }

    public boolean get_Auction_Set() {
        return pref.getBoolean("Auction_Set", false);
    }

    public void POApi(boolean poApi) {
        editor.putBoolean("POApi", poApi);
    }

    public boolean getPOApi() {
        return pref.getBoolean("POApi", false);
    }

    public void FavouriteSeller(boolean fav_seller) {
        editor.putBoolean("FavouriteSeller", fav_seller);
        editor.commit();
    }

    public boolean getFavouriteSeller() {
        return pref.getBoolean("FavouriteSeller", true);
    }

    public void PincodeChecker(boolean pincode) {
        editor.putBoolean("PincodeChecker", pincode);
        editor.commit();
    }

    public boolean getPincodeChecker() {
        return pref.getBoolean("PincodeChecker", true);
    }

    public void ProductRatingAndReview(boolean rating) {
        editor.putBoolean("ProductRatingAndReview", rating);
        editor.commit();
    }

    public boolean getRatingAndReview() {
        return pref.getBoolean("ProductRatingAndReview", true);
    }
}
