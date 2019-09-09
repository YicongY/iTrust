package edu.ncsu.csc.itrust.selenium;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.support.ui.Select;

/*
 * This is the Selenium equivalent of FindExpertTest.java
 */
public class NewUseCaseTest extends iTrustSeleniumTest {

    /**
     * Selenium html unit driver.
     */
    private HtmlUnitDriver driver;
    //private WebDriver driver;

    /**
     * MID of the test user "Random Person"
     */
    private static final String RANDOM_PERSON_MID = "1";

    /**
     * Default user password.
     */
    private static final String PASSWORD = "pw";

    /**
     * Build the absolute URL for Find Expert off of the base URL and relative
     * url.
     */
    private static final String FIND_EXPERT = ADDRESS + "auth/patient/findExpert.jsp";

    /**
     * Set up for testing by clearing and recreating all standard data, then
     * performing UC47 specific data generation.
     */
    @Override
    protected void setUp() throws Exception {
        super.setUp(); // clear tables is called in super
//        String pathToChromeDriver = "./chromedriver";
//        System.setProperty("webdriver.chrome.driver", pathToChromeDriver);
//        String current_dir = System.getProperty("user.dir");
//        System.out.println(current_dir);
//        driver = new ChromeDriver();

        gen.clearAllTables();
        gen.standardData();
        gen.uc47SetUp();
        gen.uc807SetUp();
    }

    /**
     * Remove the UC47 specific data and clear all tables.
     */
    @Override
    protected void tearDown() throws Exception {
        gen.uc47TearDown();
        gen.uc807TearDown();
        //gen.clearAllTables();
    }

    /**
     * Test the displayed distances when searching for an expert. According to
     * the iTrust Wiki experts should be displayed by hospital location, not by
     * their personal listed address.
     *
     * EX: A doctor that lives in New York but is a provider for a hospital in
     * Raleigh, NC should be displayed as from Raleigh, NC. When a user from
     * Raleigh, NC searches for an expert, that doctor should be displayed with
     * a distance of zero miles.
     */
    public void testRatingsPreview() throws Exception {
        // Login as Random User
        //driver.navigate().to("http://localhost:8080/iTrust/");
        driver = (HtmlUnitDriver) login(RANDOM_PERSON_MID, PASSWORD);

        // Navigate to the Find Expert page
        driver.get(FIND_EXPERT);

        // Verify Specialty: All Doctors, ZIP Code: 27606, Distance: All, Sort
        // By: Rating

        Select select;

        select = new Select(driver.findElement(By.name("specialty")));
        select.selectByVisibleText("All Doctors");

        WebElement zipcode = driver.findElement(By.name("zipCode"));
        zipcode.clear();
        zipcode.sendKeys("27606");

        select = new Select(driver.findElement(By.name("range")));
        select.selectByVisibleText("All");

        select = new Select(driver.findElement(By.name("sortby")));
        select.selectByVisibleText("Rating");

        // Submit the form
        driver.findElement(By.name("findExpert")).click();


        // Verify rating for the doctors.
        // Shelly Vang: N/A
        // John Zoidberg: N/A
        // Kelly Doctor: 4
        // Gandalf Stormcrow: 3.4

        // Get container elements that group doctor information
        List<WebElement> elements = driver.findElements(By.className("grey-border-container"));

        // Number of doctors found
        int doctors = 0;

        // Search through the list of doctors
        for (WebElement doc : elements) {

            // Get the doctor name
            WebElement docID = doc.findElements(By.tagName("p")).get(0);
            String name = docID.findElement(By.tagName("a")).getText();

            //Get the doctor's rate
            String rate = doc.findElements(By.tagName("p")).get(4).getText();

            // If doctor is Shelly Vang, verify rating is N/A
            if (name.equals("Shelly Vang")) {
                doctors++;
                assertTrue("Rating is N/A", rate.contains("N/A"));
            }
            // If doctor is John Zoidberg, verify rating is N/A
            else if (name.equals("John Zoidberg")) {
                doctors++;
                assertTrue("Rating is N/A", rate.contains("N/A"));
            }
            // If doctor is Kelly Doctor, verify rating is N/A
            else if (name.equals("Kelly Doctor")) {
                doctors++;
                assertTrue("Avg Rating is 4", rate.contains("4"));
            }
            // If doctor is Gandalf Stormcrow, verify rating is 3.4
            else if (name.equals("Gandalf Stormcrow")) {
                doctors++;
                assertTrue("Rating is 3.4", rate.contains("3.4"));
            }
        }
    }


