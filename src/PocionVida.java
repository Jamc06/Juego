public class PocionVida extends Item {
    private int cantidad;

    public PocionVida(String nombre, int cantidad) {
        super(nombre, "Restaura " + cantidad + " puntos de vida");
        this.cantidad = cantidad;
    }

    @Override
    public void aplicar(Personaje objetivo) {
        int vidaAntes = objetivo.getVida();
        objetivo.recibirDanio(-cantidad); // truco: daño negativo = curar
        System.out.println(
            nombre + " usada en " + objetivo.getNombre() +
            " → HP: " + vidaAntes + " → " + objetivo.getVida()
        );
    }
}
