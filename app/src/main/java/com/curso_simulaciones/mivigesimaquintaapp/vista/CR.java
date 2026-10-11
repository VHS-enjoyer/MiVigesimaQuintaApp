package com.curso_simulaciones.mivigesimaquintaapp.vista;

public class CR {
    public static float anchoPizarra;
    public static float altoPizarra;

    public CR() {
    }

    /** Convierte porcentaje de posición en X a pixeles. */
    public static float pcApxX(float pcX) {
        float pxX = pcX * anchoPizarra / 100f;
        return pxX;
    }

    /** Convierte porcentaje de posición en Y a pixeles. */
    public static float pcApxY(float pcY) {
        float pxY = pcY * altoPizarra / 100f;
        return pxY;
    }

    /**
     * Dada una longitud pcL en porcentaje referido al menor entre
     * el ancho y el alto de Pizarra la convierte a pixeles.
     */
    public static float pcApxL(float pcL) {
        float pxL = 0;
        if (anchoPizarra > altoPizarra) {
            pxL = pcL * altoPizarra / 100f;
        } else {
            pxL = pcL * anchoPizarra / 100f;
        }
        return pxL;
    }

    /** Convierte pixeles de una posición en X a porcentaje. */
    public static float pxXApc(float pxX) {
        float pcX = pxX * 100f / anchoPizarra;
        return pcX;
    }

    /** Convierte pixeles de una posición en Y a porcentaje. */
    public static float pxYApc(float pxY) {
        float pcY = pxY * 100f / altoPizarra;
        return pcY;
    }

    /**
     * Dada una longitud pxL en pixeles la convierte a porcentaje
     * referido al menor entre el ancho y el alto.
     */
    public static float pxApcL(float pxL) {
        float pcL = 0;
        if (CR.anchoPizarra > altoPizarra) {
            pcL = pxL * 100f / altoPizarra;
        } else {
            pcL = pxL * 100f / anchoPizarra;
        }
        return pcL;
    }
}