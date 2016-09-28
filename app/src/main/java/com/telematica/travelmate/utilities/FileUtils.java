package com.telematica.travelmate.utilities;

import android.content.Context;
import android.telephony.TelephonyManager;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.text.DecimalFormat;
import java.util.Date;


public class FileUtils {


    /**
     * Empty constructor to prevent instantiation.
     */
    protected FileUtils() {}

    public static String getPreviousDatabase(Context context) {
        return context.getCacheDir() + File.separator + "prev.db";
    }

    public static String getTemporaryDatabase(Context context) {
        return context.getCacheDir() + File.separator + "temp.db";
    }

    public static String getDeviceId(Context context) {
        final String deviceId = ((TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();
        if (deviceId != null) {
            return deviceId;
        } else {
            return android.os.Build.SERIAL;
        }
    }




    /**
     * Copies a file byte for byte.
     *
     * @param fromFile FileInputStream for the file to copy from.
     * @param toFile FileInputStream for the file to copy to.
     */
    public static void copyFile(FileInputStream fromFile, FileOutputStream toFile)
            throws IOException {

        FileChannel fromChannel = null;
        FileChannel toChannel = null;

        try {
            fromChannel = fromFile.getChannel();
            toChannel = toFile.getChannel();
            fromChannel.transferTo(0, fromChannel.size(), toChannel);
        } finally {
            try {
                if (fromChannel != null) {
                    fromChannel.close();
                }
            } finally {
                if (toChannel != null) {
                    toChannel.close();
                }
            }
        }
    }

    public static Date getLastModifyTime(File file) {
        return new Date(file.lastModified());
    }


    public static String readableFileSize(long size) {
        if(size <= 0) return "0";
        final String[] units = new String[] { "B", "kB", "MB", "GB", "TB" };
        int digitGroups = (int) (Math.log10(size)/Math.log10(1024));
        return new DecimalFormat("#,##0.#").format(size/Math.pow(1024, digitGroups)) + " " + units[digitGroups];
    }












}
