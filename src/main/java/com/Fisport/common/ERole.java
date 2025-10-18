package com.Fisport.common;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum ERole {
    @JsonProperty("admin")
    ADMIN,
    @JsonProperty("owner")
    OWNER,
    @JsonProperty("user")
    USER;
}
