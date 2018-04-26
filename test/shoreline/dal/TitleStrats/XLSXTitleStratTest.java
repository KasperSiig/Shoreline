/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shoreline.dal.TitleStrats;

import java.io.File;
import java.util.HashMap;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Kasper Siig
 */
public class XLSXTitleStratTest {
    
    private String userDir = System.getProperty("user.dir");
    private String testFile;
    
    public XLSXTitleStratTest() {
        testFile = userDir + "\\test\\shoreline\\res\\XLSX\\Import_data.xlsx";
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of getTitles method, of class XLSXTitleStrat.
     */
    @Test
    public void testGetTitles() throws Exception {
        File file = new File(testFile);
        XLSXTitleStrat instance = new XLSXTitleStrat();
        
        HashMap<String, Integer> expResult = new HashMap<>();
        expResult.put("MaintActivType", 11);
        expResult.put("Description", 5);
        expResult.put("Group Counter", 15);
        expResult.put("Functional Loc.", 4);
        expResult.put("Bas. start date", 29);
        expResult.put("Opr. short text", 22);
        
        HashMap<String, Integer> result = instance.getTitles(file);
        assertEquals(expResult.get("Description"), result.get("Description"));
        assertEquals(expResult.get("MaintActivType"), result.get("MaintActivType"));
        assertEquals(expResult.get("Group Counter"), result.get("Group Counter"));
        assertEquals(expResult.get("Functional Loc."), result.get("Functional Loc."));
        assertEquals(expResult.get("Bas. start date"), result.get("Bas. start date"));
        assertEquals(expResult.get("Opr. short text"), result.get("Opr. short text"));
    }
    
}
