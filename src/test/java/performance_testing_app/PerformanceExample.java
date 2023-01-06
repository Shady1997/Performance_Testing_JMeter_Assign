package performance_testing_app;

import performance_testing_app.WebTesting;

import org.junit.Test;
import org.junit.After;
import org.junit.BeforeClass;
import org.junit.AfterClass;

import org.openqa.selenium.*;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.firefox.*;
import org.openqa.selenium.chrome.*;
import org.openqa.selenium.support.ui.*;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.jmeter.samplers.SampleResult;

public class PerformanceExample {
	private static long shortTimeout = 10;
	private static long longTimeout = 90;
	
	private SampleResult sample;
	private static PrintStream outputStream;
		
	@BeforeClass
	public static void setUp() throws IOException {
		WebTesting.initSystemProperties();
		// Make the output file
		Path currentRelativePath = Paths.get("");
		String outputPath = Paths.get(currentRelativePath.toAbsolutePath().toString(), "results/OutputFile.txt").toString();
		File output = new File(outputPath);
		output.delete();
		output.createNewFile();
		outputStream = new PrintStream(output);
	}
		
	@AfterClass
	public static void tearDownAll() {
		if (WebTesting.getDriver() != null) {
			WebTesting.quitDriver();
		}
	}

	@After
	public void tearDown() {
		if (WebTesting.getDriver() != null) {
			WebTesting.quitDriver();
		}
	}

