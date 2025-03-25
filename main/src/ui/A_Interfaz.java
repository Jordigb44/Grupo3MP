package ui;

public class A_Interfaz implements I_Interfaz {
    private InterfazCLI interfazCLI;

    // Constructor que inicializa InterfazCLI
    public A_Interfaz() {
        this.interfazCLI = new InterfazCLI(); // Initialize the adapter InterfazCLI
    }

    @Override
    public void mostrar(String contenido) {
        // Delegar a InterfazCLI
        interfazCLI.mostrar(contenido);
    }

    @Override
    public String pedirEntrada() {
        // Delegar a InterfazCLI y retornar la entrada del usuario
        return interfazCLI.pedirEntrada();
    }

    @Override
    public void limpiarPantalla() {
        // Delegar a InterfazCLI
        interfazCLI.limpiarPantalla();
    }
}