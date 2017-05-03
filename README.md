# NotificationMessage

#### Show a message at the top of your phone that supports multiple message types.


![](https://github.com/breewf/NotificationMessage/blob/master/app/src/main/java/com/ghy/notificationmessage/gif/notification_message_demo.gif?raw=true)

```javascript
NotificationMessage message = new NotificationMessage.Builder(this).setTitle("标题标题")
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
```

Notice: setBgColor() > setMessageType()

##### Add dependency and enjoy it
```javascript
compile 'com.ghy:NotificationMessage:1.0'
```



