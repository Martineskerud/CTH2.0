package net.eskerud.martin.masterthesis.util;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.Log;

import net.eskerud.martin.masterthesis.MainActivity;
import net.eskerud.martin.masterthesis.R;
import net.eskerud.martin.masterthesis.bitmapblocks.BitmapBlock;

import java.lang.reflect.Field;
import java.util.LinkedHashMap;
import java.util.Map;

import static net.eskerud.martin.masterthesis.util.Consts.BACKGROUND;
import static net.eskerud.martin.masterthesis.util.Consts.DUMMY_HACK;
import static net.eskerud.martin.masterthesis.util.Consts.IF_BLOCK;
import static net.eskerud.martin.masterthesis.util.Consts.OFF_BLOCK;
import static net.eskerud.martin.masterthesis.util.Consts.ON_BLOCK;

/**
 * Created by Martin on 11-Mar-15.
 */
public class RenderHelper {



    public RenderHelper(){
    }

    public  void initBitmapPositions(Resources res, LinkedHashMap<String, BitmapBlock> bitmaps) {
        //   bitmaps.get(DUMMY_HACK).setBitmap(BitmapFactory.decodeResource(res, R.drawable.hack));
        //   bitmaps.get(DUMMY_HACK).getMatrix().setTranslate(0, 0);

        if(res==null){
            Log.d("asdasd", "res is null");
        }
        if(bitmaps.get(IF_BLOCK)==null){
            Log.d("sdfsdfsdf","if block is null");
        }

        bitmaps.get(IF_BLOCK).setBitmap(BitmapFactory.decodeResource(res, R.drawable.if_smaller_3));
        bitmaps.get(OFF_BLOCK).setBitmap(BitmapFactory.decodeResource(res, R.drawable.off_smaller));
        bitmaps.get(ON_BLOCK).setBitmap(BitmapFactory.decodeResource(res, R.drawable.on_smaller));
        bitmaps.get(IF_BLOCK).getMatrix().setTranslate(100, 200);
        bitmaps.get(ON_BLOCK).getMatrix().setTranslate(400, 300);
        bitmaps.get(OFF_BLOCK).getMatrix().setTranslate(400, 200);
    }



    /*
    * TODO: Bug. Sometimes doesn't reset everything. It's just straight up random. Not hard to reproduce.
    * */
    //sets every piece back to its place.
    public  LinkedHashMap<String, BitmapBlock> reset(LinkedHashMap<String,BitmapBlock> bitmaps, Resources res){

        bitmaps.clear();
        bitmaps = populateBitmaps(bitmaps,res);
        initBitmapPositions(res, bitmaps);
        placeBackground(bitmaps, res);
        return bitmaps;
    }

    public LinkedHashMap<String,BitmapBlock> placeBackground(LinkedHashMap<String, BitmapBlock> bitmaps, Resources res){
        bitmaps.get(BACKGROUND).setBitmap(BitmapFactory.decodeResource(res, R.drawable.bckgrnd_smaller));
        bitmaps.get(BACKGROUND).getMatrix().setTranslate(0, 0);

        return bitmaps;
    }



    public LinkedHashMap<String, BitmapBlock> populateBitmaps(LinkedHashMap<String, BitmapBlock> bitmaps, Resources res) {
        for (Field id : R.drawable.class.getFields()) {
            //The default names are long and we don't want those files.
            if (id.getName().length() < 19) {
                bitmaps.put(id.getName(), new BitmapBlock(new Matrix(), id.getName(), res));
                Log.d("ADDED THIS:::", id.getName());
            }
        }
        return bitmaps;
    }


    public static void spawnNewPiece(float x, float y, LinkedHashMap<String,BitmapBlock> bitmaps, Resources res) {

        //spawn if-piece

        if(x > 10 && x <165)
        {
            Matrix m = new Matrix();
            BitmapBlock temp = new BitmapBlock(m, "if_smaller_3"+bitmaps.size(),res);
            int imageResource = res.getIdentifier("if_smaller_3", null, Consts.PACKAGE_NAME);
            temp.setBitmap(BitmapFactory.decodeResource(res, imageResource));
            temp.getMatrix().setTranslate(x,y);
            bitmaps.put("if_smaller_3"+(bitmaps.size()-1), temp);
        }

    }




}
