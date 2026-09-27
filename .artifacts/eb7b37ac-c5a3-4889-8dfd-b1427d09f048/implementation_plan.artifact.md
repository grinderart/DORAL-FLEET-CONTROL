# Plan de Implementación: Versión 2.1 FINAL (Lunes a Viernes)

Este plan aborda el ajuste de los recordatorios automáticos de fichaje (07:00 AM y 15:50 PM) para que funcionen exclusivamente de **Lunes a Viernes**, respetando los fines de semana, y consolida la **Versión 2.1 FINAL**.

## Cambios Propuestos

### 1. Programador de Recordatorios (`ReminderScheduler.kt`)

#### [MODIFY] [ReminderScheduler.kt](file:///home/rtorgil/AndroidStudioProjects/DORALFLEETCONTROL/app/src/main/java/com/example/doralfleetcontrol/ReminderScheduler.kt)
- Añadir lógica de filtrado de días de la semana (`Calendar.DAY_OF_WEEK`).
- Si la fecha calculada cae en **Sábado** (`Calendar.SATURDAY`) o **Domingo** (`Calendar.SUNDAY`), la alarma avanzará automáticamente hasta el próximo **Lunes** a la misma hora (07:00 AM o 15:50 PM).
- Esto garantiza que los viernes por la tarde, tras saltar la alarma de las 15:50, el siguiente recordatorio se programe automáticamente para el lunes a las 07:00 AM.

---

### 2. Actualización de Versión a 2.1 FINAL

#### [MODIFY] [MainActivity.kt](file:///home/rtorgil/AndroidStudioProjects/DORALFLEETCONTROL/app/src/main/java/com/example/doralfleetcontrol/MainActivity.kt)
- Cambiar `CURRENT_VERSION_TAG = "v2.1-FINAL"`.
- Actualizar el texto del cuadro de créditos a **"Versión 2.1 FINAL"**.

#### [MODIFY] [README.md](file:///home/rtorgil/AndroidStudioProjects/DORALFLEETCONTROL/README.md)
- Actualizar badge de versión a `Version-2.1_FINAL`.
- Reflejar que los recordatorios de fichaje operan de Lunes a Viernes.

---

### 3. Publicación y Lanzamiento

- Compilar APK para v2.1 FINAL.
- Guardar commit en Git, crear etiqueta `v2.1-FINAL` y hacer push a GitHub.

## Plan de Verificación

### Prueba Lógica
- Verificar que el cálculo de `Calendar` para días como Viernes, Sábado y Domingo devuelva la fecha del Lunes siguiente a las 07:00 AM o 15:50 PM.
