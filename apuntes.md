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

## CÓDIGO

El método onCreate no se toca a no ser q carlos lo diga (te lo cargas todo)

En res/values/themes.xml se quita .NoActionBar para mostrar el nombre de la aplicación

Hay tmb values-night, es un recurso alternativo y funciona en función de una configuración o otra (en este caso para el modo oscuro), hay otro para diferentes resoluciones (p.ej. pedir que trabaje con interfaz A en una resolución e interfaz B en otra)

en values/activity_main.xml cambiar a LinearLayout (borrar lo otro) y añadir orientation=vertical

para que todo funcione hay que añadir en themes/* la linea de las diapositivas Lab21 o algo asi (diapo 3) item name windowActionbarOverlay

Chequea en el tema 2.1a el grafo de estados y métodos del ciclo de vida de una actividad.

onCreate() -> onStart() -> onResume()/onPause() -> onStop()/onRestart() -> onDestroy()

ejemplio: para lanzar un video hay que meter el codigo de lanzar el video en onResume() 

onPause()/onresume() tienen que ser muy rápidos porque el usuario los va a notar.

onStop() te permite hacer otras cosas que onPause() no te deja.

ahora toca sobrecargar los métodos del ciclo de vida con código

salvar estado (onSaveInstanceState()) no asegura persistencia, pero en esta clase lo vemos. en el tema 3 veremos como guardar persistentemente información.

checkear diapositivas de estado de una actividad para ver como se hace, guardar, recuperar, etc...

Para que un evento gfuncione tenemos que hacer un setOnClickListener

tenemos Log.d(...) para meter cosas en el Logcat y debugear los eventos sobrecargados y ver cuales se llaman

** Se usa LinearLayout porque pone los elementos uno detrás de otro y es más fácil, hay otros que permiten colocarlos donde se quiera

HAY QUE DEFINIR TODOS LOS STRINGS EN RECURSOS, NO SE PUEDE PONER A PELO (para poder internacionalizar después)
dentro de las comillas "" pulsas alt+enter y te abre para añadir a recursos ya

File > Settings > Editor > General > Auto Import > marcar Add unambiguous imports on the fly (añade imports como el de Button automaticamente)

el codigo del boton esta hecho ya

AÑADIR SEGUNDA ACTIVIDAD

click derecho en MainActivity.java > new > Activity > Empty Views Activity, y le metes el nombre 

para que se pueda ejecutar la segunda actividad tiene que aparecer en el manifiesto

un componente que no este declarado en este manifiesto no es ejecutable

cada vez q creamos una clase nueva hay q ponerlo ahí, deberia aparecer la segunda actividad pq lo hace androidstudio autmat.

### Teoría de gestión de eventos (diapos)
Hace falta definir la funcion q se escucha (meter codigo) y Registrar el metodo a elementos de la vista para q la funcion se ejecute

