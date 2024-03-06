package com.hogwarts.school.service;

import com.hogwarts.school.exception.AvatarNotFoundException;
import com.hogwarts.school.exception.AvatarProcessingException;
import com.hogwarts.school.model.Avatar;
import com.hogwarts.school.model.Student;

import com.hogwarts.school.repositories.AvatarRepository;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;


@Service
public class AvatarService {

    private final Path avatarsDir;
    private final AvatarRepository avatarRepository;
    private final StudentService studentService;

    public AvatarService(@Value("${path.to.avatars.folder}") String avatarsDir, AvatarRepository avatarRepository, StudentService studentService) {
        this.avatarsDir = Paths.get(avatarsDir);
        this.avatarRepository = avatarRepository;
        this.studentService = studentService;
    }
@PostConstruct
    public void init() {
    try {
        if (!Files.exists(avatarsDir) || !Files.isDirectory(avatarsDir)) {
            Files.createDirectories(avatarsDir);
        }
    } catch (IOException e) {
        throw new AvatarProcessingException();
    }

    }

    @Transactional
    public void uploadAvatar(Long studentId, MultipartFile avatarFile) throws IOException {
        try {
            Student student = studentService.findStudent(studentId);
            byte[] data = avatarFile.getBytes();

            String extension = StringUtils.getFilenameExtension(avatarFile.getOriginalFilename());
            String fileName = String.format("%s%s", UUID.randomUUID(), extension);
            Path path = avatarsDir.resolve(fileName);
            Files.write(path, data);

            Avatar avatar = new Avatar();
            avatar.setStudent(student);
            avatar.setData(data);
            avatar.setFileSize(avatarFile.getSize());
            avatar.setMediaType(avatarFile.getContentType());
            avatar.setFilePath(path.toString());

            avatarRepository.save(avatar);
        } catch (IOException e) {
            throw new AvatarProcessingException();
        }

    }

    @Transactional(readOnly = true)
    public Pair<byte[], String> getAvatarFromDb(Long studentId) {
        Avatar avatar = avatarRepository.findByStudentId(studentId).orElseThrow(AvatarNotFoundException::new);
        return Pair.of(avatar.getData(), avatar.getMediaType());
    }

    @Transactional(readOnly = true)
    public Pair<byte[], String> getAvatarFormFs(Long studentId) {
        Avatar avatar = avatarRepository.findByStudentId(studentId).orElseThrow(AvatarNotFoundException::new);
        try {
            byte[] data = Files.readAllBytes(Paths.get(avatar.getFilePath()));
            return Pair.of(data, avatar.getMediaType());
        } catch (IOException e) {
            throw new AvatarProcessingException();
        }

    }
}
