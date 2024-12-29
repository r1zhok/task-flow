package org.r1zhok.app.dto;

public record EmailDetails(
        String supplier,
        String messageBody,
        String subject
) {}