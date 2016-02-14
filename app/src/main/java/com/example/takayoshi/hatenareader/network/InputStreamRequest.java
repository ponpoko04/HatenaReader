package com.example.takayoshi.hatenareader.network;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

/**
 * Created by takayoshi on 2016/02/14.
 */
public class InputStreamRequest extends Request<InputStream> {

    private final Response.Listener<InputStream> mListener;

    /**
     *
     * @param method
     * @param url
     * @param listener
     * @param errorListener
     */
    public InputStreamRequest(int method, String url,
                              Response.Listener<InputStream> listener,
                              Response.ErrorListener errorListener) {
        super(method, url, errorListener);
        mListener = listener;
    }

    /**
     *
     * @param url
     * @param listener
     * @param errorListener
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
