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

package com.haskins.cloudtrailviewer.components.servicespanel;

import com.haskins.cloudtrailviewer.feature.Feature;
import com.haskins.cloudtrailviewer.model.event.Event;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

/**
 *
 * @author mark
 */
class ServicesOverviewPanel extends JPanel {

    private static final long serialVersionUID = -2461359163355407044L;
    
    private final CountPanel awsCountPanel;
    private final CountPanel iamCountPanel;
    
    private final EpsPanel awsEpsPanel;
    private final EpsPanel iamEpsPanel;
    
    private final JLabel totalLabel = new JLabel("0", SwingConstants.CENTER);
    
    /**
     * Default Constructor.
     * @param serviceName Name of the service that panel will be showing
     * @param f The feature that panel will be showing
     */
    ServicesOverviewPanel(String serviceName, Feature f) {
        
        this.setLayout(new BorderLayout());
        this.setMinimumSize(new Dimension(250,130));
        this.setMaximumSize(new Dimension(250,130));
        this.setPreferredSize(new Dimension(250,130));
        this.setBackground(Color.white);
        this.setOpaque(true);
        
        this.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.black));
        
        JLabel title = new JLabel(serviceName, SwingConstants.CENTER);
        title.setAlignmentX(CENTER_ALIGNMENT);
        
        JPanel labelPanel = new JPanel();
        labelPanel.setLayout(new BoxLayout(labelPanel,BoxLayout.PAGE_AXIS));
        labelPanel.add(Box.createHorizontalGlue());
        labelPanel.add(title);
        labelPanel.add(Box.createHorizontalGlue());
        labelPanel.setOpaque(false);
        
        awsCountPanel = new CountPanel("AWS", new Color(51, 102, 153), f);
        
        iamCountPanel = new CountPanel("IAM", new Color(102, 153, 153), f);
      
        JPanel countsPanel = new JPanel(new GridLayout(1,2));
        countsPanel.add(awsCountPanel);
        countsPanel.add(iamCountPanel);
        
        awsEpsPanel = new EpsPanel(new Color(51, 102, 153));
        iamEpsPanel = new EpsPanel(new Color(102, 153, 153));
        JPanel epsPanel = new JPanel(new GridLayout(1,2));
        epsPanel.add(awsEpsPanel);
        epsPanel.add(iamEpsPanel);
        
        JPanel statsContainer = new JPanel(new BorderLayout());
        statsContainer.add(countsPanel, BorderLayout.CENTER);
        statsContainer.add(epsPanel, BorderLayout.PAGE_END);
        
        JPanel totalPanel = new JPanel(new GridLayout(1,2));
        totalPanel.add(new JLabel("Total : ", SwingConstants.CENTER));
        totalPanel.add(totalLabel);
        
        this.add(labelPanel, BorderLayout.PAGE_START);
        this.add(statsContainer, BorderLayout.CENTER);
        this.add(totalPanel, BorderLayout.PAGE_END);
    }

    /**
     * Adds Events to Panel.
     * @param event event to added to Panel
     */
    public void addEvent(Event event) {
                
        if (event.getUserIdentity().getType().equalsIgnoreCase("Root")) {
            awsCountPanel.newEvent(event);
            awsEpsPanel.newEvent(event);
        } else {
            iamCountPanel.newEvent(event);
            iamEpsPanel.newEvent(event);
        }
        
        String strTotal = totalLabel.getText();
        int intCount = Integer.parseInt(strTotal);
        intCount++;
        totalLabel.setText(String.valueOf(intCount));
        
        this.revalidate();
    }
}
