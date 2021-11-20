package com.hg.hyy.controller;

import com.hg.hyy.exception.StorageFileNotFoundException;
import com.hg.hyy.interfaces.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.stream.Collectors;

@Controller
@RequestMapping("/file")
public class UploadController {

  private final StorageService storageService;

  @Autowired
  public UploadController(StorageService storageService) {
    this.storageService = storageService;
  }

  @GetMapping("/")
  public String listUploadedFiles(Model model) {

    model.addAttribute(
            "files",
            storageService
                    .loadAll()
                    .map(
                            path ->
                                    MvcUriComponentsBuilder.fromMethodName(
                                                    UploadController.class, "serveFile", path.getFileName().toString())
                                            .build()
                                            .toUri()
                                            .toString())
                    .collect(Collectors.toList()));

    return "uploadForm";
  }

  @GetMapping("/files/{filename:.+}")
  @ResponseBody
  public ResponseEntity<Resource> serveFile(@PathVariable String filename) {

    Resource file = storageService.loadAsResource(filename);
    return ResponseEntity.ok()
            .header(
                    HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"")
            .body(file);
  }

  @PostMapping("/upload")
  public String handleFileUpload(
          @RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes) {

    storageService.store(file);
    redirectAttributes.addFlashAttribute(
            "message", "You successfully uploaded " + file.getOriginalFilename() + "!");

    return "redirect:/file/";
  }

  @ExceptionHandler(StorageFileNotFoundException.class)
  public ResponseEntity<?> handleStorageFileNotFound(StorageFileNotFoundException exc) {
    return ResponseEntity.notFound().build();
  }
}
