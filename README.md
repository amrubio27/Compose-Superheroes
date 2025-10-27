# Compose Superheroes

Este proyecto es una aplicación de ejemplo que muestra un listado de superhéroes y permite ver el detalle de cada uno. La aplicación está desarrollada 100% en Kotlin y sigue las buenas prácticas recomendadas por Google para el desarrollo de aplicaciones Android modernas.

## ✨ Características

- **100% Kotlin**: Código moderno, conciso y seguro.
- **Jetpack Compose**: UI declarativa para un desarrollo más rápido y sencillo.
- **Arquitectura MVVM**: Separación de la lógica de negocio de la interfaz de usuario.
- **Clean Architecture**: Código modular, escalable y mantenible.
- **Corrutinas y Flow**: Programación asíncrona para una experiencia de usuario fluida.
- **Koin**: Inyección de dependencias para un código más desacoplado y testeable.
- **Retrofit y Ktor**: Clientes HTTP para consumir servicios web.
- **Room**: Base de datos local para persistir datos.
- **DataStore**: Almacenamiento de datos clave-valor de forma asíncrona.
- **Navigation Component**: Navegación entre pantallas de forma segura y consistente.
- **Material Design 3**: Componentes de UI modernos y personalizables.

## 🚀 Cómo empezar

### Prerrequisitos

- Android Studio Iguana | 2023.2.1 o superior
- JDK 17

### Instalación

1.  Clona el repositorio:
    ```bash
    git clone https://github.com/amrubio27/compose-superheroes.git
    ```
2.  Abre el proyecto en Android Studio.
3.  Ejecuta la aplicación en un emulador o dispositivo físico.

## 🏗️ Arquitectura

El proyecto sigue los principios de **Clean Architecture**, dividiendo el código en tres capas principales:

- **Capa de Presentación**: Contiene la UI (vistas y componentes de Jetpack Compose) y los ViewModels.
- **Capa de Dominio**: Contiene la lógica de negocio de la aplicación (casos de uso y modelos de dominio).
- **Capa de Datos**: Contiene los repositorios y los orígenes de datos (remotos y locales).

## Modularización

El proyecto está modularizado por funcionalidades (`features`), lo que permite un desarrollo más ágil y un mejor mantenimiento del código. Cada `feature` tiene sus propias capas de presentación, dominio y datos.

```
compose-superheroes/
├── app/
│   ├── build.gradle.kts
│   └── src/
│       └── main/
│           └── java/
│               └── com/amrubio27/compose_superheroes/
│                   ├── app/
│                   ├── di/
│                   ├── features/
│                   │   ├── list/
│                   │   │   ├── data/
│                   │   │   ├── di/
│                   │   │   ├── domain/
│                   │   │   └── presentation/
│                   │   ├── detail/
│                   │   └── featureb/
│                   └── ui/
└── build.gradle.kts
```

## 🛠️ Tecnologías utilizadas

- **Lenguaje**: [Kotlin](https://kotlinlang.org/)
- **UI**: [Jetpack Compose](https://developer.android.com/jetpack/compose)
- **Asincronía**: [Corrutinas](https://kotlinlang.org/docs/coroutines-overview.html) y [Flow](https://kotlinlang.org/docs/flow.html)
- **Inyección de dependencias**: [Koin](https://insert-koin.io/)
- **Networking**: [Retrofit](https://square.github.io/retrofit/) y [Ktor](https://ktor.io/)
- **Base de datos**: [Room](https://developer.android.com/training/data-storage/room)
- **Almacenamiento clave-valor**: [DataStore](https://developer.android.com/topic/libraries/architecture/datastore)
- **Navegación**: [Navigation Component](https://developer.android.com/guide/navigation/navigation-getting-started)
- **Serialización**: [Kotlinx Serialization](https://github.com/Kotlin/kotlinx.serialization)
- **Imágenes**: [Coil](https://coil-kt.github.io/coil/)

## 🤝 Contribuciones

Las contribuciones son bienvenidas. Si quieres mejorar el proyecto, por favor, abre un _pull request_ con tus cambios.

## 📄 Licencia

Este proyecto está bajo la licencia MIT. Consulta el archivo `LICENSE` para más detalles.
