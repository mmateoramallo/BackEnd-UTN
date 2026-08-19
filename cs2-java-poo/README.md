# Clase Semana 2 — Java y Programación Orientada a Objetos

Proyecto incremental para introducir y consolidar conceptos de **Programación Orientada a Objetos con Java** a partir de la construcción de una mascota virtual.

La propuesta parte de una implementación mínima y evoluciona progresivamente. Cada fase agrega comportamiento, introduce nuevos conceptos y conserva los tests de las fases anteriores como red de seguridad.

El objetivo no es resolver el problema completo desde el comienzo, sino **hacer crecer el modelo mientras aparecen naturalmente los conceptos de diseño que necesitamos discutir**.

---

# Enunciado general — Mascota Virtual

## Introducción

Modelar e implementar en Java la clase `Mascota`, a partir de la cual se puedan instanciar objetos que representen una mascota virtual en una aplicación que emula este tipo de juegos.

Cada mascota posee un **nombre**, un nivel de **energía**, un nivel de **humor**, un estado de descanso y, en la versión completa, un estado de vida.

La solución será construida incrementalmente durante las distintas fases del ejercicio.

---

## Estado de la mascota

### Nombre

Cada mascota posee un nombre que permite identificarla.

---

### Energía

La energía se representa mediante un número entero comprendido entre:

```text
0 y 100
```

Los cálculos porcentuales de energía utilizan solamente unidades completas.

Cuando el resultado de un cálculo contiene una parte decimal, esta se descarta.

Por ejemplo:

```text
energía actual = 53
10% de 53 = 5,3
incremento efectivo = 5
```

La energía nunca puede superar las `100` unidades.

Cuando la energía llega a `0`, la mascota muere por agotamiento.

---

### Humor

El humor se representa internamente mediante un valor entero comprendido entre `1` y `5`:

```text
1 -> Muy enojado
2 -> Enojado
3 -> Neutral
4 -> Contento
5 -> Chocho
```

El humor mínimo posible es `1`.

La representación numérica es un detalle interno del modelo y no necesita ser expuesta directamente al exterior.

---

# Comportamientos

Los comportamientos se agrupan en:

- comportamientos de ingesta;
- comportamientos de actividad;
- comportamientos de descanso.

---

## Comportamientos de ingesta

### Comer

```text
comer()
```

Incrementa la energía en un `10%` de la energía actual.

Además, normalmente incrementa el humor en `1` nivel.

A partir de la **tercera ingesta consecutiva**, la mascota comienza a molestarse: el incremento de humor deja de aplicarse y se transforma directamente en un decremento de `1` nivel por cada nueva ingesta.

---

### Beber

```text
beber()
```

Incrementa la energía en un `5%` de la energía actual.

Además, normalmente incrementa el humor en `1` nivel.

Al igual que al comer, desde la **tercera ingesta consecutiva** el incremento de humor se reemplaza por un decremento de `1`.

---

# Comportamientos de actividad

## Correr

```text
correr()
```

Reduce la energía en un `35%` de la energía actual.

Además reduce el humor en `2` niveles.

---

## Saltar

```text
saltar()
```

Reduce la energía en un `15%` de la energía actual.

Además reduce el humor en `2` niveles.

---

# Descanso

## Dormir

```text
dormir()
```

La mascota pasa al estado dormida.

Además:

```text
energía +25
humor +2
```

Dormir corta las rachas de ingesta y de actividad.

Mientras está dormida, la mascota no responde a los demás comportamientos.

---

## Despertar

```text
despertar()
```

La mascota pasa nuevamente al estado despierta.

Además:

```text
humor -1
```

El ciclo de descanso corta las rachas que la mascota pudiera estar acumulando.

Una mascota dormida solamente puede despertarse.

---

# Consecutividad de comportamientos

## Racha de ingestas

Una racha de ingestas está formada por acciones consecutivas de:

```text
comer
beber
```

sin una actividad de movimiento entre ellas.

La racha se corta cuando la mascota:

