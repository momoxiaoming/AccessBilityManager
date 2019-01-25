package assist.auto.com.accessbilitymanager;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import assist.auto.com.accessbilitymanager.data.bean.BillInfoBean;
import assist.auto.com.accessbilitymanager.data.bean.CodeInfoBean;
import assist.auto.com.accessbilitymanager.service.AutoCoreService;
import assist.auto.com.accessbilitymanager.service.TimerService;
import assist.auto.com.accessbilitymanager.util.LogUtil;
import assist.auto.com.accessbilitymanager.util.ToolUtil;

public class MainActivity extends AppCompatActivity
{

    private TextView click_text, onTouch;


    private ImageView imgV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Acessibility.init(getApplicationContext(),0,0);

        startService(new Intent(this, TimerService.class));

        imgV = findViewById(R.id.imageview);


        findViewById(R.id.btn).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {

//                getAllNode();

                Acessibility.queryBillInfo(new DataCallBack.BillRltCallBack()
                {
                    @Override
                    public void onbillResult(List<BillInfoBean> lists) {
                        if(lists!=null){
                            LogUtil.D("最终数据条数:"+lists.size());
                            for (BillInfoBean ben : lists)
                            {
                                LogUtil.D("最终数据: " + ben.toString());

                            }
                        }

                    }
                });
            }


        });
        findViewById(R.id.btn3).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {

                startCode();
            }


        });
        findViewById(R.id.btn2).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                ToolUtil.toggleAccessibilityService(getApplicationContext(), AutoCoreService.class);
            }
        });


    }


    public void startCode() {
        CodeInfoBean ben = new CodeInfoBean();
        ben.setCodePrice(1);
        ben.setCodeRemarks(null);

        CodeInfoBean ben2 = new CodeInfoBean();
        ben2.setCodePrice(50000);
        ben2.setCodeRemarks("abcd");

        CodeInfoBean ben3 = new CodeInfoBean();
        ben3.setCodePrice(499900);
        ben3.setCodeRemarks("12sd32a6");

        List<CodeInfoBean> lists = new ArrayList<>();
        lists.add(ben);
        lists.add(ben2);
        lists.add(ben3);

        Acessibility.startCreateCode( lists, new DataCallBack.CodeRltCallBack()
        {

            @Override
            public void onCodeResult(List<CodeInfoBean> lists) {
                for (CodeInfoBean ben : lists)
                {
                    LogUtil.D("完成的二维码信息:" + ben.toString());

                }
            }
        });
    }
}
