package com.practiceqa.medicare.base;

import com.microsoft.playwright.Page;
import com.practiceqa.medicare.browser.BrowserManager;
import com.practiceqa.medicare.data.Clinic;
import com.practiceqa.medicare.pages.DoctorPage;
import com.practiceqa.medicare.pages.DoctorsPage;
import io.qameta.allure.Allure;
import java.io.ByteArrayInputStream;
import java.nio.file.Path;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeMethod;

/**
 * Gives every test a fresh browser context. When a test fails, a screenshot goes
 * into the Allure report and a Playwright trace is saved under target/traces.
 */
public abstract class BaseTest {

  @BeforeMethod(alwaysRun = true)
  public void openPage() {
    BrowserManager.newPage();
  }

  @AfterMethod(alwaysRun = true)
  public void closePage(ITestResult result) {
    Path trace = null;
    if (result.getStatus() == ITestResult.FAILURE && BrowserManager.page() != null) {
      byte[] screenshot = BrowserManager.page().screenshot(new Page.ScreenshotOptions().setFullPage(true));
      Allure.addAttachment("Screenshot at failure", "image/png", new ByteArrayInputStream(screenshot), "png");
      trace = Path.of("target", "traces", result.getMethod().getMethodName() + ".zip");
    }
    BrowserManager.closePage(trace);
  }

  /** TestNG reuses its worker threads, so each thread's browser is closed once, at the end. */
  @AfterSuite(alwaysRun = true)
  public void closeBrowser() {
    BrowserManager.quit();
  }

  protected Page page() {
    return BrowserManager.page();
  }

  protected DoctorsPage doctorsPage() {
    return new DoctorsPage(page()).open();
  }

  /** Opens the booking page of the cardiologist most tests use. */
  protected DoctorPage cardiologist() {
    return doctorsPage().filterBySpecialty("cardiology").book(Clinic.CARDIOLOGIST_ID);
  }
}
