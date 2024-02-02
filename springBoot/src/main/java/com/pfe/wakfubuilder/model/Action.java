package com.pfe.wakfubuilder.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonProperty;

class definition {
    public int id;
    public String effect;
}

class description {
    public String fr;
    public String en;
    public String es;
    public String pt;
}

@Document(collection = "action")
public class Action {

    @Id
    @JsonProperty("id")
    private long id;
    private definition definition;
    private InfoTranslate description;

    /* Constructors */
    public Action() {}

    public Action(definition definition, InfoTranslate description) {
        super();
        this.definition = definition;
        this.description = description;
    }
    
    
    /* Getters */
    public long getId() {
        return id;
    }

    public definition getDefinition() {
        return definition;
    }
    
    public InfoTranslate getDescription() {
        return description;
    }

    /* Setters */
    public void setDefinition(definition definition) {
        this.definition = definition;
    }


    public void setDescription(InfoTranslate description) {
        this.description = description;
    }


    
    
}
