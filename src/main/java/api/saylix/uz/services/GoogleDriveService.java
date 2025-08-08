package api.saylix.uz.services;

import api.saylix.uz.dto.GoogleDriveFilesResponseDTO;
import com.google.api.client.http.AbstractInputStreamContent;
import com.google.api.client.http.InputStreamContent;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.Permission;
import com.google.auth.http.HttpCredentialsAdapter;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.auth.oauth2.ServiceAccountCredentials;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;

@Service
public class GoogleDriveService {

    private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
    private final String APPLICATION_NAME = "Saylix Google Drive Upload";
    private final String FOLDER_ID = "1AkL4bhyRBoEaOe-nv3Pf1PY_G86QMv1O";

    private Drive getDriveService() throws IOException {
        InputStream credentialsStream = new ClassPathResource("googleDriveCredentials.json").getInputStream();

        GoogleCredentials credentials = ServiceAccountCredentials.fromStream(credentialsStream)
                .createScoped(Collections.singleton("https://www.googleapis.com/auth/drive"));

        return new Drive.Builder(new NetHttpTransport(), JSON_FACTORY, new HttpCredentialsAdapter(credentials))
                .setApplicationName(APPLICATION_NAME)
                .build();
    }

    public GoogleDriveFilesResponseDTO uploadMedia(MultipartFile multipartFile) throws IOException {
        if (multipartFile.getContentType() != null && multipartFile.getContentType().contains("video")
                && multipartFile.getSize() > 100 * 1024 * 1024) {
            throw new IllegalArgumentException("Please upload a file size not more than 100MB");
        }

        if (multipartFile.getContentType() != null && multipartFile.getContentType().contains("image")
                && multipartFile.getSize() > 5 * 1024 * 1024) {
            throw new IllegalArgumentException("Please upload a file size not more than 5MB");
        }

        Drive driveService = getDriveService();

        String fileName = System.currentTimeMillis() + "-" + multipartFile.getOriginalFilename();

        File fileMetadata = new File();
        fileMetadata.setName(fileName);
        fileMetadata.setParents(Collections.singletonList(FOLDER_ID));

        AbstractInputStreamContent uploadStreamContent =
                new InputStreamContent(multipartFile.getContentType(), multipartFile.getInputStream());

        File uploadedFile = driveService.files()
                .create(fileMetadata, uploadStreamContent)
                .setFields("id")
                .execute();

        // Make public
        Permission permission = new Permission()
                .setType("anyone")
                .setRole("reader");
        driveService.permissions().create(uploadedFile.getId(), permission).execute();

        String publicUrl = "https://drive.google.com/uc?id=" + uploadedFile.getId();

        return new GoogleDriveFilesResponseDTO(publicUrl, multipartFile.getSize(), uploadedFile.getId(),
                multipartFile.getContentType().split("/")[0], multipartFile.getContentType());
    }

    public void deleteMedia(String fileId) throws IOException {
        Drive driveService = getDriveService();
        driveService.files().delete(fileId).execute();
    }

    // DTO javobi
    public static class UploadedMediaResponse {
        private String url;
        private long size;
        private String key;
        private String type;
        private String mimetype;

        public UploadedMediaResponse(String url, long size, String key, String type, String mimetype) {
            this.url = url;
            this.size = size;
            this.key = key;
            this.type = type;
            this.mimetype = mimetype;
        }

        // getters va setters
        public String getUrl() { return url; }
        public long getSize() { return size; }
        public String getKey() { return key; }
        public String getType() { return type; }
        public String getMimetype() { return mimetype; }
    }
}
