package utnfc.back.mascota;

/**
 * Representa una mascota virtual con identidad, descanso,
 * energía, humor, vida y reglas de interacción consecutivas.
 *
 * Fase 05 — Mascota Funcional.
 */
public class Mascota {

    private String nombre;
    private Energia energia;
    private Humor humor;
    private boolean dormida;
    private boolean viva;

    // Control de rachas consecutivas
    private int rachaIngestas;
    private int rachaActividades;

    public Mascota(String nombre) {
        this(nombre, 50, 3);
    }

    public Mascota(String nombre, int energia, int humor) {
        this.nombre = nombre;
        this.energia = new Energia(energia);
        this.humor = new Humor(humor);
        this.dormida = false;
        this.viva = this.energia.getValor() > 0;
        this.rachaIngestas = 0;
        this.rachaActividades = 0;
    }

    public String getNombre() {
        return this.nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getEnergia() {
        return this.energia.getValor();
    }

    public String getHumor() {
        return this.humor.toString();
    }

    public boolean isDormida() {
        return this.dormida;
    }

    public boolean isViva() {
        return this.viva;
    }

    public boolean respondeA(String nombre) {
        if (!this.viva || this.dormida) {
            return false;
        }

        return this.nombre.equals(nombre);
    }

    public void dormir() {
        if (!this.viva || this.dormida) {
            return;
        }

        this.dormida = true;
        this.energia.incrementar(25);
        this.humor.incrementar(2);

        // Corta las rachas
        this.rachaIngestas = 0;
        this.rachaActividades = 0;
    }

    public void despertar() {
        if (!this.viva || !this.dormida) {
            return;
        }

        this.dormida = false;
        this.humor.decrementar(1);

        // Corta las rachas
        this.rachaIngestas = 0;
        this.rachaActividades = 0;
    }

    public boolean comer() {
        return procesarIngesta(10);
    }

    public boolean beber() {
        return procesarIngesta(5);
    }

    private boolean procesarIngesta(int porcentajeEnergia) {
        if (!this.viva || this.dormida) {
            return false;
        }

        // Corta racha de actividades
        this.rachaActividades = 0;
        this.rachaIngestas++;

        // Incremento de energía
        this.energia.incrementarPorcentaje(porcentajeEnergia);

        // Reglas de racha de ingesta
        if (this.rachaIngestas == 5) {
            // Muerte por empacho
            this.viva = false;
            this.energia = new Energia(0);
            return true;
        }

        if (this.rachaIngestas >= 3) {
            // A partir de la 3ra ingesta consecutiva, humor -1
            this.humor.decrementar(1);
        } else {
            // 1ra y 2da ingesta consecutiva, humor +1
            this.humor.incrementar(1);
        }

        return true;
    }

    public boolean correr() {
        return procesarActividad(35);
    }

    public boolean saltar() {
        return procesarActividad(15);
    }

    private boolean procesarActividad(int porcentajeEnergia) {
        if (!this.viva || this.dormida) {
            return false;
        }

        // Corta racha de ingestas
        this.rachaIngestas = 0;
        this.rachaActividades++;

        // Reducción de energía y humor
        this.energia.decrementarPorcentaje(porcentajeEnergia);
        this.humor.decrementar(2);

        // Muerte por agotamiento
        if (this.energia.getValor() == 0) {
            this.viva = false;
            return true;
        }

        // Empaque por 3 actividades consecutivas -> se duerme
        if (this.rachaActividades == 3) {
            this.dormida = true;
            this.rachaActividades = 0;
        }

        return true;
    }

    @Override
    public String toString() {
        return """
                {
                  "nombre": "%s",
                  "energia": %d,
                  "humor": "%s",
                  "dormida": %b,
                  "viva": %b
                }""".formatted(
                    this.nombre,
                    this.energia.getValor(),
                    this.humor,
                    this.dormida,
                    this.viva
                );
    }
}
