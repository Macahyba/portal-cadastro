package com.sony.engineering.portalcadastro.controller;

import com.sony.engineering.portalcadastro.model.Status;
import com.sony.engineering.portalcadastro.service.StatusService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class StatusController {
    private Logger logger = LoggerFactory.getLogger(StatusController.class);

    private StatusService statusService;

    @Autowired
    public StatusController(StatusService statusService) {
        this.statusService = statusService;
    }

    @GetMapping(value = "status")
    public ResponseEntity<List<Status>> getAll (){
        return new ResponseEntity<>(statusService.findAllByOrderByIdAsc(), HttpStatus.OK);
    }

    public ResponseEntity<Status> setStatus(@RequestBody Status status){

        try {
            return new ResponseEntity<>(statusService.save(status), HttpStatus.OK);
        } catch (RuntimeException e) {
            logger.error("Error on creating status: " + e);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
