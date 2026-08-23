package utnfc.back.ui;

import utnfc.back.mascota.Mascota;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.geom.RoundRectangle2D;

/**
 * Carcasa interactiva estilo Tamagotchi clásico con botones físicos A, B, C y pantalla LCD.
 */
public class TamagotchiFrame extends JFrame {

    private Mascota mascota;
    private PixelMascotaCanvas canvasLCD;

    // Colores de la carcasa retro
    private final Color COLOR_CARCASA_FONDO = new Color(254, 240, 138); // Amarillo pastel Tamagotchi
    private final Color COLOR_CARCASA_BORDE = new Color(234, 179, 8);
    private final Color COLOR_BISEL_PANTALLA = new Color(71, 85, 105);
    private final Color COLOR_BOTON = new Color(239, 68, 68);           // Botones de goma rojos/rosas
    private final Color COLOR_BOTON_TEXTO = Color.WHITE;

    public TamagotchiFrame(Mascota mascotaInicial) {
        this.mascota = mascotaInicial;
        inicializarUI();
    }

    private void inicializarUI() {
        setTitle("UTN Tamagotchi Virtual — Mascota POO");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        getContentPane().setBackground(new Color(241, 245, 249));
        setLayout(new GridBagLayout());

        // Panel principal con forma de huevo / carcasa de Tamagotchi
        JPanel panelTamagotchi = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                int w = getWidth();
                int h = getHeight();

                // 1. Sombra exterior
                g2.setColor(new Color(0, 0, 0, 30));
                g2.fill(new RoundRectangle2D.Double(8, 12, w - 16, h - 16, 70, 70));

                // 2. Carcasa curva
                g2.setColor(COLOR_CARCASA_FONDO);
                g2.fill(new RoundRectangle2D.Double(4, 4, w - 8, h - 10, 60, 60));

                // 3. Borde exterior
                g2.setColor(COLOR_CARCASA_BORDE);
                g2.setStroke(new BasicStroke(4));
                g2.draw(new RoundRectangle2D.Double(4, 4, w - 8, h - 10, 60, 60));

                // 4. Anilla del llavero arriba
                g2.setColor(new Color(203, 213, 225));
                g2.fillOval(w / 2 - 15, 8, 30, 16);
                g2.setColor(COLOR_CARCASA_FONDO);
                g2.fillOval(w / 2 - 8, 12, 16, 8);
                g2.setColor(new Color(148, 163, 184));
                g2.setStroke(new BasicStroke(2));
                g2.drawOval(w / 2 - 15, 8, 30, 16);
            }
        };

        panelTamagotchi.setLayout(new BoxLayout(panelTamagotchi, BoxLayout.Y_AXIS));
        panelTamagotchi.setOpaque(false);
        panelTamagotchi.setBorder(new EmptyBorder(30, 35, 25, 35));

        // Rótulo del Tamagotchi
        JLabel lblTitulo = new JLabel("★ TAMAGOTCHI POO ★", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 15));
        lblTitulo.setForeground(new Color(161, 98, 7));
        lblTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblTitulo.setBorder(new EmptyBorder(0, 0, 10, 0));

        // Bisel oscuro alrededor de la pantalla LCD
        JPanel panelBiselLCD = new JPanel(new BorderLayout());
        panelBiselLCD.setBackground(COLOR_BISEL_PANTALLA);
        panelBiselLCD.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(51, 65, 85), 3, true),
                new EmptyBorder(8, 8, 8, 8)
        ));

        // Pantalla LCD interactiva con animaciones Pixel-Art
        canvasLCD = new PixelMascotaCanvas(this.mascota);
        panelBiselLCD.add(canvasLCD, BorderLayout.CENTER);

        // Panel de los 3 Botones Físicos (A, B, C)
        JPanel panelBotones = crearPanelBotonesFisicos();

        // Leyenda de atajos de teclado
        JLabel lblAtajos = new JLabel("<html><center><font size='2' color='#78350F'>Teclas: [A] Seleccionar | [B] / Enter: OK | [C] / Esc: Volver<br>O haz clic directo en los iconos de la pantalla</font></center></html>", SwingConstants.CENTER);
        lblAtajos.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblAtajos.setBorder(new EmptyBorder(12, 0, 0, 0));

        panelTamagotchi.add(lblTitulo);
        panelTamagotchi.add(panelBiselLCD);
        panelTamagotchi.add(panelBotones);
        panelTamagotchi.add(lblAtajos);

        add(panelTamagotchi);
        pack();
        setLocationRelativeTo(null);

        // Soporte para control total por teclado
        configurarTeclado();
    }

    private JPanel crearPanelBotonesFisicos() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 35, 15));
        panel.setOpaque(false);

        JButton btnA = crearBotonRedondo("A", "Seleccionar", e -> canvasLCD.botonAPresionado());
        JButton btnB = crearBotonRedondo("B", "OK / Acción", e -> canvasLCD.botonBPresionado());
        JButton btnC = crearBotonRedondo("C", "Volver", e -> canvasLCD.botonCPresionado());

        // Ligero desplazamiento vertical para simular la curvatura clásica de los 3 botones
        btnA.setPreferredSize(new Dimension(58, 58));
        btnB.setPreferredSize(new Dimension(62, 62)); // Botón B ligeramente más grande
        btnC.setPreferredSize(new Dimension(58, 58));

        panel.add(btnA);
        panel.add(btnB);
        panel.add(btnC);

        return panel;
    }

    private JButton crearBotonRedondo(String letra, String tooltip, java.awt.event.ActionListener accion) {
        JButton btn = new JButton(letra) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                int w = getWidth();
                int h = getHeight();

                // Sombra del botón
                g2.setColor(new Color(185, 28, 28));
                g2.fillOval(2, 4, w - 4, h - 4);

                // Cuerpo del botón
                if (getModel().isPressed()) {
                    g2.setColor(new Color(220, 38, 38));
                    g2.fillOval(2, 4, w - 4, h - 6);
                } else if (getModel().isRollover()) {
                    g2.setColor(new Color(248, 113, 113));
                    g2.fillOval(2, 2, w - 4, h - 6);
                } else {
                    g2.setColor(COLOR_BOTON);
                    g2.fillOval(2, 2, w - 4, h - 6);
                }

                // Brillo superior
                g2.setColor(new Color(255, 255, 255, 90));
                g2.fillOval(w / 4, 5, w / 2, h / 4);

                // Letra
                g2.setColor(COLOR_BOTON_TEXTO);
                g2.setFont(new Font("Segoe UI", Font.BOLD, 18));
                FontMetrics fm = g2.getFontMetrics();
                int tx = (w - fm.stringWidth(letra)) / 2;
                int ty = (h + fm.getAscent() - 8) / 2;
                g2.drawString(letra, tx, ty);
            }
        };

        btn.setToolTipText(tooltip);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.addActionListener(accion);

        return btn;
    }

    private void configurarTeclado() {
        setFocusable(true);
        requestFocusInWindow();

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_A, KeyEvent.VK_LEFT -> canvasLCD.botonAPresionado();
                    case KeyEvent.VK_B, KeyEvent.VK_ENTER, KeyEvent.VK_SPACE -> canvasLCD.botonBPresionado();
                    case KeyEvent.VK_C, KeyEvent.VK_ESCAPE, KeyEvent.VK_BACK_SPACE -> canvasLCD.botonCPresionado();
                    case KeyEvent.VK_1 -> canvasLCD.ejecutarComer();
                    case KeyEvent.VK_2 -> canvasLCD.ejecutarBeber();
                    case KeyEvent.VK_3 -> canvasLCD.ejecutarCorrer();
                    case KeyEvent.VK_4 -> canvasLCD.ejecutarSaltar();
                    case KeyEvent.VK_5 -> canvasLCD.alternarDescanso();
                }
            }
        });
    }
}
