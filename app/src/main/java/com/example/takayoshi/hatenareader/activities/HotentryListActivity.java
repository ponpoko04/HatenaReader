package com.example.takayoshi.hatenareader.activities;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.example.takayoshi.hatenareader.ApplicationDomain;
import com.example.takayoshi.hatenareader.adapters.ListViewAdapter;
import com.example.takayoshi.hatenareader.models.HatenaRss;
import com.example.takayoshi.hatenareader.network.InputStreamRequest;
import com.example.takayoshi.hatenareader.R;
import com.example.takayoshi.hatenareader.utils.HatenaRssParser;

import java.io.InputStream;
import java.util.List;

public class HotentryListActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private final String REQUEST_URL = "http://b.hatena.ne.jp/hotentry/it.rss";
    private final String TAG_REQUEST_OBJ = "hatena_hotentry_rss_req";
    private List<HatenaRss> hotentries;
    private BaseAdapter adapter;
    private Context context;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_hotentry);

        context = this.getApplicationContext();
        listView = (ListView)findViewById(R.id.listView);
        listView.setOnItemClickListener(this);

        InputStreamRequest request = new InputStreamRequest(REQUEST_URL,
                new Response.Listener<InputStream>() {
                    @Override
                    public void onResponse(InputStream response) {
                        hotentries = new HatenaRssParser().parse(response);
                        adapter = new ListViewAdapter(context, R.layout.hotentry, hotentries);
                        listView.setAdapter(adapter);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        VolleyLog.d(TAG_REQUEST_OBJ, "Error: " + error.getMessage());

                        if( error instanceof NetworkError) {
                        //} else if( error instanceof ClientError) {
                        } else if( error instanceof ServerError) {
                        } else if( error instanceof AuthFailureError) {
                        } else if( error instanceof ParseError) {
                        } else if( error instanceof NoConnectionError) {
                        } else if( error instanceof TimeoutError) {
                        }
                    }
                }
        );

        ApplicationDomain.getInstance().addToRequestQueue(request, TAG_REQUEST_OBJ);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String base = hotentries.get(position).link;
        Uri uri = Uri.parse(base);
        // 単純にブラウザで詳細を表示させる
        Intent i = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(i);
    }
}