	/* Success: Speed test passes
	 * Failure: Speed Test fails
	 */
	@Test
	public void performanceTestGoogleSpeedTest() {
		// Number of tests to run
		int numTests = 1;
		
		// Sample times arrays
		long[] loadTimes = new long[numTests];
		long[] successTimes = new long[numTests];
		long[] failureTimes = new long[numTests];
		
		// Initialize arrays
		for (int i=0; i<numTests; i++) {
			loadTimes[i] = -1;
			successTimes[i] = -1;
			failureTimes[i] = -1;
		}
		
		// Run the speed test numTests times and average multiple performance measures
		// Page loading times, successful test times, test failure recognition times
		for (int i=0; i<numTests; i++) {
//			FirefoxDriver driver = (FirefoxDriver) WebTesting.launchDriver("https://www.google.com", "firefox");
			ChromeDriver driver = (ChromeDriver) WebTesting.launchDriver("https://www.google.com", "chrome");
			
			if (driver == null) {
				WebTesting.quitDriver();
				continue;
			}
			
			WebDriverWait shortWait = new WebDriverWait(driver, shortTimeout);
			WebDriverWait longWait = new WebDriverWait(driver, longTimeout);
			
			// Create a new SampleResult
			sample = new SampleResult();
			
			// Ensure that the search bar is visible before accessing it
			try {
				shortWait.until(ExpectedConditions.visibilityOfElementLocated(By.name("q")));
			} catch (TimeoutException ex) {
				WebTesting.quitDriver();
				continue;
			}
			
			// Enter "internet speed test" into the search bar then press the "Return" key
			driver.findElement(By.name("q")).sendKeys("internet speed test" + Keys.RETURN);

			/* Start page loading sample */
			sample.sampleStart();
			
			// Ensure that the page is loaded ("RUN SPEED TEST" button is clickable)
			try {
				shortWait.until(ExpectedConditions.elementToBeClickable(By.id("knowledge-verticals-internetspeedtest__test_button")));
			} catch (TimeoutException ex) {
				// Failure
				sample.sampleEnd();
				printFailureTime();
				failureTimes[i] = sample.getTime();
				WebTesting.quitDriver();
				continue;
			}
			
			// Page Loaded
			sample.sampleEnd();
			printLoadTime();
			loadTimes[i] = sample.getTime();
			
			// Start the speed test
			driver.findElement(By.id("knowledge-verticals-internetspeedtest__test_button")).click();

			/* Start speed test sample */
			sample = new SampleResult();
			sample.sampleStart();

			// Ensure that the test started ("CANCEL" button is clickable directly after)
			try {
				shortWait.until(ExpectedConditions.elementToBeClickable(By.xpath("//g-raised-button[@jsaction=\'dArJMd\']")));
			} catch (TimeoutException ex) {
				// Failure
				sample.sampleEnd();
				printFailureTime();
				failureTimes[i] = sample.getTime();
				WebTesting.quitDriver();
				continue;
			}
			
			// Now wait for the the test to finish ("CANCEL" button disappears)
			try {
				longWait.until(ExpectedConditions.not(ExpectedConditions.elementToBeClickable(By.xpath("//g-raised-button[@jsaction=\'dArJMd\']"))));
			} catch (TimeoutException ex) {
				// Failure
				sample.sampleEnd();
				printFailureTime();
				failureTimes[i] = sample.getTime();
				WebTesting.quitDriver();
				continue;
			}
			
			/* End speed test sample */
			sample.sampleEnd();
			
			// "RETRY" button signifies a failure of the test
			try {
				shortWait.until(ExpectedConditions.elementToBeClickable(By.xpath("//g-raised-button[@jsaction=\'i0JLwd\']")));
				// Failure
				printFailureTime();
				failureTimes[i] = sample.getTime();
				WebTesting.quitDriver();
				continue;
			} catch (TimeoutException ex) {
				
			}
			
			// "TEST AGAIN" button signifies a success
			try {
				// Success
				shortWait.until(ExpectedConditions.elementToBeClickable(By.xpath("//g-raised-button[@jsaction=\'iyDKIb\']")));
				printSuccessTime("speedtest");
				successTimes[i] = sample.getTime();
			} catch (TimeoutException ex) {
				// Failure
				printFailureTime();
				failureTimes[i] = sample.getTime();
				WebTesting.quitDriver();
				continue;
			}
			
			WebTesting.quitDriver();
		}
		
		int numLoads = 0;
		int numSuccess = 0;
		int numFailure = 0;
		long averageLoadTime = 0;
		long averageSuccessTime = 0;
		long averageFailureTime = 0;
		
		// Add total number of completions done for each
		for (int i=0; i<numTests; i++) {
			if (loadTimes[i] != -1) {
				numLoads++;
				averageLoadTime += loadTimes[i];
			}
			
			if(successTimes[i] != -1) {
				numSuccess++;
				averageSuccessTime += successTimes[i];
			}
			
			if(failureTimes[i] != -1) {
				numFailure++;
				averageFailureTime += failureTimes[i];
			}
		}
		
		// Get floored average in ms
		if (numLoads > 0)
			averageLoadTime = averageLoadTime/numLoads;
		if (numSuccess > 0)
			averageSuccessTime = averageSuccessTime/numSuccess;
		if (numFailure > 0)
			averageFailureTime = averageFailureTime/numFailure;
		
		System.out.println("Page was loaded "+numLoads+" times with an average load time of "+averageLoadTime+" ms.");
		System.out.println("Speed test succeeded "+numSuccess+" times with an average runtime of "+averageSuccessTime+" ms.");
		System.out.println("Speed test failed "+numFailure+" times with an average recognition time of "+averageFailureTime+" ms.\n");
		outputStream.println("Page was loaded "+numLoads+" times with an average load time of "+averageLoadTime+" ms.");
		outputStream.println("Speed test succeeded "+numSuccess+" times with an average runtime of "+averageSuccessTime+" ms.");
		outputStream.println("Speed test failed "+numFailure+" times with an average recognition time of "+averageFailureTime+" ms.\n");
	}

