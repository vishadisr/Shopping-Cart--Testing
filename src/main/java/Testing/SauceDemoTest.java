package Testing;


import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;


public class SauceDemoTest {

    WebDriver driver;

    @BeforeClass
    public void setUp(){
        System.setProperty("webdriver.chrome.driver","C:\\Users\\Administrator\\IdeaProjects\\Shopping Cart - Test\\src\\main\\resources\\chromedriver.exe");
        driver = new ChromeDriver();
        driver.get("https://www.saucedemo.com/");
    }

    // Login Test
    @Test(priority = 0)
    public void TestLogin(){
        WebElement userNameField = driver.findElement(By.id("user-name"));
        WebElement passwordField = driver.findElement(By.id("password"));
        WebElement loginButton = driver.findElement(By.id("login-button"));

        // login with recommended credentials
        userNameField.sendKeys("visual_user");
        passwordField.sendKeys("secret_sauce");
        loginButton.click();

        // Assert the title of inventory page
        WebElement textTitle = driver.findElement(By.cssSelector(".header_secondary_container .title"));
        assertEquals(textTitle.getText(),"Products","Title is Incorrect");


    }
    @Test(priority = 1)

    public void testProductActions(){

        //verify inventory page url
        assertEquals(driver.getCurrentUrl(),"https://www.saucedemo.com/inventory.html");

        // click on the title of the product named Sauce Labs Bolt T-Shirt
        WebElement product = driver.findElement(By.xpath("//*[@id=\"item_1_title_link\"]/div"));
        product.click();

        String actualProductName = "Sauce Labs Bolt T-Shirt";
        String actualProductPrice = "$15.99";

        WebElement selectedProductName = driver.findElement(By.xpath("//*[@id=\"inventory_item_container\"]/div/div/div[2]/div[1]"));
        WebElement selectedProductPrice = driver.findElement(By.xpath("//*[@id=\"inventory_item_container\"]/div/div/div[2]/div[3]"));

        // Verify the product name and price is similar to the selected product
        assertEquals(selectedProductName.getText(),actualProductName,"Product Name not Matched");
        assertEquals(selectedProductPrice.getText(),actualProductPrice,"Product Price not Matched");

        // add selected product to cart
        WebElement addCartButton = driver.findElement(By.id("add-to-cart-sauce-labs-bolt-t-shirt"));
        addCartButton.click();

        // view cart by clicking cart icon
        WebElement cartIcon = driver.findElement(By.id("shopping_cart_container"));
        cartIcon.click();
        // verify cart url
        assertEquals(driver.getCurrentUrl(),"https://www.saucedemo.com/cart.html");

        //Verify the added product name and the price is similar to selection
        WebElement cartProductName = driver.findElement(By.xpath("//*[@id=\"item_1_title_link\"]/div"));
        WebElement cartProductPrice = driver.findElement(By.xpath("//*[@id=\"cart_contents_container\"]/div/div[1]/div[3]/div[2]/div[2]/div"));

        assertEquals(cartProductName.getText(),actualProductName);
        assertEquals(cartProductPrice.getText(),actualProductPrice);


        // click remove button to remove product from cart
        WebElement removeButton = driver.findElement(By.id("remove-sauce-labs-bolt-t-shirt"));
        removeButton.click();

        // verify product is removed
        boolean isElementPresent = true;
        try {
            cartProductName.getText();
        }
        catch (org.openqa.selenium.StaleElementReferenceException e){
            isElementPresent = false;

        }
        assertFalse(isElementPresent);

        // continue shopping by clicking continue shopping button
        WebElement continueButton = driver.findElement(By.id("continue-shopping"));
        continueButton.click();
        // verify came back to the inventory page
        assertEquals(driver.getCurrentUrl(),"https://www.saucedemo.com/inventory.html");


        // add another two products to cart
        WebElement addBackPack = driver.findElement(By.id("add-to-cart-sauce-labs-backpack"));
        addBackPack.click();
        WebElement addBikeLight = driver.findElement(By.id("add-to-cart-sauce-labs-bike-light"));
        addBikeLight.click();

        // click cart icon to view added products
        WebElement cart = driver.findElement(By.xpath("//*[@id=\"shopping_cart_container\"]/a"));
        cart.click();

        // Verify the product name and price is similar to the selected products
        String actualProductName1 = "Sauce Labs Backpack";
        String actualProductPrice1 = "$29.99";
        String actualProductName2 = "Sauce Labs Bike Light";
        String actualProductPrice2 = "$9.99";

        WebElement cartProductName1 = driver.findElement(By.xpath("//*[@id=\"item_4_title_link\"]/div"));
        WebElement cartProductPrice1 = driver.findElement(By.xpath("//*[@id=\"cart_contents_container\"]/div/div[1]/div[3]/div[2]/div[2]/div"));
        WebElement cartProductName2 = driver.findElement(By.xpath("//*[@id=\"item_0_title_link\"]/div"));
        WebElement cartProductPrice2 = driver.findElement(By.xpath("//*[@id=\"cart_contents_container\"]/div/div[1]/div[4]/div[2]/div[2]/div"));

        assertEquals(cartProductName1.getText(),actualProductName1);
        assertEquals(cartProductPrice1.getText(),actualProductPrice1);
        assertEquals(cartProductName2.getText(),actualProductName2);
        assertEquals(cartProductPrice2.getText(),actualProductPrice2);

        // click on checkout button
        WebElement checkoutButton = driver.findElement(By.id("checkout"));
        checkoutButton.click();

        // fill information
        driver.findElement(By.id("first-name")).sendKeys("first");
        driver.findElement(By.id("last-name")).sendKeys("last");
        driver.findElement(By.id("postal-code")).sendKeys("1000");

        // click on continue
        WebElement contButton = driver.findElement(By.id("continue"));
        contButton.click();

        // get the products price as double
        double productPrice1 = Double.parseDouble(actualProductPrice1.replace("$",""));
        double productPrice2 = Double.parseDouble(actualProductPrice2.replace("$",""));

        // calculate expected item total and convert as string
        double itemTotal = productPrice1 + productPrice2;
        String expectedResult = "Item total: $" + itemTotal;


        //  verify item total
        WebElement actualItemTotal = driver.findElement(By.xpath("//*[@id=\"checkout_summary_container\"]/div/div[2]/div[6]"));
        assertEquals(actualItemTotal.getText(),expectedResult);

        // click finish button
        WebElement finishButton = driver.findElement(By.id("finish"));
        finishButton.click();

        // verify thank you text visible
        WebElement thankYouText = driver.findElement(By.cssSelector("#checkout_complete_container > h2"));
        assertEquals(thankYouText.getText(),"Thank you for your order!");
        


    }

    @AfterClass
    public void tearDown() {
        driver.quit();
    }

}
