# Fase 04 — Estado Compuesto de la Mascota

## Objetivo

En `f03-estado-base` nuestra clase `Mascota` ya era capaz de administrar:

- nombre;
- descanso;
- energía;
- humor;
- ingesta;
- actividad;
- representación textual.

La implementación funcionaba y los tests estaban en verde.

Sin embargo, empezó a aparecer un problema de diseño: `Mascota` conocía demasiados detalles internos sobre cómo funcionaban la energía y el humor.

Por ejemplo, sabía:

- que la energía debía estar entre `0` y `100`;
- cómo calcular y truncar porcentajes;
- cómo limitar incrementos y decrementos;
- que el humor debía estar entre `1` y `5`;
- cómo transformar un nivel entero de humor en una descripción textual.

En esta fase vamos a **separar responsabilidades mediante composición**.

La idea será reemplazar:

```java
private int energia;
private int humor;
```

por:

```java
private Energia energia;
private Humor humor;
```

De esta manera:

```text
Mascota
├── Energia
└── Humor
```

`Mascota` seguirá coordinando sus comportamientos de alto nivel, mientras que `Energia` y `Humor` administrarán correctamente su propio estado.

> En esta fase todavía NO implementamos muerte, empacho, ingestas consecutivas, actividades consecutivas, sueño automático por humor ni otras reglas de vida o muerte. Esas reglas quedan para `f05-mascota-funcional`.

---

# 1. El problema de la fase anterior

Antes de refactorizar, `Mascota` contenía:

```java
private int energia;
private int humor;
```

También tenía métodos privados como:

```java
private void setEnergia(...)
private void setHumor(...)
```

y lógica como:

```java
public String getHumor() {
    return switch (this.humor) {
        ...
    };
}
```

Por ejemplo, `comer()` necesitaba resolver directamente:

```java
int incrementoEnergia = (int) (this.energia * 0.10);

this.setEnergia(this.energia + incrementoEnergia);
this.setHumor(this.humor + 1);
```

Esto funciona, pero mezcla responsabilidades.

`Mascota` debe saber que **comer** implica:

```text
energía +10%
humor +1
```

Ese conocimiento pertenece al comportamiento de la mascota.

Pero no necesariamente debería saber:

```text
cómo calcular el porcentaje;
cómo truncarlo;
cuáles son los límites de energía;
cuáles son los límites del humor;
cómo representar el humor.
```

Esas responsabilidades pueden trasladarse a objetos especializados.

---

# 2. Estrategia de refactorización

Introduciremos dos nuevas clases:

```text
Energia
Humor
```

Cada una tendrá:

- su propio estado;
- sus propias invariantes;
- sus propios comportamientos;
- sus propios tests unitarios.

La clase `Mascota` pasará a **componer** esos objetos.

---

# 3. Tres suites de tests

En esta fase tendremos:

```text
EnergiaTest
HumorTest
MascotaTest
```

La idea importante es que `MascotaTest` **no debería cambiar**.

Los tests existentes describen el contrato público de `Mascota`.

Internamente podremos cambiar:

```java
private int energia;
```

por:

```java
private Energia energia;
```

sin que quien utiliza `Mascota` tenga que enterarse.

Si la refactorización está bien hecha, los tests anteriores seguirán verdes.

---

# 4. Desarrollo incremental con `@Disabled`

Durante la clase puede ser útil trabajar una suite por vez.

Por ejemplo:

```java
import org.junit.jupiter.api.Disabled;

@Disabled("Pendiente implementar Humor")
class HumorTest {
    ...
}
```

Mientras construimos `Energia`, JUnit descubre `HumorTest` pero no ejecuta sus pruebas.

Cuando `EnergiaTest` queda verde, quitamos:

```java
@Disabled
```

y comenzamos con `Humor`.

Esto permite avanzar incrementalmente sin eliminar tests.

---

# 5. Clase `Energia`

## Responsabilidad

`Energia` será responsable de:

- conservar su valor;
- garantizar el rango `0..100`;
- incrementar unidades;
- incrementar porcentajes;
- decrementar porcentajes;
- truncar las fracciones de unidad.

Su API será:

```java
Energia(int valorInicial)

int getValor()

void incrementar(int unidades)
void incrementarPorcentaje(int porcentaje)
void decrementarPorcentaje(int porcentaje)
```

No existe un `setValor()` público.

