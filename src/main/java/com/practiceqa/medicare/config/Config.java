package com.practiceqa.medicare.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Settings for a run. Each value is looked up as a system property first
 * (-Dbrowser=firefox), then an environment variable (BROWSER), then config.properties.
 */
public final class Config {
  private static final Properties FILE = load();

  private Config() {}

  private static Properties load() {
    Properties properties = new Properties();
    try (InputStream in = Config.class.getClassLoader().getResourceAsStream("config.properties")) {
      if (in != null) properties.load(in);
    } catch (IOException e) {
      throw new IllegalStateException("Could not read config.properties", e);
    }
    return properties;
  }

  private static String get(String key) {
    String fromSystem = System.getProperty(key);
    if (fromSystem != null && !fromSystem.isBlank()) return fromSystem;
    String fromEnv = System.getenv(key.toUpperCase().replace('.', '_'));
    if (fromEnv != null && !fromEnv.isBlank()) return fromEnv;
    return FILE.getProperty(key, "");
  }

  public static String baseUrl() {
    return get("base.url");
  }

  public static String browser() {
    return get("browser");
  }

  public static boolean headless() {
    return Boolean.parseBoolean(get("headless"));
  }

  public static double slowMo() {
    return Double.parseDouble(get("slow.mo"));
  }

  public static double timeout() {
    return Double.parseDouble(get("timeout"));
  }
}
