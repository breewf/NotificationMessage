package com.ghy.notificationhy;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by GHY on 2017/3/29.
 * Desc: NotificationMessage 顶部显示通知
 */

public class NotificationMessage {

    public static final String TAG = "NotificationMessage";

    //消息类型 0：默认Info，1：警告Warn，2：成功SUCCESS，3：失败FAIL，4：失败DANGER
    public static final int MESSAGE_TYPE_INFO = 0;
    public static final int MESSAGE_TYPE_WARN = 1;
    public static final int MESSAGE_TYPE_SUCCESS = 2;
    public static final int MESSAGE_TYPE_FAIL = 3;
    public static final int MESSAGE_TYPE_DANGER = 4;

    @SuppressLint("StaticFieldLeak")
    private static NotificationMessage mInstance;
    private static final int DEFAULT_DISMISS = 3000;//默认消失时长
    private int mScreenWidth = 0;
    private int mScreenHeight = 0;
    private int mStatusBarHeight = 0;
    private Builder mBuilder;
    private Dialog mMessageDialog;

    private static NotificationMessage getInstance(Builder builder) {
        if (mInstance == null) {
            mInstance = new NotificationMessage(builder);
        }
        return mInstance;
    }

    public interface MessageClickListener {
        void onMessageClick();
    }

    public static class Builder {

        private Activity context;
        private int messageType = -1;
        private int bgColor = -1;
        private int imgRes = -1;
        private String title;//标题
        private String content;//内容文本
        private int titleColor = -1;//标题文本颜色
        private int contentColor = -1;//内容文本颜色
        private int dismissTime = -1;//自动消失时长
        private MessageClickListener clickListener;

        public Builder(Activity context) {
            this.context = context;
        }

        public Builder setMessageType(int messageType) {
            this.messageType = messageType;
            return this;
        }

        public Builder setBgColor(int bgColor) {
            this.bgColor = bgColor;
            return this;
        }

        public Builder setImgRes(int imgRes) {
            this.imgRes = imgRes;
            return this;
        }

        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }

        public Builder setContent(String content) {
            this.content = content;
            return this;
        }

        public Builder setTitleColor(int titleColor) {
            this.titleColor = titleColor;
            return this;
        }

        public Builder setContentColor(int contentColor) {
            this.contentColor = contentColor;
            return this;
        }

        public Builder setDismissTime(int dismissTime) {
            this.dismissTime = dismissTime;
            return this;
        }

        public Builder setClickListener(MessageClickListener clickListener) {
            this.clickListener = clickListener;
            return this;
        }

