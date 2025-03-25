public class InterfazCLI implements I_Interfaz {
    private A_Interfaz aInterfaz;

    public InterfazCLI() {
        this.aInterfaz = new A_Interfaz(); // Initialize the adapter A_Interfaz
    }

    @Override
    public void mostrar(String contenido) {
        aInterfaz.mostrar(contenido); // Delegate to A_Interfaz with content parameter
    }

    @Override
    public String pedirEntrada() {
        return aInterfaz.pedirEntrada(); // Return the result of the input from A_Interfaz
    }

    @Override
    public void limpiarPantalla() {
        aInterfaz.limpiarPantalla(); // Delegate to A_Interfaz
    }
}
