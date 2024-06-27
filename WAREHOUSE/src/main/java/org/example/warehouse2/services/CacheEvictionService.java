package org.example.warehouse2.services;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.PersistenceUnit;
import org.springframework.stereotype.Service;

@Service
public class CacheEvictionService {

    @PersistenceUnit
    private EntityManagerFactory entityManagerFactory;

    public void evictAllCaches() {
        entityManagerFactory.getCache().evictAll();
    }

}