- realiza una actividad de movimiento;
- duerme;
- despierta.

A partir de la tercera ingesta consecutiva, cada nueva ingesta reduce el humor en un nivel en lugar de incrementarlo.

Si la mascota realiza **cinco ingestas consecutivas**, muere de empacho.

---

## Racha de actividades

Una racha de actividades está formada por acciones consecutivas de:

```text
correr
saltar
```

La racha se corta cuando la mascota:

- realiza una ingesta;
- duerme;
- despierta.

Cuando realiza **tres actividades consecutivas**, la mascota se empaca y se duerme.

---

# Vida y muerte

La mascota muere cuando:

- su energía llega a `0`;
- realiza cinco ingestas consecutivas.

Cuando la mascota muere:

```text
energía = 0
```

El humor se conserva como parte del último estado alcanzado.

Una mascota muerta ya no responde a ningún comportamiento.

En esta versión del ejercicio, cualquier intento de realizar un comportamiento sobre una mascota muerta retorna:

```java
false
```

sin modificar su estado.

---

# Retorno de los comportamientos

Los comportamientos que representan acciones retornan un valor booleano.

```java
true
```

indica que el comportamiento pudo realizarse correctamente.

```java
false
```

indica que el comportamiento no pudo realizarse debido al estado de la mascota.

Por ejemplo:

- intentar comer mientras duerme;
- intentar correr mientras duerme;
- intentar realizar cualquier comportamiento cuando está muerta.

---

# Representación textual

La mascota debe sobrescribir:

```java
toString()
```

para retornar una representación legible de su estado.

Durante las fases del ejercicio utilizaremos una representación inspirada visualmente en JSON.

En la versión completa deberá permitir observar al menos:

- nombre;
- energía;
- humor;
- si está dormida;
- si está viva.

---

# Desarrollo incremental

**El enunciado anterior describe el comportamiento completo de la mascota, pero NO se implementará todo de una vez.**

El ejercicio está dividido en fases.

Cada fase parte de la anterior, conserva lo aprendido y agrega nuevos conceptos.

La intención es observar cómo una solución inicialmente pequeña comienza a crecer y cómo ese crecimiento nos obliga a tomar decisiones de diseño.

---

# Estructura del proyecto

```text
.
├── f01-identidad
│   ├── pom.xml
│   ├── README.md
│   ├── src
│   └── target
├── f02-descanso
│   ├── pom.xml
│   ├── README-ori.md
│   ├── README.md
│   └── src
├── f03-estado-base
│   ├── pom.xml
│   ├── README.md
│   └── src
├── f04-estado-compuesto
│   ├── pom.xml
│   ├── README.md
│   ├── src
│   └── target
└── README.md
```

---

# Fases

## [Fase 01 — Identidad](./f01-identidad/README.md)

La primera versión de la mascota es deliberadamente mínima.

El objetivo es comenzar a trasladar los conceptos conocidos de objetos hacia Java sin introducir todavía complejidad de negocio.

Trabajamos con:

- definición de una clase;
- creación de objetos;
- atributo `nombre`;
- constructor;
- `private`;
- `public`;
- `getNombre()`;
- `setNombre()`;
- referencia `this`;
- primeros tests unitarios con JUnit;
- ciclo test → implementación → test verde;
- compilación y ejecución mediante Maven.

La mascota comienza simplemente siendo capaz de **tener y conservar una identidad**.

---

## [Fase 02 — Descanso](./f02-descanso/README.md)

Incorporamos el primer estado que afecta realmente al comportamiento de la mascota:

```java
private boolean dormida;
```

Aparecen:

```java
isDormida()
dormir()
despertar()
respondeA(String nombre)
```

Esta fase permite diferenciar dos ideas importantes:

### Consultar estado

```java
getNombre()
isDormida()
```

### Provocar comportamiento

```java
dormir()
despertar()
respondeA(...)
```

La mascota puede conservar su nombre mientras duerme, pero su comportamiento cambia: dormida ya no responde cuando la llaman.

