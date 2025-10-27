package com.kenny.doitpay.automation.Page;

import java.time.Duration;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.asserts.SoftAssert;

import com.kenny.doitpay.automation.Config.WebDriverManager;
import com.kenny.doitpay.automation.Helper.CustomCommand;
import com.kenny.doitpay.automation.Listeners.LogHelper;



/**
 * Dashboard adalah Page Object yang merepresentasikan halaman utama aplikasi e-commerce.
 * <p>
 * class ini menyediakan metode untuk berinteraksi dengan elemen UI pada halaman dashboard,
 * termasuk memilih produk, menambahkan ke keranjang, membuka menu navigasi, reset aplikasi,
 * dan logout. Juga mendukung verifikasi jumlah item di keranjang.
 * </p>
 * @author Kenny Ramadhan
 * @version 1.0
 */
public class Dashboard {

	 private final CustomCommand utils;
	   private final SoftAssert softAssert;
	   
	   
	   
	   
	   /**
	     * Konstruktor Dashboard.
	     * <p>
	     * Menginisialisasi elemen halaman menggunakan {@link PageFactory} dan membuat instance 
	     * {@link CustomCommand} serta {@link SoftAssert}.
	     * </p>
	     */
	   public Dashboard() {
	    	this.utils = new CustomCommand();
	
	        this.softAssert = new SoftAssert();
	        
	        PageFactory.initElements(WebDriverManager.getDriver(), this);
	        
	   }
	   
	   @FindBy(xpath ="//select[@class='product_sort_container']")
	   private WebElement filterDropdown;
	   
	   @FindBy(xpath ="//div[@data-test=\"inventory-item-name\"]")
	   private List<WebElement> productNames;
	   
	   @FindBy(xpath ="//button[@class='btn btn_primary btn_small btn_inventory ']")
	   private List<WebElement> productListAddToCartBtn;
	   
	   @FindBy(xpath ="//a[@class='shopping_cart_link']")
	   private WebElement cartIcon;
	   
	   private By cartBadge = By.xpath("//span[@class='shopping_cart_badge']");
	   
	   @FindBy(id="add-to-cart")
	   private WebElement addToCartInDetailProduct;
	   
	   @FindBy(id="react-burger-menu-btn")
	   private WebElement burgerBtn;
	   
	   
	   @FindBy(id="reset_sidebar_link")
	   private WebElement resetAppStateBtn;
	   
	   
	   @FindBy(id="logout_sidebar_link")
	   private WebElement logoutBtn;
	   
	  

	   /**
	     * Mengklik tombol hamburger untuk membuka menu navigasi.
	     */
	    public void hamburgerBtn() {
	        LogHelper.step("Membuka Navigasi");
	        utils.clickWhenReady(burgerBtn);
	        LogHelper.detail("Berhasil menampilkan navigasi");
	    }

	    /**
	     * Melakukan reset state aplikasi melalui menu navigasi.
	     */
	    public void resetStateApp() {
	        LogHelper.step("Melakukan Reset App");
	        utils.clickWhenReady(resetAppStateBtn);
	        LogHelper.detail("Berhasil Melakukan Reset App");
	    }

	    /**
	     * Logout user dari aplikasi.
	     */
	    public void logout() {
	        LogHelper.step("Log out User");
	        utils.clickWhenReady(logoutBtn);
	        LogHelper.detail("Berhasil Log out dan menampilkan halaman Login");
	    }

	   
	   /**
	    * Memilih dan menambahkan produk ke keranjang berdasarkan nama produk secara dinamis.
	    * <p>
	    * Digunakan untuk skenario data-driven test di mana nama produk berasal dari input eksternal 
	    * seperti Excel, CSV, atau parameter test. Metode ini akan melakukan pencarian produk 
	    * pada daftar elemen yang tampil di halaman, kemudian membuka detail produk 
	    * dan menekan tombol "Add to cart".
	    * </p>
	    *
	    * @param name nama produk yang ingin dipilih dan ditambahkan ke keranjang
	    */

