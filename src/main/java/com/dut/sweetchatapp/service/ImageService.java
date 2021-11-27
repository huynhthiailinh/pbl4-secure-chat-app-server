package com.dut.sweetchatapp.service;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Objects;

import static com.dut.sweetchatapp.constant.DefaultParam.AVATAR;
import static com.dut.sweetchatapp.constant.DefaultPath.createAvatarImageFolder;
import static com.dut.sweetchatapp.constant.DefaultPath.createRootFolder;

@Service
public class ImageService {
    @Value("${storage.path}")
    private String storagePath;

    public String getImageDirectory(String type, int id) {
        String imageDir = "";
        switch (type) {
            case AVATAR:
                imageDir = createAvatarImageFolder(storagePath) + File.separator + id;
                break;
            default:
                break;
        }
        return imageDir;
    }

    public String uploadToLocalFileSystem(MultipartFile multipartFile, String type, int id) {
        String fileName = StringUtils.cleanPath(Objects.requireNonNull(multipartFile.getOriginalFilename()));
        Path storageDirectory = Paths.get(createRootFolder(storagePath));
        String imageDir = "";
        Path imageDirPath = null;

        if(!Files.exists(storageDirectory)) {
            try {
                Files.createDirectories(storageDirectory);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            imageDir = getImageDirectory(type, id);

            imageDirPath = Paths.get(imageDir);

            if(!Files.exists(imageDirPath)) {
                try {
                    Files.createDirectories(imageDirPath);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        Path destination = Paths.get(Objects.requireNonNull(imageDirPath).toString() + File.separator + fileName);

        try {
            Files.copy(multipartFile.getInputStream(), destination, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return type + "/" + id + "/" + fileName;
    }

    public byte[] getImageWithMediaType(String imageName, int id, String type) throws IOException {
        Path destination = null;

        String imageDir = "";

        imageDir = getImageDirectory(type, id);

        destination = Paths.get(imageDir + File.separator + imageName);

        return IOUtils.toByteArray(destination.toUri());
    }


}
