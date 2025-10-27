package com.kenny.doitpay.automation.Page;

import java.util.List;

import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.testng.asserts.SoftAssert;

import com.kenny.doitpay.automation.Config.WebDriverManager;
import com.kenny.doitpay.automation.Helper.CustomCommand;
import com.kenny.doitpay.automation.Listeners.LogHelper;

/**
 * <b>Checkout Page Object</b>
 * <p>
 * class ini merepresentasikan halaman Checkout pada aplikasi e-commerce. Berisi
 * elemen-elemen UI dan method yang digunakan untuk melakukan interaksi seperti
 * input data pengguna, validasi field wajib, dan menyelesaikan proses checkout.
 * <p>
 * Class ini menggunakan pendekatan Page Object Model (POM) untuk menjaga
 * keterpisahan antara logika pengujian dan interaksi antarmuka.
 * </p>
 * 
 * @author Kenny Ramadhan
 * @version 1.0
 */
public class Checkout {

	private final CustomCommand utils;
	private final SoftAssert softAssert;

	/**
	 * Konstruktor default untuk inisialisasi Page Object Checkout.
	 * <p>
	 * Menginisialisasi WebDriver dari {@link WebDriverManager}, kemudian melakukan
	 * binding elemen halaman menggunakan {@link PageFactory}.
	 * </p>
	 */
	public Checkout() {
		this.utils = new CustomCommand();
		this.softAssert = new SoftAssert();
		PageFactory.initElements(WebDriverManager.getDriver(), this);
	}

	// ============================== LOCATORS ==============================

	@FindBy(id = "checkout")
	private WebElement checkoutBtn;

	@FindBy(id = "first-name")
	private WebElement firstNameField;

	@FindBy(id = "last-name")
	private WebElement lastNameField;

	@FindBy(id = "postal-code")
	private WebElement postalCodeField;

	@FindBy(xpath = "//h3[normalize-space(text())='Error: Postal Code is required']")
	private WebElement errorMessagePostalCode;

	@FindBy(xpath = "//h3[normalize-space(text())='Error: First Name is required']")
	private WebElement errorMessageFirstName;

	@FindBy(xpath = "//h3[normalize-space(text())='Error: Last Name is required']")
	private WebElement errorMessageLastName;

	@FindBy(id = "continue")
	private WebElement continueBtn;

	@FindBy(id = "cancel")
	private WebElement cancelBtn;

	@FindBy(id = "finish")
	private WebElement finishBtn;

	@FindBy(xpath = "//h2[normalize-space()='Thank you for your order!']")
	private WebElement completeOrderSuccessMessage;

	@FindBy(xpath = "//div[@data-test=\"inventory-item-price\"]")
	private List<WebElement> priceListOnCart;

	@FindBy(xpath = "//div[@class='summary_subtotal_label']")
	private WebElement subTotalLabel;

	@FindBy(xpath = "//div[@class='summary_tax_label']")
	private WebElement taxLabel;

	@FindBy(xpath = "//div[@class='summary_total_label']")
	private WebElement grandTotal;

	// ============================== ACTION METHODS ==============================

	/**
	 * Menghitung total harga semua item di keranjang, termasuk item yang
	 * tersembunyi dan perlu di-scroll. * Metode ini menggunakan JavaScript
	 * 'textContent' untuk mengambil harga sehingga lebih cepat dan tidak memerlukan
	 * scroll visual.
	 *
	 * @return Total harga (Double)
	 */
	public Double getTotalPriceInCart() {
		double totalAmount = 0.0;
		int itemIndex = 1; // Untuk debug

		LogHelper.step("Menghitung total harga dari " + priceListOnCart.size() + " item di keranjang.");

		for (WebElement priceElement : priceListOnCart) {

			// untuk debug jika terjadi error
			String labelUntukLog = "Item Keranjang ke-" + itemIndex;

			Double price = extractPriceFromLabel(priceElement, labelUntukLog);

			// Cek jika helper mengembalikan null (gagal parsing/ekstraksi)
			if (price != null) {
				totalAmount += price; // Auto-unboxing Double ke double
			}
			// Jika price == null, error sudah di-log di dalam extractPriceFromLabel

			itemIndex++;
		}

		LogHelper.detail("Total Harga yang ada di keranjang tanpa pajak adalah :" + totalAmount);
		return totalAmount;
	}

	
	/**
	 * Melakukan klik pada tombol "Checkout" untuk melanjutkan proses checkout.
	 */
	public void checkoutProducts() {
		LogHelper.step("Menavigasi ke halaman Checkout");
		utils.scrollIntoText("Checkout");
		utils.clickWhenReady(checkoutBtn);
	}