    // Verify details rating for Gandalf Stormcrow
    // 3 4 4 3 3
    public void testRatingsVisualizationPage() throws Exception {
        // Login as Random User
        driver = (HtmlUnitDriver)login(RANDOM_PERSON_MID, PASSWORD);

        // Navigate to the Find Expert page
        driver.get(FIND_EXPERT);

        // Verify Specialty: All Doctors, ZIP Code: 27606, Distance: All, Sort
        // By: Rating

        Select select;

        select = new Select(driver.findElement(By.name("specialty")));
        select.selectByVisibleText("All Doctors");

        WebElement zipcode = driver.findElement(By.name("zipCode"));
        zipcode.clear();
        zipcode.sendKeys("27606");

        select = new Select(driver.findElement(By.name("range")));
        select.selectByVisibleText("All");

        select = new Select(driver.findElement(By.name("sortby")));
        select.selectByVisibleText("Rating");

        // Submit the form
        driver.findElement(By.name("findExpert")).click();

        // Get container elements that group doctor information
        List<WebElement> anchors = driver.findElements(By.tagName("a"));
        for (WebElement link : anchors) {
            if(link.getAttribute("href").contains("reviewsPage.jsp?expertID=9000000003")) {
                link.click();
                List<WebElement> elements = driver.findElements(By.className("number-rating"));
                assertTrue("Avg Punctuality is 3", elements.get(0).getText().contains("3"));
                assertTrue("Avg Attitude is 4", elements.get(1).getText().contains("4"));
                assertTrue("Avg Skillfulness is 4", elements.get(2).getText().contains("4"));
                assertTrue("Avg Knowledge is 3", elements.get(3).getText().contains("3"));
                assertTrue("Avg Efficiency is 3", elements.get(4).getText().contains("3"));
                break;
            }
        }
    }


    // Verify details rating for Kelly Doctor
    // 4 4 4 4 4
    public void testRatingsVisualizationPage2() throws Exception {
        // Login as Random User
        driver = (HtmlUnitDriver)login(RANDOM_PERSON_MID, PASSWORD);

        // Navigate to the Find Expert page
        driver.get(FIND_EXPERT);

        // Verify Specialty: All Doctors, ZIP Code: 27606, Distance: All, Sort
        // By: Rating

        Select select;

        select = new Select(driver.findElement(By.name("specialty")));
        select.selectByVisibleText("All Doctors");

        WebElement zipcode = driver.findElement(By.name("zipCode"));
        zipcode.clear();
        zipcode.sendKeys("27606");

        select = new Select(driver.findElement(By.name("range")));
        select.selectByVisibleText("All");

        select = new Select(driver.findElement(By.name("sortby")));
        select.selectByVisibleText("Rating");

        // Submit the form
        driver.findElement(By.name("findExpert")).click();

        // Get container elements that group doctor information
        List<WebElement> anchors = driver.findElements(By.tagName("a"));
        for (WebElement link : anchors) {
            if(link.getAttribute("href").contains("reviewsPage.jsp?expertID=9000000000")) {
                link.click();
                List<WebElement> elements = driver.findElements(By.className("number-rating"));
                assertTrue("Avg Punctuality is 4", elements.get(0).getText().contains("4"));
                assertTrue("Avg Attitude is 4", elements.get(1).getText().contains("4"));
                assertTrue("Avg Skillfulness is 4", elements.get(2).getText().contains("4"));
                assertTrue("Avg Knowledge is 4", elements.get(3).getText().contains("4"));
                assertTrue("Avg Efficiency is 4", elements.get(4).getText().contains("4"));
                break;
            }
        }
    }
}