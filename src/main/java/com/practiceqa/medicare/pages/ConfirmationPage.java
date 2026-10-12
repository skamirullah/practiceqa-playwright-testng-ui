package com.practiceqa.medicare.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import io.qameta.allure.Step;

public class ConfirmationPage extends BasePage {

  public ConfirmationPage(Page page) {
    super(page);
  }

  public Locator confirmation() {
    return testId("appointment-confirmed");
  }

  public Locator appointmentId() {
    return testId("appointment-id");
  }

  public Locator type() {
    return testId("appointment-type");
  }

  @Step("Open My appointments")
  public AppointmentsPage viewAppointments() {
    testId("view-appointments").click();
    return new AppointmentsPage(page);
  }
}
