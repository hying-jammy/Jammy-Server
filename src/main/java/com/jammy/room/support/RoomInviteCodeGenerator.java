package com.jammy.room.support;

import org.springframework.stereotype.Component;

import java.security.SecureRandom;

@Component
public class RoomInviteCodeGenerator {

    private static final String INVITE_CODE_CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final int INVITE_CODE_LENGTH = 7;
    private static final int INVITE_CODE_SEPARATOR_POSITION = 3;
    private static final SecureRandom SECURE_RANDOM = new SecureRandom();

    public String generateInviteCode() {
        StringBuilder inviteCodeBuilder = new StringBuilder(INVITE_CODE_LENGTH + 1);
        for (int i = 0; i < INVITE_CODE_LENGTH; i++) {
            if (i == INVITE_CODE_SEPARATOR_POSITION) {
                inviteCodeBuilder.append('-');
            }
            inviteCodeBuilder.append(INVITE_CODE_CHARACTERS.charAt(SECURE_RANDOM.nextInt(INVITE_CODE_CHARACTERS.length())));
        }
        return inviteCodeBuilder.toString();
    }
}
