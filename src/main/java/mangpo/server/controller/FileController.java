package mangpo.server.controller;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import mangpo.server.service.common.S3Service;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/files")
public class FileController {

    private final S3Service s3Service;

    @PutMapping("/upload")
    public String upload(@RequestParam("data") MultipartFile multipartFile) throws IOException {
        String result = s3Service.upload(multipartFile, "static");

        UploadResultDto uploadResultURLDto = new UploadResultDto();
        uploadResultURLDto.setImgLocation(result);

        return result;
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> delete(@RequestParam String imageLocation) {
        String[] s = imageLocation.split("/");
        String fileName = s[s.length - 1];
        String dirName = "static";

        s3Service.deleteFile(fileName, dirName);

        return ResponseEntity.noContent().build();

    }

    @Data
    static class UploadResultListDto {
        public List<String> imgLocations = new ArrayList<>();
    }

    @Data
    static class UploadResultDto {
        public String imgLocation;
    }
}
