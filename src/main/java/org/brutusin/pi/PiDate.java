/*
 * Copyright 2015 Ignacio del Valle Alles idelvall@brutusin.org.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.brutusin.pi;

import java.io.IOException;
import java.io.InputStream;

/**
 *
 * @author Ignacio del Valle Alles idelvall@brutusin.org
 */
public class PiDate {

    private static final int[] MIN = new int[]{1, 1, 0, 0, 0};
    private static final int[] MAX = new int[]{12, 31, 23, 59, 59};

    private static String readNextDigits(final int numberOfDigits, final InputStream piStream) throws IOException {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < numberOfDigits; i++) {
            int read = piStream.read();
            if (read < 0) {
                break;
            }
            sb.append(Character.digit(read, 10));
        }
        return sb.toString();
    }

    private static boolean test(final int current, final InputStream piStream) throws IOException {
        if (current >= MIN.length) {
            return true;
        }
        int minValue = MIN[current];
        int maxValue = MAX[current];
        int value = Integer.valueOf(readNextDigits(2, piStream));
        if (value > maxValue || value < minValue) {
            return false;
        }
        return test(current + 1, piStream);
    }

    public static String computePerfectPiDate() throws IOException {
        InputStream piStream = PiDate.class.getResourceAsStream("Pi.txt");
        int readed;
        StringBuilder sb = new StringBuilder();
        while ((readed = piStream.read()) >= 0) {
            int digit = Character.digit(readed, 10);
            piStream.mark(Integer.MAX_VALUE); // At most 10 bytes will be cached
            boolean found = test(0, piStream);
            piStream.reset();
            if (found) {
                sb.append("-").append(readNextDigits(2, piStream)); // MM
                sb.append("-").append(readNextDigits(2, piStream)); // DD
                sb.append(":").append(readNextDigits(2, piStream)); // hh
                sb.append(":").append(readNextDigits(2, piStream)); // mm
                sb.append(":").append(readNextDigits(2, piStream)); // ss
                sb.append(".").append(readNextDigits(3, piStream)); // sss
                return sb.toString();
            }
            sb.append(digit);
        }
        throw new Error("Insuficient Pi decimals in file");
    }

    public static void main(String[] args) throws IOException {
        System.out.println(computePerfectPiDate());
    }
}
