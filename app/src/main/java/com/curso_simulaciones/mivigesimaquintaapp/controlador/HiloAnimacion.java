package com.curso_simulaciones.mivigesimaquintaapp.controlador;

import com.curso_simulaciones.mivigesimaquintaapp.datos.AlmacenDatosRAM;
import com.curso_simulaciones.mivigesimaquintaapp.modelo.ModeloFisico;
import com.curso_simulaciones.mivigesimaquintaapp.vista.CR;

public class HiloAnimacion extends Thread {
    public boolean pausa = true;
    private boolean corriendo;
    private long periodo_muestreo = 50; // Período de actualización en ms
    public float tiempo = 0;
    private ModeloFisico modelo = new ModeloFisico();
    private ActividadControladora actividad;

    public HiloAnimacion(ActividadControladora actividad) {
        this.actividad = actividad;
    }

    public void detener() {
        corriendo = false;
    }

    @Override
    public void run() {
        corriendo = true;
        while (corriendo) {
            try {
                Thread.sleep(periodo_muestreo);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (!pausa) {
                tiempo = tiempo + 0.02f;
                setCalculos(tiempo);
                actualizarFisica();
            }
        }
    }

    private void setCalculos(float tiempo) {
        float m1 = AlmacenDatosRAM.m1;
        float m2 = AlmacenDatosRAM.m2;
        float m3 = AlmacenDatosRAM.m3;
        modelo.setCalculos(tiempo, m1, m2, m3);
    }

    private void actualizarFisica() {
        actividad.cambiarEstadosEscenaPizarra();

        // Límites de colisión:
        // 1. Choque contra el piso (~80% del alto)
        boolean m1_piso = AlmacenDatosRAM.y1_en_pixeles > CR.pcApxY(80.0f);
        boolean m2_piso = AlmacenDatosRAM.y2_en_pixeles > CR.pcApxY(80.0f);
        boolean m3_piso = AlmacenDatosRAM.y3_en_pixeles > CR.pcApxY(80.0f);

        // 2. Choque de m3 o polea P con las poleas fijas superiores (~15% del alto)
        boolean m3_tope = AlmacenDatosRAM.y3_en_pixeles < CR.pcApxY(15.0f);
        boolean poleaP_tope = AlmacenDatosRAM.yP_en_pixeles < CR.pcApxY(15.0f);

        // 3. Choque de m1 o m2 contra la polea móvil P
        boolean m1_topeP = AlmacenDatosRAM.y1_en_pixeles < (AlmacenDatosRAM.yP_en_pixeles + CR.pcApxL(2.0f));
        boolean m2_topeP = AlmacenDatosRAM.y2_en_pixeles < (AlmacenDatosRAM.yP_en_pixeles + CR.pcApxL(2.0f));

        // Si alguno de los bloques o la polea móvil alcanza un límite, se reinicia la animación
        if (m1_piso || m2_piso || m3_piso || m3_tope || poleaP_tope || m1_topeP || m2_topeP) {
            tiempo = 0.0f;
        }
    }
}