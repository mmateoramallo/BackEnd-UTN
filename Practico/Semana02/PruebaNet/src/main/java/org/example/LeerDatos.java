package org.example;
import java.util.Scanner;

public class LeerDatos {
    public static void main(String[] args) {
        System.out.println("Hola Mundo");
        Scanner scanner = new Scanner(System.in);
        System.out.println("Ingresa tu nombre: ");
        String nombre = scanner.nextLine();
        System.out.println("Hola " + nombre);

        if(nombre != null) System.out.println("El nombre ingresado es: " + nombre + " y tiene " + nombre.length() + " caracteres");
        else System.out.println("No se ingreso un nombre");
    }
}
