package QATraining.TrainingBasic;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.HttpCookie;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.net.ssl.HttpsURLConnection;

import org.apache.http.client.CookieStore;
import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
public class taToc {
	WebDriver driver=null;
	
  @BeforeSuite
  public void initializeCourse() {
	System.setProperty("webdriver.chrome.driver", "C:\\chrome\\chromedriver.exe");
	driver=new ChromeDriver();
	//visit the website
	driver.get("http://10.0.1.86/tatoc/basic/grid/gate"); 
  }
	@Test(priority=1)
  public void greenGrid() {
	driver.findElement(By.cssSelector("div.greenbox")).click();
	Assert.assertTrue((driver.getTitle()).contains("Dungeon"), "Greenbox found, proceeded to next test");
  }
	@Test(priority=2)
	 public void frameDungeon() {
		//List <WebElement> frame=driver.findElements(By.tagName("iframe"));
		driver.switchTo().frame("main");
		/*WebDriverWait webWait=new WebDriverWait(driver, 10);
		By box1=By.xpath("//*[contains(text(),'Box 1')]");
		webWait.until(ExpectedConditions.presenceOfElementLocated(box1));*/
		WebElement box_1=driver.findElement(By.xpath("//*[contains(text(),'Box 1')]"));
		WebElement repaintBttn=driver.findElement(By.xpath("//*[contains(text(),'Repaint')]"));
		WebElement proceedBttn=driver.findElement(By.xpath("//*[contains(text(),'Repaint')]/following-sibling::a"));
		String box_1_color=box_1.getAttribute("class");
		driver.switchTo().frame("child");
		WebElement box_2=driver.findElement(By.xpath("//*[contains(text(),'Box 2')]"));
		String box_2_color=box_2.getAttribute("class");
		
		while(!box_2_color.equalsIgnoreCase(box_1_color))
			{   
				driver.switchTo().parentFrame();
				repaintBttn.click();
				//repaint box2 until both boxes color not same
				driver.switchTo().frame("child");
				
				box_2=driver.findElement(By.xpath("//*[contains(text(),'Box 2')]"));
				box_2_color=box_2.getAttribute("class");
				
			}
		driver.switchTo().parentFrame();
		proceedBttn.click();
		Assert.assertTrue((driver.getTitle()).contains("Drag"), "Box 1 and Box2 colour same, proceeded to next test");
	}
	@Test(priority=3)
	  public void dragBox() {
		WebElement dragBox=driver.findElement(By.cssSelector("div#dragbox"));
		WebElement dropBox=driver.findElement(By.cssSelector("div#dropbox"));
		Actions builder= new Actions(driver);
		Action drag=builder.clickAndHold(dragBox).moveToElement(dropBox).release().build();
		//draganddrop() function of action class not working in chrome so work around is above piece of line
		drag.perform();
		WebElement proceedBttn=driver.findElement(By.xpath("//div[@id='dropbox']/following-sibling::a"));
		proceedBttn.click();
		//codesnippet to wait for page load
		/*WebDriverWait wait = new WebDriverWait(driver, 30);

	    wait.until(new ExpectedCondition<Boolean>() {
	        public BooleanString apply(WebDriver wdriver) {
	            return ((JavascriptExecutor) driver).executeScript(
	                "return document.readyState"
	            ).equals("complete");
	        }
	    });*/
		Assert.assertTrue((driver.findElement(By.xpath("//h1[contains(text(),'Popup')]"))).isDisplayed(), "Box droped in correct location, proceeded to next test");
	  }
	@Test(priority=4)
	  public void LaunchPopup() {
		String parentWindow=driver.getWindowHandle();
		driver.findElement(By.xpath("//h1/following-sibling::a[1]")).click();
		Set <String> handles = driver.getWindowHandles();
		 String  WindowHandlerID=null;
		 Iterator<String> ht=handles.iterator();
		 while(ht.hasNext())
		 {
		    WindowHandlerID = ht.next();
		    }
		driver.switchTo().window(WindowHandlerID);
		/*for (String winHandle : driver.getWindowHandles()) {
		    driver.switchTo().window(winHandle); // switch focus of WebDriver to the next found window handle (that's your newly opened window)
		}*/
		 System.out.println(driver.getTitle());
		 WebElement inputArea=driver.findElement(By.cssSelector("input#name"));
		 inputArea.sendKeys("Shubhi");
		 driver.findElement(By.cssSelector("input#submit")).click();
		 System.out.println("submitted");
		 driver.switchTo().window(parentWindow);
		 System.out.println("parent window");
		 Assert.assertTrue((driver.findElement(By.xpath("//h1[contains(text(),'Popup')]"))).isDisplayed(), "came back to main window after entering text");
		 driver.findElement(By.xpath("//h1/following-sibling::a[2]")).click();
		 Assert.assertTrue((driver.getTitle()).contains("Cookie"), "Popup test proceeded, proceeded to next test");
	}
	@Test(priority=5)
	  public void generateToken() {
		URL myurl;
	    HttpURLConnection con=null;
		driver.findElement(By.xpath("//h1/following-sibling::a[1]")).click();
		WebElement tokenElement=driver.findElement(By.cssSelector("span#token"));
		String token=tokenElement.getText();
		String[] cookieVal=token.split(":");
		Cookie cookieval=new Cookie(cookieVal[0],cookieVal[1].trim());
		driver.manage().addCookie(cookieval);
		/*JavascriptExecutor js = (JavascriptExecutor) driver;  
		String executionScript="document.cookie='"+cookieVal[0]+"="+cookieVal[1].trim()+"'";
		js.executeScript(executionScript);*/
		driver.findElement(By.xpath("//h1/following-sibling::a[2]")).click();
		//con.disconnect();
		Assert.assertTrue((driver.getTitle()).contains("End"), "Course completed");
	}
	@AfterSuite
	public void closeDriver() {
		driver.quit();
	}
}
