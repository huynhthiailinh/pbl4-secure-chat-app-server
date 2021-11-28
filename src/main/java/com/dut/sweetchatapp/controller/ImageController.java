package com.dut.sweetchatapp.controller;

import com.dut.sweetchatapp.service.ImageService;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

import static com.dut.sweetchatapp.constant.DefaultPath.PUBLIC_IMAGE_PATH;

@AllArgsConstructor
@RestController
@RequestMapping(PUBLIC_IMAGE_PATH)
@CrossOrigin
public class ImageController {
    public final ImageService imageService;

    @GetMapping(
            value = "getImage/{type}/{id}/{imageName:.+}",
            produces = {MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_GIF_VALUE, MediaType.IMAGE_PNG_VALUE}
    )
    public @ResponseBody
    byte[] getImageWithMediaType(@PathVariable(name = "type") String type,
                                 @PathVariable(name = "id") int id,
                                 @PathVariable(name = "imageName") String fileName) throws IOException {
        return this.imageService.getImageWithMediaType(fileName, id, type);
    }
}
