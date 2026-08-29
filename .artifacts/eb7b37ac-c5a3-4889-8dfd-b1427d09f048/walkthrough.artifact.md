# Walkthrough - Implementación de Registro Dual (QR y Manual)

Se ha añadido la capacidad de introducir matrículas de forma manual para aquellos vehículos que aún no disponen de un código QR asignado, manteniendo la misma seguridad y flujo de datos.

## Cambios Realizados

### Nuevo Flujo de Registro
- **Diálogo de Selección**: Al pulsar "Usa Vehículo" o "Deja Vehículo", la aplicación ahora presenta un menú intermedio para elegir entre el escaneo por cámara o la introducción manual.
- **Entrada Manual Segura**: Se ha implementado un cuadro de diálogo con un campo de texto que:
    - Convierte automáticamente todo a **mayúsculas**.
    - Limita la entrada a un máximo de **7 caracteres**.
    - Deshabilita el botón de registro si no se cumple la longitud exacta, evitando errores de dedo.

### Consistencia y Persistencia
- **Integración con Google Sheets**: Independientemente del método (QR o Manual), los datos se envían a la misma base de datos con la acción correspondiente.
- **Memoria de Matrícula**: Las matrículas introducidas manualmente también se guardan en la memoria local (`DataStore`) y aparecen en el recuadro verde al reabrir la app.

## Visualización de los Nuevos Diálogos

````carousel
![Selección de Método](file:///home/rtorgil/AndroidStudioProjects/DORALFLEETCONTROL/.artifacts/eb7b37ac-c5a3-4889-8dfd-b1427d09f048/preview_selection_dialog.png)
<!-- slide -->
![Entrada Manual](file:///home/rtorgil/AndroidStudioProjects/DORALFLEETCONTROL/.artifacts/eb7b37ac-c5a3-4889-8dfd-b1427d09f048/preview_manual_input.png)
````

## Verificación Realizada
- [x] **Flujo Completo**: Se ha probado el ciclo de vida desde la pulsación del botón hasta el envío final.
- [x] **Validación de Datos**: Se ha confirmado que no es posible registrar manualmente una matrícula que no tenga exactamente 7 caracteres.
- [x] **Robustez**: Los diálogos se cierran correctamente tras la acción o al pulsar fuera de ellos.
