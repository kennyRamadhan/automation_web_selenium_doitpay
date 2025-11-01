package com.kenny.doitpay.automation.Config;

import java.util.HashMap;
import java.util.Map;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public class ChromeDriverFactory implements DriverFactory {

    

    @Override
    public WebDriver createDriver() {
        
        // Nonaktifkan logging Netty dan Selenium WebSocket warnings
        System.setProperty("webdriver.chrome.silentOutput", "true");
        System.setProperty("org.slf4j.simpleLogger.defaultLogLevel", "off");
        System.setProperty("io.netty.noUnsafe", "true");
        System.setProperty("io.netty.noKeySetOptimization", "true");
        System.setProperty("io.netty.leakDetection.level", "disabled");
        System.setProperty("io.netty.eventLoopThreads", "1");
        
       
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--start-maximized");
        options.addArguments("--disable-notifications");
        options.addArguments("--disable-infobars");
        options.addArguments("--disable-extensions");
        options.addArguments("--disable-popup-blocking");
        options.addArguments("--disable-save-password-bubble");
        options.addArguments("--incognito");
        
        
        String browserMode = System.getProperty("BROWSER_MODE", "normal");
        if ("headless".equalsIgnoreCase(browserMode)) {
            options.addArguments("--headless=new");
            options.addArguments("--window-size=1920,1080");
        }


        Map<String, Object> prefs = new HashMap<>();
        prefs.put("credentials_enable_service", false);
        prefs.put("profile.password_manager_enabled", false);
        options.setExperimentalOption("prefs", prefs);

        return new ChromeDriver(options);
    }
}
