package assist.auto.com.accessbilitymanager.script;

import android.view.accessibility.AccessibilityNodeInfo;

import com.auto.assist.accessibility.api.UiApi;
import com.auto.assist.accessibility.selector.ActionSelector;
import com.auto.assist.accessibility.selector.NodeSelector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import assist.auto.com.accessbilitymanager.data.AbDataCenter;
import assist.auto.com.accessbilitymanager.data.bean.BillInfoBean;
import assist.auto.com.accessbilitymanager.data.storage.SharpManager;
import assist.auto.com.accessbilitymanager.util.LogUtil;
import assist.auto.com.accessbilitymanager.util.ToolUtil;

/**
 * 获取微信账单
 */
public class Wx_ObtainBillDetail_Script extends BaseScript
{
    //我的钱包右上角机内支付中心的节点id
    private static final String MY_MONEY_RIGHT_TOP_ID = "com.tencent.mm:id/hh";

    /**
     * 开始主操作
     */
    public static void doMain() {


        //直接home,让自己程序后台运行
        UiApi.backHome();
//


        ToolUtil.startWxApp();

        List<BillInfoBean> lists = exeTask();
        UiApi.back();

        UiApi.backHome();

        AbDataCenter.getInstance().getBillCallBack().onbillResult(lists);

        // 标记
        ToolUtil.tagOrder(lists);

    }


    /**
     * 执行脚本
     *
     * @param
     * @return
     */
    private static List<BillInfoBean> exeTask() {

        //重置生码状态
        boolean flg = resetState();
        if (flg)
        {

            return getBillDataList();
        }

        return new ArrayList<>();
    }


    //重置账单状态状态
    private static boolean resetState() {

        //判断是否是设置金额状态

        if (isMoneyPage())
        {
            //点击账单
            LogUtil.I("程序在支付中心页,直接进入账单");

            return UiApi.clickNodeByTextWithTimeOut(3000, "账单");
        } else
        {
            LogUtil.I("程序不在支付中心页,开始进入全套流程");

            //回退桌面
            if (UiApi.backToDesk())
            {
                //启动微信
                ToolUtil.startWxApp();
                return toBillPage();
            }
            return false;


        }

    }

    private static List<BillInfoBean> getBillDataList() {

        //判断是否在账单页
        List<BillInfoBean> billList = new ArrayList<>();


        if (!isBillPage())
        {
            LogUtil.D("未到达账单页");
            return billList;
        }
        LogUtil.D("已在账单页,开始获取账单列表");


        int titleHeight = ToolUtil.getTitleNodeHeight();


        LogUtil.D("title高度:" + titleHeight);
        //获取收款列表
        while (true)
        {

            List<AccessibilityNodeInfo> lists = ToolUtil.getInSideImageView(titleHeight);

            if (lists != null && lists.size() != 0)
            {
                //点进详情
                LogUtil.I("找到账单条数:" + lists.size());
                for (AccessibilityNodeInfo item : lists)
                {
                    BillInfoBean infoBean = getBillInfo(item);


                    if (infoBean != null)
                    {
                        if (filterBillBean(infoBean))
                        {
                            LogUtil.D("得到的账单详情:" + infoBean.toString());

                            billList.add(infoBean);


                        } else
                        {
                            LogUtil.D("账单已重复或者时间超过,暂时停止");
                            return billList;

                        }

                    } else
                    {
                        LogUtil.D("获取账单详情为空,暂时停止");
                        return null;
                    }

                }

                //滑动半屏
                ToolUtil.perforGlobalSwipe();
            } else
            {
                LogUtil.D("没有找到订单信息");

                break;

            }


        }


        return billList;


    }


    public static boolean filterBillBean(BillInfoBean bean) {
        String order = SharpManager.getInstance().getPayOrder(AbDataCenter.getInstance().getContext());

        LogUtil.D("存储的订单号:" + order);
        if (bean != null)
        {


            if (!"".equals(bean.getReceiptTime()))
            {
                if (!ToolUtil.morezhanTime(bean.getReceiptTime()))
                {
                    return false;
                }
            }

            if (!"".equals(bean.getPayTime()))
            {
                if (!ToolUtil.morezhanTime(bean.getPayTime()))
                    return false;

            }
            if (!"".equals(bean.getTransTime()))
            {
                if (!ToolUtil.morezhanTime(bean.getTransTime()))
                {
                    return false;

                }
            }


            String beanOrder = "";
            if (!"".equals(bean.getPayOrder()))
            {
                beanOrder = bean.getPayOrder();
            }
            if (!"".equals(bean.getOrderNum()))
            {
                beanOrder = bean.getOrderNum();
            }


            LogUtil.D("当前数据的订单号:" + beanOrder);
            if (beanOrder.equals(order))
            {
                LogUtil.D("订单号已重复");
                return false;
            } else
            {

                return true;

            }


        }

        return false;
    }


