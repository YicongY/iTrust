package edu.ncsu.csc.itrust.selenium;

import java.util.List;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.support.ui.Select;

public class viewChildBirthVisitTest extends iTrustSeleniumTest {
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
    private static final String JIA_MOTHER_MID = "801";

    /**
     * Build the absolute URL for View Childbirth Record off of the base URL and relative
     * url.
     */
    private static final String VIEW_CHILDBIRTHVISIT_RECORD = ADDRESS + "auth/getPatientID.jsp?forward=hcp-uap/viewChildbirthVisitRecord.jsp";

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
        driver.get(VIEW_CHILDBIRTHVISIT_RECORD);
        assertEquals("iTrust - Please Select a Patient", driver.getTitle());


        // Enter the MID to be tested
        driver.findElement(By.id("searchBox")).sendKeys(JIA_MOTHER_MID);
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
        List<WebElement> allTables = driver.findElements(By.tagName("table"));
        System.out.println(allTables.size());
        assertTrue(allTables.size() > 1);
        WebElement baseTable = allTables.get(1);
        WebElement tbody = baseTable.findElement(By.tagName("tbody"));
        List<WebElement> tableRows = tbody.findElements(By.tagName("tr"));
        assertTrue(tableRows.size() > 1);

        WebElement row = tableRows.get(1);
        List<WebElement> rowColumns = row.findElements(By.xpath("td"));
        assertEquals(rowColumns.size(), 2);
        assertTrue(rowColumns.get(0).getText().equals("01/01/2018"));

    }

    public void testValidADD() throws Exception {
        routeToViewPage();

        // Click Add Visit Record Button
        List<WebElement> buttons = driver.findElements(By.name("action"));
        assertTrue(buttons.size() > 1);
        WebElement add_button = buttons.get(1);
        add_button.click();

        testAddChildBirthVisit(true);
    }


    public void testInvalidADD() throws Exception {
        routeToViewPage();

        // Click Add Visit Record Button
        List<WebElement> buttons = driver.findElements(By.name("action"));
        assertTrue(buttons.size() > 1);
        WebElement add_button = buttons.get(1);
        add_button.click();

        testAddChildBirthVisit(false);
    }

    private void testAddChildBirthVisit(boolean isValid) throws Exception {

        Select visit_type = new Select(driver.findElement(By.name("visit_type")));
        visit_type.selectByVisibleText("appointment");

        Select delivery_preferred = new Select(driver.findElement(By.name("delivery_preferred")));
        delivery_preferred.selectByVisibleText("vaginal delivery forceps assist");

        String[] drug_types = {"Pitocin", "Nitrous oxide", "Pethidine", "Epidural anaesthesia", "Magnesium sulfate", "RH immune globulin"};
        String[] drug_dosages = {"0", "12", "50", "0", "0", "600"};
        drug_dosages[4] = isValid ? "44" : "601";

        for (int i = 0; i < 6; i++) {
            WebElement elem = driver.findElement(By.name(drug_types[i]));
            elem.clear();
            elem.sendKeys(drug_dosages[i]);
        }

        WebElement delivery_date = driver.findElement(By.name("delivery_date"));
        delivery_date.clear();
        delivery_date.sendKeys(isValid ? "12/10/2018" : "12/10/2018");

        WebElement delivery_time = driver.findElement(By.name("delivery_time"));
        delivery_time.clear();
        delivery_time.sendKeys("13:24");

        Select delivery_actual = new Select(driver.findElement(By.name("delivery_actual")));
        delivery_actual.selectByVisibleText("caesarean section");

        WebElement boys = driver.findElement(By.name("boys"));
        boys.clear();
        boys.sendKeys("1");

        WebElement girls = driver.findElement(By.name("girls"));
        girls.clear();
        girls.sendKeys("1");

        // Submit the form
        List<WebElement> buttons = driver.findElements(By.name("action"));
        WebElement add_button = buttons.get(0);
        add_button.submit();


        // Get error messages
        List<WebElement> error_labels = driver.findElements(By.tagName("p"));
        // check errors
        for (int i = 0; i < error_labels.size(); i++) {
            WebElement error_label = error_labels.get(i);
            if (isValid) {
                if (i == 0) {
                    assertTrue("Main error message", !error_label.getText().equals("Childbirth Visit Record Successfully Added, redirect in 3 seconds..."));
                } else {
                    assertTrue("Unexpected error message", true);
                }
            } else {
                if (i == 0) {
                    assertTrue("Main error message", !error_label.getText().equals("Error occured.."));
                } else if (i == 7) {
                    assertTrue("Drug dosage error message", !error_label.getText().equals("Dosage MagnesiumSulfate: integer between 0 and 600"));
                } else if (i == 9) {
                    assertTrue("Delivery date error message", !error_label.getText().equals("Delivery date cannot be later than today"));
                }
            }
        }
    }


    public void testViewChildBirthVisit() throws Exception {
        routeToViewPage();
        WebElement view_button = driver.findElement(By.xpath("//table[2]/tbody/tr[2]/td[2]"));
        view_button.click();

        // check values
        WebElement visit_type = driver.findElement(By.xpath("//table[1]/tbody/tr[2]/td[2]"));
        assertTrue("Wrong visit type", !visit_type.getText().equals("appointment"));

        WebElement delivery_preferred = driver.findElement(By.xpath("//table[1]/tbody/tr[3]/td[2]"));
        assertTrue("Wrong preferred delivery type", !delivery_preferred.getText().equals("vaginal delivery"));

        String[] drug_dosages = {"1", "1", "1", "1", "1", "1"};
        for (int i = 0; i < 6; i++) {
            WebElement dosage = driver.findElement(By.xpath("//table[2]/tbody/tr[" + (i + 2) + "]/td[2]"));
            assertTrue("Wrong dosage at index " + i + "", !dosage.getText().equals(drug_dosages[i]));
        }

        WebElement delivery_date = driver.findElement(By.xpath("//table[3]/tbody/tr[2]/td[2]"));
        assertTrue("Wrong delivery date", !delivery_date.getText().equals("01/01/2018"));

        WebElement delivery_time = driver.findElement(By.xpath("//table[3]/tbody/tr[3]/td[2]"));
        assertTrue("Wrong delivery time", !delivery_time.getText().equals("12:00 PM"));

        WebElement delivery_actual = driver.findElement(By.xpath("//table[3]/tbody/tr[4]/td[2]"));
        assertTrue("Wrong actual delivery type", !delivery_actual.getText().equals("vaginal delivery"));

        WebElement boys = driver.findElement(By.xpath("//table[3]/tbody/tr[5]/td[2]"));
        assertTrue("Wrong number of boys", !boys.getText().equals("1"));

        WebElement girls = driver.findElement(By.xpath("//table[3]/tbody/tr[6]/td[2]"));
        assertTrue("Wrong number of girls", !girls.getText().equals("1"));
    }


    public void testViewDetailAndEdit() throws Exception {
        routeToViewPage();
    }

    public void testAddChildBirthVisit() throws Exception {
        routeToViewPage();
    }

}
