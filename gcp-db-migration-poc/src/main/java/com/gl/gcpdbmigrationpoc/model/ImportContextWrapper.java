package com.gl.gcpdbmigrationpoc.model;

import java.io.Serializable;

import lombok.Data;

@Data
public class ImportContextWrapper implements Serializable {

	private static final long serialVersionUID = 1L;
	
	public ImportContext importContext;
}
