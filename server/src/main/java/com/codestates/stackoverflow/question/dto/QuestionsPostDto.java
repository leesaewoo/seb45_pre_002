package com.codestates.stackoverflow.question.dto;

import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
public class QuestionsPostDto {

    @NotNull
    private Long memberId;

    @NotBlank
    private String questionTitle;

    @NotBlank
    private String questionBody;
}
