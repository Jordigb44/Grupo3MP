import javax.xml.bind.*;
import java.io.*;
import java.util.List;
import com.proyecto.model.usuario.Usuario;
import com.proyecto.model.personaje.Personaje;
import com.proyecto.model.desafio.Desafio;
import com.proyecto.model.desafio.Combate;
import com.proyecto.model.Ranking;

public class XMLStorage implements I_Storage {
    private String file_path;

    // Constructor para inicializar la ruta del archivo
    public XMLStorage(String file_path) {
        this.file_path = file_path;
    }

    @Override
    public String guardarUsuario(Usuario usuario) {
        try {
            JAXBContext context = JAXBContext.newInstance(Usuario.class);
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

            File file = new File(file_path);
            if (!file.exists()) {
                file.createNewFile(); // Crear el archivo si no existe
            }

            // Guardar el usuario en el archivo XML
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

            File file = new File(file_path);
            if (file.exists()) {
                // Cargar los usuarios desde el archivo XML
                return (List<Usuario>) unmarshaller.unmarshal(file);
            }
            return null; // No se encontraron usuarios si el archivo no existe
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

            File file = new File(file_path);
            if (!file.exists()) {
                file.createNewFile(); // Crear el archivo si no existe
            }

            // Guardar los personajes en el archivo XML
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

            File file = new File(file_path);
            if (file.exists()) {
                // Cargar los personajes desde el archivo XML
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

            File file = new File(file_path);
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

            File file = new File(file_path);
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

            File file = new File(file_path);
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

            File file = new File(file_path);
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

            File file = new File(file_path);
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

            File file = new File(file_path);
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