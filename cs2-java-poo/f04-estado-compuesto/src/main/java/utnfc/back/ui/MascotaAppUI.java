package utnfc.back.ui;

import utnfc.back.mascota.Mascota;

import javax.swing.*;

/**
 * Punto de entrada principal para lanzar la Interfaz Gráfica de Mascota.
 */
public class MascotaAppUI {

    public static void main(String[] args) {
        // Configurar el Look & Feel del sistema para una apariencia nativa moderna
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {
            // Si falla, utiliza el Look & Feel por defecto de Swing
        }

        SwingUtilities.invokeLater(() -> {
            // Diálogo inicial de adopción de mascota
            String nombre = JOptionPane.showInputDialog(
                    null,
                    "¡Bienvenido a la Mascota Virtual!\n¿Qué nombre deseas darle a tu mascota?",
                    "Crear Mascota",
                    JOptionPane.QUESTION_MESSAGE
            );

            if (nombre == null || nombre.trim().isEmpty()) {
                nombre = "Ahsoka";
            }

            // Instanciamos el modelo con los valores por defecto (50 energía, 3 humor)
            Mascota mascota = new Mascota(nombre.trim());

            // Abrimos la ventana principal
            VentanaMascota ventana = new VentanaMascota(mascota);
            ventana.setVisible(true);
        });
    }
}
