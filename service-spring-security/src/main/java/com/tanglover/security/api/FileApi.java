package com.tanglover.security.api;

import com.tanglover.security.remote.IRemoteFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @author TangXu
 * @create 2020-04-28 13:49
 * @description:
 */
@RestController
public class FileApi extends BaseApi {

    @Autowired
    IRemoteFileService fileService;

    @PostMapping("/upload")
    public Map upload(@RequestPart(value = "file") MultipartFile file) {
        String s = fileService.fileUpload(file);
        return returnSuccess(s);
    }
}