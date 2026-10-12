package com.practiceqa.medicare.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import io.qameta.allure.Step;

public class DoctorsPage extends BasePage {

  public DoctorsPage(Page page) {
    super(page);
  }

  @Step("Open the doctor directory")
  public DoctorsPage open() {
    page.navigate("/apps/clinic");
    return this;
  }

  @Step("Filter by specialty: {specialty}")
  public DoctorsPage filterBySpecialty(String specialty) {
    testId("specialty-" + specialty).click();
    return this;
  }

  public Locator count() {
    return testId("doctor-count");
  }

  public Locator cards() {
    return testId("doctor-card");
  }

  public Locator names() {
    return testId("doctor-name");
  }

  @Step("Book doctor {doctorId}")
  public DoctorPage book(String doctorId) {
    page.locator("[data-testid='book-doctor'][data-doctor-id='" + doctorId + "']").click();
    return new DoctorPage(page);
  }
}
