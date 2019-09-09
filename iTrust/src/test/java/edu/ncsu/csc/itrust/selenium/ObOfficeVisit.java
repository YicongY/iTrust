package edu.ncsu.csc.itrust.selenium;
import edu.ncsu.csc.itrust.unit.datagenerators.TestDataGenerator;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.Select;

import java.util.List;

import static junit.framework.TestCase.assertEquals;

public class ObOfficeVisit extends iTrustSeleniumTest {
    /**
     * Selenium html unit driver.
     */
    //private HtmlUnitDriver driver;
    private WebDriver driver;

    /**
     * MID of the test user "Kelly Doctor"
     */
    private static final String KELLY_DOCTOR_MID = "9000000000";

    /**
     * Default user password.
     */
    private static final String PASSWORD = "pw";

    /**
     * MID of the test user input "Random Person"
     */
    private static final String TEST_MID = "1";

    /**
     * Build the absolute URL for View Childbirth Record off of the base URL and relative
     * url.
     */
    private static final String VIEW_OBOFFICE_VISIT = ADDRESS + "auth/hcp/viewObOfficeVisit.jsp";

    /**
     * Set up for testing by clearing and recreating all standard data, then
     * performing UC94 specific data generation.
     */
    @Override
    protected void setUp() throws Exception {
        super.setUp(); // clear tables is called in super

        String pathToChromeDriver = "./chromedriver.exe";
        String os_name = System.getProperty("os.name");
        if(!os_name.startsWith("W")){
            pathToChromeDriver = "./chromedriver";}
        System.setProperty("webdriver.chrome.driver", pathToChromeDriver);
        String current_dir = System.getProperty("user.dir");
        System.out.println(current_dir);
        driver = new ChromeDriver();


        gen.clearAllTables();
        gen.standardData();
    }

    /**
     * Remove the UC94 specific data and clear all tables.
     */
    @Override
    protected void tearDown() throws Exception {
        gen.clearAllTables();
    }

    private void Checkdelete() throws  Exception{
        RouteToViewPage();
        List<WebElement> allTables = driver.findElements(By.tagName("table"));
        assertTrue(allTables.size() > 1);
        WebElement baseTable = allTables.get(1);
        WebElement tbody = baseTable.findElement(By.tagName("tbody"));
        List<WebElement> tableRows = tbody.findElements(By.tagName("tr"));
        assertEquals(tableRows.size(),2);


    }

    private void RouteToViewPage() throws Exception  {
        // Login as Kelly Doctor
        driver = chromeDriverLogin(KELLY_DOCTOR_MID, PASSWORD);
        assertEquals("iTrust - HCP Home", driver.getTitle());

        // Navigate to the View ChildBirthVisit Record Page
        driver.get(VIEW_OBOFFICE_VISIT);
        assertEquals("iTrust - Please Select a Patient", driver.getTitle());

        // Enter the MID to be tested
        driver.findElement(By.id("searchBox")).sendKeys(TEST_MID);
        WebElement div = driver.findElement(By.id("searchTarget"));
        List<WebElement> allTables = driver.findElements(By.tagName("table"));
        assertTrue(allTables.size() >=1);
        WebElement baseTable = allTables.get(2);
        WebElement tbody = baseTable.findElement(By.tagName("tbody"));
        List<WebElement> tableRows = tbody.findElements(By.tagName("tr"));

        assertTrue(tableRows.size() > 1);

        // Check the row for the test user with MID 1
        WebElement row = tableRows.get(1);
        List<WebElement> rowColumns = row.findElements(By.xpath("td"));
        assertEquals(rowColumns.size(), 4);
        rowColumns.get(0).click();
    }

    /**
     Test the view jsp for OB office Visit
     */
    public void testObOfficeVisitDisplay() throws Exception {
        RouteToViewPage();


        // Click the Random Person to go to the real ViewChildBirthVisit page
        List<WebElement> allTables = driver.findElements(By.tagName("table"));
        assertTrue(allTables.size() > 1);
        WebElement baseTable = allTables.get(1);
        WebElement tbody = baseTable.findElement(By.tagName("tbody"));
        List<WebElement> tableRows = tbody.findElements(By.tagName("tr"));
        assertTrue(tableRows.size() > 1);
        WebElement row = tableRows.get(1);
        List<WebElement> rowColumns = row.findElements(By.xpath("td"));
        assertEquals(rowColumns.size(), 3);
        assertTrue(rowColumns.get(0).getText().equals("05/15/2017"));
    }

