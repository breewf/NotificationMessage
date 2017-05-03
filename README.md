# NotificationMessage

#### Show a message at the top of your phone that supports multiple message types.


![](https://github.com/breewf/NotificationMessage/blob/master/app/src/main/java/com/ghy/notificationmessage/gif/notification_message_demo.gif?raw=true)

```javascript
NotificationMessage message = new NotificationMessage.Builder(this).setTitle("�������")
                .setContent("���Ѿ�������û����������Ԫ")
                .setBgColor(ContextCompat.getColor(this, R.color.colorAccent))
                .setMessageType(NotificationMessage.MESSAGE_TYPE_SUCCESS)
                .setClickListener(new NotificationMessage.MessageClickListener() {
                    @Override
                    public void onMessageClick() {
                        Toast.makeText(MainActivity.this, "�ٵ�һ�£��ֻ���ը��", Toast.LENGTH_SHORT).show();
                    }
                }).build();

        message.showNotification();
```

Notice: setBgColor() > setMessageType()

##### Add dependency and enjoy it
```javascript
compile 'com.ghy:NotificationMessage:1.0'
```



