package com.practiceqa.medicare.tests;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

import com.practiceqa.medicare.base.BaseTest;
import com.practiceqa.medicare.data.Clinic;
import com.practiceqa.medicare.data.Patient;
import com.practiceqa.medicare.pages.PatientDetailsPage;
import io.qameta.allure.Feature;
import org.testng.annotations.Test;

@Feature("Patient form validation")
public class PatientFormValidationTests extends BaseTest {

  private PatientDetailsPage patientForm() {
    return cardiologist().chooseType("video").pickFirstAvailableSlot().continueToDetails();
  }

  @Test(description = "The form asks whether the patient is new or existing")
  public void patientTypeIsRequired() {
    PatientDetailsPage form = patientForm().submit();

    assertThat(form.errorSummary()).hasText("Please fix the highlighted fields.");
    assertThat(form.fieldError("kind")).hasText("Tell us if you're a new or existing patient");
    assertThat(form.fieldError("symptoms")).hasText("Describe symptoms in at least 10 characters");
    assertThat(form.fieldError("consent")).hasText("Consent is required");
  }

  @Test(description = "Every empty field of a new patient is flagged")
  public void newPatientFieldsAreRequired() {
    PatientDetailsPage form = patientForm().fillNewPatient(Patient.blank()).submit();

    assertThat(form.fieldError("patient-name")).hasText("Enter the patient's name");
    assertThat(form.fieldError("patient-age")).hasText("Enter a valid age");
    assertThat(form.fieldError("patient-gender")).hasText("Choose gender");
    assertThat(form.fieldError("patient-phone")).hasText("Phone must be 10 digits");
    assertThat(form.fieldError("patient-email")).hasText("Enter a valid email");
  }

  @Test(description = "Symptoms need at least 10 characters")
  public void symptomsNeedDetail() {
    PatientDetailsPage form = patientForm().fillNewPatient(Patient.valid()).describeSymptoms("Cough").giveConsent().submit();

    assertThat(form.fieldError("symptoms")).hasText("Describe symptoms in at least 10 characters");
    assertThat(form.fieldError("patient-name")).hasCount(0);
  }

  @Test(description = "An unknown patient ID is reported and blocks the booking")
  public void unknownPatientIdIsRejected() {
    PatientDetailsPage form = patientForm().lookUpPatient("P-9999");
    assertThat(form.patientNotFound()).hasText("No patient with that ID.");

    form.describeSymptoms(Clinic.SYMPTOMS).giveConsent().submit();

    assertThat(form.fieldError("patient-id")).hasText("Look up a valid patient ID");
  }
}