    /**
     Test the add function for OB office Visit
     */
    public void testObOfficeVisitADD() throws Exception {
        RouteToViewPage();

        //click to add new record
        List<WebElement> ele = driver.findElements(By.name("action"));
        ele.get(0).sendKeys(Keys.ENTER);
        //set new form
        Select placenta_type = new Select(driver.findElement(By.name("placenta")));
        placenta_type.selectByVisibleText("true");

        WebElement babys = driver.findElement(By.name("numBaby"));
        babys.clear();
        babys.sendKeys("2");

        WebElement blood_pressure = driver.findElement(By.name("blood_pressure"));
        blood_pressure.clear();
        blood_pressure.sendKeys("123/123");

        WebElement heart_rate = driver.findElement(By.name("heart_rate"));
        heart_rate.clear();
        heart_rate.sendKeys("86");

        WebElement weight = driver.findElement(By.name("weight"));
        weight.clear();
        weight.sendKeys("123");

        //add the new office visit record
        List<WebElement> action = driver.findElements(By.name("action"));
        action.get(0).sendKeys(Keys.ENTER);
        //back to the page to check whether return successfully
        WebElement success_element = driver.findElement(By.id("test_id"));
        assertTrue(success_element.getText().contains("OB Office Visit Record Successfully Add"));

    }

    /**
     Test the update for OB office Visit
     */
    public void testObOfficeVisitUpdate() throws Exception {
        RouteToViewPage();

        //click to add new record
        List<WebElement> elements = driver.findElements(By.tagName("table"));
        WebElement record_table = elements.get(1);
        WebElement tbody = record_table.findElement(By.tagName("tbody"));
        List<WebElement> tableRows = tbody.findElements(By.tagName("tr"));
        WebElement row = tableRows.get(1);
        WebElement targetElement = row .findElements(By.xpath("td")).get(1);
        targetElement.click();


        //set new form
        Select placenta_type = new Select(driver.findElement(By.name("placenta")));
        placenta_type.selectByVisibleText("true");

        WebElement babys = driver.findElement(By.name("numBaby"));
        babys.clear();
        babys.sendKeys("2");

        WebElement blood_pressure = driver.findElement(By.name("blood_pressure"));
        blood_pressure.clear();
        blood_pressure.sendKeys("110/123");

        WebElement heart_rate = driver.findElement(By.name("heart_rate"));
        heart_rate.clear();
        heart_rate.sendKeys("90");

        WebElement weight = driver.findElement(By.name("weight"));
        weight.clear();
        weight.sendKeys("99");

        //add the new office visit record
        List<WebElement> action = driver.findElements(By.name("action"));
        action.get(0).sendKeys(Keys.ENTER);
        //back to the page to check whether update successfully

        elements = driver.findElements(By.tagName("table"));
        record_table = elements.get(1);
        tbody = record_table.findElement(By.tagName("tbody"));
        tableRows = tbody.findElements(By.tagName("tr"));
        WebElement row_info = tableRows.get(2).findElements(By.tagName("td")).get(1);
        assertEquals(row_info.getText(), "11-5");
        row_info = tableRows.get(3).findElements(By.tagName("td")).get(1);
        assertEquals(row_info.getText(), "99.0");
        row_info = tableRows.get(4).findElements(By.tagName("td")).get(1);
        assertEquals(row_info.getText(), "110/123");
        row_info = tableRows.get(5).findElements(By.tagName("td")).get(1);
        assertEquals(row_info.getText(), "90");


    }


    /**
     Test the view jsp for OB office Visit
     */
    public void testObOfficeVisitDelete() throws Exception {
        RouteToViewPage();


        // Click the Random Person to go to the real ViewChildBirthVisit page
        List<WebElement> allTables = driver.findElements(By.tagName("table"));
        assertTrue(allTables.size() > 1);
        WebElement baseTable = allTables.get(1);
        WebElement tbody = baseTable.findElement(By.tagName("tbody"));
        List<WebElement> tableRows = tbody.findElements(By.tagName("tr"));
        assertEquals(tableRows.size(),3);
        WebElement row = tableRows.get(1);
        List<WebElement> rowColumns = row.findElements(By.xpath("td"));
        assertEquals(rowColumns.size(), 3);
        rowColumns.get(2).click();
        //Check whether the record get delete or not
        Checkdelete();
    }

}