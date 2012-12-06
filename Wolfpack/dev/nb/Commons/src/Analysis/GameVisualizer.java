package Analysis;

import java.awt.Dimension;
import javax.swing.JFrame;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.*;

public class GameVisualizer {

    public long sleepInterval = 2000l;
    XYSeriesCollection balances;
    XYSeriesCollection rates;
    JFreeChart balanceChart;
    JFreeChart ratesChart;
    JFrame frame;
    
    int c = 0;
    int maxBalance = 20000;
    int minBalance = 20000;
    float maxRate = 1;
    float minRate = 0;

    public void initCharts(String name1,String name2) {
        initBalanceChart(name1,name2);
        initRatesChart(name1,name2);
    }
  
    public void initBalanceChart(String name1,String name2) {
        createFrame(name2, name1,0,513,800,512);        
        balances = new XYSeriesCollection();
        frame.add(createBalanceChart(name2, name1, balances));
        frame.pack();
        frame.setVisible(true);
    }
    
    public void initRatesChart(String name1,String name2) {
        createFrame(name2, name1,0,0,800,512);  
        createRatesChart(name2, name1);
        frame.pack();
        frame.setVisible(true);        
    }
    
    public void updateRatesChart(int gameID, float n, float n2) {
        rates.getSeries(0).add(gameID, n);
        rates.getSeries(1).add(gameID, n2);
        float maxCurrentRate=Math.max(n, n2);
        float minCurrentRate=Math.min(n, n2);
        maxRate=Math.max(maxCurrentRate, maxRate);
        minRate=Math.min(minCurrentRate, minRate);        
        ratesChart.getXYPlot().getRangeAxis().setRange(minRate,maxRate);
        ratesChart.fireChartChanged();
    }

    public void updateBalanceChart(int gameID, int balance, int balance2) {
        balances.getSeries(0).add(gameID, balance);
        balances.getSeries(1).add(gameID, balance2);
        int maxCurrentBalance=Math.max(balance, balance2);
        int minCurrentBalance=Math.min(balance, balance2);
        maxBalance=Math.max(maxCurrentBalance, maxBalance);
        minBalance=Math.min(minCurrentBalance, minBalance);
        balanceChart.getXYPlot().getRangeAxis().setRange(minBalance,maxBalance);
        balanceChart.fireChartChanged();
    }

    public void createFrame(String name2, String name1,int locationX,int locationY,int sizeX,int sizeY) {
        frame = new JFrame();
        frame.setLocation(locationX, locationY);
        frame.setPreferredSize(new Dimension(sizeX, sizeY));
        frame.setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);        
    }

    public void createRatesChart(String name2, String name1) {
        rates = new XYSeriesCollection();
        XYSeries xys1 = new XYSeries(name2), 
                xys2 = new XYSeries(name1);
        rates.addSeries(xys1);
        rates.addSeries(xys2);
        ratesChart = ChartFactory.createLineXYChart("Rate measurement", "Game ID", "rates",
                rates, PlotOrientation.VERTICAL, true, true, true);
        ratesChart.getXYPlot().getRangeAxis().setRange(minRate,maxRate);
        frame.add(new ChartPanel(ratesChart));
    }
    
    public ChartPanel createBalanceChart(String name2, String name1,XYSeriesCollection sc) {
        XYSeries xys1 = new XYSeries(name2), 
                xys2 = new XYSeries(name1);
        sc.addSeries(xys1);
        sc.addSeries(xys2);
        balanceChart = ChartFactory.createLineXYChart("Tournament Results", "Games", "Balance in $",
                sc, PlotOrientation.VERTICAL, true, true, true);
        balanceChart.getXYPlot().getRangeAxis().setRange(minBalance,maxBalance);
        return new ChartPanel(balanceChart);
    }    

}