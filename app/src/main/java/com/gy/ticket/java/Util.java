package com.gy.ticket.java;

import android.content.Context;

import java.io.File;

/**
 * Created by TWINKLE on 2017/12/26.
 */

public class Util {

    public void delete(Context context) {
        File file_cache = context.getCacheDir();
        deleteFolder(file_cache);
    }

    public void deleteFolder(File file) {
        if (!file.exists())
            return;

        if (file.isDirectory()) {
            File files[] = file.listFiles();
            for (int i = 0; i < files.length; i++) {
                deleteFolder(files[i]);
            }
        }
        file.delete();
    }

}
