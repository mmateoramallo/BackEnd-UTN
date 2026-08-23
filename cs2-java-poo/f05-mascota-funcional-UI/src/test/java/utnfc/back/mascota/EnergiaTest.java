package utnfc.back.mascota;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("Energía")
class EnergiaTest {

    @Test
    @DisplayName("Conserva un valor inicial válido")
    void debeConservarValorInicial() {

        Energia energia = new Energia(53);

        assertEquals(53, energia.getValor());
    }

    @Test
    @DisplayName("No puede superar las 100 unidades")
    void debeRespetarElMaximo() {

        Energia energia = new Energia(150);

        assertEquals(100, energia.getValor());
    }

    @Test
    @DisplayName("No puede disminuir por debajo de cero")
    void debeRespetarElMinimo() {

        Energia energia = new Energia(-20);

        assertEquals(0, energia.getValor());
    }

    @Test
    @DisplayName("Puede incrementarse una cantidad fija de unidades")
    void debeIncrementarUnidades() {

        Energia energia = new Energia(50);

        energia.incrementar(25);

        assertEquals(75, energia.getValor());
    }

    @Test
    @DisplayName("Un incremento fijo nunca puede superar el máximo")
    void incrementoFijoDebeRespetarMaximo() {

        Energia energia = new Energia(90);

        energia.incrementar(25);

        assertEquals(100, energia.getValor());
    }

    @ParameterizedTest(
        name = "{0} unidades incrementadas un {1}% deben quedar en {2}"
    )
    @CsvSource({
        "50, 10, 55",
        "53, 10, 58",
        "53,  5, 55",
        "80, 10, 88",
        "95, 10, 100"
    })
    @DisplayName("Incrementa porcentajes truncando fracciones de unidad")
    void debeIncrementarPorcentaje(
            int valorInicial,
            int porcentaje,
            int esperado) {

        Energia energia = new Energia(valorInicial);

        energia.incrementarPorcentaje(porcentaje);

        assertEquals(esperado, energia.getValor());
    }

    @ParameterizedTest(
        name = "{0} unidades decrementadas un {1}% deben quedar en {2}"
    )
    @CsvSource({
        "50, 35, 33",
        "53, 35, 35",
        "53, 15, 46",
        "80, 15, 68",
        "10, 35, 7"
    })
    @DisplayName("Decrementa porcentajes truncando fracciones de unidad")
    void debeDecrementarPorcentaje(
            int valorInicial,
            int porcentaje,
            int esperado) {

        Energia energia = new Energia(valorInicial);

        energia.decrementarPorcentaje(porcentaje);

        assertEquals(esperado, energia.getValor());
    }
}
