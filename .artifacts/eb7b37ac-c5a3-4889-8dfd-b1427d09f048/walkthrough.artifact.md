# Walkthrough - Indicador de Sede Conectada y Configuración de Servidor

Se ha implementado el indicador visual de **Sede Conectada** directamente en la pantalla principal sobre el logotipo de DORAL, permitiendo identificar claramente a qué base de datos/departamento está enviando los registros cada dispositivo.

## Novedades Implementadas

### 1. Indicador Visual de Sede
- **Ubicación Estratégica**: Colocado justo encima del logotipo principal en `MainScreen` e `IdentificationScreen`.
- **Estilo**: Texto en verde suave (`#81C784`) con el mensaje: **`Conectado a la Sede de [Nombre]`** (por defecto `"DEMO"`).

### 2. Configuración de Servidor y Sede
- **Diálogo Actualizado (`ServerConfigDialog`)**:
  - Campo para el **Nombre de la Sede** (ej: `DEMO`, `Taller Sur`, `Repuestos Central`).
  - Campo para la **URL del Servidor**.
  - **Escáner QR Inteligente**: Si se escanea un QR con formato JSON `{"sede": "...", "url": "..."}`, la app autocompleta e identifica ambos campos al instante.
- **Persistencia**: La sede seleccionada se guarda en `DataStore` (`SEDE_NAME_KEY`), manteniendo el nombre entre reinicios de la aplicación.

### 3. Documentación de Futuro (Opción 1)
- Se ha incluido en el plan de la app las instrucciones técnicas para que, en futuras versiones de Google Apps Script, la app pueda descubrir el nombre de la sede automáticamente mediante `doGet`.

---

## Capturas de Previsualización

```carousel
![Pantalla Principal con Indicador de Sede](file:///home/rtorgil/AndroidStudioProjects/DORALFLEETCONTROL/.artifacts/eb7b37ac-c5a3-4889-8dfd-b1427d09f048/preview_sede_indicator.png)
<!-- slide -->
![Diálogo de Configuración de Servidor y Sede](file:///home/rtorgil/AndroidStudioProjects/DORALFLEETCONTROL/.artifacts/eb7b37ac-c5a3-4889-8dfd-b1427d09f048/preview_sede_dialog.png)
```

## Verificación Realizada
- [x] **Compilación**: `./gradlew assembleDebug` exitoso.
- [x] **GitHub**: Cambios subidos a la rama `main` en `https://github.com/grinderart/DORAL-FLEET-CONTROL`.
