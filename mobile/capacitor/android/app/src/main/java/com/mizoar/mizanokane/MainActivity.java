package com.mizoar.mizanokane;

import android.os.Bundle;
import android.webkit.WebView;
import androidx.activity.OnBackPressedCallback;
import com.getcapacitor.BridgeActivity;

public class MainActivity extends BridgeActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                WebView webView = getBridge() != null ? getBridge().getWebView() : null;
                if (webView == null) {
                    finish();
                    return;
                }

                String currentUrl = webView.getUrl();
                if (currentUrl != null && (currentUrl.contains("privasi.html") || currentUrl.contains("syarat.html"))) {
                    if (webView.canGoBack()) {
                        webView.goBack();
                    } else {
                        webView.loadUrl(currentUrl.replaceAll("(privasi|syarat)\\.html.*", "index.html#pengaturan"));
                    }
                    return;
                }

                webView.evaluateJavascript(
                    "(function() { return typeof window.handleAndroidBack === 'function' ? window.handleAndroidBack() : false; })();",
                    value -> {
                        if (!"true".equals(value)) {
                            runOnUiThread(() -> {
                                if (!isFinishing() && !isDestroyed()) {
                                    finish();
                                }
                            });
                        }
                    }
                );
            }
        });
    }
}
