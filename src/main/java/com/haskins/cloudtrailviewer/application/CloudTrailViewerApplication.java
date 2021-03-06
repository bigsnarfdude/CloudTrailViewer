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

package com.haskins.cloudtrailviewer.application;

import com.haskins.cloudtrailviewer.feature.Feature;
import com.haskins.cloudtrailviewer.feature.NoDataFeature;
import com.haskins.cloudtrailviewer.feature.SimpleTableFeature;
import com.haskins.cloudtrailviewer.core.EventLoader;
import com.haskins.cloudtrailviewer.core.EventLoaderListener;
import com.haskins.cloudtrailviewer.core.FilteredEventDatabase;
import com.haskins.cloudtrailviewer.feature.ErrorFeature;
import com.haskins.cloudtrailviewer.feature.GeoDataFeature;
import com.haskins.cloudtrailviewer.feature.InvokersFeature;
import com.haskins.cloudtrailviewer.feature.MetricsFeature;
import com.haskins.cloudtrailviewer.feature.OverviewFeature;
import com.haskins.cloudtrailviewer.feature.ResourceFeature;
import com.haskins.cloudtrailviewer.feature.SecurityFeature;
import com.haskins.cloudtrailviewer.model.filter.AllFilter;
import com.haskins.cloudtrailviewer.model.filter.CompositeFilter;
import com.haskins.cloudtrailviewer.model.load.LoadFileRequest;
import com.haskins.cloudtrailviewer.utils.GeoIpUtils;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * Main Application class, in some ways it acts as a Controller to controller the views (or features).
 * 
 * @author mark
 */
public class CloudTrailViewerApplication extends JFrame implements EventLoaderListener {

    private static final long serialVersionUID = -596259130540845420L;
        
    private final FilteredEventDatabase database ;
    private final EventLoader eventLoader;
    
    private final JPanel features = new JPanel(new CardLayout());
    private final Map<String,Feature> featureMap = new LinkedHashMap<>();
    
    private final StatusBar statusBar = new StatusBar();
    private final HelpToolBar helpToolBar = new HelpToolBar();
    private final FeatureToolBar featureToolBar;

    /**
     * Default Constructor
     */
    public CloudTrailViewerApplication() {
        
        super("CloudTrail Viewer");
        
        database = new FilteredEventDatabase(new AllFilter(), statusBar);
        
        eventLoader = new EventLoader(database);
        eventLoader.addEventLoaderListener(this);
        
        featureToolBar = new FeatureToolBar(this);
                
        addFeatures();
        buildUI();
    }
    
    /**
     * Loads files from the local file system.
     * 
     * @param files An Array of FILEs to be loaded
     * @param filter An object to filter only specific events. Pass NULL in if no
     * filtering is required.
     */
    void loadLocalFiles(File[] files, CompositeFilter filter) {
        
        if (files != null && files.length > 0) {
            
            changeFeature(OverviewFeature.NAME, true);
            
            List<String> filePaths = new ArrayList<>();
            for (File file : files) {
                if (file.isFile()) {
                    filePaths.add(file.getAbsolutePath());
                }
            }
            
            LoadFileRequest loadRequest = new LoadFileRequest(filePaths, filter);
            eventLoader.loadEventsFromLocalFiles(loadRequest);            
        }
    }
    
    /**
     * Loads files from the an AWS S3 Bucket.
     * 
     * @param request request containing filters and keys
     */
     void newS3Files(LoadFileRequest request) {
    
        if (!request.getFilenames().isEmpty()) {
            
            changeFeature(OverviewFeature.NAME, true);
            eventLoader.loadEventsFromS3(request);
        }
    }
    
    /**
     * Changes the feature that is visible.
     * @param name The name of the Feature to show.
     * @param loading boolean to indicate if EventLoader is loading events
     */
     void changeFeature(String name, boolean loading) {
            
        if (database.size() > 0 || loading) {
        
            for (Component component : features.getComponents()) {
                Feature feature = (Feature)component;
                feature.will_hide();
            }
            
            CardLayout cl = (CardLayout)(features.getLayout());
            cl.show(features, name); 
            Feature f = featureMap.get(name);
            f.will_appear();
        }
    }
    
    /**
     * When called clears the database of all events and then informs all features
     * to reset themselves.
     */
    public void clearEvents() {
        
        GeoIpUtils.getInstance().clear();
        database.clear();
        statusBar.eventsCleared();
        
        for (Component component : features.getComponents()) {
            Feature feature = (Feature)component;
            feature.reset();
        } 
        
        CardLayout cl = (CardLayout)(features.getLayout());
        cl.show(features, NoDataFeature.NAME); 
        Feature f = featureMap.get(NoDataFeature.NAME);
        f.will_appear();
        
        this.revalidate();
    }

    ////////////////////////////////////////////////////////////////////////////
    ///// EventLoaderListener implementation
    ////////////////////////////////////////////////////////////////////////////
    @Override
    public void processingFile(int fileCount, int total) {
        this.statusBar.setStatusMessage("Processing file " + fileCount + " of " + total);
    }

    @Override
    public void finishedLoading() {
        
        this.statusBar.setStatusMessage("");
        
        for (Component component : features.getComponents()) {
            Feature feature = (Feature)component;
            feature.eventLoadingComplete();
        }
    }
    
    ////////////////////////////////////////////////////////////////////////////
    ///// private methods
    ////////////////////////////////////////////////////////////////////////////
    private void buildUI() {
        
        this.setTitle("CloudTrail Viewer");
        this.setLayout(new BorderLayout());
        
        int inset = 50;
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setBounds(inset, inset,
                screenSize.width / 2,
                screenSize.height / 2);


        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BorderLayout());
                
        JPanel toolbars = new JPanel(new BorderLayout());
        toolbars.setBackground(Color.WHITE);
        toolbars.add(new LoadToolBar(this), BorderLayout.WEST);
        toolbars.add(featureToolBar, BorderLayout.CENTER);
        toolbars.add(helpToolBar, BorderLayout.EAST);
        
        topPanel.add(toolbars, BorderLayout.SOUTH);
           
        this.add(topPanel, BorderLayout.NORTH);
        this.add(features, BorderLayout.CENTER);
        this.add(statusBar, BorderLayout.PAGE_END);
    }
    
    private void addFeatures() {

        addFeature(new NoDataFeature(helpToolBar));
        addFeature(new OverviewFeature(statusBar, helpToolBar));
        addFeature(new SimpleTableFeature(database, helpToolBar));
        addFeature(new InvokersFeature(statusBar, helpToolBar));
        addFeature(new ErrorFeature(statusBar, helpToolBar));
        addFeature(new SecurityFeature(statusBar, helpToolBar));
        addFeature(new ResourceFeature(statusBar, helpToolBar));
        addFeature(new GeoDataFeature(statusBar, helpToolBar));
        addFeature(new MetricsFeature(statusBar, helpToolBar));
               
        Set<String> keys = featureMap.keySet();
        for (String key : keys) {

            Feature feature = featureMap.get(key);
            if (feature.showOnToolBar()) {
                featureToolBar.addFeature(feature);
            }
        }
    }
    
    private void addFeature(Feature newFeature) {
        
        database.addListener(newFeature);
        featureMap.put(newFeature.getName(), newFeature);
        features.add((JPanel)newFeature, newFeature.getName()); 
    }
}