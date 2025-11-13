/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.ull.esit.app;

/**
 * Clase principal de la aplicación Black Plate Restaurant Management System.
 * 
 * Esta clase contiene el método main que inicia la aplicación mostrando
 * la ventana de inicio de sesión (Login).
 *
 * @author Behzad Qasim
 * @version 1.0
 * @since 2024
 */
public class JavaApplication2 {

    /**
     * Método principal que inicia la aplicación.
     * 
     * Crea y muestra la ventana de inicio de sesión (Login) como punto
     * de entrada al sistema de gestión del restaurante.
     * 
     * @param args argumentos de línea de comandos (no utilizados)
     */
    public static void main(String[] args) {
       new Login().setVisible(true);
    }
    
}
