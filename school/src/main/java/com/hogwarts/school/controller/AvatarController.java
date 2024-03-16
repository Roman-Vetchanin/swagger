package com.hogwarts.school.controller;

import com.hogwarts.school.dto.AvatarDto;
import com.hogwarts.school.model.Avatar;
import com.hogwarts.school.service.AvatarService;
import jakarta.validation.constraints.Min;
import org.springframework.data.util.Pair;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

@RestController
@RequestMapping(AvatarController.BASE_URI)
@Validated
public class AvatarController {

    public static final String BASE_URI = "/avatars";
    private final AvatarService avatarService;

    public AvatarController(AvatarService avatarService) {
        this.avatarService = avatarService;
    }

    @PostMapping(value = "/{studentId}/avatar", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> uploadAvatar(@PathVariable Long studentId, @RequestParam MultipartFile avatar) {
        avatarService.uploadAvatar(studentId, avatar);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/from-db")
    public ResponseEntity<byte[]> getAvatarFromDb(@RequestParam Long studentId) {
        return transform(avatarService.getAvatarFromDb(studentId));
    }
    @GetMapping("/from-fs")
    public ResponseEntity<byte[]> getAvatarFromFs(@RequestParam Long studentId) {
        return transform(avatarService.getAvatarFormFs(studentId));
    }
    private ResponseEntity<byte[]> transform(Pair<byte[], String> pair) {
        byte[] data = pair.getFirst();
        return ResponseEntity.status(HttpStatus.OK)
                .contentLength(data.length)
                .contentType(MediaType.parseMediaType(pair.getSecond()))
                .body(data);
    }

    @GetMapping
    public List<AvatarDto> getAvatars(@RequestParam @Min(1) int page, @RequestParam @Min(1) int size) {
        return avatarService.getAvatars(page, size);
    }

}
