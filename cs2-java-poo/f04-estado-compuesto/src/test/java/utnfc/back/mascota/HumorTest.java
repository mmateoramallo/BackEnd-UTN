package utnfc.back.mascota;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

//@Disabled("Se habilitará al implementar la clase Humor")
@DisplayName("Humor")
class HumorTest {

    @ParameterizedTest(name = "Nivel {0} se representa como \"{1}\"")
    @CsvSource({
        "1, 'Muy enojado'",
        "2, 'Enojado'",
        "3, 'Neutral'",
        "4, 'Contento'",
        "5, 'Chocho'"
    })
    @DisplayName("Cada nivel tiene una representación textual")
    void debeRepresentarCadaNivel(
            int nivel,
            String descripcionEsperada) {

        Humor humor = new Humor(nivel);

        assertEquals(descripcionEsperada, humor.toString());
    }

    @Test
    @DisplayName("Un valor superior a cinco queda limitado a Chocho")
    void debeRespetarElMaximo() {

        Humor humor = new Humor(20);

        assertEquals("Chocho", humor.toString());
    }

    @Test
    @DisplayName("Un valor inferior a uno queda limitado a Muy enojado")
    void debeRespetarElMinimo() {

        Humor humor = new Humor(-10);

        assertEquals("Muy enojado", humor.toString());
    }

    @Test
    @DisplayName("Puede incrementar un nivel")
    void debeIncrementarUnNivel() {

        Humor humor = new Humor(3);

        humor.incrementar(1);

        assertEquals("Contento", humor.toString());
    }

    @Test
    @DisplayName("Puede incrementar varios niveles")
    void debeIncrementarVariosNiveles() {

        Humor humor = new Humor(2);

        humor.incrementar(2);

        assertEquals("Contento", humor.toString());
    }

    @Test
    @DisplayName("Incrementar nunca permite superar Chocho")
    void incrementarDebeRespetarElMaximo() {

        Humor humor = new Humor(4);

        humor.incrementar(10);

        assertEquals("Chocho", humor.toString());
    }

    @Test
    @DisplayName("Puede decrementar niveles")
    void debeDecrementarNiveles() {

        Humor humor = new Humor(4);

        humor.decrementar(2);

        assertEquals("Enojado", humor.toString());
    }

    @Test
    @DisplayName("Decrementar nunca permite bajar de Muy enojado")
    void decrementarDebeRespetarElMinimo() {

        Humor humor = new Humor(2);

        humor.decrementar(10);

        assertEquals("Muy enojado", humor.toString());
    }
}
