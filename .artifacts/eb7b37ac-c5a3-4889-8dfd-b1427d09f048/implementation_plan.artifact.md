# Plan de Implementación: Registro QR vs. Manual

Este plan detalla la adición de una opción para introducir la matrícula de forma manual en caso de que el vehículo no tenga código QR asignado.

## Cambios Propuestos

### 1. Gestión de Diálogos y Estado

#### [MODIFY] [MainActivity.kt](file:///home/rtorgil/AndroidStudioProjects/DORALFLEETCONTROL/app/src/main/java/com/example/doralfleetcontrol/MainActivity.kt)
- **Nuevos Estados**:
    - `showActionDialog`: Controla el popup de selección inicial (QR vs Manual).
    - `showManualDialog`: Controla el popup para escribir la matrícula.
    - `pendingAction`: Almacena si el usuario pulsó "Usa" o "Deja" para saber qué enviar después.
- **Diálogo de Selección (`ActionSelectionDialog`)**:
    - Dos botones grandes con iconos: "Escanear QR" e "Introducir Manualmente".
- **Diálogo de Entrada Manual (`ManualInputDialog`)**:
    - Un campo de texto (`OutlinedTextField`) para escribir la matrícula.
    - Botón "Registrar" que valide los 7 caracteres antes de enviar.

### 2. Flujo de Usuario

1. El usuario pulsa **"Usa Vehículo"** o **"Deja Vehículo"**.
2. Aparece un popup: **¿Cómo quieres registrar la matrícula?**
3. Si elige **QR**: Se abre la cámara (comportamiento actual).
4. Si elige **Manual**: Aparece un teclado y escribe la matrícula (ej: `1234ABC`).
5. La app valida la longitud, guarda en memoria y envía a Google Sheets.

## Plan de Verificación

### Manual
1. **Flujo QR**: Pulsar "Usa", elegir "QR", escanear. Verificar registro.
2. **Flujo Manual**: Pulsar "Deja", elegir "Manual", escribir matrícula correcta. Verificar registro.
3. **Validación**: Escribir menos o más de 7 caracteres en el modo manual. Verificar que el botón "Registrar" esté deshabilitado o muestre error.
4. **Persistencia**: Verificar que la matrícula introducida manualmente también se queda guardada en el recuadro verde al salir y entrar.
