package com.curso_simulaciones.mivigesimaquintaapp.vista;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.curso_simulaciones.mivigesimaquintaapp.datos.AlmacenDatosRAM;
import com.curso_simulaciones.simulphysics.objetos_laboratorio.ObjetoLaboratorio;

import java.util.Locale;

public class Pizarra extends View {
    private ObjetoLaboratorio[] objetosLab;
    private float origen_x, origen_y;
    private float m_x = 1;
    private float m_y = 1;
    private final Paint paint = new Paint();

    public Pizarra(Context context) {
        super(context);
        init();
    }

    public Pizarra(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public Pizarra(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        paint.setAntiAlias(true);
    }

    public void setSistemaCoordenadas(float origen_x, float origen_y, float m_x, float m_y) {
        this.origen_x = origen_x;
        this.origen_y = origen_y;
        this.m_x = m_x;
        this.m_y = m_y;
    }

    public void setEstadoEscena(ObjetoLaboratorio[] cuerpos) {
        this.objetosLab = cuerpos;
    }

    private void dibujarEscena(Canvas canvas, Paint paint) {
        if (objetosLab != null) {
            for (ObjetoLaboratorio objeto : objetosLab) {
                if (objeto != null) {
                    objeto.dibujese(canvas, paint);
                }
            }
        }
    }

    @Override
    protected void onDraw(@NonNull Canvas canvas) {
        super.onDraw(canvas);

        paint.reset();
        paint.setAntiAlias(true);

        canvas.save();
        canvas.translate(origen_x, origen_y);
        canvas.scale(m_x, m_y);

        dibujarEscena(canvas, paint);

        canvas.restore();

        dibujarEjeCoordenado(canvas);
        dibujarDisplayValores(canvas);

        invalidate();
    }

    private void dibujarEjeCoordenado(Canvas canvas) {
        // Ubicar el eje Y en la parte derecha de la pantalla (78% del ancho),
        // a la derecha de m3 para no tapar los datos de la izquierda (2%).
        float xEje = CR.pcApxX(78f);

        // El origen Y=0 se alinea con las poleas fijas azules superiores
        float yInicio = AlmacenDatosRAM.origenY_en_pixeles > 0 ? AlmacenDatosRAM.origenY_en_pixeles : CR.pcApxY(20f);
        float yFin = yInicio + CR.pcApxY(55f);

        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.DKGRAY);
        paint.setStrokeWidth(3);

        // Marca de origen 0
        canvas.drawLine(xEje - 15, yInicio, xEje + 15, yInicio, paint);
        // Eje vertical Y+ hacia abajo
        canvas.drawLine(xEje, yInicio, xEje, yFin, paint);
        // Flecha apuntando hacia abajo
        canvas.drawLine(xEje, yFin, xEje - 10, yFin - 15, paint);
        canvas.drawLine(xEje, yFin, xEje + 10, yFin - 15, paint);

        // Texto de las etiquetas "0" y "Eje y"
        paint.setStyle(Paint.Style.FILL);
        paint.setTextSize(CR.pcApxL(2.2f));
        canvas.drawText("0", xEje + 20, yInicio + 10, paint);
        canvas.drawText("Eje y", xEje + 20, yFin, paint);

    }

    private void dibujarDisplayValores(Canvas canvas) {
        paint.setColor(Color.BLACK);
        paint.setTextSize(CR.pcApxL(2.0f));
        paint.setStyle(Paint.Style.FILL);

        float posX = CR.pcApxX(2);
        float posY = CR.pcApxY(4);
        float dy = CR.pcApxY(3.6f);

        canvas.drawText("VALORES FÍSICOS (SI):", posX, posY, paint); posY += dy;
        canvas.drawText(String.format(Locale.US, "m1 = %.2f kg", AlmacenDatosRAM.m1), posX, posY, paint); posY += dy;
        canvas.drawText(String.format(Locale.US, "m2 = %.2f kg", AlmacenDatosRAM.m2), posX, posY, paint); posY += dy;
        canvas.drawText(String.format(Locale.US, "m3 = %.2f kg", AlmacenDatosRAM.m3), posX, posY, paint); posY += dy;

        canvas.drawText(String.format(Locale.US, "a1 = %.2f m/s²", AlmacenDatosRAM.a1), posX, posY, paint); posY += dy;
        canvas.drawText(String.format(Locale.US, "a2 = %.2f m/s²", AlmacenDatosRAM.a2), posX, posY, paint); posY += dy;
        canvas.drawText(String.format(Locale.US, "a3 = %.2f m/s²", AlmacenDatosRAM.a3), posX, posY, paint); posY += dy;

        canvas.drawText(String.format(Locale.US, "Tensión T1 = %.2f N", AlmacenDatosRAM.T1), posX, posY, paint); posY += dy;
        canvas.drawText(String.format(Locale.US, "Tensión T2 = %.2f N", AlmacenDatosRAM.T2), posX, posY, paint); posY += dy;

        canvas.drawText(String.format(Locale.US, "y1 = %.3f m", AlmacenDatosRAM.y1_en_metros), posX, posY, paint); posY += dy;
        canvas.drawText(String.format(Locale.US, "y2 = %.3f m", AlmacenDatosRAM.y2_en_metros), posX, posY, paint); posY += dy;
        canvas.drawText(String.format(Locale.US, "y3 = %.3f m", AlmacenDatosRAM.y3_en_metros), posX, posY, paint); posY += dy;

        canvas.drawText(String.format(Locale.US, "Desplaz. m1 = %.3f m", AlmacenDatosRAM.desplazamiento_m1_en_metros), posX, posY, paint); posY += dy;
        canvas.drawText(String.format(Locale.US, "Desplaz. m2 = %.3f m", AlmacenDatosRAM.desplazamiento_m2_en_metros), posX, posY, paint); posY += dy;
        canvas.drawText(String.format(Locale.US, "Desplaz. m3 = %.3f m", AlmacenDatosRAM.desplazamiento_m3_en_metros), posX, posY, paint); posY += dy;

        canvas.drawText(String.format(Locale.US, "Tiempo = %.2f s", AlmacenDatosRAM.tiempo), posX, posY, paint);
    }
}