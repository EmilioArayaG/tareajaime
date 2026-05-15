package componentes;

public class Estadisticas {
    private int hpActual; 
    private int hpMaximo; 
    private int mpActual; 
    private int mpMaximo; 
    private int fuerza; 
    private int magia; 

    public Estadisticas(int hpMaximo, int mpMaximo, int fuerza, int magia) {
        this.hpMaximo= hpMaximo;
        this.hpActual= hpMaximo;
        this.mpMaximo= mpMaximo;
        this.mpActual= mpMaximo; 
        this.fuerza= fuerza;
        this.magia= magia;
    }

    /**
     * Reduce el HP actual en base al daño recibido en combate.
     * @param valor Cantidad de daño a restar del HP.
     */
    public void recibirDMG(int valor) {
        this.hpActual-= valor;
        if (this.hpActual< 0) {
            this.hpActual= 0;
        }
    }

}