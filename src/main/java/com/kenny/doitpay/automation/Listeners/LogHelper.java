package com.kenny.doitpay.automation.Listeners;



import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import com.kenny.doitpay.automation.Helper.CustomCommand;
import java.util.Base64;
import java.io.ByteArrayInputStream;

import io.qameta.allure.Allure;


/**
 * <h1>LogHelper</h1>
 * class utilitas untuk membantu penulisan log ke dalam Extent Report.
 * 
 * <p><b>Fungsi utama:</b></p>
 * <ul>
 *   <li>Mencatat langkah (step) dengan nomor urut otomatis.</li>
 *   <li>Mencatat detail hasil eksekusi (PASS / FAIL).</li>
 *   <li>Mencatat warning atau informasi tambahan.</li>
 * </ul>
 *
 * <p><b>Contoh Penggunaan:</b></p>
 * <pre>
 * LogHelper.resetCounter();
 * LogHelper.step("Buka halaman login");
 * LogHelper.detail("Input username berhasil");
 * LogHelper.pass("Login berhasil");
 * </pre>
 *
 * <p>Kelas ini bekerja bersama {@link ExtentNode} untuk mencatat log dalam report.
 * Jika tidak ada step aktif, log akan ditulis langsung pada parent test.</p>
 *
 * @author Kenny Ramadhan
 * @version 1.0
 */


public class LogHelper {
	
	 /** Counter otomatis untuk penomoran step */
    private static int stepCounter = 1;
    
    /** Menyimpan node step yang sedang aktif */
    private static ExtentTest currentStepNode;
    
    /** Nama test case saat ini (di-set dari TestListeners.onTestStart) */
    private static String currentTestName;
    
    /**
     * Gunakan method ini di Test Listener di method TestListeners.OnTestStart()
     * @param testName
     */
    public static void setCurrentTestName(String testName) {
        currentTestName = testName;
    }
    

    /**
     * Reset counter step ke 1.
     * 
     * <p>Gunakan di awal test case untuk memastikan step dimulai dari STEP 1.</p>
     */
    public static void resetCounter() {
        stepCounter = 1;
    }
    
    
 
    
    /**
     * Gunakan ini di setiap action 
     * @param message Input Nama Step
     */
    public static void step(String message) {
        String stepMessage = "STEP " + stepCounter++ + ": " + message;
        currentStepNode = ExtentNode.createNode(MarkupHelper.createLabel(stepMessage, ExtentColor.BLACK).getMarkup());
        
        Allure.step(message);

    }
    
    
    /**
     * 
     * @param message
     */
    public static void detail(String message) {
        if (currentStepNode != null) {
            currentStepNode.log(Status.INFO, MarkupHelper.createLabel(message, ExtentColor.GREEN).getMarkup());
         
            String screenshotBase64 = CustomCommand.captureScreenshotBase64(message);
            
            try {
                if (screenshotBase64 != null) {
                    // Embed base64 ke report (portable)
                    currentStepNode.addScreenCaptureFromBase64String(screenshotBase64, message);
                    
                    byte[] decodedScreenshot = Base64.getDecoder().decode(screenshotBase64);
                    Allure.addAttachment(message, new ByteArrayInputStream(decodedScreenshot));
                 
                }
            } catch (Exception e) {
                currentStepNode.warning("Gagal attach screenshot: " + e.getMessage());
            }
        } else {
            ExtentNode.getTest().log(Status.INFO, message);
            Allure.step(message);
        }
    }
    
    /**
     * 
     * @param message
     */
    public static void pass(String message) {
        if (currentStepNode != null) {
            currentStepNode.log(Status.PASS,  MarkupHelper.createLabel(message, ExtentColor.GREEN).getMarkup());
        } else {
            ExtentNode.getTest().log(Status.PASS,  MarkupHelper.createLabel(message, ExtentColor.GREEN).getMarkup());
        }
    }
    
    
    /**
     * 
     * @param message
     */
    public static void fail(String message) {
        if (currentStepNode != null) {
            currentStepNode.log(Status.FAIL,  MarkupHelper.createLabel(message, ExtentColor.RED).getMarkup());
        } else {
            ExtentNode.getTest().log(Status.FAIL, message);
        }
    }

    
    
    
    
    


}
