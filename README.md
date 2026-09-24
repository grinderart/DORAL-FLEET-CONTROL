# DORAL Fleet Control

![Status](https://img.shields.io/badge/Version-2.0--ALPHA-orange)
![Platform](https://img.shields.io/badge/Platform-Android-blue)
![Kotlin](https://img.shields.io/badge/Language-Kotlin-purple)

**DORAL Fleet Control** es una aplicación profesional diseñada para la gestión eficiente de flotas de vehículos. Permite el registro en tiempo real de entradas y salidas de vehículos mediante escaneo de códigos QR o introducción manual, integrándose directamente con Google Sheets para el almacenamiento de datos.

## 🚀 Características principales

- **Identificación de Pilotos**: Registro persistente del personal para agilizar el uso diario (con alta visibilidad y alto contraste).
- **Servidor Dinámico & QR de Configuración (v2.0-ALPHA)**: Opción en el menú para escanear un QR con la URL del servidor o pegarla manualmente.
- **Recordatorios Diarios de Fichaje (07:00 AM y 15:50 PM)**: Notificaciones automáticas para recordar el fichaje al inicio y fin de la jornada, resistentes a reinicios.
- **Escáner QR Nativo**: Integración con Google Play Services Code Scanner para un escaneo rápido y seguro sin necesidad de permisos de cámara manuales.
- **Registro Manual**: Opción alternativa para vehículos sin código QR asignado con validación estricta de formato (7 caracteres).
- **Persistencia de Datos**: La aplicación recuerda la última matrícula gestionada gracias a Jetpack DataStore.
- **Integración en la Nube**: Envío automático de datos a Google Sheets mediante Google Apps Script.
- **Modo Background**: Funcionamiento persistente con notificación en la barra de estado para acceso instantáneo.
- **Interfaz Moderna**: Desarrollada íntegramente con Jetpack Compose, Material 3 y fondo de marca en mosaico diagonal.

## 🛠️ Tecnologías utilizadas

- **Lenguaje**: Kotlin
- **UI**: Jetpack Compose (Material 3)
- **Networking**: OkHttp 3
- **Escaneo**: Google ML Kit & GMS Barcode Scanning
- **Almacenamiento Local**: Jetpack DataStore Preferences
- **Backend**: Google Apps Script (JavaScript)

---

## ⚙️ Configuración y Despliegue (Para la Empresa/Propietario)

Para que la aplicación pueda registrar datos, se debe configurar un backend en Google Sheets siguiendo estos pasos:

### 1. Preparar la Hoja de Google
*   Crea una nueva **Hoja de Cálculo**.
*   Define los encabezados en la primera fila: 
    *   `A1: Fecha y Hora`
    *   `B1: Nombre Piloto`
    *   `C1: Matrícula`
    *   `D1: Acción`

### 2. Configurar el Google Apps Script
*   En la hoja, ve a `Extensiones` > `Apps Script`.
*   Pega el código del servidor (función `doPost` y `doGet`).
*   Haz clic en `Implementar` > `Nueva implementación`.
*   Configura el tipo como `Aplicación web`.
*   **IMPORTANTE**: En "Quién tiene acceso", selecciona **"Cualquiera" (Anyone)**.
*   Copia la URL generada (debe terminar en `/exec`).

### 3. Vincular la App con la Base de Datos
- **Opción A (Desde la propia App - Recomendado)**:
  - Abre el menú de 3 puntos (esquina superior derecha) > **Servidor**.
  - Pega la URL de tu Google Apps Script y pulsa **Guardar**.
  - La URL se guardará permanentemente en el teléfono.
- **Opción B (Desde el código fuente)**:
  - Edita la variable `DEFAULT_URL_SCRIPT` en `MainActivity.kt`.

---

### 📜 Licencia y Propiedad Intelectual

© 2026 Rayco Torres Gil. Todos los derechos reservados.

### 1. Propiedad Intelectual y Autoría
El código fuente, el diseño de la interfaz, la arquitectura técnica, la estructura lógica y la idea original de esta aplicación son propiedad intelectual exclusiva e inviolable de su creador, **Rayco Torres Gil**. Esta aplicación ha sido desarrollada de manera independiente y por iniciativa propia, no constituyendo una obra por encargo corporativo ni implicando cesión alguna de derechos patrimoniales sobre el software.

### 2. Ámbito de Uso Permitido
El autor concede a **DORAL PARTS S.L.** y a su personal operativo (repartidores y gestores de flota) una licencia de uso temporal, gratuita, intransferible y no exclusiva. Este uso está estrictamente limitado a la gestión interna de flotas y al registro de entrada y salida de vehículos dentro de la actividad ordinaria de la empresa, bajo la modalidad «tal cual» (*as-is*) de la versión actual.

La gratuidad de esta licencia se limita exclusivamente a la ejecución operativa del programa en su estado presente. Queda expresamente excluida cualquier obligación de soporte técnico, mantenimiento evolutivo, corrección de incidencias o desarrollo de nuevas funcionalidades. Toda solicitud de modificación, mejora o implementación de cambios requerida por **DORAL PARTS S.L.** estará condicionada a presupuesto previo y sujeta a la **tarifa profesional de programador** del autor. La contratación o pago de dichos servicios no conferirá en ningún caso titularidad sobre el software ni alterará los derechos exclusivos del autor.

### 3. Restricciones Estrictas
Para proteger la propiedad intelectual del autor y garantizar la inviolabilidad del software, queda terminantemente prohibido:
*   Plagiar, copiar o reproducir total o parcialmente el diseño, las funciones o el código de esta aplicación.
*   Realizar ingeniería inversa, descompilar o intentar extraer el código fuente de la misma.
*   Distribuir, vender, sublicenciar, comercializar o registrar esta aplicación (así como cualquier obra derivada basada en esta idea, estructura o código) a nombre de DORAL PARTS S.L. o de cualquier tercero.
*   Efectuar modificaciones o alteraciones en el código sin la autorización expresa, previa y por escrito del autor.

### 4. Uso de Activos Corporativos
Los logotipos, marcas comerciales e imágenes corporativas pertenecientes a **DORAL PARTS S.L.** integrados en esta aplicación son propiedad exclusiva de la empresa. Su inclusión tiene una finalidad puramente estética y de usabilidad para el entorno interno. El uso de estos elementos corporativos no otorga al autor ningún derecho sobre los mismos; de igual manera, la presencia de estos activos en la interfaz no transfiere a la empresa la titularidad, ni total ni parcial, sobre el software, su código o su propiedad intelectual, que siguen perteneciendo exclusivamente a **Rayco Torres Gil**.

El acceso y uso de esta aplicación por parte del personal de DORAL PARTS S.L. implica la aceptación incondicional de estos términos. El autor se reserva el derecho de revocar esta licencia gratuita de uso en cualquier momento si se detecta un incumplimiento de estas condiciones.