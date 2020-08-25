package Utilities;

import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class FileAndEnv {
	
	public static Map<String, String> fileandenv = new HashMap<String, String>();
	
	public static Properties proMain = new Properties();
	public static Properties proPreset = new Properties();
	
	public static Map<String, String> envAndFile() {
		
		String environment = System.getProperty("env");
		
		try {
			if(environment.equalsIgnoreCase("dev")) {
				FileInputStream devPro = new FileInputStream(System.getProperty("user.dir")+ "/input/dev.properties");
				proMain.load(devPro);
				
				fileandenv.put("ServerURL", proMain.getProperty("ServerUrl"));
				fileandenv.put("Key", proMain.getProperty("wa_key"));
			}else if(environment.equalsIgnoreCase("qa")){
				FileInputStream devPro = new FileInputStream(System.getProperty("user.dir")+ "/input/qa.properties");
				proMain.load(devPro);
				
				fileandenv.put("ServerURL", proMain.getProperty("ServerUrl"));
				fileandenv.put("Key", proMain.getProperty("wa_key"));
			}else if(environment.equalsIgnoreCase("stage")){
				FileInputStream devPro = new FileInputStream(System.getProperty("user.dir")+ "/input/stage.properties");
				proMain.load(devPro);
				
				fileandenv.put("ServerURL", proMain.getProperty("ServerUrl"));
				fileandenv.put("Key", proMain.getProperty("wa_key"));
			}
		} catch (Exception e) {
			System.out.println(e.getStackTrace());
		}
		
	return fileandenv;
		
	}
	
	public static Map<String, String> getConfigReader() {
		
		if(fileandenv == null) {
			fileandenv = envAndFile();
		}
		
		return fileandenv;
	}

}
