package com.pfe.wakfubuilder.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.pfe.wakfubuilder.model.Action;
import com.pfe.wakfubuilder.service.ActionService;

@RestController
public class ActionController {
    
    @Autowired
    private ActionService actionService;

    @GetMapping("/actions")
    public List<Action> getActions() {
        return actionService.getActions();
    }

    @GetMapping("/action/{id}")
    public Action getAction(@PathVariable String id) {
        return actionService.getAction(id);
    }
}
