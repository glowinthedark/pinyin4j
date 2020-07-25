package net.sourceforge.pinyin4j.test;

import junit.framework.TestCase;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

public class PinyinHelperTest extends TestCase {

    public void testTricky() throws BadHanyuPinyinOutputFormatCombination {
        HanyuPinyinOutputFormat outputFormat = new HanyuPinyinOutputFormat();
        outputFormat.setToneType(HanyuPinyinToneType.WITH_TONE_MARK);
        outputFormat.setVCharType(HanyuPinyinVCharType.WITH_U_UNICODE);
        outputFormat.setCaseType(HanyuPinyinCaseType.LOWERCASE);

        assertEquals("xīān", PinyinHelper.toHanYuPinyinString("西安", outputFormat, " ", true));
        assertEquals("xué xí zhōngwén", PinyinHelper.toHanYuPinyinString("学习中文", outputFormat, " ", true));
        assertEquals("er2hua4", PinyinHelper.toHanYuPinyinString("儿化", outputFormat.setToneType(HanyuPinyinToneType.WITH_TONE_NUMBER), " ", true));
        assertEquals("er2 hua4yin1", PinyinHelper.toHanYuPinyinString("兒化音", outputFormat, " ", true));
        assertEquals("tai4yang2", PinyinHelper.toHanYuPinyinString("太阳", outputFormat, " ", true));
        assertEquals("yōng bu zháo", PinyinHelper.toHanYuPinyinString("用不着", outputFormat
                                                                                    .restoreDefault()
                                                                                    .setVCharType(HanyuPinyinVCharType.WITH_U_UNICODE)
                                                                                    .setToneType(HanyuPinyinToneType.WITH_TONE_MARK), " ", true));
    }


