package com.curso_simulaciones.mivigesimaquintaapp.controlador;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.curso_simulaciones.mivigesimaquintaapp.datos.AlmacenDatosRAM;
import com.curso_simulaciones.mivigesimaquintaapp.vista.CR;
import com.curso_simulaciones.mivigesimaquintaapp.vista.Pizarra;
import com.curso_simulaciones.simulphysics.objetos_laboratorio.Cuerda;
import com.curso_simulaciones.simulphysics.objetos_laboratorio.CuerpoRectangular;
import com.curso_simulaciones.simulphysics.objetos_laboratorio.Marca;
import com.curso_simulaciones.simulphysics.objetos_laboratorio.Masa;
import com.curso_simulaciones.simulphysics.objetos_laboratorio.ObjetoLaboratorio;
import com.curso_simulaciones.simulphysics.objetos_laboratorio.Polea;

import java.util.Locale;

public class ActividadControladora extends Activity {
    private int tamanoLetraResolucionIncluida;
    private Pizarra pizarra;

    // Elementos GUI de Control Lado Derecho
    private TextView text_m1, text_m2, text_m3, text_y3;
    private SeekBar seek_bar_m1, seek_bar_m2, seek_bar_m3, seek_bar_y3;
    private Button boton_empezar, boton_pausar;

    // Valores iniciales de entrada
    private float m1 = 10.0f;
    private float m2 = 10.0f;
    private float m3 = 20.0f;
    private float pos_y3_m = 0.8f; // En metros (0.5m a 1.5m)

    // Restricciones de longitud inextensible de las cuerdas (en metros)
    private final float LONGITUD_CUERDA_PRINCIPAL_M = 1.4f; // L2 = yP + y3
    private final float DIST_M1_A_POLEA_P_M = 0.25f;
    private final float DIST_M2_A_POLEA_P_M = 0.45f;

    // Geometría y dimensiones
    private float radio_polea_azul, radio_polea_p;
    private float ancho_masa, alto_masa;

    // Coordenadas fijas y móviles en píxeles
    private float x_polea_azul_1, y_polea_azul_1;
    private float x_polea_azul_2, y_polea_azul_2;
    private float x_polea_p, x_m1, x_m2, x_m3;
    private float y1_pixeles_ini, y2_pixeles_ini, y3_pixeles_ini, yP_pixeles_ini;

    // Objetos de laboratorio (simulphysics)
    private Masa masa_1, masa_2, masa_3;
    private Polea polea_azul_1, polea_azul_2, polea_verde;
    private CuerpoRectangular plataforma, base;
    private Cuerda cuerda_horizontal, cuerda_p_azul, cuerda_m1, cuerda_m2, cuerda_m3;
    private Marca marca_p, marca_m1, marca_m2, marca_m3;

    private final ObjetoLaboratorio[] objetos = new ObjetoLaboratorio[20];

    // Hilo de simulación
    private HiloAnimacion hilo;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        gestionarResolucion();
        crearElementosGUI();

