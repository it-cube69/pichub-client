package ru.itcube.pichub.model;

public class JWTToken {

    private String tokenId;

    public JWTToken(String tokenId) {
        this.tokenId = tokenId;
    }

    public JWTToken() {
    }

    public String getTokenId() {
        return tokenId;
    }

    public void setTokenId(String tokenId) {
        this.tokenId = tokenId;
    }
}
