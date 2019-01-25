package assist.auto.com.accessbilitymanager.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Rect;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.view.WindowManager;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.Toast;

import com.auto.assist.accessibility.api.AcessibilityApi;
import com.auto.assist.accessibility.api.UiApi;
import com.auto.assist.accessibility.util.ApiUtil;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import assist.auto.com.accessbilitymanager.data.AbDataCenter;
import assist.auto.com.accessbilitymanager.data.bean.BillInfoBean;
import assist.auto.com.accessbilitymanager.data.storage.SharpManager;


public class ToolUtil
{


    /**
     * type 1 获取宽,2获取 高
     *
     * @param context
     * @param type
     */
    public static int getScreen(Context context, int type) {

        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        int width = wm.getDefaultDisplay().getWidth();
        int height = wm.getDefaultDisplay().getHeight();
        LogUtil.D("屏幕宽高:" + width + "x" + height);
        if (type == 1)
        {
            return width;

        } else
        {
            return height;

        }

    }


    /**
     * 判断指定的应用的辅助功能是否开启,
     *
     * @param context 上下文
     * @param
     * @return 是否开启
     */
    public static boolean isAccessibilityServiceOn(@NonNull Context context, Class cls) {


        return ApiUtil.isAccessibilityServiceOn(context, cls);
    }

    /**
     * 前往开启辅助服务界面
     */
    public static void goAccess(Context context) {
        Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    /**
     * 触发系统rebind通知监听服务
     *
     * @param context      上下文
     * @param serviceClass 辅助功能服务的类
     */
    public static void toggleAccessibilityService(Context context, Class serviceClass) {
        ApiUtil.rebindAccessibilityService(context, serviceClass);
    }

    public static long getCurrentTime() {

        return System.currentTimeMillis();
    }


    public static void sleepTime(long t) {

        try
        {
            Thread.sleep(t);
        } catch (InterruptedException e)
        {
            e.printStackTrace();
        }

    }


    public static void CmdClick(AccessibilityNodeInfo node) {

        Rect rect = new Rect();
        node.getBoundsInScreen(rect);
        LogUtil.D("点击的坐标:x=" + rect.centerX() + " y=" + rect.centerY());
        ApiUtil.perforGlobalClick(rect.centerX(), rect.centerY());

        sleepRandom();
    }


    /**
     * 滑动半屏
     */
    public static void perforGlobalSwipe() {
        int width = AbDataCenter.getInstance().getScreenWidth();
        int height = AbDataCenter.getInstance().getScreenHeight();

        int x1 = width / 2;
        int y1 = height - 1;

        int x2 = width / 2;
        int y2 = height / 2;

        LogUtil.D("开始坐标:x=" + x1 + " y=" + y1);
        LogUtil.D("结束坐标:x=" + x2 + " y=" + y2);


        ApiUtil.perforGlobalSwipe(x1, y1, x2, y2);

        sleepRandom();
    }


    /**
     * 获取屏幕内可见的img点
     *
     * @return
     */
    public static List<AccessibilityNodeInfo> getInSideImageView(int titleHeight) {
        List<AccessibilityNodeInfo> list_new = new ArrayList<>();

        //获取收款列表
        List<AccessibilityNodeInfo> lists = AcessibilityApi.findViewByCls("android.widget.Image");
        if (lists != null)
        {
            for (AccessibilityNodeInfo item : lists)
            {
                if (filteNode(item, titleHeight))
                {
                    list_new.add(item);
                }

            }
        }


        return list_new;
    }

    //过滤
    public static boolean filteNode(AccessibilityNodeInfo node, int height) {

        Rect rect = new Rect();
        node.getBoundsInScreen(rect);

        return node.isClickable() && rect.centerY() < AbDataCenter.getInstance().getScreenHeight() && rect.centerY() > height;

    }


    /**
     * 睡眠随机延迟
     */
    public static void sleepRandom() {
        if (!Config.OPEN_DELAY) return;
        try
        {
            Thread.sleep(AbDataCenter.getInstance().getRandomDelay());
        } catch (InterruptedException e)
        {
            e.printStackTrace();
        }

    }

    /**
     * 启动微信
     */
    public static void startWxApp() {
        startApp(Config.WX_PAKENAM);
        sleepRandom();
    }

    /**
     * 通过命令关闭软键盘
     */
    public static void closeKeyBorad() {
        ApiUtil.closeKeyBorad();
        sleepRandom();
    }


    /**
     * 获取title的默认高度
     *
     * @return
     */
    public static int getTitleNodeHeight() {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy年MM月");

        String time = dateFormat.format(new Date(System.currentTimeMillis()));


        AccessibilityNodeInfo timeNode = UiApi.findNodeByDesWithTimeOut(3000, time);

        if (timeNode != null && timeNode.getParent() != null && timeNode.getParent().getParent() != null)
        {
            AccessibilityNodeInfo parent = timeNode.getParent().getParent();

            Rect rect = new Rect();
            parent.getBoundsInScreen(rect);


            return rect.bottom;


        }

        return Config.DEFULT_ORDER_TITLE_HEIGHT;
    }


    public static boolean morezhanTime(String time) {

        if (time != null && !"".equals(time))
        {
            @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            try
            {
                Date date = dateFormat.parse(time);

                if ((System.currentTimeMillis() - date.getTime()) > Config.DEFULT_REDAD_DATA_TIME * 60 * 60 * 1000)
                {
                    return false;
                } else
                {
                    return true;
                }

            } catch (ParseException e)
            {
                e.printStackTrace();
            }
        }

        return false;
    }

    public static boolean isStringEmpty(String str) {

        return str==null||"".equals(str);
    }

    public static void startApp(String pkg) {

        Context context = AbDataCenter.getInstance().getContext();

        PackageManager packageManager = context.getPackageManager();
        Intent intent = new Intent();
        intent = packageManager.getLaunchIntentForPackage(pkg);
        if (intent == null)
        {
            showToast(pkg+"应用未安装");
        } else
        {
            context.startActivity(intent);
        }

    }

    public static void showToast(String msg){
       Handler handler= new Handler(AbDataCenter.getInstance().getContext().getMainLooper());


       handler.post(new Runnable()
       {
           @Override
           public void run() {
               Toast.makeText(AbDataCenter.getInstance().getContext(), "msg", Toast.LENGTH_SHORT).show();
           }
       });
    }

    /**
     * 标记
     * @param billList
     * @return
     */
    public static List<BillInfoBean> tagOrder(List<BillInfoBean> billList) {

        //保存第一条订单信息的订单号
        if (billList != null && billList.size() != 0)
        {
            BillInfoBean billInfoBean = billList.get(0);
            String beanOrder = "";
            if (!"".equals(billInfoBean.getOrderNum()))
            {
                beanOrder = billInfoBean.getOrderNum();
            }

            if (!"".equals(billInfoBean.getPayOrder()))
            {
                beanOrder = billInfoBean.getPayOrder();
            }

            SharpManager.getInstance().savePayOrder(AbDataCenter.getInstance().getContext(), beanOrder);

        }
        return billList;
    }

    /**
     * 判断手机是否root
     * @return
     */
    public static boolean isRoot(){
        Process process = null;
        try
        {
           process= Runtime.getRuntime().exec("su");
        } catch (IOException e)
        {
            e.printStackTrace();
        }

        return process!=null?true:false;
    }

}
