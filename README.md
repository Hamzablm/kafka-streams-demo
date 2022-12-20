

```shell
kafka-avro-console-producer --bootstrap-server localhost:9092 \
 --property value.schema.file=src/main/avro/Book.avsc \
 --property schema.registry.url=http://localhost:8081 \
 --topic book
```

```json
{"id": 294, "title": "Effective Java", "release_year": 2018}
{"id": 354, "title": "Kafka Streams in Action", "release_year": 2018}
{"id": 782, "title": "Kafka: The Definitive Guide", "release_year": 2021}
{"id": 128, "title": "Designing Data-Intensive Applications", "release_year": 2017}
{"id": 780, "title": "The Pragmatic Programmer", "release_year": 2019}
```

```shell
kafka-avro-console-consumer --bootstrap-server localhost:9092 \
--property schema.registry.url=http://localhost:8081 \
--topic ratedBook
```

```shell
kafka-avro-console-producer --bootstrap-server localhost:9092 \
--property value.schema.file=src/main/avro/Rating.avsc \
--property schema.registry.url=http://localhost:8081 \
--topic rating
```

```json
{"id": 294, "rating": 8.2}
{"id": 294, "rating": 8.5}
{"id": 354, "rating": 9.9}
{"id": 354, "rating": 9.7}
{"id": 782, "rating": 7.8}
{"id": 782, "rating": 7.7}
{"id": 128, "rating": 8.7}
{"id": 128, "rating": 8.4}
{"id": 780, "rating": 2.1}
```