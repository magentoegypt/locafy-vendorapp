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
package magentoegypt.locafy.addons.vendor_member_ship_plans;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.github.ybq.android.spinkit.SpinKitView;
import magentoegypt.locafy.navigation_drawer.Activity.Ced_MultiVendor_NavigationActivity;
import magentoegypt.locafy.R;

import java.util.HashMap;
import java.util.Objects;

public class Ced_Weblink extends Ced_MultiVendor_NavigationActivity {
    WebView webView;
    String url;
    SpinKitView spinKitView;
    ProgressBar progressBar;
    boolean loader = false;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewGroup content = findViewById(R.id.MultiVendor_frame_container);
        getLayoutInflater().inflate(R.layout.magenative_webpage, content, true);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.backbutton);
        webView = findViewById(R.id.MageNative_webview);
        progressBar = findViewById(R.id.MageNative_loader);
        spinKitView = findViewById(R.id.spin_kit);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setUseWideViewPort(true);
        webView.addJavascriptInterface(new Ced_WebAppInterface(this), "Android");
        webView.setWebChromeClient(new WebChromeClient());
        url = getIntent().getStringExtra("link");
        if (getIntent().getStringExtra("fromcheckout") != null) {
            loader = true;
        }
        if (!(url.contains("https") || url.contains("http"))) {
            url = "http://" + url;
        }
        setUpWebViewDefaults(webView);
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("Mobiconnectheader", getResources().getString(R.string.header));
        webView.loadUrl(url, hashMap);
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void setUpWebViewDefaults(WebView webView) {
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setDomStorageEnabled(true);
        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);
        settings.setBuiltInZoomControls(true);

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.HONEYCOMB) {
            settings.setDisplayZoomControls(false);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WebView.setWebContentsDebuggingEnabled(true);
        }

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                super.onReceivedError(view, errorCode, description, failingUrl);
            }

            public void onLoadResource(WebView view, String url) {
                Log.i("URL", "" + url);
                if (url.equals("com")) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            spinKitView.setVisibility(View.GONE);
                            progressBar.setVisibility(View.GONE);
                            invalidateOptionsMenu();
                        }
                    }, 5000);
                    if (loader) {
                        spinKitView.setVisibility(View.GONE);
                        progressBar.setVisibility(View.GONE);
                    } else {
                        spinKitView.setVisibility(View.VISIBLE);
                        progressBar.setVisibility(View.VISIBLE);
                    }
                }
                invalidateOptionsMenu();
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                spinKitView.setVisibility(View.GONE);
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String request) {
                Log.d("REpo", "request: " + request);
                if (request.contains(session.getBase_Url()) || request == null) {
                    return false;
                } else if (request.startsWith("http://") || request.startsWith("https://")) {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(request));
                    Intent chooser = Intent.createChooser(browserIntent, "Open with");
                    startActivity(chooser);
                    return true;
                } else {
                    try {
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(request));
                        view.getContext().startActivity(intent);
                        return true;
                    } catch (Exception e) {
                        Log.i("REpo", "shouldOverrideUrlLoading Exception:" + e);
                        return true;
                    }
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}