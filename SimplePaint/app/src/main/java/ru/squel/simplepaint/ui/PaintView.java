package ru.squel.simplepaint.ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.provider.MediaStore;
import android.support.v4.print.PrintHelper;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

import ru.squel.simplepaint.R;

/**
 * Created by sq on 29.07.2017.
 * предоставляет функции рисования, сохранения и печати.
 * пользовательское представление, на котором рисует пользователь
 */
public class PaintView extends View {

    // Смещение, необходимое для продолжения рисования
    private static final float TOUCH_TOLERANCE = 10;

    private Bitmap bitmap; // Область рисования для вывода или сохранения
    private Canvas bitmapCanvas; // Используется для рисования на Bitmap
    private final Paint paintScreen; // Используется для вывода Bitmap на экран
    private final Paint paintLine; // Используется для рисования линий на Bitmap

    // Данные нарисованных контуров Path и содержащихся в них точек
    private final Map<Integer, Path> pathMap = new HashMap<>();
    private final Map<Integer, Point> previousPointMap = new HashMap<>();

    /// Конструктор, второй аргумент - настройки из xml файла
    public PaintView(Context context, AttributeSet attrs) {
        super(context, attrs);
        paintScreen = new Paint();

        // значения по умолчанию для рисуемых линий
        paintLine = new Paint();
        paintLine.setAntiAlias(true);           //сглаживание коаев
        paintLine.setColor(Color.BLACK);        //по умолчанию черный цвет
        paintLine.setStyle(Paint.Style.STROKE); // сплошная линия
        paintLine.setStrokeWidth(5);            // толщина линиии по умолчанию
        paintLine.setStrokeCap(Paint.Cap.ROUND); // Закругленные концы
    }

    // Создание объектов Bitmap и Canvas на основании размеров View
    @Override
    public void onSizeChanged(int w, int h, int oldW, int oldH) {
        bitmap = Bitmap.createBitmap(getWidth(), getHeight(),
                Bitmap.Config.ARGB_8888);
        bitmapCanvas = new Canvas(bitmap);
        bitmap.eraseColor(Color.WHITE); // Bitmap стирается белым цветом
    }

    // Стирание рисунка
    public void clear() {
        pathMap.clear(); // Удалить все контуры
        previousPointMap.clear(); // Удалить все предыдущие точки
        bitmap.eraseColor(Color.WHITE); // Очистка изображения
        invalidate(); // Перерисовать изображение
    }

    // Назначение цвета рисуемой линии
    public void setDrawingColor(int color) {
        paintLine.setColor(color);
    }

    // Получение цвета рисуемой линии
    public int getDrawingColor() {
        return paintLine.getColor();
    }

    // Назначение толщины рисуемой линии
    public void setLineWidth(int width) {
        paintLine.setStrokeWidth(width);
    }

    // Получение толщины рисуемой линии
    public int getLineWidth() {
        return (int) paintLine.getStrokeWidth();
    }

    // Перерисовка при обновлении DoodleView на экране
    @Override
    protected void onDraw(Canvas canvas) {
        // Перерисовка фона
        canvas.drawBitmap(bitmap, 0, 0, paintScreen);

        // Для каждой выводимой линии
        for (Integer key : pathMap.keySet())
            canvas.drawPath(pathMap.get(key), paintLine); // Рисование линии
    }

    // Обработка события касания
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getActionMasked();       // Тип события
        int actionIndex = event.getActionIndex();   // Указатель (палец)

        // Что происходит: начало касания, конец, перемещение?
        if (action == MotionEvent.ACTION_DOWN ||
                action == MotionEvent.ACTION_POINTER_DOWN) {
            touchStarted( event.getX(actionIndex), event.getY(actionIndex),
                    event.getPointerId(actionIndex));
            }
        else if (action == MotionEvent.ACTION_UP ||
                action == MotionEvent.ACTION_POINTER_UP) {
            touchEnded(event.getPointerId(actionIndex));
            }
        else {
            touchMoved(event);
        }

