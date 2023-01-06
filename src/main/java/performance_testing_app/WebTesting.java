package performance_testing_app;


import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.*;
import org.openqa.selenium.chrome.*;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;


public class WebTesting {
	private static WebDriver localDriver;
	
	
	public static WebDriver getDriver() {
		return localDriver;
	}
	
	
	public static void initSystemProperties() {
		// Collect path to drivers
		Path currentRelativePath = Paths.get("");

		String geckoDriverName = "geckodriver.exe";
		String geckoPath = Paths.get(currentRelativePath.toAbsolutePath().toString(), "drivers/", geckoDriverName).toString();
		geckoPath = geckoPath.replace("\\", "/");
		System.out.println(geckoPath);
		
		String chromeDriverName = "chromedriver.exe";
		String chromePath = Paths.get(currentRelativePath.toAbsolutePath().toString(), "drivers/", chromeDriverName).toString();
		chromePath = chromePath.replace("\\", "/");
		System.out.println(chromePath);
				
		// Firefox Driver
		/** Tested for Firefox Version 92.0.1    **/
		/** Tested for geckodriver Version 0.30.0 **/
		System.setProperty("webdriver.gecko.driver", geckoPath);
		
		// Chrome Driver
		/** Tested for Chrome Version 96.0.4664.45       **/
		/** Tested for chromedriver Version 96.0.4664.45 **/
		System.setProperty("webdriver.chrome.driver", chromePath);
	}
	
	
	public static WebDriver launchDriver(String siteUrl, String browser) {
		if (browser.equals("firefox")) {
			// Set options for Firefox
			FirefoxOptions options = new FirefoxOptions()
					     	.addPreference("browser.startup.page", 1)
					     	.addPreference("browser.startup.homepage", siteUrl)
					     	.setAcceptInsecureCerts(true)
					     	.setHeadless(false);
				
			// Browser is launched on creation of the driver
			quitDriver();
			localDriver = new FirefoxDriver(options);
		} else if (browser.equals("chrome")) {
			// Set options for Chrome
			ChromeOptions options = new ChromeOptions()
							.addArguments("--homepage "+siteUrl)
							.addArguments("--disable-blink-features AutomationControlled")
							.setAcceptInsecureCerts(true)
							.setHeadless(false);
							
			// Browser is launched on creation of the driver
			quitDriver();
			localDriver = new ChromeDriver(options);
			
			// Fix visible tab for ChromeDriver
			ArrayList<String> tabs = new ArrayList<>(localDriver.getWindowHandles());
			System.out.println(tabs.get(1));
			localDriver.switchTo().window(tabs.get(1));
		}
		
		return localDriver;
	}
	
	public static void quitDriver() {
		if (localDriver != null) {
			localDriver.quit();
			localDriver = null;
		}
	}
}
