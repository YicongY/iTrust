package edu.ncsu.csc.itrust.unit.model.ratings;

import edu.ncsu.csc.itrust.model.ratings.Rating;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class RatingTest {
    Rating rating;

    @Before
    public void setup() {
        rating = new Rating();
    }

    @Test
    public void testApptId() {
        rating.setAppt_id(1);
        Assert.assertTrue(rating.getAppt_id() == 1);
    }

    @Test
    public void testDoctorId() {
        rating.setDoctor_id(1L);
        Assert.assertTrue(rating.getDoctor_id() == 1L);
    }

    @Test
    public void testPatientId() {
        rating.setPatient_id(1L);
        Assert.assertTrue(rating.getPatient_id() == 1L);
    }

    @Test
    public void testPunctuality() {
        rating.setPunctuality(0.5f);
        Assert.assertTrue(rating.getPunctuality() == 0.5f);
    }

    @Test
    public void testAttitude() {
        rating.setAttitude(0.5f);
        Assert.assertTrue(rating.getAttitude() == 0.5f);
    }

    @Test
    public void testSkillfulness() {
        rating.setSkillfulness(0.5f);
        Assert.assertTrue(rating.getSkillfulness() == 0.5f);
    }

    @Test
    public void testKnowledge() {
        rating.setKnowledge(0.5f);
        Assert.assertTrue(rating.getKnowledge() == 0.5f);
    }

    @Test
    public void testEfficiency() {
        rating.setEfficiency(0.5f);
        Assert.assertTrue(rating.getEfficiency() == 0.5f);
    }

    @Test
    public void testComment() {
        rating.setComment("Good!");
        Assert.assertTrue("Good!".equals(rating.getComment()));
    }

}
