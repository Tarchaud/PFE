package com.pfe.wakfubuilder.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import com.pfe.wakfubuilder.model.Action;

@Service
public class ActionService {
    
    private List<Action> actions;

    public ActionService() {
        this.actions = new ArrayList<>();
        loadActionsFromJson();
    }

    private void loadActionsFromJson() {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            ClassPathResource resource = new ClassPathResource("db/actions.json");
            Action[] actionsArray = objectMapper.readValue(resource.getInputStream(), Action[].class);
            actions.addAll(Arrays.asList(actionsArray));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<Action> getActions() {
        return actions;
    }

    public Action getAction(long id) {
        return actions.stream().filter(action -> action.getId() == id).findFirst().orElse(null);
    }
}
