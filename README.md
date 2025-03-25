# Proyecto: Sistema de Gestión de Juego

## 📌 Descripción del Proyecto
Este proyecto es un sistema de gestión de un juego basado en una arquitectura modular y orientada a objetos en **Java**. Implementa múltiples patrones de diseño para garantizar una estructura flexible, mantenible y escalable. El sistema permite la gestión de jugadores, combates, desafíos, notificaciones y almacenamiento de datos de manera eficiente.

**Asignatura:** Metodología de la Programación - URJC (2025)

**Autores:**
- Jordi Guix Betancor
- Víctor Omar Llantoy Núñez del Arco
- Adam El Kassmi Serroukh
- Elena Ceinos Abeijon
- Santiago Sánchez Merino Pérez

## 📂 Estructura del Proyecto
```
📦 com.proyecto
 ├── 📁 auth            (Autenticación y autorización)
 │   ├── Authorization.java
 │   ├── PasarelaAuthorization.java
 │
 ├── 📁 main           (Punto de entrada)
 │   ├── Main.java
 │
 ├── 📁 model           (Clases de modelo y datos)
 │   ├── 📁 desafio
 │      ├── Combate.java
 │      ├── Desafio.java
 │      ├── Rondas.java
 │   ├── 📁 personaje
 │      ├── 📁 estado
 │         ├── EstadoBestia.java
 │         ├── EstadoHumano.java
 │         ├── I_EstadoLicantropo.java
 │      ├── 📁 habilidad
 │         ├── Arma.java
 │         ├── Armadura.java
 │         ├── Debilidad.java
 │         ├── Fortaleza.java
 │         ├── I_Habilidad.java
 │      ├── 📁 tipo
 │         ├── Cazador.java
 │         ├── Licantropo.java
 │         ├── Vampiro.java
 │      ├── Builder.java
 │      ├── Esbirro.java
 │      ├── I_Personaje.java
 │      ├── Personaje.java
 │   ├── 📁 usuario
 │      ├── I_Usuario.java
 │      ├── Jugador.java
 │      ├── Operador.java
 │      ├── Usuario.java
 │   ├── Ranking.java
 │   ├── Sistema.java
 │
 ├── 📁 notifications   (Sistema de notificaciones)
 │   ├── I_Notification.java
 │   ├── NotificationDecorator.java
 │   ├── NotificationInterna.java
 │
 ├── 📁 storage         (Gestión de almacenamiento)
 │   ├── FileManager.java
 │   ├── I_Storage.java
 │   ├── XMLStorage.java
 │
 ├── 📁 ui              (Interfaz de usuario)
 │   ├── A_Interfaz.java
 │   ├── I_Interfaz.java
 │   ├── InterfazCLI.java
📄 .gitignore (Carpetas o archivos que se ignoran)
🖌️ diagramas.drawio (Todos los diagramas del proyecto)
ℹ️ README.md (Información básica del proyecto)
```

## 📌 Diagrama de Clases
En el archivo `diagramas.drawio` encontrará todos los diagramas que se ceraron para la realizacion de este proyecto, en los que se encuentran: Diagramas de clase, diagramas de estado, diagramas de secuencia, diagramas de actividad, diagramas de caso de uso, y diagramas de flujo

## 🛠 Patrones de Diseño Utilizados

### 1️⃣ **Singleton** 
**Ubicación:** 
- `Sistema.java`
- `FileManager.java`

**Descripción:** 
Se usa para garantizar que solo exista una única instancia de estas clases en el sistema, asegurando un control centralizado y evitando duplicación de datos en memoria.

### 2️⃣ **Fachada (Facade)** 
**Ubicación:** 
- `Sistema.java`

**Descripción:** 
Centraliza la gestión del juego en una única clase (`Sistema`), simplificando la interacción de otras partes del sistema con funcionalidades clave como autenticación, combates y almacenamiento de datos.

### 3️⃣ **Adapter** 
**Ubicación:** 
- `I_Storage.java` (Interfaz de almacenamiento)
- `XMLStorage.java` (Implementación específica)
- `FileManager.java`

**Descripción:** 
Permite que el sistema pueda cambiar fácilmente el método de almacenamiento de datos sin modificar la lógica central del programa.

### 4️⃣ **Mediador (Mediator)** 
**Ubicación:** 
- `PasarelaAuthorization.java`
- `Authorization.java`

**Descripción:** 
Evita la comunicación directa entre la interfaz de usuario y la lógica de autenticación, delegando esta responsabilidad a `PasarelaAuthorization` para mejorar la modularidad y reducir acoplamiento.

### 5️⃣ **Decorator** 
**Ubicación:** 
- `Notification.java` (Interfaz base)
- `NotificationDecorator.java` (Clase decoradora)
- `NotificationInterna.java` (Decorador concreto)

**Descripción:** 
Permite extender las funcionalidades del sistema de notificaciones sin modificar el código base, agregando dinámicamente nuevas características.

## 🚀 Instalación y Ejecución
1. Clonar este repositorio:
   ```bash
   git clone https://github.com/Jordigb44/Grupo3MP.git
   ```
2. Importar el proyecto en un IDE como **IntelliJ IDEA** o **Eclipse**.
3. Ejecutar la clase `Main.java` para iniciar el sistema.

## 📌 Contribuciones
Las contribuciones son bienvenidas. Puedes realizar un fork del repositorio, realizar cambios y enviar un pull request.

## 📜 Licencia
Este proyecto está bajo una licencia **restrictiva**. No se permite la modificación, redistribución, creación de forks o uso comercial del código. Solo se autoriza su uso para ejecución y consulta.

---
### ✨ ¡Gracias por usar este sistema de gestión de juego! ✨