	/**
	 * Mengisi field "First Name" pada form checkout.
	 *
	 * @param firstName nama depan pengguna
	 */
	public void inputFirstName(String firstName) {
		LogHelper.step("Input First Name");
		utils.sendKeysWhenReady(firstNameField, firstName);
		LogHelper.detail("Berhasil mengisi First Name: " + firstName);
	}

	/**
	 * Mengisi field "Last Name" pada form checkout.
	 *
	 * @param lastName nama belakang pengguna
	 */
	public void inputLastName(String lastName) {
		LogHelper.step("Input Last Name");
		utils.sendKeysWhenReady(lastNameField, lastName);
		LogHelper.detail("Berhasil mengisi Last Name: " + lastName);
	}

	/**
	 * Mengisi field "Postal Code" pada form checkout.
	 *
	 * @param postalCode kode pos pengguna
	 */
	public void inputPostalCode(String postalCode) {
		LogHelper.step("Input Postal Code");
		utils.sendKeysWhenReady(postalCodeField, postalCode);
		LogHelper.detail("Berhasil mengisi Postal Code: " + postalCode);
	}

	
	/**
	 * Mengisi Informasi Checkout
	 * <p>
	 * Method ini:
	 * <ul>
	 * <li>Input Semua Field yang ada di form Information</li>
	 * <li>Melakukan validasi jika ada field wajib yang belum diisi</li>
	 * </ul>
	 */
	public void verifyInformationIsValid() {
		LogHelper.step("Klik tombol Continue");
		utils.clickWhenReady(continueBtn);

		// Validasi error message jika field wajib kosong
		try {
			if (errorMessageFirstName.isDisplayed()) {
				LogHelper.detail("First Name is required!");
				softAssert.fail("First Name is required");
			}
		} catch (NoSuchElementException ignored) {
		}

		try {
			if (errorMessageLastName.isDisplayed()) {
				LogHelper.detail("Last Name is required!");
				softAssert.fail("Last Name is required");
			}
		} catch (NoSuchElementException ignored) {
		}

		try {
			if (errorMessagePostalCode.isDisplayed()) {
				LogHelper.detail("Postal Code is required!");
				softAssert.fail("Postal Code is required");
			}
		} catch (NoSuchElementException ignored) {
		}
	
		softAssert.assertAll();
	}
	
	/**
	 * Memastikan form checkout menampilkan pesan error jika field kosong.
	 */
	public void verifyInformationIsInvalid() {
	    LogHelper.step("Klik tombol Continue (Negative Test)");
	    utils.clickWhenReady(continueBtn);

	    boolean hasError = false;

	    if (utils.isElementPresent(errorMessageFirstName)) {
	        LogHelper.detail("Validasi muncul: First Name is required!");
	        hasError = true;
	    }
	    if (utils.isElementPresent(errorMessageLastName)) {
	        LogHelper.detail("Validasi muncul: Last Name is required!");
	        hasError = true;
	    }
	    if (utils.isElementPresent(errorMessagePostalCode)) {
	        LogHelper.detail("Validasi muncul: Postal Code is required!");
	        hasError = true;
	    }

	    softAssert.assertTrue(hasError, 
	        "Tidak ada pesan error yang muncul padahal field kosong!");
	    softAssert.assertAll();
	}

	
	
