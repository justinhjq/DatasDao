package ttyy.com.datasdao.demo;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;

import ttyy.com.datasdao.Datas;

/**
 * Author: hjq
 * Date  : 2017/06/09 21:56
 * Name  : NextActivity
 * Intro : Edit By hjq
 * Version : 1.0
 */
public class NextActivity extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_next);

        new Thread(new Runnable() {
            @Override
            public void run() {
                DB_A find_a = Datas.from("database_a").findQuery(DB_A.class).selectFirst();
                Log.i("Datas", "NextActivity DB_A "+ find_a.tag + " abc " + find_a.abc);
            }
        }).start();

    }
}
