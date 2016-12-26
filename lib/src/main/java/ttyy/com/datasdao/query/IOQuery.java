package ttyy.com.datasdao.query;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Author: Administrator
 * Date  : 2016/12/26 15:39
 * Name  : IOQuery
 * Intro : 数据库IO操作
 * 外部导入数据库
 * 导出数据库到sdcard
 * Modification  History:
 * Date          Author        	 Version          Description
 * ----------------------------------------------------------
 * 2016/12/26    Administrator   1.0              1.0
 */
public class IOQuery {

    protected SQLiteDatabase mDatabase;

    private boolean isDebug;
    private boolean isAppend;

    /**
     * 外部数据库原
     */
    protected InputStream mDBSourceStream;
    /**
     * 导出数据库的目标文件
     */
    protected File mExportDBFile;

    public IOQuery(SQLiteDatabase database) {
        this.mDatabase = database;
    }

    public IOQuery setIsDebug(boolean isDebug) {
        this.isDebug = isDebug;
        return this;
    }

    /**
     * 设置数据数据写入模式
     * 追加模式 不清空原数据 继续追加写入
     * 非追加模式 清空原数据 重新写入
     *
     * @param isAppend
     * @return
     */
    public IOQuery setIsAppendMode(boolean isAppend) {
        this.isAppend = isAppend;
        return this;
    }

    /**
     * 从assets外部数据库导入
     *
     * @param mContext
     * @param assetPath
     * @return
     */
    public IOQuery addAssetsDBSourcePath(Context mContext, String assetPath) {

        if (TextUtils.isEmpty(assetPath))
            return this;

        try {
            InputStream is = mContext.getAssets().open(assetPath);
            addDBSourceStream(is);
        } catch (IOException e) {
        }

        return this;
    }

    /**
     * 从指定外部数据库文件路径导入
     *
     * @param path
     * @return
     */
    public IOQuery addDBSourcePath(String path) {

        if (TextUtils.isEmpty(path))
            return this;

        File mDBSourceFile = new File(path);
        addDBSourceFile(mDBSourceFile);

        return this;
    }

    /**
     * 从指定指定数据库文件导入
     *
     * @param file
     * @return
     */
    public IOQuery addDBSourceFile(File file) {

        if (file == null)
            return this;

        try {
            InputStream is = new FileInputStream(file);
            addDBSourceStream(is);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return this;
    }

    /**
     * 外部数据库输入流
     *
     * @param is
     * @return
     */
    public IOQuery addDBSourceStream(InputStream is) {
        if (is == null)
            return this;

        try {

            if(mDBSourceStream != null){
                mDBSourceStream.close();
            }

            mDBSourceStream = is;
            if (isAppend) {
                startImport();
                mDBSourceStream.close();
                mDBSourceStream = null;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return this;
    }

    /**
     * 开始导入
     */
    public void startImport() {
        if (mDBSourceStream == null) {
            return;
        }

        try {
            OutputStream os = new FileOutputStream(mDatabase.getPath(), isAppend);
            byte[] buffer = new byte[1024];
            int l = 0;
            while ((l = mDBSourceStream.read(buffer)) != -1) {
                os.write(buffer, 0, l);
            }
            os.close();
            mDBSourceStream.close();
            mDBSourceStream = null;
            os = null;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 开始导出
     */
    public void startExport() {
        if (mExportDBFile == null
                || !mExportDBFile.exists())
            return;

        try {
            mDBSourceStream = new FileInputStream(mDatabase.getPath());
            OutputStream os = new FileOutputStream(mExportDBFile);
            byte[] buffer = new byte[1024];
            int l = 0;
            while ((l = mDBSourceStream.read(buffer)) != -1) {
                os.write(buffer, 0, l);
            }
            os.close();
            mDBSourceStream.close();
            mDBSourceStream = null;
            os = null;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
