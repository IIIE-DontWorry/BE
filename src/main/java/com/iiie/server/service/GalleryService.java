package com.iiie.server.service;
import com.iiie.server.domain.Gallery;
import com.iiie.server.dto.GalleryDTO;
import com.iiie.server.exception.NotFoundException;
import com.iiie.server.repository.CaregiverRepository;
import com.iiie.server.repository.GalleryRepository;
import com.iiie.server.repository.GuardianRepository;
import com.iiie.server.repository.NoteRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class GalleryService {
    private final GalleryRepository galleryRepository;

    public GalleryService(GalleryRepository gallertRepository) {
        this.galleryRepository = gallertRepository;
    }
}
