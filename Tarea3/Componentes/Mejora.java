package componentes;

public class Mejora {
    private String   nombre;
    private int      costoChatarra;
    private TipoStat statAfectado;
    private int      valorBono;

    public Mejora(String nombre, int costoChatarra, TipoStat statAfectado, int valorBono) {
        this.nombre        = nombre;
        this.costoChatarra = costoChatarra;
        this.statAfectado  = statAfectado;
        this.valorBono     = valorBono;
    }

    public String   getNombre()        { return nombre; }
    public int      getCostoChatarra() { return costoChatarra; }
    public TipoStat getStatAfectado()  { return statAfectado; }
    public int      getValorBono()     { return valorBono; }
}
