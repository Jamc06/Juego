public abstract class Enemigo extends Personaje {
    protected boolean esJefe;

    public Enemigo(String nombre, int vida, int ataque, boolean jefe) {
        super(nombre, vida, ataque);
        this.esJefe = jefe;
    }

    @Override
    public void mensajeInicio() {
        System.out.println(nombre + ": TUU CAERAS!");
    }

    @Override
    public void mensajeFinal(boolean gano) {
        if (gano) System.out.println(nombre + ": ¡ JAJA He vencido!");
        else System.out.println(nombre + ": He sido derrotado...");
    }
}
