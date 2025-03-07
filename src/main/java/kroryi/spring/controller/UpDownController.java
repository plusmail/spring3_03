package kroryi.spring.controller;

import io.swagger.annotations.ApiOperation;
import kroryi.spring.dto.UploadFileDTO;
import kroryi.spring.dto.UploadResultDTO;
import lombok.extern.log4j.Log4j2;
import net.coobird.thumbnailator.Thumbnailator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@RestController
@Log4j2
public class UpDownController {

    @Value("${spring.servlet.multipart.location}")
    private String location;

    @ApiOperation(value = "Upload POST", notes = "POST 방식으로 파일 등록")
    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public List<UploadResultDTO> upload(UploadFileDTO uploadFileDTO) {
        log.info(uploadFileDTO);
        if (uploadFileDTO.getFiles() != null && !uploadFileDTO.getFiles().isEmpty()) {

            final List<UploadResultDTO> list = new ArrayList<>();


            uploadFileDTO.getFiles().forEach(file -> {
                log.info(file.getOriginalFilename());

                String uuid = UUID.randomUUID().toString();
                // savePath = "D:/upload/x1232342342342343_image01.jpg"
                Path savePath = Paths.get(location, uuid + "_" + file.getOriginalFilename());
                log.info(savePath);

                // 섬네일인 아닌지 구분
                boolean image = false;

                try {
                    file.transferTo(savePath);

                    if (Files.probeContentType(savePath).startsWith("image")) {
                        image = true;
                        File thumbFile = new File(location, "s_" + uuid + "_" + file.getOriginalFilename());
                        Thumbnailator.createThumbnail(savePath.toFile(), thumbFile, 200, 200);

                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

                list.add(UploadResultDTO.builder()
                        .uuid(uuid)
                        .fileName(file.getOriginalFilename())
                        .img(image)
                        .build());

            });

            return list;
        }

        return null;
    }


    @ApiOperation(value = "view 파일", notes = "GET방식으로 첨부파일 조회")
    @GetMapping("/view/{fileName}")
    public ResponseEntity<Resource> viewFileGET(@PathVariable String fileName) {

        Resource resource = new FileSystemResource(location + File.separator + fileName);
        String resourceName = resource.getFilename();

        HttpHeaders headers = new HttpHeaders();
        try {
            headers.add("Content-Type",
                    Files.probeContentType(resource.getFile().toPath()));

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // resource에 사진 이미지 내용이 있다. 브라우즈로 전송
        return ResponseEntity.ok().headers(headers).body(resource);
    }


    // 이방식은 보안에 취약하니 추후 보안관계 설정이 필수
    @ApiOperation(value = "첨부파일 삭제", notes = "DELETE 방식으로 파일 삭제")
    @DeleteMapping("/remove/{fileName}")
    public Map<String, Boolean> removeFile(@PathVariable String fileName) {
        Resource resource = new FileSystemResource(location + File.separator + fileName);
        String resourceName = resource.getFilename();
        Map<String, Boolean> resultMap = new HashMap<>();
        boolean removed = false;

        try {
            String contentType = Files.probeContentType(resource.getFile().toPath());
            removed = resource.getFile().delete();

            if (contentType.startsWith("image")) {
                File thumbFile = new File(location + File.separator + "s_" + fileName);
                thumbFile.delete();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        resultMap.put("result", removed);

        return resultMap;
    }

}
