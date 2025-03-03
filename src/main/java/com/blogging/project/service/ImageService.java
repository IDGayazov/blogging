package com.blogging.project.service;

import com.blogging.project.exceptions.FileFetchException;
import com.blogging.project.exceptions.FileUploadException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class ImageService {

    @Value("${file.upload-dir}")
    String fileDir;

    public String handleFileUpload(MultipartFile file){
        String fileName = file.getOriginalFilename();
        if(fileName == null){
            throw new IllegalArgumentException();
        }

        Path uploadingPath = Path.of(fileDir);
        try {
            if(!Files.exists(uploadingPath)){
                Files.createDirectories(uploadingPath);
            }
            String uploadFilePath = uploadingPath.resolve(hashFileName(fileName)).toString();
            file.transferTo(new File(uploadFilePath));
            log.info("Upload file into: {}", uploadFilePath);
            return uploadFilePath;
        }catch(IOException e) {
            log.error("Can't upload file: {}", fileName, e);
            throw new FileUploadException("Can't upload file");
        }
    }

    public Resource handleFileFetch(String fileName){
        try {
            Path filePath = Paths.get(fileDir).resolve(fileName).normalize();
            Resource resource = new UrlResource(filePath.toUri());
            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                log.error("Can't find file {}", fileName);
                throw new FileNotFoundException();
            }
        } catch (IOException e) {
            log.error("Can't fetch file {}", fileName, e);
            throw new FileFetchException(String.format("Can't fetch file: %s", fileName));
        }
    }

    private static String hashFileName(String fileName){
        return UUID.randomUUID() + "_" + fileName;
    }

}
