package com.pfe.wakfubuilder.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonProperty;


class definition {
    public int id;
}

@Document(collection = "states")
public class States {
    
    @Id
    @JsonProperty("id")
    private String id;
    private definition definition;
    private InfoTranslate title;


    /* Constructors */
    public States() {}

    public States(definition definition, InfoTranslate title) {
        super();
        this.definition = definition;
        this.title = title;
    }

    /* Getters */
    public String getId() {
        return id;
    }

    public definition getDefinition() {
        return definition;
    }

    public InfoTranslate getTitle() {
        return title;
    }

    /* Setters */
    public void setDefinition(definition definition) {
        this.definition = definition;
    }

    public void setTitle(InfoTranslate title) {
        this.title = title;
    }

}
