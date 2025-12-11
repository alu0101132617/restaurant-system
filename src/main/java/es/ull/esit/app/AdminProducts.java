package es.ull.esit.app;

import es.ull.esit.app.middleware.ApiClient;
import es.ull.esit.app.middleware.model.Appetizer;
import es.ull.esit.app.middleware.model.Drink;
import es.ull.esit.app.middleware.model.MainCourse;
import es.ull.esit.app.middleware.service.ProductService;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * @brief Administrative window for managing products and prices.
 *
 *        Swing window that allows administrators to:
 *        - view drinks, appetizers and main courses loaded from the backend.
 *        - add new items to each category.
 *        - update the price and name of existing items.
 *
 *        All data is obtained and persisted through the ProductService, which
 *        internally uses ApiClient to call the REST API.
 */
public class AdminProducts extends javax.swing.JFrame {

  /** Service used to call the REST API and apply product-related logic. */
  private final transient ProductService productService;

  /** Logger for logging events and errors. */
  private static final Logger LOGGER = LoggerFactory.getLogger(AdminProducts.class);

  /** Primary font used throughout the GUI. */
  private static final String PRIMARY_FONT = "Yu Gothic UI";
  /** Secondary font used for buttons and smaller text. */
  private static final String SECONDARY_FONT = "Thonburi";

  /** Column header for the ID column in product tables. */
  private static final String FIRST_COLUMN_HEADER = "ID";
  /** Column header for the name column in product tables. */
  private static final String SECOND_COLUMN_HEADER = "Item Name";
  /** Column header for the price column in product tables. */
  private static final String THIRD_COLUMN_HEADER = "Item Price";

  /** String constant for the "Update" button label. */
  private static final String UPDATE_STRING = "Update";

  /** Table model used to display drink items. */
  DefaultTableModel modelDrink;
  /** Table model used to display appetizer items. */
  DefaultTableModel modelappetizers;
  /** Table model used to display main course items. */
  DefaultTableModel modelmaincourse;

  /** Column headers shared by all product tables (ID, name and price). */
  String[] columnNames = { FIRST_COLUMN_HEADER, SECOND_COLUMN_HEADER, THIRD_COLUMN_HEADER };

  /** ID of the currently selected drink (null if no selection). */
  Long selectedDrinkID;
  /** ID of the currently selected appetizer (null if no selection). */
  Long selectedAppetizerID;
  /** ID of the currently selected main course (null if no selection). */
  Long selectedMainCourseID;

  /**
   * @brief Constructor.
   *
   *        Creates the admin products window, initializes GUI components,
   *        configures the table models and loads the initial data from the
   *        backend service.
   */
  public AdminProducts() {
    initComponents();

    // Initialize the Service Layer.
    ApiClient client = new ApiClient("http://localhost:8080");
    this.productService = new ProductService(client);

    // Initialize table models and set shared headers.
    modelDrink = new DefaultTableModel();
    modelDrink.setColumnIdentifiers(columnNames);
    modelappetizers = new DefaultTableModel();
    modelappetizers.setColumnIdentifiers(columnNames);
    modelmaincourse = new DefaultTableModel();
    modelmaincourse.setColumnIdentifiers(columnNames);

    // Link models to tables.
    jTable1.setModel(modelDrink);
    jTable2.setModel(modelappetizers);
    jTable3.setModel(modelmaincourse);

    // Load initial data from backend.
    refreshAllTables();
  }
  
  /**
   * Package-private constructor used for tests to inject a stubbed
   * ProductService and optionally avoid starting the asynchronous loaders.
   *
   * @param productService [ProductService] injected service (test stub)
   * @param startLoading   [boolean] if true, triggers refreshAllTables()
   */
  AdminProducts(ProductService productService, boolean startLoading) {
    initComponents();

    // Use injected service instead of creating a new ApiClient.
    this.productService = productService;

    // Initialize table models and set shared headers.
    modelDrink = new DefaultTableModel();
    modelDrink.setColumnIdentifiers(columnNames);
    modelappetizers = new DefaultTableModel();
    modelappetizers.setColumnIdentifiers(columnNames);
    modelmaincourse = new DefaultTableModel();
    modelmaincourse.setColumnIdentifiers(columnNames);

    // Link models to tables.
    jTable1.setModel(modelDrink);
    jTable2.setModel(modelappetizers);
    jTable3.setModel(modelmaincourse);

    if (startLoading) {
      refreshAllTables();
    }
  }

  /**
   * @brief Reloads all product tables.
   *
   *        Helper method that triggers asynchronous loading of drinks,
   *        appetizers and main courses from the server.
   */
  private void refreshAllTables() {
    loadDrinks();
    loadAppetizer();
    loadmainCourse();
  }

  /**
   * @brief Loads the list of drinks from the backend asynchronously.
   *
   *        Performs the REST call in a background thread and, when finished,
   *        updates the Swing table model on the Event Dispatch Thread.
   *        If an error occurs, a message dialog is shown to the user.
   */
  void loadDrinks() {
    new Thread(() -> {
      try {
        List<Drink> drinks = productService.getAllDrinks();
        SwingUtilities.invokeLater(() -> {
          modelDrink.setRowCount(0);
          for (Drink drink : drinks) {
            modelDrink.addRow(new Object[] {
                drink.getDrinksId(),
                drink.getItemDrinks(),
                drink.getDrinksPrice()
            });
          }
        });
      } catch (Exception ex) {
        SwingUtilities
            .invokeLater(() -> JOptionPane.showMessageDialog(null, "Error loading drinks: " + ex.getMessage()));
      }
    }).start();
  }

