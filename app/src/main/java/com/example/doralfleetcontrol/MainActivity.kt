package com.example.doralfleetcontrol

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Assignment
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material.icons.filled.VpnKey
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.ImageShader
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.lifecycleScope
import com.example.doralfleetcontrol.ui.theme.DORALFLEETCONTROLTheme
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.codescanner.GmsBarcodeScannerOptions
import com.google.mlkit.vision.codescanner.GmsBarcodeScanning
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject

// Extensión para acceder a DataStore
private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")
private val PILOT_NAME_KEY = stringPreferencesKey("pilot_name")
private val LAST_PLATE_KEY = stringPreferencesKey("last_plate")

class MainActivity : ComponentActivity() {

    private val URL_SCRIPT = "https://script.google.com/macros/s/AKfycbxiw3bZHGE502h7hfPj85XBlOQGJpSMlTd9l0uKjEqqo0SpxKnmrvKMWEkGVHlkOGfl7w/exec"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DORALFLEETCONTROLTheme {
                val settings by remember {
                    dataStore.data.map { preferences ->
                        Pair(preferences[PILOT_NAME_KEY], preferences[LAST_PLATE_KEY])
                    }
                }.collectAsState(initial = Pair("", "Ninguna"))

                val pilotName = settings.first
                val lastPlate = settings.second

                // Lógica de permisos de notificación para Android 13+
                val context = LocalContext.current
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    val launcher = rememberLauncherForActivityResult(
                        contract = ActivityResultContracts.RequestPermission(),
                        onResult = { isGranted ->
                            if (isGranted && !pilotName.isNullOrBlank()) {
                                startFleetService(context)
                            }
                        }
                    )
                    LaunchedEffect(Unit) {
                        if (context.checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                            launcher.launch(Manifest.permission.POST_NOTIFICATIONS)
                        }
                    }
                }

