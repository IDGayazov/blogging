package com.blogging.project.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.ai.ollama.api.OllamaModel;
import org.springframework.ai.ollama.api.OllamaOptions;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/v1/ai")
@Slf4j
@RequiredArgsConstructor
public class ChatController {

    private final OllamaChatModel chatModel;

    @GetMapping("/generate")
    public Map<String,String> generate(@RequestParam(value = "message") String message) {
        log.info("Request for generating text");
        return Map.of("generation", this.chatModel.call(message));
    }
}