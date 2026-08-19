package utnfc.back.mascota;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

@DisplayName("Mascota")
class MascotaTest {

    // ========================================================================
    // FASE 01 - IDENTIDAD
    // ========================================================================
    @Test
    @DisplayName("La mascota conserva el nombre indicado al crearla")
    void debeConservarElNombreInicial() {

        Mascota mascota = new Mascota("Ahsoka");

        assertEquals("Ahsoka", mascota.getNombre());
    }

    @Test
    @DisplayName("El nombre de la mascota puede modificarse")
    void debePermitirCambiarElNombre() {

        Mascota mascota = new Mascota("Ahsoka");

        mascota.setNombre("Tamagotchi");

        assertEquals("Tamagotchi", mascota.getNombre());
    }

    // ========================================================================
    // FASE 02 - DESCANSO
    // ========================================================================
    @Test
    @DisplayName("Una mascota recién creada comienza despierta")
    void debeComenzarDespierta() {

        Mascota mascota = new Mascota("Ahsoka");

        assertFalse(mascota.isDormida());
    }

    @Test
    @DisplayName("Una mascota despierta responde a su nombre")
    void debeResponderASuNombreSiEstaDespierta() {

        Mascota mascota = new Mascota("Ahsoka");

        assertTrue(mascota.respondeA("Ahsoka"));
    }

    @Test
    @DisplayName("Una mascota despierta no responde a otro nombre")
    void noDebeResponderAOtroNombre() {

        Mascota mascota = new Mascota("Ahsoka");

        assertFalse(mascota.respondeA("Grogu"));
    }

    @Test
    @DisplayName("Dormir cambia el estado de la mascota")
    void dormirDebeCambiarElEstado() {

        Mascota mascota = new Mascota("Ahsoka");

        mascota.dormir();

        assertTrue(mascota.isDormida());
    }

    @Test
    @DisplayName("Una mascota dormida no responde pero conserva su nombre")
    void mascotaDormidaNoRespondePeroConservaSuNombre() {

        Mascota mascota = new Mascota("Ahsoka");

        mascota.dormir();

        assertAll(
                "Estado de una mascota dormida",
                () -> assertTrue(mascota.isDormida()),
                () -> assertFalse(mascota.respondeA("Ahsoka")),
                () -> assertEquals("Ahsoka", mascota.getNombre())
        );
    }

    @Test
    @DisplayName("Una mascota vuelve a responder cuando despierta")
    void mascotaVuelveAResponderAlDespertar() {

        Mascota mascota = new Mascota("Ahsoka");

        mascota.dormir();
        mascota.despertar();

        assertAll(
                "Estado después de despertar",
                () -> assertFalse(mascota.isDormida()),
                () -> assertTrue(mascota.respondeA("Ahsoka")),
                () -> assertEquals("Ahsoka", mascota.getNombre())
        );
    }

    // ========================================================================
    // FASE 03 - ESTADO BASE
    // ========================================================================
    @Test
    @DisplayName("Una mascota creada solamente con nombre comienza con energía 50 y humor Neutral")
    void debeComenzarConEstadoInicial() {

        Mascota mascota = new Mascota("Ahsoka");

        assertAll(
                "Estado inicial",
                () -> assertEquals(50, mascota.getEnergia()),
                () -> assertEquals("Neutral", mascota.getHumor())
        );
    }

    // ------------------------------------------------------------------------
    // SOBRECARGA DEL CONSTRUCTOR
    // ------------------------------------------------------------------------
    @Test
    @DisplayName("La mascota puede crearse indicando energía y humor iniciales")
    void debePermitirConstruirUnaMascotaConEstadoInicial() {

        Mascota mascota = new Mascota("Ahsoka", 75, 4);

        assertAll(
                "Estado proporcionado al constructor",
                () -> assertEquals("Ahsoka", mascota.getNombre()),
                () -> assertEquals(75, mascota.getEnergia()),
                () -> assertEquals("Contento", mascota.getHumor())
        );
    }

