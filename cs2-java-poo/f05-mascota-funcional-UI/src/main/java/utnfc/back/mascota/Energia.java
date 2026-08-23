package utnfc.back.mascota;

/**
 * Representa la energía de una mascota.
 *
 * La energía se expresa mediante unidades enteras
 * y siempre debe mantenerse entre 0 y 100.
 */
public class Energia {

    private int valor;

    public Energia(int valor) {
        this.setValor(valor);
    }

    public int getValor() {
        return this.valor;
    }

    /**
     * Garantiza la invariante:
     *
     * 0 <= valor <= 100
     */
    private void setValor(int valor) {
        if (valor < 0) {
            this.valor = 0;
        }
        else if (valor > 100) {
            this.valor = 100;
        }
        else {
            this.valor = valor;
        }
    }

    public void incrementar(int unidades) {
        this.setValor(this.valor + unidades);
    }

    /**
     * Incrementa un porcentaje respecto del valor actual.
     * Las fracciones de unidad se descartan.
     */
    public void incrementarPorcentaje(int porcentaje) {
        int incremento =
                (int) (this.valor * porcentaje / 100.0);

        this.incrementar(incremento);
    }

    /**
     * Decrementa un porcentaje respecto del valor actual.
     * Las fracciones de unidad se descartan.
     */
    public void decrementarPorcentaje(int porcentaje) {
        int decremento =
                (int) (this.valor * porcentaje / 100.0);

        this.setValor(this.valor - decremento);
    }
}
