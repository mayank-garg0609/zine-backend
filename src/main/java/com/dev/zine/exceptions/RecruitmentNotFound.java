package com.dev.zine.exceptions;

public class RecruitmentNotFound extends Exception {
    public RecruitmentNotFound(Long id) {
        super("Recruitment "+id.toString()+" does not exist.");
    }
}
