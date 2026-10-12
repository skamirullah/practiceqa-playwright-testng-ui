package com.practiceqa.medicare.tests;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

import com.practiceqa.medicare.base.BaseTest;
import com.practiceqa.medicare.data.Clinic;
import com.practiceqa.medicare.pages.DoctorPage;
import com.practiceqa.medicare.pages.DoctorsPage;
import io.qameta.allure.Feature;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

@Feature("Finding a doctor")
public class DoctorSearchTests extends BaseTest {

  @Test(groups = "smoke", description = "The directory lists every doctor")
  public void listsAllDoctors() {
    DoctorsPage doctors = doctorsPage();

    assertThat(doctors.count()).hasText("10 doctors");
    assertThat(doctors.cards()).hasCount(10);
  }

  @DataProvider
  public Object[][] specialties() {
    return new Object[][] {{"cardiology"}, {"dermatology"}, {"pediatrics"}, {"orthopedics"}, {"general"}};
  }

  @Test(dataProvider = "specialties", description = "A specialty filter shows only that specialty's doctors")
  public void filtersBySpecialty(String specialty) {
    DoctorsPage doctors = doctorsPage().filterBySpecialty(specialty);

    assertThat(doctors.count()).hasText("2 doctors");
    assertThat(doctors.cards()).hasCount(2);
  }

  @Test(description = "Clicking the active specialty again clears the filter")
  public void specialtyFilterToggles() {
    DoctorsPage doctors = doctorsPage().filterBySpecialty("cardiology");
    assertThat(doctors.cards()).hasCount(2);

    doctors.filterBySpecialty("cardiology");

    assertThat(doctors.cards()).hasCount(10);
  }

  @Test(description = "A slot must be picked before the booking can continue")
  public void slotIsRequiredToContinue() {
    DoctorPage doctor = cardiologist();

    assertThat(doctor.title()).containsText(Clinic.CARDIOLOGIST_NAME);
    assertThat(doctor.continueButton()).isDisabled();

    doctor.pickFirstAvailableSlot();

    assertThat(doctor.continueButton()).isEnabled();
  }
}
