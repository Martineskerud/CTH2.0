package net.eskerud.martin.masterthesis.util;

import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.util.Log;

import net.eskerud.martin.masterthesis.R;
import net.eskerud.martin.masterthesis.bitmapblocks.BitmapBlock;

import java.lang.reflect.Field;
import java.util.LinkedHashMap;
import static net.eskerud.martin.masterthesis.util.Consts.*;

/**
 * Created by Martin on 11-Mar-15.
 */
public class RenderHelper {

    //sets every piece back to its place.
    public LinkedHashMap<String, BitmapBlock> reset(LinkedHashMap<String, BitmapBlock> bitmaps, Resources res) {

        bitmaps.clear();
       bitmaps = populateBitmaps(bitmaps, res);
      //  initBitmapPositions(res, bitmaps);
        placeBackground(bitmaps, res);
        return bitmaps;
    }

    public LinkedHashMap<String, BitmapBlock> placeBackground(LinkedHashMap<String, BitmapBlock> bitmaps, Resources res) {
        bitmaps.put(BACKGROUND, new BitmapBlock(new Matrix(), BACKGROUND, res, 0));
        bitmaps.get(BACKGROUND).setBitmap(BitmapFactory.decodeResource(res, R.drawable.bckgrnd_smaller));
        bitmaps.get(BACKGROUND).getMatrix().setTranslate(0, 0);

        return bitmaps;
    }


    public LinkedHashMap<String, BitmapBlock> populateBitmaps(LinkedHashMap<String, BitmapBlock> bitmaps, Resources res) {
        for (Field id : R.drawable.class.getFields()) {
            //The default names are long and we don't want those files.
            int num = bitmaps.size();
            if (id.getName().length() < 19) {
                bitmaps.put(id.getName(), new BitmapBlock(new Matrix(), id.getName(), res,num));
            }
        }
        return bitmaps;
    }



    public static BitmapBlock spawnNewIf(float x, float y, LinkedHashMap<String, BitmapBlock> bitmaps, Resources res) {

        int num = bitmaps.size();
        Matrix m = new Matrix();
        BitmapBlock temp = new BitmapBlock(m, IF_BLOCK, res,  num);
        temp.setBitmap(BitmapFactory.decodeResource(res, R.drawable.if_smaller_3));
        temp.setWidth(245);
        temp.setHeight(225);
        temp.getMatrix().setTranslate(x, y);
        Log.d("spawned ", "" + temp.getId());
        return temp;
    }

    public static BitmapBlock spawnNewOn(float x, float y, LinkedHashMap<String, BitmapBlock> bitmaps, Resources res) {

        int num = bitmaps.size();
        Matrix m = new Matrix();
        BitmapBlock temp = new BitmapBlock(m, ON_BLOCK, res, num);
        temp.setWidth(106);
        temp.setHeight(37);
        temp.setBitmap(BitmapFactory.decodeResource(res, R.drawable.on_smaller));
        temp.getMatrix().setTranslate(x, y);
        Log.d("spawned ", "" + temp.getId());
        return temp;
    }

    public static BitmapBlock spawnNewOff(float x, float y, LinkedHashMap<String, BitmapBlock> bitmaps, Resources res) {

        int num = bitmaps.size();
        Matrix m = new Matrix();
        BitmapBlock temp = new BitmapBlock(m, OFF_BLOCK, res, num);
        temp.setWidth(106);
        temp.setHeight(37);
        Log.d("spawned ",""+ temp.getId());
        temp.setBitmap(BitmapFactory.decodeResource(res, R.drawable.off_smaller));
        temp.getMatrix().setTranslate(x, y);
        return temp;
    }

    public static BitmapBlock spawnNewTimer(float x, float y, LinkedHashMap<String, BitmapBlock> bitmaps, Resources res) {

        int num = bitmaps.size();
        Matrix m = new Matrix();
        BitmapBlock temp = new BitmapBlock(m, TIMER_BLOCK, res, num);
        temp.setWidth(106);
        temp.setHeight(37);
        temp.setBitmap(BitmapFactory.decodeResource(res, R.drawable.o_timer));
        temp.getMatrix().setTranslate(x, y);
        Log.d("spawned ", "" + temp.getId());
        return temp;
    }


    public static BitmapBlock spawnNewPercent(float x, float y, LinkedHashMap<String, BitmapBlock> bitmaps, Resources res) {

        int num = bitmaps.size();
        Matrix m = new Matrix();
        BitmapBlock temp = new BitmapBlock(m, PERCENT_BLOCK, res, num);
        temp.setWidth(106);
        temp.setHeight(37);
        temp.setBitmap(BitmapFactory.decodeResource(res, R.drawable.o_percent));
        temp.getMatrix().setTranslate(x, y);
        Log.d("spawned ", "" + temp.getId());
        return temp;
    }


