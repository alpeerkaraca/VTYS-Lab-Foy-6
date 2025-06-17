package com.foy6.foy6.Controllers;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable; // Import @PathVariable
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.foy6.foy6.Models.Student;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;

@RestController
@RequestMapping("/nosql-lab")
public class StudentController {

    private final RedisTemplate<String, Student> redisTemplate;
    private final MongoTemplate mongoTemplate;
    private final HazelcastInstance hazelcastInstance;
    private final IMap<String, Student> studentHazelcastMap;

    public StudentController(RedisTemplate<String, Student> redisTemplate,
            MongoTemplate mongoTemplate,
            HazelcastInstance hazelcastInstance) {
        this.redisTemplate = redisTemplate;
        this.mongoTemplate = mongoTemplate;
        this.hazelcastInstance = hazelcastInstance;
        this.studentHazelcastMap = this.hazelcastInstance.getMap("students");
    }

    @GetMapping("/rd/{studentNo}")
    public ResponseEntity<Student> getStudentFromRedis(@PathVariable String studentNo) { // Add @PathVariable
        Student student = redisTemplate.opsForValue().get(studentNo);
        if (student != null) {
            return ResponseEntity.ok(student);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/mon/{studentNo}")
    public ResponseEntity<Student> getStudentFromMongo(@PathVariable String studentNo) { // Add @PathVariable
        Student student = mongoTemplate.findById(studentNo, Student.class);
        if (student != null) {
            return ResponseEntity.ok(student);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/hz/{studentNo}")
    public ResponseEntity<Student> getStudentFromHazelcast(@PathVariable String studentNo) { // Add @PathVariable
        Student student = studentHazelcastMap.get(studentNo);
        if (student != null) {
            return ResponseEntity.ok(student);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}