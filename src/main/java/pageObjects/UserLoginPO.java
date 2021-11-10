package pageObjects;

import org.openqa.selenium.WebDriver;

import commons.AbstractPage;
import pageUIs.*;

public class UserLoginPO extends  AbstractPage{
	WebDriver driver;
	public UserLoginPO(WebDriver driver) {
		this.driver = driver;
	}
	public void inputToPhoneNumeTextbox(String email) {
		waitToElementVisible(driver, UserLoginPageUI.PHONE_NUMBER_TEXTBOX);
		sendkeyToElement(driver, UserLoginPageUI.PHONE_NUMBER_TEXTBOX, email);
	}

	public void inputToPassWordTextbox(String passWord) {
		waitToElementClickable(driver, UserLoginPageUI.PASSWORD_TEXTBOX);
		sendkeyToElement(driver, UserLoginPageUI.PASSWORD_TEXTBOX, passWord);
		
	}

	public UserHomePO clickToLoginButton() {
		waitToElementClickable(driver, UserLoginPageUI.LOGIN_BUTTON);
		clickToElement(driver, UserLoginPageUI.LOGIN_BUTTON);
		return PageGeneratorManager.geUsertHomePage(driver);
		
	}

}