Comenzamos así a observar que un objeto no es solamente un conjunto de datos: **su comportamiento depende de su estado**.

---

## [Fase 03 — Estado Base](./f03-estado-base/README.md)

Incorporamos:

```java
private int energia;
private int humor;
```

y comenzamos a implementar los comportamientos principales:

```java
comer()
beber()
correr()
saltar()
dormir()
despertar()
```

En esta etapa aparecen:

- energía entre `0` y `100`;
- humor entre `1` y `5`;
- truncamiento de porcentajes;
- constructores sobrecargados;
- setters privados;
- invariantes;
- encapsulamiento;
- métodos que modifican estado;
- tests parametrizados;
- conservación de tests anteriores;
- `toString()`;
- `@Override`;
- Text Blocks;
- `formatted()`.

El humor se conserva internamente como un entero, pero se expone mediante una representación significativa:

```text
Muy enojado
Enojado
Neutral
Contento
Chocho
```

La fase termina deliberadamente con una clase `Mascota` que **funciona pero comienza a conocer demasiadas cosas**.

Ese problema prepara la siguiente refactorización.

---

## [Fase 04 — Estado Compuesto](./f04-estado-compuesto/README.md)

Refactorizamos:

```java
private int energia;
private int humor;
```

hacia:

```java
private Energia energia;
private Humor humor;
```

Aparecen dos nuevas clases con sus propias responsabilidades y sus propias suites de tests:

```text
EnergiaTest
HumorTest
```

Mientras tanto:

```text
MascotaTest
```

debería continuar funcionando sin cambios.

Esta fase introduce y permite discutir:

- composición;
- referencias entre objetos;
- objetos como atributos;
- delegación;
- colaboración entre objetos;
- encapsulamiento real;
- distribución de responsabilidades;
- invariantes;
- refactorización;
- tests de regresión;
- conservación del contrato público;
- una primera aproximación al principio de responsabilidad única.

La transformación fundamental puede observarse en `comer()`.

### Antes

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

### Después

```java
this.energia.incrementarPorcentaje(10);
this.humor.incrementar(1);
```

`Mascota` continúa sabiendo **qué significa comer**, pero deja de saber **cómo administrar internamente energía y humor**.

---

# Próxima etapa — Fase 05: Mascota Funcional

Una vez construido y refactorizado el modelo base, queda implementar las reglas completas del enunciado.

En `f05-mascota-funcional` incorporaremos:

- rachas de ingesta;
- rachas de actividad;
- corte de consecutividad;
- cambio de humor desde la tercera ingesta;
- muerte por cinco ingestas consecutivas;
- muerte cuando la energía llega a cero;
- sueño después de tres actividades consecutivas;
- vida y muerte;
- restricciones de comportamiento según estado;
- interacción entre todas las reglas.

La diferencia fundamental es que ahora llegamos a esas reglas con un modelo donde las responsabilidades ya están mejor distribuidas.

---

# Recorrido conceptual

El proyecto puede verse como una evolución:

```text
F01
Mascota
└── identidad
        ↓
F02
Mascota
├── identidad
└── descanso
        ↓
F03
Mascota
├── identidad
├── descanso
├── energía
├── humor
├── ingesta
└── actividad
        ↓
F04
Mascota
├── identidad
├── descanso
├── comportamientos
├── Energia
│   └── reglas de energía
└── Humor
    └── reglas de humor
        ↓
F05
Mascota funcional
└── reglas completas del dominio
```

---

# Idea central

A lo largo del ejercicio no buscamos solamente aprender sintaxis Java.

Buscamos observar cómo los conceptos de orientación a objetos aparecen al resolver problemas concretos:

```text
Clase
    ↓
Objeto
    ↓
Estado
    ↓
Comportamiento
    ↓
Encapsulamiento
    ↓
Referencias
    ↓
Composición
    ↓
Delegación
    ↓
Distribución de responsabilidades
```

Y todo el recorrido se apoya en una idea transversal:

> **Los tests describen el comportamiento esperado y nos permiten hacer evolucionar el diseño conservando aquello que ya funciona.**

