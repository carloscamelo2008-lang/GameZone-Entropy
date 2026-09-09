# Bitácora de Uso de IA - Desarrollador 1

## Información del estudiante

- **Nombre:** Jesús Daniel Díaz Álvarez
- **Rol:** Desarrollador 1
- **Equipo:** Entropy
- **Proyecto:** GameZone Unicesar
- **Asignatura:** Programación III

## Herramienta de IA utilizada

- **Herramienta:** Claude
- **Proveedor:** Anthropic

## Propósito del uso de IA

Durante el desarrollo del módulo de productos (modelo, persistencia y
servicio), asignado a mí como Desarrollador 1, utilicé Claude como
herramienta de apoyo para aclarar dudas conceptuales, revisar código
que yo mismo había escrito, interpretar errores del compilador,
entender el flujo de Git/GitHub exigido por el taller, y resolver
dudas de configuración del entorno (IntelliJ, Maven).

La IA se usó principalmente cuando encontraba situaciones que no
comprendía completamente. Usé las explicaciones como apoyo, pero
verifiqué cada una contra el código real del proyecto y el diagrama de
clases acordado por el equipo (`docs/class-diagram.md`), y probé los
cambios después de aplicarlos.

## Registros de uso de IA

### 1. Entendiendo la jerarquía de clases antes de programar

Antes de escribir código, tenía dudas sobre cómo debía estructurarse la
jerarquía `Product` → `VideoGame`/`Console`: qué atributos debían ir en
la clase base, cuáles eran propios de cada subtipo, y por qué la clase
base debía ser abstracta.

Usé Claude para aclarar estos conceptos de encapsulamiento y herencia
antes de tocar el editor. Confirmé cada decisión contra el
`docs/class-diagram.md` real del repositorio antes de dar por válida
cualquier sugerencia.

---

### 2. Escribiendo `Product.java` y corrigiendo mi propio código

Escribí la clase `Product` yo mismo en IntelliJ. Al terminar, compartí
capturas de pantalla y pedí revisión.

Se me señaló que faltaba decidir si `setId()` debía existir o no. En
ese momento comparé directamente el contenido de
`docs/class-diagram.md` y confirmé que el diagrama oficial del equipo
sí incluía `setId()`, así que lo mantuve — no me basé en la primera
sugerencia genérica que se me había dado antes de revisar el archivo
real.

---

### 3. Errores de compilación al escribir `VideoGame.java`

IntelliJ reportó el error `Class 'VideoGame' must either be declared
abstract or implement abstract method 'getDescription()' in
'Product'`.

Pregunté qué significaba este error. Se me explicó que era el
comportamiento esperado del diseño con método abstracto: el compilador
obliga a implementar `getDescription()` en cualquier subclase concreta.
Entendí que no era un error real de mi código, sino una validación
correcta del diseño, y terminé de escribir el método para resolverlo.

También cometí un error de escritura real (`plataform` en lugar de
`platform`), detectado durante la revisión de mi código, que corregí
usando la función de renombrado automático de IntelliJ (`Shift+F6`)
para no dejar inconsistencias en el archivo.

---

### 4. Configuración de Maven sin tener `mvn` instalado

Al intentar ejecutar `mvn compile` desde la terminal de PowerShell,
obtuve el error `El término 'mvn' no se reconoce como nombre de un
cmdlet`.

Pregunté por qué pasaba esto. Se me explicó que Maven no estaba
instalado en el `PATH` del sistema, y se me indicó cómo compilar
directamente desde IntelliJ (`Ctrl+F9` o el panel de Maven integrado)
como alternativa mientras no lo instalara en la terminal. Usé esa
alternativa para compilar y verificar mi código durante todo el
desarrollo.

---

### 5. Orden incorrecto de argumentos en el constructor de `Console`

Al escribir `registerConsole()` en `ProductService`, obtuve el error
`'Console(String, String, double, int, String, String, String)' cannot
be applied to '(String, String, String, String, String, double, int)'`.

Pregunté qué significaba. Se me explicó que el orden de los argumentos
al llamar `new Console(...)` debía coincidir exactamente con el orden
declarado en el constructor real de `Console.java`. Comparé ambos y
corregí el orden de mis argumentos (`price, stock` antes de `brand,
model, generation`, no después).

---

