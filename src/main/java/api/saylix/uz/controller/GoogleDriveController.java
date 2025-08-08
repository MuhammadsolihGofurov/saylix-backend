package api.saylix.uz.controller;

import api.saylix.uz.dto.GoogleDriveFilesResponseDTO;
import api.saylix.uz.services.GoogleDriveService;
import api.saylix.uz.services.GoogleDriveService.UploadedMediaResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/attach")
public class GoogleDriveController {

    private final GoogleDriveService googleDriveService;

    public GoogleDriveController(GoogleDriveService googleDriveService) {
        this.googleDriveService = googleDriveService;
    }

    @PostMapping("/upload")
    public ResponseEntity<?> uploadFile(
            @RequestParam("file") MultipartFile file
    ) {
        try {
            GoogleDriveFilesResponseDTO response = googleDriveService.uploadMedia(file);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Upload failed: " + e.getMessage());
        }
    }

    @DeleteMapping("/delete/{fileId}")
    public ResponseEntity<?> deleteFile(@PathVariable String fileId) {
        try {
            googleDriveService.deleteMedia(fileId);
            return ResponseEntity.ok("File deleted successfully: " + fileId);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Delete failed: " + e.getMessage());
        }
    }
}
