public class XMLStorage implements I_Storage {
    private String directoryPath;

    // Constructor to initialize the directory path
    public XMLStorage(String directoryPath) {
        this.directoryPath = directoryPath;
    }

    // Helper method to generate the file path for each object type
    private String getFilePath(String objectType) {
        return directoryPath + "/" + objectType + ".xml";
    }

    @Override
    public String guardarUsuario(Usuario usuario) {
        try {
            JAXBContext context = JAXBContext.newInstance(Usuario.class);
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

            File file = new File(getFilePath("usuarios"));
            if (!file.exists()) {
                file.createNewFile(); // Create file if it doesn't exist
            }

            marshaller.marshal(usuario, file);
            return "Usuario guardado con éxito";
        } catch (JAXBException | IOException e) {
            e.printStackTrace();
            return "Error al guardar el usuario";
        }
    }

    @Override
    public List<Usuario> cargarUsuarios() {
        try {
            JAXBContext context = JAXBContext.newInstance(Usuario.class);
            Unmarshaller unmarshaller = context.createUnmarshaller();

            File file = new File(getFilePath("usuarios"));
            if (file.exists()) {
                return (List<Usuario>) unmarshaller.unmarshal(file);
            }
            return null; // No users found if the file doesn't exist
        } catch (JAXBException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public String guardarPersonajes(List<Personaje> personajes) {
        try {
            JAXBContext context = JAXBContext.newInstance(Personaje.class);
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

            File file = new File(getFilePath("personajes"));
            if (!file.exists()) {
                file.createNewFile();
            }

            marshaller.marshal(personajes, file);
            return "Personajes guardados con éxito";
        } catch (JAXBException | IOException e) {
            e.printStackTrace();
            return "Error al guardar los personajes";
        }
    }

    @Override
    public List<Personaje> cargarPersonajes() {
        try {
            JAXBContext context = JAXBContext.newInstance(Personaje.class);
            Unmarshaller unmarshaller = context.createUnmarshaller();

            File file = new File(getFilePath("personajes"));
            if (file.exists()) {
                return (List<Personaje>) unmarshaller.unmarshal(file);
            }
            return null;
        } catch (JAXBException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public String guardarCombates(List<Combate> combates) {
        try {
            JAXBContext context = JAXBContext.newInstance(Combate.class);
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

            File file = new File(getFilePath("combates"));
            if (!file.exists()) {
                file.createNewFile();
            }

            marshaller.marshal(combates, file);
            return "Combates guardados con éxito";
        } catch (JAXBException | IOException e) {
            e.printStackTrace();
            return "Error al guardar los combates";
        }
    }

    @Override
    public List<Combate> cargarCombates() {
        try {
            JAXBContext context = JAXBContext.newInstance(Combate.class);
            Unmarshaller unmarshaller = context.createUnmarshaller();

            File file = new File(getFilePath("combates"));
            if (file.exists()) {
                return (List<Combate>) unmarshaller.unmarshal(file);
            }
            return null;
        } catch (JAXBException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public String guardarDesafios(List<Desafio> desafios) {
        try {
            JAXBContext context = JAXBContext.newInstance(Desafio.class);
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

            File file = new File(getFilePath("desafios"));
            if (!file.exists()) {
                file.createNewFile();
            }

            marshaller.marshal(desafios, file);
            return "Desafíos guardados con éxito";
        } catch (JAXBException | IOException e) {
            e.printStackTrace();
            return "Error al guardar los desafíos";
        }
    }

    @Override
    public List<Desafio> cargarDesafios() {
        try {
            JAXBContext context = JAXBContext.newInstance(Desafio.class);
            Unmarshaller unmarshaller = context.createUnmarshaller();

            File file = new File(getFilePath("desafios"));
            if (file.exists()) {
                return (List<Desafio>) unmarshaller.unmarshal(file);
            }
            return null;
        } catch (JAXBException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public String guardarRanking(Ranking ranking) {
        try {
            JAXBContext context = JAXBContext.newInstance(Ranking.class);
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

            File file = new File(getFilePath("ranking"));
            if (!file.exists()) {
                file.createNewFile();
            }

            marshaller.marshal(ranking, file);
            return "Ranking guardado con éxito";
        } catch (JAXBException | IOException e) {
            e.printStackTrace();
            return "Error al guardar el ranking";
        }
    }

    @Override
    public List<Ranking> cargarRanking() {
        try {
            JAXBContext context = JAXBContext.newInstance(Ranking.class);
            Unmarshaller unmarshaller = context.createUnmarshaller();

            File file = new File(getFilePath("ranking"));
            if (file.exists()) {
                return (List<Ranking>) unmarshaller.unmarshal(file);
            }
            return null;
        } catch (JAXBException e) {
            e.printStackTrace();
            return null;
        }
    }
}