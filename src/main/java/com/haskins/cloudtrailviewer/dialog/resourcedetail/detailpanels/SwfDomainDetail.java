/*
 * Copyright (C) 2015 Mark P. Haskins
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.haskins.cloudtrailviewer.dialog.resourcedetail.detailpanels;

import com.amazonaws.AmazonClientException;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.simpleworkflow.AmazonSimpleWorkflow;
import com.amazonaws.services.simpleworkflow.AmazonSimpleWorkflowClient;
import com.amazonaws.services.simpleworkflow.model.DescribeDomainRequest;
import com.amazonaws.services.simpleworkflow.model.DomainDetail;
import com.haskins.cloudtrailviewer.dialog.resourcedetail.ResourceDetailRequest;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JPanel;

/**
 *
 * @author mark.haskins
 */
public class SwfDomainDetail extends AbstractDetail {

    private final static Logger LOGGER = Logger.getLogger("CloudTrail");
    
    private static final long serialVersionUID = -7879721877676919400L;

    public SwfDomainDetail(ResourceDetailRequest detailRequest) {
        super(detailRequest);
    }
    
    @Override
    public String retrieveDetails(ResourceDetailRequest detailRequest) {
       
        String response = null;
        
        try {

            AmazonSimpleWorkflow client = new AmazonSimpleWorkflowClient(credentials);
            client.setRegion(Region.getRegion(Regions.fromName(detailRequest.getRegion())));
            
            DescribeDomainRequest request = new DescribeDomainRequest();
            request.setName(detailRequest.getResourceName());
            
            DomainDetail result = client.describeDomain(request);
            buildUI(result); 
            
        } catch (IllegalArgumentException | AmazonClientException e) {
            response = e.getMessage();
            LOGGER.log(Level.WARNING, "Problem retrieving SWF details from AWS", e);
        }

        return response;
    }

    @Override
    public JPanel getPanel() {
        return this;
    }
    
    ////////////////////////////////////////////////////////////////////////////
    ///// private methods
    ////////////////////////////////////////////////////////////////////////////
    private void buildUI(DomainDetail detail) {
        
    }
    
}
