    private String combates_file;
    private String desafios_file;
    private Usuario usuario;
    private List<Personajes> personajes;
    private List<Combates> combates;
    private List<Desafio> desafios;
    private List<Arma> armas;
    private List<Armadura> armaduras;
    private List<Notificacion> notificaciones;
    private I_Storage storage;

    private static final Map<Class<? extends I_Storage>, I_Storage> storageMap = new HashMap<>();

    static {
        // Initialize the storage map with each class and its instance
        storageMap.put(XMLStorage.class, XMLStorage.getInstance());
    }

    protected FileManager(Class<? extends I_Storage> storageClass) {
        this.storage = storageMap.get(storageClass);  // Retrieve the appropriate storage instance
        if (this.storage == null) {
            throw new IllegalArgumentException("Unsupported storage class: " + storageClass);
        }
    }

    protected String guardarUsuario(Usuario usuario) {
        return storage.saveUsuario(usuario);
    }

    protected List<Usuario> cargarUsuarios() {
        return storage.loadUsuarios();
    }

    protected String guardarPersonajes(List<Personajes> personajes) {
        return storage.savePersonajes(personajes);
    }

    protected List<Personajes> cargarPersonajes() {
        return storage.loadPersonajes();
    }

    protected String guardarCombates(List<Combates> combates) {
        return storage.saveCombates(combates);
    }

    protected List<Combates> cargarCombates() {
        return storage.loadCombates();
    }

    protected String guardarDesafio(Desafio desafio) {
        return storage.saveDesafio(desafio);
    }

    protected List<Desafio> cargarDesafios() {
        return storage.loadDesafios();
    }

    protected List<Arma> cargarArmas() {
        return storage.loadArmas();
    }

    protected List<Armadura> cargarArmaduras() {
        return storage.loadArmaduras();
    }

    protected Notificacion getNotificacion(Usuario usuario) {
        return storage.getNotificacion(usuario);
    }

    protected void setNotificacion(Usuario usuario, String mensaje) {
        storage.setNotificacion(usuario, mensaje);
    }

    protected void deleteNotificacion(Usuario usuario) {
        storage.deleteNotificacion(usuario);
    }
}