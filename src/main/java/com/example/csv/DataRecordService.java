package com.example.csv;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.opencsv.CSVReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
public class DataRecordService {


    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private CustomerRepository customerRepository;

    private final ExecutorService executorService = Executors.newFixedThreadPool(10);

    public void saveAccountsFromCsv(MultipartFile file) throws IOException {
        List<String> errorList = new ArrayList<>();
        try (CSVReader csvReader = new CSVReader(new InputStreamReader(file.getInputStream()))) {
            String[] nextLine;
            csvReader.readNext();
            while ((nextLine = csvReader.readNext()) != null) {
                try {

                    String accountNumber = nextLine[0];
                    int accountType = Integer.parseInt(nextLine[1]);
                    Long customerID = Long.parseLong(nextLine[2]);
                    double accountLimit = Double.parseDouble(nextLine[3]);
                    LocalDate openDate = LocalDate.parse(nextLine[4]);
                    double balance = Double.parseDouble(nextLine[5]);


                    String encryptedAccountNumber = EncryptionUtil.encrypt(accountNumber);
                    String encryptedBalance = EncryptionUtil.encrypt(String.valueOf(balance));


                    executorService.submit(() -> {
                        try {
                            Account account = new Account();
                            account.setAccountNumber(encryptedAccountNumber);
                            account.setAccountType(accountType);
                            account.setCustomerID(customerID);
                            account.setAccountLimit(accountLimit);
                            account.setOpenDate(openDate);
                            account.setBalance(encryptedBalance);
                            accountRepository.save(account);
                        } catch (Exception e) {
                            String errorMessage = "Error saving account: " + e.getMessage();
                            System.err.println(errorMessage);
                            errorList.add(errorMessage);
                        }
                    });
                } catch (Exception e) {
                    String errorMessage = "Error reading account data: " + e.getMessage();
                    System.err.println(errorMessage);
                    errorList.add(errorMessage);
                }
            }
        } catch (Exception e) {
            String errorMessage = "Error reading CSV file: " + e.getMessage();
            System.err.println(errorMessage);
            errorList.add(errorMessage);
        }


        if (!errorList.isEmpty()) {
            saveErrorsToJson(errorList);
        }
    }

    public void saveCustomersFromCsv(MultipartFile file) throws IOException {
        final List<String> errorLines = new ArrayList<>();
        try (CSVReader csvReader = new CSVReader(new InputStreamReader(file.getInputStream()))) {
            String[] nextLine;
            csvReader.readNext();
            while ((nextLine = csvReader.readNext()) != null) {
                try {
                    String customerName = nextLine[0];
                    String customerSurname = nextLine[1];
                    String customerAddress = nextLine[2];
                    String zipCode = nextLine[3];
                    String nationalID = nextLine[4];
                    LocalDate birthDate = LocalDate.parse(nextLine[5]);

                    executorService.submit(() -> {
                        try {
                            Customer customer = new Customer();
                            customer.setCustomerName(customerName);
                            customer.setCustomerSurname(customerSurname);
                            customer.setCustomerAddress(customerAddress);
                            customer.setZipCode(zipCode);
                            customer.setNationalID(nationalID);
                            customer.setBirthDate(birthDate);
                            customerRepository.save(customer);
                        } catch (Exception e) {
                            System.err.println("Error saving customer: " + e.getMessage());
                        }
                    });
                } catch (Exception e) {
                    errorLines.add(String.join(",", nextLine) + " | Error: " + e.getMessage());
                }
            }
        } catch (Exception e) {
            System.err.println("Error reading CSV file: " + e.getMessage());
        }

        saveErrorsToJson(errorLines);
    }

    private void saveErrorsToJson(List<String> errors) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            objectMapper.writeValue(new File("Error.json"), errors);
        } catch (IOException e) {
            System.err.println("Error saving error messages to JSON: " + e.getMessage());
        }
    }

}


