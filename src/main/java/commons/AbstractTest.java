package commons;

import java.io.File;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.testng.Assert;
import org.testng.Reporter;

import io.github.bonigarcia.wdm.WebDriverManager;

public class AbstractTest {
	private WebDriver driver;
	protected final Log log;
	public WebDriver getDriver() {
		return driver;
	}
	protected WebDriver getBrowserDriver(String browsername) {
		Browser browser = Browser.valueOf(browsername.toUpperCase());
		if(browser == Browser.FIREFOX_UI) {
			// linux , mac , windown (latest official)
			WebDriverManager.firefoxdriver().setup();
			FirefoxProfile profile = new FirefoxProfile();
			File file = new File(GlobalConstants.BROWSER_EXTENSION_FOLDER +"\\to_google_translate-4.1.0-fx.xpi");
			profile.addExtension(file);
			FirefoxOptions options = new FirefoxOptions();
			options.setProfile(profile);
			options.addPreference("intl.accept_languages","vi-VN");
			options.addPreference("browser.download.folderList",2);
			options.addPreference("browser.download.dir",GlobalConstants.DOWNLOAD_FOLDER);
			options.addPreference("browser.download.manager.showWhenStarting",false);
			options.addPreference("browser.helperApps.neverAsk.saveToDisk","application/zip,application/octet-stream,image/jpeg,application/vnd.ms-outlook,text/html,application/pdf");
			options.addPreference("pdfjs.disabled", true);
			options.addArguments("--private");
			driver = new FirefoxDriver(options);
		}
		else if(browser == Browser.CHROME_UI) {
			WebDriverManager.chromedriver().setup();
			File file = new File(GlobalConstants.BROWSER_EXTENSION_FOLDER +"\\extension_2_0_9_0.crx");
			Map<String, Object> prefsMap = new HashMap<String, Object>();
			prefsMap.put("profile.default_content_settings.popups", 0);
			prefsMap.put("download.default_directory", GlobalConstants.DOWNLOAD_FOLDER);
			
			ChromeOptions options = new ChromeOptions();
			options.addExtensions(file);
			options.addArguments("--lang=vi");
			options.addArguments("--disable-infobars");
			options.addArguments("--disable-notifications");
			options.addArguments("--disable-geolocation");
			options.addArguments("--incognito");
			
			options.setExperimentalOption("prefs", prefsMap);
			options.setExperimentalOption("useAutomationExtension", false);
			options.setExperimentalOption("excludeSwitches", Collections.singletonList("enable-automation"));
			
			driver = new ChromeDriver(options);
		}
		else if(browser == Browser.FIREFOX_HEDLESS) {
			WebDriverManager.firefoxdriver().setup();
			FirefoxOptions options = new FirefoxOptions();
			options.addArguments("--headless");
//			options.addArguments("--windown-size=1366x768");
			driver = new FirefoxDriver(options);	
			
		}
		else if(browser == Browser.CHROME_HEDLESS) {
			WebDriverManager.chromedriver().setup();
			ChromeOptions options = new ChromeOptions();
			options.addArguments("--headless");
			options.addArguments("--windown-size=1366x768");
			driver = new ChromeDriver(options);
		}
		else if(browser == Browser.EDGE_CHROMIUM) {
			WebDriverManager.edgedriver().browserVersion("86.0.622.63").setup();
			driver = new EdgeDriver();
		}else {
			throw new RuntimeException(" Please input valid brower name value");
		}
		
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		driver.get("https://www.chotot.com/");
		return driver;
	}
	protected WebDriver getBrowserDriver(String browsername, String url) {
		Browser browser = Browser.valueOf(browsername.toUpperCase());
		if(browser == Browser.FIREFOX_UI) {
			// linux , mac , windown (latest official)
			WebDriverManager.firefoxdriver().setup();
			driver = new FirefoxDriver();
		}
		else if(browser == Browser.CHROME_UI) {
			WebDriverManager.chromedriver().setup();
			ChromeOptions options = new ChromeOptions();
			options.setExperimentalOption("useAutomationExtension", false);
			options.setExperimentalOption("excludeSwitches", Collections.singletonList("enable-automation"));
			driver = new ChromeDriver(options);
		}
		else if(browser == Browser.FIREFOX_HEDLESS) {
			WebDriverManager.firefoxdriver().setup();
			FirefoxOptions options = new FirefoxOptions();
			options.addArguments("--headless");
//			options.addArguments("--windown-size=1366x768");
			driver = new FirefoxDriver(options);	
		
		}
		else if(browser == Browser.CHROME_HEDLESS) {
			WebDriverManager.chromedriver().setup();
			ChromeOptions options = new ChromeOptions();
			options.addArguments("--headless");
			options.addArguments("--windown-size=1366x768");
			driver = new ChromeDriver(options);
		}
		else if(browser == Browser.EDGE_CHROMIUM) {
			WebDriverManager.edgedriver().browserVersion("86.0.622.63").setup();
			driver = new EdgeDriver();
		}else {
			throw new RuntimeException(" Please input valid brower name value");
		}
		
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		driver.get(url);
		return driver;
	}
	public int getRamdomNumber() {
		Random random = new Random();
		return random.nextInt(999);
	}
	protected AbstractTest() {
		log = LogFactory.getLog(getClass());
		
	}
	private boolean checkTrue(boolean condition) {
		boolean pass = true;
		try {
			if (condition == true) {
				log.info(" -------------------------- PASSED -------------------------- ");
			} else {
				log.info(" -------------------------- FAILED -------------------------- ");
			}
			Assert.assertTrue(condition);
		} catch (Throwable e) {
			pass = false;

			// Add error into ReportNG
			VerificationFailures.getFailures().addFailureForTest(Reporter.getCurrentTestResult(), e);
			Reporter.getCurrentTestResult().setThrowable(e);
		}
		return pass;
	}

