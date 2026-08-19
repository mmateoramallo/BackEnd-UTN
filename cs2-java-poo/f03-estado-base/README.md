# Fase 03 — Estado Base de la Mascota

## Objetivo

En las fases anteriores construimos una `Mascota` con identidad y descanso. En esta fase incorporamos dos nuevas dimensiones del estado: **energía** y **humor**.

Todavía mantendremos toda esta lógica dentro de `Mascota`, incluso aunque la clase empiece a acumular responsabilidades. Ese crecimiento deliberado nos permitirá justificar la próxima refactorización mediante **composición**.

> En esta fase todavía no implementamos muerte, empacho, rachas de ingesta, rachas de actividad ni sueño automático por humor.

## Reglas

### Energía

- Se representa con `int`.
- Rango válido: `0..100`.
- Se consulta con `getEnergia()`.
- No existe setter público.
- Los porcentajes se truncan.

Ejemplo:

```text
energia = 53
10% = 5,3
incremento efectivo = 5
```

### Humor

Se representa internamente con un entero entre `1` y `5`, pero ese valor no se expone directamente.

```text
1 -> Muy enojado
2 -> Enojado
3 -> Neutral
4 -> Contento
5 -> Chocho
```

`getHumor()` retorna directamente la descripción.

## Constructores

Se conserva:

```java
Mascota(String nombre)
```

con estado inicial:

```text
energia = 50
humor = Neutral
dormida = false
```

Y se agrega la sobrecarga:

```java
Mascota(String nombre, int energia, int humor)
```

Los valores iniciales se ajustan automáticamente a sus límites.

## Comportamientos

### `comer()`

- Solo despierta.
- +10% de energía, truncado.
- +1 humor.
- Respeta máximos.
- `true` si se realiza, `false` si está dormida.

### `beber()`

- Solo despierta.
- +5% de energía, truncado.
- +1 humor.
- Respeta máximos.

### `correr()`

- Solo despierta.
- -35% de energía, truncado.
- -2 humor.
- Respeta mínimos.

### `saltar()`

- Solo despierta.
- -15% de energía, truncado.
- -2 humor.
- Respeta mínimos.

### `dormir()`

- Pasa a dormida.
- +25 energía.
- +2 humor.
- Si ya estaba dormida no vuelve a aplicar los efectos.

### `despertar()`

- Solo produce efecto si estaba dormida.
- Pasa a despierta.
- -1 humor.

## Suite acumulativa

Se conservan todos los tests anteriores y se agregan pruebas para:

- estado inicial;
- sobrecarga de constructor;
- límites de energía;
- límites de humor;
- traducción de humor;
- comer;
- beber;
- correr;
- saltar;
- dormir;
- despertar;
- bloqueo de acciones mientras duerme;
- representación con `toString()`.

## Tests principales

### Estado inicial

```java
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
```

### Constructor sobrecargado

```java
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
```

### Humor parametrizado

```java
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
```

### Comer

```java
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
```

### Correr

```java
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
```

## Implementación final de `Mascota`

