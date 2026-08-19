package utnfc.back.mascota;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("Mascota")
class MascotaTest {

    // -------------------------------------------------------------------------
    // FASE 01 - IDENTIDAD
    // -------------------------------------------------------------------------

    @Test
    @DisplayName("La mascota conserva el nombre indicado al crearla")
    void debeConservarElNombreInicial() {

        // Arrange
        Mascota mascota = new Mascota("Ahsoka");

        // Act
        String nombre = mascota.getNombre();

        // Assert
        assertEquals("Ahsoka", nombre);
    }

    @Test
    @DisplayName("El nombre de la mascota puede modificarse")
    void debePermitirCambiarElNombre() {

        // Arrange
        Mascota mascota = new Mascota("Ahsoka");

        // Act
        mascota.setNombre("Tamagotchi");

        // Assert
        assertEquals("Tamagotchi", mascota.getNombre());
    }

    // -------------------------------------------------------------------------
    // FASE 02 - DESCANSO
    // -------------------------------------------------------------------------

    @Test
    @DisplayName("Una mascota recién creada comienza despierta")
    void debeComenzarDespierta() {

        // Arrange
        Mascota mascota = new Mascota("Ahsoka");

        // Assert
        assertFalse(mascota.isDormida());
    }

    @Test
    @DisplayName("Una mascota despierta responde a su nombre")
    void debeResponderASuNombreSiEstaDespierta() {

        // Arrange
        Mascota mascota = new Mascota("Ahsoka");

        // Act
        boolean responde = mascota.respondeA("Ahsoka");

        // Assert
        assertTrue(responde);
    }

    @Test
    @DisplayName("Una mascota despierta no responde a otro nombre")
    void noDebeResponderAOtroNombre() {

        // Arrange
        Mascota mascota = new Mascota("Ahsoka");

        // Act
        boolean responde = mascota.respondeA("Grogu");

        // Assert
        assertFalse(responde);
    }

    @Test
    @DisplayName("Dormir cambia el estado de la mascota")
    void dormirDebeCambiarElEstado() {

        // Arrange
        Mascota mascota = new Mascota("Ahsoka");

        // Act
        mascota.dormir();

        // Assert
        assertTrue(mascota.isDormida());
    }

    @Test
    @DisplayName("Una mascota dormida no responde pero conserva su nombre")
    void mascotaDormidaNoRespondePeroConservaSuNombre() {

        // Arrange
        Mascota mascota = new Mascota("Ahsoka");

        // Act
        mascota.dormir();

        // Assert
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

        // Arrange
        Mascota mascota = new Mascota("Ahsoka");

        // Act
        mascota.dormir();
        mascota.despertar();

        // Assert
        assertAll(
            "Estado de una mascota después de despertar",
            () -> assertFalse(mascota.isDormida()),
            () -> assertTrue(mascota.respondeA("Ahsoka")),
            () -> assertEquals("Ahsoka", mascota.getNombre())
        );
    }
}