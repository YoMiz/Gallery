package com.example.app.domain;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EventData {
	private List<ArtInfo> artInfoList;
	private Event event;
	private int eventId;
	private int pageStyle;
	private String titleImg;
	private Event currentEvent;

	public List<ArtInfo> getArtInfoList() {
		return artInfoList;
	}

	public void setArtInfoList(List<ArtInfo> artInfoList) {
		this.artInfoList = artInfoList;
	}

	public Event getCurrentEvent() {
		return currentEvent;
	}

	public void setCurrentEvent(Event currentEvent) {
		this.currentEvent = currentEvent;
	}
}
