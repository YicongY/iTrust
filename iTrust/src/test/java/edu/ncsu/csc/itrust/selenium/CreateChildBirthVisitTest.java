package edu.ncsu.csc.itrust.selenium;
import edu.ncsu.csc.itrust.unit.datagenerators.TestDataGenerator;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import static junit.framework.TestCase.assertEquals;


public class CreateChildBirthVisitTest{

    private WebDriver driver;
    private TestDataGenerator gen;



    @Before
    public void setUp() throws Exception {
        String pathToChromeDriver = "./chromedriver.exe";
        String os_name = System.getProperty("os.name");
        if(!os_name.startsWith("W")){
            pathToChromeDriver = "./chromedriver";}

        System.setProperty("webdriver.chrome.driver", pathToChromeDriver);
        String current_dir = System.getProperty("user.dir");
        System.out.println(current_dir);

        driver = new ChromeDriver();
//        gen = new TestDataGenerator();
//        gen.clearAllTables();
//        gen.standardData();


    }

    @Test
    public void dummyTest(){
        // test
        String baseURL = "https://www.google.com";
        String testURL = "http://the-internet.herokuapp.com/login";
        driver.navigate().to(testURL);
        driver.findElement(By.id("username")).sendKeys("tomsmith");
        driver.findElement(By.id("password")).sendKeys("SuperSecretPassword!");
        driver.findElement(By.className("radius")).click();
        String title = driver.findElement(By.cssSelector(".example h2")).getText();;
        assertEquals("Secure Area", title);
//        assertThat(title, is("Secure Area"));
    }
}
