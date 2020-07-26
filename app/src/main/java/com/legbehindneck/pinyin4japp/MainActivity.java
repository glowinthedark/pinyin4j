package com.legbehindneck.pinyin4japp;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

public class MainActivity extends Activity {

    private EditText result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        result = findViewById(R.id.result);

        EditText input = findViewById(R.id.input);

        String origText = input.getText().toString();

        setPinyin(origText);

        Button btnConvert = findViewById(R.id.btnConvert);
        btnConvert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setPinyin(input.getText().toString());
            }
        });

    }

    private void setPinyin(String origText) {
        HanyuPinyinOutputFormat outputFormat = new HanyuPinyinOutputFormat()
                                                       .setToneType(HanyuPinyinToneType.WITH_TONE_NUMBER)
                                                       .setVCharType(HanyuPinyinVCharType.WITH_U_AND_COLON)
                                                       .setCaseType(HanyuPinyinCaseType.LOWERCASE);


        try {
            String pinyinWithToneNumbers = PinyinHelper.toHanYuPinyinString(origText, outputFormat, " ", true);
            String pinyinWithToneMarks = PinyinHelper.toHanYuPinyinString(origText, outputFormat
                                                                                            .restoreDefault()
                                                                                            .setToneType(HanyuPinyinToneType.WITH_TONE_MARK)
                                                                                            .setVCharType(HanyuPinyinVCharType.WITH_U_UNICODE)
                                                                                            .setCaseType(HanyuPinyinCaseType.LOWERCASE), " ", true);


            result.setText(pinyinWithToneNumbers + "\n"
                                   + pinyinWithToneMarks);
        } catch (BadHanyuPinyinOutputFormatCombination e) {
            Toast.makeText(this, "Error converting to pinyin " + e, Toast.LENGTH_SHORT).show();
        }
    }
}