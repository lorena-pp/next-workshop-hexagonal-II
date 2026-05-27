# FEAT: añadir canciones favoritas

### (más tarde me di cuenta de que tenía que implementarlo en \music-hexagonal, no en \next-workshop-hexagonal-II (aquí). Encontrarás una explicación casi idéntica allí jeje)

## 1. Enriquecimiento del Modelo de Dominio
Se ha introducido la lista de canciones favoritas (`favouriteSongs`) directamente dentro de la clase `User`, junto con el método de comportamiento para gestionarlas (`addFavouriteSong`).

Desde una perspectiva académica, la lógica de negocio pura (cómo el estado del usuario muta al añadir un favorito o evitar duplicados) pertenece exclusivamente al Dominio. Al encapsular este comportamiento en la entidad, evitamos que la lógica de negocio se "filtre" hacia los servicios de aplicación. El dominio se mantiene agnóstico a cualquier tecnología de persistencia o framework externo.

## 2. Inversión de Dependencias y uso de puertos
Para cumplir con el requisito de notificar al usuario al llegar al límite de canciones, se ha creado una interfaz en la capa de Aplicación: `EmailNotificationPort`.

Esta decisión es el núcleo de la Arquitectura Hexagonal. El caso de uso (`AddFavouriteSongService`) sabe que debe enviar una notificación bajo una regla concreta, pero no debe importarle cómo se envía.
Al definir un puerto de salida, la capa de Aplicación obliga a la infraestructura exterior a adaptarse a ellas, respetando el Principio de Inversión de Dependencias (la "D" de SOLID).

## 3. Orquestación en la capa de Aplicación
Se ha creado un nuevo caso de uso: `AddFavouriteSongService`. Su responsabilidad es puramente de orquestación.

Un servicio de aplicación no debe contener reglas de negocio, sino coordinarlas.

El flujo consiste en:

1. Pide al puerto de base de datos (`UserInMemoryRepository`) que recupere el usuario
2. Delega en el objeto `User` (del Dominio) la acción de añadir la canción
3. Pide al puerto de base de datos que guarde el nuevo estado
4. Evalúa si se ha cumplido el límite de canciones y, de ser así, acciona el puerto de notificación

## 4. Aislamiento tecnológico en la Infraestructura
Se ha implementado el adaptador `EmailNotification` en la capa de Infraestructura, el cual implementa el `EmailNotificationPort`.

Este adaptador simplemente simula el envío mediante una impresión en consola.

La decisión de separarlo en esta capa externa demuestra el valor real de la arquitectura: en el día de mañana podemos cambiar este archivo por una integración real con un servicio de correos sin tener que modificar ni una sola línea de código en la capa de Aplicación o Dominio.