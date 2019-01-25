package assist.auto.com.accessbilitymanager.data.bean;

/**
 * 账单信息类
 */
public class BillInfoBean
{

    /*****************************正常二维码收款************************/
    /**
     * 头像
     */
    private String txIcon; //头像

    /**
     * 姓名,
     */
    private String name; //姓名

    /**
     * 金额
     */
    private String price;

    /**
     * 当前状态
     */
    private String state;

    /**
     * 收款方备注
     */
    private String codeRemarks; //备注


    /**
     * 收款时间
     */
    private String receiptTime; //收款时间

    /**
     * 转账单号
     */
    private String orderNum;


    /*****************************转账************************/

    /**
     * 转账时间
     */
    private String transTime; //

    /**
     * 转账说明
     */
    private String transRemarks; //



    /*****************************商家支付************************/

    /**
     * 商品
     */
    private String payCommodity; //


    /**
     * 商户全称
     */
    private String payFullName; //

    /**
     * 支付时间
     */
    private String payTime;

    /**
     * 支付方式
     */
    private String payType;


    /**
     * 商户单号
     */
    private String payFullOrder;


    /**
     * 交易单号
     */
    private String payOrder;



    public String getTxIcon() {
        return txIcon;
    }

    public void setTxIcon(String txIcon) {
        this.txIcon = txIcon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }



    public String getTransTime() {
        return transTime;
    }

    public void setTransTime(String transTime) {
        this.transTime = transTime;
    }

    public String getReceiptTime() {
        return receiptTime;
    }

    public void setReceiptTime(String receiptTime) {
        this.receiptTime = receiptTime;
    }

    public String getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(String orderNum) {
        this.orderNum = orderNum;
    }

    public String getCodeRemarks() {
        return codeRemarks;
    }

    public void setCodeRemarks(String codeRemarks) {
        this.codeRemarks = codeRemarks;
    }

    public String getTransRemarks() {
        return transRemarks;
    }

    public void setTransRemarks(String transRemarks) {
        this.transRemarks = transRemarks;
    }


    public String getPayTime() {
        return payTime;
    }

    public void setPayTime(String payTime) {
        this.payTime = payTime;
    }



    public String getPayOrder() {
        return payOrder;
    }

    public void setPayOrder(String payOrder) {
        this.payOrder = payOrder;
    }

    public String getPayCommodity() {
        return payCommodity;
    }

    public void setPayCommodity(String payCommodity) {
        this.payCommodity = payCommodity;
    }

    public String getPayFullName() {
        return payFullName;
    }

    public void setPayFullName(String payFullName) {
        this.payFullName = payFullName;
    }

    public String getPayFullOrder() {
        return payFullOrder;
    }

    public void setPayFullOrder(String payFullOrder) {
        this.payFullOrder = payFullOrder;
    }

    public String getPayType() {
        return payType;
    }

    public void setPayType(String payType) {
        this.payType = payType;
    }

    @Override
    public String toString() {
        return "BillInfoBean{" +
                "txIcon='" + txIcon + '\'' +
                ", name='" + name + '\'' +
                ", price='" + price + '\'' +
                ", state='" + state + '\'' +
                ", codeRemarks='" + codeRemarks + '\'' +
                ", receiptTime='" + receiptTime + '\'' +
                ", orderNum='" + orderNum + '\'' +
                ", transTime='" + transTime + '\'' +
                ", transRemarks='" + transRemarks + '\'' +
                ", payCommodity='" + payCommodity + '\'' +
                ", payFullName='" + payFullName + '\'' +
                ", payTime='" + payTime + '\'' +
                ", payType='" + payType + '\'' +
                ", payFullOrder='" + payFullOrder + '\'' +
                ", payOrder='" + payOrder + '\'' +
                '}';
    }
}
