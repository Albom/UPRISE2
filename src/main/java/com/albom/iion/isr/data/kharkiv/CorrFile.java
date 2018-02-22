package com.albom.iion.isr.data.kharkiv;

import java.time.LocalDateTime;

public class CorrFile {

	protected int session;
	protected int integration;
	protected LocalDateTime time;

	public int getSession() {
		return session;
	}

	public void setSession(int session) {
		this.session = session;
	}

	public int getIntegration() {
		return integration;
	}

	public void setIntegration(int integration) {
		this.integration = integration;
	}

	public LocalDateTime getTime() {
		return time;
	}

	public void setTime(LocalDateTime time) {
		this.time = time;
	}

}
