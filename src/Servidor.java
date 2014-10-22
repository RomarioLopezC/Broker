import java.util.ArrayList;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Romario
 */
public class Servidor {

    private ChartFrame frame;
    private JFreeChart chart;
    private DefaultCategoryDataset data;

    public Servidor() {
        this.data = new DefaultCategoryDataset();
        chart = ChartFactory.createBarChart(
                "Gráfica de barras.",
                "Candidatos",
                "Votos Obtenidos",
                data,
                PlotOrientation.VERTICAL,
                true,
                true,
                false);
        frame = new ChartFrame("Vista", chart);
        frame.pack();
        frame.setVisible(true);
    }

    public String letrasNumeros(ArrayList<Candidatos> candidatos) {
        for (Candidatos candidato : candidatos) {
            if (candidato != null) {
                data.setValue(candidato.getVotos(), "", candidato.getNombre());
            }
        }

        chart = ChartFactory.createBarChart(
                "Gráfica de barras.",
                "Candidatos",
                "Votos Obtenidos",
                data,
                PlotOrientation.VERTICAL,
                true,
                true,
                false);

        return "Solicitud procesada con éxito.";
    }

}
