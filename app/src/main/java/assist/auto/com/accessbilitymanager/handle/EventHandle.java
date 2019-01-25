package assist.auto.com.accessbilitymanager.handle;

import android.widget.Toast;

import assist.auto.com.accessbilitymanager.Acessibility;
import assist.auto.com.accessbilitymanager.data.AbDataCenter;
import assist.auto.com.accessbilitymanager.script.Wx_CreateCode_Script;
import assist.auto.com.accessbilitymanager.script.Wx_ObtainBillDetail_Script;
import assist.auto.com.accessbilitymanager.service.AutoCoreService;
import assist.auto.com.accessbilitymanager.util.ToolUtil;

public class EventHandle
{


   public enum WorkType
    {
        WX_CODE, //微信生码
        WX_BILL, //微信账单信息
        ZFB_CODE, //支付宝生码

    }



    public static void startWrok( WorkType workType) {

        if (AbDataCenter.getInstance().isDoTag())
        {
            Toast.makeText(AbDataCenter.getInstance().getContext(), "有任务在进行中,请等待完成", Toast.LENGTH_SHORT).show();

            return;
        }

        if(!ToolUtil.isRoot()){
            Toast.makeText(AbDataCenter.getInstance().getContext(), "手机需要root才可使用该功能", Toast.LENGTH_SHORT).show();

            return;
        }


        if (!ToolUtil.isAccessibilityServiceOn(AbDataCenter.getInstance().getContext(), AutoCoreService.class))
        {
            Toast.makeText(AbDataCenter.getInstance().getContext(), "请打开辅助功能", Toast.LENGTH_SHORT).show();

            return;

        }

        synchronized (Acessibility.class)
        {


            AbDataCenter.getInstance().setDoTag(true);

            switch (workType)
            {
                case WX_CODE:

                    Wx_CreateCode_Script.doMain();

                    break;

                case WX_BILL:
                    Wx_ObtainBillDetail_Script.doMain();
                    break;
            }
            AbDataCenter.getInstance().setDoTag(false);

        }
    }
}
