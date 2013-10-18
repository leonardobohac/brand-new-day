package com.example.brandnewday;

public class Alarm {
	public int index, hour, minute, snooze;
	public boolean isActive;
	
	Alarm(int index, boolean isActive, int hour, int minute, int snooze) {
		this.index = index;
		this.isActive = isActive;
		this.hour = hour;
		this.minute = minute;
		this.snooze = snooze;
	}
	Alarm() {
		this.index = 0;
		this.isActive = false;
		this.hour = 0;
		this.minute = 0;
		this.snooze = 0;
	}
	
	void setIndex(int index) {
		this.index = index;
	}
	void setIsAcive(boolean isActive) {
		this.isActive = isActive;
	}
	void setHour(int hour) {
		this.hour = hour;
	}
	void setMinute(int minute) {
		this.minute = minute;
	}
	void setSnooze(int snooze) {
		this.snooze = snooze;
	}
}

