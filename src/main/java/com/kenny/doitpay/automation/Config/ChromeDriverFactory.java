package com.kenny.doitpay.automation.Config;

import java.util.HashMap;
import java.util.Map;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public class ChromeDriverFactory implements DriverFactory {

    private static final String CHROMEDRIVER_PATH = System.getProperty("user.dir") + "/web-driver/chromedriver";

    @Override
    public WebDriver createDriver() {
        
        // Nonaktifkan logging Netty dan Selenium WebSocket warnings
        System.setProperty("webdriver.chrome.silentOutput", "true");
        System.setProperty("org.slf4j.simpleLogger.defaultLogLevel", "off");
        System.setProperty("io.netty.noUnsafe", "true");
        System.setProperty("io.netty.noKeySetOptimization", "true");
        System.setProperty("io.netty.leakDetection.level", "disabled");
        System.setProperty("io.netty.eventLoopThreads", "1");
        
        // Path ke ChromeDriver
        System.setProperty("webdriver.chrome.driver", CHROMEDRIVER_PATH);

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--start-maximized");
        options.addArguments("--disable-notifications");
        options.addArguments("--disable-infobars");
        options.addArguments("--disable-extensions");
        options.addArguments("--disable-popup-blocking");
        options.addArguments("--disable-save-password-bubble");
        options.addArguments("--incognito");

        Map<String, Object> prefs = new HashMap<>();
        prefs.put("credentials_enable_service", false);
        prefs.put("profile.password_manager_enabled", false);
        options.setExperimentalOption("prefs", prefs);

        return new ChromeDriver(options);
    }
}
