package utnfc.back;

import utnfc.back.mascota.Mascota;

public class App {

    public static void main(String[] args) {

        System.out.println("=== Mascota - Fase 02: Descanso ===");
        System.out.println();

        // Creamos nuestra mascota.
        Mascota mascota = new Mascota("Ahsoka");

        // ------------------------------------------------------------
        // ESTADO INICIAL
        // ------------------------------------------------------------

        System.out.println("--- Estado inicial ---");
        System.out.println("Nombre: " + mascota.getNombre());
        System.out.println("¿Está dormida?: " + mascota.isDormida());
        System.out.println("¿Responde a Ahsoka?: " + mascota.respondeA("Ahsoka"));
        System.out.println("¿Responde a Grogu?: " + mascota.respondeA("Grogu"));
        System.out.println();

        // ------------------------------------------------------------
        // LA MASCOTA SE DUERME
        // ------------------------------------------------------------

        System.out.println("--- La mascota se duerme ---");

        mascota.dormir();

        System.out.println("¿Está dormida?: " + mascota.isDormida());
        System.out.println("¿Responde a Ahsoka?: " + mascota.respondeA("Ahsoka"));

        // Aunque esté dormida, su nombre continúa siendo una
        // propiedad accesible del objeto.
        System.out.println("Su nombre sigue siendo: " + mascota.getNombre());
        System.out.println();

        // ------------------------------------------------------------
        // LA MASCOTA SE DESPIERTA
        // ------------------------------------------------------------

        System.out.println("--- La mascota se despierta ---");

        mascota.despertar();

        System.out.println("¿Está dormida?: " + mascota.isDormida());
        System.out.println("¿Responde a Ahsoka?: " + mascota.respondeA("Ahsoka"));
        System.out.println();

        // ------------------------------------------------------------
        // CAMBIAMOS SU NOMBRE
        // ------------------------------------------------------------

        System.out.println("--- Cambiamos el nombre ---");

        mascota.setNombre("Grogu");

        System.out.println("Nuevo nombre: " + mascota.getNombre());
        System.out.println("¿Responde a Ahsoka?: " + mascota.respondeA("Ahsoka"));
        System.out.println("¿Responde a Grogu?: " + mascota.respondeA("Grogu"));
    }
}