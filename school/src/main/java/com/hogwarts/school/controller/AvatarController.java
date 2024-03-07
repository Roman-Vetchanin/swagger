package com.hogwarts.school.controller;

import com.hogwarts.school.service.AvatarService;
import org.springframework.data.util.Pair;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;

@RestController
@RequestMapping("avatars")
public class AvatarController {


    private final AvatarService avatarService;

    public AvatarController(AvatarService avatarService) {
        this.avatarService = avatarService;
    }

    @PostMapping(value = "/{studentId}/avatar", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> uploadAvatar(@PathVariable Long studentId, @RequestParam MultipartFile avatar) throws IOException {
        avatarService.uploadAvatar(studentId, avatar);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/form-db")
    public ResponseEntity<byte[]> getAvatarFromDb(@RequestParam Long studentId) {
        return transform(avatarService.getAvatarFromDb(studentId));
    }
    @GetMapping("/form-fs")
    public ResponseEntity<byte[]> getAvatarFromFs(@RequestParam Long studentId) {
        return transform(avatarService.getAvatarFormFs(studentId));
    }
//такая логика может быть в контроллере?
    private ResponseEntity<byte[]> transform(Pair<byte[], String> pair) {
        byte[] data = pair.getFirst();
        return ResponseEntity.status(HttpStatus.OK)
                .contentLength(data.length)
                .contentType(MediaType.parseMediaType(pair.getSecond()))
                .body(data);
    }
}
