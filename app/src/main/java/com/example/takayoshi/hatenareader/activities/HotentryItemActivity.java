package com.example.takayoshi.hatenareader.activities;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.takayoshi.hatenareader.R;

import java.util.List;

import static com.example.takayoshi.hatenareader.utils.LogUtils.makeLogTag;

/**
 * ホッテントリ1件を表示するActivityです。
 * @author takayoshi uchida
 */
public class HotentryItemActivity extends AppCompatActivity {

    public static final String TAG_LOAD_URI = makeLogTag(HotentryItemActivity.class);
    public static final String HATEBU_APP_NAME = "com.hatena.android.bookmark";

    private WebView webView;
    private ProgressBar progressBar;
    private FloatingActionButton fab, fab_menu1, fab_menu2;
    private boolean isFabOpen = false, isInstalledHatebu = false;
    private Animation fab_open,fab_close;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_hotentry);

        progressBar = (ProgressBar)findViewById(R.id.progressBar);

        // WebView の設定
        webView = (WebView)findViewById(R.id.webview);
        webView.getSettings().setJavaScriptEnabled(true);   //JS有効化
        webView.getSettings().setLoadWithOverviewMode(true);    //ページ横幅を画面幅に合わせる
        webView.getSettings().setUseWideViewPort(true); //ワイドビューポート対応
        webView.getSettings().setBuiltInZoomControls(true); //ズーム機能
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

        isInstalledHatebu = this.isExistHatebu();

        // FloatingActionButton の設定
        final CoordinatorLayout coordinatorLayout = (CoordinatorLayout)findViewById(R.id.coordinator_layout);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab_menu1 = (FloatingActionButton) findViewById(R.id.fab_menu1);
        if (isInstalledHatebu) fab_menu2 = (FloatingActionButton) findViewById(R.id.fab_menu2);
        fab_open = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_open);
        fab_close = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fab_close);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                animateFAB(isInstalledHatebu);
            }
        });
        fab_menu1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WebView showingView = ((WebView)((View)v.getParentForAccessibility()).findViewById(R.id.webview));
                // 単純にブラウザで表示させる
                Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(showingView.getUrl()));
                startActivity(i);
            }
        });
        if (isInstalledHatebu) {
            fab_menu2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    WebView showingView = ((WebView) ((View) v.getParentForAccessibility()).findViewById(R.id.webview));

                    try {
                        // はてなブックマーク追加をIntentで直コールする
                        Intent intent = new Intent(Intent.ACTION_SEND);
                        intent.setClassName(HATEBU_APP_NAME, "com.hatena.android.bookmark.PostActivity");
                        intent.setType("text/plain");
                        intent.putExtra(Intent.EXTRA_TEXT, showingView.getUrl());
                        startActivity(intent);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }
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

    public void animateFAB(boolean isInstalledHatebu){
        if(isFabOpen){
            fab_menu1.startAnimation(fab_close);
            if (isInstalledHatebu) fab_menu2.startAnimation(fab_close);
            fab_menu1.setClickable(false);
            if (isInstalledHatebu) fab_menu2.setClickable(false);
            isFabOpen = false;
            Log.d("Fab_menu", "close");
        } else {
            fab_menu1.startAnimation(fab_open);
            if (isInstalledHatebu) fab_menu2.startAnimation(fab_open);
            fab_menu1.setClickable(true);
            if (isInstalledHatebu) fab_menu2.setClickable(true);
            isFabOpen = true;
            Log.d("Fab_menu", "open");
        }
    }

    /**
     * はてなブックマークがインストール済であるか判定
     * @return true:インストール済、false:未インストール
     */
    private boolean isExistHatebu() {
        List<ApplicationInfo> appInfoList = getPackageManager().getInstalledApplications(PackageManager.GET_META_DATA);

        for (ApplicationInfo info : appInfoList) {
            if (HATEBU_APP_NAME.equals(info.processName)) {
                return true;
            }
        }
        return false;
    }
}
