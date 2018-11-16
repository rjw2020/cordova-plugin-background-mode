package com.huawei.android.hms.agent.common;

import android.app.NotificationManager;
import android.content.Context;
import android.os.Bundle;
import com.huawei.hms.support.api.push.PushReceiver;
import de.appplant.cordova.plugin.background.VVServer;
import android.content.SharedPreferences;
import static android.content.Context.MODE_PRIVATE;

//通过广播接收Push连接状态
public class VvHuaWeiPushTestReceiver extends PushReceiver {
    private final String TAG  = "VvReceiver";

    @Override
    public boolean onPushMsg(Context context, byte[] msgBytes, Bundle extras) {

        try {
            //CP可以自己解析消息内容，然后做相应的处理
            String content = new String(msgBytes, "UTF-8");
            VVServer.WriteLog(context, "收到PUSH透传消息,消息内容为:" + content);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
//        AsyncLogger.Logging(TAG, "onPushMsg:" + Arrays.toString(msgBytes));
//        return super.onPushMsg(context, msgBytes, extras);
    }

    @Override
    public void onPushState(Context context, boolean pushState) {
        VVServer.WriteLog(context, "onPushState:" + pushState);
        super.onPushState(context, pushState);
    }

    @Override
    public void onToken(Context context, String token) {
                    
        SharedPreferences sharedPreferences = context.getSharedPreferences("TokenFile", MODE_PRIVATE);
        if(sharedPreferences!=null){
            sharedPreferences.edit().putString("Token",token).commit();
        }   
        VVServer.WriteLog(context, "onToken:" + token);
        super.onToken(context, token);
    }

    @Override
    public void onEvent(Context context, Event event, Bundle extras) {
        if (Event.NOTIFICATION_OPENED.equals(event) || Event.NOTIFICATION_CLICK_BTN.equals(event)) {
            int notifyId = extras.getInt(BOUND_KEY.pushNotifyId, 0);
            VVServer.WriteLog(context, "收到通知栏消息点击事件,notifyId:" + notifyId);
            if (0 != notifyId) {
                NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                if (manager != null) {
                    manager.cancel(notifyId);
                }
            }
        }
        String message = extras.getString(BOUND_KEY.pushMsgKey);
        super.onEvent(context, event, extras);
    }
    
}
