package com.ghy.notificationmessage;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.ghy.notificationhy.NotificationMessage;

public class MainActivity extends AppCompatActivity {

    NotificationMessage message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.btn_hide).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (message != null) message.hideNotification();
            }
        });

        findViewById(R.id.btn_show).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showMessage();
            }
        });

        findViewById(R.id.btn_info).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showMessageInfo();
            }
        });

        findViewById(R.id.btn_warn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showMessageWarn();
            }
        });

        findViewById(R.id.btn_success).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showMessageSuccess();
            }
        });

        findViewById(R.id.btn_fail).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showMessageFail();
            }
        });

        findViewById(R.id.btn_danger).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showMessageDanger();
            }
        });

        findViewById(R.id.btn_custom).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showMessageCustom();
            }
        });

    }

    private void showMessage() {
        message = new NotificationMessage.Builder(this).setTitle("标题标题")
                .setContent("您已经进入了没有网络的异次元")
                .setBgColor(ContextCompat.getColor(this, R.color.colorAccent))
                .setMessageType(NotificationMessage.MESSAGE_TYPE_SUCCESS)
                .setClickListener(new NotificationMessage.MessageClickListener() {
                    @Override
                    public void onMessageClick() {
                        Toast.makeText(MainActivity.this, "再点一下，手机爆炸！", Toast.LENGTH_SHORT).show();
                    }
                }).build();

        message.showNotification();
    }

    private void showMessageInfo() {
        NotificationMessage message = new NotificationMessage.Builder(this).setTitle("NotificationMessage")
                .setContent("天青色等烟雨，而我在等你~")
                .setImgRes(R.mipmap.ic_launcher)
                .setMessageType(NotificationMessage.MESSAGE_TYPE_INFO)
                .build();
        message.showNotification();
    }

    private void showMessageWarn() {
        NotificationMessage message = new NotificationMessage.Builder(this).setTitle("NotificationMessage")
                .setContent("你在南方的艳阳里大雪纷飞----_----")
                .setMessageType(NotificationMessage.MESSAGE_TYPE_WARN)
                .build();
        message.showNotification();
    }

    private void showMessageSuccess() {
        NotificationMessage message = new NotificationMessage.Builder(this).setTitle("NotificationMessage")
                .setContent("您的隐私信息已上传成功！O(∩_∩)O~")
                .setMessageType(NotificationMessage.MESSAGE_TYPE_SUCCESS)
                .build();
        message.showNotification();
    }

    private void showMessageFail() {
        NotificationMessage message = new NotificationMessage.Builder(this).setTitle("NotificationMessage")
                .setContent("手机尝试自动重启失败，请手动重启！")
                .setMessageType(NotificationMessage.MESSAGE_TYPE_FAIL)
                .build();
        message.showNotification();
    }

    private void showMessageDanger() {
        NotificationMessage message = new NotificationMessage.Builder(this).setTitle("NotificationMessage")
                .setContent("危险，请速速远离您的手机！")
                .setMessageType(NotificationMessage.MESSAGE_TYPE_DANGER)
                .build();
        message.showNotification();
    }

    private void showMessageCustom() {
        NotificationMessage message = new NotificationMessage.Builder(this).setTitle("NotificationMessage")
                .setContent("让我掉下眼泪的，不止昨夜的酒----")
                .setImgRes(R.mipmap.ic_launcher)
                .setBgColor(ContextCompat.getColor(this, R.color.bg_default))
                .setTitleColor(ContextCompat.getColor(this, R.color.colorAccent))
                .setContentColor(ContextCompat.getColor(this, R.color.colorAccent))
                .setDismissTime(4000)
                .setClickListener(new NotificationMessage.MessageClickListener() {
                    @Override
                    public void onMessageClick() {
                        Toast.makeText(MainActivity.this, "让我依依不舍的，不止你的温柔", Toast.LENGTH_LONG).show();
                    }
                })
                .build();
        message.showNotification();
    }

}
