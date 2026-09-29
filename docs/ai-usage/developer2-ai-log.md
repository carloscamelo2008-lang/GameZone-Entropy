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
- **Fecha:** 27/09/2026 · **Rama:** fix/warranty-circular-dependency
- **Consulta:** revisión del comentario de Carlos sobre Main.java y WarrantyService, y cómo verificar y subir la corrección.
- **Decisión:** revisé el diff antes del commit, subí solo los tres archivos modificados y usé javac para compilar por no tener Maven instalado.
- **Commit:** 46e4795

### Planificación — orden de fases (A3 antes de A4)
- **Fecha:** 27/09/2026
- **Consulta:** si A4 podía empezar antes de que se cerrara A3.
- **Decisión:** confirmé con el enunciado que no, y esperé a que A3 y feature/return-module estuvieran fusionadas antes de crear ramas de fase 4.

### Módulo de promociones — PromotionRepository y PromotionService (PR #24)
- **Fecha:** 27/09/2026 · **Rama:** feature/promotion-service-persistence
- **Consulta:** cómo subir dos clases que tenía sin rastrear en mi copia local y que A3 necesitaba.
- **Decisión:** confirmé que ya tenían contenido, compilé antes de subir y las separé en dos commits atómicos. Corregí, tras la revisión de Carlos, el JavaDoc de una categoría faltante y el manejo de comas en el CSV, con una prueba de guardar y recargar una promoción con coma en el nombre.
- **Commits:** 9c44b1e, 6fc223c, fffab28, 4339f3a

### Cierre del PR #24 y limpieza de ramas
- **Fecha:** 27/09/2026
- **Consulta:** si borrar ramas fusionadas es obligatorio según el enunciado.
- **Decisión:** confirmé que sí (secciones 3, 4, 7 y 12) y borré mis ramas ya fusionadas, dejando las de otros integrantes para que cada uno las borre.

### A4 — restoreStock de accesorios y accesorios en ReturnRepository
- **Fecha:** 27/09/2026 · **Rama:** feature/return-module
- **Consulta:** traje a este chat una sugerencia recibida en otro hilo para revisarla antes de aplicarla.
- **Decisión:** confirmé que el punto sobre A2 estaba desactualizado y no lo apliqué. Sí corregí que ReturnRepository no resolvía accesorios al cargar una devolución.
- **Commits:** 7c9ee04, 1d79c4c

### A6 — Balance mensual con el total final de la venta (PR #29)
- **Fecha:** 27/09/2026 · **Rama:** fix/monthly-balance-report
- **Consulta:** separar el cálculo del balance mensual y corregir que use el total final (con descuento y garantías), según pidió el enunciado.
- **Decisión:** dividí el método en tres, mantuve la firma original de generateMonthlyBalance, y le pedí al Líder Técnico que agregara la parte del menú, que le correspondía según la sección 6.
- **Commit:** 192b7a3

### A7 — Anulación de garantías al devolver una consola (PR aprobado)
- **Fecha:** 27/09/2026 · **Rama:** feature/return-warranty-cancellation
- **Consulta:** cómo implementar la cancelación de garantías al procesar una devolución.
- **Decisión:** revisé que la lógica solo afectara consolas y documenté en el PR dos limitaciones que quedaban fuera del alcance de este ajuste (conexión pendiente en Main y persistencia del reembolso de garantías).
- **Commit:** 0daa30d

### Elaboración de esta bitácora
- **Fecha:** 27/09/2026
- **Consulta:** ayuda para redactar y mantener actualizado este archivo según la sección 11 del enunciado.
- **Decisión:** revisé cada entrada contra el historial real de commits antes de subirla.