    //前往账单页面
    private static boolean toBillPage() {

        LogUtil.D("..............正在前往账单页............");
        String mainPageStr = "{" +
                "'maxWClickMSec':1000," +
                "'click':{'text':'我'}," +
                "'page':{" +
                "'maxMustMills':5000," +
                "'maxOptionMills':5000," +
                "'must':{'text':['我','通讯录']}" +
                "}}";


        String mePageStr = "{" +
                "'maxWClickMSec':1000," +
                "'click':{'text':'钱包'}," +
                "'page':{" +
                "'maxMustMills':3000," +
                "'maxOptionMills':3000," +
                "'must':{'text':['钱包','相册']}" +
                "}}";


        String paycentStr = "{" +
                "'maxWClickMSec':1000," +
                "'click':{'id':'" + MY_MONEY_RIGHT_TOP_ID + "'}," +
                "'page':{" +
                "'maxMustMills':3000," +
                "'maxOptionMills':3000," +
                "'must':{'text':['收付款','零钱'],'id':['" + MY_MONEY_RIGHT_TOP_ID + "']}" +
                "}}";

        String payPageStr = "{" +
                "'maxWClickMSec':1000," +
                "'click':{'text':'账单'}," +
                "'page':{" +
                "'maxMustMills':3000," +
                "'maxOptionMills':3000," +
                "'must':{'text':['支付中心','账单']}" +
                "}}";


        ActionSelector mainPage =ActionSelector.fromJson(mainPageStr);
        ActionSelector mePage =ActionSelector.fromJson(mePageStr);
        ActionSelector paycenter =ActionSelector.fromJson(paycentStr);
        ActionSelector payPage =ActionSelector.fromJson(payPageStr);

        List<ActionSelector> lists = new ArrayList<>();
        lists.add(mainPage);
        lists.add(mePage);
        lists.add(paycenter);
        lists.add(payPage);


        return UiApi.jumpToNeedPage(lists);


    }


    /**
     * 获取账单详情
     *
     * @param node
     * @return
     */
    private static BillInfoBean getBillInfo(AccessibilityNodeInfo node) {

        BillInfoBean billInfoBean = null;

        if (node != null)
        {

            //判断是否在屏幕中
            ToolUtil.CmdClick(node);

            //进入详情也
            if (isBillDetailPage())
            {
                //获取各项信息
                LogUtil.D("到达账单详情页,开始获取账单详情信息");


                String price = getNameOrPrice(2);
                String name = getNameOrPrice(1);

                List<AccessibilityNodeInfo> lists = getDetailNodeLists();
                if (lists.size() != 0)
                {

                    String state = getNodeInfo("当前状态", lists);
                    String codeRemarks = getNodeInfo("收款方备注", lists);
                    String receiptTime = getNodeInfo("收款时间", lists);
                    String orderNum = getNodeInfo("转账单号", lists);
                    String transRemarks = getNodeInfo("转账说明", lists);
                    String transTime = getNodeInfo("转账时间", lists);
                    String payCommodity = getNodeInfo("商品", lists);
                    String payFullName = getNodeInfo("商户全称", lists);
                    String payTime = getNodeInfo("支付时间", lists);
                    String payType = getNodeInfo("支付方式", lists);
                    String payOrder = getNodeInfo("交易单号", lists);
                    String payFullOrder = getNodeInfo("商户单号", lists);


                    billInfoBean = new BillInfoBean();
                    billInfoBean.setState(state);
                    billInfoBean.setCodeRemarks(codeRemarks);
                    billInfoBean.setReceiptTime(receiptTime);
                    billInfoBean.setOrderNum(orderNum);
                    billInfoBean.setPrice(price);
                    billInfoBean.setName(name);
//
//
                    billInfoBean.setTransRemarks(transRemarks);
                    billInfoBean.setTransTime(transTime);


                    billInfoBean.setPayCommodity(payCommodity);
                    billInfoBean.setPayFullName(payFullName);
                    billInfoBean.setPayTime(payTime);
                    billInfoBean.setPayType(payType);
                    billInfoBean.setPayOrder(payOrder);
                    billInfoBean.setPayFullOrder(payFullOrder);


                } else
                {
                    LogUtil.D("未找到包含详情信息节点");

                }


            } else
            {
                LogUtil.D("未找到详情页");


            }


        }
        //返回
        UiApi.back();


        return billInfoBean;
    }


