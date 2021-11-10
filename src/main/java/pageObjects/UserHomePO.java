package pageObjects;

import org.openqa.selenium.WebDriver;

import commons.AbstractPage;
import pageUIs.*;

public class UserHomePO extends AbstractPage{
	WebDriver driver;
	public UserHomePO(WebDriver driver) { // local
		this.driver = driver;
	}

	public UserLoginPO clickToLoginLink() {
		waitToElementClickable(driver, UserHomePageUI.LOGIN_LINK);
		clickToElement(driver, UserHomePageUI.LOGIN_LINK);
		return PageGeneratorManager.getUserLoginPage(driver);
		
	}

	public boolean isLoginLinkDisplayed() {
		waitToElementVisible(driver, UserHomePageUI.LOGIN_LINK);
		return isElementDisplayed(driver, UserHomePageUI.LOGIN_LINK);
	}

	public boolean isLoginLinkUnDisplayed() {
		waitToElementInVisible(driver, UserHomePageUI.LOGIN_LINK);
		return isElementUnDisplayed(driver, UserHomePageUI.LOGIN_LINK);
	}

}
