package com.uab.taller.store.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.uab.taller.store.domain.Rol;
import com.uab.taller.store.repository.RolRepository;
import com.uab.taller.store.service.interfaces.IRolService;

@Service
public class RolServiceImp implements IRolService {

    RolRepository repository;

    public RolServiceImp(RolRepository rolRepository) {
        this.repository = rolRepository;
    }

    @Override
    public List<Rol> findAll() {
        return repository.findAll();
    }

    @Override
    public Rol save(Rol entity) {
        if (entity.getId() == null) {
            return repository.save(entity);
        } else {
            return update(entity);
        }
    }

    @Override
    public Rol findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Rol not found with id: " + id));
    }

    @Override
    public void deleteById(Long id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
        } else {
            throw new RuntimeException("Rol not found with id: " + id);
        }
    }

    @Override
    public Rol update(Rol entity) {
        if (repository.existsById(entity.getId())) {
            return repository.save(entity);
        } else {
            throw new RuntimeException("Rol not found with id: " + entity.getId());
        }
    }
}
