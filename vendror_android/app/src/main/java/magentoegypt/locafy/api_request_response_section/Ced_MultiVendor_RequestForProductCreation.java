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

import magentoegypt.locafy.R;
import magentoegypt.locafy.base_app.Ced_MultiVendor_VendorFunctionalityList;
import magentoegypt.locafy.base_app.UtilityMethods;
import magentoegypt.locafy.vendor_session.Ced_MultiVendor_VendorSessionManagement;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Created by developer on 7/5/16.
 */
public class Ced_MultiVendor_RequestForProductCreation extends AsyncTask<String ,String,String>
{

    Ced_MultiVendor_VendorFunctionalityList functionalityList;
    Activity c;
    Ced_MultiVendor_VendorSessionManagement vendorSessionManagement;
    public AsyncResponse delegate = null;
    String product_id;
    ProgressDialog progressDialog;
    ArrayList<String> uri;
    HashMap<String,String>hash;
    String response = " ";
    boolean checkfornextrequest = true;
    boolean connect=true;
    String url;
    Ced_NewLoader cedNewLoader;
    public Ced_MultiVendor_RequestForProductCreation(AsyncResponse asyncResponse, Activity editVendorProfileSection, ArrayList<String> uris, String Pro_id, HashMap<String, String> defaulthash)
    {
        delegate = asyncResponse;
        c=editVendorProfileSection;
        functionalityList=new Ced_MultiVendor_VendorFunctionalityList(c);
        vendorSessionManagement=new Ced_MultiVendor_VendorSessionManagement(c);
        uri=uris;
        product_id=Pro_id;
        hash=defaulthash;
        Log.i("vaibhav1",""+uris+""+hash+""+product_id);
    }
    @Override
    protected void onPreExecute()
    {
        super.onPreExecute();
        cedNewLoader =new Ced_NewLoader(c);
        cedNewLoader.show();
        Log.i("vaibhav2","in");

    }
    @Override
    protected String doInBackground(String... params)
    {
        String json="";
        Log.i("REposnse",""+params[0]);
        url=params[0];
        try
        {
            Log.i("vaibhav3","prams"+params[0]);
           json= doFileUpload(params[0]);

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        if (c.getResources().getString(R.string.Enable_Log).equals("yes"))
        {
            Log.i("response", "" + json);
        }
        Log.i("REposnse", "" + json);
        return json;

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
                    //listDialog.setTitle(c.getResources().getString(R.string.oops));
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
                            Ced_MultiVendor_RequestForProductCreation ced_requestwithoutLoader = new Ced_MultiVendor_RequestForProductCreation(delegate, c,uri,product_id,hash);
                            ced_requestwithoutLoader.execute(url);
                        }
                    });
                    listDialog.show();
                }
            });

        }
    }

    private String doFileUpload(String stringurl) throws IOException
    {
        Log.i("vaibhav3.1","");
        Log.i("hello",stringurl);
        Iterator iterator=uri.iterator();
        Log.i("vaibhav3.2",""+iterator.hasNext());
        int count=0;
        while (iterator.hasNext())
        {
            Log.i("vaibhav4","");
            response = " ";
            if(checkfornextrequest)
            {
                Log.i("vaibhav5","");
                count=count+1;
                String fileName = (String) iterator.next();
                HttpURLConnection conn = null;
                DataOutputStream dos = null;
                String lineEnd = "\r\n";
                String twoHyphens = "--";
                String boundary = "*****";
                int bytesRead, bytesAvailable, bufferSize;
                byte[] buffer;
                int maxBufferSize = 1 * 1024 * 1024;
                String serverResponseMessage = null;
                File sourceFile = new File(fileName);

                try
                {

                    // open a URL connection to the Servlet
                    FileInputStream fileInputStream = new FileInputStream(sourceFile);
                    URL url = new URL(stringurl);
                    // Open a HTTP  connection to  the URL
                    conn = (HttpURLConnection) url.openConnection();
                    conn.setDoInput(true); // Allow Inputs
                    conn.setDoOutput(true); // Allow Outputs
                    conn.setUseCaches(false);
                    conn.setReadTimeout(1500000);
                    conn.setConnectTimeout(1500000);
                    conn.setRequestMethod("POST");
                    conn.setRequestProperty("Connection", "Keep-Alive");
                    conn.setRequestProperty("ENCTYPE", "multipart/form-data");
                    if(hash.size()>0)
                    {
                        if(hash.containsKey(fileName))
                        {
                            conn.setRequestProperty("Content-Type", "multipart/form-data;default="+1+";boundary=" + boundary);
                        }
                        else
                        {
                            conn.setRequestProperty("Content-Type", "multipart/form-data;default="+0+";boundary=" + boundary);
                        }

                    }
                    else
                    {
                        conn.setRequestProperty("Content-Type", "multipart/form-data;default="+0+";boundary=" + boundary);
                    }

                    conn.setRequestProperty("profile_picture", fileName);

                  //  conn.setRequestProperty("Authorization", product_id);
                //    conn.setRequestProperty("Authorization", UtilityMethods.getAuthData());
                    conn.setRequestProperty("User-Agent", product_id);
                    dos = new DataOutputStream(conn.getOutputStream());
                    dos.writeBytes(twoHyphens + boundary + lineEnd);
                    dos.writeBytes("Content-Disposition: form-data; name='images';filename="+ fileName.replace(" ","%20") + "" + lineEnd);
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
                        Log.i("uploadFile", "HTTP Response is : " + serverResponseMessage + ": " + serverResponseCode);
                    }

                    if (serverResponseCode == 200)
                    {

                        String line;

                        BufferedReader br=new BufferedReader(new InputStreamReader(conn.getInputStream()));
                        while ((line=br.readLine()) != null)
                        {
                            response+=line;

                        }
                        if (c.getResources().getString(R.string.Enable_Log).equals("yes"))
                        {
                            Log.i("Responseontrue",""+response);
                        }
                        JSONObject object=new JSONObject(response);
                        String successobject=object.getJSONObject("data").getString("success");
                        String message=object.getJSONObject("data").getString("message");
                        if(successobject.equals("false"))
                        {
                           checkfornextrequest=false;
                           response="{\"data\":{\"message\":\"Image number "+count+" has Exception : "+message+",Only "+(count-1)+" images have been updated.\",\"success\":true}}";
                            if (c.getResources().getString(R.string.Enable_Log).equals("yes"))
                            {
                                Log.i("Responseonfalse",""+response);
                            }
                            break;
                        }

                    }
                    else
                    {
                        connect=false;
                        response="";
                    }
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


        }

            return  response;

    }

}
