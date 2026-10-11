package com.curso_simulaciones.mivigesimaquintaapp.utilidades;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;

public class Boton extends AppCompatImageView {
    private Bitmap imagen;
    private String cadena = "";
    private final Paint pincel = new Paint();

    public Boton(Context context) {
        super(context);
    }

    public Boton(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public Boton(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setText(String cadena) {
        this.cadena = cadena;
        invalidate();
    }

    public String getText() {
        return cadena;
    }

    public void setImagen(int imagen_importada) {
        imagen = BitmapFactory.decodeResource(getResources(), imagen_importada);
        invalidate();
    }

    @Override
    protected void onDraw(@NonNull Canvas canvas) {
        super.onDraw(canvas);

        if (getWidth() == 0 || getHeight() == 0) return;

        pincel.reset();
        pincel.setAntiAlias(true);
        pincel.setLinearText(true);

        float tamamano_letra = 0.1f * getWidth();

        pincel.setTextSize(tamamano_letra);
        pincel.setColor(Color.BLACK);

        if (imagen != null) {
            Bitmap imagen_escalada = escalarImagen(imagen, (int) (0.8f * getHeight()), (int) (0.8f * getHeight()));

            if (imagen_escalada != null) {
                int ex = (getWidth() - imagen_escalada.getWidth()) / 2;
                int ey = (getHeight() - imagen_escalada.getHeight()) / 2;
                canvas.drawBitmap(imagen_escalada, ex, ey, null);

                float anchoCadenaUnidades = pincel.measureText(cadena);
                float posicion_x_letra = (getWidth() - anchoCadenaUnidades) / 2f;
                float posicion_y_letra = ey + imagen_escalada.getHeight() + tamamano_letra;
                canvas.drawText(cadena, posicion_x_letra, posicion_y_letra, pincel);
            }
        } else {
            float anchoCadenaUnidades = pincel.measureText(cadena);
            float posicion_x_letra = (getWidth() - anchoCadenaUnidades) / 2f;
            float posicion_y_letra = (getHeight() + tamamano_letra) / 2f;
            canvas.drawText(cadena, posicion_x_letra, posicion_y_letra, pincel);
        }
    }

    public Bitmap escalarImagen(Bitmap bitmap, int newWidth, int newHeight) {
        if (bitmap == null || newWidth <= 0 || newHeight <= 0) return null;

        Bitmap.Config config = bitmap.getConfig();
        if (config == null) {
            config = Bitmap.Config.ARGB_8888;
        }
        Bitmap scaledBitmap = Bitmap.createBitmap(newWidth, newHeight, config);
        float scaleX = newWidth / (float) bitmap.getWidth();
        float scaleY = newHeight / (float) bitmap.getHeight();
        Matrix scaleMatrix = new Matrix();
        scaleMatrix.setScale(scaleX, scaleY, 0f, 0f);
        Canvas canvas = new Canvas(scaledBitmap);
        canvas.setMatrix(scaleMatrix);
        Paint paint = new Paint(Paint.FILTER_BITMAP_FLAG);
        paint.setAntiAlias(true);
        paint.setDither(true);
        paint.setFilterBitmap(true);
        canvas.drawBitmap(bitmap, 0, 0, paint);
        return scaledBitmap;
    }
}