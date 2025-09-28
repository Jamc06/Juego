public abstract class Jugador extends Personaje {
    public Jugador(String nombre, int vida, int ataque) {
        super(nombre, vida, ataque);
    }

    @Override
    public void mensajeInicio() {
        System.out.println(nombre + ": Listo para la batalla");
    }

    @Override
    public void mensajeFinal(boolean gano) {
        if (gano) System.out.println(nombre + ": He triunfado...");
        else System.out.println(nombre + ": He caído en batalla...");
    }
}
