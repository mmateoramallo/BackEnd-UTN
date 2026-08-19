package utnfc.back;

import utnfc.back.mascota.Mascota;

public class App {

    public static void main(String[] args) {

        System.out.println("=== Nuestra primera Mascota ===");

        // Creamos una mascota
        Mascota mascota = new Mascota("Ahsoka");

        // Consultamos su nombre
        System.out.println("Nombre inicial: " + mascota.getNombre());

        // Cambiamos su nombre
        mascota.setNombre("Grogu");

        // Volvemos a consultar el estado del objeto
        System.out.println("Nuevo nombre: " + mascota.getNombre());

        // Creamos otra mascota para comprobar que
        // cada objeto mantiene su propio estado
        Mascota otraMascota = new Mascota("Chewbacca");

        System.out.println();
        System.out.println("Primera mascota: " + mascota.getNombre());
        System.out.println("Segunda mascota: " + otraMascota.getNombre());
    }
}