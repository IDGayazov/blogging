package com.blogging.project.service;

import com.blogging.project.exceptions.FileFetchException;
import com.blogging.project.exceptions.FileUploadException;
import com.ibm.icu.text.Transliterator;
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

import static java.lang.String.format;

@Service
@Slf4j
@RequiredArgsConstructor
public class ImageService {

    private final static String FILE_FETCH_ERROR_TEMPLATE = "Can't fetch file: %s";
    private final static String FILE_UPLOAD_ERROR_TEMPLATE = "Can't upload file: %s";
    private final static String FILENAME_ERROR = "Filename can't be null";
    private final static String FILENAME_NOT_FOUND_ERROR_TEMPLATE = "Filename not found %s";

    @Value("${file.upload-dir}")
    String fileDir;

    public String handleFileUpload(MultipartFile file){
        try {
            String changedName = saveFileAndGetName(file);
            log.info("Upload file: {}", changedName);
            return changedName;
        }catch(IOException e) {
            throw new FileUploadException(format(FILE_UPLOAD_ERROR_TEMPLATE, file.getOriginalFilename()));
        }
    }

    public Resource handleFileFetch(String fileName){
        try {
            return getFileResource(fileName);
        } catch (IOException e) {
            throw new FileFetchException(format(FILE_FETCH_ERROR_TEMPLATE, fileName));
        }
    }

    private String saveFileAndGetName(MultipartFile file) throws IOException{
        String fileName = file.getOriginalFilename();
        checkFileNameNotNullElseThrowException(fileName);

        Path uploadingPath = Path.of(fileDir);    
        checkDirExistsElseCreate(uploadingPath);

        String changedName = hashFileName(fileName);
        String uploadFilePath = uploadingPath.resolve(changedName).toString();
        file.transferTo(new File(uploadFilePath));

        return changedName;
    }

    private Resource getFileResource(String fileName)
        throws IOException, FileNotFoundException
    {
        Path filePath = Paths.get(fileDir).resolve(fileName).normalize();
        Resource resource = new UrlResource(filePath.toUri());
        if (resource.exists() || resource.isReadable()) {
            log.info("Sending file: {}", fileName);
            return resource;
        } else {
            throw new FileNotFoundException(format(FILENAME_NOT_FOUND_ERROR_TEMPLATE, fileName));
        }
    }

    private static String hashFileName(String fileName){
        String CYRILLIC_TO_LATIN = "Russian-Latin/BGN";
        Transliterator toLatinTrans = Transliterator.getInstance(CYRILLIC_TO_LATIN);
        String result = toLatinTrans.transliterate(fileName);
        return UUID.randomUUID() + "_" + result;
    }

    private static void checkFileNameNotNullElseThrowException(String fileName){
        if(fileName == null){
            throw new IllegalArgumentException(FILENAME_ERROR);
        }
    }

    private static void checkDirExistsElseCreate(Path path) throws IOException{
        if(!Files.exists(path)){
            Files.createDirectories(path);
        }
    }

}
