package es.ull.esit.app;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Coloca aquí stubs para que las clases reales no abran ventanas ni llamen al backend
 * cuando se invoquen desde las pruebas de `AdminLogin`.
 */
public class TestStubs {
  public static final AtomicBoolean adminProductsShown = new AtomicBoolean(false);
  public static final AtomicBoolean orderShown = new AtomicBoolean(false);
  public static final AtomicBoolean loginShown = new AtomicBoolean(false);
}
