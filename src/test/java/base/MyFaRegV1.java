
package base;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.Wait;
import org.testng.annotations.Test;

public class MyFaRegV1 {

	public static Logger log = Logger.getLogger("devpinoyLogger");
		
		@Test
		public void setRegister() throws IOException, InterruptedException {
		
		Properties OR = new Properties();
		Properties config = new Properties();
		FileInputStream fis1 = new FileInputStream(System.getProperty("user.dir")+"//src//test//resources//properties//OR.properties");
		FileInputStream fis2 = new FileInputStream(System.getProperty("user.dir")+"//src//test//resources//properties//config.properties");
		OR.load(fis1);
		config.load(fis2);
		
		ExcelReader excel= new ExcelReader(config.getProperty("myfaxls"));
		String sheetName = config.getProperty("sheetname");
		for(int i=2;i<=excel.getRowCount(sheetName);i++) {
			
			int x = i-1;
			System.out.println("This is loop through Row :" + x);
			log.debug("This is loop through Row :" + x);
			String status = excel.getCellData(sheetName, "Status",i);
			String f_name = excel.getCellData(sheetName, "FirstName", i);		
			String l_name = excel.getCellData(sheetName, "LastName", i);
			String mon = excel.getCellData(sheetName, "Month", i);
			String date =  excel.getCellData(sheetName, "Date", i);
			String yr = excel.getCellData(sheetName, "Year", i);
			String sn = excel.getCellData(sheetName, "SSN", i);
			String clid_part1 = excel.getCellData(sheetName, "ClientPart1", i);
			String clid_part2 = excel.getCellData(sheetName, "ClientPart2", i);
			String clid_part3 = excel.getCellData(sheetName, "ClientPart3", i);
			String uid = excel.getCellData(sheetName, "Userid", i);
			
			
		if(status.equalsIgnoreCase("pass")){
		log.debug(f_name + " "+ l_name +" is already registered and marked as pass");
		continue;
		}
		
		else if(f_name.isEmpty()) {
		log.debug("no more rows available");
		break;
		}
		
		else{
			
		System.setProperty("webdriver.ie.driver", config.getProperty("ieexe"));
		WebDriver driver = new InternetExplorerDriver();
		driver.navigate().to(config.getProperty("myfalink"));
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(5L, TimeUnit.SECONDS);
		
		// ***************Page 1 automation********************** //
		
		WebElement first_name = driver.findElement(By.xpath(OR.getProperty("firstname")));
		first_name.sendKeys(f_name);
		
		WebElement last_name = driver.findElement(By.xpath(OR.getProperty("lastname")));
		last_name.sendKeys(l_name);
		
		WebElement month = driver.findElement(By.xpath(OR.getProperty("month")));
		Select select_month = new Select(month);
		select_month.selectByVisibleText(mon);
		
		WebElement dd = driver.findElement(By.xpath(OR.getProperty("day")));
		Select select_dd = new Select(dd);
		select_dd.selectByVisibleText(date);
		
		WebElement year = driver.findElement(By.xpath(OR.getProperty("year")));
		Select select_year = new Select(year);
		select_year.selectByVisibleText(yr);
		
		WebElement ssn = driver.findElement(By.xpath(OR.getProperty("ssn")));
		ssn.sendKeys(sn);
		
		WebElement client_part1 = driver.findElement(By.xpath(OR.getProperty("clidpart1")));
		client_part1.sendKeys(clid_part1);
		
		WebElement client_part2 = driver.findElement(By.xpath(OR.getProperty("clidpart2")));
		client_part2.sendKeys(clid_part2);
		
		WebElement client_part3 = driver.findElement(By.xpath(OR.getProperty("clidpart3")));
		client_part3.sendKeys(clid_part3);
		
		WebElement client_part4 = driver.findElement(By.xpath(OR.getProperty("clidpart4")));
		client_part4.sendKeys(OR.getProperty("clidpart4val"));
		
		WebElement primary_email = driver.findElement(By.xpath(OR.getProperty("priemail")));
		primary_email.sendKeys(OR.getProperty("emailid"));
		
		WebElement secondary_email = driver.findElement(By.xpath(OR.getProperty("secemail")));
		secondary_email.sendKeys(OR.getProperty("emailid"));
		
		WebElement page1_continue = driver.findElement(By.xpath(OR.getProperty("page1cont")));
		page1_continue.click();
		
		try{
			if (driver.findElement(By.xpath(OR.getProperty("clientprevregis"))).isDisplayed()){
			System.out.println("client is already registered");
			log.debug("client is already registered");
			excel.setCellData(sheetName, "Status", i, "pass");
			excel.setCellData(sheetName, "Note", i, "client is already registered");
			driver.quit();
			continue;
		}
		}catch(Throwable t){
		
		}
		
		// ***************Page 2 automation**********************//

		Wait<WebDriver> wait = new FluentWait<WebDriver>(driver)
			       .withTimeout(20, TimeUnit.SECONDS)
			       .pollingEvery(30, TimeUnit.SECONDS)
			       .ignoring(NoSuchElementException.class);
		wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(OR.getProperty("page2cont"))));
		WebElement page2_continue = driver.findElement(By.xpath(OR.getProperty("page2cont")));
		page2_continue.click();
		
		// ***************Page 3 automation**********************//
		
		WebElement odca_check = driver.findElement(By.xpath(OR.getProperty("odcacheck")));
		odca_check.click();
		
		WebElement cfua_check = driver.findElement(By.xpath(OR.getProperty("cfuacheck")));
		cfua_check.click();
		
		WebElement page3_continue = driver.findElement(By.xpath(OR.getProperty("page3cont")));
		page3_continue.click();
		
		// ***************Page 4 automation**********************//
		
		WebElement userid = driver.findElement(By.xpath(OR.getProperty("userid")));
		userid.sendKeys(uid);
		
		WebElement enter_password = driver.findElement(By.xpath(OR.getProperty("pwd")));
		enter_password.sendKeys(OR.getProperty("pwdval"));
		
		WebElement verify_password = driver.findElement(By.xpath(OR.getProperty("confpwd")));
		verify_password.sendKeys(OR.getProperty("pwdval"));
		
		WebElement page4_continue = driver.findElement(By.xpath(OR.getProperty("page4cont")));
		page4_continue.click();
		//} //catch
		
		try{
		if (driver.findElement(By.xpath(OR.getProperty("useridinuse"))).isDisplayed()) {
			//System.out.println("User id selected is already in use");
			log.debug("User id selected is already in use");
			excel.setCellData(sheetName, "Status", i, "fail");
			excel.setCellData(sheetName, "Note", i, "User id selected is already in use");
			driver.quit();
			continue;
		}
		}catch(Throwable t){
		} 
		
		// ***************Page 5 automation********************** //
		// Dropdown 1
		WebElement quest1 = driver.findElement(By.xpath(OR.getProperty("quest1")));
		Select select_quest1 = new Select(quest1);
		select_quest1.selectByIndex(1);
		
		List<WebElement> option1 = select_quest1.getOptions();
		String option1_text = option1.get(1).getText();
		excel.setCellData(sheetName, "Question1", i, option1_text);
		
		/* String section */
		String option1_substr= option1_text.substring(option1_text.lastIndexOf(" ") + 1);
		String option1_str = option1_substr.substring(0,option1_substr.length()-1);
		//System.out.println(option1_text);
		//System.out.println(option1_str);
		/* ************** */
		
		
		WebElement quest1_ans = driver.findElement(By.xpath(OR.getProperty("ans1")));
		quest1_ans.sendKeys(option1_str);
		excel.setCellData(sheetName, "Answer1", i, option1_str);
		
		// Dropdown 2
		WebElement quest2 = driver.findElement(By.xpath(OR.getProperty("quest2")));
		Select select_quest2 = new Select(quest2);
		select_quest2.selectByIndex(2);
		
		List<WebElement> option2 = select_quest2.getOptions();
		String option2_text = option2.get(2).getText();
		excel.setCellData(sheetName, "Question2", i, option2_text);
		
		/* String section */
		String option2_substr= option2_text.substring(option2_text.lastIndexOf(" ") + 1);
		String option2_str = option2_substr.substring(0,option2_substr.length()-1);
		//System.out.println(option2_text);
		//System.out.println(option2_str);
		/* ************** */
		
		WebElement quest2_ans = driver.findElement(By.xpath(OR.getProperty("ans2")));
		quest2_ans.sendKeys(option2_str);
		excel.setCellData(sheetName, "Answer2", i, option2_str);
		
		// Dropdown 3
		WebElement quest3 = driver.findElement(By.xpath(OR.getProperty("quest3")));
		Select select_quest3 = new Select(quest3);
		select_quest3.selectByIndex(3);
		
		List<WebElement> option3 = select_quest3.getOptions();
		String option3_text = option3.get(3).getText();
		excel.setCellData(sheetName, "Question3", i, option3_text);
		
		/* String section */
		String option3_substr= option3_text.substring(option3_text.lastIndexOf(" ") + 1);
		String option3_str = option3_substr.substring(0,option3_substr.length()-1);
		//System.out.println(option3_text);
		//System.out.println(option3_str);
		/* ************** */
		
		WebElement quest3_ans = driver.findElement(By.xpath(OR.getProperty("ans3")));
		quest3_ans.sendKeys(option3_str);
		excel.setCellData(sheetName, "Answer3", i, option3_str);
		
		WebElement page5_continue = driver.findElement(By.xpath(OR.getProperty("page5submit")));
		page5_continue.submit();
		
		/*  Final page */
		
		try {
		WebElement regcomp = driver.findElement(By.xpath(OR.getProperty("regsuccessmsg")));
		String regtext_actual = regcomp.getText();
		String regtext_expected = "Registration Complete";
		
			if(regtext_actual.equals(regtext_expected)){
				excel.setCellData(sheetName, "Status", i, "pass");
				driver.manage().deleteAllCookies();
				driver.quit();
			}
		} catch(Throwable a){
		
		excel.setCellData(sheetName, "Status", i, "fail");	
		continue;
		
		}
		}
		} 
		
		} 	
		
		
	} 


