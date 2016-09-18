package com.wangban.yzbbanban.test_nfc_temperature;

import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private ScaleView mPanelView;
    private Button btn;
    private Button btnClean;
    private int persent;
    private boolean isFirst = true;
    private boolean isStart;
    private Thread myThread;
    int i = 0;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    if (persent <= 100) {
                        mPanelView.setText(String.valueOf(persent));
                        mPanelView.setPercent(persent);
                    } else {
                        Toast.makeText(MainActivity.this, "完成", Toast.LENGTH_SHORT).show();
                        isStart=false;
                    }
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mPanelView = (ScaleView) findViewById(R.id.panel_view);
        btn = (Button) findViewById(R.id.btn_start_or_stop);
        btnClean = (Button) findViewById(R.id.btn_clean);
        mPanelView.setText("ban");
        mPanelView.setBackgroundColor(Color.parseColor("#000000"));
        mPanelView.setArcColor(Color.DKGRAY);
        mPanelView.setTextSize(60);
        mPanelView.setArcWidth(60);
        btn.setOnClickListener(new View.OnClickListener() {
                                   @Override
                                   public void onClick(View v) {
                                       if (isFirst) {
                                           isStart = true;
                                           isFirst = false;
                                           btn.setText("暂停");
                                           myThread = new Mythread();
                                           myThread.start();
                                       } else {
                                           isFirst = true;
                                           isStart = false;
                                           btn.setText("开始");
                                       }

                                   }
                               }

        );
        btnClean.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                i = 0;
            }
        });
    }

    private class Mythread extends Thread {
        @Override
        public void run() {
            while (isStart) {
                try {
                    sleep(1000);
                    i++;
                    persent = i;
                    Message msg = Message.obtain();
                    msg.arg1 = persent;
                    msg.what = 1;
                    handler.sendMessage(msg);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
