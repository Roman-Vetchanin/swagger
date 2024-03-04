package com.hogwarts.school.service;

import com.hogwarts.school.exception.AvatarNotFoundException;
import com.hogwarts.school.exception.AvatarProcessingException;
import com.hogwarts.school.exception.IncorrectNameException;
import com.hogwarts.school.model.Avatar;
import com.hogwarts.school.model.Student;

import com.hogwarts.school.repositories.AvatarRepository;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static java.nio.file.StandardOpenOption.CREATE_NEW;


@Service
public class AvatarService {
    @Value("${path.to.avatars.folder}")
    private String avatarsDir;
    private final AvatarRepository avatarRepository;
    private final StudentService studentService;

    public AvatarService(AvatarRepository avatarRepository, StudentService studentService) {
        this.avatarRepository = avatarRepository;
        this.studentService = studentService;
    }

    //не увидел в разборе про @Transactional, эта аннотация нужна или нет?
    @Transactional
    public void uploadAvatar(Long studentId, MultipartFile avatarFile) throws IOException {
        //можно ли инжектить другой сервис, для пробрасывания исключения в этот сервис?
        Student student = studentService.findStudent(studentId);
        Path filePath = Path.of(avatarsDir, student + "." + getExtensions(avatarFile.getOriginalFilename()));
        try {
            Files.createDirectories(filePath.getParent());
            Files.deleteIfExists(filePath);
        } catch (IOException e) {
            throw new AvatarProcessingException();
        }
        //смотрел разбор домашки, в голове каша, этот способ не подходит?
        try (InputStream is = avatarFile.getInputStream();
             OutputStream os = Files.newOutputStream(filePath, CREATE_NEW);
             BufferedInputStream bis = new BufferedInputStream(is, 1024);
             BufferedOutputStream bos = new BufferedOutputStream(os, 1024);
        ) {
            bis.transferTo(bos);
        }
        Avatar avatar = findAvatar(studentId);
            avatar.setStudent(student);
            avatar.setFilePath(filePath.toString());
            avatar.setFileSize(avatarFile.getSize());
            avatar.setMediaType(avatarFile.getContentType());
            avatar.setData(avatarFile.getBytes());
        avatarRepository.save(avatar);
    }

    //здесь может быть ошибка, возможно лучше переделать на orElseThrow(()-> new AvatarNotFoundException())
    public Avatar findAvatar(Long avatarId) {
        return avatarRepository.findByStudentId(avatarId).orElse(new Avatar());
    }

    private String getExtensions(String fileName) {
        if (fileName == null) {
            throw new IncorrectNameException();
        }
        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }
@Transactional
    public Pair<byte[], String> getAvatarFromDb(Long studentId) {
        Avatar avatar = avatarRepository.findByStudentId(studentId).orElseThrow(AvatarNotFoundException::new);
        return Pair.of(avatar.getData(), avatar.getMediaType());
    }
@Transactional
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
