package com.major.speaksense.dto;

import java.util.List;

public record WordAnalysisDTO(
        String word,
        double word_score,
        List<PhonemeDTO> phonemes,
        String audio_base64
) {}

record PhonemeDTO(
        String phoneme,
        double score
) {}