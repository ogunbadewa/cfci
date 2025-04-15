package com.duke.innovation.repository;

import com.duke.innovation.model.Resource;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ResourceRepository extends JpaRepository<Resource, Long> {
    List<Resource> findByResourceType(Resource.ResourceType resourceType);
    List<Resource> findByCategory(String category);
    List<Resource> findByResourceTypeAndCategory(Resource.ResourceType resourceType, String category);
}