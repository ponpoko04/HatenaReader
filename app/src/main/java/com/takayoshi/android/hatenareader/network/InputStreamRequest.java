package com.takayoshi.android.hatenareader.network;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

/**
 * Volley標準API取得外のリクエスト処理用
 * @author takayoshi uchida
 */
public class InputStreamRequest extends Request<InputStream> {

    private final Response.Listener<InputStream> mListener;

    /**
     * コンストラクタ
     * @param method HTTPメソッド
     * @param url アクセスURL
     * @param listener 取得成功時実行リスナー
     * @param errorListener 取得失敗時実行リスナー
     */
    public InputStreamRequest(int method, String url,
                              Response.Listener<InputStream> listener,
                              Response.ErrorListener errorListener) {
        super(method, url, errorListener);
        mListener = listener;
    }

    /**
     * コンストラクタ(GET)
     * @param url アクセスURL
     * @param listener 取得成功時実行リスナー
     * @param errorListener 取得失敗時実行リスナー
     */
    public InputStreamRequest(String url, Response.Listener<InputStream> listener,
                              Response.ErrorListener errorListener) {
        this(Method.GET, url, listener, errorListener);
    }

    @Override
    protected void deliverResponse(InputStream response) {
        mListener.onResponse(response);
    }

    @Override
    protected Response<InputStream> parseNetworkResponse(NetworkResponse response) {
        InputStream is = new ByteArrayInputStream(response.data);
        return Response.success(is, HttpHeaderParser.parseCacheHeaders(response));
    }
}
