package assist.auto.com.accessbilitymanager.data.bean;

public class CodeInfoBean
{
    /**
     * 金额,单位分
     */
    private int codePrice;  //金额,分

    /**
     * 备注,可为空或者""
     */
    private String codeRemarks; //备注

    /**
     * 二维码信息
     */
    private String codeInfo;

    public int getCodePrice() {
        return codePrice;
    }

    public void setCodePrice(int codePrice) {
        this.codePrice = codePrice;
    }

    public String getCodeRemarks() {
        return codeRemarks;
    }

    public void setCodeRemarks(String codeRemarks) {
        this.codeRemarks = codeRemarks;
    }

    public String getCodeInfo() {
        return codeInfo;
    }

    public void setCodeInfo(String codeInfo) {
        this.codeInfo = codeInfo;
    }

    @Override
    public String toString() {
        return "CodeInfoBean{" +
                "codePrice=" + codePrice +
                ", codeRemarks='" + codeRemarks + '\'' +
                ", codeInfo='" + codeInfo + '\'' +
                '}';
    }



}
