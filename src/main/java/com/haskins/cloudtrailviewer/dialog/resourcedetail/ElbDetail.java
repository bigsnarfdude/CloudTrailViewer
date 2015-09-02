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
package com.haskins.cloudtrailviewer.dialog.resourcedetail;

import com.amazonaws.AmazonClientException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.elasticloadbalancing.AmazonElasticLoadBalancingClient;
import com.amazonaws.services.elasticloadbalancing.model.DescribeLoadBalancersRequest;
import com.amazonaws.services.elasticloadbalancing.model.DescribeLoadBalancersResult;
import com.amazonaws.services.elasticloadbalancing.model.HealthCheck;
import com.amazonaws.services.elasticloadbalancing.model.Instance;
import com.amazonaws.services.elasticloadbalancing.model.Listener;
import com.amazonaws.services.elasticloadbalancing.model.ListenerDescription;
import com.amazonaws.services.elasticloadbalancing.model.LoadBalancerDescription;
import java.awt.BorderLayout;
import java.util.Arrays;
import java.util.List;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author mark.haskins
 */
public class ElbDetail extends JPanel implements ResourceDetail {

    protected final DefaultTableModel primaryTableModel = new DefaultTableModel();
    protected final DefaultTableModel listenersTableModel = new DefaultTableModel();
    protected final DefaultTableModel healthCheckTableModel = new DefaultTableModel();
    
