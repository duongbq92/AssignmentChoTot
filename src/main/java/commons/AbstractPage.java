package commons;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

public class AbstractPage {
    private WebDriverWait explicitWait;
    private JavascriptExecutor jsExecutor;
    private Select select;
    private WebElement element;
    private List<WebElement> elements, elementList;
    private Actions action;

    protected void openPageUrl(WebDriver driver, String url) {
        driver.get(url);
    }

    protected String getCurrentPageUrl(WebDriver driver) {
        return driver.getCurrentUrl();
    }

    protected String getCurrentPageTitle(WebDriver driver) {
        return driver.getTitle();
    }

    protected void backToPage(WebDriver driver) {
        driver.navigate().back();
    }

    protected void forwardToPage(WebDriver driver) {
        driver.navigate().forward();
    }

    protected void refreshCurrentPage(WebDriver driver) {
        driver.navigate().refresh();
    }

    protected void acceptAlert(WebDriver driver) {
        driver.switchTo().alert().accept();
    }

    protected void canceltAlert(WebDriver driver) {
        driver.switchTo().alert().dismiss();
    }

    protected String getTextAlert(WebDriver driver) {
        return driver.switchTo().alert().getText();
    }

    protected void setTextAlert(WebDriver driver, String value) {
        driver.switchTo().alert().sendKeys(value);
    }

    protected void waitAlertPresent(WebDriver driver) {
        WebDriverWait explicitWait = new WebDriverWait(driver, GlobalConstants.LONGTIMEOUT);
        explicitWait.until(ExpectedConditions.alertIsPresent());
    }

    protected void switchToWindowByID(WebDriver driver, String parentID) {
        Set<String> allWindows = driver.getWindowHandles();
        for (String runWindow : allWindows) {
            if (!runWindow.equals(parentID)) {
                driver.switchTo().window(runWindow);
                break;
            }
        }
    }

    protected void switchToWindowByTitle(WebDriver driver, String title) {
        Set<String> allWindows = driver.getWindowHandles();
        for (String runWindows : allWindows) {
            driver.switchTo().window(runWindows);
            String curentPageTitle = driver.getTitle();
            if (curentPageTitle.equals(title)) {
                break;
            }
        }
    }

    protected void closeAllWindowsWithoutParent(WebDriver driver, String parentID) {
        Set<String> allWindows = driver.getWindowHandles();
        for (String runWindows : allWindows) {
            if (!runWindows.equals(parentID)) {
                driver.switchTo().window(runWindows);
                driver.close();
            }
        }
        driver.switchTo().window(parentID);
    }

    protected By getByXpath(String locator) {
        return By.xpath(locator);
    }

    protected String getDynamicLocator(String locator, String... values) {
        locator = String.format(locator, (Object[]) values);
        return locator;
    }

    protected WebElement getElement(WebDriver driver, String locator) {
        return driver.findElement(getByXpath(locator));
    }

    protected List<WebElement> getElements(WebDriver driver, String locator) {
        return driver.findElements(getByXpath(locator));
    }

    protected void clickToElement(WebDriver driver, String locator) {
        if (driver.toString().toLowerCase().contains("edge")) {
            sleepInSecond(1);
        }
        element = getElement(driver, locator);
        element.click();
    }

    protected void clickToElement(WebDriver driver, String locator, String... values) {
        if (driver.toString().toLowerCase().contains("edge")) {
            sleepInSecond(1);
        }
        element = getElement(driver, getDynamicLocator(locator, values));
        element.click();
    }

    protected void sendkeyToElement(WebDriver driver, String locator, String value) {
        element = getElement(driver, locator);
        element.clear();
        element.sendKeys(value);
    }

    protected void sendkeyToElement(WebDriver driver, String locator, String value, String... values) {
        element = getElement(driver, getDynamicLocator(locator, values));
        element.clear();
        element.sendKeys(value);
    }

