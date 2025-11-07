package fullstack.backend.service;

import fullstack.backend.model.Label;
import fullstack.backend.repository.LabelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class LabelService {

    @Autowired
    private LabelRepository labelRepository;

    public List<Label> getAllLabels() {
        return labelRepository.findAll();
    }

    public Optional<Label> getLabelById(Long id) {
        return labelRepository.findById(id);
    }

    public Optional<Label> getLabelByName(String name) {
        return labelRepository.findByLabelName(name);
    }

    public Label createLabel(Label label) {
        // Validate unique constraint
        if (labelRepository.existsByLabelName(label.getLabelName())) {
            throw new RuntimeException("Label already exists: " + label.getLabelName());
        }
        return labelRepository.save(label);
    }

    public Label updateLabel(Long id, Label labelDetails) {
        Optional<Label> label = labelRepository.findById(id);
        if (label.isPresent()) {
            Label existingLabel = label.get();

            // Check name uniqueness if changed
            if (!existingLabel.getLabelName().equals(labelDetails.getLabelName()) &&
                    labelRepository.existsByLabelName(labelDetails.getLabelName())) {
                throw new RuntimeException("Label already exists: " + labelDetails.getLabelName());
            }

            existingLabel.setLabelName(labelDetails.getLabelName());
            return labelRepository.save(existingLabel);
        }
        throw new RuntimeException("Label not found with id: " + id);
    }

    public void deleteLabel(Long id) {
        Optional<Label> label = labelRepository.findById(id);
        if (label.isPresent()) {
            labelRepository.delete(label.get());
        } else {
            throw new RuntimeException("Label not found with id: " + id);
        }
    }

    public boolean labelExists(Long id) {
        return labelRepository.existsById(id);
    }
}