	/* Success: Correct response to calculator press
	 * Failure: Incorrect response to calculator press
	 */
	@Test
	public void performanceTestCalculator() {
		// Number of tests to run
		int numTests = 1;
		int buttonPresses = 13;
		
		// Sample times arrays
		long[] loadTimes = new long[numTests];
		long[] successTimes = new long[numTests*buttonPresses];
		long[] failureTimes = new long[numTests*buttonPresses];
		
		// Initialize arrays
		for (int i=0; i<numTests; i++) {
			loadTimes[i] = -1;
		}
		
		for (int i=0; i<numTests*buttonPresses; i++) {
			successTimes[i] = -1;
			failureTimes[i] = -1;
		}
		
		// Run the calculator numTests times and average multiple performance measures
		// Page loading times, successful key press times, key press failure recognition times
		for (int i=0; i<numTests; i++) {
//			FirefoxDriver driver = (FirefoxDriver) WebTesting.launchDriver("https://www.google.com", "firefox");
			ChromeDriver driver = (ChromeDriver) WebTesting.launchDriver("https://www.google.com", "chrome");
			
			if (driver == null) {
				continue;
			}
			
			WebDriverWait shortWait = new WebDriverWait(driver, shortTimeout);

			// Create a new SampleResult
			sample = new SampleResult();

			// Ensure that the search bar is visible before accessing it
			try {
				shortWait.until(ExpectedConditions.visibilityOfElementLocated(By.name("q")));
			} catch (TimeoutException ex) {
				continue;
			}

			// Enter "calculator" into the search bar then press the "Return" key
			driver.findElement(By.name("q")).sendKeys("calculator" + Keys.RETURN);
			
			/* Start page loading sample */
			sample.sampleStart();
			
			// Ensure that the page is loaded (Calculator is visible)
			try {
				shortWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class=\'tyYmIf\']")));
			} catch (TimeoutException ex) {
				// Failure
				sample.sampleEnd();
				printFailureTime();
				failureTimes[i*buttonPresses] = sample.getTime();
				continue;
			}

			// Page Loaded
			sample.sampleEnd();
			printLoadTime();
			loadTimes[i] = sample.getTime();
			
			WebElement calculatorText = driver.findElement(By.id("cwos"));
			
			// Press 1
			sample = new SampleResult();
			driver.findElement(By.xpath("//div[@jsname=\'N10B9\']")).click();
			sample.sampleStart();
					
			try {
				// Success
				shortWait.until(ExpectedConditions.textToBePresentInElement(calculatorText, "1"));
				sample.sampleEnd();
				printSuccessTime("calculatorpress");
				successTimes[i*buttonPresses] = sample.getTime();
			} catch (TimeoutException ex) {
				// Failure
				sample.sampleEnd();
				printFailureTime();
				failureTimes[i*buttonPresses] = sample.getTime();
			}
			
			// Press +
			sample = new SampleResult();
			driver.findElement(By.xpath("//div[@jsname=\'XSr6wc\']")).click();
			sample.sampleStart();
			
			try {
				// Success
				shortWait.until(ExpectedConditions.textToBePresentInElement(calculatorText, "1 +"));
				sample.sampleEnd();
				printSuccessTime("calculatorpress");
				successTimes[i*buttonPresses+1] = sample.getTime();
			} catch (TimeoutException ex) {
				// Failure
				sample.sampleEnd();
				printFailureTime();
				failureTimes[i*buttonPresses+1] = sample.getTime();
			}
			
			// Press -
			sample = new SampleResult();
			driver.findElement(By.xpath("//div[@jsname=\'pPHzQc\']")).click();
			sample.sampleStart();
					
			try {
				// Success
				shortWait.until(ExpectedConditions.textToBePresentInElement(calculatorText, "1 -"));
				sample.sampleEnd();
				printSuccessTime("calculatorpress");
				successTimes[i*buttonPresses+2] = sample.getTime();
			} catch (TimeoutException ex) {
				// Failure
				sample.sampleEnd();
				printFailureTime();
				failureTimes[i*buttonPresses+2] = sample.getTime();
			}
			
			// Press 2
			sample = new SampleResult();
			driver.findElement(By.xpath("//div[@jsname=\'lVjWed\']")).click();
			sample.sampleStart();
					
			try {
				// Success
				shortWait.until(ExpectedConditions.textToBePresentInElement(calculatorText, "1 - 2"));
				sample.sampleEnd();
				printSuccessTime("calculatorpress");
				successTimes[i*buttonPresses+3] = sample.getTime();
			} catch (TimeoutException ex) {
				// Failure
				sample.sampleEnd();
				printFailureTime();
				failureTimes[i*buttonPresses+3] = sample.getTime();
			}
			
			// Press =
			sample = new SampleResult();
			driver.findElement(By.xpath("//div[@jsname=\'Pt8tGc\']")).click();
			sample.sampleStart();
					
			try {
				// Success
				shortWait.until(ExpectedConditions.textToBePresentInElement(calculatorText, "-1"));
				sample.sampleEnd();
				printSuccessTime("calculatorpress");
				successTimes[i*buttonPresses+4] = sample.getTime();
			} catch (TimeoutException ex) {
				// Failure
				sample.sampleEnd();
				printFailureTime();
				failureTimes[i*buttonPresses+4] = sample.getTime();
			}
			
			// Press AC
			sample = new SampleResult();
			driver.findElement(By.xpath("//div[@jsname=\'SLn8gc\']")).click();
			sample.sampleStart();
					
			try {
				// Success
				shortWait.until(ExpectedConditions.textToBePresentInElement(calculatorText, "0"));
				sample.sampleEnd();
				printSuccessTime("calculatorpress");
				successTimes[i*buttonPresses+5] = sample.getTime();
			} catch (TimeoutException ex) {
				// Failure
				sample.sampleEnd();
				printFailureTime();
				failureTimes[i*buttonPresses+5] = sample.getTime();
			}
			
			// Press 4
			sample = new SampleResult();
			driver.findElement(By.xpath("//div[@jsname=\'xAP7E\']")).click();
			sample.sampleStart();
					
			try {
				// Success
				shortWait.until(ExpectedConditions.textToBePresentInElement(calculatorText, "4"));
				sample.sampleEnd();
				printSuccessTime("calculatorpress");
				successTimes[i*buttonPresses+6] = sample.getTime();
			} catch (TimeoutException ex) {
				// Failure
				sample.sampleEnd();
				printFailureTime();
				failureTimes[i*buttonPresses+6] = sample.getTime();
			}
			
			// Press 5
			sample = new SampleResult();
			driver.findElement(By.xpath("//div[@jsname=\'Ax5wH\']")).click();
			sample.sampleStart();
					
			try {
				// Success
				shortWait.until(ExpectedConditions.textToBePresentInElement(calculatorText, "45"));
				sample.sampleEnd();
				printSuccessTime("calculatorpress");
				successTimes[i*buttonPresses+7] = sample.getTime();
			} catch (TimeoutException ex) {
				// Failure
				sample.sampleEnd();
				printFailureTime();
				failureTimes[i*buttonPresses+7] = sample.getTime();
			}
			
			// Press ×—
			sample = new SampleResult();
			driver.findElement(By.xpath("//div[@jsname=\'YovRWb\']")).click();
			sample.sampleStart();
					
			try {
				// Success
				shortWait.until(ExpectedConditions.textToBePresentInElement(calculatorText, "45 ×"));
				sample.sampleEnd();
				printSuccessTime("calculatorpress");
				successTimes[i*buttonPresses+8] = sample.getTime();
			} catch (TimeoutException ex) {
				// Failure
				sample.sampleEnd();
				printFailureTime();
				failureTimes[i*buttonPresses+8] = sample.getTime();
			}
			
			// Press ÷
			sample = new SampleResult();
			driver.findElement(By.xpath("//div[@jsname=\'WxTTNd\']")).click();
			sample.sampleStart();
					
			try {
				// Success
				shortWait.until(ExpectedConditions.textToBePresentInElement(calculatorText, "45 ÷"));
				sample.sampleEnd();
				printSuccessTime("calculatorpress");
				successTimes[i*buttonPresses+9] = sample.getTime();
			} catch (TimeoutException ex) {
				// Failure
				sample.sampleEnd();
				printFailureTime();
				failureTimes[i*buttonPresses+9] = sample.getTime();
			}
			
			// Press 9
			sample = new SampleResult();
			driver.findElement(By.xpath("//div[@jsname=\'XoxYJ\']")).click();
			sample.sampleStart();
					
			try {
				// Success
				shortWait.until(ExpectedConditions.textToBePresentInElement(calculatorText, "45 ÷ 9"));
				sample.sampleEnd();
				printSuccessTime("calculatorpress");
				successTimes[i*buttonPresses+10] = sample.getTime();
			} catch (TimeoutException ex) {
				// Failure
				sample.sampleEnd();
				printFailureTime();
				failureTimes[i*buttonPresses+10] = sample.getTime();
			}
			
			// Press =
			sample = new SampleResult();
			driver.findElement(By.xpath("//div[@jsname=\'Pt8tGc\']")).click();
			sample.sampleStart();
					
			try {
				// Success
				shortWait.until(ExpectedConditions.textToBePresentInElement(calculatorText, "5"));
				sample.sampleEnd();
				printSuccessTime("calculatorpress");
				successTimes[i*buttonPresses+11] = sample.getTime();
			} catch (TimeoutException ex) {
				// Failure
				sample.sampleEnd();
				printFailureTime();
				failureTimes[i*buttonPresses+11] = sample.getTime();
			}
			
			// Press AC
			sample = new SampleResult();
			driver.findElement(By.xpath("//div[@jsname=\'SLn8gc\']")).click();
			sample.sampleStart();
					
			try {
				// Success
				shortWait.until(ExpectedConditions.textToBePresentInElement(calculatorText, "0"));
				sample.sampleEnd();
				printSuccessTime("calculatorpress");
				successTimes[i*buttonPresses+12] = sample.getTime();
			} catch (TimeoutException ex) {
				// Failure
				sample.sampleEnd();
				printFailureTime();
				failureTimes[i*buttonPresses+12] = sample.getTime();
			}
			
			WebTesting.quitDriver();
		}
		
		int numLoads = 0;
		int numSuccess = 0;
		int numFailure = 0;
		long averageLoadTime = 0;
		long averageSuccessTime = 0;
		long averageFailureTime = 0;
		
		// Add total number of completions done for each
		for (int i=0; i<numTests; i++) {
			if (loadTimes[i] != -1) {
				numLoads++;
				averageLoadTime += loadTimes[i];
			}
		}
		
		for (int i=0; i<numTests*buttonPresses; i++) {
			if(successTimes[i] != -1) {
				numSuccess++;
				averageSuccessTime += successTimes[i];
			}
			
			if(failureTimes[i] != -1) {
				numFailure++;
				averageFailureTime += failureTimes[i];
			}
		}
		
		// Get floored average in ms
		if (numLoads > 0)
			averageLoadTime = averageLoadTime/numLoads;
		if (numSuccess > 0)
			averageSuccessTime = averageSuccessTime/numSuccess;
		if (numFailure > 0)
			averageFailureTime = averageFailureTime/numFailure;
		
		System.out.println("Page was loaded "+numLoads+" times with an average load time of "+averageLoadTime+" ms.");
		System.out.println("Calculator press succeeded "+numSuccess+" times with an average runtime of "+averageSuccessTime+" ms.");
		System.out.println("Calculator press failed "+numFailure+" times with an average recognition time of "+averageFailureTime+" ms.");
		outputStream.println("Page was loaded "+numLoads+" times with an average load time of "+averageLoadTime+" ms.");
		outputStream.println("Calculator press succeeded "+numSuccess+" times with an average runtime of "+averageSuccessTime+" ms.");
		outputStream.println("Calculator press failed "+numFailure+" times with an average recognition time of "+averageFailureTime+" ms.");
	}
	
	
	
	// Helper functions for stdout information
	private void printLoadTime() {
		System.out.println("Took "+sample.getTime()+" ms to load the page.");
	}
	
	private void printSuccessTime(String type) {
		if (type.equals("speedtest"))
			System.out.println("Took "+sample.getTime()+" ms to run the speed test.");
		else if (type.equals("calculatorpress"))
			System.out.println("Took "+sample.getTime()+" ms to press the calculator button.");
		else
			System.out.println("Took "+sample.getTime()+" ms to successfully complete the task.");
	}
	
	private void printFailureTime() {
		System.out.println("Took "+sample.getTime()+" ms to fail.");
	}
}
