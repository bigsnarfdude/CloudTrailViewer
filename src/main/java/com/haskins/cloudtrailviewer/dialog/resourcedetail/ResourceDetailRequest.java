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

package com.haskins.cloudtrailviewer.dialog.resourcedetail;

import com.haskins.cloudtrailviewer.model.AwsAccount;

/**
 *
 * @author mark.haskins
 */
public class ResourceDetailRequest {
    
    private final AwsAccount account;
    private final String region;
    private final String resourceType;
    private final String resourceName;

    public ResourceDetailRequest(AwsAccount account, String region, String resourceType, String resourceName) {
        
        this.account = account;
        this.region = region;
        this.resourceType = resourceType;
        this.resourceName = resourceName;
    }
    
    /**
     * @return the account
     */
    public AwsAccount getAccount() {
        return account;
    }

    /**
     * @return the region
     */
    public String getRegion() {
        return region;
    }

    /**
     * @return the resourceType
     */
    String getResourceType() {
        return resourceType;
    }

    /**
     * @return the resourceName
     */
    public String getResourceName() {
        return resourceName;
    }
    
}
