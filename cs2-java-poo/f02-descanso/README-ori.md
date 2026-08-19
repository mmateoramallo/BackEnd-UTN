# Fase 02 — Descanso de la Mascota

## Objetivo

En la fase anterior construimos nuestra primera clase `Mascota` y logramos que cada instancia conserve un nombre que puede ser consultado y modificado.

Ahora vamos a incorporar por primera vez un **estado adicional** y comportamientos capaces de modificarlo.

La mascota podrá encontrarse despierta o dormida y deberá responder a los comportamientos `dormir()` y `despertar()`.

Los tests construidos en la fase anterior **se conservan**. Los nuevos requerimientos se incorporarán agregando nuevos tests a la misma suite. De esta manera, además de verificar las nuevas funcionalidades, comprobaremos que los comportamientos previamente implementados continúan funcionando.

---

## Enunciado

Extender la clase `Mascota` para representar también su estado de descanso.

Toda mascota deberá:

- comenzar **despierta** al momento de ser creada;
- poder pasar al estado dormida mediante el método `dormir()`;
- poder volver al estado despierta mediante el método `despertar()`;
- permitir consultar si se encuentra dormida mediante el método `estaDormida()`.

La interfaz pública que incorporaremos en esta fase será:

```java
boolean estaDormida()
void dormir()
void despertar()
```

La información que representa internamente si la mascota está dormida deberá permanecer oculta al exterior de la clase.

> En esta fase todavía no tenemos comportamientos como comer, correr o saltar. La regla general que indica que una mascota dormida solamente responde a `despertar()` será aplicada progresivamente cuando esos comportamientos sean incorporados.

---

# Desarrollo paso a paso

## 1. Conservamos los tests existentes

Partimos del `MascotaTest` construido en la fase anterior.

Los tests que verifican el nombre **no se eliminan ni se reemplazan**:

```java
@Test
@DisplayName("La mascota conserva el nombre indicado al crearla")
void debeConservarElNombreInicial() {
    Mascota mascota = new Mascota("Ahsoka");

    String nombre = mascota.getNombre();

    assertEquals("Ahsoka", nombre);
}

@Test
@DisplayName("El nombre de la mascota puede modificarse")
void debePermitirCambiarElNombre() {
    Mascota mascota = new Mascota("Ahsoka");

    mascota.setNombre("Tamagotchi");

    assertEquals("Tamagotchi", mascota.getNombre());
}
```

Esto es importante: nuestra suite de tests comienza a funcionar como una **red de seguridad**.

Cada nueva modificación de `Mascota` deberá satisfacer los nuevos requerimientos **sin romper los comportamientos que ya funcionaban**.

---

## 2. Primer nuevo requerimiento: una mascota nace despierta

Agregamos un test que expresa el estado esperado de una mascota recién creada:

```java
@Test
@DisplayName("Una mascota comienza despierta")
void debeComenzarDespierta() {

    // Arrange
    Mascota mascota = new Mascota("Ahsoka");

    // Act
    boolean dormida = mascota.estaDormida();

    // Assert
    assertFalse(dormida);
}
```

Para utilizar `assertFalse` agregaremos el import estático correspondiente:

```java
import static org.junit.jupiter.api.Assertions.assertFalse;
```

Ejecutamos nuevamente:

```bash
mvn test
```

En este momento esperamos un error de compilación porque `Mascota` todavía no posee el método:

```text
estaDormida()
```

El test está indicando la siguiente modificación necesaria en el contrato público de la clase.

---

## 3. Incorporamos el estado de descanso

Agregamos un nuevo atributo a `Mascota`:

```java
private boolean dormida;
```

Este atributo representa parte del estado interno de cada instancia.

El tipo `boolean` puede almacenar únicamente dos valores:

```text
true
false
```

En nuestro dominio interpretaremos:

```text
false -> la mascota está despierta
true  -> la mascota está dormida
```

Como los atributos `boolean` de una instancia se inicializan por defecto en `false`, una mascota nueva ya comenzaría técnicamente despierta.

Sin embargo, para hacer explícita la intención del constructor podemos inicializarlo:

```java
public Mascota(String nombre) {
    this.nombre = nombre;
    this.dormida = false;
}
```

---

## 4. Permitimos consultar el estado

Agregamos el método:

```java
public boolean estaDormida() {
    return dormida;
}
```

Volvemos a ejecutar:

