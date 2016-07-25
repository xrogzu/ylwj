package com.administrator.utils;

import android.content.Context;
import android.os.Environment;

import com.administrator.bean.Constant;

import java.io.File;

/**
 * 文件存储路径工具类
 * Created by Administrator on 2016/4/10.
 */
public class FileStoreUtil {
    /**
     * 获取存储路径
     *
     * @param context
     * @return
     */
    public static File getStorageDirectory(Context context, String name) {
        if (hasSDCardMounted()) {
            File dir = context.getExternalFilesDir(name);
            if (dir != null && (dir.mkdir() || dir.isDirectory())) {
                return dir;
            } else {
                dir = new File(context.getFilesDir(), name);
                if (dir != null && (dir.mkdir() || dir.isDirectory())) {
                    return dir;
                }
            }
        } else {
            File dir = new File(context.getFilesDir(), name);
            if (dir != null && (dir.mkdir() || dir.isDirectory())) {
                return dir;
            }
        }
        return null;
    }

    /**
     * 判断是否存在sd卡
     *
     * @return 存在为true，否则为false
     */
    public static boolean hasSDCardMounted() {
        String state = Environment.getExternalStorageState();
        if (state != null && state.equals(Environment.MEDIA_MOUNTED) ) {
            return true;
        } else {
            return false;
        }
    }
}
