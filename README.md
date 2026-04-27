<div align="center">

# 📊 Aprende SQL

**Plataforma móvil educativa gratuita para dominar SQL — desde los fundamentos hasta nivel profesional.**

[![Android](https://img.shields.io/badge/Plataforma-Android-3DDC84?style=flat-square&logo=android&logoColor=white)](https://android.com)
[![Kotlin](https://img.shields.io/badge/Lenguaje-Kotlin-7F52FF?style=flat-square&logo=kotlin&logoColor=white)](https://kotlinlang.org)
[![Jetpack Compose](https://img.shields.io/badge/UI-Jetpack%20Compose-4285F4?style=flat-square&logo=jetpackcompose&logoColor=white)](https://developer.android.com/jetpack/compose)
[![License: MIT](https://img.shields.io/badge/Licencia-MIT-yellow.svg?style=flat-square)](LICENSE)
[![PRs Welcome](https://img.shields.io/badge/PRs-bienvenidos-brightgreen.svg?style=flat-square)](CONTRIBUTING.md)

</div>

---

## Descripción general

**Aprende SQL** es una plataforma educativa móvil creada para que el conocimiento de bases de datos sea accesible para todos. Inspirada en la experiencia de aprendizaje de plataformas líderes por suscripción como Mimo y SoloLearn, este proyecto ofrece una experiencia equivalente y de calidad premium — completamente gratis, sin anuncios y sin barreras de pago.

> *La educación técnica de calidad no debería estar restringida por una suscripción.*

---

## ✨ Características

### 🛤️ Ruta de aprendizaje estructurada
Contenido organizado en tres niveles progresivos — **Principiante**, **Intermedio** y **Avanzado** — que garantizan una progresión coherente y paso a paso a través de los conceptos de SQL.

### 🔒 Sistema de progreso secuencial
Un mecanismo de desbloqueo guiado impide saltarse lecciones, asegurando que el usuario domine los fundamentos antes de enfrentarse a temas complejos. Cada lección debe completarse antes de que la siguiente esté disponible.

### ⌨️ Editor SQL interactivo
Un entorno de práctica completamente integrado dentro de la app. Los usuarios escriben consultas SQL reales, las ejecutan y reciben retroalimentación inmediata — sin necesidad de herramientas externas.

### 📖 Biblioteca de referencia SQL
Un módulo de consulta rápida y completa que cubre:
- **Consultas y filtros** — `SELECT`, `WHERE`, `DISTINCT`
- **Ordenamiento y paginación** — `ORDER BY`, `LIMIT`, `OFFSET`
- **Operaciones avanzadas** — `JOIN`, `GROUP BY`, `HAVING`, subconsultas

### 🎓 Examen de certificación final
Una evaluación integral que se desbloquea únicamente al completar todos los módulos. Diseñada para validar el alcance completo del conocimiento adquirido durante el curso.

### 📈 Perfil y seguimiento de progreso
Estadísticas detalladas que incluyen puntos de experiencia (XP), precisión en ejercicios, racha de días, módulos completados y calificación del examen — dándole al usuario visibilidad clara sobre su avance.

### 🌗 Modo oscuro y modo claro
Soporte completo de temas con preferencia persistente del usuario, optimizado para largas sesiones de estudio en cualquier condición de iluminación.

---

## 🏗️ Stack tecnológico

| Capa | Tecnología |
|---|---|
| Lenguaje | [Kotlin](https://kotlinlang.org/) |
| Framework UI | [Jetpack Compose](https://developer.android.com/jetpack/compose) + Material Design 3 |
| Arquitectura | MVVM (Model-View-ViewModel) |
| Gestión de estado | `StateFlow` + Corrutinas de Kotlin |
| Persistencia de datos | DataStore Preferences + Room Database |
| Navegación | Compose Navigation (type-safe) |

---

## 🚀 Cómo empezar

### Requisitos previos
- Android Studio Hedgehog o posterior
- Android SDK 26+
- Kotlin 1.9+

### Instalación

```bash
# Clonar el repositorio
git clone https://github.com/your-username/aprende-sql.git

# Abrir en Android Studio y sincronizar Gradle
# Ejecutar en emulador o dispositivo físico (Android 8.0+)
```

---

## 📁 Estructura del proyecto

```
app/src/main/java/com/sqlmimo/
├── data/           # Base de datos Room, DAOs y entidades
├── model/          # Datos del curso, modelos de lecciones y módulos
├── ui/
│   ├── components/ # Componentes Compose reutilizables
│   ├── screens/    # Composables de cada pantalla
│   └── theme/      # Esquemas de color y tipografía Material3
└── viewmodel/      # AppViewModel con la lógica de negocio
```

---

## 🤝 Contribuciones

Las contribuciones son bienvenidas. Si deseas agregar nuevas lecciones, corregir un error o mejorar la interfaz, abre un issue o envía un pull request.

1. Haz un fork del repositorio
2. Crea tu rama de trabajo (`git checkout -b feature/nuevo-modulo`)
3. Confirma tus cambios (`git commit -m 'Agrega módulo avanzado de JOINs'`)
4. Sube la rama (`git push origin feature/nuevo-modulo`)
5. Abre un Pull Request

---

## 👨‍💻 Autor

**Daniel Santos Navarro Mendoza**
Ingeniería en Sistemas Computacionales

---

## 🛡️ Licencia

Este proyecto es de código abierto bajo la licencia [MIT](LICENSE).

> *La educación es un derecho, no un privilegio.*

---

<div align="center">

Hecho con ❤️ usando Kotlin & Jetpack Compose

</div>
