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
import com.amazonaws.services.ec2.AmazonEC2;
import com.amazonaws.services.ec2.AmazonEC2Client;
import com.amazonaws.services.ec2.model.DescribeInstancesRequest;
import com.amazonaws.services.ec2.model.DescribeInstancesResult;
import com.amazonaws.services.ec2.model.Instance;
import com.amazonaws.services.ec2.model.Reservation;
import com.amazonaws.services.ec2.model.Tag;
import com.haskins.cloudtrailviewer.dialog.resourcedetail.ResourceDetailRequest;
import java.awt.BorderLayout;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JTabbedPane;

/**
 *
 * @author mark.haskins
 */
public class EC2InstanceDetail extends AbstractDetail {

    private final static Logger LOGGER = Logger.getLogger("CloudTrail");
    
    private static final long serialVersionUID = 8109771800393230032L;
    
    public EC2InstanceDetail(ResourceDetailRequest detailRequest) {
        super(detailRequest);
    }
    
    @Override
    public String retrieveDetails(ResourceDetailRequest detailRequest) {
       
        String response = null;
        
        try {
            
            AmazonEC2 ec2Client = new AmazonEC2Client(credentials);
            ec2Client.setRegion(Region.getRegion(Regions.fromName(detailRequest.getRegion())));

            DescribeInstancesRequest request = new DescribeInstancesRequest();
            request.setInstanceIds(Collections.singletonList(detailRequest.getResourceName()));

            DescribeInstancesResult result = ec2Client.describeInstances(request);
            buildUI(result); 
            
        } catch (IllegalArgumentException | AmazonClientException e) {
            response = e.getMessage();
            LOGGER.log(Level.WARNING, "Problem retrieving EC2 details from AWS", e);
        }

        return response;
    }
    
    ////////////////////////////////////////////////////////////////////////////
    ///// private methods
    ////////////////////////////////////////////////////////////////////////////
    private void buildUI(DescribeInstancesResult detail) {
       
        JTabbedPane tabs = new JTabbedPane();
        tabs.add("Instance", primaryScrollPane);
        tabs.add("Tags", tagsScrollPane);
        
        this.add(tabs, BorderLayout.CENTER);
        
        List<Reservation> reservations = detail.getReservations();
        if (!reservations.isEmpty()) {
            
            Reservation reservation = reservations.get(0);
            List<Instance> instances = reservation.getInstances();
            if (!instances.isEmpty()) {
                
                Instance instance = instances.get(0);
                
                if (instance.getAmiLaunchIndex() != null) { primaryTableModel.addRow(new Object[]{"AMI Launch Index", instance.getAmiLaunchIndex()}); }
                if (instance.getArchitecture() != null) { primaryTableModel.addRow(new Object[]{"Architecture", instance.getArchitecture()}); }
                if (instance.getAmiLaunchIndex() != null) { primaryTableModel.addRow(new Object[]{"Block Mapping Device", ""}); }
                if (instance.getClientToken() != null) { primaryTableModel.addRow(new Object[]{"Client Token", instance.getClientToken()}); }
                primaryTableModel.addRow(new Object[]{"EBS Optimised", instance.isEbsOptimized()});
                if (instance.getHypervisor() != null) { primaryTableModel.addRow(new Object[]{"Hypervisor", instance.getHypervisor()}); }
                if (instance.getIamInstanceProfile() != null) { primaryTableModel.addRow(new Object[]{"Instance Profile", instance.getIamInstanceProfile().toString()}); }
                if (instance.getImageId() != null) { primaryTableModel.addRow(new Object[]{"Image Id", instance.getImageId()}); }
                if (instance.getInstanceId() != null) { primaryTableModel.addRow(new Object[]{"Instance Id", instance.getInstanceId()}); }
                if (instance.getInstanceLifecycle() != null) { primaryTableModel.addRow(new Object[]{"Instance Lifecyle", instance.getInstanceLifecycle()}); }
                if (instance.getInstanceType() != null) { primaryTableModel.addRow(new Object[]{"Instance Type", instance.getInstanceType()}); }
                if (instance.getKernelId() != null) { primaryTableModel.addRow(new Object[]{"Kernel Id", instance.getKernelId()}); }
                if (instance.getKeyName() != null) { primaryTableModel.addRow(new Object[]{"Key Name", instance.getKeyName()}); }
                if (instance.getLaunchTime() != null) { primaryTableModel.addRow(new Object[]{"Launch Time", instance.getLaunchTime()}); }
                if (instance.getMonitoring() != null) { primaryTableModel.addRow(new Object[]{"Monitoring", instance.getMonitoring().toString()}); }
                if (instance.getPlacement() != null) { primaryTableModel.addRow(new Object[]{"Placement", instance.getPlacement().getAvailabilityZone()}); }
                if (instance.getPlatform() != null) { primaryTableModel.addRow(new Object[]{"Platform", instance.getPlatform()}); }
                if (instance.getPrivateDnsName() != null) { primaryTableModel.addRow(new Object[]{"Private DNS Name", instance.getPrivateDnsName()}); }
                if (instance.getPrivateIpAddress() != null) { primaryTableModel.addRow(new Object[]{"Private IP Address", instance.getPrivateIpAddress()}); }
                if (instance.getAmiLaunchIndex() != null) { primaryTableModel.addRow(new Object[]{"Product Codes", ""}); }
                if (instance.getPublicDnsName() != null) { primaryTableModel.addRow(new Object[]{"Public DNS Name", instance.getPublicDnsName()}); }
                if (instance.getPublicIpAddress() != null) { primaryTableModel.addRow(new Object[]{"Public IP Address", instance.getPublicIpAddress()}); }
                if (instance.getRamdiskId() != null) { primaryTableModel.addRow(new Object[]{"Ram Disk Id", instance.getRamdiskId()}); }
                if (instance.getRootDeviceName() != null) { primaryTableModel.addRow(new Object[]{"Root Device Name", instance.getRootDeviceName()}); }
                if (instance.getRootDeviceType() != null) { primaryTableModel.addRow(new Object[]{"Root Device Type", instance.getRootDeviceType()}); }
                if (instance.getAmiLaunchIndex() != null) { primaryTableModel.addRow(new Object[]{"Security Groups", ""}); }
                if (instance.getSourceDestCheck() != null) { primaryTableModel.addRow(new Object[]{"Source Destination Check", instance.getSourceDestCheck()}); }
                if (instance.getSpotInstanceRequestId() != null) { primaryTableModel.addRow(new Object[]{"Spot Instance Request Id", instance.getSpotInstanceRequestId()}); }
                if (instance.getSriovNetSupport() != null) { primaryTableModel.addRow(new Object[]{"Sriov network Support", instance.getSriovNetSupport()}); }
                if (instance.getState() != null) { primaryTableModel.addRow(new Object[]{"State", instance.getState().getName()}); }
                if (instance.getStateReason() != null) { primaryTableModel.addRow(new Object[]{"State Reason", instance.getStateReason().getMessage()}); }
                if (instance.getSubnetId() != null) { primaryTableModel.addRow(new Object[]{"Subnet Id", instance.getSubnetId()}); }
                if (instance.getVirtualizationType() != null) { primaryTableModel.addRow(new Object[]{"Virtualisation Type", instance.getVirtualizationType()}); }
                if (instance.getVpcId() != null) { primaryTableModel.addRow(new Object[]{"Vpc Id", instance.getVpcId()}); }
                
                List<Tag> tags = instance.getTags();
                for (Tag tag : tags) {
                    tagsTableModel.addRow(new Object[]{tag.getKey(), tag.getValue()});
                }
            }
        }
    }
}
