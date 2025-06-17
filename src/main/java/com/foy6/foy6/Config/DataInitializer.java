package com.foy6.foy6.Config;

import com.foy6.foy6.Models.Student;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
@Component
public class DataInitializer implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(DataInitializer.class);
    private static final int STUDENT_COUNT = 10000;

    private final RedisTemplate<String, Student> redisTemplate;
    private final MongoTemplate mongoTemplate;
    private final HazelcastInstance hazelcastInstance;

    public DataInitializer(RedisTemplate<String, Student> redisTemplate, MongoTemplate mongoTemplate,
            HazelcastInstance hazelcastInstance) {
        this.redisTemplate = redisTemplate;
        this.mongoTemplate = mongoTemplate;
        this.hazelcastInstance = hazelcastInstance;
    }

    @Override
    public void run(String... args) throws Exception {
        logger.info("Creating {} students...", STUDENT_COUNT);

        List<Student> students = generateStudents();
        loadToRedis(students);
        loadToMongo(students);
        loadToHazelcast(students);
        logger.info("Data initialization completed.");
    }

    private List<Student> generateStudents() {
        logger.info("Generating {} student records...", STUDENT_COUNT);
        return IntStream.range(0, STUDENT_COUNT)
                .mapToObj(i -> {
                    String id = "26" + String.format("%06d", i);
                    return new Student(id, "Student Name " + i, "Computer Science");
                })
                .collect(Collectors.toCollection(ArrayList::new));
    }

    private void loadToRedis(List<Student> students) {
        logger.info("Purging Redis...");
        redisTemplate.getConnectionFactory().getConnection().flushDb();
        Map<String, Student> studentMap = students.stream()
                .collect(Collectors.toMap(Student::getStudentNo, student -> student));
        redisTemplate.opsForValue().multiSet(studentMap);
        logger.info("Loaded {} students into Redis.", students.size());
    }

    private void loadToMongo(List<Student> students) {
        logger.info("Purging MongoDB...");
        mongoTemplate.dropCollection(Student.class);
        mongoTemplate.insertAll(students);
        logger.info("Loaded {} students into MongoDB.", students.size());
    }

    private void loadToHazelcast(List<Student> students) {
        logger.info("Purging Hazelcast...");
        IMap<String, Student> studentMap = hazelcastInstance.getMap("students");
        studentMap.clear();

        Map<String, Student> studentHazelcastMap = students.stream()
                .collect(Collectors.toMap(Student::getStudentNo, student -> student));
        studentMap.putAll(studentHazelcastMap);
        logger.info("Loaded {} students into Hazelcast.", students.size());

    }

}
