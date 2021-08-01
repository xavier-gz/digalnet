package gal.sli.digalnet;

import android.webkit.WebViewClient;
import android.app.Activity;
import android.webkit.WebView;

public class WebViewClt extends WebViewClient {

    private Activity activity;

    WebViewClt(Activity activity) {
        this.activity = activity;
    }


    @Override
    public void onReceivedError(WebView webView, int errorCode, String description, String failingUrl) {
        try {
            webView.stopLoading();
        } catch (Exception e) {
        }
        try {
            webView.clearView();
        } catch (Exception e) {
        }
        if (webView.canGoBack()) {
            webView.goBack();
        }

        webView.loadUrl("file:///android_asset/erro.html");
        super.onReceivedError(webView, errorCode, description, failingUrl);
    }

}





