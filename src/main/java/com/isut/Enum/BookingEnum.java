package com.isut.Enum;

public enum BookingEnum {
	PENDIG(0), ACCEPTED(1), REJECTED(2);

	private int num;

	BookingEnum(int i) {
		this.num = i;
	}

	public int getvalue() {
		return num;
	}

}
