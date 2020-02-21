package com.example.testhttp1;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
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

public class MainActivity extends AppCompatActivity implements View.OnTouchListener {

    final String TAG = "MainActivity";
    static short serversLoadTimes = 0;

    //paras for moving imageview
    final int IMAGE_H = 200;
    final int IMAGE_W = 160;
    private int lastX, lastY;
    private RelativeLayout relativeLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final TextView text01 = (TextView)findViewById(R.id.text01);

        //Animations
        final ImageView iv1 = (ImageView)findViewById(R.id.iv);
        final AnimationDrawable animDrawable1 = (AnimationDrawable) iv1.getBackground();

        relativeLayout = (RelativeLayout) findViewById(R.id.layout1);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(IMAGE_W, IMAGE_H);
        iv1.setLayoutParams(layoutParams);
        iv1.setOnTouchListener(this);

        animDrawable1.start();

        //Buttons
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

    public boolean onTouch(View view, MotionEvent event){
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                lastX = (int) event.getRawX();
                lastY = (int) event.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                //计算出需要移动的距离
                int dx = (int) event.getRawX() - lastX;
                int dy = (int) event.getRawY() - lastY;
                //将移动距离加上，现在本身距离边框的位置
                int left = view.getLeft() + dx;
                int top = view.getTop() + dy;
                //获取到layoutParams然后改变属性，在设置回去
                RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) view
                        .getLayoutParams();
                layoutParams.height = IMAGE_H;
                layoutParams.width = IMAGE_W;
                layoutParams.leftMargin = left;
                layoutParams.topMargin = top;
                view.setLayoutParams(layoutParams);
                //记录最后一次移动的位置
                lastX = (int) event.getRawX();
                lastY = (int) event.getRawY();
                break;
        }
        //刷新界面
        relativeLayout.invalidate();
        return true;
    }

    public void skip(View v){
        Intent intent=new Intent();
        intent.setClass(MainActivity.this, SecondActivity.class);
        startActivity(intent);
    }
}
