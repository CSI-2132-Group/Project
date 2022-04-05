package csi2136.project.core;

public enum Province {

	ONTARIO("Ontario", "ON"),
	QUEBEC("Quebec", "QC");

	private final String name;
	private final String format;

	Province(String name, String format) {
		this.name = name;
		this.format = format;
	}

	public static Province fromFormat(String format) {
		for(Province value : values()) {
			if(value.format.equalsIgnoreCase(format)) {
				return value;
			}
		}

		return null;
	}

	public String getName() {
		return this.name;
	}

	public String getFormat() {
		return this.format;
	}

	@Override
	public String toString() {
		return this.name;
	}

}
