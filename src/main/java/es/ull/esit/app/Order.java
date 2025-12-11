package es.ull.esit.app;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.ull.esit.app.middleware.ApiClient;
import es.ull.esit.app.middleware.model.Appetizer;
import es.ull.esit.app.middleware.model.BillResult;
import es.ull.esit.app.middleware.model.Drink;
import es.ull.esit.app.middleware.model.MainCourse;
import es.ull.esit.app.middleware.service.OrderService;
import es.ull.esit.app.middleware.service.ProductService;

import java.io.PrintWriter;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

/**
 * @brief Main menu window for taking customer orders.
 *
 *        Swing window that allows the cashier to:
 *        - select quantities of drinks, appetizers and main courses.
 *        - see the items and prices loaded dynamically from the database.
 *        - calculate subtotal, VAT and total.
 *        - save a simple text receipt to disk.
 *
 *        All menu items and prices are now loaded from the backend
 *        using ProductService (instead of being hardcoded).
 */
public class Order extends javax.swing.JFrame {

  /** Primary font used in the GUI. */
  private static final String PRIMARY_FONT = "Yu Gothic UI";

  private static final String PRIMARY_FONT_LIGHT = "Yu Gothic UI Light";

  /** Service used to load products from the backend. */
  private final transient ProductService productService;

  /** Service used to calculate bill totals and generate receipt files. */
  private final transient OrderService orderService = new OrderService();

  /** Logger for this class. Replaces printStackTrace() debug output. */
  private static final Logger LOGGER = LoggerFactory.getLogger(Order.class);

  /** Table models for the three product categories. */
  private DefaultTableModel drinksModel;
  private DefaultTableModel appetizersModel;
  private DefaultTableModel mainsModel;

  /** Last calculated bill (after pressing Pay). */
  private transient BillResult lastBill;

  /** Subtotal of the order (sum of all line prices). */
  double subTotal;
  /** VAT amount (15% of the subtotal). */
  double vat;
  /** Total amount (subtotal + VAT). */
  double total;

  /** Current receipt number (used when saving receipt files). */
  int receiptNo = 1;

  /**
   * Writer used to generate the text file for the receipt (kept for
   * compatibility).
   */
  transient PrintWriter output;

  /**
   * @brief Constructor.
   *
   *        Creates the order menu window, initializes all Swing components,
   *        configures the table models and loads the menu from the backend.
   */
  public Order() {
    initComponents();

    // Initialize the Service Layer.
    ApiClient client = new ApiClient("http://localhost:8080");
    this.productService = new ProductService(client);

    // Initialize receipt number label.
    receiptNoLbl.setText("Receipt No. : " + receiptNo);

    // Configure tables and models.
    setupTables();

    // Load menu items from database via REST API.
    loadMenuFromDatabase();
  }

  /**
   * Package-private constructor used by tests to inject a fake ProductService
   * and optionally skip the asynchronous loading of the menu.
   *
   * @param productService ProductService instance to use (may be a stub in tests)
   * @param loadMenu whether to invoke loadMenuFromDatabase() (tests may pass false)
   */
  Order(es.ull.esit.app.middleware.service.ProductService productService, boolean loadMenu) {
    initComponents();
    this.productService = productService;

    // Initialize receipt number label.
    receiptNoLbl.setText("Receipt No. : " + receiptNo);

    // Configure tables and models.
    setupTables();

    if (loadMenu) {
      loadMenuFromDatabase();
    }
  }

  /**
   * Package-private convenience constructor for tests that inject a ProductService
   * and perform the normal menu loading.
   */
  Order(es.ull.esit.app.middleware.service.ProductService productService) {
    this(productService, true);
  }

