package com.iiie.server.repository;

import com.iiie.server.domain.Gallery;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface GalleryRepository extends JpaRepository<Gallery, Integer> {}
