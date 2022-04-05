package csi2136.project.core;

public enum Gender {

	MALE("Male", "M"),
	FEMALE("Female", "F"),
	OTHER("Other", "O"),
	UNSPECIFIED("Unspecified", "U");

	private final String name;
	private final String format;

	Gender(String name, String format) {
		this.name = name;
		this.format = format;
	}

	public static Gender fromFormat(String format) {
		for(Gender value : values()) {
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