	protected boolean verifyTrue(boolean condition) {
		return checkTrue(condition);
	}

	private boolean checkFailed(boolean condition) {
		boolean pass = true;
		try {
			if (condition == false) {
				log.info(" -------------------------- PASSED -------------------------- ");
			} else {
				log.info(" -------------------------- FAILED -------------------------- ");
			}
			Assert.assertFalse(condition);
		} catch (Throwable e) {
			pass = false;
			VerificationFailures.getFailures().addFailureForTest(Reporter.getCurrentTestResult(), e);
			Reporter.getCurrentTestResult().setThrowable(e);
		}
		return pass;
	}

	protected boolean verifyFalse(boolean condition) {
		return checkFailed(condition);
	}

	private boolean checkEquals(Object actual, Object expected) {
		boolean pass = true;
		try {
			Assert.assertEquals(actual, expected);
			log.info(" -------------------------- PASSED -------------------------- ");
		} catch (Throwable e) {
			pass = false;
			log.info(" -------------------------- FAILED -------------------------- ");
			VerificationFailures.getFailures().addFailureForTest(Reporter.getCurrentTestResult(), e);
			Reporter.getCurrentTestResult().setThrowable(e);
		}
		return pass;
	}

	protected boolean verifyEquals(Object actual, Object expected) {
		return checkEquals(actual, expected);
	}
	protected void closeBrowserAndDriver(WebDriver driver) {
		try {
			// Get name OS
			String osName = System.getProperty("os.name").toLowerCase();
			log.info("OS name = " + osName);

			String cmd = "";
			// check close browser
			if (driver != null) {
				driver.quit();
			}
			
			// Quit driver executable file in Task Manager
			if (driver.toString().toLowerCase().contains("chrome")) {
				if (osName.toLowerCase().contains("mac")) {
					cmd = "pkill chromedriver";
				} else if (osName.toLowerCase().contains("windows")) {
					cmd = "taskkill /F /FI \"IMAGENAME eq chromedriver*\"";
				}
			} else if (driver.toString().toLowerCase().contains("internetexplorer")) {
				if (osName.toLowerCase().contains("window")) {
					cmd = "taskkill /F /FI \"IMAGENAME eq IEDriverServer*\"";
				}
			} else if (driver.toString().toLowerCase().contains("firefox")) {
				if (osName.toLowerCase().contains("mac")) {
					cmd = "pkill geckodriver";
				} else if (osName.toLowerCase().contains("windows")) {
					cmd = "taskkill /F /FI \"IMAGENAME eq geckodriver*\"";
				}
			} else if (driver.toString().toLowerCase().contains("edge")) {
				if (osName.toLowerCase().contains("mac")) {
					cmd = "pkill msedgedriver";
				} else if (osName.toLowerCase().contains("windows")) {
					cmd = "taskkill /F /FI \"IMAGENAME eq msedgedriver*\"";
				}
			}

			Process process = Runtime.getRuntime().exec(cmd);
			process.waitFor();

			log.info("---------- QUIT BROWSER SUCCESS ----------");
		} catch (Exception e) {
			log.info(e.getMessage());
		}
	}
	
	
}
