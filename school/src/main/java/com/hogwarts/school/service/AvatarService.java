package com.hogwarts.school.service;

import com.hogwarts.school.dto.AvatarDto;
import com.hogwarts.school.exception.AvatarNotFoundException;
import com.hogwarts.school.exception.AvatarProcessingException;
import com.hogwarts.school.mapper.AvatarMapper;
import com.hogwarts.school.model.Avatar;
import com.hogwarts.school.model.Student;

import com.hogwarts.school.repositories.AvatarRepository;

import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;


@Service
public class AvatarService {

    Logger logger = LoggerFactory.getLogger(AvatarService.class);

    private final Path avatarsDir;
    private final AvatarRepository avatarRepository;
    private final StudentService studentService;

    private final AvatarMapper avatarMapper;

    public AvatarService(@Value("${path.to.avatars.folder}") String avatarsDir,
                         AvatarRepository avatarRepository,
                         StudentService studentService,
                         AvatarMapper avatarMapper) {
        this.avatarsDir = Paths.get(avatarsDir);
        this.avatarRepository = avatarRepository;
        this.studentService = studentService;
        this.avatarMapper = avatarMapper;
    }

    @PostConstruct
    public void init() {
        try {
            if (!Files.exists(avatarsDir) && Files.isDirectory(avatarsDir)) {
                Files.createDirectories(avatarsDir);
            }
        } catch (IOException e) {
            throw new AvatarProcessingException();
        }

    }

    @Transactional
    public void uploadAvatar(Long studentId, MultipartFile avatarFile) {
        logger.info("Was invoked method for upload avatar");
        try {
            Student student = studentService.findStudent(studentId);
            byte[] data = avatarFile.getBytes();

            String extension = StringUtils.getFilenameExtension(avatarFile.getOriginalFilename());
            String fileName = String.format("%s%s", UUID.randomUUID(), extension);
            Path path = avatarsDir.resolve(fileName);
            Files.write(path, data);

            logger.debug("Path to save avatar in FS {}",path);

            Avatar avatar = new Avatar();
            avatar.setStudent(student);
            avatar.setData(data);
            avatar.setFileSize(avatarFile.getSize());
            avatar.setMediaType(avatarFile.getContentType());
            avatar.setFilePath(path.toString());

            avatarRepository.save(avatar);
        } catch (IOException e) {
            logger.error("An I/O exception occurred");
            throw new AvatarProcessingException();
        }
    }

    @Transactional(readOnly = true)
    public Pair<byte[], String> getAvatarFromDb(Long studentId) {
        Avatar avatar = avatarRepository.findByStudentId(studentId).orElseThrow(null);
        if (avatar == null) {
            logger.error("Avatar not found");
            throw new AvatarNotFoundException();
        }
        logger.info("Was invoked method for get Avatar From Db");
        return Pair.of(avatar.getData(), avatar.getMediaType());
    }

    @Transactional(readOnly = true)
    public Pair<byte[], String> getAvatarFormFs(Long studentId) {
        logger.debug("Student id {} entered",studentId);
        Avatar avatar = avatarRepository.findByStudentId(studentId).orElseThrow(null);
        if (avatar == null) {
            logger.error("Avatar not found");
            throw new AvatarNotFoundException();
        }
        logger.info("Was invoked method for get Avatar Form Fs");
        try {
            byte[] data = Files.readAllBytes(Paths.get(avatar.getFilePath()));
            return Pair.of(data, avatar.getMediaType());
        } catch (IOException e) {
            throw new AvatarProcessingException();
        }
    }

    public List<AvatarDto> getAvatars(int page, int size) {
        logger.info("Was invoked method for get Avatar Form Fs");
        logger.debug("Pagination used: pages {}, size {}", page,size);
        return avatarRepository.findAll(PageRequest.of(page-1, size))
                .get().map(avatarMapper::toDto)
                .collect(Collectors.toList());
    }
}
