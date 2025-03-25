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
 ├── 📁 model           (Clases de modelo y datos)
 │   ├── Sistema.java
 │   ├── Usuario.java
 │   ├── Jugador.java
 │   ├── Operador.java
 │   ├── Personaje.java
 │   ├── Desafio.java
 │   ├── Combate.java
 │   ├── Notificacion.java
 │   ├── Ranking.java
 │
 ├── 📁 storage         (Gestión de almacenamiento)
 │   ├── I_Storage.java
 │   ├── XMLStorage.java
 │   ├── FileManager.java
 │
 ├── 📁 auth            (Autenticación y autorización)
 │   ├── Authorization.java
 │   ├── PasarelaAuthorization.java
 │
 ├── 📁 notifications   (Sistema de notificaciones)
 │   ├── Notification.java
 │   ├── NotificationDecorator.java
 │   ├── NotificationInterna.java
 │
 ├── 📁 ui              (Interfaz de usuario)
 │   ├── Interfaz.java
 │
 ├── 📁 main           (Punto de entrada)
 │   ├── Main.java
```

## 📌 Diagrama de Clases
_Adjunta aquí una imagen del diagrama de clases en formato PNG o SVG_

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
   git clone https://github.com/tu-repositorio.git
   ```
2. Importar el proyecto en un IDE como **IntelliJ IDEA** o **Eclipse**.
3. Ejecutar la clase `Main.java` para iniciar el sistema.

## 📌 Contribuciones
Las contribuciones son bienvenidas. Puedes realizar un fork del repositorio, realizar cambios y enviar un pull request.

## 📜 Licencia
Este proyecto está bajo una licencia **restrictiva**. No se permite la modificación, redistribución, creación de forks o uso comercial del código. Solo se autoriza su uso para ejecución y consulta.

---
### ✨ ¡Gracias por usar este sistema de gestión de juego! ✨

