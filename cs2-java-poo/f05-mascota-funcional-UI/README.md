# Fase 05 — Mascota Funcional

## Objetivo

Completar el modelo de la clase `Mascota` para que satisfaga **todas las reglas de dominio del enunciado general**, apoyándose en la arquitectura de composición desarrollada en `f04-estado-compuesto`.

---

## Nuevas Reglas de Dominio Incorporadas

### 1. Vida y Muerte
- `isViva()`: Determina si la mascota sigue con vida.
- Al morir:
  - `energia` se fija en `0`.
  - `humor` se preserva con su último estado.
  - La mascota no responde a ninguna interacción posterior (`comer()`, `beber()`, `correr()`, `saltar()`, `dormir()`, `despertar()`, `respondeA(...)` retornan `false` y no modifican el estado).

### 2. Rachas de Ingesta (`comer()` / `beber()`)
- **1° y 2° ingesta consecutiva**: Incrementa energía porcentual y `humor +1`.
- **3° y 4° ingesta consecutiva**: Incrementa energía porcentual, pero el humor pasa a decrementar: `humor -1`.
- **5° ingesta consecutiva**: **Muerte por empacho** (`isViva() = false`, `energia = 0`).
- **Interrupción**: Una actividad (`correr()`, `saltar()`), dormir o despertar corta la racha de ingestas.

### 3. Rachas de Actividad (`correr()` / `saltar()`)
- Cada actividad consume energía porcentual (-35% o -15%) y reduce `humor -2`.
- **Muerte por agotamiento**: Si la energía se reduce a `0`, la mascota muere.
- **3° actividad consecutiva**: La mascota se empaca y **pasa al estado dormida**.
- **Interrupción**: Una ingesta (`comer()`, `beber()`), dormir o despertar corta la racha de actividades.

### 4. Representación Textual (`toString()`)
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

## Ejecución y Tests

### Ejecutar Tests Unitarios
```powershell
mvn test
```

### Ejecutar Aplicación de Consola
```powershell
mvn exec:java
```

### Ejecutar Interfaz Gráfica (UI)
```powershell
mvn exec:java "-Dexec.mainClass=utnfc.back.ui.MascotaAppUI"
```