    public static BitmapBlock spawnNewEquals(float x, float y, LinkedHashMap<String, BitmapBlock> bitmaps, Resources res) {

        int num = bitmaps.size();
        Matrix m = new Matrix();
        BitmapBlock temp = new BitmapBlock(m, EQUALS_BLOCK, res, num);
        temp.setWidth(39);
        temp.setHeight(39);
        Log.d("spawned ",""+ temp.getId());
        temp.setBitmap(BitmapFactory.decodeResource(res, R.drawable.equals_smaller_3));
        temp.getMatrix().setTranslate(x, y);

        return temp;
    }

    public static BitmapBlock spawnNewLess(float x, float y, LinkedHashMap<String, BitmapBlock> bitmaps, Resources res) {

        int num = bitmaps.size();
        Matrix m = new Matrix();
        BitmapBlock temp = new BitmapBlock(m, LESS_BLOCK, res, num);
        temp.setWidth(39);
        temp.setHeight(39);
        Log.d("spawned ",""+ temp.getId());
        temp.setBitmap(BitmapFactory.decodeResource(res, R.drawable.e_less));
        temp.getMatrix().setTranslate(x, y);

        return temp;
    }

    public static BitmapBlock spawnNewMore(float x, float y, LinkedHashMap<String, BitmapBlock> bitmaps, Resources res) {

        int num = bitmaps.size();
        Matrix m = new Matrix();
        BitmapBlock temp = new BitmapBlock(m, MORE_BLOCK, res, num);
        temp.setWidth(39);
        temp.setHeight(39);
        Log.d("spawned ",""+ temp.getId());
        temp.setBitmap(BitmapFactory.decodeResource(res, R.drawable.e_more));
        temp.getMatrix().setTranslate(x, y);

        return temp;
    }


    public static BitmapBlock spawnNewClock(float x, float y, LinkedHashMap<String, BitmapBlock> bitmaps, Resources res) {

        int num = bitmaps.size();
        Matrix m = new Matrix();
        BitmapBlock temp = new BitmapBlock(m, CLOCK_BLOCK, res, num);
        temp.setWidth(45);
        temp.setHeight(45);
        Log.d("spawned ",""+ temp.getId());
        temp.setBitmap(BitmapFactory.decodeResource(res, R.drawable.clock_smaller_1));
        temp.getMatrix().setTranslate(x, y);

        return temp;
    }

    public static BitmapBlock spawnNewLight(float x, float y, LinkedHashMap<String, BitmapBlock> bitmaps, Resources res) {

        int num = bitmaps.size();
        Matrix m = new Matrix();
        BitmapBlock temp = new BitmapBlock(m, LIGHT_BLOCK, res, num);
        temp.setWidth(45);
        temp.setHeight(45);
        Log.d("spawned ",""+ temp.getId());
        temp.setBitmap(BitmapFactory.decodeResource(res, R.drawable.c_light));
        temp.getMatrix().setTranslate(x, y);

        return temp;
    }
    public static BitmapBlock spawnNewMovement(float x, float y, LinkedHashMap<String, BitmapBlock> bitmaps, Resources res) {

        int num = bitmaps.size();
        Matrix m = new Matrix();
        BitmapBlock temp = new BitmapBlock(m, MOVEMENT_BLOCK, res, num);
        temp.setWidth(45);
        temp.setHeight(45);
        Log.d("spawned ",""+ temp.getId());
        temp.setBitmap(BitmapFactory.decodeResource(res, R.drawable.c_movement));
        temp.getMatrix().setTranslate(x, y);

        return temp;
    }
    public static BitmapBlock spawnNewFireplace(float x, float y, LinkedHashMap<String, BitmapBlock> bitmaps, Resources res) {

        int num = bitmaps.size();
        Matrix m = new Matrix();
        BitmapBlock temp = new BitmapBlock(m, FIREPLACE_BLOCK, res, num);
        temp.setWidth(45);
        temp.setHeight(45);
        Log.d("spawned ",""+ temp.getId());
        temp.setBitmap(BitmapFactory.decodeResource(res, R.drawable.c_fireplace));
        temp.getMatrix().setTranslate(x, y);

        return temp;
    }
    public static BitmapBlock spawnNewTv(float x, float y, LinkedHashMap<String, BitmapBlock> bitmaps, Resources res) {

        int num = bitmaps.size();
        Matrix m = new Matrix();
        BitmapBlock temp = new BitmapBlock(m, TV_BLOCK, res, num);
        temp.setWidth(45);
        temp.setHeight(45);
        Log.d("spawned ",""+ temp.getId());
        temp.setBitmap(BitmapFactory.decodeResource(res, R.drawable.c_tv));
        temp.getMatrix().setTranslate(x, y);

        return temp;
    }
}