  /**
   * @brief Configures the three tables (Drinks, Appetizers, Main Course).
   *
   *        Each table has four columns:
   *        - ID (not editable)
   *        - Item (not editable)
   *        - Price (not editable)
   *        - Qty (editable, quantity selected by the cashier)
   */
  private void setupTables() {
    // DRINKS
    drinksModel = new DefaultTableModel(
        new Object[] { "ID", "Item", "Price (SR)", "Qty" }, 0) {
      @Override
      public boolean isCellEditable(int row, int column) {
        // Only Qty column is editable
        return column == 3;
      }

      @Override
      public Class<?> getColumnClass(int columnIndex) {
        return switch (columnIndex) {
          case 0 -> Long.class; // ID
          case 2, 3 -> Integer.class; // Price, Qty
          default -> String.class;
        };
      }
    };
    drinksTable.setModel(drinksModel);

    // APPETIZERS
    appetizersModel = new DefaultTableModel(
        new Object[] { "ID", "Item", "Price (SR)", "Qty" }, 0) {
      @Override
      public boolean isCellEditable(int row, int column) {
        return column == 3;
      }

      @Override
      public Class<?> getColumnClass(int columnIndex) {
        return switch (columnIndex) {
          case 0 -> Long.class;
          case 2, 3 -> Integer.class;
          default -> String.class;
        };
      }
    };
    appetizersTable.setModel(appetizersModel);

    // MAIN COURSES
    mainsModel = new DefaultTableModel(
        new Object[] { "ID", "Item", "Price (SR)", "Qty" }, 0) {
      @Override
      public boolean isCellEditable(int row, int column) {
        return column == 3;
      }

      @Override
      public Class<?> getColumnClass(int columnIndex) {
        return switch (columnIndex) {
          case 0 -> Long.class;
          case 2, 3 -> Integer.class;
          default -> String.class;
        };
      }
    };
    mainsTable.setModel(mainsModel);
  }

