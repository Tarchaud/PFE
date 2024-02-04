package com.pfe.wakfubuilder.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.pfe.wakfubuilder.model.Action;
import com.pfe.wakfubuilder.repository.ActionRepository;

@Service
public class ActionService {
    
    private final ActionRepository actionRepository;

    public ActionService(ActionRepository actionRepository) {
        this.actionRepository = actionRepository;
    }

    public List<Action> getActions() {
        return actionRepository.findAll();
    }

    public Action getAction(long id) {
        Optional<Action> optionalAction = actionRepository.findById(id);
        return optionalAction.orElse(null);
    }
    
}
