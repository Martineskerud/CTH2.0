package net.eskerud.martin.masterthesis.util;

import android.graphics.Point;
import android.view.Display;
import android.view.WindowManager;

import net.eskerud.martin.masterthesis.bitmapblocks.BitmapBlock;

/**
 * Created by Martin on 11-Mar-15.
 */
public class MathHelper {

    Display display;

    public MathHelper(Display display) {

        this.display = display;
    }

    //used for clamping the x value so things stay within the relationship editor.
    public float evaluateX(float x, BitmapBlock b) {
        //Point size = new Point();
        //display.getSize(size);
        float threshhold;
        if(b!= null){

             threshhold  = b.getWidth();
        }
        else{
             threshhold = 200;
        }

        int width = 640;
        if (x > width - threshhold) {
            x = width - threshhold;
            return x;
        }


        return x;
    }

    //used for clamping the y value so things stay within the relationship editor.
    public float evaluateY(float y, BitmapBlock b) {
        //Point size = new Point();
        //display.getSize(size);

        float threshhold;
        if(b!= null){

            threshhold  = b.getHeight();
        }
        else{
            threshhold = 200;
        }

        int height = 490;
        if (y > height - threshhold) {
            y = height - threshhold;
            return y;
        }


        return y;
    }


}
