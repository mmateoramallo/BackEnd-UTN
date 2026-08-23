package utnfc.back.mascota;

/**
 * Representa una mascota con identidad,
 * descanso, energía y humor.
 *
 * Energia y Humor son objetos responsables
 * de gestionar su propio estado.
 */
public class Mascota {

    private String nombre;
    private Energia energia;
    private Humor humor;
    private boolean dormida;

    public Mascota(String nombre) {
        this(nombre, 50, 3);
    }

    public Mascota(String nombre, int energia, int humor) {
        this.nombre = nombre;
        this.energia = new Energia(energia);
        this.humor = new Humor(humor);
        this.dormida = false;
    }

    public String getNombre() {
        return this.nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Conservamos el contrato público.
     * El consumidor sigue obteniendo un int.
     */
    public int getEnergia() {
        return this.energia.getValor();
    }

    /**
     * Mascota delega la representación al objeto Humor.
     */
    public String getHumor() {
        return this.humor.toString();
    }

    public boolean isDormida() {
        return this.dormida;
    }

    public void dormir() {
        if (this.dormida) {
            return;
        }

        this.dormida = true;

        this.energia.incrementar(25);
        this.humor.incrementar(2);
    }

    public void despertar() {
        if (!this.dormida) {
            return;
        }

        this.dormida = false;

        this.humor.decrementar(1);
    }

    public boolean respondeA(String nombre) {
        if (this.dormida) {
            return false;
        }

        return this.nombre.equals(nombre);
    }

    public boolean comer() {
        if (this.dormida) {
            return false;
        }

        this.energia.incrementarPorcentaje(10);
        this.humor.incrementar(1);

        return true;
    }

    public boolean beber() {
        if (this.dormida) {
            return false;
        }

        this.energia.incrementarPorcentaje(5);
        this.humor.incrementar(1);

        return true;
    }

    public boolean correr() {
        if (this.dormida) {
            return false;
        }

        this.energia.decrementarPorcentaje(35);
        this.humor.decrementar(2);

        return true;
    }

    public boolean saltar() {
        if (this.dormida) {
            return false;
        }

        this.energia.decrementarPorcentaje(15);
        this.humor.decrementar(2);

        return true;
    }

    @Override
    public String toString() {
        return """
                {
                  "nombre": "%s",
                  "energia": %d,
                  "humor": "%s",
                  "dormida": %b
                }""".formatted(
                    this.nombre,
                    this.energia.getValor(),
                    this.humor,
                    this.dormida
                );
    }
}