  /**
   * @brief Loads menu data (drinks, appetizers, main courses)
   *        from the backend in a background thread.
   */
  private void loadMenuFromDatabase() {
    new Thread(() -> {
      try {
        java.util.List<Drink> drinks = productService.getAllDrinks();
        java.util.List<Appetizer> appetizers = productService.getAllAppetizers();
        java.util.List<MainCourse> mains = productService.getAllMainCourses();

        SwingUtilities.invokeLater(() -> {
          fillDrinks(drinks);
          fillAppetizers(appetizers);
          fillMains(mains);
        });

  } catch (Exception ex) {
    LOGGER.error("Error loading menu from backend", ex);
    SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(
    this,
    "Error loading menu from backend:\n" + ex.getMessage(),
    "Menu loading error",
    JOptionPane.ERROR_MESSAGE));
  }
    }).start();
  }

  /**
   * @brief Fills the drinks table model with data from the backend.
   * 
   * @param drinks [java.util.List<Drink>] List of drinks retrieved from the backend.
   */
  private void fillDrinks(java.util.List<Drink> drinks) {
    drinksModel.setRowCount(0);
    for (Drink d : drinks) {
      drinksModel.addRow(new Object[] {
          d.getDrinksId(),
          d.getItemDrinks(),
          d.getDrinksPrice(),
          0 // initial quantity
      });
    }
  }

  /**
   * @brief Fills the appetizers table model with data from the backend.
   * 
   * @param appetizers [java.util.List<Appetizer>] List of appetizers retrieved from the backend.
   */
  private void fillAppetizers(java.util.List<Appetizer> appetizers) {
    appetizersModel.setRowCount(0);
    for (Appetizer a : appetizers) {
      appetizersModel.addRow(new Object[] {
          a.getAppetizersId(),
          a.getItemAppetizers(),
          a.getAppetizersPrice(),
          0
      });
    }
  }

  /**
   * @brief Fills the main courses table model with data from the backend.
   * 
   * @param mains [java.util.List<MainCourse>] List of main courses retrieved from the backend.
   */
  private void fillMains(java.util.List<MainCourse> mains) {
    mainsModel.setRowCount(0);
    for (MainCourse m : mains) {
      mainsModel.addRow(new Object[] {
          m.getFoodId(),
          m.getItemFood(),
          m.getFoodPrice(),
          0
      });
    }
  }

  /**
   * @brief Sums the total price for a given table model.
   *
   *        For each row:
   *        - reads Price (column 2) and Qty (column 3),
   *        - if Qty > 0, accumulates (Qty * Price).
   *
   * @param model [javax.swing.table.DefaultTableModel] Table model (drinksModel, appetizersModel or mainsModel).
   * @return [double] Sum of the line totals in that model.
   */
  private double sumFromModel(DefaultTableModel model) {
    double sum = 0.0;
    for (int row = 0; row < model.getRowCount(); row++) {
      Object qtyObj = model.getValueAt(row, 3); // Qty
      Object priceObj = model.getValueAt(row, 2); // Price

      if (qtyObj == null || priceObj == null)
        continue;

      int qty;
      int price;
      try {
        qty = ((Number) qtyObj).intValue();
        price = ((Number) priceObj).intValue();
      } catch (ClassCastException e) {
        // Fallback in case something comes as String
        qty = Integer.parseInt(qtyObj.toString());
        price = Integer.parseInt(priceObj.toString());
      }

      if (qty > 0) {
        sum += qty * price;
      }
    }
    return sum;
  }

  /**
   * @brief Sets the Qty column of the given model to 0 in all rows.
   */
  private void resetQtyColumn(DefaultTableModel model) {
    for (int row = 0; row < model.getRowCount(); row++) {
      model.setValueAt(0, row, 3); // column 3 is Qty
    }
  }

  /**
   * @brief Placeholder method for loading prices from an external source.
   *
   *        Currently not used directly, as menu items are loaded in
   *        loadMenuFromDatabase(). Kept for compatibility.
   */
  void loadPrice() {
    // No-op
  }

  /**
   * @brief Initializes GUI components.
   *
   *        Generated by the Form Editor: do not modify manually.
   *        Creates and arranges the panels for Drinks, Appetizers,
   *        Main courses and the Receipt summary, as well as all labels,
   *        tables and buttons.
   */
  @SuppressWarnings("unchecked")
  // <editor-fold defaultstate="collapsed" desc="Generated
  // Code">//GEN-BEGIN:initComponents
  private void initComponents() {

    jPanel2 = new javax.swing.JPanel();
    drinksPnl = new javax.swing.JPanel();
    drinksScroll = new javax.swing.JScrollPane();
    drinksTable = new javax.swing.JTable();
    appetizerPnl = new javax.swing.JPanel();
    appetizersScroll = new javax.swing.JScrollPane();
    appetizersTable = new javax.swing.JTable();
    mainCoursePnl = new javax.swing.JPanel();
    mainsScroll = new javax.swing.JScrollPane();
    mainsTable = new javax.swing.JTable();
    jPanel1 = new javax.swing.JPanel();
    subTotalLbl = new javax.swing.JLabel();
    vatLbl = new javax.swing.JLabel();
    totalLbl = new javax.swing.JLabel();
    receiptNoLbl = new javax.swing.JLabel();
    payBtn = new javax.swing.JButton();
    newReceiptBtn = new javax.swing.JButton();
    saveReceiptBtn = new javax.swing.JButton();
    jLabel1 = new javax.swing.JLabel();
    goBackMenueBtn = new javax.swing.JButton();
    jLabel2 = new javax.swing.JLabel();

    setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
    setTitle("Menu");
    setBackground(new java.awt.Color(204, 204, 204));
    setResizable(false);

    jPanel2.setBackground(new java.awt.Color(248, 244, 230));

    drinksPnl.setBackground(new java.awt.Color(248, 244, 230));
    drinksPnl.setBorder(javax.swing.BorderFactory.createTitledBorder(
        new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true), "Drinks",
        javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.TOP,
        new java.awt.Font(PRIMARY_FONT, 0, 18))); // NOI18N
    drinksPnl.setPreferredSize(new java.awt.Dimension(260, 278));

    drinksTable.setFont(new java.awt.Font(PRIMARY_FONT, 0, 14)); // NOI18N
    drinksTable.setModel(new javax.swing.table.DefaultTableModel(
        new Object[][] {

        },
        new String[] {

        }));
    drinksTable.getTableHeader().setReorderingAllowed(false);
    drinksScroll.setViewportView(drinksTable);

    javax.swing.GroupLayout drinksPnlLayout = new javax.swing.GroupLayout(drinksPnl);
    drinksPnl.setLayout(drinksPnlLayout);
    drinksPnlLayout.setHorizontalGroup(
        drinksPnlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(drinksPnlLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(drinksScroll, javax.swing.GroupLayout.DEFAULT_SIZE, 281, Short.MAX_VALUE)
                .addContainerGap()));
    drinksPnlLayout.setVerticalGroup(
        drinksPnlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(drinksPnlLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(drinksScroll, javax.swing.GroupLayout.DEFAULT_SIZE, 186, Short.MAX_VALUE)
                .addContainerGap()));

    appetizerPnl.setBackground(new java.awt.Color(248, 244, 230));
    appetizerPnl.setBorder(javax.swing.BorderFactory.createTitledBorder(
        new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true), "Appetizers",
        javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.TOP,
        new java.awt.Font(PRIMARY_FONT, 0, 18))); // NOI18N
    appetizerPnl.setPreferredSize(new java.awt.Dimension(260, 278));

    appetizersTable.setFont(new java.awt.Font(PRIMARY_FONT, 0, 14)); // NOI18N
    appetizersTable.setModel(new javax.swing.table.DefaultTableModel(
        new Object[][] {

        },
        new String[] {

        }));
    appetizersTable.getTableHeader().setReorderingAllowed(false);
    appetizersScroll.setViewportView(appetizersTable);

    javax.swing.GroupLayout appetizerPnlLayout = new javax.swing.GroupLayout(appetizerPnl);
    appetizerPnl.setLayout(appetizerPnlLayout);
    appetizerPnlLayout.setHorizontalGroup(
        appetizerPnlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(appetizerPnlLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(appetizersScroll, javax.swing.GroupLayout.DEFAULT_SIZE, 368, Short.MAX_VALUE)
                .addContainerGap()));
    appetizerPnlLayout.setVerticalGroup(
        appetizerPnlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(appetizerPnlLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(appetizersScroll, javax.swing.GroupLayout.DEFAULT_SIZE, 186, Short.MAX_VALUE)
                .addContainerGap()));

    mainCoursePnl.setBackground(new java.awt.Color(248, 244, 230));
    mainCoursePnl.setBorder(javax.swing.BorderFactory.createTitledBorder(
        new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true), "Main Course",
        javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.TOP,
        new java.awt.Font(PRIMARY_FONT, 0, 18))); // NOI18N
    mainCoursePnl.setPreferredSize(new java.awt.Dimension(260, 278));

    mainsTable.setFont(new java.awt.Font(PRIMARY_FONT, 0, 14)); // NOI18N
    mainsTable.setModel(new javax.swing.table.DefaultTableModel(
        new Object[][] {

        },
        new String[] {

        }));
    mainsTable.getTableHeader().setReorderingAllowed(false);
    mainsScroll.setViewportView(mainsTable);

    javax.swing.GroupLayout mainCoursePnlLayout = new javax.swing.GroupLayout(mainCoursePnl);
    mainCoursePnl.setLayout(mainCoursePnlLayout);
    mainCoursePnlLayout.setHorizontalGroup(
        mainCoursePnlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mainCoursePnlLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(mainsScroll, javax.swing.GroupLayout.DEFAULT_SIZE, 427, Short.MAX_VALUE)
                .addContainerGap()));
    mainCoursePnlLayout.setVerticalGroup(
        mainCoursePnlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mainCoursePnlLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(mainsScroll, javax.swing.GroupLayout.DEFAULT_SIZE, 186, Short.MAX_VALUE)
                .addContainerGap()));

    jPanel1.setBackground(new java.awt.Color(248, 244, 230));
    jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(
        new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true), "Receipt",
        javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.TOP,
        new java.awt.Font(PRIMARY_FONT, 0, 14))); // NOI18N

    subTotalLbl.setFont(new java.awt.Font(PRIMARY_FONT_LIGHT, 0, 18)); // NOI18N
    subTotalLbl.setText("SubTotal: 0.0 SR");

    vatLbl.setFont(new java.awt.Font(PRIMARY_FONT_LIGHT, 0, 18)); // NOI18N
    vatLbl.setText("VAT included: 0.0 SR");

    totalLbl.setFont(new java.awt.Font(PRIMARY_FONT_LIGHT, 0, 18)); // NOI18N
    totalLbl.setText("Total: 0.0 SR");

    receiptNoLbl.setFont(new java.awt.Font(PRIMARY_FONT_LIGHT, 0, 14)); // NOI18N
    receiptNoLbl.setText("Receipt No. : 0");

    javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
    jPanel1.setLayout(jPanel1Layout);
    jPanel1Layout.setHorizontalGroup(
        jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(subTotalLbl)
                    .addComponent(vatLbl)
                    .addComponent(totalLbl)
                    .addComponent(receiptNoLbl))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
    jPanel1Layout.setVerticalGroup(
        jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(subTotalLbl)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(vatLbl)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(totalLbl)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 10, Short.MAX_VALUE)
                .addComponent(receiptNoLbl)));

    payBtn.setFont(new java.awt.Font("Lucida Grande", 1, 18)); // NOI18N
    payBtn.setText("Pay");
    payBtn.addActionListener(this::payBtnActionPerformed);

    newReceiptBtn.setFont(new java.awt.Font(PRIMARY_FONT, 1, 14)); // NOI18N
    newReceiptBtn.setText("New Receipt");
    newReceiptBtn.addActionListener(this::newReceiptBtnActionPerformed);

    saveReceiptBtn.setFont(new java.awt.Font(PRIMARY_FONT, 1, 14)); // NOI18N
    saveReceiptBtn.setText("Save Receipt");
    saveReceiptBtn.addActionListener(this::saveReceiptBtnActionPerformed);

    jLabel1.setBackground(new java.awt.Color(255, 153, 0));
    jLabel1.setFont(new java.awt.Font(PRIMARY_FONT, 1, 36)); // NOI18N
    jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
    jLabel1.setText("BLACK PLATE MENU");

    goBackMenueBtn.setFont(new java.awt.Font(PRIMARY_FONT, 1, 14)); // NOI18N
    goBackMenueBtn.setText("Go Back");
    goBackMenueBtn.addActionListener(this::goBackMenueBtnActionPerformed);

    jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ui/screenshot.png"))); // NOI18N

    javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
    jPanel2.setLayout(jPanel2Layout);
    jPanel2Layout.setHorizontalGroup(
        jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(40, 40, 40)
                        .addComponent(drinksPnl, javax.swing.GroupLayout.PREFERRED_SIZE, 305,
                            javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(appetizerPnl, javax.swing.GroupLayout.PREFERRED_SIZE, 392,
                            javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(mainCoursePnl, javax.swing.GroupLayout.PREFERRED_SIZE, 451,
                            javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(251, 251, 251)
                        .addComponent(jLabel2)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 433,
                            javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(16, 16, 16)
                        .addComponent(goBackMenueBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 130,
                            javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(147, 147, 147)
                        .addComponent(payBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 180,
                            javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(newReceiptBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 220,
                                javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(saveReceiptBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 220,
                                javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(28, 28, 28)
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE,
                            javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(30, Short.MAX_VALUE)));
    jPanel2Layout.setVerticalGroup(
        jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap(16, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.TRAILING,
                        javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(40, 40, 40)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(appetizerPnl, javax.swing.GroupLayout.DEFAULT_SIZE, 230, Short.MAX_VALUE)
                    .addComponent(mainCoursePnl, javax.swing.GroupLayout.DEFAULT_SIZE, 230, Short.MAX_VALUE)
                    .addComponent(drinksPnl, javax.swing.GroupLayout.DEFAULT_SIZE, 230, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(goBackMenueBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 40,
                        javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(newReceiptBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 30,
                            javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(saveReceiptBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 29,
                            javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(payBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 70,
                        javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE,
                        javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(30, 30, 30)));

    javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
    getContentPane().setLayout(layout);
    layout.setHorizontalGroup(
        layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE,
                Short.MAX_VALUE));
    layout.setVerticalGroup(
        layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE,
                    Short.MAX_VALUE)
                .addContainerGap()));

    pack();
    setLocationRelativeTo(null);
  }// </editor-fold>//GEN-END:initComponents

  /**
   * @brief Calculates subtotal, VAT and total and shows a confirmation dialog.
   *
   *        Steps:
   *        - Sums all line prices from the three tables.
   *        - Uses OrderService to compute BillResult (subtotal, VAT, total).
   *        - Updates the labels in the Receipt panel.
   *        - If no items were selected (sum == 0), shows an information dialog.
   *        - Otherwise, shows a "Paid successfully" dialog.
   *
   * @param evt [java.awt.event.ActionEvent] Event triggered by clicking the "Pay"
   *            button.
   */
  private void payBtnActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_payBtnActionPerformed
    double itemsTotal = 0.0;

    itemsTotal += sumFromModel(drinksModel);
    itemsTotal += sumFromModel(appetizersModel);
    itemsTotal += sumFromModel(mainsModel);

    if (itemsTotal == 0.0) {
      JOptionPane.showMessageDialog(
          this,
          "Please select at least one item (Qty > 0).",
          "No items selected",
          JOptionPane.INFORMATION_MESSAGE);
      return;
    }

    // Calculate bill using the dedicated service
    lastBill = orderService.calculateBill(itemsTotal);

    subTotal = lastBill.getSubTotal();
    vat = lastBill.getVat();
    total = lastBill.getTotal();

    subTotalLbl.setText("SubTotal: " + subTotal + " SR");
    vatLbl.setText("VAT included: " + vat + " SR");
    totalLbl.setText("Total: " + total + " SR");

    JOptionPane.showMessageDialog(this, "Paid successfully");
  }// GEN-LAST:event_payBtnActionPerformed

  /**
   * @brief Saves the current bill information to a text file through
   *        OrderService.
   *
   *        The file is created under the "receipts" directory with the name:
   *        - "billNo.<receiptNo>.txt"
   *
   *        If there is no calculated bill (lastBill == null or total == 0),
   *        an information dialog is shown and no file is created.
   *
   * @param evt [java.awt.event.ActionEvent] Event triggered by clicking the "Save
   *            Receipt" button.
   */
  private void saveReceiptBtnActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_saveReceiptBtnActionPerformed
    try {
      if (lastBill == null || lastBill.getTotal() == 0.0) {
        JOptionPane.showMessageDialog(
            this,
            "There is no paid bill to save.\nPlease press Pay first.",
            "No bill",
            JOptionPane.INFORMATION_MESSAGE);
        return;
      }

      orderService.generateReceiptFile(receiptNo, lastBill);

      JOptionPane.showMessageDialog(
          this,
          "Receipt number: " + receiptNo + " has been saved successfully.",
          "Receipt saved",
          JOptionPane.INFORMATION_MESSAGE);

    } catch (Exception ex) {
      LOGGER.error("Error saving receipt", ex);
      JOptionPane.showMessageDialog(
          this,
          "Error saving receipt:\n" + ex.getMessage(),
          "Error",
          JOptionPane.ERROR_MESSAGE);
    }
  }// GEN-LAST:event_saveReceiptBtnActionPerformed

  /**
   * @brief Starts a new receipt and resets the form to its initial state.
   *
   *        Only works when the current total is not zero. If so, it:
   *        - resets Qty column to 0 in all tables,
   *        - resets subtotal, VAT and total labels,
   *        - clears internal subtotal, VAT and total variables,
   *        - clears lastBill,
   *        - increments the receipt number and updates its label.
   *
   * @param evt [java.awt.event.ActionEvent] Event triggered by clicking the "New
   *            Receipt" button.
   */
  private void newReceiptBtnActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_newReceiptBtnActionPerformed
    if (total == 0.0) {
      // Nothing to reset
      return;
    }

    resetQtyColumn(drinksModel);
    resetQtyColumn(appetizersModel);
    resetQtyColumn(mainsModel);

    subTotal = 0.0;
    vat = 0.0;
    total = 0.0;
    lastBill = null;

    subTotalLbl.setText("SubTotal: 0.0 SR");
    vatLbl.setText("VAT included: 0.0 SR");
    totalLbl.setText("Total: 0.0 SR");

    receiptNo++;
    receiptNoLbl.setText("Receipt No. : " + receiptNo);
  }// GEN-LAST:event_newReceiptBtnActionPerformed

  /**
   * @brief Returns to the Login window.
   *
   *        Closes the current Order window and opens a new instance
   *        of the Login form.
   *
   * @param evt [java.awt.event.ActionEvent] Event triggered by clicking the "Go
   *            Back" button.
   */
  private void goBackMenueBtnActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_goBackMenueBtnActionPerformed
    this.dispose();
    new Login().setVisible(true);
  }// GEN-LAST:event_goBackMenueBtnActionPerformed

  /**
   * @brief Standalone entry point for testing the Order window.
   *
   *        Sets the Nimbus look and feel if available and displays a new
   *        instance of Order. In the normal application flow, this window
   *        is opened after a successful login.
   *
   * @param args [String[]] Command line arguments (not used).
   */
  public static void main(String[] args) {
    /* Set the Nimbus look and feel */
    // <editor-fold defaultstate="collapsed" desc=" Look and feel setting code
    // (optional) ">
    /*
     * If Nimbus (introduced in Java SE 6) is not available, stay with the default
     * look and feel.
     * For details see
     * http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html
     */
    try {
      for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
        if ("Nimbus".equals(info.getName())) {
          javax.swing.UIManager.setLookAndFeel(info.getClassName());
          break;
        }
      }
    } catch (ClassNotFoundException | javax.swing.UnsupportedLookAndFeelException |
        InstantiationException | IllegalAccessException ex) {
      java.util.logging.Logger.getLogger(Order.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
    } 
    // </editor-fold>

    /* Create and display the form */
    java.awt.EventQueue.invokeLater(() -> new Order().setVisible(true));
  }

  // Variables declaration - do not modify//GEN-BEGIN:variables
  // Sonarqube rule java:S1450 must be ignored here as these variables are
  // auto-generated by the Form Editor and need to remain as instance variables.
  /** Panel that groups all appetizer items and controls. */
  private javax.swing.JPanel appetizerPnl;
  /** Panel that groups all drink items and controls. */
  private javax.swing.JPanel drinksPnl;
  /** Panel that groups all main course items and controls. */
  private javax.swing.JPanel mainCoursePnl;
  /** Table for appetizers items. */
  private javax.swing.JTable appetizersTable;
  /** Scroll pane for appetizers table. */
  private javax.swing.JScrollPane appetizersScroll;
  /** Table for drinks items. */
  private javax.swing.JTable drinksTable;
  /** Scroll pane for drinks table. */
  private javax.swing.JScrollPane drinksScroll;
  /** Button to go back to the Login window. */
  private javax.swing.JButton goBackMenueBtn;
  /** Title label "BLACK PLATE MENU". */
  private javax.swing.JLabel jLabel1;
  /** Logo / screenshot label on the top left. */
  private javax.swing.JLabel jLabel2;
  /** Panel that shows subtotal, VAT, total and receipt number. */
  private javax.swing.JPanel jPanel1;
  /** Main container panel of the Order window. */
  private javax.swing.JPanel jPanel2;
  /** Table for main course items. */
  private javax.swing.JTable mainsTable;
  /** Scroll pane for main course table. */
  private javax.swing.JScrollPane mainsScroll;
  /** Button to start a new receipt. */
  private javax.swing.JButton newReceiptBtn;
  /** Button to calculate and confirm payment. */
  private javax.swing.JButton payBtn;
  /** Label that shows the current receipt number. */
  private javax.swing.JLabel receiptNoLbl;
  /** Button to save the current receipt to a file. */
  private javax.swing.JButton saveReceiptBtn;
  /** Label that displays the subtotal amount. */
  private javax.swing.JLabel subTotalLbl;
  /** Label that displays the total amount. */
  private javax.swing.JLabel totalLbl;
  /** Label that displays the VAT amount. */
  private javax.swing.JLabel vatLbl;
  // End of variables declaration//GEN-END:variables
}
