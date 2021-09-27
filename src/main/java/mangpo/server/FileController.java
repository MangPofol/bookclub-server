package mangpo.server;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
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


    @PutMapping("/upload-multiple-files")
    public List<String> uploadMultipleFiles(@RequestParam("data") MultipartFile[] files) throws IOException {
        List<String> result = new ArrayList<>();

        for (MultipartFile f : files) {
            String uploadResult = upload(f);
            result.add(uploadResult);
        }

        UploadResultListDto uploadResultURLDto = new UploadResultListDto();
        uploadResultURLDto.setImgLocations(result);

        return result;
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> delete(@RequestParam String imageLocation){
        String[] s = imageLocation.split("/");
        String fileName = s[s.length - 1];
        String dirName = "static";

        s3Service.deleteFile(fileName,dirName);

        return ResponseEntity.noContent().build();

    }

    @DeleteMapping("/delete-multiple-files")
    public ResponseEntity<?> deleteMultipleFiles(@RequestParam String[] imageLocations){
        String[] fileNames = Arrays.stream(imageLocations)
                .map(m -> m.split("/"))
                .map(m -> m[m.length - 1])
                .toArray(String[]::new);

        String dirName = "static";
        s3Service.deleteMultipleFiles(fileNames, dirName);

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
