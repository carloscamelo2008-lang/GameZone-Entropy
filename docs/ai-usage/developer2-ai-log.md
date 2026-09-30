# Bitácora de uso de IA — Desarrollador 2

## Contexto
Al agregar JavaDoc a `Seller.java` (métodos `getEmployeeCode`, `setEmployeeCode`,
`getShift`, `setShift`), los cambios no aparecían en el repositorio pese a
haberlos editado en NetBeans.

## Cómo se usó la IA
Se consultó a Claude (Anthropic) para diagnosticar el problema, compartiendo
capturas de la terminal de PowerShell. La IA sugirió:

1. Buscar en todo el disco si existía más de una copia de `Seller.java`
   (`Get-ChildItem -Recurse -Filter Seller.java`), ya que antes había pasado
   algo similar con `PersonRepository.java` y `PersonService.java`.
2. Confirmar con `Select-String` si el JavaDoc estaba realmente guardado en
   el archivo del repositorio.

## Resultado
La búsqueda reveló una copia duplicada de `Seller.java` en
`C:\Users\andre\Downloads\files`, distinta a la del repositorio. Tras
verificar y corregir el archivo correcto, se confirmaron los 4 bloques de
JavaDoc, se hizo commit (`94e5012`) y push a `feature/person-module`.

## Segundo caso: verificación de comandos antes de ejecutarlos
Antes de correr comandos de PowerShell y Git (como `Get-ChildItem`,
`Copy-Item`, `git add`/`commit`/`push`), se le pidió a la IA que revisara si
estaban bien escritos y si harían lo esperado, para evitar errores o
sobrescribir archivos por accidente. La IA confirmó la sintaxis y el efecto
de cada comando antes de ejecutarlo, y el desarrollador validó los
resultados con las capturas de la terminal en cada paso.

## Supervisión humana
Todos los comandos fueron ejecutados y revisados manualmente por el
desarrollador antes de aplicarlos; la IA no tuvo acceso directo al repositorio.
El Pull Request resultante fue revisado y aprobado por el equipo antes de fusionarse a `develop`.

## Requerimiento 5 — Integración del sistema (21-27 de septiembre)

### A2 — Corrección de dependencia circular en garantías (PR #23)
- **Fecha:** 27/09/2026
- **Herramienta:** Claude (Anthropic)
- **Fase y rama:** Fase 3 · fix/warranty-circular-dependency
- **Objetivo:** Corregir dos puntos señalados por el Líder Técnico en Main.java y WarrantyService.
- **Consulta:** Cómo verificar y subir la corrección al comentario de revisión de Carlos.
- **Respuesta:** Orientación para revisar el diff antes del commit y compilar con javac.
- **Decisión:** Revisé el diff, subí solo los tres archivos modificados y compilé con javac por no tener Maven instalado.
- **Commit relacionado:** 46e4795

### Planificación — orden de fases (A3 antes de A4)
- **Fecha:** 27/09/2026
- **Herramienta:** Claude (Anthropic)
- **Fase y rama:** Fase 4 (planificación) · sin rama
- **Objetivo:** Saber si A4 podía empezar antes de cerrar A3.
- **Consulta:** Si el orden de fases del enunciado obligaba a esperar.
- **Respuesta:** Confirmación de que la fase 4 no inicia hasta fusionar A3 y feature/return-module.
- **Decisión:** Esperé a que ambas estuvieran fusionadas antes de crear ramas de fase 4.
- **Commit relacionado:** No aplica

### Módulo de promociones — PromotionRepository y PromotionService (PR #24)
- **Fecha:** 27/09/2026
- **Herramienta:** Claude (Anthropic)
- **Fase y rama:** Fase 2 · feature/promotion-service-persistence
- **Objetivo:** Subir a develop dos clases sin rastrear que A3 necesitaba.
- **Consulta:** Cómo confirmar su contenido y subirlas correctamente.
- **Respuesta:** Verificación de dependencias, compilación previa y separación en commits atómicos.
- **Decisión:** Compilé antes de subir y separé en dos commits. Tras la revisión de Carlos, corregí el JavaDoc de una categoría faltante y el manejo de comas en el CSV, verificado con una prueba de guardar/recargar.
- **Commit relacionado:** 9c44b1e, 6fc223c, fffab28, 4339f3a

