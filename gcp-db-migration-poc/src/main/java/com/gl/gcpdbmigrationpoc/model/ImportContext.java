package com.gl.gcpdbmigrationpoc.model;

import java.io.Serializable;

import lombok.Data;

@Data
public class ImportContext implements Serializable {

	private static final long serialVersionUID = 1L;
	
	public String fileType;
	public String uri;
	public String database;

}