    /**
     * 获取包含详情节点的集合
     *
     * @return
     */
    public static List<AccessibilityNodeInfo> getDetailNodeLists() {

        List<AccessibilityNodeInfo> lists = new ArrayList<>();

        HashMap<String, String[]> map = new HashMap<>();
        map.put("desc", new String[]{"当前状态"});
//        map.put("text", new String[]{"当前状态"});

        AccessibilityNodeInfo imgNode = UiApi.findOptionNodeWithTimeOut(3000, map);

        if (imgNode != null)
        {

            AccessibilityNodeInfo imgNodeParent = imgNode.getParent();

            if (imgNodeParent != null)
            {

                int childNum = imgNodeParent.getChildCount();

                for (int i = 0; i < childNum; i++)
                {
                    lists.add(imgNodeParent.getChild(i));

                }


            }


        }


        return lists;
    }


    /**
     * 寻找详解节点集合中需要的信息
     *
     * @param lab
     * @param list
     * @return
     */
    private static String getNodeInfo(String lab, List<AccessibilityNodeInfo> list) {

        for (int i = 0; i < list.size(); i++)
        {

            AccessibilityNodeInfo item = list.get(i);

            CharSequence des = item.getContentDescription();
            CharSequence text = item.getText();

            if (des != null && des.toString().equals(lab))
            {
                //
                AccessibilityNodeInfo infoNode = list.get(i + 1);

                if (infoNode != null && infoNode.getChildCount() > 0 && infoNode.getChild(0) != null)
                {

                    infoNode = infoNode.getChild(0);

                    CharSequence infoChar = infoNode.getContentDescription();
                    if (infoChar != null)
                    {

                        return infoChar.toString();
                    }
                }


            }

            if (text != null && text.toString().equals(lab))
            {
                //
                AccessibilityNodeInfo infoNode = list.get(i + 1);
                if (infoNode != null && infoNode.getChildCount() > 0 && infoNode.getChild(0) != null)
                {
                    infoNode = infoNode.getChild(0);
                    CharSequence infoChar = infoNode.getText();
                    if (infoChar != null)
                    {

                        return infoChar.toString();
                    }

                }
            }

        }
        return "";

    }


    /**
     * 1是名字,2是金额
     *
     * @param tag
     * @return
     */
    public static String getNameOrPrice(int tag) {


        AccessibilityNodeInfo imgNode = UiApi.findNodeByClsWithTimeOut(3000, "android.widget.Image");

        if (imgNode != null)
        {

            AccessibilityNodeInfo imgNodeParent = imgNode.getParent();
            if (imgNodeParent != null)
            {

                int childNum = imgNodeParent.getChildCount();

                for (int i = 0; i < childNum; i++)
                {

                    if (i == tag)
                    {
                        AccessibilityNodeInfo nodeInfo = imgNodeParent.getChild(i);

                        CharSequence des = nodeInfo.getContentDescription();
                        CharSequence text = nodeInfo.getText();

                        if (des != null)
                        {

                            return des.toString();
                        }

                        if (text != null)
                        {

                            return text.toString();
                        }

                    }


                }


            }


        }


        return "";

    }


    /**
     * 判断是否在支付中心页面
     *
     * @return
     */
    private static boolean isMoneyPage() {
        String lab = "{" +
                "'maxMustMills':5000," +
                "'maxOptionMills':5000," +
                "'must':{'text':['账单',支付中心]}" +
                "}";

        NodeSelector mainPage =NodeSelector.fromJson(lab);
        return UiApi.isMyNeedPage(mainPage);

    }

    private static boolean isBillDetailPage() {

        String lab = "{" +
                "'maxMustMills':5000," +
                "'maxOptionMills':5000," +
                "'must':{'text':['账单详情']}," +
                "'option':{'desc':['当前状态']}" +
                "}";

        NodeSelector page = NodeSelector.fromJson(lab);
        return UiApi.isMyNeedPage(page);

    }


    /**
     * 判断是否在账单页面
     *
     * @return
     */
    private static boolean isBillPage() {
        String lab = "{" +
                "'maxMustMills':5000," +
                "'maxOptionMills':5000," +
                "'option':{'desc':['全部交易类型']}" +
                "}";


        NodeSelector mainPage =NodeSelector.fromJson(lab);


        return UiApi.isMyNeedPage(mainPage);

    }
}
