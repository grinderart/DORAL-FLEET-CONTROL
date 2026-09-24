# Walkthrough - Recordatorios Diarios de Fichaje (07:00 AM y 15:50 PM)

Se han implementado las notificaciones programadas automáticas para recordar al personal operativo el fichaje de inicio y fin de jornada.

## Funcionalidades Implementadas

### 1. Horarios Programados
- **07:00 AM (Inicio de Jornada)**:
  - **Título**: DORAL Fleet Control
  - **Mensaje**: *"¿Has registrado tu vehículo hoy?"*
- **15:50 PM (Fin de Jornada)**:
  - **Título**: DORAL Fleet Control
  - **Mensaje**: *"¿Has dejado el vehículo en la sede?"*

### 2. Arquitectura de Alertas (AlarmManager + BroadcastReceiver)
- **`ReminderScheduler.kt`**:
  - Calcula el tiempo hasta la siguiente ocurrencia de las 07:00 AM o las 15:50 PM.
  - Programa alarmas exactas en el sistema utilizando `AlarmManager.RTC_WAKEUP` y `setAndAllowWhileIdle`.
- **`ReminderReceiver.kt`**:
  - `BroadcastReceiver` que captura el evento a la hora programada.
  - Emite la notificación relevante a través de un canal exclusivo de alta prioridad (`DoralRemindersChannel`).
  - **Reprogramación Automática**: Al dispararse una alarma, programa inmediatamente la del día siguiente.
- **Acceso Directo**: Al tocar la notificación, la app se abre automáticamente en la pantalla de fichaje.

### 3. Resistencia a Reinicios
- Se ha añadido el permiso `RECEIVE_BOOT_COMPLETED` y registrado la acción `android.intent.action.BOOT_COMPLETED` en el manifiesto.
- Si el teléfono del empleado se apaga o reinicia, la aplicación reprograma los dos recordatorios automáticamente al volver a encender el dispositivo.

## Verificación Realizada

- [x] **Compilación**: `./gradlew assembleDebug` ejecutado con éxito.
- [x] **Seguridad**: Uso de `PendingIntent` con banderas inmutables (`FLAG_IMMUTABLE`) para compatibilidad completa con Android 12+.
- [x] **Manifiesto**: Permisos y receptor configurados correctamente.
