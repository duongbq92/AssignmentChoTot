package pageObjects;

import org.openqa.selenium.WebDriver;

public class PageGeneratorManager {
		public static UserHomePO geUsertHomePage(WebDriver driver) {
			return new UserHomePO(driver);
		}
		public static UserLoginPO getUserLoginPage(WebDriver driver) {
			return new UserLoginPO(driver);
		}
}
