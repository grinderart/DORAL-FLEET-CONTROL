# DORAL Fleet Control

![Status](https://img.shields.io/badge/Version-1.2_RC-green)
![Platform](https://img.shields.io/badge/Platform-Android-blue)
![Kotlin](https://img.shields.io/badge/Language-Kotlin-purple)

**DORAL Fleet Control** es una aplicación profesional diseñada para la gestión eficiente de flotas de vehículos. Permite el registro en tiempo real de entradas y salidas de vehículos mediante escaneo de códigos QR o introducción manual, integrándose directamente con Google Sheets para el almacenamiento de datos.

## 🚀 Características principales

- **Identificación de Pilotos**: Registro persistente del personal para agilizar el uso diario.
- **Escáner QR Nativo**: Integración con Google Play Services Code Scanner para un escaneo rápido y seguro sin necesidad de permisos de cámara manuales.
- **Registro Manual**: Opción alternativa para vehículos sin código QR asignado con validación de formato.
- **Persistencia de Datos**: La aplicación recuerda la última matrícula gestionada.
- **Integración en la Nube**: Envío automático de datos a Google Sheets mediante Google Apps Script.
- **Modo Background**: Funcionamiento persistente con notificación en la barra de estado para acceso instantáneo.
- **Interfaz Moderna**: Desarrollada íntegramente con Jetpack Compose y Material 3.

## 🛠️ Tecnologías utilizadas

- **Lenguaje**: Kotlin
- **UI**: Jetpack Compose (Material 3)
- **Networking**: OkHttp 3
- **Escaneo**: Google ML Kit & GMS Barcode Scanning
- **Almacenamiento Local**: Jetpack DataStore Preferences
- **Backend**: Google Apps Script (JavaScript)

---

## 📜 Licencia y Propiedad Intelectual

© 2026 Rayco Torres Gil. Todos los derechos reservados.

### 1. Propiedad Intelectual y Autoría
El código fuente, el diseño de la interfaz, la estructura lógica y la idea original de esta aplicación son propiedad intelectual exclusiva de su creador, **Rayco Torres Gil**. Esta aplicación ha sido desarrollada de manera independiente y por iniciativa propia, y no constituye una obra por encargo corporativo.

### 2. Ámbito de Uso Permitido
El autor concede a **DORAL PARTS S.L.** y a su personal operativo (repartidores y gestores de flota) una licencia de uso temporal, gratuita, intransferible y no exclusiva. Este uso está estrictamente limitado a la gestión interna de flotas y al registro de entrada y salida de vehículos dentro de la actividad de la empresa.

### 3. Restricciones Estrictas
Para proteger la propiedad intelectual del autor, queda terminantemente prohibido:
*   Plagiar, copiar o reproducir total o parcialmente el diseño, las funciones o el código de esta aplicación.
*   Realizar ingeniería inversa, descompilar o intentar extraer el código fuente de la misma.
*   Distribuir, vender, comercializar o registrar esta aplicación (así como cualquier obra derivada basada en esta idea o estructura) a nombre de DORAL PARTS S.L. o de cualquier tercero.

### 4. Uso de Activos Corporativos
Los logotipos, marcas comerciales e imágenes corporativas pertenecientes a **DORAL PARTS S.L.** integrados en esta aplicación son propiedad exclusiva de la empresa. Su inclusión tiene una finalidad puramente estética y de usabilidad para el entorno interno. El uso de estos elementos corporativos no otorga al autor ningún derecho sobre los mismos; de igual manera, la presencia de estos activos en la interfaz no transfiere a la empresa la titularidad, ni total ni parcial, sobre el software, su código o su propiedad intelectual, que siguen perteneciendo exclusivamente a **Rayco Torres Gil**.

El acceso y uso de esta aplicación por parte del personal de DORAL PARTS S.L. implica la aceptación incondicional de estos términos. El autor se reserva el derecho de revocar esta licencia gratuita de uso en cualquier momento si se detecta un incumplimiento de estas condiciones.
