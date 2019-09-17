package io.study.global.config;

public class AppConfig {
	private volatile static AppConfig instance = null; 

	  public static AppConfig getInstance() { 
	    if (instance == null) { 
	      synchronized (AppConfig.class) { 
	        if(instance == null) { 
	          instance = new AppConfig(); 
	        } 
	      } 
	    } 
	    return instance; 
	  } 
	  private AppConfig() { 
	  } 
}
