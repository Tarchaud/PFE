package com.pfe.wakfubuilder.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonProperty;

class DefinitionEffect {
    public int idEffectDef;
    public int actionId;
    public int areaShape;
    public int[] areaSize;
    public int[] params;
}

class Effect {
    public DefinitionEffect definition;
}

class EquipEffect {
    public Effect effect;
}
class UseEffect {
    public Effect effect;
}

class BaseParameters {
    public int itemTypeId;
    public int itemSetId;
    public int rarity;
    public int bindType;
    public int minimunShardSlot;
    public int maximumShardSlot;
}

class UseParameters {
    public int useCostAP;
    public int useCostMP;
    public int useCostWP;
    public int useRangeMin;
    public int useRangeMax;
    public boolean useTestFreeCell;
    public boolean useTestLos;
    public boolean useTestOnlyLine;
    public boolean useTestNoBorderCell;
    public int useWorldTarget;
}

class GraphicParameters {
    public int gfxId;
    public int femaleGfxId;
}

class ItemDef {
    public int idItemDef;
    public int level;
    public BaseParameters baseParameters;
    public UseParameters useParameters;
    public GraphicParameters graphicParameters;
    public int[] properties;
}

class Definition {
    public ItemDef item;
    public UseEffect[] useEffects;
    public UseEffect[] useCriticalEffects;
    public EquipEffect[] equipEffects;
}

@Document(collection = "items")
public class Item {

    @Id
    @JsonProperty("id")
    private String id;
    private Definition definition;
    private InfoTranslate title;
    private InfoTranslate description;

    /* Constructors */
    public Item() {}

    public Item(Definition definition, InfoTranslate title, InfoTranslate description) {
        super();
        this.definition = definition;
        this.title = title;
        this.description = description;
    }

    /* Getters */
    public String getId() {
        return id;
    }

    public Definition getDefinition() {
        return definition;
    }

    public InfoTranslate getTitle() {
        return title;
    }

    public InfoTranslate getDescription() {
        return description;
    }

    /* Setters */
    public void setDefinition(Definition definition) {
        this.definition = definition;
    }

    public void setTitle(InfoTranslate title) {
        this.title = title;
    }

    public void setDescription(InfoTranslate description) {
        this.description = description;
    }

}