    @Override
    public String retrieveDetails(ResourceDetailRequest detailRequest) {
       
        String response = null;
        
        try {
            
            AWSCredentials credentials= new BasicAWSCredentials(
                detailRequest.getAccount().getKey(),
                detailRequest.getAccount().getSecret()
            );

            AmazonElasticLoadBalancingClient elbClient = new AmazonElasticLoadBalancingClient(credentials);
            elbClient.setRegion(Region.getRegion(Regions.fromName(detailRequest.getRegion())));

            DescribeLoadBalancersRequest request = new DescribeLoadBalancersRequest();
            request.setLoadBalancerNames(Arrays.asList(detailRequest.getResourceName()));

            DescribeLoadBalancersResult result = elbClient.describeLoadBalancers(request);
            buildUI(result); 
            
        } catch (IllegalArgumentException | AmazonClientException e) {
            response = e.getMessage();
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
    private void buildUI(DescribeLoadBalancersResult detail) {
        
        primaryTableModel.addColumn("Property");
        primaryTableModel.addColumn("Value");
        
        listenersTableModel.addColumn("Instance Port");
        listenersTableModel.addColumn("Instance Protocol");
        listenersTableModel.addColumn("Load Balancer Port");
        listenersTableModel.addColumn("Load Balancer Protocol");
        listenersTableModel.addColumn("SSL Certificate Id");
        
        healthCheckTableModel.addColumn("Property");
        healthCheckTableModel.addColumn("Value");
        
        JTabbedPane tabs = new JTabbedPane();
        
        final JTable primaryTable = new JTable(primaryTableModel);
        JScrollPane primaryScrollPane = new JScrollPane(primaryTable);
        tabs.add("Load Balancer", primaryScrollPane);
        
        final JTable healthCheckTable = new JTable(healthCheckTableModel);
        JScrollPane healthCheckScrollPane = new JScrollPane(healthCheckTable);
        tabs.add("Health Check", healthCheckScrollPane);
        
        final JTable listenersTable = new JTable(listenersTableModel);
        JScrollPane listenersScrollPane = new JScrollPane(listenersTable);
        tabs.add("Listeners", listenersScrollPane);
        
        this.setLayout(new BorderLayout());
        this.add(tabs, BorderLayout.CENTER);
        
        List<LoadBalancerDescription> elbs = detail.getLoadBalancerDescriptions();
        if (!elbs.isEmpty()) {
            
            LoadBalancerDescription elb = elbs.get(0);
            
            if (!elb.getAvailabilityZones().isEmpty()) { 
            
                StringBuilder azs = new StringBuilder();
                for (String az : elb.getAvailabilityZones()) {
                    azs.append(az).append(", ");
                }
                
                primaryTableModel.addRow(new Object[]{"Availability Zones", azs.toString()}); 
            }
            
            if (elb.getCanonicalHostedZoneName()!= null) { primaryTableModel.addRow(new Object[]{"Canonical Hosted Zone name", elb.getCanonicalHostedZoneName()}); }
            if (elb.getCanonicalHostedZoneNameID()!= null) { primaryTableModel.addRow(new Object[]{"Canonical Hosted Zone name Id", elb.getCanonicalHostedZoneNameID()}); }
            if (elb.getCreatedTime()!= null) { primaryTableModel.addRow(new Object[]{"Created", elb.getCreatedTime()}); }
            if (elb.getDNSName()!= null) { primaryTableModel.addRow(new Object[]{"DNS Name", elb.getDNSName()}); }
            
            if (!elb.getInstances().isEmpty()) { 
            
                StringBuilder instances = new StringBuilder();
                for (Instance instance : elb.getInstances()) {
                    instances.append(instance.getInstanceId()).append(", ");
                }
                
                primaryTableModel.addRow(new Object[]{"Instances", instances.toString()}); 
            }
            
            if (elb.getLoadBalancerName()!= null) { primaryTableModel.addRow(new Object[]{"Load Balander Name", elb.getLoadBalancerName()}); }
            if (elb.getScheme()!= null) { primaryTableModel.addRow(new Object[]{"Scheme", elb.getScheme()}); }
            
            if (!elb.getSecurityGroups().isEmpty()) { 
            
                StringBuilder sgs = new StringBuilder();
                for (String sg : elb.getSecurityGroups()) {
                    sgs.append(sg).append(", ");
                }
                
                primaryTableModel.addRow(new Object[]{"Security Groups", sgs.toString()}); 
            }
            
            if (elb.getSourceSecurityGroup()!= null) { primaryTableModel.addRow(new Object[]{"Source Security Group", elb.getSourceSecurityGroup().getGroupName()}); }
            
            if (!elb.getSubnets().isEmpty()) { 
            
                StringBuilder subnets = new StringBuilder();
                for (String subnet : elb.getSubnets()) {
                    subnets.append(subnet).append(", ");
                }
                
                primaryTableModel.addRow(new Object[]{"Subnets", subnets.toString()}); 
            }
            
            if (elb.getVPCId()!= null) { primaryTableModel.addRow(new Object[]{"VPC Id", elb.getVPCId()}); }
            
            /**
             * Health Check
             */        
            HealthCheck healthCheck = elb.getHealthCheck();
            if (healthCheck.getHealthyThreshold()!= null) { healthCheckTableModel.addRow(new Object[]{"Threshold", healthCheck.getHealthyThreshold()}); }
            if (healthCheck.getInterval()!= null) { healthCheckTableModel.addRow(new Object[]{"Interval", healthCheck.getInterval()}); }
            if (healthCheck.getTarget()!= null) { healthCheckTableModel.addRow(new Object[]{"Target", healthCheck.getTarget()}); }
            if (healthCheck.getTimeout()!= null) { healthCheckTableModel.addRow(new Object[]{"Timeout", healthCheck.getTimeout()}); }
            if (healthCheck.getUnhealthyThreshold()!= null) { healthCheckTableModel.addRow(new Object[]{"Unhealth Threshold", healthCheck.getUnhealthyThreshold()}); }
            
            /**
             * Listeners
             */
            List<ListenerDescription> listenerDescriptions = elb.getListenerDescriptions();
            for (ListenerDescription description : listenerDescriptions) {
                
                Listener listener = description.getListener();
                
                String ssl = "";
                if (listener.getSSLCertificateId() != null) {
                    ssl = listener.getSSLCertificateId();
                }
                
                listenersTableModel.addRow(new Object[]{
                    listener.getInstancePort(), 
                    listener.getInstanceProtocol(),
                    listener.getLoadBalancerPort(),
                    listener.getProtocol(),
                    ssl
                });
            }
        }
    }
}