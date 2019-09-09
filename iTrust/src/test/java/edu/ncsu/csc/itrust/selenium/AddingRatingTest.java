package edu.ncsu.csc.itrust.selenium;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import java.util.List;

/**
 * @ClassName:    AddingRatingTest
 * @Description:  test Add Rating and View Rating workflow
 * @Author:       Xiaocong Yu
 **/
public class AddingRatingTest extends iTrustSeleniumTest{
    /**
     * Selenium html unit driver.
     */

    private WebDriver driver;

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
    private static final String VIEW_APPTS = ADDRESS + "auth/patient/viewMyAppts.jsp";


    private static final String DEFAULT_DOCTOR_NAME = "Kelly Doctor";
    private static final String DEFAULT_DATE = "12/05/2018 09:00 AM";
    private static final String DEFAULT_DATE_FOR_VIEW = "10/14/2018 08:00 AM";
    private static final String DEFAULT_DOCTOR_NAME_FOR_VIEW = "Gandalf Stormcrow";
    private static final String DEFAULT_COMMENT = "test comment";
    private static final String DEFAULT_COMMENT_FOR_VIEW = "Ok.";
    private static final String RANDOM_PERSON_MID_VIEW = "2";

    /**
     * Set up for testing by clearing and recreating all standard data, then
     * performing UC47 specific data generation.
     */
    @Override
    protected void setUp() throws Exception {
        super.setUp();
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

    /**
     * used for execute js command
     * @param ins
     * @param driver
     * @return
     */
    private String javascript_executor(String ins, ChromeDriver driver){
            JavascriptExecutor jsexe = (JavascriptExecutor) driver;
            String result = (String) jsexe.executeScript(ins);
            return result;
    }

    /**
     * test Adding Rating workflow
     * @throws Exception
     */
    public void testPageNavigation_test() throws Exception {
        // Login as Ratndom User
        driver = chromeDriverLogin(RANDOM_PERSON_MID, PASSWORD);

        // Navigate to the view appointments page
        driver.get(VIEW_APPTS);

        int index = 1;
        String[] rating_metrics = new String[]{"punctuality", "attitude" ,"skillfulness"
                ,"knowledge","efficiency"};

        // Get table rows elements that represent the appointment records
        List<WebElement> elements = driver.findElements(By.xpath("//table/tbody/tr"));
        String apptDate = null;
        String apptDoctor = null;
        for (int i = 1; i < elements.size(); i++){ //i = 1: skip table head
            WebElement rowElement = elements.get(i);
            WebElement targetElement = rowElement.findElements(By.xpath("td")).get(5);
            String ratingStatus = targetElement.getText();
            if(ratingStatus.contains("Rate Now!")){
                List<WebElement> tdAll = rowElement.findElements(By.xpath("td"));
                apptDate = tdAll.get(2).getText();
                apptDoctor = tdAll.get(0).getText();
                targetElement.click();
                break;
            }
        }

        WebElement table = driver.findElements(By.tagName("table")).get(0);
        List<WebElement> tableRows = table.findElements(By.tagName("tr"));

        WebElement nameElement = tableRows.get(1).findElements(By.tagName("td")).get(1);
        WebElement dateElement = tableRows.get(2).findElements(By.tagName("td")).get(1);
        assertEquals(nameElement.getText(), apptDoctor);
        assertEquals(dateElement.getText(), apptDate);

        // fill in the fields needed
        WebElement commentTmpElement = tableRows.get(3).findElements(By.tagName("td")).get(1);
        WebElement commentElement = commentTmpElement.findElements(By.tagName("textarea")).get(0);
        commentElement.sendKeys(DEFAULT_COMMENT);

        for(int i = 0; i < rating_metrics.length; i++){
            String js_scipt = "document.getElementById('input" + index + "_" + rating_metrics[i] + "').setAttribute('checked', 'checked');";
            // change the ratings to value == 5
            javascript_executor(js_scipt, (ChromeDriver) driver);
        }

        List<WebElement> ele = driver.findElements(By.name("action"));
        ele.get(0).sendKeys(Keys.ENTER);
        WebElement success_element = driver.findElement(By.id("test_id"));
        assertTrue(success_element.getText().contains("Childbirth Visit Record Successfully Updated"));
    }

    /**
     * test for view rating details of existed rating
     * @throws Exception
     */
    public void testViewRatings() throws Exception {
        // Login as Ratndom User
        driver = chromeDriverLogin(RANDOM_PERSON_MID_VIEW, PASSWORD);

        // Navigate to the view appointments page
        driver.get(VIEW_APPTS);

        // Get table rows elements that represent the appointment records
        List<WebElement> elements = driver.findElements(By.xpath("//table/tbody/tr"));
        String apptDate = null;
        String apptDoctor = null;

        for (int i = 1; i < elements.size(); i++){ //i = 1: skip table head
            WebElement rowElement = elements.get(i);
            WebElement targetElement = rowElement.findElements(By.tagName("td")).get(5);
            String ratingStatus = targetElement.getText();
            if(ratingStatus.contains("Read")){
                List<WebElement> tdAll = rowElement.findElements(By.tagName("td"));
                apptDate = tdAll.get(2).getText();
                apptDoctor = tdAll.get(0).getText();
                targetElement.findElements(By.tagName("a")).get(0).click();
                break;
            }
        }

        WebElement table = driver.findElements(By.tagName("table")).get(0);
        List<WebElement> tableRows = table.findElements(By.tagName("tr"));

        WebElement nameElement = tableRows.get(1).findElements(By.tagName("td")).get(1);
        WebElement dateElement = tableRows.get(2).findElements(By.tagName("td")).get(1);
        assertEquals(nameElement.getText(), apptDoctor);
        assertEquals(dateElement.getText(), apptDate);

        String currentDoctor = tableRows.get(1).findElements(By.tagName("td")).get(1).getText();
        String currentDate = tableRows.get(2).findElements(By.tagName("td")).get(1).getText();
        assertTrue(currentDoctor.equals(DEFAULT_DOCTOR_NAME_FOR_VIEW));
        assertTrue(currentDate.equals(DEFAULT_DATE_FOR_VIEW));

        int index = 1;
        String[] rating_metrics = new String[]{"punctuality", "attitude" ,"skillfulness"
                ,"knowledge","efficiency"};
        for(int i = 0; i < rating_metrics.length; i++){
            String current_metric = driver.findElement(By.id(rating_metrics[i] + index)).getAttribute("checked");
            // all target has been checked, attr checked equals true
            assertNotNull(current_metric);
        }

    }
}