El valor cambia mediante comportamientos del propio objeto.

---

# 6. `EnergiaTest`

```java
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
```

---

# 7. Implementación de `Energia`

```java
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
```

---

# 8. Clase `Humor`

## Responsabilidad

`Humor` será responsable de:

- conservar internamente un nivel;
- garantizar el rango `1..5`;
- incrementar niveles;
- decrementar niveles;
- representar el estado de humor como texto.

La codificación:

```text
1 -> Muy enojado
2 -> Enojado
3 -> Neutral
4 -> Contento
5 -> Chocho
```

queda encapsulada dentro de `Humor`.

Su API será:

```java
Humor(int nivelInicial)

void incrementar(int niveles)
void decrementar(int niveles)

String toString()
```

Deliberadamente NO exponemos:

```java
getNivel()
setNivel(...)
```

El consumidor no necesita conocer el entero utilizado internamente.

---

# 9. `HumorTest`

```java
package utnfc.back.mascota;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

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
```

---

# 10. Implementación de `Humor`

```java
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
```

---

# 11. Refactorización de `Mascota`

La estructura anterior:

```java
private int energia;
private int humor;
```

se reemplaza por:

```java
private Energia energia;
private Humor humor;
```

Ahora `Mascota` tiene **referencias a otros objetos** que forman parte de su estado.

Esto introduce composición.

---

# 12. `Mascota` refactorizada

```java
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
```

---

# 13. Comparación antes y después

## Antes

`comer()` necesitaba conocer detalles internos:

```java
int incrementoEnergia =
        (int) (this.energia * 0.10);

this.setEnergia(
        this.energia + incrementoEnergia
);

this.setHumor(
        this.humor + 1
);
```

`Mascota` sabía:

- cómo calcular porcentajes;
- cómo truncarlos;
- cómo validar energía;
- cómo validar humor.

## Después

```java
this.energia.incrementarPorcentaje(10);
this.humor.incrementar(1);
```

La lectura del código expresa directamente la intención.

`Mascota` sabe:

> Comer implica aumentar la energía un 10% y el humor un nivel.

Pero delega a `Energia`:

> ¿Cómo se incrementa correctamente la energía?

Y delega a `Humor`:

> ¿Cómo se incrementa correctamente el humor?

---

# 14. División de responsabilidades

```text
Mascota
│
├── sabe cuándo puede realizar una acción
├── sabe qué efectos tiene comer
├── sabe qué efectos tiene beber
├── sabe qué efectos tiene correr
├── sabe qué efectos tiene saltar
│
├── Energia
│   ├── conserva su valor
│   ├── controla 0..100
│   ├── incrementa unidades
│   ├── incrementa porcentajes
│   ├── decrementa porcentajes
│   └── trunca fracciones
│
└── Humor
    ├── conserva su nivel
    ├── controla 1..5
    ├── incrementa niveles
    ├── decrementa niveles
    └── sabe representarse
```

---

# 15. Composición

En esta versión:

```java
private Energia energia;
private Humor humor;
```

los atributos ya no almacenan solamente valores primitivos.

Almacenan **referencias a objetos**.

Cada `Mascota` está compuesta por:

- un objeto `Energia`;
- un objeto `Humor`.

Esto permite distribuir comportamiento entre objetos especializados.

La composición no consiste simplemente en “poner una clase dentro de otra”.

La idea importante es:

> Cada objeto debe administrar el estado y las reglas que naturalmente le corresponden.

---

# 16. Referencias

Cuando escribimos:

```java
this.energia = new Energia(energia);
```

`this.energia` no contiene directamente el número de energía.

Contiene una **referencia a un objeto `Energia`**.

Luego:

```java
this.energia.incrementarPorcentaje(10);
```

significa que `Mascota` envía una operación al objeto al que referencia.

Lo mismo ocurre con:

```java
this.humor.incrementar(1);
```

Los objetos comienzan a **colaborar**.

---

# 17. Delegación

Comparemos:

```java
public String getHumor() {
    return switch (this.humor) {
        ...
    };
}
```

con:

```java
public String getHumor() {
    return this.humor.toString();
}
```

En la segunda versión, `Mascota` ya no sabe cómo representar el humor.

**Delega** esa responsabilidad al objeto que conoce el humor.

La delegación aparece nuevamente en:

```java
this.energia.incrementarPorcentaje(10);
this.humor.incrementar(1);
```

