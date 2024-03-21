package com.hogwarts.school.mapper;

import com.hogwarts.school.controller.AvatarController;
import com.hogwarts.school.dto.AvatarDto;
import com.hogwarts.school.model.Avatar;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

@Component
public class AvatarMapper {

    @Value("${server.port}")
    private int port;


    public AvatarDto toDto(Avatar avatar) {
        AvatarDto avatarDto = new AvatarDto();
        avatarDto.setId(avatar.getId());
        avatarDto.setStudentName(avatar.getStudent().getName());
        avatarDto.setUrl(
                UriComponentsBuilder.newInstance()
                        .scheme("http")
                        .host("localhost")
                        .port(port)
                        .path(AvatarController.BASE_URI)
                        .pathSegment("from-db")
                        .queryParam("studentId",avatar.getStudent().getId())
                        .build()
                        .toString()
        );
        return avatarDto;
    }
}
