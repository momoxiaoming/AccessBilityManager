package assist.auto.com.accessbilitymanager.script;

import android.graphics.Bitmap;
import android.view.accessibility.AccessibilityNodeInfo;

import com.auto.assist.accessibility.api.UiApi;
import com.auto.assist.accessibility.selector.ActionSelector;
import com.auto.assist.accessibility.selector.NodeSelector;
import com.mas.userpayutil.ScreenshotUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import assist.auto.com.accessbilitymanager.data.AbDataCenter;
import assist.auto.com.accessbilitymanager.data.bean.CodeInfoBean;
import assist.auto.com.accessbilitymanager.util.LogUtil;
import assist.auto.com.accessbilitymanager.util.ToolUtil;

public class Wx_CreateCode_Script
{

    /**
     * 开始主操作
     */
    public static void doMain() {


        //直接home,让自己程序后台运行
        UiApi.backHome();


        ToolUtil.startWxApp();

        List<CodeInfoBean> lists = AbDataCenter.getInstance().getmLists();

        List<CodeInfoBean> rlt_lists = new ArrayList<>();

        for (CodeInfoBean bean : lists)
        {
            rlt_lists.add(exeTask(bean));
        }

        //回调结果
        UiApi.backHome();

        AbDataCenter.getInstance().getDataCallBack().onCodeResult(rlt_lists);


    }


    /**
     * 执行脚本
     *
     * @param bean
     * @return
     */
    private static CodeInfoBean exeTask(CodeInfoBean bean) {

        //重置生码状态
        boolean flg = resetState();
        if (flg)
        {
            return createCode(bean);
        }

        return bean;
    }


    //重置生码状态
    private static boolean resetState() {

        //判断是否是设置金额状态
        if (!isSettingState())
        {

            if (isCleanState())
            {
                LogUtil.I("程序在清除金额界面,点击清除金额");

                //程序处于清除金额状态,重置为设置金额状态
                AccessibilityNodeInfo node = UiApi.findNodeByTextWithTimeOut(3000, "清除金额");
                if (node != null)
                {
                    //点击
                    ToolUtil.CmdClick(node);
                    return true;
                } else
                {
                    LogUtil.I("清除金额节点查找出错");
                    return false;
                }
            } else
            {
                LogUtil.I("程序既不在设置金额,又不在清除金额状态,开始进入全套流程");


                //回退桌面
                if (UiApi.backToDesk())
                {
                    //启动微信
                    ToolUtil.startWxApp();
                    return toCodePage();
                }

                return false;

            }

        } else
        {
            LogUtil.I("程序在设置金额界面,可直接生产二维码");

            return true;
        }
    }

    //判断是否是设置金额状态
    public static boolean isSettingState() {
        String codePageStr = "{" +
                "'maxMustMills':3000," +
                "'maxOptionMills':3000," +
                "'must':{'text':['设置金额','收款小账本']}" +
                "}";

        NodeSelector mainPage = NodeSelector.fromJson(codePageStr);
        return UiApi.isMyNeedPage(mainPage);
    }

    //判断是否是清除金额状态
    private static boolean isCleanState() {
        String codePageStr = "{" +
                "'maxMustMills':3000," +
                "'maxOptionMills':3000," +
                "'must':{'text':['清除金额','保存收款码']}" +
                "}";

        NodeSelector mainPage = NodeSelector.fromJson(codePageStr);
        return UiApi.isMyNeedPage(mainPage);
    }


    //前往生码页面
    private static boolean toCodePage() {


        String mainPageStr = "{" +
                "'maxWClickMSec':1000," +
                "'click':{'text':'我'}," +
                "'page':{" +
                "'maxMustMills':5000," +
                "'maxOptionMills':5000," +
                "'must':{'text':['微信','通讯录']}" +
                "}}";


        String mePageStr = "{" +
                "'maxWClickMSec':1000," +
                "'click':{'text':'钱包'}," +
                "'page':{" +
                "'maxMustMills':3000," +
                "'maxOptionMills':3000," +
                "'must':{'text':['钱包','相册']}" +
                "}}";

        String payMentStr = "{" +
                "'maxWClickMSec':1000," +
                "'click':{'text':'收付款'}," +
                "'page':{" +
                "'maxMustMills':3000," +
                "'maxOptionMills':3000," +
                "'must':{'text':['收付款','零钱']}" +
                "}}";

        String payPageStr = "{" +
                "'maxWClickMSec':1000," +
                "'click':{'text':'二维码收款'}," +
                "'page':{" +
                "'maxMustMills':3000," +
                "'maxOptionMills':3000," +
                "'must':{'text':['二维码收款']}" +
                "}}";


        ActionSelector mainPage = ActionSelector.fromJson(mainPageStr);
        ActionSelector mePage =   ActionSelector.fromJson(mePageStr);
        ActionSelector payMent = ActionSelector.fromJson(payMentStr);
        ActionSelector payPage = ActionSelector.fromJson(payPageStr);

        List<ActionSelector> lists = new ArrayList<>();
        lists.add(mainPage);
        lists.add(mePage);
        lists.add(payMent);
        lists.add(payPage);


        return UiApi.jumpToNeedPage(lists);


    }

