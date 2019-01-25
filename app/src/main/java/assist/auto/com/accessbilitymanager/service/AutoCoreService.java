package assist.auto.com.accessbilitymanager.service;

import android.accessibilityservice.AccessibilityService;
import android.view.accessibility.AccessibilityEvent;

import com.auto.assist.accessibility.api.AcessibilityApi;
import assist.auto.com.accessbilitymanager.util.LogUtil;

public class AutoCoreService extends AccessibilityService
{


    @Override
    public void onCreate() {
        super.onCreate();
        LogUtil.E("onCreate");
        AcessibilityApi.setAccessibilityService(this);

    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {

        AcessibilityApi.setAccessibilityEvent(event);
        AcessibilityApi.setAccessibilityService(this);



//        LogUtil.D("接收包名事件: "+event.getPackageName());

//        if(event.getEventType()==AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED){
//            Parcelable data = event.getParcelableData();
//            //判断是否是Notification对象
//            if (data instanceof Notification)
//            {
//
//                Notification notification = (Notification) data;
//                String pkg = (String) event.getPackageName();
//
//                Object fn = notification.extras.get(Notification.EXTRA_TITLE);
//                Object txt = notification.extras.get(Notification.EXTRA_TEXT);
//                if (fn == null || txt == null)
//                {
//                    return;
//                }
//                String fromName = fn.toString();
//                String text = txt.toString();
//                LogUtil.E("监听到通知:From package: " + pkg + "fromName: " + fromName + "  text: " + text);
//
//            }
//        }



    }

    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();
        LogUtil.E("onServiceConnected");

    }

    @Override
    public void onInterrupt() {
        LogUtil.E("onInterrupt");
    }

}
