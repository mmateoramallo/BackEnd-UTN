# Fase 01 — Identidad de la Mascota

## Objetivo

En esta primera etapa comenzamos a construir una aplicación que simula el comportamiento de una **mascota virtual**.

La implementación se realizará de manera incremental. En cada fase agregaremos nuevos datos y comportamientos a la mascota y utilizaremos **tests unitarios** para verificar que la implementación responda a los requerimientos planteados.

En esta primera fase trabajamos con una única propiedad: el **nombre**.

---

## Enunciado

Crear una clase llamada `Mascota` que permita representar una mascota identificada inicialmente por su nombre.

Toda mascota deberá tener un nombre al momento de ser creada.

Una vez creada, deberá ser posible:

- consultar su nombre;
- modificar su nombre.

La clase deberá proporcionar los siguientes métodos públicos:

```java
String getNombre()
void setNombre(String nombre)
```

El atributo utilizado para almacenar el nombre deberá ser **privado**, de manera que no pueda ser accedido o modificado directamente desde fuera de la clase.

La creación de una mascota deberá realizarse indicando obligatoriamente su nombre:

```java
Mascota mascota = new Mascota("Ahsoka");
```

---

# Desarrollo paso a paso

## 1. Escribimos primero los tests

Antes de implementar completamente la clase `Mascota`, escribimos los tests que expresan el comportamiento esperado.

Archivo:

```text
src/test/java/utnfc/back/mascota/MascotaTest.java
```

```java
package utnfc.back.mascota;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("Mascota - Identidad")
class MascotaTest {

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
}
```

---

## 2. Primera ejecución

Ejecutamos:

```bash
mvn test
```

Como la clase todavía no tiene el constructor ni los métodos requeridos por el test, Maven informa errores de compilación.

Entre otros mensajes aparecen errores equivalentes a:

```text
constructor Mascota cannot be applied to given types
required: no arguments
found: java.lang.String
```

y también:

```text
cannot find symbol
method getNombre()
```

```text
cannot find symbol
method setNombre(java.lang.String)
```

Esta primera ejecución nos permite descubrir algo importante:

> El test ya está expresando el contrato que queremos que cumpla la clase, pero la clase todavía no ofrece esa interfaz.

---

## 3. Incorporamos el constructor

El primer error nos indica que estamos intentando construir una mascota pasando un `String`, pero la clase todavía no posee ese constructor.

Agregamos entonces:

```java
public Mascota(String nombre) {
}
```

Con esto aparece uno de los primeros conceptos importantes de la clase:

- el constructor tiene el mismo nombre que la clase;
- no declara tipo de retorno;
- puede recibir parámetros;
- se ejecuta cuando utilizamos `new`.

Todavía no estamos guardando el nombre. Solo estamos haciendo que la estructura requerida por el test exista.

---

## 4. Incorporamos los métodos requeridos

Agregamos ahora una implementación mínima de los métodos pedidos por el test:

```java
public String getNombre() {
    return null;
}

public void setNombre(String nombre) {
}
```

La clase ya posee ahora la interfaz requerida por los tests.

Podemos volver a ejecutar:

```bash
mvn test
```

Esta vez el proyecto **compila**, pero los tests fallan.

Un resultado posible es:

```text
expected: <Ahsoka> but was: <null>
```

y:

```text
expected: <Tamagotchi> but was: <null>
```

El dato más importante del resumen es:

```text
Tests run: 2, Failures: 2, Errors: 0
```

Ahora ya no tenemos un problema de compilación.

Los tests pudieron ejecutarse, pero la implementación no satisface el comportamiento esperado.

---

## 5. Incorporamos estado al objeto

Hasta este momento la mascota recibe un nombre, pero no lo conserva.

Necesitamos entonces agregar un atributo:

```java
private String nombre;
```

Este atributo representa parte del **estado interno** de cada objeto `Mascota`.

Lo declaramos `private` para impedir que pueda ser accedido directamente desde fuera de la clase.

---

## 6. El constructor inicializa el estado

Modificamos el constructor:

```java
public Mascota(String nombre) {
    this.nombre = nombre;
}
```

En esta sentencia aparecen dos elementos diferentes llamados `nombre`:

