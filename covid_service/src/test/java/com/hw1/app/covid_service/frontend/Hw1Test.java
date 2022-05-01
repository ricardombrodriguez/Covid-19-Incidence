package com.hw1.app.covid_service.frontend;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.FindBy;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.github.bonigarcia.seljup.SeleniumJupiter;

@ExtendWith(SeleniumJupiter.class)
class Hw1Test {

  private WebDriver driver;

  @FindBy(name="country")
  private WebElement country;

  @FindBy(id="month")
  private WebElement month;

  @FindBy(name="search")
  private WebElement search;

  @FindBy(css="label:nth-child(2)")
  private WebElement label2;

  @FindBy(css="label:nth-child(4)")
  private WebElement label4;

  @FindBy(css="label:nth-child(6)")
  private WebElement label6;

  @FindBy(css="label:nth-child(8)")
  private WebElement label8;

  @FindBy(id="selected_country")
  private WebElement selected_country;

  @FindBy(css="text-muted")
  private WebElement differential;

  @BeforeEach
  public void setUp() {
    driver = new ChromeDriver();
  }

  @AfterEach
  public void tearDown() {
    driver.quit();
  }

  @When("I want to access {string}")
  public void accessURL(String URL) {
    driver.get(URL);
    driver.manage().window().setSize(new Dimension(1846, 1053));
  }

  @Then("page title should be {string}")
  public void checkPageTitle(String title) {
    assertThat(driver.getTitle()).isEqualTo(title);
  }

  @And("I want to search statistics from {string}")
  public void searchCountry(String strCountry) {
    country.click();
    country.sendKeys(strCountry);
  }

  @And("And I search for the {string}")
  public void lastDays(String fetchDays) {

    Map<String, WebElement> map  = new HashMap<>();
    map.put("today", label2); 
    map.put("last week", label4);
    map.put("last month", label6);
    map.put("last year", label8);

    WebElement el = map.get(fetchDays);
    el.click();
    search.click();
    driver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);
  }

  @Then("country title should be {string}")
  public void checkCountryTitle(String title) {
    assertThat(selected_country.getText()).isEqualToIgnoringCase(title);
  }

  @And("statitic differential text should be last {string}")
  public void assertStatisticDifferential(String days) {
    assertThat(differential.getText()).isEqualToIgnoringCase(days);
  }

}
