package assist.auto.com.accessbilitymanager;

import java.util.List;

import assist.auto.com.accessbilitymanager.data.bean.BillInfoBean;
import assist.auto.com.accessbilitymanager.data.bean.CodeInfoBean;

public interface DataCallBack
{


   public interface CodeRltCallBack{
      void onCodeResult(List<CodeInfoBean> lists);

   }

   public interface BillRltCallBack{
      void onbillResult(List<BillInfoBean> lists);

   }

}
