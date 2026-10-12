package com.practiceqa.medicare.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import io.qameta.allure.Step;

public class AppointmentsPage extends BasePage {

  public AppointmentsPage(Page page) {
    super(page);
  }

  public Locator cards() {
    return testId("appointment-card");
  }

  public Locator status() {
    return testId("appointment-status");
  }

  @Step("Cancel the first appointment")
  public AppointmentsPage cancelFirst() {
    page.locator("[data-testid^='cancel-appt-']").first().click();
    testId("confirm-cancel-appointment").click();
    return this;
  }

  @Step("Start cancelling, then keep the appointment")
  public AppointmentsPage startCancelThenKeep() {
    page.locator("[data-testid^='cancel-appt-']").first().click();
    testId("keep-appointment").click();
    return this;
  }
}
