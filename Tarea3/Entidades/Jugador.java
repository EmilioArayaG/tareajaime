package entidades;

import componentes.Estadisticas;
import componentes.Materia;
import componentes.Elemento;
import java.util.ArrayList;
import java.util.List;

public class Jugador {
    private String nombre= "Cloud";
    private int nivel;
    private int chatarra;
    private int xpActual;
    private int limiteActual;
    private Estadisticas stats;
    private List<Materia> mochila;
    private Arma busterSword;

    public Jugador() {
        this.nivel= 1;
        this.chatarra= 0;
        this.xpActual= 0;
        this.limiteActual= 0;
        this.stats= new Estadisticas(200, 50, 15, 15);
        this.mochila= new ArrayList<>();
        this.busterSword= new Arma();
    }

    /**
     * Incrementa la experiencia y gestiona la subida de nivel lineal.
     * XP necesaria = 10 * nivel actual[cite: 121].
     * @param xp Puntos de experiencia obtenidos.
     */
    public void recibirXP(int xp) {
        this.xpActual+= xp;
        int xpNecesaria= 10 * this.nivel;
        
        if (this.xpActual>= xpNecesaria) {
            this.nivel++;
            this.xpActual-= xpNecesaria;
            stats.setHpMaximo(stats.getHpMaximo() + 10);
            stats.setMpMaximo(stats.getMpMaximo() + 5);
            stats.setFuerza(stats.getFuerza() + 4);
            stats.setMagia(stats.getMagia() + 6);
            stats.setHpActual(stats.getHpMaximo());
            stats.setMpActual(stats.getMpMaximo());
        }
    }

    /**
     * Aumenta la barra de límite al recibir daño.
     * @param dano Cantidad de daño recibido.
     */
    public void cargarLimiteRecibido(int dano) {
        this.limiteActual += Math.abs(dano) / 2;
        if (this.limiteActual > 100) this.limiteActual = 100;
    }

    public class Arma{
        private String nombre= "Buster Sword";
        private List<Materia> materiasEquipadas;

        public Arma() {
            this.materiasEquipadas= new ArrayList<>();
        }

        public int calcularDanoMagico(Elemento elemento) {
            int n= 0;
            for (Materia m : materiasEquipadas) {
                if (m.getElemento()== elemento) n++;
            }
            
            if (n== 0) return 0;
            
            double dano= stats.getMagia() * (1.0 + (0.5 * n));
            return (int) dano;
        }
        public int calcularCostoMP(Elemento elemento) {
            int n= 0;
            for (Materia m : materiasEquipadas) {
                if (m.getElemento()== elemento) n++;
            }
            return 10 + (5 * n);
        }

        public int calcularDanoFisico(){
            return (int) (stats.getFuerza() * 1.25);
        }

        public int calcularDanoLimite(){
            limiteActual= 0;
            return stats.getFuerza() * 5;
        }
    }
}