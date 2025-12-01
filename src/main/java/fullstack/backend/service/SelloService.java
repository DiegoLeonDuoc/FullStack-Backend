package fullstack.backend.service;

import fullstack.backend.model.Sello;
import fullstack.backend.repository.SelloRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class SelloService {

    @Autowired
    private SelloRepository selloRepository;

    public List<Sello> getAllSellos() {
        return selloRepository.findAll();
    }

    public Optional<Sello> getSelloById(Long id) {
        return selloRepository.findById(id);
    }

    public Optional<Sello> getSelloByNombre(String nombre) {
        return selloRepository.findByNombreSello(nombre);
    }

    public Sello createSello(Sello sello) {
        // Validate unique constraint
        if (selloRepository.existsByNombreSello(sello.getNombreSello())) {
            throw new RuntimeException("Sello ya existe: " + sello.getNombreSello());
        }
        return selloRepository.save(sello);
    }

    public Sello updateSello(Long id, Sello selloDetails) {
        Optional<Sello> sello = selloRepository.findById(id);
        if (sello.isPresent()) {
            Sello existingSello = sello.get();

            // Check name uniqueness if changed
            if (!existingSello.getNombreSello().equals(selloDetails.getNombreSello()) &&
                    selloRepository.existsByNombreSello(selloDetails.getNombreSello())) {
                throw new RuntimeException("Sello ya existe: " + selloDetails.getNombreSello());
            }

            existingSello.setNombreSello(selloDetails.getNombreSello());
            return selloRepository.save(existingSello);
        }
        throw new RuntimeException("Sello no encontrado con id: " + id);
    }

    public void deleteSello(Long id) {
        Optional<Sello> sello = selloRepository.findById(id);
        if (sello.isPresent()) {
            selloRepository.delete(sello.get());
        } else {
            throw new RuntimeException("Sello no encontrado con id: " + id);
        }
    }

    public boolean selloExists(Long id) {
        return selloRepository.existsById(id);
    }
}
