public class PocionDamage extends Item {
    private int cantidad;

    public PocionDamage(String nombre, int cantidad) {
        super(nombre, "Inflige " + cantidad + " puntos de daño al objetivo");
        this.cantidad = cantidad;
    }

    @Override
    public void aplicar(Personaje objetivo) {
        int vidaAntes = objetivo.getVida();
        objetivo.recibirDanio(cantidad);
        System.out.println(
            nombre + " lanzada a " + objetivo.getNombre() +
            " → VIDA: " + vidaAntes + " → " + objetivo.getVida()
        );
    }
}
