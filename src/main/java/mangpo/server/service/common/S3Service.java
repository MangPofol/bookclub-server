package mangpo.server.service.common;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class S3Service {

    private final AmazonS3Client amazonS3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    public String upload(MultipartFile multipartFile, String dirName) throws IOException {
        File uploadFile = convert(multipartFile)
                .orElseThrow(() -> new IllegalArgumentException("MultipartFile -> File로 전환이 실패했습니다."));

        return upload(uploadFile, dirName);
    }

    private String upload(File uploadFile, String dirName) {
        UUID uuid = UUID.randomUUID();
        StringBuffer sb = new StringBuffer();

        String fileName = sb.append(dirName).append("/").append(uuid).append("_").append(uploadFile.getName()).toString();

        String uploadImageUrl = putS3(uploadFile, fileName);
        removeNewFile(uploadFile);
        return uploadImageUrl;
    }

    private String putS3(File uploadFile, String fileName) {
        amazonS3Client.putObject(new PutObjectRequest(bucket, fileName, uploadFile).withCannedAcl(CannedAccessControlList.PublicRead));
        return amazonS3Client.getUrl(bucket, fileName).toString();
    }

    private void removeNewFile(File targetFile) {
        if (targetFile.delete()) {
            log.info("파일이 삭제되었습니다.");
        } else {
            log.info("파일이 삭제되지 못했습니다.");
        }
    }

    public void deleteFile(String fileName, String dirName) {
        String fileDirAndName = buildFileDirAndName(dirName, fileName);
        amazonS3Client.deleteObject(bucket, fileDirAndName);
    }

    /*
        String fileNames[] = {
         "document/hello.txt",
         "document/pic.png"
    };
    */
    public void deleteMultipleFiles(String[] fileNames, String dirName) {
        String[] fileDirAndNames = Arrays.stream(fileNames)
                .map(m -> buildFileDirAndName(dirName, m))
                .toArray(String[]::new);

        DeleteObjectsRequest deleteObjectRequest = new DeleteObjectsRequest(bucket)
                .withKeys(fileDirAndNames);

        amazonS3Client.deleteObjects(deleteObjectRequest);
    }

    private String buildFileDirAndName(String dirName, String fileName) {
        StringBuffer sb = new StringBuffer();
        return sb.append(dirName).append("/").append(fileName).toString();
    }


    private Optional<File> convert(MultipartFile file) throws IOException {
        File convertFile = new File(file.getOriginalFilename());
        if (convertFile.createNewFile()) {
            try (FileOutputStream fos = new FileOutputStream(convertFile)) {
                fos.write(file.getBytes());
            }
            return Optional.of(convertFile);
        }

        return Optional.empty();
    }


    public InputStream getObjectInputStream(String fileName) {
        S3Object object = amazonS3Client.getObject(new GetObjectRequest(bucket, fileName));

        return object.getObjectContent();
    }
}