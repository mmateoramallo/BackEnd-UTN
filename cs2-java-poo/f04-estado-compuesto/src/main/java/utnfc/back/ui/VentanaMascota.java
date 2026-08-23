package utnfc.back.ui;

import utnfc.back.mascota.Mascota;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 * Interfaz Gráfica interactiva estilo Tamagotchi para Mascota.
 */
public class VentanaMascota extends JFrame {

    private Mascota mascota;

    // Componentes visuales principales
    private JLabel lblAvatar;
    private JLabel lblNombre;
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
        agregarLog("¡Bienvenido! Mascota adoptada: " + mascota.getNombre());
    }

    private void inicializarUI() {
        setTitle("Mascota Virtual — Tamagotchi POO");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(780, 680);
        setMinimumSize(new Dimension(650, 580));
        setLocationRelativeTo(null);
        getContentPane().setBackground(COLOR_FONDO);
        setLayout(new BorderLayout(15, 15));

        // Panel Principal con margen
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
     * Dashboard superior con Avatar, Nombre, Estado de Sueño, Energía y Humor.
     */
    private JPanel crearPanelDashboard() {
        JPanel tarjeta = new JPanel(new BorderLayout(15, 10));
        tarjeta.setBackground(COLOR_TARJETA);
        tarjeta.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(COLOR_BORDE, 1, true),
                new EmptyBorder(15, 20, 15, 20)
        ));

        // Panel Izquierdo: Avatar grande
        JPanel panelAvatar = new JPanel(new BorderLayout());
        panelAvatar.setOpaque(false);
        lblAvatar = new JLabel("😊", SwingConstants.CENTER);
        lblAvatar.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 70));
        panelAvatar.add(lblAvatar, BorderLayout.CENTER);

        // Panel Central: Datos de la mascota
        JPanel panelInfo = new JPanel();
        panelInfo.setLayout(new BoxLayout(panelInfo, BoxLayout.Y_AXIS));
        panelInfo.setOpaque(false);

        // Fila Nombre + Estado
        JPanel filaNombre = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        filaNombre.setOpaque(false);

        lblNombre = new JLabel(mascota != null ? mascota.getNombre() : "Sin nombre");
        lblNombre.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblNombre.setForeground(COLOR_TEXTO_TITULO);

        lblEstadoSueno = new JLabel(" ☀️ Despierta ");
        lblEstadoSueno.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblEstadoSueno.setOpaque(true);
        lblEstadoSueno.setBackground(new Color(220, 252, 231));
        lblEstadoSueno.setForeground(new Color(22, 101, 52));
        lblEstadoSueno.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(187, 247, 208), 1, true),
                new EmptyBorder(3, 8, 3, 8)
        ));

        JButton btnRenombrar = new JButton("✏️ Renombrar");
        btnRenombrar.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        btnRenombrar.setFocusPainted(false);
        btnRenombrar.addActionListener(e -> solicitarCambioNombre());

        filaNombre.add(lblNombre);
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
     * Panel de Acciones y Juegos.
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
        JButton btnComer = crearBotonAccion("🍕 Comer", "+10% Energía, +1 Humor", e -> {
            boolean resultado = mascota.comer();
            if (resultado) {
                agregarLog("🍕 " + mascota.getNombre() + " comió rico (+10% energía, +1 humor).");
            } else {
                agregarLog("⚠️ " + mascota.getNombre() + " está dormida y no puede comer.");
            }
            actualizarEstadoUI();
        });

        // 2. Beber
        JButton btnBeber = crearBotonAccion("💧 Beber", "+5% Energía, +1 Humor", e -> {
            boolean resultado = mascota.beber();
            if (resultado) {
                agregarLog("💧 " + mascota.getNombre() + " bebió agua fresca (+5% energía, +1 humor).");
            } else {
                agregarLog("⚠️ " + mascota.getNombre() + " está dormida y no puede beber.");
            }
            actualizarEstadoUI();
        });

        // 3. Correr
        JButton btnCorrer = crearBotonAccion("🏃 Correr", "-35% Energía, -2 Humor", e -> {
            boolean resultado = mascota.correr();
            if (resultado) {
                agregarLog("🏃 " + mascota.getNombre() + " corrió por el parque (-35% energía, -2 humor).");
            } else {
                agregarLog("⚠️ " + mascota.getNombre() + " está dormida y no puede correr.");
            }
            actualizarEstadoUI();
        });

        // 4. Saltar
        JButton btnSaltar = crearBotonAccion("🦘 Saltar", "-15% Energía, -2 Humor", e -> {
            boolean resultado = mascota.saltar();
            if (resultado) {
                agregarLog("🦘 " + mascota.getNombre() + " saltó alegremente (-15% energía, -2 humor).");
            } else {
                agregarLog("⚠️ " + mascota.getNombre() + " está dormida y no puede saltar.");
            }
            actualizarEstadoUI();
        });

        // 5. Dormir / Despertar
        btnDormirDespertar = crearBotonAccion("💤 Dormir", "+25 Energía, +2 Humor", e -> {
            if (mascota.isDormida()) {
                mascota.despertar();
                agregarLog("⏰ " + mascota.getNombre() + " se ha despertado (-1 humor).");
            } else {
                mascota.dormir();
                agregarLog("💤 " + mascota.getNombre() + " se fue a dormir (+25 energía, +2 humor).");
            }
            actualizarEstadoUI();
        });

        // 6. Llamar por nombre
        JButton btnLlamar = crearBotonAccion("🗣️ Llamar", "Verifica respondeA(nombre)", e -> {
            solicitarLlamadaPorNombre();
        });

        // 7. Ver JSON / toString
        JButton btnEstadoJson = crearBotonAccion("📋 Ver Estado", "Consulta toString()", e -> {
            mostrarEstadoJSON();
        });

        // 8. Crear / Cambiar Mascota
        JButton btnNuevaMascota = crearBotonAccion("🐾 Nueva Mascota", "Crear otra mascota", e -> {
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

        JLabel lblTituloLog = new JLabel("📜 Registro de Actividades");
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

    /**
     * Helper para fabricar botones estilizados con subtítulo explicativo.
     */
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

    /**
     * Refresca la vista en función del estado de la instancia Mascota.
     */
    private void actualizarEstadoUI() {
        if (mascota == null) return;

        // Nombre
        lblNombre.setText(mascota.getNombre());

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

        // Humor y Avatar
        String humor = mascota.getHumor();
        lblHumorTexto.setText(humor);

        if (mascota.isDormida()) {
            lblAvatar.setText("😴");
            lblEstadoSueno.setText(" 💤 Dormida ");
            lblEstadoSueno.setBackground(new Color(254, 243, 199));
            lblEstadoSueno.setForeground(new Color(146, 64, 14));
            lblEstadoSueno.setBorder(BorderFactory.createCompoundBorder(
                    new LineBorder(new Color(253, 230, 138), 1, true),
                    new EmptyBorder(3, 8, 3, 8)
            ));
            btnDormirDespertar.setText("<html><center><b>⏰ Despertar</b><br><font size='2' color='#64748B'>-1 Humor</font></center></html>");
        } else {
            lblEstadoSueno.setText(" ☀️ Despierta ");
            lblEstadoSueno.setBackground(new Color(220, 252, 231));
            lblEstadoSueno.setForeground(new Color(22, 101, 52));
            lblEstadoSueno.setBorder(BorderFactory.createCompoundBorder(
                    new LineBorder(new Color(187, 247, 208), 1, true),
                    new EmptyBorder(3, 8, 3, 8)
            ));
            btnDormirDespertar.setText("<html><center><b>💤 Dormir</b><br><font size='2' color='#64748B'>+25 Energía, +2 Humor</font></center></html>");

            // Emojis según el nivel de humor
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
                        "¡" + mascota.getNombre() + " escuchó su nombre y vino moviendo la colita! 🐶",
                        "Respuesta Positiva",
                        JOptionPane.INFORMATION_MESSAGE
                );
                agregarLog("🗣️ Llamaste a la mascota como '" + nombrePrueba + "' -> ¡Respondió! (true)");
            } else {
                String motivo = mascota.isDormida() ? " (Está profundamente dormida 💤)" : " (No reconoció ese nombre ❌)";
                JOptionPane.showMessageDialog(
                        this,
                        mascota.getNombre() + " no respondió al llamado." + motivo,
                        "Sin Respuesta",
                        JOptionPane.WARNING_MESSAGE
                );
                agregarLog("🗣️ Llamaste a la mascota como '" + nombrePrueba + "' -> No respondió (false)." + motivo);
            }
        }
    }

    private void mostrarEstadoJSON() {
        JOptionPane.showMessageDialog(
                this,
                new JScrollPane(new JTextArea(mascota.toString(), 6, 25)),
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