                // Iniciar servicio si ya hay nombre y permisos (o versión anterior)
                LaunchedEffect(pilotName) {
                    if (!pilotName.isNullOrBlank()) {
                        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU || 
                            context.checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) {
                            startFleetService(context)
                        }
                    }
                }

                TiledBackground {
                    if (pilotName.isNullOrBlank()) {
                        IdentificationScreen { name ->
                            lifecycleScope.launch {
                                saveName(name)
                            }
                        }
                    } else {
                        MainScreen(pilotName, lastPlate ?: "Ninguna")
                    }
                }
            }
        }
    }

    private suspend fun saveName(name: String) {
        dataStore.edit { preferences ->
            preferences[PILOT_NAME_KEY] = name
        }
    }

    private suspend fun saveLastPlate(plate: String) {
        dataStore.edit { preferences ->
            preferences[LAST_PLATE_KEY] = plate
        }
    }

    @Composable
    fun TiledBackground(content: @Composable () -> Unit) {
        val image = ImageBitmap.imageResource(id = R.drawable.caddy)
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black)
                .drawBehind {
                    rotate(-45f) {
                        val paint = Paint().apply {
                            shader = ImageShader(image, TileMode.Repeated, TileMode.Repeated)
                            alpha = 0.1f
                        }
                        // Dibujamos un rectángulo mucho más grande para cubrir los huecos de la rotación
                        val extra = size.maxDimension
                        drawIntoCanvas { canvas ->
                            canvas.drawRect(
                                left = -extra,
                                top = -extra,
                                right = size.width + extra,
                                bottom = size.height + extra,
                                paint = paint
                            )
                        }
                    }
                }
        ) {
            content()
        }
    }

    private fun startFleetService(context: Context) {
        val intent = Intent(context, FleetService::class.java)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(intent)
        } else {
            context.startService(intent)
        }
    }

    private fun stopFleetService(context: Context) {
        val intent = Intent(context, FleetService::class.java)
        context.stopService(intent)
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun AppTopBar(onExit: () -> Unit) {
        var showMenu by remember { mutableStateOf(false) }
        var showCredits by remember { mutableStateOf(false) }
        var showLicence by remember { mutableStateOf(false) }

        TopAppBar(
            title = { },
            actions = {
                IconButton(onClick = { showMenu = true }) {
                    Icon(
                        imageVector = Icons.Default.MoreVert,
                        contentDescription = "Menú",
                        tint = Color.White
                    )
                }
                DropdownMenu(
                    expanded = showMenu,
                    onDismissRequest = { showMenu = false }
                ) {
                    DropdownMenuItem(
                        text = { Text("Créditos") },
                        onClick = {
                            showMenu = false
                            showCredits = true
                        },
                        leadingIcon = { Icon(Icons.Default.Info, contentDescription = null) }
                    )
                    DropdownMenuItem(
                        text = { Text("Licencia") },
                        onClick = {
                            showMenu = false
                            showLicence = true
                        },
                        leadingIcon = { Icon(Icons.Default.Assignment, contentDescription = null) }
                    )
                    DropdownMenuItem(
                        text = { Text("Salir") },
                        onClick = {
                            showMenu = false
                            onExit()
                        },
                        leadingIcon = { Icon(Icons.Default.VpnKey, contentDescription = null) }
                    )
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color.Transparent
            )
        )

        if (showCredits) {
            AlertDialog(
                onDismissRequest = { showCredits = false },
                title = { Text("Créditos") },
                text = {
                    Column {
                        Text("DORAL Fleet Control", fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text("Versión 1.2 RC", fontSize = 12.sp, color = Color.Gray)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("Desarrollado para la gestión de flota de DORAL.")
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("© 2026 - Rayco T.G.", color = MaterialTheme.colorScheme.primary)
                    }
                },
                confirmButton = {
                    TextButton(onClick = { showCredits = false }) {
                        Text("Cerrar")
                    }
                }
            )
        }

        if (showLicence) {
            AlertDialog(
                onDismissRequest = { showLicence = false },
                title = { Text("Licencia de Uso") },
                text = {
                    val scroll = rememberScrollState()
                    Column(
                        modifier = Modifier
                            .height(400.dp)
                            .verticalScroll(scroll)
                    ) {
                        Text(
                            text = "© 2026 Rayco Torres Gil. Todos los derechos reservados.",
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        
                        Text("1. Propiedad Intelectual y Autoría", fontWeight = FontWeight.Bold)
                        Text(
                            "El código fuente, el diseño de la interfaz, la estructura lógica y la idea original de esta aplicación son propiedad intelectual exclusiva de su creador, Rayco Torres Gil. Esta aplicación ha sido desarrollada de manera independiente y por iniciativa propia, y no constituye una obra por encargo corporativo.",
                            fontSize = 13.sp
                        )
                        Spacer(modifier = Modifier.height(12.dp))

                        Text("2. Ámbito de Uso Permitido", fontWeight = FontWeight.Bold)
                        Text(
                            "El autor concede a DORAL PARTS S.L. y a su personal operativo (repartidores y gestores de flota) una licencia de uso temporal, gratuita, intransferible y no exclusiva. Este uso está estrictamente limitado a la gestión interna de flotas y al registro de entrada y salida de vehículos dentro de la actividad de la empresa.",
                            fontSize = 13.sp
                        )
                        Spacer(modifier = Modifier.height(12.dp))

                        Text("3. Restricciones Estrictas", fontWeight = FontWeight.Bold)
                        Text(
                            "Para proteger la propiedad intelectual del autor, queda terminantemente prohibido:\n\n" +
                            "• Plagiar, copiar o reproducir total o parcialmente el diseño, las funciones o el código de esta aplicación.\n" +
                            "• Realizar ingeniería inversa, descompilar o intentar extraer el código fuente de la misma.\n" +
                            "• Distribuir, vender, comercializar o registrar esta aplicación (así como cualquier obra derivada basada en esta idea o estructura) a nombre de DORAL PARTS S.L. o de cualquier tercero.",
                            fontSize = 13.sp
                        )
                        Spacer(modifier = Modifier.height(12.dp))

                        Text("4. Uso de Activos Corporativos", fontWeight = FontWeight.Bold)
                        Text(
                            "Los logotipos, marcas comerciales e imágenes corporativas pertenecientes a DORAL PARTS S.L. (incluyendo, pero no limitándose a, iconos de vehículos y enlaces a redes sociales) integrados en esta aplicación son propiedad exclusiva de la empresa. Su inclusión tiene una finalidad puramente estética y de usabilidad para el entorno interno. El uso de estos elementos corporativos no otorga al autor ningún derecho sobre los mismos; de igual manera, la presencia de estos activos en la interfaz no transfiere a la empresa la titularidad, ni total ni parcial, sobre el software, su código o su propiedad intelectual, que siguen perteneciendo exclusivamente a Rayco Torres Gil.",
                            fontSize = 13.sp
                        )
                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            "El acceso y uso de esta aplicación por parte del personal de DORAL PARTS S.L. implica la aceptación incondicional de estos términos. El autor, Rayco Torres Gil, se reserva el derecho de revocar esta licencia gratuita de uso en cualquier momento si se detecta un incumplimiento de estas condiciones.",
                            fontSize = 13.sp,
                            fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
                        )
                    }
                },
                confirmButton = {
                    TextButton(onClick = { showLicence = false }) {
                        Text("He leído y acepto")
                    }
                }
            )
        }
    }

    @Composable
    fun IdentificationScreen(onStart: (String) -> Unit) {
        var nameInput by remember { mutableStateOf("") }
        val context = LocalContext.current

        Scaffold(
            modifier = Modifier.fillMaxSize(),
            containerColor = Color.Transparent,
            topBar = {
                AppTopBar(onExit = {
                    stopFleetService(context)
                    finishAndRemoveTask()
                })
            }
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(horizontal = 32.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.logo_doral),
                    contentDescription = "Logo DORAL",
                    modifier = Modifier.size(150.dp)
                )

                Spacer(modifier = Modifier.height(32.dp))

                Text(
                    text = "Bienvenido a DORAL",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Por favor, introduce tu nombre para empezar",
                    fontSize = 16.sp,
                    color = Color.LightGray
                )

                Spacer(modifier = Modifier.height(32.dp))

                OutlinedTextField(
                    value = nameInput,
                    onValueChange = { nameInput = it },
                    label = { Text("Nombre del Piloto") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp)
                )

                Spacer(modifier = Modifier.height(32.dp))

                Button(
                    onClick = { if (nameInput.isNotBlank()) onStart(nameInput) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    enabled = nameInput.isNotBlank(),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(text = "Empezar", fontSize = 18.sp)
                }
            }
        }
    }

    @Composable
    fun MainScreen(pilotName: String, initialPlate: String) {
        var scannedPlate by remember { mutableStateOf(initialPlate) }
        val context = LocalContext.current
        val scope = rememberCoroutineScope()

        // Estados para los nuevos diálogos
        var showActionDialog by remember { mutableStateOf(false) }
        var showManualDialog by remember { mutableStateOf(false) }
        var pendingAction by remember { mutableStateOf("") } // "USA VEHICULO" o "DEJA VEHICULO"

        // Manejo del botón atrás: minimiza la app conservando la matrícula
        BackHandler {
            moveTaskToBack(true)
        }

        Scaffold(
            modifier = Modifier.fillMaxSize(),
            containerColor = Color.Transparent,
            topBar = {
                AppTopBar(onExit = {
                    stopFleetService(context)
                    finishAndRemoveTask()
                })
            }
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.logo_doral),
                    contentDescription = "Logo DORAL",
                    modifier = Modifier.size(120.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Control de Flotas DORAL",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )

                Text(
                    text = "Hola, $pilotName",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.LightGray
                )
                
                Spacer(modifier = Modifier.height(32.dp))

                Text(
                    text = "Matrícula detectada:",
                    fontSize = 16.sp,
                    color = Color.LightGray
                )
                
                Spacer(modifier = Modifier.height(8.dp))

                Surface(
                    modifier = Modifier
                        .padding(horizontal = 32.dp)
                        .border(2.dp, Color(0xFF2E7D32), RoundedCornerShape(8.dp)),
                    shape = RoundedCornerShape(8.dp),
                    color = Color(0xFFF1F8E9).copy(alpha = 0.9f)
                ) {
                    Box(
                        modifier = Modifier
                            .padding(horizontal = 24.dp, vertical = 12.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = scannedPlate,
                            fontSize = 28.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color(0xFF2E7D32)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))

                Button(
                    onClick = {
                        pendingAction = "USA VEHICULO"
                        showActionDialog = true
                    },
                    modifier = Modifier.width(240.dp)
                ) {
                    Icon(imageVector = Icons.Default.QrCodeScanner, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = "Usa Vehículo")
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        pendingAction = "DEJA VEHICULO"
                        showActionDialog = true
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.secondary
                    ),
                    modifier = Modifier.width(240.dp)
                ) {
                    Icon(imageVector = Icons.Default.VpnKey, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = "Deja Vehículo")
                }
            }
        }

        // DIÁLOGO DE SELECCIÓN: QR O MANUAL
        if (showActionDialog) {
            AlertDialog(
                onDismissRequest = { showActionDialog = false },
                title = { Text("¿Cómo desea registrar?") },
                text = { Text("Seleccione el método para capturar la matrícula del vehículo.") },
                confirmButton = {
                    Button(
                        onClick = {
                            showActionDialog = false
                            // Lógica de Escaneo QR
                            val options = GmsBarcodeScannerOptions.Builder()
                                .setBarcodeFormats(Barcode.FORMAT_QR_CODE)
                                .build()
                            val scanner = GmsBarcodeScanning.getClient(this@MainActivity, options)
                            scanner.startScan()
                                .addOnSuccessListener { barcode ->
                                    val rawValue: String? = barcode.rawValue
                                    if (rawValue != null) {
                                        if (rawValue.length == 7) {
                                            scannedPlate = rawValue
                                            lifecycleScope.launch { saveLastPlate(rawValue) }
                                            enviarDatosASheets(rawValue, pilotName, pendingAction)
                                        } else {
                                            Toast.makeText(this@MainActivity, "Operación Incorrecta", Toast.LENGTH_LONG).show()
                                        }
                                    }
                                }
                                .addOnFailureListener { e ->
                                    Toast.makeText(this@MainActivity, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                                }
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(Icons.Default.QrCodeScanner, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Escanear QR")
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = {
                            showActionDialog = false
                            showManualDialog = true
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(Icons.Default.Edit, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Introducir Manualmente")
                    }
                }
            )
        }

        // DIÁLOGO DE ENTRADA MANUAL
        if (showManualDialog) {
            var manualPlate by remember { mutableStateOf("") }
            AlertDialog(
                onDismissRequest = { showManualDialog = false },
                title = { Text("Registro Manual") },
                text = {
                    Column {
                        Text("Escriba la matrícula (7 caracteres):")
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(
                            value = manualPlate.uppercase(),
                            onValueChange = { 
                                if (it.length <= 7) manualPlate = it.uppercase() 
                            },
                            label = { Text("Matrícula") },
                            placeholder = { Text("Ej: 1234ABC") },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true
                        )
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            if (manualPlate.length == 7) {
                                scannedPlate = manualPlate
                                lifecycleScope.launch { saveLastPlate(manualPlate) }
                                enviarDatosASheets(manualPlate, pilotName, pendingAction)
                                showManualDialog = false
                            }
                        },
                        enabled = manualPlate.length == 7
                    ) {
                        Text("Registrar")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showManualDialog = false }) {
                        Text("Cancelar")
                    }
                }
            )
        }
    }

    private fun enviarDatosASheets(matricula: String, nombre: String, accion: String) {
        if (URL_SCRIPT.isBlank() || URL_SCRIPT.contains("TU_URL")) {
            Toast.makeText(this, "Escaneo OK: $matricula (URL no configurada)", Toast.LENGTH_LONG).show()
            return
        }

        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val client = OkHttpClient()
                val json = JSONObject()
                // Mapeo exacto según espera tu Script de Google Apps Script
                json.put("email", nombre)       // El script usa datos.email para el nombre
                json.put("matricula", matricula) // El script usa datos.matricula
                json.put("accion", accion)      // El script usa datos.accion

                val mediaType = "application/json; charset=utf-8".toMediaType()
                val body = json.toString().toRequestBody(mediaType)

                val request = Request.Builder()
                    .url(URL_SCRIPT)
                    .post(body)
                    .build()

                val response = client.newCall(request).execute()
                val responseBody = response.body?.string() ?: "Sin respuesta"

                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        Toast.makeText(this@MainActivity, "Registro completado", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this@MainActivity, "Error Servidor (${response.code}): $responseBody", Toast.LENGTH_LONG).show()
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@MainActivity, "Error al enviar: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    @Preview(showBackground = true)
    @Composable
    fun LicenceDialogPreview() {
        DORALFLEETCONTROLTheme {
            AlertDialog(
                onDismissRequest = { },
                title = { Text("Licencia de Uso") },
                text = {
                    val scroll = rememberScrollState()
                    Column(
                        modifier = Modifier
                            .height(400.dp)
                            .verticalScroll(scroll)
                    ) {
                        Text(
                            text = "© 2026 Rayco Torres Gil. Todos los derechos reservados.",
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        
                        Text("1. Propiedad Intelectual y Autoría", fontWeight = FontWeight.Bold)
                        Text(
                            "El código fuente, el diseño de la interfaz, la estructura lógica y la idea original de esta aplicación son propiedad intelectual exclusiva de su creador, Rayco Torres Gil. Esta aplicación ha sido desarrollada de manera independiente y por iniciativa propia, y no constituye una obra por encargo corporativo.",
                            fontSize = 13.sp
                        )
                        Spacer(modifier = Modifier.height(12.dp))

                        Text("2. Ámbito de Uso Permitido", fontWeight = FontWeight.Bold)
                        Text(
                            "El autor concede a DORAL PARTS S.L. y a su personal operativo (repartidores y gestores de flota) una licencia de uso temporal, gratuita, intransferible y no exclusiva. Este uso está estrictamente limitado a la gestión interna de flotas y al registro de entrada y salida de vehículos dentro de la actividad de la empresa.",
                            fontSize = 13.sp
                        )
                        Spacer(modifier = Modifier.height(12.dp))

                        Text("3. Restricciones Estrictas", fontWeight = FontWeight.Bold)
                        Text(
                            "Para proteger la propiedad intelectual del autor, queda terminantemente prohibido:\n\n" +
                            "• Plagiar, copiar o reproducir total o parcialmente el diseño, las funciones o el código de esta aplicación.\n" +
                            "• Realizar ingeniería inversa, descompilar o intentar extraer el código fuente de la misma.\n" +
                            "• Distribuir, vender, comercializar o registrar esta aplicación (así como cualquier obra derivada basada en esta idea o estructura) a nombre de DORAL PARTS S.L. o de cualquier tercero.",
                            fontSize = 13.sp
                        )
                        Spacer(modifier = Modifier.height(12.dp))

                        Text("4. Uso de Activos Corporativos", fontWeight = FontWeight.Bold)
                        Text(
                            "Los logotipos, marcas comerciales e imágenes corporativas pertenecientes a DORAL PARTS S.L. (incluyendo, pero no limitándose a, iconos de vehículos y enlaces a redes sociales) integrados en esta aplicación son propiedad exclusiva de la empresa. Su inclusión tiene una finalidad puramente estética y de usabilidad para el entorno interno. El uso de estos elementos corporativos no otorga al autor ningún derecho sobre los mismos; de igual manera, la presencia de estos activos en la interfaz no transfiere a la empresa la titularidad, ni total ni parcial, sobre el software, su código o su propiedad intelectual, que siguen perteneciendo exclusivamente a Rayco Torres Gil.",
                            fontSize = 13.sp
                        )
                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            "El acceso y uso de esta aplicación por parte del personal de DORAL PARTS S.L. implica la aceptación incondicional de estos términos. El autor, Rayco Torres Gil, se reserva el derecho de revocar esta licencia gratuita de uso en cualquier momento si se detecta un incumplimiento de estas condiciones.",
                            fontSize = 13.sp,
                            fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
                        )
                    }
                },
                confirmButton = {
                    TextButton(onClick = { }) {
                        Text("He leído y acepto")
                    }
                }
            )
        }
    }

    @Preview(showBackground = true)
    @Composable
    fun IdentificationPreview() {
        DORALFLEETCONTROLTheme {
            TiledBackground {
                IdentificationScreen {}
            }
        }
    }

    @Preview(showBackground = true)
    @Composable
    fun MainScreenPreview() {
        DORALFLEETCONTROLTheme {
            TiledBackground {
                MainScreen("Juan Pérez", "1234ABC")
            }
        }
    }
}
