package edu.ncsu.csc.itrust.unit.model.ratings;

import edu.ncsu.csc.itrust.exception.FormValidationException;
import edu.ncsu.csc.itrust.model.ConverterDAO;
import edu.ncsu.csc.itrust.model.ratings.Rating;
import edu.ncsu.csc.itrust.model.ratings.RatingValidator;
import edu.ncsu.csc.itrust.unit.datagenerators.TestDataGenerator;
import junit.framework.TestCase;

import javax.sql.DataSource;
import java.sql.SQLException;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class RatingValidatorTest extends TestCase {
    private Rating rating;
    private TestDataGenerator gen;
    private DataSource ds;
    private RatingValidator validator;

    @Override
    protected void setUp() throws Exception {
        ds = ConverterDAO.getDataSource();
        gen = new TestDataGenerator();
        validator = new RatingValidator(ds);
        gen.clearAllTables();

        rating = new Rating();
        rating.setAppt_id(3);
        rating.setDoctor_id(10000L);
        rating.setPatient_id(500L);
        rating.setPunctuality(4F);
        rating.setEfficiency(4F);
        rating.setKnowledge(4F);
        rating.setSkillfulness(4F);
        rating.setAttitude(4F);
        rating.setComment("Test");
    }

    @Test
    public void testValidRating() {
        try{
            validator.validate(rating);
            Assert.assertTrue(true);
        } catch (FormValidationException e) {
            Assert.fail("Should not have any form error");
        }
    }

    @Test
    public void testInvalidPunctuality1() {
        rating.setPunctuality(-1F);

        try{
            validator.validate(rating);
            Assert.fail("Negative punctuality should not pass the form validation!");
        } catch (FormValidationException e) {
            Assert.assertTrue(true);
        }
    }

    @Test
    public void testInvalidPunctuality2() {
        rating.setPunctuality(6F);

        try{
            validator.validate(rating);
            Assert.fail("Overflow punctuality should not pass the form validation!");
        } catch (FormValidationException e) {
            Assert.assertTrue(true);
        }
    }

    @Test
    public void testInvalidAttitude1() {
        rating.setAttitude(-1F);

        try{
            validator.validate(rating);
            Assert.fail("Negative Attitude should not pass the form validation!");
        } catch (FormValidationException e) {
            Assert.assertTrue(true);
        }
    }

    @Test
    public void testInvalidAttitude2() {
        rating.setAttitude(6F);

        try{
            validator.validate(rating);
            Assert.fail("Overflow Attitude should not pass the form validation!");
        } catch (FormValidationException e) {
            Assert.assertTrue(true);
        }
    }

    @Test
    public void testInvalidSkillfulness1() {
        rating.setSkillfulness(-1F);

        try{
            validator.validate(rating);
            Assert.fail("Negative Skillfulness should not pass the form validation!");
        } catch (FormValidationException e) {
            Assert.assertTrue(true);
        }
    }

    @Test
    public void testInvalidSkillfulness2() {
        rating.setSkillfulness(6F);

        try{
            validator.validate(rating);
            Assert.fail("Overflow Skillfulness should not pass the form validation!");
        } catch (FormValidationException e) {
            Assert.assertTrue(true);
        }
    }


    @Test
    public void testInvalidKnowledge1() {
        rating.setKnowledge(-1F);

        try{
            validator.validate(rating);
            Assert.fail("Negative Knowledge should not pass the form validation!");
        } catch (FormValidationException e) {
            Assert.assertTrue(true);
        }
    }

    @Test
    public void testInvalidKnowledge2() {
        rating.setKnowledge(6F);

        try{
            validator.validate(rating);
            Assert.fail("Overflow Knowledge should not pass the form validation!");
        } catch (FormValidationException e) {
            Assert.assertTrue(true);
        }
    }

    @Test
    public void testInvalidEfficiency1() {
        rating.setEfficiency(-1F);

        try{
            validator.validate(rating);
            Assert.fail("Negative Efficiency should not pass the form validation!");
        } catch (FormValidationException e) {
            Assert.assertTrue(true);
        }
    }

    @Test
    public void testInvalidEfficiency2() {
        rating.setEfficiency(6F);

        try{
            validator.validate(rating);
            Assert.fail("Overflow Efficiency should not pass the form validation!");
        } catch (FormValidationException e) {
            Assert.assertTrue(true);
        }
    }
}