```bash
mvn test
```

Ahora el nuevo test debería pasar y, al mismo tiempo, los tests de identidad de la fase anterior deberán continuar en verde.

---

## 5. Segundo requerimiento: la mascota puede dormir

Agregamos un nuevo test:

```java
@Test
@DisplayName("Una mascota despierta puede dormirse")
void debePoderDormirse() {

    // Arrange
    Mascota mascota = new Mascota("Ahsoka");

    // Act
    mascota.dormir();

    // Assert
    assertTrue(mascota.estaDormida());
}
```

Agregamos también:

```java
import static org.junit.jupiter.api.Assertions.assertTrue;
```

Ejecutamos:

```bash
mvn test
```

El test todavía no puede completarse porque necesitamos incorporar el comportamiento `dormir()`.

---

## 6. Implementamos el comportamiento dormir

Agregamos:

```java
public void dormir() {
    dormida = true;
}
```

El método no necesita retornar ningún valor.

Su responsabilidad es modificar el estado interno del objeto.

Volvemos a ejecutar:

```bash
mvn test
```

Ahora esperamos que también este requerimiento quede en verde.

---

## 7. Tercer requerimiento: la mascota puede despertar

Agregamos otro test:

```java
@Test
@DisplayName("Una mascota dormida puede despertarse")
void debePoderDespertarse() {

    // Arrange
    Mascota mascota = new Mascota("Ahsoka");
    mascota.dormir();

    // Comprobación del estado previo
    assertTrue(mascota.estaDormida());

    // Act
    mascota.despertar();

    // Assert
    assertFalse(mascota.estaDormida());
}
```

El test describe una pequeña transición de estado:

```text
despierta -> dormir() -> dormida -> despertar() -> despierta
```

Ejecutamos:

```bash
mvn test
```

Como todavía no existe `despertar()`, el compilador indicará qué parte del contrato falta implementar.

---

## 8. Implementamos despertar

Agregamos:

```java
public void despertar() {
    dormida = false;
}
```

Volvemos a ejecutar toda la suite:

```bash
mvn test
```

Ahora esperamos que **todos los tests**, tanto los anteriores como los nuevos, permanezcan en verde.

---

# Versión resultante de la clase

Al finalizar esta fase, `Mascota` tendrá una estructura equivalente a:

```java
package utnfc.back.mascota;

public class Mascota {

    private String nombre;
    private boolean dormida;

    public Mascota(String nombre) {
        this.nombre = nombre;
        this.dormida = false;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public boolean estaDormida() {
        return dormida;
    }

    public void dormir() {
        dormida = true;
    }

    public void despertar() {
        dormida = false;
    }
}
```

---

# Suite acumulativa de tests

Al finalizar la fase tendremos una única clase `MascotaTest` que contiene tanto las pruebas anteriores como las incorporadas ahora.

Conceptualmente:

```text
MascotaTest
│
├── conserva el nombre inicial
├── permite modificar el nombre
├── comienza despierta
├── puede dormirse
└── puede despertarse
```

La ejecución completa:

```bash
mvn test
```

debe verificar **todo el comportamiento construido hasta el momento**.

Esta forma de trabajo será mantenida durante las próximas fases: cada nuevo comportamiento agrega nuevas pruebas sin eliminar las anteriores.

---

# Conceptos incorporados en esta fase

Además de reforzar los conceptos de la fase anterior, incorporamos:

- atributos de tipo `boolean`;
- estado interno de un objeto;
- métodos que modifican el estado;
- métodos que consultan el estado;
- transición entre estados;
- conservación de funcionalidades anteriores;
- tests como protección frente a regresiones;
- crecimiento incremental de una suite de pruebas.

---

## Para pensar

1. ¿Por qué `dormida` es un atributo privado?
2. ¿Sería conveniente permitir un método público `setDormida(boolean dormida)`?
3. ¿Qué diferencia conceptual existe entre `estaDormida()` y `dormir()`?
4. ¿Por qué conservamos los tests de la fase anterior?
5. Si agregamos una nueva funcionalidad y un test antiguo comienza a fallar, ¿qué información nos está proporcionando?
6. ¿Es necesario asignar explícitamente `false` a un atributo `boolean` de instancia al construir el objeto?
7. Aunque Java lo inicialice automáticamente en `false`, ¿puede tener valor dejar esa decisión explícita en el constructor?
