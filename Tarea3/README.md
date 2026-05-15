# Tarea 3 LP, La Amenaza de Sephiroth

**Alumno:** Jaime Muena  
**Rol** 202473599-3

---

## Descripcion

Prototipo de RPG por turnos en consola. Controlas a Cloud, un mercenario que debe
recorrer tres zonas (Sector 7, Gongaga, Nucleo del Planeta), derrotar enemigos,
recolectar Materias y enfrentarse al jefe final Sephiroth.

---

## Para Compilar
*Importante estar en la carpeta correcta del directorio
Esta tarea se probo en vscode, en un WSL: Ubuntu, todo fuer probado en estas condiciones
para compilar los archivos escribir en la terminal "make" y para ejecutar el juego "make run"
se crearan archivos .class en la carpeta bin los cuales se pueden limpiar con "make clean"

## Estructura del proyecto

```
Main.java
Makefile
README.md
Componentes/   -> Elemento, TipoStat, Estadisticas, Materia, Mejora, Vulnerable
Entidades/     -> Enemigo, EnemigoSimulador, EnemigoSalvaje, Sephiroth, Jugador (con Arma anidada)
Mapa/          -> Zona, Sector7, Gongaga, NucleoPlaneta
```

---

