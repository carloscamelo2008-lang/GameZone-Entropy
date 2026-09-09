# Bitácora de uso de IA — Desarrollador 2 (módulo de personas)

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

