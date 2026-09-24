# Plan de Implementación: Comprobación de Actualizaciones OTA y Lanzamiento v2.0-ALPHA

Este plan aborda la actualización del texto legal en la aplicación, la implementación del comprobador de actualizaciones OTA desde GitHub API y la subida final del repositorio.

## Cambios Propuestos

### 1. Sincronización del Blindaje Legal
- Actualizar el diálogo `LicenceDialog` en `MainActivity.kt` para reflejar con precisión las nuevas cláusulas del `README.md`:
    - Tarifas profesionales por mantenimiento/soporte.
    - Prohibición de modificaciones no autorizadas y sublicenciamiento.

### 2. Comprobador de Actualizaciones OTA (GitHub Releases API)
- Implementar una consulta asíncrona a `https://api.github.com/repos/grinderart/DORAL-FLEET-CONTROL/releases/latest` usando OkHttp.
- Extraer la última versión disponible (`tag_name`) y la URL de descarga del `.apk` (`browser_download_url`).
- Si la versión en GitHub es más reciente que la instalada:
    - Mostrar un diálogo `UpdateDialog`: *"¡Nueva versión disponible (vX.X)! ¿Deseas descargar la actualización?"*.
    - Al aceptar, abrir el enlace de descarga oficial en el navegador del sistema para instalar el nuevo APK de forma segura.

### 3. Commit, Tag v2.0-ALPHA y Push a GitHub
- Guardar todos los cambios en Git.
- Crear la etiqueta oficial `v2.0-ALPHA`.
- Subir código y tags a GitHub (`git push origin main --tags`).

## Plan de Verificación

### Manual
1. **Verificación Legal**: Abrir el menú 3 puntos > Licencia y verificar que incluye la cláusula de soporte/mantenimiento.
2. **Prueba OTA**: Verificar que la llamada al API de GitHub recupera correctamente la versión publicada.
3. **Build**: Generar el APK compilado para adjuntar a la Release en GitHub.
