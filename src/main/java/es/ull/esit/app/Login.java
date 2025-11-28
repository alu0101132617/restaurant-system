package es.ull.esit.app;

import es.ull.esit.app.middleware.ApiClient;
import es.ull.esit.app.middleware.model.User;
import es.ull.esit.app.middleware.service.AuthService;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

/**
 * @file Login.java
 * @brief Graphical interface for user login.
 *
 * This class provides a simple Swing window that allows a user to enter a
 * username, password, and select a user type.
 *
 * @version 1.0
 * @author Restaurant System Team
 */
public class Login extends javax.swing.JFrame {

    // Service to handle logic
    private final AuthService authService;

    /**
     * @brief Creates new form Login
     */
    public Login() {
        initComponents();
        // Initialize Service
        ApiClient client = new ApiClient("http://localhost:8080");
        this.authService = new AuthService(client);
    }

    /**
     * @brief Initializes GUI components.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        usernametxt = new javax.swing.JTextField();
        usertypecmbo = new javax.swing.JComboBox<>();
        jButton1 = new javax.swing.JButton();
        jPasswordField1 = new javax.swing.JPasswordField();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Login");
        setResizable(false);

        jPanel1.setBackground(new java.awt.Color(248, 244, 230));

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ui/screenshot.png"))); // NOI18N

        jLabel3.setFont(new java.awt.Font("Yu Gothic UI", 1, 24)); // NOI18N
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel3.setText("User Login ");

        usernametxt.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        usernametxt.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        usernametxt.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true), "Username", javax.swing.border.TitledBorder.LEFT, javax.swing.border.TitledBorder.TOP, new java.awt.Font("Yu Gothic UI", 0, 18))); // NOI18N

        usertypecmbo.setEditable(true);
        usertypecmbo.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        usertypecmbo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "admin", "cashier" }));
        usertypecmbo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                usertypecmboActionPerformed(evt);
            }
        });

        jButton1.setFont(new java.awt.Font("Yu Gothic UI", 1, 18)); // NOI18N
        jButton1.setText("Log In");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jPasswordField1.setFont(new java.awt.Font("Thonburi", 0, 18)); // NOI18N
        jPasswordField1.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jPasswordField1.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)), "Password", javax.swing.border.TitledBorder.LEFT, javax.swing.border.TitledBorder.TOP, new java.awt.Font("Lucida Grande", 0, 18))); // NOI18N

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(132, 132, 132)
                        .addComponent(jLabel1))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(61, 61, 61)
                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 309, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(74, 74, 74)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jPasswordField1, javax.swing.GroupLayout.PREFERRED_SIZE, 311, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(usernametxt, javax.swing.GroupLayout.PREFERRED_SIZE, 311, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(usertypecmbo, javax.swing.GroupLayout.PREFERRED_SIZE, 311, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(146, 146, 146)
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 141, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(86, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(39, 39, 39)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(usernametxt, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jPasswordField1, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(usertypecmbo, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(39, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    /**
     * @brief Action for the "Log In" button.
     * * Handles the login process asynchronously.
     */
    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        String usernameInput = usernametxt.getText();
        String passwordInput = new String(jPasswordField1.getPassword());
        
        // Disable button to prevent double clicks
        jButton1.setEnabled(false);
        jButton1.setText("Loading...");

        new Thread(() -> {
            try {
                // 1. Delegate logic to Service
                User loggedInUser = authService.authenticate(usernameInput, passwordInput);
                
                // 2. Success: Update UI in Event Thread
                SwingUtilities.invokeLater(() -> {
                    
                    // Route based on the role returned from SERVER (Security!)
                    // We ignore what was selected in the ComboBox, as the server knows the truth.
                    if ("ADMIN".equalsIgnoreCase(loggedInUser.getRole())) { 
                         new AdminLogin().setVisible(true);
                    } else if ("CASHIER".equalsIgnoreCase(loggedInUser.getRole())) {
                         new Cashier(loggedInUser.getUsername()).setVisible(true); 
                    } else {
                         JOptionPane.showMessageDialog(this, "Unknown Role: " + loggedInUser.getRole());
                         jButton1.setEnabled(true);
                         jButton1.setText("Log In");
                         return;
                    }
                    
                    this.dispose(); // Close login window
                });

            } catch (Exception e) {
                // 3. Failure: Show error in Event Thread
                SwingUtilities.invokeLater(() -> {
                    JOptionPane.showMessageDialog(this, 
                        "Login failed: " + e.getMessage(), 
                        "Login Error", 
                        JOptionPane.ERROR_MESSAGE);
                    
                    // Reset button
                    jButton1.setEnabled(true);
                    jButton1.setText("Log In");
                });
            }
        }).start();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void usertypecmboActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_usertypecmboActionPerformed
        // No action needed here
    }//GEN-LAST:event_usertypecmboActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Login.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }

        java.awt.EventQueue.invokeLater(() -> new Login().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    /** @brief Button to perform login ("Log In"). */
    private javax.swing.JButton jButton1;
    /** @brief Image/logo displayed in the window. */
    private javax.swing.JLabel jLabel1;
    /** @brief Window title label ("User Login"). */
    private javax.swing.JLabel jLabel3;
    /** @brief Main container panel. */
    private javax.swing.JPanel jPanel1;
    /** @brief Input field for the password. */
    private javax.swing.JPasswordField jPasswordField1;
    /** @brief Input field for the username. */
    private javax.swing.JTextField usernametxt;
    /** @brief Combo box to select the user type (admin/cashier). */
    private javax.swing.JComboBox<String> usertypecmbo;
    // End of variables declaration//GEN-END:variables
}