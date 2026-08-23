package utnfc.back.mascota;

/**
 * Representa el humor de una mascota.
 *
 * Internamente utiliza un nivel entero entre 1 y 5.
 * Ese valor es un detalle de implementación.
 */
public class Humor {

    private int nivel;

    public Humor(int nivel) {
        this.setNivel(nivel);
    }

    /**
     * Garantiza:
     *
     * 1 <= nivel <= 5
     */
    private void setNivel(int nivel) {
        if (nivel < 1) {
            this.nivel = 1;
        }
        else if (nivel > 5) {
            this.nivel = 5;
        }
        else {
            this.nivel = nivel;
        }
    }

    public void incrementar(int niveles) {
        this.setNivel(this.nivel + niveles);
    }

    public void decrementar(int niveles) {
        this.setNivel(this.nivel - niveles);
    }

    /**
     * Retorna una representación significativa
     * sin exponer la codificación numérica.
     */
    @Override
    public String toString() {
        return switch (this.nivel) {
            case 1 -> "Muy enojado";
            case 2 -> "Enojado";
            case 3 -> "Neutral";
            case 4 -> "Contento";
            case 5 -> "Chocho";
            default -> "Desconocido";
        };
    }
}
