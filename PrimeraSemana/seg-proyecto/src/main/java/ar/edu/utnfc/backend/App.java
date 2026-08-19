package ar.edu.utnfc.backend;


import java.util.Scanner;
/**
 * Hello world!
 */
public class App {

    private String nombre;
    private int horas;
    private int horasCompletadas;

    public static void main(String[] args) {
        System.out.println("Hello World!");

        //Solicitar nombre cant de horas de trabajo, cant de tareas
        Scanner consola = new Scanner(System.in);
        App miApp = new App();

        System.out.println("Ingrese su nombre: ");
        miApp.nombre = consola.nextLine();
        
        System.out.println("Ingrese su nombre: ");

    }
}