### Cierre del PR #24 y limpieza de ramas
- **Fecha:** 27/09/2026
- **Herramienta:** Claude (Anthropic)
- **Fase y rama:** Fases 2 y 3 · feature/promotion-service-persistence, fix/warranty-circular-dependency
- **Objetivo:** Confirmar si borrar ramas fusionadas es obligatorio.
- **Consulta:** Si el enunciado exige eliminar ramas del remoto tras la fusión.
- **Respuesta:** Confirmación de la exigencia en las secciones 3, 4, 7 y 12.
- **Decisión:** Borré mis ramas ya fusionadas y dejé las de otros integrantes para que cada uno borre las suyas.
- **Commit relacionado:** e9a43ce

### A4 — restoreStock de accesorios y accesorios en ReturnRepository
- **Fecha:** 27/09/2026
- **Herramienta:** Claude (Anthropic)
- **Fase y rama:** Fase 4 · feature/return-module
- **Objetivo:** Completar A4 tras el comentario de revisión de Carlos.
- **Consulta:** Revisión de una sugerencia recibida en otro chat antes de aplicarla.
- **Respuesta:** Confirmación de que restoreStock ya estaba resuelto y que faltaba que ReturnRepository resolviera accesorios al cargar.
- **Decisión:** No apliqué el punto sobre A2 por estar desactualizado. Sí corregí la carga de accesorios en ReturnRepository.
- **Commit relacionado:** 7c9ee04, 1d79c4c

### A6 — Balance mensual con el total final de la venta (PR #29)
- **Fecha:** 27/09/2026
- **Herramienta:** Claude (Anthropic)
- **Fase y rama:** Fase 4 · fix/monthly-balance-report
- **Objetivo:** Separar el cálculo del balance mensual y usar el total final de la venta.
- **Consulta:** Cómo dividir generateMonthlyBalance conforme al enunciado.
- **Respuesta:** Propuesta de dividir el método en tres, usando calculateFinalTotal en vez de calculateTotal.
- **Decisión:** Apliqué la división de métodos y le pedí al Líder Técnico que agregara la parte del menú, que le correspondía según la sección 6.
- **Commit relacionado:** 192b7a3

### A7 — Anulación de garantías al devolver una consola
- **Fecha:** 27/09/2026
- **Herramienta:** Claude (Anthropic)
- **Fase y rama:** Fase 4 · feature/return-warranty-cancellation
- **Objetivo:** Implementar la cancelación de garantías al procesar una devolución.
- **Consulta:** Cómo estructurar el cambio en WarrantyService, ReturnService y Return.
- **Respuesta:** Propuesta de un método en WarrantyService que retorna el monto reembolsable, invocado por cada consola devuelta.
- **Decisión:** Apliqué el cambio y documenté en el PR dos limitaciones fuera de alcance (conexión pendiente en Main y persistencia del reembolso).
- **Commit relacionado:** 0daa30d

### Elaboración de esta bitácora
- **Fecha:** 27/09/2026
- **Herramienta:** Claude (Anthropic)
- **Fase y rama:** Documentación · docs/developer2-ai-log
- **Objetivo:** Redactar y mantener este archivo según la sección 11 del enunciado.
- **Consulta:** Ayuda para estructurar las entradas con los campos requeridos.
- **Respuesta:** Formato de entrada con los ocho campos de la tabla de la sección 11.
- **Decisión:** Revisé cada entrada contra el historial real de commits antes de subirla.
- **Commit relacionado:** 4568c3d