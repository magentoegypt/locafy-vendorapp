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

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import magentoegypt.locafy.base_app.Ced_MultiVendor_VendorFunctionalityList;
import magentoegypt.locafy.base_app.UtilityMethods;
import magentoegypt.locafy.vendor_session.Ced_MultiVendor_VendorSessionManagement;
import magentoegypt.locafy.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
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

public class Client_Request_Response_File_Upload extends AsyncTask<String ,String,String>
{

    Ced_MultiVendor_VendorFunctionalityList functionalityList;
    Activity c;
    Ced_MultiVendor_VendorSessionManagement vendorSessionManagement;
    public AsyncResponse delegate = null;
    String req="";
    HashMap<String,String> params;
    ProgressDialog progressDialog;
    String Profileuri="";
    String Logouri="";
    String Banneruri="";
    String responsefileupload="";
    String logopicresponse="";
    String bannerpicresponse="";
    boolean connect=true;
    boolean profileflag=true,logoflag=true,bannerflag=true;
    Ced_NewLoader cedNewLoader;
    String url;
    int determine_activity;

    public Client_Request_Response_File_Upload(AsyncResponse asyncResponse, Activity editVendorProfileSection, String post, HashMap<String, String> dataforvendorprofile, String profileuri, String logouri, String banneruri, int determine_activity)   //determine_activity for images upload -> bank_detials or business_details
    {
        delegate = asyncResponse;
        c=editVendorProfileSection;
        req=post;
        params=dataforvendorprofile;
        functionalityList=new Ced_MultiVendor_VendorFunctionalityList(c);
        Profileuri=profileuri;
        Logouri=logouri;
        Banneruri=banneruri;
        vendorSessionManagement=new Ced_MultiVendor_VendorSessionManagement(c);
        this.determine_activity = determine_activity;
    }
    @Override
    protected void onPreExecute()
    {
        super.onPreExecute();
        cedNewLoader =new Ced_NewLoader(c);
        cedNewLoader.show();

    }
    @Override
    protected String doInBackground(String... params)
    {
        String json="";
        Log.i("REposnse",""+params[0]);
        url=params[0];
        try
        {
            if(Profileuri.equals("nouri"))
            {
                if(Logouri.equals("nouri"))
                {
                    if(Banneruri.equals("nouri"))
                    {
                        json=ClientPostwithupload(params[0],false,false,false);
                    }
                    else
                    {
                        json=ClientPostwithupload(params[0],false,false,true);
                    }

                }
                else
                {
                    if(Banneruri.equals("nouri"))
                    {
                        json=ClientPostwithupload(params[0],false,true,false);
                    }
                    else
                    {
                        json=ClientPostwithupload(params[0],false,true,true);
                    }
                }

            }
            else
            {
                if(Logouri.equals("nouri"))
                {
                    if(Banneruri.equals("nouri"))
                    {
                        json=ClientPostwithupload(params[0],true,false,false);
                    }
                    else
                    {
                        json=ClientPostwithupload(params[0],true,false,true);
                    }

                }
                else
                {
                    if(Banneruri.equals("nouri"))
                    {
                        json=ClientPostwithupload(params[0],true,true,false);
                    }
                    else
                    {
                        json=ClientPostwithupload(params[0], true, true, true);
                    }
                }

            }

            Log.i("REposnse", "" + json);

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        if(c.getResources().getString(R.string.Enable_Log).equals("yes"))
        {
            Log.i("response",""+json);
        }
        return json;
    }
    public String ClientPost(String url,String profile,String logo,String banner)
    {
        if(c.getResources().getString(R.string.Enable_Log).equals("yes"))
        {
            Log.i("REQUESTED_URL", url);
        }
        URL url1;
        String response = "";
        try
        {
            url1 = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) url1.openConnection();
            conn.setReadTimeout(1500000);
            conn.setConnectTimeout(1500000);
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setRequestProperty("Mobiconnectheader", c.getResources().getString(R.string.header));
            if(determine_activity==1)
            {
                conn.setRequestProperty("personal_pan_file", profile);
                conn.setRequestProperty("cancelled_cheque", logo);
            }

            if(determine_activity==2)
            {
                conn.setRequestProperty("gst_id_file", profile);
                conn.setRequestProperty("tan_id_file", logo);
                conn.setRequestProperty("cin_id_file", banner);
            }
          //  conn.setRequestProperty("Authorization", UtilityMethods.getAuthData());
            OutputStream os = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
            writer.write(getPostDataString(params));
            writer.flush();
            writer.close();
            os.close();
            int responseCode=conn.getResponseCode();
            if (c.getResources().getString(R.string.Enable_Log).equals("yes"))
            {
                Log.i("responsecode", "" + responseCode);
            }
            if(responseCode == HttpsURLConnection.HTTP_OK)
            {
                String line;
                BufferedReader br=new BufferedReader(new InputStreamReader(conn.getInputStream()));
                while ((line=br.readLine()) != null)
                {
                    response+=line;
                }
                functionalityList.Extension(true);
            }
            else
            {
                functionalityList.Extension(false);
                connect=false;
                response="";
            }
        }
        catch (Exception e)
        {
            if(c.getResources().getString(R.string.Enable_Log).equals("yes"))
            {
                Log.i("errorwhilerequest",""+e.getMessage());
                e.printStackTrace();
            }
            connect=false;
        }
        if(c.getResources().getString(R.string.Enable_Log).equals("yes"))
        {
            Log.i("Response",""+response);
        }
        return response;
    }
    @Override
    protected void onPostExecute(String av)
    {
        super.onPostExecute(av);

        if(connect)
        {
            try
            {
                delegate.processFinish(av);
            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }
            try
            {
                if ((this.cedNewLoader != null) && this.cedNewLoader.isShowing())
                {
                    this.cedNewLoader.dismiss();
                }
            }
            catch (final IllegalArgumentException e)
            {
                // Handle or log or ignore
            }
            catch (final Exception e)
            {
                // Handle or log or ignore
            }
            finally
            {
                this.cedNewLoader = null;
            }
        }
        else
        {
            try
            {
                if ((this.cedNewLoader != null) && this.cedNewLoader.isShowing())
                {
                    this.cedNewLoader.dismiss();
                }
            }
            catch (final IllegalArgumentException e)
            {
                // Handle or log or ignore
            }
            catch (final Exception e)
            {
                // Handle or log or ignore
            }
            finally
            {
                this.cedNewLoader = null;
            }
            c.runOnUiThread(new Runnable()
            {
                public void run()
                {
                    final Dialog listDialog = new Dialog(c, R.style.PauseDialog);
                    listDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    ((ViewGroup)((ViewGroup)listDialog.getWindow().getDecorView()).getChildAt(0)).getChildAt(1).setBackgroundColor(c.getResources().getColor(R.color.AppTheme));
                   // listDialog.setTitle(c.getResources().getString(R.string.oops));
                    LayoutInflater li = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    View v = li.inflate(R.layout.magenative_activity_no_module, null, false);
                    listDialog.setContentView(v);
                    listDialog.setCancelable(false);
                    TextView conti = (TextView) listDialog.findViewById(R.id.conti);
                    TextView cross = (TextView)listDialog.findViewById(R.id.cross);

                    cross.setOnClickListener(new View.OnClickListener()
                    {
                        @Override
                        public void onClick(View view)
                        {
                            listDialog.dismiss();
                        }
                    });
                    conti.setOnClickListener(new View.OnClickListener()
                    {
                        @Override
                        public void onClick(View v)
                        {
                            listDialog.dismiss();
                            Ced_MultiVendor_ClientRequestResponseProductupload ced_requestwithoutLoader = new Ced_MultiVendor_ClientRequestResponseProductupload(delegate, c,req,params,Profileuri,Logouri,Banneruri);
                            ced_requestwithoutLoader.execute(url);
                        }
                    });
                    listDialog.show();
                }
            });

        }

    }
    private String getPostDataString(HashMap<String, String> params) throws UnsupportedEncodingException
    {
        StringBuilder result = new StringBuilder();
        boolean first = true;
        for(Map.Entry<String, String> entry : params.entrySet())
        {
            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
        }
        if (c.getResources().getString(R.string.Enable_Log).equals("yes"))
        {
            Log.i("POSTDATA", result.toString());
        }

        return result.toString();
    }
    public String  ClientPostwithupload(String uploadurl ,boolean profile,boolean logo,boolean banner) throws IOException
    {

        String profilepicuploaded="false";
        String logouploaded="false";
        String banneruploaded="false";
        String success="";
        if(profile)
        {
            String fileName = Profileuri;
            HttpURLConnection conn = null;
            DataOutputStream dos = null;
            String lineEnd = "\r\n";
            String twoHyphens = "--";
            String boundary = "*****";
            int bytesRead, bytesAvailable, bufferSize;
            byte[] buffer;
            int maxBufferSize = 1 * 1024 * 1024;
            String serverResponseMessage = null;
            File sourceFile = new File(Profileuri);

            try
            {
                FileInputStream fileInputStream = new FileInputStream(sourceFile);
                URL url = new URL(uploadurl);
                conn = (HttpURLConnection) url.openConnection();
                conn.setDoInput(true);
                conn.setDoOutput(true);
                conn.setUseCaches(true);
                conn.setRequestMethod("POST");
                conn.setReadTimeout(1500000);
                conn.setConnectTimeout(1500000);
                conn.setRequestProperty("Connection", "Keep-Alive");
                conn.setRequestProperty("ENCTYPE", "multipart/form-data");
                conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
                if(determine_activity==1)
                {
                    conn.setRequestProperty("personal_pan_file", fileName);
                }

                if(determine_activity==2)
                {
                    conn.setRequestProperty("gst_id_file", fileName);
                }

             //   conn.setRequestProperty("Authorization", vendorSessionManagement.getVendorid());
             //   conn.setRequestProperty("Authorization", UtilityMethods.getAuthData());
                dos = new DataOutputStream(conn.getOutputStream());
                dos.writeBytes(twoHyphens + boundary + lineEnd);


                if(determine_activity==1)
                {
                    dos.writeBytes("Content-Disposition: form-data; name='personal_pan_file';filename="+ fileName.replace(" ","%20") + "" + lineEnd);
                }

                if(determine_activity==2)
                {
                    dos.writeBytes("Content-Disposition: form-data; name='gst_id_file';filename="+ fileName.replace(" ","%20") + "" + lineEnd);
                }


                dos.writeBytes(lineEnd);
                // create a buffer of  maximum size
                bytesAvailable = fileInputStream.available();
                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                buffer = new byte[bufferSize];
                // read file and write it into form...
                bytesRead = fileInputStream.read(buffer, 0, bufferSize);
                while (bytesRead > 0)
                {
                    dos.write(buffer, 0, bufferSize);
                    bytesAvailable = fileInputStream.available();
                    bufferSize = Math.min(bytesAvailable, maxBufferSize);
                    bytesRead = fileInputStream.read(buffer, 0, bufferSize);
                }

                // send multipart form data necesssary after file data...

                dos.writeBytes(lineEnd);
                dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

                // Responses from the server (code and message)
                int serverResponseCode = conn.getResponseCode();
                serverResponseMessage = conn.getResponseMessage();
                if (c.getResources().getString(R.string.Enable_Log).equals("yes"))
                {
                    Log.i("uploadFile", "HTTP Response is : "
                            + serverResponseMessage + ": " + serverResponseCode);
                }

                if (serverResponseCode == 200)
                {

                    String line;
                    String response = " ";
                    BufferedReader br=new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    while ((line=br.readLine()) != null)
                    {
                        response+=line;

                    }
                    if (c.getResources().getString(R.string.Enable_Log).equals("yes"))
                    {
                        Log.i("fileuploadlog",""+response);
                    }
                    JSONObject object=new JSONObject(response);
                    String successobject=object.getJSONObject("data").getString("success");
                    if(successobject.equals("false"))
                    {
                        profileflag=false;
                        responsefileupload=response;
                    }
                }

                //close the streams //
                fileInputStream.close();
                dos.flush();
                dos.close();

            }
            catch (Exception ex)
            {
                if(c.getResources().getString(R.string.Enable_Log).equals("yes"))
                {
                    Log.e("Upload file to server", "error: " + ex.getMessage(), ex);
                }
                connect=false;
            }
        }
        if(logo)
        {

            if(profileflag)
            {
                String fileName = Logouri;
                HttpURLConnection conn = null;
                DataOutputStream dos = null;
                String lineEnd = "\r\n";
                String twoHyphens = "--";
                String boundary = "*****";
                int bytesRead, bytesAvailable, bufferSize;
                byte[] buffer;
                int maxBufferSize = 1 * 1024 * 1024;
                String serverResponseMessage = null;
                File sourceFile = new File(Logouri);
                try
                {

                    // open a URL connection to the Servlet
                    FileInputStream fileInputStream = new FileInputStream(sourceFile);
                    URL url = new URL(uploadurl);

                    conn = (HttpURLConnection) url.openConnection();
                    conn.setDoInput(true); // Allow Inputs
                    conn.setDoOutput(true); // Allow Outputs
                    conn.setUseCaches(false); // Don't use a Cached Copy
                    conn.setRequestMethod("POST");
                    conn.setReadTimeout(1500000);
                    conn.setConnectTimeout(1500000);
                    conn.setRequestProperty("Connection", "Keep-Alive");
                    conn.setRequestProperty("ENCTYPE", "multipart/form-data");
                    conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);

                    if(determine_activity==1)
                    {
                        conn.setRequestProperty("cancelled_cheque", fileName);
                    }

                    if(determine_activity==2)
                    {
                        conn.setRequestProperty("tan_id_file", fileName);
                    }

                    conn.setRequestProperty("cancelled_cheque", fileName);
                  //  conn.setRequestProperty("Authorization", vendorSessionManagement.getVendorid());
                 //   conn.setRequestProperty("Authorization", UtilityMethods.getAuthData());
                    dos = new DataOutputStream(conn.getOutputStream());
                    dos.writeBytes(twoHyphens + boundary + lineEnd);

                    if(determine_activity==1)
                    {
                        dos.writeBytes("Content-Disposition: form-data; name='cancelled_cheque';filename="+ fileName.replace(" ","%20") + "" + lineEnd);
                    }

                    if(determine_activity==2)
                    {
                        dos.writeBytes("Content-Disposition: form-data; name='tan_id_file';filename="+ fileName.replace(" ","%20") + "" + lineEnd);
                    }



                    dos.writeBytes(lineEnd);

                    // create a buffer of  maximum size
                    bytesAvailable = fileInputStream.available();

                    bufferSize = Math.min(bytesAvailable, maxBufferSize);
                    buffer = new byte[bufferSize];

                    // read file and write it into form...
                    bytesRead = fileInputStream.read(buffer, 0, bufferSize);

                    while (bytesRead > 0) {

                        dos.write(buffer, 0, bufferSize);
                        bytesAvailable = fileInputStream.available();
                        bufferSize = Math.min(bytesAvailable, maxBufferSize);
                        bytesRead = fileInputStream.read(buffer, 0, bufferSize);

                    }

                    // send multipart form data necesssary after file data...

                    dos.writeBytes(lineEnd);
                    dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

                    // Responses from the server (code and message)
                    int serverResponseCode = conn.getResponseCode();
                    serverResponseMessage = conn.getResponseMessage();
                    if (c.getResources().getString(R.string.Enable_Log).equals("yes"))
                    {
                        Log.i("uploadFile", "HTTP Response is : "
                                + serverResponseMessage + ": " + serverResponseCode);
                    }

                    if (serverResponseCode == 200)
                    {

                        String line;
                        String response = " ";
                        BufferedReader br=new BufferedReader(new InputStreamReader(conn.getInputStream()));
                        while ((line=br.readLine()) != null)
                        {
                            response+=line;

                        }
                        JSONObject object=new JSONObject(response);
                        String successobject=object.getJSONObject("data").getString("success");
                        if(successobject.equals("false"))
                        {
                            logoflag=false;
                            responsefileupload=response;
                        }
                        if (c.getResources().getString(R.string.Enable_Log).equals("yes"))
                        {
                            Log.i("RESPONSe",""+response);
                        }
                    }
                    logouploaded="true";
                    //close the streams //
                    fileInputStream.close();
                    dos.flush();
                    dos.close();

                } catch (Exception ex)
                {

                    if (c.getResources().getString(R.string.Enable_Log).equals("yes"))
                    {
                        Log.e("Upload file to server", "error: " + ex.getMessage(), ex);
                    }
                    connect=false;
                }

            }
            else
            {
                return responsefileupload;

            }

        }
        if(banner)
        {
            if(logoflag&&profileflag)
            {
                String fileName = Banneruri;
                HttpURLConnection conn = null;
                DataOutputStream dos = null;
                String lineEnd = "\r\n";
                String twoHyphens = "--";
                String boundary = "*****";
                int bytesRead, bytesAvailable, bufferSize;
                byte[] buffer;
                int maxBufferSize = 1 * 1024 * 1024;
                String serverResponseMessage = null;
                File sourceFile = new File(Banneruri);

                try
                {

                    // open a URL connection to the Servlet
                    FileInputStream fileInputStream = new FileInputStream(sourceFile);
                    URL url = new URL(uploadurl);

                    // Open a HTTP  connection to  the URL
                    conn = (HttpURLConnection) url.openConnection();
                    conn.setDoInput(true); // Allow Inputs
                    conn.setDoOutput(true); // Allow Outputs
                    conn.setUseCaches(false); // Don't use a Cached Copy
                    conn.setRequestMethod("POST");
                    conn.setReadTimeout(1500000);
                    conn.setConnectTimeout(1500000);
                    conn.setRequestProperty("Connection", "Keep-Alive");
                    conn.setRequestProperty("ENCTYPE", "multipart/form-data");
                    conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
                    if(determine_activity==1)
                    {
                        conn.setRequestProperty("cin_id_file", fileName);
                    }
                  //  conn.setRequestProperty("Authorization", vendorSessionManagement.getVendorid());
                 //   conn.setRequestProperty("Authorization", UtilityMethods.getAuthData());

                    dos = new DataOutputStream(conn.getOutputStream());
                    dos.writeBytes(twoHyphens + boundary + lineEnd);

                    if(determine_activity==1)
                    {
                        dos.writeBytes("Content-Disposition: form-data; name='cin_id_file';filename="+ fileName.replace(" ","%20") + "" + lineEnd);
                    }
                    dos.writeBytes(lineEnd);

                    // create a buffer of  maximum size
                    bytesAvailable = fileInputStream.available();

                    bufferSize = Math.min(bytesAvailable, maxBufferSize);
                    buffer = new byte[bufferSize];

                    // read file and write it into form...
                    bytesRead = fileInputStream.read(buffer, 0, bufferSize);

                    while (bytesRead > 0)
                    {

                        dos.write(buffer, 0, bufferSize);
                        bytesAvailable = fileInputStream.available();
                        bufferSize = Math.min(bytesAvailable, maxBufferSize);
                        bytesRead = fileInputStream.read(buffer, 0, bufferSize);

                    }

                    // send multipart form data necesssary after file data...
                    dos.writeBytes(lineEnd);
                    dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

                    // Responses from the server (code and message)
                    int serverResponseCode = conn.getResponseCode();
                    serverResponseMessage = conn.getResponseMessage();
                    if (c.getResources().getString(R.string.Enable_Log).equals("yes"))
                    {
                        Log.i("uploadFile", "HTTP Response is : "
                                + serverResponseMessage + ": " + serverResponseCode);
                    }

                    if (serverResponseCode == 200)
                    {

                        String line;
                        String response = " ";
                        BufferedReader br=new BufferedReader(new InputStreamReader(conn.getInputStream()));
                        while ((line=br.readLine()) != null)
                        {
                            response+=line;

                        }
                        JSONObject object=new JSONObject(response);
                        String successobject=object.getJSONObject("data").getString("success");
                        if(successobject.equals("false"))
                        {
                            bannerflag=false;
                            responsefileupload=response;
                        }
                        if (c.getResources().getString(R.string.Enable_Log).equals("yes"))
                        {
                            Log.i("RESPONSe",""+response);
                        }
                    }
                    banneruploaded="true";
                    //close the streams //
                    fileInputStream.close();
                    dos.flush();
                    dos.close();
                }
                catch (Exception ex)
                {
                    if (c.getResources().getString(R.string.Enable_Log).equals("yes"))
                    {
                        Log.e("Upload file to server", "error: " + ex.getMessage(), ex);
                    }
                    connect=false;
                }
            }
            else
            {
                return responsefileupload;
            }

        }
        if(profileflag&&logoflag&&bannerflag)
        {
            success=ClientPost(uploadurl,profilepicuploaded,logouploaded,banneruploaded);
            return success;
        }
        else
        {
            return responsefileupload;
        }
    }
}
