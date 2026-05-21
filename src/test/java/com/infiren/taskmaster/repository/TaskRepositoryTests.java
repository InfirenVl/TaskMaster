package com.infiren.taskmaster.repository;

import com.infiren.taskmaster.object.entity.TaskEntity;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.junit.jupiter.api.Test;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.postgresql.PostgreSQLContainer;
import java.time.LocalDateTime;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class TaskRepositoryTests {

    private static final Integer ID = 3;

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private TaskRepository repository;

    static PostgreSQLContainer postgres = new PostgreSQLContainer(
            "postgres:16-alpine"
    );

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @BeforeAll
    static void beforeAll() {
        postgres.start();
    }

    @AfterAll
    static void afterAll() {
        postgres.stop();
    }

    @Test
    public void findByAssignedUserIdAndStatus_shouldReturnMatchingTasks(){
        // Arrange
        final TaskEntity task1 = new TaskEntity();
                task1.setCreatorId(1);
                task1.setAssignedUserId(2);
                task1.setTitle("Title 1");
                task1.setDescription("Desc 1");
                task1.setPriority(TaskEntity.Priority.LOW);
                task1.setStatus(TaskEntity.Status.CREATED);
                task1.setCreatedDateTime(LocalDateTime.now().plusSeconds(1));
                task1.setCompletedDateTime(null);
                task1.setDeadlineDate(null);


        final TaskEntity task2 = new TaskEntity();
                task2.setCreatorId(1);
                task2.setAssignedUserId(3);
                task2.setTitle("Title 2");
                task2.setDescription("Desc 2");
                task2.setPriority(TaskEntity.Priority.LOW);
                task2.setStatus(TaskEntity.Status.CREATED);
                task2.setCreatedDateTime(LocalDateTime.now().plusSeconds(1));
                task2.setCompletedDateTime(null);
                task2.setDeadlineDate(null);

        final TaskEntity task3 = new TaskEntity();
                task3.setCreatorId(1);
                task3.setAssignedUserId(3);
                task3.setTitle("Title 3");
                task3.setDescription("Desc 3");
                task3.setPriority(TaskEntity.Priority.LOW);
                task3.setStatus(TaskEntity.Status.IN_PROGRESS);
                task3.setCreatedDateTime(LocalDateTime.now().plusSeconds(1));
                task3.setCompletedDateTime(null);
                task3.setDeadlineDate(null);


        entityManager.merge(task1);
        entityManager.merge(task2);
        entityManager.merge(task3);
        entityManager.flush();


        // Act
        final List<TaskEntity> actualList = repository.findByAssignedUserIdAndStatus(ID, TaskEntity.Status.CREATED);

        // Assert
        assertNotNull(actualList);
        assertEquals(1, actualList.size());
    }

    @Test
    public void findByAssignedUserIdAndStatus_shouldReturnEmptyList_whenNoMatches(){
        // Arrange
        final TaskEntity task1 = new TaskEntity();
                task1.setCreatorId(1);
                task1.setAssignedUserId(2);
                task1.setTitle("Title 1");
                task1.setDescription("Desc 1");
                task1.setPriority(TaskEntity.Priority.LOW);
                task1.setStatus(TaskEntity.Status.CREATED);
                task1.setCreatedDateTime(LocalDateTime.now().plusSeconds(1));
                task1.setCompletedDateTime(null);
                task1.setDeadlineDate(null);

        final TaskEntity task2 = new TaskEntity();
                task2.setCreatorId(1);
                task2.setAssignedUserId(3);
                task2.setTitle("Title 2");
                task2.setDescription("Desc 2");
                task2.setPriority(TaskEntity.Priority.LOW);
                task2.setStatus(TaskEntity.Status.CREATED);
                task2.setCreatedDateTime(LocalDateTime.now().plusSeconds(1));
                task2.setCompletedDateTime(null);
                task2.setDeadlineDate(null);

        final TaskEntity task3 = new TaskEntity();
                task3.setCreatorId(1);
                task3.setAssignedUserId(3);
                task3.setTitle("Title 3");
                task3.setDescription("Desc 3");
                task3.setPriority(TaskEntity.Priority.LOW);
                task3.setStatus(TaskEntity.Status.IN_PROGRESS);
                task3.setCreatedDateTime(LocalDateTime.now().plusSeconds(1));
                task3.setCompletedDateTime(null);
                task3.setDeadlineDate(null);


        entityManager.merge(task1);
        entityManager.merge(task2);
        entityManager.merge(task3);
        entityManager.flush();


        // Act
        final List<TaskEntity> actualList = repository.findByAssignedUserIdAndStatus(ID, TaskEntity.Status.CREATED);

        // Assert
        assertTrue(actualList.isEmpty());
    }
}