### 6. Error real de lógica: `products.add(products)`

En un borrador de `registerConsole()`, escribí por error
`products.add(products)` en lugar de `products.add(console)` —es
decir, intenté agregar la lista completa dentro de sí misma. Este
error fue detectado en la revisión de mi código, se me explicó por qué
estaba mal, y lo corregí yo mismo.

---

### 7. Entendiendo el flujo de Git Flow para mi rama

Pedí orientación paso a paso para: crear mi rama `feature/product-module`
desde `develop`, hacer commits atómicos siguiendo Conventional Commits,
subir cada commit inmediatamente (`git push`), y abrir un Pull Request
hacia `develop`.

Verifiqué cada paso ejecutándolo yo mismo en PowerShell y en GitHub
Desktop, y confirmé los resultados con capturas de la terminal antes de
continuar al siguiente paso.

---

### 8. Detección de un error real en la rama base de un Pull Request

Mientras el Líder Técnico preparaba el Pull Request de configuración de
Maven, noté (con ayuda de la IA para interpretar la pantalla de GitHub
Desktop) que el selector de "base" decía `main` en lugar de `develop`.

Pregunté si esto era un problema. Se me confirmó que fusionar una rama
`feature/*` directo a `main` viola el flujo de Git Flow exigido por el
taller (página 9), y que debía corregirse antes de crear el Pull
Request. Avisé al Líder Técnico antes de que continuara, y verifiqué
después que el Pull Request final sí tuviera `base: develop`.

---

### 9. Revisión técnica de los Pull Requests de mis compañeros

Antes de aprobar los PR #3 (análisis), #4 (diagramas), #5 (Maven) y
#10 (integración de ventas) del Líder Técnico, pedí ayuda para
estructurar comentarios técnicos de revisión, referenciando las
restricciones específicas del taller (por ejemplo, la dirección
permitida de dependencias entre capas, página 13).

En el caso del PR #10, verifiqué directamente en el código diferido de
GitHub que `SaleService` solo llamara a métodos públicos de mi
`ProductService` (`reduceStock`, `listAllProducts`) sin acceder
directamente a `ProductRepository`, y dejé pendiente mi aprobación
hasta confirmar con el Líder Técnico si `hasSufficientStock()` se
validaba antes de `reduceStock()` en el flujo de venta — lo cual él
confirmó explícitamente antes de que yo aprobara.

---

### 10. Decisión de persistencia: serialización vs. CSV

Antes de implementar `ProductRepository`, pregunté qué diferencias
prácticas había entre usar serialización de Java y formato CSV para
guardar los productos.

Se me presentaron ventajas y desventajas de cada uno (velocidad de
implementación vs. legibilidad del archivo). Tomé la decisión final de
usar serialización, y la apliqué escribiendo yo mismo `saveAll()` y
`loadAll()`, incluyendo el manejo del caso en que el archivo no existe
todavía (primera ejecución).

---

### 11. Agregando JavaDoc tras retroalimentación del Líder Técnico

El Líder Técnico comentó en mi Pull Request que debía agregar
JavaDoc/comentarios a mis clases y métodos públicos. Pregunté por el
formato estándar en inglés (`@param`, `@return`) y lo apliqué en mis
cinco clases (`Product`, `VideoGame`, `Console`, `ProductRepository`,
`ProductService`).

Verifiqué que el proyecto siguiera compilando correctamente después de
agregar la documentación, y subí el cambio como un commit independiente
(`docs: add JavaDoc to product module classes`).

## Conclusión

Claude fue usado a lo largo del desarrollo como herramienta de apoyo
para aprender, entender errores, revisar código que yo mismo escribí,
y seguir correctamente el flujo de Git exigido por el taller. Las
situaciones principales en las que usé IA estuvieron relacionadas con
conceptos de POO (abstracción, polimorfismo), errores del compilador,
configuración de Maven, y el flujo de Git/GitHub del equipo.

Las sugerencias recibidas se compararon siempre contra el código real
del proyecto y el diagrama de clases oficial del equipo, y se probaron
después de aplicarlas (compilación en IntelliJ, ejecución de comandos
Git, verificación en GitHub). Este proceso me ayudó a fortalecer mi
comprensión de Java, programación orientada a objetos, arquitectura en
capas, y el flujo de trabajo colaborativo con Git.
