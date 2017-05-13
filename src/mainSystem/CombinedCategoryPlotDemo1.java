/* ===========================================================
 * JFreeChart : a free chart library for the Java(tm) platform
 * ===========================================================
 *
 * (C) Copyright 2000-2013, by Object Refinery Limited and Contributors.
 *
 * Project Info:  http://www.jfree.org/jfreechart/index.html
 *
 * This library is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation; either version 2.1 of the License, or
 * (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public
 * License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301,
 * USA.
 *
 * [Oracle and Java are registered trademarks of Oracle and/or its affiliates. 
 * Other names may be trademarks of their respective owners.]
 *
 * ------------------------------
 * CombinedCategoryPlotDemo1.java
 * ------------------------------
 * (C) Copyright 2008, by Object Refinery Limited and Contributors.
 *
 * Original Author:  David Gilbert (for Object Refinery Limited);
 * Contributor(s):   ;
 *
 * Changes
 * -------
 * 05-May-2008 : Version 1 (DG);
 *
 */

package mainSystem;

import java.awt.Font;
import java.sql.Timestamp;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.labels.StandardCategoryToolTipGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.experimental.chart.plot.CombinedCategoryPlot;
import org.jfree.ui.ApplicationFrame;

/**
 * A demo for the {@link CombinedCategoryPlot} class.
 */
public class CombinedCategoryPlotDemo1 extends JFrame {
    private  String []timestamp=null;
    private double[] pm2_5=null;
    private int[] aqi=null;
    private static String title = "PM2.5监控系统—PM2.5及AQI数据走势图";
    /**
     * Creates a new demo instance.
     *
     * @param title  the frame title.
     */
    public CombinedCategoryPlotDemo1(String[]timestamp,double[]pm2_5,int[]aqi) {
        super(title);
        this.aqi=aqi;
        this.pm2_5=pm2_5;
        this.timestamp=timestamp;
        JPanel chartPanel = createDemoPanel();
        chartPanel.setPreferredSize(new java.awt.Dimension(800, 540));
        setContentPane(chartPanel);
        this.setIconImage(new ImageIcon("weatherIcon.png").getImage());
    }

    /**
     * Creates a dataset.
     *
     * @return A dataset.
     */
    public  CategoryDataset createDataset1() {
        DefaultCategoryDataset result = new DefaultCategoryDataset();
        String series1 = "PM2.5";
        String series2 = "AQI";
        
        for(int i=0;i<timestamp.length;i++){
        	result.addValue(pm2_5[i], series1, timestamp[i]);
        	result.addValue(aqi[i], series2, timestamp[i]);
        }
//        String type1 = "Type 1";
//        String type2 = "Type 2";
//        String type3 = "Type 3";
//        String type4 = "Type 4";
//        String type5 = "Type 5";
//        String type6 = "Type 6";
//        String type7 = "Type 7";
//        String type8 = "Type 8";
//
//        result.addValue(1.0, series1, type1);
//        result.addValue(4.0, series1, type2);
//        result.addValue(3.0, series1, type3);
//        result.addValue(5.0, series1, type4);
//        result.addValue(5.0, series1, type5);
//        result.addValue(7.0, series1, type6);
//        result.addValue(7.0, series1, type7);
//        result.addValue(8.0, series1, type8);
//
//        result.addValue(5.0, series2, type1);
//        result.addValue(7.0, series2, type2);
//        result.addValue(6.0, series2, type3);
//        result.addValue(8.0, series2, type4);
//        result.addValue(4.0, series2, type5);
//        result.addValue(4.0, series2, type6);
//        result.addValue(2.0, series2, type7);
//        result.addValue(1.0, series2, type8);

        return result;
    }

    /**
     * Creates a dataset.
     *
     * @return A dataset.
     */
    public CategoryDataset createDataset2() {

        DefaultCategoryDataset result = new DefaultCategoryDataset();

        String series1 = "PM2.5";
        String series2 = "AQI";
        
        for(int i=0;i<timestamp.length;i++){
        	result.addValue(pm2_5[i], series1, timestamp[i]);
        	result.addValue(aqi[i], series2, timestamp[i]);
        }
//        String type1 = "Type 1";
//        String type2 = "Type 2";
//        String type3 = "Type 3";
//        String type4 = "Type 4";
//        String type5 = "Type 5";
//        String type6 = "Type 6";
//        String type7 = "Type 7";
//        String type8 = "Type 8";
//
//        result.addValue(11.0, series1, type1);
//        result.addValue(14.0, series1, type2);
//        result.addValue(13.0, series1, type3);
//        result.addValue(15.0, series1, type4);
//        result.addValue(15.0, series1, type5);
//        result.addValue(17.0, series1, type6);
//        result.addValue(17.0, series1, type7);
//        result.addValue(18.0, series1, type8);
//
//        result.addValue(15.0, series2, type1);
//        result.addValue(17.0, series2, type2);
//        result.addValue(16.0, series2, type3);
//        result.addValue(18.0, series2, type4);
//        result.addValue(14.0, series2, type5);
//        result.addValue(14.0, series2, type6);
//        result.addValue(12.0, series2, type7);
//        result.addValue(11.0, series2, type8);

        return result;

    }

    /**
     * Creates a chart.
     *
     * @return A chart.
     */
    private JFreeChart createChart() {

        CategoryDataset dataset1 = createDataset1();
        NumberAxis rangeAxis1 = new NumberAxis("Value");
        rangeAxis1.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
        LineAndShapeRenderer renderer1 = new LineAndShapeRenderer();
        renderer1.setBaseToolTipGenerator(
                new StandardCategoryToolTipGenerator());
        CategoryPlot subplot1 = new CategoryPlot(dataset1, null, rangeAxis1,
                renderer1);
        subplot1.setDomainGridlinesVisible(true);

        CategoryDataset dataset2 = createDataset2();
        NumberAxis rangeAxis2 = new NumberAxis("Value");
        rangeAxis2.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
        BarRenderer renderer2 = new BarRenderer();
        renderer2.setBaseToolTipGenerator(
                new StandardCategoryToolTipGenerator());
        CategoryPlot subplot2 = new CategoryPlot(dataset2, null, rangeAxis2,
                renderer2);
        subplot2.setDomainGridlinesVisible(true);

        CategoryAxis domainAxis = new CategoryAxis("日期与时间");
        CombinedCategoryPlot plot = new CombinedCategoryPlot(
                domainAxis, new NumberAxis("取值"));
        plot.add(subplot1, 2);
        plot.add(subplot2, 1);

        JFreeChart result = new JFreeChart(
                "PM2.5及AQI数据走势图",
                new Font("SansSerif", Font.BOLD, 12), plot, true);
        return result;

    }

    /**
     * Creates a panel for the demo (used by SuperDemo.java).
     *
     * @return A panel.
     */
    public JPanel createDemoPanel() {
        JFreeChart chart = createChart();
        return new ChartPanel(chart);
    }

    /**
     * Starting point for the demonstration application.
     *
     * @param args  ignored.
     */
//    public static void main(String[] args) {
//        CombinedCategoryPlotDemo1 demo = new CombinedCategoryPlotDemo1(title);
//        demo.pack();
//        RefineryUtilities.centerFrameOnScreen(demo);
//        demo.setVisible(true);
//    }

}
