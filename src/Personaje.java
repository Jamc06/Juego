public abstract class Personaje {
    protected String nombre;
    protected int vida;
    protected int ataque;
    protected int vidaMax;


    // Nuevo: estado envenenado
    protected boolean envenenado = false;

    public Personaje(String nombre, int vida, int ataque) {
        this.nombre = nombre;
        this.vida = vida;
        this.ataque = ataque;
        this.vidaMax = vida;
    }

    public boolean estaVivo() { return vida > 0; }

    public void recibirDanio(int d) {
        vida -= d;
        if (vida < 0) vida = 0;
    }

    public String getNombre() { return nombre; }
    public int getVida() { return vida; }
    public int getAtaque() { return ataque; }

    // Nuevo: veneno
    public void envenenar() {
        envenenado = true;
    }

    public void curarVeneno() {
        envenenado = false;
    }
    public int getVidaMax() {
        return vidaMax;
    }
    public boolean isEnvenenado() {
        return envenenado;
    }

    // Métodos abstractos
    public abstract void mensajeInicio();
    public abstract void mensajeFinal(boolean gano);

    @Override
    public String toString() {
        return nombre + " [HP: " + vida + ", ATK: " + ataque + 
               (envenenado ? ", VENENO" : "") + "]";
    }
}