        ViewGroup.LayoutParams parametro_layout_principal = new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
        );

        this.setContentView(crearGUI(), parametro_layout_principal);

        hilo = new HiloAnimacion(this);
        hilo.start();

        eventos();
    }

    private void gestionarResolucion() {
        tamanoLetraResolucionIncluida = (int) (0.7f * AlmacenDatosRAM.tamanoLetraResolucionIncluida);

        if (AlmacenDatosRAM.ancho_pantalla > AlmacenDatosRAM.alto_pantalla) {
            CR.anchoPizarra = 0.80f * AlmacenDatosRAM.ancho_pantalla;
            CR.altoPizarra = AlmacenDatosRAM.alto_pantalla;
        } else {
            CR.anchoPizarra = 0.80f * AlmacenDatosRAM.alto_pantalla;
            CR.altoPizarra = AlmacenDatosRAM.ancho_pantalla;
        }
    }

    private void crearElementosGUI() {
        // Masa M1 (5 a 20 kg)
        text_m1 = new TextView(this);
        text_m1.setTextSize(TypedValue.COMPLEX_UNIT_SP, tamanoLetraResolucionIncluida);
        text_m1.setGravity(Gravity.CENTER);
        text_m1.setTextColor(Color.BLACK);
        text_m1.setText("Masa M1: 10.0 kg\n(5 a 20 kg)");

        seek_bar_m1 = new SeekBar(this);
        seek_bar_m1.setMax(15);
        seek_bar_m1.setProgress(5);
        seek_bar_m1.setScaleY(0.6f);

        // Masa M2 (5 a 20 kg)
        text_m2 = new TextView(this);
        text_m2.setTextSize(TypedValue.COMPLEX_UNIT_SP, tamanoLetraResolucionIncluida);
        text_m2.setGravity(Gravity.CENTER);
        text_m2.setTextColor(Color.BLACK);
        text_m2.setText("Masa M2: 10.0 kg\n(5 a 20 kg)");

        seek_bar_m2 = new SeekBar(this);
        seek_bar_m2.setMax(15);
        seek_bar_m2.setProgress(5);
        seek_bar_m2.setScaleY(0.6f);

        // Masa M3 (10 a 40 kg)
        text_m3 = new TextView(this);
        text_m3.setTextSize(TypedValue.COMPLEX_UNIT_SP, tamanoLetraResolucionIncluida);
        text_m3.setGravity(Gravity.CENTER);
        text_m3.setTextColor(Color.BLACK);
        text_m3.setText("Masa M3: 20.0 kg\n(10 a 40 kg)");

        seek_bar_m3 = new SeekBar(this);
        seek_bar_m3.setMax(30);
        seek_bar_m3.setProgress(10);
        seek_bar_m3.setScaleY(0.6f);

        // Posición Inicial Y3 (0.5m a 1.5m)
        text_y3 = new TextView(this);
        text_y3.setTextSize(TypedValue.COMPLEX_UNIT_SP, tamanoLetraResolucionIncluida);
        text_y3.setGravity(Gravity.CENTER);
        text_y3.setTextColor(Color.BLACK);
        text_y3.setText("Posición Y3: 0.80 m\n(0.5 a 1.5 m)");

        seek_bar_y3 = new SeekBar(this);
        seek_bar_y3.setMax(100);
        seek_bar_y3.setProgress(30);
        seek_bar_y3.setScaleY(0.6f);

        // Botones
        boton_empezar = new Button(this);
        boton_empezar.setTextSize(TypedValue.COMPLEX_UNIT_SP, tamanoLetraResolucionIncluida);
        boton_empezar.setText("EMPEZAR");
        boton_empezar.getBackground().setColorFilter(Color.rgb(220, 156, 80), PorterDuff.Mode.MULTIPLY);

        boton_pausar = new Button(this);
        boton_pausar.setTextSize(TypedValue.COMPLEX_UNIT_SP, tamanoLetraResolucionIncluida);
        boton_pausar.setText("PAUSAR");
        boton_pausar.getBackground().setColorFilter(Color.rgb(220, 156, 80), PorterDuff.Mode.MULTIPLY);
        boton_pausar.setEnabled(false);

        // Pizarra de simulación
        pizarra = new Pizarra(this);
        pizarra.setBackgroundColor(Color.WHITE);
        pizarra.setSistemaCoordenadas(0, 0, 1, 1);

        crearObjetosLaboratorio();
        actualizarValoresIniciales();
    }

    private LinearLayout crearGUI() {
        LinearLayout linear_principal = new LinearLayout(this);
        linear_principal.setOrientation(LinearLayout.HORIZONTAL);
        linear_principal.setWeightSum(10.0f);

        LinearLayout linear_izquierda = new LinearLayout(this);
        linear_izquierda.setWeightSum(1.0f);

        LinearLayout linear_derecha = new LinearLayout(this);
        linear_derecha.setBackgroundColor(Color.parseColor("#4CAF50")); // Panel Verde
        linear_derecha.setWeightSum(10.0f);

        LinearLayout.LayoutParams parametros_pegado_izquierdo = new LinearLayout.LayoutParams(
                0, ViewGroup.LayoutParams.MATCH_PARENT
        );
        parametros_pegado_izquierdo.weight = 8.0f;
        linear_principal.addView(linear_izquierda, parametros_pegado_izquierdo);

        LinearLayout.LayoutParams parametros_pegado_derecho = new LinearLayout.LayoutParams(
                0, ViewGroup.LayoutParams.MATCH_PARENT
        );
        parametros_pegado_derecho.weight = 2.0f;
        linear_principal.addView(linear_derecha, parametros_pegado_derecho);

        linear_derecha.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams pComp = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, 0
        );
        pComp.setMargins(4, 2, 4, 2);
        pComp.weight = 1.0f;

        linear_derecha.addView(text_m1, pComp);
        linear_derecha.addView(seek_bar_m1, pComp);
        linear_derecha.addView(text_m2, pComp);
        linear_derecha.addView(seek_bar_m2, pComp);
        linear_derecha.addView(text_m3, pComp);
        linear_derecha.addView(seek_bar_m3, pComp);
        linear_derecha.addView(text_y3, pComp);
        linear_derecha.addView(seek_bar_y3, pComp);
        linear_derecha.addView(boton_empezar, pComp);
        linear_derecha.addView(boton_pausar, pComp);

        linear_izquierda.setOrientation(LinearLayout.VERTICAL);
        linear_izquierda.addView(pizarra);

        return linear_principal;
    }

    private void eventos() {
        boton_empezar.setOnClickListener(v -> {
            if ("EMPEZAR".equals(boton_empezar.getText().toString())) {
                hilo.pausa = false;
                seek_bar_m1.setEnabled(false);
                seek_bar_m2.setEnabled(false);
                seek_bar_m3.setEnabled(false);
                seek_bar_y3.setEnabled(false);
                boton_empezar.setText("NUEVO");
                boton_pausar.setText("PAUSAR");
                boton_pausar.setEnabled(true);
            } else {
                hilo.pausa = true;
                hilo.tiempo = 0;
                actualizarValoresIniciales();
                cambiarEstadosEscenaPizarra();
                seek_bar_m1.setEnabled(true);
                seek_bar_m2.setEnabled(true);
                seek_bar_m3.setEnabled(true);
                seek_bar_y3.setEnabled(true);
                boton_empezar.setText("EMPEZAR");
                boton_pausar.setEnabled(false);
            }
        });

        boton_pausar.setOnClickListener(v -> {
            if ("PAUSAR".equals(boton_pausar.getText().toString())) {
                boton_pausar.setText("CONTINUAR");
                hilo.pausa = true;
            } else {
                boton_pausar.setText("PAUSAR");
                hilo.pausa = false;
            }
        });

        seek_bar_m1.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                m1 = 5.0f + progress;
                text_m1.setText(String.format(Locale.US, "Masa M1: %.1f kg\n(5 a 20 kg)", m1));
                actualizarValoresIniciales();
            }

            @Override public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        seek_bar_m2.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                m2 = 5.0f + progress;
                text_m2.setText(String.format(Locale.US, "Masa M2: %.1f kg\n(5 a 20 kg)", m2));
                actualizarValoresIniciales();
            }

            @Override public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        seek_bar_m3.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                m3 = 10.0f + progress;
                text_m3.setText(String.format(Locale.US, "Masa M3: %.1f kg\n(10 a 40 kg)", m3));
                actualizarValoresIniciales();
            }

            @Override public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        seek_bar_y3.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                pos_y3_m = 0.5f + (progress * 0.01f);
                text_y3.setText(String.format(Locale.US, "Posición Y3: %.2f m\n(0.5 a 1.5 m)", pos_y3_m));
                actualizarValoresIniciales();
            }

            @Override public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override public void onStopTrackingTouch(SeekBar seekBar) {}
        });
    }

    private void crearObjetosLaboratorio() {
        // Radios de las poleas según la referencia visual
        radio_polea_azul = CR.pcApxL(4.5f);
        radio_polea_p = CR.pcApxL(3.5f);

        AlmacenDatosRAM.radio_B_en_pixeles = radio_polea_azul;
        AlmacenDatosRAM.radio_P_en_pixeles = radio_polea_p;

        // Dimensiones de las masas (rectángulos negros delgados)
        ancho_masa = CR.pcApxL(2.2f);
        alto_masa = CR.pcApxL(8.0f);

        // Base (Piso negro)
        float ancho_base = CR.pcApxL(70);
        float alto_base = CR.pcApxL(10);
        float x_base = CR.pcApxX(50);
        float y_base = CR.pcApxY(87);

        // Plataforma (Bloque amarillo)
        float ancho_plataforma = CR.pcApxL(36);
        float y_top_plataforma = CR.pcApxY(22);
        float y_bottom_plataforma = y_base - alto_base / 2;
        float alto_plataforma = y_bottom_plataforma - y_top_plataforma;

        float x_plataforma = CR.pcApxX(50);
        float y_plataforma = y_top_plataforma + alto_plataforma / 2;

        // Geometría del soporte de las poleas azules a -60°
        float x_vertice_izq = x_plataforma - ancho_plataforma / 2;
        float x_vertice_der = x_plataforma + ancho_plataforma / 2;
        float y_vertice = y_top_plataforma;

        float angulo_soporte = -60f;
        float angulo_rad = (float) Math.toRadians(angulo_soporte);
        float longitud_soporte = radio_polea_azul;

        float dx = longitud_soporte * (float) Math.cos(angulo_rad);
        float dy = longitud_soporte * (float) Math.sin(angulo_rad);

        x_polea_azul_1 = x_vertice_izq - 3 * dx;
        y_polea_azul_1 = y_vertice + dy;

        x_polea_azul_2 = x_vertice_der + 3 * dx;
        y_polea_azul_2 = y_vertice + dy;

        // Posiciones horizontales en X
        x_polea_p = x_polea_azul_1 - radio_polea_azul;
        x_m1 = x_polea_p - radio_polea_p;
        x_m2 = x_polea_p + radio_polea_p;
        x_m3 = x_polea_azul_2 + radio_polea_azul;

        int ind = 0;

        // Base
        base = new CuerpoRectangular(x_base, y_base, ancho_base, alto_base);
        base.setColor(Color.BLACK);
        objetos[ind++] = base;

        // Plataforma
        plataforma = new CuerpoRectangular(x_plataforma, y_plataforma, ancho_plataforma, alto_plataforma);
        plataforma.setColor(Color.YELLOW);
        objetos[ind++] = plataforma;

        // Poleas Fijas Azules
        polea_azul_1 = new Polea(x_polea_azul_1, y_polea_azul_1, radio_polea_azul);
        polea_azul_1.setColor(Color.BLUE);
        polea_azul_1.setGrosorLinea(CR.pcApxL(0.5f));
        polea_azul_1.setSoportePolea(true);
        polea_azul_1.rotarEje(angulo_soporte);
        objetos[ind++] = polea_azul_1;

        polea_azul_2 = new Polea(x_polea_azul_2, y_polea_azul_2, radio_polea_azul);
        polea_azul_2.setColor(Color.BLUE);
        polea_azul_2.setGrosorLinea(CR.pcApxL(0.5f));
        polea_azul_2.setSoportePolea(true);
        polea_azul_2.rotarEje(-angulo_soporte);
        objetos[ind++] = polea_azul_2;

        // Polea Verde P
        polea_verde = new Polea(x_polea_p, 0, radio_polea_p);
        polea_verde.setColor(Color.GREEN);
        polea_verde.setGrosorLinea(CR.pcApxL(0.5f));
        polea_verde.setSoportePolea(true);
        objetos[ind++] = polea_verde;

        // Cuerdas
        float grosor_cuerda = CR.pcApxL(0.4f);

        cuerda_horizontal = new Cuerda(x_polea_azul_1, y_polea_azul_1 - radio_polea_azul,
                x_polea_azul_2, y_polea_azul_2 - radio_polea_azul);
        cuerda_horizontal.setColor(Color.RED);
        cuerda_horizontal.setGrosorLinea(grosor_cuerda);
        objetos[ind++] = cuerda_horizontal;

        cuerda_p_azul = new Cuerda(x_polea_p, y_polea_azul_1, x_polea_p, 0);
        cuerda_p_azul.setColor(Color.RED);
        cuerda_p_azul.setGrosorLinea(grosor_cuerda);
        objetos[ind++] = cuerda_p_azul;

        cuerda_m1 = new Cuerda(x_m1, 0, x_m1, 0);
        cuerda_m1.setColor(Color.RED);
        cuerda_m1.setGrosorLinea(grosor_cuerda);
        objetos[ind++] = cuerda_m1;

        cuerda_m2 = new Cuerda(x_m2, 0, x_m2, 0);
        cuerda_m2.setColor(Color.RED);
        cuerda_m2.setGrosorLinea(grosor_cuerda);
        objetos[ind++] = cuerda_m2;

        cuerda_m3 = new Cuerda(x_m3, y_polea_azul_2, x_m3, 0);
        cuerda_m3.setColor(Color.RED);
        cuerda_m3.setGrosorLinea(grosor_cuerda);
        objetos[ind++] = cuerda_m3;

        // Masas
        masa_1 = new Masa(x_m1, 0, ancho_masa, alto_masa);
        masa_1.setColor(Color.BLACK);
        objetos[ind++] = masa_1;

        masa_2 = new Masa(x_m2, 0, ancho_masa, alto_masa);
        masa_2.setColor(Color.BLACK);
        objetos[ind++] = masa_2;

        masa_3 = new Masa(x_m3, 0, ancho_masa, alto_masa);
        masa_3.setColor(Color.BLACK);
        objetos[ind++] = masa_3;

        // Marcas / Etiquetas
        float tamano_texto = CR.pcApxL(2.8f);

        marca_p = new Marca("P", x_polea_p - CR.pcApxL(4.0f), 0);
        marca_p.setColor(Color.BLACK);
        marca_p.setTamano(tamano_texto);
        objetos[ind++] = marca_p;

        marca_m1 = new Marca("m1", x_m1 - CR.pcApxL(7f), 0);
        marca_m1.setColor(Color.BLACK);
        marca_m1.setTamano(tamano_texto);
        objetos[ind++] = marca_m1;

        marca_m2 = new Marca("m2", x_m2 - CR.pcApxL(7f), 0);
        marca_m2.setColor(Color.BLACK);
        marca_m2.setTamano(tamano_texto);
        objetos[ind++] = marca_m2;

        marca_m3 = new Marca("m3", x_m3 - CR.pcApxL(7f), 0);
        marca_m3.setColor(Color.BLACK);
        marca_m3.setTamano(tamano_texto);
        objetos[ind++] = marca_m3;

        pizarra.setEstadoEscena(objetos);
    }

    public void cambiarEstadosEscenaPizarra() {
        float dy1 = AlmacenDatosRAM.desplazamiento_m1_en_pixeles;
        float dy2 = AlmacenDatosRAM.desplazamiento_m2_en_pixeles;
        float dy3 = AlmacenDatosRAM.desplazamiento_m3_en_pixeles;
        float dyP = AlmacenDatosRAM.desplazamiento_P_en_pixeles;

        float teta_B = AlmacenDatosRAM.teta_B;
        float teta_P = AlmacenDatosRAM.teta_P;

        // Uso de rotar con 3 argumentos (xEje, yEje, angulo)
        polea_azul_1.rotar(x_polea_azul_1, y_polea_azul_1, teta_B);
        polea_azul_2.rotar(x_polea_azul_2, y_polea_azul_2, -teta_B);

        // Mover traslacionalmente (2 args) y rotar (3 args) polea verde
        polea_verde.mover(0, dyP);
        polea_verde.rotar(x_polea_p, yP_pixeles_ini + dyP, teta_P);

        // Mover masas
        masa_1.mover(0, dy1);
        masa_2.mover(0, dy2);
        masa_3.mover(0, dy3);

        float yP_curr = yP_pixeles_ini + dyP;
        float y1_curr = y1_pixeles_ini + dy1;
        float y2_curr = y2_pixeles_ini + dy2;
        float y3_curr = y3_pixeles_ini + dy3;

        cuerda_p_azul.setPosicionFinal(x_polea_p, yP_curr - radio_polea_p);

        cuerda_m1.setPosicionInicial(x_m1, yP_curr);
        cuerda_m1.setPosicionFinal(x_m1, y1_curr - alto_masa / 2);

        cuerda_m2.setPosicionInicial(x_m2, yP_curr);
        cuerda_m2.setPosicionFinal(x_m2, y2_curr - alto_masa / 2);

        cuerda_m3.setPosicionFinal(x_m3, y3_curr - alto_masa / 2);

        float tamano_texto = CR.pcApxL(2.8f);
        marca_p = new Marca("P", x_polea_p - CR.pcApxL(4.0f), yP_curr - radio_polea_p - CR.pcApxL(0.5f));
        marca_p.setColor(Color.BLACK);
        marca_p.setTamano(tamano_texto);
        objetos[13] = marca_p;

        marca_m1 = new Marca("m1", x_m1 - CR.pcApxL(7f), y1_curr);
        marca_m1.setColor(Color.BLACK);
        marca_m1.setTamano(tamano_texto);
        objetos[14] = marca_m1;

        marca_m2 = new Marca("m2", x_m2 - CR.pcApxL(7f), y2_curr);
        marca_m2.setColor(Color.BLACK);
        marca_m2.setTamano(tamano_texto);
        objetos[15] = marca_m2;

        marca_m3 = new Marca("m3", x_m3 - CR.pcApxL(7f), y3_curr);
        marca_m3.setColor(Color.BLACK);
        marca_m3.setTamano(tamano_texto);
        objetos[16] = marca_m3;
    }

    private void actualizarValoresIniciales() {
        AlmacenDatosRAM.m1 = m1;
        AlmacenDatosRAM.m2 = m2;
        AlmacenDatosRAM.m3 = m3;

        // Escala física: 100% alto de pantalla = 2m (40% = 1m en coordenadas relativas)
        float factor_m_px = CR.pcApxY(40.0f);

        // 1. Posición inicial m3 desde polea fija
        y3_pixeles_ini = y_polea_azul_2 + pos_y3_m * factor_m_px;

        // 2. Restricción cinemática cuerda principal L2 = yP + y3
        float pos_yP_m = LONGITUD_CUERDA_PRINCIPAL_M - pos_y3_m;
        if (pos_yP_m < 0.15f) pos_yP_m = 0.15f;
        yP_pixeles_ini = y_polea_azul_1 + pos_yP_m * factor_m_px;

        // 3. Restricción cinemática cuerda secundaria L1
        float pos_y1_m = pos_yP_m + DIST_M1_A_POLEA_P_M;
        float pos_y2_m = pos_yP_m + DIST_M2_A_POLEA_P_M;

        y1_pixeles_ini = y_polea_azul_1 + pos_y1_m * factor_m_px;
        y2_pixeles_ini = y_polea_azul_1 + pos_y2_m * factor_m_px;

        // Guardar posiciones e inicializar datos RAM
        AlmacenDatosRAM.origenY_en_pixeles = y_polea_azul_2;
        AlmacenDatosRAM.yi1_en_pixeles = y1_pixeles_ini;
        AlmacenDatosRAM.yi2_en_pixeles = y2_pixeles_ini;
        AlmacenDatosRAM.yi3_en_pixeles = y3_pixeles_ini;
        AlmacenDatosRAM.yiP_en_pixeles = yP_pixeles_ini;

        AlmacenDatosRAM.y1_en_pixeles = y1_pixeles_ini;
        AlmacenDatosRAM.y2_en_pixeles = y2_pixeles_ini;
        AlmacenDatosRAM.y3_en_pixeles = y3_pixeles_ini;
        AlmacenDatosRAM.yP_en_pixeles = yP_pixeles_ini;

        // Reconstrucción de la escena visual en estado inicial
        polea_verde = new Polea(x_polea_p, yP_pixeles_ini, radio_polea_p);
        polea_verde.setColor(Color.GREEN);
        polea_verde.setGrosorLinea(CR.pcApxL(0.5f));
        polea_verde.setSoportePolea(true);
        objetos[4] = polea_verde;

        if (cuerda_p_azul != null) {
            cuerda_p_azul.setPosicionFinal(x_polea_p, yP_pixeles_ini - radio_polea_p);
        }

        cuerda_m1 = new Cuerda(x_m1, yP_pixeles_ini, x_m1, y1_pixeles_ini - alto_masa / 2);
        cuerda_m1.setColor(Color.RED);
        cuerda_m1.setGrosorLinea(CR.pcApxL(0.4f));
        objetos[7] = cuerda_m1;

        cuerda_m2 = new Cuerda(x_m2, yP_pixeles_ini, x_m2, y2_pixeles_ini - alto_masa / 2);
        cuerda_m2.setColor(Color.RED);
        cuerda_m2.setGrosorLinea(CR.pcApxL(0.4f));
        objetos[8] = cuerda_m2;

        cuerda_m3 = new Cuerda(x_m3, y_polea_azul_2, x_m3, y3_pixeles_ini - alto_masa / 2);
        cuerda_m3.setColor(Color.RED);
        cuerda_m3.setGrosorLinea(CR.pcApxL(0.4f));
        objetos[9] = cuerda_m3;

        masa_1 = new Masa(x_m1, y1_pixeles_ini, ancho_masa, alto_masa);
        masa_1.setColor(Color.BLACK);
        objetos[10] = masa_1;

        masa_2 = new Masa(x_m2, y2_pixeles_ini, ancho_masa, alto_masa);
        masa_2.setColor(Color.BLACK);
        objetos[11] = masa_2;

        masa_3 = new Masa(x_m3, y3_pixeles_ini, ancho_masa, alto_masa);
        masa_3.setColor(Color.BLACK);
        objetos[12] = masa_3;

        float tamano_texto = CR.pcApxL(2.8f);

        marca_p = new Marca("P", x_polea_p - CR.pcApxL(4.0f), yP_pixeles_ini - radio_polea_p - CR.pcApxL(0.5f));
        marca_p.setColor(Color.BLACK);
        marca_p.setTamano(tamano_texto);
        objetos[13] = marca_p;

        marca_m1 = new Marca("m1", x_m1 - CR.pcApxL(7f), y1_pixeles_ini);
        marca_m1.setColor(Color.BLACK);
        marca_m1.setTamano(tamano_texto);
        objetos[14] = marca_m1;

        marca_m2 = new Marca("m2", x_m2 - CR.pcApxL(7f), y2_pixeles_ini);
        marca_m2.setColor(Color.BLACK);
        marca_m2.setTamano(tamano_texto);
        objetos[15] = marca_m2;

        marca_m3 = new Marca("m3", x_m3 - CR.pcApxL(7f), y3_pixeles_ini);
        marca_m3.setColor(Color.BLACK);
        marca_m3.setTamano(tamano_texto);
        objetos[16] = marca_m3;

        if (pizarra != null) {
            pizarra.setEstadoEscena(objetos);
            pizarra.invalidate();
        }
    }
}