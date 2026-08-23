package utnfc.back.ui;

import utnfc.back.mascota.Mascota;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

/**
 * Pantalla LCD Retro estilo Tamagotchi con renderizado Pixel-Art animado.
 */
public class PixelMascotaCanvas extends JPanel {

    private Mascota mascota;
    private int selectedIconIndex = -1; // 0..7 (-1 = ninguno)

    // Estados de pantalla
    public enum ScreenMode {
        PET_ANIMATION,
        STATS,
        FOOD_MENU,
        PLAY_MENU,
        LOG_VIEW
    }

    private ScreenMode currentMode = ScreenMode.PET_ANIMATION;
    private int subMenuOption = 0; // 0 o 1 en submenús

    // Variables de animación
    private int frameTick = 0;
    private int petXOffset = 0;
    private int petYOffset = 0;
    private int petDirection = 1;

    // Animación de acción activa
    public enum ActionAnim {
        NONE, EATING, DRINKING, RUNNING, JUMPING, ASLEEP, EMPACADA, DEAD
    }

    private ActionAnim currentAction = ActionAnim.NONE;
    private int actionTimerTicks = 0;
    private String actionMessage = "";

    // Registro de eventos para la pantalla LCD
    private final List<String> recentLogs = new ArrayList<>();

    // Dimensiones de la cuadrícula de píxeles
    private static final int PIXEL_SCALE = 4;

    // Colores retro LCD
    private final Color COLOR_LCD_BG = new Color(158, 189, 138);       // Verde LCD clásico
    private final Color COLOR_LCD_GRID = new Color(148, 178, 128);     // Píxeles sutiles de fondo
    private final Color COLOR_PIXEL_DARK = new Color(34, 48, 28);      // Píxeles oscuros LCD activos
    private final Color COLOR_PIXEL_LIGHT = new Color(92, 120, 78);    // Píxeles en segundo plano
    private final Color COLOR_HIGHLIGHT = new Color(50, 75, 40);       // Borde de selección

    // Nombres e iconos de la barra superior (0..3) e inferior (4..7)
    public static final String[] ICON_SYMBOLS = {"🍴", "💡", "⚾", "🗣️", "⚖️", "✏️", "🐾", "📜"};
    public static final String[] ICON_LABELS = {"Comer", "Luz", "Jugar", "Llamar", "Estado", "Nombre", "Nuevo", "Log"};