    protected void selectItemInDropdown(WebDriver driver, String locator, String itemValue) {
        element = getElement(driver, locator);
        select = new Select(element);
        select.selectByVisibleText(itemValue);
    }

    protected void selectItemInDropdown(WebDriver driver, String locator, String itemValue, String... values) {
        element = getElement(driver, getDynamicLocator(locator, values));
        select = new Select(element);
        select.selectByVisibleText(itemValue);
    }

    protected String getFirstSelectTextInDropdown(WebDriver driver, String locator) {
        element = getElement(driver, locator);
        select = new Select(element);
        return select.getFirstSelectedOption().getText();
    }

    protected boolean isDropdownMutiple(WebDriver driver, String locator) {
        element = getElement(driver, locator);
        select = new Select(element);
        return select.isMultiple();
    }


    protected void selectItemInCustomDropdown(WebDriver driver, String parentLocator, String childItemLocator, String expectedItem) {
        getElement(driver, parentLocator).click();
        sleepInSecond(1);

        // 2 - Chờ cho tất cả các item con được load ra
        WebDriverWait explicitWait = new WebDriverWait(driver, GlobalConstants.LONGTIMEOUT);
        explicitWait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(getByXpath(childItemLocator)));

        // Đưa tất cả các item trong dropdown vào 1 list để kiểm tra
        List<WebElement> allItems = getElements(driver, childItemLocator);

