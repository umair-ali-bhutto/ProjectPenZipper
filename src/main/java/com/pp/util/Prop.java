package com.pp.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Prop {
  static Properties logProperties;
  
  static {
    if (logProperties == null)
      try {
        InputStream in = Prop.class.getResourceAsStream("Prop.properties");
        logProperties = new Properties();
        logProperties.load(in);
      } catch (IOException e) {
        e.printStackTrace();
      }  
  }
  
  public static String getProperty(String key) {
    return logProperties.getProperty(key);
  }
  
  public static Properties getlogProperties() {
    return logProperties;
  }
  
  public static void setlogProperties(Properties logProperties) {
    Prop.logProperties = logProperties;
  }
}
