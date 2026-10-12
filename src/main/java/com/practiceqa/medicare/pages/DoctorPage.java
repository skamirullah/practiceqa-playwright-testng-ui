package com.practiceqa.medicare.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import io.qameta.allure.Step;

public class DoctorPage extends BasePage {

  public DoctorPage(Page page) {
    super(page);
  }

  public Locator title() {
    return testId("doctor-title");
  }

  /** @param type "clinic" for an in-clinic visit or "video" for a video consult */
  @Step("Choose consultation type: {type}")
  public DoctorPage chooseType(String type) {
    testId("type-" + type).click();
    return this;
  }

  /** Picks the first day the clinic is open (Sundays are disabled) and its first free slot. */
  @Step("Pick the first available slot")
  public DoctorPage pickFirstAvailableSlot() {
    page.locator("[data-testid='appointment-day']:not([disabled])").first().click();
    page.locator("[data-testid='slot'][data-available='true']").first().click();
    return this;
  }

  public Locator slotSummary() {
    return testId("slot-summary");
  }

  public Locator continueButton() {
    return testId("continue-to-details");
  }

  @Step("Continue to patient details")
  public PatientDetailsPage continueToDetails() {
    continueButton().click();
    return new PatientDetailsPage(page);
  }
}
