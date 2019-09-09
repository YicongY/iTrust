package edu.ncsu.csc.itrust.selenium;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.Select;

import java.util.List;

public class viewLaborDeliveryReport extends iTrustSeleniumTest {
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
    private static final String RANDOM_PERSON = "1";

    /**
     * Build the absolute URL for View Childbirth Record off of the base URL and relative
     * url.
     */
    private static final String VIEW_LABORDELIVERY_REPORT = ADDRESS + "auth/getPatientID.jsp?forward=hcp/viewLaborDeliveryReport.jsp";

    /**
     * Set up for testing by clearing and recreating all standard data, then
     * performing UC47 specific data generation.
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
     * Remove the UC47 specific data and clear all tables.
     */
    @Override
    protected void tearDown() throws Exception {
        gen.clearAllTables();
    }

    private void routeToViewPage() throws Exception  {
        // Login as Kelly Doctor
        driver = chromeDriverLogin(KELLY_DOCTOR_MID, PASSWORD);
        assertEquals("iTrust - HCP Home", driver.getTitle());

        // Navigate to the View ChildBirthVisit Record Page
        driver.get(VIEW_LABORDELIVERY_REPORT);
        assertEquals("iTrust - Please Select a Patient", driver.getTitle());


        // Enter the MID to be tested
        driver.findElement(By.id("searchBox")).sendKeys(RANDOM_PERSON);
        WebElement div = driver.findElement(By.id("searchTarget"));
        List<WebElement> allTables = driver.findElements(By.tagName("table"));
        System.out.println(allTables.size());
        assertTrue(allTables.size() > 2);
        WebElement baseTable = allTables.get(2);
        WebElement tbody = baseTable.findElement(By.tagName("tbody"));
        List<WebElement> tableRows = tbody.findElements(By.tagName("tr"));

        assertTrue(tableRows.size() > 1);

        // Check the row for the test user with MID 800
        WebElement row = tableRows.get(1);
        List<WebElement> rowColumns = row.findElements(By.xpath("td"));
        assertEquals(rowColumns.size(), 3);
        // TODO: assert name later
        // assertTrue(rowColumns.get(1).getText().equals("Mother"));
        // assertTrue(rowColumns.get(1).getText().equals("Jia"));
        rowColumns.get(0).click();
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
    public void testViewChildBirthVisitDisplay() throws Exception {
        routeToViewPage();


        // Click the Random Person to go to the real ViewChildBirthVisit page
//        List<WebElement> allTables = driver.findElements(By.tagName("table"));
//        System.out.println(allTables.size());
//        assertTrue(allTables.size() > 1);
//        WebElement baseTable = allTables.get(1);
//        WebElement tbody = baseTable.findElement(By.tagName("tbody"));
//        List<WebElement> tableRows = tbody.findElements(By.tagName("tr"));
//        assertTrue(tableRows.size() > 1);
//
//        WebElement row = tableRows.get(1);
//        List<WebElement> rowColumns = row.findElements(By.xpath("td"));
//        assertEquals(rowColumns.size(), 2);
//        assertTrue(rowColumns.get(0).getText().equals("01/01/2018"));

    }

}
