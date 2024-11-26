package com.iiie.server.controller;
import com.iiie.server.domain.Gallery;
import com.iiie.server.service.GalleryService;
import com.iiie.server.dto.GalleryDTO;
import com.iiie.server.utils.SuccessResponse;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/gallery")
public class GalleryController {

    private final GalleryService galleryService;

    public GalleryController(GalleryService galleryService) { this.galleryService = galleryService; }
}
