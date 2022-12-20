package com.example.kafkastreamsdemo;

import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KTable;
import org.apache.kafka.streams.kstream.ValueJoiner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.annotation.EnableKafkaStreams;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@EnableKafkaStreams
public class KafkaStreamsDemoApplication {

  public static void main(String[] args) {
    SpringApplication.run(KafkaStreamsDemoApplication.class, args);
  }

  @Bean
  public NewTopic book() {
    return new NewTopic("book", 6, (short) 1);
  }

  @Bean
  public NewTopic rekeyedBook() {
    return new NewTopic("rekeyedBook", 6, (short) 1);
  }

  @Bean
  public NewTopic rating() {
    return new NewTopic("rating", 6, (short) 1);
  }

  @Bean
  public NewTopic ratedBook() {
    return new NewTopic("ratedBook", 6, (short) 1);
  }

}

@RestController
class MyController {

  @GetMapping("/hello")
  public String hello() {
    return "Hello";
  }
}

@Component
class BookProcessor {

  @Autowired
  public void processBooks(StreamsBuilder builder) {

    // repartition
    builder.<String, Book>stream("book")
        .map((key, book) -> new KeyValue<>(String.valueOf(book.getId()), book))
        .to("rekeyedBook");
    KTable<String, Book> book = builder.table("rekeyedBook");

    builder.<String, Rating>stream("rating")
        .map((key, rating) -> new KeyValue<>(String.valueOf(rating.getId()), rating))
        .join(book, new BookRatingJoiner())
        .to("ratedBook");

    System.out.println(builder.build().describe());

  }
}

class BookRatingJoiner implements ValueJoiner<Rating, Book, RatedBook> {

  public RatedBook apply(Rating rating, Book book) {
    return RatedBook.newBuilder()
        .setId(book.getId())
        .setTitle(book.getTitle())
        .setReleaseYear(book.getReleaseYear())
        .setRating(rating.getRating())
        .build();
  }
}