package com.kenny.doitpay.automation.Page;

import java.util.HashMap;
import java.util.Map;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.testng.asserts.SoftAssert;

import com.kenny.doitpay.automation.Config.WebDriverManager;
import com.kenny.doitpay.automation.Helper.CustomCommand;
import com.kenny.doitpay.automation.Listeners.LogHelper;

/**
 * Page Object class yang merepresentasikan halaman <b>Login</b> pada situs
 * SauceDemo.
 * <p>
 * class ini mengatur seluruh interaksi pengguna dengan elemen pada halaman
 * login, seperti input username, password, dan tombol login. <br>
 * <br>
 * Disediakan dua pendekatan login:
 * <ul>
 * <li>{@link #loginWithHardcodedCredentials(String)} — menggunakan kredensial
 * bawaan (hardcoded).</li>
 * <li>{@link #loginWithDataDriven(String, String)} — menggunakan kredensial
 * dari data eksternal (Excel, CSV, dsb).</li>
 * </ul>
 *
 * class ini juga menerapkan pola <b>PageFactory</b> untuk inisialisasi elemen
 * dan memanfaatkan {@link CustomCommand} sebagai helper aksi WebDriver.
 * 
 * @author Kenny
 * @since 2025
 */
public class Login {

	/** Utilitas custom untuk aksi klik dan input. */
	private final CustomCommand utils;

	/**
	 * Soft assertion untuk verifikasi yang tidak langsung menghentikan eksekusi.
	 */
	private final SoftAssert softAssert;

	/** Kumpulan kredensial bawaan (default user dan password). */
	private final Map<String, String> credentials = new HashMap<>();
	

	/**
	 * Konstruktor untuk inisialisasi halaman Login.
	 * <p>
	 * Konstruktor ini akan:
	 * <ul>
	 * <li>Menginisialisasi elemen halaman dengan {@link PageFactory}.</li>
	 * <li>Mendaftarkan akun default seperti <code>standard_user</code>,
	 * <code>problem_user</code>, dll.</li>
	 * </ul>
	 */
	public Login() {
		this.utils = new CustomCommand();

		this.softAssert = new SoftAssert();

		PageFactory.initElements(WebDriverManager.getDriver(), this);

		credentials.put("standard_user", "secret_sauce");
		credentials.put("locked_out_user", "secret_sauce");
		credentials.put("problem_user", "secret_sauce");

		credentials.put("performance_glitch_user", "secret_sauce");
		credentials.put("error_user", "secret_sauce");
		credentials.put("visual_user", "secret_sauce");

	}

	// ======================= Locators =======================

	/** Input field untuk username. */
	@FindBy(id = "user-name")
	private WebElement inputUserName;

	/** Input field untuk password. */
	@FindBy(id = "password")
	private WebElement inputPassword;

	/** Tombol Login. */
	@FindBy(id = "login-button")
	private WebElement loginBtn;


	/** Elemen judul halaman (title) yang muncul setelah login berhasil. */
	@FindBy(xpath = "//span[@class=\"title\"]")
	private WebElement verfifySuccessLogin;
	
	/** Pesan error yang muncul ketika login gagal. */
	@FindBy(xpath = "//button[@class='error-button']")
	private WebElement errorMessageLogin;
	
	

	// ======================= Methods =======================

	/**
	 * Melakukan login menggunakan kredensial hardcoded dari daftar {@code credentials}.
	 * <p>
	 * Method ini mendukung pengujian positif (login berhasil) maupun negatif 
	 * (login gagal, seperti user yang dikunci).
	 * </p>
	 *
	 * @param username         nama pengguna yang akan digunakan
	 * @param expectedSuccess  true jika login diharapkan berhasil, false jika login diharapkan gagal
	 */
	@SuppressWarnings("static-access")
	public void loginWithHardcodedCredentials(String username, boolean expectedSuccess) {
	    LogHelper.step("Mulai proses login dengan user: " + username);

	    // Validasi username tersedia
	    if (!credentials.containsKey(username)) {
	        softAssert.fail("Username '" + username + "' tidak dikenali dalam daftar credentials!");
	        return;
	    }

	    String password = credentials.get(username);

	    // Input username dan password
	    utils.sendKeysWhenReady(inputUserName, username);
	    utils.sendKeysWhenReady(inputPassword, password);
	    utils.clickWhenReady(loginBtn);

	    // Tunggu hasil login
	    utils.sleep(1000); // opsional: memberi waktu loading antar skenario

	    try {
	        if (expectedSuccess) {
	            // EXPECTED SUCCESS 
	            utils.verifyElementExist(verfifySuccessLogin);
	            LogHelper.detail("Login berhasil sesuai ekspektasi dengan user: " + username);
	        } else {
	            // EXPECTED FAILURE 
	            utils.verifyElementExist(errorMessageLogin);
	            LogHelper.detail("Login gagal sesuai ekspektasi (negative test) untuk user: " + username);
	        }

	    } catch (Exception e) {
	        // Jika hasil tidak sesuai ekspektasi
	        if (expectedSuccess) {
	            LogHelper.detail("Login gagal padahal diharapkan sukses untuk user: " + username);
	            softAssert.fail("Login gagal padahal diharapkan sukses.");
	        } else {
	            LogHelper.detail("Login berhasil padahal diharapkan gagal untuk user: " + username);
	            softAssert.fail("Login berhasil padahal diharapkan gagal.");
	        }
	    }

	    softAssert.assertAll();
	}

	/**
	 * Melakukan login dengan kredensial yang diperoleh secara dinamis
	 * (data-driven).
	 * <p>
	 * Metode ini cocok digunakan untuk pengujian berbasis data seperti dari Excel,
	 * CSV, atau DataProvider. Login dianggap berhasil jika tidak ada pesan error
	 * dan elemen verifikasi halaman utama tampil.
	 * </p>
	 *
	 * @param username username yang digunakan untuk login.
	 * @param password password yang digunakan untuk login.
	 */
	public void loginWithDataDriven(String username, String password) {

		utils.sendKeysWhenReady(inputUserName, username);
		utils.sendKeysWhenReady(inputPassword, password);
		utils.clickWhenReady(loginBtn);
		if (utils.isElementPresent(errorMessageLogin)) {
			LogHelper.step("Verify Login");
			LogHelper.detail("Login Failed with user :" + username
					+ ", please check again your credential or check your element locator");
			softAssert.fail();
			return;
		}

		utils.verifyElementExist(verfifySuccessLogin);
		boolean succesLogin = verfifySuccessLogin.isDisplayed();
		if (succesLogin) {

			LogHelper.step("Verify Login");
			LogHelper.detail("Login Succesfully with user :" + username);
		} else {
			LogHelper.step("Verify Login");
			LogHelper.detail("Login Failed with user :" + username
					+ ", please check again your credential or check your element locator");
			softAssert.fail();
		}

		softAssert.assertAll();
	}
	
	
	

}