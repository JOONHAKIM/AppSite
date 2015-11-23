package org.androidtown.appsite;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.TextView;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;

/**
 * Created by K on 2015-11-02.
 */
public class mainSite extends Activity{

    WebView mainWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        TextView topTitle = (TextView) findViewById(R.id.topTitle);
        topTitle.setText(R.string.main_name);

        ImageButton top_arrow_left = (ImageButton) findViewById(R.id.top_arrow_left);

        top_arrow_left.setOnClickListener(new ImageButton.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        ImageButton top_arrow_right = (ImageButton) findViewById(R.id.top_arrow_right);
        top_arrow_right.setVisibility(View.INVISIBLE);

        mainTabListenerOn();
        mainContentToggle(findViewById(R.id.mainMenu1));

       /* mainWebView = (WebView) findViewById(R.id.mainWebView);
        mainWebView.getSettings().setJavaScriptEnabled(true);
        mainWebView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        mainWebView.getSettings().setSupportMultipleWindows(true);
        mainWebView.getSettings().setSupportZoom(true);
        mainWebView.getSettings().setBuiltInZoomControls(false);
        mainWebView.getSettings().setLoadsImagesAutomatically(true);
        mainWebView.getSettings().setUseWideViewPort(true);
        mainWebView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        mainWebView.getSettings().setPluginState(WebSettings.PluginState.ON_DEMAND);
        mainWebView.setInitialScale(30);
        mainWebView.setWebViewClient(new owlWebViewClient());
        mainWebView.loadUrl("http://www.owllab.com/android/index.html");*/

    }

    public void loadInfoHTML(){
        mainWebView = (WebView) findViewById(R.id.mainWebView);
        mainWebView.getSettings().setJavaScriptEnabled(true);
        mainWebView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        mainWebView.getSettings().setSupportMultipleWindows(true);
        mainWebView.getSettings().setSupportZoom(true);
        mainWebView.getSettings().setBuiltInZoomControls(false);
        mainWebView.getSettings().setLoadsImagesAutomatically(true);
        mainWebView.getSettings().setUseWideViewPort(true);
        mainWebView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        mainWebView.getSettings().setPluginState(WebSettings.PluginState.ON_DEMAND);

        String theUrl="http://www.owllab.com/android/index.php?company_name_en=OWLLAB&company_name_ko=아울컴";
        ArrayList<NameValuePair> httpParams = new ArrayList<NameValuePair>();
        httpParams.add(new BasicNameValuePair("company__name_en","OWL"));
        httpParams.add(new BasicNameValuePair("company__name_ko", "아울"));

        Log.i("WEB", theUrl);

        cmsHTTP cmsHTTP = new cmsHTTP();
        cmsHTTP.webView=mainWebView;
        cmsHTTP.encoding="UTF-8";
        cmsHTTP.getPost(theUrl,httpParams);

    }


    public void mainContentToggle(View v){
        findViewById(R.id.main_tab1).setVisibility(View.INVISIBLE);
        findViewById(R.id.main_tab2).setVisibility(View.INVISIBLE);
        findViewById(R.id.main_tab3).setVisibility(View.INVISIBLE);

        if(v.getId() == R.id.mainMenu2){
            findViewById(R.id.main_tab2).setVisibility(View.VISIBLE);
            loadInfoHTML();
        }else if(v.getId() == R.id.mainMenu3){
            findViewById(R.id.main_tab3).setVisibility(View.VISIBLE);
        }else {
            findViewById(R.id.main_tab1).setVisibility(View.VISIBLE);
        }

        findViewById(R.id.mainMenu1).setBackgroundResource(R.drawable.main_menu1);
        findViewById(R.id.mainMenu2).setBackgroundResource(R.drawable.main_menu2);
        findViewById(R.id.mainMenu3).setBackgroundResource(R.drawable.main_menu3);

        if(v.getId() == R.id.mainMenu2){
            v.setBackgroundResource(R.drawable.main_menu2_on);
        }else if(v.getId() == R.id.mainMenu3){
            v.setBackgroundResource(R.drawable.main_menu3_on);
        }else {
            v.setBackgroundResource(R.drawable.main_menu1_on);
        }

    }

    public void mainTabListenerOn(){
        findViewById(R.id.mainMenu1).setOnClickListener(new ImageButton.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainContentToggle(v);
            }
        });

        findViewById(R.id.mainMenu2).setOnClickListener(new ImageButton.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainContentToggle(v);
            }
        });

        findViewById(R.id.mainMenu3).setOnClickListener(new ImageButton.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainContentToggle(v);
            }
        });

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if((keyCode ==KeyEvent.KEYCODE_BACK)&& mainWebView.canGoBack()){
            mainWebView.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private class owlWebViewClient extends WebViewClient{
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }


}


