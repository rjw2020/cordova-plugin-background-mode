package de.appplant.cordova.plugin.background;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.NotificationChannel;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;
import android.os.Build;
import java.util.Timer;
import java.util.TimerTask;
import android.os.PowerManager;

import static android.os.PowerManager.PARTIAL_WAKE_LOCK;

public class LocalCastielService extends Service {

    MyBinder myBinder;
    private PendingIntent pintent;
    MyServiceConnection myServiceConnection;
    private int i = 0;
    private String errorStr = "";
    private boolean isOpenDebugModel = false;
    
    @Override
    public void onCreate() {
        super.onCreate();
        Log.e("LocalCastielService", "onCreate");
        if (myBinder == null) {
            myBinder = new MyBinder();
        }
        myServiceConnection = new MyServiceConnection();
 
        //注册广播
//         IntentFilter intentFilter = new IntentFilter();
//         intentFilter.addAction(Intent.ACTION_TIME_TICK);//系统时间，每分钟发送一次
//         intentFilter.addAction("VV_Test");
//         AutoStartBroadcastReceiver myBroadcast = new AutoStartBroadcastReceiver();
//         registerReceiver(myBroadcast, intentFilter);
        
        if(isCurTimerStop){
            StartWakeTimer(1000,1000);
        }else{
            StopCurTimer();
            StartWakeTimer(1000,1000);
        }
    }
    
    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what){              
                case 1:    
                    if(isOpenDebugModel){
                        Toast.makeText(LocalCastielService.this, "Local:VVService被关闭重新启动中。。。", Toast.LENGTH_SHORT).show();
                    }
                    LocalCastielService.this.startService(new Intent(LocalCastielService.this, VVServer.class));
                    break;            
                case 2:   
                    if(isOpenDebugModel)
                    {
                        Toast.makeText(LocalCastielService.this, "Local:线程内弹出", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case 3:
                    if(isOpenDebugModel){
                        Toast.makeText(LocalCastielService.this, "Local:定时器检测VVService是否被关闭", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case 4: 
                    if(isOpenDebugModel){
                        Toast.makeText(LocalCastielService.this, "Local:时间到了，由Local服务拉起程序", Toast.LENGTH_SHORT).show();
                    }
                    Log.i("LocalCastielService", "时间到了，由Local服务拉起程序");
                    Intent notificationIntent;
                    notificationIntent = new Intent(LocalCastielService.this, com.limainfo.vv.MainActivity.class);     
                    WakeScreen();    
                    notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP |Intent.FLAG_ACTIVITY_NEW_TASK);      
                    PendingIntent pendingIntent = PendingIntent.getActivity(LocalCastielService.this, 0, notificationIntent, 0);              
                    try           
                    {        
                        pendingIntent.send();     
                    }
                    catch (PendingIntent.CanceledException e)    
                    {       
                        e.printStackTrace();  
                    }
                    break;
            }
            
            return true;
        }
    });

    /**
     * 显示一个普通的通知
     *
     * @param context 上下文
     */
    public void showNotification(Context context,int startId) {
        final String channelId = "vv";
        NotificationCompat.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel channel = new NotificationChannel(channelId, "VV小助手", NotificationManager.IMPORTANCE_DEFAULT);
            context.getSystemService(NotificationManager.class).createNotificationChannel(channel);
            builder = new NotificationCompat.Builder(context, channelId);
        }else {
            builder = new NotificationCompat.Builder(context);
        }
        Log.e("LocalCastielService", "显示一个普通的通知");
        Notification notification = builder
                /**通知首次出现在通知栏，带上升动画效果的**/
                .setTicker("VV小助手为您服务")
                /**设置通知的标题**/
                .setContentTitle("VV小助手")
                /**设置通知的内容**/
                .setContentText("vv is running")
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
                .build();
        Log.e("LocalCastielService","notifyId"+String.valueOf(startId));
        startForeground(startId, notification);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        this.bindService(new Intent(this, RemoteCastielService.class), myServiceConnection, Context.BIND_IMPORTANT);
        Log.e("LocalCastielService", "绑定RemoteCastielService服务");
        // showNotification(this,startId );
         
        //测试线程
        new Thread(new Runnable() {
            @Override
            public void run() {
                // while (true){
                //     try{
                //         Thread.sleep(1000);
                //         WakePage();
                //     }catch (Exception e){
                //
                //     }
                // }
                WakePage();
            }
        }).start();
        
        
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                    Message messageF = new Message(); 
                    messageF.what = 3;
                    handler.sendMessage(messageF); 
                if(!MyJobService.isServiceWork(LocalCastielService.this,"de.appplant.cordova.plugin.background.VVServer")){                                  
                    Message message = new Message(); 
                    message.what = 1;
                    handler.sendMessage(message); 
                }
            }
        }, 0, 5000);//5秒检测一次
        
        return START_STICKY;
    }
  
    public  class MyServiceConnection implements ServiceConnection {
        @Override
        public void onServiceConnected(ComponentName arg0, IBinder arg1) {
            Log.e("LocalCastielService", "远程服务连接成功");
        }
        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            Log.e("LocalCastielService", "远程服务Remote被干掉");
            // 连接出现了异常断开了，RemoteService被杀掉了
            // 启动RemoteCastielService
            LocalCastielService.this.startService(new Intent(LocalCastielService.this, RemoteCastielService.class));
            LocalCastielService.this.bindService(new Intent(LocalCastielService.this, RemoteCastielService.class),
                    myServiceConnection, Context.BIND_IMPORTANT);
        }

    }

    static class MyBinder extends CastielProgressConnection.Stub {
        @Override
        public String getProName() throws RemoteException {
            return "Local";
        }
    }

    @Override
    public IBinder onBind(Intent arg0) {
        return myBinder;
    }

    @Override
    public void onDestroy() {
        this.unbindService(myServiceConnection);
        releaseWakeLock();
        super.onDestroy();
    }
    
        
    private static Timer curTimer;
    private static TimerTask curTimerTask;
    private static boolean isCurTimerStop = true; 
    
    private Class<?> mClass = null;
    
    private void StartWakeTimer(int delay,int period){
        if (curTimer == null) {
            curTimer = new Timer();
        }
        if (curTimerTask == null) {
            curTimerTask = new TimerTask() {
                @Override
                public void run() {
                    WakePage();
                }
            };
        }

        if(curTimer != null && curTimerTask != null)
        {
            curTimer.schedule(curTimerTask,delay,period);
            isCurTimerStop = false;
        }
    }

    private void StopCurTimer(){
        if (curTimer != null) {
            curTimer.cancel();
            curTimer = null;
        }
        if (curTimerTask != null) {
            curTimerTask.cancel();
            curTimerTask = null;
        }
        isCurTimerStop = true;
    }    
    
    private PowerManager.WakeLock wakeLock;
    private void WakeScreen(){
        PowerManager pm = (PowerManager)getSystemService(POWER_SERVICE);

        releaseWakeLock();
        if (Build.VERSION.SDK_INT < 20) {
            if(pm.isScreenOn()){
                return;
            }
        }
        
        int level = PowerManager.SCREEN_DIM_WAKE_LOCK |
                    PowerManager.ACQUIRE_CAUSES_WAKEUP;
        wakeLock = pm.newWakeLock(
                level, "Locationtion");
        wakeLock.setReferenceCounted(false);
        wakeLock.acquire(1000);
    }
    
    private void releaseWakeLock() {
        if (wakeLock != null && wakeLock.isHeld()) {
            wakeLock.release();
            wakeLock = null;
        }
    }
    
    private void WakePage(){
                    if(VVServer.wakeMainActivityTime/1000 - System.currentTimeMillis()/1000 == 0)
                    {
                        //如果VVService没有启动 则由本服务拉起activity
                        if(!MyJobService.isServiceWork(LocalCastielService.this,"de.appplant.cordova.plugin.background.VVServer")){                                  
                            WakeScreen();
                            Message message = new Message(); 
                            message.what = 4;
                            handler.sendMessage(message); 
//                             Intent intent = new Intent("VV_Test");       
//                             intent.putExtra("ClassInfo",VVServer.prop.get("class").toString());       
//                             LocalCastielService.this.sendBroadcast(intent);
                        }

                    }        
    }
    
}
