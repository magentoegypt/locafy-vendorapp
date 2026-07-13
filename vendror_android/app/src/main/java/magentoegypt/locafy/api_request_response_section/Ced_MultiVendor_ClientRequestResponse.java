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

package magentoegypt.locafy.api_request_response_section;

import static magentoegypt.locafy.base_app.UtilityMethods.getFileName;
import static magentoegypt.locafy.base_app.UtilityMethods.getMimeType;
import static magentoegypt.locafy.base_app.UtilityMethods.getRealPathFromURI;

import android.app.Activity;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import magentoegypt.locafy.R;
import magentoegypt.locafy.base_app.Ced_MultiVendor_VendorFunctionalityList;
import magentoegypt.locafy.base_app.UtilityMethods;
import magentoegypt.locafy.gallary.Image;
import magentoegypt.locafy.vendor_registration_section.new_registration.RegistrationDynamic;

import org.apache.http.HttpResponse;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.X509TrustManager;

public class Ced_MultiVendor_ClientRequestResponse extends AsyncTask<String, String, String> {

    static String facebookToken = "EAAXa7lItDpYBO84bpz10qjSzZAKcXe6YnUNgWEMsCarHKEUUOc7wFk3e4opjOESdKT1xGwguyV8yeuJu3jjfh3SvUeVOryl6jR4EZBRZAl4SUfwbgS2aGCipWTusU9tEyZBpPgjncTojB7dn6aocFw1wtQ0dnRIyLoPcdhSZAzc3ycQCVZA3L6ZBU5Blp7n2HX8LQVlTwg3fA3Ingb4";
    public AsyncResponse delegate;
    Ced_MultiVendor_VendorFunctionalityList functionalityList;
    Activity c;
    String req = "";
    HashMap<String, String> params;
    String activity = "";
    Ced_NewLoader cedNewLoader;
    boolean connect = true;
    String url;
    String post_rul = "";
    List<Uri> allFileUri;
    private boolean isNeedtoShowLoader = true;
    public Ced_MultiVendor_ClientRequestResponse(AsyncResponse asyncResponse, Activity context) {
        delegate = asyncResponse;
        c = context;
        req = "GET";
        functionalityList = new Ced_MultiVendor_VendorFunctionalityList(c);
    }

    public Ced_MultiVendor_ClientRequestResponse(AsyncResponse asyncResponse, Activity context, List<Uri> allFileUri) {
        delegate = asyncResponse;
        c = context;
        req = "FileUpload";
        this.allFileUri = allFileUri;
        functionalityList = new Ced_MultiVendor_VendorFunctionalityList(c);
    }

    public Ced_MultiVendor_ClientRequestResponse(AsyncResponse asyncResponse, Activity context, String RequestMethod, HashMap<String, String> postparam) {
        delegate = asyncResponse;
        c = context;
        req = RequestMethod;
        params = postparam;
        functionalityList = new Ced_MultiVendor_VendorFunctionalityList(c);
    }

    public Ced_MultiVendor_ClientRequestResponse(AsyncResponse asyncResponse, Activity context, String RequestMethod, HashMap<String, String> postparam,boolean isNeedtoShowLoader) {
        delegate = asyncResponse;
        c = context;
        req = RequestMethod;
        params = postparam;
        this.isNeedtoShowLoader = isNeedtoShowLoader;
        functionalityList = new Ced_MultiVendor_VendorFunctionalityList(c);
    }

    public Ced_MultiVendor_ClientRequestResponse(AsyncResponse asyncResponse, Activity context, String RequestMethod, HashMap<String, String> postparam, String comingfrom) {
        activity = comingfrom;
        delegate = asyncResponse;
        c = context;
        req = RequestMethod;
        params = postparam;
        functionalityList = new Ced_MultiVendor_VendorFunctionalityList(c);
    }

