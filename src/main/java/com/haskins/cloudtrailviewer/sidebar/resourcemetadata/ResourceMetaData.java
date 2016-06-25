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
package com.haskins.cloudtrailviewer.sidebar.resourcemetadata;

import com.haskins.cloudtrailviewer.model.event.Event;

import java.util.List;

/**
 *
 * @author mark
 */
public interface ResourceMetaData {
    
    /**
     * Populate the MetaData from the given Event
     * @param event 
     */
    void populate(Event event);
    
    /**
     * returns menu items
     * @return 
     */
    String[] getMenuItems();
    
    /**
     * returns values for menu item
     * @param menuItem
     * @return 
     */
    List<String> getValuesForMenuItem(String menuItem);
}
