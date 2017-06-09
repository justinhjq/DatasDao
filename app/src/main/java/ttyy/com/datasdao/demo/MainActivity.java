package ttyy.com.datasdao.demo;

import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import ttyy.com.datasdao.DaoBuilder;
import ttyy.com.datasdao.Datas;

public class MainActivity extends Activity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tv_create_database:
                DaoBuilder db_builder_a = DaoBuilder.from(this)
                        .setDbDir(getExternalCacheDir().getAbsolutePath())
                        .setDbName("database_a")
                        .setDebug(true)
                        .setVersion(1)
                        .setCallback(new DaoBuilder.Callback() {
                            @Override
                            public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

                            }

                            @Override
                            public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {

                            }
                        });

                DaoBuilder db_builder_b = DaoBuilder.from(this)
                        .setDbDir(getExternalCacheDir().getAbsolutePath())
                        .setDbName("database_b")
                        .setDebug(true)
                        .setVersion(1)
                        .setCallback(new DaoBuilder.Callback() {
                            @Override
                            public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

                            }

                            @Override
                            public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {

                            }
                        });
                Datas.createSqliteDatabase(db_builder_b);
                Datas.createSqliteDatabase(db_builder_a);

                break;
            case R.id.tv_insert:

                DB_A insert_a = new DB_A();
                Datas.from("database_a")
                        .insertQuery(DB_A.class)
                        .insert(insert_a);

                DB_B insert_b = new DB_B();
                Datas.from("database_b")
                        .insertQuery(DB_B.class)
                        .insert(insert_b);

                break;
            case R.id.tv_find:

                DB_A find_a = Datas.from("database_a").findQuery(DB_A.class).selectFirst();
                int count = Datas.from("database_b").findQuery(DB_B.class).count();
                Log.i("Datas", "DB_A "+ find_a.tag + " abc " + find_a.abc);
                Log.i("Datas", "DB_B datas.size() "+count);

                break;
            case R.id.tv_delete:

                Datas.from("database_b").deleteQuery(DB_B.class)
                        .where("B_TAG = 'DB_B'")
                        .delete();

                break;
            case R.id.tv_update:

                Datas.core().updateQuery(DB_A.class)
                        .set("abc = 3")
                        .update();

                break;
            case R.id.tv_alter:

                Datas.from("database_a").alterQuery(DB_A.class)
                        .addColumn("abc", int.class);

                break;
            case R.id.tv_destroy_database:
                boolean result = Datas.destroySqliteDatabase("database_a");
                Log.i("Datas", "destroy database_a "+result);
                break;
            case R.id.tv_export_database:

                String export_path = getExternalFilesDir("export_db").getPath()+"/target.db";
                Datas.from("database_a")
                        .ioQuery()
                        .setDBExportPath(export_path)
                        .startExport();
                break;
            case R.id.tv_import_database:

                String source_path = getExternalFilesDir("export_db").getPath()+"/target.db";
                Datas.from("database_a")
                        .ioQuery()
                        .addDBSourcePath(source_path)
                        .startImport();
                break;
            case R.id.tv_next:

                startActivity(new Intent(this, NextActivity.class));

                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        Datas.from("database_a").deleteQuery(DB_A.class).delete();
        Datas.from("database_b").deleteQuery(DB_B.class).delete();
    }
}
