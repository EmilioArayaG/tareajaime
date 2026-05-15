package entidades;

public class Sephiroth extends Enemigo {
    private int contadorSuperNova;

    public Sephiroth() {
        super("Sephiroth", 500, 40, 0.90);
        this.contadorSuperNova  = 0;
        this.xpRecompensa       = 0;
        this.chatarraRecompensa = 0;
    }

    // Incrementa el contador de SuperNova en 1 cada turno de combate.
    public void incrementarContador() {
        contadorSuperNova++;
    }

    // Reinicia el contador de SuperNova a 0 (ocurre al recibir un Ataque Limite de Cloud).
    public void reiniciarContador() {
        contadorSuperNova = 0;
        System.out.println("El contador de SuperNova de Sephiroth fue reiniciado!");
    }

    // Ejecuta SuperNova: imprime el mensaje del ataque devastador que aniquila a Cloud.
    public void lanzarSuperNova() {
        System.out.println("==================================================");
        System.out.println("  SEPHIROTH lanza *** SUPERNOVA ***");
        System.out.println("  El planeta tiembla... Cloud es aniquilado.");
        System.out.println("==================================================");
    }

    public int getContadorSuperNova() { return contadorSuperNova; }
}
