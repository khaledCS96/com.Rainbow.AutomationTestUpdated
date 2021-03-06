package com.Rainbow.Testcases;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import org.openqa.selenium.By;

import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.Rainbow.base.TestBase;

public class ReadWriteDataFromCSV extends TestBase{
	
	public static WebDriverWait wait;
	
	public ReadWriteDataFromCSV() throws IOException {
		super();

	}
	
	   private static final String SAMPLE_CSV_FILE_PATH = 
			   "D:\\KhaledProjects\\Selenium\\Selenium\\RainbowApplicationTest\\src\\com\\Rainbow\\TestData\\datacsv.csv";
		private static final String SAMPLE_CSV_FILE = 
				"D:\\KhaledProjects\\Selenium\\Selenium\\RainbowApplicationTest\\src\\com\\Rainbow\\TestData\\Createcsv.csv";
	@BeforeMethod
	public void login( ) throws InterruptedException {
		inittializationWithOption();
		wait  = new WebDriverWait(driver, 12);
		 
		wait.until(ExpectedConditions.visibilityOfElementLocated
				(By.xpath("//input[@id='username']"))).click();  //Click on the email TextBox
		
		wait.until(ExpectedConditions.visibilityOfElementLocated
				(By.xpath("//input[@id='username']"))).sendKeys(prop.getProperty("userName"));  //Send email to email TextBox
		
		wait.until(ExpectedConditions.elementToBeClickable(By.xpath
				("//span[@class='c-button__label']"))).click();     //Click on the Continue Button
		
		wait.until(ExpectedConditions.visibilityOfElementLocated
				(By.id("authPwd"))).click();       //Click on Password TextBox
		
        wait.until(ExpectedConditions.visibilityOfElementLocated
        		(By.id("authPwd"))).sendKeys(prop.getProperty("password")); //Send Password to Password TextBox
        
	    wait.until(ExpectedConditions.elementToBeClickable
	    		(By.xpath("//span[@class='c-button__label']"))).click(); //Click on the Continue Button
	    
	    wait.until(ExpectedConditions.elementToBeClickable
	    		(By.xpath("//*[@id=\"popup\"]/userwindow/userwindow-header/div/button"))).click(); //Click on the whatsNew Button After Login
	    
	    wait.until(ExpectedConditions.elementToBeClickable
	    		(By.xpath("//*[@id=\"popup\"]/userwindow/userwindow-footer/div/square-button[1]"))).click();//Click on the Welcome Screen Button After Login

	}
//(
	@Test(dataProvider="readCSVData")	
	public  void SearchAndWriteDataToCsvTest(String[] nameFromSearch) throws IOException {
		String username1 = nameFromSearch[0];
		boolean staleElement = true; 
		WebElement searchName = wait.
				until(ExpectedConditions.visibilityOfElementLocated(By.xpath("(//input[@placeholder='People, bubbles...'])[1]")));
     searchName.sendKeys(username1);
		 //It is used so that the page does not fail when recent conversations appear
		while(staleElement){
		  try{
			     
				   
				   //Get the results of the first three searches
			       WebElement Firstindex = wait.
					         until(ExpectedConditions.visibilityOfElementLocated(
						         	By.xpath("//*[@id=\"leftArea\"]/conversations/div/div/div/conversation-cell[1]/div/div/div[1]/p")));
			
			       WebElement Secondindex = wait.
					         until(ExpectedConditions.visibilityOfElementLocated(
					         		By.xpath("//*[@id=\"leftArea\"]/conversations/div/div/div/conversation-cell[2]/div/div/div[1]/p/span")));
			       WebElement Thardindex = wait.
					         until(ExpectedConditions.visibilityOfElementLocated(
					        		 By.xpath("//*[@id=\"leftArea\"]/conversations/div/div/div/conversation-cell[3]/div/div/div[1]/p")));
			       //-Get the first 3 results and write them to CSV file.
			       String firstName = Firstindex.getText();
			       String secondName = Secondindex.getText();
			       String tharidName = Thardindex.getText();
			       
			     //-print the first 3 results in Console For each execution 
			 
			        System.out.println("Data write in CsvFile get From Search  :"+firstName);
			        System.out.println("Data write in CsvFile get From Search  :"+secondName);
			        System.out.println("Data write in CsvFile get From Search  :"+tharidName);
			  	  try (
				            BufferedWriter writer = Files.newBufferedWriter(Paths.get(SAMPLE_CSV_FILE));

				            CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT
				                    .withHeader("Name From Search"));
				        ) {
					  
				            csvPrinter.printRecord(firstName);
				            csvPrinter.printRecord(secondName);
				            csvPrinter.printRecord(Arrays.asList(tharidName));
				            csvPrinter.flush();    
				  }

		     staleElement = false;


		  } catch(StaleElementReferenceException e){

		    staleElement = true;

		  }

		}


	}
	
	@DataProvider
	public Object[][] readCSVData() throws Exception {
		
		   String[][] testData;
	        //Get the workbook
	        Reader fileInputStream = new FileReader
	        		("D:\\KhaledProjects\\Selenium\\Selenium\\RainbowApplicationTest\\src\\com\\Rainbow\\TestData\\datacsv.csv");
	        Iterable<CSVRecord> records = CSVFormat.EXCEL.parse(fileInputStream);
	        int numberOfRecords = 0,numberOfColumns = 0 , currentRecord = 0;
	
	        for (CSVRecord record : records
	        ) {
	             ++numberOfRecords;
            numberOfColumns = record.size(); 
              }
            testData = new String[numberOfRecords - 1][numberOfColumns]; 
	        fileInputStream = new FileReader
	        		("D:\\KhaledProjects\\Selenium\\Selenium\\RainbowApplicationTest\\src\\com\\Rainbow\\TestData\\datacsv.csv");
	        records = CSVFormat.EXCEL.parse(fileInputStream);
	        for (CSVRecord record : records
	        ) {
	             //Reading test data
	            if (record.getRecordNumber() == 1) 
	                {
	                continue;
	                }

	            for (int i = 0; i < record.size(); i++) {
	                testData[currentRecord][i] = record.get(i);
	                }
	            currentRecord++;
	        }


	        return testData;          
	        }
	@AfterMethod
	public void afterMethod() {
		driver.quit();
		
	}
	
       }
