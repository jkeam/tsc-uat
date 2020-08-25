package hellocucumber;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import static org.junit.Assert.*;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.Augmenter;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.UnreachableBrowserException;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;

import org.junit.Assert;

import org.openqa.selenium.chrome.*;

class IsItFriday {
    static String isItFriday(String today) {
        return "Friday".equals(today) ? "TGIF" : "Nope";
    }
}

public class Stepdefs {
    private String today;
    private String actualAnswer;

    private boolean isDriverReady = false;
    private WebDriver driver = null;
    private String website = "";
    private String hubHost = "localhost";
    private String hubPort = "4444";


    @Given("^I use Chrome browser$")
    public void I_use_Chrome_browser() throws Throwable {


      ChromeOptions options = new ChromeOptions();
      options.addArguments("--headless");

      // DesiredCapabilities capabilities = new DesiredCapabilities();
      DesiredCapabilities capabilities = DesiredCapabilities.chrome();
      capabilities.setBrowserName("chrome");
      capabilities.setCapability(ChromeOptions.CAPABILITY, options);
      // options.merge(capabilities);
      // ChromeDriver driver = new ChromeDriver(options);

      initDriver(capabilities);
    }

    @Given("today is {string}")
    public void today_is(String today) {
      System.out.println(today);
        this.today = today;
    }

    @When("I ask whether it's Friday yet")
    public void i_ask_whether_it_s_Friday_yet() {
        this.actualAnswer = IsItFriday.isItFriday(today);
    }

    @When("^I visit \"([^\"]*)\"$")
    public void I_visit(String website) throws Throwable {
      this.website = website;
      driver.navigate().to(new URL(website));
      // driver.manage().window().maximize();
    }

    @Then("I should be told {string}")
    public void i_should_be_told(String expectedAnswer) {
        assertEquals(expectedAnswer, actualAnswer);
    }

    @Then("^I should see \"([^\"]*)\"$")
    public void I_should_see(String text) throws Throwable {
      Assert.assertTrue("<[" + text + "]> is not present on current page!", isTextPresent(text));
    }

    private boolean isTextPresent(String text) throws InterruptedException {
      return isTextPresent(text, 50);
    }

    private boolean isTextPresent(String text, int retries) throws InterruptedException {
      boolean ready = false;
      while(!ready && retries-- > 0) {
        Thread.sleep(100);
        ready = driver.getPageSource().contains(text);
      }
      return ready;
    }

    private void initDriver(DesiredCapabilities capabilities) {
      if(this.isDriverReady) {
        return;
      }

      try {
        this.driver = new RemoteWebDriver(new URL("http://" + this.hubHost + ":" + this.hubPort + "/wd/hub"), capabilities);
        this.driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
      } catch(UnreachableBrowserException e) {
        Assert.fail("UnreachableBrowserException: " + e.getMessage());
      } catch(MalformedURLException e) {
        Assert.fail("MalformedURLException: http://" + this.hubHost + ":" + this.hubPort + "/wd/hub");
      } catch(WebDriverException e) {
        Assert.fail("WebDriverException: " + e.getMessage());
      }
      this.isDriverReady = true;
    }
}