---

# Fase 05 — Mascota Funcional

## Consigna

Partiendo de la implementación obtenida en `f04-estado-compuesto`, completar el comportamiento de `Mascota` para que cumpla **todas las reglas del dominio planteadas en el enunciado general**.

Las clases `Energia` y `Humor` ya encapsulan las reglas propias de esas dimensiones. En esta fase deberán incorporarse las reglas que dependen de la **secuencia de acciones y del estado general de la mascota**.

## Consecutividad de ingestas

Se consideran comportamientos de ingesta:

```text
comer()
beber()
```

La mascota debe llevar registro de las ingestas realizadas consecutivamente.

Una racha de ingestas se interrumpe cuando la mascota:

- realiza una actividad (`correr()` o `saltar()`);
- duerme;
- despierta.

### Tercera ingesta consecutiva

Las dos primeras ingestas consecutivas producen normalmente:

```text
humor +1
```

A partir de la **tercera ingesta consecutiva**, la mascota comienza a molestarse.

Desde ese momento, cada nueva ingesta debe:

```text
humor -1
```

Es importante observar que **no se incrementa y luego se decrementa el humor**.

El incremento habitual es reemplazado directamente por el decremento.

### Quinta ingesta consecutiva

Cuando la mascota realiza la **quinta ingesta consecutiva**, muere de empacho.

Al morir:

```text
energia = 0
```

y deja de responder a cualquier comportamiento posterior.

---

## Consecutividad de actividades

Se consideran comportamientos de actividad:

```text
correr()
saltar()
```

La mascota debe llevar registro de las actividades realizadas consecutivamente.

Una racha de actividades se interrumpe cuando la mascota:

- realiza una ingesta (`comer()` o `beber()`);
- duerme;
- despierta.

Cuando realiza la **tercera actividad consecutiva**, la mascota se empaca y pasa al estado dormida.

---

## Muerte por agotamiento

Luego de realizar una actividad debe verificarse el estado de la energía.

Cuando:

```text
energia == 0
```

la mascota muere por agotamiento.

Una mascota muerta conserva el último estado alcanzado de su humor, pero su energía debe permanecer en:

```text
0
```

---

## Comportamiento de una mascota muerta

Una vez muerta, la mascota no puede volver a realizar ninguna acción.

Cualquier intento posterior de:

```text
comer()
beber()
correr()
saltar()
dormir()
despertar()
```

debe:

- retornar `false`;
- no modificar ningún atributo ni objeto que forme parte de su estado.

La muerte es un estado definitivo.

---

## Comportamiento de una mascota dormida

Mientras la mascota está dormida:

```text
comer()
beber()
correr()
saltar()
```

deben retornar:

```java
false
```

sin producir modificaciones.

El único comportamiento que permite abandonar ese estado es:

```java
despertar()
```

---

## Vida

Agregar al modelo la posibilidad de determinar si la mascota continúa viva.

La representación producida por:

```java
toString()
```

deberá incorporar también esta información.

Una posible salida final será:

```json
{
  "nombre": "Ahsoka",
  "energia": 58,
  "humor": "Contento",
  "dormida": false,
  "viva": true
}
```

---

# Condición de finalización

La implementación estará completa cuando la mascota respete simultáneamente:

- los límites y comportamientos de `Energia`;
- los límites y comportamientos de `Humor`;
- las reglas de descanso;
- las reglas de ingesta;
- las reglas de actividad;
- la consecutividad de acciones;
- la muerte por agotamiento;
- la muerte por empacho;
- las restricciones correspondientes a una mascota dormida;
- las restricciones correspondientes a una mascota muerta.

Además, **todos los tests construidos en las fases anteriores deben continuar pasando**.

Los nuevos comportamientos deberán incorporarse mediante nuevos tests que permitan verificar cada una de estas reglas.

> El objetivo no es solamente conseguir que la mascota funcione, sino incorporar las nuevas reglas procurando conservar la distribución de responsabilidades alcanzada en la fase anterior.