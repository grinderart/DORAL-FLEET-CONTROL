# Plan de Implementación: Versión 1.3 RC (BugFix + Servidor Manual)

Este plan aborda la corrección del problema de visibilidad de texto en la identificación de usuario y la incorporación de la configuración manual de servidor para la **Versión 1.3 RC**.

## Cambios Propuestos

### 1. Corrección de Bug: Visibilidad de Texto en Nombre de Piloto

#### [MODIFY] [MainActivity.kt](file:///home/rtorgil/AndroidStudioProjects/DORALFLEETCONTROL/app/src/main/java/com/example/doralfleetcontrol/MainActivity.kt)
- **Causa del Bug**: En algunos dispositivos con Modo Claro o configuraciones de tema personalizadas, el texto introducido en el `OutlinedTextField` tomaba el color oscuro por defecto, haciéndolo invisible sobre el fondo negro de la app.
- **Solución**:
    - Especificar explícitamente `textStyle = TextStyle(color = Color.White)` en el `OutlinedTextField` de `IdentificationScreen`.
    - Ajustar los colores del campo (`OutlinedTextFieldDefaults.colors`) para asegurar un alto contraste (texto blanco, bordes verdes/blancos, etiqueta visible).

---

### 2. Nueva Función: Configuración Manual de Servidor (v1.3 RC)

#### [MODIFY] [MainActivity.kt](file:///home/rtorgil/AndroidStudioProjects/DORALFLEETCONTROL/app/src/main/java/com/example/doralfleetcontrol/MainActivity.kt)
- **Persistencia**: Añadir `SERVER_URL_KEY` en `DataStore` para guardar la URL del script de cada propietario/sede.
- **Menú de Opciones**: Añadir un nuevo elemento en el menú de los 3 puntos: **"Servidor"** (con icono de nube/red).
- **Diálogo de Servidor (`ServerConfigDialog`)**:
    - Muestra la URL actual almacenada.
    - Campo para pegar/escribir una nueva URL de Google Apps Script (`https://script.google.com/...`).
    - Botón "Guardar" que actualiza inmediatamente la configuración.
- **Uso Dinámico**: Modificar `enviarDatosASheets` para que lea y utilice siempre la URL guardada en DataStore.

---

### 3. Plan de Actualizaciones OTA (Visión para v1.4 / v1.5)

Para las futuras versiones, las actualizaciones automáticas desde GitHub funcionarán así:
1. **Consulta en Inicio**: La app consultará el API de GitHub (`api.github.com/repos/grinderart/DORAL-FLEET-CONTROL/releases/latest`) en segundo plano.
2. **Detección**: Comparará el `tag_name` (ej: `v1.3`) con la versión instalada.
3. **Notificación**: Si hay una versión nueva, mostrará un aviso: *"Nueva versión 1.4 disponible"*.
4. **Instalación**: Descargará el archivo `.apk` y lanzará el instalador nativo de Android.

---

## Plan de Verificación

### Manual
1. **Bug Text Visibility**: Probar la pantalla de identificación en dispositivos con modo claro/oscuro y comprobar que el nombre tecleado se ve en blanco brillante.
2. **Configuración de Servidor**:
    - Entrar en el menú 3 puntos > Servidor.
    - Cambiar la URL por una de prueba.
    - Confirmar que se guarda en DataStore y que los escaneos usan la nueva dirección.
