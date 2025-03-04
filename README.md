# AREP_Basic-Framework

## Taller 3 - Servidor Web e IoC Framework en Java

Este proyecto es un prototipo mínimo de un servidor web construido en Java, que demuestra las capacidades reflexivas (reflection) de Java para cargar un bean (POJO) y derivar una aplicación web a partir de él. Se apoya en Apache HttpComponents para manejar las solicitudes HTTP y utiliza anotaciones personalizadas para configurar los endpoints.

## Descripción

El servidor web cumple con los siguientes requerimientos:

- **Entrega de recursos estáticos:** Permite servir páginas HTML, imágenes (por ejemplo, PNG) y otros archivos desde el directorio `src/main/resources/static`.
- **Framework IoC básico:** Utiliza reflection para detectar y cargar clases anotadas con `@RestController`. De esta forma, se puede definir el comportamiento de la aplicación web a partir de POJOs.
- **Soporte de Endpoints:**
  - **GET /greet:** Responde con un saludo. Recibe un parámetro `name` (por defecto "Guest") y retorna un mensaje de bienvenida.
  - **POST /post:** Responde de forma similar al endpoint GET, pero utilizando el método POST.

Este prototipo es capaz de atender múltiples solicitudes de forma no concurrente y fue desarrollado para facilitar la comprensión de conceptos básicos de IoC y reflection en Java.

## Estructura del Proyecto

La estructura de directorios es la siguiente:

```
C:\USERS\JUANP\DOCUMENTS\DESKTOP\UNIVERSIDAD\SEMESTRE 8\AREP\TALLER3\SRC
├───main
│   ├───java
│   │   └───arep
│   │       └───server
│   │           └───app
│   │               │   App.java
│   │               │   BasicHttpServer.java
│   │               │   BasicWebFramework.java
│   │               │   GreetController.java
│   │               │
│   │               ├───notations
│   │               │       GetMapping.java
│   │               │       PostMapping.java
│   │               │       RequestParam.java
│   │               │       RestController.java
│   │               │
│   │               └───types
│   │                       HttpMethod.java
│   │
│   └───resources
│       └───static
│               Gardenia.jpg
│               index.html
│               Magnolia.png
│               Passiflora.jpg
│               Plumeria.jpg
│               views.png
│
└───test
```

## Endpoints Disponibles

El servidor ofrece los siguientes endpoints:

### Recursos Estáticos

- **Método:** `GET`
- **Patrón de URL:** `/static/{nombre-archivo}`
- **Archivos disponibles:**
  - `Gardenia.jpg`
  - `index.html`
  - `Magnolia.png`
  - `Passiflora.jpg` 
  - `Plumeria.jpg`
  - `views.png`

### Endpoints API

#### Endpoint de Saludo

- **GET** `/greet`
  - **Parámetros:** `name` (opcional, valor por defecto: "Guest")
  - **Respuesta:** Mensaje de saludo en formato JSON
  - **Ejemplo:** `GET http://localhost:8080/greet?name=Juliana` → `"Hello Juliana"`

- **POST** `/post`
  - **Parámetros:** `name` (opcional, valor por defecto: "Guest")
  - **Respuesta:** Mensaje de saludo en formato JSON
  - **Ejemplo:** `POST http://localhost:8080/post?name=Juanita` → `"Hello Juanita"`


## Guía de Prueba

### 🛠 Paso previo: Compilar y ejecutar el servidor

Antes de realizar pruebas, asegúrate de compilar y ejecutar el servidor con:

```sh
mvn clean install 

java -cp "target/classes;target/dependency/*" arep.server.app.App "arep.server.app.GreetController"
```

### 🌐 Pruebas en Postman

#### 1. Obtener recursos estáticos

- **Método:** `GET`
- **URL:** `http://localhost:8080/static/Gardenia.jpg`
- **Resultado esperado:** Se debe recibir la imagen **Gardenia.jpg** en la respuesta.

#### 2. Prueba de endpoint GET

- **Método:** `GET`
- **URL:** `http://localhost:8080/greet?name=Juliana`
- **Resultado esperado:**
  ```json
  "Hello Juliana"
  ```
- **Explicación:** Este endpoint devuelve un saludo con el nombre enviado como parámetro.

#### 3. Prueba de endpoint POST

- **Método:** `POST`
- **URL:** `http://localhost:8080/post?name=Juanita`
- **Resultado esperado:**
  ```json
  "Hello Juanita"
  ```
- **Explicación:** Similar al anterior, pero usando `POST` en lugar de `GET`.