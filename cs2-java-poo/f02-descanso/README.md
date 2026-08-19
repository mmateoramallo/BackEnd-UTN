# Fase 02 — Descanso de la Mascota

## Objetivo

En la fase anterior construimos `Mascota` con la propiedad `nombre`. En esta segunda fase incorporamos un nuevo estado: la mascota puede estar despierta o dormida.

También comenzamos a diferenciar entre:

- **propiedades observables** del objeto;
- **comportamientos** que modifican su estado o responden en función de ese estado.

Los tests de la fase anterior se conservan y se agregan nuevos tests. Así verificamos que lo nuevo funcione sin romper lo anterior.

---

## Enunciado

Extender la clase `Mascota` para que:

- comience despierta al momento de ser creada;
- permita consultar si está dormida mediante `isDormida()`;
- pueda dormirse mediante `dormir()`;
- pueda despertarse mediante `despertar()`;
- responda a su nombre mediante `respondeA(String nombre)` solamente si está despierta;
- no responda a ningún nombre mientras está dormida;
- conserve su nombre aunque esté dormida.

Se mantienen:

```java
String getNombre()
void setNombre(String nombre)
```

Y se incorporan:

```java
boolean isDormida()
void dormir()
void despertar()
boolean respondeA(String nombre)
```

El estado interno que representa si la mascota está dormida debe permanecer oculto al exterior de la clase.

---

# Desarrollo paso a paso

## 1. Conservamos los tests de identidad

Los tests de la fase anterior no se eliminan:

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

A partir de ahora la suite de tests funciona también como protección frente a regresiones.

---

## 2. Una mascota comienza despierta

Agregamos:

```java
@Test
@DisplayName("Una mascota recién creada comienza despierta")
void debeComenzarDespierta() {
    Mascota mascota = new Mascota("Ahsoka");

    assertFalse(mascota.isDormida());
}
```

La primera ejecución fallará si `Mascota` todavía no implementa `isDormida()`.

Agregamos entonces el atributo:

```java
private boolean dormida;
```

y el método:

```java
public boolean isDormida() {
    return dormida;
}
```

Los atributos `boolean` de instancia se inicializan automáticamente en `false`, por lo que una nueva mascota comienza despierta aunque no asignemos explícitamente ese valor en el constructor.

---

## 3. Una mascota despierta responde a su nombre

Agregamos:

```java
@Test
@DisplayName("Una mascota despierta responde a su nombre")
void debeResponderASuNombreSiEstaDespierta() {
    Mascota mascota = new Mascota("Ahsoka");

    boolean responde = mascota.respondeA("Ahsoka");

    assertTrue(responde);
}
```

Y también verificamos el caso contrario:

```java
@Test
@DisplayName("Una mascota despierta no responde a otro nombre")
void noDebeResponderAOtroNombre() {
    Mascota mascota = new Mascota("Ahsoka");

    boolean responde = mascota.respondeA("Grogu");

    assertFalse(responde);
}
```

Una primera implementación posible es:

```java
public boolean respondeA(String nombre) {
    return this.nombre.equals(nombre);
}
```

---

## 4. La mascota puede dormirse

Agregamos:

```java
@Test
@DisplayName("Dormir cambia el estado de la mascota")
void dormirDebeCambiarElEstado() {
    Mascota mascota = new Mascota("Ahsoka");

    mascota.dormir();

    assertTrue(mascota.isDormida());
}
```

Implementamos:

```java
public void dormir() {
    this.dormida = true;
}
```

Ahora `dormir()` representa un comportamiento que modifica el estado interno del objeto.

---

## 5. Dormir modifica el comportamiento, pero no la identidad

Agregamos:

```java
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
```

Este test expresa tres condiciones:

1. la mascota está dormida;
2. una mascota dormida no responde a su nombre;
3. el nombre continúa existiendo y puede consultarse.

Por eso `respondeA()` debe tener en cuenta el estado:

```java
public boolean respondeA(String nombre) {
    if (this.dormida) {
        return false;
    }

    return this.nombre.equals(nombre);
}
```

---

## 6. La mascota puede despertarse

Agregamos:

```java
@Test
@DisplayName("Una mascota vuelve a responder cuando despierta")
void mascotaVuelveAResponderAlDespertar() {
    Mascota mascota = new Mascota("Ahsoka");

    mascota.dormir();
    mascota.despertar();

    assertAll(
        "Estado de una mascota después de despertar",
        () -> assertFalse(mascota.isDormida()),
        () -> assertTrue(mascota.respondeA("Ahsoka")),
        () -> assertEquals("Ahsoka", mascota.getNombre())
    );
}
```

Implementamos:

```java
public void despertar() {
    this.dormida = false;
}
```

La transición de estado queda:

```text
despierta -> dormir() -> dormida -> despertar() -> despierta
```

---

# Clase `Mascota` resultante

