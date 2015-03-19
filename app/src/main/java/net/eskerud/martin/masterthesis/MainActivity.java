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
        private BitmapBlock selectedBitmap;
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
            //  bitmaps = rh.populateBitmaps(bitmaps, res);
            //  rh.initBitmapPositions(res, bitmaps);
            bitmaps = rh.placeBackground(bitmaps, res);
        }

        @Override
        public boolean onTouchEvent(MotionEvent e) {
            realX = e.getX();
            realY = e.getY();
            x = mh.evaluateX(e.getX(), selectedBitmap);
            y = mh.evaluateY(e.getY(), selectedBitmap);

            //reset button
            if (realX > 1150 && realX < 1300 && realY > 0 && realY < 50) {
                bitmaps = rh.reset(bitmaps, res);
                Log.d("reset", "attempting to reset the application...");
                this.invalidate();
                return true;
            }


            //offset values for where on/off fits on the if-block.
            int onOffConditionOffsetX = 140;
            int onOffConditionOffsetY = 15;
            //offset values for where on/off fits on the then-part of the if-block
            int onOffThenConditionOffsetX = 140;
            int onOffThenConditionOffsetY = -124;


            //offset values for where on/off fits on the if-block.
            int equalsConditionOffsetX = 132;
            int equalsConditionOffsetY = -20;
            //offset values for where on/off fits on the then-part of the if-block
            int equalsThenConditionOffsetX = 90;
            int equalsThenConditionOffsetY = -180;

            //offset values for where on/off fits on the if-block.
            int deviceConditionOffsetX = 55;
            int deviceConditionOffsetY = -15;
            //offset values for where on/off fits on the then-part of the if-block
            int deviceThenConditionOffsetX = 10;
            int deviceThenConditionOffsetY = -175;


            //leeway is the error margin for pressing the element. SmallerLeeway is used for smaller items to be pressed, which requires greater accuracy.
            int selectionLeeway = 50;
            int snapLeeway = 35;
            //started
            if (e.getAction() == 0) {
                selectBitmap(selectionLeeway);
            }
            //let go
            else if (e.getAction() == 1) {

                /**
                 * TODO: this should delete the attached children too
                 */
                if (realX < 40 && realX >= -50 && realY > -50 && realY <= 70 && selectedBitmap != null) {
                    //we've hit delete.
                    //there are infinite dimentions - and each one except this has connectedDevices implemented as a list.
                    //TODO: Implement this as a list. Lord have mercy.
                    try {
                        bitmaps.remove(selectedBitmap.getConnectedDevice1().getId());
                    } catch (NullPointerException ex) {
                    }
                    try {
                        bitmaps.remove(selectedBitmap.getConnectedDevice2().getId());
                    } catch (NullPointerException ex) {
                    }
                    try {
                        bitmaps.remove(selectedBitmap.getConnectedDevice3().getId());
                    } catch (NullPointerException ex) {
                    }
                    try {
                        bitmaps.remove(selectedBitmap.getConnectedDevice4().getId());
                    } catch (NullPointerException ex) {
                    }
                    try {
                        bitmaps.remove(selectedBitmap.getConnectedDevice5().getId());
                    } catch (NullPointerException ex) {
                    }
                    try {
                        bitmaps.remove(selectedBitmap.getConnectedDevice6().getId());
                    } catch (NullPointerException ex) {
                    }
                    //  Log.d("asd", (bitmaps.get(selectedBitmap).getConnectedDevice1().getSrc()));

                    bitmaps.remove(selectedBitmap.getId());
                    selectedBitmap = null;
                    this.invalidate();
                    return true;
                }

                if (!isSnapped) {

                    if (selectedBitmap == null) {
                        return true;
                    }
                    selectedBitmap.getMatrix().setTranslate(x, y);
                }
            }
            //dragging
            else if (e.getAction() == 2 && selectedBitmap != null) {

                for (Map.Entry<String, BitmapBlock> hashEntry : bitmaps.entrySet()) {
                    BitmapBlock hashBlock = hashEntry.getValue();
                    if (selectedBitmap.getSrc().startsWith("o")) {
//                    if (selectedBitmap.getSrc() == Consts.ON_BLOCK || selectedBitmap.getSrc() == Consts.OFF_BLOCK) {

                        if (hashBlock.getSrc().startsWith("if") && hashBlock != null) {
                            //snapping to the top position
                            if (x <= hashBlock.getX() + onOffConditionOffsetX + snapLeeway && x > hashBlock.getX() + onOffConditionOffsetX - snapLeeway &&
                                    y <= hashBlock.getY() - onOffConditionOffsetY + snapLeeway && y > hashBlock.getY() - onOffConditionOffsetY - snapLeeway) {

                                isSnapped = true;

                                hashBlock.setConnectedDevice1(selectedBitmap);
                                selectedBitmap.setActive(true);
                                //snapping to fixed position.
                                selectedBitmap.getMatrix().setTranslate(hashBlock.getX() + onOffConditionOffsetX, hashBlock.getY() - onOffConditionOffsetY);
                                //breaking here, because otherwise we would go to the next "if block" and remove the progress we've made here.
                                break;
                                //snapping to then lower (then) position
                            } else if (x <= hashBlock.getX() + onOffThenConditionOffsetX + snapLeeway && x > hashBlock.getX() + onOffThenConditionOffsetX - snapLeeway &&
                                    y <= hashBlock.getY() - onOffThenConditionOffsetY + snapLeeway && y > hashBlock.getY() - onOffThenConditionOffsetY - snapLeeway) {
                                isSnapped = true;

                                hashBlock.setConnectedDevice2(selectedBitmap);
                                selectedBitmap.setActive(true);
                                //snapping to fixed position.
                                selectedBitmap.getMatrix().setTranslate(hashBlock.getX() + onOffThenConditionOffsetX, hashBlock.getY() - onOffThenConditionOffsetY);
                                //breaking here, because otherwise we would go to the next "if block" and remove the progress we've made here.
                                break;
                            } else {
                                selectedBitmap.setActive(false);
                                isSnapped = false;
                                selectedBitmap.getMatrix().setTranslate(x, y);

                                //clear the fuck out of this shit
                                for (Map.Entry<String, BitmapBlock> entry : bitmaps.entrySet()) {
                                    BitmapBlock block = entry.getValue();
                                    //if this crashes, there could be files in the drawables folder which is not used.
                                    try {
                                        if (block.getConnectedDevice1() == selectedBitmap) {
                                            block.setConnectedDevice1(null);
                                        }
                                        if (block.getConnectedDevice2() == selectedBitmap) {
                                            block.setConnectedDevice2(null);
                                        }
                                    } catch (NullPointerException ex) {

                                    }
                                }
                            }
                        }
                        selectedBitmap.setActive(false);
                        isSnapped = false;
                        selectedBitmap.getMatrix().setTranslate(x, y);
                    } else if (selectedBitmap.getSrc().startsWith("e") && selectedBitmap != null) {

                        if (hashBlock.getSrc().startsWith("if") && hashBlock != null) {
                            //snapping to the top position
                            if (x <= hashBlock.getX() + equalsConditionOffsetX + snapLeeway && x > hashBlock.getX() + equalsConditionOffsetX - snapLeeway &&
                                    y <= hashBlock.getY() - equalsConditionOffsetY + snapLeeway && y > hashBlock.getY() - equalsConditionOffsetY - snapLeeway) {

                                isSnapped = true;
                                hashBlock.setConnectedDevice4(selectedBitmap);
                                selectedBitmap.setActive(true);
                                //snapping to fixed position.
                                selectedBitmap.getMatrix().setTranslate(hashBlock.getX() + equalsConditionOffsetX, hashBlock.getY() - equalsConditionOffsetY);
                                //         Log.d("snapped", selectedBitmap.getId() + " is snapped to " + hashBlock.getId());
                                //breaking here, because otherwise we would go to the next "if block" and remove the progress we've made here.
                                break;
                                //snapping to then lower (then) position
                            } else if (x <= hashBlock.getX() + equalsThenConditionOffsetX + snapLeeway && x > hashBlock.getX() + equalsThenConditionOffsetX - snapLeeway &&
                                    y <= hashBlock.getY() - equalsThenConditionOffsetY + snapLeeway && y > hashBlock.getY() - equalsThenConditionOffsetY - snapLeeway) {

                                isSnapped = true;
                                //      Log.d("snapped", selectedBitmap.getId() + " is snapped to " + hashBlock.getId());
                                hashBlock.setConnectedDevice6(selectedBitmap);
                                selectedBitmap.setActive(true);
                                //snapping to fixed position.
                                selectedBitmap.getMatrix().setTranslate(hashBlock.getX() + equalsThenConditionOffsetX, hashBlock.getY() - equalsThenConditionOffsetY);
                                //breaking here, because otherwise we would go to the next "if block" and remove the progress we've made here.
                                break;
                            } else {
                                selectedBitmap.setActive(false);
                                isSnapped = false;
                                selectedBitmap.getMatrix().setTranslate(x, y);

                                //clear the fuck out of this shit
                                for (Map.Entry<String, BitmapBlock> entry : bitmaps.entrySet()) {
                                    BitmapBlock block = entry.getValue();
                                    //if this crashes, there could be files in the drawables folder which are not used.
                                    try {
                                        if (block.getConnectedDevice4() == selectedBitmap) {
                                            block.setConnectedDevice4(null);
                                        }
                                        if (block.getConnectedDevice6() == selectedBitmap) {
                                            block.setConnectedDevice6(null);
                                        }
                                    } catch (NullPointerException ex) {

                                    }
                                }

                            }
                        }

                        selectedBitmap.setActive(false);
                        isSnapped = false;
                        selectedBitmap.getMatrix().setTranslate(x, y);

                    }

                    //device nodes. Starting with "c", obviously.
                    else if (selectedBitmap.getSrc().startsWith("c") && selectedBitmap != null) {

                        if (hashBlock.getSrc().startsWith("if") && hashBlock != null) {
                            //snapping to the top position
                            if (x <= hashBlock.getX() + deviceConditionOffsetX + snapLeeway && x > hashBlock.getX() + deviceConditionOffsetX - snapLeeway &&
                                    y <= hashBlock.getY() - deviceConditionOffsetY + snapLeeway && y > hashBlock.getY() - deviceConditionOffsetY - snapLeeway) {

                                isSnapped = true;
                                hashBlock.setConnectedDevice3(selectedBitmap);
                                selectedBitmap.setActive(true);
                                //snapping to fixed position.
                                selectedBitmap.getMatrix().setTranslate(hashBlock.getX() + deviceConditionOffsetX, hashBlock.getY() - deviceConditionOffsetY);
                                //          Log.d("snapped", selectedBitmap.getId() + " is snapped to " + hashBlock.getId());
                                //breaking here, because otherwise we would go to the next "if block" and remove the progress we've made here.
                                break;
                                //snapping to then lower (then) position
                            } else if (x <= hashBlock.getX() + deviceThenConditionOffsetX + snapLeeway && x > hashBlock.getX() + deviceThenConditionOffsetX - snapLeeway &&
                                    y <= hashBlock.getY() - deviceThenConditionOffsetY + snapLeeway && y > hashBlock.getY() - deviceThenConditionOffsetY - snapLeeway) {

                                isSnapped = true;
                                //           Log.d("snapped", selectedBitmap.getId() + " is snapped to " + hashBlock.getId());
                                hashBlock.setConnectedDevice5(selectedBitmap);
                                selectedBitmap.setActive(true);
                                //snapping to fixed position.
                                selectedBitmap.getMatrix().setTranslate(hashBlock.getX() + deviceThenConditionOffsetX, hashBlock.getY() - deviceThenConditionOffsetY);
                                //breaking here, because otherwise we would go to the next "if block" and remove the progress we've made here.
                                break;
                            } else {
                                selectedBitmap.setActive(false);
                                isSnapped = false;
                                selectedBitmap.getMatrix().setTranslate(x, y);
                                //this is called if we drag something away from a locked position
                                //clear the fuck out of this shit
                                for (Map.Entry<String, BitmapBlock> entry : bitmaps.entrySet()) {
                                    BitmapBlock block = entry.getValue();
                                    //if this crashes, there could be files in the drawables folder which are not used.
                                    try {
                                        if (block.getConnectedDevice3() == selectedBitmap) {
                                            block.setConnectedDevice3(null);
                                        }
                                        if (block.getConnectedDevice5() == selectedBitmap) {
                                            block.setConnectedDevice5(null);
                                        }
                                    } catch (NullPointerException ex) {
                                    }
                                }

                            }
                        }

                        selectedBitmap.setActive(false);
                        isSnapped = false;
                        selectedBitmap.getMatrix().setTranslate(x, y);

                    } else if (selectedBitmap.getSrc().startsWith("if") && selectedBitmap != null) {

                        for (Map.Entry<String, BitmapBlock> entry2 : bitmaps.entrySet()) {
                            BitmapBlock block2 = entry2.getValue();
                            //is the if block which we've iterated to the same if block that we've pressed?
                            if (block2 == selectedBitmap) {
                                block2.getMatrix().setTranslate(x, y);
                                for (Map.Entry<String, BitmapBlock> en : bitmaps.entrySet()) {
                                    BitmapBlock b = en.getValue();

                                    /*
                                    * A smarter man would've implemented a list a long time ago.
                                    * */
                                    if (selectedBitmap.getConnectedDevice1() == b && b != null) {
                                        b.getMatrix().setTranslate(x + onOffConditionOffsetX, y - onOffConditionOffsetY);
                                    }
                                    if (selectedBitmap.getConnectedDevice2() == b && b != null) {
                                        b.getMatrix().setTranslate(x + onOffThenConditionOffsetX, y - onOffThenConditionOffsetY);
                                    }
                                    if (selectedBitmap.getConnectedDevice3() == b && b != null) {
                                        b.getMatrix().setTranslate(x + deviceConditionOffsetX, y - deviceConditionOffsetY);
                                    }
                                    if (selectedBitmap.getConnectedDevice4() == b && b != null) {
                                        b.getMatrix().setTranslate(x + equalsConditionOffsetX, y - equalsConditionOffsetY);
                                    }
                                    if (selectedBitmap.getConnectedDevice5() == b && b != null) {
                                        b.getMatrix().setTranslate(x + deviceThenConditionOffsetX, y - deviceThenConditionOffsetY);
                                    }
                                    if (selectedBitmap.getConnectedDevice6() == b && b != null) {
                                        b.getMatrix().setTranslate(x + equalsThenConditionOffsetX, y - equalsThenConditionOffsetY);
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

            Point size = new Point();
            getWindowManager().getDefaultDisplay().getSize(size);
            final Paint transparentPaint = new Paint();
            transparentPaint.setARGB(128, 255, 255, 255);

            //draw the background should it exist.
            //TODO: Iterating here  is inefficient. This could be O(1), it is instead O(n). But there are deadlines and the FPS is fine.
            for (Map.Entry<String, BitmapBlock> entry : bitmaps.entrySet()) {
                BitmapBlock block = entry.getValue();
                //draw the if tests first, so they are on bottom.
                if (block.getId().startsWith("b")) {

                    //if this crashes, there could be files in the drawables folder which is not used.
                    try {

                        canvas.drawBitmap(block.getBitmap(), block.getMatrix(), paint);

                    } catch (NullPointerException ex) {
                    }
                }

            }
            for (Map.Entry<String, BitmapBlock> entry : bitmaps.entrySet()) {
                BitmapBlock block = entry.getValue();
                //draw the if tests first, so they are on bottom.
                if (block.getId().startsWith("i")) {

                    //if this crashes, there could be files in the drawables folder which is not used.
                    try {

                        canvas.drawBitmap(block.getBitmap(), block.getMatrix(), paint);
                    } catch (NullPointerException ex) {
                    }
                }

            }
            for (Map.Entry<String, BitmapBlock> entry : bitmaps.entrySet()) {
                BitmapBlock block = entry.getValue();
                if (!block.getId().startsWith("i") && !block.getId().startsWith("b")) {
                    //if this crashes, there could be files in the drawables folder which is not used.
                    try {

                        canvas.drawBitmap(block.getBitmap(), block.getMatrix(), paint);
                    } catch (NullPointerException ex) {
                    }
                }
            }
        }


        private void selectBitmap(int leeway) {

            /*
            * TODO: more blocks! This will become a bigger structure fo'sho.
            * */
            if (realX > 10 && realX <= 165 && realY < 700 && realY >= 530) {
                bitmaps.put(Consts.IF_BLOCK + bitmaps.size(), rh.spawnNewIf(realX, realY, bitmaps, res));
                for (Map.Entry<String, BitmapBlock> entry : bitmaps.entrySet()) {
                    selectedBitmap = entry.getValue();
                }
                return;
            } else if (realX > 298 && realX <= 380 && realY > 540 && realY <= 580) {

                bitmaps.put(Consts.ON_BLOCK + bitmaps.size(), rh.spawnNewOn(realX, realY, bitmaps, res));
                for (Map.Entry<String, BitmapBlock> entry : bitmaps.entrySet()) {
                    selectedBitmap = entry.getValue();
                }
                return;
            } else if (realX > 315 && realX <= 380 && realY > 610 && realY <= 660) {

                bitmaps.put(Consts.OFF_BLOCK + (bitmaps.size()), rh.spawnNewOff(realX, realY, bitmaps, res));
                for (Map.Entry<String, BitmapBlock> entry : bitmaps.entrySet()) {
                    selectedBitmap = entry.getValue();
                }
                return;
            } else if (realX > 500 && realX <= 630 && realY > 530 && realY <= 585) {

                bitmaps.put(Consts.TIMER_BLOCK + (bitmaps.size()), rh.spawnNewTimer(realX, realY, bitmaps, res));
                for (Map.Entry<String, BitmapBlock> entry : bitmaps.entrySet()) {
                    selectedBitmap = entry.getValue();
                }
                return;
            } else if (realX > 535 && realX <= 630 && realY > 610 && realY <= 650) {

                bitmaps.put(Consts.PERCENT_BLOCK + (bitmaps.size()), rh.spawnNewPercent(realX, realY, bitmaps, res));
                for (Map.Entry<String, BitmapBlock> entry : bitmaps.entrySet()) {
                    selectedBitmap = entry.getValue();
                }
                return;
            } else if (realX > 215 && realX <= 280 && realY > 660 && realY <= 700) {

                bitmaps.put(Consts.EQUALS_BLOCK + (bitmaps.size()), rh.spawnNewEquals(realX, realY, bitmaps, res));
                for (Map.Entry<String, BitmapBlock> entry : bitmaps.entrySet()) {
                    selectedBitmap = entry.getValue();
                }
                return;
            } else if (realX > 215 && realX <= 280 && realY > 600 && realY <= 650) {

                bitmaps.put(Consts.MORE_BLOCK + (bitmaps.size()), rh.spawnNewMore(realX, realY, bitmaps, res));
                for (Map.Entry<String, BitmapBlock> entry : bitmaps.entrySet()) {
                    selectedBitmap = entry.getValue();
                }
                return;
            } else if (realX > 215 && realX <= 280 && realY > 545 && realY <= 590) {


                bitmaps.put(Consts.LESS_BLOCK + (bitmaps.size()), rh.spawnNewLess(realX, realY, bitmaps, res));
                for (Map.Entry<String, BitmapBlock> entry : bitmaps.entrySet()) {
                    selectedBitmap = entry.getValue();
                }
                return;
            } else if (realX > 660 && realX <= 760 && realY > 190 && realY <= 270) {

                bitmaps.put(Consts.MOVEMENT_BLOCK + (bitmaps.size()), rh.spawnNewMovement(realX, realY, bitmaps, res));
                for (Map.Entry<String, BitmapBlock> entry : bitmaps.entrySet()) {
                    selectedBitmap = entry.getValue();
                }
                return;
            } else if ((realX > 800 && realX <= 900 && realY > 190 && realY <= 270) || (realX > 1010 && realX <= 1130 && realY > 415 && realY <= 515)) {

                bitmaps.put(Consts.LIGHT_BLOCK + (bitmaps.size()), rh.spawnNewLight(realX, realY, bitmaps, res));
                for (Map.Entry<String, BitmapBlock> entry : bitmaps.entrySet()) {
                    selectedBitmap = entry.getValue();
                }
                return;
            } else if (realX > 660 && realX <= 760 && realY > 400 && realY <= 500) {

                bitmaps.put(Consts.TV_BLOCK + (bitmaps.size()), rh.spawnNewTv(realX, realY, bitmaps, res));
                for (Map.Entry<String, BitmapBlock> entry : bitmaps.entrySet()) {
                    selectedBitmap = entry.getValue();
                }
                return;
            } else if (realX > 800 && realX <= 900 && realY > 400 && realY <= 500) {

                bitmaps.put(Consts.FIREPLACE_BLOCK + (bitmaps.size()), rh.spawnNewFireplace(realX, realY, bitmaps, res));
                for (Map.Entry<String, BitmapBlock> entry : bitmaps.entrySet()) {
                    selectedBitmap = entry.getValue();
                }
                return;
            } else if (realX > 660 && realX <= 760 && realY > 0 && realY <= 96) {

                bitmaps.put(Consts.CLOCK_BLOCK + (bitmaps.size()), rh.spawnNewClock(realX, realY, bitmaps, res));
                for (Map.Entry<String, BitmapBlock> entry : bitmaps.entrySet()) {
                    selectedBitmap = entry.getValue();
                }
                return;
            }


            for (Map.Entry<String, BitmapBlock> entry : bitmaps.entrySet()) {
                BitmapBlock block = entry.getValue();
                //if this crashes, there could be files in the drawables folder which is not used.
                try {
                    //select the piece, disable dragging the background.
                    if (realX <= block.getX() + leeway && realX > block.getX() - leeway &&
                            realY <= block.getY() + leeway && realY > block.getY() - leeway && block != bitmaps.get(Consts.BACKGROUND)) {
                        selectedBitmap = block;
                        selectedBitmap.getMatrix().setTranslate(x, y);
                        return;
                    }
                } catch (NullPointerException ex) {
                }
            }
            selectedBitmap = null;
        }
    }
}