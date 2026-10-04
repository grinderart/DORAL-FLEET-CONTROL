# Plan de Implementación: Visualización de Sede Conectada (Opción 2 + Guía Opción 1)

Este plan detalla la adición del indicador de **Sede Conectada** encima del logotipo de DORAL, permitiendo configurar tanto el nombre de la sede como la URL del servidor sin necesidad de modificar el script de Google actual.

## Cambios Propuestos

### 1. Persistencia de Sede (DataStore)

#### [MODIFY] [MainActivity.kt](file:///home/rtorgil/AndroidStudioProjects/DORALFLEETCONTROL/app/src/main/java/com/example/doralfleetcontrol/MainActivity.kt)
- **Nueva Clave DataStore**: Añadir `SEDE_NAME_KEY` con valor por defecto `"DEMO"`.
- **Carga de Estado**: Leer `sedeName` e integrarlo en el flujo global de datos de la app.

---

### 2. Interfaz de Usuario (Compose)

#### [MODIFY] [MainActivity.kt](file:///home/rtorgil/AndroidStudioProjects/DORALFLEETCONTROL/app/src/main/java/com/example/doralfleetcontrol/MainActivity.kt)
- **Indicador de Sede**: Añadir el texto **`Conectado a la Sede de [Nombre]`** justo encima del logotipo de DORAL en `MainScreen` e `IdentificationScreen` (estilizado en verde suave `0xFF81C784`).
- **Configuración de Servidor Actualizada (`ServerConfigDialog`)**:
    - Campo 1: **Nombre de la Sede** (ej: `DEMO`, `Sede Central`, `Taller Sur`).
    - Campo 2: **URL del Servidor**.
    - **Escáner QR Inteligente**: Si el QR escaneado contiene un formato con metadatos JSON `{"sede": "...", "url": "..."}`, la app autocompletará ambos campos automáticamente. Si es una URL simple, actualizará la URL y mantendrá o solicitará el nombre.

---

### 3. Guía de Futuro: Guía de la Opción 1 (Auto-Descubrimiento en Servidor)

Para cuando se creen las nuevas hojas de cálculo de Google y se desee que la app descubra el nombre de la sede automáticamente sin escribir nada:

```javascript
// Añadir esta función en el Google Apps Script de cada nueva hoja:
function doGet(e) {
  var nombreSede = SpreadsheetApp.getActiveSpreadsheet().getName();
  var respuesta = {
    "status": "OK",
    "sede": nombreSede
  };
  return ContentService.createTextOutput(JSON.stringify(respuesta))
                       .setMimeType(ContentService.MimeType.JSON);
}
```

---

## Plan de Verificación

### Manual
1. **Verificación Visual**: Abrir la pantalla principal y confirmar que aparece *"Conectado a la Sede de DEMO"* en verde sobre el logotipo.
2. **Cambio de Sede**: Ir al menú 3 puntos > Servidor, cambiar la sede a *"Sede Norte"* y guardar. Verificar que el texto de la pantalla cambia a *"Conectado a la Sede de Sede Norte"*.
3. **Escaneo QR**: Probar el escáner de servidor con un QR de formato simple o con metadatos.