        // 3 - Chạy qua tất cả các giá trị đang có trong list
        for (WebElement item : allItems) {
            // 4 - Kiểm tra xem text của các giá trị có item nào bằng vs text mong muốn ko
            if (item.getText().equals(expectedItem)) {
                // 5 - Scroll xuống đến đúng item này
                jsExecutor = (JavascriptExecutor) driver;
                jsExecutor.executeScript("arguments[0].scrollIntoView(true);", item);
                sleepInSecond(1);

                // 6 - Click vào cái item này
                item.click();
                sleepInSecond(1);
                break;
            }
        }
    }

    protected String getElementAttibute(WebDriver driver, String locator, String atiiributeName) {
        element = getElement(driver, locator);
        return element.getAttribute(atiiributeName);
    }

    protected String getElementAttibute(WebDriver driver, String locator, String atiiributeName, String... values) {
        element = getElement(driver, getDynamicLocator(locator, values));
        return element.getAttribute(atiiributeName);
    }

    protected String getElementText(WebDriver driver, String locator) {
        element = getElement(driver, locator);
        return element.getText();
    }

    protected String getElementText(WebDriver driver, String locator, String... values) {
        element = getElement(driver, getDynamicLocator(locator, values));
        return element.getText();
    }

    protected int countElementSize(WebDriver driver, String locator) {
        return getElements(driver, locator).size();
    }

    protected int countElementSize(WebDriver driver, String locator, String... values) {
        return getElements(driver, getDynamicLocator(locator, values)).size();
    }

    protected void checkToCheckbox(WebDriver driver, String locator) {
        element = getElement(driver, locator);
        if (!element.isSelected()) {
            element.click();
        }
    }

    protected void checkToCheckbox(WebDriver driver, String locator, String... values) {
        element = getElement(driver, getDynamicLocator(locator, values));
        if (!element.isSelected()) {
            element.click();
        }
    }

    protected void uncheckToCheckbox(WebDriver driver, String locator) {
        element = getElement(driver, locator);
        if (element.isSelected()) {
            element.click();
        }
    }

    protected boolean isElementDisplayed(WebDriver driver, String locator) {
        try {
            return getElement(driver, locator).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    protected boolean isElementDisplayed(WebDriver driver, String locator, String... values) {
        return getElement(driver, getDynamicLocator(locator, values)).isDisplayed();
    }

    protected boolean isElementEnabled(WebDriver driver, String locator) {
        return getElement(driver, locator).isEnabled();
    }

    protected boolean isElementSelected(WebDriver driver, String locator) {
        return getElement(driver, locator).isSelected();
    }

    protected boolean isElementSelected(WebDriver driver, String locator, String values) {
        return getElement(driver, getDynamicLocator(locator, values)).isSelected();
    }

    protected void switchToFrame(WebDriver driver, String locator) {
        driver.switchTo().frame(getElement(driver, locator));
    }

    protected void switchToDefaultContent(WebDriver driver) {
        driver.switchTo().defaultContent();
    }

    protected void doubleClickToElement(WebDriver driver, String locator) {
        action = new Actions(driver);
        action.doubleClick(getElement(driver, locator)).perform();
    }

    protected void rightClickToElement(WebDriver driver, String locator) {
        action = new Actions(driver);
        action.contextClick(getElement(driver, locator)).perform();
    }

    protected void hoverMouseToElement(WebDriver driver, String locator) {
        action = new Actions(driver);
        action.moveToElement(getElement(driver, locator)).perform();
    }

    protected void clickAndHoldToElement(WebDriver driver, String locator) {
        action = new Actions(driver);
        action.clickAndHold(getElement(driver, locator)).perform();
    }

    protected void dragAnDropdToElement(WebDriver driver, String sourceLocator, String targerLocator) {
        action = new Actions(driver);
        action.dragAndDrop(getElement(driver, sourceLocator), getElement(driver, targerLocator)).perform();
    }

    protected void sendKeysBoardToElement(WebDriver driver, String lorcator, Keys key) {
        action = new Actions(driver);
        action.sendKeys(getElement(driver, lorcator), key).perform();
    }

    protected Object executeForBrowser(WebDriver driver, String javaScript) {
        jsExecutor = (JavascriptExecutor) driver;
        return jsExecutor.executeScript(javaScript);
    }

    protected String getInnerText(WebDriver driver) {
        jsExecutor = (JavascriptExecutor) driver;
        return (String) jsExecutor.executeScript("return document.documentElement.innerText;");
    }

    protected boolean areExpectedTextInInnerText(WebDriver driver, String textExpected) {
        jsExecutor = (JavascriptExecutor) driver;
        String textActual = (String) jsExecutor.executeScript("return document.documentElement.innerText.match('" + textExpected + "')[0]");
        return textActual.equals(textExpected);
    }

    protected void scrollToBottomPage(WebDriver driver) {
        jsExecutor = (JavascriptExecutor) driver;
        jsExecutor.executeScript("window.scrollBy(0,document.body.scrollHeight)");
    }

    protected void navigateToUrlByJS(WebDriver driver, String url) {
        jsExecutor = (JavascriptExecutor) driver;
        jsExecutor.executeScript("window.location = '" + url + "'");
    }

    protected void highlightElement(WebDriver driver, String locator) {
        jsExecutor = (JavascriptExecutor) driver;
        element = getElement(driver, locator);
        String originalStyle = element.getAttribute("style");
        jsExecutor.executeScript("arguments[0].setAttribute(arguments[1], arguments[2])", element, "style", "border: 2px solid red; border-style: dashed;");
        sleepInSecond(1);
        jsExecutor.executeScript("arguments[0].setAttribute(arguments[1], arguments[2])", element, "style", originalStyle);
    }

    protected void clickToElementByJS(WebDriver driver, String locator) {
        jsExecutor = (JavascriptExecutor) driver;
        element = getElement(driver, locator);
        jsExecutor.executeScript("arguments[0].click();", element);
    }

    protected void scrollToElement(WebDriver driver, String locator) {
        jsExecutor = (JavascriptExecutor) driver;
        element = getElement(driver, locator);
        jsExecutor.executeScript("arguments[0].scrollIntoView(true);", element);
        sleepInSecond(1);
    }

    protected void sendkeyToElementByJS(WebDriver driver, String locator, String value) {
        jsExecutor = (JavascriptExecutor) driver;
        element = getElement(driver, locator);
        jsExecutor.executeScript("arguments[0].setAttribute('value', '" + value + "')", element);
    }

    protected void removeAttributeInDOM(WebDriver driver, String locator, String attributeRemove) {
        jsExecutor = (JavascriptExecutor) driver;
        element = getElement(driver, locator);
        jsExecutor.executeScript("arguments[0].removeAttribute('" + attributeRemove + "');", element);
    }

    protected boolean isImageLoade(WebDriver driver, String locator) {
        jsExecutor = (JavascriptExecutor) driver;
        element = getElement(driver, locator);
        Boolean ImagePresent = (Boolean) ((JavascriptExecutor) driver).executeScript("return arguments[0].complete && typeof arguments[0].naturalWidth != \"undefined\" && arguments[0].naturalWidth > 0", element);
        if (ImagePresent) {
            return true;
        }
        return false;
    }

    protected void waitToElementVisible(WebDriver driver, String locator) {
        explicitWait = new WebDriverWait(driver, GlobalConstants.LONGTIMEOUT);
        explicitWait.until(ExpectedConditions.visibilityOfElementLocated(getByXpath(locator)));
    }

    protected void waitToElementVisible(WebDriver driver, String locator, String... values) {
        explicitWait = new WebDriverWait(driver, GlobalConstants.LONGTIMEOUT);
        explicitWait.until(ExpectedConditions.visibilityOfElementLocated(getByXpath(getDynamicLocator(locator, values))));
    }

    protected void waitAllToElementVisible(WebDriver driver, String locator) {
        explicitWait = new WebDriverWait(driver, GlobalConstants.LONGTIMEOUT);
        explicitWait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(getByXpath(locator)));
    }

    protected void waitAllToElementVisible(WebDriver driver, String locator, String... values) {
        explicitWait = new WebDriverWait(driver, GlobalConstants.LONGTIMEOUT);
        explicitWait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(getByXpath(getDynamicLocator(locator, values))));
    }

    protected void waitToElementInVisible(WebDriver driver, String locator) {

        explicitWait = new WebDriverWait(driver, GlobalConstants.SHORTTIMEOUT);
        overideImplicitWait(driver, GlobalConstants.SHORTTIMEOUT);
        explicitWait.until(ExpectedConditions.invisibilityOfElementLocated(getByXpath(locator)));
        overideImplicitWait(driver, GlobalConstants.LONGTIMEOUT);
    }

    protected boolean isElementUnDisplayed(WebDriver driver, String locator) {
        overideImplicitWait(driver, GlobalConstants.SHORTTIMEOUT);
        elements = getElements(driver, locator);
        overideImplicitWait(driver, GlobalConstants.LONGTIMEOUT);
        if (elements.size() == 0) {
            return true;
        } else if (elements.size() > 0 && !elements.get(0).isDisplayed()) {
            return true;
        } else {
            return false;
        }
    }

    protected void overideImplicitWait(WebDriver driver, long timeSecond) {
        driver.manage().timeouts().implicitlyWait(timeSecond, TimeUnit.SECONDS);
    }

    protected void waitToElementInVisible(WebDriver driver, String locator, String... values) {
        explicitWait = new WebDriverWait(driver, GlobalConstants.LONGTIMEOUT);
        explicitWait.until(ExpectedConditions.invisibilityOfElementLocated(getByXpath(getDynamicLocator(locator, values))));
    }

    protected void waitToElementClickable(WebDriver driver, String locator) {
        explicitWait = new WebDriverWait(driver, GlobalConstants.LONGTIMEOUT);
        explicitWait.until(ExpectedConditions.elementToBeClickable(getByXpath(locator)));
    }

    protected void waitToElementClickable(WebDriver driver, String locator, String... values) {
        explicitWait = new WebDriverWait(driver, GlobalConstants.LONGTIMEOUT);
        explicitWait.until(ExpectedConditions.elementToBeClickable(getByXpath(getDynamicLocator(locator, values))));
    }

    protected boolean areJQueryAndJSLoadedSuccess(WebDriver driver) {
        WebDriverWait explicitWait = new WebDriverWait(driver, GlobalConstants.LONGTIMEOUT);
        jsExecutor = (JavascriptExecutor) driver;

        ExpectedCondition<Boolean> jQueryLoad = new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver driver) {
                try {
                    return ((Long) jsExecutor.executeScript("return jQuery.active") == 0);
                } catch (Exception e) {
                    return true;
                }
            }
        };

        ExpectedCondition<Boolean> jsLoad = new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver driver) {
                return jsExecutor.executeScript("return document.readyState").toString().equals("complete");
            }
        };

        return explicitWait.until(jQueryLoad) && explicitWait.until(jsLoad);
    }

    protected void sleepInSecond(long time) {
        try {
            Thread.sleep(time * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    private String osName = System.getProperty("os.name");

    protected String getDirectorySlash(String folderName) {
        if (isMac() || isUnix() || isSolaris()) {
            folderName = "/" + folderName + "/";
        } else {
            folderName = "\\" + folderName + "\\";
        }
        return folderName;
    }

    protected boolean isWindows() {
        return (osName.toLowerCase().indexOf("win") >= 0);
    }

    protected boolean isMac() {
        return (osName.toLowerCase().indexOf("mac") >= 0);
    }

    protected boolean isUnix() {
        return (osName.toLowerCase().indexOf("nix") >= 0 || osName.toLowerCase().indexOf("nux") >= 0 || osName.toLowerCase().indexOf("aix") > 0);
    }

    protected boolean isSolaris() {
        return (osName.toLowerCase().indexOf("sunos") >= 0);
    }

    protected boolean isDataStringSortedAscending(WebDriver driver, String locator) {
        ArrayList<String> arrayList = new ArrayList<>();
        /*Find all element matching condition*/
        elementList = getElements(driver, locator);
        /*Get text each element add to Array list*/
        for (WebElement element : elementList) {
            arrayList.add(element.getText());
        }
        System.out.println("======-------------Data UI--------");
        for (String name : arrayList) {
            System.out.println(name + "\n");
        }
        /*copy to 1 new array list  to sort */
        ArrayList<String> sortedList = new ArrayList<>();
        for (String child : arrayList) {
            sortedList.add(child);
        }
        Collections.sort(arrayList);
        System.out.println("======-------------data sorted asc trong code-----");
        for (String name : arrayList) {
            System.out.println(name + "\n");
        }
        return sortedList.equals(arrayList);
    }

    protected boolean isDataStringSortedDesceding(WebDriver driver, String locator) {
        ArrayList<String> arrayList = new ArrayList<>();
        /*Find all element matching condition*/
        elementList = getElements(driver, locator);
        /*Get text each element add to Array list*/
        for (WebElement element : elementList) {
            arrayList.add(element.getText());
        }
        System.out.println("======-------------Data UI--------");
        for (String name : arrayList) {
            System.out.println(name);
        }
        /*copy to 1 new array list  to sort */
        ArrayList<String> sortedList = new ArrayList<>();
        for (String child : arrayList) {
            sortedList.add(child);
        }

        Collections.sort(arrayList);
        System.out.println("======-------------data sorted asc trong code-----");
        for (String name : arrayList) {
            System.out.println(name + "\n");
        }
        Collections.reverse(arrayList);
        System.out.println("======-------------data sorted desc trong code-----");
        for (String name : arrayList) {
            System.out.println("\n" + name);
        }
        return sortedList.equals(arrayList);
    }

    protected boolean isDataFloatSortedAscending(WebDriver driver, String locator) {
        ArrayList<Float> arrayList = new ArrayList<Float>();
        /*Find all element matching condition*/
        elementList = getElements(driver, locator);
        /*Get text each element add to Array list*/
        for (WebElement element : elementList) {
            arrayList.add(Float.parseFloat(element.getText().replace("$", "").replaceAll(",", "").trim()));
        }
        System.out.println("======-------------Data UI--------");
        for (Float name : arrayList) {
            System.out.println(name + "\n");
        }
        /*copy to 1 new array list  to sort */
        ArrayList<Float> sortedList = new ArrayList<Float>();
        for (Float child : arrayList) {
            sortedList.add(child);
        }
        Collections.sort(arrayList);
        System.out.println("======-------------data sorted asc trong code-----");
        for (Float name : arrayList) {
            System.out.println(name + "\n");
        }
        return sortedList.equals(arrayList);
    }

    protected boolean isDataFloatSortedDesceding(WebDriver driver, String locator) {
        ArrayList<Float> arrayList = new ArrayList<Float>();
        /*Find all element matching condition*/
        elementList = getElements(driver, locator);
        /*Get text each element add to Array list*/
        for (WebElement element : elementList) {
            arrayList.add(Float.parseFloat(element.getText().replace("$", "").replaceAll(",", "").trim()));
        }
        System.out.println("======-------------Data UI--------");
        for (Float name : arrayList) {
            System.out.println(name);
        }
        /*copy to 1 new array list  to sort */
        ArrayList<Float> sortedList = new ArrayList<Float>();
        for (Float child : arrayList) {
            sortedList.add(child);
        }

        Collections.sort(arrayList);
        System.out.println("======-------------data sorted asc trong code-----");
        for (Float name : arrayList) {
            System.out.println(name + "\n");
        }
        Collections.reverse(arrayList);
        System.out.println("======-------------data sorted desc trong code-----");
        for (Float name : arrayList) {
            System.out.println("\n" + name);
        }
        return sortedList.equals(arrayList);
    }

    protected boolean isDataDateSortedAscending(WebDriver driver, String locator) {
        ArrayList<Date> arrayList = new ArrayList<>();
        /*Find all element matching condition*/
        elementList = getElements(driver, locator);
        /*Get text each element add to Array list*/
        for (WebElement element : elementList) {
            arrayList.add(convertStringToDate(element.getText()));
        }
        System.out.println("======-------------Data UI--------");
        for (Date name : arrayList) {
            System.out.println(name + "\n");
        }
        /*copy to 1 new array list  to sort */
        ArrayList<Date> sortedList = new ArrayList<>();
        for (Date child : arrayList) {
            sortedList.add(child);
        }
        Collections.sort(arrayList);
        System.out.println("======-------------data sorted asc trong code-----");
        for (Date name : arrayList) {
            System.out.println(name + "\n");
        }
        return sortedList.equals(arrayList);
    }

    protected boolean isDataDateSortedDesceding(WebDriver driver, String locator) {
        ArrayList<Date> arrayList = new ArrayList<>();
        /*Find all element matching condition*/
        elementList = getElements(driver, locator);
        /*Get text each element add to Array list*/
        for (WebElement element : elementList) {
            arrayList.add(convertStringToDate(element.getText()));
        }
        System.out.println("======-------------Data UI--------");
        for (Date name : arrayList) {
            System.out.println(name);
        }
        /*copy to 1 new array list  to sort */
        ArrayList<Date> sortedList = new ArrayList<>();
        for (Date child : arrayList) {
            sortedList.add(child);
        }

        Collections.sort(arrayList);
        System.out.println("======-------------data sorted asc in code-----");
        for (Date name : arrayList) {
            System.out.println(name + "\n");
        }
        Collections.reverse(arrayList);
        System.out.println("======-------------data sorted desc in code-----");
        for (Date name : arrayList) {
            System.out.println("\n" + name);
        }
        return sortedList.equals(arrayList);
    }

    protected Date convertStringToDate(String dateInString) {
        dateInString = dateInString.replace(".", "");
        DateFormat formatter = new SimpleDateFormat("MMM dd yyyy");
        Date date = null;
        try {
            date = formatter.parse(dateInString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }
}
 