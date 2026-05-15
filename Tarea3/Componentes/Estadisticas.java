package componentes;

public class Estadisticas {
    private int hpActual;
    private int hpMaximo;
    private int mpActual;
    private int mpMaximo;
    private int fuerza;
    private int magia;

    public Estadisticas(int hpMaximo, int mpMaximo, int fuerza, int magia) {
        this.hpMaximo = hpMaximo;
        this.hpActual = hpMaximo;
        this.mpMaximo = mpMaximo;
        this.mpActual = mpMaximo;
        this.fuerza   = fuerza;
        this.magia    = magia;
    }

    // Reduce el HP actual por el dano recibido, sin bajar de 0.
    // @param valor - cantidad de dano a aplicar
    public void recibirDMG(int valor) {
        this.hpActual -= valor;
        if (this.hpActual < 0) this.hpActual = 0;
    }

    public int  getHpActual()  { return hpActual; }
    public void setHpActual(int v)  { this.hpActual  = v; }
    public int  getHpMaximo()  { return hpMaximo; }
    public void setHpMaximo(int v)  { this.hpMaximo  = v; }
    public int  getMpActual()  { return mpActual; }
    public void setMpActual(int v)  { this.mpActual  = v; }
    public int  getMpMaximo()  { return mpMaximo; }
    public void setMpMaximo(int v)  { this.mpMaximo  = v; }
    public int  getFuerza()    { return fuerza; }
    public void setFuerza(int v)    { this.fuerza    = v; }
    public int  getMagia()     { return magia; }
    public void setMagia(int v)     { this.magia     = v; }
}
