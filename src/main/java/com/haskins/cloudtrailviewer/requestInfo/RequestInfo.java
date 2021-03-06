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

package com.haskins.cloudtrailviewer.requestInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author mark.haskins
 */
public class RequestInfo {
        
    private final Map<String, List<String>> resourceMap = new HashMap();
    private final Map<String, List<String>> parameterMap = new HashMap();

    public void addResource(String type, String name) {
        
        List<String> resources = resourceMap.get(type);
        if (resources == null) {
            
            resources = new ArrayList();
            resourceMap.put(type, resources);
        }
        
        resources.add(name);
    }
        
    public Map<String, List<String>> getResourcesMap() {
        return this.resourceMap;
    }
    
    public Map<String, List<String>> getParameterMap() {
        return this.parameterMap;
    }
    
    public void addParameter(String type, String name) {
        
        List<String> parameters = parameterMap.get(type);
        if (parameters == null) {
            
            parameters = new ArrayList();
            parameterMap.put(type, parameters);
        }
        
        parameters.add(name);
    }
    
    public String getResourceTypesAsString() {
        
        List<String> values = new ArrayList<>();
        
        Set<String> keys = resourceMap.keySet();
        for (String key : keys) {
            values.add(key);
        }
        
        return createResponseString(values);
    }
    
    public String getResourceNamesAsString() {
        
        List<String> values = new ArrayList<>();
        
        Set<String> keys = resourceMap.keySet();
        for (String key : keys) {

            List<String> nameValues = resourceMap.get(key);
            for (String value : nameValues) {
                values.add(value);
            }
        }
        
        return createResponseString(values);
    }
    
    ////////////////////////////////////////////////////////////////////////////
    ///// private methods
    ////////////////////////////////////////////////////////////////////////////
    private String createResponseString(List<String> values) {
        
        StringBuilder response = new StringBuilder();
        
        if (values.size() >= 1) {
            response.append(values.get(0));
        }
        
        if (values.size() >= 2) {
            response.append(", ").append(values.get(1));
        }
        
        if (values.size() > 2) {
            
            int remaining = values.size() - 2;
            response.append(" and ").append(remaining).append(" more");
        }
                   
        return response.toString();
    }
}
