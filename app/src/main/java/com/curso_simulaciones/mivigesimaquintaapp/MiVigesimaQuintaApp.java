package com.curso_simulaciones.mivigesimaquintaapp;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.curso_simulaciones.mivigesimaquintaapp.controlador.ActividadControladora;
import com.curso_simulaciones.mivigesimaquintaapp.datos.AlmacenDatosRAM;
import com.curso_simulaciones.mivigesimaquintaapp.utilidades.Boton;

public class MiVigesimaQuintaApp extends Activity {
    private int tamanoLetraResolucionIncluida;
    private Boton entrar, salir;
    private ImageView imagen;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        gestionarResolucion();
        creacionElementosGui();

        ViewGroup.LayoutParams layout_principal_params = new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
        );

        this.setContentView(crearGUI(), layout_principal_params);
        eventos();
    }

    /**
     * Gestiona la resolución de pantalla e independiza la escala de letra
     */
    private void gestionarResolucion() {
        DisplayMetrics displayMetrics = this.getApplicationContext().getResources().getDisplayMetrics();
        int alto = displayMetrics.heightPixels;
        int ancho = displayMetrics.widthPixels;
        int dimensionReferencia;

        if (alto > ancho) {
            dimensionReferencia = ancho;
        } else {
            dimensionReferencia = alto;
        }

        int tamanoLetra = dimensionReferencia / 20;
        tamanoLetraResolucionIncluida = (int) (tamanoLetra / displayMetrics.scaledDensity);

        // Guardar dimensiones y tamaño de letra en RAM
        AlmacenDatosRAM.tamanoLetraResolucionIncluida = tamanoLetraResolucionIncluida;
        AlmacenDatosRAM.ancho_pantalla = ancho;
        AlmacenDatosRAM.alto_pantalla = alto;
    }

    /**
     * Creación de elementos GUI de la portada
     */
    private void creacionElementosGui() {
        entrar = new Boton(this);
        entrar.setImagen(R.drawable.entrar);
        entrar.setText(" ");

        salir = new Boton(this);
        salir.setImagen(R.drawable.salir);
        salir.setText(" ");

        imagen = new ImageView(this);
        imagen.setImageResource(R.drawable.imagen_entrada_app_25);
    }

    /**
     * Construcción de la interfaz gráfica
     */
    private LinearLayout crearGUI() {
        // Layout Principal Vertical
        LinearLayout linear_layout_principal = new LinearLayout(this);
        linear_layout_principal.setOrientation(LinearLayout.VERTICAL);
        linear_layout_principal.setGravity(Gravity.CENTER_HORIZONTAL);
        linear_layout_principal.setGravity(Gravity.FILL);
        linear_layout_principal.setBackgroundColor(Color.WHITE);
        linear_layout_principal.setWeightSum(10);

        // Primera Fila (Imagen de Portada) - Peso 8.0
        LinearLayout linear_layout_primera_fila = new LinearLayout(this);
        linear_layout_primera_fila.setOrientation(LinearLayout.HORIZONTAL);
        linear_layout_primera_fila.setGravity(Gravity.FILL);
        linear_layout_primera_fila.setBackgroundColor(Color.WHITE);

        LinearLayout.LayoutParams parametros_primera_fila = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, 0
        );
        parametros_primera_fila.weight = 8.0f;
        linear_layout_primera_fila.setLayoutParams(parametros_primera_fila);

        // Segunda Fila (Botones) - Peso 2.0
        LinearLayout linear_layout_segunda_fila = new LinearLayout(this);
        linear_layout_segunda_fila.setOrientation(LinearLayout.HORIZONTAL);
        linear_layout_segunda_fila.setGravity(Gravity.FILL);

        LinearLayout.LayoutParams parametros_segunda_fila = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, 0
        );
        parametros_segunda_fila.weight = 2.0f;
        linear_layout_segunda_fila.setWeightSum(2);
        linear_layout_segunda_fila.setLayoutParams(parametros_segunda_fila);

        // Colocar Imagen en primera fila
        LinearLayout.LayoutParams parametros_pegado_imagen = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
        );
        linear_layout_primera_fila.setGravity(Gravity.CENTER);
        imagen.setLayoutParams(parametros_pegado_imagen);
        linear_layout_primera_fila.addView(imagen);

        // Colocar Botones Entrar y Salir en segunda fila
        LinearLayout.LayoutParams parametros_pegado_boton = new LinearLayout.LayoutParams(
                0, ViewGroup.LayoutParams.MATCH_PARENT
        );
        parametros_pegado_boton.weight = 1.0f;
        int borde = 10;
        parametros_pegado_boton.setMargins(borde, borde, borde, borde);

        entrar.setLayoutParams(parametros_pegado_boton);
        salir.setLayoutParams(parametros_pegado_boton);

        linear_layout_segunda_fila.addView(entrar);
        linear_layout_segunda_fila.addView(salir);

        linear_layout_principal.addView(linear_layout_primera_fila);
        linear_layout_principal.addView(linear_layout_segunda_fila);

        return linear_layout_principal;
    }

    /**
     * Configuración de eventos de los botones
     */
    private void eventos() {
        entrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lanzarEntrar();
            }
        });

        salir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lanzarSalir();
            }
        });
    }

    private void lanzarEntrar() {
        Intent intent = new Intent(this, ActividadControladora.class);
        startActivity(intent);
    }

    private void lanzarSalir() {
        this.finish();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onDestroy() {
        finish();
        super.onDestroy();
    }
}