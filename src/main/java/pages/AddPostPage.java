package pages;

import java.awt.Desktop.Action;
import java.awt.Frame;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import base.Base;

public class AddPostPage extends Base{
	
	@FindBy(xpath ="//*[@id=\"user_login\"]")
	WebElement UsernameTab ; 
	
	@FindBy(xpath ="//*[@id=\"user_pass\"]")
	WebElement PasswordTab ; 

	@FindBy(xpath ="//*[@id=\"wp-submit\"]")
	WebElement LoginButton ;
	
	
	
	@FindBy(xpath ="//*[@id=\"welcome-panel\"]/div/div/div[2]/ul/li[1]/a")
	WebElement AddNewPost ;
	
	@FindBy(xpath="//*[@id=\"title\"]")
	WebElement PostTitleTab;
	
	@FindBy(xpath="//*[@id=\"tinymce\"]/p")
	WebElement PostSubjectTab;
	
	@FindBy(xpath="//*[@id=\"publish\"]")
	WebElement PublishButton;
	
	
	@FindBy(xpath="//*[@id=\"login_error\"]")
	WebElement ErrorMsg;
	
	static WebDriverWait wait;
	
	public AddPostPage() {
		PageFactory.initElements(driver, this);
		wait = new WebDriverWait(driver, 5);
	}
	
	public void openUrl(String url) {

		driver.get(prop.getProperty(url));
		((JavascriptExecutor) driver).executeScript("document.body.style.zoom='100%';");
	}
	
	public void login(String UserName, String Password) {
		UsernameTab.sendKeys(UserName);
		PasswordTab.sendKeys(Password);
		LoginButton.submit();
	}

	public String addNewPost(String Title, String Subject)
	{
		AddNewPost.click();
		PostTitleTab.sendKeys(Title);
		driver.switchTo().frame("content_ifr");
		PostSubjectTab.sendKeys(Subject);
		driver.switchTo().defaultContent();	
		PublishButton.submit();
		wait.until(ExpectedConditions.alertIsPresent());
		driver.switchTo().alert().accept();
		PublishButton.click();
		return PublishButton.getAttribute("value");

		}
	
	public String getPageURL() {
		return driver.getCurrentUrl();
	}

	public String getErrorMsg() {
		return ErrorMsg.getText();
	}
}