  /**
   * Synchronous variant of loadDrinks used by tests. Does not spawn new
   * threads or use SwingUtilities; fills the model directly from the
   * ProductService.
   */
  void loadDrinksSync() {
    try {
      List<Drink> drinks = productService.getAllDrinks();
      modelDrink.setRowCount(0);
      for (Drink drink : drinks) {
        modelDrink.addRow(new Object[] { drink.getDrinksId(), drink.getItemDrinks(), drink.getDrinksPrice() });
      }
    } catch (Exception ex) {
      // In tests we prefer to propagate exceptions; keep same behaviour as async but avoid dialogs.
      throw new RuntimeException(ex);
    }
  }

  /**
   * @brief Loads the list of appetizers from the backend asynchronously.
   *
   *        Clears the current table rows and fills them with fresh data from
   *        the database. Errors are reported with a dialog.
   */
  void loadAppetizer() {
    new Thread(() -> {
      try {
        List<Appetizer> appetizers = productService.getAllAppetizers();
        SwingUtilities.invokeLater(() -> {
          modelappetizers.setRowCount(0);
          for (Appetizer appetizer : appetizers) {
            modelappetizers.addRow(new Object[] {
                appetizer.getAppetizersId(),
                appetizer.getItemAppetizers(),
                appetizer.getAppetizersPrice()
            });
          }
        });
      } catch (Exception ex) {
        SwingUtilities
            .invokeLater(() -> JOptionPane.showMessageDialog(null, "Error loading appetizers: " + ex.getMessage()));
      }
    }).start();
  }

  /**
   * Synchronous variant of loadAppetizer used by tests.
   */
  void loadAppetizerSync() {
    try {
      List<Appetizer> appetizers = productService.getAllAppetizers();
      modelappetizers.setRowCount(0);
      for (Appetizer appetizer : appetizers) {
        modelappetizers.addRow(new Object[] { appetizer.getAppetizersId(), appetizer.getItemAppetizers(), appetizer.getAppetizersPrice() });
      }
    } catch (Exception ex) {
      throw new RuntimeException(ex);
    }
  }

  /**
   * @brief Loads the list of main courses from the backend asynchronously.
   *
   *        After retrieving the data, the rows of the main course table
   *        are replaced with the updated list of items.
   */
  void loadmainCourse() {
    new Thread(() -> {
      try {
        List<MainCourse> mainCourses = productService.getAllMainCourses();
        SwingUtilities.invokeLater(() -> {
          modelmaincourse.setRowCount(0);
          for (MainCourse mainCourse : mainCourses) {
            modelmaincourse.addRow(new Object[] {
                mainCourse.getFoodId(),
                mainCourse.getItemFood(),
                mainCourse.getFoodPrice()
            });
          }
        });
      } catch (Exception ex) {
        SwingUtilities
            .invokeLater(() -> JOptionPane.showMessageDialog(null, "Error loading main courses: " + ex.getMessage()));
      }
    }).start();
  }

  /**
   * Synchronous variant of loadmainCourse used by tests.
   */
  void loadmainCourseSync() {
    try {
      List<MainCourse> mainCourses = productService.getAllMainCourses();
      modelmaincourse.setRowCount(0);
      for (MainCourse mainCourse : mainCourses) {
        modelmaincourse.addRow(new Object[] { mainCourse.getFoodId(), mainCourse.getItemFood(), mainCourse.getFoodPrice() });
      }
    } catch (Exception ex) {
      throw new RuntimeException(ex);
    }
  }

  /**
   * Helper used in tests to synchronously select a drink row and populate
   * the editable fields without spawning background threads.
   *
   * @param row index in the table model to select
   */
  void selectDrinkByRowSync(int row) {
    if (row < 0 || row >= modelDrink.getRowCount())
      return;
    String idStr = modelDrink.getValueAt(row, 0).toString();
    Long id = Long.valueOf(idStr);
    Drink drink = productService.getDrinkById(id);
    itemname.setText(drink.getItemDrinks());
    itemprice.setText(String.valueOf(drink.getDrinksPrice()));
  }

  void selectAppetizerByRowSync(int row) {
    if (row < 0 || row >= modelappetizers.getRowCount())
      return;
    String idStr = modelappetizers.getValueAt(row, 0).toString();
    Long id = Long.valueOf(idStr);
    Appetizer appetizer = productService.getAppetizerById(id);
    itemname1.setText(appetizer.getItemAppetizers());
    itemprice1.setText(String.valueOf(appetizer.getAppetizersPrice()));
  }

