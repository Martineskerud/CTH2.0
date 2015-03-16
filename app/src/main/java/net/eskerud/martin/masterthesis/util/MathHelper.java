package net.eskerud.martin.masterthesis.util;

import android.graphics.Point;
import android.view.Display;
import android.view.WindowManager;

/**
 * Created by Martin on 11-Mar-15.
 */
public class MathHelper {

    Display display;

    public MathHelper(Display display) {

        this.display = display;
    }

    //used for clamping the x value so things stay within the relationship editor.
    public float evaluateX(float x) {
        Point size = new Point();
        display.getSize(size);

        int width = size.x;
        float threshhold = width / 1.5f;
        if (x > width - threshhold) {
            x = width - threshhold;
            return x;
        }


        return x;
    }

    //used for clamping the y value so things stay within the relationship editor.
    public float evaluateY(float y) {
        Point size = new Point();
        display.getSize(size);
        int height = size.y;
        float threshhold = height /2f;
        if (y > height - threshhold) {
            y = height - threshhold;
            return y;
        }


        return y;
    }


}
