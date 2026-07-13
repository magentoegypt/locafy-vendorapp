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

package magentoegypt.locafy.vendor_session;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import magentoegypt.locafy.base_app.Ced_MultiVendor_VendorFunctionalityList;
import magentoegypt.locafy.vendor_login_section.Ced_Multivendor_New_Login;
import magentoegypt.locafy.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by developer on 9/21/2015.
 */
public class Ced_MultiVendor_VendorSessionManagement {

    public static final String Key_Email = "Email";
    public static final String Key_HAsh = "hash";
    public static final String KEY_SESSIONID = "Sessionid";
    public static final String KEY_CUSTOMER_ID = "CustomerId";
    public static final String KEY_Language = "language";
    public static final String KEY_STORE_ID = "storeid";//
    public static final String KEY_SAVE_CREATE_DEAL_STATUS = "saveCreateDealStatus";
    public static final String KEY_INSTANCE = "instance";
    public static final String KEY_CREDITMEMO_ITEM = "creditmemo_item";
    public static final String FCM_TOKEN = "fcm_token";
    private static final String PREF_NAME = "Login_Pref";
    private static final String Is_Login = "IS_Logged_In";
    private static final String REVIEW_STATUS_FOR_FILTER = "REVIEW_STATUS_FOR_FILTER";
    private static final String REVIEW_ACCESS_TYPE_FOR_FILTER = "REVIEW_ACCESS_TYPE_FOR_FILTER";
    private static final String REVIEW_STORE_FOR_FILTER = "REVIEW_STORE_FOR_FILTER";
    private static final String Base_Url = "base_url";
    private static final String VENDOR_ACCOUNT_UNDER_REVIEW = "account_under_review";
    private static final String LONGITUDE = "longitude";
    private static final String LATITUDE = "latitude";
    private static final String LOCATION = "location";
    private static final String CURRENCY_SYMBOL ="CURRENCYSYMBOL" ;
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    int Private_Mode = 0;
    Context con;
    String counter;
    String customer_id;
    String language;
    String storeid;
    Boolean instance = false;
    Ced_MultiVendor_VendorFunctionalityList functionalityList;
    List creditmemolist;

    public Ced_MultiVendor_VendorSessionManagement(Context context) {
        this.con = context;
        pref = con.getSharedPreferences(PREF_NAME, Private_Mode);
        editor = pref.edit();
        functionalityList = new Ced_MultiVendor_VendorFunctionalityList(context);
        creditmemolist = new ArrayList();
    }

    public void createLoginSession(String hash, String email) {
        // Storing login value as TRUE
        editor.putBoolean(Is_Login, true);
        // Storing name in pref
        editor.putString(Key_HAsh, hash);
        // Storing email in pref
        editor.putString(Key_Email, email);
        // commit changes
        editor.commit();
        Log.i("hello", "saved value: " + editor);
    }

    public HashMap<String, String> getUserDetails() {
        HashMap<String, String> user = new HashMap<String, String>();
        user.put(Key_HAsh, pref.getString(Key_HAsh, "213"));
        user.put(Key_Email, pref.getString(Key_Email, "213"));
        return user;
    }

    public boolean isLoggedIn() {
        return pref.getBoolean(Is_Login, false);
    }

    public void logoutUser() {
        // Clearing all data from Shared Preferences
        Log.i("logout", "called");
        editor.remove(Key_Email);
        editor.remove(Key_HAsh);
        editor.remove(KEY_CUSTOMER_ID);
        editor.remove("customer");
        editor.remove("vendor_name");
        editor.remove(Is_Login);
        editor.remove("vendor_picurl");

        editor.apply();
        editor.commit();

        /*functionalityList.clearedaddons();*/
        // After logout redirect user to Loing Activity
        Intent i = new Intent(con, Ced_Multivendor_New_Login.class);
        // Closing all the Activities
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        // Add new Flag to start new Activity
        //  i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        // Staring Login Activity
        con.startActivity(i);

    }

    public void saveSessionID(String id) {
        counter = id;
        editor.putString(KEY_SESSIONID, id);
        Log.i("sessionid", id);
        editor.commit();

    }

    public String getSessionID() {
        return pref.getString(KEY_SESSIONID, counter);

    }

    public void clearSessionID() {
        pref.edit().remove(KEY_SESSIONID);
    }

    public void saveVendorId(String cst_id) {
        editor.putString(KEY_CUSTOMER_ID, cst_id);
        editor.commit();
    }

    public String getVendorid() {
        return pref.getString(KEY_CUSTOMER_ID, customer_id);
    }

    public void ClearVendorId() {
        pref.edit().remove(KEY_CUSTOMER_ID);
        editor.commit();
    }

    public void saveCustomerId(String cst_id) {
        editor.putString("customer", cst_id);
        editor.commit();
    }

    public String getCustomerId() {
        return pref.getString("customer", null);

    }

    public void ClearCustomerId() {
        pref.edit().remove("customer");
        editor.commit();
    }

    public String getHahkey() {

        return pref.getString(Key_HAsh, null);
    }

