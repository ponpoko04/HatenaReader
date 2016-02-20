package com.example.takayoshi.hatenareader;

import android.app.Application;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.takayoshi.hatenareader.network.InputStreamRequest;

/**
 * Singletonオブジェクト
 * @author takayoshi uchida
 */
public class ApplicationDomain extends Application {

    public static final String TAG = ApplicationDomain.class.getSimpleName();

    private RequestQueue mRequestQueue;

    private static ApplicationDomain mInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
    }

    public static synchronized ApplicationDomain getInstance() {
        return mInstance;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        return mRequestQueue;
    }

    /**
     * JSON取得用
     * @param req リクエストオブジェクト
     * @param tag 識別タグ
     */
    public void addToRequestQueue(JsonObjectRequest req, String tag) {
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);
    }

    /**
     * その他取得用
     * @param req リクエストオブジェクト
     * @param tag
     */
    public void addToRequestQueue(InputStreamRequest req, String tag) {
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);
    }

    /**
     * その他取得用(タグなし)
     * @param req リクエストオブジェクト
     * @param <T> リクエスト型
     */
    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag(TAG);
        getRequestQueue().add(req);
    }

    /**
     * リクエストキャンセル
     * @param tag 識別タグ
     */
    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }
}
