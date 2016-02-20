package com.example.takayoshi.hatenareader.activities;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.takayoshi.hatenareader.R;

import static com.example.takayoshi.hatenareader.utils.LogUtils.makeLogTag;

/**
 * ホッテントリ1件を表示するActivityです。
 * @author takayoshi uchida
 */
public class HotentryItemActivity extends AppCompatActivity {

    public static final String TAG_LOAD_URI = makeLogTag(HotentryItemActivity.class);

    private WebView webView;
    private ProgressBar progressBar;
    private FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_hotentry);

        progressBar = (ProgressBar)findViewById(R.id.progressBar);

        // WebView の設定
        webView = (WebView)findViewById(R.id.webview);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                progressBar.setVisibility(View.VISIBLE);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                progressBar.setVisibility(View.GONE);
            }
        });
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                progressBar.setProgress(newProgress);
            }
        });

        webView.loadUrl(getIntent().getStringExtra(TAG_LOAD_URI));

        // FloatingActionButton の設定
        final CoordinatorLayout coordinatorLayout = (CoordinatorLayout)findViewById(R.id.coordinator_layout);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                WebView showingView = ((WebView)((View)view.getParentForAccessibility()).findViewById(R.id.webview));

                // TODO: (2016/2/20 記)ブックマークなどがまだできないので暫定でクリップボードへコピー
                ClipboardManager clipboard = (ClipboardManager)getSystemService(CLIPBOARD_SERVICE);
                clipboard.setPrimaryClip(ClipData.newPlainText("SHOW_URL", showingView.getUrl()));

                // Material Designガイドラインに則り、Snackbar 表示時にFABと被らないようにするため、CoordinatorLayoutを使用する
                Snackbar.make(coordinatorLayout, "クリップボードにコピーしました！\r\n" + showingView.getUrl(), Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && webView.canGoBack()) {
            // 戻るボタン押下時
            webView.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
