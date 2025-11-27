
package es.ull.esit.app;

import es.ull.esit.app.middleware.ApiClient;
import es.ull.esit.app.middleware.model.Appetizer;
import es.ull.esit.app.middleware.model.Drink;
import es.ull.esit.app.middleware.model.MainCourse;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import java.util.logging.Logger;

/**
 * @file AdminProducts.java
 * @brief Administrative screen to manage products and prices.
 *
 *        Allows listing drinks, appetizers and main courses retrieved from the
 *        REST middleware and performing basic CRUD operations (add/update).
 */
public class AdminProducts extends javax.swing.JFrame {
  /** Column name constants used in tables. */
  static final String COL_ID = "ID";
  static final String COL_ITEM_NAME = "Item Name";
  static final String COL_ITEM_PRICE = "Item Price";
  /** Font name constants used in the UI. */
  static final String PRINCIPAL_FONT_NAME = "Yu Gothic UI";
  static final String SECONDARY_FONT_NAME = "Thonburi";

  /** HTTP client for backend calls. */
  private transient ApiClient apiClient;

  /** Table models used to display product lists. */
  DefaultTableModel modelDrink;
  DefaultTableModel modelappetizers;
  DefaultTableModel modelmaincourse;

  /** Common headers used by the tables. */
  String[] columnNames = { COL_ID, COL_ITEM_NAME, COL_ITEM_PRICE };
  /** Currently selected IDs in each table (if any). */
  Long selectedDrinkID;
  Long selectedAppetizerID;
  Long selectedMainCourseID;

  /** Logger for the class. */
  private transient Logger logger = Logger.getLogger(getClass().getName());

  /**
     * Creates new form AdminProducts
     */
    public AdminProducts() {
        initComponents();
        apiClient = new ApiClient("http://localhost:8080"); // Backend URL
        
        modelDrink = new DefaultTableModel();
        modelDrink.setColumnIdentifiers(columnNames);
        modelappetizers = new DefaultTableModel();
        modelappetizers.setColumnIdentifiers(columnNames);
        modelmaincourse = new DefaultTableModel();
        modelmaincourse.setColumnIdentifiers(columnNames);
        
        loadDrinks();
        loadAppetizer();
        loadmainCourse();
    }

  /**
   * @brief Loads the list of drinks from the backend and populates the table.
   *
   *        Catches exceptions and shows a dialog in case of network or backend
   *        error.
   */
  void loadDrinks() {
    try {
      List<Drink> drinks = apiClient.getAllDrinks();
      for (Drink drink : drinks) {
        modelDrink.addRow(new Object[] {
            drink.getDrinksId(),
            drink.getItemDrinks(),
            drink.getDrinksPrice()
        });
      }
      jTable1.setModel(modelDrink);
    } catch (Exception ex) {
      JOptionPane.showMessageDialog(null, "Error loading drinks: " + ex.getMessage());
    }
  }

  /**
   * @brief Loads the list of appetizers from the backend and populates the table.
   */
  void loadAppetizer() {
    try {
      List<Appetizer> appetizers = apiClient.getAllAppetizers();
      for (Appetizer appetizer : appetizers) {
        modelappetizers.addRow(new Object[] {
            appetizer.getAppetizersId(),
            appetizer.getItemAppetizers(),
            appetizer.getAppetizersPrice()
        });
      }
      jTable2.setModel(modelappetizers);
    } catch (Exception ex) {
      JOptionPane.showMessageDialog(null, "Error loading appetizers: " + ex.getMessage());
    }
  }

  /**
   * @brief Loads the list of main courses from the backend and populates the
   *        table.
   */
  void loadmainCourse() {
    try {
      List<MainCourse> mainCourses = apiClient.getAllMainCourses();
      for (MainCourse mainCourse : mainCourses) {
        modelmaincourse.addRow(new Object[] {
            mainCourse.getFoodId(),
            mainCourse.getItemFood(),
            mainCourse.getFoodPrice()
        });
      }
      jTable3.setModel(modelmaincourse);
    } catch (Exception ex) {
      JOptionPane.showMessageDialog(null, "Error loading main courses: " + ex.getMessage());
    }
  }

