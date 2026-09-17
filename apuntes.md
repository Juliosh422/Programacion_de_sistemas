# TEMA 1
## Android tal y pascual

- Las interfaces (GUI) *son reactivas* -> El usuario decide en qué orden se interactúa con los elementos de la interfaz
	- Programación basada en eventos

COMPONENTES FUNDAMENTALES DE ANDROID:

1. Actividades (Fragmentos) - Asociado a GUIs de las apps, (Fragmentos solos no van, necesitan ir dentro de una actividad)
	UNA SOLA ACTIVIDAD POR PANTALLA

2. Servicios - Trabajan en segundo plano, no tienen interfaz grafica

3. Proveedores de contenidos - Intercambiar información entre apps (están en un sandbox y sin este componente no pueden)

4. Receptores de emisiones - reaccionan a eventos

## CICLOS DE VIDA DE UNA ACTIVIDAD
Eventos declarados por el SO a los que te suscribes y que te permite meter código (fuera de los eventos no se puede) 