    public Ced_MultiVendor_ClientRequestResponse(AsyncResponse asyncResponse, Activity context, String comingfrom) {
        delegate = asyncResponse;
        c = context;
        req = "GET";
        functionalityList = new Ced_MultiVendor_VendorFunctionalityList(c);
        activity = comingfrom;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        try {
            if (activity.isEmpty() && c instanceof Activity && isNeedtoShowLoader) {
                Activity activityContext = (Activity) c;
                if (!activityContext.isFinishing() && !activityContext.isDestroyed()) {
                    cedNewLoader = new Ced_NewLoader(c);
                    cedNewLoader.show();
                }
            }
        }catch (Exception e){

        }
    }

    @Override
    protected String doInBackground(String... params) {
        String json = "";
        Log.i("REposnse", "" + params[0]);
        url = params[0];
        if (req.equals("GET")) {
            json = Client(params[0]);
        }else if (req.equals("FileUpload")) {
            json = MultipartUtilityV2(params[0]);
        } else {
            json = ClientPost(params[0], 0);
        }
        Log.i("REposnse", "" + json);
        return json;
    }

    @Override
    protected void onPostExecute(String av) {
        super.onPostExecute(av);
        if (connect) {
            try {
                if(!av.isEmpty()){
                    Object json = new JSONTokener(av).nextValue();
                    if (json instanceof JSONObject) {
                        JSONObject check_repo = new JSONObject(av);
                        if (check_repo.has("data")) {
                            Object data = check_repo.get("data");
                            if (data instanceof JSONObject)
                            {
                                JSONObject jsonObject = check_repo.getJSONObject("data");
                                if (jsonObject.has("logout")) {
                                    if (jsonObject.getBoolean("logout")) {
                                        c.sendBroadcast(new Intent().setAction("logout"));
                                    }
                                }
                            }
                        }
                    }
                }
                delegate.processFinish(av);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                if ((this.cedNewLoader != null) && this.cedNewLoader.isShowing() && isNeedtoShowLoader) {
                    this.cedNewLoader.dismiss();
                }
            } catch (final IllegalArgumentException e) {
                // Handle or log or ignore
            } catch (final Exception e) {
                // Handle or log or ignore
            } finally {
                this.cedNewLoader = null;
            }
        }
        else {
            post_rul = url;
         /*   Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        ClientPost(c.getResources().getString(R.string.base_url) + "vendorapi/index/test", 1);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            thread.start();*/
            try {
                if ((this.cedNewLoader != null) && this.cedNewLoader.isShowing() && isNeedtoShowLoader) {
                    this.cedNewLoader.dismiss();
                }
            } catch (final IllegalArgumentException e) {
                // Handle or log or ignore
            } catch (final Exception e) {
                // Handle or log or ignore
            } finally {
                this.cedNewLoader = null;
            }
            try {
                c.runOnUiThread(new Runnable() {
                    public void run() {
                   /* Intent intent = new Intent();
                    intent.setAction("error");
                    c.sendBroadcast(intent);*/
                        final Dialog listDialog = new Dialog(c, R.style.PauseDialog);
                        listDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        ((ViewGroup) ((ViewGroup) listDialog.getWindow().getDecorView()).getChildAt(0)).getChildAt(1).setBackgroundColor(c.getResources().getColor(R.color.AppTheme));
                        //listDialog.setTitle(c.getResources().getString(R.string.oops));
                        LayoutInflater li = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                        View v = li.inflate(R.layout.magenative_activity_no_module, null, false);
                        listDialog.setContentView(v);
                        listDialog.setCancelable(false);
                        TextView conti = (TextView) listDialog.findViewById(R.id.conti);
                        TextView cross = (TextView) listDialog.findViewById(R.id.cross);

                        cross.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                listDialog.dismiss();
                            }
                        });
                        conti.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                listDialog.dismiss();
                                if (req.equals("GET")) {
                                    Ced_ClientRequestResponse ced_requestwithoutLoader = new Ced_ClientRequestResponse(delegate, c);
                                    ced_requestwithoutLoader.execute(url);
                                } else {
                                    if (activity.isEmpty()) {
                                        Ced_ClientRequestResponse ced_requestwithoutLoader = new Ced_ClientRequestResponse(delegate, c, req, params);
                                        ced_requestwithoutLoader.execute(url);
                                    } else {
                                        Ced_ClientRequestResponse ced_requestwithoutLoader = new Ced_ClientRequestResponse(delegate, c, req, params, activity);
                                        ced_requestwithoutLoader.execute(url);
                                    }
                                }
                            }
                        });
                        if (listDialog != null) {
                            listDialog.show();
                        }
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    public String MultipartUtilityV2(String requestURL) {
        String response = "";
        // creates a unique boundary based on time stamp
        try {
            DataOutputStream request;
            HttpURLConnection httpConn;
            String boundary =  "*****";
            String crlf = "\r\n";
            String twoHyphens = "--";

            URL url = new URL(requestURL);
            httpConn = (HttpURLConnection) url.openConnection();
            httpConn.setUseCaches(false);
            httpConn.setDoOutput(true); // indicates POST method
            httpConn.setDoInput(true);

            httpConn.setRequestMethod("POST");
            httpConn.setRequestProperty("Connection", "Keep-Alive");
            httpConn.setRequestProperty("Cache-Control", "no-cache");
           // httpConn.setRequestProperty("Authorization", UtilityMethods.getAuthData());
            httpConn.setRequestProperty(
                    "Content-Type", "multipart/form-data;boundary=" + boundary);

            request =  new DataOutputStream(httpConn.getOutputStream());

            for(Uri uri:allFileUri) {
                String filePath = uri.getPath();//getRealPathFromURI(c, uri);
                System.out.println(uri.getPath());
                File uploadFile = new File(filePath);
                String filname =  getFileName(uri, c);
                String type = getMimeType(uri,c);
                request.writeBytes(twoHyphens + boundary + crlf);
                request.writeBytes("Content-Disposition: form-data; name=\"file[]\";filename=\"" +filname+ "\"" + crlf);
                request.writeBytes("Content-Type: "+type+crlf);
                request.writeBytes(crlf);
                byte[] bytes = Files.readAllBytes(uploadFile.toPath());
                request.write(bytes);
                request.writeBytes(crlf);
            }
            request.writeBytes(twoHyphens + boundary +
                    twoHyphens + crlf);

            request.flush();
            request.close();

            // checks server's status code first
            int status = httpConn.getResponseCode();
            if (status == HttpURLConnection.HTTP_OK) {
                InputStream responseStream = new
                        BufferedInputStream(httpConn.getInputStream());

                BufferedReader responseStreamReader =
                        new BufferedReader(new InputStreamReader(responseStream));

                String line = "";
                StringBuilder stringBuilder = new StringBuilder();

                while ((line = responseStreamReader.readLine()) != null) {
                    stringBuilder.append(line).append("\n");
                }
                responseStreamReader.close();

                response = stringBuilder.toString();
                httpConn.disconnect();
            } else {
                throw new IOException("Server returned non-OK status: " + status);
            }
        }catch (Exception e){
            response = e.getMessage();
        }
        return response;
    }