    public void saveLanguageToLoad(String lang) {
        editor.putString(KEY_Language, lang);
        editor.commit();
    }

    public String getLanguageToLoad() {
        return pref.getString(KEY_Language, language);
    }

    public void saveStoreId(String store_id) {
        editor.putString(KEY_STORE_ID, store_id);
        editor.commit();
    }

    public void saveCreateDealStatus(String status) {
        editor.putString(KEY_SAVE_CREATE_DEAL_STATUS, status);
        editor.commit();
    }

    public String getCreateDealStatus() {
        return pref.getString(KEY_SAVE_CREATE_DEAL_STATUS, null);
    }


    public String getStoreId() {
        return pref.getString(KEY_STORE_ID, null);
    }

    public void saveStorelocale(String locale) {
        editor.putString("storelocale", locale);
        editor.commit();
    }

    public String getStoreLocale() {
        return pref.getString("storelocale", "eg");
    }

    public void saveUrlRun(Boolean instance) {
        editor.putBoolean(KEY_INSTANCE, instance);
        editor.commit();
    }

    public Boolean getUrlRun() {
        return pref.getBoolean(KEY_INSTANCE, instance);
    }

    public void savevendorname(String name) {
        editor.putString("vendor_name", name);
        editor.commit();
    }

    public String getvendorname() {
        return pref.getString("vendor_name", null);
    }

    public void savevendorpic(String picurl) {
        editor.putString("vendor_picurl", picurl);
        editor.commit();
    }

    public String getvendorpic() {
        return pref.getString("vendor_picurl", null);
    }

    public void registrationenable(boolean enable) {
        editor.putBoolean("regenable", enable);
        editor.commit();
    }

    public boolean isregistraionenable() {
        return pref.getBoolean("regenable", false);
    }

    public void shopurl(boolean enable) {
        editor.putBoolean("shopurl", enable);
        editor.commit();
    }

    public boolean isshopurlenable() {
        return pref.getBoolean("shopurl", false);
    }

    public void addItemtocreditmemo(List list) {
        editor.putString(KEY_CREDITMEMO_ITEM, list.toString());
        editor.commit();
    }

    public String getItemtocreditmemo() {
        return pref.getString(KEY_CREDITMEMO_ITEM, "" + creditmemolist);
    }

    public void clearitemincreditmemo() {
        editor.remove(KEY_CREDITMEMO_ITEM).commit();
    }

    public void saveSubVendorId(String a) {
        editor.putString("sub_vendor_id", a);
        editor.commit();
    }

    public String getSubVendorId() {
        return pref.getString("sub_vendor_id", "");

    }

    public void ClearSubVendor() {
        pref.edit().remove("sub_vendor_id");
        editor.commit();
    }

    public void DeviceToken(String token) {
        editor.putString(FCM_TOKEN, token);
        editor.commit();
    }

    public String getDeviceToken() {
        return pref.getString(FCM_TOKEN, "");
    }

    public void saveReviewStatusJSONObject(String status_obj) {
        editor.putString(REVIEW_STATUS_FOR_FILTER, status_obj);
        editor.commit();
    }

    public String getReviewStatusJSONObject() {
        return pref.getString(REVIEW_STATUS_FOR_FILTER, "");
    }

    public void saveReviewStoreJSONObject(String obj) {
        editor.putString(REVIEW_STORE_FOR_FILTER, obj);
        editor.commit();
    }

    public String getReviewStoreJSONObject() {
        return pref.getString(REVIEW_STORE_FOR_FILTER, "");
    }

    public void saveReviewAccessTypeJSONObject(String obj) {
        editor.putString(REVIEW_ACCESS_TYPE_FOR_FILTER, obj);
        editor.commit();
    }

    public String getReviewAccessTypeJSONObject() {
        return pref.getString(REVIEW_ACCESS_TYPE_FOR_FILTER, "");
    }

    public void saveBaseUrl(String base_url) {
        editor.putString(Base_Url, base_url);
        editor.commit();
    }

    public String getBase_Url() {
       // return  "https://locafy.market/";
        if (getStoreLocale() != null){
            return  "https://locafy.market/"+getStoreLocale()+"/";
        }else{
            return  "https://locafy.market/eg/";
        }
      //  return pref.getString(Base_Url, con.getResources().getString(R.string.base_url));
    }

    public void saveLongitude(String longitude) {
        editor.putString(LONGITUDE, longitude);
        editor.commit();
    }

    public String getLongitude() {
        return pref.getString(LONGITUDE, "");
    }

    public void saveLatitude(String latitude) {
        editor.putString(LATITUDE, latitude);
        editor.commit();
    }

    public String getLatitude() {
        return pref.getString(LATITUDE, "");
    }

    public void saveLocation(String location) {
        editor.putString(LOCATION, location);
        editor.commit();
    }

    public String getLocation() {
        return pref.getString(LOCATION, "");
    }


    public void saveCurrencySymbol(String currency_symbol) {
        editor.putString(CURRENCY_SYMBOL, currency_symbol);
        editor.commit();
    }

    public String getCurrencySymbol() {
        return pref.getString(CURRENCY_SYMBOL, "");
    }

}