  void selectMainCourseByRowSync(int row) {
    if (row < 0 || row >= modelmaincourse.getRowCount())
      return;
    String idStr = modelmaincourse.getValueAt(row, 0).toString();
    Long id = Long.valueOf(idStr);
    MainCourse mainCourse = productService.getMainCourseById(id);
    itemname2.setText(mainCourse.getItemFood());
    itemprice2.setText(String.valueOf(mainCourse.getFoodPrice()));
  }

  /**
   * @brief Initializes GUI components.
   *
   *        Generated by the Form Editor: do not modify manually.
   *        Creates labels, buttons, tabbed panes and tables for the three
   *        product categories (drinks, appetizers, main courses) and the
   *        navigation button "Go Back".
   */
  @SuppressWarnings("unchecked")
  // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
  private void initComponents() {

    jPanel1 = new javax.swing.JPanel();
    jLabel1 = new javax.swing.JLabel();
    jTabbedPane1 = new javax.swing.JTabbedPane();
    jPanel2 = new javax.swing.JPanel();
    itemname = new javax.swing.JTextField();
    itemprice = new javax.swing.JTextField();
    jScrollPane1 = new javax.swing.JScrollPane();
    jTable1 = new javax.swing.JTable();
    jButton1 = new javax.swing.JButton();
    jButton2 = new javax.swing.JButton();
    jLabel2 = new javax.swing.JLabel();
    jLabel3 = new javax.swing.JLabel();
    jLabel4 = new javax.swing.JLabel();
    jPanel3 = new javax.swing.JPanel();
    jLabel5 = new javax.swing.JLabel();
    jLabel6 = new javax.swing.JLabel();
    jLabel7 = new javax.swing.JLabel();
    jScrollPane2 = new javax.swing.JScrollPane();
    jTable2 = new javax.swing.JTable();
    itemname1 = new javax.swing.JTextField();
    itemprice1 = new javax.swing.JTextField();
    jButton4 = new javax.swing.JButton();
    jButton5 = new javax.swing.JButton();
    jPanel4 = new javax.swing.JPanel();
    jLabel8 = new javax.swing.JLabel();
    jLabel9 = new javax.swing.JLabel();
    jLabel10 = new javax.swing.JLabel();
    jScrollPane3 = new javax.swing.JScrollPane();
    jTable3 = new javax.swing.JTable();
    itemname2 = new javax.swing.JTextField();
    itemprice2 = new javax.swing.JTextField();
    jButton6 = new javax.swing.JButton();
    jButton7 = new javax.swing.JButton();
    jButton3 = new javax.swing.JButton();

    setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
    setTitle("Admin");
    setResizable(false);

    jPanel1.setBackground(new java.awt.Color(248, 244, 230));

    jLabel1.setBackground(new java.awt.Color(255, 153, 0));
    jLabel1.setFont(new java.awt.Font(PRIMARY_FONT, 1, 36)); // NOI18N
    jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
    jLabel1.setText("Items Prices Update Portal");

    jTabbedPane1.setBackground(new java.awt.Color(248, 244, 230));
    jTabbedPane1.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));
    jTabbedPane1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
    jTabbedPane1.setFont(new java.awt.Font(PRIMARY_FONT, 1, 18)); // NOI18N

    jPanel2.setBackground(new java.awt.Color(248, 244, 230));

    itemname.setFont(new java.awt.Font(PRIMARY_FONT, 0, 18)); // NOI18N
    itemname.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
    itemname.setBorder(javax.swing.BorderFactory.createTitledBorder(
        new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true), SECOND_COLUMN_HEADER,
        javax.swing.border.TitledBorder.LEFT, javax.swing.border.TitledBorder.TOP,
        new java.awt.Font(PRIMARY_FONT, 0, 18))); // NOI18N

    itemprice.setFont(new java.awt.Font(PRIMARY_FONT, 0, 18)); // NOI18N
    itemprice.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
    itemprice.setBorder(javax.swing.BorderFactory.createTitledBorder(
        new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true), THIRD_COLUMN_HEADER,
        javax.swing.border.TitledBorder.LEFT, javax.swing.border.TitledBorder.TOP,
        new java.awt.Font(PRIMARY_FONT, 0, 18))); // NOI18N

    jTable1.setFont(new java.awt.Font(PRIMARY_FONT, 0, 14)); // NOI18N
    jTable1.setModel(new javax.swing.table.DefaultTableModel(
        new Object[][] {
            {},
            {},
            {},
            {}
        },
        new String[] {

        }));
    jTable1.setInheritsPopupMenu(true);
    jTable1.getTableHeader().setResizingAllowed(false);
    jTable1.getTableHeader().setReorderingAllowed(false);
    jTable1.addMouseListener(new java.awt.event.MouseAdapter() {
      @Override
      public void mouseClicked(java.awt.event.MouseEvent evt) {
        jTable1MouseClicked(evt);
      }
    });
    jTable1.addKeyListener(new java.awt.event.KeyAdapter() {
      @Override
      public void keyPressed(java.awt.event.KeyEvent evt) {
        jTable1KeyPressed(evt);
      }
    });
    jScrollPane1.setViewportView(jTable1);

    jButton1.setBackground(new java.awt.Color(255, 255, 255));
    jButton1.setFont(new java.awt.Font(SECONDARY_FONT, 0, 18)); // NOI18N
    jButton1.setText(UPDATE_STRING);
    jButton1.addActionListener(this::jButton1ActionPerformed);

    jButton2.setBackground(new java.awt.Color(255, 255, 255));
    jButton2.setFont(new java.awt.Font(SECONDARY_FONT, 0, 18)); // NOI18N
    jButton2.setText("Add");
    jButton2.addActionListener(this::jButton2ActionPerformed);

    jLabel2.setBackground(new java.awt.Color(255, 153, 0));
    jLabel2.setFont(new java.awt.Font(PRIMARY_FONT, 1, 25)); // NOI18N
    jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
    jLabel2.setText("Available Drinks");

    jLabel3.setFont(new java.awt.Font(PRIMARY_FONT, 0, 14)); // NOI18N
    jLabel3.setText("Enter new drink information carefully to add.");

    jLabel4.setFont(new java.awt.Font(PRIMARY_FONT, 0, 14)); // NOI18N
    jLabel4.setText("Please select the drink to update the price.");

    javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
    jPanel2.setLayout(jPanel2Layout);
    jPanel2Layout.setHorizontalGroup(
        jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addGap(0, 27, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(24, 24, 24)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 91,
                                    javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 109,
                                    Short.MAX_VALUE)
                                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 103,
                                    javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(8, 8, 8))
                            .addComponent(itemprice)
                            .addComponent(itemname, javax.swing.GroupLayout.DEFAULT_SIZE, 311, Short.MAX_VALUE)
                            .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE,
                                javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED,
                            javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 586,
                                javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 408,
                                javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(115, 115, 115))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 1111,
                            javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)))));
    jPanel2Layout.setVerticalGroup(
        jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 21, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(jLabel4))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(itemname, javax.swing.GroupLayout.PREFERRED_SIZE, 59,
                            javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(itemprice, javax.swing.GroupLayout.PREFERRED_SIZE, 59,
                            javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(37, 37, 37)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 42,
                                javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 42,
                                javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 203,
                        javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(48, 48, 48)));

    jTabbedPane1.addTab("Drinks", jPanel2);

    jPanel3.setBackground(new java.awt.Color(248, 244, 230));

    jLabel5.setFont(new java.awt.Font(PRIMARY_FONT, 0, 14)); // NOI18N
    jLabel5.setText("Enter new appetizer information carefully to add.");

    jLabel6.setBackground(new java.awt.Color(255, 153, 0));
    jLabel6.setFont(new java.awt.Font(PRIMARY_FONT, 1, 25)); // NOI18N
    jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
    jLabel6.setText("Available Appetizer");

    jLabel7.setFont(new java.awt.Font(PRIMARY_FONT, 0, 14)); // NOI18N
    jLabel7.setText("Please select the appetizer to update the price.");

    jTable2.setFont(new java.awt.Font(PRIMARY_FONT, 0, 14)); // NOI18N
    jTable2.setModel(new javax.swing.table.DefaultTableModel(
        new Object[][] {
            {},
            {},
            {},
            {}
        },
        new String[] {

        }));
    jTable2.addMouseListener(new java.awt.event.MouseAdapter() {
      @Override
      public void mouseClicked(java.awt.event.MouseEvent evt) {
        jTable2MouseClicked(evt);
      }
    });
    jScrollPane2.setViewportView(jTable2);

    itemname1.setFont(new java.awt.Font(PRIMARY_FONT, 0, 18)); // NOI18N
    itemname1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
    itemname1.setBorder(javax.swing.BorderFactory.createTitledBorder(
        new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true), SECOND_COLUMN_HEADER,
        javax.swing.border.TitledBorder.LEFT, javax.swing.border.TitledBorder.TOP,
        new java.awt.Font(PRIMARY_FONT, 0, 18))); // NOI18N

    itemprice1.setFont(new java.awt.Font(PRIMARY_FONT, 0, 18)); // NOI18N
    itemprice1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
    itemprice1.setBorder(javax.swing.BorderFactory.createTitledBorder(
        new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true), THIRD_COLUMN_HEADER,
        javax.swing.border.TitledBorder.LEFT, javax.swing.border.TitledBorder.TOP,
        new java.awt.Font(PRIMARY_FONT, 0, 18))); // NOI18N

    jButton4.setBackground(new java.awt.Color(255, 255, 255));
    jButton4.setFont(new java.awt.Font(SECONDARY_FONT, 0, 18)); // NOI18N
    jButton4.setText(UPDATE_STRING);
    jButton4.addActionListener(this::jButton4ActionPerformed);

    jButton5.setBackground(new java.awt.Color(255, 255, 255));
    jButton5.setFont(new java.awt.Font(SECONDARY_FONT, 0, 18)); // NOI18N
    jButton5.setText("Add");
    jButton5.addActionListener(this::jButton5ActionPerformed);

    javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
    jPanel3.setLayout(jPanel3Layout);
    jPanel3Layout.setHorizontalGroup(
        jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addGap(0, 27, Short.MAX_VALUE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(24, 24, 24)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 91,
                                    javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED,
                                    javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 103,
                                    javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(8, 8, 8))
                            .addComponent(itemprice1)
                            .addComponent(itemname1)
                            .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE,
                                javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED,
                            javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 586,
                                javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 408,
                                javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(115, 115, 115))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 1111,
                            javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)))));
    jPanel3Layout.setVerticalGroup(
        jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 21, Short.MAX_VALUE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(jLabel7))
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(itemname1, javax.swing.GroupLayout.PREFERRED_SIZE, 59,
                            javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(itemprice1, javax.swing.GroupLayout.PREFERRED_SIZE, 59,
                            javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(37, 37, 37)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 42,
                                javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 42,
                                javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 203,
                        javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(48, 48, 48)));

    jTabbedPane1.addTab("Appetizers", jPanel3);

    jPanel4.setBackground(new java.awt.Color(248, 244, 230));
    jPanel4.setFont(new java.awt.Font(PRIMARY_FONT, 0, 18)); // NOI18N

    jLabel8.setBackground(new java.awt.Color(248, 244, 230));
    jLabel8.setFont(new java.awt.Font(PRIMARY_FONT, 0, 14)); // NOI18N
    jLabel8.setText("Enter new item information carefully to add.");

    jLabel9.setBackground(new java.awt.Color(255, 153, 0));
    jLabel9.setFont(new java.awt.Font(PRIMARY_FONT, 1, 25)); // NOI18N
    jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
    jLabel9.setText("Avaliable MainCourse");

    jLabel10.setFont(new java.awt.Font(PRIMARY_FONT, 0, 14)); // NOI18N
    jLabel10.setText("Please select the item to update the price.");

    jTable3.setFont(new java.awt.Font(PRIMARY_FONT, 0, 14)); // NOI18N
    jTable3.setModel(new javax.swing.table.DefaultTableModel(
        new Object[][] {
            {},
            {},
            {},
            {}
        },
        new String[] {

        }));
    jTable3.addMouseListener(new java.awt.event.MouseAdapter() {
      @Override
      public void mouseClicked(java.awt.event.MouseEvent evt) {
        jTable3MouseClicked(evt);
      }
    });
    jScrollPane3.setViewportView(jTable3);

    itemname2.setFont(new java.awt.Font(PRIMARY_FONT, 0, 18)); // NOI18N
    itemname2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
    itemname2.setBorder(javax.swing.BorderFactory.createTitledBorder(
        new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true), SECOND_COLUMN_HEADER,
        javax.swing.border.TitledBorder.LEFT, javax.swing.border.TitledBorder.TOP,
        new java.awt.Font(PRIMARY_FONT, 0, 18))); // NOI18N

    itemprice2.setFont(new java.awt.Font(PRIMARY_FONT, 0, 18)); // NOI18N
    itemprice2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
    itemprice2.setBorder(javax.swing.BorderFactory.createTitledBorder(
        new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true), THIRD_COLUMN_HEADER,
        javax.swing.border.TitledBorder.LEFT, javax.swing.border.TitledBorder.TOP,
        new java.awt.Font(PRIMARY_FONT, 0, 18))); // NOI18N

    jButton6.setBackground(new java.awt.Color(255, 255, 255));
    jButton6.setFont(new java.awt.Font(SECONDARY_FONT, 0, 18)); // NOI18N
    jButton6.setText(UPDATE_STRING);
    jButton6.addActionListener(this::jButton6ActionPerformed);

    jButton7.setBackground(new java.awt.Color(255, 255, 255));
    jButton7.setFont(new java.awt.Font(SECONDARY_FONT, 0, 18)); // NOI18N
    jButton7.setText("Add");
    jButton7.addActionListener(this::jButton7ActionPerformed);

    javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
    jPanel4.setLayout(jPanel4Layout);
    jPanel4Layout.setHorizontalGroup(
        jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addGap(0, 27, Short.MAX_VALUE)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(24, 24, 24)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addComponent(jButton7, javax.swing.GroupLayout.PREFERRED_SIZE, 91,
                                    javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 109,
                                    Short.MAX_VALUE)
                                .addComponent(jButton6, javax.swing.GroupLayout.PREFERRED_SIZE, 103,
                                    javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(8, 8, 8))
                            .addComponent(itemprice2)
                            .addComponent(itemname2, javax.swing.GroupLayout.DEFAULT_SIZE, 311, Short.MAX_VALUE)
                            .addComponent(jLabel8, javax.swing.GroupLayout.DEFAULT_SIZE,
                                javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED,
                            javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 586,
                                javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 408,
                                javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(115, 115, 115))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 1111,
                            javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)))));
    jPanel4Layout.setVerticalGroup(
        jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel9)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 21, Short.MAX_VALUE)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(jLabel10))
                .addGap(18, 18, 18)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(itemname2, javax.swing.GroupLayout.PREFERRED_SIZE, 59,
                            javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(itemprice2, javax.swing.GroupLayout.PREFERRED_SIZE, 59,
                            javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(37, 37, 37)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jButton6, javax.swing.GroupLayout.PREFERRED_SIZE, 42,
                                javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton7, javax.swing.GroupLayout.PREFERRED_SIZE, 42,
                                javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 203,
                        javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(48, 48, 48)));

    jTabbedPane1.addTab("MainCourse", jPanel4);

    jButton3.setBackground(new java.awt.Color(255, 255, 255));
    jButton3.setFont(new java.awt.Font(PRIMARY_FONT, 1, 18)); // NOI18N
    jButton3.setText("Go Back");
    jButton3.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));
    jButton3.addActionListener(this::jButton3ActionPerformed);

    javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
    jPanel1.setLayout(jPanel1Layout);
    jPanel1Layout.setHorizontalGroup(
        jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 122,
                            javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED,
                            javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 689,
                            javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(218, 218, 218))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE,
                            javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(29, Short.MAX_VALUE)))));
    jPanel1Layout.setVerticalGroup(
        jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 60,
                        javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 37,
                        javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED,
                    javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 392,
                    javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(29, 29, 29)));

    javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
    getContentPane().setLayout(layout);
    layout.setHorizontalGroup(
        layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE,
                Short.MAX_VALUE));
    layout.setVerticalGroup(
        layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE,
                    javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE)));

    pack();
    setLocationRelativeTo(null);
  }// </editor-fold>//GEN-END:initComponents

  /**
   * @brief Action handler for the "Add" button in the Drinks tab.
   *
   *        Steps:
   *        - Reads the name and price from the text fields.
   *        - Uses ProductService to create a new Drink in the backend.
   *        - Shows a confirmation dialog and refreshes the drinks table.
   *
   * @param evt [java.awt.event.ActionEvent] Event triggered by the button click.
   */
  private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jButton2ActionPerformed
    String name = itemname.getText();
    String price = itemprice.getText();

    new Thread(() -> {
      try {
        productService.addDrink(name, price);
        SwingUtilities.invokeLater(() -> {
          JOptionPane.showMessageDialog(this, "Drink Added Successfully.");
          itemname.setText("");
          itemprice.setText("");
          loadDrinks();
        });
      } catch (Exception ex) {
        SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(this, "Error adding drink: " + ex.getMessage()));
      }
    }).start();
  }// GEN-LAST:event_jButton2ActionPerformed

  /**
   * @brief Action handler for the "Go Back" button.
   *
   *        Closes the current AdminProducts window and returns to the
   *        AdminLogin window.
   *
   * @param evt [java.awt.event.ActionEvent] Event triggered by the button click.
   */
  private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jButton3ActionPerformed
    new AdminLogin().setVisible(true);
    this.dispose();
  }// GEN-LAST:event_jButton3ActionPerformed

  /**
   * @brief Action handler for the "Add" button in the Appetizers tab.
   *
   *        Creates a new appetizer in the backend using the data entered
   *        by the administrator and reloads the appetizers table.
   *
   * @param evt [java.awt.event.ActionEvent] Event triggered by the button click.
   */
  private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jButton5ActionPerformed
    String name = itemname1.getText();
    String price = itemprice1.getText();

    new Thread(() -> {
      try {
        productService.addAppetizer(name, price);
        SwingUtilities.invokeLater(() -> {
          JOptionPane.showMessageDialog(this, "Appetizer Added Successfully.");
          itemname1.setText("");
          itemprice1.setText("");
          loadAppetizer();
        });
      } catch (Exception ex) {
        SwingUtilities
            .invokeLater(() -> JOptionPane.showMessageDialog(this, "Error adding appetizer: " + ex.getMessage()));
      }
    }).start();
  }// GEN-LAST:event_jButton5ActionPerformed

  /**
   * @brief Action handler for the "Add" button in the MainCourse tab.
   *
   *        Sends a new main course to the backend and refreshes the
   *        corresponding table.
   *
   * @param evt [java.awt.event.ActionEvent] Event triggered by the button click.
   */
  private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jButton7ActionPerformed
    String name = itemname2.getText();
    String price = itemprice2.getText();

    new Thread(() -> {
      try {
        productService.addMainCourse(name, price);
        SwingUtilities.invokeLater(() -> {
          JOptionPane.showMessageDialog(this, "Main Course Added Successfully.");
          itemname2.setText("");
          itemprice2.setText("");
          loadmainCourse();
        });
      } catch (Exception ex) {
        SwingUtilities
            .invokeLater(() -> JOptionPane.showMessageDialog(this, "Error adding main course: " + ex.getMessage()));
      }
    }).start();
  }// GEN-LAST:event_jButton7ActionPerformed

  /**
   * @brief Key handler for the drinks table.
   *
   *        Currently not used. Kept for potential future keyboard handling
   *        when navigating drink rows.
   *
   * @param evt [java.awt.event.KeyEvent] Key event generated by the table.
   */
  private void jTable1KeyPressed(java.awt.event.KeyEvent evt) {// GEN-FIRST:event_jTable1KeyPressed
    // No action needed for key press in this version.
  }// GEN-LAST:event_jTable1KeyPressed

  /**
   * @brief Mouse handler for the drinks table.
   *
   *        When a row is clicked:
   *        - Saves the selected drink ID.
   *        - Requests detailed data from the backend.
   *        - Fills the name and price fields so they can be edited.
   *
   * @param evt [java.awt.event.MouseEvent] Mouse event generated by the table.
   */
  private void jTable1MouseClicked(java.awt.event.MouseEvent evt) {// GEN-FIRST:event_jTable1MouseClicked
    int row = jTable1.getSelectedRow();
    if (row == -1)
      return;

    String idStr = jTable1.getValueAt(row, 0).toString();
    selectedDrinkID = Long.valueOf(idStr);
    LOGGER.info("Selected Drink ID: {}", selectedDrinkID);

    new Thread(() -> {
      try {
        Drink drink = productService.getDrinkById(selectedDrinkID);
        SwingUtilities.invokeLater(() -> {
          itemname.setText(drink.getItemDrinks());
          itemprice.setText(String.valueOf(drink.getDrinksPrice()));
        });
      } catch (Exception ex) {
        SwingUtilities
            .invokeLater(() -> JOptionPane.showMessageDialog(null, "Error loading drink details: " + ex.getMessage()));
      }
    }).start();
  }// GEN-LAST:event_jTable1MouseClicked

  /**
   * @brief Mouse handler for the appetizers table.
   *
   *        Loads the selected appetizer from the backend and populates
   *        the corresponding text fields to allow editing.
   *
   * @param evt [java.awt.event.MouseEvent] Mouse event generated by the table.
   */
  private void jTable2MouseClicked(java.awt.event.MouseEvent evt) {// GEN-FIRST:event_jTable2MouseClicked
    int row = jTable2.getSelectedRow();
    if (row == -1)
      return;

    String idStr = jTable2.getValueAt(row, 0).toString();
    selectedAppetizerID = Long.valueOf(idStr);
    LOGGER.info("Selected Appetizer ID: {}", selectedAppetizerID);

    new Thread(() -> {
      try {
        Appetizer appetizer = productService.getAppetizerById(selectedAppetizerID);
        SwingUtilities.invokeLater(() -> {
          itemname1.setText(appetizer.getItemAppetizers());
          itemprice1.setText(String.valueOf(appetizer.getAppetizersPrice()));
        });
      } catch (Exception ex) {
        SwingUtilities.invokeLater(
            () -> JOptionPane.showMessageDialog(null, "Error loading appetizer details: " + ex.getMessage()));
      }
    }).start();
  }// GEN-LAST:event_jTable2MouseClicked

  /**
   * @brief Mouse handler for the main course table.
   *
   *        Loads the selected main course from the backend and puts its
   *        data into the editable text fields.
   *
   * @param evt [java.awt.event.MouseEvent] Mouse event generated by the table.
   */
  private void jTable3MouseClicked(java.awt.event.MouseEvent evt) {// GEN-FIRST:event_jTable3MouseClicked
    int row = jTable3.getSelectedRow();
    if (row == -1)
      return;

    String idStr = jTable3.getValueAt(row, 0).toString();
    selectedMainCourseID = Long.valueOf(idStr);
    LOGGER.info("Selected Main Course ID: {}", selectedMainCourseID);

    new Thread(() -> {
      try {
        MainCourse mainCourse = productService.getMainCourseById(selectedMainCourseID);
        SwingUtilities.invokeLater(() -> {
          itemname2.setText(mainCourse.getItemFood());
          itemprice2.setText(String.valueOf(mainCourse.getFoodPrice()));
        });
      } catch (Exception ex) {
        SwingUtilities.invokeLater(
            () -> JOptionPane.showMessageDialog(null, "Error loading main course details: " + ex.getMessage()));
      }
    }).start();
  }// GEN-LAST:event_jTable3MouseClicked

  /**
   * @brief Action handler for the "Update" button in the Drinks tab.
   *
   *        Sends the modified drink data (name and price) to the backend,
   *        resets the selection and reloads the drinks table.
   *
   * @param evt [java.awt.event.ActionEvent] Event triggered by the button click.
   */
  private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jButton1ActionPerformed
    String name = itemname.getText();
    String price = itemprice.getText();

    new Thread(() -> {
      try {
        productService.updateDrink(selectedDrinkID, name, price);
        SwingUtilities.invokeLater(() -> {
          JOptionPane.showMessageDialog(this, "Drink updated successfully.");
          itemname.setText("");
          itemprice.setText("");
          selectedDrinkID = null;
          loadDrinks();
        });
      } catch (Exception ex) {
        SwingUtilities
            .invokeLater(() -> JOptionPane.showMessageDialog(this, "Error updating drink: " + ex.getMessage()));
      }
    }).start();
  }// GEN-LAST:event_jButton1ActionPerformed

  /**
   * @brief Action handler for the "Update" button in the Appetizers tab.
   *
   *        Updates the selected appetizer in the backend and refreshes the
   *        appetizers table.
   *
   * @param evt [java.awt.event.ActionEvent] Event triggered by the button click.
   */
  private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jButton4ActionPerformed
    String name = itemname1.getText();
    String price = itemprice1.getText();

    new Thread(() -> {
      try {
        productService.updateAppetizer(selectedAppetizerID, name, price);
        SwingUtilities.invokeLater(() -> {
          JOptionPane.showMessageDialog(this, "Appetizer updated successfully.");
          itemname1.setText("");
          itemprice1.setText("");
          selectedAppetizerID = null;
          loadAppetizer();
        });
      } catch (Exception ex) {
        SwingUtilities
            .invokeLater(() -> JOptionPane.showMessageDialog(this, "Error updating appetizer: " + ex.getMessage()));
      }
    }).start();
  }// GEN-LAST:event_jButton4ActionPerformed

  /**
   * @brief Action handler for the "Update" button in the MainCourse tab.
   *
   *        Updates the selected main course data in the backend and reloads
   *        the main course list.
   *
   * @param evt [java.awt.event.ActionEvent] Event triggered by the button click.
   */
  private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jButton6ActionPerformed
    String name = itemname2.getText();
    String price = itemprice2.getText();

    new Thread(() -> {
      try {
        productService.updateMainCourse(selectedMainCourseID, name, price);
        SwingUtilities.invokeLater(() -> {
          JOptionPane.showMessageDialog(this, "Main course updated successfully.");
          itemname2.setText("");
          itemprice2.setText("");
          selectedMainCourseID = null;
          loadmainCourse();
        });
      } catch (Exception ex) {
        SwingUtilities
            .invokeLater(() -> JOptionPane.showMessageDialog(this, "Error updating main course: " + ex.getMessage()));
      }
    }).start();
  }// GEN-LAST:event_jButton6ActionPerformed

  /**
   * @brief Standalone entry point for testing the AdminProducts window.
   *
   *        Sets the Nimbus look and feel if available and shows an instance
   *        of AdminProducts. In the normal application flow, this window is
   *        opened from AdminLogin after authentication.
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
    } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
      java.util.logging.Logger.getLogger(AdminProducts.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
    } 
    // </editor-fold>

    /* Create and display the form */
    java.awt.EventQueue.invokeLater(() -> new AdminProducts().setVisible(true));
  }

  // Variables declaration - do not modify//GEN-BEGIN:variables
  // Sonarqube rule java:S1450 must be ignored here as these variables are
  // auto-generated by the Form Editor and need to remain as instance variables.
  /** Text field for the drink name (Drinks tab). */
  javax.swing.JTextField itemname;
  /** Text field for the appetizer name (Appetizers tab). */
  javax.swing.JTextField itemname1;
  /** Text field for the main course name (MainCourse tab). */
  javax.swing.JTextField itemname2;
  /** Text field for the drink price (Drinks tab). */
  javax.swing.JTextField itemprice;
  /** Text field for the appetizer price (Appetizers tab). */
  javax.swing.JTextField itemprice1;
  /** Text field for the main course price (MainCourse tab). */
  javax.swing.JTextField itemprice2;
  /** Button to update an existing drink. */
  javax.swing.JButton jButton1;
  /** Button to add a new drink. */
  javax.swing.JButton jButton2;
  /** Button to go back to the admin main window. */
  javax.swing.JButton jButton3;
  /** Button to update an existing appetizer. */
  javax.swing.JButton jButton4;
  /** Button to add a new appetizer. */
  javax.swing.JButton jButton5;
  /** Button to update an existing main course. */
  javax.swing.JButton jButton6;
  /** Button to add a new main course. */
  javax.swing.JButton jButton7;
  /** Main title label ("Items Prices Update Portal"). */
  javax.swing.JLabel jLabel1;
  /** Helper label used in the MainCourse tab. */
  javax.swing.JLabel jLabel10;
  /** Section title label for the Drinks tab. */
  javax.swing.JLabel jLabel2;
  /** Helper label with hints for adding drinks. */
  javax.swing.JLabel jLabel3;
  /** Helper label with hints for updating drinks. */
  javax.swing.JLabel jLabel4;
  /** Helper label with hints for adding appetizers. */
  javax.swing.JLabel jLabel5;
  /** Section title label for the Appetizers tab. */
  javax.swing.JLabel jLabel6;
  /** Helper label with hints for updating appetizers. */
  javax.swing.JLabel jLabel7;
  /** Helper label with hints for adding main courses. */
  javax.swing.JLabel jLabel8;
  /** Section title label for the MainCourse tab. */
  javax.swing.JLabel jLabel9;
  /** Main container panel of the window. */
  javax.swing.JPanel jPanel1;
  /** Panel that contains the Drinks controls and table. */
  javax.swing.JPanel jPanel2;
  /** Panel that contains the Appetizers controls and table. */
  javax.swing.JPanel jPanel3;
  /** Panel that contains the MainCourse controls and table. */
  javax.swing.JPanel jPanel4;
  /** Scroll pane wrapping the drinks table. */
  javax.swing.JScrollPane jScrollPane1;
  /** Scroll pane wrapping the appetizers table. */
  javax.swing.JScrollPane jScrollPane2;
  /** Scroll pane wrapping the main course table. */
  javax.swing.JScrollPane jScrollPane3;
  /** Tabbed pane containing the three product categories. */
  javax.swing.JTabbedPane jTabbedPane1;
  /** Table displaying drink items. */
  javax.swing.JTable jTable1;
  /** Table displaying appetizer items. */
  javax.swing.JTable jTable2;
  /** Table displaying main course items. */
  javax.swing.JTable jTable3;
  // End of variables declaration//GEN-END:variables
}