	   public void selectProduct(String name) {
		    LogHelper.step("Memilih produk dengan nama: " + name);
		    boolean found = false;

		    try {
		        
		        List<WebElement> products = utils.refreshElement(() -> productNames);

		        for (WebElement product : products) {
		            String productText = product.getText().trim();
		            if (productText.equalsIgnoreCase(name)) {
		                utils.clickWhenReady(product);
		                utils.clickWhenReady(addToCartInDetailProduct);
		                LogHelper.detail("Produk '" + name + "' berhasil ditambahkan ke keranjang.");
		                found = true;
		                break; 
		            }
		        }

		        if (!found) {
		            LogHelper.detail("Produk '" + name + "' tidak ditemukan di halaman.");
		            softAssert.fail("Produk '" + name + "' tidak ditemukan di halaman.");
		        }

		    } catch (StaleElementReferenceException e) {
		        LogHelper.detail("Terjadi stale element, mencoba ulang untuk produk: " + name);
		        selectProduct(name); // recursive retry 1x
		    }

		    softAssert.assertAll();
		}

	   
	   /**
	    * Memastikan keranjang kosong setelah reset app state.
	    */
	   public void verifyCartIsEmptyAfterReset() {
	       int count = getCartItemCount();
	       LogHelper.step("Memeriksa keranjang setelah reset. Jumlah item: " + count);
	       softAssert.assertEquals(count, 0, "Keranjang tidak kosong setelah reset.");
	       softAssert.assertAll();
	   }

	   
	   /**
	     * Menambahkan seluruh produk yang terlihat di halaman ke dalam keranjang belanja secara otomatis.
	     * <p>
	     * Metode ini akan melakukan iterasi terhadap semua tombol <b>"Add to cart"</b> 
	     * yang masih aktif di halaman, kemudian mengkliknya satu per satu hingga semua produk 
	     * berhasil ditambahkan. Setelah semua produk berhasil dimasukkan ke keranjang, 
	     * metode ini akan membuka halaman <b>Cart</b> untuk verifikasi.
	     * </p>
	     *
	     * <p><b>Catatan Teknis:</b></p>
	     * <ul>
	     *   <li>Setiap klik tombol diverifikasi dengan menunggu atribut tombol berubah menjadi
	     *       <code>REMOVE</code> sebagai indikasi sukses.</li>
	     *   <li>Jika tidak ada lagi tombol "Add to cart", proses berhenti otomatis.</li>
	     *   <li>Jika terjadi <code>StaleElementReferenceException</code> atau <code>TimeoutException</code>,
	     *       elemen akan dilewati dan proses dilanjutkan ke produk berikutnya.</li>
	     * </ul>
	     *
	     * <p><b>Langkah akhir:</b></p>
	     * Setelah semua produk ditambahkan, metode akan melakukan klik pada ikon cart 
	     * untuk membuka halaman keranjang.
	     *
	     * @throws IllegalStateException jika WebDriver belum diinisialisasi dengan benar
	     */
	   @SuppressWarnings("static-access")
	public void selectAllProductsToCart() {
	       LogHelper.step("Menambahkan semua produk yang tersedia ke keranjang");

	       WebDriver driver = WebDriverManager.getDriver();
	       WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
	       int totalAdded = 0;

	       // Loop utama: selama masih ada tombol "Add to Cart" di halaman
	       while (true) {
	           // Ambil ulang tombol Add to Cart yang masih aktif
	           List<WebElement> addButtons = driver.findElements(By.xpath("//button[contains(normalize-space(.), 'Add to cart')]"));
	           if (addButtons.isEmpty()) {
	        	   utils.scrollToTop();
	               LogHelper.detail("Semua produk berhasil ditambahkan ke keranjang.");
	               break;
	           }

	           // Klik satu per satu tombol yang masih ada
	           for (WebElement addButton : addButtons) {
	               try {
	                   utils.clickWhenReady(addButton);
	                   totalAdded++;

	                   // Tunggu tombol berubah jadi REMOVE (indikasi sukses klik)
	                   wait.until(ExpectedConditions.attributeContains(addButton, "class", "btn_secondary"));
	                   LogHelper.detail("Produk ke-" + totalAdded + " berhasil ditambahkan ke keranjang.");

	               } catch (StaleElementReferenceException ignored) {
	                   // Tombol sudah hilang dari DOM — lanjut saja
	               } catch (TimeoutException te) {
	                   LogHelper.detail("imeout: tombol tidak berubah menjadi 'Remove' setelah diklik.");
	               } catch (Exception e) {
	                   LogHelper.detail("Gagal menambahkan produk: " + e.getMessage());
	               }
	           }

	           WebElement lastProduct = addButtons.get(addButtons.size() - 1);
	           utils.scrollIntoView(lastProduct);
	           utils.sleep(500);
	       }

	       LogHelper.step("Membuka halaman Cart");
	       try {
	           utils.clickWhenReady(cartIcon);
	           LogHelper.detail("Berhasil membuka halaman Cart.");
	       } catch (Exception e) {
	           softAssert.fail("Gagal membuka halaman Cart: " + e.getMessage());
	       }

	       softAssert.assertAll();
	   }
	   
	   
	   /**
	    * Mengecek apakah keranjang memiliki produk.
	    * @return jumlah item di keranjang, atau 0 jika kosong.
	    */
	   public int getCartItemCount() {
		   WebDriver driver = WebDriverManager.getDriver();
	       try {
	           List<WebElement> badges = driver.findElements(cartBadge);
	           if (!badges.isEmpty()) {
	               String countText = badges.get(0).getText().trim();
	               return Integer.parseInt(countText);
	           }
	       } catch (Exception e) {
	           LogHelper.detail("Tidak dapat membaca jumlah keranjang: " + e.getMessage());
	       }
	       return 0;
	   }

	   




}
