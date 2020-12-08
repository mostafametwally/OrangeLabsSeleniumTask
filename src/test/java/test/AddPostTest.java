package test;

import java.io.IOException;

import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.*;

import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.LogStatus;

import base.Base;
import pages.AddPostPage;
import utilities.ReadExcelFile;

public class AddPostTest extends Base {

	AddPostPage addPostPage;

	@BeforeTest
	public void setupTest() {
		extent = new ExtentReports(System.getProperty("user.dir") + "/test-output/ExtentReport.html", true);

	}

	@BeforeMethod
	public void setupMethod() {
		Base.initialization();
		addPostPage = new AddPostPage();
		addPostPage.openUrl("LoginPageLink");
	}

	@AfterMethod
	public void tearDownMethod(ITestResult result) throws IOException {
		if (result.getStatus() == ITestResult.FAILURE) {
			extentTest.log(LogStatus.FAIL, "TEST CASE FAILED IS " + result.getName()); // to add name in the report
			extentTest.log(LogStatus.FAIL, "TEST CASE FAILED IS " + result.getThrowable()); // to add error/exception

			String screenshotPath = Base.takeScreenShot(result.getName());
			System.out.println(screenshotPath);
			extentTest.log(LogStatus.FAIL, extentTest.addScreenCapture(screenshotPath)); // to add screenshot in the
																							// report

		} else if (result.getStatus() == ITestResult.SKIP) {
			extentTest.log(LogStatus.SKIP, "Test Case SKIPPED IS " + result.getName());
		} else if (result.getStatus() == ITestResult.SUCCESS) {
			extentTest.log(LogStatus.PASS, "Test Case PASSED IS " + result.getName());
		}
		driver.quit();
	}

	@AfterTest
	public void tearDownTest() {
		extent.endTest(extentTest); // ending test and ends the current test and prepare to create html report
		extent.flush();
//		extent.close();
	}

	@DataProvider // to read valid data from excel sheet
	public Object[][] readValidLoginDataFromExcelFile() throws IOException {
		String filePath = System.getProperty("user.dir") + "/src/main/java/testData";
		String fileName = "Test_Data.xlsx";
		String sheetName = "ValidLoginData";
		return ReadExcelFile.readExcel(filePath, fileName, sheetName);

	}

	@Test(dataProvider = "readValidLoginDataFromExcelFile")
	public void VerifyloginWithValidData(String UserName, String Password) {
		extentTest = extent.startTest("VerifyloginWithValidData");
		addPostPage.login(UserName, Password);
		String ActualURL = addPostPage.getPageURL();
		String ExpectedURL = "http://demosite.center/wordpress/wp-admin/";
		Assert.assertEquals(ActualURL, ExpectedURL);
	}

	@DataProvider // to read Invalid data from excel sheet
	public Object[][] readInvalidLoginDataFromExcelFile() throws IOException {
		String filePath = System.getProperty("user.dir") + "/src/main/java/testData";
		String fileName = "Test_Data.xlsx";
		String sheetName = "InvalidLoginData";
		return ReadExcelFile.readExcel(filePath, fileName, sheetName);

	}

	@Test(dataProvider = "readInvalidLoginDataFromExcelFile")
	public void VerifyloginWithInvalidData(String UserName, String Password) {
		extentTest = extent.startTest("VerifyloginWithInvalidData");
		addPostPage.login(UserName, Password);
		Assert.assertTrue(addPostPage.getErrorMsg().contains("Invalid username"));
	}

	@Test
	public void verifyAddPost() {
		extentTest = extent.startTest("verifyAddPost");
		addPostPage.login("admin", "demo123");
		String Title = "Testing New Post";
		String Subject = "This article is to test adding new post functionality";
		String ActuallPostLink = addPostPage.addNewPost(Title, Subject);
		Assert.assertEquals(ActuallPostLink, "Update");

	}
}
