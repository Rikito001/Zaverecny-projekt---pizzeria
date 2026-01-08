package sk.ukf.pizzeria.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sk.ukf.pizzeria.entity.Tag;
import sk.ukf.pizzeria.exception.ObjectNotFoundException;
import sk.ukf.pizzeria.repository.TagRepository;
import java.util.List;

@Service
@Transactional
public class TagService {

    private final TagRepository tagRepository;

    @Autowired
    public TagService(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    public List<Tag> findAll() {
        return tagRepository.findAll();
    }

    public Tag findById(Long id) {
        return tagRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Tag s ID " + id + " neexistuje"));
    }

    public Tag save(Tag tag) {
        return tagRepository.save(tag);
    }

    public Tag update(Long id, Tag updatedTag) {
        Tag tag = findById(id);
        tag.setName(updatedTag.getName());
        tag.setColor(updatedTag.getColor());
        return tagRepository.save(tag);
    }

    public void delete(Long id) {
        if (!tagRepository.existsById(id)) {
            throw new ObjectNotFoundException("Tag s ID " + id + " neexistuje");
        }
        tagRepository.deleteById(id);
    }

    public boolean existsByName(String name) {
        return tagRepository.existsByName(name);
    }
}
