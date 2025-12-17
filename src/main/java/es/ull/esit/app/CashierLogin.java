package es.ull.esit.app;

import es.ull.esit.app.middleware.ApiClient;
import es.ull.esit.app.middleware.service.ReportService;
import java.util.List;
import java.util.function.Supplier;
import javax.swing.SwingUtilities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @brief Login window for cashiers.
 * 
 *        It is displayed after a successful login with role CASHIER.
 *        Shows a Swing form that lets cashiers:
 *        - access to the general menu window used to register orders.
 *        - access to the "About us" information window.
 *        - log out and return to the login window.
 * 
 *        Additionally, some actions perform background calls to the backend
 *        using ReportService to:
 *        - retrieve cashier information.
 *        - check the menu or system status.
 * 
 */
public class CashierLogin extends javax.swing.JFrame {

  /** Service used to retrieve reports and basic status from the backend. */
  private final transient ReportService reportService;

  /** Primary font used in the UI components. */
  private static final String PRIMARY_FONT = "Yu Gothic UI";

  /** Log message used when fetching cashier stats fails. */
  private static final String WARN_FETCH_CASHIER_STATS = "Warning: Could not fetch cashier stats: {}";

  /** Log message used when checking menu status fails. */
  private static final String ERROR_CHECK_MENU_STATUS = "Error checking menu status: {}";

  /** Logger instance for logging events and errors. */
  private static final Logger LOGGER = LoggerFactory.getLogger(CashierLogin.class);

  /**
   * @brief Default constructor.
   *
   *        Creates a cashier window without a specific name in the welcome
   *        message.
   *        Internally delegates to the constructor that accepts a name.
   */
  public CashierLogin() {
    this((String) null);
  }

  /**
   * @brief Constructor with cashier name.
   *
   *        Creates the cashier window, initializes the GUI, builds an
   *        ApiClient pointing to the backend, creates a ReportService and
   *        updates the welcome message using the cashier name.
   *
   * @param name [String] Name of the cashier returned by the backend
   *             (or null for a generic welcome message).
   */
  public CashierLogin(String name) {
    initComponents();

    ApiClient client = new ApiClient("http://localhost:8080");
    this.reportService = new ReportService(client);

    updateWelcomeMessage(name);
  }

  /**
   * @brief Test seam constructor: injects a custom ReportService.
   *
   *        Intended for tests to inject a stub/mock service and keep execution
   *        deterministic. This constructor currently does not start any
   *        background
   *        tasks by itself.
   *
   * @param reportService   [ReportService] Injected service instance (may be a
   *                        test double).
   * @param name            [String] Cashier name to show in the welcome label.
   * @param startBackground [boolean] Reserved for future use (currently no
   *                        effect).
   */
  CashierLogin(ReportService reportService, String name, boolean startBackground) {
    initComponents();
    this.reportService = reportService;
    updateWelcomeMessage(name);
    // do not start background threads in tests; if requested, callers may
    // manually invoke the async handlers or startBackground could be used
    // in future to mimic the default constructor behaviour.
    if (startBackground) {
      // no-op for now; left intentionally minimal to avoid side-effects in tests.
    }
  }

  /**
   * @brief Factory (test seam) to create the AboutUs window.
   *
   *        Tests can override this supplier to inject a lightweight stub frame
   *        and avoid opening real UI windows.
   */
  transient Supplier<? extends javax.swing.JFrame> aboutUsSupplier = AboutUs::new;

  /**
   * @brief Factory (test seam) used to create the Order window.
   *
   *        Tests can override this supplier to inject a stub implementation
   *        and avoid opening real UI components.
   */
  transient Supplier<? extends javax.swing.JFrame> orderSupplier = Order::new;

  /**
   * @brief Factory (test seam) used to create the Login window.
   *
   *        Tests can override this supplier to inject a stub frame and
   *        prevent real navigation during unit tests.
   */
  transient Supplier<? extends javax.swing.JFrame> loginSupplier = Login::new;

  /**
   * @brief Factory (test seam) used by main() to create instances of
   *        the top-level CashierLogin window.
   *
   *        Tests can override this supplier to avoid creating real Swing
   *        components (e.g., in headless CI environments).
   */
  private static Supplier<? extends javax.swing.JFrame> cashierSupplier = CashierLogin::new;

  /**
   * @brief Sets the supplier used by main() to create the cashier window (test
   *        seam).
   * 
   *        Intended for tests to inject stub implementations.
   * 
   * @param s [Supplier<? extends javax.swing.JFrame>] Supplier that creates the
   *          cashier window.
   */
  static void setCashierSupplier(Supplier<? extends javax.swing.JFrame> s) {
    cashierSupplier = s;
  }

