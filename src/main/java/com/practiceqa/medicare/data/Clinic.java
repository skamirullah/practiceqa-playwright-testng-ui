package com.practiceqa.medicare.data;

/** Catalogue facts the tests rely on, kept in one place. */
public final class Clinic {
  public static final String CARDIOLOGIST_ID = "d1";
  public static final String CARDIOLOGIST_NAME = "Dr. Ananya Rao";
  public static final int CARDIOLOGIST_FEE = 900;
  /** A video consult costs 80% of the in-clinic fee. */
  public static final double VIDEO_DISCOUNT = 0.8;
  public static final String KNOWN_PATIENT_ID = "P-1001";
  public static final String KNOWN_PATIENT_NAME = "Sara Khan";
  public static final String SYMPTOMS = "Occasional chest discomfort after exercise";

  private Clinic() {}
}
