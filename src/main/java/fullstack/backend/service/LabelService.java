package fullstack.backend.service;

import fullstack.backend.exception.DomainValidationException;
import fullstack.backend.exception.ResourceNotFoundException;
import fullstack.backend.model.Label;
import fullstack.backend.repository.LabelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
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
        validateLabel(label);
        if (labelRepository.existsByLabelName(label.getLabelName())) {
            throw new DomainValidationException("Label already exists: " + label.getLabelName());
        }
        return labelRepository.save(label);
    }

    public Label updateLabel(Long id, Label labelDetails) {
        validateLabel(labelDetails);
        Label existingLabel = labelRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Label not found with id: " + id));

        if (!existingLabel.getLabelName().equals(labelDetails.getLabelName()) &&
                labelRepository.existsByLabelName(labelDetails.getLabelName())) {
            throw new DomainValidationException("Label already exists: " + labelDetails.getLabelName());
        }

        existingLabel.setLabelName(labelDetails.getLabelName());
        return labelRepository.save(existingLabel);
    }

    public void deleteLabel(Long id) {
        Label label = labelRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Label not found with id: " + id));
        labelRepository.delete(label);
    }

    public boolean labelExists(Long id) {
        return labelRepository.existsById(id);
    }

    private void validateLabel(Label label) {
        if (label == null || !StringUtils.hasText(label.getLabelName())) {
            throw new DomainValidationException("Label name is required");
        }
    }
}