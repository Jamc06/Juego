public abstract class Item {
    protected String nombre;
    protected String descripcion;

    public Item(String nombre, String descripcion) {
        this.nombre = nombre;
        this.descripcion = descripcion;
    }

    public String getNombre() { return nombre; }
    public String getDescripcion() { return descripcion; }

    // Método abstracto: cada ítem decide qué hace al aplicarse
    public abstract void aplicar(Personaje objetivo);
}
