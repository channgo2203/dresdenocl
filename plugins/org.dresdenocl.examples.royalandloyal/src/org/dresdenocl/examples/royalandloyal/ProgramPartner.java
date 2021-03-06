/**
 * Copyright (C) 2009 by Claas Wilke (info@claaswilke.de)
 * 
 * This file is part of the Royal and Loyal Example of Dresden OCL2 for Eclipse.
 * 
 * Dresden OCL2 for Eclipse is free software: you can redistribute it and/or modify 
 * it under the terms of the GNU Lesser General Public License as published by the 
 * Free Software Foundation, either version 3 of the License, or (at your option)
 * any later version.
 * 
 * Dresden OCL2 for Eclipse is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY 
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License 
 * for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License along 
 * with Dresden OCL2 for Eclipse. If not, see <http://www.gnu.org/licenses/>.
 */
package org.dresdenocl.examples.royalandloyal;

import org.dresdenocl.ocl2java.types.OclBag;

/**
 * <p>
 * Represents an implementation of the class {@link ProgramPartner} of the
 * Loyals and Royals example.
 * </p>
 * 
 * @author Claas Wilke
 */
public class ProgramPartner {

	protected String name;

	protected int numberOfCustomers;

	protected OclBag<LoyaltyProgram> programs = new OclBag<LoyaltyProgram>();

	protected OclBag<Service> deliveredServices = new OclBag<Service>();

	public String getName() {

		return name;
	}

	public void setName(String name) {

		this.name = name;
	}

	public int getNumberOfCustomers() {

		return numberOfCustomers;
	}

	public void setNumberOfCustomers(int numberOfCustomers) {

		this.numberOfCustomers = numberOfCustomers;
	}

	public OclBag<LoyaltyProgram> getPrograms() {

		return programs;
	}

	public void addProgram(LoyaltyProgram aProgram) {

		this.programs.add(aProgram);
	}

	public OclBag<Service> getDeliveredServices() {

		return deliveredServices;
	}

	public void addDeliveredService(Service aDeliveredService) {

		this.deliveredServices.add(aDeliveredService);
	}

	@Override
	public String toString() {

		StringBuilder builder = new StringBuilder();
		builder.append("ProgramPartner [");
		if (deliveredServices != null) {
			builder.append("deliveredServices=");
			builder.append("[");
			for (Service deliveredService : deliveredServices) {
				builder.append(deliveredService.getServiceNr());
				builder.append(", ");
			}
			builder.append("]");
			builder.append(", ");
		}
		if (name != null) {
			builder.append("name=");
			builder.append(name);
			builder.append(", ");
		}
		builder.append("numberOfCustomers=");
		builder.append(numberOfCustomers);
		builder.append(", ");
		if (programs != null) {
			builder.append("programs=");
			builder.append("[");
			for (LoyaltyProgram program : programs) {
				builder.append(program.getName());
				builder.append(", ");
			}
			builder.append("]");
		}
		builder.append("]");
		return builder.toString();
	}

}