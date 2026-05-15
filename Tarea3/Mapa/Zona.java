package mapa;

import java.util.List;
import entidades.Enemigo;
import entidades.Jugador;

public abstract class Zona {
    public String nombre; 
    protected int nivelRequerido; 
    protected List<Enemigo> enemigosDisponibles; 

    public Zona(String nombre, int nivelRequerido, List<Enemigo> enemigosDisponibles) {
        this.nombre = nombre;
        this.nivelRequerido = nivelRequerido;
        this.enemigosDisponibles = enemigosDisponibles;
    }

    public void accionZona(Jugador cloud) { 
    }

    public boolean validarAcceso(Jugador cloud) { 
        return false;
    }
}