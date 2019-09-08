package com.neuedu.controller.backend;
import com.neuedu.common.ResponseCode;
import com.neuedu.common.ServerResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Controller
@RequestMapping("/manage/")
public class UploadController {
    @GetMapping(value = "/upload")
    public String upload(){
        return "upload";
    }
    @PostMapping(value = "/upload")
   @ResponseBody
    public String upload(@RequestParam("uploadfile")MultipartFile uploadfile){
        if (uploadfile==null || uploadfile.getOriginalFilename().equals("")){
           // return ServerResponse.serverResponseBySuccess(ResponseCode.ERROR,"图片必须上传");
        }
        //获取上传图片的名称
        String oldFileName=uploadfile.getOriginalFilename();
        //获取文件扩展名
        String extendName=oldFileName.substring(oldFileName.lastIndexOf('.'));

        //生成新的文件名
        String newFilename= UUID.randomUUID().toString()+extendName;
        File mkdir=new File("f:/upload");
        if (!mkdir.exists()){
            mkdir.mkdir();
        }
        File newFile=new File(mkdir,newFilename);
        try {
            uploadfile.transferTo(newFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return newFilename;
    }
}
