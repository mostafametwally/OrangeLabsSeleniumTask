package base;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.opera.OperaDriver;

import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;

import io.github.bonigarcia.wdm.WebDriverManager;

public class Base {
	public static Properties prop;
	public static WebDriver driver;
	public ExtentReports extent;
	public ExtentTest extentTest;
	
	
	//Initializting the porperties file in 
	//The Base Class constructor 
	
	public Base() {

		try {
			prop = new Properties();
			//FileInputStream ip = new FileInputStream("src/main/java/Config/Config.properties");
			FileInputStream ip = new FileInputStream(System.getProperty("user.dir")+"/src/main/java/config/Config.properties");
			
			prop.load(ip);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	// The WebDriver initialization function
	// it can be extended for (Chrome, Firefox, Opera, IE)
	
	public static void initialization() {
		String BrowserName = prop.getProperty("Browser");
		if (BrowserName.equals("Chrome")) {
			WebDriverManager.chromedriver().setup();
			driver = new ChromeDriver();
		} else
			if (BrowserName.equals("Firefox")) {
			WebDriverManager.firefoxdriver().setup();
			driver = new FirefoxDriver();
		}else if (BrowserName.equals("Opera")) {
			WebDriverManager.operadriver().setup();
			driver = new OperaDriver();
		}else if (BrowserName.equals("InternetExplorer")) {
			WebDriverManager.iedriver().setup();
			driver = new FirefoxDriver();
		}		
		
		driver.manage().window().maximize();
		driver.manage().deleteAllCookies();
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

	}
	
	// Taking Screen shot function that return the file path as String.
	public static String takeScreenShot(String TestMethod) {

		// Convert web driver object to TakeScreenshot

		TakesScreenshot SrcShot = (TakesScreenshot) driver;

		// Call getScreenshotAs method to create image file

		File SrcFile = SrcShot.getScreenshotAs(OutputType.FILE);
		String fileSuffix = new SimpleDateFormat("_MM_dd_HH_mm_ss.SSS").format(new Date());

		File DesFile = new File(System.getProperty("user.dir") + "/test-output/ScreenShots/"+TestMethod+fileSuffix+".png");
		System.out.println("Absolut path"+DesFile.getPath());
		try {
			FileUtils.moveFile(SrcFile, DesFile);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			// This function for debugging.
			System.out.println("Hello screenShot not taken");
		}
		
		return DesFile.getPath();
	}
}