    @Test
    @DisplayName("La energía inicial debe quedar limitada entre 0 y 100")
    void debeControlarLimitesDeEnergiaEnLaConstruccion() {

        Mascota demasiadaEnergia = new Mascota("Ahsoka", 150, 3);
        Mascota energiaNegativa = new Mascota("Grogu", -20, 3);

        assertAll(
                "Límites de energía",
                () -> assertEquals(100, demasiadaEnergia.getEnergia()),
                () -> assertEquals(0, energiaNegativa.getEnergia())
        );
    }

    @Test
    @DisplayName("El humor inicial debe quedar limitado entre 1 y 5")
    void debeControlarLimitesDeHumorEnLaConstruccion() {

        Mascota demasiadoContenta = new Mascota("Ahsoka", 50, 10);
        Mascota demasiadoEnojada = new Mascota("Grogu", 50, -5);

        assertAll(
                "Límites de humor",
                () -> assertEquals("Chocho", demasiadoContenta.getHumor()),
                () -> assertEquals("Muy enojado", demasiadoEnojada.getHumor())
        );
    }

    // ------------------------------------------------------------------------
    // REPRESENTACIÓN DEL HUMOR
    // ------------------------------------------------------------------------
    @ParameterizedTest(name = "Humor {0} debe representarse como \"{1}\"")
    @CsvSource({
        "1, 'Muy enojado'",
        "2, 'Enojado'",
        "3, 'Neutral'",
        "4, 'Contento'",
        "5, 'Chocho'"
    })
    @DisplayName("El humor se expone como una descripción y no como su valor entero")
    void debeTraducirElNivelDeHumor(int nivel, String descripcionEsperada) {

        Mascota mascota = new Mascota("Ahsoka", 50, nivel);

        assertEquals(descripcionEsperada, mascota.getHumor());
    }

    // ------------------------------------------------------------------------
    // INGESTA
    // ------------------------------------------------------------------------
    @Test
    @DisplayName("Comer incrementa la energía un 10% truncado y aumenta un nivel el humor")
    void comerDebeIncrementarEnergiaYHumor() {

        // 10% de 53 = 5,3.
        // Se ganan solamente 5 unidades completas de energía.
        Mascota mascota = new Mascota("Ahsoka", 53, 3);

        boolean resultado = mascota.comer();

        assertAll(
                "Resultado de comer",
                () -> assertTrue(resultado),
                () -> assertEquals(58, mascota.getEnergia()),
                () -> assertEquals("Contento", mascota.getHumor())
        );
    }

    @Test
    @DisplayName("Beber incrementa la energía un 5% truncado y aumenta un nivel el humor")
    void beberDebeIncrementarEnergiaYHumor() {

        // 5% de 53 = 2,65.
        // Se ganan solamente 2 unidades completas.
        Mascota mascota = new Mascota("Ahsoka", 53, 3);

        boolean resultado = mascota.beber();

        assertAll(
                "Resultado de beber",
                () -> assertTrue(resultado),
                () -> assertEquals(55, mascota.getEnergia()),
                () -> assertEquals("Contento", mascota.getHumor())
        );
    }

    @Test
    @DisplayName("La energía y el humor no pueden superar sus máximos al comer")
    void comerDebeRespetarLosLimitesSuperiores() {

        Mascota mascota = new Mascota("Ahsoka", 95, 5);

        mascota.comer();

        assertAll(
                "Límites superiores",
                () -> assertEquals(100, mascota.getEnergia()),
                () -> assertEquals("Chocho", mascota.getHumor())
        );
    }

    // ------------------------------------------------------------------------
    // ACTIVIDAD
    // ------------------------------------------------------------------------
    @Test
    @DisplayName("Correr reduce la energía un 35% truncado y disminuye dos niveles el humor")
    void correrDebeReducirEnergiaYHumor() {

        // 35% de 53 = 18,55.
        // Se consumen 18 unidades completas.
        Mascota mascota = new Mascota("Ahsoka", 53, 3);

        boolean resultado = mascota.correr();

        assertAll(
                "Resultado de correr",
                () -> assertTrue(resultado),
                () -> assertEquals(35, mascota.getEnergia()),
                () -> assertEquals("Muy enojado", mascota.getHumor())
        );
    }

