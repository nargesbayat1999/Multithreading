package com.example.csv;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/csv")
public class CsvController {

    @Autowired
    private DataRecordService dataRecordService;

    @PostMapping("/upload-accounts")
    public String uploadAccounts(@RequestParam("file") MultipartFile file) {
        try {
            dataRecordService.saveAccountsFromCsv(file);
            return "Accounts uploaded successfully!";
        } catch (IOException e) {
            e.printStackTrace();
            return "Failed to upload accounts: " + e.getMessage();
        }
    }

    @PostMapping("/upload-customers")
    public String uploadCustomers(@RequestParam("file") MultipartFile file) {
        try {
            dataRecordService.saveCustomersFromCsv(file);
            return "Customers uploaded successfully!";
        } catch (IOException e) {
            e.printStackTrace();
            return "Failed to upload customers: " + e.getMessage();
        }
    }
}
