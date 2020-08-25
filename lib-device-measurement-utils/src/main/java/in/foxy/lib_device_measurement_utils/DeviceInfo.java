package in.foxy.lib_device_measurement_utils;

import android.Manifest;
import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;
import java.util.HashMap;
import java.util.Map;

public class DeviceInfo
{
    private static ActivityManager activityManager = null;
    private static StatFs statFs = null;
    private static WindowManager windowManager = null;
    private static HashMap<String,Long> ramStats = null;
    private static HashMap<String,Long> deviceMemoryStats = null;
    private static HashMap<String, String> deviceDisplayStats = null;
    private static HashMap<String,Integer> cameraStats = null;
  
    /**
     * @param: Context context
     */
    public static void initialize(final Context context) {
        activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        statFs = new StatFs(Environment.getDataDirectory().getPath());
        windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        ramStats = new HashMap<String,Long>();
        deviceMemoryStats = new HashMap<String,Long>();
        deviceDisplayStats = new HashMap<String,String>();
        cameraStats = new HashMap<>();
        new Thread(new Runnable() {
            @Override
            public void run () {
                calculateRAMStats(context);
                calculateDeviceMemoryStats();
                calculateDeviceDisplayStats(context);
                calculateCameraMetrics(context);
            }
        }).start();
    }
    
    /**
     * @param context
     */
    private static void calculateRAMStats(Context context) {
        ActivityManager.MemoryInfo memInfo = new ActivityManager.MemoryInfo();
        long mb = 1024*1024;
        if(activityManager != null) {
            activityManager.getMemoryInfo(memInfo);
            ramStats.put("totalRamSize", memInfo.totalMem/mb);
            ramStats.put("availableRamSize", memInfo.availMem/mb);
        }
    }
    
    private static void calculateDeviceMemoryStats() {
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
    private static void calculateDeviceDisplayStats(Context context) {
        Display display = windowManager.getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        display.getMetrics(metrics);
        deviceDisplayStats.put("height", ""+metrics.heightPixels);
        deviceDisplayStats.put("width", ""+metrics.widthPixels);
        deviceDisplayStats.put("scaleDensity", ""+metrics.scaledDensity);
        deviceDisplayStats.put("densityDpi", ""+metrics.densityDpi);
        deviceDisplayStats.put("density", ""+metrics.density);
    }
    
    
    private static void calculateCameraMetrics (Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (context.checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                Log.e("DeviceInfo:calculateCameraMetrics()","permission not granted : CAMERA");
                return;
            }
        }
        int noOfCameras = Camera.getNumberOfCameras();
        Map<String,Object> cameraStat= new HashMap<>();
        for (int i = 0; i < noOfCameras; i++)
        {
            Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
            Camera.getCameraInfo(i, cameraInfo);
            Camera camera = Camera.open(i);
            Camera.Parameters cameraParams = camera.getParameters();
            if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_BACK)
            {
                cameraStat.put("rearCamPictureHeight", cameraParams.getPictureSize().height);
                cameraStat.put("rearCamPictureWidth", cameraParams.getPictureSize().width);
            }else if(cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT){
                cameraStat.put("frontCamPictureHeight", cameraParams.getPictureSize().height);
                cameraStat.put("frontCamPictureWidth", cameraParams.getPictureSize().width);
            }
            camera.release();
        }
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
    
    /**
     * Extracts info : rearCamPictureHeight,rearCamPictureWidth, frontCamPictureHeight,frontCamPictureWidth
     * @return HashMap<String,String>
     */
     public static HashMap<String,Integer> getCameraStats(){
            return cameraStats;
     }
}

