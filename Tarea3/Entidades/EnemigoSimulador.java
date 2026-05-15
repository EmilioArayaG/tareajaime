package entidades;

public class EnemigoSimulador extends Enemigo {

    public EnemigoSimulador() {
        super("Soldado Comun", 50, 15, 0.85);
        this.xpRecompensa      = 15 + rand.nextInt(6); // 15 a 20
        this.chatarraRecompensa = 0;
    }

    // Verifica si el dano fisico normal del simulador eliminaria a Cloud (HP quedaria < 1).
    // @param cloud - jugador objetivo
    // @return true si el golpe normal dejaria a Cloud con menos de 1 HP
    public boolean checkDanoSeguro(Jugador cloud) {
        int dano = (int)(stats.getFuerza() * 1.25);
        return (cloud.getStats().getHpActual() - dano) < 1;
    }

    // Ataca al jugador garantizando que su HP nunca baje de 1 (zona segura).
    // @param cloud - jugador objetivo
    @Override
    public void atacar(Jugador cloud) {
        if (rand.nextDouble() < precision) {
            int dano = (int)(stats.getFuerza() * 1.25);
            if (checkDanoSeguro(cloud)) {
                dano = cloud.getStats().getHpActual() - 1;
            }
            if (dano > 0) {
                cloud.getStats().recibirDMG(dano);
                cloud.cargarLimiteRecibido(dano);
                System.out.println(nombre + " ataca a Cloud por " + dano + " de dano!");
            } else {
                System.out.println(nombre + " golpea a Cloud, pero el ataque no tiene efecto!");
            }
        } else {
            System.out.println(nombre + " fallo su ataque!");
        }
    }
}
