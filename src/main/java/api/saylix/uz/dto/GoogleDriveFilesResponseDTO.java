package api.saylix.uz.dto;

import lombok.Data;

@Data
public class GoogleDriveFilesResponseDTO {

    private String url;
    private long size;
    private String key;
    private String type;
    private String mimetype;

    public GoogleDriveFilesResponseDTO(String publicUrl, long size, String id,  String s, String contentType) {
        this.url = publicUrl;
        this.size = size;
        this.key = id;
        this.mimetype = contentType;
        this.type = s;
    }
}
