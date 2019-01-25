package assist.auto.com.accessbilitymanager.data;

import android.content.Context;
import android.view.accessibility.AccessibilityNodeInfo;

import java.util.List;
import java.util.Random;

import assist.auto.com.accessbilitymanager.DataCallBack;
import assist.auto.com.accessbilitymanager.data.bean.CodeInfoBean;

/**
 * 数据中心
 */
public class AbDataCenter
{

    private static AbDataCenter dataControlInstance;

    public static AbDataCenter getInstance() {
        if (null == dataControlInstance)
        {
            synchronized (AbDataCenter.class)
            {
                if (null == dataControlInstance)
                {
                    dataControlInstance = new AbDataCenter();
                }
            }
        }
        return dataControlInstance;
    }

    private Context context;

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }


    private int maxDelay = 2000; //最大操作延迟,毫秒
    private int minDelay = 1000; //最小操作延迟,毫秒

    public void setDelay(int maxDelay, int minDelay) {
        if (maxDelay != 0 && minDelay != 0 && maxDelay > minDelay)
        {
            this.maxDelay = maxDelay;
            this.minDelay = minDelay;
        }
    }

    /**
     * 获取随机操作延迟
     *
     * @return
     */
    public int getRandomDelay() {
        Random random = new Random();
        return random.nextInt(maxDelay) + minDelay;
    }


    private int screenWidth;  //屏幕宽高
    private int screenHeight; //屏幕宽高


    public int getScreenWidth() {
        return screenWidth;
    }

    public void setScreenWidth(int screenWidth) {
        this.screenWidth = screenWidth;
    }

    public int getScreenHeight() {
        return screenHeight;
    }

    public void setScreenHeight(int screenHeight) {
        this.screenHeight = screenHeight;
    }

    private List<CodeInfoBean> mLists; //生码信息

    public List<CodeInfoBean> getmLists() {
        return mLists;
    }

    public void setmLists(List<CodeInfoBean> mLists) {
        this.mLists = mLists;
    }

    //重置数据
    public void resetData() {
        mLists = null;
        dataCallBack = null;
        doTag = false;
        billCallBack = null;
    }

    private DataCallBack.CodeRltCallBack dataCallBack; //生码数据回调接口

    public DataCallBack.CodeRltCallBack getDataCallBack() {
        return dataCallBack;
    }

    public void setDataCallBack(DataCallBack.CodeRltCallBack dataCallBack) {
        this.dataCallBack = dataCallBack;
    }

    private DataCallBack.BillRltCallBack billCallBack; //账单数据回调接口

    public DataCallBack.BillRltCallBack getBillCallBack() {
        return billCallBack;
    }

    public void setBillCallBack(DataCallBack.BillRltCallBack billCallBack) {
        this.billCallBack = billCallBack;
    }

    private boolean doTag;   //是否在工作中

    public boolean isDoTag() {
        return doTag;
    }

    public void setDoTag(boolean doTag) {
        this.doTag = doTag;
    }


    private AccessibilityNodeInfo tempNode;//账单标志位



}
