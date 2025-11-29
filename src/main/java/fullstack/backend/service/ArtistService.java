package fullstack.backend.service;

import fullstack.backend.exception.DomainValidationException;
import fullstack.backend.exception.ResourceNotFoundException;
import fullstack.backend.model.Artist;
import fullstack.backend.repository.ArtistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
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
        validateArtist(artist);
        if (artistRepository.existsByArtistName(artist.getArtistName())) {
            throw new DomainValidationException("Artist already exists: " + artist.getArtistName());
        }
        return artistRepository.save(artist);
    }

    public Artist updateArtist(Long id, Artist artistDetails) {
        validateArtist(artistDetails);
        Artist existingArtist = artistRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Artist not found with id: " + id));

        if (!existingArtist.getArtistName().equals(artistDetails.getArtistName()) &&
                artistRepository.existsByArtistName(artistDetails.getArtistName())) {
            throw new DomainValidationException("Artist already exists: " + artistDetails.getArtistName());
        }

        existingArtist.setArtistName(artistDetails.getArtistName());
        return artistRepository.save(existingArtist);
    }

    public void deleteArtist(Long id) {
        Artist artist = artistRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Artist not found with id: " + id));
        artistRepository.delete(artist);
    }

    public List<Artist> searchArtistsByName(String name) {
        return artistRepository.findAll().stream()
                .filter(artist -> artist.getArtistName().toLowerCase().contains(name.toLowerCase()))
                .toList();
    }

    public boolean artistExists(Long id) {
        return artistRepository.existsById(id);
    }

    private void validateArtist(Artist artist) {
        if (artist == null || !StringUtils.hasText(artist.getArtistName())) {
            throw new DomainValidationException("Artist name is required");
        }
    }
}
