package com.iiie.server.repository;

import com.iiie.server.domain.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ImageRepository extends JpaRepository<Image, Long> { }
