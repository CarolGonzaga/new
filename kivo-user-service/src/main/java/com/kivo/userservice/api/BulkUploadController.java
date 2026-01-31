package com.kivo.userservice.api;

import com.kivo.userservice.api.dto.CreateUserRequest;
import com.kivo.userservice.service.UserService;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/users/bulk")
public class BulkUploadController {

    private final UserService userService;

    public BulkUploadController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('ADMIN')")
    public List<Long> upload(@RequestPart("file") MultipartFile file) throws Exception {
        List<Long> created = new ArrayList<>();
        var formatter = new org.apache.poi.ss.usermodel.DataFormatter();

        try (InputStream in = file.getInputStream(); var wb = WorkbookFactory.create(in)) {
            var sheet = wb.getSheetAt(0);

            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                var row = sheet.getRow(i);
                if (row == null) continue;

                String email = row.getCell(0) != null ? formatter.formatCellValue(row.getCell(0)) : null;
                String name = row.getCell(1) != null ? formatter.formatCellValue(row.getCell(1)) : null;
                String password = row.getCell(2) != null ? formatter.formatCellValue(row.getCell(2)) : null;

                if (email == null || name == null || password == null) continue;

                email = email.trim();
                name = name.trim();
                password = password.trim();

                if (email.isBlank() || name.isBlank() || password.isBlank()) continue;

                try {
                    var res = userService.create(new CreateUserRequest(email, name, password));
                    created.add(res.id());
                } catch (IllegalArgumentException ex) {
                }
            }
        }

        return created;
    }
}
