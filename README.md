# android-device-measurement-lib
 This library helps you to fetch all internal devices related info related to 
  - Ram Info 
  - Storage Memory Info
  - Device Display info 
  - Camera Related info 
  
  # How to Use  
  Step 1:  Add it in your root build.gradle at the end of repositories:

	allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
Step 2. Add the dependency

	dependencies {
	        implementation 'com.github.ekanek:android-device-measurement-lib:Tag'
	}
  
  Step 3 : Initialise Library 
  DeviceInfo.initialize(Context context)
  
  # Public Methods 
  
  - DeviceInfo.getDeviceRamInfo(); 
  - DeviceInfo.getDeviceMemoryStats();
  - DeviceInfo.getDeviceDisplayStats();
  - DeviceInfo.getCameraStats();
