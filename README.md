# Captura Digital Móvil de Prospectos

**THERIONLABS SpA** — Aplicación móvil para la captura digital de prospectos en ferias, eventos y visitas en terreno.

## 📱 Descripción

Aplicación móvil Android (Kotlin + Jetpack Compose) que reemplaza el uso de formularios físicos por un flujo de registro digital directo. Permite la captura estandarizada de información, validación de datos en tiempo real, operación offline y sincronización automática con un backend central.

## 🏗️ Arquitectura

```
┌─────────────────────────────────────────────────────────────────────────┐
│                           App Móvil (Android)                            │
│  ┌──────────┐  ┌──────────────┐  ┌────────────────┐  ┌──────────────┐  │
│  │   UI     │  │  ViewModel   │  │  Repository    │  │  Room (SQLite)│  │
│  │  Compose │◄─┤  (MVVM)      │◄─┤  (Offline-first)│◄─┤  Persistencia │  │
│  │          │  │              │  │                │  │  Local        │  │
│  └──────────┘  └──────────────┘  └───────┬────────┘  └──────────────┘  │
│                                          │                              │
│                                   ┌──────┴──────┐                       │
│                                   │  Retrofit    │                       │
│                                   │  (API REST)  │                       │
│                                   └──────┬──────┘                       │
└──────────────────────────────────────────┼──────────────────────────────┘
                                           │ HTTP
┌──────────────────────────────────────────┼──────────────────────────────┐
│                    Backend (Spring Boot)  │                              │
│                                   ┌──────┴──────┐                       │
│                                   │  Controller  │                       │
│                                   │  (REST API)  │                       │
│                                   └──────┬──────┘                       │
│                                   ┌──────┴──────┐                       │
│                                   │   Service    │                       │
│                                   └──────┬──────┘                       │
│                                   ┌──────┴──────┐                       │
│                                   │  JPA / H2    │                       │
│                                   └─────────────┘                       │
└─────────────────────────────────────────────────────────────────────────┘
```

## 🛠️ Tecnologías

### App Móvil
- **Lenguaje:** Kotlin 1.9.22
- **UI:** Jetpack Compose + Material Design 3
- **Arquitectura:** MVVM (ViewModel + StateFlow)
- **Persistencia Local:** Room (SQLite)
- **API Client:** Retrofit 2.9 + OkHttp
- **Navegación:** Navigation Compose + Bottom Navigation
- **Testing:** JUnit 4 + MockK + Coroutines Test

### Backend
- **Framework:** Spring Boot 3.2.2
- **Lenguaje:** Kotlin + JPA
- **Base de Datos:** H2 (desarrollo), PostgreSQL (producción)
- **Build:** Gradle Kotlin DSL

## 📋 Funcionalidades

### Captador Móvil (Perfil Principal)
| RF | Funcionalidad | Estado |
|----|--------------|--------|
| RF-MOV-01 | Botón REGISTRAR NUEVO PROSPECTO | ✅ |
| RF-MOV-02 | Consulta de Oferta Académica | ✅ |
| RF-MOV-03 | Navegación REGISTRO / ESTADO | ✅ |
| RF-MOV-04 | Campos: Nombre, Correo, Teléfono | ✅ |
| RF-MOV-05 | Validación de correo en tiempo real | ✅ |
| RF-MOV-06 | Guardado local preventivo | ✅ |
| RF-MOV-07.1 | Dropdowns: Nivel, Área, Curso | ✅ |
| RF-MOV-08 | RadioButtons: Presencial / Online | ✅ |
| RF-MOV-09 | Botón CONFIRMAR REGISTRO | ✅ |
| RF-MOV-010 | Banner "REGISTRO GUARDADO EN EL DISPOSITIVO" | ✅ |
| RF-MOV-011 | Persistencia en Room | ✅ |
| RF-MOV-012 | Botón NUEVO REGISTRO | ✅ |
| RF-MOV-013 | Filtro de Pendientes | ✅ |
| RF-MOV-014 | Filtro de Sincronizados | ✅ |
| RF-MOV-015 | Actualización Manual | ✅ |
| RF-MOV-016 | Barra de progreso de sincronización | ✅ |
| RF-MOV-017 | Historial de eventos (Sync Log) | ✅ |
| RF-MOV-018 | Botón FORZAR ENVÍO | ✅ |
| RF-MOV-019 | Banners: EXCELENTE SEÑAL, ERROR DE ENVÍO, DATOS VALIDADOS | ✅ |
| RF-MOV-020 | Switch MODO TERRENO | ✅ |
| RF-MOV-021 | ID Captador y Batería | ✅ |
| RF-MOV-022 | VERIFICAR CONEXIÓN API | ✅ |

### Administrativo (Backend)
| RF | Funcionalidad | Estado |
|----|--------------|--------|
| RF-ADM-01 al 15 | Panel administrativo (dashboard, tablas, exportación) | 🔲 Pendiente |

## 🔌 Endpoints de la API

### Microservicio Propio
| Método | Endpoint | Descripción |
|--------|----------|-------------|
| GET | `/api/health` | Health check |
| POST | `/api/prospectos` | Crear prospecto |
| GET | `/api/prospectos` | Listar prospectos |
| GET | `/api/prospectos/{id}` | Obtener prospecto por ID |
| GET | `/api/catalogos` | Obtener catálogo académico |

### API Externa (pendiente de integrar)
Se recomienda: REST Countries API, Random User Generator, o API de geolocalización.

## 🚀 Cómo ejecutar

### Backend
```bash
cd backend
./gradlew bootRun
```
El servidor arranca en `http://localhost:8080`

### App Móvil
```bash
cd mobile
./gradlew assembleDebug
```
O abrir en Android Studio y compilar. El APK se genera en `app/build/outputs/apk/debug/`.

## 📦 APK Firmado

```bash
cd mobile
./gradlew assembleRelease
```

El APK firmado se genera en `app/build/outputs/apk/release/`.  
La keystore (.jks) debe configurarse en `app/build.gradle.kts`.

## 👥 Integrantes

- [Nombre del estudiante 1]
- [Nombre del estudiante 2]

## 📊 Planificación (Trello)

[Enlace al tablero Trello]

## 🔗 Repositorio

[URL del repositorio GitHub]

---

**THERIONLABS SpA** — Captura Digital Móvil de Prospectos  
DSY1105 · Desarrollo de Aplicaciones Móviles · Duoc UC