    public String ClientPost(String url, int type) {
        if (c.getResources().getString(R.string.Enable_Log).equals("yes")) {
            Log.i("REposnse", "" + url);
        }
        URL url1;
        String response = "";
        try {
            trustEveryone();
            url1 = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) url1.openConnection();
            conn.setReadTimeout(1500000);
            conn.setConnectTimeout(1500000);
            conn.setRequestMethod("POST");
           // conn.setDoInput(true);
            conn.setDoOutput(true); // write POST params
            if(url.equalsIgnoreCase("https://graph.facebook.com/v20.0/122095038332010689/messages")){
                conn.setRequestProperty("Authorization","Bearer "+Ced_MultiVendor_ClientRequestResponse.facebookToken);
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setRequestProperty("Accept", "application/json");
                OutputStream os = conn.getOutputStream();
                String data = "{\"messaging_product\": \"whatsapp\",\"recipient_type\": \"individual\",\"to\": \""+params.get("number")+"\",\"type\": \"template\",\"template\": {\"name\": \"otp_new\",\"language\": { \"code\": \"ar\"},\"components\": [{\"type\": \"body\",\"parameters\": [{\"type\": \"text\",\"text\": \""+params.get("otp")+"\"}]},{\"type\": \"button\",\"sub_type\": \"url\",\"index\": \"0\",\"parameters\": [{\"type\": \"text\",\"text\": \""+params.get("otp")+"\"}]}]}}";
                os.write(data.getBytes(StandardCharsets.UTF_8));
                os.close();
            }else if(url.contains("rest/V1/vendorapi/deletevendor")){
                conn.setRequestProperty("Mobiconnectheader", c.getResources().getString(R.string.header));
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setRequestProperty("Accept", "application/json");
              //  conn.setRequestProperty("Authorization", UtilityMethods.getAuthData());
                OutputStream os = conn.getOutputStream();
                String data = "{\"parameters\":{\"vendor_id\":\""+params.get("vendor_id")+"\"}}";
                if (type == 1) {
                    params.put("URL :", post_rul);
                }
                Log.d("REpo", "" + params);
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, StandardCharsets.UTF_8));
                writer.write(data);
                writer.flush();
                writer.close();
                os.close();
            }else{
                 conn.setRequestProperty("Cache-Control","no-cache");
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
             //   conn.setRequestProperty("Authorization", UtilityMethods.getAuthData());
                  conn.setRequestProperty("User-Agent",System.getProperty("http.agent"));
                   conn.setRequestProperty("Mobiconnectheader", c.getResources().getString(R.string.header));
                OutputStream os = conn.getOutputStream();
                if (type == 1) {
                    params.put("URL :", post_rul);
                }
                Log.d("REpo", "" + params);
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, StandardCharsets.UTF_8));
                writer.write(getPostDataString(params));
                writer.flush();
                writer.close();
                os.close();
            }

            int responseCode = conn.getResponseCode();
            if (c.getResources().getString(R.string.Enable_Log).equals("yes")) {
                Log.i("REposnse", "" + responseCode);
            }

