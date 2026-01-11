package com.ai._ojo.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class KeyClause {

    private ClauseType type;
    private String title;
    private String description;
    private RiskLevel riskLevel;
}