---

# 18. Conservación del contrato público

Desde afuera seguimos utilizando:

```java
mascota.getEnergia();
mascota.getHumor();

mascota.comer();
mascota.beber();
mascota.correr();
mascota.saltar();
mascota.dormir();
mascota.despertar();
```

La implementación cambió.

El contrato público no.

Por eso `MascotaTest` debería continuar pasando **sin modificaciones**.

---

# 19. Tests como red de seguridad

Partimos de una clase funcionando.

Luego modificamos profundamente su implementación:

```text
int energia
      ↓
Energia energia
```

y:

```text
int humor
      ↓
Humor humor
```

Después ejecutamos:

```bash
mvn test
```

Si todos los tests anteriores continúan verdes, tenemos evidencia de que la refactorización conservó el comportamiento observable.

Los tests permiten modificar la estructura interna con mayor confianza.

---

# 20. Ejecución

Una vez implementadas las tres clases:

```bash
mvn test
```

debería ejecutar:

```text
EnergiaTest
HumorTest
MascotaTest
```

y mantener toda la suite en verde.

---

# 21. Qué cambió realmente

Antes:

```text
Mascota
 ├── nombre
 ├── int energia
 ├── int humor
 └── dormida
```

Después:

```text
Mascota
 ├── nombre
 ├── Energia
 │    └── valor
 ├── Humor
 │    └── nivel
 └── dormida
```

La información del sistema es esencialmente la misma.

Lo que cambió es:

> **quién es responsable de administrarla.**

---

# 22. Responsabilidad única

Sin profundizar todavía en principios de diseño, podemos observar una idea sencilla:

> Una clase debería tener una responsabilidad clara.

`Energia` cambia cuando cambian las reglas de energía.

`Humor` cambia cuando cambian las reglas de humor.

`Mascota` cambia cuando cambian los comportamientos propios de una mascota.

La separación no busca crear más clases por sí misma.

Busca que las responsabilidades estén ubicadas donde corresponde.

---

# 23. Conceptos incorporados en esta fase

- composición;
- referencias entre objetos;
- objetos como atributos;
- división de responsabilidades;
- delegación;
- encapsulamiento;
- invariantes;
- colaboración entre objetos;
- conservación del contrato público;
- refactorización;
- pruebas de regresión;
- tests independientes por clase;
- `@Disabled` como herramienta temporal;
- primera aproximación al principio de responsabilidad única.

---

# 24. Para pensar

1. ¿Por qué `Energia` controla sus propios límites?
2. ¿Por qué `Humor` sabe cómo representarse?
3. ¿Por qué `Mascota` ya no necesita un `setEnergia()`?
4. ¿Por qué `Mascota` ya no necesita un `setHumor()`?
5. ¿Qué diferencia existe entre `int energia` y `Energia energia`?
6. ¿Qué contiene realmente la variable `energia` dentro de `Mascota`?
7. ¿Quién crea los objetos `Energia` y `Humor`?
8. ¿Qué significa que `Mascota` delegue una operación?
9. ¿Por qué `Mascota` sigue sabiendo que comer incrementa energía 10%?
10. ¿Por qué no debería saber cómo se calcula ese incremento?
11. ¿Qué ocurriría si nuestros tests dependieran directamente de la implementación interna?
12. ¿Por qué `MascotaTest` puede seguir igual después de la refactorización?
13. ¿Qué ventaja ofrecen tests propios para `Energia` y `Humor`?
14. ¿En qué sentido los objetos comienzan ahora a colaborar?
15. ¿Qué responsabilidad debería quedar en `Mascota`?
16. Si mañana cambiara la escala del humor, ¿qué clase deberíamos modificar?
17. Si mañana la energía máxima fuera 200, ¿qué clase debería conocer esa decisión?
18. ¿Cómo ayuda esta separación a reducir responsabilidades dentro de `Mascota`?

---

# Próxima fase

En `f05-mascota-funcional` podremos volver sobre las reglas completas del dominio:

- consecutividad de ingestas;
- consecutividad de actividades;
- empacho;
- agotamiento;
- vida y muerte;
- sueño provocado por el estado;
- interacción entre las distintas reglas.

La diferencia es que ahora partimos de un modelo donde `Mascota`, `Energia` y `Humor` ya tienen responsabilidades mejor distribuidas.
