package org.frc.isi.dba.creditos.app;

import java.util.UUID;

/**
 * Hello world!
 */
public class App {

    public static void main(String[] args) {
        System.out.println("Hello World!");
        //int[] vector = new int[10]; //Declarar un arreglo

        //Declarar el codigo de la cuenta
        // Generación del código
        UUID guid = UUID.randomUUID();
        String guidString = guid.toString();

    }
}
