package com.major.speaksense.dto;

import java.util.List;

public record Analysis(
        double overall_pronunciation_score,
        List<WordAnalysisDTO> word_details
) {}