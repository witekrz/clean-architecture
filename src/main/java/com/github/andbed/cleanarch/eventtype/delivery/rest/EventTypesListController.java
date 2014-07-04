package com.github.andbed.cleanarch.eventtype.delivery.rest;

import java.util.List;

import org.springframework.hateoas.Resources;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.github.andbed.cleanarch.eventtype.core.boundary.EventTypeResponseModel;
import com.github.andbed.cleanarch.eventtype.core.boundary.EventTypesListPresenter;
import com.github.andbed.cleanarch.util.common.Command;
import com.github.andbed.cleanarch.util.common.Factory;
import com.github.andbed.cleanarch.util.common.MessageCode;

@Controller
@RequestMapping(EventTypesListController.URL)
public class EventTypesListController {

	public static final String URL = "/event";

	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<Resources<EventTypeResponseModel>> getAllEventTypes() {

		Presenter presenter = new Presenter();
		Command getAllEventTypes = Factory.createGetAllEventTypesCommand(presenter);
		getAllEventTypes.execute();

		return presenter.generateResponse();

	}

	static class Presenter implements EventTypesListPresenter {

		private List<EventTypeResponseModel> eventTypes;
		private MessageCode code;

		public ResponseEntity<Resources<EventTypeResponseModel>> generateResponse() {
			return code == null ?
					new ResponseEntity<>(new Resources<>(eventTypes), HttpStatus.OK) :
					new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

		@Override
		public void sendMessage(MessageCode code) {
			this.code = code;
		}

		@Override
		public void sendResult(List<EventTypeResponseModel> eventTypes) {
			this.eventTypes = eventTypes;
		}
	};
}