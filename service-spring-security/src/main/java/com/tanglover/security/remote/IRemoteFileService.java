package com.tanglover.security.remote;

import com.tanglover.security.config.FeignSupportConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author TangXu
 * @create 2020-04-28 13:52
 * @description:
 */
@FeignClient(value = "SERVICE-MALL", url = "http://localhost:7074", configuration = FeignSupportConfig.class)
public interface IRemoteFileService {

    @RequestMapping(value = "upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    String fileUpload(@RequestPart("file") MultipartFile file);
}