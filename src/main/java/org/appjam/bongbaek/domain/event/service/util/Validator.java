package org.appjam.bongbaek.domain.event.service.util;

import org.appjam.bongbaek.domain.event.exception.InvalidNoteException;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Validator {
    // 하나의 시각적 유니코드 grapheme의 정규식인 "\X"를 이용하여 문자열의 길이를 카운트
    private static final Pattern GRAPHEME_PATTERN = Pattern.compile("\\X");

    public static String validateLength(String note) {

        if (lengthWithEmoji(note) > 50){
            throw new InvalidNoteException();
        }

        return note;
    }

    private static int lengthWithEmoji(String string) {
        Matcher matcher = GRAPHEME_PATTERN.matcher(string);
        int count = 0;

        while (matcher.find()) {
            count++;
        }

        return count;
    }
}
