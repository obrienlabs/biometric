package com.obrienlabs.gps.business;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.obrienlabs.gps.business.entity.Record;
import com.obrienlabs.gps.business.service.ApplicationService;
@RequestMapping("/records")
@Controller
public class ContactController {
    private final Logger logger = LoggerFactory.getLogger(ContactController.class);
    private ApplicationService service;
    
    @RequestMapping(method = RequestMethod.GET)
    public String list(Model uiModel) {
    	logger.info("Listing Records");

    	Record record = service.latest("201503300");
    	List<Record> records = new ArrayList<>();
    	records.add(record);
        uiModel.addAttribute("records", records);
        logger.info("No. of records: " + records.size());
        return "records/list";
    }
    
    @Autowired
    public void setContactService(ApplicationService contactService) {
        this.service = contactService;
    }
}