    private static CodeInfoBean createCode(CodeInfoBean codeInfoBean) {
        //判断是否在二维码界面


        AccessibilityNodeInfo node = UiApi.findNodeByTextWithTimeOut(3000, "设置金额");

        if (node != null)
        {

            //点击
            ToolUtil.CmdClick(node);


            //判断是否在金额输入界面

            String codePageStr = "{" +
                    "'maxMustMills':3000," +
                    "'maxOptionMills':3000," +
                    "'must':{'text':['收款金额','确定']}}";

            NodeSelector mainPage =NodeSelector.fromJson(codePageStr);

            boolean rlt = UiApi.isMyNeedPage(mainPage);
            if (rlt)
            {
                float pri = ((float) codeInfoBean.getCodePrice()) / 100;
                String inputPrice = pri + "";
                //输入金额
                if (UiApi.findNodeByIdAndInput(3000, "com.tencent.mm:id/bx", inputPrice))
                {

                    //验证金额是否正确
                    String node_text = UiApi.findNodeByIdWithTimeOut(3000, "com.tencent.mm:id/bx").getText().toString();

                    if (!inputPrice.equals(node_text)) return codeInfoBean;


                    if (codeInfoBean.getCodeRemarks()!=null&&!"".equals(codeInfoBean.getCodeRemarks()))
                    {

                        if (!UiApi.clickNodeByTextWithTimeOut(3000, "添加收钱备注")) return codeInfoBean;

                        if (!UiApi.findNodeByIdAndInput(3000, "com.tencent.mm:id/hz", codeInfoBean.getCodeRemarks()))
                            return codeInfoBean;


                        //尝试关闭键盘
                        ToolUtil.closeKeyBorad();

                        if (!UiApi.clickNodeByTextWithTimeOut(3000, "确定")) return codeInfoBean;

                    }

                    UiApi.clickNodeByTextWithTimeOut(3000, "确定");


                    if(checkPriceAndRmk(codeInfoBean)){
                        //只有金额和描述通过,才需要设置二维码信息

                        Bitmap map = ScreenshotUtil.screensPicter();
                        if (map == null) return codeInfoBean;

                        String info = ScreenshotUtil.scanbitmap(map);

                        if (ToolUtil.isStringEmpty(info)) return codeInfoBean;

                        codeInfoBean.setCodeInfo(info);
                        LogUtil.D("得到的二维码信息:" + info);

                    }else{

                        LogUtil.E("二维码上的金额或者备注和生成的不一致,舍弃!");
                    }


                    return codeInfoBean;

                } else
                {

                    return codeInfoBean;
                }


            } else
            {
                return codeInfoBean;
            }


        }
        return codeInfoBean;
    }

    /**
     * 检查金额和备注是否正确,防止生成错误
     *
     * @return
     */
    public static boolean checkPriceAndRmk(CodeInfoBean codeInfoBean) {


        HashMap<String, String> map1 = new HashMap<>();
        map1.put("id", "com.tencent.mm:id/am6");
        String price = UiApi.getTextByNode(500, map1);

        HashMap<String, String> map2 = new HashMap<>();
        map2.put("id", "com.tencent.mm:id/am7");
        String rmk = UiApi.getTextByNode(500, map2);


        LogUtil.I("检查二维码上的备注信息: price="+price+" rmk="+rmk);

        if (!"".equals(price))
        {
            double duprice = Double.valueOf(price);
            int itPrict = (int) (duprice * 100);
            if (itPrict != codeInfoBean.getCodePrice())
            {


                return false;
            }

        }


        String benRmk = codeInfoBean.getCodeRemarks();

        //有备注
        if (benRmk != null && !"".equals(benRmk))
        {
            if (!rmk.equals(benRmk))
            {
                return false;
            }

        }else{
            if (!"".equals(rmk))
            {
                return false;
            }

        }


        return true;

    }

}
