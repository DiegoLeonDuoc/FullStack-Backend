package fullstack.backend.service;

import fullstack.backend.model.Artista;
import fullstack.backend.repository.ArtistaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class ArtistaService {

    @Autowired
    private ArtistaRepository artistaRepository;

    public List<Artista> getAllArtistas() {
        return artistaRepository.findAll();
    }

    public Optional<Artista> getArtistaById(Long id) {
        return artistaRepository.findById(id);
    }

    public Optional<Artista> getArtistaByNombre(String nombre) {
        return artistaRepository.findByNombreArtista(nombre);
    }

    public Artista createArtista(Artista artista) {
        // Validate unique constraint
        if (artistaRepository.existsByNombreArtista(artista.getNombreArtista())) {
            throw new RuntimeException("Artista ya existe: " + artista.getNombreArtista());
        }
        return artistaRepository.save(artista);
    }

    public Artista updateArtista(Long id, Artista artistaDetails) {
        Optional<Artista> artista = artistaRepository.findById(id);
        if (artista.isPresent()) {
            Artista existingArtista = artista.get();

            // Check name uniqueness if changed
            if (!existingArtista.getNombreArtista().equals(artistaDetails.getNombreArtista()) &&
                    artistaRepository.existsByNombreArtista(artistaDetails.getNombreArtista())) {
                throw new RuntimeException("Artista ya existe: " + artistaDetails.getNombreArtista());
            }

            existingArtista.setNombreArtista(artistaDetails.getNombreArtista());
            return artistaRepository.save(existingArtista);
        }
        throw new RuntimeException("Artista no encontrado con id: " + id);
    }

    public void deleteArtista(Long id) {
        Optional<Artista> artista = artistaRepository.findById(id);
        if (artista.isPresent()) {
            artistaRepository.delete(artista.get());
        } else {
            throw new RuntimeException("Artista no encontrado con id: " + id);
        }
    }

    public List<Artista> searchArtistasByNombre(String nombre) {
        return artistaRepository.findAll().stream()
                .filter(artista -> artista.getNombreArtista().toLowerCase().contains(nombre.toLowerCase()))
                .toList();
    }

    public boolean artistaExists(Long id) {
        return artistaRepository.existsById(id);
    }
}
