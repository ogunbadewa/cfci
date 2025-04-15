package com.duke.innovation.service;

import com.duke.innovation.model.Resource;
import com.duke.innovation.repository.ResourceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ResourceService {

    private final ResourceRepository resourceRepository;

    @Autowired
    public ResourceService(ResourceRepository resourceRepository) {
        this.resourceRepository = resourceRepository;
    }

    public List<Resource> findAll() {
        return resourceRepository.findAll();
    }

    public Optional<Resource> findById(Long id) {
        return resourceRepository.findById(id);
    }

    public List<Resource> findByType(Resource.ResourceType type) {
        return resourceRepository.findByResourceType(type);
    }

    public List<Resource> findByCategory(String category) {
        return resourceRepository.findByCategory(category);
    }

    public List<Resource> findByTypeAndCategory(Resource.ResourceType type, String category) {
        return resourceRepository.findByResourceTypeAndCategory(type, category);
    }

    @Transactional
    public Resource save(Resource resource) {
        return resourceRepository.save(resource);
    }

    @Transactional
    public void delete(Long id) {
        resourceRepository.deleteById(id);
    }
}