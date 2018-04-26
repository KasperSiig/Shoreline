/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shoreline.dal.ConvStrats;

import java.io.File;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import shoreline.be.ConvTask;
import shoreline.dal.TitleStrats.TitleImpl;
import shoreline.dal.TitleStrats.XLSXTitleStrat;
import shoreline.exceptions.DALException;

/**
 *
 * @author Kasper Siig
 */
public class XLXSConvStratTest {
    private HashMap<String, Integer> cellIndexMap;
    private String userDir = System.getProperty("user.dir");
    private String testFile;
    
    
    public XLXSConvStratTest() {
        try {
            testFile = userDir + "\\test\\shoreline\\res\\XLSX\\Import_data.xlsx";
            TitleImpl impl = new TitleImpl(new XLSXTitleStrat());
            cellIndexMap = impl.getTitles(new File(testFile));
        } catch (DALException ex) {
            Logger.getLogger(XLXSConvStratTest.class.getName()).log(Level.SEVERE, null, ex);
        }
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
     * Test of convertAndWrite method, of class XLXSConvStrat.
     */
    @Test
    public void testConvertAndWrite() throws Exception {
//        System.out.println("convertAndWrite");
        HashMap mapper = new HashMap<String, String>();
        mapper.put("siteName", "");
        mapper.put("assetSerialNumber", "");
        mapper.put("type", "Order Type");
        mapper.put("externalWorkOrderId", "Order");
        mapper.put("systemStatus", "System status");
        mapper.put("userStatus", "User status");
        mapper.put("name", "Opr. short text");
        mapper.put("priority", "Priority");
        mapper.put("latestFinishDate", "Lat.finish date");
        mapper.put("earliestStartDate", "Earl.start date");
        mapper.put("latestStartDate", "Latest start");
        mapper.put("estimatedTime", "Work");
        
        ConvTask task = new ConvTask(cellIndexMap, mapper, "", "", "", new File(testFile), new File("C:\\JavaProjects\\Shoreline\\test\\shoreline\\res\\XLSX\\Json.json"));
        XLXSConvStrat instance = new XLXSConvStrat();
        instance.convertAndWrite(task);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
        
    }
    
}
