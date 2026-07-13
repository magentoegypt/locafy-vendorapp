package magentoegypt.locafy.api_request_response_section;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.AsyncTask;
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

import org.json.JSONArray;
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
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.X509TrustManager;

public class Ced_ClientRequestResponseRest_Data_Array extends AsyncTask<String, String, String> {
    Ced_MultiVendor_VendorFunctionalityList cedFunctionalityList;
    Activity c;
    Context context;
    public AsyncResponse delegate = null;
    String req = "";
    HashMap<String, String> params;
    String data;
    boolean connect = true;
    Ced_NewLoader cedNewLoader;
    String Splash = "";
    int responseCode;
    String url;
    JSONObject data_post;

    public Ced_ClientRequestResponseRest_Data_Array(AsyncResponse cedAsyncResponse, Activity context) {
        delegate = cedAsyncResponse;
        c = context;
        req = "GET";
        cedFunctionalityList = new Ced_MultiVendor_VendorFunctionalityList(c);
    }

    public Ced_ClientRequestResponseRest_Data_Array(AsyncResponse cedAsyncResponse, Activity context, String splash) {
        delegate = cedAsyncResponse;
        c = context;
        req = "GET";
        cedFunctionalityList = new Ced_MultiVendor_VendorFunctionalityList(c);
        Splash = splash;
    }

    public Ced_ClientRequestResponseRest_Data_Array(AsyncResponse cedAsyncResponse, Activity context, String RequestMethod, String postparam) {
        delegate = cedAsyncResponse;
        c = context;
        req = RequestMethod;
        data = postparam;

        cedFunctionalityList = new Ced_MultiVendor_VendorFunctionalityList(c);
    }


    public Ced_ClientRequestResponseRest_Data_Array(AsyncResponse cedAsyncResponse, Activity context, String RequestMethod, String postparam, String splash) {
        Splash = splash;
        delegate = cedAsyncResponse;
        c = context;
        req = RequestMethod;
        data = postparam;
        Log.i("REposnse", "" + data);
        cedFunctionalityList = new Ced_MultiVendor_VendorFunctionalityList(c);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        if (Splash.isEmpty()) {
            cedNewLoader = new Ced_NewLoader(c);
            cedNewLoader.show();
        }

    }

    @Override
    protected String doInBackground(String... params) {
        String json = "";
        Log.i("REposnse", "" + params[0]);
        url = params[0];
        if (req.equals("GET")) {
            json = Client(params[0]);
        } else {
            json = ClientPost(params[0]);
        }
        Log.i("REposnse", "" + json);
        return json;
    }

