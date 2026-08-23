package utnfc.back;

import utnfc.back.mascota.Mascota;

public class App {

    public static void main(String[] args) {

        System.out.println("=== Mascota — Fase 05: Mascota Funcional ===");
        System.out.println();

        // 1. Creamos la mascota
        Mascota mascota = new Mascota("Ahsoka");
        System.out.println("--- Estado Inicial ---");
        System.out.println(mascota);
        System.out.println("¿Está viva?: " + mascota.isViva());
        System.out.println();

        // 2. Racha de ingestas
        System.out.println("--- Probando Racha de Ingestas ---");
        System.out.println("1° Ingesta (comer): " + mascota.comer() + " -> Humor: " + mascota.getHumor() + ", Energía: " + mascota.getEnergia());
        System.out.println("2° Ingesta (beber): " + mascota.beber() + " -> Humor: " + mascota.getHumor() + ", Energía: " + mascota.getEnergia());
        System.out.println("3° Ingesta (comer - empieza a molestarse): " + mascota.comer() + " -> Humor: " + mascota.getHumor() + ", Energía: " + mascota.getEnergia());
        System.out.println("4° Ingesta (beber - sigue molesta): " + mascota.beber() + " -> Humor: " + mascota.getHumor() + ", Energía: " + mascota.getEnergia());
        System.out.println();

        // 3. Actividad corta racha de ingesta
        System.out.println("--- Actividad (interrumpe racha de ingesta) ---");
        System.out.println("Actividad (saltar): " + mascota.saltar() + " -> Humor: " + mascota.getHumor() + ", Energía: " + mascota.getEnergia());
        System.out.println("Nueva Ingesta después de saltar (vuelve a subir humor): " + mascota.comer() + " -> Humor: " + mascota.getHumor() + ", Energía: " + mascota.getEnergia());
        System.out.println();

        // 4. Racha de 3 actividades consecutivas (empaque y sueño)
        System.out.println("--- Racha de Actividades (3 consecutivas provocan sueño) ---");
        Mascota deportista = new Mascota("Grogu", 100, 5);
        System.out.println("1° Actividad (correr): " + deportista.correr() + " -> Dormida: " + deportista.isDormida());
        System.out.println("2° Actividad (saltar): " + deportista.saltar() + " -> Dormida: " + deportista.isDormida());
        System.out.println("3° Actividad (correr): " + deportista.correr() + " -> Dormida: " + deportista.isDormida() + " (¡Se empacó y se durmió!)");
        System.out.println();

        // 5. Muerte por empacho (5 ingestas seguidas)
        System.out.println("--- Demostración de Muerte por Empacho (5 ingestas) ---");
        Mascota glotona = new Mascota("Chewbacca", 50, 3);
        for (int i = 1; i <= 5; i++) {
            boolean resultado = glotona.comer();
            System.out.println("Ingesta " + i + ": " + resultado + " | Viva: " + glotona.isViva() + " | Energía: " + glotona.getEnergia());
        }
        System.out.println("Intento de comer estando muerta: " + glotona.comer());
        System.out.println("Estado final: " + glotona);
    }
}
