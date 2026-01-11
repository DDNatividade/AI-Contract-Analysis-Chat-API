package com.ai._ojo.domain;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ContractAnalysis {

    private String contractType;
    private String summary;
    private Parties parties;
    private KeyDates keyDates;
    private String duration;
    private FinancialTerms financialTerms;
    private List<KeyClause> keyClauses;
    private List<String> specialNotes;

}
