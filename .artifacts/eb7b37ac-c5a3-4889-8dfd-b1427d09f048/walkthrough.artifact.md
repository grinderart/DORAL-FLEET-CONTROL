# Walkthrough - Versión 2.0-ALPHA (Publicada en GitHub + OTA)

Se ha completado la implementación de la **Versión 2.0-ALPHA**, incluyendo la sincronización del blindaje legal ampliado, el comprobador automático de actualizaciones OTA vía GitHub API y la subida oficial al repositorio.

## Novedades de la Versión 2.0-ALPHA

### 1. Comprobador de Actualizaciones OTA (GitHub API)
- **Consulta Asíncrona**: Al iniciar la aplicación, consulta el endpoint oficial de GitHub Releases (`api.github.com/repos/grinderart/DORAL-FLEET-CONTROL/releases/latest`).
- **Detección Automática**: Compara la etiqueta de la versión instalada (`v2.0-ALPHA`) con la versión más reciente en GitHub.
- **Aviso Interactivo**: Si existe una nueva versión publicada en GitHub, muestra un diálogo invitando al usuario a descargar e instalar la actualización.
- **Descarga Directa**: Al confirmar, abre el enlace del APK publicado en el navegador del sistema para proceder con la instalación.

### 2. Sincronización del Blindaje Legal
- **Licencia Actualizada**: El diálogo de licencia interna y el archivo `README.md` contienen exactamente el mismo texto legal blindado:
  - **Punto 1**: Propiedad intelectual exclusiva e inviolable de Rayco Torres Gil.
  - **Punto 2**: Licencia de uso "tal cual" (*as-is*) limitada a ejecución operativa, especificando que cualquier soporte, mantenimiento o evolutivo está sujeto a presupuesto previo y tarifa profesional.
  - **Punto 3**: Prohibición explícita de plagio, ingeniería inversa, sublicenciamiento y modificaciones no autorizadas.
  - **Punto 4**: Propiedad corporativa de logotipos/activos de DORAL PARTS S.L. sin transferencia de titularidad sobre el software.

### 3. Recordatorios Diarios de Fichaje (07:00 AM y 15:50 PM)
- Notificaciones programadas automáticas para avisar al personal al inicio y fin de jornada.
- Reprogramación automática resistente a reinicios del teléfono.

### 4. Configuración de Servidor mediante Escaneo QR
- Botón rápido en el menú "Servidor" para escanear un código QR con la URL del Google Apps Script para diferentes sedes o departamentos.

---

## Verificación y Estado del Repositorio

- [x] **Compilación APK**: Compilado con éxito (`app-debug.apk`).
- [x] **Git Commit**: Commit realizado en la rama `main`.
- [x] **Git Tag**: Etiqueta `v2.0-ALPHA` creada.
- [x] **GitHub Push**: **Exitoso** (`main -> main`, `v2.0-ALPHA -> v2.0-ALPHA`).
