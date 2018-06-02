/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shoreline.bll.ConvStrats;

import java.io.File;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import shoreline.be.Config;
import shoreline.be.ConvTask;
import shoreline.bll.ThreadPool;
import shoreline.dal.DataManager;
import shoreline.dal.Readers.InputReader;
import shoreline.dal.Readers.XLSXReader;
import shoreline.dal.Writers.OutputWriter;
import shoreline.dal.Writers.StringToFile;
import shoreline.exceptions.DALException;
import shoreline.exceptions.GUIException;
import shoreline.gui.model.ModelManager;

/**
 *
 * @author kaspe
 */
public class XLXSConvStratTest {

    private static String userDir = System.getProperty("user.dir");
    private static String inputFilePath;
    private static File inputFile;
    private static String outputFilePath;
    private static File outputFile;
    private static String expectedPath;
    private static DataManager dataManager;
    private static ThreadPool threadPool;
    private static ModelManager modelManager;
    

    public XLXSConvStratTest() {
        
    }

    @BeforeClass
    public static void setUpClass() {
        try {
            inputFilePath = userDir + "\\test\\shoreline\\res\\XLSX\\Import_data.xlsx";
            inputFile = new File(inputFilePath);
            outputFilePath = userDir + "\\test\\shoreline\\res\\JSON\\actual.json";
            outputFile = new File(outputFilePath);
            expectedPath = userDir + "\\test\\shoreline\\res\\JSON\\expected.json";
            dataManager = new DataManager();
            threadPool = ThreadPool.getInstance();
            try {
                modelManager = new ModelManager();
            } catch (GUIException ex) {
                Logger.getLogger(XLXSConvStratTest.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (DALException ex) {
            Logger.getLogger(XLXSConvStratTest.class.getName()).log(Level.SEVERE, null, ex);
        }
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
     * Test of addCallable method, of class XLXSConvStrat.
     */
    @Test
    public void testAddCallable() throws Exception {
        Config config = null;
        for (Config loopConfig : dataManager.getAllConfigs()) {
            if (loopConfig.getName().equals("SiemensXLSX")) {
                config = loopConfig;
            }
        }
        config.setTemplate(dataManager.getTemplate());
        ConvTask task = new ConvTask("TestOutput", inputFile, outputFile, config);
        InputReader reader = new XLSXReader();
        OutputWriter writer = new StringToFile();
        XLXSConvStrat instance = new XLXSConvStrat();
        instance.addCallable(task, reader, writer);
        threadPool.startTask(task);
        while (!task.getFuture().isDone()) {
            // Prevents test from moving on, before file is converted
        }
        
        /* JSONObjects are created with a 'createdOn' field, meaning the files
           themselves will never be exactly the same. This means JSONArrays
           have to be made, and then those need to be compared. */
        Scanner expectedScanner = new Scanner(new File(expectedPath));
        String expectedString = "";
        while (expectedScanner.hasNextLine()) {
            expectedString += expectedScanner.nextLine();
        }
        
        Scanner actualScanner = new Scanner(task.getTarget());
        String actualString = "";
        
        while (actualScanner.hasNextLine()) {
            actualString += actualScanner.nextLine();
        }
        JSONArray expected = new JSONArray(expectedString);
        JSONArray actual = new JSONArray(actualString);
        for (int i = 0; i < 40; i++) {
            String expectedPlanning = expected.getJSONObject(i)
                    .getJSONObject("planning").getString("latestFinishDate");
            String actualPlanning = actual.getJSONObject(i)
                    .getJSONObject("planning").getString("latestFinishDate");
            Assert.assertEquals(expectedPlanning, actualPlanning);
            
            String expectedSystemStatus = expected
                    .getJSONObject(i).getString("systemStatus");
            String actualSystemStatus = actual
                    .getJSONObject(i).getString("systemStatus");
            Assert.assertEquals(expectedSystemStatus, actualSystemStatus);
            
            String expectedName = expected
                    .getJSONObject(i).getString("name");
            String actualName = actual
                    .getJSONObject(i).getString("name");
            Assert.assertEquals(expectedName, actualName);
        }
    }

}
