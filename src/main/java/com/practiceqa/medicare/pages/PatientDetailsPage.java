package com.practiceqa.medicare.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.practiceqa.medicare.data.Patient;
import io.qameta.allure.Step;

public class PatientDetailsPage extends BasePage {

  public PatientDetailsPage(Page page) {
    super(page);
  }

  @Step("Register a new patient")
  public PatientDetailsPage fillNewPatient(Patient patient) {
    testId("patient-new").click();
    testId("patient-name").fill(patient.name());
    testId("patient-age").fill(patient.age());
    if (!patient.gender().isEmpty()) testId("patient-gender").selectOption(patient.gender());
    testId("patient-phone").fill(patient.phone());
    testId("patient-email").fill(patient.email());
    return this;
  }

  @Step("Look up existing patient {patientId}")
  public PatientDetailsPage lookUpPatient(String patientId) {
    testId("patient-existing").click();
    testId("patient-id").fill(patientId);
    testId("lookup-patient").click();
    return this;
  }

  public PatientDetailsPage describeSymptoms(String symptoms) {
    testId("symptoms").fill(symptoms);
    return this;
  }

  public PatientDetailsPage giveConsent() {
    testId("consent").check();
    return this;
  }

  /** Submits without expecting success; used by validation tests. */
  public PatientDetailsPage submit() {
    testId("confirm-appointment").click();
    return this;
  }

  @Step("Confirm the appointment")
  public ConfirmationPage confirm() {
    submit();
    return new ConfirmationPage(page);
  }

  public Locator fee() {
    return testId("appointment-fee");
  }

  public Locator patientFound() {
    return testId("patient-found");
  }

  public Locator patientNotFound() {
    return testId("patient-not-found");
  }

  public Locator errorSummary() {
    return testId("form-errors");
  }
}
