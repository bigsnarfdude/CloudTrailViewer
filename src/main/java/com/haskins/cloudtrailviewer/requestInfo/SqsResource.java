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
package com.haskins.cloudtrailviewer.requestInfo;

import com.haskins.cloudtrailviewer.model.event.Event;
import java.util.Collections;
import java.util.HashMap;

/**
 *
 * @author mark
 */
public class SqsResource extends AbstractRequest implements Request {

    private static final String QUEUE_NAME = "Queue Name";
    private static final String QUEUE_URL = "Queue Url";
    
    public SqsResource() {
        
        this.resourceMap = Collections.unmodifiableMap(new HashMap<String, String>() {
            {
                put("queueName", QUEUE_NAME);
                put("queueUrl", QUEUE_URL);
            }
        }); 
    }
    
    /**
     * Return the resource for the passed Event
     * @param event Event from which the resource is require
     * @param resources 
     */
    @Override
    public void populateRequestInfo(Event event, RequestInfo resources) {
        
        if (event.getEventName().equalsIgnoreCase("CreateQueue")) {
            getTopLevelResource(QUEUE_NAME, "queueName", event, resources);
            getTopLevelParameters(event, resources, "queueName");
        } else {
            getTopLevelResource(QUEUE_URL, "queueUrl", event, resources);
            getTopLevelParameters(event, resources, "queueUrl");
        }
    }
}
