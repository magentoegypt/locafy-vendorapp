/*
 * Copyright/**
 *          * CedCommerce
 *           *
 *           * NOTICE OF LICENSE
 *           *
 *           * This source file is subject to the End User License Agreement (EULA)
 *           * that is bundled with this package in the file LICENSE.txt.
 *           * It is also available through the world-wide-web at this URL:
 *           * http://cedcommerce.com/license-agreement.txt
 *           *
 *           * @category  Ced
 *           * @package   MageNative
 *           * @author    CedCommerce Core Team <connect@cedcommerce.com >
 *           * @copyright Copyright CEDCOMMERCE (http://cedcommerce.com/)
 *           * @license      http://cedcommerce.com/license-agreement.txt
 *
 */
package magentoegypt.locafy.api_request_response_section;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import magentoegypt.locafy.base_app.Ced_MultiVendor_VendorFunctionalityList;
import magentoegypt.locafy.R;
import magentoegypt.locafy.base_app.UtilityMethods;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

public class RestNotificatioRequest extends AsyncTask<String, String, String> {

    Ced_MultiVendor_VendorFunctionalityList functionalityList;
    public AsyncResponse delegate = null;
    String req = "";
    String data;
    boolean connect = true;
    int responseCode;
    String url;
    JSONObject data_post;
    HashMap<String, String> params;
    Context c;
    private static final String TAG = "RestNotificatioRequest";

    public RestNotificatioRequest(AsyncResponse cedAsyncResponse, Context context, String RequestMethod, HashMap<String, String> postparam) {

        delegate = cedAsyncResponse;
        req = RequestMethod;
        params = postparam;
        Log.i("REposnse", "" + data);
        c = context;
        functionalityList = new Ced_MultiVendor_VendorFunctionalityList(c);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(String... params) {
        String json = "";
        Log.i("REposnse", "" + params[0]);
        url = params[0];
        json = ClientPost(params[0]);
        Log.i("REposnse", "" + json);

        return json;


    }

    @Override
    protected void onPostExecute(String av) {
        super.onPostExecute(av);
        if (connect) {

            JSONObject jsonObject = null;
            try {
                jsonObject = new JSONObject(av);
                if (jsonObject.getString("success").equalsIgnoreCase("true")) {
                    delegate.processFinish(jsonObject);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }


    public String ClientPost(String url) {
        if (c.getResources().getString(R.string.Enable_Log).equals("yes")) {
            Log.i("REposnse", "" + url);
        }
        URL url1;
        String response = "";
        try {
            url1 = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) url1.openConnection();

            conn.setReadTimeout(1500000);
            conn.setConnectTimeout(1500000);
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setRequestProperty("Mobiconnectheader", c.getResources().getString(R.string.header));
           // conn.setRequestProperty("Authorization", UtilityMethods.getAuthData());
            OutputStream os = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(os, "UTF-8"));
            writer.write(getPostDataString(params));
            writer.flush();
            writer.close();
            os.close();
            int responseCode = conn.getResponseCode();
            if (c.getResources().getString(R.string.Enable_Log).equals("yes")) {
                Log.i("REposnse", "" + responseCode);
            }
            if (responseCode == HttpsURLConnection.HTTP_OK) {
                String line;
                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                while ((line = br.readLine()) != null) {
                    response += line;

                }
                functionalityList.Extension(true);
            } else {
                functionalityList.Extension(false);
                connect = false;
                response = "";
            }
        } catch (Exception e) {

            if (c.getResources().getString(R.string.Enable_Log).equals("yes")) {
                Log.i("exceptionsocket", "" + e.getMessage());
                e.printStackTrace();
            }
            connect = false;
        }
        if (c.getResources().getString(R.string.Enable_Log).equals("yes")) {
            Log.i("REposnse", "" + response);
        }
        return response;
    }

    private String getPostDataString(HashMap<String, String> params) throws UnsupportedEncodingException {
        StringBuilder result = new StringBuilder();
        boolean first = true;
        for (Map.Entry<String, String> entry : params.entrySet()) {
            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
        }
        if (c.getResources().getString(R.string.Enable_Log).equals("yes")) {
            Log.i("REposnse", result.toString());
        }

        return result.toString();
    }
}
