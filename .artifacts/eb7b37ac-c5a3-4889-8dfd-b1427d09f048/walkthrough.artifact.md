# Walkthrough - Versión 2.1 FINAL (Lunes a Viernes + Publicado en GitHub)

Se ha completado el ajuste del programador de recordatorios para operar exclusivamente de **Lunes a Viernes**, excluyendo los fines de semana, y se ha cerrado y publicado la **Versión 2.1 FINAL** en GitHub.

## Novedades de la Versión 2.1 FINAL

### 1. Recordatorios Exclusivos de Lunes a Viernes
- **Cálculo de Días (`ReminderScheduler.kt`)**: Se ha incorporado una verificación mediante `Calendar.DAY_OF_WEEK`.
- **Filtro de Fines de Semana**: Si el cálculo de la siguiente alarma cae en **Sábado** o **Domingo**, la alarma salta automáticamente hasta el **Lunes siguiente** a la hora correspondiente (07:00 AM o 15:50 PM).
- **Tranquilidad los Fines de Semana**: El personal no recibirá avisos de fichaje durante el sábado o domingo.

### 2. Estado del Repositorio
- **Versión**: Actualizada a `Versión 2.1 FINAL` en Créditos e información interna de la App.
- **Git Commit**: Cambios confirmados en la rama `main`.
- **Git Tag**: Etiqueta oficial `v2.1-FINAL` creada.
- **Push a GitHub**: **Exitoso** (`main -> main`, `v2.1-FINAL -> v2.1-FINAL`).

---

## Verificación Realizada

- [x] **Compilación**: `./gradlew assembleDebug` completado sin errores.
- [x] **Prueba Lógica de Fecha**: Confirmado que al programar el viernes por la tarde, la fecha calculada avanza al lunes por la mañana.
- [x] **GitHub**: Cambios y tags disponibles públicamente en `https://github.com/grinderart/DORAL-FLEET-CONTROL`.
