package com.im.penguin.controller;

import com.im.penguin.mode.Tweet;
import com.im.penguin.repository.TweetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
public class TweetController {

    private final TweetRepository tweetRepository;

    @Autowired
    public TweetController(TweetRepository tweetRepository) {
        this.tweetRepository = tweetRepository;
    }

    @GetMapping("/")
    public Flux<Tweet> getAllTweets() {
        return tweetRepository.findAll();
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<Tweet>> getTweetById(@PathVariable(value = "id") String id) {
        return tweetRepository.findById(id)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PostMapping("/")
    public Mono<Tweet> createTweet(@RequestBody Tweet tweet) {
        return tweetRepository.save(tweet);
    }

    @PutMapping("/{id}")
    public Mono<ResponseEntity<Tweet>> updateTweet(@PathVariable(value = "id") String id, @RequestBody Tweet tweet) {
        return tweetRepository.findById(id)
                .flatMap(existingTweet -> {
                    existingTweet.setText(tweet.getText());
                    return tweetRepository.save(existingTweet);
                }).map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteTweet(@PathVariable(value = "id") String id) {
        return tweetRepository.findById(id)
                .flatMap(existingTweet -> tweetRepository.delete(existingTweet)
                        .then(Mono.just(new ResponseEntity<Void>(HttpStatus.OK)))
                )
                        .defaultIfEmpty(ResponseEntity.notFound().build());
    }
}
