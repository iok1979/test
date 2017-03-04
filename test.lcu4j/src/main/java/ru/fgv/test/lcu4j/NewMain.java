package ru.fgv.test.lcu4j;

import com.ibm.icu.text.RuleBasedNumberFormat;
import java.util.Locale;

/**
 *
 * @author Fgv
 */
public class NewMain {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        RuleBasedNumberFormat nf = new RuleBasedNumberFormat(Locale.forLanguageTag("ru"), RuleBasedNumberFormat.SPELLOUT);
        System.out.println(nf.format(52369));
    }

}
