/**
 * Mogwai ERDesigner. Copyright (C) 2002 The Mogwai Project.
 * 
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later
 * version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 59 Temple
 * Place - Suite 330, Boston, MA 02111-1307, USA.
 */
package de.mogwai.erdesignerng.model;

/**
 * A list of attributes.
 * 
 * @author Mirko Sertic <mail@mirkosertic.de>
 */
public class AttributeList extends ModelItemVector<Attribute> {

	private static final long serialVersionUID = 890361971577085178L;

	/**
	 * Find a attribute by a given system id.
	 * 
	 * @param aSystemId
	 *            the system id
	 * @return the found element
	 */
	public Attribute findBySystemId(String aSystemId) {
		for (Attribute theAttribute : this) {
			if (aSystemId.equals(theAttribute.getSystemId())) {
				return theAttribute;
			}
		}
		return null;
	}

}