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

import com.haskins.cloudtrailviewer.dialog.resourcedetail.ResourceDetailRequest;
import javax.swing.JPanel;

/**
 *
 * @author mark.haskins
 */
public class UnhandledDetail extends AbstractDetail {

    private static final long serialVersionUID = 6573371644112827100L;

    public UnhandledDetail(ResourceDetailRequest detailRequest) {
        super(detailRequest);
    }
    
    @Override
    public String retrieveDetails(ResourceDetailRequest detailRequest) {
        return null;
    }
    
    @Override
    public JPanel getPanel() {
        return this;
    }
    
}
