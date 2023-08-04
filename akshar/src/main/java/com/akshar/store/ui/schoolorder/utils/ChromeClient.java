package com.akshar.store.ui.schoolorder.utils;

import android.util.Log;
import android.webkit.ConsoleMessage;
import android.webkit.WebChromeClient;

public class ChromeClient extends WebChromeClient {


    @Override
    public boolean onConsoleMessage(ConsoleMessage msg) {
        Log.d(
                "WebView",
                String.format("%s %s:%d", msg.message(), msg.lineNumber(), msg.sourceId())
        );
        return true;
    }
}
