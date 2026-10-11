package com.curso_simulaciones.mivigesimaquintaapp.modelo;

import com.curso_simulaciones.mivigesimaquintaapp.datos.AlmacenDatosRAM;
import com.curso_simulaciones.mivigesimaquintaapp.vista.CR;

public class ModeloFisico {
    // Aceleración de la gravedad en m/s²
    private float g = 9.81f;

    private float factorConversion_metroApixel, factorConversion_pixelAmetro;

    // Desplazamientos en píxeles de las 3 masas y la polea móvil P
    private float desplazamiento_m1_en_pixeles, desplazamiento_m2_en_pixeles, desplazamiento_m3_en_pixeles, desplazamiento_P_en_pixeles;

    // Posiciones en Y en píxeles
    private float y1_en_pixeles, y2_en_pixeles, y3_en_pixeles, yP_en_pixeles;

    public ModeloFisico() {
    }

    /*
    Dados los valores de las 3 masas y el tiempo, calcula posiciones,
    aceleraciones, tensiones (T1, T2) y el desplazamiento angular de las poleas
    */
    public void setCalculos(float tiempo, float m1, float m2, float m3) {
        factorConversion();

        // Denominador común de la solución analítica
        float denominador = m2 * m3 + m1 * m3 + 4.0f * m1 * m2;

        // Cálculo de las tensiones en las cuerdas (N)
        float T1 = (4.0f * m1 * m2 * m3 * g) / denominador;
        float T2 = 2.0f * T1; // Cuerda principal soporta el doble de tensión

        // Aceleraciones en m/s² (dirección +Y hacia abajo)
        float a1 = g * (1.0f - (4.0f * m2 * m3) / denominador);
        float a2 = g * (1.0f - (4.0f * m1 * m3) / denominador);
        float a3 = g * (1.0f - (8.0f * m1 * m2) / denominador);
        float aP = -a3; // Restricción cinemática de la cuerda principal

        // Desplazamientos traslacionales de las masas y la polea móvil en metros
        float desplazamiento_m1_en_metros = 0.5f * a1 * tiempo * tiempo;
        float desplazamiento_m2_en_metros = 0.5f * a2 * tiempo * tiempo;
        float desplazamiento_m3_en_metros = 0.5f * a3 * tiempo * tiempo;
        float desplazamiento_P_en_metros  = 0.5f * aP * tiempo * tiempo;

        // Almacenar desplazamientos en metros
        AlmacenDatosRAM.desplazamiento_m1_en_metros = desplazamiento_m1_en_metros;
        AlmacenDatosRAM.desplazamiento_m2_en_metros = desplazamiento_m2_en_metros;
        AlmacenDatosRAM.desplazamiento_m3_en_metros = desplazamiento_m3_en_metros;
        AlmacenDatosRAM.desplazamiento_P_en_metros  = desplazamiento_P_en_metros;

        // Convertir desplazamientos de metros a píxeles
        desplazamiento_m1_en_pixeles = factorConversion_metroApixel * desplazamiento_m1_en_metros;
        desplazamiento_m2_en_pixeles = factorConversion_metroApixel * desplazamiento_m2_en_metros;
        desplazamiento_m3_en_pixeles = factorConversion_metroApixel * desplazamiento_m3_en_metros;
        desplazamiento_P_en_pixeles  = factorConversion_metroApixel * desplazamiento_P_en_metros;

        AlmacenDatosRAM.desplazamiento_m1_en_pixeles = desplazamiento_m1_en_pixeles;
        AlmacenDatosRAM.desplazamiento_m2_en_pixeles = desplazamiento_m2_en_pixeles;
        AlmacenDatosRAM.desplazamiento_m3_en_pixeles = desplazamiento_m3_en_pixeles;
        AlmacenDatosRAM.desplazamiento_P_en_pixeles  = desplazamiento_P_en_pixeles;

        // Posiciones iniciales en píxeles
        float yi1_en_pixeles = AlmacenDatosRAM.yi1_en_pixeles;
        float yi2_en_pixeles = AlmacenDatosRAM.yi2_en_pixeles;
        float yi3_en_pixeles = AlmacenDatosRAM.yi3_en_pixeles;
        float yiP_en_pixeles = AlmacenDatosRAM.yiP_en_pixeles;

        // Posiciones actuales de las masas y polea móvil en píxeles
        y1_en_pixeles = yi1_en_pixeles + desplazamiento_m1_en_pixeles;
        y2_en_pixeles = yi2_en_pixeles + desplazamiento_m2_en_pixeles;
        y3_en_pixeles = yi3_en_pixeles + desplazamiento_m3_en_pixeles;
        yP_en_pixeles = yiP_en_pixeles + desplazamiento_P_en_pixeles;

        AlmacenDatosRAM.y1_en_pixeles = y1_en_pixeles;
        AlmacenDatosRAM.y2_en_pixeles = y2_en_pixeles;
        AlmacenDatosRAM.y3_en_pixeles = y3_en_pixeles;
        AlmacenDatosRAM.yP_en_pixeles = yP_en_pixeles;

        // Posiciones en metros (respecto al origen Y=0 en las poleas fijas)
        AlmacenDatosRAM.y1_en_metros = factorConversion_pixelAmetro * (y1_en_pixeles - AlmacenDatosRAM.origenY_en_pixeles);
        AlmacenDatosRAM.y2_en_metros = factorConversion_pixelAmetro * (y2_en_pixeles - AlmacenDatosRAM.origenY_en_pixeles);
        AlmacenDatosRAM.y3_en_metros = factorConversion_pixelAmetro * (y3_en_pixeles - AlmacenDatosRAM.origenY_en_pixeles);
        AlmacenDatosRAM.yP_en_metros = factorConversion_pixelAmetro * (yP_en_pixeles - AlmacenDatosRAM.origenY_en_pixeles);

        // Rotación de las poleas fijas azules (B): theta_B = desplazamiento_m3 / R_B (en grados)
        float teta_B = 0f;
        if (AlmacenDatosRAM.radio_B_en_metros > 0) {
            teta_B = (float) Math.toDegrees(desplazamiento_m3_en_metros / AlmacenDatosRAM.radio_B_en_metros);
        }

        // Rotación de la polea móvil verde (P): theta_P = (desplazamiento_m1 - desplazamiento_P) / R_P (en grados)
        float teta_P = 0f;
        if (AlmacenDatosRAM.radio_P_en_metros > 0) {
            teta_P = (float) Math.toDegrees((desplazamiento_m1_en_metros - desplazamiento_P_en_metros) / AlmacenDatosRAM.radio_P_en_metros);
        }

        // Guardar todos los resultados en AlmacenDatosRAM
        AlmacenDatosRAM.m1 = m1;
        AlmacenDatosRAM.m2 = m2;
        AlmacenDatosRAM.m3 = m3;
        AlmacenDatosRAM.a1 = a1;
        AlmacenDatosRAM.a2 = a2;
        AlmacenDatosRAM.a3 = a3;
        AlmacenDatosRAM.aP = aP;
        AlmacenDatosRAM.T1 = T1;
        AlmacenDatosRAM.T2 = T2;
        AlmacenDatosRAM.teta_B = teta_B;
        AlmacenDatosRAM.teta_P = teta_P;
        AlmacenDatosRAM.tiempo = tiempo;
    }

    private void factorConversion() {
        /*
        El 100% del alto de la pantalla (en LANDSCAPE) equivale a 2 metros.
        factorConversion_metroApixel = (ALTO en píxeles / 2 metros)
        factorConversion_pixelAmetro = (2 metros / ALTO en píxeles)
        */
        factorConversion_metroApixel = CR.pcApxY(100f) / 2.0f;
        factorConversion_pixelAmetro = 2.0f / CR.pcApxY(100f);
    }
}