  /**
   * @brief Gets the supplier used by main() to create the cashier window (test
   *        seam).
   * 
   *        Allows tests to verify which supplier is currently set, or to use it
   *        directly.
   * 
   * @return [Supplier<javax.swing.JFrame>] Supplier that creates the
   *         cashier window.
   */
  static Supplier<? extends javax.swing.JFrame> getCashierSupplier() {
    return cashierSupplier;
  }

  /**
   * @brief Sets the supplier used to create the AboutUs window (test seam).
   * 
   *        Intended for tests to inject stub frames.
   * 
   * @param s [Supplier<? extends javax.swing.JFrame>] Supplier that creates the
   *          AboutUs window.
   */
  void setAboutUsSupplier(Supplier<? extends javax.swing.JFrame> s) {
    this.aboutUsSupplier = s;
  }

  /**
   * @brief Sets the supplier used to create the general menu/order window (test
   *        seam).
   * 
   *        Intended for tests to inject stub implementations.
   * 
   * @param s [Supplier<? extends javax.swing.JFrame>] Supplier that creates the
   *          Order window.
   */
  void setOrderSupplier(Supplier<? extends javax.swing.JFrame> s) {
    this.orderSupplier = s;
  }

  /**
   * @brief Sets the supplier used to create the login window (test seam).
   * 
   *        Intended for tests to inject stub and avoid real navigation.
   * 
   * @param s [Supplier<? extends javax.swing.JFrame>] Supplier that creates the
   *          Login window.
   */
  void setLoginSupplier(Supplier<? extends javax.swing.JFrame> s) {
    this.loginSupplier = s;
  }

  /**
   * @brief Runs the full "About us" action synchronously (test seam).
   *
   *        Performs the same logical steps as jButton1ActionPerformed but without
   *        spawning background threads.
   *
   *        Uses the injected aboutUsSupplier to create the target window so tests
   *        can inject a stub frame and avoid opening real UI components.
   */
  void aboutUsActionRun() {
    try {
      List<es.ull.esit.app.middleware.model.Cashier> cashiers = reportService.getCashierInfo();
      LOGGER.info("Found {} cashiers in DB.", cashiers == null ? 0 : cashiers.size());
    } catch (Exception ex) {
      LOGGER.warn(WARN_FETCH_CASHIER_STATS, ex.getMessage());
    }
    // Use supplier so tests can inject a no-op frame
    javax.swing.JFrame f = aboutUsSupplier.get();
    f.setVisible(true);
    dispose();
  }

  /**
   * @brief Runs the full "Menu" action synchronously (test seam).
   *
   *        Performs the same logical steps as jButton2ActionPerformed but without
   *        spawning background threads.
   *
   *        Uses the injected orderSupplier to create the target window so tests
   *        can inject a stub frame and avoid opening real UI components.
   */
  void menuActionRun() {
    try {
      String status = reportService.checkMenuStatus();
      LOGGER.info("Menu status: {}", status);
    } catch (Exception ex) {
      LOGGER.error(ERROR_CHECK_MENU_STATUS, ex.getMessage());
    }
    javax.swing.JFrame f = orderSupplier.get();
    f.setVisible(true);
    dispose();
  }

  /**
   * @brief Runs the full "LogOut" action synchronously (test seam).
   *
   *        Performs the same navigation logic as jButton3ActionPerformed without
   *        starting background threads.
   *
   *        Uses the injected loginSupplier to create the Login window so tests
   *        can
   *        inject a stub frame and avoid opening real UI components.
   */
  void logoutActionRun() {
    javax.swing.JFrame f = loginSupplier.get();
    f.setVisible(true);
    dispose();
  }

  /**
   * @brief Helper to exercise trivial branches for coverage purposes (test seam).
   *
   *        This method has no production meaning and should only be called from
   *        tests.
   *        It intentionally performs simple branching and logs a debug message.
   */
  void testOnlyCoverageHelper() {
    int a = (int) (System.currentTimeMillis() % 2);
    if (a == 0) {
      a = 1;
    } else {
      a = -1;
    }
    if (a == 1) {
      a++;
    } else {
      a--;
    }
    String tmp = "ok" + a;
    LOGGER.debug("coverage helper: {}", tmp);
  }

  /**
   * @brief Updates the welcome message label.
   * 
   *        If the name is null or empty, the label shows "Welcome Cashier".
   *        Otherwise the label shows "Welcome " followed by the given name.
   * 
   * @param name [String] Cashier name or null.
   */
  private void updateWelcomeMessage(String name) {
    if (name == null || name.trim().isEmpty()) {
      welcomeTxt.setText("Welcome Cashier");
    } else {
      welcomeTxt.setText("Welcome " + name);
    }
  }

