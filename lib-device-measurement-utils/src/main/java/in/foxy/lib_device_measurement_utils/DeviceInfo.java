package in.foxy.lib_device_measurement_utils;

import android.app.ActivityManager;
import android.content.Context;
import android.os.Environment;
import android.os.StatFs;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

import java.util.HashMap;

public class DeviceInfo
{
    private static ActivityManager activityManager=null;
    private static StatFs statFs=null;
    private static WindowManager windowManager=null;
    private static HashMap<String,Long> ramStats=null;
    private static HashMap<String,Long> deviceMemoryStats=null;
    private static HashMap<String, String> deviceDisplayStats=null;
  
    /**
     * @param: Context context
     */
    public static void initialize(Context context){
        activityManager= (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        statFs = new StatFs(Environment.getDataDirectory().getPath());
        windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        ramStats=new HashMap<String,Long>();
        deviceMemoryStats=new HashMap<String,Long>();
        deviceDisplayStats=new HashMap<String,String>();
        calculateRAMStats(context);
        calculateDeviceMemoryStats();
        calculateDeviceDisplayStats(context);
    }
    
    /**
     * @param context
     */
    private static void calculateRAMStats(Context context){
        ActivityManager.MemoryInfo memInfo = new ActivityManager.MemoryInfo();
        long mb = 1024*1024;
        if(activityManager!=null){
            activityManager.getMemoryInfo(memInfo);
            ramStats.put("totalRamSize", memInfo.totalMem/mb);
            ramStats.put("availableRamSize", memInfo.availMem/mb);
        }
    }
    
    private static void calculateDeviceMemoryStats(){
        long totalBytes = 0;
        long freeBytes = 0;
        long mb = 1024*1024;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR2) {
            long blockSize = statFs.getBlockSizeLong();
            totalBytes = blockSize * statFs.getBlockCountLong();
            freeBytes = blockSize * statFs.getAvailableBlocksLong();
        }else{
            long blockSize = statFs.getBlockSize();
            totalBytes = blockSize * statFs.getBlockCount();
            freeBytes = blockSize * statFs.getAvailableBlocks();
        }
        deviceMemoryStats.put("totalDeviceStorageSize", totalBytes/mb);
        deviceMemoryStats.put("availableDeviceStorageSize", freeBytes/mb);
    
    }
    
    /**
     * @param context
     */
    private static void calculateDeviceDisplayStats(Context context){
        Display display = windowManager.getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        display.getMetrics(metrics);
        deviceDisplayStats.put("height",""+metrics.heightPixels);
        deviceDisplayStats.put("width",""+metrics.widthPixels);
        deviceDisplayStats.put("scaleDensity",""+metrics.scaledDensity);
        deviceDisplayStats.put("densityDpi",""+metrics.densityDpi);
        deviceDisplayStats.put("density",""+metrics.density);
    }
    
    
    /**
     * Extracts info : totalRamSize, availableRamSize
     * @return HashMap<String,Long>
     */
    public static HashMap<String,Long> getDeviceRamInfo(){
        return ramStats;
    }
    
    /**
     * Extracts info : totalDeviceStorageSize, availableDeviceStorageSize
     * @return HashMap<String,Long>
     */
    public static HashMap<String,Long> getDeviceMemoryStats()
    {
        return deviceMemoryStats;
    }
    
    /**
     * Extracts info : height, width,scaleDensity,densityDpi,density
     * @return HashMap<String,String>
     */
    public static HashMap<String,String> getDeviceDisplayStats(){
        return deviceDisplayStats;
    }

}

