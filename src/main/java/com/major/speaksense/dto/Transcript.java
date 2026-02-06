package com.major.speaksense.dto;

import java.util.List;


public record Transcript(
        String full_text,
        List<SegmentDTO> segments,
        String language
) {}

record SegmentDTO(
        String text,
        List<WordDTO> words
) {}

record WordDTO(
        String word,
        double start,
        double end,
        double probability
) {}