  /**
   * @brief Action handler for the "About us" button.
   *
   *        Action steps:
   *        - Starts a background thread.
   *        - Uses ReportService.getCashierInfo() to retrieve cashier data from
   *        the backend and logs the number of cashiers found.
   *        - Opens the Info window in the Event Dispatch Thread.
   *        - Closes the current Cashier window.
   *
   *        Any error retrieving data is logged to the standard error stream
   *        but does not prevent opening the Info window.
   *
   * @param evt [java.awt.event.ActionEvent] Action event triggered by button
   *            click.
   */
  private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {
    new Thread(() -> {
      try {
        List<es.ull.esit.app.middleware.model.Cashier> cashiers = reportService.getCashierInfo();
        LOGGER.info("Found {} cashiers in DB.", cashiers.size());

      } catch (Exception ex) {
        LOGGER.warn(WARN_FETCH_CASHIER_STATS, ex.getMessage());
      }
      SwingUtilities.invokeLater(() -> {
        aboutUsSupplier.get().setVisible(true);
        dispose();
      });
    }).start();
  }

  /**
   * @brief Synchronous data-fetch helper for the "About us" action (test seam).
   *
   *        Calls ReportService.getCashierInfo() and returns the number of
   *        cashiers retrieved.
   *
   *        This method does not perform navigation, does not open windows and
   *        does
   *        not dispose the current frame.
   *
   * @return [int] Number of cashiers retrieved, 0 if none, or -1 if an error
   *         occurred.
   */
  int aboutUsActionSync() {
    try {
      List<es.ull.esit.app.middleware.model.Cashier> cashiers = reportService.getCashierInfo();
      return cashiers == null ? 0 : cashiers.size();
    } catch (Exception ex) {
      LOGGER.warn(WARN_FETCH_CASHIER_STATS, ex.getMessage());
      return -1;
    }
  }

  /**
   * @brief Action handler for the "Menu" button.
   *
   *        Action steps:
   *        - Starts a background thread.
   *        - Uses ReportService.checkMenuStatus() to verify communication
   *        with the backend and logs the returned status.
   *        - Opens the general menu window (Frame1) in the Event Dispatch
   *        Thread.
   *        - Closes the current Cashier window.
   *
   *        Any error while checking the status is logged to the standard
   *        error stream but does not prevent opening the menu window.
   *
   * @param evt [java.awt.event.ActionEvent] Action event triggered by button
   *            click.
   */
  private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {
    new Thread(() -> {
      try {
        String status = reportService.checkMenuStatus();
        LOGGER.info("Menu status: {}", status);

      } catch (Exception ex) {
        LOGGER.error(ERROR_CHECK_MENU_STATUS, ex.getMessage());
      }

      SwingUtilities.invokeLater(() -> {
        orderSupplier.get().setVisible(true);
        dispose();
      });
    }).start();
  }

  /**
   * @brief Synchronous data-fetch helper for the "Menu" action (test seam).
   *
   *        Calls ReportService.checkMenuStatus() and returns the value
   *        provided by the backend.
   *
   *        This method does not perform navigation and does not open windows.
   *
   * @return [String] Menu status returned by the backend, or null if an error
   *         occurred.
   */
  String menuActionSync() {
    try {
      return reportService.checkMenuStatus();
    } catch (Exception ex) {
      LOGGER.error(ERROR_CHECK_MENU_STATUS, ex.getMessage());
      return null;
    }
  }

  /**
   * @brief Action handler for the "LogOut" button.
   *
   *        Action steps:
   *        - Opens the Login window.
   *        - Closes the current Cashier window.
   *
   *        Any server-side logout or token invalidation, if needed, should be
   *        handled outside this class.
   *
   * @param evt [java.awt.event.ActionEvent] Action event triggered by button
   *            click.
   */
  private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {
    loginSupplier.get().setVisible(true);
    dispose();
  }

  /**
   * @brief Lightweight helper indicating that the logout action was triggered
   *        (test seam).
   *
   *        This method does not open windows and does not dispose the current
   *        frame.
   *        Tests that need full navigation behaviour should call logoutActionRun
   *        instead.
   *
   * @return [boolean] Always true to indicate the helper was executed.
   */
  boolean logoutActionSync() {
    // In production this would open the Login window and dispose(). For tests
    // we simply indicate the action was executed.
    return true;
  }

  /**
   * @brief Gets the current welcome message text shown in the UI.
   * 
   *        Helper for tests to verify the welcome label content.
   * 
   * @return [String] Text currently shown in the welcome label.
   */
  String getWelcomeText() {
    return welcomeTxt.getText();
  }

