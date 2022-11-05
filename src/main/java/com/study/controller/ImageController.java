package com.study.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.study.entity.Image;
import com.study.entity.User;
import com.study.mapper.ImageMapper;
import com.study.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.*;
import java.util.List;

@RestController
@RequestMapping("/image")
public class ImageController {
    @Autowired
    private ImageMapper imageMapper;
    String filepath ="http://121.4.170.191:8181/img/";
//String filepath ="http://localhost:8181/img/";
    @RequestMapping("/addImage")
    public String addImage(@RequestParam(name = "imageData", required = false) MultipartFile file, @RequestParam(name = "imageName", required = false) String author ){
        Image image = new Image();
        if (!file.isEmpty()) {
            try {
                //图片命名
                String fileName=file.getOriginalFilename();
                String filePath="/www/wwwroot/yuan/image";
//                String filePath="D:\\test\\image";
                File newFile = new File(filePath,fileName);
                    String url=filepath+fileName;
                    image.setAuthor(author);
                    image.setUrl(url);
                System.out.println(image);
                imageMapper.insert(image);
                if (!newFile.exists()) {
                    newFile.createNewFile();
                    image.setUrl("/img/"+fileName);
                }
                BufferedOutputStream out = new BufferedOutputStream(
                        new FileOutputStream(newFile));
                out.write(file.getBytes());
                out.flush();
                out.close();
                return "true";
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                return "false！";
            } catch (IOException e) {
                e.printStackTrace();
                return "false！";
            }
        }
        return "图片上传失败！";
    }
    @RequestMapping("/findAllByUrl")
    public List<Image> findAllByUrl(@RequestBody Image image){
        System.out.println(image);
        QueryWrapper<Image> wrapper = new QueryWrapper<>();
        wrapper.eq("author",image.getAuthor());
        List<Image> list = imageMapper.selectList(wrapper);
        return list;
    }
    @RequestMapping("/deleteImage")
    public void deleteImage(@RequestBody Image image){
        String url1=image.getUrl();
        System.out.println(url1);
        String url2=url1.substring(29);
        String url="/www/wwwroot/yuan/image/"+url2;
//        String url="D:\\test\\image\\"+url2;
        System.out.println(url);
        File file= new File(url);
        file.deleteOnExit();
        QueryWrapper<Image> wrapper = new QueryWrapper<>();
        wrapper.eq("author",image.getAuthor());
        wrapper.eq("url",image.getUrl());
        imageMapper.delete(wrapper);
    }

}