    @Override
    protected void onPostExecute(String av) {
        super.onPostExecute(av);
        if (connect) {

            try {
                JSONObject jsonObject = new JSONObject(av);
                if (jsonObject.has("successresponse")) {
                    cedFunctionalityList.Extension(true);
                    JSONArray array = new JSONArray(jsonObject.getString("successresponse"));
//                    JSONObject array = new JSONObject(jsonObject.getString("successresponse"));
                    Object object = array;
                    delegate.processFinish(object);
                } else {
                    Toast.makeText(c, jsonObject.getString("message"), Toast.LENGTH_LONG).show();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                if ((this.cedNewLoader != null) && this.cedNewLoader.isShowing()) {
                    this.cedNewLoader.dismiss();
                }
            } catch (final IllegalArgumentException e) {
                // Handle or log or ignore
            } catch (final Exception e) {
                // Handle or log or ignore
            } finally {
                this.cedNewLoader = null;
            }
        } else {
            try {
                if ((this.cedNewLoader != null) && this.cedNewLoader.isShowing()) {
                    this.cedNewLoader.dismiss();
                }
            }
            catch (final IllegalArgumentException e) {
                // Handle or log or ignore
            } catch (final Exception e) {
                // Handle or log or ignore
            } finally {
                this.cedNewLoader = null;
            }
            try {
                c.runOnUiThread(new Runnable() {
                    public void run() {
                        final Dialog listDialog = new Dialog(c, R.style.PauseDialog);
                        listDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        ((ViewGroup) ((ViewGroup) listDialog.getWindow().getDecorView()).getChildAt(0)).getChildAt(1).setBackgroundColor(c.getResources().getColor(R.color.AppTheme));
                        // listDialog.setTitle(c.getResources().getString(R.string.oops));
                        LayoutInflater li = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                        View v = li.inflate(R.layout.magenative_activity_no_module, null, false);
                        listDialog.setContentView(v);
                        listDialog.setCancelable(true);
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
                                    Ced_ClientRequestResponseRest_Data_Array ced_requestwithoutLoader = new Ced_ClientRequestResponseRest_Data_Array(delegate, c);
                                    ced_requestwithoutLoader.execute(url);
                                } else {
                                    if (Splash.isEmpty()) {
                                        Ced_ClientRequestResponseRest_Data_Array ced_requestwithoutLoader = null;
                                        try {
                                            ced_requestwithoutLoader = new Ced_ClientRequestResponseRest_Data_Array(delegate, c, req, data);
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                        ced_requestwithoutLoader.execute(url);
                                    } else {
                                        Ced_ClientRequestResponseRest_Data_Array ced_requestwithoutLoader = new Ced_ClientRequestResponseRest_Data_Array(delegate, c, req, data, Splash);
                                        ced_requestwithoutLoader.execute(url);
                                    }
                                }

                            }
                        });
                        listDialog.show();
                    }
                });
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public String ClientPost(String url) {
        Log.i("REposnse", "" + url);
        URL url1;
        String response = "";

        try {

            trustEveryone();

            url1 = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) url1.openConnection();
            conn.setReadTimeout(1500000);
            conn.setConnectTimeout(1500000);
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setUseCaches(true);
            conn.setRequestProperty("Content-Type", "application/json");
         //   conn.setRequestProperty("Authorization", UtilityMethods.getAuthData());
            OutputStream os = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
            JSONObject jsonObject = new JSONObject(data);
            data_post = new JSONObject();
            data_post.put("data", jsonObject);
            Log.i("REposnse", "" + data_post);
            writer.write(data_post.toString());
            writer.flush();
            writer.close();
            os.close();
            responseCode = conn.getResponseCode();
            Log.i("REposnse", "" + responseCode);
            if (responseCode == HttpsURLConnection.HTTP_OK) {
                String line;
                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                while ((line = br.readLine()) != null) {
                    response += line;
                }
                JSONObject object = new JSONObject();
                object.put("successresponse", response);
                response = object.toString();
            } else {
               /* String line;
                BufferedReader br=new BufferedReader(new InputStreamReader(conn.getInputStream()));
                while ((line=br.readLine()) != null)
                {
                    response+=line;
                }*/
                String line;
                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
                while ((line = br.readLine()) != null) {
                    response += line;
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            connect = false;
        }

        return response;
    }

    public String Client(String url) {
        String result = "";
        try {

            trustEveryone();

            URL apiurl = null;
            HttpURLConnection conn;
            String line;
            BufferedReader rd;
            apiurl = new URL(url);
            conn = (HttpURLConnection) apiurl.openConnection();
            conn.setRequestMethod("GET");
            conn.setReadTimeout(1500000);
            conn.setConnectTimeout(1500000);
            conn.setRequestProperty("Mobiconnectheader", c.getResources().getString(R.string.header));
          //  conn.setRequestProperty("Authorization", UtilityMethods.getAuthData());
            responseCode = conn.getResponseCode();
            Log.i("REposnse", "" + responseCode);
            if (responseCode == HttpsURLConnection.HTTP_OK) {
                rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                while ((line = rd.readLine()) != null) {
                    result += line;
                }
                rd.close();
                JSONObject object = new JSONObject();
                object.put("successresponse", result);
                result = object.toString();
            } else {
                rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
                while ((line = rd.readLine()) != null) {
                    result += line;
                }
                rd.close();
            }

        } catch (Exception e) {
            e.printStackTrace();
            connect = false;
        }
        return result;
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
        Log.i("REposnse", "" + params);
        return result.toString();
    }

    private void trustEveryone() {
        try {
            HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier(){
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }});
            SSLContext context = SSLContext.getInstance("TLS");
            context.init(null, new X509TrustManager[]{new X509TrustManager(){
                public void checkClientTrusted(X509Certificate[] chain,
                                               String authType) throws CertificateException {}
                public void checkServerTrusted(X509Certificate[] chain,
                                               String authType) throws CertificateException {}
                public X509Certificate[] getAcceptedIssuers() {
                    return new X509Certificate[0];
                }}}, new SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(
                    context.getSocketFactory());
        } catch (Exception e) { // should never happen
            e.printStackTrace();
        }
    }
}