  /**
   * @brief Handler for the "Add" button (Drinks).
   *
   *        Creates a Drink object from the form data and sends it to the backend
   *        using ApiClient. After creating the resource the table is refreshed.
   *
   * @param evt action event
   */
  private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {
    try {
      Drink newDrink = new Drink(null, itemname.getText(),
          Integer.parseInt(itemprice.getText()), null);
      apiClient.createDrink(newDrink);
      JOptionPane.showMessageDialog(null, "Drink Added Successfully.");

      // Refresh table
      DefaultTableModel dtm = (DefaultTableModel) jTable1.getModel();
      dtm.setRowCount(0);
      loadDrinks();
    } catch (Exception ex) {
      JOptionPane.showMessageDialog(null, "Error adding drink: " + ex.getMessage());
    }
  }

  /**
   * @brief This method is called from within the constructor to initialize the
   *        form.
   *        WARNING: Do NOT modify this code. The content of this method is always
   *        regenerated by the Form Editor.
   *        @SuppressWarnings("unchecked")
   */
  @SuppressWarnings("unchecked")
  // <editor-fold defaultstate="collapsed" desc="Generated
  // Code">//GEN-BEGIN:initComponents
  private void initComponents() {

    javax.swing.JPanel jPanel1 = new javax.swing.JPanel();
    javax.swing.JLabel jLabel1 = new javax.swing.JLabel();
    javax.swing.JTabbedPane jTabbedPane1 = new javax.swing.JTabbedPane();
    javax.swing.JPanel jPanel2 = new javax.swing.JPanel();
    itemname = new javax.swing.JTextField();
    itemprice = new javax.swing.JTextField();
    javax.swing.JScrollPane jScrollPane1 = new javax.swing.JScrollPane();
    jTable1 = new javax.swing.JTable();
    javax.swing.JButton jButton1 = new javax.swing.JButton();
    javax.swing.JButton jButton2 = new javax.swing.JButton();
    javax.swing.JLabel jLabel2 = new javax.swing.JLabel();
    javax.swing.JLabel jLabel3 = new javax.swing.JLabel();
    javax.swing.JLabel jLabel4 = new javax.swing.JLabel();
    javax.swing.JPanel jPanel3 = new javax.swing.JPanel();
    javax.swing.JLabel jLabel5 = new javax.swing.JLabel();
    javax.swing.JLabel jLabel6 = new javax.swing.JLabel();
    javax.swing.JLabel jLabel7 = new javax.swing.JLabel();
    javax.swing.JScrollPane jScrollPane2 = new javax.swing.JScrollPane();
    jTable2 = new javax.swing.JTable();
    itemname1 = new javax.swing.JTextField();
    itemprice1 = new javax.swing.JTextField();
    javax.swing.JButton jButton4 = new javax.swing.JButton();
    javax.swing.JButton jButton5 = new javax.swing.JButton();
    javax.swing.JPanel jPanel4 = new javax.swing.JPanel();
    javax.swing.JLabel jLabel8 = new javax.swing.JLabel();
    javax.swing.JLabel jLabel9 = new javax.swing.JLabel();
    javax.swing.JLabel jLabel10 = new javax.swing.JLabel();
    javax.swing.JScrollPane jScrollPane3 = new javax.swing.JScrollPane();
    jTable3 = new javax.swing.JTable();
    itemname2 = new javax.swing.JTextField();
    itemprice2 = new javax.swing.JTextField();
    javax.swing.JButton jButton6 = new javax.swing.JButton();
    javax.swing.JButton jButton7 = new javax.swing.JButton();
    javax.swing.JButton jButton3 = new javax.swing.JButton();

    setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
    setTitle("Admin");
    setResizable(false);

    jPanel1.setBackground(new java.awt.Color(248, 244, 230));

    jLabel1.setBackground(new java.awt.Color(255, 153, 0));
    jLabel1.setFont(new java.awt.Font(PRINCIPAL_FONT_NAME, 1, 36)); // NOI18N
    jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
    jLabel1.setText("Items Prices Update Portal");

    jTabbedPane1.setBackground(new java.awt.Color(248, 244, 230));
    jTabbedPane1.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));
    jTabbedPane1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
    jTabbedPane1.setFont(new java.awt.Font(PRINCIPAL_FONT_NAME, 1, 18)); // NOI18N

    jPanel2.setBackground(new java.awt.Color(248, 244, 230));

    itemname.setFont(new java.awt.Font(PRINCIPAL_FONT_NAME, 0, 18)); // NOI18N
    itemname.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
    itemname.setBorder(javax.swing.BorderFactory.createTitledBorder(
        new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true), COL_ITEM_NAME,
        javax.swing.border.TitledBorder.LEFT, javax.swing.border.TitledBorder.TOP,
        new java.awt.Font(FONT_NAME, 0, 18))); // NOI18N

    itemprice.setFont(new java.awt.Font(PRINCIPAL_FONT_NAME, 0, 18)); // NOI18N
    itemprice.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
    itemprice.setBorder(javax.swing.BorderFactory.createTitledBorder(
        new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true), COL_ITEM_PRICE,
        javax.swing.border.TitledBorder.LEFT, javax.swing.border.TitledBorder.TOP,
        new java.awt.Font(FONT_NAME, 0, 18))); // NOI18N
    jTable1.setFont(new java.awt.Font(PRINCIPAL_FONT_NAME, 0, 14)); // NOI18N
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
      /**
       * @brief Handler for clicks on the drinks table.
       *
       *        When a row is clicked, the ID of the selected drink is captured
       *        for future update operations.
       *
       * @param evt mouse event generated by the click.
       * @return void
       */
      @Override
      public void mouseClicked(java.awt.event.MouseEvent evt) {
        jTable1MouseClicked(evt);
      }
    });
    jTable1.addKeyListener(new java.awt.event.KeyAdapter() {
      /**
       * @brief Handler for key presses on the drinks table.
       *
       *        Allows capturing the ID of the selected drink when navigating
       *        with the keyboard.
       *
       * @param evt key event generated by the user's action.
       * @return void
       */
      @Override
      public void keyPressed(java.awt.event.KeyEvent evt) {
        jTable1KeyPressed(evt);
      }
    });
    jScrollPane1.setViewportView(jTable1);

    jButton1.setBackground(new java.awt.Color(255, 255, 255));
    jButton1.setFont(new java.awt.Font(SECONDARY_FONT_NAME, 0, 18)); // NOI18N
    jButton1.setText("Update");
    jButton1.addActionListener(this::jButton1ActionPerformed);

    jButton2.setBackground(new java.awt.Color(255, 255, 255));
    jButton2.setFont(new java.awt.Font(SECONDARY_FONT_NAME, 0, 18)); // NOI18N
    jButton2.setText("Add");
    jButton2.addActionListener(this::jButton2ActionPerformed);

    jLabel2.setBackground(new java.awt.Color(255, 153, 0));
    jLabel2.setFont(new java.awt.Font(PRINCIPAL_FONT_NAME, 1, 25)); // NOI18N
    jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
    jLabel2.setText("Available Drinks");

    jLabel3.setFont(new java.awt.Font(PRINCIPAL_FONT_NAME, 0, 14)); // NOI18N
    jLabel3.setText("Enter new drink information carefully to add.");

    jLabel4.setFont(new java.awt.Font(PRINCIPAL_FONT_NAME, 0, 14)); // NOI18N
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

    jLabel5.setFont(new java.awt.Font(PRINCIPAL_FONT_NAME, 0, 14)); // NOI18N
    jLabel5.setText("Enter new appetizer information carefully to add.");

    jLabel6.setBackground(new java.awt.Color(255, 153, 0));
    jLabel6.setFont(new java.awt.Font(PRINCIPAL_FONT_NAME, 1, 25)); // NOI18N
    jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
    jLabel6.setText("Available Appetizer");

    jLabel7.setFont(new java.awt.Font(PRINCIPAL_FONT_NAME, 0, 14)); // NOI18N
    jLabel7.setText("Please select the appetizer to update the price.");

    jTable2.setFont(new java.awt.Font(PRINCIPAL_FONT_NAME, 0, 14)); // NOI18N
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
      /**
       * @brief Handler for clicks on the appetizers table.
       *
       *        When a row is clicked, the ID of the selected appetizer is captured
       *        for future update operations.
       *
       * @param evt mouse event generated by the click.
       * @return void
       */
      @Override
      public void mouseClicked(java.awt.event.MouseEvent evt) {
        jTable2MouseClicked(evt);
      }
    });
    jScrollPane2.setViewportView(jTable2);

    itemname1.setFont(new java.awt.Font(PRINCIPAL_FONT_NAME, 0, 18)); // NOI18N
    itemname1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
    itemname1.setBorder(javax.swing.BorderFactory.createTitledBorder(
        new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true), COL_ITEM_NAME,
        javax.swing.border.TitledBorder.LEFT, javax.swing.border.TitledBorder.TOP,
        new java.awt.Font(PRINCIPAL_FONT_NAME, 0, 18))); // NOI18N

    itemprice1.setFont(new java.awt.Font(PRINCIPAL_FONT_NAME, 0, 18)); // NOI18N
    itemprice1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
    itemprice1.setBorder(javax.swing.BorderFactory.createTitledBorder(
        new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true), COL_ITEM_PRICE,
        javax.swing.border.TitledBorder.LEFT, javax.swing.border.TitledBorder.TOP,
        new java.awt.Font(PRINCIPAL_FONT_NAME, 0, 18))); // NOI18N
    jButton4.setBackground(new java.awt.Color(0, 51, 204));
    jButton4.setFont(new java.awt.Font(SECONDARY_FONT_NAME, 0, 18)); // NOI18N
    jButton4.setText("Update");
    jButton4.addActionListener(this::jButton4ActionPerformed);

    jButton5.setBackground(new java.awt.Color(0, 153, 0));
    jButton5.setFont(new java.awt.Font(SECONDARY_FONT_NAME, 0, 18)); // NOI18N
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
    jPanel4.setFont(new java.awt.Font(PRINCIPAL_FONT_NAME, 0, 18)); // NOI18N

    jLabel8.setBackground(new java.awt.Color(248, 244, 230));
    jLabel8.setFont(new java.awt.Font(PRINCIPAL_FONT_NAME, 0, 14)); // NOI18N
    jLabel8.setText("Enter new item information carefully to add.");

    jLabel9.setBackground(new java.awt.Color(255, 153, 0));
    jLabel9.setFont(new java.awt.Font(PRINCIPAL_FONT_NAME, 1, 25)); // NOI18N
    jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
    jLabel9.setText("Avaliable MainCourse");

    jLabel10.setFont(new java.awt.Font(PRINCIPAL_FONT_NAME, 0, 14)); // NOI18N
    jLabel10.setText("Please select the item to update the price.");

    jTable3.setFont(new java.awt.Font(PRINCIPAL_FONT_NAME, 0, 14)); // NOI18N
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
      /**
       * @brief Handler for clicks on the main course table.
       *
       *        When a row is clicked, the ID of the selected main course is
       *        captured for subsequent update operations.
       *
       * @param evt mouse event generated by the click.
       * @return void
       */
      @Override
      public void mouseClicked(java.awt.event.MouseEvent evt) {
        jTable3MouseClicked(evt);
      }
    });
    jScrollPane3.setViewportView(jTable3);

    itemname2.setFont(new java.awt.Font(PRINCIPAL_FONT_NAME, 0, 18)); // NOI18N
    itemname2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
    itemname2.setBorder(javax.swing.BorderFactory.createTitledBorder(
        new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true), COL_ITEM_NAME,
        javax.swing.border.TitledBorder.LEFT, javax.swing.border.TitledBorder.TOP,
        new java.awt.Font(PRINCIPAL_FONT_NAME, 0, 18))); // NOI18N

    itemprice2.setFont(new java.awt.Font(PRINCIPAL_FONT_NAME, 0, 18)); // NOI18N
    itemprice2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
    itemprice2.setBorder(javax.swing.BorderFactory.createTitledBorder(
        new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true), COL_ITEM_PRICE,
        javax.swing.border.TitledBorder.LEFT, javax.swing.border.TitledBorder.TOP,
        new java.awt.Font(PRINCIPAL_FONT_NAME, 0, 18))); // NOI18N
    jButton6.setBackground(new java.awt.Color(0, 51, 204));
    jButton6.setFont(new java.awt.Font(SECONDARY_FONT_NAME, 0, 18)); // NOI18N
    jButton6.setText("Update");
    jButton6.addActionListener(this::jButton6ActionPerformed);

    jButton7.setBackground(new java.awt.Color(0, 153, 0));
    jButton7.setFont(new java.awt.Font(SECONDARY_FONT_NAME, 0, 18)); // NOI18N
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
    jButton3.setFont(new java.awt.Font(PRINCIPAL_FONT_NAME, 1, 18)); // NOI18N
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
   * @brief Handler for the "Add" button (Drinks).
   *
   *        Creates a Drink object from the form data and sends it to the backend
   *        using ApiClient. After creating the resource the table is refreshed.
   *
   * @param evt action event
   */
  private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jButton3ActionPerformed

    new AdminLogin().setVisible(true);
  }// GEN-LAST:event_jButton3ActionPerformed

  /**
   * @brief Handler for the "Add" button (Appetizers).
   *
   *        Creates an Appetizer object from the form data and sends it to the
   *        backend using ApiClient. After creating the resource the table is
   *        refreshed.
   *
   * @param evt action event
   */
  private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jButton5ActionPerformed
    try {
      Appetizer newAppetizer = new Appetizer(null, itemname1.getText(),
          Integer.parseInt(itemprice1.getText()), null);
      apiClient.createAppetizer(newAppetizer);
      JOptionPane.showMessageDialog(null, "Appetizer Added Successfully.");

      // Refresh table
      DefaultTableModel dtm = (DefaultTableModel) jTable2.getModel();
      dtm.setRowCount(0);
      loadAppetizer();
    } catch (Exception ex) {
      JOptionPane.showMessageDialog(null, "Error adding appetizer: " + ex.getMessage());
    }
  }// GEN-LAST:event_jButton5ActionPerformed

  /**
   * @brief Handler for the "Add" button (MainCourse).
   *
   *        Creates a MainCourse object from the form data and sends it to the
   *        backend using ApiClient. After creating the resource the table is
   *        refreshed.
   *
   * @param evt action event
   */
  private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jButton7ActionPerformed
    try {
      MainCourse newMainCourse = new MainCourse(null, itemname2.getText(),
          Integer.parseInt(itemprice2.getText()), null);
      apiClient.createMainCourse(newMainCourse);
      JOptionPane.showMessageDialog(null, "Main Course Added Successfully.");

      // Refresh table
      DefaultTableModel dtm = (DefaultTableModel) jTable3.getModel();
      dtm.setRowCount(0);
      loadmainCourse();
    } catch (Exception ex) {
      JOptionPane.showMessageDialog(null, "Error adding main course: " + ex.getMessage());
    }
  }// GEN-LAST:event_jButton7ActionPerformed

  /**
   * @brief Handler for key presses on the drinks table.
   *
   *        Allows capturing the ID of the selected drink when navigating with the
   *        keyboard.
   *
   * @return void
   */
  private void jTable1KeyPressed() {// GEN-FIRST:event_jTable1KeyPressed
    // Keep as is - no changes needed
  }// GEN-LAST:event_jTable1KeyPressed

  /**
   * @brief Handler for clicks on the drinks table.
   *
   *        When a row is clicked, the ID of the selected drink is captured for
   *        subsequent update operations.
   *
   * @return void
   */
  private void jTable1MouseClicked() {// GEN-FIRST:event_jTable1MouseClicked
    String selectedIDKey = jTable1.getValueAt(jTable1.getSelectedRow(), 0).toString();
    selectedDrinkID = Long.valueOf(selectedIDKey);
    logger.info("You select id " + selectedDrinkID + " of Drink to update.");

    try {
      Drink drink = apiClient.getDrinkById(selectedDrinkID);
      itemname.setText(drink.getItemDrinks());
      itemprice.setText(String.valueOf(drink.getDrinksPrice()));
    } catch (Exception ex) {
      JOptionPane.showMessageDialog(null, "Error loading drink: " + ex.getMessage());
    }
  }// GEN-LAST:event_jTable1MouseClicked

  /**
   * @brief Handler for clicks on the appetizers table.
   *
   *        When a row is clicked, the ID of the selected appetizer is captured
   *        for
   *        subsequent update operations.
   *
   * @return void
   */
  private void jTable2MouseClicked() {// GEN-FIRST:event_jTable2MouseClicked
    String selectedIDKey = jTable2.getValueAt(jTable2.getSelectedRow(), 0).toString();
    selectedAppetizerID = Long.valueOf(selectedIDKey);
    logger.info("You select id " + selectedAppetizerID + " of appetizers to update.");

    try {
      Appetizer appetizer = apiClient.getAppetizerById(selectedAppetizerID);
      itemname1.setText(appetizer.getItemAppetizers());
      itemprice1.setText(String.valueOf(appetizer.getAppetizersPrice()));
    } catch (Exception ex) {
      JOptionPane.showMessageDialog(null, "Error loading appetizer: " + ex.getMessage());
    }
  }// GEN-LAST:event_jTable2MouseClicked

  /**
   * @brief Handler for clicks on the main course table.
   *
   *        When a row is clicked, the ID of the selected main course is captured
   *        for subsequent update operations.
   *
   * @return void
   */
  private void jTable3MouseClicked() {// GEN-FIRST:event_jTable3MouseClicked
    String selectedIDKey = jTable3.getValueAt(jTable3.getSelectedRow(), 0).toString();
    selectedMainCourseID = Long.valueOf(selectedIDKey);
    logger.info("You select id " + selectedMainCourseID + " of maincourse to update.");

    try {
      MainCourse mainCourse = apiClient.getMainCourseById(selectedMainCourseID);
      itemname2.setText(mainCourse.getItemFood());
      itemprice2.setText(String.valueOf(mainCourse.getFoodPrice()));
    } catch (Exception ex) {
      JOptionPane.showMessageDialog(null, "Error loading main course: " + ex.getMessage());
    }
  }// GEN-LAST:event_jTable3MouseClicked

  /**
   * @brief Handler for the "Update" button (Drinks).
   *
   *        Updates the selected drink's price on the backend using ApiClient and
   *        refreshes the table after the update.
   *
   */
  private void jButton1ActionPerformed() {// GEN-FIRST:event_jButton1ActionPerformed
    try {
      Drink updatedDrink = new Drink(selectedDrinkID, itemname.getText(),
          Integer.parseInt(itemprice.getText()), null);
      apiClient.updateDrink(selectedDrinkID, updatedDrink);
      JOptionPane.showMessageDialog(null, "Drink updated successfully.");

      // Refresh table
      DefaultTableModel dtm = (DefaultTableModel) jTable1.getModel();
      dtm.setRowCount(0);
      loadDrinks();
    } catch (Exception ex) {
      JOptionPane.showMessageDialog(null, "Error updating drink: " + ex.getMessage());
    }
  }// GEN-LAST:event_jButton1ActionPerformed

  /**
   * @brief Handler for the "Update" button (Appetizers).
   *
   *        Updates the selected appetizer's price on the backend using ApiClient
   *        and
   *        refreshes the table after the update.
   *
   * @param evt action event
   */
  private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jButton4ActionPerformed
    try {
      Appetizer updatedAppetizer = new Appetizer(selectedAppetizerID, itemname1.getText(),
          Integer.parseInt(itemprice1.getText()), null);
      apiClient.updateAppetizer(selectedAppetizerID, updatedAppetizer);
      JOptionPane.showMessageDialog(null, "Appetizer updated successfully.");

      // Refresh table
      DefaultTableModel dtm = (DefaultTableModel) jTable2.getModel();
      dtm.setRowCount(0);
      loadAppetizer();
    } catch (Exception ex) {
      JOptionPane.showMessageDialog(null, "Error updating appetizer: " + ex.getMessage());
    }
  }// GEN-LAST:event_jButton4ActionPerformed

  /**
   * @brief Handler for the "Update" button (MainCourse).
   *
   *        Updates the selected main course's price on the backend using
   *        ApiClient
   *        and refreshes the table after the update.
   *
   * @param evt action event
   */
  private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jButton6ActionPerformed
    try {
      MainCourse updatedMainCourse = new MainCourse(selectedMainCourseID, itemname2.getText(),
          Integer.parseInt(itemprice2.getText()), null);
      apiClient.updateMainCourse(selectedMainCourseID, updatedMainCourse);
      JOptionPane.showMessageDialog(null, "Main course updated successfully.");

      // Refresh table
      DefaultTableModel dtm = (DefaultTableModel) jTable3.getModel();
      dtm.setRowCount(0);
      loadmainCourse();
    } catch (Exception ex) {
      JOptionPane.showMessageDialog(null, "Error updating main course: " + ex.getMessage());
    }
  }// GEN-LAST:event_jButton6ActionPerformed

  /**
   * @param args the command line arguments
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
    } catch (ClassNotFoundException | InstantiationException | IllegalAccessException
        | javax.swing.UnsupportedLookAndFeelException ex) {
      java.util.logging.Logger.getLogger(AdminProducts.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
    }
    // </editor-fold>

    /* Create and display the form */
    java.awt.EventQueue.invokeLater(() -> new AdminProducts().setVisible(true));
  }

  // Variables declaration - do not modify//GEN-BEGIN:variables
  private javax.swing.JTextField itemname;
  private javax.swing.JTextField itemname1;
  private javax.swing.JTextField itemname2;
  private javax.swing.JTextField itemprice;
  private javax.swing.JTextField itemprice1;
  private javax.swing.JTextField itemprice2;

  // UI components declared locally in initComponents() to satisfy Sonar rule

  private javax.swing.JTable jTable1;
  private javax.swing.JTable jTable2;
  private javax.swing.JTable jTable3;
  // End of variables declaration//GEN-END:variables
}
