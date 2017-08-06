package ru.squel.tipcalculator;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;

import java.text.NumberFormat;

public class MainActivity extends AppCompatActivity {

    // Форматировщики денежных сумм и процентов
    private static final NumberFormat currencyFormat = NumberFormat.getCurrencyInstance();
    private static final NumberFormat percentFormat = NumberFormat.getPercentInstance();

    private TextView amountTextView;
    private TextView percentTextView;
    private TextView tipTextView;
    private TextView totalTextView;

    private double billAmount = 0.0; // Сумма счета, введенная пользователем
    private double percent = 0.15; // Исходный процент чаевых

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        amountTextView = (TextView) findViewById(R.id.amountTextView);
        percentTextView = (TextView) findViewById(R.id.percentTextView);
        tipTextView = (TextView) findViewById(R.id.tipTextView);
        totalTextView = (TextView) findViewById(R.id.totalTextView);
        tipTextView.setText(currencyFormat.format(0)); // Заполнить 0
        totalTextView.setText(currencyFormat.format(0)); // Заполнить 0

        EditText amountTextEdit = (EditText) findViewById(R.id.amountEditText);
        amountTextEdit.addTextChangedListener(amountEditTextWatcher);

        // Назначение слушателя OnSeekBarChangeListener для percentSeekBar
        SeekBar percentSeekBar =
                (SeekBar) findViewById(R.id.seekBar);
        percentSeekBar.setOnSeekBarChangeListener(seekBarListener);
    }

    private void calculate() {
        // отображение процентов
        percentTextView.setText(percentFormat.format(percent));

        //вычисление чаевых и общей суммы
        double tip = billAmount * percent;
        double total = tip + billAmount;

        tipTextView.setText(currencyFormat.format(tip));
        totalTextView.setText(currencyFormat.format(total));
    }

    /**
     * Анонимный внутренний класс для обработки изменения сикбара, здесь же логика
     */
    private final SeekBar.OnSeekBarChangeListener seekBarListener =
            new SeekBar.OnSeekBarChangeListener() {

                // изменение ползунка
                @Override
                public void onProgressChanged(SeekBar seekbar, int progress, boolean fromUser) {
                    percent = progress / 100.00;
                    calculate();
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) { }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) { }
            };

    private final TextWatcher amountEditTextWatcher = new TextWatcher() {
        // Вызывается при изменении пользователем величины счета
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

            try { // Получить счет и вывести в формате денежной суммы
                billAmount = Double.parseDouble(s.toString()) / 100.0;
                amountTextView.setText(currencyFormat.format(billAmount));
                }
            catch (NumberFormatException e) { // Если s пусто или не число
                amountTextView.setText("");
                billAmount = 0.0;
                }

            calculate(); // Обновление полей с чаевыми и общей суммой
            }

        @Override
        public void afterTextChanged(Editable s) { }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
    };
}
