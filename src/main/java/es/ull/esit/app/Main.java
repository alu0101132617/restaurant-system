
package es.ull.esit.app;

/**
 * @file JavaApplication2.java
 * @brief Minimal class to start the application (auxiliary entry-point).
 *
 * Launches the Login window when run independently.
 */
public class JavaApplication2 {

    /**
     * @brief Alternative entry point used to launch the UI.
     *
     * @param args Command line arguments (not used).
     */
    public static void main(String[] args) {
       new Login().setVisible(true);
    }
    
}