    public void testToHanyuPinyinStringArray() {

        // any input of non-Chinese characters will return null
        {
            HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();
            try {
                assertEquals(0, PinyinHelper.toHanyuPinyinStringArray('A', defaultFormat).length);
                assertEquals(0, PinyinHelper.toHanyuPinyinStringArray('z', defaultFormat).length);
                assertEquals(0, PinyinHelper.toHanyuPinyinStringArray(',', defaultFormat).length);
                assertEquals(0, PinyinHelper.toHanyuPinyinStringArray('。', defaultFormat).length);
            } catch (BadHanyuPinyinOutputFormatCombination e) {
                e.printStackTrace();
            }
        }

        // Chinese characters
        // single pronounciation
        {
            try {
                HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();

                String[] expectedPinyinArray = new String[]{"li3"};
                String[] pinyinArray = PinyinHelper.toHanyuPinyinStringArray('李', defaultFormat);

                assertEquals(expectedPinyinArray.length, pinyinArray.length);

                for (int i = 0; i < expectedPinyinArray.length; i++) {
                    assertEquals(expectedPinyinArray[i], pinyinArray[i]);
                }
            } catch (BadHanyuPinyinOutputFormatCombination e) {
                e.printStackTrace();
            }
        }
        {
            try {
                HanyuPinyinOutputFormat upperCaseFormat = new HanyuPinyinOutputFormat();
                upperCaseFormat.setCaseType(HanyuPinyinCaseType.UPPERCASE);

                String[] expectedPinyinArray = new String[]{"LI3"};
                String[] pinyinArray = PinyinHelper.toHanyuPinyinStringArray('李', upperCaseFormat);

                assertEquals(expectedPinyinArray.length, pinyinArray.length);

                for (int i = 0; i < expectedPinyinArray.length; i++) {
                    assertEquals(expectedPinyinArray[i], pinyinArray[i]);
                }
            } catch (BadHanyuPinyinOutputFormatCombination e) {
                e.printStackTrace();
            }
        }
        {
            try {
                HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();

                String[] expectedPinyinArray = new String[]{"lu:3"};
                String[] pinyinArray = PinyinHelper.toHanyuPinyinStringArray('吕', defaultFormat);

                assertEquals(expectedPinyinArray.length, pinyinArray.length);

                for (int i = 0; i < expectedPinyinArray.length; i++) {
                    assertEquals(expectedPinyinArray[i], pinyinArray[i]);
                }
            } catch (BadHanyuPinyinOutputFormatCombination e) {
                e.printStackTrace();
            }
        }
        {
            try {
                HanyuPinyinOutputFormat vCharFormat = new HanyuPinyinOutputFormat();
                vCharFormat.setVCharType(HanyuPinyinVCharType.WITH_V);

                String[] expectedPinyinArray = new String[]{"lv3"};
                String[] pinyinArray = PinyinHelper.toHanyuPinyinStringArray('吕', vCharFormat);

                assertEquals(expectedPinyinArray.length, pinyinArray.length);

                for (int i = 0; i < expectedPinyinArray.length; i++) {
                    assertEquals(expectedPinyinArray[i], pinyinArray[i]);
                }
            } catch (BadHanyuPinyinOutputFormatCombination e) {
                e.printStackTrace();
            }
        }

        // multiple pronounciations
        {
            try {
                HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();

                String[] expectedPinyinArray = new String[]{"jian1", "jian4"};
                String[] pinyinArray = PinyinHelper.toHanyuPinyinStringArray('间', defaultFormat);

                assertEquals(expectedPinyinArray.length, pinyinArray.length);

                for (int i = 0; i < expectedPinyinArray.length; i++) {
                    assertEquals(expectedPinyinArray[i], pinyinArray[i]);
                }
            } catch (BadHanyuPinyinOutputFormatCombination e) {
                e.printStackTrace();
            }
        }

        {
            try {
                HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();

                String[] expectedPinyinArray = new String[]{"hao3", "hao4"};
                String[] pinyinArray = PinyinHelper.toHanyuPinyinStringArray('好', defaultFormat);

                assertEquals(expectedPinyinArray.length, pinyinArray.length);

                for (int i = 0; i < expectedPinyinArray.length; i++) {
                    assertEquals(expectedPinyinArray[i], pinyinArray[i]);
                }
            } catch (BadHanyuPinyinOutputFormatCombination e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * test for combination of output formats
     */
    public void testOutputCombination() {
        try {
            HanyuPinyinOutputFormat outputFormat = new HanyuPinyinOutputFormat();

            // fix case type to lowercase firstly, change VChar and Tone
            // combination
            outputFormat.setCaseType(HanyuPinyinCaseType.LOWERCASE);

            // WITH_U_AND_COLON and WITH_TONE_NUMBER
            outputFormat.setVCharType(HanyuPinyinVCharType.WITH_U_AND_COLON);
            outputFormat.setToneType(HanyuPinyinToneType.WITH_TONE_NUMBER);

            assertEquals("lu:3", PinyinHelper.toHanyuPinyinStringArray('吕', outputFormat)[0]);

            // WITH_V and WITH_TONE_NUMBER
            outputFormat.setVCharType(HanyuPinyinVCharType.WITH_V);
            outputFormat.setToneType(HanyuPinyinToneType.WITH_TONE_NUMBER);

            assertEquals("lv3", PinyinHelper.toHanyuPinyinStringArray('吕', outputFormat)[0]);

            // WITH_U_UNICODE and WITH_TONE_NUMBER
            outputFormat.setVCharType(HanyuPinyinVCharType.WITH_U_UNICODE);
            outputFormat.setToneType(HanyuPinyinToneType.WITH_TONE_NUMBER);

            assertEquals("lü3", PinyinHelper.toHanyuPinyinStringArray('吕', outputFormat)[0]);

            // // WITH_U_AND_COLON and WITHOUT_TONE
            outputFormat.setVCharType(HanyuPinyinVCharType.WITH_U_AND_COLON);
            outputFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);

            assertEquals("lu:", PinyinHelper.toHanyuPinyinStringArray('吕', outputFormat)[0]);

            // WITH_V and WITHOUT_TONE
            outputFormat.setVCharType(HanyuPinyinVCharType.WITH_V);
            outputFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);

            assertEquals("lv", PinyinHelper.toHanyuPinyinStringArray('吕', outputFormat)[0]);

            // WITH_U_UNICODE and WITHOUT_TONE
            outputFormat.setVCharType(HanyuPinyinVCharType.WITH_U_UNICODE);
            outputFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);

            assertEquals("lü", PinyinHelper.toHanyuPinyinStringArray('吕', outputFormat)[0]);

            // WITH_U_AND_COLON and WITH_TONE_MARK is forbidden

            // WITH_V and WITH_TONE_MARK is forbidden

            // WITH_U_UNICODE and WITH_TONE_MARK
            outputFormat.setVCharType(HanyuPinyinVCharType.WITH_U_UNICODE);
            outputFormat.setToneType(HanyuPinyinToneType.WITH_TONE_MARK);

            assertEquals("lǚ", PinyinHelper.toHanyuPinyinStringArray('吕', outputFormat)[0]);

            // fix case type to UPPERCASE, change VChar and Tone
            // combination
            outputFormat.setCaseType(HanyuPinyinCaseType.UPPERCASE);

            // WITH_U_AND_COLON and WITH_TONE_NUMBER
            outputFormat.setVCharType(HanyuPinyinVCharType.WITH_U_AND_COLON);
            outputFormat.setToneType(HanyuPinyinToneType.WITH_TONE_NUMBER);

            assertEquals("LU:3", PinyinHelper.toHanyuPinyinStringArray('吕', outputFormat)[0]);

            // WITH_V and WITH_TONE_NUMBER
            outputFormat.setVCharType(HanyuPinyinVCharType.WITH_V);
            outputFormat.setToneType(HanyuPinyinToneType.WITH_TONE_NUMBER);

            assertEquals("LV3", PinyinHelper.toHanyuPinyinStringArray('吕', outputFormat)[0]);

            // WITH_U_UNICODE and WITH_TONE_NUMBER
            outputFormat.setVCharType(HanyuPinyinVCharType.WITH_U_UNICODE);
            outputFormat.setToneType(HanyuPinyinToneType.WITH_TONE_NUMBER);

            assertEquals("LÜ3", PinyinHelper.toHanyuPinyinStringArray('吕', outputFormat)[0]);

            // // WITH_U_AND_COLON and WITHOUT_TONE
            outputFormat.setVCharType(HanyuPinyinVCharType.WITH_U_AND_COLON);
            outputFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);

            assertEquals("LU:", PinyinHelper.toHanyuPinyinStringArray('吕', outputFormat)[0]);

            // WITH_V and WITHOUT_TONE
            outputFormat.setVCharType(HanyuPinyinVCharType.WITH_V);
            outputFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);

            assertEquals("LV", PinyinHelper.toHanyuPinyinStringArray('吕', outputFormat)[0]);

            // WITH_U_UNICODE and WITHOUT_TONE
            outputFormat.setVCharType(HanyuPinyinVCharType.WITH_U_UNICODE);
            outputFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);

            assertEquals("LÜ", PinyinHelper.toHanyuPinyinStringArray('吕', outputFormat)[0]);

            // WITH_U_AND_COLON and WITH_TONE_MARK is forbidden

            // WITH_V and WITH_TONE_MARK is forbidden

            // WITH_U_UNICODE and WITH_TONE_MARK
            outputFormat.setVCharType(HanyuPinyinVCharType.WITH_U_UNICODE);
            outputFormat.setToneType(HanyuPinyinToneType.WITH_TONE_MARK);

            assertEquals("LǙ", PinyinHelper.toHanyuPinyinStringArray('吕', outputFormat)[0]);
        } catch (BadHanyuPinyinOutputFormatCombination e) {
            e.printStackTrace();
        }
    }
}
