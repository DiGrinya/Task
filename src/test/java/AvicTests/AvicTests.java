package AvicTests;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.Test;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static io.github.bonigarcia.wdm.WebDriverManager.chromedriver;
import static org.openqa.selenium.By.xpath;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

public class AvicTests {
    private WebDriver driver;

    @BeforeTest
    public void profileSetUp() {
        chromedriver().setup();
    }

    @BeforeMethod
    public void testsSetUp() {
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.get("https://avic.ua/");
    }

    @Test(priority = 1)
    public void checkThatUrlContainsSearchWord() {
        driver.findElement(xpath("//input[@id='input_search']")).sendKeys("Xiaomi Band", Keys.ENTER);
        assertTrue(driver.getCurrentUrl().contains("query=Xiaomi+Band"));
    }

    @Test(priority = 2)
    public void checkThatProductInQuestionIsAvailable() {
        driver.findElement(xpath("//input[@id='input_search']")).sendKeys("Xiaomi Band", Keys.ENTER);
        driver.findElement(xpath("//a[contains(@href, 'band-6-yellow-item')]")).click();
        driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
        String productsInQuestion = driver.findElement(xpath("//div[contains(@data-ecomm, 'Band 6 Yellow')]//..//a[contains(@href,'availableProductNotification')]")).getAttribute("href");
        assertTrue(productsInQuestion.contains("availableProductNotification"));

    }
    @Test(priority = 3)
    public void checkProductCountInCart() {
        driver.findElement(xpath("//span[@class='sidebar-item']")).click();
        driver.findElement(xpath("//ul[contains(@class,'sidebar-list')]//a[contains(@href, 'game-zone')]")).click();
        driver.findElement(xpath("//div[@class='height brand-box']//a[contains(@href,'igrovie-pristavki')]")).click();
        new WebDriverWait(driver, 30).until(
                webDriver -> ((JavascriptExecutor) webDriver).executeScript("return document.readyState").equals("complete"));
        List<WebElement> elementList = driver.findElements(xpath("//div[@class='container-main']//a[contains(@data-ecomm-cart,'PlayStation 5')]"));
        int actualElementsSize = elementList.size();
        assertEquals(actualElementsSize, 2);
    }


    @Test(priority = 4)
    public void checkAddToCart() {
        driver.findElement(xpath("//span[@class='sidebar-item']")).click();
        driver.findElement(xpath("//ul[contains(@class,'sidebar-list')]//a[contains(@href, 'audio-texnika')]")).click();
        driver.findElement(xpath("//div[@class='height brand-box']//a[contains(@href,'naushniki')]")).click();
        new WebDriverWait(driver, 30).until(
                webDriver -> ((JavascriptExecutor) webDriver).executeScript("return document.readyState").equals("complete"));
        driver.findElement(xpath("//div[@class='container-main']//a[contains(@data-ecomm-cart, 'Marshall Major IV Black')]")).click();
        WebDriverWait wait = new WebDriverWait(driver, 30);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("js_cart")));
        driver.findElement(xpath("//a[contains(@href,'checkout')]")).click();
        String actualProductsCountInCart = driver.findElement(xpath("//div[contains(@data-ecomm-cart, 'Marshall Major IV Black')]")).getText();
        assertTrue(actualProductsCountInCart.contains("Marshall Major IV Black"));
    }

    @AfterMethod
    public void tearDown() {
        driver.close();
    }
}
