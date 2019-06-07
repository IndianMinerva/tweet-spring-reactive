package com.im.penguin.controller;

import com.im.penguin.mode.Tweet;
import com.im.penguin.repository.TweetRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@Api("Tweets Controller")
public class TweetController {

    private final TweetRepository tweetRepository;

    @Autowired
    public TweetController(TweetRepository tweetRepository) {
        this.tweetRepository = tweetRepository;
    }

    @GetMapping("/")
    @ApiOperation("Get All Tweets")
    public Flux<Tweet> getAllTweets() {
        return tweetRepository.findAll();
    }

    @GetMapping("/{id}")
    @ApiOperation("Get a give Tweet")
    public Mono<ResponseEntity<Tweet>> getTweetById(@PathVariable(value = "id") String id) {
        return tweetRepository.findById(id)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }


    @PostMapping("/")
    @ApiOperation("Create a tweet")
    public Mono<Tweet> createTweet(@RequestBody Tweet tweet) {
        return tweetRepository.save(tweet);
    }

    @PutMapping("/{id}")
    @ApiOperation("Update a tweet")
    public Mono<ResponseEntity<Tweet>> updateTweet(@PathVariable(value = "id") String id, @RequestBody Tweet tweet) {
        return tweetRepository.findById(id)
                .flatMap(existingTweet -> {
                    existingTweet.setText(tweet.getText());
                    return tweetRepository.save(existingTweet);
                }).map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @ApiOperation("Delete a tweet")
    public Mono<ResponseEntity<Void>> deleteTweet(@PathVariable(value = "id") String id) {
        return tweetRepository.findById(id)
                .flatMap(existingTweet -> tweetRepository.delete(existingTweet)
                        .then(Mono.just(new ResponseEntity<Void>(HttpStatus.OK)))
                )
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }
}
