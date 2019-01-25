package assist.auto.com.accessbilitymanager;

import android.content.Context;
import android.widget.Toast;

import java.util.List;

import assist.auto.com.accessbilitymanager.data.AbDataCenter;
import assist.auto.com.accessbilitymanager.data.bean.CodeInfoBean;
import assist.auto.com.accessbilitymanager.handle.EventHandle;
import assist.auto.com.accessbilitymanager.service.AutoCoreService;
import assist.auto.com.accessbilitymanager.util.LogUtil;
import assist.auto.com.accessbilitymanager.util.ToolUtil;

public class Acessibility
{


    /**
     * 初始化方法
     *
     * @param context
     * @param maxDelay 操作最大延迟,毫秒
     * @param minDelay 操作最小延迟,毫秒
     */
    public static void init(Context context, int maxDelay, int minDelay) {

        if (context == null)
        {
            LogUtil.E("context 不能为空!");
            return;
        }
        //初始化操作延迟
        AbDataCenter.getInstance().setDelay(maxDelay, minDelay);
        AbDataCenter.getInstance().setContext(context);
        AbDataCenter.getInstance().setScreenHeight(ToolUtil.getScreen(context, 2));
        AbDataCenter.getInstance().setScreenWidth(ToolUtil.getScreen(context, 1));


        if (!ToolUtil.isAccessibilityServiceOn(context, AutoCoreService.class))
        {
            Toast.makeText(context, "请打开辅助功能", Toast.LENGTH_SHORT).show();

            ToolUtil.goAccess(context);

        }
    }


    /**
     * 生码任务
     *
     * @param lists 生码信息,如果没有备注,备注一栏可传空或者""
     */
    public static void startCreateCode(List<CodeInfoBean> lists, DataCallBack.CodeRltCallBack callBack) {


        if (lists == null || lists.size() == 0 || callBack == null)
        {
            ToolUtil.showToast("参数输入不能为空!");
            return;
        }

        //重置数据
        AbDataCenter.getInstance().resetData();
        AbDataCenter.getInstance().setmLists(lists);
        AbDataCenter.getInstance().setDataCallBack(callBack);
        EventHandle.startWrok(EventHandle.WorkType.WX_CODE);


    }

    /**
     * 查询账单信息
     */
    public static void queryBillInfo(DataCallBack.BillRltCallBack callBack) {

        if (callBack == null)
        {
            ToolUtil.showToast("参数输入不能为空!");
            return;
        }
        AbDataCenter.getInstance().resetData();
        AbDataCenter.getInstance().setBillCallBack(callBack);

        EventHandle.startWrok(EventHandle.WorkType.WX_BILL);

    }


}
