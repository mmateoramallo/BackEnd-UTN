package utnfc.back.ui;

import utnfc.back.mascota.Mascota;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 * Interfaz Gráfica interactiva estilo Tamagotchi para Mascota en Fase 05.
 * Incorpora estados de vida/muerte, rachas de ingesta y actividad.
 */
public class VentanaMascota extends JFrame {

    private Mascota mascota;

    // Componentes visuales principales
    private JLabel lblAvatar;
    private JLabel lblNombre;
    private JLabel lblEstadoVida;
    private JLabel lblEstadoSueno;
    private JProgressBar barraEnergia;
    private JLabel lblValorEnergia;
    private JLabel lblHumorTexto;
    private JButton btnDormirDespertar;
    private JTextArea areaLog;

    // Colores del tema
    private final Color COLOR_FONDO = new Color(245, 247, 250);
    private final Color COLOR_TARJETA = Color.WHITE;
    private final Color COLOR_BORDE = new Color(225, 230, 238);
    private final Color COLOR_TEXTO_TITULO = new Color(30, 41, 59);
    private final Color COLOR_TEXTO_SECUNDARIO = new Color(100, 116, 139);
    private final Color COLOR_PRIMARIO = new Color(79, 70, 229);
    private final Color COLOR_VERDE = new Color(34, 197, 94);
    private final Color COLOR_AMARILLO = new Color(234, 179, 8);
    private final Color COLOR_ROJO = new Color(239, 68, 68);

    private final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");

    public VentanaMascota(Mascota mascotaInicial) {
        this.mascota = mascotaInicial;
        inicializarUI();
        actualizarEstadoUI();
        agregarLog("¡Bienvenido a Fase 05! Mascota adoptada: " + mascota.getNombre());
    }

    private void inicializarUI() {
        setTitle("Mascota Virtual — Fase 05 (Mascota Funcional)");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 700);
        setMinimumSize(new Dimension(680, 600));
        setLocationRelativeTo(null);
        getContentPane().setBackground(COLOR_FONDO);
        setLayout(new BorderLayout(15, 15));

        JPanel panelContenedor = new JPanel(new BorderLayout(15, 15));
        panelContenedor.setBackground(COLOR_FONDO);
        panelContenedor.setBorder(new EmptyBorder(15, 15, 15, 15));

        // 1. Cabecera y Dashboard Superior
        JPanel panelSuperior = crearPanelDashboard();
        panelContenedor.add(panelSuperior, BorderLayout.NORTH);

        // 2. Panel Central: Acciones e Interacciones
        JPanel panelCentral = crearPanelAcciones();
        panelContenedor.add(panelCentral, BorderLayout.CENTER);

        // 3. Panel Inferior: Historial de Actividades
        JPanel panelInferior = crearPanelHistorial();
        panelContenedor.add(panelInferior, BorderLayout.SOUTH);

