package it.nesea.albergo.hotel_service.model.repository;

import it.nesea.albergo.hotel_service.model.Camera;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CameraRepository extends JpaRepository<Camera,Integer> {
    Camera findByNumeroCamera(String numeroCamera);
}
