package net.eskerud.martin.masterthesis;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import net.eskerud.martin.masterthesis.bitmapblocks.BitmapBlock;
import net.eskerud.martin.masterthesis.util.Consts;
import net.eskerud.martin.masterthesis.util.MathHelper;
import net.eskerud.martin.masterthesis.util.RenderHelper;

import java.lang.reflect.Field;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;


public class MainActivity extends Activity {

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(new MyView(this));
    }

    public class MyView extends View {
        private LinkedHashMap<String, BitmapBlock> bitmaps = new LinkedHashMap<String, BitmapBlock>();
        private float x;
        private float y;
        //non clamped x,y values
        private float realX;
        private float realY;
        private MathHelper mh;
        private RenderHelper rh;
        private String selectedBitmap;
        private boolean isSnapped = false;
        private Resources res;

        public MyView(Context context) {
            super(context);
            mh = new MathHelper(getWindowManager().getDefaultDisplay());
            rh = new RenderHelper();
            initBitmapHashmapKeys();
        }

        //Called once on launch
        private void initBitmapHashmapKeys() {
            res = getResources();
            bitmaps = rh.populateBitmaps(bitmaps, res);
            rh.initBitmapPositions(res, bitmaps);
            bitmaps = rh.placeBackground(bitmaps, res);
        }

        @Override
        public boolean onTouchEvent(MotionEvent e) {
            //TODO: interpolate between current position and finger position

            realX = e.getX();
            realY = e.getY();
            x = mh.evaluateX(e.getX());
            y = mh.evaluateY(e.getY());

            //reset button
            if (realX > 1150 && realX < 1300 && realY > 0 && realY < 50) {
                bitmaps = rh.reset(bitmaps, res);
                Log.d("reset", "attempting to reset the application...");
                this.invalidate();
                return true;
            }


            //offset values for where on/off fits on the if-block.
            int conditionOffsetX = 140;
            int conditionOffsetY = 15;
            //offset values for where on/off fits on the then-part of the if-block
            int thenConditionOffsetX = 140;
            int thenConditionOffsetY = -124;

            //leeway is the error margin for pressing the element. SmallerLeeway is used for smaller items to be pressed, which requires greater accuracy.
            int leeway = 50;

            //started
            if (e.getAction() == 0) {
                selectBitmap(leeway);
            }
            //let go
            else if (e.getAction() == 1) {
                if (!isSnapped) {
                    //   Log.d(Consts.PACKAGE_NAME, "this is the selected bitmap " + selectedBitmap);
                    if (selectedBitmap == null) {
                        return true;
                    }
                    bitmaps.get(selectedBitmap).getMatrix().setTranslate(x, y);
                }
            }
            //dragging
            else if (e.getAction() == 2 && selectedBitmap != null) {
                {

                    //the on and off blocks should be able to snap. More blocks should too
                    //TODO: an ELSE here should address snapping other things than just the off and on blocks. Such as device nodes etc.
                    //TODO: this needs to be iterated in order to accommodate more than one if-test
                    if (selectedBitmap == Consts.ON_BLOCK || selectedBitmap == Consts.OFF_BLOCK) {

                        //snapping to the top position
                        if (x <= bitmaps.get(Consts.IF_BLOCK).getX() + conditionOffsetX + leeway && x > bitmaps.get(Consts.IF_BLOCK).getX() + conditionOffsetX - leeway &&
                                y <= bitmaps.get(Consts.IF_BLOCK).getY() - conditionOffsetY + leeway && y > bitmaps.get(Consts.IF_BLOCK).getY() - conditionOffsetY - leeway) {

                            isSnapped = true;
                            bitmaps.get(Consts.IF_BLOCK).setConnectedDevice1(bitmaps.get(selectedBitmap));
                            bitmaps.get(selectedBitmap).setActive(true);
                            //snapping to fixed position.
                            //bitmaps.get(selectedBitmap).getMatrix().setTranslate(bitmaps.get(IF_BLOCK).getX() + 144, bitmaps.get(IF_BLOCK).getY() - 20);
                            bitmaps.get(selectedBitmap).getMatrix().setTranslate(bitmaps.get(Consts.IF_BLOCK).getX() + conditionOffsetX, bitmaps.get(Consts.IF_BLOCK).getY() - conditionOffsetY);

                            //snapping to then lower (then) position
                        } else if (x <= bitmaps.get(Consts.IF_BLOCK).getX() + thenConditionOffsetX + leeway && x > bitmaps.get(Consts.IF_BLOCK).getX() + thenConditionOffsetX - leeway &&
                                y <= bitmaps.get(Consts.IF_BLOCK).getY() - thenConditionOffsetY + leeway && y > bitmaps.get(Consts.IF_BLOCK).getY() - thenConditionOffsetY - leeway) {
                            isSnapped = true;
                            bitmaps.get(Consts.IF_BLOCK).setConnectedDevice2(bitmaps.get(selectedBitmap));
                            bitmaps.get(selectedBitmap).setActive(true);
                            //snapping to fixed position.
                            bitmaps.get(selectedBitmap).getMatrix().setTranslate(bitmaps.get(Consts.IF_BLOCK).getX() + thenConditionOffsetX, bitmaps.get(Consts.IF_BLOCK).getY() - thenConditionOffsetY);

                        } else {
                            bitmaps.get(selectedBitmap).setActive(false);
                            isSnapped = false;
                            bitmaps.get(selectedBitmap).getMatrix().setTranslate(x, y);

                            //clear the fuck ou of this shit
                            for (Map.Entry<String, BitmapBlock> entry : bitmaps.entrySet()) {
                                String key = entry.getKey();
                                BitmapBlock block = entry.getValue();
                                //if this crashes, there could be files in the drawables folder which is not used.
                                if (block.getConnectedDevice1() == bitmaps.get(selectedBitmap)) {
                                    block.setConnectedDevice1(null);
                                }
                                if (block.getConnectedDevice2() == bitmaps.get(selectedBitmap)) {
                                    block.setConnectedDevice2(null);
                                }
                            }


                        }
                    } else if (selectedBitmap.startsWith("if")) {

                        for (Map.Entry<String, BitmapBlock> entry : bitmaps.entrySet()) {
                            String key = entry.getKey();
                            BitmapBlock block = entry.getValue();
                            //is the if block which we've iterated to the same if block that we've pressed?
                            if (block.getSrc() == selectedBitmap) {
                                block.getMatrix().setTranslate(x, y);
                                for (Map.Entry<String, BitmapBlock> en : bitmaps.entrySet()) {
                                    //inner loop variables
                                    String k = en.getKey();
                                    BitmapBlock b = en.getValue();

                                    //checking the upper on/off position
                                    if (block.getX() <= b.getX() + leeway + conditionOffsetX && realX > block.getX() - leeway - conditionOffsetX
                                            && block.getY() <= b.getY() + leeway + conditionOffsetY && block.getY() > b.getY() - leeway - conditionOffsetY && b.getSrc().startsWith("o") && b.getActive()) {
                                        selectedBitmap = block.getSrc();
                                        block.getMatrix().setTranslate(x, y);
                                        b.getMatrix().setTranslate(x + conditionOffsetX, y - conditionOffsetY);
                                    }

                                    //checking the lower 'than part' for an on/off block
                                    if (block.getConnectedDevice2() != null) {
                                        block.getConnectedDevice2().getMatrix().setTranslate(x + thenConditionOffsetX, y - thenConditionOffsetY);
                                    }
                                }
                            }
                        }
                    }
                }
            }
            this.invalidate();
            return true;
        }


        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            Paint paint = new Paint();
            paint.setStyle(Paint.Style.FILL);
            paint.setColor(Color.WHITE);
            canvas.drawPaint(paint);

            paint.setColor(Color.parseColor("#CD5C5C"));

            //not sure why we dont have to redraw the background... Neat nevertheless.
            //  rh.placeBackground(bitmaps, res);

            Point size = new Point();
            getWindowManager().getDefaultDisplay().getSize(size);


            final Paint transparentPaint = new Paint();
            transparentPaint.setARGB(128, 255, 255, 255);
            canvas.drawText(x + ", " + y, x, y, paint);

            //draw all elements in the bitmaps HashMap, which is everything there needs to be drawn.
            for (Map.Entry<String, BitmapBlock> entry : bitmaps.entrySet()) {
                String key = entry.getKey();
                BitmapBlock block = entry.getValue();
                //if this crashes, there could be files in the drawables folder which is not used.
                try {

                    canvas.drawBitmap(block.getBitmap(), block.getMatrix(), paint);
                } catch (NullPointerException ex) {
                    //Log.d(Consts.PACKAGE_NAME,"Asset " + block.getSrc() + " is unused, consider removing...");
                    Log.d(Consts.PACKAGE_NAME, "shit's null, yo " + key);
                }
            }
            paint.setTextSize(40);
            canvas.drawText(Math.round(realX) + "," + Math.round(realY), realX, realY, paint);
        }


        private void selectBitmap(int leeway) {

            if (realX > 0 && realX < 640 && realY > 510 && realY < 720) {
                Log.d(Consts.PACKAGE_NAME, "You pressed somewhere to spawn a new piece");
                rh.spawnNewPiece(realX, realY, bitmaps, res);
                selectedBitmap = null;
                return;
            }


            boolean hit = false;
            for (Map.Entry<String, BitmapBlock> entry : bitmaps.entrySet()) {
                String key = entry.getKey();
                BitmapBlock block = entry.getValue();
                //if this crashes, there could be files in the drawables folder which is not used.
                try {
                    if (realX <= block.getX() + leeway && realX > block.getX() - leeway &&
                            realY <= block.getY() + leeway && realY > block.getY() - leeway) {
                        selectedBitmap = block.getSrc();
                        block.getMatrix().setTranslate(x, y);
                        hit = true;
                    }


                } catch (NullPointerException ex) {
                    //Log.d(Consts.PACKAGE_NAME,"Asset " + block.getSrc() + " is unused, consider removing...");
                    Log.d(Consts.PACKAGE_NAME, "shit's null, yo " + key);
                }
                if (!hit) {
                    selectedBitmap = null;
                }
            }
        }


    }
}