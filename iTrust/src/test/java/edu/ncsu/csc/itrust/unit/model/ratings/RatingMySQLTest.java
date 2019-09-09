package edu.ncsu.csc.itrust.unit.model.ratings;
import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;
import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.model.ConverterDAO;
import edu.ncsu.csc.itrust.model.ratings.Rating;
import edu.ncsu.csc.itrust.model.ratings.RatingMySQL;
import edu.ncsu.csc.itrust.unit.datagenerators.TestDataGenerator;
import junit.framework.TestCase;
import javax.sql.DataSource;
import java.util.List;

import org.junit.After;
import org.junit.Test;
import org.mockito.Mock;

import java.sql.ResultSet;


public class RatingMySQLTest extends TestCase {

    private DataSource ds;
    private RatingMySQL ratingsql;
    private TestDataGenerator gen;

    @Mock
    private DataSource mockDataSource;
    @Mock
    private Connection mockConnection;
    @Mock
    private PreparedStatement mockPreparedStatement;
    @Mock
    private ResultSet mockResultSet;
    private RatingMySQL mockRatingsql;

    @Override
    public void setUp() throws Exception {
        ds = ConverterDAO.getDataSource();
        ratingsql = new RatingMySQL(ds);
        gen = new TestDataGenerator();
        gen.clearAllTables();
        gen.uc807();

    }

    @After
    public void tearDown() throws Exception {
        gen.clearAllTables();
    }

    @Test
    public void testConstructor() {
        boolean thrown = false;
        try {
            RatingMySQL testRatingMySQL = new RatingMySQL();
        } catch(DBException dbe) {
            thrown = true;
        }
        assertTrue(thrown);
    }

    @Test
    public void testGetRatingsForDoctor() throws Exception {
        List<Rating> retList = ratingsql.getRatingsForDoctor(9000000003L);
        assertEquals(1, retList.size());
    }

    @Test
    public void testGetAll() throws Exception {
        List<Rating> retList = ratingsql.getAll();
        assertEquals(1, retList.size());
    }

    @Test
    public void testGetByID() throws Exception {
        Rating ret = ratingsql.getByID(2000);
        assertEquals(Integer.valueOf(2000), ret.getAppt_id());
    }


    @Test
    public void testGetByIDWithInvalidID() throws Exception {
        Rating ret = ratingsql.getByID(188);
        assertNull(ret);
    }

    @Test
    public void testAdd() throws Exception {
        Rating addObj = new Rating();
        addObj.setAppt_id(3);
        addObj.setDoctor_id(10000L);
        addObj.setPatient_id(500L);
        addObj.setPunctuality(4F);
        addObj.setEfficiency(4F);
        addObj.setKnowledge(4F);
        addObj.setSkillfulness(4F);
        addObj.setAttitude(4F);
        addObj.setComment("Test");

        boolean ret = ratingsql.add(addObj);
        assertEquals(true, ret);
    }

    @Test
    public void testUpdate() throws Exception {
        Rating updateObj = new Rating();
        updateObj.setAppt_id(2000);
        updateObj.setDoctor_id(900000003L);
        updateObj.setPatient_id(2L);
        updateObj.setPunctuality(5F);
        updateObj.setEfficiency(5F);
        updateObj.setKnowledge(5F);
        updateObj.setSkillfulness(5F);
        updateObj.setAttitude(5F);
        updateObj.setComment("Test");

        boolean ret = ratingsql.update(updateObj);
        assertEquals(true, ret);
    }
}
