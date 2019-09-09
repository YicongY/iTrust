package edu.ncsu.csc.itrust.selenium;

import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.support.ui.Select;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

/*
 * This is the Selenium equivalent of FindExpertTest.java
 */
public class ViewAppointmentsTest extends iTrustSeleniumTest {

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
    private static final String VIEW_APPTS = ADDRESS + "auth/patient/viewMyAppts.jsp";

    /**
     * Set up for testing by clearing and recreating all standard data, then
     * performing UC47 specific data generation.
     */
    @Override
    protected void setUp() throws Exception {
        super.setUp();

        gen.clearAllTables();
        gen.standardData();
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

    @Test
    public void testApptCanBeRated() throws Exception {
        // Login as Random User
        //driver.navigate().to("http://localhost:8080/iTrust/");
        driver = (HtmlUnitDriver) login(RANDOM_PERSON_MID, PASSWORD);

        // Navigate to the view appointments page
        driver.get(VIEW_APPTS);

        // Get table rows elements that represent the appointment records
        List<WebElement> elements = driver.findElements(By.xpath("//table/tbody/tr"));

        // Iterate all the rows of the table, and access all the cells
        for (int i = 1; i < elements.size(); i++) { //i = 1: skip table head
            WebElement rowElement = elements.get(i);
            LocalDate currDate = LocalDate.now();
            List<WebElement> tdAll = rowElement.findElements(By.xpath("td"));
            String apptDate = tdAll.get(2).getText().split(" ")[0];

            //convert String to LocalDate
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
            LocalDate localDate = LocalDate.parse(apptDate, formatter);
            boolean isBeforeToday = localDate.isBefore(currDate);
            String ratingStatus = rowElement.findElements(By.xpath("td")).get(5).getText();

            if (isBeforeToday) {
                assertTrue("Appointment is before today, can be rated", ratingStatus.contains("Rate Now!") || ratingStatus.contains("Read Rating"));
            }
        }


    }

    @Test
    public void testApptCanNotBeRated() throws Exception {
        // Login as Random User
        //driver.navigate().to("http://localhost:8080/iTrust/");
        driver = (HtmlUnitDriver) login(RANDOM_PERSON_MID, PASSWORD);

        // Navigate to the appointments page
        driver.get(VIEW_APPTS);

        // Get table rows elements that represent the appointment records
        List<WebElement> elements = driver.findElements(By.xpath("//table/tbody/tr"));

        for (int i = 1; i < elements.size(); i++) { //i = 1: skip table head
            WebElement rowElement = elements.get(i);
            LocalDate currDate = LocalDate.now();
            List<WebElement> tdAll = rowElement.findElements(By.xpath("td"));
            String apptDate = tdAll.get(2).getText().split(" ")[0];

            //convert String to LocalDate
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
            LocalDate localDate = LocalDate.parse(apptDate, formatter);
            boolean isBeforeToday = localDate.isBefore(currDate);
            String ratingStatus = rowElement.findElements(By.xpath("td")).get(5).getText();

            if (!isBeforeToday) {
                assertTrue("Appointment is before today, can be rated", ratingStatus.contains("Incomplete Visit"));
            }
        }


    }


}