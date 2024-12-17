package com.cia103g5.user.availabletime.controller;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
public class SseController {

	private final List<SseEmitter> emitters = new CopyOnWriteArrayList<>();

	@GetMapping("/sse")
	public SseEmitter streamUpdates() {
		SseEmitter emitter = new SseEmitter();
		emitters.add(emitter);

		emitter.onCompletion(() -> emitters.remove(emitter));
		emitter.onTimeout(() -> emitters.remove(emitter));

		return emitter;
	}

	public void notifyClients(String message) {
		for (SseEmitter emitter : emitters) {
			try {
				emitter.send(SseEmitter.event().data(message));
			} catch (IOException e) {
				emitters.remove(emitter);
			}
		}
	}
}
