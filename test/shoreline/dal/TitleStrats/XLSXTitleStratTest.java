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
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author Kasper Siig
 */
public class XLSXTitleStratTest {
    
    private String userDir = System.getProperty("user.dir");
    private String testFilePath;
    private File file;
    
    public XLSXTitleStratTest() {
        testFilePath = userDir + "\\test\\shoreline\\res\\XLSX\\Import_data.xlsx";
        file = new File(testFilePath);
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
        XLSXTitleStrat instance = new XLSXTitleStrat();
        
        HashMap<String, Integer> titleIndexMap = instance.getTitles(file);
        int expectedSize = 41;
        int count = titleIndexMap.size();
        assertEquals(expectedSize, count);
        
        assertEquals(6, titleIndexMap.get("Equipment"), 0);
        assertEquals(7, titleIndexMap.get("Description1"), 0);
        assertEquals(22, titleIndexMap.get("Opr. short text"), 0);
        assertEquals(14, titleIndexMap.get("Group"), 0);
        assertEquals(27, titleIndexMap.get("Actual work"), 0);
    }
    
}