        invalidate();   // Перерисовка
        return true;    // признак обработки события
    }

    /**
     * Вызывается при первом касании экрана
     * @param x - координаты касания
     * @param y - координаты касания
     * @param lineID - идентификатор пальца, если уже существует, то вызывается reset и
     *               объект будет переиспользован
     */
    private void touchStarted(float x, float y, int lineID) {
        Path path; // Для хранения контура с заданным идентификатором
        Point point; // Для хранения последней точки в контуре

    // Если для lineID уже существует объект Path
        if (pathMap.containsKey(lineID)) {
            path = pathMap.get(lineID); // Получение Path
            path.reset(); // Очистка Path с началом нового касания
            point = previousPointMap.get(lineID); // Последняя точка Path
        }
        else {
            path = new Path();
            pathMap.put(lineID, path); // Добавление Path в Map
            point = new Point(); // Создание нового объекта Point
            previousPointMap.put(lineID, point); // Добавление Point в Map
        }

    // Переход к координатам касания
        path.moveTo(x, y);
        point.x = (int) x;
        point.y = (int) y;
    }

    // Вызывается при перемещении пальца по экрану
    private void touchMoved(MotionEvent event) {
        // Для каждого указателя (пальца) в объекте MotionEvent
        for (int i = 0; i < event.getPointerCount() ; i++) {
            // Получить идентификатор и индекс указателя
            int pointerID = event.getPointerId(i);
            int pointerIndex = event.findPointerIndex(pointerID);

            // Если существует объект Path, связанный с указателем
            if (pathMap.containsKey(pointerID)) {
                // Получить новые координаты для указателя
                float newX = event.getX(pointerIndex);
                float newY = event.getY(pointerIndex);

                // Получить объект Path и предыдущий объект Point,
                // связанный с указателем
                Path path = pathMap.get(pointerID);
                Point point = previousPointMap.get(pointerID);

                // Вычислить величину смещения от последнего обновления
                float deltaX = Math.abs(newX - point.x);
                float deltaY = Math.abs(newY - point.y);

                // Если расстояние достаточно велико
                if (deltaX >= TOUCH_TOLERANCE || deltaY >= TOUCH_TOLERANCE) {
                    // Расширение контура до новой точки
                    path.quadTo(point.x, point.y, (newX + point.x) / 2,
                            (newY + point.y) / 2);
                    // Сохранение новых координат
                    point.x = (int) newX;
                    point.y = (int) newY;
                }
            }
        }
    }

    // Вызывается при завершении касания
    private void touchEnded(int lineID) {
        Path path = pathMap.get(lineID);            // Получение объекта Path
        bitmapCanvas.drawPath(path, paintLine);     // Рисование на bitmapCanvas
        path.reset();                               // Сброс объекта Path
    }

    // Сохранение текущего изображения в галерее
    public String saveImage() {
        // Имя состоит из префикса "SimplePaint" и текущего времени
        final String name = "SimplePaint" + System.currentTimeMillis() + ".jpg";

        // Сохранение изображения в галерее устройства
        String location = MediaStore.Images.Media.insertImage(
                getContext().getContentResolver(), bitmap, name,
                "SimplePaint Drawing");
        if (location != null) {
        // Вывод сообщения об успешном сохранении
            Toast message = Toast.makeText(getContext(),
                    R.string.message_saved,
                    Toast.LENGTH_SHORT);
            message.setGravity(Gravity.CENTER, message.getXOffset() / 2,
                    message.getYOffset() / 2);
            message.show();
        }
        else {
        // Вывод сообщения об ошибке сохранения
            Toast message = Toast.makeText(getContext(),
                    R.string.message_error_saving, Toast.LENGTH_SHORT);
            message.setGravity(Gravity.CENTER, message.getXOffset() / 2,
                    message.getYOffset() / 2);
            message.show();
        }

        return (location + "/" + name);
    }

    // Печать текущего изображения
    public void printImage() {
        if (PrintHelper.systemSupportsPrint()) {
            // Использование класса PrintHelper для печати
            PrintHelper printHelper = new PrintHelper(getContext());

            // Изображение масштабируется и выводится на печать
            printHelper.setScaleMode(PrintHelper.SCALE_MODE_FIT);
            printHelper.printBitmap("SimplePaint Image", bitmap);
        }
        else {
            // Вывод сообщения о том, что система не поддерживает печать
            Toast message = Toast.makeText(getContext(),
                    R.string.message_error_printing, Toast.LENGTH_SHORT);
            message.setGravity(Gravity.CENTER, message.getXOffset() / 2,
                    message.getYOffset() / 2);
            message.show();
        }
    }

    public Bitmap getImage() {
        return this.bitmap;
    }
}
