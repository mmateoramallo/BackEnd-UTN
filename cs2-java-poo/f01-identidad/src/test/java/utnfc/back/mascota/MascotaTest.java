package utnfc.back.mascota;


import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Mascota - Identidad")
class MascotaTest {

    @Test
    @DisplayName("La mascota conserva el nombre indicado al crearla")
    void debeConservarElNombreInicial() {

        // Arrange:
        // Creamos una referencia de tipo Mascota y una nueva instancia
        // cuyo nombre inicial será "Ahsoka".
        Mascota mascota = new Mascota("Ahsoka");

        // Act:
        // Consultamos el nombre almacenado por el objeto.
        String nombre = mascota.getNombre();

        // Assert:
        // Verificamos que el nombre recuperado sea el mismo
        // que se indicó al construir la mascota.
        assertEquals("Ahsoka", nombre);
    }

    @Test
    @DisplayName("El nombre de la mascota puede modificarse")
    void debePermitirCambiarElNombre() {

        // Arrange:
        // Partimos de una mascota cuyo nombre inicial es "Ahsoka".
        Mascota mascota = new Mascota("Ahsoka");

        // Act:
        // Modificamos el nombre mediante el método público setNombre().
        mascota.setNombre("Tamagotchi");

        // Assert:
        // Verificamos que el objeto conserve el nuevo nombre.
        assertEquals("Tamagotchi", mascota.getNombre());
    }
}