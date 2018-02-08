package de.appplant.cordova.plugin.background;

import android.app.ActivityManager;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.widget.Toast;

import java.util.List;
import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.IBinder;


import de.appplant.cordova.plugin.background.ForegroundService.ForegroundBinder;
import static android.content.Context.BIND_AUTO_CREATE;

/**
 * Created by loi on 2018/1/8.
 */

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class MyJobService extends JobService {
    // Service that keeps the app awake
    private ForegroundService service;

    // Used to (un)bind the service to with the activity
    private final ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
//             Toast.makeText(MyJobService.this, "ForegroundService已绑定", Toast.LENGTH_LONG).show();
            ForegroundBinder binder = (ForegroundBinder) service;
            MyJobService.this.service = binder.getService();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
//             Toast.makeText(MyJobService.this, "ForegroundService已关闭", Toast.LENGTH_LONG).show();
        }
    };
    
    
    public void startBackgroundService() {
        if (BackgroundMode.isDisabled || BackgroundMode.isBind)
            return;
        Intent intent = new Intent(MyJobService.this, ForegroundService.class);

        try {
//             Toast.makeText(MyJobService.this, "startBackgroundService", Toast.LENGTH_LONG).show();
            MyJobService.this.bindService(intent, connection, BIND_AUTO_CREATE);
            MyJobService.this.startService(intent);
        } catch (Exception e) {
           
        }
        BackgroundMode.isBind = true;
    }    
    
    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
//             Toast.makeText(MyJobService.this, "MyJobService", Toast.LENGTH_LONG).show();
            JobParameters param = (JobParameters) msg.obj;
            jobFinished(param, true);

            Log.e("MyJobService", "JobServer---启动服务");  
            //启动一个服务
            if(!isServiceWork(getApplicationContext(),"de.appplant.cordova.plugin.background.VVServer")){
               Intent i = new Intent(getApplicationContext(), VVServer.class);
               startService(i);
               Log.e("MyJobService", "开始启动服务");
//                Toast.makeText(MyJobService.this, "启动服务VVServer", Toast.LENGTH_LONG).show();
            }else {
//                 Toast.makeText(MyJobService.this, "VVServer-服务已启动", Toast.LENGTH_LONG).show();
                Log.e("MyJobService", "服务已启动");
            }
            
            //启动后台一个服务
            if(!isServiceWork(getApplicationContext(),"de.appplant.cordova.plugin.background.ForegroundService")&&BackgroundMode.inBackground){
                BackgroundMode.isDisabled = false;
                startBackgroundService();
//                 Toast.makeText(MyJobService.this, "启动服务backgroundService", Toast.LENGTH_LONG).show();
            }else {
//                 Toast.makeText(MyJobService.this, "服务backgroundService已启动", Toast.LENGTH_LONG).show();
            }
   
            if(!isServiceWork(getApplicationContext(),"de.appplant.cordova.plugin.background.LocalCastielService")){
                Log.e("MainActivity", "启动LocalCastielService");
                startService(new Intent(getApplicationContext(), LocalCastielService.class));
            }else {
                Log.e("MainActivity", "LocalCastielService已启动");
            }
            if(!isServiceWork(getApplicationContext(),"de.appplant.cordova.plugin.background.RemoteCastielService")){
                Log.e("MainActivity", "启动RemoteCastielService");
                startService(new Intent(getApplicationContext(), RemoteCastielService.class));
            }else {
                Log.e("MainActivity", "RemoteCastielService已启动");
            }
            
            return true;
        }
    });

    // 判断服务是否正在运行
    public static boolean isServiceWork(Context mContext, String serviceName) {
        boolean isWork = false;
        ActivityManager myAM = (ActivityManager) mContext
                .getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> myList = myAM.getRunningServices(100);
        if (myList.size() <= 0) {
            return false;
        }
        for (int i = 0; i < myList.size(); i++) {
            String mName = myList.get(i).service.getClassName().toString();
            if (mName.equals(serviceName)) {
                isWork = true;
                break;
            }
        }
        return isWork;
    }


    @Override
    public boolean onStartJob(JobParameters jobParameters) {
        Log.e("MyJobService", "onStartJob");
        Message m = Message.obtain();
        m.obj = jobParameters;
        handler.sendMessage(m);
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        Log.e("MyJobService", "onStopJob");
        handler.removeCallbacksAndMessages(null);
        return false;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e("MyJobService", "onStartCommand");
        return START_STICKY;
    }
}
