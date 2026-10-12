package com.practiceqa.medicare.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

/**
 * Shared behaviour for every page object. Pages return Locators, so tests assert
 * with Playwright's web-first assertions, which retry until the page catches up.
 */
public abstract class BasePage {
  protected final Page page;

  protected BasePage(Page page) {
    this.page = page;
  }

  /** The app marks every interactive element with data-testid. */
  protected Locator testId(String id) {
    return page.getByTestId(id);
  }

  /** Inline error shown under a form field. */
  public Locator fieldError(String fieldId) {
    return testId("error-" + fieldId);
  }
}
