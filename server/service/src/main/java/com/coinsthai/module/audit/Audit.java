package com.coinsthai.module.audit;

import org.springframework.data.annotation.Id;

// @Document(indexName = "csp", type = "audit")
// FIXME introduce Elastic Search dependence
public class Audit {

    @Id
    private String id;

    private String timestamp;

    private String actor, action, type, recipient, result;

    public Audit() {
    }

    public Audit(String actor, String action, String type, String recipient, String result) {
        this.actor = actor;
        this.action = action;
        this.type = type;
        this.recipient = recipient;
        this.result = result;
    }

    public String getId() {
        return id;
    }

    //Needed to map id
    public void setId(String id) {
        this.id = id;
    }

    public String getMessage() {
        return actor + " " + action + " " + type + " " + recipient + " " + result;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public String getActor() {
        return actor;
    }

    public void setActor(String actor) {
        this.actor = actor;
    }

    public String getAction() {
        return action;
    }

    public String getType() {
        return type;
    }

    public String getRecipient() {
        return recipient;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }

    public String getResult() {
        return result;
    }

}