    public PixelMascotaCanvas(Mascota mascota) {
        this.mascota = mascota;
        setPreferredSize(new Dimension(380, 320));
        setBackground(COLOR_LCD_BG);

        // Timer de animación a ~15 FPS
        Timer animTimer = new Timer(70, e -> {
            actualizarAnimacion();
            repaint();
        });
        animTimer.start();

        // Soporte para hacer clic directo en los iconos de la pantalla
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                manejarClickPantalla(e.getX(), e.getY());
            }
        });

        addLog("Mascota: " + mascota.getNombre() + " lista.");
    }

    public void setMascota(Mascota nuevaMascota) {
        this.mascota = nuevaMascota;
        this.currentAction = ActionAnim.NONE;
        addLog("Nueva mascota: " + mascota.getNombre());
        repaint();
    }

    public void addLog(String mensaje) {
        recentLogs.add(mensaje);
        if (recentLogs.size() > 20) {
            recentLogs.remove(0);
        }
    }

    public void triggerAction(ActionAnim anim, String mensaje, int durationTicks) {
        this.currentAction = anim;
        this.actionMessage = mensaje;
        this.actionTimerTicks = durationTicks;
        this.currentMode = ScreenMode.PET_ANIMATION;
        addLog(mensaje);
    }

    private void actualizarAnimacion() {
        frameTick++;

        // Manejo de temporizador de acción especial
        if (actionTimerTicks > 0) {
            actionTimerTicks--;
            if (actionTimerTicks == 0) {
                currentAction = ActionAnim.NONE;
                actionMessage = "";
            }
        }

        // Animación idle / caminata
        if (mascota == null || !mascota.isViva()) {
            return;
        }

        if (mascota.isDormida()) {
            petYOffset = 0;
            return;
        }

        if (currentAction == ActionAnim.RUNNING) {
            petXOffset += petDirection * 6;
            if (petXOffset > 70 || petXOffset < -70) {
                petDirection *= -1;
            }
        } else if (currentAction == ActionAnim.JUMPING) {
            // Curva de salto
            int phase = frameTick % 12;
            if (phase < 6) {
                petYOffset = -phase * 4;
            } else {
                petYOffset = -(12 - phase) * 4;
            }
        } else {
            // Idle suave
            if (frameTick % 20 == 0) {
                petDirection = (Math.random() > 0.5) ? 1 : -1;
            }
            if (frameTick % 8 == 0) {
                petXOffset += petDirection * 2;
                if (petXOffset > 40) petXOffset = 40;
                if (petXOffset < -40) petXOffset = -40;
            }
            petYOffset = (frameTick / 6 % 2 == 0) ? -2 : 0;
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);

        int w = getWidth();
        int h = getHeight();

        // 1. Fondo con rejilla LCD matricial
        dibujarMatrizLCD(g2, w, h);

        // 2. Barra Superior de Iconos
        dibujarBarraIconos(g2, 0, 4, 10, true);

        // 3. Contenido Central (Animación o Vistas Especiales)
        switch (currentMode) {
            case STATS -> dibujarVistaEstado(g2, w, h);
            case FOOD_MENU -> dibujarSubMenu(g2, "🍴 MENU COMIDA", "1. Comer (+10% E)", "2. Beber (+5% E)", w, h);
            case PLAY_MENU -> dibujarSubMenu(g2, "⚾ MENU ACTIVIDAD", "1. Correr (-35% E)", "2. Saltar (-15% E)", w, h);
            case LOG_VIEW -> dibujarVistaLogs(g2, w, h);
            case PET_ANIMATION -> dibujarAreaMascota(g2, w, h);
        }

        // 4. Barra Inferior de Iconos
        dibujarBarraIconos(g2, 4, 8, h - 35, false);

        // 5. Borde interior LCD
        g2.setColor(COLOR_PIXEL_DARK);
        g2.drawRect(2, 2, w - 5, h - 5);
        g2.drawRect(4, 4, w - 9, h - 9);
    }

    private void dibujarMatrizLCD(Graphics2D g, int w, int h) {
        g.setColor(COLOR_LCD_GRID);
        for (int x = 6; x < w - 6; x += 4) {
            for (int y = 6; y < h - 6; y += 4) {
                g.fillRect(x, y, 1, 1);
            }
        }
    }

    private void dibujarBarraIconos(Graphics2D g, int inicio, int fin, int yPos, boolean superior) {
        int w = getWidth();
        int espacio = (w - 20) / 4;

        g.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 18));
        for (int i = inicio; i < fin; i++) {
            int x = 12 + (i % 4) * espacio;
            boolean seleccionado = (i == selectedIconIndex);

            if (seleccionado) {
                g.setColor(COLOR_HIGHLIGHT);
                g.fillRoundRect(x - 2, yPos - 3, 28, 26, 6, 6);
                g.setColor(COLOR_LCD_BG);
            } else {
                g.setColor(COLOR_PIXEL_DARK);
            }

            g.drawString(ICON_SYMBOLS[i], x + 3, yPos + 17);
        }
    }

    /**
     * Dibuja el sprite animado de la mascota en el centro de la pantalla.
     */
    private void dibujarAreaMascota(Graphics2D g, int w, int h) {
        int centroX = w / 2 + petXOffset;
        int centroY = h / 2 + petYOffset - 5;

        // Nombre de la mascota en pixel font arriba
        g.setColor(COLOR_PIXEL_DARK);
        g.setFont(new Font("Monospaced", Font.BOLD, 13));
        String rotulo = mascota != null ? mascota.getNombre() : "PET";
        int strW = g.getFontMetrics().stringWidth(rotulo);
        g.drawString(rotulo, (w - strW) / 2, 52);

        // Si hay un mensaje de acción en curso
        if (!actionMessage.isEmpty()) {
            g.setFont(new Font("Monospaced", Font.BOLD, 11));
            int msgW = g.getFontMetrics().stringWidth(actionMessage);
            g.drawString(actionMessage, Math.max(10, (w - msgW) / 2), 70);
        }

        if (mascota == null) return;

        // Determinar qué sprite dibujar
        if (!mascota.isViva()) {
            dibujarSpriteFantasma(g, centroX, centroY);
        } else if (mascota.isDormida()) {
            dibujarSpriteDurmiendo(g, centroX, centroY);
        } else if (currentAction == ActionAnim.EATING) {
            dibujarSpriteComiendo(g, centroX, centroY);
        } else if (currentAction == ActionAnim.DRINKING) {
            dibujarSpriteBebiendo(g, centroX, centroY);
        } else if (currentAction == ActionAnim.RUNNING) {
            dibujarSpriteCorriendo(g, centroX, centroY);
        } else if (currentAction == ActionAnim.JUMPING) {
            dibujarSpriteSaltando(g, centroX, centroY);
        } else {
            dibujarSpriteIdle(g, centroX, centroY);
        }
    }

    // ========================================================================
    // SPRITES PIXEL ART (MATRICES DE PÍXELES PROCEDURALES)
    // ========================================================================

    private void dibujarPixel(Graphics2D g, int x, int y, int size) {
        g.fillRect(x, y, size, size);
    }

    /**
     * Sprite Idle clásico Tamagotchi (Conejito / Gatito rechoncho que respira).
     */
    private void dibujarSpriteIdle(Graphics2D g, int cx, int cy) {
        g.setColor(COLOR_PIXEL_DARK);
        int s = PIXEL_SCALE;
        int step = (frameTick / 6) % 2;

        int ox = cx - 7 * s;
        int oy = cy - 8 * s;

        // Orejas
        dibujarPixel(g, ox + 1 * s, oy + 0 * s, s * 2);
        dibujarPixel(g, ox + 2 * s, oy + 1 * s, s * 2);
        dibujarPixel(g, ox + 9 * s, oy + 0 * s, s * 2);
        dibujarPixel(g, ox + 8 * s, oy + 1 * s, s * 2);

        // Cuerpo / Cabeza
        g.fillRoundRect(ox + 1 * s, oy + 3 * s, 11 * s, 10 * s, 4, 4);

        // Ojos y Expresión según Humor
        dibujarExpresionHumor(g, ox, oy, s);

        // Patitas con balanceo
        if (step == 0) {
            dibujarPixel(g, ox + 2 * s, oy + 13 * s, s * 2);
            dibujarPixel(g, ox + 8 * s, oy + 13 * s, s * 2);
        } else {
            dibujarPixel(g, ox + 3 * s, oy + 13 * s, s * 2);
            dibujarPixel(g, ox + 7 * s, oy + 13 * s, s * 2);
        }
    }

    private void dibujarExpresionHumor(Graphics2D g, int ox, int oy, int s) {
        String humor = mascota.getHumor();
        g.setColor(COLOR_LCD_BG); // Píxeles transparentes/inversos para los ojos

        switch (humor) {
            case "Chocho" -> {
                // Ojos brillantes / estrellas y corazoncito flotante
                dibujarPixel(g, ox + 3 * s, oy + 5 * s, s);
                dibujarPixel(g, ox + 4 * s, oy + 6 * s, s);
                dibujarPixel(g, ox + 8 * s, oy + 5 * s, s);
                dibujarPixel(g, ox + 7 * s, oy + 6 * s, s);
                // Boca sonriente
                dibujarPixel(g, ox + 5 * s, oy + 8 * s, s * 2);

                // Corazón flotante
                g.setColor(COLOR_PIXEL_DARK);
                int hy = oy - ((frameTick % 10) * s / 3);
                g.drawString("♥", ox + 12 * s, hy);
            }
            case "Contento" -> {
                // Ojos felices ^ ^
                dibujarPixel(g, ox + 3 * s, oy + 6 * s, s * 2);
                dibujarPixel(g, ox + 7 * s, oy + 6 * s, s * 2);
                // Sonrisa
                dibujarPixel(g, ox + 5 * s, oy + 8 * s, s * 2);
            }
            case "Enojado" -> {
                // Cejas fruncidas \ /
                g.setColor(COLOR_PIXEL_DARK);
                dibujarPixel(g, ox + 2 * s, oy + 4 * s, s * 2);
                dibujarPixel(g, ox + 8 * s, oy + 4 * s, s * 2);

                g.setColor(COLOR_LCD_BG);
                dibujarPixel(g, ox + 3 * s, oy + 6 * s, s);
                dibujarPixel(g, ox + 8 * s, oy + 6 * s, s);
                dibujarPixel(g, ox + 5 * s, oy + 9 * s, s * 2);
            }
            case "Muy enojado" -> {
                // Cejas furiossas + boca recta + vapor
                g.setColor(COLOR_LCD_BG);
                dibujarPixel(g, ox + 3 * s, oy + 6 * s, s * 2);
                dibujarPixel(g, ox + 7 * s, oy + 6 * s, s * 2);
                dibujarPixel(g, ox + 4 * s, oy + 9 * s, s * 4);

                g.setColor(COLOR_PIXEL_DARK);
                if (frameTick % 4 < 2) {
                    g.drawString("♨", ox - 2 * s, oy + 2 * s);
                    g.drawString("♨", ox + 11 * s, oy + 2 * s);
                }
            }
            default -> { // Neutral
                dibujarPixel(g, ox + 3 * s, oy + 6 * s, s * 2);
                dibujarPixel(g, ox + 7 * s, oy + 6 * s, s * 2);
                dibujarPixel(g, ox + 5 * s, oy + 8 * s, s * 2);
            }
        }
    }

    private void dibujarSpriteDurmiendo(Graphics2D g, int cx, int cy) {
        g.setColor(COLOR_PIXEL_DARK);
        int s = PIXEL_SCALE;
        int ox = cx - 8 * s;
        int oy = cy - 2 * s;

        // Cuerpo acostado
        g.fillRoundRect(ox, oy, 14 * s, 8 * s, 6, 6);

        // Ojos cerrados (- -)
        g.setColor(COLOR_LCD_BG);
        dibujarPixel(g, ox + 2 * s, oy + 3 * s, s * 3);
        dibujarPixel(g, ox + 8 * s, oy + 3 * s, s * 3);

        // Letras "Z z z" animadas flotantes
        g.setColor(COLOR_PIXEL_DARK);
        int zPhase = (frameTick / 4) % 4;
        g.setFont(new Font("Monospaced", Font.BOLD, 12 + zPhase * 2));
        g.drawString("Z", ox + 14 * s + zPhase * 3, oy - zPhase * 5);
        if (zPhase > 1) {
            g.setFont(new Font("Monospaced", Font.BOLD, 10));
            g.drawString("z", ox + 11 * s, oy - 15);
        }
    }

    private void dibujarSpriteFantasma(Graphics2D g, int cx, int cy) {
        g.setColor(COLOR_PIXEL_DARK);
        int s = PIXEL_SCALE;
        int floatY = (int) (Math.sin(frameTick * 0.2) * 5);
        int ox = cx - 6 * s;
        int oy = cy - 8 * s + floatY;

        // Halo de ángel
        g.drawOval(ox + 2 * s, oy - 4 * s, 8 * s, 2 * s);

        // Cuerpo de fantasma
        g.fillRoundRect(ox, oy, 12 * s, 12 * s, 8, 8);

        // Cola ondulada
        int tail = (frameTick / 4) % 2;
        if (tail == 0) {
            dibujarPixel(g, ox + 1 * s, oy + 12 * s, s * 2);
            dibujarPixel(g, ox + 5 * s, oy + 12 * s, s * 2);
            dibujarPixel(g, ox + 9 * s, oy + 12 * s, s * 2);
        } else {
            dibujarPixel(g, ox + 3 * s, oy + 12 * s, s * 2);
            dibujarPixel(g, ox + 7 * s, oy + 12 * s, s * 2);
        }

        // Ojos de muerto (X X)
        g.setColor(COLOR_LCD_BG);
        g.drawString("x", ox + 2 * s, oy + 6 * s);
        g.drawString("x", ox + 7 * s, oy + 6 * s);

        // Lápida pequeña al lado
        g.setColor(COLOR_PIXEL_DARK);
        g.setFont(new Font("Monospaced", Font.BOLD, 11));
        g.drawString("🪦 R.I.P", cx + 25, cy + 20);
    }

    private void dibujarSpriteComiendo(Graphics2D g, int cx, int cy) {
        dibujarSpriteIdle(g, cx, cy);
        int s = PIXEL_SCALE;

        // Animación de comida (Hamburguesa / Manzana) acercándose a la boca
        int bitePhase = (frameTick / 3) % 4;
        g.setColor(COLOR_PIXEL_DARK);
        g.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 18));
        g.drawString("🍔", cx - 8 * s - bitePhase * 3, cy);

        if (bitePhase == 2 || bitePhase == 3) {
            g.setFont(new Font("Monospaced", Font.BOLD, 11));
            g.drawString("nom!", cx + 8 * s, cy - 10);
        }
    }

    private void dibujarSpriteBebiendo(Graphics2D g, int cx, int cy) {
        dibujarSpriteIdle(g, cx, cy);
        int s = PIXEL_SCALE;
        g.setColor(COLOR_PIXEL_DARK);
        g.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 18));
        g.drawString("🥤", cx - 7 * s, cy - 2);

        g.setFont(new Font("Monospaced", Font.BOLD, 11));
        g.drawString("glup!", cx + 8 * s, cy - 10);
    }

    private void dibujarSpriteCorriendo(Graphics2D g, int cx, int cy) {
        dibujarSpriteIdle(g, cx, cy);
        // Nube de polvo de velocidad
        g.setColor(COLOR_PIXEL_DARK);
        int dust = (frameTick % 3);
        g.drawString("💨", cx - petDirection * 35, cy + 10 + dust * 2);
    }

    private void dibujarSpriteSaltando(Graphics2D g, int cx, int cy) {
        dibujarSpriteIdle(g, cx, cy);
        // Estrellitas de salto
        g.setColor(COLOR_PIXEL_DARK);
        g.drawString("✨", cx - 25, cy - 15);
        g.drawString("✨", cx + 20, cy - 15);
    }

    // ========================================================================
    // VISTAS ESPECIALES EN LA PANTALLA LCD (STATS, MENUS, LOGS)
    // ========================================================================

    private void dibujarVistaEstado(Graphics2D g, int w, int h) {
        g.setColor(COLOR_PIXEL_DARK);
        g.setFont(new Font("Monospaced", Font.BOLD, 13));

        g.drawString("=== ESTADO MASCOTA ===", 20, 55);

        if (mascota == null) return;

        // Nombre y Vida
        g.drawString("Nombre: " + mascota.getNombre(), 20, 80);
        g.drawString("Estado: " + (mascota.isViva() ? (mascota.isDormida() ? "Dormida 💤" : "Viva ❤️") : "Muerta 💀"), 20, 105);

        // Barra de energía gráfica
        int energia = mascota.getEnergia();
        g.drawString("Energía: " + energia + "/100", 20, 130);
        dibujarBarraRetro(g, 20, 140, 160, 14, energia, 100);

        // Humor
        g.drawString("Humor:   " + mascota.getHumor(), 20, 175);

        g.setFont(new Font("Monospaced", Font.PLAIN, 11));
        g.drawString("[Presiona (C) para volver]", 20, 230);
    }

    private void dibujarBarraRetro(Graphics2D g, int x, int y, int bw, int bh, int valor, int max) {
        g.drawRect(x, y, bw, bh);
        int fillW = (int) ((valor / (double) max) * (bw - 4));
        if (fillW > 0) {
            g.fillRect(x + 2, y + 2, fillW, bh - 3);
        }
    }

    private void dibujarSubMenu(Graphics2D g, String titulo, String opc1, String opc2, int w, int h) {
        g.setColor(COLOR_PIXEL_DARK);
        g.setFont(new Font("Monospaced", Font.BOLD, 13));
        g.drawString(titulo, 25, 60);

        g.setFont(new Font("Monospaced", Font.PLAIN, 13));

        // Opción 1
        if (subMenuOption == 0) {
            g.drawString("▶ " + opc1, 20, 100);
            g.drawString("  " + opc2, 20, 130);
        } else {
            g.drawString("  " + opc1, 20, 100);
            g.drawString("▶ " + opc2, 20, 130);
        }

        g.setFont(new Font("Monospaced", Font.PLAIN, 11));
        g.drawString("(A) Cambiar | (B) OK | (C) Salir", 20, 210);
    }

    private void dibujarVistaLogs(Graphics2D g, int w, int h) {
        g.setColor(COLOR_PIXEL_DARK);
        g.setFont(new Font("Monospaced", Font.BOLD, 12));
        g.drawString("=== REGISTRO DE EVENTOS ===", 15, 55);

        g.setFont(new Font("Monospaced", Font.PLAIN, 10));
        int y = 80;
        int start = Math.max(0, recentLogs.size() - 6);
        for (int i = start; i < recentLogs.size(); i++) {
            String log = recentLogs.get(i);
            if (log.length() > 38) log = log.substring(0, 35) + "...";
            g.drawString("• " + log, 15, y);
            y += 18;
        }

        g.drawString("[Presiona (C) para volver]", 15, 230);
    }

    // ========================================================================
    // INTERACCIÓN Y BOTONES (A, B, C)
    // ========================================================================

    public void botonAPresionado() {
        if (currentMode == ScreenMode.FOOD_MENU || currentMode == ScreenMode.PLAY_MENU) {
            subMenuOption = (subMenuOption == 0) ? 1 : 0;
        } else if (currentMode == ScreenMode.PET_ANIMATION) {
            selectedIconIndex = (selectedIconIndex + 1) % 8;
        }
        repaint();
    }

    public void botonBPresionado() {
        if (currentMode == ScreenMode.FOOD_MENU) {
            if (subMenuOption == 0) {
                ejecutarComer();
            } else {
                ejecutarBeber();
            }
            currentMode = ScreenMode.PET_ANIMATION;
        } else if (currentMode == ScreenMode.PLAY_MENU) {
            if (subMenuOption == 0) {
                ejecutarCorrer();
            } else {
                ejecutarSaltar();
            }
            currentMode = ScreenMode.PET_ANIMATION;
        } else if (currentMode == ScreenMode.STATS || currentMode == ScreenMode.LOG_VIEW) {
            currentMode = ScreenMode.PET_ANIMATION;
        } else {
            // Ejecutar según el icono seleccionado
            ejecutarIconoSeleccionado();
        }
        repaint();
    }

    public void botonCPresionado() {
        currentMode = ScreenMode.PET_ANIMATION;
        selectedIconIndex = -1;
        repaint();
    }

    private void ejecutarIconoSeleccionado() {
        if (selectedIconIndex == -1) selectedIconIndex = 0;

        switch (selectedIconIndex) {
            case 0 -> { // Comida
                currentMode = ScreenMode.FOOD_MENU;
                subMenuOption = 0;
            }
            case 1 -> { // Luz (Dormir / Despertar)
                alternarDescanso();
            }
            case 2 -> { // Actividad (Correr / Saltar)
                currentMode = ScreenMode.PLAY_MENU;
                subMenuOption = 0;
            }
            case 3 -> { // Llamar
                solicitarLlamar();
            }
            case 4 -> { // Estado
                currentMode = ScreenMode.STATS;
            }
            case 5 -> { // Renombrar
                solicitarRenombrar();
            }
            case 6 -> { // Adopción
                solicitarAdopcion();
            }
            case 7 -> { // Historial
                currentMode = ScreenMode.LOG_VIEW;
            }
        }
    }

    private void manejarClickPantalla(int mx, int my) {
        int w = getWidth();
        int espacio = (w - 20) / 4;

        // Clic en barra superior
        if (my >= 5 && my <= 35) {
            int col = (mx - 10) / espacio;
            if (col >= 0 && col < 4) {
                selectedIconIndex = col;
                ejecutarIconoSeleccionado();
                repaint();
                return;
            }
        }

        // Clic en barra inferior
        if (my >= getHeight() - 40 && my <= getHeight() - 10) {
            int col = (mx - 10) / espacio;
            if (col >= 0 && col < 4) {
                selectedIconIndex = 4 + col;
                ejecutarIconoSeleccionado();
                repaint();
                return;
            }
        }

        // Clic central para salir de menús
        if (currentMode != ScreenMode.PET_ANIMATION) {
            currentMode = ScreenMode.PET_ANIMATION;
            repaint();
        }
    }

    // ========================================================================
    // ACCIONES DE DOMINIO DE MASCOTA
    // ========================================================================

    public void ejecutarComer() {
        if (mascota == null) return;
        boolean vivaAntes = mascota.isViva();
        boolean res = mascota.comer();
        if (res) {
            if (!mascota.isViva() && vivaAntes) {
                triggerAction(ActionAnim.DEAD, "¡Falleció de empacho! 💀", 60);
            } else {
                triggerAction(ActionAnim.EATING, "¡Comiendo rico! (+10% E)", 25);
            }
        } else {
            triggerAction(ActionAnim.NONE, mascota.isDormida() ? "¡Está dormida!" : "¡No responde!", 20);
        }
    }

    public void ejecutarBeber() {
        if (mascota == null) return;
        boolean vivaAntes = mascota.isViva();
        boolean res = mascota.beber();
        if (res) {
            if (!mascota.isViva() && vivaAntes) {
                triggerAction(ActionAnim.DEAD, "¡Falleció de empacho! 💀", 60);
            } else {
                triggerAction(ActionAnim.DRINKING, "¡Bebiendo agua! (+5% E)", 25);
            }
        } else {
            triggerAction(ActionAnim.NONE, mascota.isDormida() ? "¡Está dormida!" : "¡No responde!", 20);
        }
    }

    public void ejecutarCorrer() {
        if (mascota == null) return;
        boolean vivaAntes = mascota.isViva();
        boolean dormidaAntes = mascota.isDormida();
        boolean res = mascota.correr();
        if (res) {
            if (!mascota.isViva() && vivaAntes) {
                triggerAction(ActionAnim.DEAD, "¡Agotamiento total! 💀", 60);
            } else if (mascota.isDormida() && !dormidaAntes) {
                triggerAction(ActionAnim.EMPACADA, "¡Se empacó y se durmió! 😴", 40);
            } else {
                triggerAction(ActionAnim.RUNNING, "¡Corriendo rápido! (-35% E)", 30);
            }
        } else {
            triggerAction(ActionAnim.NONE, mascota.isDormida() ? "¡Está dormida!" : "¡No responde!", 20);
        }
    }

    public void ejecutarSaltar() {
        if (mascota == null) return;
        boolean vivaAntes = mascota.isViva();
        boolean dormidaAntes = mascota.isDormida();
        boolean res = mascota.saltar();
        if (res) {
            if (!mascota.isViva() && vivaAntes) {
                triggerAction(ActionAnim.DEAD, "¡Agotamiento total! 💀", 60);
            } else if (mascota.isDormida() && !dormidaAntes) {
                triggerAction(ActionAnim.EMPACADA, "¡Se empacó y se durmió! 😴", 40);
            } else {
                triggerAction(ActionAnim.JUMPING, "¡Saltando alegre! (-15% E)", 25);
            }
        } else {
            triggerAction(ActionAnim.NONE, mascota.isDormida() ? "¡Está dormida!" : "¡No responde!", 20);
        }
    }

    public void alternarDescanso() {
        if (mascota == null || !mascota.isViva()) {
            triggerAction(ActionAnim.NONE, "¡La mascota ha fallecido!", 25);
            return;
        }

        if (mascota.isDormida()) {
            mascota.despertar();
            triggerAction(ActionAnim.NONE, "¡Se ha despertado! ☀️", 25);
        } else {
            mascota.dormir();
            triggerAction(ActionAnim.ASLEEP, "A dormir... 💤 (+25 E)", 35);
        }
    }

    private void solicitarLlamar() {
        String test = JOptionPane.showInputDialog(this, "¿Con qué nombre la llamas?", "Llamar Mascota", JOptionPane.PLAIN_MESSAGE);
        if (test != null && !test.trim().isEmpty()) {
            boolean resp = mascota.respondeA(test.trim());
            if (resp) {
                triggerAction(ActionAnim.JUMPING, "¡Vino contenta! 🐶", 30);
            } else {
                triggerAction(ActionAnim.NONE, mascota.isDormida() ? "ZZZ... (Dormida)" : "No respondió ❌", 25);
            }
        }
    }

    private void solicitarRenombrar() {
        String n = JOptionPane.showInputDialog(this, "Nuevo nombre:", "Renombrar", JOptionPane.PLAIN_MESSAGE);
        if (n != null && !n.trim().isEmpty()) {
            mascota.setNombre(n.trim());
            addLog("Nombre cambiado a " + n.trim());
            repaint();
        }
    }

    private void solicitarAdopcion() {
        String n = JOptionPane.showInputDialog(this, "Nombre de la nueva mascota:", "Adoptar Mascota", JOptionPane.PLAIN_MESSAGE);
        if (n != null && !n.trim().isEmpty()) {
            setMascota(new Mascota(n.trim()));
        }
    }
}