        add(panelContenedor);
    }

    /**
     * Dashboard superior con Avatar, Nombre, Estado de Vida, Sueño, Energía y Humor.
     */
    private JPanel crearPanelDashboard() {
        JPanel tarjeta = new JPanel(new BorderLayout(15, 10));
        tarjeta.setBackground(COLOR_TARJETA);
        tarjeta.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(COLOR_BORDE, 1, true),
                new EmptyBorder(15, 20, 15, 20)
        ));

        // Avatar
        JPanel panelAvatar = new JPanel(new BorderLayout());
        panelAvatar.setOpaque(false);
        lblAvatar = new JLabel("😊", SwingConstants.CENTER);
        lblAvatar.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 70));
        panelAvatar.add(lblAvatar, BorderLayout.CENTER);

        // Información de la mascota
        JPanel panelInfo = new JPanel();
        panelInfo.setLayout(new BoxLayout(panelInfo, BoxLayout.Y_AXIS));
        panelInfo.setOpaque(false);

        // Fila Nombre + Badges (Vida y Sueño) + Renombrar
        JPanel filaNombre = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        filaNombre.setOpaque(false);

        lblNombre = new JLabel(mascota != null ? mascota.getNombre() : "Sin nombre");
        lblNombre.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblNombre.setForeground(COLOR_TEXTO_TITULO);

        lblEstadoVida = new JLabel(" ❤️ Viva ");
        lblEstadoVida.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblEstadoVida.setOpaque(true);
        lblEstadoVida.setBackground(new Color(220, 252, 231));
        lblEstadoVida.setForeground(new Color(22, 101, 52));
        lblEstadoVida.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(187, 247, 208), 1, true),
                new EmptyBorder(3, 8, 3, 8)
        ));

        lblEstadoSueno = new JLabel(" ☀️ Despierta ");
        lblEstadoSueno.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblEstadoSueno.setOpaque(true);
        lblEstadoSueno.setBackground(new Color(239, 246, 255));
        lblEstadoSueno.setForeground(new Color(29, 78, 216));
        lblEstadoSueno.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(191, 219, 254), 1, true),
                new EmptyBorder(3, 8, 3, 8)
        ));

        JButton btnRenombrar = new JButton("✏️ Renombrar");
        btnRenombrar.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        btnRenombrar.setFocusPainted(false);
        btnRenombrar.addActionListener(e -> solicitarCambioNombre());

        filaNombre.add(lblNombre);
        filaNombre.add(lblEstadoVida);
        filaNombre.add(lblEstadoSueno);
        filaNombre.add(btnRenombrar);

        // Barra de Energía
        JPanel filaEnergia = new JPanel(new BorderLayout(8, 0));
        filaEnergia.setOpaque(false);
        filaEnergia.setBorder(new EmptyBorder(8, 0, 4, 0));

        JLabel lblTituloEnergia = new JLabel("⚡ Energía:");
        lblTituloEnergia.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblTituloEnergia.setForeground(COLOR_TEXTO_SECUNDARIO);

        barraEnergia = new JProgressBar(0, 100);
        barraEnergia.setValue(mascota != null ? mascota.getEnergia() : 50);
        barraEnergia.setStringPainted(false);
        barraEnergia.setPreferredSize(new Dimension(180, 18));
        barraEnergia.setForeground(COLOR_VERDE);
        barraEnergia.setBackground(new Color(241, 245, 249));

        lblValorEnergia = new JLabel("50 / 100");
        lblValorEnergia.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblValorEnergia.setForeground(COLOR_TEXTO_TITULO);

        JPanel panelBarraWrap = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        panelBarraWrap.setOpaque(false);
        panelBarraWrap.add(barraEnergia);
        panelBarraWrap.add(lblValorEnergia);

        filaEnergia.add(lblTituloEnergia, BorderLayout.WEST);
        filaEnergia.add(panelBarraWrap, BorderLayout.CENTER);

        // Fila de Humor
        JPanel filaHumor = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        filaHumor.setOpaque(false);
        filaHumor.setBorder(new EmptyBorder(4, 0, 0, 0));

        JLabel lblTituloHumor = new JLabel("🎭 Humor:");
        lblTituloHumor.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblTituloHumor.setForeground(COLOR_TEXTO_SECUNDARIO);

        lblHumorTexto = new JLabel("Neutral");
        lblHumorTexto.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblHumorTexto.setForeground(COLOR_PRIMARIO);

        filaHumor.add(lblTituloHumor);
        filaHumor.add(lblHumorTexto);

        panelInfo.add(filaNombre);
        panelInfo.add(filaEnergia);
        panelInfo.add(filaHumor);

        tarjeta.add(panelAvatar, BorderLayout.WEST);
        tarjeta.add(panelInfo, BorderLayout.CENTER);

        return tarjeta;
    }

    /**
     * Panel de Acciones e Interacciones.
     */
    private JPanel crearPanelAcciones() {
        JPanel tarjeta = new JPanel(new BorderLayout(10, 10));
        tarjeta.setBackground(COLOR_TARJETA);
        tarjeta.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(COLOR_BORDE, 1, true),
                new EmptyBorder(15, 20, 15, 20)
        ));

        JLabel lblTitulo = new JLabel("Acciones y Comportamientos");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblTitulo.setForeground(COLOR_TEXTO_TITULO);
        tarjeta.add(lblTitulo, BorderLayout.NORTH);

        JPanel gridBotones = new JPanel(new GridLayout(2, 4, 10, 10));
        gridBotones.setOpaque(false);

        // 1. Comer
        JButton btnComer = crearBotonAccion("🍕 Comer", "+10% E | Ingesta", e -> {
            boolean vivaAntes = mascota.isViva();
            boolean resultado = mascota.comer();
            if (resultado) {
                if (!mascota.isViva() && vivaAntes) {
                    agregarLog("☠️ ¡" + mascota.getNombre() + " comió por 5ta vez consecutiva y falleció por empacho!");
                    JOptionPane.showMessageDialog(this, "¡Oh no! " + mascota.getNombre() + " comió 5 veces seguidas y falleció de empacho. 💀", "Mascota Fallecida", JOptionPane.ERROR_MESSAGE);
                } else {
                    agregarLog("🍕 " + mascota.getNombre() + " comió. (Humor: " + mascota.getHumor() + ", Energía: " + mascota.getEnergia() + ")");
                }
            } else {
                agregarLog(obtenerMotivoBloqueo("comer"));
            }
            actualizarEstadoUI();
        });

        // 2. Beber
        JButton btnBeber = crearBotonAccion("💧 Beber", "+5% E | Ingesta", e -> {
            boolean vivaAntes = mascota.isViva();
            boolean resultado = mascota.beber();
            if (resultado) {
                if (!mascota.isViva() && vivaAntes) {
                    agregarLog("☠️ ¡" + mascota.getNombre() + " bebió por 5ta vez consecutiva y falleció por empacho!");
                    JOptionPane.showMessageDialog(this, "¡Oh no! " + mascota.getNombre() + " realizó 5 ingestas consecutivas y falleció de empacho. 💀", "Mascota Fallecida", JOptionPane.ERROR_MESSAGE);
                } else {
                    agregarLog("💧 " + mascota.getNombre() + " bebió agua. (Humor: " + mascota.getHumor() + ", Energía: " + mascota.getEnergia() + ")");
                }
            } else {
                agregarLog(obtenerMotivoBloqueo("beber"));
            }
            actualizarEstadoUI();
        });

        // 3. Correr
        JButton btnCorrer = crearBotonAccion("🏃 Correr", "-35% E, -2 H | Actividad", e -> {
            boolean vivaAntes = mascota.isViva();
            boolean dormidaAntes = mascota.isDormida();
            boolean resultado = mascota.correr();
            if (resultado) {
                if (!mascota.isViva() && vivaAntes) {
                    agregarLog("☠️ ¡" + mascota.getNombre() + " se quedó sin energía (0) y falleció por agotamiento!");
                    JOptionPane.showMessageDialog(this, "¡" + mascota.getNombre() + " agotó toda su energía y falleció. 💀", "Mascota Agotada", JOptionPane.ERROR_MESSAGE);
                } else if (mascota.isDormida() && !dormidaAntes) {
                    agregarLog("😴 ¡" + mascota.getNombre() + " realizó 3 actividades seguidas, se empacó y se durmió!");
                    JOptionPane.showMessageDialog(this, mascota.getNombre() + " realizó 3 actividades consecutivas, se empacó y se quedó dormida. 😴", "Mascota Empacada", JOptionPane.WARNING_MESSAGE);
                } else {
                    agregarLog("🏃 " + mascota.getNombre() + " corrió. (Humor: " + mascota.getHumor() + ", Energía: " + mascota.getEnergia() + ")");
                }
            } else {
                agregarLog(obtenerMotivoBloqueo("correr"));
            }
            actualizarEstadoUI();
        });

        // 4. Saltar
        JButton btnSaltar = crearBotonAccion("🦘 Saltar", "-15% E, -2 H | Actividad", e -> {
            boolean vivaAntes = mascota.isViva();
            boolean dormidaAntes = mascota.isDormida();
            boolean resultado = mascota.saltar();
            if (resultado) {
                if (!mascota.isViva() && vivaAntes) {
                    agregarLog("☠️ ¡" + mascota.getNombre() + " se quedó sin energía (0) y falleció por agotamiento!");
                    JOptionPane.showMessageDialog(this, "¡" + mascota.getNombre() + " agotó toda su energía y falleció. 💀", "Mascota Agotada", JOptionPane.ERROR_MESSAGE);
                } else if (mascota.isDormida() && !dormidaAntes) {
                    agregarLog("😴 ¡" + mascota.getNombre() + " realizó 3 actividades seguidas, se empacó y se durmió!");
                    JOptionPane.showMessageDialog(this, mascota.getNombre() + " realizó 3 actividades consecutivas, se empacó y se quedó dormida. 😴", "Mascota Empacada", JOptionPane.WARNING_MESSAGE);
                } else {
                    agregarLog("🦘 " + mascota.getNombre() + " saltó. (Humor: " + mascota.getHumor() + ", Energía: " + mascota.getEnergia() + ")");
                }
            } else {
                agregarLog(obtenerMotivoBloqueo("saltar"));
            }
            actualizarEstadoUI();
        });

        // 5. Dormir / Despertar
        btnDormirDespertar = crearBotonAccion("💤 Dormir", "+25 E, +2 H | Descanso", e -> {
            if (!mascota.isViva()) {
                agregarLog("⚠️ No puedes interactuar con una mascota que ha fallecido.");
                return;
            }

            if (mascota.isDormida()) {
                mascota.despertar();
                agregarLog("⏰ " + mascota.getNombre() + " se ha despertado (-1 humor, corta rachas).");
            } else {
                mascota.dormir();
                agregarLog("💤 " + mascota.getNombre() + " se fue a dormir (+25 energía, +2 humor, corta rachas).");
            }
            actualizarEstadoUI();
        });

        // 6. Llamar por nombre
        JButton btnLlamar = crearBotonAccion("🗣️ Llamar", "Verifica respondeA(nombre)", e -> {
            solicitarLlamadaPorNombre();
        });

        // 7. Ver Estado JSON
        JButton btnEstadoJson = crearBotonAccion("📋 Ver Estado", "Consulta toString()", e -> {
            mostrarEstadoJSON();
        });

        // 8. Nueva Mascota
        JButton btnNuevaMascota = crearBotonAccion("🐾 Nueva Mascota", "Adoptar otra mascota", e -> {
            solicitarNuevaMascota();
        });

        gridBotones.add(btnComer);
        gridBotones.add(btnBeber);
        gridBotones.add(btnCorrer);
        gridBotones.add(btnSaltar);
        gridBotones.add(btnDormirDespertar);
        gridBotones.add(btnLlamar);
        gridBotones.add(btnEstadoJson);
        gridBotones.add(btnNuevaMascota);

        tarjeta.add(gridBotones, BorderLayout.CENTER);

        return tarjeta;
    }

    /**
     * Panel Inferior con el log de actividades.
     */
    private JPanel crearPanelHistorial() {
        JPanel tarjeta = new JPanel(new BorderLayout(5, 5));
        tarjeta.setBackground(COLOR_TARJETA);
        tarjeta.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(COLOR_BORDE, 1, true),
                new EmptyBorder(10, 15, 10, 15)
        ));

        JLabel lblTituloLog = new JLabel("📜 Registro de Actividades y Rachas");
        lblTituloLog.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblTituloLog.setForeground(COLOR_TEXTO_TITULO);

        areaLog = new JTextArea(6, 40);
        areaLog.setFont(new Font("Consolas", Font.PLAIN, 12));
        areaLog.setEditable(false);
        areaLog.setBackground(new Color(248, 250, 252));
        areaLog.setForeground(new Color(51, 65, 85));
        areaLog.setBorder(new EmptyBorder(5, 5, 5, 5));

        JScrollPane scroll = new JScrollPane(areaLog);
        scroll.setBorder(new LineBorder(new Color(226, 232, 240), 1, true));

        tarjeta.add(lblTituloLog, BorderLayout.NORTH);
        tarjeta.add(scroll, BorderLayout.CENTER);

        return tarjeta;
    }

    private String obtenerMotivoBloqueo(String accion) {
        if (!mascota.isViva()) {
            return "⚠️ No se puede " + accion + ": la mascota ha fallecido (isViva() = false).";
        }
        if (mascota.isDormida()) {
            return "⚠️ No se puede " + accion + ": " + mascota.getNombre() + " está dormida.";
        }
        return "⚠️ La acción " + accion + " no pudo realizarse.";
    }

    private JButton crearBotonAccion(String titulo, String subtitulo, java.awt.event.ActionListener accion) {
        JButton btn = new JButton("<html><center><b>" + titulo + "</b><br><font size='2' color='#64748B'>" + subtitulo + "</font></center></html>");
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        btn.setBackground(new Color(248, 250, 252));
        btn.setForeground(COLOR_TEXTO_TITULO);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(203, 213, 225), 1, true),
                new EmptyBorder(8, 8, 8, 8)
        ));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.addActionListener(accion);
        return btn;
    }

    private void actualizarEstadoUI() {
        if (mascota == null) return;

        lblNombre.setText(mascota.getNombre());

        // Vida
        if (mascota.isViva()) {
            lblEstadoVida.setText(" ❤️ Viva ");
            lblEstadoVida.setBackground(new Color(220, 252, 231));
            lblEstadoVida.setForeground(new Color(22, 101, 52));
        } else {
            lblEstadoVida.setText(" 💀 Muerta ");
            lblEstadoVida.setBackground(new Color(254, 226, 226));
            lblEstadoVida.setForeground(new Color(153, 27, 27));
        }

        // Energía
        int energia = mascota.getEnergia();
        barraEnergia.setValue(energia);
        lblValorEnergia.setText(energia + " / 100");

        if (energia > 50) {
            barraEnergia.setForeground(COLOR_VERDE);
        } else if (energia >= 20) {
            barraEnergia.setForeground(COLOR_AMARILLO);
        } else {
            barraEnergia.setForeground(COLOR_ROJO);
        }

        // Humor y Sueño / Vida
        String humor = mascota.getHumor();
        lblHumorTexto.setText(humor);

        if (!mascota.isViva()) {
            lblAvatar.setText("💀");
            lblEstadoSueno.setText(" ⚰️ Inerte ");
            lblEstadoSueno.setBackground(new Color(241, 245, 249));
            lblEstadoSueno.setForeground(new Color(100, 116, 139));
            btnDormirDespertar.setText("<html><center><b>💀 Inerte</b><br><font size='2' color='#64748B'>Fallecida</font></center></html>");
        } else if (mascota.isDormida()) {
            lblAvatar.setText("😴");
            lblEstadoSueno.setText(" 💤 Dormida ");
            lblEstadoSueno.setBackground(new Color(254, 243, 199));
            lblEstadoSueno.setForeground(new Color(146, 64, 14));
            btnDormirDespertar.setText("<html><center><b>⏰ Despertar</b><br><font size='2' color='#64748B'>-1 Humor</font></center></html>");
        } else {
            lblEstadoSueno.setText(" ☀️ Despierta ");
            lblEstadoSueno.setBackground(new Color(239, 246, 255));
            lblEstadoSueno.setForeground(new Color(29, 78, 216));
            btnDormirDespertar.setText("<html><center><b>💤 Dormir</b><br><font size='2' color='#64748B'>+25 E, +2 H</font></center></html>");

            switch (humor) {
                case "Chocho" -> lblAvatar.setText("🤩");
                case "Contento" -> lblAvatar.setText("😊");
                case "Neutral" -> lblAvatar.setText("😐");
                case "Enojado" -> lblAvatar.setText("😠");
                case "Muy enojado" -> lblAvatar.setText("😡");
                default -> lblAvatar.setText("🐾");
            }
        }
    }

    private void agregarLog(String mensaje) {
        String hora = LocalTime.now().format(timeFormatter);
        areaLog.append("[" + hora + "] " + mensaje + "\n");
        areaLog.setCaretPosition(areaLog.getDocument().getLength());
    }

    private void solicitarCambioNombre() {
        String nuevoNombre = JOptionPane.showInputDialog(
                this,
                "Ingresa el nuevo nombre para tu mascota:",
                "Renombrar Mascota",
                JOptionPane.QUESTION_MESSAGE
        );

        if (nuevoNombre != null && !nuevoNombre.trim().isEmpty()) {
            String anterior = mascota.getNombre();
            mascota.setNombre(nuevoNombre.trim());
            agregarLog("✏️ Se cambió el nombre de '" + anterior + "' a '" + mascota.getNombre() + "'.");
            actualizarEstadoUI();
        }
    }

    private void solicitarLlamadaPorNombre() {
        String nombrePrueba = JOptionPane.showInputDialog(
                this,
                "¿Con qué nombre deseas llamar a " + mascota.getNombre() + "?",
                "Llamar Mascota",
                JOptionPane.QUESTION_MESSAGE
        );

        if (nombrePrueba != null) {
            boolean responde = mascota.respondeA(nombrePrueba.trim());
            if (responde) {
                JOptionPane.showMessageDialog(
                        this,
                        "¡" + mascota.getNombre() + " escuchó su nombre y respondió! 🐶",
                        "Respuesta Positiva",
                        JOptionPane.INFORMATION_MESSAGE
                );
                agregarLog("🗣️ Llamaste como '" + nombrePrueba + "' -> ¡Respondió! (true)");
            } else {
                String motivo = !mascota.isViva() ? " (Está fallecida 💀)" : (mascota.isDormida() ? " (Está profundamente dormida 💤)" : " (No reconoció ese nombre ❌)");
                JOptionPane.showMessageDialog(
                        this,
                        mascota.getNombre() + " no respondió al llamado." + motivo,
                        "Sin Respuesta",
                        JOptionPane.WARNING_MESSAGE
                );
                agregarLog("🗣️ Llamaste como '" + nombrePrueba + "' -> No respondió (false)." + motivo);
            }
        }
    }

    private void mostrarEstadoJSON() {
        JOptionPane.showMessageDialog(
                this,
                new JScrollPane(new JTextArea(mascota.toString(), 7, 25)),
                "Estado Actual (JSON - toString)",
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    private void solicitarNuevaMascota() {
        JTextField txtNombre = new JTextField("Grogu", 15);
        JSpinner spinEnergia = new JSpinner(new SpinnerNumberModel(50, 0, 100, 5));
        JSpinner spinHumor = new JSpinner(new SpinnerNumberModel(3, 1, 5, 1));

        JPanel panel = new JPanel(new GridLayout(3, 2, 8, 8));
        panel.add(new JLabel("Nombre:"));
        panel.add(txtNombre);
        panel.add(new JLabel("Energía inicial (0..100):"));
        panel.add(spinEnergia);
        panel.add(new JLabel("Nivel de humor (1..5):"));
        panel.add(spinHumor);

        int opcion = JOptionPane.showConfirmDialog(
                this,
                panel,
                "Adoptar Nueva Mascota",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE
        );

        if (opcion == JOptionPane.OK_OPTION) {
            String nombre = txtNombre.getText().trim();
            if (nombre.isEmpty()) nombre = "Mascota";
            int energia = (int) spinEnergia.getValue();
            int humor = (int) spinHumor.getValue();

            this.mascota = new Mascota(nombre, energia, humor);
            agregarLog("✨ Has adoptado una nueva mascota: " + nombre + " (Energía: " + energia + ", Humor: " + humor + ")");
            actualizarEstadoUI();
        }
    }
}
