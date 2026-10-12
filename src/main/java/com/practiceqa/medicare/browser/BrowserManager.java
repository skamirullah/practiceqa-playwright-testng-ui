package com.practiceqa.medicare.browser;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.Tracing;
import com.practiceqa.medicare.config.Config;
import java.nio.file.Path;

/**
 * Playwright objects are not thread-safe, so each thread keeps its own. The
 * browser is started once per thread and reused; every test gets a new, isolated
 * context (its own cookies and storage), which is much cheaper than a new browser.
 */
public final class BrowserManager {
  private static final ThreadLocal<Playwright> PLAYWRIGHT = new ThreadLocal<>();
  private static final ThreadLocal<Browser> BROWSER = new ThreadLocal<>();
  private static final ThreadLocal<BrowserContext> CONTEXT = new ThreadLocal<>();
  private static final ThreadLocal<Page> PAGE = new ThreadLocal<>();

  private BrowserManager() {}

  /** Opens a fresh context and page for one test and starts recording a trace. */
  public static Page newPage() {
    BrowserContext context = browser().newContext(new Browser.NewContextOptions()
        .setBaseURL(Config.baseUrl())
        .setViewportSize(1366, 900));
    context.setDefaultTimeout(Config.timeout());
    context.tracing().start(new Tracing.StartOptions().setScreenshots(true).setSnapshots(true));
    CONTEXT.set(context);
    PAGE.set(context.newPage());
    return PAGE.get();
  }

  public static Page page() {
    return PAGE.get();
  }

  /** Closes the test's context. The trace is kept only when a path is given, i.e. when the test failed. */
  public static void closePage(Path traceFile) {
    BrowserContext context = CONTEXT.get();
    if (context == null) return;
    if (traceFile == null) context.tracing().stop();
    else context.tracing().stop(new Tracing.StopOptions().setPath(traceFile));
    context.close();
    CONTEXT.remove();
    PAGE.remove();
  }

  /** Stops the browser of the current thread. */
  public static void quit() {
    if (BROWSER.get() != null) BROWSER.get().close();
    if (PLAYWRIGHT.get() != null) PLAYWRIGHT.get().close();
    BROWSER.remove();
    PLAYWRIGHT.remove();
  }

  private static Browser browser() {
    if (BROWSER.get() == null) {
      Playwright playwright = Playwright.create();
      PLAYWRIGHT.set(playwright);
      BROWSER.set(typeFor(playwright).launch(new BrowserType.LaunchOptions()
          .setHeadless(Config.headless())
          .setSlowMo(Config.slowMo())));
    }
    return BROWSER.get();
  }

  private static BrowserType typeFor(Playwright playwright) {
    return switch (Config.browser().toLowerCase()) {
      case "chromium" -> playwright.chromium();
      case "firefox" -> playwright.firefox();
      case "webkit" -> playwright.webkit();
      default -> throw new IllegalArgumentException("Unsupported browser: " + Config.browser() + " (use chromium, firefox or webkit)");
    };
  }
}
