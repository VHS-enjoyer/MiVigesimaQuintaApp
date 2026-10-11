package com.curso_simulaciones.mivigesimaquintaapp.datos;

public class AlmacenDatosRAM {
    // Dimensiones de pantalla y texto según resolución
    public static float ancho_pantalla, alto_pantalla;
    public static int tamanoLetraResolucionIncluida;

    // Masas en kg
    public static float m1, m2, m3;

    // Radio de las poleas en píxeles y metros (fijas azules B y móvil verde P)
    public static float radio_B_en_pixeles, radio_P_en_pixeles;
    public static float radio_B_en_metros, radio_P_en_metros;

    // Tiempo de simulación en segundos
    public static float tiempo;

    // Aceleraciones de m1, m2, m3 y polea móvil P en m/s²
    public static float a1, a2, a3, aP;

    // Tensiones en las cuerdas en N (T1: Cuerda secundaria, T2: Cuerda principal)
    public static float T1 = 0, T2 = 0;

    // Desplazamiento angular (radianes) de rotación de las poleas
    public static float teta_B, teta_P;

    // Origen de coordenadas
    public static float origenX_en_pixeles;
    public static float origenY_en_pixeles;
    public static float origenY_en_metros;

    // Posiciones en X y Y en píxeles
    public static float x1_en_pixeles, x2_en_pixeles, x3_en_pixeles, xP_en_pixeles;
    public static float y1_en_pixeles, y2_en_pixeles, y3_en_pixeles, yP_en_pixeles;

    // Posiciones en Y iniciales en píxeles
    public static float yi1_en_pixeles, yi2_en_pixeles, yi3_en_pixeles, yiP_en_pixeles;

    // Posiciones en Y iniciales y actuales en metros
    public static float yi1_en_metros, yi2_en_metros, yi3_en_metros, yiP_en_metros;
    public static float y1_en_metros, y2_en_metros, y3_en_metros, yP_en_metros;

    // Desplazamientos en metros
    public static float desplazamiento_m1_en_metros, desplazamiento_m2_en_metros, desplazamiento_m3_en_metros, desplazamiento_P_en_metros;

    // Desplazamientos en píxeles
    public static float desplazamiento_m1_en_pixeles, desplazamiento_m2_en_pixeles, desplazamiento_m3_en_pixeles, desplazamiento_P_en_pixeles;
}