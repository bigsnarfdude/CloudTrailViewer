/*    
CloudTrail Viewer, is a Java desktop application for reading AWS CloudTrail logs
files.

Copyright (C) 2015  Mark P. Haskins

This program is free software: you can redistribute it and/or modify it under the
terms of the GNU General Public License as published by the Free Software Foundation,
either version 3 of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful,but WITHOUT ANY 
WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A 
PARTICULAR PURPOSE.  See the GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/

package com.haskins.cloudtrailviewer.utils;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.ui.HorizontalAlignment;

/**
 * Utility class for creating a jFreeChart.
 * 
 * @author mark
 */
public class ChartFactory {
    
    /**
     * Using the passed values creates the appropriate chart
     * @param type Type of chart to create e.g. Pie
     * @param events Events to populate chart width
     * @param width preferred width of chart
     * @param height preferred height of chart
     * @param orientation orientation of bar, only used with Bar charts
     * @return 
     */
    public static ChartPanel createChart(String type, List<Entry<String,Integer>> events, int width, int height, PlotOrientation orientation) {
        
        if (type.contains("Pie")) {
            return ChartFactory.createPieChart(type, events, width, height);
        } else {
            return ChartFactory.createBarChart(type, events, width, height, orientation);
        } 
    }
    
    ///////////////////////////////////////////////////////////////////////////
    // private methods
    ///////////////////////////////////////////////////////////////////////////
    private static ChartPanel createPieChart(String type, List<Entry<String,Integer>> events, int width, int height) {

        DefaultPieDataset dataset = new DefaultPieDataset();
        
        for (Map.Entry<String,Integer> event : events) {
            dataset.setValue(event.getKey(), event.getValue());
        }
        
        JFreeChart jFChart;
        if (type.contains("3d")) {
            jFChart = org.jfree.chart.ChartFactory.createPieChart3D(
                "", 
                dataset, 
                false, 
                true, 
                false
            );
        } else {
            jFChart = org.jfree.chart.ChartFactory.createPieChart(
                "", 
                dataset, 
                false, 
                true, 
                false
            );
        }
        
        PiePlot plot = (PiePlot) jFChart.getPlot();
        plot.setBackgroundPaint(null);
        plot.setInteriorGap(0.01);
        plot.setOutlineVisible(false);
        plot.setLabelGenerator(null);

        // use gradients and white borders for the section colours
        plot.setBaseSectionOutlinePaint(Color.WHITE);
        plot.setSectionOutlinesVisible(true);
        plot.setBaseSectionOutlineStroke(new BasicStroke(2.0f));
        
        TextTitle t = jFChart.getTitle();
        t.setHorizontalAlignment(HorizontalAlignment.LEFT);
        t.setFont(new Font("Arial", Font.BOLD, 16));
        
        final ChartPanel jChartPanel =  new ChartPanel(jFChart, width, height, width, height, width, height, false, false, true, true, false, true);  
        jChartPanel.setMinimumDrawWidth(0);
        jChartPanel.setMaximumDrawWidth(Integer.MAX_VALUE);
        jChartPanel.setMinimumDrawHeight(0);
        jChartPanel.setMaximumDrawHeight(Integer.MAX_VALUE);
        
        return jChartPanel;
    }
    
    /**
     * Returns a Bar chart
     * @param events events to include on chart
     * @return 
     */
    private static ChartPanel createBarChart(String type, List<Entry<String,Integer>> events, int width, int height, PlotOrientation orientation) {
        
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();        

        for (Entry<String,Integer> event : events) {
            dataset.addValue(event.getValue().intValue(), event.getKey(), "");
        }
        
        JFreeChart jFChart;
        if (type.contains("3d")) {
            jFChart = org.jfree.chart.ChartFactory.createBarChart3D(
                "", 
                "", 
                "", 
                dataset, 
                orientation, 
                false, 
                true, 
                false
            );
        } else {
            jFChart = org.jfree.chart.ChartFactory.createBarChart(
                "", 
                "", 
                "", 
                dataset, 
                orientation,
                false, 
                true, 
                false
            );
        }
                            
        final ChartPanel jChartPanel =  new ChartPanel(jFChart, width, height, width, height, width, height, false, false, true, true, false, true);  
        jChartPanel.setMinimumDrawWidth(0);
        jChartPanel.setMaximumDrawWidth(Integer.MAX_VALUE);
        jChartPanel.setMinimumDrawHeight(0);
        jChartPanel.setMaximumDrawHeight(Integer.MAX_VALUE);
        
        return jChartPanel;
    }
}
