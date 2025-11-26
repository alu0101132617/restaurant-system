/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.ull.esit.app;

/**
 * @file JavaApplication2.java
 * @brief Clase mínima para arrancar la aplicación (entry-point auxiliar).
 *
 * Lanza la ventana de Login cuando se ejecuta de forma independiente.
 */
public class JavaApplication2 {

    /**
     * @brief Punto de entrada alternativo usado para lanzar la UI.
     *
     * @param args argumentos de línea de comandos (no usados).
     */
    public static void main(String[] args) {
       new Login().setVisible(true);
    }
    
}