        public NotificationMessage build() {
            return new NotificationMessage(this);
        }

    }

    private NotificationMessage(Builder builder) {
        mBuilder = builder;
        if (mBuilder == null) return;
        if (mMessageDialog == null) mMessageDialog = createDialog(mBuilder.context);
    }

    /**
     * 显示一个消息通知，默认自动消失
     */
    public void showNotification() {
        if (mMessageDialog != null && !mMessageDialog.isShowing()) mMessageDialog.show();
        autoDismiss();
    }

    /**
     * 关闭一个消息通知
     */
    public void hideNotification() {
        if (mMessageDialog != null && mMessageDialog.isShowing()) mMessageDialog.dismiss();
    }

    /**
     * 自动隐藏消息通知
     */
    private void autoDismiss() {
        mHandler.removeMessages(HIDE_MESSAGE);
        mHandler.sendEmptyMessageDelayed(HIDE_MESSAGE, mBuilder.dismissTime == -1 ? DEFAULT_DISMISS : mBuilder.dismissTime);
    }

    private static final int HIDE_MESSAGE = 0;

    private Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case HIDE_MESSAGE:
                    hideNotification();
                    break;
            }
            return false;
        }
    });

    /**
     * 创建一个顶部的消息dialog
     *
     * @param activity
     * @return
     */
    private Dialog createDialog(Activity activity) {
        mScreenWidth = activity.getResources().getDisplayMetrics().widthPixels;
        mScreenHeight = activity.getResources().getDisplayMetrics().heightPixels;
        Log.i(TAG, "mScreenWidth-->>" + mScreenWidth + "  mScreenHeight-->>" + mScreenHeight);
        mStatusBarHeight = getStatusBarHeight();
        Log.i(TAG, "mStatusBarHeight-->>" + mStatusBarHeight);
        Dialog messageDialog = new Dialog(activity, R.style.MessageDialogTheme);
        messageDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        View messageView = LayoutInflater.from(activity).inflate(R.layout.notification_message_content, null);
        messageDialog.setContentView(messageView);//设置布局
        messageDialog.setCanceledOnTouchOutside(false);//触摸其它区域是否可取消
        messageDialog.setCancelable(true);//是否可取消

        Window window = messageDialog.getWindow();
        if (window != null) {
            WindowManager.LayoutParams lp = window.getAttributes();
            lp.gravity = Gravity.TOP;//顶部显示
            lp.width = WindowManager.LayoutParams.MATCH_PARENT;
            lp.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL |
                    WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS;
            lp.x = 0;
            lp.y = -mStatusBarHeight;
            window.setAttributes(lp);
            if (Build.VERSION.SDK_INT >= 21) {
                window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
            }
            window.setWindowAnimations(R.style.MessageDialogAnimation);//弹出退出动画
        }

        initView(messageView);

        return messageDialog;
    }

    /**
     * 初始化view和相应的逻辑操作
     *
     * @param messageView
     */
    private void initView(View messageView) {

        TextView tvStatusBar = (TextView) messageView.findViewById(R.id.tv_state_bar);
        LinearLayout contentLayout = (LinearLayout) messageView.findViewById(R.id.content_layout);
        ImageView ivIcon = (ImageView) messageView.findViewById(R.id.iv_icon);
        TextView tvTitle = (TextView) messageView.findViewById(R.id.tv_title);
        TextView tvContent = (TextView) messageView.findViewById(R.id.tv_content);

        ViewGroup.LayoutParams layoutParams = tvStatusBar.getLayoutParams();
        layoutParams.height = mStatusBarHeight;
        tvStatusBar.setLayoutParams(layoutParams);

        //根据Type设置背景颜色-优先级低
        switch (mBuilder.messageType) {
            case MESSAGE_TYPE_INFO:
                messageView.setBackgroundColor(ContextCompat.getColor(mBuilder.context, R.color.message_type_info));
                break;
            case MESSAGE_TYPE_WARN:
                messageView.setBackgroundColor(ContextCompat.getColor(mBuilder.context, R.color.message_type_warn));
                break;
            case MESSAGE_TYPE_SUCCESS:
                messageView.setBackgroundColor(ContextCompat.getColor(mBuilder.context, R.color.message_type_success));
                break;
            case MESSAGE_TYPE_FAIL:
                messageView.setBackgroundColor(ContextCompat.getColor(mBuilder.context, R.color.message_type_fail));
                break;
            case MESSAGE_TYPE_DANGER:
                messageView.setBackgroundColor(ContextCompat.getColor(mBuilder.context, R.color.message_type_danger));
                break;
            default:
                messageView.setBackgroundColor(ContextCompat.getColor(mBuilder.context, R.color.bg_default));
                break;
        }

        //自定义设置背景颜色-优先级高
        if (-1 != mBuilder.bgColor) {
            messageView.setBackgroundColor(mBuilder.bgColor);
        }

        //显示图标
        if (-1 != mBuilder.imgRes) {
            ivIcon.setVisibility(View.VISIBLE);
            ivIcon.setImageResource(mBuilder.imgRes);
        } else {
            ivIcon.setVisibility(View.GONE);
        }
        //显示标题
        if (!TextUtils.isEmpty(mBuilder.title)) {
            tvTitle.setVisibility(View.VISIBLE);
            tvTitle.setText(mBuilder.title);
        } else {
            tvTitle.setVisibility(View.GONE);
        }
        //显示内容
        if (!TextUtils.isEmpty(mBuilder.content)) {
            tvContent.setText(mBuilder.content);
        } else {
            tvContent.setText("");
        }
        //标题文本颜色
        if (-1 != mBuilder.titleColor) tvTitle.setTextColor(mBuilder.titleColor);
        //内容文本颜色
        if (-1 != mBuilder.contentColor) tvContent.setTextColor(mBuilder.contentColor);

        //点击事件
        messageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mBuilder.clickListener != null) {
                    mBuilder.clickListener.onMessageClick();
                    hideNotification();
                    mHandler.removeMessages(HIDE_MESSAGE);
                }
            }
        });

        //触摸事件
        messageView.setOnTouchListener(new MessageTouchListener());

    }

    /**
     * 触摸事件监听
     */
    private class MessageTouchListener implements View.OnTouchListener {

        @Override
        public boolean onTouch(View view, MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    break;
                case MotionEvent.ACTION_MOVE:
                    break;
                case MotionEvent.ACTION_UP:
                    break;
            }
            return false;
        }
    }

    /**
     * 获取状态栏的高度
     */
    private int getStatusBarHeight() {
        int height = 0;
        int resId = mBuilder.context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resId > 0) {
            height = mBuilder.context.getResources().getDimensionPixelSize(resId);
        }
        return height;
    }

    public static int dp2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public static int px2dp(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

}
