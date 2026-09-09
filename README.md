# GameZone-Entropy

Sistema de administración para GameZone, desarrollado en Java mediante programación orientada a objetos y una arquitectura organizada por capas.

## Equipo

| **Integrante**                | **Rol**          |
| ----------------------------- | ---------------- |
| Carlos Eduardo Camelo Montaño | Líder Técnico    |
| Jesús Daniel Díaz Álvarez     | Desarrollador 1  |
| Daniel Josué Arrieta Fontalvo | Desarrollador 2  |

## Descripción

GameZone es un sistema de administración para una tienda de videojuegos. Permite gestionar productos, clientes, vendedores e información relacionada con las ventas.

El sistema permite registrar videojuegos y consolas, consultar el inventario, registrar clientes, consultar clientes y vendedores, registrar ventas y consultar diferentes historiales de ventas.

La aplicación también administra el inventario, validando la disponibilidad de los productos antes de registrar una venta y reduciendo automáticamente el stock correspondiente.

## Tecnologías

- Java 17
- Maven
- IntelliJ IDEA
- Git
- GitHub
- Mermaid

## Arquitectura

El sistema está organizado en cuatro capas:

1. **Model**: contiene las clases que representan las entidades y conceptos principales del dominio.
2. **Persistence**: contiene las clases responsables de guardar y recuperar información mediante archivos.
3. **Service**: contiene la lógica y las reglas de negocio del sistema.
4. **UI**: contiene la interfaz de usuario mediante consola.

La clase `Main` se encuentra en el paquete raíz `com.gamezone` y se encarga de inicializar los repositorios, servicios e interfaz de usuario.

Las dependencias entre capas siguen el siguiente flujo:

```text
UI → Service → Persistence
       ↓
      Model
```

La interfaz de usuario utiliza los servicios para realizar las operaciones del sistema. Los servicios contienen las reglas de negocio y utilizan las clases de persistencia para guardar y recuperar información. Las clases del dominio pertenecen a la capa `Model`.

## Estructura del proyecto

```text
src/main/java/com/gamezone/
├── model/
├── persistence/
├── service/
├── ui/
└── Main.java
```

## Funcionalidades

### Gestión de productos

1. Registrar un nuevo videojuego.
2. Registrar una nueva consola.
3. Listar todos los productos disponibles en el inventario.

### Gestión de personas

4. Registrar un nuevo cliente.
5. Listar todos los clientes registrados.
6. Listar todos los vendedores registrados.

### Gestión de ventas

7. Registrar una nueva venta seleccionando cliente, vendedor y uno o más productos.
8. Consultar el historial completo de ventas.
9. Consultar el historial de compras de un cliente específico.
10. Consultar el historial de ventas atendidas por un vendedor específico.

## Inventario y ventas

Antes de registrar una venta, el sistema verifica:

- que el cliente exista;
- que el vendedor exista;
- que exista al menos un producto en la venta;
- que los productos seleccionados existan;
- que exista stock suficiente para las cantidades solicitadas.

Cuando todas las validaciones son correctas, el sistema registra la venta, calcula su total y reduce el stock correspondiente.

Si un mismo producto se incluye varias veces en una venta, el sistema considera la cantidad total solicitada al realizar la validación del stock.

## Persistencia

La aplicación utiliza archivos para conservar la información entre ejecuciones.

Los datos se cargan automáticamente al iniciar el sistema y los cambios realizados durante la ejecución se guardan mediante las clases de persistencia.

El archivo `data/sellers.csv` contiene los vendedores precargados necesarios para la primera ejecución.

Los archivos de datos generados durante la ejecución, como los correspondientes a clientes, productos y ventas, son gestionados por la aplicación y se encuentran excluidos del control de versiones mediante `.gitignore`.

## Documentación

El proyecto incluye documentación relacionada con el análisis, la arquitectura y el diseño:

- `docs/analysis.md`
- `docs/hierarchy-diagram.md`
- `docs/class-diagram.md`
- `docs/layers-diagram.md`

Las bitácoras individuales de uso de inteligencia artificial se encuentran en:

```text
docs/ai-usage/
```

## Ejecución

El proyecto utiliza Maven y Java 17.

Para compilar el proyecto se puede utilizar Maven mediante:

```bash
mvn clean compile
```

La aplicación se inicia ejecutando la clase:

```text
com.gamezone.Main
```

## Control de versiones

El proyecto utiliza un flujo basado en Git Flow simplificado:

```text
main
  ↑
develop
  ↑
feature/*
```

Las nuevas funcionalidades y cambios se desarrollan en ramas `feature/*` y se integran a `develop` mediante Pull Requests revisados por otro integrante del equipo.

`develop` es la rama de integración y `main` representa la versión estable del proyecto.
