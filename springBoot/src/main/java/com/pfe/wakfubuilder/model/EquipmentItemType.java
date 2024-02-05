package com.pfe.wakfubuilder.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonProperty;


class EquipmentItemTypeDefinition {
    public int id;
    public String parentId;
    public String[] equipmentPositions;
    public String[] equipmentDisabledPositions;
    public boolean isRecyclable;
    public boolean isVisibleInAnimation;
}


@Document(collection = "equipmentItemTypes")
public class EquipmentItemType {
    
    @Id
    @JsonProperty("id")
    private long id;
    private EquipmentItemTypeDefinition definition;
    private InfoTranslate title;


    /* Constructors */
    public EquipmentItemType() {}

    public EquipmentItemType(EquipmentItemTypeDefinition definition, InfoTranslate title) {
        super();
        this.definition = definition;
        this.title = title;
    }

    /* Getters */
    public long getId() {
        return id;
    }

    public EquipmentItemTypeDefinition getDefinition() {
        return definition;
    }
    
    public InfoTranslate getTitle() {
        return title;
    }

    /* Setters */
    public void setDefinition(EquipmentItemTypeDefinition definition) {
        this.definition = definition;
    }

    public void setTitle(InfoTranslate title) {
        this.title = title;
    }

    

}