```java
package utnfc.back.mascota;

public class Mascota {

  private String nombre;
  private boolean dormida;

  public Mascota(String nombre) {
    this.nombre = nombre;
  }

  public String getNombre() {
    return this.nombre;
  }

  public void setNombre(String nombre) {
    this.nombre = nombre;
  }

  public boolean isDormida() {
    return dormida;
  }

  public void dormir() {
    this.dormida = true;
  }

  public void despertar() {
    this.dormida = false;
  }

  public boolean respondeA(String nombre) {
    if (this.dormida) {
      return false;
    }
    return this.nombre.equals(nombre);
  }
}
```

---

# Suite acumulativa

Al finalizar esta fase, `MascotaTest` verifica:

```text
MascotaTest
├── conserva el nombre inicial
├── permite modificar el nombre
├── comienza despierta
├── responde a su nombre si está despierta
├── no responde a otro nombre
├── dormir modifica su estado
├── dormida no responde pero conserva su nombre
└── despertar recupera el comportamiento normal
```

Ejecutamos:

```bash
mvn test
```

y esperamos todos los tests en verde.

---

# Ejecución desde `App`

```java
package utnfc.back;

import utnfc.back.mascota.Mascota;

public class App {

    public static void main(String[] args) {

        System.out.println("=== Mascota - Fase 02: Descanso ===");
        System.out.println();

        Mascota mascota = new Mascota("Ahsoka");

        System.out.println("--- Estado inicial ---");
        System.out.println("Nombre: " + mascota.getNombre());
        System.out.println("¿Está dormida?: " + mascota.isDormida());
        System.out.println("¿Responde a Ahsoka?: " + mascota.respondeA("Ahsoka"));
        System.out.println("¿Responde a Grogu?: " + mascota.respondeA("Grogu"));
        System.out.println();

        System.out.println("--- La mascota se duerme ---");
        mascota.dormir();

        System.out.println("¿Está dormida?: " + mascota.isDormida());
        System.out.println("¿Responde a Ahsoka?: " + mascota.respondeA("Ahsoka"));
        System.out.println("Su nombre sigue siendo: " + mascota.getNombre());
        System.out.println();

        System.out.println("--- La mascota se despierta ---");
        mascota.despertar();

        System.out.println("¿Está dormida?: " + mascota.isDormida());
        System.out.println("¿Responde a Ahsoka?: " + mascota.respondeA("Ahsoka"));
        System.out.println();

        System.out.println("--- Cambiamos el nombre ---");
        mascota.setNombre("Grogu");

        System.out.println("Nuevo nombre: " + mascota.getNombre());
        System.out.println("¿Responde a Ahsoka?: " + mascota.respondeA("Ahsoka"));
        System.out.println("¿Responde a Grogu?: " + mascota.respondeA("Grogu"));
    }
}
```

Ejecutamos:

```bash
mvn exec:java
```

Una salida posible:

```text
=== Mascota - Fase 02: Descanso ===

--- Estado inicial ---
Nombre: Ahsoka
¿Está dormida?: false
¿Responde a Ahsoka?: true
¿Responde a Grogu?: false

--- La mascota se duerme ---
¿Está dormida?: true
¿Responde a Ahsoka?: false
Su nombre sigue siendo: Ahsoka

--- La mascota se despierta ---
¿Está dormida?: false
¿Responde a Ahsoka?: true

--- Cambiamos el nombre ---
Nuevo nombre: Grogu
¿Responde a Ahsoka?: false
¿Responde a Grogu?: true
```

---

# Propiedades y comportamientos

En esta fase podemos distinguir explícitamente entre propiedades y comportamientos.

## Acceso a propiedades

```java
getNombre()
isDormida()
```

Permiten observar parte del estado del objeto.

## Comportamientos

```java
setNombre(...)
respondeA(...)
dormir()
despertar()
```

Representan acciones o decisiones del objeto.

En particular, `respondeA(...)` muestra que un comportamiento puede depender del estado interno: la mascota conserva su nombre aunque esté dormida, pero decide no responder mientras se encuentra en ese estado.

---

# Conceptos incorporados

- atributos booleanos;
- convención JavaBean `is...`;
- estado interno;
- métodos de consulta;
- métodos que modifican estado;
- métodos con parámetros;
- métodos con retorno booleano;
- comportamiento condicionado por estado;
- transición de estados;
- `assertTrue`;
- `assertFalse`;
- `assertAll`;
- conservación de tests anteriores;
- regresión y crecimiento incremental de la suite.

---

## Para pensar

1. ¿Por qué `dormida` es `private`?
2. ¿Por qué existe `isDormida()` pero no `setDormida(boolean)`?
3. ¿Qué diferencia existe entre observar una propiedad y ejecutar un comportamiento?
4. ¿Por qué una mascota dormida puede responder `getNombre()` pero no `respondeA(nombre)`?
5. ¿Qué parte del estado interno consulta `respondeA()` para decidir qué devolver?
6. ¿Por qué seguimos ejecutando los tests de identidad?
7. ¿Qué significa que una modificación nueva haga fallar un test anterior?
8. ¿Es necesario asignar explícitamente `false` a `dormida` en el constructor?