	/**
	 * Melakukan scroll agar tombol Finish terlihat dan detail Harga terlihat untuk validasi lebih lanjut.
	 * <p>
	 * Method ini:
	 * <ul>
	 * <li>Scroll ke text "Finish"</li>
	 * </ul>
	 */
	public void scrollToFinishOrder() {
		
		// Hanya lanjut jika tidak ada error
		LogHelper.step("Scroll ke tombol Finish");
		utils.scrollIntoText("Finish");
		LogHelper.detail("Menamppilkan Tombol Finish & Informasi Harga");
	}
	
	/**
	 * Mengambil nilai SubTotal (Item Total) dari halaman summary.
	 * 
	 * @return Harga SubTotal sebagai Double, atau null jika gagal.
	 */
	public Double getSubTotal() {
		LogHelper.step("Mengambil harga Subtotal (Item Total)");
		return extractPriceFromLabel(subTotalLabel, "SubTotal");
	}
	

	/**
	 * Mengambil nilai Tax (Pajak) dari halaman summary.
	 * 
	 * @return Harga Tax sebagai Double, atau null jika gagal.
	 */
	public Double getTax() {
		LogHelper.step("Mengambil nilai Pajak (Tax)");
		return extractPriceFromLabel(taxLabel, "Tax");
	}
	

	/**
	 * Mengambil nilai Grand Total dari halaman summary.
	 * 
	 * @return Harga Grand Total sebagai Double, atau null jika gagal.
	 */
	public Double getGrandTotal() {
		LogHelper.step("Mengambil harga Grand Total");
		return extractPriceFromLabel(grandTotal, "Total");
	}

	

	/**
	 * Menyelesaikan proses checkout hingga order berhasil dibuat.
	 * <p>
	 * Method ini:
	 * <ul>
	 * <li>Menekan tombol "Finish"</li>
	 * </ul>
	 */
	public void finishOrder() {
		LogHelper.step("Klik tombol Finish untuk menyelesaikan order");
		utils.clickWhenReady(finishBtn);
		LogHelper.detail("Berhasil klik tombol Finish");
	}
	
	
	/**
	 * Melakukan validasi akhir bahwa proses order sukses
	 * <p>
	 * Method ini:
	 * <ul>
	 * <li>Validasi bahwa teks "Thank you for your order!" muncul dan sebagai indikato sukses</li>
	 * </ul>
	 */
	public void verifySuccessOrder() {
		
		LogHelper.step("Verifikasi pesan sukses muncul");
		utils.verifyElementExist(completeOrderSuccessMessage);
		LogHelper.detail("Berhasil menampilkan 'Thank you for your order!' , Order Sukses");
	}

	/**
	 * Helper internal untuk mengekstrak nilai numerik (harga) dari elemen label
	 * summary di halaman checkout. * @param element WebElement yang berisi teks
	 * (e.g., "Item total: $29.99")
	 * 
	 * @param labelName Nama label untuk logging (e.g., "SubTotal")
	 * @return Nilai Double dari harga, atau null jika gagal parsing/ekstraksi
	 */
	private Double extractPriceFromLabel(WebElement element, String labelName) {
		// 1. Ambil teks mentah menggunakan helper JS kita
		String rawText = CustomCommand.getTextWithJS(element);

		if (rawText == null || rawText.isEmpty()) {
			LogHelper.detail("Teks untuk '" + labelName + "' kosong atau null.");
			softAssert.fail();
			return null; // Mengembalikan null agar assertion bisa gagal (NullPointerException)
		}

		// 2. Logika parsing Anda yang sudah ada
		String cleanPrice = rawText.replaceAll("[^0-9.]", "");

		try {
			// 3. Parse ke Double
			double price = Double.parseDouble(cleanPrice);
			LogHelper.detail("Harga yang diekstrak untuk '" + labelName + "' adalah: " + price);

			return price;

		} catch (NumberFormatException e) {
			LogHelper.detail("Gagal mem-parsing harga dari teks: '" + rawText + "' untuk " + labelName);
			softAssert.fail();
			return null; // Mengembalikan null jika parsing gagal
		}
	}

}
