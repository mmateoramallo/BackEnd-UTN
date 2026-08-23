package utnfc.back.ui;

import utnfc.back.mascota.Mascota;

import javax.swing.*;

/**
 * Punto de entrada principal para lanzar la Interfaz Gráfica Tamagotchi Retro en Fase 05.
 */
public class MascotaAppUI {

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {
        }

        SwingUtilities.invokeLater(() -> {
            String nombre = JOptionPane.showInputDialog(
                    null,
                    "¡Bienvenido a UTN Tamagotchi (Fase 05: Mascota Funcional)!\n¿Qué nombre deseas darle a tu mascota?",
                    "Crear Mascota",
                    JOptionPane.QUESTION_MESSAGE
            );

            if (nombre == null || nombre.trim().isEmpty()) {
                nombre = "Ahsoka";
            }

            Mascota mascota = new Mascota(nombre.trim());

            // Abrimos la experiencia completa Tamagotchi Retro
            TamagotchiFrame ventana = new TamagotchiFrame(mascota);
            ventana.setVisible(true);
        });
    }
}