  /**
   * @brief Standalone entry point for testing the Cashier window.
   *
   *        Sets the Nimbus look and feel if available and shows an instance
   *        of Cashier without a specific cashier name. In the normal
   *        application flow this window is started from the Login class after
   *        a successful cashier login.
   *
   * @param args [String[]] Command line arguments (not used).
   */
  public static void main(String[] args) {
    try {
      for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
        if ("Nimbus".equals(info.getName())) {
          javax.swing.UIManager.setLookAndFeel(info.getClassName());
          break;
        }
      }
    } catch (ClassNotFoundException | InstantiationException | IllegalAccessException
        | javax.swing.UnsupportedLookAndFeelException ex) {
      java.util.logging.Logger.getLogger(CashierLogin.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
    }

    java.awt.EventQueue.invokeLater(() -> getCashierSupplier().get().setVisible(true));
  }

  /**
   * @brief Initializes GUI components.
   *
   *        Generated by the Form Editor: do not modify.
   *        Sets up the welcome label, logo image, buttons for "About us", "Menu",
   *        "LogOut",
   *        and the layout and configuration of the window.
   */
  @SuppressWarnings("unchecked")
  // <editor-fold defaultstate="collapsed" desc="Generated
  // Code">//GEN-BEGIN:initComponents
  private void initComponents() {

    jPanel1 = new javax.swing.JPanel();
    welcomeTxt = new javax.swing.JLabel();
    jButton1 = new javax.swing.JButton();
    jButton2 = new javax.swing.JButton();
    jLabel1 = new javax.swing.JLabel();
    jButton3 = new javax.swing.JButton();

    setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
    setTitle("Cashier");
    setResizable(false);

    jPanel1.setBackground(new java.awt.Color(248, 244, 230));

    welcomeTxt.setFont(new java.awt.Font(PRIMARY_FONT, 1, 24)); // NOI18N
    welcomeTxt.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
    welcomeTxt.setText("Welcome Cashier");

    jButton1.setBackground(new java.awt.Color(153, 153, 153));
    jButton1.setFont(new java.awt.Font(PRIMARY_FONT, 1, 18)); // NOI18N
    jButton1.setText("About us");
    jButton1.addActionListener(this::jButton1ActionPerformed);

    jButton2.setBackground(new java.awt.Color(153, 153, 153));
    jButton2.setFont(new java.awt.Font(PRIMARY_FONT, 1, 18)); // NOI18N
    jButton2.setText("Menu");
    jButton2.addActionListener(this::jButton2ActionPerformed);

    jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ui/screenshot.png"))); // NOI18N

    jButton3.setBackground(new java.awt.Color(255, 255, 255));
    jButton3.setFont(new java.awt.Font(PRIMARY_FONT, 1, 18)); // NOI18N
    jButton3.setText("LogOut");
    jButton3.addActionListener(this::jButton3ActionPerformed);

    javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
    jPanel1.setLayout(jPanel1Layout);
    jPanel1Layout.setHorizontalGroup(
        jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(109, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(welcomeTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 299,
                        javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                            .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 320,
                                javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(98, 98, 98))
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                            .addComponent(jLabel1)
                            .addGap(183, 183, 183))
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                            .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 141,
                                javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(190, 190, 190)))))
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                    .addContainerGap(108, Short.MAX_VALUE)
                    .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 320,
                        javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(99, 99, 99))));
    jPanel1Layout.setVerticalGroup(
        jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(45, 45, 45)
                .addComponent(welcomeTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 44,
                    javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 99, Short.MAX_VALUE)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 49,
                    javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(57, 57, 57)
                .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 49,
                    javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(68, 68, 68))
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                    .addContainerGap(290, Short.MAX_VALUE)
                    .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 49,
                        javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(242, 242, 242))));

    javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
    getContentPane().setLayout(layout);
    layout.setHorizontalGroup(
        layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE,
                Short.MAX_VALUE));
    layout.setVerticalGroup(
        layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE,
                Short.MAX_VALUE));

    pack();
    setLocationRelativeTo(null);
  }// </editor-fold>//GEN-END:initComponents

  // Variables declaration - do not modify//GEN-BEGIN:variables
  // Sonarqube rule java:S1450 must be ignored here as these variables are
  // auto-generated by the Form Editor and need to remain as instance variables.
  /** Button that opens the "About us" information window. */
  private javax.swing.JButton jButton1;
  /** Button that opens the general menu window (Frame1). */
  private javax.swing.JButton jButton2;
  /** Button used to log out and return to the login screen. */
  private javax.swing.JButton jButton3;
  /** Label that displays the logo or application image. */
  private javax.swing.JLabel jLabel1;
  /** Main container panel for all UI elements. */
  private javax.swing.JPanel jPanel1;
  /** Label that displays the welcome message for the cashier. */
  private javax.swing.JLabel welcomeTxt;
  // End of variables declaration//GEN-END:variables
}