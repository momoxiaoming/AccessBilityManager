package assist.auto.com.accessbilitymanager.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import assist.auto.com.accessbilitymanager.util.ToolUtil;

public class TimerService extends Service
{
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        new Thread(new Runnable()
        {
            @Override
            public void run() {
                while(true){
                    ToolUtil.sleepTime(5000);

                }

            }
        }).start();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }
}
