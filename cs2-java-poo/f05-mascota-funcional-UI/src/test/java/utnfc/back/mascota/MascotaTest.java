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
    @DisplayName("Una mascota recién creada comienza despierta y viva")
    void debeComenzarDespiertaYViva() {

        Mascota mascota = new Mascota("Ahsoka");

        assertAll(
                "Estado inicial de descanso y vida",
                () -> assertFalse(mascota.isDormida()),
                () -> assertTrue(mascota.isViva())
        );
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
                () -> assertEquals(0, energiaNegativa.getEnergia()),
                () -> assertFalse(energiaNegativa.isViva())
        );
    }

    @Test
    @DisplayName("El humor inicial debe quedar limitada entre 1 y 5")
    void debeControlarLimitesDeHumorEnLaConstruccion() {

        Mascota demasiadoContenta = new Mascota("Ahsoka", 50, 10);
        Mascota demasiadoEnojada = new Mascota("Grogu", 50, -5);

        assertAll(
                "Límites de humor",
                () -> assertEquals("Chocho", demasiadoContenta.getHumor()),
                () -> assertEquals("Muy enojado", demasiadoEnojada.getHumor())
        );
    }

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

    @Test
    @DisplayName("Comer incrementa la energía un 10% truncado y aumenta un nivel el humor")
    void comerDebeIncrementarEnergiaYHumor() {

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

    @Test
    @DisplayName("Correr reduce la energía un 35% truncado y disminuye dos niveles el humor")
    void correrDebeReducirEnergiaYHumor() {

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

    @Test
    @DisplayName("La mascota puede representar su estado como una cadena JSON con vida")
    void debeRepresentarSuEstadoComoCadena() {

        Mascota mascota = new Mascota("Ahsoka", 75, 4);

        String esperado = """
            {
              "nombre": "Ahsoka",
              "energia": 75,
              "humor": "Contento",
              "dormida": false,
              "viva": true
            }""";

        assertEquals(esperado, mascota.toString());
    }

    // ========================================================================
    // FASE 05 - MASCOTA FUNCIONAL (REGLAS COMPLETAS)
    // ========================================================================

    // ------------------------------------------------------------------------
    // RACHAS DE INGESTA
    // ------------------------------------------------------------------------
    @Test
    @DisplayName("A partir de la tercera ingesta consecutiva, el humor se decrementa en 1")
    void terceraYCuartaIngestaConsecutivaDebenReducirHumor() {

        Mascota mascota = new Mascota("Ahsoka", 50, 3);

        mascota.comer(); // 1°: humor 3 -> 4
        assertEquals("Contento", mascota.getHumor());

        mascota.beber(); // 2°: humor 4 -> 5
        assertEquals("Chocho", mascota.getHumor());

        mascota.comer(); // 3°: humor 5 -> 4 (se molesta y decrementa)
        assertEquals("Contento", mascota.getHumor());

        mascota.beber(); // 4°: humor 4 -> 3 (sigue molesta y decrementa)
        assertEquals("Neutral", mascota.getHumor());
    }

    @Test
    @DisplayName("En la quinta ingesta consecutiva, la mascota muere de empacho")
    void quintaIngestaConsecutivaDebeMatarPorEmpacho() {

        Mascota mascota = new Mascota("Ahsoka", 50, 3);

        assertTrue(mascota.comer()); // 1°
        assertTrue(mascota.comer()); // 2°
        assertTrue(mascota.comer()); // 3°
        assertTrue(mascota.comer()); // 4°
        boolean resultado5 = mascota.comer(); // 5°: muere de empacho

        assertAll(
                "Muerte por empacho",
                () -> assertTrue(resultado5),
                () -> assertFalse(mascota.isViva()),
                () -> assertEquals(0, mascota.getEnergia()),
                () -> assertEquals("Neutral", mascota.getHumor()) // Conserva último humor
        );
    }

    @Test
    @DisplayName("Una actividad de movimiento interrumpe la racha de ingestas")
    void actividadDebeCortarRachaDeIngestas() {

        Mascota mascota = new Mascota("Ahsoka", 50, 3);

        mascota.comer(); // 1° ingesta (humor 3 -> 4)
        mascota.comer(); // 2° ingesta (humor 4 -> 5)

        // Realiza actividad: interrumpe la racha
        mascota.saltar(); // Humor: 5 -> 3, energía: 50 -> 43

        // La siguiente ingesta vuelve a ser la 1° de una nueva racha (humor sube)
        mascota.comer(); // Nueva 1° ingesta (humor 3 -> 4)
        assertEquals("Contento", mascota.getHumor());
    }

    @Test
    @DisplayName("Dormir o despertar interrumpe la racha de ingestas")
    void descansoDebeCortarRachaDeIngestas() {

        Mascota mascota = new Mascota("Ahsoka", 50, 3);

        mascota.comer(); // 1°
        mascota.comer(); // 2°
        mascota.comer(); // 3° -> humor 4

        mascota.dormir();    // Corta racha
        mascota.despertar(); // Corta racha

        // La siguiente ingesta vuelve a ser la 1°
        mascota.comer(); // Humor debe subir normalmente
        assertTrue(mascota.isViva());
    }

    // ------------------------------------------------------------------------
    // RACHAS DE ACTIVIDAD
    // ------------------------------------------------------------------------
    @Test
    @DisplayName("Tres actividades consecutivas hacen que la mascota se empaque y quede dormida")
    void tresActividadesConsecutivasDebenEmpacarYDormir() {

        Mascota mascota = new Mascota("Ahsoka", 100, 5);

        assertTrue(mascota.correr()); // 1° actividad
        assertFalse(mascota.isDormida());

        assertTrue(mascota.saltar()); // 2° actividad
        assertFalse(mascota.isDormida());

        assertTrue(mascota.correr()); // 3° actividad -> se empaca y se duerme
        assertTrue(mascota.isDormida());
    }

    @Test
    @DisplayName("Una ingesta interrumpe la racha de actividades")
    void ingestaDebeCortarRachaDeActividades() {

        Mascota mascota = new Mascota("Ahsoka", 100, 5);

        mascota.correr(); // 1° actividad
        mascota.saltar(); // 2° actividad

        mascota.comer();  // Ingesta: corta racha de actividades

        mascota.correr(); // Nueva 1° actividad
        assertFalse(mascota.isDormida());
    }

    // ------------------------------------------------------------------------
    // MUERTE POR AGOTAMIENTO
    // ------------------------------------------------------------------------
    @Test
    @DisplayName("La mascota muere por agotamiento si su energía llega a cero")
    void debeMorirPorAgotamientoCuandoEnergiaLlegaACero() {

        // Mascota con poca energía
        Mascota mascota = new Mascota("Ahsoka", 1, 3);

        // Correr con 1 de energía: 35% de 1 = 0, pero energía inicial baja a 0
        // Para asegurar que llegue a 0:
        Mascota cansada = new Mascota("Grogu", 0, 3);

        assertAll(
                "Muerte por energía cero",
                () -> assertFalse(cansada.isViva()),
                () -> assertEquals(0, cansada.getEnergia())
        );
    }

    // ------------------------------------------------------------------------
    // COMPORTAMIENTO DE MASCOTA MUERTA
    // ------------------------------------------------------------------------
    @Test
    @DisplayName("Una mascota muerta no responde a ningún comportamiento y retorna false")
    void mascotaMuertaNoDebeRealizarNingunComportamiento() {

        Mascota mascota = new Mascota("Ahsoka", 50, 3);

        // Provocamos muerte por 5 ingestas
        for (int i = 0; i < 5; i++) {
            mascota.comer();
        }

        assertFalse(mascota.isViva());
        String humorFallecida = mascota.getHumor();

        // Todos los intentos posteriores deben fallar y no mutar el estado
        assertAll(
                "Comportamientos en mascota muerta",
                () -> assertFalse(mascota.comer()),
                () -> assertFalse(mascota.beber()),
                () -> assertFalse(mascota.correr()),
                () -> assertFalse(mascota.saltar()),
                () -> assertFalse(mascota.respondeA("Ahsoka")),
                () -> {
                    mascota.dormir();
                    assertFalse(mascota.isDormida());
                },
                () -> {
                    mascota.despertar();
                    assertFalse(mascota.isDormida());
                },
                () -> assertEquals(0, mascota.getEnergia()),
                () -> assertEquals(humorFallecida, mascota.getHumor())
        );
    }
}
