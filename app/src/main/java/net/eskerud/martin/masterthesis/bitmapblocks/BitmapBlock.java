package net.eskerud.martin.masterthesis.bitmapblocks;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.util.Log;

import net.eskerud.martin.masterthesis.MainActivity;
import net.eskerud.martin.masterthesis.R;
import net.eskerud.martin.masterthesis.util.Consts;

/**
 * Created by Martin on 21.02.2015.
 */
public class BitmapBlock {


    private boolean active;
    private Matrix matrix;
    //source is the type of bitmap or node.
    private String src;
    private String id;
    private Bitmap bitmap;
    private BitmapBlock connectedDevice1;
    private BitmapBlock connectedDevice2;
    private BitmapBlock  connectedDevice3;
    private BitmapBlock  connectedDevice4;
    private BitmapBlock  connectedDevice5;
    private BitmapBlock  connectedDevice6;
    private float height;
    private float width;


    public BitmapBlock(Matrix matrix, String src, Resources res, int num) {
        this.matrix = matrix;
        this.src = src;
        this.id=src+num;
        active = false;
        int imageResource = res.getIdentifier(src, null, Consts.PACKAGE_NAME);
        this.bitmap = BitmapFactory.decodeResource(res, imageResource);
        connectedDevice1=null;
        connectedDevice2=null;
        connectedDevice3=null;
        connectedDevice4=null;
        connectedDevice5=null;
        connectedDevice6=null;
    }


    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public float getWidth() {
        return width;
    }

    public void setWidth(float width) {
        this.width = width;
    }


    public String getId() {
        return id;
    }



    public BitmapBlock getConnectedDevice1() {
        return connectedDevice1;
    }

    //The device snapped in place in the "if" piece
    public void setConnectedDevice1(BitmapBlock connectedDevice1) {
        this.connectedDevice1 = connectedDevice1;
    }


    public BitmapBlock getConnectedDevice2() {
        return connectedDevice2;
    }

    //the device snapped in place in the "than" block
    public void setConnectedDevice2(BitmapBlock connectedDevice2) {
        this.connectedDevice2 = connectedDevice2;
    }

    public BitmapBlock getConnectedDevice3() {
        return connectedDevice3;
    }

    public void setConnectedDevice3(BitmapBlock connectedDevice3) {
        this.connectedDevice3 = connectedDevice3;
    }

    public BitmapBlock getConnectedDevice4() {
        return connectedDevice4;
    }

    public void setConnectedDevice4(BitmapBlock connectedDevice4) {
        this.connectedDevice4 = connectedDevice4;
    }

    public BitmapBlock getConnectedDevice5() {
        return connectedDevice5;
    }

    public void setConnectedDevice5(BitmapBlock connectedDevice5) {
        this.connectedDevice5 = connectedDevice5;
    }

    public BitmapBlock getConnectedDevice6() {
        return connectedDevice6;
    }

    public void setConnectedDevice6(BitmapBlock connectedDevice6) {
        this.connectedDevice6 = connectedDevice6;
    }



    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    /*
    * x and y are found within the transformation matrix.
    * */
    public float getX() {
        float[] transformationMatrix = new float[9];
        this.matrix.getValues(transformationMatrix);
        return transformationMatrix[2];
    }

    public void setX(float x) {
        float[] transformationMatrix = new float[9];
        this.matrix.getValues(transformationMatrix);
        transformationMatrix[2] = x;
    }

    public float getY() {
        float[] transformationMatrix = new float[9];
        this.matrix.getValues(transformationMatrix);
        return transformationMatrix[5];
    }

    public void setY(float y) {
        float[] transformationMatrix = new float[9];
        this.matrix.getValues(transformationMatrix);
        transformationMatrix[5] = y;
    }

    public Matrix getMatrix() {
        return matrix;
    }

    public void setMatrix(Matrix matrix) {
        this.matrix = matrix;
    }

    public String getSrc() {
        return src;
    }

    public void setSrc(String src) {
        this.src = src;
    }


    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public boolean getActive() {
        return active;
    }



}

