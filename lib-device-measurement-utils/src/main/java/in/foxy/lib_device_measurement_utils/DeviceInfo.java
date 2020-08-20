package in.foxy.lib_device_measurement_utils;

import android.app.ActivityManager;
import android.content.Context;
import android.os.Environment;
import android.os.StatFs;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

public class DeviceInfo
{
    /*
     * @returns : Total ram size in MB
     * */
    public static long getTotalRamSize(Context context){
        ActivityManager actManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo memInfo = new ActivityManager.MemoryInfo();
        if(actManager!=null){
            actManager.getMemoryInfo(memInfo);
            return memInfo.totalMem/(1024*1024);
        }

        return 0;
    }

    /*
     * @returns : Free ram size in MB
     * */
    public static long getFreeRamSize(Context context){
        ActivityManager actManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo memInfo = new ActivityManager.MemoryInfo();
        if(actManager!=null){
            actManager.getMemoryInfo(memInfo);
            return memInfo.availMem/(1024*1024);
        }

        return 0;

    }

    /*
     * @returns : total device storage size in MB
     * */
    public static long getTotalStorageSize(){
        StatFs stat = new StatFs(Environment.getDataDirectory().getPath());
        long bytesAvailable = stat.getBlockSizeLong() * stat.getBlockCountLong();
        return bytesAvailable / (1024 * 1024);
    }

    /*
     * @returns : free device storage size in MB
     * */

    public static long getFreeStorageSize(){
        StatFs stat = new StatFs(Environment.getDataDirectory().getPath());
        long bytesAvailable = stat.getBlockSizeLong() * stat.getAvailableBlocksLong();
        return bytesAvailable / (1024 * 1024);
    }

    public static String getScreenResolution(Context context){
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        display.getMetrics(metrics);
        int width = metrics.widthPixels;
        int height = metrics.heightPixels;
        return ""+width+"x"+height;
    }

}