```java
package utnfc.back.mascota;

public class Mascota {

    private String nombre;
    private int energia;
    private int humor;
    private boolean dormida;

    public Mascota(String nombre) {
        this(nombre, 50, 3);
    }

    public Mascota(String nombre, int energia, int humor) {
        this.nombre = nombre;
        this.setEnergia(energia);
        this.setHumor(humor);
        this.dormida = false;
    }

    public String getNombre() {
        return this.nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getEnergia() {
        return this.energia;
    }

    private void setEnergia(int energia) {
        if (energia < 0) {
            this.energia = 0;
        } else if (energia > 100) {
            this.energia = 100;
        } else {
            this.energia = energia;
        }
    }

    public String getHumor() {
        return switch (this.humor) {
            case 1 -> "Muy enojado";
            case 2 -> "Enojado";
            case 3 -> "Neutral";
            case 4 -> "Contento";
            case 5 -> "Chocho";
            default -> "Desconocido";
        };
    }

    private void setHumor(int humor) {
        if (humor < 1) {
            this.humor = 1;
        } else if (humor > 5) {
            this.humor = 5;
        } else {
            this.humor = humor;
        }
    }

    public boolean isDormida() {
        return this.dormida;
    }

    public void dormir() {
        if (this.dormida) {
            return;
        }

        this.dormida = true;
        this.setEnergia(this.energia + 25);
        this.setHumor(this.humor + 2);
    }

    public void despertar() {
        if (!this.dormida) {
            return;
        }

        this.dormida = false;
        this.setHumor(this.humor - 1);
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

        int incrementoEnergia = (int) (this.energia * 0.10);
        this.setEnergia(this.energia + incrementoEnergia);
        this.setHumor(this.humor + 1);
        return true;
    }

    public boolean beber() {
        if (this.dormida) {
            return false;
        }

        int incrementoEnergia = (int) (this.energia * 0.05);
        this.setEnergia(this.energia + incrementoEnergia);
        this.setHumor(this.humor + 1);
        return true;
    }

    public boolean correr() {
        if (this.dormida) {
            return false;
        }

        int consumoEnergia = (int) (this.energia * 0.35);
        this.setEnergia(this.energia - consumoEnergia);
        this.setHumor(this.humor - 2);
        return true;
    }

    public boolean saltar() {
        if (this.dormida) {
            return false;
        }

        int consumoEnergia = (int) (this.energia * 0.15);
        this.setEnergia(this.energia - consumoEnergia);
        this.setHumor(this.humor - 2);
        return true;
    }

    @Override
    public String toString() {
        return """
                {
                  \"nombre\": \"%s\",
                  \"energia\": %d,
                  \"humor\": \"%s\",
                  \"dormida\": %b
                }""".formatted(
                    this.nombre,
                    this.energia,
                    this.getHumor(),
                    this.dormida
                );
    }
}
```

## `toString()` y Text Blocks

La representación se inspira visualmente en JSON, pero sigue siendo un `String`.

```java
@Override
public String toString() {
    return """
            {
              \"nombre\": \"%s\",
              \"energia\": %d,
              \"humor\": \"%s\",
              \"dormida\": %b
            }""".formatted(
                this.nombre,
                this.energia,
                this.getHumor(),
                this.dormida
            );
}
```

La llave final queda pegada al cierre del Text Block para evitar un salto de línea extra al final.

## Ejecución

```bash
mvn test
```

Al finalizar esperamos toda la suite en verde.

## El problema de diseño que aparece

`Mascota` ahora conoce:

- límites de energía;
- límites de humor;
- truncamiento;
- traducción del humor;
- modificación de ambas dimensiones;
- reglas de descanso.

La clase funciona, pero empieza a concentrar demasiadas responsabilidades.

Eso prepara la siguiente fase.

## Próxima fase — `f04-estado-compuesto`

Vamos a transformar:

```java
private int energia;
private int humor;
```

en:

```java
private Energia energia;
private Humor humor;
```

La idea será que cada objeto gestione sus propias reglas.

```text
Mascota
├── Energia
└── Humor
```

`Mascota` seguirá coordinando comportamientos; `Energia` y `Humor` administrarán su propio estado.

## Conceptos incorporados

- sobrecarga de constructores;
- estado con tipos primitivos;
- invariantes;
- setters privados;
- encapsulamiento;
- truncamiento;
- límites de dominio;
- tests parametrizados;
- `assertAll`;
- `toString()`;
- `@Override`;
- Text Blocks;
- `formatted()`;
- primeras señales de exceso de responsabilidades;
- preparación para composición.

## Para pensar

1. ¿Por qué `setEnergia()` es privado?
2. ¿Por qué `setHumor()` es privado?
3. ¿Por qué no exponemos el entero del humor?
4. ¿Qué problema evita centralizar los límites?
5. ¿Cuántas responsabilidades diferentes tiene ahora `Mascota`?
6. ¿Qué lógica pertenece realmente a `Energia`?
7. ¿Qué lógica pertenece realmente a `Humor`?
8. ¿Qué tests deberían continuar pasando después de la refactorización?
