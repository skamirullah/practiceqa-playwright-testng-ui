package com.practiceqa.medicare.tests;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

import com.practiceqa.medicare.base.BaseTest;
import com.practiceqa.medicare.data.Clinic;
import com.practiceqa.medicare.data.Patient;
import com.practiceqa.medicare.pages.AppointmentsPage;
import com.practiceqa.medicare.pages.ConfirmationPage;
import com.practiceqa.medicare.pages.DoctorPage;
import com.practiceqa.medicare.pages.PatientDetailsPage;
import io.qameta.allure.Feature;
import java.util.regex.Pattern;
import org.testng.annotations.Test;

@Feature("Booking an appointment")
public class BookingTests extends BaseTest {

  private ConfirmationPage bookVideoConsult() {
    return cardiologist().chooseType("video").pickFirstAvailableSlot().continueToDetails()
        .fillNewPatient(Patient.valid())
        .describeSymptoms(Clinic.SYMPTOMS)
        .giveConsent()
        .confirm();
  }

  @Test(groups = "smoke", description = "A new patient books a video consultation")
  public void newPatientBooksAVideoConsult() {
    DoctorPage doctor = cardiologist().chooseType("video").pickFirstAvailableSlot();
    assertThat(doctor.slotSummary()).containsText("Video");

    ConfirmationPage confirmation = doctor.continueToDetails()
        .fillNewPatient(Patient.valid())
        .describeSymptoms(Clinic.SYMPTOMS)
        .giveConsent()
        .confirm();

    assertThat(confirmation.confirmation()).isVisible();
    assertThat(confirmation.appointmentId()).hasText(Pattern.compile("^APT-\\w+$"));
    assertThat(confirmation.type()).hasText("Video consult");
  }

  @Test(description = "An existing patient is found by ID and books an in-clinic visit")
  public void existingPatientBooksAnInClinicVisit() {
    PatientDetailsPage details = cardiologist().chooseType("clinic").pickFirstAvailableSlot().continueToDetails()
        .lookUpPatient(Clinic.KNOWN_PATIENT_ID);
    assertThat(details.patientFound()).hasText("Welcome back, " + Clinic.KNOWN_PATIENT_NAME + ".");

    ConfirmationPage confirmation = details.describeSymptoms(Clinic.SYMPTOMS).giveConsent().confirm();

    assertThat(confirmation.confirmation()).isVisible();
    assertThat(confirmation.type()).hasText("In-clinic");
  }

  @Test(description = "A video consult costs 80% of the in-clinic fee")
  public void videoConsultIsCheaper() {
    String videoFee = String.valueOf(Math.round(Clinic.CARDIOLOGIST_FEE * Clinic.VIDEO_DISCOUNT));

    PatientDetailsPage details = cardiologist().chooseType("video").pickFirstAvailableSlot().continueToDetails();

    assertThat(details.fee()).containsText(videoFee);
  }

  @Test(description = "A booked appointment shows up as upcoming and can be cancelled")
  public void appointmentCanBeCancelled() {
    AppointmentsPage appointments = bookVideoConsult().viewAppointments();
    assertThat(appointments.cards()).hasCount(1);
    assertThat(appointments.status()).hasText("Upcoming");

    appointments.cancelFirst();

    assertThat(appointments.status()).hasText("Cancelled");
  }

  @Test(description = "Choosing Keep in the cancel dialog leaves the appointment as it was")
  public void keepingAnAppointmentChangesNothing() {
    AppointmentsPage appointments = bookVideoConsult().viewAppointments().startCancelThenKeep();

    assertThat(appointments.status()).hasText("Upcoming");
  }
}