    @Test
    @DisplayName("Saltar reduce la energía un 15% truncado y disminuye dos niveles el humor")
    void saltarDebeReducirEnergiaYHumor() {

        // 15% de 53 = 7,95.
        // Se consumen 7 unidades completas.
        Mascota mascota = new Mascota("Ahsoka", 53, 3);

        boolean resultado = mascota.saltar();

        assertAll(
                "Resultado de saltar",
                () -> assertTrue(resultado),
                () -> assertEquals(46, mascota.getEnergia()),
                () -> assertEquals("Muy enojado", mascota.getHumor())
        );
    }

    @Test
    @DisplayName("El humor nunca puede disminuir por debajo de Muy enojado")
    void actividadesDebenRespetarElHumorMinimo() {

        Mascota mascota = new Mascota("Ahsoka", 50, 1);

        mascota.correr();

        assertEquals("Muy enojado", mascota.getHumor());
    }

    // ------------------------------------------------------------------------
    // DESCANSO Y ESTADO BASE
    // ------------------------------------------------------------------------
    @Test
    @DisplayName("Dormir incrementa la energía en 25 unidades y el humor en dos niveles")
    void dormirDebeRecuperarEnergiaYHumor() {

        Mascota mascota = new Mascota("Ahsoka", 53, 3);

        mascota.dormir();

        assertAll(
                "Estado mientras duerme",
                () -> assertTrue(mascota.isDormida()),
                () -> assertEquals(78, mascota.getEnergia()),
                () -> assertEquals("Chocho", mascota.getHumor())
        );
    }

    @Test
    @DisplayName("Dormir respeta los valores máximos de energía y humor")
    void dormirDebeRespetarLimitesSuperiores() {

        Mascota mascota = new Mascota("Ahsoka", 90, 5);

        mascota.dormir();

        assertAll(
                "Límites durante el descanso",
                () -> assertEquals(100, mascota.getEnergia()),
                () -> assertEquals("Chocho", mascota.getHumor())
        );
    }

    @Test
    @DisplayName("Despertar reduce el humor en un nivel")
    void despertarDebeReducirElHumor() {

        Mascota mascota = new Mascota("Ahsoka", 50, 3);

        mascota.dormir();      // Humor: 3 -> 5
        mascota.despertar();   // Humor: 5 -> 4

        assertAll(
                "Estado después de despertar",
                () -> assertFalse(mascota.isDormida()),
                () -> assertEquals("Contento", mascota.getHumor())
        );
    }

    // ------------------------------------------------------------------------
    // COMPORTAMIENTOS MIENTRAS DUERME
    // ------------------------------------------------------------------------
    @Test
    @DisplayName("Una mascota dormida no puede comer")
    void mascotaDormidaNoDebeComer() {

        Mascota mascota = new Mascota("Ahsoka", 50, 3);

        mascota.dormir();

        int energiaAntes = mascota.getEnergia();
        String humorAntes = mascota.getHumor();

        boolean resultado = mascota.comer();

        assertAll(
                "Comer mientras duerme",
                () -> assertFalse(resultado),
                () -> assertEquals(energiaAntes, mascota.getEnergia()),
                () -> assertEquals(humorAntes, mascota.getHumor())
        );
    }

    @Test
    @DisplayName("Una mascota dormida no puede realizar actividades")
    void mascotaDormidaNoDebeRealizarActividades() {

        Mascota mascota = new Mascota("Ahsoka", 50, 3);

        mascota.dormir();

        int energiaAntes = mascota.getEnergia();
        String humorAntes = mascota.getHumor();

        boolean correr = mascota.correr();
        boolean saltar = mascota.saltar();

        assertAll(
                "Actividades mientras duerme",
                () -> assertFalse(correr),
                () -> assertFalse(saltar),
                () -> assertEquals(energiaAntes, mascota.getEnergia()),
                () -> assertEquals(humorAntes, mascota.getHumor())
        );
    }

    // ------------------------------------------------------------------------
    // COMPORTAMIENTOS MIENTRAS DUERME
    // ------------------------------------------------------------------------
    @Test
    @DisplayName("La mascota puede representar su estado como una cadena")
    void debeRepresentarSuEstadoComoCadena() {

        Mascota mascota = new Mascota("Ahsoka", 75, 4);

        String esperado = """
            {
              "nombre": "Ahsoka",
              "energia": 75,
              "humor": "Contento",
              "dormida": false
            }""";

        assertEquals(esperado, mascota.toString());
    }
}
