package de.appplant.cordova.plugin.background;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Build;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

public class RemoteCastielService extends Service {
    MyBinder myBinder;
    private PendingIntent pintent;
    MyServiceConnection myServiceConnection;
    private final static int GRAY_SERVICE_ID = 1001;
    private int i =0;
    @Override
    public void onCreate() {
        super.onCreate();
        if (myBinder == null) {
            myBinder = new MyBinder();
        }
        myServiceConnection = new MyServiceConnection();
        Log.e("RemoteCastielService", "onCreate");
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if(!MyJobService.isServiceWork(RemoteCastielService.this,"de.appplant.cordova.plugin.background.VVServer")){
                        Intent intent = new Intent(RemoteCastielService.this, VVServer.class);
                        RemoteCastielService.this.startService(intent);
                }
            }
        }, 5000, 5000);
    }

    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            Log.e("LocalCastielService", String.valueOf(msg.what));
//             Toast.makeText(RemoteCastielService.this,"RemoteCastielService: "+ String.valueOf(msg.what), Toast.LENGTH_SHORT).show();
            return true;
        }
    });

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e("RemoteCastielService", "启动LocalCastielService服务"+flags+" "+startId);
//         Toast.makeText(RemoteCastielService.this, "启动LocalCastielService服务", Toast.LENGTH_LONG).show();
        this.bindService(new Intent(this,LocalCastielService.class), myServiceConnection, Context.BIND_IMPORTANT);
        // showNotification(RemoteCastielService.this,startId);
        return START_STICKY;
    }

    /**
     * 显示一个普通的通知
     *
     * @param context 上下文
     */
    public void showNotification(Context context,int startId) {
        final String channelId = "vv";
        Notification.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel channel = new NotificationChannel(channelId, "VV小助手", NotificationManager.IMPORTANCE_DEFAULT);
            context.getSystemService(NotificationManager.class).createNotificationChannel(channel);
            builder = new Notification.Builder(context, channelId);
        }else {
            builder = new Notification.Builder(context);
        }
        Intent intent = new Intent(this, com.limainfo.vv.MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP |Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
        Log.e("RemoteCastielService", "显示一个普通的通知");
        Notification notification = builder
               /**通知首次出现在通知栏，带上升动画效果的**/
                .setTicker("远程服务")
                /**设置通知的标题**/
                .setContentTitle("VV小助手")
                /**设置通知的内容**/
                .setContentText("VV小助手正在后台运行")
                /**通知产生的时间，会在通知信息里显示**/
                .setWhen(System.currentTimeMillis())
                /**设置该通知优先级**/
                .setPriority(Notification.PRIORITY_DEFAULT)
                /**设置这个标志当用户单击面板就可以让通知将自动取消**/
                .setAutoCancel(true)
                /**设置他为一个正在进行的通知。他们通常是用来表示一个后台任务,用户积极参与(如播放音乐)或以某种方式正在等待,因此占用设备(如一个文件下载,同步操作,主动网络连接)**/
                .setOngoing(false)
                /**向通知添加声音、闪灯和振动效果的最简单、最一致的方式是使用当前的用户默认设置，使用defaults属性，可以组合：**/
                // .setDefaults(Notification.DEFAULT_VIBRATE | Notification.DEFAULT_SOUND)
                // .setContentIntent(pendingIntent)
                .setFullScreenIntent(pendingIntent, true)
                .build();
        Log.e("RemoteCastielService","notifyId"+String.valueOf(GRAY_SERVICE_ID));
        startForeground(GRAY_SERVICE_ID, notification);
    }


    class MyServiceConnection implements ServiceConnection {

        @Override
        public void onServiceConnected(ComponentName arg0, IBinder arg1) {
            Log.e("RemoteCastielService", "本地服务连接成功");
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            Log.e("RemoteCastielService", "本地服务Local被干掉");
            // 连接出现了异常断开了，LocalCastielService被杀死了
//             Toast.makeText(RemoteCastielService.this, "本地服务Local被干掉", Toast.LENGTH_LONG).show();
            // 启动LocalCastielService
            RemoteCastielService.this.startService(new Intent(RemoteCastielService.this,LocalCastielService.class));
            RemoteCastielService.this.bindService(new Intent(RemoteCastielService.this,LocalCastielService.class), myServiceConnection, Context.BIND_IMPORTANT);
        }

    }

        class MyBinder extends CastielProgressConnection.Stub {

            @Override
            public String getProName() throws RemoteException {
                return "";
            }

        }

        @Override
        public IBinder onBind(Intent arg0) {
            return myBinder;
        }
}
