# DatasDao
<br>简化了Android Sqlite数据库的操作
<br>
<br>
# Usage

### 打开数据库
```Java
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
Datas.createSqliteDatabase(db_builder_a);
```
### 获取已经打开的数据库操作对象
```Java
Datas.from("database_a"); // 指定名称database_a
Datas.from(SqliteDatabase); // 根据Sqlitedatabase获取一个操作对象
Datas.core(); // 上一次使用的数据库对象
```
### 支持数据库操作
```Java
// 插入操作
Datas.from("database_a")
     .insertQuery(DB_A.class)
     .insert(insert_a);

// 查找操作/内置函数
DB_A find_a = Datas.from("database_a").findQuery(DB_A.class).selectAt(0);
int count = Datas.from("database_b").findQuery(DB_B.class).count();

// 更新操作
Datas.core().updateQuery(DB_A.class)
            .set("abc = 3")
            .update();

// 删除操作
  Datas.from("database_b").deleteQuery(DB_B.class)
                          .where("B_TAG = 'DB_B'")
                          .delete();

// Alter Sqlite只支持RENAME TO /ADD COLUMN
 Datas.from("database_a")
      .alterQuery(DB_A.class)
      .addColumn("abc", int.class);
```
<br><br><br>源码可见更多可用数据库操作方法
