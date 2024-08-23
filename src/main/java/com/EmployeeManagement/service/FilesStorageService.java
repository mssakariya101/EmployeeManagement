package com.EmployeeManagement.service;


import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface FilesStorageService {
  void save(MultipartFile file,String fileName) throws IOException;
  Resource load(String filename);
  boolean delete(String filename);
}
