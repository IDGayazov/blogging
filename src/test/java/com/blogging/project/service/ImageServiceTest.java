package com.blogging.project.service;

import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import com.blogging.project.mock.MockMultipartFile;
import com.blogging.project.exceptions.FileUploadException;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.core.io.Resource;
import java.io.FileNotFoundException;
import com.blogging.project.exceptions.FileFetchException;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.doThrow;

@ExtendWith(MockitoExtension.class)
public class ImageServiceTest{
	
	@TempDir
	Path uploadingDir;

	private ImageService imageService;

	@BeforeEach
	public void setUp(){
		imageService = new ImageService();
		ReflectionTestUtils.setField(imageService, "fileDir", uploadingDir.toString());
	}

	@Nested
	class FileUploadTest{

		private MockMultipartFile mockFile;

		@BeforeEach
		public void setUp(){
			byte[] content = "Hello, World!".getBytes();
			mockFile = new MockMultipartFile(
				"file",
		        "file.txt",
		        "text/plain",
		        content
			);
		}

		@Test
		void testSuccess(){
			String uploadedFileName = imageService.handleFileUpload(mockFile);
			assertTrue(Files.exists(Paths.get(uploadingDir.toString()).resolve(uploadedFileName)));
		}


		@Test
		void throwFileUploadException() throws IOException {
			MockMultipartFile mockMultipartFile = Mockito.mock(MockMultipartFile.class);

			when(mockMultipartFile.getOriginalFilename()).thenReturn("file.txt");
			doThrow(new IOException()).when(mockMultipartFile).transferTo(any(File.class));

			assertThrows(FileUploadException.class, () -> imageService.handleFileUpload(mockMultipartFile));
		}
	}

	@Nested
	class FileFetchTest{

        @Test
		void testSuccess() throws IOException{
			byte[] content = "Hello, World!".getBytes();
            MockMultipartFile mockFile = new MockMultipartFile(
                    "file",
                    "file.txt",
                    "text/plain",
                    content
            );
			mockFile.transferTo(uploadingDir.resolve("file.txt").toFile());

			Resource resource = imageService.handleFileFetch("file.txt");
			assertTrue(resource.exists());
		}


		@Test
		void throwFileNotFoundException() {
			FileFetchException exception = assertThrows(FileFetchException.class,
					() -> imageService.handleFileFetch("file.txt")
			);

			assertEquals("Can't fetch file: file.txt", exception.getMessage());
            assertInstanceOf(FileNotFoundException.class, exception.getCause());
		}
	}

}