package fullstack.backend.service;

import fullstack.backend.model.Artist;
import fullstack.backend.repository.ArtistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class ArtistService {

    @Autowired
    private ArtistRepository artistRepository;

    public List<Artist> getAllArtists() {
        return artistRepository.findAll();
    }

    public Optional<Artist> getArtistById(Long id) {
        return artistRepository.findById(id);
    }

    public Optional<Artist> getArtistByName(String name) {
        return artistRepository.findByArtistName(name);
    }

    public Artist createArtist(Artist artist) {
        // Validate unique constraint
        if (artistRepository.existsByArtistName(artist.getArtistName())) {
            throw new RuntimeException("Artist already exists: " + artist.getArtistName());
        }
        return artistRepository.save(artist);
    }

    public Artist updateArtist(Long id, Artist artistDetails) {
        Optional<Artist> artist = artistRepository.findById(id);
        if (artist.isPresent()) {
            Artist existingArtist = artist.get();

            // Check name uniqueness if changed
            if (!existingArtist.getArtistName().equals(artistDetails.getArtistName()) &&
                    artistRepository.existsByArtistName(artistDetails.getArtistName())) {
                throw new RuntimeException("Artist already exists: " + artistDetails.getArtistName());
            }

            existingArtist.setArtistName(artistDetails.getArtistName());
            return artistRepository.save(existingArtist);
        }
        throw new RuntimeException("Artist not found with id: " + id);
    }

    public void deleteArtist(Long id) {
        Optional<Artist> artist = artistRepository.findById(id);
        if (artist.isPresent()) {
            artistRepository.delete(artist.get());
        } else {
            throw new RuntimeException("Artist not found with id: " + id);
        }
    }

    public List<Artist> searchArtistsByName(String name) {
        return artistRepository.findAll().stream()
                .filter(artist -> artist.getArtistName().toLowerCase().contains(name.toLowerCase()))
                .toList();
    }

    public boolean artistExists(Long id) {
        return artistRepository.existsById(id);
    }
}
