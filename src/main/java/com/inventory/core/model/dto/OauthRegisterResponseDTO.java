package com.inventory.core.model.dto;

/**
 * Created by dhiraj on 1/15/18.
 */
public class OauthRegisterResponseDTO {

    private String client_id;

    private String client_secret;

    public String getClient_id() {
        return client_id;
    }

    public void setClient_id(String client_id) {
        this.client_id = client_id;
    }

    public String getClient_secret() {
        return client_secret;
    }

    public void setClient_secret(String client_secret) {
        this.client_secret = client_secret;
    }
}
