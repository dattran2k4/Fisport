package com.fisport.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ETeam {
    TEAM_A("Đội A"),
    TEAM_B("Đội B");

    private final String value;
}
