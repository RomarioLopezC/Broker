import java.util.ArrayList;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Lalo
 */
public class Servidor {

    private ChartFrame frame;
    private JFreeChart chart;
    private DefaultCategoryDataset datosBarras;
    private DefaultPieDataset datosPastel;

    public Servidor() {
        System.out.println("inicializando Servidor.");
        inicializarGraficosPastel();
        inicializarGraficosBarras();

    }

    private void inicializarGraficosBarras() {
        datosBarras = new DefaultCategoryDataset();
        chart = ChartFactory.createBarChart(
                "Gráfica de barras.",
                "Candidatos",
                "Votos Obtenidos",
                datosBarras,
                PlotOrientation.VERTICAL,
                true,
                true,
                false);
        frame = new ChartFrame("Vista", chart);
        frame.pack();
        frame.setVisible(true);

    }

    private void inicializarGraficosPastel() {
        datosPastel = new DefaultPieDataset();
        chart = ChartFactory.createPieChart(
                "Gráfica de Pastel.",
                datosPastel, true,
                true,
                false);
        frame = new ChartFrame("Vista", chart);
        frame.pack();
        frame.setVisible(true);
    }

    private boolean esMultiploDeTres(int num) {
     
        return ((num % 3) == 0);
    }

    public String graficarBarras(ArrayList<String> candidatos) {

        for (int i = 0; i < candidatos.size(); i++) {

            //recordar que el arrayList Está en múltiplos de 3:
            if (esMultiploDeTres(i + 1)) {
                datosBarras.setValue(Integer.parseInt(candidatos.get(i)), "", candidatos.get(i - 1));
            }
        }

        chart = ChartFactory.createBarChart(
                "Gráfica de barras.",
                "Candidatos",
                "Votos Obtenidos",
                datosBarras,
                PlotOrientation.VERTICAL,
                true,
                true,
                false);

        return "Terminar; Solicitud para Graficar Barras ha sido procesada con éxito.";
    }

    public String graficarPastel(ArrayList<String> candidatos) {
        for (int i = 0; i < candidatos.size(); i++) {

            //recordar que el arrayList Está en múltiplos de 3:
            if (esMultiploDeTres(i + 1)) {
                datosPastel.setValue(candidatos.get(i - 1), Integer.parseInt(candidatos.get(i)));
            }
        }
        //creando el gráfico.
        chart = ChartFactory.createPieChart(
                "Gráfica de pastel.",
                datosPastel,
                true,
                true,
                false);

        return "Terminar; Solicitud para Graficar Pastel ha sido procesada con éxito.";
    }

}
