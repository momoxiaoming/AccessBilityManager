package assist.auto.com.accessbilitymanager.data.storage;

import android.content.Context;
import android.content.SharedPreferences;

public class SharpManager
{

    private SharedPreferences sharedPreferences;

    private static SharpManager mIntence;

    public static SharpManager getInstance() {
        if (null == mIntence)
        {
            synchronized (SharpManager.class)
            {
                if (null == mIntence)
                {
                    mIntence = new SharpManager();
                }
            }
        }
        return mIntence;
    }


    private static final String DB_FILE_NAME = "accessbillty";

    private static final String ORDERID_KEY = "payOrder";


    /**
     * 保存最后一笔订单号
     * @param context
     * @param order
     */
    public void savePayOrder(Context context, String order) {
        SharedPreferences preferences = context.getSharedPreferences(DB_FILE_NAME, Context.MODE_PRIVATE);

        preferences.edit().putString(ORDERID_KEY, order).apply();

    }

    /**
     * 获取最后一笔订单号
     * @param context
     * @return
     */
    public String getPayOrder(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(DB_FILE_NAME, Context.MODE_PRIVATE);
        return preferences.getString(ORDERID_KEY, "");

    }

}
