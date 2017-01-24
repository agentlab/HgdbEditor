/**
 *
 */
package ru.agentlab.wordnet.editor.app.ui;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utilities {
    public String getWords(String str) {
        String pattern1 = "Words: ";
        String pattern2 = " --";

        Pattern p = Pattern.compile(Pattern.quote(pattern1) + "(.*?)" + Pattern.quote(pattern2));
        Matcher m = p.matcher(str);

        while (m.find())
        {
            return m.group(1);
        }
        return null;
    }
}
