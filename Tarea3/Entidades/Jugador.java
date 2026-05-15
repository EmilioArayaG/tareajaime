package entidades;

import componentes.Elemento;
import componentes.Estadisticas;
import componentes.Materia;
import java.util.ArrayList;
import java.util.List;

public class Jugador {
    public  String        nombre       = "Cloud";
    private int           nivel;
    private int           xpActual;
    private int           chatarra;
    private int           limiteActual;
    private Estadisticas  stats;
    private List<Materia> mochila;
    private Arma          busterSword;

    public Jugador() {
        this.nivel        = 1;
        this.xpActual     = 0;
        this.chatarra     = 0;
        this.limiteActual = 0;
        this.stats        = new Estadisticas(200, 50, 15, 15);
        this.mochila      = new ArrayList<>();
        this.busterSword  = new Arma();
    }

    // Recibe XP y gestiona subidas de nivel (XP necesaria = 10 * nivel actual).
    // Al subir de nivel aumentan HP Max+10, MP Max+5, Fuerza+4, Magia+6.
    // @param xp - puntos de experiencia obtenidos
    public void recibirXP(int xp) {
        this.xpActual += xp;
        int xpNecesaria = 10 * this.nivel;
        while (this.xpActual >= xpNecesaria) {
            this.xpActual -= xpNecesaria;
            this.nivel++;
            stats.setHpMaximo(stats.getHpMaximo() + 10);
            stats.setMpMaximo(stats.getMpMaximo() + 5);
            stats.setFuerza(stats.getFuerza() + 4);
            stats.setMagia(stats.getMagia() + 6);
            stats.setHpActual(stats.getHpMaximo());
            stats.setMpActual(stats.getMpMaximo());
            System.out.println("*** Cloud subio al Nivel " + nivel + "! ***");
            System.out.println("  HP Max: " + stats.getHpMaximo()
                + " | MP Max: " + stats.getMpMaximo()
                + " | Fuerza: " + stats.getFuerza()
                + " | Magia: "  + stats.getMagia());
            xpNecesaria = 10 * this.nivel;
        }
    }

    // Carga la barra de limite segun el dano recibido: Carga += floor(dano/2).
    // @param dano - cantidad de dano recibido en ese golpe
    public void cargarLimiteRecibido(int dano) {
        this.limiteActual += dano / 2;
        if (this.limiteActual > 100) this.limiteActual = 100;
    }

    // Carga la barra de limite segun el dano infligido: Carga += floor(dano/5).
    // @param dano - cantidad de dano infligido al enemigo
    public void cargarLimiteInfligido(int dano) {
        this.limiteActual += dano / 5;
        if (this.limiteActual > 100) this.limiteActual = 100;
    }

    // Aplica la penalizacion de muerte: chatarra = 0 y mochila vaciada; materias equipadas intactas.
    // Tambien restaura HP y MP al maximo.
    public void aplicarPenalidadMuerte() {
        this.chatarra = 0;
        this.mochila.clear();
        stats.setHpActual(stats.getHpMaximo());
        stats.setMpActual(stats.getMpMaximo());
        System.out.println("Cloud fue derrotado. Pierde toda su chatarra y las materias de la mochila.");
        System.out.println("Las materias equipadas en " + busterSword.nombre + " permanecen intactas.");
    }

    // Restaura HP y MP al maximo (sin penalizacion).
    public void restaurarEstado() {
        stats.setHpActual(stats.getHpMaximo());
        stats.setMpActual(stats.getMpMaximo());
    }

    // Agrega chatarra al inventario del jugador.
    // @param cantidad - cantidad de chatarra a sumar
    public void addChatarra(int cantidad) {
        this.chatarra += cantidad;
    }

    // Agrega una materia a la mochila del jugador.
    // @param m - materia a guardar en la mochila
    public void addToMochila(Materia m) {
        this.mochila.add(m);
    }

    public int           getNivel()       { return nivel; }
    public int           getXpActual()    { return xpActual; }
    public int           getChatarra()    { return chatarra; }
    public void          setChatarra(int v) { this.chatarra = v; }
    public int           getLimiteActual(){ return limiteActual; }
    public void          setLimiteActual(int v) { this.limiteActual = v; }
    public Estadisticas  getStats()       { return stats; }
    public List<Materia> getMochila()     { return mochila; }
    public Arma          getBusterSword() { return busterSword; }

    // =========================================================
    // Clase anidada Arma: gestiona materias equipadas y calculos
    // =========================================================
    public class Arma {
        public  String        nombre           = "Buster Sword";
        private List<Materia> materiasEquipadas;
        private static final  int MAX_RANURAS  = 5;

        public Arma() {
            this.materiasEquipadas = new ArrayList<>();
        }

        // Calcula el dano magico del elemento dado segun n materias de ese tipo equipadas.
        // Formula: floor(Magia * (1.0 + 0.5 * n)); retorna 0 si no hay materias del elemento.
        // @param elemento - elemento magico a utilizar
        // @return dano magico base calculado
        public int calcularDanoMagico(Elemento elemento) {
            int n = contarMaterias(elemento);
            if (n == 0) return 0;
            return (int)(stats.getMagia() * (1.0 + 0.5 * n));
        }

        // Calcula el costo de MP para usar magia del elemento dado: 10 + (5 * n).
        // @param elemento - elemento magico a utilizar
        // @return costo en MP
        public int calcularCostoMP(Elemento elemento) {
            return 10 + (5 * contarMaterias(elemento));
        }

        // Calcula el dano del ataque fisico: floor(Fuerza * 1.25).
        // @return dano fisico
        public int calcularDanoFisico() {
            return (int)(stats.getFuerza() * 1.25);
        }

        // Calcula el dano del Ataque Limite (Fuerza * 5) y reinicia la barra de limite a 0.
        // @return dano del ataque limite
        public int calcularDanoLimite() {
            limiteActual = 0;
            return stats.getFuerza() * 5;
        }

        // Equipa una materia de la mochila al arma si hay ranuras disponibles.
        // @param m - materia a equipar
        // @return true si se equipo correctamente, false si no hay espacio
        public boolean equiparMateria(Materia m) {
            if (materiasEquipadas.size() >= MAX_RANURAS) return false;
            if (!mochila.contains(m)) return false;
            mochila.remove(m);
            materiasEquipadas.add(m);
            return true;
        }

        // Desequipa la materia en el indice dado y la devuelve a la mochila.
        // @param indice - posicion en la lista de materias equipadas
        // @return true si se desequipo correctamente
        public boolean desequiparMateria(int indice) {
            if (indice < 0 || indice >= materiasEquipadas.size()) return false;
            mochila.add(materiasEquipadas.remove(indice));
            return true;
        }

        // Retorna la lista de elementos distintos actualmente equipados en el arma.
        // @return lista de elementos sin duplicados
        public List<Elemento> getElementosEquipados() {
            List<Elemento> elementos = new ArrayList<>();
            for (Materia m : materiasEquipadas) {
                if (!elementos.contains(m.getElemento())) {
                    elementos.add(m.getElemento());
                }
            }
            return elementos;
        }

        // Cuenta cuantas materias del elemento dado estan equipadas.
        // @param elemento - elemento a contar
        // @return cantidad de materias de ese elemento en el arma
        private int contarMaterias(Elemento elemento) {
            int n = 0;
            for (Materia m : materiasEquipadas) {
                if (m.getElemento() == elemento) n++;
            }
            return n;
        }

        public List<Materia> getMateriasEquipadas() { return materiasEquipadas; }
        public int           getMaxRanuras()        { return MAX_RANURAS; }
    }
}
