package com.kenny.doitpay.automation.Web;

import org.testng.annotations.Test;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.util.Map;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;



import com.kenny.doitpay.automation.Helper.UtilsDataDriven;

import com.kenny.doitpay.automation.Page.Login;
import com.opencsv.exceptions.CsvException;

import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;



/**
 * LoginTest adalah class test yang mencakup pengujian login pada aplikasi web
 * menggunakan pendekatan Data-Driven Testing (DDT) dan kredensial hardcoded.
 * <p>
 * Test ini memastikan bahwa fitur login bekerja sesuai harapan untuk skenario valid maupun invalid.
 * </p>
 * @author Kenny
 * @since 2025
 */
public class LoginTest extends BaseTest {

	private Login login;


	 /**
     * Inisialisasi objek Page Object Login sebelum setiap test method dijalankan.
     */
	@BeforeMethod
	public void setUpPage() {

		login = new Login();
		
	}
	
	
	/**
	 * Provider data untuk membaca file excel maupun csv, gunakan path dan sheet
	 * name untuk excel dan gunakan path file serta kosongkan sheet name untuk csv
	 * 
	 * @return csv files/excel files
	 * @throws CsvException
	 */
	@DataProvider(name = "csvData")
	public Object[][] getCSVData() throws CsvException {
		String csvPath = System.getProperty("user.dir")
				+ "/src/main/java/com/kenny/doitpay/automation/Resources/data.csv";
		return UtilsDataDriven.getTestData(csvPath, null);
	}

    /**
     * Test case untuk melakukan pengujian login menggunakan pendekatan 
     * <b>Data-Driven Testing (DDT)</b> dengan sumber data dari file CSV.
     * <p>
     * Data CSV harus memiliki minimal dua kolom:
     * <ul>
     *   <li><b>username</b> – kredensial pengguna</li>
     *   <li><b>password</b> – kata sandi pengguna</li>
     * </ul>
     * 
     * <p>
     * Alur pengujian:
     * <ol>
     *   <li>Membaca data dari CSV melalui {@code csvData} DataProvider.</li>
     *   <li>Melakukan login dengan kombinasi username dan password dari file CSV.</li>
     *   <li>Verifikasi keberhasilan atau kegagalan login dapat dilakukan
     *       di dalam metode {@code loginWithDataDriven()}.</li>
     * </ol>
     *
     * @param data Map berisi pasangan key-value hasil pembacaan file CSV,
     *             minimal mencakup key <b>username</b> dan <b>password</b>.
     * @throws MalformedURLException jika URL aplikasi tidak valid.
     * @throws URISyntaxException jika format URI tidak sesuai spesifikasi.
     * @see #getCSVData() untuk definisi DataProvider CSV.
     */
	@Epic("Login Feature")
	@Feature("Authentication")
	@Severity(SeverityLevel.CRITICAL)
    @Test(dataProvider = "csvData")
    public void testLoginWithDataDrivenTesting(Map<String, String> data)
            throws MalformedURLException, URISyntaxException {

        login.loginWithDataDriven(data.get("username"), data.get("password"));
    }


    /**
     * Test case untuk memverifikasi login menggunakan kredensial hardcoded yang valid.
     * <p>
     * Tujuan pengujian ini adalah memastikan pengguna dengan kredensial sah
     * dapat login dan diarahkan ke halaman dashboard tanpa error.
     * 
     * <p>
     * Nilai boolean {@code true} pada parameter kedua metode
     * {@code loginWithHardcodedCredentials()} menandakan bahwa hasil yang diharapkan
     * adalah <b>berhasil login</b>.
     */
	@Epic("Login Feature")
	@Feature("Authentication")
	@Severity(SeverityLevel.CRITICAL)
    @Test
    public void loginWithValidCredentials() {
        login.loginWithHardcodedCredentials("standard_user", true);
    }


    /**
     * Test case untuk memverifikasi login menggunakan kredensial hardcoded yang tidak valid.
     * <p>
     * Tujuan pengujian ini adalah memastikan sistem menampilkan pesan error atau
     * gagal login ketika pengguna memasukkan username atau password yang salah.
     * 
     * <p>
     * Nilai boolean {@code false} pada parameter kedua metode
     * {@code loginWithHardcodedCredentials()} menandakan bahwa hasil yang diharapkan
     * adalah <b>gagal login</b>.
     */
	@Epic("Login Feature")
	@Feature("Authentication")
	@Severity(SeverityLevel.CRITICAL)
    @Test
    public void loginWithInvalidCredentials() {
        login.loginWithHardcodedCredentials("invalid_username", false);
    }


}
