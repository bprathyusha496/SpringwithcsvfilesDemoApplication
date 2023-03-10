package com.rgt.csv.controller;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.HttpHeaders;

import com.opencsv.CSVWriter;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.rgt.csv.model.User;
import com.rgt.csv.service.UserService;

@Controller
public class UserController {
	
	 private UserService userService;

	    public UserController(UserService userService) {
	        this.userService = userService;
	    }

	 @GetMapping("/")
	 public String index() {
	        return "index";
	    }
	 @PostMapping("/upload-csv-file")
	    public String uploadCSVFile(@RequestParam("file") MultipartFile file, Model model) {
		 if(file.isEmpty()) {
			 model.addAttribute("message","please select a csv file to upload.");
			 model.addAttribute("status",false);
		 }else {
			// parse CSV file to create a list of `User` objects
			 try(Reader reader=new BufferedReader(new InputStreamReader(file.getInputStream()))){
				// create csv bean reader
	                CsvToBean<User> csvToBean = new CsvToBeanBuilder(reader)
	                        .withType(User.class)
	                        .withIgnoreLeadingWhiteSpace(true)
	                        .build();

	                // convert `CsvToBean` object to list of users
	                List<User> users = csvToBean.parse();

	                // TODO: save users in DB?

	                // save users list on model
	                model.addAttribute("users", users);
	                model.addAttribute("status", true);
				 
			 } catch (Exception ex) {
	                model.addAttribute("message", "An error occurred while processing the CSV file.");
	                model.addAttribute("status", false);
	            }
	        }

	        return "file-upload-status";
	    }
	 
	 @GetMapping("/export-users")
	    public void exportCSV(HttpServletResponse response) throws Exception {

	        //set file name and content type
	        String filename = "users.csv";

	        response.setContentType("text/csv");
	        response.setHeader(HttpHeaders.CONTENT_DISPOSITION,
	                "attachment; filename=\"" + filename + "\"");

	        //create a csv writer
	        StatefulBeanToCsv<User> writer = new StatefulBeanToCsvBuilder<User>(response.getWriter())
	                .withQuotechar(CSVWriter.NO_QUOTE_CHARACTER)
	                .withSeparator(CSVWriter.DEFAULT_SEPARATOR)
	                .withOrderedResults(false)
	                .build();

	        //write all users to csv file
	        writer.write(userService.listUsers());
	                
	    }
	 
	 }

