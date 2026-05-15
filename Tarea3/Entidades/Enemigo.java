package entidades;

import componentes.Estadisticas;
import java.util.Random;

public abstract class Enemigo {
    public    String      nombre;
    protected int         xpRecompensa;
    protected int         chatarraRecompensa;
    protected Estadisticas stats;
    protected double      precision;
    protected Random      rand;

    public Enemigo(String nombre, int hp, int fuerza, double precision) {
        this.nombre     = nombre;
        this.stats      = new Estadisticas(hp, 0, fuerza, 0);
        this.precision  = precision;
        this.rand       = new Random();
    }

    // Ataca al jugador con dano fisico floor(fuerza*1.25), sujeto a la precision del enemigo.
    // @param cloud - jugador objetivo del ataque
    public void atacar(Jugador cloud) {
        if (rand.nextDouble() < precision) {
            int dano = (int)(stats.getFuerza() * 1.25);
            cloud.getStats().recibirDMG(dano);
            cloud.cargarLimiteRecibido(dano);
            System.out.println(nombre + " ataca a Cloud por " + dano + " de dano!");
        } else {
            System.out.println(nombre + " fallo su ataque!");
        }
    }

    // Otorga la recompensa de XP al jugador tras ser derrotado.
    // @param cloud - jugador que recibe la experiencia
    public void giveXpRecompensa(Jugador cloud) {
        System.out.println(nombre + " derrotado! Cloud gana " + xpRecompensa + " XP.");
        cloud.recibirXP(xpRecompensa);
    }

    public Estadisticas getStats()            { return stats; }
    public int          getXpRecompensa()     { return xpRecompensa; }
    public int          getChatarraRecompensa(){ return chatarraRecompensa; }
    public double       getPrecision()        { return precision; }
}
