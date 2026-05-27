# FEAT: Añadir canciones favoritas

## 1. Enriquecimiento del Modelo de Dominio
Se ha introducido la lista de canciones favoritas directamente dentro de la clase `User`, junto con el método de comportamiento `addFavouriteSong`.

* **Encapsulamiento:** La lógica de negocio pura reside exclusivamente en el Dominio. Esto mantiene al dominio independiente de frameworks externos.

## 2. Inversión de Dependencias y uso de puertos
Para el requisito de notificación, se ha definido la interfaz `EmailNotificationPort` en la capa de Aplicación.

Esta decisión es el núcleo de la Arquitectura Hexagonal. El caso de uso (`AddFavouriteSongService`) sabe que debe enviar una notificación bajo una regla concreta, pero no debe importarle cómo se envía.
Al definir un puerto de salida, la capa de Aplicación obliga a la infraestructura exterior a adaptarse a ellas, respetando el Principio de Inversión de Dependencias (la "D" de SOLID).

## 3. Orquestación en la capa de Aplicación
Los servicios de la capa de aplicación coordinan la ejecución, delegando las reglas de negocio al dominio.

El flujo ejecutado es:
1. Recuperación del `User` mediante `UserRepositoryPort`
2. Mutación del estado llamando a `addFavouriteSong(...)`
3. Persistencia del estado mediante el adaptador de repositorio
4. Evaluación del límite de favoritos y llamada al puerto de notificación si es necesario

## 4. Aislamiento tecnológico e integración JPA
* Se ha implementado `UserRepositoryAdapter`. Para no contaminar el dominio, se utilizan `UserEntity` y `UserEntityMapper`.
* El adaptador `EmailNotificationAdapter` implementa el puerto en la capa de infraestructura. La decisión de separarlo en esta capa externa demuestra el valor real de la arquitectura: en el día de mañana podemos cambiar este archivo por una integración real con un servicio de correos sin tener que modificar ni una sola línea de código en la capa de Aplicación o Dominio.

## 5. Orquestación en `MusicApplication` (Main)
Se ha configurado la clase `MusicApplication` como punto de entrada que ensambla el hexágono mediante `CommandLineRunner`.

* El main utiliza `SongRepository` para inyectar canciones en la base de datos al arrancar, asegurando un entorno de pruebas coherente
* Instancia un usuario mediante el `@SuperBuilder` de Lombok, añade canciones reales obtenidas del catálogo y verifica la activación del puerto de notificación cuando se alcanza el límite
* Esta clase actúa como un test de integración, validando que Spring Boot, JPA, el Dominio y los Puertos están correctamente cableados :)