```java
this.nombre
```

representa el atributo del objeto actual.

Mientras que:

```java
nombre
```

representa el parámetro recibido por el constructor.

La referencia `this` permite indicar explícitamente que estamos trabajando con el atributo perteneciente al objeto actual.

---

## 7. Implementamos los métodos de acceso

El getter permite consultar el valor:

```java
public String getNombre() {
    return nombre;
}
```

El setter permite modificarlo:

```java
public void setNombre(String nombre) {
    this.nombre = nombre;
}
```

La versión completa queda:

```java
package utnfc.back.mascota;

public class Mascota {

    private String nombre;

    public Mascota(String nombre) {
        this.nombre = nombre;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}
```

---

## 8. Volvemos a ejecutar los tests

Ejecutamos nuevamente:

```bash
mvn test
```

Ahora esperamos obtener:

```text
Tests run: 2, Failures: 0, Errors: 0, Skipped: 0
```

y finalmente:

```text
BUILD SUCCESS
```

Llegamos al primer estado **verde** de nuestra implementación.

---

# Probando la clase desde una aplicación

Además de probar automáticamente la clase mediante JUnit, podemos utilizarla desde un programa Java tradicional.

Creamos:

```text
src/main/java/utnfc/back/App.java
```

```java
package utnfc.back;

import utnfc.back.mascota.Mascota;

public class App {

    public static void main(String[] args) {

        System.out.println("=== Nuestra primera Mascota ===");

        Mascota mascota = new Mascota("Ahsoka");

        System.out.println("Nombre inicial: " + mascota.getNombre());

        mascota.setNombre("Grogu");

        System.out.println("Nuevo nombre: " + mascota.getNombre());

        Mascota otraMascota = new Mascota("Chewbacca");

        System.out.println();
        System.out.println("Primera mascota: " + mascota.getNombre());
        System.out.println("Segunda mascota: " + otraMascota.getNombre());
    }
}
```

Este ejemplo permite observar que:

- `Mascota` es una clase;
- podemos crear múltiples objetos a partir de ella;
- cada objeto conserva su propio estado;
- las variables `mascota` y `otraMascota` son referencias a objetos distintos.

---

## Ejecutando la aplicación con Maven

Configuramos el plugin `exec-maven-plugin` para indicar cuál es la clase principal:

```xml
<plugin>
    <groupId>org.codehaus.mojo</groupId>
    <artifactId>exec-maven-plugin</artifactId>
    <version>3.6.3</version>
    <configuration>
        <mainClass>utnfc.back.App</mainClass>
    </configuration>
</plugin>
```

Luego podemos ejecutar:

```bash
mvn exec:java
```

Una salida posible es:

```text
=== Nuestra primera Mascota ===
Nombre inicial: Ahsoka
Nuevo nombre: Grogu

Primera mascota: Grogu
Segunda mascota: Chewbacca
```

---

# Conceptos incorporados en esta fase

Durante esta primera etapa aparecieron, a partir del código, los siguientes conceptos:

- clase;
- objeto o instancia;
- atributo;
- estado de un objeto;
- método;
- constructor;
- parámetro;
- operador `new`;
- referencia a un objeto;
- referencia `this`;
- modificadores de acceso `public` y `private`;
- métodos de acceso `get` y `set`;
- test unitario;
- diferencia entre un error de compilación y un test que ejecuta pero falla;
- ciclo básico de prueba e implementación:
  - escribir el comportamiento esperado;
  - ejecutar;
  - observar el error;
  - implementar;
  - volver a ejecutar;
  - obtener el test en verde.

---

## Para pensar

1. ¿Por qué el atributo `nombre` es `private` si tenemos métodos públicos para acceder a él?
2. ¿Qué diferencia existe entre la clase `Mascota` y la variable `mascota`?
3. ¿Qué hace el operador `new`?
4. ¿Qué representa `this.nombre` dentro del constructor?
5. ¿Por qué dos objetos construidos desde la misma clase pueden conservar nombres diferentes?
6. ¿Que una clase compile correctamente garantiza que su comportamiento sea correcto?
7. ¿Qué información adicional nos proporcionan los tests respecto de una simple ejecución manual?