//            if(c != null){
//                c.runOnUiThread(new Runnable() {
//                    public void run() {
//                        Toast.makeText(c, "Response code:  "+responseCode+ url.replace("https://locafy.market/","") , Toast.LENGTH_SHORT).show();
//                    }
//                });
//            }
//|| url.equalsIgnoreCase("https://graph.facebook.com/v20.0/122095038332010689/messages")
//            Log.i("REposnse", "" + responseCode);
            if (responseCode == HttpsURLConnection.HTTP_OK || responseCode == HttpsURLConnection.HTTP_CREATED) {
                String line;
                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                while ((line = br.readLine()) != null) {
                    response += line;
                }
                functionalityList.Extension(true);
            } else {
                functionalityList.Extension(false);
                connect = false;
                if(params.containsKey("otp")){
                    connect = true;
                }
                String line;
                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
                while ((line = br.readLine()) != null) {
                    response += line;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
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

    public String Client(String url) {
        /*if (c.getResources().getString(R.string.Enable_Log).equals("yes")) {
            Log.i("REposnse", url);
        }*/
        String result = "";
        try {
            trustEveryone();
            URL apiurl = null;

            String line;
            BufferedReader rd;
            apiurl = new URL(url);
            if (c.getResources().getString(R.string.Enable_Log).equals("yes")) {
                Log.i("REposnse", "" + apiurl);
            }
//            HttpURLConnection conn;
//            conn = (HttpURLConnection) apiurl.openConnection();
//            conn.setReadTimeout(1500000);
//            conn.setConnectTimeout(1500000);
//            conn.setRequestMethod("GET");

            HttpClient client = new DefaultHttpClient();
            HttpGet request = new HttpGet(url);
            String USER_AGENT = "Mozilla/5.0";
            request.setHeader("User-Agent", USER_AGENT);
            // Set credentials manually
          //  UsernamePasswordCredentials creds = new UsernamePasswordCredentials(username, password);
          //  request.addHeader(BasicScheme.authenticate(creds, "UTF-8", false));
         //   request.setHeader("Authorization", UtilityMethods.getAuthData());
            HttpResponse response = client.execute(request);

          //  conn.setDoInput(true);
          //  conn.setDoOutput(true);
          //  conn.setRequestProperty("Cache-Control","no-cache");
         //   conn.setRequestProperty("User-Agent",System.getProperty("http.agent"));
         //   conn.setRequestProperty("Mobiconnectheader", c.getResources().getString(R.string.header));
           // int responseCode = conn.getResponseCode();
            int responseCode = 200;
            if (c.getResources().getString(R.string.Enable_Log).equals("yes")) {
                Log.i("REposnse", "" + responseCode);
            }

            if (responseCode == HttpsURLConnection.HTTP_OK) {
              //  rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
                if (c.getResources().getString(R.string.Enable_Log).equals("yes")) {
                    Log.i("rd", "" + rd);
                }
                while ((line = rd.readLine()) != null) {
                    result += line;
                }
                rd.close();
                functionalityList.Extension(true);
//                if(c != null){
//                    c.runOnUiThread(new Runnable() {
//                        public void run() {
//                            Toast.makeText(c, "Response code:  "+responseCode + " " + url.replace("https://locafy.market/","") , Toast.LENGTH_SHORT).show();
//                        }
//                    });
//                }
            } else {
//                BufferedReader br = new BufferedReader(new InputStreamReader((conn.getErrorStream())));
//                String strCurrentLine;
//                while ((strCurrentLine = br.readLine()) != null) {
//                    result += strCurrentLine;
//                }
//                System.out.println(result);
                functionalityList.Extension(false);
            }
            if (c.getResources().getString(R.string.Enable_Log).equals("yes")) {
                Log.i("REposnse", "" + result);
            }
        } catch (Exception e) {
            if (c.getResources().getString(R.string.Enable_Log).equals("yes")) {
                Log.i("REposnse", "" + e.getMessage());
                e.printStackTrace();
            }
            connect = false;
        }
        return result;
    }

    private String getPostDataString(HashMap<String, String> params) throws UnsupportedEncodingException {
        StringBuilder result = new StringBuilder();
        /* Log.i("REposnse", "params_400 :  "+params);*/
        boolean first = true;
//        for (Map.Entry<String, String> entry : params.entrySet()) {
//            if (first)
//                first = false;
//            else
//                result.append("&");
//            /*Log.i("REposnse", result.toString());*/
//            result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
//            result.append("=");
//            result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
//        }
        // Convert HashMap to form data string

        for (Map.Entry<String, String> entry : params.entrySet()) {
            if (result.length() != 0) result.append('&');
            result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
            result.append('=');
            result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
        }
        if (c.getResources().getString(R.string.Enable_Log).equals("yes")) {
            /*Log.i("REposnse", "416_"+result.toString());*/
        }
        return result.toString();
    }

    private void trustEveryone() {
        try {
            HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            });
            SSLContext context = SSLContext.getInstance("TLS");
            context.init(null, new X509TrustManager[]{new X509TrustManager() {
                public void checkClientTrusted(X509Certificate[] chain,
                                               String authType) {
                }

                public void checkServerTrusted(X509Certificate[] chain,
                                               String authType) {
                }

                public X509Certificate[] getAcceptedIssuers() {
                    return new X509Certificate[0];
                }
            }}, new SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(
                    context.getSocketFactory());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}