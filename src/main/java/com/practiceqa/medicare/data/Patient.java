package com.practiceqa.medicare.data;

/** A patient's registration details. All data is fictional. */
public record Patient(String name, String age, String gender, String phone, String email) {

  public static Patient valid() {
    return new Patient("Amir Khan", "36", "male", "9876543210", "amir@example.com");
  }

  /** A form with nothing filled in, for validation tests. */
  public static Patient blank() {
    return new Patient("", "", "", "", "");
  }
}
