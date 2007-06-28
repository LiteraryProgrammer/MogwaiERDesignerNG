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

import de.mogwai.erdesignerng.exception.CannotDeleteException;
import de.mogwai.erdesignerng.exception.ElementAlreadyExistsException;
import de.mogwai.erdesignerng.exception.ElementInvalidNameException;

/**
 * A database table.
 * 
 * @author Mirko Sertic <mail@mirkosertic.de>
 */
public class Table extends OwnedModelItem<Model> implements
		OwnedModelItemVerifier {

	private AttributeList attributes = new AttributeList();

	private IndexList indexes = new IndexList();

	/**
	 * Add an attribute to the table.
	 * 
	 * @param aAttribute
	 *            the table
	 * @throws ElementAlreadyExistsException
	 * @throws ElementInvalidNameException
	 */
	public void addAttribute(Attribute aAttribute)
			throws ElementAlreadyExistsException, ElementInvalidNameException {

		if (owner != null) {
			ModelUtilities.checkNameAndExistance(attributes, aAttribute, owner
					.getDialect());
		}

		aAttribute.setOwner(this);
		attributes.add(aAttribute);
	}

	/**
	 * Add an index to the table.
	 * 
	 * @param aIndex
	 *            the table
	 * @throws ElementAlreadyExistsException
	 * @throws ElementInvalidNameException
	 */
	public void addIndex(Index aIndex) throws ElementAlreadyExistsException,
			ElementInvalidNameException {

		if (owner != null) {
			ModelUtilities.checkNameAndExistance(indexes, aIndex, owner
					.getDialect());
		}

		aIndex.setOwner(this);
		indexes.add(aIndex);
	}

	public void checkNameAlreadyExists(ModelItem aSender, String aName)
			throws ElementAlreadyExistsException {
		if (aSender instanceof Attribute) {
			ModelUtilities.checkExistance(attributes, aName, owner
					.getDialect());
			
			owner.getModelHistory().createAddAttributeCommand((Attribute)aSender);
		}
		if (aSender instanceof Index) {
			ModelUtilities.checkExistance(indexes, aName, owner
					.getDialect());
			owner.getModelHistory().createAddIndexCommand((Index)aSender);
			
		}

	}

	public void delete(ModelItem aSender) throws CannotDeleteException {
		if (aSender instanceof Attribute) {
			if (attributes.size() == 1) {
				throw new CannotDeleteException(
						"Table must have at least one attribute!");
			}

			Attribute theAttribute = (Attribute) aSender;

			if (owner.isUsedByRelations(theAttribute)) {
				throw new CannotDeleteException(
						"Attribute in use by relations!");
			}

			attributes.remove(theAttribute);

			return;
		}

		if (aSender instanceof Index) {

			Index theIndex = (Index) aSender;

			indexes.remove(theIndex);

			return;
		}

		throw new UnsupportedOperationException("Unknown element " + aSender);

	}

	@Override
	protected void generateRenameHistoryCommand(String aNewName) {
		if (owner != null) {
			owner.getModelHistory().createRenameTableCommand(this, aNewName);
		}
	}

	public String checkName(String aName) throws ElementInvalidNameException {
		if (owner != null) {
			return owner.checkName(aName);
		}

		return aName;
	}

	@Override
	protected void generateDeleteCommand() {
		if (owner != null) {
			owner.getModelHistory().createDeleteCommand(this);
		}
	}

	public AttributeList getAttributes() {
		return attributes;
	}

	public boolean isForeignKey(Attribute aAttribute) {
		return owner.isUsedByRelations(aAttribute);
	}

	public IndexList getIndexes() {
		return indexes;
	}

	public void setIndexes(IndexList indexes) {
		this.indexes = indexes;
	}

	public void setAttributes(AttributeList attributes) {
		this.attributes = attributes;
	}
}