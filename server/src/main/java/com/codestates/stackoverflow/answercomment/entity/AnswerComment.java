package com.codestates.stackoverflow.answercomment.entity;

import com.codestates.stackoverflow.answer.entity.Answer;
import com.codestates.stackoverflow.member.entity.Member;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
public class AnswerComment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long answerCommentId;

    private String body;

    @ManyToOne(targetEntity = Member.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(targetEntity = Answer.class)
    @JoinColumn(name = "answer_id")
    private Answer answer;
}
