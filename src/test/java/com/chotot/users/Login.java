package com.chotot.users;

import commons.AbstractTest;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import pageObjects.*;

public class Login extends AbstractTest{
	
	WebDriver driver;
	String  passWord, phoneNumber;
	UserHomePO homePage;
	UserLoginPO loginPage;

	@Parameters("browser")
	@BeforeClass
	public void beforeClass(String browsername) {
		driver = getBrowserDriver(browsername);
		phoneNumber = "0938506191";
		passWord = "automation";
		
	}
	@Test
	public void TC_01_Login_Successful() {
		log.info("Login - Step 01: Open Home Page");
		homePage = PageGeneratorManager.geUsertHomePage(driver);

		log.info("Login - Step 02: Verify login link is display");
		homePage.isLoginLinkDisplayed();

		log.info("Login - Step 03: Click \" Đang nhap \" Link");
		loginPage = homePage.clickToLoginLink();
		
		log.info("Login - Step 04: Input phone number textbox :" + phoneNumber);
		loginPage.inputToPhoneNumeTextbox(phoneNumber);

		log.info("Login - Step 05: Input password textbox: "+passWord);
		loginPage.inputToPassWordTextbox(passWord);

		log.info("Login - Step 06: Click button ' Đang nhap '");
		homePage = loginPage.clickToLoginButton();

		log.info("Login - Step 07: Verify login link isn't display - Login successful");
		verifyTrue(homePage.isLoginLinkUnDisplayed());
		
	}


	@AfterClass
	public void afterClass() {
		closeBrowserAndDriver(driver);
	}
	
	
}
