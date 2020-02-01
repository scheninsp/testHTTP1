package com.example.testhttp1;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.net.URLEncoder;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    final String TAG = "MainActivity";
    static short serversLoadTimes = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final TextView text01 = (TextView)findViewById(R.id.text01);

        Button button01 = (Button) findViewById(R.id.button01);
        button01.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){

                //get usage
                String url = "http://122.51.211.211:80/api";
                Request request = new Request.Builder().url(url).get().build();

                final OkHttpClient okHttpClient = new OkHttpClient.Builder()
                        .connectTimeout(5, TimeUnit.SECONDS) // 设置连接超时时间
                        .readTimeout(5, TimeUnit.SECONDS) // 设置读取超时时间
                        .build();

                final short maxLoadTimes = 2;

                Call call = okHttpClient.newCall(request);

                call.enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        Log.e(TAG, "onFailure: " + e.getMessage());
                        if(e instanceof SocketTimeoutException && serversLoadTimes < maxLoadTimes) // 如果超时并未超过指定次数，则重新连接
                        {
                            serversLoadTimes++;
                            Log.e(TAG, "Reconnect: " + serversLoadTimes + " times");
                            okHttpClient.newCall(call.request()).enqueue(this);
                        }else {
                            e.printStackTrace();
                        }
                        text01.setText("Message Sent Failed");
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        String result = response.body().string();
                        text01.setText(result);
                        Log.d("result",result);
                    }
                });

                // post usage
                /*
                MediaType mediaType = MediaType.parse("text/x-markdown; charset=utf-8");
                String requestBody = "Sent from Mi8";
                Request request = new Request.Builder().url("http://122.51.211.211/api")
                        .post(RequestBody.create(mediaType, requestBody)).build();

                final OkHttpClient okHttpClient = new OkHttpClient.Builder()
                        .connectTimeout(5, TimeUnit.SECONDS) // 设置连接超时时间
                        .readTimeout(5, TimeUnit.SECONDS) // 设置读取超时时间
                        .build();

                final short maxLoadTimes = 2;

                Call call = okHttpClient.newCall(request);

                //send request
                call.enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        Log.e(TAG, "onFailure: " + e.getMessage());
                        if(e instanceof SocketTimeoutException && serversLoadTimes < maxLoadTimes) // 如果超时并未超过指定次数，则重新连接
                        {
                            serversLoadTimes++;
                            Log.e(TAG, "Reconnect: " + serversLoadTimes + " times");
                            okHttpClient.newCall(call.request()).enqueue(this);
                        }else {
                            e.printStackTrace();
                        }
                        text01.setText("Message Sent Failed");
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        text01.setText("Message Sent Success");
                        String result = response.body().string();
                        Log.d("result",result);
                    }
                });
                */
            }

        });
    }
}
